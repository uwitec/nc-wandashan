package nc.ui.wds.load.price;

import java.util.ArrayList;

import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.bs.uap.bd.BDRuntimeException;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.trade.bill.ICardController;
import nc.ui.trade.card.CardEventHandler;
import nc.ui.trade.pub.IVOTreeData;
import nc.ui.trade.pub.IVOTreeDataByID;
import nc.ui.trade.treecard.BillTreeCardUI;
import nc.ui.wl.pub.LoginInforHelper;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.wds.load.LoadpriceVO;
import nc.vo.wl.pub.LoginInforVO;

public class ClientCartUI extends BillTreeCardUI{



	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private LoginInforVO login = null;
	public LoginInforVO getLogInfor(){
		if(login == null)
			try {
				login = new LoginInforHelper().getLogInfor(_getOperator());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				login = null;
			}
		return login;
	}
	public ClientCartUI() {
		super();
	}
	@Override
	public void setDefaultData() throws Exception {
		// TODO Auto-generated method stub
		super.setDefaultData();
		//公司
		getBillCardPanel().setHeadItem("pk_corp", _getCorp().getPrimaryKey());
		if(getLogInfor() == null){
			showErrorMessage("当前登录人关联的仓库信息为空");
			return;
		}
			
		getBillCardPanel().setHeadItem("cwarehouseid", getLogInfor().getWhid());
	}
	
	public ClientCartUI(String pk_corp, String pk_billType, String pk_busitype,String operater, String billId) {
		super(pk_corp, pk_billType, pk_busitype, operater, billId);
	}

	protected IVOTreeData createTreeData() {
		return new ChnlManagerTreeCardData();
	}

	protected CardEventHandler createEventHandler() {
		return EventHandler.getCardEventHandler(this, getUIControl());
	}

	protected ICardController createController() {
		return new ClientCtrl();
	}

	public void afterInit() throws Exception {
		super.afterInit();
		// 修改树根节点名字
		modifyRootNodeShowName("装卸费价格");
	}
	@Override
	public void afterEdit(BillEditEvent e) {
		super.afterEdit(e);
		String key = e.getKey();
		Object value = e.getValue();
		if("pk_invmandoc".equalsIgnoreCase(key)){
			getBillCardPanel().execHeadTailLoadFormulas();
		}
		//是否采码 和 是否有采码价格保持一致
		else if("fiscoded".equalsIgnoreCase(key)){
			if(!PuPubVO.getUFBoolean_NullAs(value, UFBoolean.FALSE).booleanValue()){
				getBillCardPanel().setHeadItem("ncodeprice", null);
			}
		}else if("ncodeprice".equalsIgnoreCase(key)){
			if(PuPubVO.getString_TrimZeroLenAsNull(value)!=null){
				getBillCardPanel().setHeadItem("fiscoded", UFBoolean.TRUE);
			}
		}
	}
	public void selectNode(CircularlyAccessibleValueObject vo) throws Exception {
		if (getTreeToBuffer().containsKey(vo.getPrimaryKey()))
			super.insertNodeToTree(vo);// 实际是选择节点
	}

	public IVOTreeDataByID getTreeCardData(){
		return new ChnlManagerTreeCardData();
	}
	
	/**
	 * 树形菜单
	 * @author Administrator
	 *
	 */
	public class ChnlManagerTreeCardData implements IVOTreeDataByID {
		private SuperVO[] m_ChnlManagerVOs = null;

		public String getIDFieldName() {
			return "pk_loadprice";
		}

		public String getParentIDFieldName() {
			return null;
		}

		public String getShowFieldName() {
			return "invname";//invcode
		}

		public SuperVO[] getTreeVO() {//zhf modify
			if (m_ChnlManagerVOs == null) {
				IUAPQueryBS queryBS = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
				ArrayList list = null;
				try {
					StringBuffer sql = new StringBuffer();
					sql.append(" select wds_loadprice.*,bd_invbasdoc.invcode invcode,bd_invbasdoc.invname invname");
					sql.append(" from wds_loadprice  ");
					sql.append(" join bd_invmandoc on wds_loadprice.pk_invmandoc=bd_invmandoc.pk_invmandoc ");
					sql.append(" join bd_invbasdoc on  bd_invmandoc.pk_invbasdoc = bd_invbasdoc.pk_invbasdoc ");
					sql.append(" where nvl(wds_loadprice.dr,0)=0 and nvl(bd_invmandoc.dr,0)=0 and nvl(bd_invbasdoc.dr,0)=0" +
							"  and wds_loadprice.pk_corp='"+ _getCorp().getPrimaryKey()+"'");
					sql.append(" and wds_loadprice.cwarehouseid = '"+getLogInfor().getWhid()+"'");
					list = (ArrayList)queryBS.executeQuery(sql.toString(), new BeanListProcessor(LoadpriceVO.class));
				} catch (BusinessException e) {
					Logger.error(e.getMessage(), e);
					throw new BDRuntimeException(e.getMessage(), e);
				}
				if (list == null)
					return null;
				m_ChnlManagerVOs = (LoadpriceVO[] )list.toArray(new LoadpriceVO[0]);
			}

			return m_ChnlManagerVOs;
		}
	}

	@Override
	protected IVOTreeData createTableTreeData() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getRefBillType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void initSelfData() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public Object getUserObject() {
		// TODO Auto-generated method stub
		return new GetCheckClasses();
	}

}
