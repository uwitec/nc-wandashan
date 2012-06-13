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
 * 运费核算  手工  单价*主数量    或    单价*辅数量    主数量是否为吨  辅数量是否为箱
 *          吨公里 单价*公里数*吨数
 *          箱粉   单价*箱数
 *          
 * @author Administrator
 *
 */
public abstract class AbstractTranPriceCol {
	protected SendBillVO billvo = null;
	protected SendBillBodyVO m_sendbody = null;
	private UFBoolean issale = UFBoolean.FALSE;//true:销售出库   false:转分仓出库
	
    private UFDate m_logDate = null;//当前登录日期  运费核算时需要
    
    protected TranspriceBVO priceBvo=null;//本次取出的运价表
    
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
	 * @作者：lyf 
	 * @说明：完达山物流项目 
	 *  运费核算
	 * @时间：2011-5-20上午10:29:00
	 * @throws BusinessException
	 */
	public TranspriceBVO getTranspriceBvo() throws BusinessException{
		setTranspriceVO();
		return priceBvo;
	}
	
	/**
	 * 
	 * @作者：lyf
	 * @说明：完达山物流项目 
	 * 运费核算：具体算法，具体实现
	 * @时间：2011-5-20上午10:30:09
	 * @throws BusinessException
	 */
	public abstract void setTranspriceVO() throws BusinessException;
	/**
	 * 
	 * @作者：lyf
	 * @说明：完达山物流项目 
	 * 从里程表上 获取两个地区的公里数 
	 * @时间：2011-5-18下午07:58:28
	 * @return
	 * @throws BusinessException
	 */
	public UFDouble getGls() throws BusinessException{
		String sql = " select mileage from wds_transmil where isnull(dr,0)=0 and pk_delocation='"+m_sendbody.getCsendareaid()+"' and pk_relocation='"+m_sendbody.getCreceiverealid()+"'";
		Object o =getDao().executeQuery(sql, WdsPubResulSetProcesser.COLUMNPROCESSOR);
		if(o == null || "".equals(o)){
			throw new BusinessException("未定义运输里程表,行号:"+m_sendbody.getCrowno());
		}
		return PuPubVO.getUFDouble_NullAsZero(o);
	}
	
	
	/**
	 * 
	 * @作者：lyf 
	 * @说明：完达山物流项目:
	 * 得到同一张来源单据的 辅数量汇总值 
	 * @时间：2011-5-20上午10:17:56
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
	 * 运价表查询时   价格区间过滤条件  吨公里运价表是日期段  箱分运价表是 箱数段
	 * @return
	 */
	public abstract String getPricePeriodWhereSql () throws BusinessException;
	
	
	/**
	 * 
	 * @作者：lyf
	 * @说明：完达山物流项目 
	 *  获取父级地区分类id
	 * @时间：2011-5-19下午07:15:32
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
				if(areaclname.contains("省")){
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
	 * @作者：lyf
	 * @说明：完达山物流项目 
	 * @时间：2011-5-18下午08:56:27
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
	 * @作者：lyf
	 * @说明：完达山物流项目 
	 * @时间：2011-5-18下午08:56:27
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
	 * @作者：lyf 
	 * @说明：完达山物流项目 
	 * @时间：2011-5-19下午07:48:15
	 * @param areaclcode
	 * @return
	 * @throws BusinessException
	 */
	protected String getAreaIdByCode(String areaclcode)throws BusinessException{
		String sql = "select  pk_areacl from  bd_areacl where areaclcode = '"+areaclcode+"'";
		return PuPubVO.getString_TrimZeroLenAsNull(getDao().executeQuery(sql, WdsPubResulSetProcesser.COLUMNPROCESSOR));

	}
	

}
