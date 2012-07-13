package nc.ui.dm.db.deal;

import java.util.ArrayList;
import java.util.List;

import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillEditListener;
import nc.ui.pub.bill.BillModel;
import nc.ui.pub.bill.IBillModelRowStateChangeEventListener;
import nc.ui.wl.pub.FilterNullBody;
import nc.ui.zmpub.pub.tool.SingleVOChangeDataUiTool;
import nc.vo.dm.db.deal.DbDeHeaderVo;
import nc.vo.dm.db.deal.DbDealBillVO;
import nc.vo.dm.db.deal.DbDealVO;
import nc.vo.ic.pub.StockInvOnHandVO;
import nc.vo.ic.pub.bill.BillStatus;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.SuperVO;
import nc.vo.pub.VOStatus;
import nc.vo.pub.ValidationException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.scm.pub.vosplit.SplitBillVOs;
import nc.vo.trade.voutils.VOUtil;
import nc.vo.wdsnew.pub.BillStockBO1;
import nc.vo.wl.pub.WdsWlPubConst;
import nc.vo.wl.pub.WdsWlPubTool;

public class DbDealEventHandler implements BillEditListener,nc.ui.pub.bill.IBillRelaSortListener2
{

	
	private DbDealClientUI ui = null;
	private DbDealQryDlg m_qrypanel = null;
	
	public DbDealVO[] curBodys = null;
	//���ݻ���
	private DbDealVO[] m_billdatas = null;
	private String whereSql = null;
	
	private BillStockBO1 stock=null;
	
	public BillStockBO1 getStock(){
		if(stock ==null){
			stock =new BillStockBO1();
		}
		return stock ;
	}
	
	
	UFBoolean getOrderType(){
		if(getQryDlg().m_rbclose.isSelected()){
			return UFBoolean.TRUE;
		}else{
			return UFBoolean.FALSE;
		}
	}
	
	public DbDealEventHandler(DbDealClientUI parent){
		super();
		ui = parent;
		getDataPane().addSortRelaObjectListener2(this);
		
	}
	private BillModel getBodyDataPane(){
		return ui.getPanel().getBodyBillModel();
	}

