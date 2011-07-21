package nc.ui.zb.price.pub;

import java.util.HashMap;
import java.util.Map;

import nc.ui.pub.ClientEnvironment;
import nc.ui.trade.business.HYPubBO_Client;
import nc.uif.pub.exception.UifException;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.zb.parmset.ParamSetVO;
import nc.vo.zb.price.SubmitPriceVO;

public abstract class AbstractPriceDataBuffer {
	protected SubmitPriceVO[] m_prices = null;//������Ϣ
	protected String cbiddingid = null;//��ǰ���
	protected String cvendorid = null;//��ǰ��Ӧ��
	protected boolean bisinv = true;//�Ƿ���ϸ�б�   //��Ϳͻ����۶��岻�ٴ��ڴ����б� һֱ������ϸ�б�
	protected String pk_corp = null;
	protected ParamSetVO para = null;
	
//	����Ʒ�ֱ�׼���Ϣ
	protected Map<String, UFDouble> invPriceInfor = null;//�������id   ��׼�
//	��ʼ����Ʒ�ֵı�׼���Ϣ
	public void initInvPriceInfor(){
		if(m_prices == null || m_prices.length == 0)
			return;
		if(invPriceInfor == null)
			invPriceInfor = new HashMap<String, UFDouble>();
		for(SubmitPriceVO price:m_prices){
			invPriceInfor.put(price.getCinvbasid(), PuPubVO.getUFDouble_NullAsZero(price.getNmarkprice()));
		}
	}
	public Map<String, UFDouble> getInvPriceInfor(){
		if(invPriceInfor == null || invPriceInfor.size() == 0){
			initInvPriceInfor();
		}
		return invPriceInfor;
	}

	protected AbstractPricePubUI ui = null;
	public AbstractPriceDataBuffer(){
		super();
		init();
	}
	
	public ParamSetVO getPara(){
		return para;
	}
	public AbstractPriceDataBuffer(AbstractPricePubUI tp){
		super();
		ui = tp;
		init();
	}
	private void init(){
		pk_corp = ClientEnvironment.getInstance().getCorporation().getPrimaryKey();
		initPara();
	}
	private void initPara(){
		String whereSql = " isnull(dr,0) = 0 and pk_corp = '"+pk_corp+"'";
		SuperVO[] vos = null;
		try {
			vos = HYPubBO_Client.queryByCondition(ParamSetVO.class, whereSql);
		} catch (UifException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			para = null;
			return;
		}
		para = vos == null || vos.length ==0 || vos.length>1?null:(ParamSetVO)vos[0];
	}
	public void clear(){
		cbiddingid = null;
//		cvendorid = null;
		m_prices = null;		
		bisinv = true;
		if(invPriceInfor!=null)
		  invPriceInfor.clear();
	}

	public abstract String getCurrentCircalID();
	
	public SubmitPriceVO[] getM_prices() {
		return m_prices;
	}
	public void setM_prices(SubmitPriceVO[] prices) {
		this.m_prices = prices;
		if(m_prices == null || m_prices.length == 0)
			return;
		String coprator = ClientEnvironment.getInstance().getUser().getPrimaryKey();
		//		UFDate data = ClientEnvironment.getInstance().getDate();
		for(SubmitPriceVO price:m_prices){
			price.setCvendorid(getCvendorid());
			price.setCcircalnoid(getCurrentCircalID());
			price.setCoprator(coprator);
			price.setTmaketime(ClientEnvironment.getServerTime().toString());
		}
	}
	public String getCbiddingid() {
		return cbiddingid;
	}
	public void setCbiddingid(String cbiddingid) {
		this.cbiddingid = cbiddingid;
	}
	public String getCvendorid() {
		return cvendorid;
	}
	public void setCvendorid(String cvendorid) {
		this.cvendorid = cvendorid;
	}
	public boolean isBisinv() {
		return bisinv;
	}
//	public void setBisinv(boolean bisinv) {
//		this.bisinv = bisinv;
//	}
	public String getPk_corp() {
		return pk_corp;
	}	
}
