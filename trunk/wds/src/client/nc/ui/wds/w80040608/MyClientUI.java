package nc.ui.wds.w80040608;

import java.util.ArrayList;

import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ArrayListProcessor;
import nc.jdbc.framework.processor.ResultSetProcessor;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.bsdelegate.BDBusinessDelegator;
import nc.ui.trade.manage.ManageEventHandler;


/**
 * <b> 在此处简要描述此类的功能 </b>
 *
 * <p>
 *     在此处添加此类的描述信息
 * </p>
 *
 *
 * @author author
 * @version tempProject version
 */
 public class MyClientUI extends AbstractMyClientUI{
       
       protected ManageEventHandler createEventHandler() {
		return new MyEventHandler(this, getUIControl());
	}
       
	public void setBodySpecialData(CircularlyAccessibleValueObject[] vos)
			throws Exception {}

	protected void setHeadSpecialData(CircularlyAccessibleValueObject vo,
			int intRow) throws Exception {}

	protected void setTotalHeadSpecialData(CircularlyAccessibleValueObject[] vos)
			throws Exception {	}

	protected void initSelfData() {	}

	public void setDefaultData() throws Exception {
	}
	
	@Override
	public void afterEdit(BillEditEvent e) {
		// TODO Auto-generated method stub
		
		if(e.getKey().equals("pk_invbasdoc")){
			IUAPQueryBS iuapQueryBS = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
			String sql = "select count(pk_invbasdoc) from tb_stockguard where pk_invbasdoc = '"+getBillCardPanel().getHeadItem("pk_invbasdoc").getValueObject()+"'";
			try {
				ArrayList list = (ArrayList) iuapQueryBS.executeQuery(sql, new ArrayListProcessor());
				int countnum = Integer.parseInt((((Object[]) list
						.get(0))[0]).toString());
				if(countnum>0){
					showWarningMessage("此条数据已存在,请更换!");
					getBillCardPanel().getHeadItem("pk_invbasdoc").setValue("");
					return;
				}
			
			} catch (BusinessException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		
		super.afterEdit(e);
	}

}
