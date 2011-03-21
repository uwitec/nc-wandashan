package nc.ui.wds.w80020206;

import java.util.ArrayList;
import java.util.List;

import com.ibm.db2.jcc.a.a;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.wds.w80020206.Iw80020206;
import nc.itf.wds.w8004040210.Iw8004040210;
import nc.jdbc.framework.processor.ArrayListProcessor;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.UIDialog;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.ui.trade.query.INormalQuery;
import nc.ui.wds.w8000.CommonUnit;
import nc.vo.ic.pub.bill.GeneralBillHeaderVO;
import nc.vo.ic.pub.bill.GeneralBillItemVO;
import nc.vo.ic.pub.bill.GeneralBillVO;
import nc.vo.ic.pub.locator.LocatorVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessRuntimeException;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.wds.w8004040204.TbOutgeneralBVO;
import nc.vo.wds.w8004040204.TbOutgeneralHVO;
import nc.vo.wds.w80060406.TbFydnewVO;
import nc.vo.wds.w80060604.SoSaleVO;
import nc.vo.wds.w80060604.SoSaleorderBVO;
import nc.vo.wds.w80060608.IcGeneralBVO;
import nc.vo.wds.w80060608.IcGeneralHVO;

/**
 * 
 * ������AbstractMyEventHandler�������ʵ���࣬ ��Ҫ�������˰�ť��ִ�ж������û����Զ���Щ����������Ҫ�����޸�
 * 
 * @author author
 * @version tempProject version
 */

public class MyEventHandler extends AbstractMyEventHandler {

	public MyEventHandler(BillManageUI billUI, IControllerBase control) {
		super(billUI, control);
		getButtonManager().getButton(
				nc.ui.wds.w80020206.buttun0206.ISsButtun.Qzqr)
				.setEnabled(false);
		getButtonManager().getButton(
				nc.ui.wds.w80020206.buttun0206.ISsButtun.Qxqz)
				.setEnabled(false);
	}

	@Override
	protected void onBoQuery() throws Exception {
		// TODO Auto-generated method stub
		StringBuffer strWhere = new StringBuffer();

		if (askForQueryCondition(strWhere) == false)
			return;// �û������˲�ѯ

		strWhere.append(" and ( vdef6='2' or vdef6='3' ) and vdef5='2' ");

		SuperVO[] queryVos = queryHeadVOs(strWhere.toString());

		getBufferData().clear();
		// �������ݵ�Buffer
		addDataToBuffer(queryVos);

		updateBuffer();
	}

	protected boolean askForQueryCondition(StringBuffer sqlWhereBuf)
			throws Exception {
		if (sqlWhereBuf == null)
			throw new IllegalArgumentException(
					"askForQueryCondition().sqlWhereBuf cann't be null");
		UIDialog querydialog = getQueryUI();

		if (querydialog.showModal() != UIDialog.ID_OK)
			return false;
		INormalQuery query = (INormalQuery) querydialog;

		String strWhere = query.getWhereSql();
		if (strWhere == null)
			strWhere = "1=1";

		if (getButtonManager().getButton(IBillButton.Busitype) != null) {
			if (getBillIsUseBusiCode().booleanValue())
				// ҵ�����ͱ���
				strWhere = "(" + strWhere + ") and "
						+ getBillField().getField_BusiCode() + "='"
						+ getBillUI().getBusicode() + "'";

			else
				// ҵ������
				strWhere = "(" + strWhere + ") and "
						+ getBillField().getField_Busitype() + "='"
						+ getBillUI().getBusinessType() + "'";

		}

		strWhere = "(" + strWhere + ") and (isnull(dr,0)=0)";

		// if (getHeadCondition() != null)
		// strWhere = strWhere + " and " + getHeadCondition();
		// ��������ֱ�Ӱ����ƴ�õĴ��ŵ�StringBuffer�ж���ȥ�Ż�ƴ���Ĺ���
		sqlWhereBuf.append(strWhere);
		return true;
	}

