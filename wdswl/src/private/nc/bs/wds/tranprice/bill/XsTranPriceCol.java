package nc.bs.wds.tranprice.bill;

import java.util.List;

import nc.jdbc.framework.processor.BeanListProcessor;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.trade.pub.IBillStatus;
import nc.vo.wds.tranprice.bill.SendBillBodyVO;
import nc.vo.wds.tranprice.bill.SendBillVO;
import nc.vo.wds.tranprice.tonkilometre.TranspriceBVO;
import nc.vo.wl.pub.WdsWlPubConst;
import nc.vo.wl.pub.WdsWlPubTool;
/**
 * 箱粉运价计算
 * @author Administrator
 *
 */
public class XsTranPriceCol extends AbstractTranPriceCol {

	@Override
	public void setTranspriceVO() throws BusinessException {
        //  单价*箱数 +调整值*数量
		SendBillBodyVO body = getSendBody();
		if(body == null)
			throw new BusinessException("传入数据为空");
		priceBvo = getXsTranspriceVO();
//		UFDouble nprice = PuPubVO.getUFDouble_NullAsZero(getXsPrice());
//		UFDouble nnum = PuPubVO.getUFDouble_NullAsZero(getXS());
//		
//		
	}
	
	/**
	 * 
	 * @作者：lyf
	 * @说明：完达山物流项目 获取 箱数运价表
	 *  获取原则： 第一维度： 公里区间 第二维度：箱数
	 * @时间：2011-5-19下午08:56:16
	 * @return
	 */
	public TranspriceBVO getXsTranspriceVO()throws BusinessException{
		SendBillBodyVO  body = getSendBody();
		List<TranspriceBVO> lprice = getXsPriceInfor(WdsWlPubConst.WDSJ, body.getCsendareaid(),body.getCreceiverealid());
		if(lprice == null || lprice.size() == 0){	
			throw new BusinessException("未定义运价，行号："+WdsWlPubTool.getString_NullAsTrimZeroLen(body.getCrowno()));
		}else		
		return lprice.get(0);

	}
	/**
	 * 
	 * @作者：lyf
	 * @说明：完达山物流项目：获取箱数运价表
	 * @时间：2011-5-20上午10:11:31
	 * @param pricetype
	 * @param pk_deplace
	 * @param reareaid
	 * @return
	 * @throws BusinessException
	 */
	public List<TranspriceBVO> getXsPriceInfor(String pricetype,String pk_deplace,String reareaid) throws BusinessException{
		List<TranspriceBVO> lprice = null;
		StringBuffer sqlb  = new StringBuffer();
		sqlb.append("select ");
		String[] names = new TranspriceBVO().getAttributeNames();
		for(String name:names){
			sqlb.append(" b."+name+",");
		}
		sqlb.append(" 'aaa'");
		sqlb.append("from wds_transprice_h h inner join wds_transprice_b  b on b.pk_wds_transprice_h = h.pk_wds_transprice_h ");
		sqlb.append(" where isnull(h.dr,0)=0 and isnull(b.dr,0)=0 and vbillstatus = "+IBillStatus.CHECKPASS);
		sqlb.append(" and h.pk_billtype = '"+pricetype+"'");
		sqlb.append(" and h.carriersid='"+billvo.getHeaderVO().getCarriersid()+"'");
		sqlb.append(" and h.reserve1='"+m_sendbody.getPk_destore()+"'");//发货仓库
		sqlb.append(" and (isnull(b.ifw,0)= "+SendBillVO.YYFW_ALL +" or b.ifw = ");//应运范围过滤
		if(isSale()){//是否销售
			sqlb.append(SendBillVO.YYFW_SALE+")");
		}else{
			sqlb.append(SendBillVO.YYFW_ZFC+")");
		}
		if(PuPubVO.getString_TrimZeroLenAsNull(getPricePeriodWhereSql())!=null)//箱数区间过滤
			sqlb.append(" and "+getPricePeriodWhereSql());
		lprice = (List<TranspriceBVO>)getDao().executeQuery(sqlb.toString(), new BeanListProcessor(TranspriceBVO.class));
		return lprice;
	}
	
	@Override
	public String getPricePeriodWhereSql()  throws BusinessException{
		UFDouble nxs = getTotalXS();
		UFDouble gls =getGls();
		return " h.nmincase <= "+nxs.doubleValue()+" and h.nmaxcase >= "+nxs.doubleValue()+
		"  and b.nmindistance <= "+gls.doubleValue()+" and b.nmaxdistance >= "+gls.doubleValue();

	}
	
}
