package nc.ui.hg.pu.plugin;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import nc.ui.hg.pu.pub.PlanPubHelper;
import nc.ui.pi.invoice.IButtonConstInv;
import nc.ui.po.oper.OrderUI;
import nc.ui.po.oper.TermRefModel;
import nc.ui.po.pub.PoCardPanel;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.ToftPanel;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillItemEvent;
import nc.ui.pub.bill.BillModel;
import nc.ui.pub.bill.BillMouseEnent;
import nc.ui.scm.plugin.IScmUIPlugin;
import nc.ui.scm.plugin.SCMUIContext;
import nc.vo.hg.pu.pact.PactItemVO;
import nc.vo.hg.pu.pub.HgPubConst;
import nc.vo.hg.pu.pub.HgPubTool;
import nc.vo.po.OrderHeaderVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.VOStatus;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.constant.ScmConst;
import nc.vo.scm.plugin.Action;
import nc.vo.scm.pu.PuPubVO;

public class PoUIPlugin implements IScmUIPlugin {
	
	private SCMUIContext ctx = null;
	
	private BillItem[] m_headEditItems = null;

	public void afterAction(Action action, AggregatedValueObject[] billvos,
			SCMUIContext conx) throws BusinessException {
		// TODO Auto-generated method stub

		
	}
	
	private OrderHeaderVO m_curhead = null;
	
//	private int m_state = 0;//0 初始  1编辑
	
	private void onPactButtonCliked(ButtonObject bo,SCMUIContext ctx){
		this.ctx = ctx;
		m_curhead = getUI().getBufferVOManager().getHeadVOAt(getUI().getBufferVOManager().getVOPos());
		if(bo == getUI().m_btnModifyPact){
			onModifyPact();
			
		}else if(bo == getUI().m_btnCancelPact){
			onCancel();
		}else if(bo == getUI().m_btnSavePact){
			onSave();
		}else if(bo == getUI().m_btnAddLinePact){
			onAddLine();
		}else if(bo == getUI().m_btnDelLinePact){
			onDelLine();
		}
	}
	
	private void onModifyPact(){
		
		ctx.getBillCardPanel().setEnabled(true);
		
		updateState(true);
		//表头设置为不可编辑
		
		//表体页签不可切换
		
		//按钮状态设置
	}
	
	private void updateState(boolean isedit){
		getUI().m_btnModifyPact.setEnabled(!isedit);
		getUI().m_btnAddLinePact.setEnabled(isedit);
		getUI().m_btnDelLinePact.setEnabled(isedit);
		getUI().m_btnCancelPact.setEnabled(isedit);
		getUI().m_btnSavePact.setEnabled(isedit);
		if(isedit)
		if(m_headEditItems!=null){
			for(BillItem item:m_headEditItems){
				item.setEdit(false);
			}
		}
		getUI().updateButtons();
		
		for(int i=0;i<HgPubConst.PO_PACT_TABLECODE_INDEX;i++){
			ctx.getBillCardPanel().getBodyTabbedPane().setEnabledAt(i, !isedit);
		}
	}
	
	private BillModel getPactPanel(){
		return ctx.getBillCardPanel().getBillModel();
	}
	
	private void onAddLine(){
		getPactPanel().addLine();
	}
	
	private void onDelLine(){
		int[] rows = ctx.getBillCardPanel().getBillTable().getSelectedRows();
		if(rows == null||rows.length ==0)
			return;
		getPactPanel().delLine(rows);
	}
	
	private void onCancel(){
		ctx.getBillCardPanel().stopEditing();
		ctx.getBillCardPanel().setEnabled(false);
		PactItemVO[] pactvos = getUI().getPactItemInfor().get(m_curhead.getPrimaryKey());
		getPactPanel().setBodyDataVO(pactvos);
		getPactPanel().execLoadFormula();
		updateState(false);
	}
	
