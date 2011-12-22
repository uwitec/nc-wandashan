package nc.ui.dm.so.deal;

import java.util.ArrayList;
import java.util.List;

import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillEditListener;
import nc.ui.pub.bill.BillModel;
import nc.ui.pub.bill.BillStatus;
import nc.ui.pub.bill.IBillModelRowStateChangeEventListener;
import nc.ui.pub.bill.IBillRelaSortListener2;
import nc.ui.wl.pub.FilterNullBody;
import nc.vo.dm.so.deal.SoDeHeaderVo;
import nc.vo.dm.so.deal.SoDealBillVO;
import nc.vo.dm.so.deal.SoDealVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.SuperVO;
import nc.vo.pub.VOStatus;
import nc.vo.pub.ValidationException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.scm.pub.vosplit.SplitBillVOs;
import nc.vo.trade.voutils.VOUtil;
import nc.vo.wl.pub.VOTool;
import nc.vo.wl.pub.WdsWlPubConst;
import nc.vo.wl.pub.WdsWlPubTool;

public class SoDealEventHandler implements BillEditListener,IBillRelaSortListener2{


	private SoDealClientUI ui = null;
	private SoDealQryDlg m_qrypanel = null;
	
	public SoDealVO[] curBodys = null;
	//数据缓存
	private SoDealVO[] m_billdatas = null;
//	private List<SoDealVO> lseldata = new ArrayList<SoDealVO>();
	
	public SoDealEventHandler(SoDealClientUI parent){
		super();
		ui = parent;
		getDataPane().addSortRelaObjectListener2(this);
		
	}
	
	private BillModel getHeadDataPane(){
		return ui.getPanel().getHeadBillModel();
	}
	
	private BillModel getBodyDataPane(){
		return ui.getPanel().getBodyBillModel();
	}

	private BillModel getDataPane(){
		return ui.getPanel().getHeadBillModel();
	}

	public void onButtonClicked(String btnTag){
		if(btnTag.equalsIgnoreCase(WdsWlPubConst.DM_PLANDEAL_BTNTAG_DEAL)){
			try {
				onDeal();
			} catch (BusinessException e) {
				e.printStackTrace();
				showErrorMessage(e.getMessage());
			}
		}else if(btnTag.equalsIgnoreCase(WdsWlPubConst.DM_PLANDEAL_BTNTAG_SELNO)){
			onNoSel();
		}else if(btnTag.equalsIgnoreCase(WdsWlPubConst.DM_PLANDEAL_BTNTAG_SELALL)){
			onAllSel();
		}else if(btnTag.equalsIgnoreCase(WdsWlPubConst.DM_PLANDEAL_BTNTAG_QRY)){
			onQuery();
		}else if (btnTag
				.equalsIgnoreCase(WdsWlPubConst.DM_PLANDEAL_BTNTAG_XNDEAL)) {
//			onXNDeal();
		}
	}
	
	public SoDealVO[] getDataBuffer(){
		return m_billdatas;
	}
	/**
	 * 
	 * @作者：lyf:根据表头单据号，加载表体
	 * @说明：完达山物流项目 
	 * @时间：2011-11-10下午08:10:37
	 * @param key
	 * @return
	 */
	protected SoDealVO[] getSelectBufferData(String key ){
		if(key == null || "".equalsIgnoreCase(key)){
			return null;
		}
		if(m_billdatas == null || m_billdatas.length ==0){
			return null;
		}
		ArrayList<SoDealVO> list = new ArrayList<SoDealVO>();
		for(SoDealVO dealvo:m_billdatas){
			String vreceiptcode = dealvo.getVreceiptcode();
			if(key.equalsIgnoreCase(vreceiptcode)){
				list.add(dealvo);
			}
		}
		curBodys = list.toArray( new SoDealVO[0]);
		return curBodys;
 	}
	public void onNoSel(){
		int rowcount = getDataPane().getRowCount();
		if(rowcount <= 0)
			return;
		for (int i = 0; i < ui.getPanel().getParentListPanel().getTable().getRowCount(); i++) {
			ui.getPanel().getParentListPanel().getTableModel().setRowState(i, BillModel.UNSTATE);
			ui.headRowChange(i);
			BillModel model = ui.getPanel().getBodyBillModel();
			IBillModelRowStateChangeEventListener l = model.getRowStateChangeEventListener();
			model.removeRowStateChangeEventListener();
			ui.getPanel().getChildListPanel().cancelSelectAllTableRow();
			model.addRowStateChangeEventListener(l);
			ui.getPanel().updateUI();
		}
	}
	

