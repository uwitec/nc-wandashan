package nc.ui.wds.ic.pub;

import java.awt.Component;
import java.awt.Container;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.table.TableColumn;

import nc.bs.logging.Logger;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.beans.UITabbedPane;
import nc.ui.pub.bill.BillCellEditor;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillItem;
import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.wds.ic.other.out.BillField;
import nc.ui.wl.pub.MutiChildForOutInUI;
import nc.ui.wl.pub.WdsBillManagUI;
import nc.vo.bd.invdoc.InvmandocVO;
import nc.vo.ic.other.out.MyBillVO;
import nc.vo.ic.other.out.TbOutgeneralBVO;
import nc.vo.ic.other.out.TbOutgeneralTVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.trade.field.IBillField;
import nc.vo.wds.ic.cargtray.SmallTrayVO;

public class OutPubClientUI extends MutiChildForOutInUI {
	
	/**
	 * 
	 */

	private static final long serialVersionUID = 1L;
	private Map<String,List<TbOutgeneralTVO>> trayInfor = null;//������  �������µ� buffer �к�+������ˮ����Ϣ
	public void setTrayInfor(Map<String,List<TbOutgeneralTVO>> trayInfor2){
		trayInfor = trayInfor2;
	}
	public Map<String,List<TbOutgeneralTVO>> getTrayInfor(){
		if(trayInfor == null)
			trayInfor = new HashMap<String, List<TbOutgeneralTVO>>();
		return trayInfor;
	}
	private Map<String,SmallTrayVO[]> lockTrayInfor = null;//�������̰󶨵�ʵ��������Ϣ zhf add
	public Map<String,SmallTrayVO[]> getLockTrayInfor(){
		return lockTrayInfor;
	}
	public void setLockTrayInfor(Map<String,SmallTrayVO[]>  newInfor){
		lockTrayInfor = newInfor;
	}
	public OutPubClientUI(){
		super();
		initlize();
	}
	public OutPubClientUI(String pk_corp, String pk_billType, String pk_busitype,
			String operater, String billId) {
		super(pk_corp, pk_billType, pk_busitype, operater, billId);
		initlize();
	}
	/**
	 * ��ʼ��
	 */
	private void initlize() {
		getBufferData().addObserver(new Observer() {
			public void update(Observable o, Object arg) {
				if (!getBufferData().isVOBufferEmpty()){
					int row = getBufferData().getCurrentRow();
					if(row < 0){
						return;
					}
					//���»���
					AggregatedValueObject obj = getBufferData().getCurrentVO();
					if(obj != null){
						AggregatedValueObject billvo = getBufferData().getCurrentVO();
						TbOutgeneralBVO[] bvo = (TbOutgeneralBVO[])billvo.getChildrenVO();
						setList(bvo);
					}
				}
			}		
		});
	}
	
	protected void setTotalUIState(int intOpType) throws Exception {
		super.setTotalUIState(intOpType);
		switch (intOpType) {
			case OP_ADD: 
			case OP_REFADD: {
				HashMap<String, List<TbOutgeneralTVO>> map = 
					new HashMap<String, List<TbOutgeneralTVO>>();
				setTrayInfor(map);
			}
		}
	}
	@Override
	protected void setBillNo() throws Exception {
	
	}
	
	public void setList(TbOutgeneralBVO[] bvo){
		Map<String,List<TbOutgeneralTVO>> m = new HashMap<String,List<TbOutgeneralTVO>>();
		if(bvo!=null && bvo.length>0){
			for(TbOutgeneralBVO b : bvo){
				String crowno = b.getCrowno();//�к�
				m.put(crowno, b.getTrayInfor());
			}
		}
		setTrayInfor(m);
	}

