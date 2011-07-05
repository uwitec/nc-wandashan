package nc.ui.wds.tranprice.tonkilometre;

import java.util.ArrayList;

import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UITable;
import nc.ui.pub.bill.BillModel;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.wl.pub.BeforeSaveValudate;
import nc.ui.wl.pub.WdsPubEnventHandler;
import nc.vo.pub.BusinessException;
import nc.vo.wl.pub.WdsWlPubConst;

public class ClientEventHandler extends WdsPubEnventHandler {

	public ClientUIQueryDlg queryDialog = null;

	public ClientEventHandler(ClientUI clientUI, IControllerBase control) {
		super(clientUI, control);
	}

	protected UIDialog createQueryUI() {
		if (queryDialog == null) {
			queryDialog=new ClientUIQueryDlg(getBillUI(),
					null,
					_getCorp().getPrimaryKey(),
					getBillUI()._getModuleCode(),
					_getOperator(),
					getBillUI().getBusinessType(),
					getBillUI().getNodeKey());
			//queryDialog = new ClientUIQueryDlg(getBillUI(), null, tempinfo);
		}
		return queryDialog;
	}
	@Override
	protected String getHeadCondition() {
		String strWhere = super.getHeadCondition();
		if(strWhere == null || "".equals(strWhere)){
			return " and pk_billtype='"+WdsWlPubConst.WDSI+"'";
		}else {
			return strWhere+" and pk_billtype='"+WdsWlPubConst.WDSI+"'";
		}
	}
	@Override
	protected void onBoSave() throws Exception {
		beforeSaveValidate();
		super.onBoSave();
	}
	protected void beforeSaveValidate() throws Exception {
		//�� ��ʼ���� �� �������� ��У�� ��ʼ����   ���ܴ���  ��������
		String o1= (String) getBillCardPanelWrapper().getBillCardPanel().getHeadItem("dstartdate").getValueObject();
		String o2= (String) getBillCardPanelWrapper().getBillCardPanel().getHeadItem("denddate").getValueObject();
		if(o1.compareTo(o2)>0){
			throw new Exception("[��ʼ����] ���ܴ���  [��������]");
		}	
		//���岻Ϊ��
		BeforeSaveValudate.BodyNotNULL(getBillCardPanelWrapper().getBillCardPanel().getBillTable());
		//�����������ջ�������Ψһ
		beforeSaveBodyUnique(
				getBillCardPanelWrapper().getBillCardPanel().getBillTable(),
				getBillCardPanelWrapper().getBillCardPanel().getBillModel(),
				new String[]{"pk_replace","ifw"}, 
				new String[]{"�ջ����","Ӧ�÷�Χ"});
		
	}
	/**
	 * 
	 * @���ߣ�mlr
	 * @˵�������ɽ������Ŀ 
	 * @ʱ�䣺2011-5-24����12:54:55
	 * @param table
	 * @param model
	 * @param fields
	 * @param displays
	 * @throws Exception
	 */
	private  void  beforeSaveBodyUnique(UITable table,BillModel model,String[] fields,String[] displays) throws Exception{
		int num =table.getRowCount();
		if(fields == null || fields.length == 0){
			return;
		}
		if(num>0){
			ArrayList<String> list = new ArrayList<String>();
			for(int i = 0 ;i<num; i++){
				String key = "";
				for(String str : fields){
					Object o1 =model.getValueAt(i, str);
					key = key + ","+String.valueOf(o1);
				}
				String dis="";
				for(int j=0;j<displays.length;j++){
					   dis=dis+"[ "+displays[j]+" ]";
					}
					
				if(list.contains(key)){							
					throw new BusinessException("��["+(i+1)+"]�б����ֶ� "+dis+" �����ظ�!");
				}else{
					list.add(key);
				}
				//���Ӧ�� ��Χ  Ϊ ȫ�� �鿴����վ���ջ�վ��ͬ����� Ӧ�÷�Χ�Ƿ���ڰ���
				if("ȫ��".equals(model.getValueAt(i, fields[2]))){
					String[] strs=key.split(",");
					
					if(list.contains(","+strs[1]+","+strs[2]+","+"�ֲ�") || list.contains(","+strs[1]+","+strs[2]+","+"������")){
						throw new Exception("��["+(i+1)+"]�б����ֶ� "+dis+" ����[ Ӧ�÷�Χ ] �İ���!");
					}
				}
				//���Ӧ�� ��Χ  Ϊ ������ �� �ֲ� �鿴����վ���ջ�վ��ͬ����� Ӧ�÷�Χ�Ƿ���ڰ���
				if("������".equals(model.getValueAt(i, fields[2])) || "�ֲ�".equals(model.getValueAt(i, fields[2])) ){
                    
					String[] strs=key.split(",");
					
					if(list.contains(","+strs[1]+","+strs[2]+","+"ȫ��") ){
						throw new Exception("��["+(i+1)+"]�б����ֶ� "+dis+" ����[ Ӧ�÷�Χ ] �İ���!");
					}
				}
				
			}
		}
	}
	@Override
	protected void onBoCopy() throws Exception {
		// TODO Auto-generated method stub
		super.onBoCopy();
		getBillUI().setDefaultData();
		getBillCardPanelWrapper().getBillCardPanel().setTailItem("vapproveid", null);
		getBillCardPanelWrapper().getBillCardPanel().setTailItem("dapprovedate", null);
	}
	
}
