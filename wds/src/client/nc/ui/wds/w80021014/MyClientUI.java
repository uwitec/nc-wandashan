package nc.ui.wds.w80021014;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ArrayListProcessor;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.trade.manage.ManageEventHandler;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.SuperVO;
import nc.vo.wds.w80021014.TbInvclBasVO;
import nc.vo.wds.w80021014.TbInvclVO;

/**
 * <b> 在此处简要描述此类的功能 </b>
 * 
 * <p>
 * 在此处添加此类的描述信息
 * </p>
 * 
 * 
 * @author author
 * @version tempProject version
 */
public class MyClientUI extends AbstractMyClientUI implements
		ListSelectionListener {

	protected ManageEventHandler createEventHandler() {
		return new MyEventHandler(this, getUIControl());
	}

	public void setBodySpecialData(CircularlyAccessibleValueObject[] vos)
			throws Exception {
	}

	protected void setHeadSpecialData(CircularlyAccessibleValueObject vo,
			int intRow) throws Exception {
	}

	protected void setTotalHeadSpecialData(CircularlyAccessibleValueObject[] vos)
			throws Exception {
	}

	protected void initSelfData() {
	}

	public void setDefaultData() throws Exception {
	}

	@Override
	protected void initEventListener() {
		// TODO Auto-generated method stub
		super.initEventListener();
		getBillListPanel().getHeadTable().getSelectionModel()
				.addListSelectionListener(this);
	}

	@Override
	public void afterEdit(BillEditEvent e) {
		// TODO Auto-generated method stub

		if (e.getKey().equals("invcode")) {

			// 获得表头行数
			int rowNum = getBufferData().getVOBufferSize();
			// 系列编号
			Object invcode = this.getBillCardPanel().getHeadItem("invcode")
					.getValueObject();
			for (int i = 0; i < rowNum; i++) {
				TbInvclVO vo = (TbInvclVO) (getBufferData().getVOByRowNo(i))
						.getParentVO();
				Object invcodenew = vo.getInvcode();
				if (invcodenew != null && !"".equals(invcodenew)) {
					if (invcode != null && !"".equals(invcode)) {
						if (invcode.toString().equals(invcodenew.toString())) {
							this.showErrorMessage("系列编号有重复！");
							getBillCardPanel().setHeadItem("invcode", "");
							return;
						}
					}
				}
			}
		}

		if (e.getKey().equals("bm")) {
			getBillCardPanel().execHeadEditFormulas();

			AggregatedValueObject mybillVO1;
			try {
				mybillVO1 = this.getVOFromUI();

				TbInvclVO tbInvclVO = (TbInvclVO) mybillVO1.getParentVO();
				TbInvclBasVO[] TbInvclBasVOs = (TbInvclBasVO[]) mybillVO1
						.getChildrenVO();

				ArrayList arr = new ArrayList();
				for (int i = 0; i < TbInvclBasVOs.length; i++) {
					if (arr.contains(TbInvclBasVOs[i].getPk_invbasdoc())) {
						this.showErrorMessage("添加的产品已经和本系列绑定，请您重新选择！");
						return;
					} else {
						arr.add(TbInvclBasVOs[i].getPk_invbasdoc());
					}
				}

				/*
				 * if (null != TbInvclBasVOs) { for (int i = 0; i <
				 * TbInvclBasVOs.length; i++) { String pk_invb =
				 * TbInvclBasVOs[i].getPk_invbasdoc(); for (int j = i + 1; j <
				 * TbInvclBasVOs.length; j++) { String pk_invbasdoccj =
				 * TbInvclBasVOs[j] .getPk_invbasdoc(); if (null !=
				 * pk_invbasdoccj && null != pk_invbasdoccj &&
				 * !"".equals(pk_invb) && !"".equals(pk_invbasdoccj)) { if
				 * (pk_invb.equals(pk_invbasdoccj)) { this.showErrorMessage(
				 * "添加的产品已经和本系列绑定，请您重新选择！"); return; } } } } }
				 */

				// 除了本系列外其他系列的产品
				/*
				 * StringBuffer sql_srcbd = new StringBuffer();
				 * sql_srcbd.append(" dr=0 "); if (null != tbInvclVO && null !=
				 * tbInvclVO.getPk_invcl()) { sql_srcbd.append("and
				 * pk_invcl!='"); sql_srcbd.append(tbInvclVO.getPk_invcl());
				 * sql_srcbd.append("' "); }
				 */

				Object pk_invbasdoc = getBillCardPanel().getBodyValueAt(
						getBillCardPanel().getBillTable().getSelectedRow(),
						"pk_invbasdoc");
				String sql = "select pk_invbasdoc from tb_invcl_bas where dr=0 ";
				if (tbInvclVO.getPk_invcl() != null
						&& !"".equals(tbInvclVO.getPk_invcl())) {
					sql += " and pk_invcl!='" + tbInvclVO.getPk_invcl() + "'";
				}
				IUAPQueryBS query = (IUAPQueryBS) NCLocator.getInstance()
						.lookup(IUAPQueryBS.class.getName());

				List list = (List) query.executeQuery(sql,
						new ArrayListProcessor());
				Iterator it = list.iterator();
				while (it.hasNext()) {
					Object[] o = (Object[]) it.next();
					if (pk_invbasdoc != null && !"".equals(pk_invbasdoc)) {
						for (int i = 0; i < o.length; i++) {
							if (pk_invbasdoc.toString().equals(o[i])) {
								this
										.showErrorMessage("添加的产品已经和其它系列绑定，请您先解除绑定关系！");
								return;
							}

						}
					}
				}
				/*
				 * ArrayList TbInvclBasVOcs = (ArrayList)
				 * query.retrieveByClause( TbInvclBasVO.class,
				 * sql_srcbd.toString()); AggregatedValueObject mybillVO =
				 * this.getChangedVOFromUI(); TbInvclBasVO[]
				 * tbInvclBasVOChangeVOs = (TbInvclBasVO[]) mybillVO
				 * .getChildrenVO(); if (null != TbInvclBasVOcs && null !=
				 * tbInvclBasVOChangeVOs) { for (int i = 0; i <
				 * TbInvclBasVOcs.size(); i++) { TbInvclBasVO tbStorcubasdocVO =
				 * (TbInvclBasVO) TbInvclBasVOcs .get(i); for (int j = 0; j <
				 * tbInvclBasVOChangeVOs.length; j++) { TbInvclBasVO
				 * tbinvclChangeVO = tbInvclBasVOChangeVOs[j]; if (null !=
				 * tbStorcubasdocVO.getPk_invbasdoc() && null !=
				 * tbinvclChangeVO.getPk_invbasdoc()) { if
				 * (tbStorcubasdocVO.getPk_invbasdoc().trim().equals(
				 * tbinvclChangeVO.getPk_invbasdoc().trim())) {
				 * this.showErrorMessage( "添加的产品已经和其它系列绑定，请您先解除绑定关系！"); return; } } } } }
				 */

			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}

		super.afterEdit(e);
	}

	public void valueChanged(ListSelectionEvent arg0) {
		// TODO Auto-generated method stub
		// int index = 0;
		// if (getBillListPanel().getHeadTable().getSelectedRow() != -1) {
		// index = getBillListPanel().getHeadTable().getSelectedRow();
		// AggregatedValueObject billvo = getBufferData().getVOByRowNo(index);
		// TbInvclVO invc = (TbInvclVO) billvo.getParentVO();
		// billvo.setChildrenVO(null);
		// String strWhere = " dr = 0 and pk_invcl = '" + invc.getPk_invcl()
		// + "' order by fr_order";
		// IUAPQueryBS iuap = (IUAPQueryBS) NCLocator.getInstance().lookup(
		// IUAPQueryBS.class.getName());
		// try {
		// ArrayList list = (ArrayList) iuap.retrieveByClause(
		// TbInvclBasVO.class, strWhere);
		// if (null != list && list.size() > 0) {
		// SuperVO[] item = new SuperVO[list.size()];
		// item = (SuperVO[]) list.toArray(item);
		// billvo.setChildrenVO(item);
		//					 
		//					
		// }
		//
		// } catch (BusinessException e) {
		// // TODO Auto-generated catch block
		//				e.printStackTrace();
		//			}
		//		}
	}

}