	IUAPQueryBS iuap = (IUAPQueryBS) NCLocator.getInstance().lookup(
			IUAPQueryBS.class.getName());

	@Override
	protected void onQzqr() throws Exception {
		// TODO Auto-generated method stub
		if (getBufferData().getCurrentRow() >= 0) {
			SoSaleVO generalh = (SoSaleVO) getBufferData().getCurrentVO()
					.getParentVO();
			TbFydnewVO[] tbFydnewVOs = (TbFydnewVO[]) getBufferData()
					.getCurrentVO().getChildrenVO();

			for (int i = 0; i < tbFydnewVOs.length; i++) {
				if (null != tbFydnewVOs[i]) {
					if (null != tbFydnewVOs[i].getFyd_constatus()) {
						if (0 == tbFydnewVOs[i].getFyd_constatus()) {
							getBillUI().showErrorMessage("�˵�δȷ�ϣ�����ǩ�֣�");
							return;
						}
					} else {
						getBillUI().showErrorMessage("�˵�δȷ�ϣ�����ǩ�֣�");
						return;
					}
				} else {
					getBillUI().showErrorMessage("�ӱ��д�����Ϣ�����ܹرգ�");
					return;
				}
			}

			Object result = iuap.retrieveByPK(SoSaleVO.class, generalh
					.getCsaleid());
			if (null != result) {
				generalh = (SoSaleVO) result;
				if (null != generalh.getVdef6()) {
					if ("3".equals(generalh.getVdef6())) {
						getBillUI().showErrorMessage("�õ����Ѿ�ǩ��");
						return;
					} else {
						generalh.setVdef6("3");
						AggregatedValueObject billvo = changeReqOutgeneraltoGeneral();
						Iw80020206 iw = (Iw80020206) NCLocator.getInstance()
								.lookup(Iw80020206.class.getName());
						iw.whs_processAction80020206("PUSHSAVE", "SIGN", "4C",
								_getDate().toString(), billvo, generalh);

						getBillUI().showHintMessage("ǩ�ֳɹ�");
						getButtonManager().getButton(
								nc.ui.wds.w80020206.buttun0206.ISsButtun.Qzqr)
								.setEnabled(false);
						getButtonManager().getButton(
								nc.ui.wds.w80020206.buttun0206.ISsButtun.Qxqz)
								.setEnabled(true);
						// getBufferData().getCurrentVO().setParentVO(generalh);
						// updateBuffer();
						super.onBoRefresh();
						return;
					}
				} else {
					getBillUI().showErrorMessage("�õ���״̬����");
					return;
				}

				// if (!"3".equals(generalh.getVdef6())) {
				//
				//					
				// }

			}
		}

	}

