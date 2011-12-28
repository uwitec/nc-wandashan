package nc.ui.wds.ic.so.out;

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
	
//	private int iType = -1;
	
	private String[] inv_Pks = null;// ���ݵ�ǰ��¼�߲�ѯ�����ֿ����ֿ����洢�Ĳ�Ʒ
	
	private LoginInforHelper helper = null;
	
	public LoginInforHelper getLoginInforHelper(){
		if(helper == null){
			helper = new LoginInforHelper();
		}
		return helper;
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
//			iType = getLoginInforHelper().getITypeByUser(m_logUser);//��Ա����
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
		hsql.append(" so_sale.csaleid in");//ֻ�ܿ���������ǰ��¼�˰󶨻�λ�´���ĵ���
		hsql.append("(");
		hsql.append(" select  distinct so_sale.csaleid ");
		hsql.append(" from so_sale ");
		hsql.append(" join so_saleorder_b ");
		hsql.append(" on so_sale.csaleid=so_saleorder_b.csaleid ");
		hsql.append(" where isnull(so_sale.dr,0)=0 ");
		hsql.append(" and so_sale.pk_corp = '"+getPkCorp()+"' ");
		hsql.append(" and so_sale.fstatus = 2 ");//����ͨ��
//		hsql.append(" and isnull(so_sale.bretinvflag,'N')='N' ");
		if(!isStock){
			hsql.append(" and so_sale.cwarehouseid='"+pk_stock+"'");//�ֲ�ֻ�ܿ����Լ��ģ��ֿܲ��Կ����ܲ�+�ֲֵ�
		}
		hsql.append(" and coalesce(so_saleorder_b.nnumber,0)<0");//�������۶���
		hsql.append(" and abs(coalesce(so_saleorder_b.nnumber,0))-abs(coalesce(so_saleorder_b.ntaldcnum,0))>0");//��������->//����ϵͳ���۶���  �Ѳ���۱�����(ntaldcnum) ��Ϊ  �ۼƷ�������
//		˵�������ֶ�ntaldcnum  ��дʱ ��Ҫ��д ��ֵ
		String sub = getInvSub(inv_Pks);
		hsql.append(" and cinventoryid in"+sub);
		hsql.append(" )");
		return hsql.toString();
	}
	private String getInvSub(String [] inv_Pks){
		if(inv_Pks == null || inv_Pks.length == 0){
			return "('')";
		}
		StringBuffer bur = new StringBuffer();
		bur.append("( ");
		for(int i=0;i<inv_Pks.length;i++){
			String pk_invmandoc = inv_Pks[i]==null?null:inv_Pks[i].trim();
			if(pk_invmandoc == null)
				continue;
			bur.append("'"+pk_invmandoc+"'");
			if(i<inv_Pks.length-1){
				bur.append(",");
			}
		}
		bur.append(" ,'aa')");//zhf  ��ֹ�п�
		return bur.toString();
	}
	@Override
	public String getBodyCondition() {
		String sub = getInvSub(inv_Pks);
		return " and abs(coalesce(so_saleorder_b.nnumber,0))-abs(coalesce(so_saleorder_b.ntaldcnum,0))>0"+//��������-��������<0
			" and cinventoryid in"+sub;
	}
	
	@Override
	protected boolean isSelfLoadHead(){
		return false;
	}
	@Override
    public boolean isSelfLoadBody() {
		
		return false;
	}
	@Override
	public boolean getIsBusinessType() {
		return false;
	}
}
