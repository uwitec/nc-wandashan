package nc.ui.wds.w8000;

import java.util.ArrayList;
import java.util.List;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.uap.IVOPersistence;
import nc.itf.uap.sfapp.IBillcodeRuleService;
import nc.jdbc.framework.processor.ArrayListProcessor;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
import nc.vo.pub.billcodemanage.BillCodeObjValueVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.lang.UFTime;
import nc.vo.wds.w8004040204.TbOutgeneralBVO;
import nc.vo.wds.w8004040204.TbOutgeneralHVO;
import nc.vo.wds.w8004040204.TbOutgeneralTVO;
import nc.vo.wds.w8004040204.TbWarehousestockVO;
import nc.vo.wds.w8004061002.BdCargdocTrayVO;
import nc.vo.wds.w80060406.TbFydmxnewVO;
import nc.vo.wds.w80060608.IcGeneralBVO;
import nc.vo.wds.w80060608.IcGeneralHVO;

public class CommonUnit {
	static IUAPQueryBS iuap = (IUAPQueryBS) NCLocator.getInstance().lookup(
			IUAPQueryBS.class.getName());
	static IVOPersistence ivo = (IVOPersistence) NCLocator.getInstance()
			.lookup(IVOPersistence.class.getName());

	/**
	 * ����Ա��ѯ���س�������й��ˣ�ֻ��ʾ�б���λ��Ʒ������
	 * 
	 * @param pkList
	 *            ��Ʒ����
	 * @param strWhere
	 *            ����sql�������
	 * @return ת�����supervo
	 */
	public static SuperVO[] queryTbOutGeneral(List pkList, StringBuffer strWhere) {
		SuperVO[] queryVos = null;
		if (null != pkList && pkList.size() > 0) {
			StringBuffer tmpWhere = new StringBuffer();
			for (int i = 0; i < pkList.size(); i++) {
				if (tmpWhere.length() <= 0) {
					tmpWhere.append("'" + pkList.get(i) + "'");
					continue;
				}
				tmpWhere.append(",'").append(pkList.get(i)).append("'");
			}
			StringBuffer sql = new StringBuffer(
					"select distinct tb_outgeneral_h.general_pk,tb_outgeneral_h.vbillcode,tb_outgeneral_h.dbilldate,"
							+ "tb_outgeneral_h.vbilltype,tb_outgeneral_h.vbillstatus,tb_outgeneral_h.coperatorid,tb_outgeneral_h.tmaketime,"
							+ "tb_outgeneral_h.csourcebillhid,tb_outgeneral_h.vsourcebillcode,tb_outgeneral_h.srl_pkr,tb_outgeneral_h.srl_pk,"
							+ "tb_outgeneral_h.pk_calbody,tb_outgeneral_h.cbiztype,tb_outgeneral_h.cdispatcherid,"
							+ "tb_outgeneral_h.cwhsmanagerid,tb_outgeneral_h.cdptid,"
							+ "tb_outgeneral_h.cbizid,tb_outgeneral_h.ccustomerid,tb_outgeneral_h.pk_cubasdocc,"
							+ "tb_outgeneral_h.vdiliveraddress,tb_outgeneral_h.vnote,tb_outgeneral_h.cregister,"
							+ "tb_outgeneral_h.taccounttime,tb_outgeneral_h.cauditorid,tb_outgeneral_h.dauditdate,"
							+ "tb_outgeneral_h.clastmodiid,"
							+ "tb_outgeneral_h.tlastmoditime,tb_outgeneral_h.iprintcount,tb_outgeneral_h.comp,"
							+ "tb_outgeneral_h.qianzidate,tb_outgeneral_h.ts,tb_outgeneral_h.dr,tb_outgeneral_h.state "
							+ "from tb_outgeneral_h,tb_outgeneral_b b "
							+ "where")
					.append(strWhere)
					.append(
							" and tb_outgeneral_h.general_pk = b.general_pk and b.dr = 0 and tb_outgeneral_h.dr = 0"
									+ " and b.cinventoryid in (").append(
							tmpWhere).append(")");
			ArrayList list = new ArrayList();
			try {
				list = (ArrayList) iuap.executeQuery(sql.toString(),
						new ArrayListProcessor());
			} catch (BusinessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (list.size() > 0) {
				queryVos = new SuperVO[list.size()];
				// �ѻ�ȡ������ֵת��������
				// WDSTools.getString_TrimZeroLenAsNull(tmpObj[15])
				for (int i = 0; i < list.size(); i++) {
					Object[] tmpObj = (Object[]) list.get(i);
					queryVos[i] = CommonUnit.getOutGenerlHVO(tmpObj);
				}
			}
		}
		return queryVos;
	}

	/**
	 * ���ݵ�Ʒ��������Դ�����������в�ѯ���س�����Ƿ���ڸ����ݲ���������
	 * 
	 * @param invbasdoc
	 *            ��Ʒ����
	 * @param sourceb_Pk
	 *            ��Դ���ݱ�������
	 * @return ��ѯ��Ľ����
	 * @throws BusinessException
	 */
	public static List getOutGeneralBVO(String invbasdoc, String sourceb_Pk)
			throws BusinessException {
		// /�����ӱ��� ��������Ʒ����������=��Y��
		// ����иñ���Ա�������⣬����ʾ������
		String strWhere = " dr = 0 and isoper = 'Y' and cinventoryid = '"
				+ invbasdoc + "' and csourcebillbid = '" + sourceb_Pk + "'";
		return (ArrayList) iuap.retrieveByClause(TbOutgeneralBVO.class,
				strWhere);
	}

	public static TbOutgeneralHVO getOutGenerlHVO(Object[] tmpObj) {
		TbOutgeneralHVO generalh = new TbOutgeneralHVO();
		generalh.setGeneral_pk(WDSTools.getString_TrimZeroLenAsNull(tmpObj[0])); // ����
		generalh.setVbillcode(WDSTools.getString_TrimZeroLenAsNull(tmpObj[1]));// ���ݺ�
		generalh.setDbilldate(CommonUnit.getUFDate_Null(tmpObj[2]));// ��������
		generalh.setVbilltype(WDSTools.getString_TrimZeroLenAsNull(tmpObj[3]));// ��������
		generalh.setVbillstatus(CommonUnit.getInteger_Null(tmpObj[4]));// ����״̬
		if (null != tmpObj[5])
			generalh.setCoperatorid(WDSTools
					.getString_TrimZeroLenAsNull(tmpObj[5].toString().trim()));// �Ƶ���
		generalh.setTmaketime(WDSTools.getString_TrimZeroLenAsNull(tmpObj[6]));// �Ƶ�ʱ��
		generalh.setCsourcebillhid(WDSTools
				.getString_TrimZeroLenAsNull(tmpObj[7]));// ��Դ���ݱ�ͷ
		generalh.setVsourcebillcode(WDSTools
				.getString_TrimZeroLenAsNull(tmpObj[8]));// ��Դ���ݺ�
		generalh.setSrl_pkr(WDSTools.getString_TrimZeroLenAsNull(tmpObj[9]));// ���ֿ�����
		generalh.setSrl_pk(WDSTools.getString_TrimZeroLenAsNull(tmpObj[10]));// �ֿ�����
		generalh
				.setPk_calbody(WDSTools.getString_TrimZeroLenAsNull(tmpObj[11]));// �����֯����
		generalh.setCbiztype(WDSTools.getString_TrimZeroLenAsNull(tmpObj[12]));// ҵ������
		generalh.setCdispatcherid(WDSTools
				.getString_TrimZeroLenAsNull(tmpObj[13]));// �շ����
		generalh.setCwhsmanagerid(WDSTools
				.getString_TrimZeroLenAsNull(tmpObj[14]));// ���Ա����
		generalh.setCdptid(WDSTools.getString_TrimZeroLenAsNull(tmpObj[15]));// ��������
		generalh.setCbizid(WDSTools.getString_TrimZeroLenAsNull(tmpObj[16]));// ҵ��Ա����
		generalh.setCcustomerid(WDSTools
				.getString_TrimZeroLenAsNull(tmpObj[17]));// �ͻ�����
		generalh.setPk_cubasdocc(WDSTools
				.getString_TrimZeroLenAsNull(tmpObj[18]));// ���̻�����������
		generalh.setVdiliveraddress(WDSTools
				.getString_TrimZeroLenAsNull(tmpObj[19]));// �ջ���ַ
		generalh.setVnote(WDSTools.getString_TrimZeroLenAsNull(tmpObj[20]));// ��ע
		generalh.setCregister(WDSTools.getString_TrimZeroLenAsNull(tmpObj[21]));// ǩ��������
		generalh.setTaccounttime(WDSTools
				.getString_TrimZeroLenAsNull(tmpObj[22]));// ǩ��ʱ��
		generalh
				.setCauditorid(WDSTools.getString_TrimZeroLenAsNull(tmpObj[23]));// �����
		generalh.setDauditdate(CommonUnit.getUFDate_Null(tmpObj[24]));// �������
		generalh.setClastmodiid(WDSTools
				.getString_TrimZeroLenAsNull(tmpObj[25]));// ����޸���
		generalh.setTlastmoditime(WDSTools
				.getString_TrimZeroLenAsNull(tmpObj[26]));// ����޸�ʱ��
		generalh.setIprintcount(CommonUnit.getInteger_Null(tmpObj[27]));// ��ӡ����
		generalh.setComp(WDSTools.getString_TrimZeroLenAsNull(tmpObj[28]));// ��˾
		generalh.setQianzidate(CommonUnit.getUFDate_Null(tmpObj[29]));// ǩ������
		if (null != tmpObj[30])
			generalh.setTs(new UFTime(tmpObj[30].toString()));// ʱ���
		generalh.setDr(CommonUnit.getInteger_Null(tmpObj[31]));// ɾ����־
		generalh.setState(CommonUnit.getInteger_Null(tmpObj[32]));// ״̬
		return generalh;
	}

	/**
	 * ����VOת�� ���س�����ͷVO��ERP�г���� ת����ͷ
	 * 
	 * @param ich
	 *            ���س�����ͷVO
	 * @return ת�����ERP�г�����ͷ
	 */
	public static GeneralBillHeaderVO setGeneralHVO(IcGeneralHVO ich) {
		if (null == ich)
			return null;
		GeneralBillHeaderVO generalh = new GeneralBillHeaderVO();
		generalh.setCgeneralhid(ich.getCgeneralhid()); // PK
		generalh.setCbiztypeid(ich.getCbiztype());// ҵ������
		generalh.setCbilltypecode(ich.getCbilltypecode());// �������ͱ���
		generalh.setCcustomerid(ich.getCcustomerid());// �ͻ�
		generalh.setPk_corp(ich.getPk_corp());// ��˾
		generalh.setCregister(ich.getCregister());// ǩ����
		generalh.setDaccountdate(ich.getDaccountdate());// ǩ������
		generalh.setAttributeValue("taccounttime", ich.getTaccounttime());// ǩ��ʱ��
		generalh.setVbillcode(ich.getVbillcode());// ���ݺ�
		generalh.setDbilldate(ich.getDbilldate());// ��������
		generalh.setCwarehouseid(ich.getCwarehouseid());// �ֿ�
		generalh.setAttributeValue("cotherwhid", ich.getCotherwhid());// �����ֿ�
		generalh.setCdispatcherid(ich.getCdispatcherid());// �շ����
		generalh.setCdptid(ich.getCdptid());// ����
		generalh.setCwhsmanagerid(ich.getCwhsmanagerid()); // ���Ա
		generalh.setCoperatorid(ich.getCoperatorid());// �Ƶ���
		generalh.setAttributeValue("coperatoridnow", ich.getCoperatorid()); // ������
		generalh.setCinventoryid(ich.getCinventoryid()); // ����Ʒ
		generalh.setVdiliveraddress(ich.getVdiliveraddress()); // ��ַ
		generalh.setCbizid(ich.getCbizid()); // ҵ��Ա
		generalh.setVnote(ich.getVnote()); // ��ע
		generalh.setFbillflag(ich.getFbillflag()); // ״̬
		generalh.setPk_calbody(ich.getPk_calbody()); // �����֯PK
		generalh.setAttributeValue("clastmodiid", ich.getClastmodiid());// ����޸���
		generalh.setAttributeValue("tlastmoditime", ich.getTlastmoditime());// ����޸�ʱ��
		generalh.setAttributeValue("tmaketime", ich.getTmaketime());// �Ƶ�ʱ��
		generalh.setPk_cubasdocC(ich.getPk_cubasdocc());// �ͻ���������ID
		return generalh;
	}

	/**
	 * ����VOת�� ���س�������VO��ERP�г���� ת������
	 * 
	 * @param ich
	 *            ���س�������VO
	 * @return ת�����ERP�г�������
	 */
	public static GeneralBillItemVO setGeneralItemVO(IcGeneralBVO icb) {
		if (null == icb)
			return null;
		GeneralBillItemVO generalb = new GeneralBillItemVO();
		generalb.setCgeneralhid(icb.getCgeneralhid()); // �����ͷID
		generalb.setCgeneralbid(icb.getCgeneralbid()); // �������ID
		generalb.setAttributeValue("pk_corp", icb.getPk_corp()); // ��˾
		generalb.setCinvbasid(icb.getCinvbasid());// �������ID
		generalb.setCinventoryid(icb.getCinventoryid());// ���ID
		generalb.setVbatchcode(icb.getVbatchcode());// ���κ�
		generalb.setDbizdate(icb.getDbizdate());// ҵ������
		generalb.setNshouldoutnum(icb.getNshouldoutnum());// Ӧ������
		generalb.setNshouldoutassistnum(icb.getNshouldoutassistnum());// Ӧ��������
		generalb.setNoutnum(icb.getNoutnum());// ʵ������
		generalb.setNoutassistnum(icb.getNoutassistnum());// ʵ��������
		generalb.setCastunitid(icb.getCastunitid());// ��������λID
		generalb.setNprice(icb.getNprice());// ����
		generalb.setNmny(icb.getNmny());// ���
		generalb.setCsourcebillhid(icb.getCsourcebillhid());// ��Դ���ݱ�ͷ���к�
		generalb.setCfirstbillhid(icb.getCfirstbillhid());// Դͷ���ݱ�ͷID
		generalb.setCfreezeid(icb.getCfreezeid());// ������Դ
		generalb.setCsourcebillbid(icb.getCsourcebillbid());// ��Դ���ݱ������к�
		generalb.setCfirstbillbid(icb.getCfirstbillbid());// Դͷ���ݱ���ID
		generalb.setCsourcetype(icb.getCsourcetype());// ��Դ��������
		generalb.setCfirsttype(icb.getCfirsttype());// Դͷ��������
		generalb.setVsourcebillcode(icb.getVsourcebillcode());// ��Դ���ݺ�
		generalb.setVfirstbillcode(icb.getVfirstbillcode());// Դͷ���ݺ�
		generalb.setVsourcerowno(icb.getVsourcerowno());// ��Դ�����к�
		generalb.setVfirstrowno(icb.getVfirstrowno());// Դͷ�����к�
		generalb.setFlargess(icb.getFlargess());// �Ƿ���Ʒ
		generalb.setDfirstbilldate(icb.getDfirstbilldate());// Դͷ�����Ƶ�����
		generalb.setCreceieveid(icb.getCreceieveid());// �ջ���λ
		generalb.setCrowno(icb.getCrowno());// �к�
		generalb.setHsl(icb.getHsl());// ������
		generalb.setNsaleprice(icb.getNsaleprice());// ���ۼ۸�
		generalb.setNtaxprice(icb.getNtaxprice());// ��˰����
		generalb.setNtaxmny(icb.getNtaxmny());// ��˰���
		generalb.setNsalemny(icb.getNsalemny());// ����˰���
		generalb.setAttributeValue("cquotecurrency", icb.getCquotecurrency());// ���ñ���

		return generalb;
	}

	/**
	 * ���ݲֿ�������ѯ���òֿ��Ƿ�Ϊ�ֻܲ�ֲ�
	 * 
	 * @param pk_stock
	 *            �ֿ�����
	 * @return true �ܲ� false �ֲ�
	 * @throws BusinessException
	 */
	public static boolean getSotckIsTotal(String pk_stock)
			throws BusinessException {
		String sql = "select def2 from bd_stordoc where pk_stordoc = '"
				+ pk_stock + "'";
		ArrayList list = (ArrayList) iuap.executeQuery(sql,
				new ArrayListProcessor());
		if (null != list && list.size() > 0 && null != list.get(0)
				&& !"".equals(list.get(0))) {
			Object[] a = (Object[]) list.get(0);
			if (null != a[0] && !"".equals(a[0]))
				if (a[0].equals("0"))
					return true;
		}

		return false;
	}

	/**
	 * ���ݵ�¼�˲�ѯ��Ա����
	 * 
	 * @param pk_user
	 *            �û�����
	 * @return 0 ����Ա 1 ��Ϣ�� 2 ���˿� 3����
	 * @throws BusinessException
	 */
	public static String getUserType(String pk_user) throws BusinessException {
		
		
		
		String sql = "select st_type from tb_stockstaff where dr = 0 and cuserid = '"
				+ pk_user + "'";
		ArrayList list = (ArrayList) iuap.executeQuery(sql,
				new ArrayListProcessor());
		if (null != list && list.size() > 0 && null != list.get(0)
				&& !"".equals(list.get(0))) {
			Object[] a = (Object[]) list.get(0);
			if (null != a[0] && !"".equals(a[0]))
				return a[0].toString();
		}

		return null;
	}

	/**
	 * �����������ݣ�����״̬���˵���ʵ������
	 * 
	 * @param generalBVO
	 *            ���ⵥ�ӱ�
	 * @param isType
	 *            �Ƿ�ת�ֲ֣�trueת�ֲ�
	 * @param isStock
	 *            �ֻܲ��߷ֲ� true �ܲ�
	 * @param operationType
	 *            true �޸� false ����
	 * @return �Ƿ�����ɹ�
	 * @throws BusinessException
	 */
	public static boolean operationWare(TbOutgeneralBVO[] generalBVO,
			boolean isType, boolean isStock, boolean operationType)
			throws BusinessException {
		if (null != generalBVO && generalBVO.length > 0) {
			if (operationType) {
				List wareList = new ArrayList();
				List gentList = new ArrayList();
				List cargList = new ArrayList();
				List fydList = new ArrayList();
				List addFydmxList = new ArrayList();// ��ӷ��˵���ϸ����
				for (int i = 0; i < generalBVO.length; i++) {
					// �ж�ʵ���������Ƿ�Ϊ��
					if (null != generalBVO[i].getNoutassistnum()
							&& !"".equals(generalBVO[i].getNoutassistnum())) {
						String sWhere = " dr = 0 and cfirstbillhid = '"
								+ generalBVO[i].getCsourcebillhid()
								+ "' and pk_invbasdoc ='"
								+ generalBVO[i].getCinventoryid()
								+ "' and cfirstbillbid='"
								+ generalBVO[i].getCsourcebillbid() + "'";
						// �������ݿ�õ����������
						ArrayList generaltList = (ArrayList) iuap
								.retrieveByClause(TbOutgeneralTVO.class, sWhere);
						// �жϽ�����Ƿ�Ϊ��
						if (null != generaltList && generaltList.size() > 0) {
							// ������Դ�����ӱ�������ȡ���˵���ϸ��Ϣ
							TbFydmxnewVO fydmxvo = (TbFydmxnewVO) iuap
									.retrieveByPK(TbFydmxnewVO.class,
											generalBVO[i].getCsourcebillbid());
							TbOutgeneralTVO[] generaltvo = new TbOutgeneralTVO[generaltList
									.size()];
							generaltvo = (TbOutgeneralTVO[]) generaltList
									.toArray(generaltvo);
							double sum1 = 0;// ��������ʵ����������
							double sum2 = 0;
							String batchCode = null; // ����
							String lbatchCode = null; // Դ����
							List<String> batchList = new ArrayList<String>(); // ���μ���
							double nprice = 0; // ����
							double nmny = 0; // ���
							for (int j = 0; j < generaltvo.length; j++) {
								// ��ȡ��������
								String pk_ware = generaltvo[j].getWhs_pk();
								// ͨ������������ѯ����Ӧ����Ϣ
								TbWarehousestockVO ware = (TbWarehousestockVO) iuap
										.retrieveByPK(TbWarehousestockVO.class,
												pk_ware);

								if (null != ware) {

									if (null != ware.getWhs_stockpieces()
											&& !"".equals(ware
													.getWhs_stockpieces())
											&& null != generaltvo[j]
													.getNoutassistnum()
											&& !"".equals(generaltvo[j]
													.getNoutassistnum())) {
										// ��ȡ��ǰ�����еĿ�渨����
										double count1 = ware
												.getWhs_stockpieces()
												.toDouble();
										// ��ȡ��ǰ������е�ʵ��������
										double num1 = generaltvo[j]
												.getNoutassistnum().toDouble();
										// ����ó�ʣ��
										double resultNum1 = count1 - num1;
										if (resultNum1 >= 0) {

											if (null != fydmxvo) {
												// ����ʵ��������
												sum1 = sum1 + num1;
												// �ۼ�����
												if (batchList.size() < 1) {
													batchList.add(generaltvo[j]
															.getVbatchcode());
													// Դ����
													lbatchCode = ware
															.getWhs_lbatchcode();
													if (null != ware
															.getWhs_nprice())
														nprice = ware
																.getWhs_nprice()
																.toDouble(); // ����
													if (null != ware
															.getWhs_nmny())
														nmny = ware
																.getWhs_nmny()
																.toDouble();// ���
												} else {
													batchList.add(generaltvo[j]
															.getVbatchcode());
												}

											}
											// ��渨����
											ware
													.setWhs_stockpieces(new UFDouble(
															resultNum1));
											// generaltvo[j]
											// .setStockpieces(new UFDouble(
											// resultNum1));
											// ����0 ���� ���ֵܲ�ʱ���������״̬
											if (resultNum1 == 0 && isStock) {
												// ��ǰ�������Ѿ�û�л��ˡ��޸�����״̬�Ϳ���״̬
												ware.setWhs_status(new Integer(
														1));
												// ����������ѯ��������Ϣ
												BdCargdocTrayVO carg = (BdCargdocTrayVO) iuap
														.retrieveByPK(
																BdCargdocTrayVO.class,
																ware
																		.getPplpt_pk());
												if (null != carg) {
													carg
															.setCdt_traystatus(new Integer(
																	0)); // ������״̬���ÿ���
													cargList.add(carg);
												}
											}
										} else {
											// ���ʣ������С��0˵������������㲻�ܳ��⣬�����쳣
											return false;
										}
									}
									// �ж�������
									if (null != ware.getWhs_stocktonnage()
											&& !"".equals(ware
													.getWhs_stocktonnage())
											&& null != generaltvo[j]
													.getNoutnum()
											&& !"".equals(generaltvo[j]
													.getNoutnum())) {
										double count2 = ware
												.getWhs_stocktonnage()
												.toDouble();
										double num2 = generaltvo[j]
												.getNoutnum().toDouble();
										// ����ó�ʣ��
										double resultNum2 = count2 - num2;
										if (resultNum2 >= 0) {

											if (null != fydmxvo) {
												// ����ʵ��������
												sum2 = sum2 + num2;
											}
											// ���������
											ware
													.setWhs_stocktonnage(new UFDouble(
															resultNum2));
											// generaltvo[j]
											// .setStocktonnage(new UFDouble(
											// resultNum2));
										}
									}
									// ���µ�ǰ������Ϣ
									wareList.add(ware);
									// gentList.add(generaltvo[j]);
								}
							}
							if (isType && null != fydmxvo) {
								if (batchList.size() > 1) {

									// �������θ�ֵ
									batchCode = batchList.get(0);
									// ѭ�����μ���
									for (int j = 0; j < batchList.size(); j++) {
										// �ж����μ����Ƿ���һ��
										if (j == batchList.size() - 1)
											break;
										// ������μ����е����β���ͬһ�����εĽ����������
										if (!batchList.get(j).equals(
												batchList.get(j + 1)))
											// Ϊ�����������
											batchCode = batchCode + ","
													+ batchList.get(j + 1);
									}
									String[] tmpArray = batchCode.split(",");
									if (tmpArray.length == 1) {
										if (sum1 != 0)
											fydmxvo.setCfd_sffsl(new UFDouble(
													sum1));
										if (sum2 != 0)
											fydmxvo.setCfd_sfsl(new UFDouble(
													sum2));
										fydmxvo.setCfd_pc(tmpArray[0]);

									} else {
										for (int k = 0; k < tmpArray.length; k++) {
											sum1 = 0;
											sum2 = 0;

											for (int j = 0; j < generaltvo.length; j++) {
												if (tmpArray[k]
														.equals(generaltvo[j]
																.getVbatchcode())) {
													sum1 = sum1
															+ generaltvo[j]
																	.getNoutassistnum()
																	.toDouble()
																	.doubleValue();
													sum2 = sum2
															+ generaltvo[j]
																	.getNoutnum()
																	.toDouble()
																	.doubleValue();
												}
											}
											fydmxvo.setCfd_sffsl(new UFDouble(
													sum1));// ʵ��������
											fydmxvo.setCfd_sfsl(new UFDouble(
													sum2));// ʵ������
											fydmxvo.setCfd_yfsl(new UFDouble(
													sum2)); // Ӧ���������֣�
											fydmxvo
													.setCfd_xs(new UFDouble(
															sum1)); // Ӧ��������
											fydmxvo.setCfd_pk(null);// ���������
											fydmxvo.setCfd_pc(tmpArray[k]);
											fydmxvo.setDr(0);
											if (nmny != 0)
												fydmxvo
														.setCfd_nmny(new UFDouble(
																nmny));
											if (nprice != 0)
												fydmxvo
														.setCfd_nprice(new UFDouble(
																nprice));
											if (null != lbatchCode)
												fydmxvo.setCfd_lpc(lbatchCode);
											ivo.insertVO(fydmxvo); // �����ӱ�
											fydmxvo.setCfd_pk(generalBVO[i]
													.getCsourcebillbid());// ��ԭ����
											fydmxvo.setDr(1);// ����ɾ����־
										}
									}
								} else {
									if (sum1 != 0)
										fydmxvo
												.setCfd_sffsl(new UFDouble(sum1));
									if (sum2 != 0)
										fydmxvo.setCfd_sfsl(new UFDouble(sum2));
									fydmxvo.setCfd_pc(batchList.get(0));
									fydList.add(fydmxvo);
								}
							} else {
								if (null != fydmxvo) {
									if (sum1 != 0)
										fydmxvo
												.setCfd_sffsl(new UFDouble(sum1));
									if (sum2 != 0)
										fydmxvo.setCfd_sfsl(new UFDouble(sum2));
									if (batchList.size() > 0) {
										// �������θ�ֵ
										batchCode = batchList.get(0);
										// ѭ�����μ���
										for (int j = 0; j < batchList.size(); j++) {
											// �ж����μ����Ƿ���һ��
											if (j == batchList.size() - 1
													|| batchList.size() == 1)
												break;
											// ������μ����е����β���ͬһ�����εĽ����������
											if (!batchList.get(j).equals(
													batchList.get(j + 1)))
												// Ϊ�����������
												batchCode = batchCode + ","
														+ batchList.get(j + 1);
										}
										fydmxvo.setCfd_pc(batchCode);
									}

									if (nmny != 0)
										fydmxvo.setCfd_nmny(new UFDouble(nmny));
									if (nprice != 0)
										fydmxvo.setCfd_nprice(new UFDouble(
												nprice));
									if (null != lbatchCode)
										fydmxvo.setCfd_lpc(lbatchCode);
									if (null != fydmxvo)
										fydList.add(fydmxvo);
								}
							}
						}
					}
				}
				// �˵�
				if (fydList.size() > 0)
					ivo.updateVOList(fydList);
				// ����
				if (wareList.size() > 0)
					ivo.updateVOList(wareList);
				// ���⻺���
				// ivo.updateVOList(gentList);
				// ����״̬
				if (cargList.size() > 0)
					ivo.updateVOList(cargList);
				// // ���뷢�˵���ϸ
				// if (addFydmxList.size() > 0)
				// ivo.insertVOList(addFydmxList);
				// System.out.println(123);
				return true;
			} else {
				operationFyd(generalBVO, isType);
				return true;
			}
		}
		return false;
	}

	/**
	 * �����޸Ĺ��ܣ��������˵���ϸ
	 * 
	 * @param generalbVO
	 *            ���ⵥ����VO����
	 * @param type
	 *            false ���۳��� true ��������
	 * @throws BusinessException
	 */
	public static void operationFyd(TbOutgeneralBVO[] generalbVO, boolean type)
			throws BusinessException {

		for (int i = 0; i < generalbVO.length; i++) {
			// ����
			if (!type) {
				// ������Դ�����ӱ�������ȡ���˵���ϸ��Ϣ
				TbFydmxnewVO fydmxvo = (TbFydmxnewVO) iuap.retrieveByPK(
						TbFydmxnewVO.class, generalbVO[i].getCsourcebillbid());
				setFydPro(fydmxvo, generalbVO[i]);
			} else {
				String strWhere1 = "";
				// �������8�����Ƴ��⣬��Ҫ��������
				if (null != generalbVO[i].getCsourcetype()
						&& generalbVO[i].getCsourcetype().equals("8")) {
					strWhere1 = generalbVO[i].getCfirstbillhid();
				} else
					strWhere1 = generalbVO[i].getCsourcebillhid();

				String strWhere = "  fyd_pk = '" + strWhere1
						+ "' and pk_invbasdoc = '"
						+ generalbVO[i].getCinventoryid() + "'";
				ArrayList list = (ArrayList) iuap.retrieveByClause(
						TbFydmxnewVO.class, strWhere);
				double sum1 = 0;// ��������ʵ����������
				double sum2 = 0;
				String batchCode = null; // ����
				String lbatchCode = null; // Դ����
				double nprice = 0; // ����
				double nmny = 0; // ���

				List<String> batchList = new ArrayList<String>(); // ���μ���
				if (null == list || list.size() == 0)
					continue;
				for (int j = 0; j < list.size(); j++) {
					((TbFydmxnewVO) list.get(j)).setDr(1);
					// ����ɾ����־Ϊ1 ִ�С���
					ivo.updateVO((TbFydmxnewVO) list.get(j));
				}
				TbFydmxnewVO fydmx = (TbFydmxnewVO) list.get(0);

				strWhere = " dr = 0 and cfirstbillhid = '"
						+ generalbVO[i].getCsourcebillhid()
						+ "' and pk_invbasdoc ='"
						+ generalbVO[i].getCinventoryid() + "'";
				// �������ݿ�õ����������
				ArrayList generaltList = (ArrayList) iuap.retrieveByClause(
						TbOutgeneralTVO.class, strWhere);
				// �жϽ�����Ƿ�Ϊ��
				if (null != generaltList && generaltList.size() > 0) {
					for (int k = 0; k < generaltList.size(); k++) {
						TbOutgeneralTVO generalt = (TbOutgeneralTVO) generaltList
								.get(k);
						// �ۼ�����
						if (batchList.size() < 1) {
							batchList.add(generalt.getVbatchcode());
							// Դ����
							lbatchCode = generalt.getLvbatchcode();
							if (null != generalt.getNprice())
								nprice = generalt.getNprice().toDouble()
										.doubleValue(); // ����
							if (null != generalt.getNmny())
								nmny = generalt.getNmny().toDouble()
										.doubleValue();// ���
						} else {
							batchList.add(generalt.getVbatchcode());
						}

					}

					if (batchList.size() > 0) {

						// �������θ�ֵ
						batchCode = batchList.get(0);
						// ѭ�����μ���
						for (int z = 0; z < batchList.size(); z++) {
							// �ж����μ����Ƿ���һ��
							if (z == batchList.size() - 1)
								break;
							// ������μ����е����β���ͬһ�����εĽ����������
							if (!batchList.get(z).equals(batchList.get(z + 1)))
								// Ϊ�����������
								batchCode = batchCode + ","
										+ batchList.get(z + 1);
						}
						String[] tmpArray = batchCode.split(",");
						for (int k = 0; k < tmpArray.length; k++) {
							sum1 = 0;
							sum2 = 0;

							for (int z = 0; z < generaltList.size(); z++) {
								if (tmpArray[k]
										.equals(((TbOutgeneralTVO) generaltList
												.get(z)).getVbatchcode())) {
									sum1 = sum1
											+ ((TbOutgeneralTVO) generaltList
													.get(z)).getNoutassistnum()
													.toDouble().doubleValue();
									sum2 = sum2
											+ ((TbOutgeneralTVO) generaltList
													.get(z)).getNoutnum()
													.toDouble().doubleValue();
								}
							}

							fydmx.setCfd_sffsl(new UFDouble(sum1));// ʵ��������
							fydmx.setCfd_sfsl(new UFDouble(sum2));// ʵ������
							fydmx.setCfd_yfsl(new UFDouble(sum2)); // Ӧ���������֣�
							fydmx.setCfd_xs(new UFDouble(sum1)); // Ӧ��������
							fydmx.setCfd_pk(null);// ���������
							fydmx.setCfd_pc(tmpArray[k]);
							fydmx.setDr(0);
							if (nmny != 0)
								fydmx.setCfd_nmny(new UFDouble(nmny));
							if (nprice != 0)
								fydmx.setCfd_nprice(new UFDouble(nprice));
							if (null != lbatchCode)
								fydmx.setCfd_lpc(lbatchCode);
							ivo.insertVO(fydmx); // �����ӱ�

						}

					}
				}
			}
		}

	}

	/**
	 * ���÷��˵���ϸ���е�����
	 * 
	 * @param fydmxvo
	 * @param generalb
	 * @throws BusinessException
	 */
	private static void setFydPro(TbFydmxnewVO fydmxvo, TbOutgeneralBVO generalb)
			throws BusinessException {
		if (null != fydmxvo) {
			// ������
			fydmxvo.setCfd_sffsl(generalb.getNoutassistnum());
			// ������
			fydmxvo.setCfd_sfsl(generalb.getNoutnum());
			// ����
			fydmxvo.setCfd_pc(generalb.getVbatchcode());
			// Դ����
			fydmxvo.setCfd_lpc(generalb.getLvbatchcode());
			// ����
			fydmxvo.setCfd_nprice(generalb.getNprice());
			// ���
			fydmxvo.setCfd_nmny(generalb.getNmny());
			// ִ�и���
			ivo.updateVO(fydmxvo);
		}
	}

	/**
	 * ���ݵ�ǰ��¼�������ʹ����Ʒ������ѯ����������Ϣ ������������
	 * 
	 * @param pk_user
	 *            ��ǰ��¼������
	 * 
	 * @param pk_invbasdoc
	 *            �����Ʒ����
	 * @return ������
	 */
	public static List getStockDetailByPk_User(String pk_user,
			String pk_invbasdoc) {
		StringBuffer sql = new StringBuffer(
				"select o.whs_pk ,o.pplpt_pk,o.whs_stockpieces,o.whs_stocktonnage,o.pk_invbasdoc,o.whs_batchcode,"
						+ "o.whs_nprice,o.whs_nmny,o.whs_lbatchcode from tb_warehousestock o ,tb_stockstaff s, "
						+ " tb_stockstate k "
						+ " where o.dr = 0 and s.dr=0 and k.dr = 0 and  o.pk_cargdoc=s.pk_cargdoc and s.cuserid='"
						+ pk_user
						+ "' and o.whs_status = 0 and o.ss_pk = k.ss_pk and k.ss_isout = 0");
		if (null != pk_invbasdoc && pk_invbasdoc.length() > 0) {
			sql.append(" and o.pk_invbasdoc = '" + pk_invbasdoc + "'");
		}
		sql.append("  order by o.whs_batchcode ");
		ArrayList list = null;
		try {
			list = (ArrayList) iuap.executeQuery(sql.toString(),
					new ArrayListProcessor());
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (null != list && list.size() > 0) {
			List strList = new ArrayList();
			for (int i = 0; i < list.size(); i++) {
				Object[] a = (Object[]) list.get(i);
				if (null != a && a.length > 0 && null != a[0]) {
					TbWarehousestockVO ware = new TbWarehousestockVO();
					ware.setWhs_pk(WDSTools.getString_TrimZeroLenAsNull(a[0])); // ��������
					ware
							.setPplpt_pk(WDSTools
									.getString_TrimZeroLenAsNull(a[1])); // ��������
					ware.setWhs_stockpieces(WDSTools
							.getUFDouble_NullAsZero(a[2])); // ������
					ware.setWhs_stocktonnage(WDSTools
							.getUFDouble_NullAsZero(a[3])); // ������
					ware.setPk_invbasdoc(WDSTools
							.getString_TrimZeroLenAsNull(a[4])); // �������
					ware.setWhs_batchcode(WDSTools
							.getString_TrimZeroLenAsNull(a[5])); // ����
					ware.setWhs_nprice(WDSTools.getUFDouble_NullAsZero(a[6]));// ����
					ware.setWhs_nmny(WDSTools.getUFDouble_NullAsZero(a[7])); // ���
					ware.setWhs_lbatchcode(WDSTools
							.getString_TrimZeroLenAsNull(a[8]));// ��Դ����
					strList.add(ware);
				}
			}
			return strList;
		}
		return null;
	}

	/**
	 * ���ݵ�¼��Ա������ѯ����Ӧ�Ļ�Ʒ����
	 * 
	 * @param pk
	 *            ��Ա����
	 * @return ��Ʒ��������
	 * @throws BusinessException
	 */
	public static List getInvbasdoc_Pk(String pk) {
		String sql = "select s.pk_invbasdoc from tb_stockstaff t,tb_spacegoods s "
				+ "where t.dr = 0 and s.dr = 0 and t.pk_cargdoc = s.pk_cargdoc and t.cuserid='"
				+ pk + "'";
		ArrayList list = null;
		try {
			list = (ArrayList) iuap.executeQuery(sql, new ArrayListProcessor());
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (null != list && list.size() > 0) {
			Object[] a = (Object[]) list.get(0);
			if (null != a && a.length > 0 && null != a[0]) {
				List strList = new ArrayList();
				for (int i = 0; i < list.size(); i++) {
					a = (Object[]) list.get(i);
					strList.add(a[0]);
				}
				return strList;
			}
		}
		return null;
	}

	/**
	 * ���ݵ�¼��Ա������ѯ����Ӧ�Ĳֿ�����
	 * 
	 * @param pk
	 *            ��Ա����
	 * @return �ֿ�����
	 * @throws BusinessException
	 */
	public static String getStordocName(String pk) throws BusinessException {
		String tmp = null;
		String sql = "select pk_stordoc from tb_stockstaff where dr = 0 and cuserid='"
				+ pk + "'";
		ArrayList list = (ArrayList) iuap.executeQuery(sql,
				new ArrayListProcessor());
		if (null != list && list.size() > 0) {
			Object[] a = (Object[]) list.get(0);
			if (null != a && a.length > 0 && null != a[0]) {
				tmp = a[0].toString();
			}
		}

		return tmp;
	}

	/**
	 * ���ݵ�¼��Ա������ѯ����Ӧ�Ļ�λ����
	 * 
	 * @param pk
	 *            ��Ա����
	 * @return ��λ����
	 * @throws BusinessException
	 */
	public static String getCargDocName(String pk) throws BusinessException {
		String tmp = null;
		String sql = "select pk_cargdoc from tb_stockstaff where dr = 0 and cuserid='"
				+ pk + "'";
		ArrayList list = (ArrayList) iuap.executeQuery(sql,
				new ArrayListProcessor());
		if (null != list && list.size() > 0) {
			Object[] a = (Object[]) list.get(0);
			if (null != a && a.length > 0 && null != a[0]) {
				tmp = a[0].toString();
			}
		}

		return tmp;
	}

	/**
	 * ���ݿͻ�������ѯ������������Ϊ����վ
	 * 
	 * @param pk_cubasdoc
	 *            �ͻ�����
	 * @return
	 * @throws BusinessException
	 */
	public static String getAreaclName(String pk_cubasdoc)
			throws BusinessException {
		String tmp = null;
		String sql = "select c.pk_areacl  from bd_cubasdoc c,bd_cumandoc m where c.dr = 0 and m.dr = 0  and m.pk_cubasdoc = c.pk_cubasdoc  and m.pk_cumandoc = '"
				+ pk_cubasdoc + "'";
		ArrayList list = (ArrayList) iuap.executeQuery(sql,
				new ArrayListProcessor());
		if (null != list && list.size() > 0) {
			Object[] a = (Object[]) list.get(0);
			if (null != a && a.length > 0 && null != a[0]) {
				tmp = a[0].toString();
			}
		}

		return tmp;
	}

	/**
	 * �������������PK���в�ѯ��ӡ����
	 * 
	 * @param pk
	 *            ����
	 * @return ��ӡ����
	 * @throws BusinessException
	 */
	public static int getIprintCount(String pk) throws BusinessException {
		int count = 0;

		String sql = "select iprintcount from so_sale where csaleid = '" + pk
				+ "' and iprintcount is not null";
		IUAPQueryBS iuap = (IUAPQueryBS) NCLocator.getInstance().lookup(
				IUAPQueryBS.class.getName());
		ArrayList list = (ArrayList) iuap.executeQuery(sql,
				new ArrayListProcessor());
		if (null != list && list.size() > 0) {
			Object[] results = (Object[]) list.get(0);
			if (results != null && results.length > 0 && results[0] != null) {
				if (null != results[0] && !"".equals(results[0])) {
					count = Integer.parseInt(results[0].toString());
				}
			}
		}
		return count;
	}

	public static UFBoolean getUFBoolean_Null(Object value) {
		if (null != value)
			return new UFBoolean(value.toString());
		return null;
	}

	public static Integer getInteger_Null(Object value) {
		if (null != value)
			return new Integer(value.toString());
		return null;
	}

	public static UFDate getUFDate_Null(Object value) {
		if (null != value)
			return new UFDate(value.toString());
		return null;
	}

	/**
	 * ���ɵ��ݺ� �������ڣ�(2001-11-22 13:03:58) zhy����
	 * 
	 * @return java.lang.String
	 * @param billtype
	 *            java.lang.String
	 * @param pkcorp
	 *            java.lang.String
	 * @param cbillcode
	 *            java.lang.String
	 */
	public static String getBillCode(String billtype, String pkcorp,
			String gcbm, String operator) throws BusinessException {
		String scddh = null;
		try {
			BillCodeObjValueVO vo = new BillCodeObjValueVO();
			String[] names = { "", "" };
			String[] values = new String[] { "", "" };
			vo.setAttributeValue(names, values);
			scddh = getBillCode(billtype, pkcorp, vo);
		} catch (Exception e) {
			nc.vo.ic.pub.GenMethod.throwBusiException(e);
		}
		return scddh;
	}

	/**
	 * ���ɵ��ݺ� �������ڣ�(2001-11-22 13:03:58)
	 * 
	 * @return java.lang.String
	 * @param billtype
	 *            java.lang.String
	 * @param pkcorp
	 *            java.lang.String
	 * @param cbillcode
	 *            java.lang.String
	 */
	private static String getBillCode(String billtype, String pkcorp,
			nc.vo.pub.billcodemanage.BillCodeObjValueVO billVO)
			throws BusinessException {
		String djh = null;
		try {
			IBillcodeRuleService bo = (IBillcodeRuleService) NCLocator
					.getInstance().lookup(IBillcodeRuleService.class.getName());
			djh = bo.getBillCode_RequiresNew(billtype, pkcorp, null, billVO);

		} catch (Exception e) {
			nc.vo.ic.pub.GenMethod.throwBusiException(e);
		}
		return djh;
	}
}
