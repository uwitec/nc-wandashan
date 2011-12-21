package nc.ui.wds.ic.zgjz;

import java.util.ArrayList;

import nc.ui.pub.beans.UIDialog;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.wds.w80020206.buttun0206.ISsButtun;
import nc.ui.wl.pub.LongTimeTask;
import nc.ui.wl.pub.WdsPubEnventHandler;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.lang.UFDate;
import nc.vo.wl.pub.WdsWlPubConst;

public class ClientEventHandler extends WdsPubEnventHandler {

	public ClientUIQueryDlg queryDialog = null;

	public ClientEventHandler(ClientUI clientUI, IControllerBase control) {
		super(clientUI, control);
	}

	protected UIDialog createQueryUI() {
		if (queryDialog == null) {
			queryDialog=new ClientUIQueryDlg(getBillUI(), null, _getCorp().getPrimaryKey(), getBillUI()._getModuleCode(),
					_getOperator(), getBillUI().getBusinessType(), getBillUI()
							.getNodeKey());
		}
		return queryDialog;
	}
	
	@Override
	protected void onBoElse(int intBtn) throws Exception {
		// TODO Auto-generated method stub
		super.onBoElse(intBtn);
		if(intBtn == ISsButtun.Reduc_lastmonth){
			onBoLastMonth();
		}
	}
	/**
	 * 
	 * @作者：lyf :冲上月欠发量
	 * @说明：完达山物流项目 
	 * @时间：2011-12-21上午09:50:55
	 * @throws Exception
	 */
	public void onBoLastMonth() throws Exception {
		AggregatedValueObject  curValue =getBufferData().getCurrentVO();
		if(curValue == null){
			getBillUI().showErrorMessage("获取当前操作数据失败，请先刷新数据");
			return;
		}
		String classname = "nc.bs.wds.ic.zgjz.ZgjzBO";
		String methodname ="refeshDeducNum";
		ArrayList<String> infor = new ArrayList<String>();
		infor.add(_getOperator());//操作人
		infor.add(_getDate().toString());//登录日期
		infor.add(_getCorp().getPrimaryKey());//登录公司
		infor.add(new UFDate(System.currentTimeMillis()).toString());//系统日期
		Class[] ParameterTypes = new Class[]{AggregatedValueObject.class,ArrayList.class};
		Object[] ParameterValues = new Object[]{curValue,infor};
		LongTimeTask.callRemoteService(WdsWlPubConst.WDS_WL_MODULENAME, classname, methodname, ParameterTypes, ParameterValues, 2);
		onBoRefresh();
	}
	
}
