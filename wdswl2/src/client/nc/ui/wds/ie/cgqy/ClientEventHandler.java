package nc.ui.wds.ie.cgqy;

import nc.ui.pub.beans.UIDialog;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.wl.pub.BeforeSaveValudate;
import nc.ui.wl.pub.WdsPubEnventHandler;
import nc.vo.pub.BusinessException;

public class ClientEventHandler extends WdsPubEnventHandler {

	public ClientUIQueryDlg queryDialog = null;

	public ClientEventHandler(ClientUI clientUI, IControllerBase control) {
		super(clientUI, control);
	}

	protected UIDialog createQueryUI() {
		if (queryDialog == null) {
			queryDialog=new ClientUIQueryDlg(	getBillUI(),
					null,
					_getCorp().getPrimaryKey(),
					getBillUI()._getModuleCode(),
					_getOperator(),
					getBillUI().getBusinessType(),
					getBillUI().getNodeKey());
			//queryDialog = new ClientUIQueryDlg(getBillUI(), null, tempinfo);
		}
		return queryDialog;
	}
	
	
	
	@Override
	protected void onBoSave() throws Exception {
		beforeSaveCheck();
		super.onBoSave();
	}
	/**
	 * 
	 * @作者：lyf
	 * @说明：完达山物流项目 
	 * 表体保存前校验
	 * @时间：2011-3-23下午09:05:20
	 * @throws Exception
	 */
	protected void beforeSaveCheck() throws Exception{
		if(getBillUI().getVOFromUI()!=null){
			if(getBillUI().getVOFromUI().getChildrenVO()==null||
					getBillUI().getVOFromUI().getChildrenVO().length==0	){
				throw new BusinessException("表体不允许为空");
			}
			/**
			 * @author yf
			 * 存货 唯一校验
			 */
			String[] fields = new String[]{"invcode"};
			String[] displays = new String[]{"存货编码"};
			BeforeSaveValudate.beforeSaveBodyUnique(getBillCardPanelWrapper().getBillCardPanel().getBillTable(),getBillCardPanelWrapper().getBillCardPanel().getBillModel(),fields,displays);
			
			
//			else{
//				super.beforeSaveBodyUnique(new String[]{"pk_invbasdoc"});
//				CgqyBVO[] bodys =(CgqyBVO[] )getBillUI().getVOFromUI().getChildrenVO();
//				for(CgqyBVO body:bodys){
//					body.validateOnsave();
//				}
//			}
		}
	}
}