	/**
	 * 
	 * @作者：全选
	 * @说明：完达山物流项目 
	 * @时间：2011-11-11上午11:09:55
	 */
	public void onAllSel(){
		if(getDataBuffer() == null||getDataBuffer().length == 0)
			return;
		for (int i = 0; i < ui.getPanel().getParentListPanel().getTable().getRowCount(); i++) {
			ui.getPanel().getParentListPanel().getTableModel().setRowState(i, BillModel.SELECTED);
			ui.headRowChange(i);
			BillModel model = ui.getPanel().getBodyBillModel();
			IBillModelRowStateChangeEventListener l = model.getRowStateChangeEventListener();
			model.removeRowStateChangeEventListener();
			ui.getPanel().getChildListPanel().selectAllTableRow();
			model.addRowStateChangeEventListener(l);
			ui.getPanel().updateUI();
		}
		
	}
	private SoDealQryDlg getQryDlg(){
		if(m_qrypanel == null){
			m_qrypanel = new SoDealQryDlg(ui,
					null,
					ui.cl.getCorp(),
					WdsWlPubConst.DM_SO_DEAL_NODECODE,
					ui.cl.getUser(),
					null
				);
			m_qrypanel.setTempletID(ui.cl.getCorp(), WdsWlPubConst.DM_SO_DEAL_NODECODE, ui.cl.getUser(), null);
			m_qrypanel.hideUnitButton();
			m_qrypanel.hideNormal();
			//			m_qrypanel.setConditionEditable("h.pk_corp",true);
			//			m_qrypanel.setValueRef("h.pk_corp", new UIRefPane("公司目录"));
			//			m_qrypanel.changeValueRef("h.pk_corp", new UIRefPane("公司目录"));
		}
		return m_qrypanel;
	}
	
