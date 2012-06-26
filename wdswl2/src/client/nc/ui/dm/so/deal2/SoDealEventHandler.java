package nc.ui.dm.so.deal2;

import java.util.ArrayList;
import java.util.List;

import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.bill.BillModel;
import nc.ui.pub.bill.BillStatus;
import nc.ui.pub.bill.IBillModelRowStateChangeEventListener;
import nc.ui.wl.pub.FilterNullBody;
import nc.vo.dm.so.deal2.SoDealBillVO;
import nc.vo.dm.so.deal2.SoDealHeaderVo;
import nc.vo.dm.so.deal2.SoDealVO;
import nc.vo.dm.so.deal2.StoreInvNumVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.scm.pub.vosplit.SplitBillVOs;
import nc.vo.trade.voutils.IFilter;
import nc.vo.trade.voutils.VOUtil;
import nc.vo.wl.pub.WdsWlPubConst;
import nc.vo.wl.pub.WdsWlPubTool;

public class SoDealEventHandler{

	private SoDealClientUI ui = null;
	private SoDealQryDlg m_qrypanel = null;	
	private SoDealBillVO[] m_buffer = null;
	private SoDealVO[] m_billdatas = null;
	
	public SoDealEventHandler(SoDealClientUI parent){
		super();
		ui = parent;		
	}

	private BillModel getDataPane(){
		return getHeadDataPane();
	}
	
	private BillModel getHeadDataPane(){
		return ui.getPanel().getHeadBillModel();
	}
	
	private BillModel getBodyDataPane(){
		return ui.getPanel().getBodyBillModel();
	}

	public void onButtonClicked(String btnTag){
		try {
			if(btnTag.equalsIgnoreCase(WdsWlPubConst.DM_PLANDEAL_BTNTAG_DEAL)){
				onDeal();
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

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ui.showErrorMessage(WdsWlPubTool.getString_NullAsTrimZeroLen(e.getMessage()));
		}
	}
	
	public SoDealBillVO[] getDataBuffer(){
		return m_buffer;
	}
	public SoDealVO[] getDataBufferDetail(){
		return m_billdatas;
	}
	
	public void onNoSel(){
		int rowcount = getDataPane().getRowCount();
		if(rowcount <= 0)
			return;
		//yf add
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
			//parent, normalPnl, pk_corp, moduleCode, operator, busiType
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
			//			m_qrypanel.setValueRef("h.pk_corp", new UIRefPane("��˾Ŀ¼"));
			//			m_qrypanel.changeValueRef("h.pk_corp", new UIRefPane("��˾Ŀ¼"));
		}
		return m_qrypanel;
	}
	
	private void clearData(){
		m_buffer = null;
		getDataPane().clearBodyData();
		getBodyDataPane().clearBodyData();
	}
	
	private String whereSql;//�����ϴβ�ѯ����  zhf add
	
	/**
	 * 
	 * @���ߣ�lyf
	 * @˵�������ɽ������Ŀ 
	 * ��ѯ������Ӧ����
	 * @ʱ�䣺2011-3-25����09:49:04
	 */
	public void onQuery() throws Exception{		
		/**
		 * ����ʲô�����ļƻ��أ���Ա�Ͳֿ��Ѿ�����   ��½��ֻ�ܲ�ѯ����Ȩ�޲ֿ�  �ֵܲ��˿��԰��ŷֲֵ�
		 * У���¼���Ƿ�Ϊ�ֿܲ���� ����ǿ��԰����κβֿ��  ת�ֲ�  �ƻ� 
		 * ����Ƿֲֵ��� ֻ�� ����  ���ֲ��ڲ���  ���˼ƻ�
		 * �ֲֿ��̰�
		 */	
		if(PuPubVO.getString_TrimZeroLenAsNull(ui.getWhid()) == null){
			showWarnMessage("��ǰ��¼��δ�󶨲ֿ�");
			return ;
		}
		getQryDlg().showModal();
		if(!getQryDlg().isCloseOK())
			return;
		whereSql = getSQL();
		m_billdatas = SoDealHealper.doQuery(whereSql,ui.getWhid());
		if(m_billdatas == null||m_billdatas.length == 0){
			clearData();
			showHintMessage("��ѯ��ɣ�û����������������");
			return;
		}
		//�����ݽ��кϲ�  ���ͻ��ϲ�  ��������ȡ��С��������
		SoDealBillVO[] billvos = SoDealHealper.combinDatas(ui.getWhid(),m_billdatas);
		clearData();
		//������ѯ���ļƻ�  ����  ����
		getDataPane().setBodyDataVO(WdsWlPubTool.getParentVOFromAggBillVo(billvos, SoDealHeaderVo.class));
		getDataPane().execLoadFormula();
		getBodyDataPane().setBodyDataVO(billvos[0].getChildrenVO());
		getBodyDataPane().execLoadFormula();
		//		billdatas = (SoDealVO[])getDataPane().getBodyValueVOs(SoDealVO.class.getName());
		setDataBuffer(billvos);		
		showHintMessage("��ѯ���");
		ui.updateButtonStatus(WdsWlPubConst.DM_PLANDEAL_BTNTAG_DEAL,true);
	}
	
