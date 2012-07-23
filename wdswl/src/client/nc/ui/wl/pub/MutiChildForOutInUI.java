package nc.ui.wl.pub;

import nc.ui.pub.ButtonObject;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.linkoperate.ILinkQueryData;
import nc.ui.trade.button.IBillButton;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.trade.button.ButtonVO;
import nc.vo.wl.pub.WdsWlPubConst;
import nc.vo.wl.pub.Button.CommonButtonDef;


public abstract class MutiChildForOutInUI extends MutilChildUI{

	private LoginInforHelper helper = null;
	
	public LoginInforHelper getLoginInforHelper(){
		if(helper == null){
			helper = new LoginInforHelper();
		}
		return helper;
	}
	public MutiChildForOutInUI() {
		super();
	}
	
	public MutiChildForOutInUI(Boolean useBillSource) {
		super(useBillSource);
	}

	public MutiChildForOutInUI(String pk_corp, String pk_billType, String pk_busitype,
			String operater, String billId) {
		super(pk_corp, pk_billType, pk_busitype, operater, billId);
	}
	@Override
	public void doQueryAction(ILinkQueryData querydata) {
		// TODO Auto-generated method stub
		super.doQueryAction(querydata);
		ButtonObject[] btns = getButtons();
		for (ButtonObject btn : btns) {
			if (("" + (IBillButton.Print)).equals(btn.getTag())) {
				btn.setEnabled(true);
				btn.setVisible(true);
			}else{
				btn.setEnabled(false);
				btn.setVisible(false);
			}
		}
	}
	@Override
	protected void initPrivateButton() {
		super.initPrivateButton();
		CommonButtonDef def = new CommonButtonDef();
		ButtonVO  joinup = def.getJoinUPButton();
		addPrivateButton(joinup);
	}
	
	public abstract String getHslFieldName();//获取换算率字段名
	public abstract String getAssNumFieldName();//获取附属楼字段名
	
	public boolean beforeEdit(BillEditEvent e) {
		if(PuPubVO.getString_TrimZeroLenAsNull(getHslFieldName()) != null && PuPubVO.getString_TrimZeroLenAsNull(getAssNumFieldName()) != null){
			if(e.getKey().equalsIgnoreCase(getAssNumFieldName())){//辅助数量  换算率为0  辅数量不可编辑
				UFDouble hsl = PuPubVO.getUFDouble_NullAsZero(getBillCardPanel().getBodyValueAt(e.getRow(), getHslFieldName()));
				if(hsl.equals(WdsWlPubConst.ufdouble_zero)){
					return false;
				}
			}
		}
		  
		return true;
	}
}
