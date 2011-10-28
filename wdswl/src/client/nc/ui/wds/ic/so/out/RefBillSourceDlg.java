package nc.ui.wds.ic.so.out;

import java.awt.Container;

import nc.bs.logging.Logger;
import nc.ui.pub.ClientEnvironment;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.wl.pub.LoginInforHelper;
import nc.ui.wl.pub.WdsBillSourceDLG;
import nc.vo.dm.so.order.SoorderBVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
import nc.vo.wl.pub.WdsWlPubTool;

/**
 * ���۳�����������˵�(WDS5)�Ի���
 * @author zpm
 *
 */
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
		return true;
	}
	@Override
	protected boolean isBodyCanSelected() {
		return true;
	}
	public String getHeadCondition() {
		StringBuffer hsql = new StringBuffer();
		hsql.append(" wds_soorder.pk_soorder in ");//ֻ�ܿ���������ǰ��¼�˰󶨻�λ�´���ĵ���
		if(inv_Pks !=null && inv_Pks.length>0){
			hsql.append("(");
			hsql.append("select distinct wds_soorder.pk_soorder  ");
			hsql.append(" from wds_soorder ");//�����˵�����
			hsql.append(" join wds_soorder_b ");//�����˵��ֱ�
			hsql.append(" on  wds_soorder.pk_soorder = wds_soorder_b.pk_soorder ");
			hsql.append(" where isnull(wds_soorder.dr,0)=0 ");
			hsql.append(" and wds_soorder.pk_corp = '"+getPkCorp()+"'");
			hsql.append(" and wds_soorder.vbillstatus =8 ");//����̬�������˵�
			hsql.append(" and isnull(iprintcount,0)>0 ");//���Ҵ�ӡ����
			if(!isStock){
				hsql.append(" and wds_soorder.pk_outwhouse='"+pk_stock+"'");//�ֲ�ֻ�ܿ����Լ��ģ��ֿܲ��Կ����ܲ�+�ֲֵ�
			}
			hsql.append(" and isnull(wds_soorder_b.dr,0)= 0");
			hsql.append(" and coalesce(wds_soorder_b.narrangnmu,0)-coalesce(wds_soorder_b.noutnum,0)>0");//��������-��������>0
			String sub = getInvSub(inv_Pks);
			hsql.append(" and pk_invmandoc in"+sub);
			hsql.append(" )");
		}else{
			hsql.append("('')");
		}
		return hsql.toString();
		
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
	public void loadBodyData(int row) {
		try {
			// �������ID
			String key = getbillListPanel().getHeadBillModel().getValueAt(row,getpkField()).toString();
			loadMultiBodyData(key);
		} catch (Exception e) {
			Logger.error(e.getMessage()+"�������ݼ���ʧ��!", e);
		}
	}
	public String[][] getMultiTableCode(){
		return new String[][]{
				{"wds_soorder_b",SoorderBVO.class.getName()},
		   	};  
	}
	
	
	public String getBodyContinos(){
		String sub =getInvSub(inv_Pks);
		return " isnull(dr,0)=0 and coalesce(narrangnmu,0)-coalesce(noutnum,0)>0"+//��������-��������>0
			" and pk_invmandoc in"+sub;
	}
	
	@Override
	public boolean getIsBusinessType() {
		return false;
	}
	public void loadMultiBodyData(String key) throws Exception{
		String[][] codes = getMultiTableCode();
		if(codes!=null && codes.length>0){
			for(int i = 0 ;i<codes.length;i++){
				String[] tablecodes = codes[i];
				String tableCode = tablecodes[0];
				String name = tablecodes[1];
				SuperVO[] supervos = HYPubBO_Client.queryByCondition(Class.forName(name), " pk_soorder='" + key + "' and "+getBodyContinos());
				getbillListPanel().setBodyValueVO(tableCode, supervos);
				getbillListPanel().getBodyBillModel(tableCode).execLoadFormula();
			}
		}
	}
	
	public void loadMultiBodyData(String tableCode,String key,String name) throws Exception{
		if(tableCode != null && key != null && name != null 
				&& !"".equals(tableCode) && !"".equals(key) && !"".equals(name) ){
			SuperVO[] supervos = HYPubBO_Client.queryByCondition(Class.forName(name), " pk_soorder='" + key + "' and "+getBodyContinos());
			getbillListPanel().setBodyValueVO(tableCode, supervos);
			getbillListPanel().getBodyBillModel(tableCode).execLoadFormula();
		}
	}
	
	@Override
	protected synchronized void headRowChange(int iNewRow) {
		try{
			if (getbillListPanel().getHeadBillModel().getValueAt(iNewRow,getpkField()) != null) {
				String[][] codes = getMultiTableCode();
				if(codes != null && codes.length>0){
					// �������ID
					String key = getbillListPanel().getHeadBillModel().getValueAt(iNewRow,getpkField()).toString();
					for(int i = 0 ;i < codes.length;i++){
						String[] tablecodes = codes[i];
						String tableCode = tablecodes[0];
						String name = tablecodes[1];
						if (!getbillListPanel().setBodyModelData(tableCode,iNewRow)) {
							// 1.���������������
							loadMultiBodyData(tableCode,key,name);
							// 2.���ݵ�ģ����
							getbillListPanel().setBodyModelDataCopy(tableCode,iNewRow);
						}
					}
				}
			}
			getbillListPanel().repaint();
		} catch (Exception e) {
			Logger.error(e.getMessage()+"�������ݼ���ʧ��!", e);
		}
	}
}
