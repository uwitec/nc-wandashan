package nc.ui.wds.ic.backin;

import java.awt.Container;
import java.util.ArrayList;
import java.util.List;

import javax.swing.event.ListSelectionEvent;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ArrayListProcessor;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.pf.BillSourceDLG;
import nc.ui.scm.pub.query.SCMQueryConditionDlg;
import nc.ui.wds.w8004040204.ssButtun.ISsButtun;
import nc.vo.ic.other.out.TbOutgeneralBVO;
import nc.vo.ic.other.out.TbOutgeneralHVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pub.SCMEnv;
import nc.vo.so.so001.SaleOrderVO;
import nc.vo.so.so001.SaleorderBVO;
import nc.vo.so.so001.SaleorderHVO;
import nc.vo.wl.pub.CommonUnit;

public class SaleOrderDlg extends BillSourceDLG {

	Container m_parent = null;

	// private boolean vdef6bool=false;

	@Override
	public void valueChanged(ListSelectionEvent e) {
		// TODO Auto-generated method stub
		if (getbillListPanel().getHeadTable().getSelectedRowCount() > 0) {

			// 获取所选择行的Id
			Object o = getbillListPanel().getHeadBillModel().getValueAt(
					getbillListPanel().getHeadTable().getSelectedRow(),
					"csaleid");
			// 判断是否为空
			if (o != null && o != "") {
				String csaleid = o.toString();
				for (int i = 0; i < this.saleVO.length; i++) {
					String saleid = this.saleVO[i].getHeadVO().getCsaleid();
					if (csaleid.equals(saleid)) {
						// 如果所选择的行id不为空 根据它去缓存里查询出子表Vo 进行显示
						getbillListPanel().setBodyValueVO(
								this.saleVO[i].getBodyVOs());
						break;
					}
				}

			}
		}

		// getbillListPanel().getHeadBillModel().execLoadFormula();
		getbillListPanel().getBodyBillModel().execLoadFormula();
	}

	public void onOk() {
		if (getbillListPanel().getHeadBillModel().getRowCount() > 0) {
			AggregatedValueObject[] selectedBillVOs = getbillListPanel()
					.getMultiSelectedVOs(SaleOrderVO.class.getName(),
							SaleorderHVO.class.getName(),
							SaleorderBVO.class.getName());
			if (selectedBillVOs.length > 1) {
				((MyClientUI) m_parent).showWarningMessage("一次只能选择一条信息!");
				return;
			}
			retBillVo = selectedBillVOs.length > 0 ? selectedBillVOs[0] : null;
			retBillVos = selectedBillVOs;
			String cumId = null;
			if (retBillVos != null && retBillVos.length > 0) {
				for (int i = 0; i < retBillVos.length; i++) {
					SaleorderHVO salehvo = (SaleorderHVO) retBillVos[i]
							.getParentVO();
					if (null == cumId || "".equals(cumId)) {
						cumId = salehvo.getCcustomerid();
					} else {
						if (!cumId.equals(salehvo.getCcustomerid())) {
							((MyClientUI) m_parent)
									.showWarningMessage("一次只能选择一条信息!");
							cumId = null;
							return;
						}
					}
				}
			}

		}
		this.closeOK();
	}

	private SCMQueryConditionDlg m_dlgQry = null;

	public SaleOrderVO[] saleVO;

	public SaleOrderDlg(String pkField, String pkCorp, String operator,
			String funNode, String queryWhere, String billType,
			String businessType, String templateId, String currentBillType,
			Container parent) {
		super(pkField, pkCorp, operator, funNode, queryWhere, billType,
				businessType, templateId, currentBillType, parent);
		// TODO Auto-generated constructor stub
	}

	public SaleOrderDlg(Container parent) {
		super(null, null, null, null, "1=1", "0208", null, null, null, parent);
		m_parent = parent;
		init();
	}

	private void init() {
		getbillListPanel().getHeadTable().getSelectionModel()
				.addListSelectionListener(this);
	}

