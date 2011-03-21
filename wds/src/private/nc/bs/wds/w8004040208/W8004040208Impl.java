package nc.bs.wds.w8004040208;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.uap.IVOPersistence;
import nc.jdbc.framework.processor.ArrayListProcessor;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDouble;
import nc.vo.wds.w8004040204.TbOutgeneralBVO;
import nc.vo.wds.w8004040204.TbOutgeneralTVO;
import nc.vo.wds.w8004040204.TbWarehousestockVO;

public class W8004040208Impl   {

	// ��ȡ���ݿ���ʶ���
	IVOPersistence ivo = null;
	// ���ݿ��ѯ����
	IUAPQueryBS iuap = null;

	/**
	 * ���ݵ�Ʒ������ѯ�ĵ�Ʒ����������
	 * 
	 * @param invpk
	 *            ��Ʒ����
	 * @return
	 * @throws BusinessException
	 */
	public String getInvbasdocName(String invpk) throws BusinessException {
		if (null != invpk && !"".equals(invpk)) {
			String sql = "select invname from bd_invbasdoc where pk_invbasdoc = '"
					+ invpk + "'";
			ArrayList list = (ArrayList) this.getIuap().executeQuery(sql,
					new ArrayListProcessor());
			if (null != list && list.size() > 0) {
				Object[] a = (Object[]) list.get(0);
				if (null != a && a.length > 0 && null != a[0]) {
					return a[0].toString();
				}
			}
		}
		return null;
	}

