package nc.ui.wds.ic.other.out;

import java.awt.Container;

import nc.bs.logging.Logger;
import nc.ui.pub.ClientEnvironment;
import nc.ui.wl.pub.LoginInforHelper;
import nc.ui.wl.pub.WdsBillSourceDLG;
import nc.vo.pub.BusinessException;
import nc.vo.wl.pub.WdsWlPubTool;

/**
 * 
 * @author Administrator
 *�������� ���� �ɹ�ȡ����WDSC��
 */
public class RefWDSCBillSourceDlg  extends WdsBillSourceDLG {

	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private boolean isStock = false; //�Ƿ����ܲ� true=�� false=��
	
	private String m_logUser = null;
	
	private String pk_stock = null; // ��ǰ��¼�߶�Ӧ�Ĳֿ�����
	
	private int iType = -1;
	
	private String[] inv_Pks = null;// ���ݵ�ǰ��¼�߲�ѯ�����ֿ����ֿ����洢�Ĳ�Ʒ
	
	private LoginInforHelper helper = null;
	
	public LoginInforHelper getLoginInforHelper(){
		if(helper == null){
			helper = new LoginInforHelper();
		}
		return helper;
	}
	public RefWDSCBillSourceDlg(String pkField, String pkCorp, String operator,
			String funNode, String queryWhere, String billType,
			String businessType, String templateId, String currentBillType,
			Container parent) {
		super(pkField, pkCorp, operator, funNode, queryWhere, billType, businessType,
				templateId, currentBillType, parent);
		init();
	}
	public RefWDSCBillSourceDlg(String pkField, String pkCorp, String operator,
			String funNode, String queryWhere, String billType,
			String businessType, String templateId, String currentBillType,
			String nodeKey, Object userObj, Container parent) {
		super(pkField, pkCorp, operator, funNode, queryWhere, billType, businessType,
				templateId, currentBillType, nodeKey, userObj, parent);
		init();
	}
	public void init(){
		try{
			m_logUser = ClientEnvironment.getInstance().getUser().getPrimaryKey();
			pk_stock = getLoginInforHelper().getWhidByUser(m_logUser); // ��ǰ��¼�߶�Ӧ�Ĳֿ�����
			if(pk_stock== null || "".equalsIgnoreCase(pk_stock)){
				throw new BusinessException("��ǰ��¼��Աû�а󶨲ֿ�");
			}
			iType = getLoginInforHelper().getITypeByUser(m_logUser);//��Ա����
			inv_Pks = getLoginInforHelper().getInvBasDocIDsByUserID(m_logUser);
			if(inv_Pks ==null || inv_Pks.length==0){
				throw new BusinessException("��ǰ��¼��Ա��λ��û�а󶨴��");
			}
			isStock = WdsWlPubTool.isZc(getLoginInforHelper().getCwhid(m_logUser));//�Ƿ����ܲ�
		}catch(Exception e){
			Logger.error(e);
		}
	}
	@Override
	public String getTitle() {
		// TODO Auto-generated method stub
		return "���ղɹ�ȡ��";
	}
	@Override
	public String getHeadCondition() {
		StringBuffer hsql = new StringBuffer();
		//�����ս���vo���pk_corp
		hsql.append("wds_cgqy_h.pk_corp = '"+getPkCorp()+"' and");

		hsql.append(" isnull(wds_cgqy_h.dr,0)=0 and wds_cgqy_h.vbillstatus =1 ");
		if(!isStock){
			//wds_cgqy_h.reserve3�ǲֿ�
//			hsql.append("and wds_cgqy_h.pk_outwhouse='"+pk_stock+"'");//�ֲ�ֻ�ܿ����Լ��ģ��ֿܲ��Կ����ܲ�+�ֲֵ�
			hsql.append("and wds_cgqy_h.reserve3='"+pk_stock+"'");//�ֲ�ֻ�ܿ����Լ��ģ��ֿܲ��Կ����ܲ�+�ֲֵ�
		}
		hsql.append("and wds_cgqy_h.pk_cgqy_h in");//ֻ�ܿ���������ǰ��¼�˰󶨻�λ�´���ĵ���
	    //if(inv_Pks !=null && inv_Pks.length>0){
			hsql.append("(");
			hsql.append("select distinct pk_cgqy_h from wds_cgqy_b where isnull(wds_cgqy_b.dr,0)=0");
			hsql.append(" and coalesce(nplannum,0)-coalesce(noutnum,0)>0");//��������-��������>0
			String sub = getInvSub(inv_Pks);
			hsql.append(" and pk_invmandoc in");
			
		
		return hsql.toString();
	}
	
	
	@Override
	public String getBodyCondition() {	
		String sub = getInvSub(inv_Pks);
		return " coalesce(wds_cgqy_b.nplannum,0)-coalesce(wds_cgqy_b.noutnum,0)>0"+//��������-��������>0
		" and wds_cgqy_b.pk_invmandoc in";
	}
	
	private String getInvSub(String [] inv_Pks){
		if(inv_Pks == null ){
			return "('')";
		}
		StringBuffer bur = new StringBuffer();
		bur.append("( ");
		for(int i=0;i<inv_Pks.length;i++){
			String pk_invmandoc = inv_Pks[i]==null?" ":inv_Pks[i];
			bur.append("'"+pk_invmandoc+"'");
			if(i<inv_Pks.length-1){
				bur.append(",");
			}
		}
		bur.append(" )");
		return bur.toString();
	}
	@Override
	protected boolean isHeadCanMultiSelect() {
		return false;
	}
	@Override
	protected boolean isBodyCanSelected() {
		return true;
	}	
	@Override
	protected Object getUseObjOnRef() throws Exception {		
		return inv_Pks;
	}
	@Override
	public boolean isSelfLoadBody() {		
		return true;
	}
	@Override
	protected boolean isSelfLoadHead() {
		
		return true;
	}
	@Override
	public boolean getIsBusinessType() {
		return false;
	}


}