	private void clearData(){
		m_billdatas = null;
		getDataPane().clearBodyData();
		getBodyDataPane().clearBodyData();
	}
	/**
	 * 
	 * @作者：lyf
	 * @说明：完达山物流项目 
	 * 查询动作相应方法
	 * @时间：2011-3-25上午09:49:04
	 */
	public void onQuery(){		
		/**
		 * 满足什么条件的计划呢？人员和仓库已经绑定了   登陆人只能查询他的权限仓库  总仓的人可以安排分仓的
		 * 校验登录人是否为总仓库德人 如果是可以安排任何仓库的  转分仓  计划 
		 * 如果是分仓的人 只能 安排  本分仓内部的  发运计划
		 * 
		 */	
		if(PuPubVO.getString_TrimZeroLenAsNull(ui.getWhid()) == null){
			showWarnMessage("当前登录人未绑定仓库");
			return ;
		}
		getQryDlg().showModal();
		if(!getQryDlg().isCloseOK())
			return;
		clearData();
		SoDealVO[] billdatas = null; 
		try{
			String whereSql = getSQL();
			billdatas = SoDealHealper.doQuery(whereSql,ui.getWhid());
		}catch(Exception e){
			e.printStackTrace();
			showErrorMessage(WdsWlPubTool.getString_NullAsTrimZeroLen(e.getMessage()));
			return;
		}		
		if(billdatas == null||billdatas.length == 0){
            clearData();
			showHintMessage("查询完成：没有满足条件的数据");
			return;
		}
		setDate(billdatas);
	}
	/**
	 * 
	 * @作者：lyf:查询完成设置界面数据
	 * @说明：完达山物流项目 
	 * @时间：2011-11-10下午11:57:53
	 * @param billdatas
	 */
	void setDate(SoDealVO[] billdatas ){
		CircularlyAccessibleValueObject[][]  voss = SplitBillVOs.getSplitVOs(billdatas, SoDeHeaderVo.split_fields);
		if(voss == null || voss.length ==0)
			return ;
		int len = voss.length;
		SoDealBillVO[] billvos = new SoDealBillVO[len];
		SoDealBillVO tmpbill = null;
		SoDeHeaderVo tmpHead = null;
		SoDealVO[] vos = null;
		for(int i=0;i<len;i++){
			vos = (SoDealVO[])voss[i];
			tmpHead = new SoDeHeaderVo();
			tmpHead.setCcustomerid(vos[0].getCcustomerid());
			tmpHead.setDbilldate((UFDate)VOTool.min(vos, "dbilldate"));//应取 最小订单日期
			tmpHead.setBisspecial(UFBoolean.FALSE);
			tmpHead.setCsalecorpid(vos[0].getCsalecorpid());
			tmpHead.setVreceiptcode(vos[0].getVreceiptcode());
			tmpHead.setCbodywarehouseid(ui.getWhid()==null?vos[0].getCbodywarehouseid():ui.getWhid());
			tmpHead.setStatus(VOStatus.NEW);
			tmpbill = new SoDealBillVO();
			tmpbill.setParentVO(tmpHead);
			tmpbill.setChildrenVO(vos);
			billvos[i] = tmpbill;
		}
		//处理查询出的计划  缓存  界面
		getDataPane().setBodyDataVO(WdsWlPubTool.getParentVOFromAggBillVo(billvos, SoDeHeaderVo.class));
		getDataPane().execLoadFormula();
		getBodyDataPane().setBodyDataVO(billvos[0].getChildrenVO());
		getBodyDataPane().execLoadFormula();
		 setDataBuffer(billdatas);		
		showHintMessage("查询完成");
		ui.updateButtonStatus(WdsWlPubConst.DM_PLANDEAL_BTNTAG_DEAL,true);
	}
	/**
	 * 
	 * @作者：lyf
	 * @说明：完达山物流项目 
	 * 获得 对 发运计划的查询条件
	 * wds_sendplanin发运计划主表
	 * wds_sendplanin_b 发运计划子表
	 * @时间：2011-3-25上午09:47:50
	 * @return
	 * @throws Exception
	 */
	private String getSQL() throws Exception{
		StringBuffer whereSql = new StringBuffer();
		whereSql.append(" h.pk_corp='"+ui.cl.getCorp());
		whereSql.append("' and (coalesce(b.nnumber,0) -  coalesce(b."+WdsWlPubConst.DM_SO_DEALNUM_FIELD_NAME+",0)) > 0 ");
		String where = getQryDlg().getWhereSQL();
		if(PuPubVO.getString_TrimZeroLenAsNull(where)!=null){
			whereSql.append(" and "+where);
		}
		whereSql.append(" and h.fstatus ='"+BillStatus.AUDIT+"' and isnull(h.dr,0)=0");//审核通过的
		/**
		 * 
		 * bifreceiptfinish              CHAR(1)             是否发货结束   NULL                
           bifinventoryfinish            CHAR(1)             是否出库结束     
		 * 
		 * 在 销售扩展子表上 存在表体的行状态   没有进行过滤 如果后续需要  应扩展对  以上发货结束的控制 
		 * 
		 */
		return whereSql.toString();
	}
	
