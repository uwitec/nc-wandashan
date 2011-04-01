package nc.ui.hg.pu.plan.balance;

import java.awt.Checkbox;
import java.awt.Component;
import java.util.ArrayList;
import java.util.List;

import nc.bd.accperiod.AccountCalendar;
import nc.bd.accperiod.InvalidAccperiodExcetion;
import nc.ui.bd.manage.UIRefCellEditor;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.UICheckBox;
import nc.ui.pub.beans.UIComboBox;
import nc.ui.pub.beans.UILabel;
import nc.ui.pub.beans.UIRadioButton;
import nc.ui.pub.query.QueryConditionClient;
import nc.ui.uapde.dialog.MessageDialog;
import nc.vo.hg.pu.pub.HgPubTool;
import nc.vo.hg.pu.pub.ItemsEnum;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.query.ConditionVO;
import nc.vo.scm.pu.PuPubVO;

// zhf 鹤岗项目   计划处理查询对话框类
public class BalancePlanQueryDlg extends QueryConditionClient {

	// 查询模板新增查询条件
	UIRadioButton m_deal = null;
	UIRadioButton m_undeal = null;
	UICheckBox m_audit = null;
	UICheckBox m_unaudit = null;
	public BalancePlanQueryDlg(){
		super();
		initBox();
		getConditionDatas();
		init();
	}
	private void init(){
		changeQueryModelLayout();
	}
	public void initData() {
		UFDate billdate = ClientEnvironment.getInstance().getDate();
		AccountCalendar calendar = AccountCalendar.getInstance();
		try {
			calendar.setDate(billdate);
		} catch (InvalidAccperiodExcetion e) {
		    MessageDialog.showErrorDlg(e);
		}
		int mon=Integer.parseInt(calendar.getMonthVO().getMonth());
		if(mon<10){
			setDefaultValue("h.cmonth",null,"0"+PuPubVO.getString_TrimZeroLenAsNull(mon));
		}else{
			setDefaultValue("h.cmonth",null,PuPubVO.getString_TrimZeroLenAsNull(mon));
		}
		
		super.initData();
	}
	private void changeQueryModelLayout() {
		if (m_deal != null && m_undeal != null)
			return;

		m_deal = new UIRadioButton();
		m_deal.setBounds(130, 65, 16, 16);
		m_deal.setSelected(true);
		
		UILabel label2 = new UILabel("平衡");
		label2.setBounds(146, 65, 100, 25);

		m_undeal = new UIRadioButton();
		m_undeal.setBounds(130, 95, 16, 16);
		
		UILabel label3 = new UILabel("未平衡");
		label3.setBounds(146, 95, 100, 25);
		
		m_audit = new UICheckBox();
		m_audit.setBounds(130, 125, 16, 16);
		m_deal.setSelected(true);
		
		UILabel label4 = new UILabel("审核");
		label4.setBounds(146, 125, 100, 25);
		
		m_unaudit = new UICheckBox();
		m_unaudit.setBounds(130, 155, 16, 16);
		
		UILabel label5 = new UILabel("未审核");
		label5.setBounds(146, 155, 100, 25);
		

		javax.swing.ButtonGroup buttonGroup = new javax.swing.ButtonGroup();
		buttonGroup.add(m_deal);
		buttonGroup.add(m_undeal);
		buttonGroup.add(m_audit);
		buttonGroup.add(m_unaudit);
		
		getUIPanelNormal().add(label2);
		getUIPanelNormal().add(label3);
		getUIPanelNormal().add(label4);
		getUIPanelNormal().add(label5);
		getUIPanelNormal().add(m_deal);
		getUIPanelNormal().add(m_undeal);
		getUIPanelNormal().add(m_audit);
		getUIPanelNormal().add(m_unaudit);
	}
	public String getWhereSQL() {

		String strWhere = null;
		String str = null;
		ConditionVO[] cons = getConditionVO();
		List<ConditionVO> lcon = new ArrayList<ConditionVO>();
		String[] sss =getComBoxYearValues();;
		for(ConditionVO con:cons){
			if(con.getFieldCode().equalsIgnoreCase("h.cmonth")){
				if(con.getValue().endsWith("-1"))
					continue;
				String a =con.getValue();
				int n=Integer.valueOf(a);
				int slen = sss.length;
				int len = a.length();
				if(slen>10){
					if(len>1){
						String g = a.substring(0,1);
						if("1".equalsIgnoreCase(g)){
							con.setValue(sss[n]);
						}else{
							con.setValue(a);
						}
				     }else{
				    	 con.setValue(sss[n]);
				     }
				}else{
					if(len>1){
							con.setValue(a);
				     }else{
				    	 con.setValue(sss[n]);
				     }
				}
			}
			lcon.add(con);
			//modify by zhw 2011-01-24  根据存货分类查询  按大类查询
			if(con.getFieldCode().equalsIgnoreCase("h.cinvclassid")){
					String pk_invcl = con.getValue();
					String invcode = PuPubVO.getString_TrimZeroLenAsNull(HgPubTool.getInvclasscode(pk_invcl));
					if(invcode !=null){
						str=" and inv.pk_invcl in(select pk_invcl from bd_invcl where invclasscode like '"+ invcode + "%')";
						con.setFieldCode("1");
						con.setValue("1");
					}
			}
		}
				
		cons = lcon.toArray(new ConditionVO[0]);		

		strWhere = getWhereSQL(cons);
		if(PuPubVO.getString_TrimZeroLenAsNull(strWhere)==null)
			strWhere = " (1=1)";
		if(PuPubVO.getString_TrimZeroLenAsNull(str) != null)
		   strWhere = strWhere + str;
		return strWhere;
	}
	
	public String[] getComBoxYearValues(){
		UFDate billdate = ClientEnvironment.getInstance().getDate();
		AccountCalendar calendar = AccountCalendar.getInstance();
		try {
			calendar.setDate(billdate);
		} catch (InvalidAccperiodExcetion e) {
		    MessageDialog.showErrorDlg(e);
		}
		int mon=Integer.parseInt(calendar.getMonthVO().getMonth());
		String[] str= new String[13-mon];
		int len =str.length;
		for(int i=0;i<len;i++){
			int m =mon+i;
			if(m<10){
				str[i]="0"+String.valueOf(mon+i);
			}else{
				str[i]=String.valueOf(mon+i);
			}
			
		}
		return str;
	}
	/**
	 * 出生年增加默认值
	 */
	public void setDefaultComBoxValue(){
			Component o = getComponent("h.cmonth");
			UIComboBox com = null;
			if( o != null && o instanceof UIComboBox){
				com = (UIComboBox)o;
				com.removeAllItems();
				com.addItems(getItemsEnum());
			}
	}
	protected ItemsEnum[] getItemsEnum(){
		ItemsEnum[] ienum = null;
		String[] status = getComBoxYearValues();
		if(status!=null && status.length>0){
			ienum = new ItemsEnum[status.length];
			for(int i = 0 ;i<status.length;i++){
				ienum[i] = new ItemsEnum();
				ienum[i].setIndex(i);
				ienum[i].setName(status[i]);
			}
		}
		return ienum;
	}

	private Component getComponent(String filedcode){
		Object o = getValueRefObjectByFieldCode(filedcode);
		Component jb  = null;
		if(o instanceof UIRefCellEditor){
			jb = ((UIRefCellEditor)o).getComponent();
		}else{
			jb = (Component)o;
		}

		return jb;
	}
	private void initBox(){
		UIComboBox box =  new UIComboBox();
		box.addItems(getItemsEnum());
		setValueRef("h.cmonth", box);
	}
}
