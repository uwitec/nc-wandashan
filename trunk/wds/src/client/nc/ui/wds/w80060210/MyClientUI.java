package nc.ui.wds.w80060210;

import java.util.ArrayList;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ArrayListProcessor;
import nc.ui.pub.bill.BillCardBeforeEditListener;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillItemEvent;
import nc.ui.trade.manage.ManageEventHandler;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.wds.w80021020.TbYslcglVO;
import nc.vo.wds.w80060804.TbTranscompanyVO;
import nc.vo.wds.w8006080802.TbFreightstandradBTVO;
import nc.vo.wds.w8006080804.TbFreightstandradBVO;
import nc.vo.wds.w8006080806.TbEvenstoreHVO;

/**
 * <b> �ڴ˴���Ҫ��������Ĺ��� </b>
 * 
 * <p>
 * �ڴ˴���Ӵ����������Ϣ
 * </p>
 * 
 * 
 * @author author
 * @version tempProject version
 */
public class MyClientUI extends AbstractMyClientUI implements
		BillCardBeforeEditListener {

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
		getBillCardPanel().setBillBeforeEditListenerHeadTail(this);
	}

	public boolean beforeEdit(BillItemEvent e) {
		// TODO Auto-generated method stub
		if (getBillCardPanel().isEnabled() == false) {
			return false;
		}

		// if ("pk_yjzbzj".equals(e.getItem().getKey())) {
		// String cif_pk = (String) this.getBillCardPanel().getHeadItem(
		// "cif_pk").getValueObject();
		// Object fyd_yjzl = this.getBillCardPanel().getHeadItem("fyd_yjzl")
		// .getValueObject();
		//
		// if (cif_pk != null && cif_pk.length() > 0 && fyd_yjzl != null) {
		// // �õ���ͬ����
		// UIRefPane panel = (UIRefPane) this.getBillCardPanel()
		// .getHeadItem("pk_yjzbzj").getComponent();
		// UIRefPane panelp = (UIRefPane) this.getBillCardPanel()
		// .getHeadItem("yjby1").getComponent();
		// UIRefPane panelt = (UIRefPane) this.getBillCardPanel()
		// .getHeadItem("yjby2").getComponent();
		// if (Integer.parseInt(fyd_yjzl + "") == 0) {
		// panel.setRefNodeName(panelt.getRefNodeName());
		// panel.setRefModel(panelt.getRefModel());
		// panel.getRefModel().setWherePart(
		// " tc_pk = '" + cif_pk + "' and dr=0 ");
		// }
		// if (Integer.parseInt(fyd_yjzl + "") == 1) {
		// panel.setRefNodeName(panelp.getRefNodeName());
		// panel.setRefModel(panelp.getRefModel());
		// panel.getRefModel().setWherePart(
		// " tc_pk = '" + cif_pk + "' and dr=0 ");
		// }
		//
		// } else {
		//
		// this.showErrorMessage("��û��ѡ�����乫˾���˼����࣬������ʾ�κλ�λ");
		// UIRefPane panel = (UIRefPane) this.getBillCardPanel()
		// .getHeadItem("pk_yjzbzj").getComponent();
		// // ���Ͽͻ���Ϊ����ȥ����
		// panel.getRefModel().setWherePart(" dr=0 and 1=2 ");
		//
		// }
		// }

		return true;
	}

	@Override
	public void afterEdit(BillEditEvent e) {
		// TODO Auto-generated method stub

		if ("tc_pk".equals(e.getKey())) {
			IUAPQueryBS query = (IUAPQueryBS) NCLocator.getInstance().lookup(
					IUAPQueryBS.class.getName());
			Object tc_pk = this.getBillCardPanel().getHeadItem("tc_pk")
					.getValueObject();
			String tc_pka = "";
			if (null == tc_pk || "".equals(tc_pk)) {
				this.showErrorMessage("����ѡ�����乫˾��");
				return;
			} else {
				tc_pka = tc_pk.toString();
			}

			// ����վ
			String dhz = "";
			// �ͻ�����
			String ckehuzhujian = "";
			// ���ݺ� CO-G��ͷ��Ϊ�����
			Object vbillno = this.getBillCardPanel().getHeadItem("vbillno")
					.getValueObject();
			// ���˵�����
			Object fyd_pk = this.getBillCardPanel().getHeadItem("fyd_pk")
					.getValueObject();
			// ����վ����
			String srl_pk = "";
			if (null != this.getBillCardPanel().getHeadItem("srl_pk")
					.getValueObject()
					&& !"".equals(this.getBillCardPanel().getHeadItem("srl_pk")
							.getValueObject())) {
				srl_pk = this.getBillCardPanel().getHeadItem("srl_pk")
						.getValueObject().toString();
			} else {
				this.showErrorMessage("��������û�з���վ��");
				return;
			}
			/*
			 * // ������ݿ��еĹ����� String str = "select fyd_yslc from tb_fydnew where
			 * fyd_pk='" + fyd_pk.toString() + "' "; ArrayList aa = null; Object
			 * oo = null; try { aa = (ArrayList) query.executeQuery(str, new
			 * ArrayListProcessor());
			 * System.out.println(aa+"aaaaaaaaaaa"+aa.size()+"bbbbbbbbb");
			 * Object[] o = (Object[])(aa.get(0)); oo = o[0]; } catch
			 * (BusinessException e2) { // TODO Auto-generated catch block
			 * e2.printStackTrace(); }
			 */
			// ��������
			Object billtype = this.getBillCardPanel().getHeadItem("billtype")
					.getValueObject();
			// ���ݺ�ǰ4λ
			String billno = "";
			if (null != vbillno) {
				billno = (vbillno.toString()).substring(0, 4);
			}
			int billtypei = 0;
			if (null != billtype) {
				billtypei = Integer.parseInt(billtype.toString());
			}
			// 0Ϊ����
			if (billtypei == 0 ||billtypei==8) {
				Object cdaohuozhan = this.getBillCardPanel().getHeadItem(
						"cdaohuozhan").getValueObject();
				if (null != cdaohuozhan && !"".equals(cdaohuozhan)) {
					dhz = cdaohuozhan.toString();
					// ����վ����
					String srl_pkr = "";
					if (null != this.getBillCardPanel().getHeadItem("srl_pkr")
							.getValueObject()
							&& !"".equals(this.getBillCardPanel().getHeadItem(
									"srl_pkr").getValueObject())) {

						srl_pkr = this.getBillCardPanel()
								.getHeadItem("srl_pkr").getValueObject()
								.toString();
						String kmsql = "select km from tb_yslcgl where tb_yslcgl.leixing=2 and tb_yslcgl.dr=0 and tb_yslcgl.station_a='"
								+ srl_pk
								+ "' and tb_yslcgl.station_b='"
								+ srl_pkr + "'";
						ArrayList arr = null;
						try {
							arr = (ArrayList) query.executeQuery(kmsql,
									new ArrayListProcessor());
							// ������
							double km = 0;
							if (arr == null || arr.size() == 0) {
								this.showErrorMessage("û�ж�Ӧ������,����д������");
								getBillCardPanel().getHeadItem("fyd_yslc")
										.setValue("");
								getBillCardPanel().getHeadItem("fyd_yslc")
										.setEnabled(true);
							} else if (arr.size() > 1) {
								this.showErrorMessage("����վ���ظ���");
								return;
							} else {
								km = Double
										.parseDouble(((Object[]) arr.get(0))[0]
												.toString());
								// ��ʾ������
								getBillCardPanel().setHeadItem("fyd_yslc", km);
							}

							//
							/*
							 * if (null != arr && arr.size() > 0) { if (null ==
							 * (Object[])arr.get(0)) { String
							 * ddd=((Object[])arr.get(0))[0].toString(); //
							 * ��ʾ������ getBillCardPanel().setHeadItem("fyd_yslc",
							 * Double.parseDouble(ddd)); }else{
							 * this.showErrorMessage("û�ж�Ӧ��������"); return; } }
							 * else { this.showErrorMessage("û�ж�Ӧ��������"); return; }
							 */
						} catch (BusinessException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}

					} else {
						this.showErrorMessage("��������û�е���վ��");
						return;
					}
				} else {
					this.showErrorMessage("����վ�����ڣ�");
					return;
				}
			} else {

				// ����
				Object pk_kh = this.getBillCardPanel().getHeadItem("pk_kh")
						.getValueObject();

				StringBuffer ckehuzhujiansql = new StringBuffer(
						"select pk_cubasdoc from  bd_cumandoc where  pk_cumandoc='");
				if (null != pk_kh && !"".equals(pk_kh)) {
					ckehuzhujiansql.append(pk_kh.toString());
				} else {
					this.showErrorMessage("�ͻ������ڣ�");
					return;
				}
				ckehuzhujiansql.append("' and dr=0 ");
				// ��ѯ�ͻ������ļ���
				ArrayList os = new ArrayList();

				try {
					os = (ArrayList) query.executeQuery(ckehuzhujiansql
							.toString(), new ArrayListProcessor());
				} catch (BusinessException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				if (null != os && os.size() > 0) {
					if (null != os.get(0)) {
						if (null != ((Object[]) os.get(0))[0]) {
							ckehuzhujian = ((Object[]) os.get(0))[0].toString();
						} else {
							this.showErrorMessage("�ͻ������ڣ�");
							return;
						}

					} else {
						this.showErrorMessage("�ͻ������ڣ�");
						return;
					}
				} else {
					this.showErrorMessage("�ͻ������ڣ�");
					return;
				}

				// ����վ�����ͻ�����������ע
				StringBuffer dhzsql = new StringBuffer(
						"select memo from bd_cubasdoc where pk_cubasdoc ='");
				dhzsql.append(ckehuzhujian);
				dhzsql.append("' and dr=0 ");
				// ����վ����
				ArrayList dhzs = new ArrayList();

				try {
					dhzs = (ArrayList) query.executeQuery(dhzsql.toString(),
							new ArrayListProcessor());
				} catch (BusinessException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				if (null != dhzs && dhzs.size() > 0) {
					if (null != dhzs.get(0)) {
						if (null != ((Object[]) dhzs.get(0))[0]) {
							dhz = ((Object[]) dhzs.get(0))[0].toString();
						} else {
							this.showErrorMessage("����վ������,��ά���õ���վ��");
							return;
						}

					} else {
						this.showErrorMessage("����վ������,��ά���õ���վ��");
						return;
					}
				} else {
					this.showErrorMessage("����վ������,��ά���õ���վ��");
					return;
				}

				// ����ۣ���ѯ������������
				if ("CO-G".equals(billno)) {
					String kmsql = "select km from tb_yslcgl where tb_yslcgl.leixing=1 and tb_yslcgl.dr=0 and tb_yslcgl.station_a='"
							+ srl_pk
							+ "' and tb_yslcgl.station_b='"
							+ dhz
							+ "'";
					ArrayList arr = null;
					try {
						arr = (ArrayList) query.executeQuery(kmsql,
								new ArrayListProcessor());

						// ������
						double km = 0;
						if (arr == null || arr.size() == 0) {
							this.showErrorMessage("û�ж�Ӧ������,����д������");
							getBillCardPanel().getHeadItem("fyd_yslc")
									.setValue("");
							getBillCardPanel().getHeadItem("fyd_yslc")
									.setEnabled(true);
						} else if (arr.size() > 1) {
							this.showErrorMessage("����վ���ظ���");
							return;
						} else {
							km = Double.parseDouble(((Object[]) arr.get(0))[0]
									.toString());
							// ��ʾ������
							getBillCardPanel().setHeadItem("fyd_yslc", km);
						}

					} catch (BusinessException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				// ʣ�µĶ��ǳ��˴���۵����۶���
				else {
					// ͨ������վ�͵���վ�õ���̹��������������
					String yslcglsql = "select km from tb_yslcgl where tb_yslcgl.leixing=0 and tb_yslcgl.dr=0 and tb_yslcgl.station_a='"
							+ srl_pk
							+ "' and tb_yslcgl.station_b='"
							+ dhz
							+ "'";
					System.out.println(yslcglsql);
					// ��̼���
					ArrayList yslcgls = new ArrayList();
					try {
						yslcgls = (ArrayList) query.executeQuery(yslcglsql,
								new ArrayListProcessor());
					} catch (BusinessException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					// �������
					String pk_yslcgl = "";
					// ������
					double km = 0;
					if (yslcgls == null || yslcgls.size() == 0) {
						this.showErrorMessage("û�ж�Ӧ������,����д������");
						getBillCardPanel().getHeadItem("fyd_yslc").setValue("");
						getBillCardPanel().getHeadItem("fyd_yslc").setEnabled(
								true);
					} else if (yslcgls.size() > 1) {
						this.showErrorMessage("����վ���ظ���");
						return;
					} else {
						km = Double.parseDouble(((Object[]) yslcgls.get(0))[0]
								.toString());
						// ��ʾ������
						getBillCardPanel().setHeadItem("fyd_yslc", km);
					}

				}
			}

			// ��ѯ���乫˾VO
			ArrayList tbTranscompanyVOs = new ArrayList();
			String tc_pksql = " tc_pk='" + tc_pka + "' and dr=0";
			try {
				tbTranscompanyVOs = (ArrayList) query.retrieveByClause(
						TbTranscompanyVO.class, tc_pksql);
			} catch (BusinessException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			if (null != tbTranscompanyVOs && tbTranscompanyVOs.size() > 0) {
				TbTranscompanyVO tbTranscompanyVO = (TbTranscompanyVO) tbTranscompanyVOs
						.get(0);
				if (null != tbTranscompanyVO) {
				} else {
					this.showErrorMessage("���乫˾�����ڣ�");
					return;
				}

			} else {
				this.showErrorMessage("���乫˾�����ڣ�");
				return;

			}
		}

	}

}
