package nc.bs.wds.ic.allocation.in;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import javax.naming.NamingException;

import nc.bs.dao.BaseDAO;
import nc.bs.scm.pub.smart.SmartDMO;
import nc.bs.trade.business.HYPubBO;
import nc.vo.ic.other.in.OtherInBillVO;
import nc.vo.ic.pub.TbGeneralBVO;
import nc.vo.ic.pub.TbGeneralHVO;
import nc.vo.ic.pub.bill.GeneralBillHeaderVO;
import nc.vo.ic.pub.bill.GeneralBillItemVO;
import nc.vo.ic.pub.bill.GeneralBillVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.trade.pub.IBillStatus;
import nc.vo.trade.pub.IExAggVO;
import nc.vo.wds.ic.write.back4y.MultiBillVO;
import nc.vo.wds.ic.write.back4y.Writeback4yB1VO;
import nc.vo.wds.ic.write.back4y.Writeback4yB2VO;
import nc.vo.wds.ic.write.back4y.Writeback4yHVO;
import nc.vo.wl.pub.WdsWlPubConst;

import org.apache.tools.ant.BuildException;

/**
 * ����������ⵥǩ�� ��ʽ���ɵ������ش���
 * 
 * @author lxg
 * 
 */
public class ChangToWDSP {
	HYPubBO pubbo = null;

	HYPubBO getHypubBO() {
		if (pubbo == null) {
			pubbo = new HYPubBO();
		}
		return pubbo;
	}

	BaseDAO dao = null;

	BaseDAO getBaseDAO() {
		if (dao == null) {
			dao = new BaseDAO();
		}
		return dao;
	}

	private String coperator = null;
	private String pk_corp = null;
	private UFDate logDate = null;

	/**
	 * 
	 * @���ߣ�lxg
	 * @˵�������ɽ������Ŀ ������ⵥǩ�֣����ɵ������ش���
	 * @ʱ�䣺2011-11-5����13:50:05
	 * @param billVO
	 * @param coperator
	 *            ����Ա
	 * @param pk_crop
	 *            ��¼��˾
	 * @param date
	 *            ��¼����
	 * @return
	 * @throws BusinessException
	 */
	public void onSign(AggregatedValueObject billVO, String coperator,
			String pk_corp, String date) throws BusinessException {
		if (billVO == null) {
			return;
		}
		this.coperator = coperator;
		this.pk_corp = pk_corp;
		this.logDate = new UFDate(date);
		// ���尴�յ������������飨��ǰһ��ֻ��һ���������⣩<�������ⵥ������������������ⵥ����VO>
		HashMap<String, ArrayList<TbGeneralBVO>> map = new HashMap<String, ArrayList<TbGeneralBVO>>();
		TbGeneralHVO head = null;
		if (billVO instanceof IExAggVO) {
			OtherInBillVO bill = (OtherInBillVO) billVO;
			head = (TbGeneralHVO) bill.getParentVO();
			TbGeneralBVO[] bvos = (TbGeneralBVO[]) bill.getTableVO(bill
					.getTableCodes()[0]);
			if(bvos == null || bvos.length ==0){
				return ;
			}
			for (TbGeneralBVO bvo : bvos) {
				String key = PuPubVO.getString_TrimZeroLenAsNull(bvo
						.getGylbillhid());// getGylbillhid
				if (key == null) {
					continue;
				}
				if (map.containsKey(key)) {
					map.get(key).add(bvo);
				} else {
					ArrayList<TbGeneralBVO> list = new ArrayList<TbGeneralBVO>();
					list.add(bvo);
					map.put(key, list);
				}
			}
		}
		// �� ������ⵥ ת�� �ɵ������ش���
		if (map.size() == 0) {
			return;
		}
		for (String key : map.keySet()) {
			MultiBillVO writeBillVO = getMutiBillvo(key, head, map.get(key));
			// ����Դ��ϸ�ϵ��������ܼӵ���������ϣ�Writeback4yB2VO-->Writeback4yB1VO��
			sumNums(writeBillVO);
			getHypubBO().saveBill(writeBillVO);
		}
	}