	private void setDataBuffer(SoDealVO[] billdatas){
		this.m_billdatas = billdatas;
	}
	/**
	 * 
	 * @throws BusinessException 
	 * @作者：zhf
	 * @说明：完达山物流项目 
	 * 发运计划  安排按钮处理方法
	 * @时间：2011-3-25下午02:59:20
	 */
	public void onDeal() throws BusinessException{
		/**
		 * 数据校验
		 * 过滤掉本次安排数量为0的行  
		 * 本次安排数量不能大于 计划数量-累计安排数量
		 * 将满足的数据传入后台   数据转换   保存    
		 * 
		 * 分单规则：计划号  发货站 收货站  存货   单据日期   安排日期
		 * 
		 */
		AggregatedValueObject[] selectVos = ui.getPanel().getMultiSelectedVOs(SoDealBillVO.class.getName(), SoDeHeaderVo.class.getName(), SoDealVO.class.getName());
		AggregatedValueObject[] newVos = (AggregatedValueObject[])VOUtil.filter(selectVos, new FilterNullBody());
		if(newVos == null || newVos.length == 0){
			showWarnMessage("未选中数据");
			return;
		}
		//表头仓库 可以编辑 ，更新表体仓库
		for(int i=0;i<newVos.length;i++){
			Object cbodywarehouseid =newVos[i].getParentVO().getAttributeValue("cbodywarehouseid");
			CircularlyAccessibleValueObject[] bodys = newVos[i].getChildrenVO();
			if(bodys != null){
				for(CircularlyAccessibleValueObject body:bodys){
					body.setAttributeValue("cbodywarehouseid", cbodywarehouseid);
				}
			}
		}
		//校验只有相同的客户可以合单
		CircularlyAccessibleValueObject[][]  splitVos = SplitBillVOs.getSplitVOs(WdsWlPubTool.getParentVOFromAggBillVo(newVos, SoDeHeaderVo.class), new String[]{"ccustomerid"});
		if(splitVos !=null && splitVos.length>1){
			showErrorMessage("只能安排相同的客户合单");
			return ;
		}
		List<SuperVO> ldata = WdsWlPubTool.filterVOsZeroNum(WdsWlPubTool.getBodysVOFromAggBillVo(newVos, SoDealVO.class),"nnum");
		if(ldata == null||ldata.size() == 0){
			showErrorMessage("选中数据没有安排");
			return;
		}
		//安排  安排前   数据校验:赠品单不能被拆分
		checkIsGiftSpilt((SoDealBillVO[])newVos);
		ArrayList<String> corder_bids = new ArrayList<String>();
		for(int i=0;i<ldata.size();i++){
			String corder_bid = (String) ldata.get(i).getAttributeValue("corder_bid");
			if(corder_bids.contains(corder_bid))
				continue;
			corder_bids.add(corder_bid);
		}
		List<SoDealVO > left = new ArrayList<SoDealVO>();
		for(SoDealVO buf:m_billdatas){
			String key = buf.getCorder_bid();
			if(corder_bids.contains(key))
				continue;
			left.add(buf);
			
		}
		try{
			for(SuperVO vo:ldata){
				((SoDealVO)vo).validataOnDeal();
			}
			SoDealHealper.doDeal(ldata, ui);
		}catch(Exception e){
			e.printStackTrace();
			if(e instanceof ValidationException){
				showErrorMessage(e.getMessage());
				return;
			}
			showErrorMessage(WdsWlPubTool.getString_NullAsTrimZeroLen(e.getMessage()));
			return;
		}
		clearData();
		setDate(left.toArray( new SoDealVO[0]));
		ui.showHintMessage("安排已经完成...");
	}
	
	/**
	 * 
	 * @作者：校验，赠品单是否被拆分
	 * @说明：完达山物流项目 
	 * @时间：2011-12-1下午02:55:46
	 * @param dealBills
	 * @throws BusinessException 
	 */
	private void checkIsGiftSpilt(SoDealBillVO[] dealBills) throws BusinessException{		
		for(SoDealBillVO dealBill:dealBills){
			SoDealVO[] bodys = dealBill.getBodyVos();
			if(bodys == null || bodys.length ==0){
				throw  new BusinessException("表体不能为空");
			}
			//将表体数据按照订单分单
			CircularlyAccessibleValueObject[][] vos = SplitBillVOs.getSplitVOs(bodys, new String[]{"csaleid"});
			if(vos == null || vos.length ==0){
				return;
			}
			//判断赠品是否拆单
			for(int i=0;i<vos.length;i++){
				SoDealVO[] splitBodys =(SoDealVO[])vos[i];
				String csaleid = splitBodys[0].getCsaleid();
				if(csaleid == null || "".equalsIgnoreCase(csaleid)){
					continue;
				}
				int count =0;
				boolean fisgift = false;
				for(SoDealVO dealvo:getDataBuffer()){//跟缓存中的数据比较
					String csaleid2 = dealvo.getCsaleid();
					boolean  blargessflag = PuPubVO.getUFBoolean_NullAs(dealvo.getBlargessflag(), UFBoolean.FALSE).booleanValue();
					if(csaleid.equalsIgnoreCase(csaleid2)){
						count= count+1;
						if(blargessflag){
							fisgift = blargessflag;
						}
					}
				}
				if(fisgift && (splitBodys.length-count)<0){//如果是赠品单，则必须整单安排，不能拆单据
					throw new BusinessException("赠品必须整单安排，不能拆单安排");
				}
			}
		}

	}
	public void bodyRowChange(BillEditEvent e) {		
	}
	
	private void showErrorMessage(String msg){
		ui.showErrorMessage(msg);
	}
	private void showWarnMessage(String msg){
		ui.showWarningMessage(msg);
	}
	private void showHintMessage(String msg){
		ui.showHintMessage(msg);
	}
	
	public Object[] getRelaSortObjectArray() {
		// TODO Auto-generated method stub
		return getDataBuffer();
	}

	public void afterEdit(BillEditEvent e) {
		// TODO Auto-generated method stub
		
	}

}
