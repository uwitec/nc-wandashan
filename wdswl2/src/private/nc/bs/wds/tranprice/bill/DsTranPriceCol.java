package nc.bs.wds.tranprice.bill;

import java.util.List;

import nc.jdbc.framework.processor.BeanListProcessor;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDate;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.trade.pub.IBillStatus;
import nc.vo.wds.tranprice.bill.SendBillBodyVO;
import nc.vo.wds.tranprice.bill.SendBillVO;
import nc.vo.wds.tranprice.tonkilometre.TranspriceBVO;
import nc.vo.wl.pub.WdsWlPubConst;
import nc.vo.wl.pub.WdsWlPubTool;
/**
 * 吨公里运价计算
 * @author Administrator
 *
 */
public class DsTranPriceCol extends AbstractTranPriceCol {


	@Override
	public void setTranspriceVO() throws BusinessException {
	//  金额 = 单价*吨数*公里数       +  单价调整值*数量
		SendBillBodyVO body = getSendBody();
		if(body == null)
			throw new BusinessException("传入数据为空");
		priceBvo = getDsTranspriceVO();	
	}
	
	/**
	 * 
	 * @作者：lyf
	 * @说明：完达山物流项目 
	 *  根据 发货地区  收货地区 当前日期 获取吨公里运价表
	 * @时间：2011-5-18下午07:56:51
	 *  获取运输单价
	 * 考虑收货地区在运价表中的定义：按地区分类级次 递归扫描获取  运价定义  如不存在  考虑 东三省内 东三省外定义的运价
	 * 考虑收货地区是否东三省内 或外  
     * 如果是东三省内的考虑  里程范围定义
	 * @return
	 * @throws BusinessException
	 */
	public  TranspriceBVO getDsTranspriceVO()  throws BusinessException{
		SendBillBodyVO  body = getSendBody();
		List<TranspriceBVO> lprice = getDsPriceInfor(WdsWlPubConst.WDSI, body.getCsendareaid(),body.getCreceiverealid(),true);
		if(lprice == null || lprice.size() == 0){	
			throw new BusinessException("未定义运价，行号："+WdsWlPubTool.getString_NullAsTrimZeroLen(body.getCrowno()));
		}else
		return lprice.get(0);
	
	}
	/**
	 * 获取运价表    按地区分类 编码  由低级向上级 递归扫描直至 找到 运价定义 或地区树遍历结束
	 * @param pricetype  运价表单据类型 :吨公里运价表和箱数运价表适用同一个库表，但是单据类型不同
	 * @param whereSql  
	 * @param pk_deplace 发货地区
	 * @param reareaid  收货地区Id
	 * @param isDG 是否递归
	 * @return
	 * @throws BusinessException
	 */
	public List<TranspriceBVO> getDsPriceInfor(String pricetype,String pk_deplace,String reareaid,boolean isDG) throws BusinessException{
		List<TranspriceBVO> lprice = null;
		StringBuffer sqlb  = new StringBuffer();
		sqlb.append("select ");
		String[] names = new TranspriceBVO().getAttributeNames();
		for(String name:names){
			sqlb.append(" b."+name+",");
		}
		sqlb.append(" 'aaa'");
		sqlb.append("from wds_transprice_h h inner join wds_transprice_b  b on b.pk_wds_transprice_h = h.pk_wds_transprice_h ");
		sqlb.append(" where isnull(h.dr,0)=0 and isnull(b.dr,0)=0 and h.vbillstatus = "+IBillStatus.CHECKPASS);
		sqlb.append(" and h.pk_billtype = '"+pricetype+"'");//运价表单据类型 :吨公里运价表和箱数运价表适用同一个库表，但是单据类型不同
		sqlb.append(" and h.carriersid='"+billvo.getHeaderVO().getCarriersid()+"'");//承运商
		sqlb.append(" and h.reserve1='"+m_sendbody.getPk_destore()+"'");//发货仓库
		sqlb.append(" and (isnull(b.ifw,0) = "+SendBillVO.YYFW_ALL +" or b.ifw = ");//应运范围过滤
		if(isSale()){//是否销售
			sqlb.append(SendBillVO.YYFW_SALE+")");
		}else{
			sqlb.append(SendBillVO.YYFW_ZFC+")");
		}
		if(PuPubVO.getString_TrimZeroLenAsNull(getPricePeriodWhereSql())!=null)//日期区间过滤
			sqlb.append(" and "+getPricePeriodWhereSql());
		sqlb.append(" and b.pk_deplace='"+pk_deplace+"' and b.pk_replace='"+reareaid+"'");
		lprice = (List<TranspriceBVO>)getDao().executeQuery(sqlb.toString(), new BeanListProcessor(TranspriceBVO.class));
		//如果没有获取对应收货地区的定义，查询有没有上级的定义
		if((lprice == null || lprice.size() == 0)&& isDG){
			String  pk_fatherarea= getFatherId(reareaid);
			if(pk_fatherarea == null || "".equalsIgnoreCase(pk_fatherarea)){
				//如果没有上级分类，取东三省内或者东三省外定义
				String code = getAreaCode(reareaid);
				// 地区分类编码 黑龙江省='01' 吉林省='02' 辽宁省='03'
				if(code.startsWith("01") || code.startsWith("02")||code.startsWith("03")){
					pk_fatherarea = getAreaIdByCode("0001");
					if(pk_fatherarea == null || "".equalsIgnoreCase(pk_fatherarea)){
						throw new BusinessException("请以 0001 作为地区分类编码 定义东三省内地区分类");
					}
				}else{
					pk_fatherarea = getAreaIdByCode("0002");
					if(pk_fatherarea == null || "".equalsIgnoreCase(pk_fatherarea)){
						throw new BusinessException("请以 0002 作为地区分类编码 定义东三省外地区分类");
					}
				}
				lprice = getDsPriceInfor(pricetype,pk_deplace,pk_fatherarea,false);
			}else{
				//如果有上进地区分类，递归查询
				lprice = getDsPriceInfor(pricetype,pk_deplace,pk_fatherarea,true);
			}
		}
		return lprice;
	}

	@Override
	public String getPricePeriodWhereSql() throws BusinessException{
		// TODO Auto-generated method stub
		UFDate logdate = getM_logDate();
		if(logdate==null)
			logdate = new UFDate(System.currentTimeMillis());
		return " h.dstartdate <= '"+logdate.toString()+"' and h.denddate >= '"+logdate.toString()+"'";
	}


}