	/**
	 * 
	 * @���ߣ�lxg
	 * @˵�������ɽ������Ŀ ������ⵥȡ��ǩ�֣����ɵ������ش���
	 * @ʱ�䣺2011-11-05����14:37:05
	 * @param billVO
	 * @param coperator
	 *            ����Ա
	 * @param pk_crop
	 *            ��¼��˾
	 * @param date
	 *            ��¼����
	 * @return
	 * @throws BusinessException
	 */
	public void onCanclSign(AggregatedValueObject billVO, String coperator,
			String pk_corp, String date) throws BusinessException {

		if (billVO == null) {
			return;
		}
		this.coperator = coperator;
		this.pk_corp = pk_corp;
		this.logDate = new UFDate(date);
		// ��ѯ������ⵥ��Ӧ�� ���ε���
		TbGeneralHVO head = (TbGeneralHVO) billVO.getParentVO();
		StringBuffer bur = new StringBuffer();
		bur.append(" isnull(dr,0)=0  ");
		bur.append(" and pk_corp='" + pk_corp + "'");
		bur.append(" and pk_wds_writeback4Y_h in (");
		bur
				.append(" select distinct pk_wds_writeback4Y_h from wds_writeback4y_b2 ");
		bur.append(" where isnull(dr,0)=0 and csourcebillhid='"
				+ head.getPrimaryKey() + "'");
		bur.append(" )");
		Writeback4yHVO[] writeHvos = (Writeback4yHVO[]) getHypubBO()
				.queryByCondition(Writeback4yHVO.class, bur.toString());
		if (writeHvos != null && writeHvos.length > 0) {
			for (Writeback4yHVO writeHead : writeHvos) {
				Integer vbillstatus = PuPubVO.getInteger_NullAs(writeHvos[0]
						.getVbillstatus(), IBillStatus.FREE);
				if (vbillstatus == IBillStatus.CHECKPASS) {
					throw new BusinessException("���ε������̨�˻�д�Ѿ��������,����������");
				}
				MultiBillVO writeBillVO = new MultiBillVO();
				String where = " isnull(dr,0)=0 and pk_wds_writeback4Y_h = '"
						+ writeHead.getPrimaryKey() + "'";
				Writeback4yB1VO[] b1vos = (Writeback4yB1VO[]) getHypubBO()
						.queryByCondition(Writeback4yB1VO.class, where);
				Writeback4yB2VO[] b2vos = (Writeback4yB2VO[]) getHypubBO()
						.queryByCondition(Writeback4yB2VO.class, where);
				// ���ɸõ�����ⵥ���� �������ش���Դ��ϸ ɾ��
				boolean isDel = false;
				int count = 0;
				if (b2vos != null) {
					for (Writeback4yB2VO b2vo : b2vos) {
						String csourcebillhid = b2vo.getCsourcebillhid();
						if (head.getPrimaryKey().equalsIgnoreCase(
								csourcebillhid)) {
							b2vo.setStatus(VOStatus.DELETED);
							count++;
						}
					}
				}
				if (count == b2vos.length)
					isDel = true;
				writeBillVO.setParentVO(writeHvos[0]);
				writeBillVO.setTableVO(writeBillVO.getTableCodes()[0], b1vos);
				writeBillVO.setTableVO(writeBillVO.getTableCodes()[1], b2vos);
				if (isDel) {
					getHypubBO().deleteBill(writeBillVO);
				} else {
					// ����Դ��ϸ�ϵ��������ܼӵ����������ϣ�Writeback4yB2VO-->Writeback4yB1VO��
					sumNumsDel(writeBillVO);
					getHypubBO().saveBill(writeBillVO);
				}
			}
		} else {
			throw new BuildException("��ѯ���ε������ش������쳣��δ��ѯ������");
		}
	}