	/*
	 * �Զ�ȡ��(non-Javadoc)
	 * 
	 * @see nc.itf.wds.w8004040208.Iw8004040208#w8004040208AutoPickAction(nc.vo.wds.w8004040204.TbOutgeneralBVO[])
	 */
	public String w8004040208AutoPickAction(TbOutgeneralBVO[] generalb)
			throws Exception {
//		deleteAutoPickAction(generalb);
//		TbOutgeneralTVO generalt = null;
//		double asum = 0;// Ӧ������
//		if (null != generalb && generalb.length > 0) {
//			for (int i = 0; i < generalb.length; i++) {
//				if (null == generalb[i])
//					continue;
//				if (null != generalb[i].getCinventoryid()
//						&& !"".equals(generalb[i].getCinventoryid().trim())) {
//					String strWhere = "  dr = 0 and pk_invbasdoc = '"
//							+ generalb[i].getCinventoryid().trim()
//							+ "' and whs_status = 0 order by whs_batchcode ";
//					List list = (List) this.getIuap().retrieveByClause(
//							TbWarehousestockVO.class, strWhere);
//					TbWarehousestockVO[] wareVO = null;
//					// ��ȡӦ��������
//					asum = generalb[i].getNshouldoutassistnum().toDouble();
//					if (null != list && list.size() > 0) {
//						wareVO = new TbWarehousestockVO[list.size()];
//						wareVO = (TbWarehousestockVO[]) list.toArray(wareVO);
//						for (int j = 0; j < wareVO.length; j++) {
//							TbWarehousestockVO warevo = wareVO[j];
//							int com = -1; // ��ǰ���ں����ݿ����������ڵ�ʱ���
//							int alertnum = -1; // ����������ѯ���������۾�������
//							if (null != warevo) {
//								String bathdate = ""; // �洢��ȡ�����������
//								if (warevo.getWhs_batchcode().length() > 8)
//									bathdate = warevo.getWhs_batchcode()
//											.substring(0, 8);
//								long DAY = 24L * 60L * 60L * 1000L;
//								SimpleDateFormat format = new SimpleDateFormat(
//										"yyyyMMdd");
//								Date date = new Date();
//								try {
//									// ���� ��ǰ���ں����ݿ����������ڵ�ʱ���
//									com = Integer
//											.parseInt(((date.getTime() - format
//													.parse(bathdate).getTime()) / DAY)
//													+ "");
//								} catch (RuntimeException e) {
//									// TODO Auto-generated catch block
//									return "���ݿ����������ڲ��ϸ�";
//								}
//								// ���ݵ�Ʒ������ѯ���õ�Ʒ��ת�⾯������
//								String sql = "select b.def16,m.scalefactor from bd_invbasdoc b ,bd_measdoc m  where b.pk_invbasdoc = '"
//										+ generalb[i].getCinventoryid().trim()
//										+ "' and b.pk_measdoc1 = m.pk_measdoc and b.dr = 0 and m.dr=0";
//								ArrayList resultsList = (ArrayList) this
//										.getIuap().executeQuery(sql,
//												new ArrayListProcessor());
//								Object[] a = null;
//								if (null != resultsList
//										&& resultsList.size() > 0) {
//									a = (Object[]) resultsList.get(0);
//								}
//								if (null != a && a.length > 0 && null != a[0]) {
//									alertnum = Integer
//											.parseInt(a[0].toString());
//								} else {
//
//									return "'"
//											+ this.getInvbasdocName(generalb[i]
//													.getCinventoryid())
//											+ "' ���������û��ά����������";
//								}
//
//								// �����ǰ���ں����ε�ʱ���С�ھ������� �û�Ʒ���Գ���
//								if (com < alertnum) {
//									if (null != warevo.getWhs_stockpieces()
//											&& !"".equals(warevo
//													.getWhs_stockpieces())
//											&& warevo.getWhs_stockpieces()
//													.toDouble() > 0) {
//										generalt = new TbOutgeneralTVO();
//										double tmpsum = asum;
//										// ���������ȥӦ������
//										asum = warevo.getWhs_stockpieces()
//												.toDouble()
//												- asum;
//										// ������
//										double noutnum = 0;
//										if (null != a[1])
//											noutnum = Double.parseDouble(a[1]
//													.toString());
//										// ���ʣ���������ڵ���0 ��ǰ���̴��������
//										if (asum >= 0) {
//											// �ѵ�ǰ�������洢ʵ������
//											generalt
//													.setNoutassistnum(new UFDouble(
//															tmpsum));
//											// ����ʵ��������
//											if (noutnum != 0) {
//												generalt
//														.setNoutnum(new UFDouble(
//																tmpsum
//																		* noutnum));
//											}
//										} else {
//											// ���ʣ������С��0 ��ǰ���̴��������������ǰ���̴����Ϊ������
//											asum = warevo.getWhs_stockpieces()
//													.toDouble()
//													- asum;
//											asum = Math.abs(asum);
//											generalt.setNoutassistnum(warevo
//													.getWhs_stockpieces());
//											if (noutnum != 0) {
//												generalt
//														.setNoutnum(new UFDouble(
//																warevo
//																		.getWhs_stockpieces()
//																		.toDouble()
//																		* noutnum));
//											}
//										}
//
//										if (null != generalb[i]
//												.getCsourcebillhid()
//												&& !"".equals(generalb[i]
//														.getCsourcebillhid())) {
//											generalt
//													.setCfirstbillhid(generalb[i]
//															.getCsourcebillhid()); // Դͷ����
//										}
//										if (null != generalb[i]
//												.getCsourcebillbid()
//												&& !"".equals(generalb[i]
//														.getCsourcebillbid())) { // Դͷ�ӱ�
//											generalt
//													.setCfirstbillbid(generalb[i]
//															.getCsourcebillbid());
//										}
//										if (null != generalb[i]
//												.getVsourcebillcode()
//												&& !"".equals(generalb[i]
//														.getVsourcebillcode())) {
//											generalt
//													.setVsourcebillcode(generalb[i]
//															.getVsourcebillcode()); // ��Դ���ݺ�
//										}
//										if (null != warevo.getPplpt_pk()
//												&& !"".equals(warevo
//														.getPplpt_pk())) {
//											generalt.setCdt_pk(warevo
//													.getPplpt_pk()); // ��������
//										}
//										if (null != warevo.getWhs_stockpieces()
//												&& !"".equals(warevo
//														.getWhs_stockpieces())) {
//											generalt.setStockpieces(warevo
//													.getWhs_stockpieces()); // �������
//										}
//										if (null != warevo
//												.getWhs_stocktonnage()
//												&& !"".equals(warevo
//														.getWhs_stocktonnage())) {
//											generalt.setStocktonnage(warevo
//													.getWhs_stocktonnage()); // ��渨����
//										}
//										if (null != warevo.getPk_invbasdoc()
//												&& !"".equals(warevo
//														.getPk_invbasdoc())) {
//											generalt.setPk_invbasdoc(warevo
//													.getPk_invbasdoc()); // �������
//										}
//										if (null != warevo.getWhs_batchcode()
//												&& !"".equals(warevo
//														.getWhs_batchcode())) {
//											generalt.setVbatchcode(warevo
//													.getWhs_batchcode()); // ����
//										}
//										generalt.setDr(0); // ɾ����־
//										// ��������
//										this.getIvo().insertVO(generalt);
//
//										if (asum >= 0) {
//											break;
//										}
//									}
//
//								}
//							}
//						}
//					}
//
//				}
//			}
//
//		}
		return null;
	}

	/**
	 * ���ݳ����ӱ���Ϣɾ��������������
	 * 
	 * @param generalb
	 *            �����ӱ�
	 * @throws BusinessException
	 */
	private void deleteAutoPickAction(TbOutgeneralBVO[] generalb)
			throws BusinessException {
		if (null != generalb && generalb.length > 0) {
			for (int i = 0; i < generalb.length; i++) {
				String strWhere = " pk_invbasdoc='"
						+ generalb[i].getCinventoryid()
						+ "' and cfirstbillbid ='"
						+ generalb[i].getCsourcebillbid() + "'";
				this.getIvo().deleteByClause(TbOutgeneralTVO.class, strWhere);
			}
		}
	}

	public IVOPersistence getIvo() {
		return (IVOPersistence) NCLocator.getInstance().lookup(
				IVOPersistence.class.getName());
	}

	public void setIvo(IVOPersistence ivo) {
		this.ivo = ivo;
	}

	public IUAPQueryBS getIuap() {
		return (IUAPQueryBS) NCLocator.getInstance().lookup(
				IUAPQueryBS.class.getName());
	}

	public void setIuap(IUAPQueryBS iuap) {
		this.iuap = iuap;
	}

}
