package nc.ui.wds2.inv;

import java.util.List;

import nc.ui.pub.bill.BillData;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillModel;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.manage.ManageEventHandler;
import nc.ui.wl.pub.LoginInforHelper;
import nc.ui.zmpub.pub.bill.DefBillManageUI;
import nc.vo.ic.other.out.TbOutgeneralBVO;
import nc.vo.ic.pub.StockInvOnHandVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.wds2.inv.StatusUpdateBodyVO;
import nc.vo.wdsnew.pub.PickTool;
import nc.vo.wl.pub.LoginInforVO;
import nc.vo.wl.pub.Wds2WlPubConst;
import nc.vo.wl.pub.WdsWlPubTool;

public class StatusUpdateUI extends DefBillManageUI {
	
private LoginInforHelper helper = null;
	
	public LoginInforHelper getLoginInforHelper(){
		if(helper == null){
			helper = new LoginInforHelper();
		}
		return helper;
	}
	
	private nc.ui.wdsnew.pub.LogNumRefUFPanel ivjLotNumbRefPane=null;
	
	protected nc.ui.wdsnew.pub.LogNumRefUFPanel getLotNumbRefPane() {
		if (ivjLotNumbRefPane == null) {
			try {
				ivjLotNumbRefPane = new nc.ui.wdsnew.pub.LogNumRefUFPanel();
				ivjLotNumbRefPane.setName("LotNumbRefPane");
				ivjLotNumbRefPane.setLocation(38, 1);
			//	ivjLotNumbRefPane.setIsMutiSel(true);
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				//handleException(ivjExc);
			}
		}
		return ivjLotNumbRefPane;
	}

	@Override
	public boolean isLinkQueryEnable() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	protected AbstractManageController createController() {
		// TODO Auto-generated method stub
		return new StatusUpdateCtrl();
	}

	@Override
	public void setBodySpecialData(CircularlyAccessibleValueObject[] vos)
			throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	protected void setHeadSpecialData(CircularlyAccessibleValueObject vo,
			int intRow) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	protected void setTotalHeadSpecialData(CircularlyAccessibleValueObject[] vos)
			throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void setDefaultData() throws Exception {
		// TODO Auto-generated method stub
		super.setDefaultData();
		LoginInforVO infor = null;
		try{
			infor = getLoginInforHelper().getLogInfor(_getOperator());
		}catch(BusinessException be){
			be.printStackTrace();
			showErrorMessage(WdsWlPubTool.getString_NullAsTrimZeroLen(be.getMessage()));
			return;
		}
		 
		
		if(infor == null){
			showErrorMessage("��ǰ������δ�󶨻�λ");
			return;
		}
		
		getBillCardPanel().setHeadItem("cwarehouseid", infor.getWhid());
		getBillCardPanel().setHeadItem("ccargdocid", infor.getSpaceid());
//		getBillCardPanel().setHeadItem("vemployeeid", infor.get);
	}
	
	public void afterEdit(BillEditEvent e) {
		if(e.getPos() == BillData.BODY){
			String key = e.getKey();
			int row = e.getRow();
			if(key.equalsIgnoreCase("invstatus2")){
				String o = WdsWlPubTool.getString_NullAsTrimZeroLen(getBillCardPanel().getBodyValueAt(row, "cinvstatusid"));
				String o2 = WdsWlPubTool.getString_NullAsTrimZeroLen(getBillCardPanel().getBodyValueAt(row, "cinvstatusid2"));
				if(o.equalsIgnoreCase(o2)){
					showErrorMessage("������״̬�͵���ǰ��ͬ");
					return;
				}				
			}else if("vbatchcode".equalsIgnoreCase(key)){				
				//֧�����κ� ��ѡ����  for add mlr
				List<StockInvOnHandVO> vos=getLotNumbRefPane().getLotNumbDlg().getLis();
				pick(vos,row);	
				getLotNumbRefPane().getLotNumbDlg().setLis(null);
			}
		}
		super.afterEdit(e);
	}
	private PickTool pick=null;
	private PickTool getPick(){
		if(pick==null){
			pick=new PickTool();
		}
		return pick;
	}
	
	/**
	 * ���ɽ���� �����ֶ����
	 * ֧�ְ������Զ����м��
	 * @���ߣ�mlr
	 * @˵�������ɽ������Ŀ 
	 * @ʱ�䣺2012-6-15����03:33:56
	 * @param vos
	 * @param row 
	 */
	public void pick(List<StockInvOnHandVO> vos, int row) {
		//ȡ�����γ�����ܵ����� 
		UFDouble zbnum=PuPubVO.getUFDouble_NullAsZero(getBillCardPanel().getBillModel().getValueAt(row, "nshouldoutassistnum"));
		if(zbnum.doubleValue()==0)
			return;
		//���з���  
		//�� ���κ�  ��С���� ���η���
		if(vos==null )
			return;
		getPick().spiltNum(vos,row,zbnum);//���м������
		//���¹�������	
		createBill(vos,row);			
  }
	