	private nc.ui.pub.bill.BillModel getDataPane(){
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
		}else if(btnTag.equalsIgnoreCase("�ر�")){
			try {
				onClose();
			} catch (Exception e) {
				e.printStackTrace();
				showErrorMessage(e.getMessage());
			}
		}else if(btnTag.equalsIgnoreCase("��")){
			try {
				onOpen();
			} catch (Exception e) {
				e.printStackTrace();
				showErrorMessage(e.getMessage());
			}
		}
	}
	
	public DbDealVO[] getDataBuffer(){
		return m_billdatas;
	}
	/**
	 * 
	 * @���ߣ�mlr:���ݱ�ͷ���ݺţ����ر���
	 * @˵�������ɽ������Ŀ 
	 * @ʱ�䣺2011-11-10����08:10:37
	 * @param key
	 * @return
	 */
	protected DbDealVO[] getSelectBufferData(String key ){
		if(key == null || "".equalsIgnoreCase(key)){
			return null;
		}
		if(m_billdatas == null || m_billdatas.length ==0){
			return null;
		}
		ArrayList<DbDealVO> list = new ArrayList<DbDealVO>();
		for(DbDealVO dealvo:m_billdatas){
			String vreceiptcode = dealvo.getVcode();
			if(key.equalsIgnoreCase(vreceiptcode)){
				list.add(dealvo);
			}
		}
		curBodys = list.toArray( new DbDealVO[0]);
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
	 * @���ߣ�ȫѡ
	 * @˵�������ɽ������Ŀ 
	 * @ʱ�䣺2011-11-11����11:09:55
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
	private DbDealQryDlg getQryDlg(){
		if(m_qrypanel == null){
			m_qrypanel = new DbDealQryDlg();
			m_qrypanel.setTempletID(ui.cl.getCorp(), WdsWlPubConst.DM_SO_DEAL_NODECODE, ui.cl.getUser(), null);
			m_qrypanel.hideUnitButton();
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
	 * @���ߣ�mlr
	 * @˵�������ɽ������Ŀ 
	 * ��ѯ������Ӧ����
	 * @ʱ�䣺2011-3-25����09:49:04
	 */
	public void onQuery(){		
		/**
		 * ����ʲô�����ļƻ��أ���Ա�Ͳֿ��Ѿ�����   ��½��ֻ�ܲ�ѯ����Ȩ�޲ֿ�  �ֵܲ��˿��԰��ŷֲֵ�
		 * У���¼���Ƿ�Ϊ�ֿܲ���� ����ǿ��԰����κβֿ��  ת�ֲ�  �ƻ� 
		 * ����Ƿֲֵ��� ֻ�� ����  ���ֲ��ڲ���  ���˼ƻ�
		 * 
		 */	
		if(PuPubVO.getString_TrimZeroLenAsNull(ui.getWhid()) == null){
			showWarnMessage("��ǰ��¼��δ�󶨲ֿ�");
			return ;
		}
		getQryDlg().showModal();
		if(!getQryDlg().isCloseOK())
			return;
		clearData();
		DbDealVO[] billdatas = null; 
		try{
			whereSql = getSQL();
			billdatas = DbDealHealper.doQuery(whereSql,ui.getWhid(),getOrderType());
		}catch(Exception e){
			e.printStackTrace();
			showErrorMessage(WdsWlPubTool.getString_NullAsTrimZeroLen(e.getMessage()));
			return;
		}		
		if(billdatas == null||billdatas.length == 0){
            clearData();
			showHintMessage("��ѯ��ɣ�û����������������");
			return;
		}
		try {
			setStock(billdatas);
		} catch (Exception e) {
			e.printStackTrace();
			showErrorMessage("���ÿ�����쳣");
			return ;
		}
		setData(billdatas);
	}
	/**
	 * �����ȴ�����Ϣ
	 * @���ߣ�mlr
	 * @˵�������ɽ������Ŀ 
	 * @ʱ�䣺2012-7-2����01:31:22
	 * @param billdatas
	 * @throws Exception 
	 */
	private void setStock(DbDealVO[] billdatas) throws Exception {
		if(billdatas==null || billdatas.length==0)
			return ;
		for(int i=0;i<billdatas.length;i++){
		    billdatas[i].setVdef1(WdsWlPubConst.WDS_STORSTATE_PK_hg);
		}
		//�����ִ�����ѯ����
		StockInvOnHandVO[] vos=(StockInvOnHandVO[]) SingleVOChangeDataUiTool.runChangeVOAry(billdatas, StockInvOnHandVO.class, "nc.ui.wds.self.changedir.CHGWDS4TOACCOUNTNUM");
		if(vos==null || vos.length==0)
			return;
		//����ִ���
		StockInvOnHandVO[] nvos=(StockInvOnHandVO[]) getStock().queryStockCombinForClient(vos);
		if(nvos==null || nvos.length==0)
			return ;
		for(int i=0;i<billdatas.length;i++){
			if(nvos[i]!=null){		
				UFDouble  uf1=nvos[i].getWhs_stocktonnage();//���������
				billdatas[i].setNstorenumout(uf1);
				UFDouble  uf2=nvos[i].getWhs_stockpieces();//��渨����
				billdatas[i].setAnstorenumout(uf2);
			}
		}
	}

	private void onRefresh() throws Exception{
		DbDealVO[] billdatas = null;
		clearData();
		if(PuPubVO.getString_TrimZeroLenAsNull(whereSql)!=null){

			try{
				billdatas = DbDealHealper.doQuery(whereSql,ui.getWhid(),getOrderType());
			}catch(Exception e){
				e.printStackTrace();
				showErrorMessage(WdsWlPubTool.getString_NullAsTrimZeroLen(e.getMessage()));
				return;
			}		
			if(billdatas == null||billdatas.length == 0){
	            clearData();
				showHintMessage("��ѯ��ɣ�û����������������");
				return;
			}
			setData(billdatas);
		}
		showHintMessage("�������");
	}
	
	/**
	 * 
	 * @���ߣ�lyf:��ѯ������ý�������
	 * @˵�������ɽ������Ŀ 
	 * @ʱ�䣺2011-11-10����11:57:53
	 * @param billdatas
	 */
	void setData(DbDealVO[] billdatas ){
		CircularlyAccessibleValueObject[][]  voss = SplitBillVOs.getSplitVOs(billdatas, DbDeHeaderVo.split_fields);
		if(voss == null || voss.length ==0)
			return ;
		int len = voss.length;
		DbDealBillVO[] billvos = new DbDealBillVO[len];
		DbDealBillVO tmpbill = null;
		DbDeHeaderVo tmpHead = null;
		DbDealVO[] vos = null;
		for(int i=0;i<len;i++){
			vos = (DbDealVO[])voss[i];
			tmpHead = new DbDeHeaderVo();
			tmpHead.setCincorpid(vos[0].getCincorpid());
			tmpHead.setDbilldate(vos[0].getDbilldate());
			tmpHead.setVcode(vos[0].getVcode());
			tmpHead.setStatus(VOStatus.NEW);
			tmpbill = new DbDealBillVO();
			tmpbill.setParentVO(tmpHead);
			tmpbill.setChildrenVO(vos);
			billvos[i] = tmpbill;
		}
		//�����ѯ���ļƻ�  ����  ����
		getDataPane().setBodyDataVO(WdsWlPubTool.getParentVOFromAggBillVo(billvos, DbDeHeaderVo.class));
		getDataPane().execLoadFormula();
		getBodyDataPane().setBodyDataVO(billvos[0].getChildrenVO());
		getBodyDataPane().execLoadFormula();
		 setDataBuffer(billdatas);		
		showHintMessage("��ѯ���");
		ui.updateButtonStatus(WdsWlPubConst.DM_PLANDEAL_BTNTAG_DEAL,true);
	}
	/**
	 * 
	 * @���ߣ�mlr
	 * @˵�������ɽ������Ŀ 
	 * @ʱ�䣺2011-3-25����09:47:50
	 * @return
	 * @throws Exception
	 */
	private String getSQL() throws Exception{
		StringBuffer whereSql = new StringBuffer();
		whereSql.append(" b.coutcorpid='"+ui.cl.getCorp());
		whereSql.append(" h.fstatusflag='"+BillStatus.ADJUST+"'");//����ͨ����
		whereSql.append("' and (coalesce(b.nnum,0) -  coalesce(b."+WdsWlPubConst.DM_DB_DEALNUM_FIELD_NAME+",0)) > 0 ");
		String where = getQryDlg().getWhereSQL();
		if(PuPubVO.getString_TrimZeroLenAsNull(where)!=null){
			whereSql.append(" and "+where);
		}
	//	whereSql.append(" and h.fstatus in('"+BillStatus.AUDIT+"','"+BillStatus.FINISH+"') and isnull(h.dr,0)=0");//���ͨ����
		/**
		 * 
		 * bifreceiptfinish              CHAR(1)             �Ƿ񷢻�����   NULL                
           bifinventoryfinish            CHAR(1)             �Ƿ�������     
		 * 
		 * �� ������չ�ӱ��� ���ڱ������״̬   û�н��й��� ���������Ҫ  Ӧ��չ��  ���Ϸ��������Ŀ��� 
		 * 
		 */
		return whereSql.toString();
	}
	
	private void setDataBuffer(DbDealVO[] billdatas){
		this.m_billdatas = billdatas;
	}
	
	private AggregatedValueObject[] getSelectVos(){
		AggregatedValueObject[] selectVos = ui.getPanel().getMultiSelectedVOs(DbDealBillVO.class.getName(), DbDeHeaderVo.class.getName(), DbDealVO.class.getName());
		AggregatedValueObject[] newVos = (AggregatedValueObject[])VOUtil.filter(selectVos, new FilterNullBody());
		return newVos;
	}
	/**
	 * 
	 * @throws BusinessException 
	 * @���ߣ�zhf
	 * @˵�������ɽ������Ŀ 
	 * ���˼ƻ�  ���Ű�ť������
	 * @ʱ�䣺2011-3-25����02:59:20
	 */
	public void onDeal() throws BusinessException{
		/**
		 * ����У��
		 * ���˵����ΰ�������Ϊ0����  
		 * ���ΰ����������ܴ��� �ƻ�����-�ۼư�������
		 * ����������ݴ����̨   ����ת��   ����    
		 * 
		 * �ֵ����򣺼ƻ���  ����վ �ջ�վ  ���   ��������   ��������
		 * 
		 */
//		AggregatedValueObject[] selectVos = ui.getPanel().getMultiSelectedVOs(DbDealBillVO.class.getName(), DbDeHeaderVo.class.getName(), DbDealVO.class.getName());
		AggregatedValueObject[] newVos = getSelectVos();
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
//		//У��ֻ����ͬ�Ŀͻ����Ժϵ�
//		CircularlyAccessibleValueObject[][]  splitVos = SplitBillVOs.getSplitVOs(WdsWlPubTool.getParentVOFromAggBillVo(newVos, DbDeHeaderVo.class), new String[]{"ccustomerid"});
//		if(splitVos !=null && splitVos.length>1){
//			showErrorMessage("ֻ�ܰ�����ͬ�Ŀͻ��ϵ�");
//			return ;
//		}
		List<SuperVO> ldata = WdsWlPubTool.filterVOsZeroNum(WdsWlPubTool.getBodysVOFromAggBillVo(newVos, DbDealVO.class),"nnum");
		if(ldata == null||ldata.size() == 0){
			showErrorMessage("ѡ������û�а���");
			return;
		}
		//����  ����ǰ   ����У��:��Ʒ�����ܱ����
	//	checkIsGiftSpilt((DbDealBillVO[])newVos);
		try{
			for(SuperVO vo:ldata){
				//((DbDealVO)vo).validataOnDeal();
			}
			DbDealHealper.doDeal(ldata, ui);
			onRefresh();
		}catch(Exception e){
			e.printStackTrace();
			if(e instanceof ValidationException){
				showErrorMessage(e.getMessage());
				return;
			}
			showErrorMessage(WdsWlPubTool.getString_NullAsTrimZeroLen(e.getMessage()));
			return;
		}
	}
	
	/**
	 * 
	 * @���ߣ�У�飬��Ʒ���Ƿ񱻲��
	 * @˵�������ɽ������Ŀ 
	 * @ʱ�䣺2011-12-1����02:55:46
	 * @param dealBills
	 * @throws BusinessException 
	 */
//	private void checkIsGiftSpilt(DbDealBillVO[] dealBills) throws BusinessException{		
//		for(DbDealBillVO dealBill:dealBills){
//			DbDealVO[] bodys = dealBill.getBodyVos();
//			if(bodys == null || bodys.length ==0){
//				throw  new BusinessException("���岻��Ϊ��");
//			}
//			//���������ݰ��ն����ֵ�
//			CircularlyAccessibleValueObject[][] vos = SplitBillVOs.getSplitVOs(bodys, new String[]{"csaleid"});
//			if(vos == null || vos.length ==0){
//				return;
//			}
//			//�ж���Ʒ�Ƿ��
//			for(int i=0;i<vos.length;i++){
//				DbDealVO[] splitBodys =(DbDealVO[])vos[i];
//			//	String csaleid = splitBodys[0].getCsaleid();
////				if(csaleid == null || "".equalsIgnoreCase(csaleid)){
////					continue;
////				}
//				int count =0;
//				boolean fisgift = false;
////				for(DbDealVO dealvo:getDataBuffer()){//�������е����ݱȽ�
////					String csaleid2 = dealvo.getCsaleid();
////					boolean  blargessflag = PuPubVO.getUFBoolean_NullAs(dealvo.getBlargessflag(), UFBoolean.FALSE).booleanValue();
////					if(csaleid.equalsIgnoreCase(csaleid2)){
////						count= count+1;
////						if(blargessflag){
////							fisgift = blargessflag;
////						}
////					}
////				}
//				if(fisgift && (splitBodys.length-count)<0){//�������Ʒ����������������ţ����ܲ𵥾�
//					throw new BusinessException("��Ʒ�����������ţ����ܲ𵥰���");
//				}
//			}
//		}
//
//	}
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
	
	public Object[] getRelaDbrtObjectArray() {
		// TODO Auto-generated method stub
		return getDataBuffer();
	}

	public void afterEdit(BillEditEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	private void doCloseOrOpen(UFBoolean bclose) throws Exception {
		AggregatedValueObject[] newVos = getSelectVos();
		if(newVos == null || newVos.length == 0){
			showWarnMessage("δѡ������");
			return;
		}
		DbDealBillVO[] orders = (DbDealBillVO[])newVos;
		DbDealVO[] bodys = null;
		List<String> lhid = new ArrayList<String>();
		String key = null;
		for(DbDealBillVO order:orders){
			bodys = order.getBodyVos();
			for(DbDealVO body:bodys){
			//	key = PuPubVO.getString_TrimZeroLenAsNull(body.getCsaleid());
				if(key == null)
					continue;
				if(lhid.contains(key))
					continue;
				lhid.add(key);				
			}
		}

		if(lhid.size()<=0)
			return;

		//		Զ�̵���
		DbDealHealper.doCloseOrOpen(lhid.toArray(new String[0]), ui, bclose);

		//		ˢ�½�������
		onRefresh();

	}
	
	private void onClose()throws Exception{
		doCloseOrOpen(UFBoolean.TRUE);
	}
    private void onOpen()throws Exception{
    	doCloseOrOpen(UFBoolean.FALSE);
    }


	public boolean beforeEdit(BillEditEvent e) {
		return false;
	}


	public Object[] getRelaSortObjectArray() {
		return getDataBuffer();
	}
}
