package nc.ui.wds.dm.trans.follow;
import java.util.ArrayList;

import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.bs.uap.bd.BDRuntimeException;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.ui.trade.bill.ICardController;
import nc.ui.trade.card.CardEventHandler;
import nc.ui.trade.pub.IVOTreeData;
import nc.ui.trade.pub.IVOTreeDataByID;
import nc.ui.trade.treecard.BillTreeCardUI;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.SuperVO;
import nc.vo.wds.dm.trans.follow.TransfollowVO;

public class ClientCartUI extends BillTreeCardUI{



	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ClientCartUI() {
		super();
	}
	@Override
	public void setDefaultData() throws Exception {
		// TODO Auto-generated method stub
		super.setDefaultData();
		//公司
		getBillCardPanel().setHeadItem("pk_corp", _getCorp().getPrimaryKey());
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
		modifyRootNodeShowName("发运跟踪信息维护");
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
			return "pk_wds_transfollow";
		}

		public String getParentIDFieldName() {
			return "pk_parent";
		}

		public String getShowFieldName() {
			return "vname";//vcode
		}

		public SuperVO[] getTreeVO() {
			if (m_ChnlManagerVOs == null) {
				IUAPQueryBS queryBS = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
				ArrayList list = null;
				try {
					StringBuffer sql = new StringBuffer() 
					.append(" select a.*")
					.append(" from wds_transfollow a ")
					.append(" where nvl(a.dr,0)=0");
					list = (ArrayList)queryBS.executeQuery(sql.toString(), new BeanListProcessor(TransfollowVO.class));
				} catch (BusinessException e) {
					Logger.error(e.getMessage(), e);
					throw new BDRuntimeException(e.getMessage(), e);
				}
				if (list == null)
					return null;
				m_ChnlManagerVOs = (TransfollowVO[]) list.toArray(new TransfollowVO[0]);
			}
//			Arrays.sort(m_ChnlManagerVOs, 
//				new Comparator() {
//					public int compare(Object o1, Object o2) {
//						String code1 = ((FylxdocVo) o1).getVcostname();
//						String code2 = ((FylxdocVo) o2).getVcostname();
//						return code1.compareTo(code2);
//					}
//				}
//			);
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

}