	public boolean beforeEdit(BillEditEvent e) {
		String key = e.getKey();
		int row = e.getRow();
		if("invstatus2".equalsIgnoreCase(key)){//������״̬
			Object o = getBillCardPanel().getBodyValueAt(row, "cinvstatusid");
			if(o == null)
				return false;
			return true;
		}
		if("vbatchcode".equalsIgnoreCase(key)){//����
			Object o = getBillCardPanel().getBodyValueAt(row, "cinvbasid");
			if(o == null)
				return false;


			nc.ui.pub.bill.BillItem biCol = getBillCardPanel().getBodyItem(key);
			getLotNumbRefPane().setMaxLength(biCol.getLength());
			getBillCardPanel().getBodyPanel().getTable().getColumn(
					biCol.getName()).setCellEditor(
							new nc.ui.pub.bill.BillCellEditor(
									getLotNumbRefPane()));
			String m_strCorpID=_getCorp().getPrimaryKey();
			String m_strWareHouseID=PuPubVO.getString_TrimZeroLenAsNull(getBillCardPanel().getHeadItem("cwarehouseid").getValueObject());
			String m_spaceId=PuPubVO.getString_TrimZeroLenAsNull(getBillCardPanel().getHeadItem("ccargdocid").getValueObject());
			String m_strInventoryID=PuPubVO.getString_TrimZeroLenAsNull(getBillCardPanel().getBodyValueAt(row, "cinvmanid"));
			String[] datas ={m_strCorpID,m_strWareHouseID,m_spaceId,m_strInventoryID};						
			getLotNumbRefPane().setDatas(datas);
			return true;
		}
		if("invcode".equalsIgnoreCase(key)){//��ͷ��λ
			Object o = getBillCardPanel().getHeadItem("ccargdocid").getValueObject();
			if(o == null)
				return false;
			return true;
		}
		return super.beforeEdit(e);
	}

	@Override
	protected ManageEventHandler createEventHandler() {
		// TODO Auto-generated method stub
		return new StatusUpdateEventHandler(this,getUIControl());
	}

	@Override
	public String getBillType() {
		// TODO Auto-generated method stub
		return Wds2WlPubConst.billtype_statusupdate;
	}
	
	/**
	 * �������β��� �����¹�����������
	 * @���ߣ�mlr
	 * @˵�������ɽ������Ŀ 
	 * @ʱ�䣺2012-6-20����11:11:57
	 * @param vos
	 * @param row
	 */
	public  void createBill(List<StockInvOnHandVO> vos, int row) {
		BillModel bm = getBillCardPanel().getBillModel();						
		if(vos == null || vos.size() == 0){
			return ;
		}
		if(vos.size()==1){
			bm.setValueAt(vos.get(0).getWhs_batchcode(), row, "vbatchcode");//����
			//			setDate(vos.get(0).getWhs_batchcode(),row);
			bm.setValueAt(vos.get(0).getSs_pk(), row, "cinvstatusid");
			bm.setValueAt(vos.get(0).getWhs_oanum(), row, "nassnum");//����ʵ��������  
		}else{
			//���һ��
			if(row==bm.getRowCount()-1){
				//������һ��
				bm.setValueAt(vos.get(0).getWhs_batchcode(), row, "vbatchcode");//����
				//				setDate(vos.get(0).getWhs_batchcode(),row);
				bm.setValueAt(vos.get(0).getSs_pk(), row, "cinvstatusid");

				//				bm.setValueAt(vos.get(0).getAttributeValue("whs_omnum"), row, "nshouldoutassistnum");//����Ӧ��������
				bm.setValueAt(vos.get(0).getAttributeValue("whs_oanum"), row, "nassnum");//����ʵ��������
				for(int i=1;i<vos.size();i++){
					bm.addLine();
					bm.setBodyRowVO(bm.getBodyValueRowVO(row, StatusUpdateBodyVO.class.getName()), row+i);
					bm.setValueAt(vos.get(i).getWhs_batchcode(), row+i, "vbatchcode");//����
					//				   setDate(vos.get(i).getWhs_batchcode(),row+i);
					bm.setValueAt(vos.get(i).getSs_pk(), row+i, "cinvstatusid");

					//				   bm.setValueAt(vos.get(i).getAttributeValue("whs_omnum"), row+i, "nshouldoutassistnum");//����Ӧ��������
					bm.setValueAt(vos.get(i).getAttributeValue("whs_oanum"), row+i, "nassnum");//����ʵ��������					
				}
			}else{
				//������һ��
				bm.setValueAt(vos.get(0).getWhs_batchcode(), row, "vbatchcode");//����
				//				setDate(vos.get(0).getWhs_batchcode(),row);
				bm.setValueAt(vos.get(0).getSs_pk(), row, "cinvstatusid");
				//				bm.setValueAt(vos.get(0).getAttributeValue("whs_omnum"), row, "nshouldoutassistnum");//����Ӧ��������
				bm.setValueAt(vos.get(0).getAttributeValue("whs_oanum"), row, "nassnum");//����ʵ��������
				for(int i=1;i<vos.size();i++){
					bm.insertRow(row+i);
					bm.setBodyRowVO(bm.getBodyValueRowVO(row, StatusUpdateBodyVO.class.getName()), row+i);
					bm.setValueAt(vos.get(i).getWhs_batchcode(), row+i, "vbatchcode");//����
					bm.setValueAt(vos.get(i).getSs_pk(), row+i, "cinvstatusid");
					//				   setDate(vos.get(i).getWhs_batchcode(),row+i);
					//				   bm.setValueAt(vos.get(i).getAttributeValue("whs_omnum"), row+i, "nshouldoutassistnum");//����Ӧ��������
					bm.setValueAt(vos.get(i).getAttributeValue("whs_oanum"), row+i, "nassnum");//����ʵ��������					
				}				
			}
		}
		getBillCardPanel().getBillModel().execLoadFormula();
		// this.updateUI();
		//for end mlr

	}
}