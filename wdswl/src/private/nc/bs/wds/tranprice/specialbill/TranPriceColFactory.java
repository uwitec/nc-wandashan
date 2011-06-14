package nc.bs.wds.tranprice.specialbill;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.wl.pub.WdsPubResulSetProcesser;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.wds.tranprice.bill.SendBillBodyVO;
import nc.vo.wds.tranprice.bill.SendBillVO;
import nc.vo.wds.tranprice.specbusiprice.SpecbusipriceVO;

/**
 * 
 * @author Administrator
 *  运费核算 核心算法类
 */
public class TranPriceColFactory {

	// 来源单据表头id 运价表子表vo
	private Map<String,SpecbusipriceVO>  tranprice = new HashMap<String,SpecbusipriceVO>();
	//当前操作行
	private SendBillBodyVO curBody = null;
	//当前使用的 特殊业务运价表vo
	private SpecbusipriceVO curTranpriceBvo = null;
	
	 private BaseDAO dao = null;
		protected BaseDAO getDao(){
			if(dao == null){
				dao = new BaseDAO();
			}
			return dao;
		}
	public TranPriceColFactory(SendBillVO billvo){
		super();
		this.billvo = billvo;
	}

	private SendBillVO billvo = null;
	public void setBillVO(SendBillVO bill){
		billvo = bill;
	}
	public SendBillVO getBillVO(){
		return billvo;
	}
	public SendBillVO col() throws BusinessException{
		if(billvo==null)
			throw new BusinessException("传入参数为空");
		SendBillBodyVO[] bodys = billvo.getBodyVos();
		if(bodys == null || bodys.length == 0){
			throw new BusinessException("传入表体参数为空");
		}
		int icoltype = SendBillVO.HAND_COLTYPE;
		for(SendBillBodyVO body:bodys){
			curBody = body;
			icoltype = PuPubVO.getInteger_NullAs(body.getIcoltype(), SendBillVO.HAND_COLTYPE);
			if(icoltype ==SendBillVO.HAND_COLTYPE )
				continue;
			if(tranprice.containsKey(body.getCsourcebillhid())){
				curTranpriceBvo = tranprice.get(body.getCsourcebillhid());
			}else{
				curTranpriceBvo= getTranspriceBvo();
				if(curTranpriceBvo == null){
					throw new BusinessException("未定义运价，行号："+curBody.getCrowno());
				}
				tranprice.put(body.getCsourcebillhid(), curTranpriceBvo);
			}
			
		
			appendPriceInfor();
			setNcolmny();
			doSavePriceInfor();
		}
		return billvo;
	}
	/**
	 * 
	 * @作者：lyf
	 * @说明：完达山物流项目
	 * 获得特殊运价表VO
	 * @时间：2011-5-21下午03:28:31
	 * @return
	 * @throws DAOException 
	 */
	@SuppressWarnings("unchecked")
	SpecbusipriceVO getTranspriceBvo() throws DAOException{
		List<SpecbusipriceVO> lprice = null;
		StringBuffer sqlb  = new StringBuffer();
		String[] names = new SpecbusipriceVO().getAttributeNames();
		sqlb.append("select ");
		if(names != null && names.length>0 ){
			sqlb.append("'aa'");
			for(String name:names){
				sqlb.append(","+name+"");
			}
		}else{
			sqlb.append("'aa'");
		}
		sqlb.append(" from wds_specbusiprice ");
		sqlb.append(" where isnull(dr,0)=0 and isnull(iseffect,'N')='Y'");//是否生效
		sqlb.append(" and priceunit="+(PuPubVO.getInteger_NullAs(curBody.getIcoltype(),0)-1));//运费计算类型相同:icoltype(手工，元/吨,元/箱数)
		sqlb.append(" and pk_wds_specbusi='"+curBody.getReserve1()+"'");
		lprice = (List<SpecbusipriceVO>)getDao().executeQuery(sqlb.toString(), new BeanListProcessor(SpecbusipriceVO.class));
		if(lprice == null || lprice.size() ==0){
			return null;
		}
		return lprice.get(0);
	 }
	
	/**
	 * 补充运价信息  用于运价扫描后将运价信息补齐到运费单上
	 * @throws BusinessException 
	 */
	 void appendPriceInfor() throws BusinessException{
		if(curBody == null || curTranpriceBvo == null)
			return;
		curBody.setCpricehid(curTranpriceBvo.getPk_wds_specbusi());
		curBody.setCpriceid(curTranpriceBvo.getPrimaryKey());//---特殊运价表vo,为单表体VO
//		单价按修订比例修订
//		实际价格=原始价格*（1+修正比例）
		curBody.setNprice(curTranpriceBvo.getTransprice());
		curBody.setIpriceunit(curTranpriceBvo.getPriceunit());
	}
	 

	 /**
	  * 
	  * @throws BusinessException 
	 * @作者：lyf
	  * @说明：完达山物流项目:
	  *  设置运费 
	  * @时间：2011-5-20下午01:22:14
	  */
	 void setNcolmny() throws BusinessException{
		 int icoltype = SendBillVO.HAND_COLTYPE;
		 icoltype =curBody.getIcoltype();
		 UFDouble nprice = curBody.getNprice();
		 UFDouble nnum = null;
		 if(icoltype == SendBillVO.DS_COLTYPE){
			 nnum = PuPubVO.getUFDouble_NullAsZero(curBody.getNnum());
			 curBody.setNcolmny(nprice.multiply(nnum,8));
		 }else if(icoltype == SendBillVO.XS_COLTYPE){
			 nnum = PuPubVO.getUFDouble_NullAsZero(curBody.getNassnum());
			 curBody.setNcolmny(nprice.multiply(nnum,8));
			 curBody.setNmny(nprice.multiply(nnum,8));
		 }
	 }
	/**
	 * 保存运费信息
	 * @throws BusinessException
	 */
	protected void doSavePriceInfor() throws BusinessException{
//		校验  单价  计算金额  运价表id  总金额;
		curBody.validateOnColSave();
		getDao().updateVO(curBody, SendBillVO.priceInfor_fieldNames);
		String sql = "select ts from wds_transprice_b where pk_wds_transprice_b = '"+curBody.getPrimaryKey()+"'";
		String ts = PuPubVO.getString_TrimZeroLenAsNull(getDao().executeQuery(sql, WdsPubResulSetProcesser.COLUMNPROCESSOR));
		curBody.setTs(new UFDateTime(ts));
	}

}
