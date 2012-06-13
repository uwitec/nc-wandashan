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
	 * @���ߣ�lyf :������Ƿ����
	 * @˵�������ɽ������Ŀ 
	 * @ʱ�䣺2011-12-21����09:50:55
	 * @throws Exception
	 */
	public void onBoLastMonth() throws Exception {
		AggregatedValueObject  curValue =getBufferData().getCurrentVO();
		if(curValue == null){
			getBillUI().showErrorMessage("��ȡ��ǰ��������ʧ�ܣ�����ˢ������");
			return;
		}
		String classname = "nc.bs.wds.ic.zgjz.ZgjzBO";
		String methodname ="refeshDeducNum";
		ArrayList<String> infor = new ArrayList<String>();
		infor.add(_getOperator());//������
		infor.add(_getDate().toString());//��¼����
		infor.add(_getCorp().getPrimaryKey());//��¼��˾
		infor.add(new UFDate(System.currentTimeMillis()).toString());//ϵͳ����
		Class[] ParameterTypes = new Class[]{AggregatedValueObject.class,ArrayList.class};
		Object[] ParameterValues = new Object[]{curValue,infor};
		LongTimeTask.callRemoteService(WdsWlPubConst.WDS_WL_MODULENAME, classname, methodname, ParameterTypes, ParameterValues, 2);
		onBoRefresh();
	}
	
}
