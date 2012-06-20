package nc.ui.wdsnew.pub;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UIRefPane;
/**
 * 物流批次号档案
 * @author mlr
 */
public class LogNumRefUFPanel extends UIRefPane {

	private static final long serialVersionUID = 3786378049066318161L;
	/** 使用者传入的参数 */
	protected String m_strCorpID = null; //公司ID
	protected String m_strWareHouseID = null; //仓库ID
	protected String m_spaceId = null; //货位id
	protected String m_strInventoryID = null; //存货ID
	private boolean m_bisClicked;
    private   String[] datas=null;
	private nc.ui.wdsnew.pub.LotNumbDlg m_dlgLotNumb = null;
    
    public LogNumRefUFPanel() {
    	super();
    	setReturnCode(true);
    	//setIsCustomDefined(true);
    	//addValueChangedListener(this);
    	getUITextField().setMaxLength(30);
    	this.setIsBatchData(false);
    }
    public void setDatas(String[] datas){
    	this.datas=datas;
    	if(datas==null )
    		return;
    
    
    	 m_strCorpID = datas[0]; //公司ID
    	 m_strWareHouseID = datas[1]; //仓库ID
//    	 m_strWareHouseName = datas[2]; //仓库名称
//    	 m_strWareHouseCode = datas[3]; //仓库编码
//    	
    	 m_spaceId = datas[2]; //货位id
//    	 m_spaceCode= datas[5] ;//货位 编码
//    	 m_spaceName=datas[6];//货位名称
//    	 
    	 m_strInventoryID = datas[3]; //存货ID
//    	 m_strInventoryName = datas[8]; //存货名称
//    	 m_strInventoryCode = datas[9]; //存货编码
    	
    }
	/**
	 * 
	 */
	public void onButtonClicked() {
		
		getLotNumbDlg().setM_strInventoryID(m_strInventoryID);
		getLotNumbDlg().setWareHouseID(m_strWareHouseID);
		getLotNumbDlg().setM_strCorpID(m_strCorpID);
		getLotNumbDlg().setM_spaceId(m_spaceId);
		//查库
		try {
			getLotNumbDlg().setData();

		} catch (Exception e) {
			nc.vo.scm.pub.SCMEnv.out("Can not read data from server!");
			nc.vo.scm.pub.SCMEnv.error(e);
		}
	

		if (getLotNumbDlg().showModal() == UIDialog.ID_OK) {
			if(getLotNumbDlg().getLis().size()>0)
			getUITextField().setText(getLotNumbDlg().getLis().get(0).getWhs_batchcode());
			m_bisClicked = true;

		} else {
			getLotNumbDlg().destroy();
			m_bisClicked = false;
		}
		getUITextField().setRequestFocusEnabled(true);
		getUITextField().grabFocus();
		return;	
	}
	public nc.ui.wdsnew.pub.LotNumbDlg getLotNumbDlg() {
		 if (m_dlgLotNumb == null) {
	
				 m_dlgLotNumb = new nc.ui.wdsnew.pub.LotNumbDlg(this.getParent(),true);
		}

		return m_dlgLotNumb;

	}

}
