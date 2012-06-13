package nc.ui.wds.ic.other.out;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ArrayListProcessor;
import nc.ui.pub.ClientEnvironment;
import nc.vo.ic.other.out.TbOutgeneralBVO;
import nc.vo.ic.other.out.TbOutgeneralHVO;
import nc.vo.ic.pub.bill.GeneralBillHeaderVO;
import nc.vo.ic.pub.bill.GeneralBillItemVO;
import nc.vo.ic.pub.bill.GeneralBillVO;
import nc.vo.ic.pub.locator.LocatorVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDate;
import nc.vo.scm.pu.PuPubVO;

public class EventHandlerTools {
	
	private IUAPQueryBS iuap = null;
	private IUAPQueryBS getQueryBO(){
		if(iuap == null){
			iuap = (IUAPQueryBS) NCLocator.getInstance().lookup(
					IUAPQueryBS.class.getName());
		}
		return iuap;
	}

	/**
	 * ת�� �ѵ�ǰҳ���е�VOת����ERP�еĳ��ⵥ�ۺ�OV ���÷��� ���л�дERP�г��ⵥ
	 * 
	 * @return ERP�г��ⵥ�ۺ�VO
	 * @throws Exception
	 */
	protected GeneralBillVO changeReqOutgeneraltoGeneral(
			AggregatedValueObject obj) throws Exception {
		if (obj == null) {
			throw new BusinessException("��ѡ���ͷ����ǩ��");
		}
		// ���س���� ��ͷ
		TbOutgeneralHVO tmphvo = (TbOutgeneralHVO) obj.getParentVO();
		// ���س���� ����
		TbOutgeneralBVO[] tmpbvo = null;
		// ����ۺ�VO
		GeneralBillVO gBillVO = new GeneralBillVO();
		// �����ͷVO
		GeneralBillHeaderVO generalhvo = null;
		// �����ӱ���
		List<GeneralBillItemVO> generalBVOList = new ArrayList<GeneralBillItemVO>();
		// �����ӱ�VO����
		GeneralBillItemVO[] generalBVO = null;
		String sWhere = " dr = 0 and general_pk = '" + tmphvo.getGeneral_pk()
				+ "'";
		ArrayList list = (ArrayList) getQueryBO().retrieveByClause(
				TbOutgeneralHVO.class, sWhere);
		if (null != list && list.size() > 0) {
			TbOutgeneralHVO outhvo = (TbOutgeneralHVO) list.get(0);
			generalhvo = new GeneralBillHeaderVO();

			if (null != outhvo) {
				// �����ⵥ��ͷ��ֵ

				generalhvo.setPk_corp(outhvo.getPk_corp());// ��˾����
				generalhvo.setAttributeValue("cothercorpid", outhvo.getPk_corp());// �Է���˾
				// generalhvo.setCbiztypeid(outhvo.getCbiztype());// ҵ������
				generalhvo.setCbilltypecode("4I");// ��浥�����ͱ���
				generalhvo.setVbillcode(outhvo.getVbillcode());// ���ݺ�
				generalhvo.setDbilldate(outhvo.getDbilldate());// ��������
				generalhvo.setCwarehouseid(outhvo.getSrl_pk());// �ֿ�ID
				generalhvo.setAttributeValue("cotherwhid", outhvo.getSrl_pkr());// �����ֿ�
				generalhvo.setCdispatcherid(outhvo.getCdispatcherid());// �շ�����outhvo.getCdispatcherid()
				generalhvo.setCdptid(outhvo.getCdptid());// ����ID1021B110000000000BN9
				generalhvo.setCwhsmanagerid(outhvo.getCwhsmanagerid());// ���ԱID
				generalhvo.setCoperatorid(ClientEnvironment.getInstance()
						.getUser().getPrimaryKey());// �Ƶ���
				generalhvo.setAttributeValue("coperatoridnow",
						ClientEnvironment.getInstance().getUser()
								.getPrimaryKey());// ������
				generalhvo.setVdiliveraddress(outhvo.getVdiliveraddress());// ���˵�ַ
				generalhvo.setCbizid(outhvo.getCbizid());// ҵ��ԱID
				generalhvo.setVnote(outhvo.getVnote());// ��ע
				generalhvo.setFbillflag(2);// ����״̬
				generalhvo.setAttributeValue("clastmodiid", outhvo
						.getClastmodiid());// ����޸���
				generalhvo.setAttributeValue("tlastmoditime", outhvo
						.getTlastmoditime());// ����޸�ʱ��
				generalhvo
						.setAttributeValue("tmaketime", outhvo.getTmaketime());// �Ƶ�ʱ��
				sWhere = " dr = 0 and general_pk = '" + tmphvo.getGeneral_pk()
						+ "'";
				list = (ArrayList) getQueryBO().retrieveByClause(TbOutgeneralBVO.class,
						sWhere);
				if (null != list && list.size() > 0) {
					tmpbvo = new TbOutgeneralBVO[list.size()];
					tmpbvo = (TbOutgeneralBVO[]) list.toArray(tmpbvo);
					// �����帳ֵ
					for (int i = 0; i < tmpbvo.length; i++) {
						// ���ݱ��帽��--��λ
						LocatorVO locatorvo = new LocatorVO();
						locatorvo.setPk_corp(tmpbvo[i].getComp());
						boolean isBatch = false;
						GeneralBillItemVO generalb = new GeneralBillItemVO();
						generalb.setAttributeValue("pk_corp", outhvo.getPk_corp());// ��˾

						generalb.setCinvbasid(tmpbvo[i].getCinventoryid());// ���ID
						generalb.setVbatchcode(tmpbvo[i].getLvbatchcode());// ���κ�
						// ��ѯ�������ں�ʧЧ����
						String sql = "select dproducedate ,dvalidate  from scm_batchcode where pk_invbasdoc='"
								+ tmpbvo[i].getCinventoryid()
								+ "' and vbatchcode='"
								+ tmpbvo[i].getLvbatchcode() + "' and dr=0";
						ArrayList batchList = (ArrayList) getQueryBO().executeQuery(
								sql, new ArrayListProcessor());
						if (null != batchList && batchList.size() > 0) {
							Object[] batch = (Object[]) batchList.get(0);
							// ��������
							if (null != batch[0] && !"".equals(batch[0]))
								generalb
										.setScrq(new UFDate(batch[0].toString()));
							// ʧЧ����
							if (null != batch[0] && !"".equals(batch[0]))
								generalb.setDvalidate(new UFDate(batch[1]
										.toString()));
							isBatch = true;
						}
						String pk_invmandoc = "select pk_invmandoc from bd_invmandoc  where pk_invbasdoc='"
								+ tmpbvo[i].getCinventoryid().trim()
								+ "' and pk_corp='"
								+ outhvo.getPk_corp()
								+ "' and dr=0 ";
						ArrayList tmpList = (ArrayList) getQueryBO().executeQuery(
								pk_invmandoc, new ArrayListProcessor());
						if (null != tmpList && tmpList.size() > 0) {
							Object[] a = (Object[]) tmpList.get(0);
							generalb.setCinventoryid(PuPubVO
									.getString_TrimZeroLenAsNull(a[0]));// �������ID
						} else {
							isBatch = false;
						}
						generalb.setDbizdate(outhvo.getDbilldate());// ҵ������
						generalb.setNshouldoutnum(tmpbvo[i].getNshouldoutnum());// Ӧ������
						generalb.setNshouldoutassistnum(tmpbvo[i]
								.getNshouldoutassistnum());// Ӧ��������
						generalb.setNoutnum(tmpbvo[i].getNoutnum());// ʵ������
						locatorvo.setNoutspacenum(tmpbvo[i].getNoutnum());
						generalb.setNoutassistnum(tmpbvo[i].getNoutassistnum());// ʵ��������
						locatorvo.setNoutspaceassistnum(tmpbvo[i]
								.getNoutassistnum());
						locatorvo.setCspaceid(tmpbvo[i].getCspaceid());// ��λID
						generalb.setCastunitid(tmpbvo[i].getCastunitid());// ��������λID
						generalb.setNprice(tmpbvo[i].getNprice());// ����
						generalb.setNmny(tmpbvo[i].getNmny());// ���
						generalb.setCsourcebillhid(tmpbvo[i]
								.getCsourcebillhid());// ��Դ���ݱ�ͷ���к�
						generalb.setCfirstbillhid(tmpbvo[i].getCfirstbillhid());// Դͷ���ݱ�ͷID
						generalb.setCfreezeid(tmpbvo[i].getCsourcebillhid());// ������Դ
						generalb.setCsourcebillbid(tmpbvo[i]
								.getCsourcebillbid());// ��Դ���ݱ������к�
						generalb.setCfirstbillbid(tmpbvo[i].getCfirstbillbid());// Դͷ���ݱ���ID
						// generalb.setCsourcetype();// ��Դ��������
						// generalb.setCfirsttype();// Դͷ��������
						generalb.setVsourcebillcode(tmpbvo[i]
								.getVsourcebillcode());// ��Դ���ݺ�
						generalb.setVfirstbillcode(tmpbvo[i]
								.getVsourcebillcode());// Դͷ���ݺ�
						generalb.setVsourcerowno(tmpbvo[i].getCrowno());// ��Դ�����к�
						generalb.setVfirstrowno(tmpbvo[i].getCrowno());// Դͷ�����к�
						generalb.setFlargess(tmpbvo[i].getFlargess());// �Ƿ���Ʒ
						generalb.setDfirstbilldate(tmpbvo[i]
								.getDfirstbilldate());// Դͷ�����Ƶ�����
						generalb.setCrowno(tmpbvo[i].getCrowno());// �к�
						generalb.setHsl(tmpbvo[i].getHsl());// ������
						LocatorVO[] locatorVO = new LocatorVO[] { locatorvo };
						generalb.setLocator(locatorVO);
						if (isBatch)
							// �������ӱ�����Ӷ���
							generalBVOList.add(generalb);
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
		}

		return gBillVO;

	}
	
	/**
	 * ���������20λ ������Сʱ����������4λ�����
	 * 
	 * @return
	 */
	protected String getRandomNum() {
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddhhmmssms");
		String tmp = format.format(new Date());
		tmp = tmp + Math.round((Math.random() * 1000000));
		tmp = tmp.substring(0, 20);
		return tmp;
	}

}
