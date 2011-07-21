package nc.ui.zb.pub;

import java.awt.Container;
import nc.vo.trade.billsource.LightBillVO;;

/**
 * 联查对话框公共类
 * @author Administrator
 *
 */
public class SourceBillFlowDlg extends nc.ui.trade.billgraph.SourceBillFlowDlg {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 
	 */
	public SourceBillFlowDlg(java.awt.Container parent,String billFinderClassname,String billType, String billID,String billNO) {
		super(parent,billFinderClassname,billType,billID,billNO);
	}
	/**
	 */
	public SourceBillFlowDlg(Container parent, String billType, String billID,
			String bizType, String userID, String bill_code) {
		super(parent,"nc.bs.trade.billsource.DefaultBillFinder", billType, billID, bill_code);
		setResizable(true);
	}
	/**
	 */
	public SourceBillFlowDlg(Container parent, LightBillVO voBillInfo) {
		super(parent,"nc.bs.trade.billsource.DefaultBillFinder",  voBillInfo.getType(), voBillInfo.getID(), voBillInfo.getCode());
		setResizable(true);
	}
}