	private void onRefresh() throws Exception{
		SoDealVO[] billdatas = null;
		clearData();
		if(PuPubVO.getString_TrimZeroLenAsNull(whereSql)!=null){

			billdatas = SoDealHealper.doQuery(whereSql,ui.getWhid());
			if(billdatas == null||billdatas.length == 0){
				return;
			}
			//�����ݽ��кϲ�  ���ͻ��ϲ�  ��������ȡ��С��������
			SoDealBillVO[] billvos = SoDealHealper.combinDatas(ui.getWhid(),billdatas);
			//������ѯ���ļƻ�  ����  ����
			getDataPane().setBodyDataVO(WdsWlPubTool.getParentVOFromAggBillVo(billvos, SoDealHeaderVo.class));
			getDataPane().execLoadFormula();
			getBodyDataPane().setBodyDataVO(billvos[0].getChildrenVO());
			getBodyDataPane().execLoadFormula();
			//			billdatas = (SoDealVO[])getDataPane().getBodyValueVOs(SoDealVO.class.getName());
			setDataBuffer(billvos);	
			ui.updateButtonStatus(WdsWlPubConst.DM_PLANDEAL_BTNTAG_DEAL,true);
		}
		showHintMessage("�������");
	}
	
	/**
	 * 
	 * @���ߣ�lyf
	 * @˵�������ɽ������Ŀ 
	 * ��� �� ���˼ƻ��Ĳ�ѯ����
	 * wds_sendplanin���˼ƻ�����
	 * wds_sendplanin_b ���˼ƻ��ӱ�
	 * @ʱ�䣺2011-3-25����09:47:50
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
		whereSql.append(" and h.fstatus in('"+BillStatus.AUDIT+"','"+BillStatus.FINISH+"') and isnull(h.dr,0)=0");//���ͨ����
	/**
	 * �����ֿܲ���   ����  �ֲֵļƻ� �������Ϊ  �� ��ѯ������  ����  �ֿ��ѡ��
	 * ����Ƿֲֵ�¼  ���������ɱ༭Ĭ��Ϊ ��¼�ֿ�
	 * ������ֵܲ�¼  ������Ĭ��Ϊ�ܲ�  ����ѡ��ֲ�-----------��δʵ��
	 */
	return whereSql.toString();
	}
	
	private void setDataBuffer(SoDealBillVO[] billvos){
		this.m_buffer = billvos;
	}
//	/**
//	 * 
//	 * @���ߣ�mlr
//	 * @˵�������ɽ������Ŀ ���˼ƻ� ���ⰲ�Ű�ť��������
//	 * @ʱ�䣺2011-3-25����02:59:20
//	 */
//	public void onXNDeal() {
//		if (lseldata == null || lseldata.size() == 0) {
//			showWarnMessage("��ѡ��Ҫ����������");
//			return;
//		}
//		XnApDLG  tdpDlg = new XnApDLG(WdsWlPubConst.XNAP,  ui.getCl().getUser(),
//				ui.getCl().getCorp(), ui, lseldata);
//		if(tdpDlg.showModal()== UIDialog.ID_OK){}
//		// nc.ui.pub.print.IDataSource dataSource = new DealDataSource(
//		// ui.getBillListPanel(), WdsWlPubConst.DM_PLAN_DEAL_NODECODE);
//		// nc.ui.pub.print.PrintEntry print = new
//		// nc.ui.pub.print.PrintEntry(null,
//		// dataSource);
//		// print.setTemplateID(ui.getEviment().getCorporation().getPk_corp(),WdsWlPubConst.DM_PLAN_DEAL_NODECODE,ui.getEviment().getUser().getPrimaryKey(),
//		// null, null);
//		// if (print.selectTemplate() == 1)
//		// print.preview();
//
//	}
	