	/**
	 * ת�� �ѵ�ǰҳ���е�VOת����ERP�еĳ��ⵥ�ۺ�OV ���÷��� ���л�дERP�г��ⵥ
	 * 
	 * @return ERP�г��ⵥ�ۺ�VO
	 * @throws Exception
	 */
	public GeneralBillVO changeReqOutgeneraltoGeneral() throws Exception {
		if (getBufferData().getCurrentRow() < 0) {
			getBillUI().showErrorMessage("��ѡ���ͷ����ǩ��");
			return null;
		}
		// ���س���� ��ͷ
		SoSaleVO soSaleVO = (SoSaleVO) getBufferData().getCurrentVO()
				.getParentVO();
		// ���س���� ����
		TbFydnewVO[] tbFydnewVOs = (TbFydnewVO[]) getBufferData()
				.getCurrentVO().getChildrenVO();

		// ����ۺ�VO
		GeneralBillVO gBillVO = new GeneralBillVO();
		// �����ͷVO
		GeneralBillHeaderVO generalhvo = null;
		// �����ӱ���
		List<GeneralBillItemVO> generalBVOList = new ArrayList<GeneralBillItemVO>();
		// �����ӱ�VO����
		GeneralBillItemVO[] generalBVO = null;
		// Object o = myClientUI.getBillCardPanel().getBodyValueAt(0,
		// "cfirstbillhid");
		if (null != soSaleVO.getCsaleid() && !"".equals(soSaleVO.getCsaleid())) {
			// String sWhere = " dr = 0 and csaleid = '"
			// + outbvo[0].getCfirstbillhid() + "'";
			// ArrayList list = (ArrayList)
			// iuap.retrieveByClause(SoSaleVO.class,
			// sWhere);
			// if (null != list && list.size() > 0) {
			// SoSaleVO salehvo = (SoSaleVO) list.get(0);
			generalhvo = new GeneralBillHeaderVO();

			if (null != tbFydnewVOs && null != tbFydnewVOs[0]) {
				// �����ⵥ��ͷ��ֵ
				TbFydnewVO tbFydnewVO = tbFydnewVOs[0];
				String fyd_sql = " csourcebillhid='" + tbFydnewVO.getFyd_pk()
						+ "' and dr=0 ";
				TbOutgeneralHVO tbOutgeneralHVO = new TbOutgeneralHVO();
				ArrayList list = (ArrayList) iuap.retrieveByClause(
						TbOutgeneralHVO.class, fyd_sql);
				if (null != list && null != list.get(0)) {
					tbOutgeneralHVO = (TbOutgeneralHVO) list.get(0);
				}
				generalhvo.setPk_corp(soSaleVO.getPk_corp());// ��˾����
				generalhvo.setCbiztypeid(soSaleVO.getCbiztype());// ҵ������
				generalhvo.setCbilltypecode("4C");// ��浥�����ͱ���
				generalhvo.setVbillcode(CommonUnit.getBillCode("4C",
						ClientEnvironment.getInstance().getCorporation()
								.getPk_corp(), "", ""));// ���ݺ�
				generalhvo.setDbilldate(getBillUI()._getDate());// ��������
				generalhvo.setCwarehouseid(tbOutgeneralHVO.getSrl_pk());// �ֿ�ID
				generalhvo.setCdispatcherid(tbOutgeneralHVO.getCdispatcherid());// �շ�����ID
				generalhvo.setCdptid(soSaleVO.getCdeptid());// ����ID
				generalhvo.setCwhsmanagerid(tbOutgeneralHVO.getCwhsmanagerid());// ���ԱID
				generalhvo.setCoperatorid(tbOutgeneralHVO.getCoperatorid());// �Ƶ���
				generalhvo.setAttributeValue("coperatoridnow", tbOutgeneralHVO
						.getCoperatorid());// ������
				// generalhvo.setCinventoryid(outbvo[0].getCinventoryid());//
				// ���ID
				generalhvo.setAttributeValue("csalecorpid", soSaleVO
						.getCsalecorpid());// ������֯
				generalhvo.setCcustomerid(soSaleVO.getCcustomerid());// �ͻ�ID
				generalhvo.setVdiliveraddress(soSaleVO.getVreceiveaddress());// ���˵�ַ
				generalhvo.setCbizid(soSaleVO.getCemployeeid());// ҵ��ԱID
				generalhvo.setVnote(soSaleVO.getVnote());// ��ע
				generalhvo.setFbillflag(2);// ����״̬
				generalhvo.setPk_calbody(soSaleVO.getCcalbodyid());// �����֯PK
				generalhvo.setAttributeValue("clastmodiid", tbOutgeneralHVO
						.getClastmodiid());// ����޸���
				generalhvo.setAttributeValue("tlastmoditime", tbOutgeneralHVO
						.getTlastmoditime());// ����޸�ʱ��
				generalhvo.setAttributeValue("tmaketime", tbOutgeneralHVO
						.getTmaketime());// �Ƶ�ʱ��
				generalhvo.setPk_cubasdocC(tbOutgeneralHVO.getPk_cubasdocc());// �ͻ���������ID

				String sql_sosaleorder = " csaleid='" + soSaleVO.getCsaleid()
						+ "' and dr=0 ";
				ArrayList sosaleorders = (ArrayList) iuap.retrieveByClause(
						SoSaleorderBVO.class, sql_sosaleorder);
				// �����帳ֵ
				for (int i = 0; i < sosaleorders.size(); i++) {
					// ���ݱ��帽��--��λ
					LocatorVO locatorvo = new LocatorVO();
					locatorvo.setPk_corp(soSaleVO.getPk_corp());
					boolean isBatch = false;
					if (null != sosaleorders) {
						// sWhere = " dr = 0 and corder_bid = '"
						// + outbvo[i].getCfirstbillbid() + "'";
						// ArrayList saleblist = (ArrayList) iuap
						// .retrieveByClause(SoSaleorderBVO.class, sWhere);
						if (null != sosaleorders.get(i)) {
							SoSaleorderBVO saleorderbvo = (SoSaleorderBVO) sosaleorders
									.get(i);
							GeneralBillItemVO generalb = new GeneralBillItemVO();
							generalb.setAttributeValue("pk_corp", saleorderbvo
									.getPk_corp());// ��˾
							generalb.setCinvbasid(saleorderbvo
									.getCinvbasdocid());// �������ID
							generalb.setCinventoryid(saleorderbvo
									.getCinventoryid());// ���ID
							generalb.setVbatchcode("2009");// ���κ�
							// ��ѯ�������ں�ʧЧ����
							String sql = "select dproducedate ,dvalidate  from scm_batchcode where pk_invbasdoc='"
									+ saleorderbvo.getCinvbasdocid()
									+ "' and vbatchcode='"
									+ "2009"
									+ "' and dr=0";
							ArrayList batchList = (ArrayList) iuap
									.executeQuery(sql, new ArrayListProcessor());
							if (null != batchList && batchList.size() > 0) {
								Object[] batch = (Object[]) batchList.get(0);
								// ��������
								if (null != batch[0] && !"".equals(batch[0]))
									generalb.setScrq(new UFDate(batch[0]
											.toString()));
								// ʧЧ����
								if (null != batch[0] && !"".equals(batch[0]))
									generalb.setDvalidate(new UFDate(batch[1]
											.toString()));
								isBatch = true;
							}
							generalb
									.setDbizdate(tbOutgeneralHVO.getDbilldate());// ҵ������
							generalb
									.setNshouldoutnum(saleorderbvo.getNnumber());// Ӧ������
							// ���г��ⵥ�ӱ�VO
							String sql_outbvo = " cfirstbillhid='"
									+ soSaleVO.getCsaleid()
									+ "' and cinventoryid='"
									+ saleorderbvo.getCinvbasdocid()
									+ "' and dr=0 ";

							ArrayList outbvos = (ArrayList) iuap
									.retrieveByClause(TbOutgeneralBVO.class,
											sql_outbvo);
							double noutnum = 0;
							double noutassistnum = 0;
							if (null != outbvos) {
								for (int j = 0; j < outbvos.size(); j++) {
									if (null != outbvos.get(j)) {
										TbOutgeneralBVO outbvo = (TbOutgeneralBVO) outbvos
												.get(j);
										if (null != outbvo.getNoutnum()
												&& null != outbvo
														.getNoutassistnum()) {
											noutnum += outbvo.getNoutnum()
													.doubleValue();
											noutassistnum += outbvo
													.getNoutassistnum()
													.doubleValue();
										}
									}
								}
							}

							if (noutnum == 0 || noutassistnum == 0) {
								isBatch = false;
							}

							// TbOutgeneralBVO outbvo = new TbOutgeneralBVO();
							// if (null != outbvos && null != outbvos.get(0)) {
							// outbvo = (TbOutgeneralBVO) outbvos.get(0);
							// }
							generalb.setNshouldoutassistnum(saleorderbvo
									.getNpacknumber());// Ӧ��������
							generalb.setNoutnum(new UFDouble(noutnum));// ʵ������
							locatorvo.setNoutspacenum(new UFDouble(noutnum));
							generalb.setNoutassistnum(new UFDouble(
									noutassistnum));// ʵ��������
							locatorvo.setNoutspaceassistnum(new UFDouble(
									noutassistnum));
							// ������Դ���ݱ�ͷ�����ͻ�����������õ���λID
							String sql_cspaceid = "select distinct cspaceid from tb_outgeneral_b where"
									+ " cfirstbillhid='"
									+ soSaleVO.getCsaleid()
									+ "' and cinventoryid='"
									+ saleorderbvo.getCinvbasdocid()
									+ "' and dr=0 ";
							ArrayList cspaceidList = (ArrayList) iuap
									.executeQuery(sql_cspaceid,
											new ArrayListProcessor());
							if (null != cspaceidList && cspaceidList.size() > 0) {
								Object[] cspaceid = (Object[]) cspaceidList
										.get(0);
								if (null != cspaceid[0]
										&& !"".equals(cspaceid[0])) {
									locatorvo.setCspaceid(cspaceid[0]
											.toString());// ��λID
								}

							}
							generalb.setCastunitid(saleorderbvo
									.getCpackunitid());// ��������λID
							generalb.setNprice(saleorderbvo.getNprice());// ����
							generalb.setNmny(saleorderbvo.getNmny());// ���
							generalb.setCsourcebillhid(soSaleVO.getCsaleid());// ��Դ���ݱ�ͷ���к�
							generalb.setCfirstbillhid(soSaleVO.getCsaleid());// Դͷ���ݱ�ͷID
							generalb.setCfreezeid(soSaleVO.getCsaleid());// ������Դ
							generalb.setCsourcebillbid(saleorderbvo
									.getCorder_bid());// ��Դ���ݱ������к�
							generalb.setCfirstbillbid(saleorderbvo
									.getCorder_bid());// Դͷ���ݱ���ID
							generalb.setCsourcetype(soSaleVO.getCreceipttype());// ��Դ��������
							generalb.setCfirsttype(soSaleVO.getCreceipttype());// Դͷ��������
							generalb.setVsourcebillcode(soSaleVO
									.getVreceiptcode());// ��Դ���ݺ�
							generalb.setVfirstbillcode(soSaleVO
									.getVreceiptcode());// Դͷ���ݺ�
							generalb.setVsourcerowno(saleorderbvo.getCrowno());// ��Դ�����к�
							generalb.setVfirstrowno(saleorderbvo.getCrowno());// Դͷ�����к�
							generalb
									.setFlargess(saleorderbvo.getBlargessflag());// �Ƿ���Ʒ
							generalb.setDfirstbilldate(soSaleVO.getDmakedate());// Դͷ�����Ƶ�����
							generalb.setCreceieveid(saleorderbvo
									.getCreceiptcorpid());// �ջ���λ
							generalb.setCrowno(saleorderbvo.getCrowno());// �к�
							generalb.setHsl(saleorderbvo.getNquoteunitrate());// ������
							generalb.setNsaleprice(saleorderbvo.getNnetprice());// ���ۼ۸�
							generalb.setNtaxprice(saleorderbvo.getNtaxprice());// ��˰����
							generalb.setNtaxmny(saleorderbvo.getNtaxmny());// ��˰���
							generalb.setNsalemny(saleorderbvo.getNmny());// ����˰���
							generalb.setAttributeValue("cquotecurrency",
									saleorderbvo.getCcurrencytypeid());// ���ñ���
							LocatorVO[] locatorVO = new LocatorVO[] { locatorvo };
							generalb.setLocator(locatorVO);
							if (isBatch)
								// �������ӱ�����Ӷ���
								generalBVOList.add(generalb);
						}
					}
				}
				// ת������
				generalBVO = new GeneralBillItemVO[generalBVOList.size()];
				generalBVO = generalBVOList.toArray(generalBVO);
				// �ۺ�VO��ͷ��ֵ
				gBillVO.setParentVO(generalhvo);
				// �ۺ�VO���帳ֵ
				gBillVO.setChildrenVO(generalBVO);
			}
		}
		// }
		// }

		return gBillVO;
	}

