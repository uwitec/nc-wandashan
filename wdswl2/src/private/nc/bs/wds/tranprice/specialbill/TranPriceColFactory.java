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
 *  �˷Ѻ��� �����㷨��
 */
public class TranPriceColFactory {

	// ��Դ���ݱ�ͷid �˼۱��ӱ�vo
	private Map<String,SpecbusipriceVO>  tranprice = new HashMap<String,SpecbusipriceVO>();
	//��ǰ������
	private SendBillBodyVO curBody = null;
	//��ǰʹ�õ� ����ҵ���˼۱�vo
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
			throw new BusinessException("�������Ϊ��");
		SendBillBodyVO[] bodys = billvo.getBodyVos();
		if(bodys == null || bodys.length == 0){
			throw new BusinessException("����������Ϊ��");
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
					throw new BusinessException("δ�����˼ۣ��кţ�"+curBody.getCrowno());
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
	 * @���ߣ�lyf
	 * @˵�������ɽ������Ŀ
	 * ��������˼۱�VO
	 * @ʱ�䣺2011-5-21����03:28:31
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
		sqlb.append(" where isnull(dr,0)=0 and isnull(iseffect,'N')='Y'");//�Ƿ���Ч
		sqlb.append(" and priceunit="+(PuPubVO.getInteger_NullAs(curBody.getIcoltype(),0)-1));//�˷Ѽ���������ͬ:icoltype(�ֹ���Ԫ/��,Ԫ/����)
		sqlb.append(" and pk_wds_specbusi='"+curBody.getReserve1()+"'");
		lprice = (List<SpecbusipriceVO>)getDao().executeQuery(sqlb.toString(), new BeanListProcessor(SpecbusipriceVO.class));
		if(lprice == null || lprice.size() ==0){
			return null;
		}
		return lprice.get(0);
	 }
	
	/**
	 * �����˼���Ϣ  �����˼�ɨ����˼���Ϣ���뵽�˷ѵ���
	 * @throws BusinessException 
	 */
	 void appendPriceInfor() throws BusinessException{
		if(curBody == null || curTranpriceBvo == null)
			return;
		curBody.setCpricehid(curTranpriceBvo.getPk_wds_specbusi());
		curBody.setCpriceid(curTranpriceBvo.getPrimaryKey());//---�����˼۱�vo,Ϊ������VO
//		���۰��޶������޶�
//		ʵ�ʼ۸�=ԭʼ�۸�*��1+����������
		curBody.setNprice(curTranpriceBvo.getTransprice());
		curBody.setIpriceunit(curTranpriceBvo.getPriceunit());
	}
	 

	 /**
	  * 
	  * @throws BusinessException 
	 * @���ߣ�lyf
	  * @˵�������ɽ������Ŀ:
	  *  �����˷� 
	  * @ʱ�䣺2011-5-20����01:22:14
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
	 * �����˷���Ϣ
	 * @throws BusinessException
	 */
	protected void doSavePriceInfor() throws BusinessException{
//		У��  ����  ������  �˼۱�id  �ܽ��;
		curBody.validateOnColSave();
		getDao().updateVO(curBody, SendBillVO.priceInfor_fieldNames);
		String sql = "select ts from wds_transprice_b where pk_wds_transprice_b = '"+curBody.getPrimaryKey()+"'";
		String ts = PuPubVO.getString_TrimZeroLenAsNull(getDao().executeQuery(sql, WdsPubResulSetProcesser.COLUMNPROCESSOR));
		curBody.setTs(new UFDateTime(ts));
	}

}
