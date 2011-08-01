package nc.ui.wds.ic.other.in;
import java.awt.Container;

import nc.bs.logging.Logger;
import nc.itf.scm.cenpur.service.TempTableUtil;
import nc.ui.pub.ClientEnvironment;
import nc.ui.wl.pub.LoginInforHelper;
import nc.ui.wl.pub.WdsBillSourceDLG;
import nc.vo.pub.BusinessException;
import nc.vo.wl.pub.WdsWlPubConst;
import nc.vo.wl.pub.WdsWlPubTool;
/**
 * @author mlr
 * ������� ���� �������� ����
 */
public class RefBillSourceDlgWDS7 extends WdsBillSourceDLG{
	private static final long serialVersionUID = 4237270665256372871L;
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
	public RefBillSourceDlgWDS7(String pkField, String pkCorp, String operator,
			String funNode, String queryWhere, String billType,
			String businessType, String templateId, String currentBillType,
			Container parent) {
		super(pkField, pkCorp, operator, funNode, queryWhere, billType, businessType,
				templateId, currentBillType, parent);
		init();
	}
	public RefBillSourceDlgWDS7(String pkField, String pkCorp, String operator,
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
		return "�����������ⵥ";
	}
	@Override
	public String getHeadCondition() {
		String  pk_corp = ClientEnvironment.getInstance().getCorporation().getPrimaryKey();		
		StringBuffer hsql = new StringBuffer();
		hsql.append(" isnull(tb_outgeneral_h.dr,0)=0 and tb_outgeneral_h.pk_corp ='"+pk_corp+"' and tb_outgeneral_h.vbilltype = '"+WdsWlPubConst.BILLTYPE_OTHER_OUT+"' ");//and head.fbillflag=3 //��ѯ ��Ӧ���������� ----���빫˾���ڵ�ǰ��˾����������Ϊ4Y
		hsql.append(" and tb_outgeneral_h.srl_pkr='"+pk_stock+"'");//
		hsql.append("");//����ͨ���ĳ��ⵥ
		hsql.append(" and tb_outgeneral_h.general_pk not in(select distinct csourcebillhid from tb_general_b where csourcebillhid is not null and csourcebillhid is not null and isnull(dr,0)=0)");
		hsql.append(" and tb_outgeneral_h.general_pk in");//ֻ�ܿ���������ǰ��¼�˰󶨻�λ�´���ĵ���
		//if(inv_Pks !=null && inv_Pks.length>0){
			hsql.append("(");
			hsql.append("select distinct general_pk from tb_outgeneral_b where isnull(tb_outgeneral_b.dr,0)=0");
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
		
	    return "  tb_outgeneral_b.general_b_pk  not in(select distinct csourcebillbid from tb_general_b where csourcebillbid is not null and csourcebillbid is not null and isnull(dr,0)=0) and tb_outgeneral_b.cinventoryid in ";	
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
