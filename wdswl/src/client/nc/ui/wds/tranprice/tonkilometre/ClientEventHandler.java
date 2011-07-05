package nc.ui.wds.tranprice.tonkilometre;

import java.util.ArrayList;

import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UITable;
import nc.ui.pub.bill.BillModel;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.wl.pub.BeforeSaveValudate;
import nc.ui.wl.pub.WdsPubEnventHandler;
import nc.vo.pub.BusinessException;
import nc.vo.wl.pub.WdsWlPubConst;

public class ClientEventHandler extends WdsPubEnventHandler {

	public ClientUIQueryDlg queryDialog = null;

	public ClientEventHandler(ClientUI clientUI, IControllerBase control) {
		super(clientUI, control);
	}

	protected UIDialog createQueryUI() {
		if (queryDialog == null) {
			queryDialog=new ClientUIQueryDlg(getBillUI(),
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
	protected String getHeadCondition() {
		String strWhere = super.getHeadCondition();
		if(strWhere == null || "".equals(strWhere)){
			return " and pk_billtype='"+WdsWlPubConst.WDSI+"'";
		}else {
			return strWhere+" and pk_billtype='"+WdsWlPubConst.WDSI+"'";
		}
	}
	@Override
	protected void onBoSave() throws Exception {
		beforeSaveValidate();
		super.onBoSave();
	}
	protected void beforeSaveValidate() throws Exception {
		//对 开始日期 和 结束日期 的校验 开始日期   不能大于  结束日期
		String o1= (String) getBillCardPanelWrapper().getBillCardPanel().getHeadItem("dstartdate").getValueObject();
		String o2= (String) getBillCardPanelWrapper().getBillCardPanel().getHeadItem("denddate").getValueObject();
		if(o1.compareTo(o2)>0){
			throw new Exception("[开始日期] 不能大于  [结束日期]");
		}	
		//表体不为空
		BeforeSaveValudate.BodyNotNULL(getBillCardPanelWrapper().getBillCardPanel().getBillTable());
		//发货地区和收获地区组合唯一
		beforeSaveBodyUnique(
				getBillCardPanelWrapper().getBillCardPanel().getBillTable(),
				getBillCardPanelWrapper().getBillCardPanel().getBillModel(),
				new String[]{"pk_replace","ifw"}, 
				new String[]{"收获地区","应用范围"});
		
	}
	/**
	 * 
	 * @作者：mlr
	 * @说明：完达山物流项目 
	 * @时间：2011-5-24下午12:54:55
	 * @param table
	 * @param model
	 * @param fields
	 * @param displays
	 * @throws Exception
	 */
	private  void  beforeSaveBodyUnique(UITable table,BillModel model,String[] fields,String[] displays) throws Exception{
		int num =table.getRowCount();
		if(fields == null || fields.length == 0){
			return;
		}
		if(num>0){
			ArrayList<String> list = new ArrayList<String>();
			for(int i = 0 ;i<num; i++){
				String key = "";
				for(String str : fields){
					Object o1 =model.getValueAt(i, str);
					key = key + ","+String.valueOf(o1);
				}
				String dis="";
				for(int j=0;j<displays.length;j++){
					   dis=dis+"[ "+displays[j]+" ]";
					}
					
				if(list.contains(key)){							
					throw new BusinessException("第["+(i+1)+"]行表体字段 "+dis+" 存在重复!");
				}else{
					list.add(key);
				}
				//如果应用 范围  为 全部 查看发货站和收获站相同情况下 应用范围是否存在包含
				if("全部".equals(model.getValueAt(i, fields[2]))){
					String[] strs=key.split(",");
					
					if(list.contains(","+strs[1]+","+strs[2]+","+"分仓") || list.contains(","+strs[1]+","+strs[2]+","+"经销商")){
						throw new Exception("第["+(i+1)+"]行表体字段 "+dis+" 存在[ 应用范围 ] 的包含!");
					}
				}
				//如果应用 范围  为 经销商 或 分仓 查看发货站和收获站相同情况下 应用范围是否存在包含
				if("经销商".equals(model.getValueAt(i, fields[2])) || "分仓".equals(model.getValueAt(i, fields[2])) ){
                    
					String[] strs=key.split(",");
					
					if(list.contains(","+strs[1]+","+strs[2]+","+"全部") ){
						throw new Exception("第["+(i+1)+"]行表体字段 "+dis+" 存在[ 应用范围 ] 的包含!");
					}
				}
				
			}
		}
	}
	@Override
	protected void onBoCopy() throws Exception {
		// TODO Auto-generated method stub
		super.onBoCopy();
		getBillUI().setDefaultData();
		getBillCardPanelWrapper().getBillCardPanel().setTailItem("vapproveid", null);
		getBillCardPanelWrapper().getBillCardPanel().setTailItem("dapprovedate", null);
	}
	
}
