package nc.ui.wds.ic.other.in;

import java.awt.Container;

import nc.bs.logging.Logger;
import nc.itf.scm.cenpur.service.TempTableUtil;
import nc.ui.pub.ClientEnvironment;
import nc.ui.wl.pub.LoginInforHelper;
import nc.ui.wl.pub.WdsBillSourceDLG;
import nc.vo.pub.BusinessException;
import nc.vo.wl.pub.WdsWlPubTool;

public class RefBillSourceDlg extends WdsBillSourceDLG{
	

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
	private TempTableUtil ttutil = null;
	private TempTableUtil getTempTableUtil(){
		if(ttutil == null)
			ttutil = new TempTableUtil();
		return ttutil;
	}		

	public RefBillSourceDlg(String pkField, String pkCorp, String operator,
			String funNode, String queryWhere, String billType,
			String businessType, String templateId, String currentBillType,
			Container parent) {
		super(pkField, pkCorp, operator, funNode, queryWhere, billType, businessType,
				templateId, currentBillType, parent);
		init();
	}
	public RefBillSourceDlg(String pkField, String pkCorp, String operator,
			String funNode, String queryWhere, String billType,
			String businessType, String templateId, String currentBillType,
			String nodeKey, Object userObj, Container parent) {
		super(pkField, pkCorp, operator, funNode, queryWhere, billType, businessType,
				templateId, currentBillType, nodeKey, userObj, parent);
		init();
	}
	public void init(){try{
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
	}}
	
	@Override
	public String getTitle() {
		// TODO Auto-generated method stub
		return "�����������ⵥ";
	}
	@Override
	public String getHeadCondition() {
		String  pk_corp = ClientEnvironment.getInstance().getCorporation().getPrimaryKey();		
		StringBuffer hsql = new StringBuffer();
		hsql.append(" isnull(head.dr,0)=0 and head.pk_corp ='"+pk_corp+"' and head.cbilltypecode = '4I' ");//and head.fbillflag=3 //��ѯ ��Ӧ���������� ----���빫˾���ڵ�ǰ��˾����������Ϊ4Y
		hsql.append("and head.cotherwhid='"+pk_stock+"'");//
		hsql.append(" and head.cgeneralhid not in(select distinct gylbillhid from tb_general_b where gylbillhid is not null and gylbillbid is not null and isnull(dr,0)=0)");
		hsql.append("and head.cgeneralhid in");//ֻ�ܿ���������ǰ��¼�˰󶨻�λ�´���ĵ���
		//if(inv_Pks !=null && inv_Pks.length>0){
			hsql.append("(");
			hsql.append("select distinct cgeneralhid from ic_general_b where isnull(ic_general_b.dr,0)=0");
			//hsql.append(" and coalesce(nshouldoutnum,0)-coalesce(nacceptnum,0)>0");//Ӧ������-ת������>0
		    //	String sub = getIvnSubSql(inv_Pks);
			hsql.append(" and cinventoryid in");
		//	hsql.append(")");
		//}else{
		//	hsql.append("('')");
		//}	
		return hsql.toString();
		//head.fbillflag=3 ǩ��״̬
		}
	/**
	 * 
	 * @���ߣ�lyf---������ʱ�����
	 * @˵�������ɽ������Ŀ 
	 * @ʱ�䣺2011-7-3����09:43:58
	 * @param inv_Pks
	 * @return
	 */
	private String getIvnSubSql(String[] inv_Pks){
		StringBuffer invSbuSql = new StringBuffer();
		invSbuSql.append("('aa'");
		if(inv_Pks != null && inv_Pks.length>0){
			for(int i=0 ;i<inv_Pks.length;i++){
				invSbuSql.append(",'"+inv_Pks[i]+"'");
			}
		}
		invSbuSql.append(")");
		return invSbuSql.toString();
	}
	@Override
	public String getBodyCondition() {
		String sub = getTempTableUtil().getSubSql(inv_Pks);
	return " and body.cgeneralbid not in(select distinct gylbillbid from tb_general_b where gylbillhid is not null and gylbillbid is not null and isnull(dr,0)=0) and body.cinventoryid in ";
	
	}
	@Override
	public boolean isSelfLoadHead(){
		return true;
	}	
	
	@Override
	public boolean isSelfLoadBody(){
		return true;
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
	public Object getUseObjOnRef() throws Exception{
		
		return inv_Pks;
	}

	
	@Override
	public boolean getIsBusinessType() {
		return false;
	}
}