	@Override
	protected AbstractManageController createController() {
		// TODO Auto-generated method stub
		return null;
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
	protected void initSelfData() {
		// TODO Auto-generated method stub

	}

	@Override
	public void setDefaultData() throws Exception {
		// TODO Auto-generated method stub

	}
	/**
	 * ��ý���仯����VO�� �������ڣ�(2004-1-7 10:01:01)
	 *
	 * @return nc.vo.pub.AggregatedValueObject
	 * @exception java.lang.Exception
	 *                �쳣˵����
	 */
	public AggregatedValueObject getChangedVOFromUI()
	throws java.lang.Exception {
		MyBillVO billvo = (MyBillVO)this.getBillCardWrapper().getChangedVOFromUI();
		MyBillVO billvo2 = (MyBillVO)this.getBillCardWrapper().getBillVOFromUI();
		if(getBillOperate() == IBillOperate.OP_ADD)
			billvo = billvo2;
		TbOutgeneralBVO[] bodys = (TbOutgeneralBVO[])billvo.getChildrenVO();

		if(bodys == null || bodys.length==0)
			return billvo;
		if(trayInfor == null)
			return billvo;
		String key = null;
		for(TbOutgeneralBVO body:bodys){
			key = body.getCrowno();
			if(trayInfor.containsKey(key)){
				body.setTrayInfor(trayInfor.get(key));
			}
		}
		billvo.setOUserObj(getLockTrayInfor());//�����������̽������Ϣ
		return billvo;
	}
	
	@Override
	protected IBillField createBillField() {
		// TODO Auto-generated method stub
		return new BillField();
	}
	
//	public void afterEdit(nc.ui.pub.bill.BillEditEvent e) {
//		if(e.getPos() == BillItem.BODY){
//			String key = e.getKey();
//			int row = e.getRow();
//			if("nshouldoutnum".equalsIgnoreCase(key)||"nshouldoutassistnum".equalsIgnoreCase(key)){
//				getBillListPanel().getBodyBillModel().setValueAt(null, row, "noutnum");
//				getBillListPanel().getBodyBillModel().setValueAt(null, row, "noutassistnum");
//			}
//		}
//		super.afterEdit(e);
//	}
	@Override
	public void afterEdit(BillEditEvent e) {
		try{
			String key = e.getKey();
			int row = e.getRow();
			Object value = e.getValue();
			if(e.getPos() == BillItem.HEAD){
				if("pk_cargdoc".equals(key)){//��λ
					UIRefPane u = (UIRefPane)e.getSource();
					afterHeadCargDoc(u.getRefPK());				
				}
			}else{
				if("cshengchanriqi".equals(key)){//��������
					String cinventoryid = (String)getBillCardPanel().getBodyValueAt(row, "cinventoryid");
					if(value == null || "".equals(value)){
						getBillCardPanel().setBodyValueAt(null, row, "cshixiaoriqi");//ʧЧ����
					}else{
						if(cinventoryid == null) 
							return;
						InvmandocVO vo = (InvmandocVO)HYPubBO_Client.queryByPrimaryKey(InvmandocVO.class, cinventoryid);
						UFDate date = new UFDate(value.toString());
						Integer num = vo.getQualitydaynum();//����������
						UFBoolean b = vo.getQualitymanflag();//�Ƿ�����
						if(b!=null && b.booleanValue()){
							getBillCardPanel().setBodyValueAt(date.getDateAfter(num), row, "cshixiaoriqi");//ʧЧ����
						}
					}
				}else if("cshixiaoriqi".equals(key)){//ʧЧ����
					String cinventoryid = (String)getBillCardPanel().getBodyValueAt(row, "cinventoryid");
					if(value == null || "".equals(value)){
						getBillCardPanel().setBodyValueAt(null, row, "cshengchanriqi");//��������
					}else{
						if(cinventoryid == null) 
							return;
						InvmandocVO vo = (InvmandocVO)HYPubBO_Client.queryByPrimaryKey(InvmandocVO.class, cinventoryid);
						UFDate date = new UFDate(value.toString());
						Integer num = vo.getQualitydaynum();//����������
						UFBoolean b = vo.getQualitymanflag();//�Ƿ�����
						if(b!=null && b.booleanValue()){
							getBillCardPanel().setBodyValueAt(date.getDateBefore(num), row, "cshengchanriqi");//��������
						}
					}
				}
			}
			
			if("vbatchcode".equalsIgnoreCase(key)){			
				// ��֤���κ��Ƿ���ȷ
				String va=(String) e.getValue();
				if(va==null ||va.equalsIgnoreCase("")){
					this.showErrorMessage("���κŲ���Ϊ��");
					return;
				}
				if (va.trim().length() < 8) {
					this.showErrorMessage("���κŲ���С��8λ");
				     return ;
				}
		
				Pattern p = Pattern
				.compile(
						"^((((1[6-9]|[2-9]\\d)\\d{2})(0?[13578]|1[02])(0?[1-9]|[12]\\d|3[01]))|"
						+ "(((1[6-9]|[2-9]\\d)\\d{2})(0?[13456789]|1[012])(0?[1-9]|[12]\\d|30))|"
						+ "(((1[6-9]|[2-9]\\d)\\d{2})0?2(0?[1-9]|1\\d|2[0-8]))|"
						+ "(((1[6-9]|[2-9]\\d)(0[48]|[2468][048]|[13579][26])|((16|[2468][048]|[3579][26])00))0?229))$",
						Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
				Matcher m = p.matcher(va.trim().substring(0, 8));
				if (!m.find()) {
					this.showErrorMessage(
					"���κ�����Ĳ���ȷ,����������ȷ������!�磺20100101XXXXXX");
					return;
		
				}
			    //������κ������ʽ��ȷ�͸��������ڸ�ֵ
				String year=va.substring(0,4);
				String month=va.substring(4,6);
				String day=va.substring(6,8);
				String startdate=year+"-"+month+"-"+day;
				UFDate date=new UFDate(startdate);
				getBillCardPanel().setBodyValueAt(date, row, "cshengchanriqi");
				
				//��ʧЧ�����ڸ�ֵ
				String cinventoryid = (String)getBillCardPanel().getBodyValueAt(row, "cinventoryid");		
					if(cinventoryid == null) 
						return;
					InvmandocVO vo = (InvmandocVO)HYPubBO_Client.queryByPrimaryKey(InvmandocVO.class, cinventoryid);
					Integer num = vo.getQualitydaynum();//����������
					UFBoolean b = vo.getQualitymanflag();//�Ƿ�����
					if(b!=null && b.booleanValue()){
						getBillCardPanel().setBodyValueAt(date.getDateAfter(num), row, "cshixiaoriqi");//ʧЧ����
					}			
			}	
				
			super.afterEdit(e);
		}catch(Exception e1){
			Logger.info(e1);
		}
	}
	
	public void afterHeadCargDoc(Object pk_cargdoc){
		//�������¸�ֵ[��λ]
		int row = getBillCardPanel().getBillTable().getRowCount();
		if(row < 0 ){
			return;
		}
		for(int i  = 0 ;i<row; i++){
			getBillCardPanel().getBillModel().setValueAt(pk_cargdoc, i, "cspaceid");//��λ
		}
		//�����ǰ����
		getTrayInfor().clear();
		getBillCardPanel().getBillModel().execLoadFormula();
	}
	/**
	 * @author yf
	 * @˵�� ���ݱ��� tableCode,���ҳǩ����
	 * @ʱ�� 2011-04-29����02:06:02
	 * @param tableCodes
	 */
	protected void clearTable(String[] tableCodes) {
		if (tableCodes != null && tableCodes.length > 0) {
			for (int i = 0; i < tableCodes.length; i++) {
				int count = getBillCardPanel().getBillModel(tableCodes[i])
						.getRowCount();
				int[] array = new int[count];
				for (int j = 0; j < count; j++) {
					array[j] = j;
				}
				getBillCardPanel().getBillData().getBillModel(tableCodes[i])
						.delLine(array);
			}
		}
	}
	protected nc.ui.pub.beans.UITabbedPane getUITabbedPane(Component c) {
		if (c instanceof UITabbedPane)
			return (UITabbedPane) c;
		if (c instanceof Container) {
			Component[] comps = ((Container) c).getComponents();
			for (int i = 0; i < comps.length; i++) {
				Component cc = getUITabbedPane(comps[i]);
				if (cc instanceof UITabbedPane)
					return (UITabbedPane) cc;
			}
		}
		return null;
	}
//	private void setUITimeTextField(String tablecode,String cellcode){
//		 TableColumn tablecol = null;
//	     BillItem dsendtime = getBillCardPanel().getBodyItem(tablecode,cellcode);
//	     if (null != dsendtime) {
//	       // ����ʱ��༭��
//	       try {
//	         //�ƻ�����ʱ��
//	         tablecol = getBillCardPanel().getBodyPanel(tablecode).getTable().getColumn(dsendtime.getName());
//	         if (null != tablecol) {
//	           BillCellEditor timecelledit = new BillCellEditor(new nc.ui.scm.pattern.pub.UITimeTextField());
//	           tablecol.setCellEditor(timecelledit);
//	         }
//	       } catch (Exception e) {
//	    	   nc.vo.scm.pub.SCMEnv.out(e);
//	       }
//	     }
//	}

	
//	public boolean onClosing() {
//		boolean flag = super.onClosing();
//		MakeBiddingHelper.clear();//���� ��̬����
//		return flag;
//	}
}
