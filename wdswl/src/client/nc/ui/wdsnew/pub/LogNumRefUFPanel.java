package nc.ui.wdsnew.pub;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UIRefPane;
/**
 * �������κŵ���
 * @author mlr
 */
public class LogNumRefUFPanel extends UIRefPane {

	private static final long serialVersionUID = 3786378049066318161L;
	/** ʹ���ߴ���Ĳ��� */
	protected String m_strCorpID = null; //��˾ID
	protected String m_strWareHouseID = null; //�ֿ�ID
	protected String m_spaceId = null; //��λid
	protected String m_strInventoryID = null; //���ID
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
    
    
    	 m_strCorpID = datas[0]; //��˾ID
    	 m_strWareHouseID = datas[1]; //�ֿ�ID
//    	 m_strWareHouseName = datas[2]; //�ֿ�����
//    	 m_strWareHouseCode = datas[3]; //�ֿ����
//    	
    	 m_spaceId = datas[2]; //��λid
//    	 m_spaceCode= datas[5] ;//��λ ����
//    	 m_spaceName=datas[6];//��λ����
//    	 
    	 m_strInventoryID = datas[3]; //���ID
//    	 m_strInventoryName = datas[8]; //�������
//    	 m_strInventoryCode = datas[9]; //�������
    	
    }
	/**
	 * 
	 */
	public void onButtonClicked() {
		
		getLotNumbDlg().setM_strInventoryID(m_strInventoryID);
		getLotNumbDlg().setWareHouseID(m_strWareHouseID);
		getLotNumbDlg().setM_strCorpID(m_strCorpID);
		getLotNumbDlg().setM_spaceId(m_spaceId);
		//���
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
