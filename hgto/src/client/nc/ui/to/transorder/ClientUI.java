package nc.ui.to.transorder;

/**
 * 创建日期：(2004-2-9 11:45:59) 作者：王乃军 说明：** 代码模版 **业务界面。
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
 * 创建日期：(2004-2-9 11:45:59) 作者：王乃军 说明：** 代码模版 **业务界面。
 */

public class ClientUI extends TransferClientUI {

  /**
   * 创建者：王乃军 功能：得到环境初始数据，如制单人等。 参数： 返回： 例外： 日期：(2001-5-9 9:23:32)
   * 修改日期，修改人，修改原因，注释标志：
   */
  protected void initNodeInfo() {
    super.initNodeInfo();
    m_NodeInfo.setBillTypeCode(ConstVO.m_sBillDBDD);
    m_NodeInfo.setNodeCode(Const.NODECODE_DBDD);
  }

  // 根据新建的节点不同，置入不同的节点号
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
    // TODO 自动生成构造函数存根
  }

  /**
   * ClientUI 构造子注解。 nc 5.6 为业务日志提供的单据联查功能构造子。
   */
  public ClientUI(
      String pk_corp, String billType, String businessType, String operator,
      String billID) {
    super(billID);
  }

  // 为了复制节点增加此方法
  public ClientUI(
      FramePanel frame_pane) {
    super(frame_pane);

  }
  
  
//  zhf add
  public void initialize() {
	  super.initialize();
	 setStockPanelEnable(false);
	 getCardPanel().setBodyMenuShow(false);//zhf  去掉调拨订单的表体 菜单
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