	/**
	 * 
	 * @���ߣ�lxg
	 * @˵�������ɽ������Ŀ
	 * @ʱ�䣺2011-11-05����14:50:05
	 * @param key
	 * @param head
	 * @param list
	 * @return
	 * @throws BusinessException
	 */
	public MultiBillVO getMutiBillvo(String key, TbGeneralHVO head,
			ArrayList<TbGeneralBVO> list) throws BusinessException {
		MultiBillVO writeBillVO = new MultiBillVO();
		GeneralBillVO generalVO = null;
		try {
			generalVO = getGeneralBill(key);
		} catch (Exception e) {
			e.printStackTrace();
			throw new BusinessException("��ѯ�������ⵥʧ��");
		}
		GeneralBillHeaderVO generalHead = generalVO.getHeaderVO();
		GeneralBillItemVO[] generalBods = generalVO.getItemVOs();
		if (generalBods == null || generalBods.length == 0) {
			throw new BusinessException("��ѯ�����������ʧ��");
		}
		// ���ݵ������� ��������ѯ�Ƿ��Ѿ����ɵ������ش������������ش��� ��ͷ�����������id,
		// һһ��Ӧ��ϵ��
		String strWhere = " isnull(dr,0)=0 and cgeneralhid='" + key
				+ "' and pk_corp='" + pk_corp + "'";
		Writeback4yHVO[] writeHvos = (Writeback4yHVO[]) getHypubBO()
				.queryByCondition(Writeback4yHVO.class, strWhere);
		if (writeHvos != null && writeHvos.length > 0) {// --������ε����Ѵ���
			Integer vbillstatus = PuPubVO.getInteger_NullAs(writeHvos[0]
					.getVbillstatus(), IBillStatus.FREE);
			if (vbillstatus == IBillStatus.CHECKPASS) {
				throw new BusinessException("���ε�������д���Ѿ��������,���������");
			}
			String where = " isnull(dr,0)=0 and pk_wds_writeback4Y_h = '"
					+ writeHvos[0].getPrimaryKey() + "'";
			Writeback4yB1VO[] b1vos = (Writeback4yB1VO[]) getHypubBO()
					.queryByCondition(Writeback4yB1VO.class, where);
			Writeback4yB2VO[] b2vos = (Writeback4yB2VO[]) getHypubBO()
					.queryByCondition(Writeback4yB2VO.class, where);
			int i = 0;
			if (b2vos != null) {
				i = 10 * b2vos.length;
			}
			Writeback4yB2VO[] b2vosNew = getWriteBackB2vo(i, head, list);
			ArrayList<Writeback4yB2VO> b2list = new ArrayList<Writeback4yB2VO>();
			b2list.addAll(Arrays.asList(b2vos));
			b2list.addAll(Arrays.asList(b2vosNew));
			writeBillVO.setParentVO(writeHvos[0]);
			writeBillVO.setTableVO(writeBillVO.getTableCodes()[0], b1vos);
			writeBillVO.setTableVO(writeBillVO.getTableCodes()[1], b2list
					.toArray(new Writeback4yB2VO[0]));
		} else {// ---������ε��ݲ�����
			Writeback4yHVO hvo = getWrirteBackHvo(key, generalHead.getVbillcode());
			Writeback4yB1VO[] b1vos = getWriteBackB1vo(generalHead, generalBods);
			Writeback4yB2VO[] b2vos = getWriteBackB2vo(0, head, list);
			writeBillVO.setParentVO(hvo);
			writeBillVO.setTableVO(writeBillVO.getTableCodes()[0], b1vos);
			writeBillVO.setTableVO(writeBillVO.getTableCodes()[1], b2vos);
		}
		return writeBillVO;
	}

	/**
	 * 
	 * @���ߣ�lyf
	 * @˵�������ɽ������Ŀ :���ɵ������ش�����ͷ��Ϣ
	 * @ʱ�䣺2011-11-9����07:35:18
	 * @param cgeneralhid:ERP������ⵥ����
	 * @param vbillno:erp�������ⵥ���ݺ�
	 * @return
	 */
	public Writeback4yHVO getWrirteBackHvo(String cgeneralhid, String vbillno) {
		Writeback4yHVO head = new Writeback4yHVO();
		head.setCgeneralhid(cgeneralhid);// �������ⵥ���ݺ�
		head.setVbillno(vbillno);// �������ⵥ�ݺ�
		head.setVbillstatus(IBillStatus.FREE);// ����״̬
		head.setPk_billtype(WdsWlPubConst.WDSP);// ��������
		head.setStatus(VOStatus.NEW);
		head.setPk_corp(pk_corp);
		return head;
	}

	/**
	 * 
	 * @���ߣ�lxg
	 * @˵�������ɽ������Ŀ :���ݵ������
	 * @ʱ�䣺2011-10-26����15:15:05
	 * @return
	 */
	public Writeback4yB1VO[] getWriteBackB1vo(GeneralBillHeaderVO generalHead,
			GeneralBillItemVO[] generalBods) {
		if (generalBods == null || generalBods.length == 0) {
			return null;
		}
		Writeback4yB1VO[] b1vos = new Writeback4yB1VO[generalBods.length];
		for (int i = 0; i < generalBods.length; i++) {
			b1vos[i] = new Writeback4yB1VO();
			b1vos[i].setCrowno("" + (i + 1) * 10);// �к�
			b1vos[i].setCfirstbillhid(generalBods[i].getCfirstbillhid());// ����������Ϣ
			b1vos[i].setCfirstbillbid(generalBods[i].getCfirstbillbid());
			b1vos[i].setCfirsttype(generalBods[i].getCfirsttype());
			b1vos[i].setVfirstbillcode(generalBods[i].getVfirstbillcode());
			b1vos[i].setCgeneralhid(generalBods[i].getCgeneralhid());// �����������Ϣ
			b1vos[i].setCgeneralbid(generalBods[i].getCgeneralbid());
			b1vos[i].setVbillcode(generalHead.getVbillcode());
			b1vos[i].setCbilltypecode(generalHead.getCbilltypecode());
			b1vos[i].setPk_invmandoc(generalBods[i].getCinventoryid());// �������id
			b1vos[i].setPk_invbasdoc(generalBods[i].getCinvmanid());// �������id
			b1vos[i].setUnit(generalBods[i].getPk_measdoc());// �����������λ
			b1vos[i].setAssunit(generalBods[i].getCastunitid());// �����������λ
			b1vos[i].setNnumber(generalBods[i].getNshouldoutnum());// Ӧ������
			b1vos[i].setNpacknumber(generalBods[i].getNshouldoutassistnum());// Ӧ��������
			b1vos[i].setNoutnum(generalBods[i].getNoutnum());// ʵ������
			b1vos[i].setNoutassistnum(generalBods[i].getNoutassistnum());// Ӧ��������
		}
		return b1vos;
	}