	private PactItemVO[] getPactVos(){
		PactItemVO[] newpacts = (PactItemVO[])getPactPanel().getBodyValueVOs(PactItemVO.class.getName());
		PactItemVO[] oldpacts = getUI().getPactItemInfor().get(m_curhead.getPrimaryKey());
		if(oldpacts == null||oldpacts.length ==0){
			for(PactItemVO pact:newpacts){
				pact.setStatus(VOStatus.NEW);
			}
			return newpacts;
		}else{
			List<PactItemVO> lpact = new ArrayList<PactItemVO>();
			Map<String,PactItemVO> oldPactInfor = new HashMap<String, PactItemVO>();
			for(PactItemVO pact:oldpacts){
				pact.setStatus(VOStatus.DELETED);//默认删除
				oldPactInfor.put(pact.getPrimaryKey(), pact);
			}
			
			for(PactItemVO pact:newpacts){
				if(PuPubVO.getString_TrimZeroLenAsNull(pact.getPrimaryKey())==null){
					pact.setStatus(VOStatus.NEW);
				}else{
					pact.setStatus(VOStatus.UPDATED);
					oldPactInfor.remove(pact.getPrimaryKey());//非删除的除去
				}
				lpact.add(pact);
			}
			
			if(oldPactInfor.size()>0){
				lpact.addAll(oldPactInfor.values());
			}
			if(lpact.size()==0)
				return null;
			return lpact.toArray(new PactItemVO[0]);			
		}
	}
	private void validation(PactItemVO[] pactvos) throws BusinessException{
		List<String> lcode = new ArrayList<String>();
		for(PactItemVO pact:pactvos){
			if(lcode.contains(pact.getTermcode()))
				throw new BusinessException("合同条款重复录入");
			lcode.add(pact.getTermcode());
		}
	}
	private void onSave(){
		ctx.getBillCardPanel().stopEditing();
		try{
			ctx.getBillCardPanel().dataNotNullValidate();
		}catch(Exception e){
			e.printStackTrace();
			getUI().showErrorMessage(e.getMessage());
			return;
		}
	
		PactItemVO[] pactvos = getPactVos();
		if(pactvos == null||pactvos.length == 0){
			getUI().showWarningMessage("无合同条款数据");
			return;
		}		
		Object o = null;
		try{
			validation(pactvos);
			o = PlanPubHelper.savePactItemsForPO(pactvos,m_curhead.getPrimaryKey());
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			getUI().showErrorMessage(HgPubTool.getString_NullAsTrimZeroLen(e.getMessage()));
			return;
		}
		
//		if(o == null||!(o instanceof PactItemVO[])){
////			getUI().showErrorMessage("保存后刷新数据异常，请手动刷新数据");
//			return;
//		}
		
		PactItemVO[] newPacts = (PactItemVO[])o;
		getUI().getPactItemInfor().put(m_curhead.getPrimaryKey(), newPacts);
		getPactPanel().setBodyDataVO(newPacts);
		getPactPanel().execLoadFormula();
		ctx.getBillCardPanel().setEnabled(false);
		updateState(false);
	}
	
	public void validntaxrate() throws BusinessException{
		UFDouble hrate = PuPubVO.getUFDouble_NullAsZero(ctx.getBillCardPanel().getHeadItem("ntaxrate").getValue());
		int rowcount = ctx.getBillCardPanel().getRowCount();
		for(int i=0;i<rowcount;i++){
			UFDouble brate = PuPubVO.getUFDouble_NullAsZero(ctx.getBillCardPanel().getBodyValueAt(i,"ntaxrate"));
			if(!hrate.equals(brate)){
				throw new BusinessException("第"+(i+1)+"行的表体税率与表头不同");
			}
				
		}
	}

