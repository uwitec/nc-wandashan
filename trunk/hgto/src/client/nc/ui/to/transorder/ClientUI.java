package nc.ui.to.transorder;

/**
 * �������ڣ�(2004-2-9 11:45:59) ���ߣ����˾� ˵����** ����ģ�� **ҵ����档
 */

import nc.ui.pub.FramePanel;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.to.pubtransfer.ButtonCtrl;
import nc.ui.to.pubtransfer.Const;
import nc.ui.to.pubtransfer.Model;
import nc.ui.to.pubtransfer.TransferClientUI;
import nc.vo.logging.Debug;
import nc.vo.sm.funcreg.FuncRegisterVO;
import nc.vo.to.pub.ConstVO;

/**
 * �������ڣ�(2004-2-9 11:45:59) ���ߣ����˾� ˵����** ����ģ�� **ҵ����档
 */

public class ClientUI extends TransferClientUI {

  /**
   * �����ߣ����˾� ���ܣ��õ�������ʼ���ݣ����Ƶ��˵ȡ� ������ ���أ� ���⣺ ���ڣ�(2001-5-9 9:23:32)
   * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
   */
  protected void initNodeInfo() {
    super.initNodeInfo();
    m_NodeInfo.setBillTypeCode(ConstVO.m_sBillDBDD);
    m_NodeInfo.setNodeCode(Const.NODECODE_DBDD);
  }

  // �����½��Ľڵ㲻ͬ�����벻ͬ�Ľڵ��
  protected void initNodeInfo(FramePanel frame_pane) {
    super.initNodeInfo(frame_pane);
    m_NodeInfo.setBillTypeCode(ConstVO.m_sBillDBDD);
    FuncRegisterVO funcVO = frame_pane.getFuncRegisterVO();
    String funCode = funcVO.getFunCode();
    if (funCode != null && !funCode.equalsIgnoreCase(Const.NODECODE_DBDD)) {
      m_NodeInfo.setNodeCode(funCode);
    }
    else {
      m_NodeInfo.setNodeCode(Const.NODECODE_DBDD);
    }
  }

  public ClientUI() {
    super();
    // TODO �Զ����ɹ��캯�����
  }

  /**
   * ClientUI ������ע�⡣ nc 5.6 Ϊҵ����־�ṩ�ĵ������鹦�ܹ����ӡ�
   */
  public ClientUI(
      String pk_corp, String billType, String businessType, String operator,
      String billID) {
    super(billID);
  }

  // Ϊ�˸��ƽڵ����Ӵ˷���
  public ClientUI(
      FramePanel frame_pane) {
    super(frame_pane);

  }
  
  
//  zhf add
  public void initialize() {
	  super.initialize();
	 setStockPanelEnable(false);
	 getCardPanel().setBodyMenuShow(false);//zhf  ȥ�����������ı��� �˵�
	 getCardPanel().setBodyMenu(null);
//	 getCardPanel().setBodyMenuShow("base", false);
	 getCardPanel().removeBodyMenuListener();
  }
  
  public ButtonCtrl getButtonCtrl(){
	  return super.m_ButtonCtrl;
  }
  
  public   Model getModel(){
	  return this.m_Model;
  }
  
  public BillCardPanel getCardPanel(){
	  return this.m_CardCtrl.getCardPanel().getBillCardPanel();
  }
  
  public void setStockPanelEnable(boolean isEnable){
	  try {

		  m_CardCtrl.getCardPanel().getBillCardPanel().getHeadTabbedPane().setEnabledAt(1, isEnable);
		  m_CardCtrl.getCardPanel().getBillCardPanel().getBodyTabbedPane().setEnabledAt(4, isEnable);

	  }
	  catch (Exception e) {
		  e.printStackTrace();
		  Debug.error(e.getMessage(), e);
	  }
  }
}