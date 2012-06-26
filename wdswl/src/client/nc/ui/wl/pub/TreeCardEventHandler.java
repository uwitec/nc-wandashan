package nc.ui.wl.pub;

import nc.ui.pub.ButtonObject;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UITree;
import nc.ui.pub.pf.PfUtilClient;
import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.bill.BillCardPanelWrapper;
import nc.ui.trade.bill.ICardController;
import nc.ui.trade.bill.ISingleController;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.pub.VOTreeNode;
import nc.ui.trade.treecard.ITreeCardController;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.SuperVO;

/**
 * ��Ƭ���¼������������
 * @author��zpm
 */
public class TreeCardEventHandler extends nc.ui.trade.card.CardEventHandler {

/**
 * CardButtonController ������ע�⡣
 * @param billUI nc.ui.trade.pub.BillCardUI
 */
public TreeCardEventHandler(BillTreeCardUI billUI,ICardController control) {
	super(billUI,control);
}
/**
 * ����ģ���װ��ķ�������
 * �������ڣ�(2004-1-6 22:29:36)
 * @return nc.ui.pub.bill.BillCardPanel
 */
protected BillCardPanelWrapper getBillCardPanelWrapper(){
	return getBillTreeCardUI().getBillCardWrapper();
}
/**
 * �˴����뷽��˵����
 * �������ڣ�(2004-1-9 8:37:34)
 * @return nc.ui.trade.pub.BillCardUI
 */
protected BillTreeCardUI getBillTreeCardUI() {
	return (BillTreeCardUI)getBillUI();
}
/**
 * Bill��Ӧ�Ļ�������,CARD,LIST�������ش˷�����
 * �������ڣ�(2004-1-7 8:44:06)
 * @return nc.ui.trade.buffer.BillUIBuffer
 */
protected nc.ui.trade.buffer.BillUIBuffer getBufferData() {
	return getBillTreeCardUI().getBufferData();
}
/**
 * ��ÿ�Ƭ��������
 * �������ڣ�(2004-1-7 11:42:27)
 * @return nc.ui.trade.controller.ICardController
 */
protected ITreeCardController getUITreeCardController() {
	return (ITreeCardController)getUIController();
}
/**
 * �Ƿ���������ڵ㡣
 * �������ڣ�(2004-02-06 13:12:42)
 * @return boolean
 */
public boolean isAllowAddNode(nc.ui.trade.pub.TableTreeNode node) {
	return true;
}
/**
 * �Ƿ����ɾ���ڵ㡣
 * �������ڣ�(2004-02-06 13:12:42)
 * @return boolean
 */
public boolean isAllowDelNode(nc.ui.trade.pub.TableTreeNode node) {
	return true;
}
/**
* �������ӵĴ���
* �������ڣ�(2002-12-23 12:43:15)
*/
public void onBoAdd(ButtonObject bo) throws Exception {
	if(isAllowAddNode(getBillTreeCardUI().getBillTreeSelectNode()))
		super.onBoAdd(bo);
}
/**
��ťm_boDel���ʱִ�еĶ���,���б�Ҫ���븲��.
*/
protected void onBoDel() throws Exception {
    if (MessageDialog.showYesNoDlg(getBillUI(), "ɾ��", "�Ƿ�ȷ��ɾ����ǰ����?") != UIDialog.ID_YES) {
        return;
    }
	if(isAllowDelNode(getBillTreeCardUI().getBillTreeSelectNode())){
		super.onBoDel();
		if(getUITreeCardController().isAutoManageTree()){
			getBillTreeCardUI().deleteNodeFromTree(getBillCardPanelWrapper().getBillVOFromUI().getParentVO());
			getBillCardPanelWrapper().getBillCardPanel().getBillData().clearViewData();
		}
	}
}
/**
	��ťm_boDel���ʱִ�еĶ���,���б�Ҫ���븲��.
	������ɾ������
*/
protected void onBoDelete() throws Exception {
	if(isAllowDelNode(getBillTreeCardUI().getBillTreeSelectNode())){
		if (MessageDialog.showOkCancelDlg(getBillUI(), 
				nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory","UPPuifactory-000064")/*@res "����ɾ��"*/, 
				nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory","UPPuifactory-000065")/*@res "�Ƿ�ȷ��ɾ���û�������?"*/
				,UIDialog.ID_CANCEL)
			!= UIDialog.ID_OK)
			return;

		AggregatedValueObject modelVo = getBufferData().getCurrentVO();
		//�����������
		Object o = null;
		ISingleController sCtrl = null;
		if (getUIController() instanceof ISingleController)
		{
			sCtrl = (ISingleController) getUIController();
			if (sCtrl.isSingleDetail())
			{
				o = modelVo.getParentVO();
				modelVo.setParentVO(null);
			}
			else
			{
				o = modelVo.getChildrenVO();
				modelVo.setChildrenVO(null);
			}
		}

		getBusinessAction().delete(
			modelVo,
			getUIController().getBillType(),
			getBillUI()._getDate().toString(),
			getBillUI().getUserObject());

		if (PfUtilClient.isSuccess())
		{
			//������ɾ���ڵ�
			if(getUITreeCardController().isAutoManageTree()){
				getBillTreeCardUI().deleteNodeFromTree(getBillCardPanelWrapper().getBillVOFromUI().getParentVO());
				getBillCardPanelWrapper().getBillCardPanel().getBillData().clearViewData();
			}
			//�����������
			if (getUIController() instanceof ISingleController)
			{
				ISingleController sctl = (ISingleController) getUIController();
				if (sctl.isSingleDetail())
					getBufferData().refresh();
				else
					getBufferData().removeCurrentRow();
			}else{
				getBufferData().removeCurrentRow();
			}
			getBillTreeCardUI().resetTreeToBufferData();
		}

		if (getBufferData().getVOBufferSize() == 0)
			getBillUI().setBillOperate(IBillOperate.OP_INIT);
		else
			getBillUI().setBillOperate(IBillOperate.OP_NOTEDIT);

	}
}
/**
��ťm_boRefresh���ʱִ�еĶ���,���б�Ҫ���븲��.
*/
protected void onBoRefresh() throws Exception {

	getBillTreeCardUI().clearTreeSelect();

	//zpm �޸�
	getBillTreeCardUI().createBillTree(getBillTreeCardUI().getCreateTreeDataRefresh());

	getBillTreeCardUI().afterInit();

	getBillTreeCardUI().setBillOperate(nc.ui.trade.base.IBillOperate.OP_INIT);
}
/**
 * �˴����뷽��˵����
 * �������ڣ�(2004-1-9 8:37:34)
 * @return nc.ui.trade.pub.BillCardUI
 */
protected void onBoSave() throws Exception {
	super.onBoSave();
	if(getUITreeCardController().isAutoManageTree()){
		getBillTreeCardUI().insertNodeToTree(getBillCardPanelWrapper().getBillVOFromUI().getParentVO());
	}
}
/**
 * Button���¼���Ӧ����
 * �������ڣ�(2004-1-6 17:20:57)
 * @param bo nc.ui.pub.ButtonObject
 * @exception java.lang.Exception �쳣˵����
 */
public void onButton(ButtonObject bo) {
	if(getUITreeCardController().isTableTree()){
		ButtonObject parentBtn = bo.getParent();
		if (parentBtn != null) {
			int intBtn = Integer.parseInt(parentBtn.getTag());
			if(intBtn == IBillButton.Line){
				System.out.println("����ģʽ��֧���в�����");
				return;
			}
		}
	}
	super.onButton(bo);
}
/**
 * �˴����뷽��˵����
 * �������ڣ�(2004-02-04 9:41:03)
 * @param node nc.ui.pub.card.treetableex.VOTreeNode
 */
private void onQueryHeadData(VOTreeNode selectnode) throws Exception{

	Class voClass = Class.forName(getUIController().getBillVoName()[1]);

	SuperVO vo = (SuperVO)voClass.newInstance();

	String strWhere = "(isnull(dr,0)=0)";

	if(vo.getParentPKFieldName() != null)
		strWhere = "(" + strWhere + ") and " + vo.getParentPKFieldName() + "='" + selectnode.getData().getPrimaryKey() + "'";

	SuperVO[] queryVos =
		getBusiDelegator().queryHeadAllData(
			voClass,
			getUIController().getBillType(),
			strWhere);

	if (queryVos != null && queryVos.length != 0)
	{
		AggregatedValueObject aVo =
			(AggregatedValueObject) Class
				.forName(getUIController().getBillVoName()[0])
				.newInstance();
		aVo.setParentVO(queryVos[0]);
		getBufferData().addVOToBuffer(aVo);

	    int num = getBufferData().getVOBufferSize();
	    if(num == -1)
		     num = 0;
	    else
		     num = num - 1;
	    getBillTreeCardUI().getTreeToBuffer().put(selectnode.getNodeID(),num+"");

	    getBillUI().setBillOperate(IBillOperate.OP_NOTEDIT);
	}
	else
	{
		getBillUI().setBillOperate(IBillOperate.OP_INIT);
	}
}
/**
 * �˴����뷽��˵����
 * �������ڣ�(2004-02-04 9:41:03)
 * @param node nc.ui.pub.card.treetableex.VOTreeNode
 */
public void onTreeSelected(VOTreeNode selectnode){
	//�˷���-----------zpmע�͵�
//	try{
//		if(!(getUITreeCardController() instanceof ISingleController)){
//			onQueryHeadData(selectnode);
//		}
//	} catch (BusinessException ex) {
//		getBillUI().showErrorMessage(ex.getMessage());
//		ex.printStackTrace();
//	} catch (Exception e) {
//		getBillUI().showErrorMessage(e.getMessage());
//		e.printStackTrace();
//	}
}

	protected void onBoCancel() throws Exception
	{
		super.onBoCancel();
		
		UITree tree = getBillTreeCardUI().getBillTree();
		
		if(tree==null||
				tree.getSelectionPath()==null||
				tree.getSelectionPath().getPath()==null||
				tree.getSelectionPath().getPath().length<=1)
		{
			getBillTreeCardUI().setBillOperate(IBillOperate.OP_INIT);
		}		
	}
}