	/**
	 * 
	 * @���ߣ�lxg
	 * @˵�������ɽ������Ŀ
	 * @ʱ�䣺2011-10-26����15:22:05
	 * @param list
	 * @return
	 */
	public Writeback4yB2VO[] getWriteBackB2vo(int rowNo, TbGeneralHVO head,
			ArrayList<TbGeneralBVO> list) {
		if (list == null || list.size() == 0) {
			return null;
		}
		Writeback4yB2VO[] b2vos = new Writeback4yB2VO[list.size()];
		for (int i = 0; i < list.size(); i++) {
			b2vos[i] = new Writeback4yB2VO();
			rowNo = rowNo + 10;
			b2vos[i].setCrowno("" + rowNo);// �к�
			// ����������Ϣ
			b2vos[i].setCfirsttype(list.get(i).getCfirsttype());
			b2vos[i].setCfirstbillhid(list.get(i).getCfirstbillhid());
			b2vos[i].setCfirstbillbid(list.get(i).getCfirstbillbid());
			b2vos[i].setVfirstbillcode(list.get(i).getVfirstbillcode());
			// �������ⵥ��Ϣ
			b2vos[i].setCgeneralhid(list.get(i).getGylbillhid());
			b2vos[i].setCgeneralbid(list.get(i).getGylbillbid());
			b2vos[i].setVbillcode(list.get(i).getGylbillcode());
			b2vos[i].setCbilltypecode(list.get(i).getGylbilltype());
			// �������������Ϣ
			b2vos[i].setCsourcebillhid(list.get(i).getGeh_pk());
			b2vos[i].setCsourcebillbid(list.get(i).getGeb_pk());
			b2vos[i].setVsourcebillcode(head.getGeh_billcode());
			b2vos[i].setCsourcetype(WdsWlPubConst.BILLTYPE_ALLO_IN);
			b2vos[i].setPk_invmandoc(list.get(i).getGeb_cinventoryid());
			b2vos[i].setPk_invbasdoc(list.get(i).getGeb_cinvbasid());
			b2vos[i].setUnit(list.get(i).getCastunitid());
			b2vos[i].setAssunit(list.get(i).getCastunitid());
			b2vos[i].setNinnum(list.get(i).getGeb_anum());
			b2vos[i].setNinassistnum(list.get(i).getGeb_banum());
			b2vos[i].setStatus(VOStatus.NEW);
		}
		return b2vos;
	}

	/**
	 * 
	 * @���ߣ�lyf
	 * @˵�������ɽ������Ŀ ����ѯERP�������ⵥ
	 * @ʱ�䣺2011-11-9����07:42:11
	 * @param primarykey
	 *            :ERP�������ⵥ����
	 * @return
	 * @throws BusinessException
	 * @throws Exception
	 * @throws NamingException
	 */
	public GeneralBillVO getGeneralBill(String primarykey)
			throws BusinessException, Exception, NamingException {
		if (primarykey == null) {
			primarykey = "";
		}
		GeneralBillHeaderVO headvo = new GeneralBillHeaderVO();
		GeneralBillItemVO[] itemvo = new GeneralBillItemVO[0];
		// ��˾+��������+����
		GeneralBillVO bill = new GeneralBillVO();
		SmartDMO dmo = null;
		dmo = new SmartDMO();
		String bodySql = " select * from ic_general_b where isnull(dr,0)=0 and cgeneralhid='"
				+ primarykey + "'";
		headvo = (GeneralBillHeaderVO) dmo.selectByKey(
				GeneralBillHeaderVO.class, primarykey);
		itemvo = (GeneralBillItemVO[]) dmo.selectBySql2(bodySql,
				GeneralBillItemVO.class);
		bill.setParentVO(headvo);
		bill.setChildrenVO(itemvo);
		return bill;
	}

