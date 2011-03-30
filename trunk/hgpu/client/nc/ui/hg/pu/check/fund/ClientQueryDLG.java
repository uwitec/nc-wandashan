package nc.ui.hg.pu.check.fund;

import java.awt.Component;
import java.awt.Container;

import nc.ui.bd.manage.UIRefCellEditor;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.UIComboBox;
import nc.ui.pub.beans.UIPanel;
import nc.ui.trade.query.HYQueryDLG;
import nc.vo.hg.pu.pub.ItemsEnum;
import nc.vo.pub.lang.UFDate;
import nc.vo.scm.pu.PuPubVO;


public class ClientQueryDLG extends HYQueryDLG{
	
	private static final long serialVersionUID = 9153238819070518045L;
	
	public ClientQueryDLG(Container parent, UIPanel normalPnl, String pk_corp,
			String moduleCode, String operator, String busiType, String nodeKey) {
		super(parent, normalPnl, pk_corp, moduleCode, operator, busiType, nodeKey);
//		initBox();
	}
	UFDate billdate = ClientEnvironment.getInstance().getDate();
	@Override
	public void initData() {
		setDefaultValue("hg_fundset.dmakedate",null,PuPubVO.getString_TrimZeroLenAsNull(billdate.toString()));
//		setDefaultValue("hg_fundset.dmodifydate",null,PuPubVO.getString_TrimZeroLenAsNull(billdate.toString()));
		super.initData();
//		setDefaultComBoxValue();
	}
	public String[] getComBoxYearValues(){
		UFDate billdate = ClientEnvironment.getInstance().getDate();
		int year=billdate.getYear();
		String[] str= new String[5];
		for(int i=-2;i<=2;i++){
			str[i+2]= String.valueOf(year+i);
		}
		return str;
	}
	/**
	 * 出生年增加默认值
	 */
	public void setDefaultComBoxValue(){
			Component o = getComponent("hg_fundset.cyear");
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
	@Override
	public String getWhereSQL() {
		return super.getWhereSQL();
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
		setValueRef("hg_fundset.cyear", box);
	}
}
