package nc.ui.zb.ref;

import java.awt.Container;

import nc.bs.logging.Logger;
import nc.ui.trade.business.HYPubBO_Client;
import nc.vo.pub.SuperVO;
import nc.vo.zb.comments.BiEvaluationBodyVO;
import nc.vo.zb.comments.BidSlvendorVO;
/**
 * �б�¼�������նԻ����������б�������
 * @author Administrator
 *
 */
public class RefZb02Bill extends ZbBillSourceDLG{

private static final long serialVersionUID = 1L;
	
	public RefZb02Bill(String pkField, String pkCorp, String operator,
			String funNode, String queryWhere, String billType,
			String businessType, String templateId, String currentBillType,
			Container parent) {
		super(pkField, pkCorp, operator, funNode, queryWhere, billType, businessType,
				templateId, currentBillType, parent);
	}
	public RefZb02Bill(String pkField, String pkCorp, String operator,
			String funNode, String queryWhere, String billType,
			String businessType, String templateId, String currentBillType,
			String nodeKey, Object userObj, Container parent) {
		super(pkField, pkCorp, operator, funNode, queryWhere, billType, businessType,
				templateId, currentBillType, nodeKey, userObj, parent);
	}
	@Override
	protected boolean isHeadCanMultiSelect() {
		return false;
	}
	@Override
	protected boolean isBodyCanSelected() {
		return false;
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
				{"zb_evaluation_b",BiEvaluationBodyVO.class.getName()},
				{"zb_slvendor",BidSlvendorVO.class.getName()}
		   	};  
	}
	public String getHeadCondition() {
	
			return " ";
		}
	public void loadMultiBodyData(String key) throws Exception{
		String[][] codes = getMultiTableCode();
		if(codes!=null && codes.length>0){
			for(int i = 0 ;i<codes.length;i++){
				String[] tablecodes = codes[i];
				String tableCode = tablecodes[0];
				String name = tablecodes[1];
				SuperVO[] supervos = HYPubBO_Client.queryByCondition(Class.forName(name), "cevaluationid='" + key + "' and isnull(dr,0)=0");
				getbillListPanel().setBodyValueVO(tableCode, supervos);
				getbillListPanel().getBodyBillModel(tableCode).execLoadFormula();
			}
		}
	}
	
	public void loadMultiBodyData(String tableCode,String key,String name) throws Exception{
		if(tableCode != null && key != null && name != null 
				&& !"".equals(tableCode) && !"".equals(key) && !"".equals(name) ){
			SuperVO[] supervos = HYPubBO_Client.queryByCondition(Class.forName(name), "pk_fhtz_h='" + key + "' and isnull(dr,0)=0");
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