	/**
	 * ȡ��ǩ��
	 */
	@Override
	protected void onQxqz() throws Exception {
		// TODO Auto-generated method stub
		if (getBufferData().getCurrentRow() >= 0) {
			SoSaleVO generalh = (SoSaleVO) getBufferData().getCurrentVO()
					.getParentVO();
			Object result = iuap.retrieveByPK(SoSaleVO.class, generalh
					.getCsaleid());
			if (null != result) {
				generalh = (SoSaleVO) result;
				if (null != generalh.getVdef6()) {
					if (!"3".equals(generalh.getVdef6())) {
						getBillUI().showErrorMessage("�õ����Ѿ�ȡ��ǩ�֣�ȡ��ʧ��");
						return;
					} else {
						generalh.setVdef6("2");
					}
				} else {
					getBillUI().showErrorMessage("�õ���״̬����");
					return;
				}

				String strWhere = " select distinct cgeneralhid from ic_general_b where cfirstbillhid='"
						+ generalh.getCsaleid() + "' and dr=0 ";

				ArrayList batchList = (ArrayList) iuap.executeQuery(strWhere,
						new ArrayListProcessor());
				if (null != batchList && batchList.size() > 0) {
					Object[] batch = (Object[]) batchList.get(0);
					strWhere = " dr = 0 and cgeneralhid='" + batch[0] + "'";
				} else {
					getBillUI().showErrorMessage("�õ����Ѿ�ȡ��ǩ��,ȡ��ʧ��");
					return;
				}

				ArrayList list = (ArrayList) iuap.retrieveByClause(
						IcGeneralHVO.class, strWhere);
				if (null != list && list.size() > 0) {
					IcGeneralHVO header = (IcGeneralHVO) list.get(0);
					if (null != header) {

						strWhere = " dr = 0 and cgeneralhid = '"
								+ header.getCgeneralhid() + "'";
						list = (ArrayList) iuap.retrieveByClause(
								IcGeneralBVO.class, strWhere);
						if (null != list && list.size() > 0) {
							IcGeneralBVO[] itemvo = new IcGeneralBVO[list
									.size()];
							GeneralBillItemVO[] generalbItem = new GeneralBillItemVO[itemvo.length];
							itemvo = (IcGeneralBVO[]) list.toArray(itemvo);
							AggregatedValueObject billvo = new GeneralBillVO();
							billvo
									.setParentVO(CommonUnit
											.setGeneralHVO(header));
							for (int i = 0; i < itemvo.length; i++) {
								generalbItem[i] = CommonUnit
										.setGeneralItemVO(itemvo[i]);
							}
							billvo.setChildrenVO(generalbItem);

							Iw80020206 iw = (Iw80020206) NCLocator
									.getInstance().lookup(
											Iw80020206.class.getName());
							iw.whs_processAction80020206("CANCELSIGN",
									"DELETE", "4C", _getDate().toString(),
									billvo, generalh);

							getBillUI().showHintMessage("ȡ��ǩ�ֳɹ�");
							getButtonManager()
									.getButton(
											nc.ui.wds.w80020206.buttun0206.ISsButtun.Qzqr)
									.setEnabled(true);
							getButtonManager()
									.getButton(
											nc.ui.wds.w80020206.buttun0206.ISsButtun.Qxqz)
									.setEnabled(false);
							super.onBoRefresh();
							return;
						}
					}
				}
				getBillUI().showErrorMessage("ǩ��ʧ��,���۳��ⵥ�ѱ�ɾ��");
				return;

			}
		}
	}

}