	/**
	 * 
	 * @���ߣ�lxg
	 * @˵�������ɽ������Ŀ ������ϸ�����������
	 * @ʱ�䣺2011-10-27����20:44:34
	 */
	private void sumNumsDel(MultiBillVO writeBillVO) {
		Writeback4yB1VO[] b1vos = (Writeback4yB1VO[]) writeBillVO
				.getTableVO(writeBillVO.getTableCodes()[0]);
		Writeback4yB2VO[] b2vos = (Writeback4yB2VO[]) writeBillVO
				.getTableVO(writeBillVO.getTableCodes()[1]);
		if (b2vos == null || b2vos.length == 0) {
			return;
		} else {
			for (Writeback4yB2VO b2vo : b2vos) {
				String cgeneralbid = PuPubVO.getString_TrimZeroLenAsNull(b2vo
						.getCgeneralbid());
				if (b2vo.getStatus() != VOStatus.DELETED)
					continue;
				UFDouble ninnum = PuPubVO.getUFDouble_NullAsZero(b2vo
						.getNinnum());
				UFDouble ninassistnum = PuPubVO.getUFDouble_NullAsZero(b2vo
						.getNinassistnum());
				for (Writeback4yB1VO b1vo : b1vos) {
					UFDouble ntotalInnum = PuPubVO.getUFDouble_NullAsZero(b1vo
							.getNinnum());// 
					UFDouble ntotalInassistnum = PuPubVO
							.getUFDouble_NullAsZero(b1vo.getNinassistnum());
					String cgeneralbid_total = b1vo.getCgeneralbid();
					if (cgeneralbid.equalsIgnoreCase(cgeneralbid_total)) {
						b1vo.setNinnum(ntotalInnum.add(ninnum));
						b1vo.setNinassistnum(ntotalInassistnum
								.add(ninassistnum));
						if (PuPubVO.getString_TrimZeroLenAsNull(b1vo
								.getPrimaryKey()) != null) {
							b1vo.setStatus(VOStatus.UPDATED);
						}
					}
				}
			}
		}
	}

	/**
	 * 
	 * @���ߣ�lxg
	 * @˵�������ɽ������Ŀ ������ϸ�������ܵ���������
	 * @ʱ�䣺2011-10-27����20:20:34
	 */
	private void sumNums(MultiBillVO writeBillVO) {
		Writeback4yB1VO[] b1vos = (Writeback4yB1VO[]) writeBillVO
				.getTableVO(writeBillVO.getTableCodes()[0]);
		Writeback4yB2VO[] b2vos = (Writeback4yB2VO[]) writeBillVO
				.getTableVO(writeBillVO.getTableCodes()[1]);
		if (b2vos == null || b2vos.length == 0) {
			return;
		} else {
			for (Writeback4yB2VO b2vo : b2vos) {
				String cgeneralbid = PuPubVO.getString_TrimZeroLenAsNull(b2vo
						.getCgeneralbid());
				String primaryKey = PuPubVO.getString_TrimZeroLenAsNull(b2vo
						.getPrimaryKey());
				if (cgeneralbid == null || primaryKey != null)// ֻ������������������Ϣ
																// ���ܵ� ��������ȥ
					continue;
				UFDouble ninnum = PuPubVO.getUFDouble_NullAsZero(b2vo
						.getNinnum());
				UFDouble ninassistnum = PuPubVO.getUFDouble_NullAsZero(b2vo
						.getNinassistnum());
				for (Writeback4yB1VO b1vo : b1vos) {
					UFDouble ntotalInnum = PuPubVO.getUFDouble_NullAsZero(b1vo
							.getNinnum());// 
					UFDouble ntotalInassistnum = PuPubVO
							.getUFDouble_NullAsZero(b1vo.getNinassistnum());
					String cgeneralbid_total = b1vo.getCgeneralbid();
					if (cgeneralbid.equalsIgnoreCase(cgeneralbid_total)) {
						b1vo.setNinnum(ntotalInnum.add(ninnum));
						b1vo.setNinassistnum(ntotalInassistnum
								.add(ninassistnum));
						if (PuPubVO.getString_TrimZeroLenAsNull(b1vo
								.getPrimaryKey()) != null) {
							b1vo.setStatus(VOStatus.UPDATED);
						}
					}
				}
			}
		}
	}

}
