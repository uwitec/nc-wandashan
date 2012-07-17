package nc.ui.wds2.inv;

import java.util.List;

import javax.swing.JComponent;

import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.bill.BillData;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillModel;
import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.manage.ManageEventHandler;
import nc.ui.wl.pub.WdsBillManagUI;
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

public class StatusUpdateUI extends WdsBillManagUI {

	public boolean isSaveAndCommitTogether() {
		return true;
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

	}

	@Override
	public void setDefaultData() throws Exception {
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
			showErrorMessage("当前操作人未绑定货位");
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
					showErrorMessage("调整后状态和调整前相同");
					getBillCardPanel().setBodyValueAt(null, row, "cinvstatusid2");
					getBillCardPanel().setBodyValueAt(null, row, "invstatus2");
					return;
				}				
			}else if("vbatchcode".equalsIgnoreCase(key)){				
				//支持批次号 多选拆行  for add mlr
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
	 * 完达山物流 出库手动拣货
	 * 支持按批次自动拆行拣货
	 * @作者：mlr
	 * @说明：完达山物流项目 
	 * @时间：2012-6-15下午03:33:56
	 * @param vos
	 * @param row 
	 */
	public void pick(List<StockInvOnHandVO> vos, int row) {
		//取出本次出库的总的数量 
		UFDouble zbnum=PuPubVO.getUFDouble_NullAsZero(getBillCardPanel().getBillModel().getValueAt(row, "nassnum"));
//		if(zbnum.doubleValue()==0)
//			return;
		//进行分量  
		//按 批次号  由小到大 依次分量
		if(vos==null )
			return;
		getPick().spiltNum(vos,row,zbnum,false);//进行拣货分量
		//重新构建表体	
		createBill(vos,row);			
  }
	
	public boolean beforeEdit(BillEditEvent e) {
		String key = e.getKey();
		int row = e.getRow();
		if("invstatus2".equalsIgnoreCase(key)){//调整后状态
			Object o = getBillCardPanel().getBodyValueAt(row, "cinvstatusid");
			if(o == null)
				return false;
			return true;
		}
		if("vbatchcode".equalsIgnoreCase(key)){//批次
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
			String invstatu = PuPubVO.getString_TrimZeroLenAsNull(getBillCardPanel().getBodyValueAt(row, "cinvstatusid"));
			String[] datas ={m_strCorpID,m_strWareHouseID,m_spaceId,m_strInventoryID,invstatu};						
			getLotNumbRefPane().setDatas(datas);
			return true;
		}
		if ("invcode".equalsIgnoreCase(key)) {
		
				String pk_cargdoc=(String) getBillCardPanel().getHeadItem("ccargdocid").getValueObject();
				if(null==pk_cargdoc || "".equalsIgnoreCase(pk_cargdoc)){
					showWarningMessage("前选择货位");
					return false;
				}			
				JComponent c =getBillCardPanel().getBodyItem("invcode").getComponent();
				if( c instanceof UIRefPane){
					UIRefPane ref = (UIRefPane)c;
					ref.getRefModel().addWherePart("  and tb_spacegoods.pk_cargdoc='"+pk_cargdoc+"' ");
				}
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
	 * 根据批次拆行 重新新构建表体数据
	 * @作者：mlr
	 * @说明：完达山物流项目 
	 * @时间：2012-6-20上午11:11:57
	 * @param vos
	 * @param row
	 */
	public  void createBill(List<StockInvOnHandVO> vos, int row) {
		BillModel bm = getBillCardPanel().getBillModel();						
		if(vos == null || vos.size() == 0){
			return ;
		}
		if(vos.size()==1){
			bm.setValueAt(vos.get(0).getWhs_batchcode(), row, "vbatchcode");//批次
			//			setDate(vos.get(0).getWhs_batchcode(),row);
			bm.setValueAt(vos.get(0).getSs_pk(), row, "cinvstatusid");
			bm.setValueAt(vos.get(0).getWhs_oanum(), row, "nassnum");//设置实发辅数量  
			bm.setValueAt(vos.get(0).getCreadate().toString(), row, "vdef1");//设置实发辅数量
		}else{
			//最后一行
			if(row==bm.getRowCount()-1){
				//处理第一行
				bm.setValueAt(vos.get(0).getWhs_batchcode(), row, "vbatchcode");//批次
				//				setDate(vos.get(0).getWhs_batchcode(),row);
				bm.setValueAt(vos.get(0).getSs_pk(), row, "cinvstatusid");
				bm.setValueAt(vos.get(0).getCreadate().toString(), row, "vdef1");//设置实发辅数量
				//				bm.setValueAt(vos.get(0).getAttributeValue("whs_omnum"), row, "nshouldoutassistnum");//设置应发辅数量
				bm.setValueAt(vos.get(0).getAttributeValue("whs_oanum"), row, "nassnum");//设置实发辅数量
				for(int i=1;i<vos.size();i++){
					bm.addLine();
					bm.setBodyRowVO(bm.getBodyValueRowVO(row, StatusUpdateBodyVO.class.getName()), row+i);
					bm.setValueAt(vos.get(i).getWhs_batchcode(), row+i, "vbatchcode");//批次
					//				   setDate(vos.get(i).getWhs_batchcode(),row+i);
					bm.setValueAt(vos.get(i).getSs_pk(), row+i, "cinvstatusid");
					bm.setValueAt(vos.get(i).getCreadate().toString(), row+i, "vdef1");//设置实发辅数量
					//				   bm.setValueAt(vos.get(i).getAttributeValue("whs_omnum"), row+i, "nshouldoutassistnum");//设置应发辅数量
					bm.setValueAt(vos.get(i).getAttributeValue("whs_oanum"), row+i, "nassnum");//设置实发辅数量					
				}
			}else{
				//处理第一行
				bm.setValueAt(vos.get(0).getWhs_batchcode(), row, "vbatchcode");//批次
				//				setDate(vos.get(0).getWhs_batchcode(),row);
				bm.setValueAt(vos.get(0).getCreadate().toString(), row, "vdef1");//设置实发辅数量+
				bm.setValueAt(vos.get(0).getSs_pk(), row, "cinvstatusid");
				//				bm.setValueAt(vos.get(0).getAttributeValue("whs_omnum"), row, "nshouldoutassistnum");//设置应发辅数量
				bm.setValueAt(vos.get(0).getAttributeValue("whs_oanum"), row, "nassnum");//设置实发辅数量
				for(int i=1;i<vos.size();i++){
					bm.insertRow(row+i);
					bm.setBodyRowVO(bm.getBodyValueRowVO(row, StatusUpdateBodyVO.class.getName()), row+i);
					bm.setValueAt(vos.get(i).getWhs_batchcode(), row+i, "vbatchcode");//批次
					bm.setValueAt(vos.get(i).getSs_pk(), row+i, "cinvstatusid");
					bm.setValueAt(vos.get(i).getCreadate().toString(), row+i, "vdef1");//设置实发辅数量
					//				   setDate(vos.get(i).getWhs_batchcode(),row+i);
					//				   bm.setValueAt(vos.get(i).getAttributeValue("whs_omnum"), row+i, "nshouldoutassistnum");//设置应发辅数量
					bm.setValueAt(vos.get(i).getAttributeValue("whs_oanum"), row+i, "nassnum");//设置实发辅数量					
				}				
			}
		}
		getBillCardPanel().getBillModel().execLoadFormula();
		getBillCardPanel().getBillModel().execEditFormulaByKey(row, "nassnum");
		// this.updateUI();
		//for end mlr

	}
}