//	class filterNullBody implements IFilter{
//
//	public boolean accept(Object o) {
//		// TODO Auto-generated method stub
//		if(!(o instanceof AggregatedValueObject))
//			return true;
//		AggregatedValueObject bill = (AggregatedValueObject)o;
//		if(bill == null)
//			return false;
//		if(bill.getChildrenVO() == null || bill.getChildrenVO().length == 0)
//			return false;
//		return true;
//	}
		
//	}
	/**
	 * 
	 * @���ߣ�zhf
	 * @˵�������ɽ������Ŀ 
	 * ���˼ƻ�  ���Ű�ť��������
	 * @ʱ�䣺2011-3-25����02:59:20
	 */
	@SuppressWarnings("unchecked")
	public void onDeal() throws Exception{	
		/**
		 * �����Ƿ����ⰲ��  ������С������   ���ǿ���ִ����Ƿ�����   ֱ�Ӱ���   �ֹ����Ž���
		 * ������־
		 */
		AggregatedValueObject[] selectVos = ui.getPanel().getMultiSelectedVOs(SoDealBillVO.class.getName(), SoDealHeaderVo.class.getName(), SoDealVO.class.getName());
		AggregatedValueObject[] newVos = (AggregatedValueObject[])VOUtil.filter(selectVos, new FilterNullBody());
		if(newVos == null || newVos.length == 0){
			showWarnMessage("δѡ������");
			return;
		}
		//��ͷ�ֿ� ���Ա༭ �����±���ֿ�
		for(int i=0;i<newVos.length;i++){
			Object cbodywarehouseid =newVos[i].getParentVO().getAttributeValue("cbodywarehouseid");
			CircularlyAccessibleValueObject[] bodys = newVos[i].getChildrenVO();
			if(bodys != null){
				for(CircularlyAccessibleValueObject body:bodys){
					body.setAttributeValue("cbodywarehouseid", cbodywarehouseid);
				}
			}
		}
		//�����ݽ���һ������У��
		SoDealBillVO[] dealBills = (SoDealBillVO[])newVos;
		SoDealBillVO.checkData(dealBills);
		//����  ����ǰ   ����У��:��Ʒ�����ܱ����
		checkIsGiftSpilt(dealBills);
		// **SoDealHealper.doDeal((SoDealBillVO[])newVos, ui)����ֵ��
		// 1.null:���пͻ��ķ�������δ�ﵽ��С������
		// 2.Object[] { isauto, null, null ,reasons}:���пͻ��������ŵĴ���У�����������������Ĵ��
		// 3.Object[] { isauto, lcust, lnum,reasons}������Ҫ�ֶ����ŵĿͻ�
		Object o = SoDealHealper.doDeal(dealBills, ui);
		boolean flag = false;
		UFBoolean isauto = UFBoolean.FALSE;
		if(o != null){
			Object[] os = (Object[])o;
			if(os == null || os.length == 0)
				return;
			//�Ƿ���һ���ֿͻ��������Զ�����
			isauto = PuPubVO.getUFBoolean_NullAs(os[0], UFBoolean.FALSE);
			//δ���ŵĿͻ���Ϣ
			List<SoDealBillVO> lcust = (List<SoDealBillVO>)os[1];		
			//����������������
			List<StoreInvNumVO> lnum = (List<StoreInvNumVO>)os[2];
			//���β��ܰ��ŵĿͻ�ԭ��
			List<String> reasons = (List<String>)os[3];
			//��վֱ�Ӱ��ŵĿͻ�
			List<String> reasons2 = (List<String>)os[4];
			if(lcust!=null && lcust.size()>0){
				flag = doHandDeal(lcust, lnum);
			}
			StringBuffer bur = new StringBuffer();
			if(reasons2 != null && reasons2.size() >0){
				bur.append("����ֱ�Ӱ��ŵĿͻ�:\n");
				for(int i=0;i<reasons2.size();i++){
					bur.append("**");
					String reason = reasons2.get(i);
					bur.append(reason+"\n");
				}
			}
			if(reasons != null && reasons.size() >0){
				bur.append("���β��ܽ��а��ŵĿͻ�:\n");
				for(int i=0;i<reasons.size();i++){
					bur.append("**");
					String reason = reasons.get(i);
					bur.append(reason+"\n");
				}
			}
			if(bur.toString().length()>0){
				showWarnMessage(bur.toString());
			}
		}else{
			showWarnMessage("���ΰ��ŵ����пͻ���δ�ﵽ��С������");
		}
		onRefresh();
		ui.showHintMessage("���ΰ��Ž���");
	}
	/**
	 * 
	 * @���ߣ�У�飬��Ʒ���Ƿ񱻲��
	 * @˵�������ɽ������Ŀ 
	 * @ʱ�䣺2011-12-1����02:55:46
	 * @param dealBills
	 * @throws BusinessException 
	 */
	private void checkIsGiftSpilt(SoDealBillVO[] dealBills) throws BusinessException{		
		for(SoDealBillVO dealBill:dealBills){
			SoDealVO[] bodys = dealBill.getBodyVos();
			if(bodys == null || bodys.length ==0){
				throw  new BusinessException("���岻��Ϊ��");
			}
			//���������ݰ��ն����ֵ�
			CircularlyAccessibleValueObject[][] vos = SplitBillVOs.getSplitVOs(bodys, new String[]{"csaleid"});
			if(vos == null || vos.length ==0){
				return;
			}
			//�ж���Ʒ�Ƿ��
			for(int i=0;i<vos.length;i++){
				SoDealVO[] splitBodys =(SoDealVO[])vos[i];
				String csaleid = splitBodys[0].getCsaleid();
				if(csaleid == null || "".equalsIgnoreCase(csaleid)){
					continue;
				}
				int count =0;
				boolean fisgift = false;
				for(SoDealVO dealvo:getDataBufferDetail()){//�������е����ݱȽ�
					String csaleid2 = dealvo.getCsaleid();
					boolean  blargessflag = PuPubVO.getUFBoolean_NullAs(dealvo.getBlargessflag(), UFBoolean.FALSE).booleanValue();
					if(csaleid.equalsIgnoreCase(csaleid2)){
						count= count+1;
						if(blargessflag){
							fisgift = blargessflag;
						}
					}
				}
				if(fisgift && (splitBodys.length-count)<0){//�������Ʒ����������������ţ����ܲ𵥾�
					throw new BusinessException("��Ʒ�����������ţ����ܲ𵥰���");
				}
			}
		}

	}

	/**
	 * 
	 * @���ߣ�lyf
	 * @˵�������ɽ������Ŀ  �ֹ�����
	 * @ʱ�䣺2011-7-11����03:08:02
	 * @param lcust
	 * @param lnum
	 * @throws Exception
	 */
	private boolean doHandDeal(List<SoDealBillVO> lcust,List<StoreInvNumVO> lnum) throws Exception{
		//1.�Կͻ����������� �Զ� ����  ������
		SoDealHealper.autoDealNum(lcust, lnum);	
		//2.�����ֹ����Ž���  ���û� �ֹ�����  ���������Ʒ
		getHandDealDlg().setLcust(lcust);
		getHandDealDlg().setLnum(lnum);
		getHandDealDlg().getDataPanel().setDataToUI();
		int retFlag = getHandDealDlg().showModal();		
		if(retFlag != UIDialog.ID_OK){
			return false;
		}
		//�����ֹ�������Ϣ  �����˵�
		SoDealVO[] bodys = null;
		List<SoDealBillVO> lcust2 = getHandDealDlg().getBuffer().getLcust();
		if(lcust2 == null || lcust2.size() == 0)
			return false;
		List<SoDealVO> ldeal = new ArrayList<SoDealVO>();
		for(SoDealBillVO cust:lcust){
			bodys = cust.getBodyVos();
			SoDealVO[] newBodys = (SoDealVO[])VOUtil.filter(bodys, new FilterNullNum("nnum"));
			if(newBodys == null || newBodys.length==0){
				continue;
			}
			for(SoDealVO body:newBodys){
				body.validataOnDeal();
				ldeal.add(body);
			}
		}
		if(ldeal.size() <= 0)
			return false;		
//		������С������   �ֹ����ŵ�  ��������С������   ֧�� �˹� ���
//		�����㰲�����ô��    
//		VOUtil.filter(ldeal, iFilter);
		SoDealHealper.doHandDeal(ldeal, ui);
		return true;
	}
	public class FilterNullNum implements IFilter{
		private String para;
		FilterNullNum(String column){
			this.para = column;
		}
		public boolean accept(Object obj) {
			if( obj instanceof SuperVO){
				SuperVO vo = (SuperVO)obj;
				UFDouble value = PuPubVO.getUFDouble_NullAsZero(vo.getAttributeValue(para));
				if(value == null || value.doubleValue() <= 0){
					return false;
				}
				return true;
			}
			return false;
		}
		
	}
	
	private HandDealDLG m_handDlg = null;
	private HandDealDLG getHandDealDlg(){
		if(m_handDlg == null){
			m_handDlg = new HandDealDLG(ui);
		}
		return m_handDlg;
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
}