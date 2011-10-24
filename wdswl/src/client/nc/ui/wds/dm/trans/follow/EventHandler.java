package nc.ui.wds.dm.trans.follow;

import nc.bs.logging.Logger;
import nc.ui.bd.pub.BillExportUtil;
import nc.ui.bd.pub.IBDButton;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.trade.base.AbstractBillUI;
import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.bill.ICardController;
import nc.ui.trade.bill.IListController;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.list.BillListUI;
import nc.ui.trade.list.ListEventHandler;
import nc.ui.trade.pub.CardPanelPRTS;
import nc.ui.trade.pub.SingleListHeadPRTS;
import nc.ui.trade.treecard.BillTreeCardUI;
import nc.ui.trade.treecard.TreeCardEventHandler;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.ValueObject;
import nc.vo.wds.dm.trans.follow.TransfollowVO;


public class EventHandler {
	public static IListEventHandler getListEventHandler(BillListUI billUI, IListController control){
		return new EventHandler().new IListEventHandler(billUI,control);
	}
	
	public static ICardEventHandler getCardEventHandler(BillTreeCardUI billUI, ICardController control){
		return new EventHandler().new ICardEventHandler(billUI,control);
	}
	public class IListEventHandler extends ListEventHandler {
		public IListEventHandler(BillListUI billUI, IListController control) {
			super(billUI, control);
		}
		@Override
		protected void onBoPrint() throws Exception {
			BillListUI ui=(BillListUI)getBillUI();
			nc.ui.pub.print.IDataSource dataSource = new SingleListHeadPRTS(getBillUI()._getModuleCode(), 
					ui.getBillListPanel());
			nc.ui.pub.print.PrintEntry print = new nc.ui.pub.print.PrintEntry(null,dataSource);
			print.setTemplateID(getBillUI()._getCorp().getPrimaryKey(), getBillUI()._getModuleCode(), 
					getBillUI()._getOperator(), getBillUI().getBusinessType(), "list");
			if (print.selectTemplate() == 1)
				print.preview();
		}

		@Override
		protected void onBoCard() throws Exception {
			super.onBoCard();
			getBufferData().clear();
			getBillUI().getMultiAppManager().showFirst();
		}
	}

	public class ICardEventHandler extends TreeCardEventHandler {
		public ICardEventHandler(BillTreeCardUI billUI, ICardController control) {
			super(billUI, control);

		}

		protected void onBoSave() throws Exception {
			this.getBillCardPanelWrapper().getBillCardPanel().getBillData()
					.dataNotNullValidate();
			AggregatedValueObject aggVo = getBillCardPanelWrapper()
					.getBillVOFromUI();
			if (aggVo.getParentVO() == null)
				return;
			boolean bIsnew = false;
			if (aggVo.getParentVO().getPrimaryKey() == null
					|| aggVo.getParentVO().getPrimaryKey().trim().length() == 0) {
				bIsnew = true;
			}
			checkBeforeSave((TransfollowVO) aggVo.getParentVO());
			super.onBoSave();
			}


		private void checkBeforeSave(TransfollowVO chnlMemTypeVO) throws Exception {
			if(chnlMemTypeVO==null)
				return;
			
		}

		protected void onBoElse(int intBtn) throws Exception {

			if (intBtn == IBDButton.EXPORT) {
				onBoBDExport();
			}
		}

		protected void onBoCancel() throws Exception {

			super.onBoCancel();
			}

		public void onBoAdd(ButtonObject bo) throws Exception {
			super.onBoAdd(bo);
		}

		@Override
		protected void buttonActionBefore(AbstractBillUI billUI, int intBtn)
				throws Exception {
			super.buttonActionBefore(billUI, intBtn);
			switch (intBtn) {
			case IBillButton.Edit:
				break;
			}
		}
		
		@Override
		protected void onBoEdit() throws Exception {
			super.onBoEdit();
		}

		protected void onBoDelete() throws Exception {
			if (getBufferData().getCurrentVO() == null)
				return;
			super.onBoDelete();
			onBoRefresh();
		}

		private void onBoBDExport() {
			try {
				BillExportUtil.output(new ValueObject[] { getBufferData().getCurrentVO().getParentVO() }, "chnlmanager", getBillTreeCardUI());
			} catch (Exception e) {
				Logger.error(e.getMessage(), e);
				MessageDialog.showErrorDlg(getBillUI(), null, e.getMessage());
			}
		}

		protected void onBoRefresh() throws Exception {
			getBufferData().clear();
			getBufferData().setCurrentRow(-1);
			getBillTreeCardUI().createBillTree(((ClientCartUI)getBillTreeCardUI()).getTreeCardData());
			//((nc.ui.med.bd.medtype.ClientUI)getBillTreeCardUI()).getBillTree();
			getBillTreeCardUI().resetTreeToBufferData();
			getBillTreeCardUI().modifyRootNodeShowName("发运跟踪信息维护");
			getBillTreeCardUI().setBillOperate(IBillOperate.OP_INIT);
		}

		@Override
		protected void onBoPrint() throws Exception {
			nc.ui.pub.print.IDataSource dataSource = new CardPanelPRTS(
					getBillUI()._getModuleCode(),
					getBillCardPanelWrapper().getBillCardPanel());
			nc.ui.pub.print.PrintEntry print = new nc.ui.pub.print.PrintEntry(null, dataSource);
			print.setTemplateID(getBillUI()._getCorp().getPrimaryKey(),
					getBillUI()._getModuleCode(), getBillUI()._getOperator(),
					getBillUI().getBusinessType(), "card");
			if (print.selectTemplate() == 1)
				print.preview();
		}

		@Override
		protected void onBoReturn() throws Exception {
			getBillUI().getMultiAppManager().showNext(
					"nc.ui.wds.dm.trans.follow.ClientListUI");
		}
	}
}
