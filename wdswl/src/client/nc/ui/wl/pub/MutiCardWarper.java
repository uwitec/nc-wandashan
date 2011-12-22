package nc.ui.wl.pub;
import java.util.ArrayList;

import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.bill.BillData;
import nc.ui.trade.bill.ICardController;
import nc.ui.trade.multichild.MultiChildBillCardPanelWrapper;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.trade.pub.IExAggVO;
/**
 * @author mlr
 */
public class MutiCardWarper extends MultiChildBillCardPanelWrapper{

	public MutiCardWarper(ClientEnvironment ce, ICardController ctl,
			String pk_busiType, String nodeKey) throws Exception {
		super(ce, ctl, pk_busiType, nodeKey);
	}
    private String[] tablecodes=null;
	public MutiCardWarper(ClientEnvironment ce, ICardController ctl,
			String pk_busiType, String nodeKey, ArrayList defAry)
			throws Exception {
		
		super(ce, ctl, pk_busiType, nodeKey, defAry);
		String cls=ctl.getBillVoName()[0];
		IExAggVO ie= (IExAggVO) Class.forName(cls).newInstance();
		this.tablecodes=ie.getTableCodes();
	}

	public MutiCardWarper(ClientEnvironment ce, ICardController ctl,
			String pk_busiType, String nodeKey, BillData billData,
			ArrayList defAry) throws Exception {
		super(ce, ctl, pk_busiType, nodeKey, billData, defAry);
	}

	public MutiCardWarper(ClientEnvironment ce, ICardController ctl,
			String pk_busiType, String nodeKey, BillData billData)
			throws Exception {
		super(ce, ctl, pk_busiType, nodeKey, billData);
	  
	}
	 /**
	  * mlr
	  */
	public AggregatedValueObject getChangedVOFromUI() throws java.lang.Exception {
		AggregatedValueObject billVO = super.getChangedVOFromUI();
		IExAggVO exBillVO = (IExAggVO) billVO;
		billVO.setChildrenVO(null);
		for (int i = 0; i < tablecodes.length; i++)
		{
			CircularlyAccessibleValueObject[] vos =
				getBillCardPanel().getBillData().getBodyValueChangeVOs(
						tablecodes[i],
					getUIControl().getBillVoName()[i + 2]);
			if (vos != null && vos.length != 0)
				exBillVO.setTableVO(tablecodes[i], vos);
		}
		billVO.setChildrenVO(
				getBillCardPanel().getBillData().getBodyValueChangeVOs(
				tablecodes[0],
				getUIControl().getBillVoName()[2]));
		return billVO;
	}

	/**
	 * 从界面上得到VO。
	 * 创建日期：(2004-1-7 10:10:26)
	 * @return nc.vo.pub.AggregatedValueObject
	 */
	public nc.vo.pub.AggregatedValueObject getBillVOFromUI() throws Exception {
		AggregatedValueObject billVO = super.getBillVOFromUI();
		IExAggVO exBillVO = (IExAggVO) billVO;
		billVO.setChildrenVO(null);
		for (int i = 0; i < tablecodes.length; i++)
		{
			CircularlyAccessibleValueObject[] vos =
				getBillCardPanel().getBillData().getBodyValueVOs(
						tablecodes[i],
					getUIControl().getBillVoName()[i + 2]);
			if (vos != null && vos.length != 0)
				exBillVO.setTableVO(tablecodes[i], vos);
		}
		billVO.setChildrenVO(getBillCardPanel().getBillData().getBodyValueVOs(
				tablecodes[0],
				getUIControl().getBillVoName()[2]));
		return billVO;
	}	
	
}