	public AggregatedValueObject[] getReturnVOs(String pkCorp, String operator,
			String billType, String currentBillType, String funNode,
			String qrynodekey, java.awt.Container parent) {
		// 得到调用节点的查询对话框
		// funnode 40092010 申请单
		// qrynodekey 40099906 参照单据查询
		try {
			st_type = CommonUnit.getUserType(ClientEnvironment
					.getInstance().getUser().getPrimaryKey());
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (null != st_type && !"".equals(st_type)) {
			if ("0".equals(st_type) || "3".equals(st_type)) {
				try {
					stordocName = CommonUnit
							.getStordocName(ClientEnvironment.getInstance()
									.getUser().getPrimaryKey());
				} catch (BusinessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (null != stordocName && !"".equals(stordocName)) {
					try {
						sotckIsTotal = CommonUnit
								.getSotckIsTotal(stordocName);

					} catch (BusinessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
		m_dlgQry = getQueryDlg(pkCorp, funNode, operator, qrynodekey);

		if (m_dlgQry.showModal() == nc.ui.pub.beans.MessageDialog.ID_OK) {

			initVar(null, pkCorp, operator, null, "null", billType, null, null,
					currentBillType, null, parent);
			if (sotckIsTotal) {
				loadHeadData();
			} else {
				loadHeadData1();
			}
			addBillUI();
			setQueyDlg(m_dlgQry);
			if (showModal() == nc.ui.pub.beans.MessageDialog.ID_OK) {
				// 托盘指定按钮和自动取货按钮可用
				((MyClientUI) m_parent).getButtonManager().getButton(
						ISsButtun.zdqh).setEnabled(true);
				((MyClientUI) m_parent).getButtonManager().getButton(
						ISsButtun.tpzd).setEnabled(true);
				// 获取所选VO
				return getRetVos();
			}
		}
		return null;
	}

	private SCMQueryConditionDlg getQueryDlg(String pkCorp, String funNode,
			String operator, String qrynodekey) {
		if (m_dlgQry == null) {
			try {
				m_dlgQry = new nc.ui.scm.pub.query.SCMQueryConditionDlg(this);
				m_dlgQry.setTempletID(pkCorp, funNode, operator, null,
						qrynodekey);
				nc.vo.pub.query.QueryConditionVO[] voaConData = m_dlgQry
						.getConditionDatas();
				// 隐藏常用条件
				m_dlgQry.hideNormal();
				// 设置单据日期
				String sDate = ClientEnvironment.getInstance()
						.getBusinessDate().toString();
				m_dlgQry.setInitDate("head.dapplydate", sDate);

				ArrayList alCorpIDs = new ArrayList();
				alCorpIDs.add(pkCorp);
				// m_dlgQry.initCorpRef("head.pk_corp", pkCorp, alCorpIDs);
				// m_dlgQry.setCorpRefs("head.pk_corp",GenMethod.getDataPowerFieldFromDlgNotByProp(m_dlgQry));

			} catch (Exception e) {
				nc.vo.scm.pub.ctrl.GenMsgCtrl.handleException(e);
			}

		}

		return m_dlgQry;
	}

	// 判断用户身份
	private String st_type = "";
	// 判断是总仓还是分仓
	private boolean sotckIsTotal = true;
	// 仓库主键
	private String stordocName = "";

	// 总仓
	public void loadHeadData() {

		try {
			st_type = CommonUnit.getUserType(ClientEnvironment
					.getInstance().getUser().getPrimaryKey());
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (null != st_type && !"".equals(st_type)) {
			if ("0".equals(st_type) || "3".equals(st_type)) {
				try {
					stordocName = CommonUnit
							.getStordocName(ClientEnvironment.getInstance()
									.getUser().getPrimaryKey());
				} catch (BusinessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (null != stordocName && !"".equals(stordocName)) {
					try {
						sotckIsTotal = CommonUnit
								.getSotckIsTotal(stordocName);

					} catch (BusinessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}

		try {
			// 获取当前登录者查询人员绑定表 是属于哪个仓库，
			String pk_stock = CommonUnit.getStordocName(ClientEnvironment
					.getInstance().getUser().getPrimaryKey());
			// 利用产品组传入的条件与当前查询条件获得条件组成主表查询条件
			// 处理dplanbindate的查询条件
			nc.vo.pub.query.ConditionVO[] voCons = m_dlgQry.getConditionVO();

			StringBuffer sWhere = new StringBuffer(
					" dr = 0 and fstatus =2 and boutendflag ='N' and vreceiptcode like '%CO%退' and ( cwarehouseid is null or cwarehouseid ='"
							+ pk_stock
							+ "' ) "
							+ " and daudittime is not null ");
			if (voCons != null && voCons.length > 0 && voCons[0] != null) {
				sWhere.append(" and " + m_dlgQry.getWhereSQL(voCons));
			}

			// 聚合VO
			SaleOrderVO[] saleVO = null;
			SaleOrderVO salevo = null;
			// 表头VO
			SaleorderHVO[] salehVO = null;
			// 获取访问数据库对象
			IUAPQueryBS IUAPQueryBS = (IUAPQueryBS) NCLocator.getInstance()
					.lookup(IUAPQueryBS.class.getName());

			ArrayList resultlist = (ArrayList) IUAPQueryBS
					.executeQuery(
							"select csaleid,vreceiptcode ,ccustomerid ,creceiptcustomerid ,"
									+ "vreceiveaddress,cemployeeid,cbiztype,vnote ,"
									+ "dbilldate,dapprovedate,cdeptid,vdef6,"
									+ "daudittime,creceipttype,cwarehouseid from so_sale where "
									+ sWhere, new ArrayListProcessor());

			List invLisk = CommonUnit
					.getInvbasdoc_Pk(ClientEnvironment.getInstance().getUser()
							.getPrimaryKey());
			// 过滤本地表
			ArrayList resultlist2 = new ArrayList();
			if (resultlist != null && resultlist.size() > 0) {
				for (int i = 0; i < resultlist.size(); i++) {
					Object a[] = (Object[]) resultlist.get(i);
					if (null != a[0]) {
						String sql_tbout = " csourcebillhid='"
								+ a[0].toString() + "' and dr =0 ";
						ArrayList resultlista = (ArrayList) IUAPQueryBS
								.retrieveByClause(TbOutgeneralHVO.class,
										sql_tbout);
						if (resultlista != null && resultlista.size() > 0) {
							if (null != resultlista.get(0)) {
								String sql_tboutb = " general_pk='"
										+ ((TbOutgeneralHVO) resultlista.get(0))
												.getGeneral_pk()
										+ "' and cinventoryid in ('";

								for (int k = 0; k < invLisk.size(); k++) {
									if (null != invLisk && invLisk.size() > 0
											&& null != invLisk.get(k)
											&& !"".equals(invLisk.get(k))) {
										sql_tboutb += invLisk.get(k) + "','";
									}
								}
								sql_tboutb += "') and dr=0 ";
								ArrayList resultlistb = (ArrayList) IUAPQueryBS
										.retrieveByClause(
												TbOutgeneralBVO.class,
												sql_tboutb);
								if (resultlista != null
										&& resultlista.size() > 0) {
								} else {
									resultlist2.add(resultlist.get(i));
								}
							}
						} else {
							resultlist2.add(resultlist.get(i));

						}
					}
				}
			}
			// 过滤子表
			ArrayList resultlist1 = new ArrayList();
			if (resultlist2 != null && resultlist2.size() > 0) {
				for (int i = 0; i < resultlist2.size(); i++) {
					Object a[] = (Object[]) resultlist2.get(i);
					// 判断是否为空和它的第一个数据是否为空
					if (a != null && a.length > 0 && a[0] != null) {

						// 根据它的第一个数据查询子表
						ArrayList bodyList = (ArrayList) IUAPQueryBS
								.executeQuery(
										"select cinvbasdocid  from so_saleorder_b where csaleid='"
												+ a[0].toString()
												+ "' and dr=0 ",
										new ArrayListProcessor());
						// 是否显示
						boolean isShow = false;
						if (bodyList != null && bodyList.size() > 0) {
							for (int j = 0; j < bodyList.size(); j++) {

								Object b[] = (Object[]) bodyList.get(j);

								for (int k = 0; k < invLisk.size(); k++) {
									if (null != b[0]) {
										if (b[0].toString().equals(
												invLisk.get(k))) {

											isShow = true;

											// break;
										}
									}
								}
							}
						}
						if (isShow) {
							resultlist1.add(resultlist2.get(i));
						}
					}

				}
			}
			// 判断表头结果是否为空
			if (resultlist1 != null && resultlist1.size() > 0) {
				// 循环表头结果
				saleVO = new SaleOrderVO[resultlist1.size()];

				salehVO = new SaleorderHVO[resultlist1.size()];
				for (int i = 0; i < resultlist1.size(); i++) {

					salevo = new SaleOrderVO();

					// 获取表头的数据
					Object a[] = (Object[]) resultlist1.get(i);
					// 判断是否为空和它的第一个数据是否为空
					if (a != null && a.length > 0 && a[0] != null) {

						// 根据它的第一个数据查询子表
						ArrayList bodyList = (ArrayList) IUAPQueryBS
								.executeQuery(
										"select csaleid ,crowno ,cinvbasdocid ,nnumber ,"
												+ "npacknumber,corder_bid,cunitid,blargessflag  from so_saleorder_b where csaleid='"
												+ a[0].toString()
												+ "' and dr=0 ",
										new ArrayListProcessor());
						SaleorderHVO salehvo = new SaleorderHVO();

						if (null != a[0] && !"".equals(a[0])) {
							salehvo.setCsaleid(a[0].toString());
						}
						if (null != a[1] && !"".equals(a[1])) {
							salehvo.setVreceiptcode(a[1].toString());
						}
						if (null != a[2] && !"".equals(a[2])) {
							salehvo.setCcustomerid(a[2].toString());
						}
						if (null != a[3] && !"".equals(a[3])) {
							salehvo.setCreceiptcustomerid(a[3].toString());
						}
						if (null != a[4] && !"".equals(a[4])) {
							salehvo.setVreceiveaddress(a[4].toString());
						}
						if (null != a[5] && !"".equals(a[5])) {
							salehvo.setCemployeeid(a[5].toString());
						}
						if (null != a[6] && !"".equals(a[6])) {
							salehvo.setCbiztype(a[6].toString());
						}
						if (null != a[7] && !"".equals(a[7])) {
							salehvo.setVnote(a[7].toString());
						}
						if (null != a[8] && !"".equals(a[8])) {
							salehvo.setDbilldate(new UFDate(a[8].toString()));
						}
						if (null != a[9] && !"".equals(a[9])) {
							salehvo
									.setDapprovedate(new UFDate(a[9].toString()));
						}
						if (null != a[10] && !"".equals(a[10])) {
							salehvo.setCdeptid(a[10].toString());
						}
						if (null != a[12] && !"".equals(a[12])) {
							salehvo.setAttributeValue("daudittime", a[12]);

						}
						if (null != a[13] && !"".equals(a[13])) {
							salehvo.setCreceipttype(a[13].toString());
						}
						if (null != a[14] && !"".equals(a[14])) {
							salehvo.setCwarehouseid(a[14].toString());
						}
						saleVO[i] = salevo;

						SaleorderBVO[] salebVO = null;
						SaleorderBVO salebvo = null;
						boolean syfslbool = true;// 是否显示明细
						ArrayList mySalebVOs = new ArrayList();
						if (bodyList != null && bodyList.size() > 0) {
							salebVO = new SaleorderBVO[bodyList.size()];
							// 剩余数量不为零的子VO

							//
							// boolean mySyfslbool = true;
							// 循环子表结果
							for (int j = 0; j < bodyList.size(); j++) {

								Object b[] = (Object[]) bodyList.get(j);

								salebvo = new SaleorderBVO();
								// 判断是否为空和它的第一个数据是否为空
								if (b != null && b.length > 0 && b[0] != null) {

									if (null != b[0] && !"".equals(b[0])) {
										salebvo.setCsaleid(b[0].toString());
									}
									if (null != b[1] && !"".equals(b[1])) {
										salebvo.setCrowno(b[1].toString());
									}
									if (null != b[2] && !"".equals(b[2])) {
										salebvo
												.setCinvbasdocid(b[2]
														.toString());
									}
									// 根据单据完成情况，显示数量

									if (null != b[3] && !"".equals(b[3])) {
										salebvo.setNnumber((new UFDouble(b[3]
												.toString())));
									}
									if (null != b[4] && !"".equals(b[4])) {
										salebvo.setNpacknumber((new UFDouble(
												b[4].toString())));
									}

									if (null != b[5] && !"".equals(b[5])) {
										salebvo.setCorder_bid(b[5].toString());
									}
									if (null != b[6] && !"".equals(b[6])) {
										salebvo.setCunitid(b[6].toString());
									}
									if (null != b[7] && !"".equals(b[7])) {
										salebvo.setBlargessflag(new UFBoolean(
												b[7].toString()));
									}

								}
								salebVO[j] = salebvo;
								if (sotckIsTotal) {

									for (int k = 0; k < invLisk.size(); k++) {
										if (null != salebvo.getCinvbasdocid()) {
											if (salebvo.getCinvbasdocid()
													.equals(invLisk.get(k))) {

												mySalebVOs.add(salebvo);

												break;
											}
										}
									}
								} else {
									mySalebVOs.add(salebvo);
								}
								// if (syfslbool) {
								// mySalebVOs.add(salebvo);
								// }
							}
							// 重新添加子VO
							if (null != mySalebVOs && mySalebVOs.size() > 0) {
								salebVO = new SaleorderBVO[mySalebVOs.size()];
								for (int k = 0; k < mySalebVOs.size(); k++) {
									salebVO[k] = (SaleorderBVO) mySalebVOs
											.get(k);
								}

								saleVO[i].setChildrenVO(salebVO);
							}

						}

						if (null != mySalebVOs && mySalebVOs.size() > 0) {
							saleVO[i].setParentVO(salehvo);
							salehVO[i] = salehvo;
						}

					}
				}
			}

			this.setSaleVO(saleVO);
			getbillListPanel().getBillListData().setHeaderValueVO(salehVO);

			getbillListPanel().getHeadBillModel().execLoadFormula();
		} catch (Exception e) {
			SCMEnv.error("数据加载失败！");
			e.printStackTrace(System.out);
		}
	}

	// 分仓
	public void loadHeadData1() {

		try {
			st_type = CommonUnit.getUserType(ClientEnvironment
					.getInstance().getUser().getPrimaryKey());
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (null != st_type && !"".equals(st_type)) {
			if ("0".equals(st_type) || "3".equals(st_type)) {
				try {
					stordocName = CommonUnit
							.getStordocName(ClientEnvironment.getInstance()
									.getUser().getPrimaryKey());
				} catch (BusinessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (null != stordocName && !"".equals(stordocName)) {
					try {
						sotckIsTotal = CommonUnit
								.getSotckIsTotal(stordocName);

					} catch (BusinessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}

		try {
			// 获取当前登录者查询人员绑定表 是属于哪个仓库，
			String pk_stock = CommonUnit.getStordocName(ClientEnvironment
					.getInstance().getUser().getPrimaryKey());
			// 利用产品组传入的条件与当前查询条件获得条件组成主表查询条件
			// 处理dplanbindate的查询条件
			nc.vo.pub.query.ConditionVO[] voCons = m_dlgQry.getConditionVO();

			StringBuffer sWhere = new StringBuffer(
					" dr = 0 and fstatus =2 and boutendflag ='N' and vreceiptcode like '%CO%退' and ( cwarehouseid is null or cwarehouseid ='"
							+ pk_stock
							+ "' ) "
							+ " and daudittime is not null ");
			if (voCons != null && voCons.length > 0 && voCons[0] != null) {
				sWhere.append(" and " + m_dlgQry.getWhereSQL(voCons));
			}

			// 聚合VO
			SaleOrderVO[] saleVO = null;
			SaleOrderVO salevo = null;
			// 表头VO
			SaleorderHVO[] salehVO = null;
			// 获取访问数据库对象
			IUAPQueryBS IUAPQueryBS = (IUAPQueryBS) NCLocator.getInstance()
					.lookup(IUAPQueryBS.class.getName());

			ArrayList resultlist = (ArrayList) IUAPQueryBS.executeQuery(
					"select csaleid,vreceiptcode ,ccustomerid ,creceiptcustomerid ,"
							+ "vreceiveaddress,cemployeeid,cbiztype,vnote ,"
							+ "dbilldate,dapprovedate,cdeptid,vdef6,"
							+ "daudittime,creceipttype from so_sale where "
							+ sWhere, new ArrayListProcessor());
			// //过滤后存储的结果集
			// ArrayList<Object[]> list = new ArrayList<Object[]>();
			// // 进行过滤，制成订单的就不能再出现了。判断结果集是否为空
			// if (resultlist != null && resultlist.size() > 0) {
			// for (int i = 0; i < resultlist.size(); i++) {
			// // 获取表头的数据
			// Object a[] = (Object[]) resultlist.get(i);
			// // 判断是否为空和它的第一个数据是否为空
			// if (a != null && a.length > 0 && a[0] != null) {
			// // 根据查询出的销售主键做为条件进行查询，查看数据库中是否有此记录
			// String sql = "select count(csaleid) from tb_fydnew where billtype
			// = 1 and dr = 0 and csaleid ='"
			// + a[0].toString() + "'";
			// ArrayList pwb_count = (ArrayList) IUAPQueryBS
			// .executeQuery(sql, new ArrayListProcessor());
			// int countnum = Integer.parseInt((((Object[]) pwb_count
			// .get(0))[0]).toString());
			// if (countnum == 0) {
			// list.add(a);
			// }
			// }
			// }
			// }
			// 过滤本地表
			ArrayList resultlist2 = new ArrayList();
			if (resultlist != null && resultlist.size() > 0) {
				for (int i = 0; i < resultlist.size(); i++) {
					Object a[] = (Object[]) resultlist.get(i);
					if (null != a[0]) {
						String sql_tbout = " csourcebillhid='"
								+ a[0].toString() + "' and dr =0 ";
						ArrayList resultlista = (ArrayList) IUAPQueryBS
								.retrieveByClause(TbOutgeneralHVO.class,
										sql_tbout);
						if (resultlista != null && resultlista.size() > 0) {

						} else {
							resultlist2.add(resultlist.get(i));

						}
					}
				}
			}

			// ArrayList resultlist1 = new ArrayList();

			// 判断表头结果是否为空
			if (resultlist2 != null && resultlist2.size() > 0) {
				// 循环表头结果
				saleVO = new SaleOrderVO[resultlist2.size()];

				salehVO = new SaleorderHVO[resultlist2.size()];
				for (int i = 0; i < resultlist2.size(); i++) {

					salevo = new SaleOrderVO();

					// 获取表头的数据
					Object a[] = (Object[]) resultlist2.get(i);
					// 判断是否为空和它的第一个数据是否为空
					if (a != null && a.length > 0 && a[0] != null) {

						// 根据它的第一个数据查询子表
						ArrayList bodyList = (ArrayList) IUAPQueryBS
								.executeQuery(
										"select csaleid ,crowno ,cinvbasdocid ,nnumber ,"
												+ "npacknumber,corder_bid,cunitid,blargessflag  from so_saleorder_b where csaleid='"
												+ a[0].toString()
												+ "' and dr=0 ",
										new ArrayListProcessor());
						SaleorderHVO salehvo = new SaleorderHVO();

						if (null != a[0] && !"".equals(a[0])) {
							salehvo.setCsaleid(a[0].toString());
						}
						if (null != a[1] && !"".equals(a[1])) {
							salehvo.setVreceiptcode(a[1].toString());
						}
						if (null != a[2] && !"".equals(a[2])) {
							salehvo.setCcustomerid(a[2].toString());
						}
						if (null != a[3] && !"".equals(a[3])) {
							salehvo.setCreceiptcustomerid(a[3].toString());
						}
						if (null != a[4] && !"".equals(a[4])) {
							salehvo.setVreceiveaddress(a[4].toString());
						}
						if (null != a[5] && !"".equals(a[5])) {
							salehvo.setCemployeeid(a[5].toString());
						}
						if (null != a[6] && !"".equals(a[6])) {
							salehvo.setCbiztype(a[6].toString());
						}
						if (null != a[7] && !"".equals(a[7])) {
							salehvo.setVnote(a[7].toString());
						}
						if (null != a[8] && !"".equals(a[8])) {
							salehvo.setDbilldate(new UFDate(a[8].toString()));
						}
						if (null != a[9] && !"".equals(a[9])) {
							salehvo
									.setDapprovedate(new UFDate(a[9].toString()));
						}
						if (null != a[10] && !"".equals(a[10])) {
							salehvo.setCdeptid(a[10].toString());
						}
						if (null != a[12] && !"".equals(a[12])) {
							salehvo.setAttributeValue("daudittime", a[12]);

						}
						if (null != a[13] && !"".equals(a[13])) {
							salehvo.setCreceipttype(a[13].toString());
						}
						saleVO[i] = salevo;

						SaleorderBVO[] salebVO = null;
						SaleorderBVO salebvo = null;
						boolean syfslbool = true;// 是否显示明细
						ArrayList mySalebVOs = new ArrayList();
						if (bodyList != null && bodyList.size() > 0) {
							salebVO = new SaleorderBVO[bodyList.size()];
							// 剩余数量不为零的子VO

							//
							// boolean mySyfslbool = true;
							// 循环子表结果
							for (int j = 0; j < bodyList.size(); j++) {

								Object b[] = (Object[]) bodyList.get(j);

								salebvo = new SaleorderBVO();
								// 判断是否为空和它的第一个数据是否为空
								if (b != null && b.length > 0 && b[0] != null) {

									if (null != b[0] && !"".equals(b[0])) {
										salebvo.setCsaleid(b[0].toString());
									}
									if (null != b[1] && !"".equals(b[1])) {
										salebvo.setCrowno(b[1].toString());
									}
									if (null != b[2] && !"".equals(b[2])) {
										salebvo
												.setCinvbasdocid(b[2]
														.toString());
									}
									// 根据单据完成情况，显示数量

									if (null != b[3] && !"".equals(b[3])) {
										salebvo.setNnumber((new UFDouble(b[3]
												.toString())));
									}
									if (null != b[4] && !"".equals(b[4])) {
										salebvo.setNpacknumber((new UFDouble(
												b[4].toString())));
									}

									if (null != b[5] && !"".equals(b[5])) {
										salebvo.setCorder_bid(b[5].toString());
									}
									if (null != b[6] && !"".equals(b[6])) {
										salebvo.setCunitid(b[6].toString());
									}
									if (null != b[7] && !"".equals(b[7])) {
										salebvo.setBlargessflag(new UFBoolean(
												b[7].toString()));
									}

								}
								salebVO[j] = salebvo;

								if (syfslbool) {
									mySalebVOs.add(salebvo);
								}
							}
							// 重新添加子VO

							salebVO = new SaleorderBVO[mySalebVOs.size()];
							for (int k = 0; k < mySalebVOs.size(); k++) {
								salebVO[k] = (SaleorderBVO) mySalebVOs.get(k);
							}

							saleVO[i].setChildrenVO(salebVO);

						}

						saleVO[i].setParentVO(salehvo);
						salehVO[i] = salehvo;

					}
				}
			}

			this.setSaleVO(saleVO);
			getbillListPanel().getBillListData().setHeaderValueVO(salehVO);

			getbillListPanel().getHeadBillModel().execLoadFormula();
		} catch (Exception e) {
			SCMEnv.error("数据加载失败！");
			e.printStackTrace(System.out);
		}
	}

	public SaleOrderVO[] getSaleVO() {
		return saleVO;
	}

	public void setSaleVO(SaleOrderVO[] saleVO) {
		this.saleVO = saleVO;
	}
}
