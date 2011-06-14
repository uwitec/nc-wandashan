package nc.bs.wds.tranprice.bill;

import java.util.ArrayList;

import nc.bs.dao.BaseDAO;
import nc.bs.wl.pub.WdsPubResulSetProcesser;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.wds.tranprice.bill.SendBillBodyVO;
import nc.vo.wds.tranprice.bill.SendBillVO;
import nc.vo.wds.tranprice.tonkilometre.TranspriceBVO;

/**
 * �˷Ѻ���  �ֹ�  ����*������    ��    ����*������    �������Ƿ�Ϊ��  �������Ƿ�Ϊ��
 *          �ֹ��� ����*������*����
 *          ���   ����*����
 *          
 * @author Administrator
 *
 */
public abstract class AbstractTranPriceCol {
	protected SendBillVO billvo = null;
	protected SendBillBodyVO m_sendbody = null;
	private UFBoolean issale = UFBoolean.FALSE;//true:���۳���   false:ת�ֲֳ���
	
    private UFDate m_logDate = null;//��ǰ��¼����  �˷Ѻ���ʱ��Ҫ
    
    protected TranspriceBVO priceBvo=null;//����ȡ�����˼۱�
    
    private BaseDAO dao = null;
	protected BaseDAO getDao(){
		if(dao == null){
			dao = new BaseDAO();
		}
		return dao;
	}
	
	public AbstractTranPriceCol(SendBillBodyVO body,UFBoolean issale,UFDate logDate){
		super();
		setSendBody(body);
		setIsSale(issale);
		setM_logDate(logDate);
	}
	public AbstractTranPriceCol(){
		super();
	}
	
	
	public SendBillVO getBillvo() {
		return billvo;
	}

	public void setBillvo(SendBillVO billvo) {
		this.billvo = billvo;
	}

	public void setSendBody(SendBillBodyVO body){
		m_sendbody = body;
	}
	public SendBillBodyVO getSendBody(){
		return m_sendbody;
	}
	
	
	public UFDate getM_logDate() {
		return m_logDate;
	}
	public void setM_logDate(UFDate date) {
		m_logDate = date;
	}

	public boolean isSale(){
		return issale.booleanValue();
	}
	public void setIsSale(UFBoolean bsale){
		issale = bsale;
	}
	
	/**
	 * 
	 * @���ߣ�lyf 
	 * @˵�������ɽ������Ŀ 
	 *  �˷Ѻ���
	 * @ʱ�䣺2011-5-20����10:29:00
	 * @throws BusinessException
	 */
	public TranspriceBVO getTranspriceBvo() throws BusinessException{
		setTranspriceVO();
		return priceBvo;
	}
	
	/**
	 * 
	 * @���ߣ�lyf
	 * @˵�������ɽ������Ŀ 
	 * �˷Ѻ��㣺�����㷨������ʵ��
	 * @ʱ�䣺2011-5-20����10:30:09
	 * @throws BusinessException
	 */
	public abstract void setTranspriceVO() throws BusinessException;
	/**
	 * 
	 * @���ߣ�lyf
	 * @˵�������ɽ������Ŀ 
	 * ����̱��� ��ȡ���������Ĺ����� 
	 * @ʱ�䣺2011-5-18����07:58:28
	 * @return
	 * @throws BusinessException
	 */
	public UFDouble getGls() throws BusinessException{
		String sql = " select mileage from wds_transmil where isnull(dr,0)=0 and pk_delocation='"+m_sendbody.getCsendareaid()+"' and pk_relocation='"+m_sendbody.getCreceiverealid()+"'";
		Object o =getDao().executeQuery(sql, WdsPubResulSetProcesser.COLUMNPROCESSOR);
		if(o == null || "".equals(o)){
			throw new BusinessException("δ����������̱�,�к�:"+m_sendbody.getCrowno());
		}
		return PuPubVO.getUFDouble_NullAsZero(o);
	}
	
	
	/**
	 * 
	 * @���ߣ�lyf 
	 * @˵�������ɽ������Ŀ:
	 * �õ�ͬһ����Դ���ݵ� ����������ֵ 
	 * @ʱ�䣺2011-5-20����10:17:56
	 * @return
	 */
	public UFDouble getTotalXS(){
		UFDouble totalAssnum = new UFDouble(0);
		SendBillBodyVO[] bodys =billvo.getBodyVos();
		for(SendBillBodyVO body: bodys){
			if(body.getCsourcebillhid().equalsIgnoreCase(m_sendbody.getCsourcebillhid())){
				UFDouble nassnum = PuPubVO.getUFDouble_NullAsZero(body.getNassnum());
				totalAssnum = totalAssnum.add(nassnum);
			}
		}
		return totalAssnum;
	}
	
	/**
	 * �˼۱��ѯʱ   �۸������������  �ֹ����˼۱������ڶ�  ����˼۱��� ������
	 * @return
	 */
	public abstract String getPricePeriodWhereSql () throws BusinessException;
	
	
	/**
	 * 
	 * @���ߣ�lyf
	 * @˵�������ɽ������Ŀ 
	 *  ��ȡ������������id
	 * @ʱ�䣺2011-5-19����07:15:32
	 * @param reareaid
	 * @return
	 * @throws BusinessException
	 */
	protected String getFatherId(String reareaid)throws BusinessException{
		String sql = "select pk_fatherarea,areaclname from  bd_areacl where pk_areacl = '"+reareaid+"'";
		Object o = getDao().executeQuery(sql, WdsPubResulSetProcesser.COLUMNLISTROCESSOR);
		if( o != null){
			ArrayList<String> list = (ArrayList<String>) o;
			String areaclname =  PuPubVO.getString_TrimZeroLenAsNull(list.get(1));
			if(areaclname != null){
				if(areaclname.contains("ʡ")){
					return null;
				}else{
					return PuPubVO.getString_TrimZeroLenAsNull(list.get(0));
				}
			}
		}
		return null;
	}
	/**
	 * 
	 * @���ߣ�lyf
	 * @˵�������ɽ������Ŀ 
	 * @ʱ�䣺2011-5-18����08:56:27
	 * @param id
	 * @return
	 * @throws BusinessException
	 */
	protected String getAreaName(String id) throws BusinessException{
		String sql = "select areaclcode from  bd_areacl where pk_areacl = '"+id+"'";
		return PuPubVO.getString_TrimZeroLenAsNull(getDao().executeQuery(sql, WdsPubResulSetProcesser.COLUMNPROCESSOR));
	}
	/**
	 * 
	 * @���ߣ�lyf
	 * @˵�������ɽ������Ŀ 
	 * @ʱ�䣺2011-5-18����08:56:27
	 * @param id
	 * @return
	 * @throws BusinessException
	 */
	protected String getAreaCode(String id) throws BusinessException{
		String sql = "select areaclcode from  bd_areacl where pk_areacl = '"+id+"'";
		return PuPubVO.getString_TrimZeroLenAsNull(getDao().executeQuery(sql, WdsPubResulSetProcesser.COLUMNPROCESSOR));
	}
	/**
	 * 
	 * @���ߣ�lyf 
	 * @˵�������ɽ������Ŀ 
	 * @ʱ�䣺2011-5-19����07:48:15
	 * @param areaclcode
	 * @return
	 * @throws BusinessException
	 */
	protected String getAreaIdByCode(String areaclcode)throws BusinessException{
		String sql = "select  pk_areacl from  bd_areacl where areaclcode = '"+areaclcode+"'";
		return PuPubVO.getString_TrimZeroLenAsNull(getDao().executeQuery(sql, WdsPubResulSetProcesser.COLUMNPROCESSOR));

	}
	

}
