package nc.ui.wds.ic.so.out;

import java.awt.Container;

import nc.bs.logging.Logger;
import nc.itf.scm.cenpur.service.TempTableUtil;
import nc.ui.pub.ClientEnvironment;
import nc.ui.wl.pub.LoginInforHelper;
import nc.ui.wl.pub.WdsBillSourceDLG;
import nc.vo.pub.BusinessException;
import nc.vo.wl.pub.WdsWlPubTool;

/**
 * 
 * @author Administrator
 * ���պ������۶���
 */
public class Ref30BillSourceDlg extends WdsBillSourceDLG{
	
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

	
	public Ref30BillSourceDlg(String pkField, String pkCorp, String operator,
			String funNode, String queryWhere, String billType,
			String businessType, String templateId, String currentBillType,
			Container parent) {
		super(pkField, pkCorp, operator, funNode, queryWhere, billType, businessType,
				templateId, currentBillType, parent);
		init();
	}
	public Ref30BillSourceDlg(String pkField, String pkCorp, String operator,
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
	protected boolean isHeadCanMultiSelect() {
		return false;
	}
	@Override
	protected boolean isBodyCanSelected() {
		return true;
	}
	
	public String getHeadCondition() {
		StringBuffer hsql = new StringBuffer();
		//�����ս���vo���pk_corp
		hsql.append(" so_sale.pk_corp = '"+getPkCorp()+"' and");

		hsql.append(" isnull(so_sale.dr,0)=0 and so_sale.fstatus = 2 ");
		hsql.append(" and isnull(so_sale.bretinvflag,'N')='N'");
		if(!isStock){
			hsql.append("and so_sale.cwarehouseid='"+pk_stock+"'");//�ֲ�ֻ�ܿ����Լ��ģ��ֿܲ��Կ����ܲ�+�ֲֵ�
		}
		hsql.append("and so_sale.csaleid in");//ֻ�ܿ���������ǰ��¼�˰󶨻�λ�´���ĵ���
//		if(inv_Pks !=null && inv_Pks.length>0){
			hsql.append("(");
			hsql.append("select distinct csaleid from so_saleorder_b where isnull(so_saleorder_b.dr,0)=0 ");
			hsql.append(" and coalesce(nnumber,0)-coalesce(ntaldcnum,0)<0");//��������->//����ϵͳ���۶���  �Ѳ���۱�����(ntaldcnum) ��Ϊ  �ۼƷ�������
//			String sub = getTempTableUtil().getSubSql(inv_Pks);
			hsql.append(" and cinventoryid in");
//			hsql.append(")");
//		}else{
//			hsql.append("('')");
//		}
		return hsql.toString();
	}
	
	@Override
	public String getBodyCondition() {
//		String sub = getTempTableUtil().getSubSql(inv_Pks);
		return " and coalesce(nnumber,0)-coalesce(ntaldcnum,0)<0"+//��������-��������<0
			" and cinventoryid in";
		}
	
//	public String getBodyContinos(){
//		StringBuffer bs = new StringBuffer();
//		bs.append(" isnull(so_saleorder_b.dr,0)=0  ");
//		return bs.toString();
//	}
	
	@Override
	protected boolean isSelfLoadHead(){
		return true;
	}
	@Override
    public boolean isSelfLoadBody() {
		
		return true;
	}
	@Override
    protected Object getUseObjOnRef()throws Exception{
		return inv_Pks;
	}
	@Override
	public boolean getIsBusinessType() {
		return false;
	}
//	public void loadMultiBodyData(String tableCode,String key,String name) throws Exception{
//		if(tableCode != null && key != null && name != null 
//				&& !"".equals(tableCode) && !"".equals(key) && !"".equals(name) ){
//			SuperVO[] supervos = HYPubBO_Client.queryByCondition(Class.forName(name), " pk_soorder='" + key + "' and "+getBodyContinos());
//			getbillListPanel().setBodyValueVO(tableCode, supervos);
//			getbillListPanel().getBodyBillModel(tableCode).execLoadFormula();
//		}
//	}
}