	public void afterButtonClicked(ButtonObject bo, SCMUIContext ctx)
			throws BusinessException {
		// TODO Auto-generated method stub
		this.ctx = ctx;
		if(ctx.getCbilltype().equalsIgnoreCase(ScmConst.PO_Order)){//采购订单处理
			//采购订单  新增编辑时   设置   合同条款页签  不可用
			if(getUI().getCurOperState()==HgPubConst.PO_STATE_BILL_EDIT){
				ctx.getBillCardPanel().getBodyTabbedPane().setEnabledAt(HgPubConst.PO_PACT_TABLECODE_INDEX, false);
				if(m_headEditItems!=null){
					for(BillItem item:m_headEditItems){
						item.setEdit(true);
					}
				}
			}else{
				ctx.getBillCardPanel().getBodyTabbedPane().setEnabledAt(HgPubConst.PO_PACT_TABLECODE_INDEX, true);			
			}
			
			
		}
	}

	public void afterEdit(BillEditEvent e, SCMUIContext ctx) {
		this.ctx = ctx;
		String key = e.getKey();
		if(e.getPos() == BillItem.HEAD){
			//lyf begin add for表头供应商有默认的税率;表头税率变化，表体税率和表头税率保持一致。
			if("cvendormangid".equals(key)){
				((PoCardPanel)ctx.getBillCardPanel()).execHeadLoadFormulas();
				Object newRate = ctx.getBillCardPanel().getHeadItem("ntaxrate").getValueObject();
				setBodyRate("ntaxrate", newRate);
			}else if( "ntaxrate".equals(key)){
				setBodyRate("ntaxrate", e.getValue());
			}
		}
//		else if(e.getPos() == BillItem.BODY){
//			if("cinventorycode".equals(key)){
//				UFDouble newRate = PuPubVO.getUFDouble_NullAsZero(ctx.getBillCardPanel().getHeadItem("ntaxrate").getValueObject());
//				int row = e.getRow();
//				((PoCardPanel)ctx.getBillCardPanel()).getBillModel().setValueAt(newRate, row, "ntaxrate");
//				afterEdtitSelf(key,row,BillItem.BODY,null,newRate);
//			}
//		}
	}
	/**
	 * 设置表体某一列的值
	 * @param key
	 * @param newRate
	 */
	private void setBodyRate(String key,Object value){
		int row = ((PoCardPanel)ctx.getBillCardPanel()).getBillModel().getRowCount();
		for(int i=0;i<row;i++){
			((PoCardPanel)ctx.getBillCardPanel()).getBillModel().setValueAt(value, i, key);
			afterEdtitSelf(key,i,BillItem.BODY,null,value);
		}
		
	}
	
	private void afterEdtitSelf(String key,int row,int pos,Object oValue,Object value){
		BillEditEvent e2 = new BillEditEvent(ctx.getToftPanel(),oValue,value,key,row,pos);
        ((PoCardPanel)ctx.getBillCardPanel()).afterEdit(e2);
	}

	public void afterSetBillVOToCard(AggregatedValueObject billvo,
			SCMUIContext conx) {
		// TODO Auto-generated method stub
		this.ctx = ctx;
	}

	public void afterSetBillVOsToListBody(
			CircularlyAccessibleValueObject[] bodyvos, SCMUIContext conx) {
		// TODO Auto-generated method stub
		this.ctx = ctx;
	}

	public void afterSetBillVOsToListHead(
			CircularlyAccessibleValueObject[] headvos, SCMUIContext conx) {
		// TODO Auto-generated method stub
		this.ctx = ctx;
	}

	public void beforeAction(Action action, AggregatedValueObject[] billvos,
			SCMUIContext conx) throws BusinessException {
		// TODO Auto-generated method stub
		this.ctx = ctx;
	}
	
	//zhf add
	private OrderUI getUI(){
		if(ctx == null)
			return null;
		this.ctx = ctx;
		ToftPanel tp = ctx.getToftPanel();
		if(tp instanceof OrderUI)
			return (OrderUI)ctx.getToftPanel();
		return null;
	}

	public void beforeButtonClicked(ButtonObject bo, SCMUIContext ctx)
			throws BusinessException {
		// TODO Auto-generated method stub	
		this.ctx = ctx;		
		if (bo.getCode().equals(IButtonConstInv.BTN_SAVE)) {
			validntaxrate();// add  by zhw 2011-03-18  表体 表头税率是否一致问题
		}
		onPactButtonCliked(bo, ctx);
		
	}

	public boolean beforeEdit(BillEditEvent e, SCMUIContext ctx) {
		// TODO Auto-generated method stub
		this.ctx = ctx;
		return true;
	}

	public boolean beforeEdit(BillItemEvent e, SCMUIContext conx) {
		// TODO Auto-generated method stub
		this.ctx = ctx;
		return true;
	}

	public AggregatedValueObject[] beforePrint(
			AggregatedValueObject[] printVOs, SCMUIContext conx) {
		// TODO Auto-generated method stub
		this.ctx = ctx;
		return null;
	}

	public void beforeSetBillVOToCard(AggregatedValueObject billvo,
			SCMUIContext conx) {
		// TODO Auto-generated method stub
		this.ctx = ctx;
	}

	public void beforeSetBillVOsToListBody(
			CircularlyAccessibleValueObject[] bodyvos, SCMUIContext conx) {
		// TODO Auto-generated method stub
		this.ctx = ctx;
	}

	public void beforeSetBillVOsToListHead(
			CircularlyAccessibleValueObject[] headvos, SCMUIContext conx) {
		// TODO Auto-generated method stub
		this.ctx = ctx;
	}

	public void bodyRowChange(BillEditEvent e, SCMUIContext ctx) {
		// TODO Auto-generated method stub
		this.ctx = ctx;
	}

	public boolean init(SCMUIContext ctx) {
		// TODO Auto-generated method stub
		this.ctx = ctx;
//		设置  合同条款  编码参照
			UIRefPane pane = (UIRefPane) (ctx.getBillCardPanel().getBillModel(HgPubConst.PU_PACT_ITEM_TABLECODE).getItemByKey("termcode").getComponent());
			((TermRefModel)pane.getRefModel()).setPK_Corp((ctx.getLoginCorpID()));
			List<BillItem> litems = new ArrayList<BillItem>();
			BillItem[] items = ctx.getBillCardPanel().getHeadItems();
			for(BillItem item:items){
				if(item.isShow()&&item.isEdit()){
					litems.add(item);
				}
			}
			if(litems.size()>0)
				m_headEditItems = litems.toArray(new BillItem[0]);
			return false;
	}

	public void mouse_doubleclick(BillMouseEnent e, SCMUIContext ctx) {
		// TODO Auto-generated method stub
		this.ctx = ctx;
	}

	public void onAddLine(SCMUIContext conx) throws BusinessException {
		// TODO Auto-generated method stub
		this.ctx = ctx;
	}

	public void onMenuItemClick(ActionEvent e, SCMUIContext ctx) {
		// TODO Auto-generated method stub
		this.ctx = ctx;
	}

	public void onPastLine(SCMUIContext conx) throws BusinessException {
		// TODO Auto-generated method stub
		this.ctx = ctx;
	}

	public String onQuery(String swhere, SCMUIContext conx)
			throws BusinessException {
		// TODO Auto-generated method stub
		this.ctx = ctx;
		return null;
	}

	public Object[] retBillToBillRefVOs(
			CircularlyAccessibleValueObject[] headVos,
			CircularlyAccessibleValueObject[] bodyVos) throws BusinessException {
		// TODO Auto-generated method stub
		this.ctx = ctx;
		return null;
	}

	public AggregatedValueObject[] retChangeBusiVOs(
			AggregatedValueObject[] preVo, AggregatedValueObject[] nowVo)
			throws BusinessException {
		// TODO Auto-generated method stub
		this.ctx = ctx;
		return null;
	}

	public void setButtonStatus(SCMUIContext conx) {
		// TODO Auto-generated method stub
		this.ctx = ctx;

	}

}
