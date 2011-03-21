package nc.bs.ncblk.bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.naming.NamingException;

import nc.bs.bd.b21.BusinessCurrencyRateUtil;
import nc.bs.ncblk.constants.Constants;
import nc.bs.ncblk.dmo.IyjcbDMO;
import nc.bs.ncblk.dmo.impl.YjcbDMOImpl;
import nc.bs.ncblk.exception.MyBsBeanException;
import nc.bs.ncblk.exception.MyDMOException;
import nc.ui.bd.b21.CurrParamQuery;
import nc.vo.bd.CorpVO;
import nc.vo.bd.b21.CurrinfoVO;
import nc.vo.ncblk.yjcb.yjcbjs.MyBillVO;
import nc.vo.ncblk.yjcb.yjcbjs.NcReportYjcbBVO;
import nc.vo.ncblk.yjcb.yjcbjs.NcReportYjcbVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.sm.UserVO;

//�½�ɱ���̨ҵ���߼�����
public class YjcbBean {

	private IyjcbDMO yjcbDMO = null;

	// ���췽��
	public YjcbBean() {
		try {
			yjcbDMO = new YjcbDMOImpl();
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}

	// ɾ���ӱ�����
	public boolean deleteYjcbJs(NcReportYjcbVO pvo) throws MyBsBeanException {
		String whereSrt = " pk_yjcb='" + pvo.getPk_yjcb() + "' and dr=0";
		try {
			yjcbDMO.deleteNcReportYjcbBVO(whereSrt);
			return true;
		} catch (MyDMOException e) {
			e.printStackTrace();
			throw new MyBsBeanException(e.getMessage());
		}
	}

	// ��������ս��
	public List jsAll(UserVO userVo, CorpVO corpVo, UFDate ufdate,
			NcReportYjcbVO pvo) throws MyBsBeanException {
		try {
			List rsList = new ArrayList();
			// �ڳ�����
			List qcsj = new ArrayList();

			// ��ȡ�ڳ����� ���ڳ����ݽ��д���(����ڳ�û�����ݣ���ȡ��һ���µ���ĩ����)
			qcsj = queryQcsj(pvo, qcsj);

			// ��ѯ�������������
			List scrkList = yjcbDMO.queryIaBillVoList(pvo, "I3");

			// ��ѯ���вɹ����
			List cgrkList = yjcbDMO.queryIaBillVoList(pvo, "I2");

			// ��ѯ����ί����� ID
			List wwrkList = yjcbDMO.queryIaBillVoList(pvo, "ID");

			// ��ѯ����������� I4
			List qrrkList = yjcbDMO.queryIaBillVoList(pvo, "I4");

			// ��ѯ�ɱ������� I9 IA
			List cbtzjList = yjcbDMO.queryIaBillVoList(pvo, "I9IA");

			// ��ѯ���в��ϳ��� I6
			List clckList = yjcbDMO.queryIaBillVoList(pvo, "I6");

			// ��ѯ����ί����� IC
			List wwckList = yjcbDMO.queryIaBillVoList(pvo, "IC");

			// ��ѯ������������ I7
			List qtckList = yjcbDMO.queryIaBillVoList(pvo, "I7");

			// ���۳�����I5
			List xsckList = yjcbDMO.queryIaBillVoList2(pvo, "I5");

			// ��������
			List xssrList = yjcbDMO.queryIaBillVoList(pvo, "xssr");

			// �ϲ��ڳ�����
			rsList = hbDate(rsList, qcsj, "I0");
			// �ϲ��������
			rsList = hbDate(rsList, scrkList, "I3");
			// �ϲ��ɹ����
			rsList = hbDate(rsList, cgrkList, "I2");
			// �ϲ�ί�����
			rsList = hbDate(rsList, wwrkList, "ID");

			// �ϲ��������
			rsList = hbDate(rsList, qrrkList, "I4");
			// �ϲ��ɱ�������
			rsList = hbDate(rsList, cbtzjList, "I9IA");
			// �ϲ����ϳ���
			rsList = hbDate(rsList, clckList, "I6");
			// �ϲ�ί�����
			rsList = hbDate(rsList, wwckList, "IC");
			// �ϲ���������
			rsList = hbDate(rsList, qtckList, "I7");

			// �ϲ����۳���
			rsList = hbDate(rsList, xsckList, "I5");

			// �ϲ���������
			rsList = hbDate(rsList, xssrList, "xssr");

			// ��Ҫ������ֶ��Լ�Ҫ������ֶν��д���
			rsList = hjProcess(rsList,pvo);
			
			//liuys ���û���
//			List<Number> hlinfo = getRowDigits_ExchangeRate(pvo);
//			List backList = hlJS(hlinfo,rsList);
			return rsList;
		} catch (MyDMOException e) {
			e.printStackTrace();
			throw new MyBsBeanException(e.getMessage());
		}
	}

	/**
	 * liuys add
	 * ���ݻ�������ʾ��ȼ�������������ص��ֶ�
	 * @param pvo
	 * @param qcsj
	 * @return
	 * @throws MyBsBeanException
	 */
	private List hlJS(List<Number> hlinfo,List reList){
		UFDouble hl = new UFDouble(hlinfo.get(0).toString());
		int jd = Integer.parseInt(hlinfo.get(1).toString());
		List rsList = new ArrayList();
		for(int i=0;i<reList.size();i++){
			NcReportYjcbBVO bvo = (NcReportYjcbBVO)reList.get(i);
//			qcjcje	�ڳ������	decimal(20,8)	��	
			bvo.setQcjcje(zerocl(bvo.getQcjcje().multiply(hl)));
//			qcjcdj	�ڳ���浥��	decimal(20,8)	��	
			bvo.setQcjcdj(zerocl(bvo.getQcjcdj().multiply(hl)));
//			scrkje	���������	decimal(20,8)	��	
			bvo.setScrkje(zerocl(bvo.getScrkje().multiply(hl)));
//			cgrkje	�ɹ������	decimal(20,8)	��	
			bvo.setCgrkje(zerocl(bvo.getCgrkje().multiply(hl)));
//			wwrkje	ί�������	decimal(20,8)	��	
			bvo.setWwrkje(zerocl(bvo.getWwrkje().multiply(hl)));
//			qtrkje	���������	decimal(20,8)	��	1.2����
			bvo.setQtrkje(zerocl(bvo.getQtrkje().multiply(hl)));
//			cbtzdje	�ɱ����������	decimal(20,8)	��	
			bvo.setCbtzdje(zerocl(bvo.getCbtzdje().multiply(hl)));
//			xgfyje	��ط��ý��	decimal(20,8)	��	
			bvo.setXgfyje(zerocl(bvo.getXgfyje().multiply(hl)));
//			bqrkdj	������ⵥ��	decimal(20,8)	��	
			bvo.setBqrkdj(zerocl(bvo.getBqrkdj().multiply(hl)));
//			bqrkje	���������	decimal(20,8)	��	
			bvo.setBqrkje(zerocl(bvo.getBqrkje().multiply(hl)));
//			xsckdj	���۳��ⵥ��	decimal(20,8)	��	
			bvo.setXsckdj(zerocl(bvo.getXsckdj().multiply(hl)));
//			xsckje	���۳�����	decimal(20,8)	��	
			bvo.setXsckje(zerocl(bvo.getXsckje().multiply(hl)));
//			xssrje	����������	decimal(20,8)	��	1.2����
			bvo.setXssrje(zerocl(bvo.getXssrje().multiply(hl)));
//			clckje	���ϳ�����	decimal(20,8)	��	1.2����
			bvo.setClckje(zerocl(bvo.getClckje().multiply(hl)));
//			wwckje	ί�������	decimal(20,8)	��	1.2����
			bvo.setWwckje(zerocl(bvo.getWwckje().multiply(hl)));
//			qtckje	����������	decimal(20,8)	��	1.2����
			bvo.setQtckje(zerocl(bvo.getQtckje().multiply(hl)));
//			ckhjje	����ϼƽ��	decimal(20,8)	��	1.2����
			bvo.setCkhjje(zerocl(bvo.getCkhjje().multiply(hl)));
//			xscbdj	���۳ɱ�����	decimal(20,8)	��	
			bvo.setXscbdj(zerocl(bvo.getXscbdj().multiply(hl)));
//			xscdje	���۳ɱ����	decimal(20,8)	��	
			bvo.setXscdje(zerocl(bvo.getXscdje().multiply(hl)));
//			qmjcdj	��ĩ��浥��	decimal(20,8)	��	
			bvo.setQmjcdj(zerocl(bvo.getQmjcdj().multiply(hl)));
//			qmjcje	��ĩ�����	decimal(20,8)	��	
			bvo.setQmjcje(zerocl(bvo.getQmjcje().multiply(hl)));
//			xscbjzje	���۳ɱ�������	decimal(20,8)	��	1.2����
			bvo.setXscbjzje(zerocl(bvo.getXscbjzje().multiply(hl)));
//			xscbtzhje	���۳ɱ��������	decimal(20,8)	��	1.2����
			bvo.setXscbtzhje(zerocl(bvo.getXscbtzhje().multiply(hl)));
			//�쳣�������
			bvo.setYctz(zerocl(bvo.getYctz().multiply(hl)));
			//����ǰ��ĩ����
			bvo.setTzqqmjcj(zerocl(bvo.getTzqqmjcj().multiply(hl)));
			rsList.add(bvo);
		}
		
		return rsList;
	}
	
	private UFDouble zerocl(UFDouble je){
		if(je.doubleValue() == 0)
			return new UFDouble(0);
		else
			return je;
	}
	// ��ȡ�ڳ����� ���ڳ����ݽ��д���(����ڳ�û�����ݣ���ȡ��һ���µ���ĩ����)
	private List queryQcsj(NcReportYjcbVO pvo, List qcsj)
			throws MyBsBeanException {
		// ����ǵ�һ���£���ȡ�ڳ�����
		if (qcsj != null && qcsj.size() > 0) {
			return qcsj;
		}
		// ȡ��һ���µ�����
		else {
			List rsList = new ArrayList();
			// �������
			String djnd = pvo.getDjnd();
			// �����ڼ�
			String djqj = pvo.getDjqj();
			UFDate currentDate = new UFDate(djnd + "-" + djqj + "-" + "01");
			int month = currentDate.getMonth();
			int year = currentDate.getYear();
			if (month != 1)
				month = month - 1;
			else {
				month = 12;
				year = year - 1;
			}
			if (month < 10)
				djqj = "0" + month;
			else
				djqj = String.valueOf(month);
			djnd = String.valueOf(year);
			// ��ȡ��һ�ڼ�ı�ͷVO
			try {
				MyBillVO billvo = yjcbDMO.queryMyBillVO_2(djnd, djqj);
				NcReportYjcbVO beforepvo = (NcReportYjcbVO) billvo
						.getParentVO();
				if (beforepvo.getIsstatus().booleanValue() == false)
					throw new MyBsBeanException("��ǰ���:" + djnd + ",�ڼ䣺" + djqj
							+ "�����½�ɱ����㻹û��ȷ�ϣ����Ƚ���ȷ�ϣ�");
				else {
					NcReportYjcbBVO[] cvos = (NcReportYjcbBVO[]) billvo
							.getChildrenVO();
					for (int i = 0; i < cvos.length; i++) {
						cvos[i].setPk_yjcb_b(null);
						cvos[i].setPk_yjcb(pvo.getPk_yjcb());
						// �ڳ�����
						cvos[i].setQcjcsl(cvos[i].getQmjcsl());
						cvos[i].setQcjcdj(cvos[i].getQmjcdj());
						cvos[i].setQcjcje(cvos[i].getQmjcje());
						// �������(����Ʒ���)
						cvos[i].setScrksl(new UFDouble(0));
						cvos[i].setScrkje(new UFDouble(0));
						// �ɹ����
						cvos[i].setCgrksl(new UFDouble(0));
						cvos[i].setCgrkje(new UFDouble(0));
						// ί�����
						cvos[i].setWwrksl(new UFDouble(0));
						cvos[i].setWwrkje(new UFDouble(0));
						// ������� added by zjj 2010-05-17
						cvos[i].setQtrksl(new UFDouble(0));
						cvos[i].setQtrkje(new UFDouble(0));
						// �ɱ�������
						cvos[i].setCbtzdje(new UFDouble(0));

						cvos[i].setXgfyje(new UFDouble(0));
						// �������
						cvos[i].setBqrksl(new UFDouble(0));
						cvos[i].setBqrkdj(new UFDouble(0));
						cvos[i].setBqrkje(new UFDouble(0));
						// ���۳���
						cvos[i].setXscksl(new UFDouble(0));
						cvos[i].setXsckje(new UFDouble(0));
						cvos[i].setXsckdj(new UFDouble(0));
						// �������� added by zjj 2010-05-17
						cvos[i].setXssrsl(new UFDouble(0));
						cvos[i].setXssrje(new UFDouble(0));
						// ���ϳ��� added by zjj 2010-05-17
						cvos[i].setClcksl(new UFDouble(0));
						cvos[i].setClckje(new UFDouble(0));
						// ί����� added by zjj 2010-05-17
						cvos[i].setWwcksl(new UFDouble(0));
						cvos[i].setWwckje(new UFDouble(0));
						// �������� added by zjj 2010-05-17
						cvos[i].setQtcksl(new UFDouble(0));
						cvos[i].setQtckje(new UFDouble(0));
						// ����ϼ� added by zjj 2010-05-17
						cvos[i].setCkhjsl(new UFDouble(0));
						cvos[i].setCkhjje(new UFDouble(0));

						cvos[i].setXscbdj(new UFDouble(0));
						cvos[i].setXscdje(new UFDouble(0));
						cvos[i].setChzzcs(new UFDouble(0));

						// ��ĩ���
						cvos[i].setQmjcsl(new UFDouble(0));
						cvos[i].setQmjcdj(new UFDouble(0));
						cvos[i].setQmjcje(new UFDouble(0));
						// ���۳ɱ� added by zjj 2010-05-17
						cvos[i].setXscbjzje(new UFDouble(0));
						cvos[i].setXscbtzhje(new UFDouble(0));

						rsList.add(cvos[i]);
					}
					return rsList;
				}
			} catch (MyDMOException e) {
				e.printStackTrace();
				throw new MyBsBeanException(e.getMessage());
			}
		}
	}

	// ���㱾�����ϼ�
	private List bqrkhj(List list) throws MyBsBeanException {
		List rsList = new ArrayList();
		for (int i = 0; i < list.size(); i++) {
			NcReportYjcbBVO bvo = (NcReportYjcbBVO) list.get(i);
			// ������������ϼ�
			UFDouble bqrksl = new UFDouble();
			// ������ⵥ��
			UFDouble bqrkdj = new UFDouble();
			// ���������
			UFDouble bqrkje = new UFDouble();

			// �����������=�������+ί�����+�ɹ����
			bqrksl = bvo.getScrksl().add(bvo.getWwrksl()).add(bvo.getCgrksl());
			bqrksl = new UFDouble(bqrksl.doubleValue(), Constants.XS_SHUNLIANG);

			// ���������=���������+ί��ÿ���+�ɹ������+�ɱ����۵�+��ط���
			bqrkje = bvo.getScrkje().add(bvo.getWwrkje()).add(bvo.getCgrkje())
					.add(bvo.getCbtzdje()).add(bvo.getXgfyje());
			bqrkje = new UFDouble(bqrkje.doubleValue(), Constants.XS_JINE);

			bqrkdj = bqrkje.div(bqrksl, Constants.XS_DANJIA);// ����

			bvo.setBqrksl(bqrksl);
			bvo.setBqrkje(bqrkje);
			bvo.setBqrkdj(bqrkdj);
			// ����ֵ
			rsList.add(bvo);
		}
		return rsList;
	}

	// �ϲ�����
	private List hbDate(List list_1, List list_2, String type)
			throws MyBsBeanException {
		Map rsMap = new HashMap();
		List rsList = new ArrayList();
		// ���list 1Ϊ�գ��򷵻� list 2
		if (list_1.size() == 0)
			return list_2;
		// ���list 2Ϊ�գ��򷵻� list 1
		if (list_2.size() == 0)
			return list_1;
		// ���list 1Ϊ�գ�list ҲΪ�գ��򷵻� �յ�list
		if (list_2.size() == 0 && list_1.size() == 0)
			return rsList;
		for (int i = 0; i < list_1.size(); i++) {
			NcReportYjcbBVO bvo_1 = (NcReportYjcbBVO) list_1.get(i);
			String key_1 = bvo_1.getChbm();
			rsMap.put(key_1, bvo_1);

		}

		for (int j = 0; j < list_2.size(); j++) {
			NcReportYjcbBVO bvo_2 = (NcReportYjcbBVO) list_2.get(j);

			String key_2 = bvo_2.getChbm();
			// �����һ���µ�VO
			if (rsMap.get(key_2) == null)
				rsMap.put(key_2, bvo_2);
			else {
				// ����Ѿ�����
				NcReportYjcbBVO tempVo = (NcReportYjcbBVO) rsMap.get(key_2);
				// �Ƴ�ԭ��VO
				rsMap.remove(key_2);
				// �ϲ� vo1��vo2
				// ������ڳ�����
				if (type.equals("I0")) {
					tempVo.setQcjcsl(bvo_2.getQcjcsl());
					tempVo.setQcjcdj(bvo_2.getQcjcdj());
					tempVo.setQcjcje(bvo_2.getQcjcje());

				}
				// �������(����Ʒ���)
				if (type.equals("I3")) {
					tempVo.setScrksl(bvo_2.getScrksl());
					tempVo.setScrkje(bvo_2.getScrkje());
				}
				// ��� �ɹ���� I2
				if (type.equals("I2")) {
					tempVo.setCgrksl(bvo_2.getCgrksl());
					tempVo.setCgrkje(bvo_2.getCgrkje());
				}
				// ί��ӹ� ��� ID
				if (type.equals("ID")) {
					tempVo.setWwrksl(bvo_2.getWwrksl());
					tempVo.setWwrkje(bvo_2.getWwrkje());
				}
				// ���۳��� I5
				if (type.equals("I5")) {
					tempVo.setXscksl(bvo_2.getXscksl());
					tempVo.setXsckdj(bvo_2.getXsckdj());
					tempVo.setXsckje(bvo_2.getXsckje());
					// tempVo.setXssrsl(bvo_2.getXssrsl());
				}

				/*
				 * 
				 * added by zjj 2010-05-18
				 */
				// ���ϳ���
				if (type.equals("I6")) {
					tempVo.setClckje(bvo_2.getClckje());
					tempVo.setClcksl(bvo_2.getClcksl());
				}
				// ��������
				if (type.equals("I7")) {
					tempVo.setQtckje(bvo_2.getQtckje());
					tempVo.setQtcksl(bvo_2.getQtcksl());
				}
				// ί�����
				if (type.equals("IC")) {
					tempVo.setWwckje(bvo_2.getWwckje());
					tempVo.setWwcksl(bvo_2.getWwcksl());
				}
				// �������
				if (type.equals("I4")) {
					tempVo.setQtrkje(bvo_2.getQtrkje());
					tempVo.setQtrksl(bvo_2.getQtrksl());
				}
				// ���ϼ�
				// if (type.equals("IIIJ")) {
				// tempVo.setBqrksl(bvo_2.getBqrksl());
				// tempVo.setBqrkje(bvo_2.getBqrkje());
				// tempVo.setBqrkdj(bvo_2.getBqrkdj());
				// }
				// �ɱ�����
				if (type.equals("I9IA")) {
					tempVo.setCbtzdje(bvo_2.getCbtzdje());//

				}
				if (type.equals("xssr")) {
					tempVo.setXssrsl(bvo_2.getXssrsl());
					tempVo.setXssrje(bvo_2.getXssrje());
				}

				// 

				/*
				 * added end
				 */

				rsMap.put(key_2, tempVo);
			}

		}

		// �ϲ����
		Set setkey = rsMap.keySet();
		Iterator iterator = setkey.iterator();

		while (iterator.hasNext()) {
			String key = (String) iterator.next();
			NcReportYjcbBVO obj = (NcReportYjcbBVO) rsMap.get(key);

			rsList.add(obj);
		}

		return rsList;
	}

	// ��������״̬
	public void updateStatusNcReportYjcbVO(NcReportYjcbVO pvo)
			throws MyBsBeanException {
		pvo.setDef4(new UFBoolean(true));
		try {
			yjcbDMO.updateNcReportYjcbVO(pvo);
		} catch (MyDMOException e) {
			e.printStackTrace();
			throw new MyBsBeanException(e.getMessage());
		}
	}

	// �������ռ�����
	public void saveVoList(List volist) throws MyBsBeanException {
		try {
			yjcbDMO.saveAllNcReportYjcbBVOList(volist);
		} catch (MyDMOException e) {
			e.printStackTrace();
			throw new MyBsBeanException(e.getMessage());
		}
	}

	/*
	 * 
	 * added by zjj 2010-05-18
	 */
	// ��Ҫ������ֶ��Լ�Ҫ������ֶν��д���
	public List hjProcess(List volist,NcReportYjcbVO pvo) {
		boolean flag = false;
		List reList = new ArrayList();
		for (int i = 0; i < volist.size(); i++) {
			NcReportYjcbBVO bvo = (NcReportYjcbBVO) volist.get(i);
			//System.out.println("i="+i+" "+bvo.getChbm()+"  "+bvo.getScrksl());
			if(bvo.getScrksl()==null)
				bvo.setScrksl(new UFDouble(0));
			// ���ϼ��� = �ɹ������ + ���������������ί����������������������
			UFDouble xs_cl11 = bvo.getCgrksl().add(bvo.getScrksl(),
					Constants.XS_SHUNLIANG);
			UFDouble xs_cl_ww11 = xs_cl11.add(bvo.getWwrksl(),
					Constants.XS_SHUNLIANG);
			UFDouble xs_cl_ww1_qt1 = xs_cl_ww11.add(bvo.getQtrksl(),
					Constants.XS_SHUNLIANG);
			bvo.setBqrksl(xs_cl_ww1_qt1);
			// ���ϼƽ� = �ɹ����� + �������𡡣���ί�����𡡣�����������
			UFDouble xs_cl111 = bvo.getCgrkje().add(bvo.getScrkje(),
					Constants.XS_JINE);
			UFDouble xs_cl_ww111 = xs_cl111.add(bvo.getWwrkje(),
					Constants.XS_JINE);
			UFDouble xs_cl_ww11_qt1 = xs_cl_ww111.add(bvo.getQtrkje(),
					Constants.XS_JINE);
			UFDouble xscl_sss11 = xs_cl_ww11_qt1.add(bvo.getCbtzdje(),
					Constants.XS_JINE);
			bvo.setBqrkje(xscl_sss11);

			// ��ⵥ�� = ���ϼƽ� / ���ϼ���
			bvo.setBqrkdj(xscl_sss11.div(xs_cl_ww1_qt1, Constants.XS_DANJIA));

			// ���ⵥ�� = ���ڳ����+����/���ڳ�����+���������
			bvo.setXsckdj(bvo.getQcjcje().add(bvo.getBqrkje()).div(
					bvo.getQcjcsl().add(bvo.getBqrksl()), Constants.XS_DANJIA));

			// ���۳���� ��ȡI5���۳��ⵥ����-������Ʒ����*���ⵥ��
			bvo.setXscdje(bvo.getXscksl().multiply(bvo.getXsckdj(),
					Constants.XS_JINE));

			// ���ϳ���� ȡI6���ϳ��ⵥ��*���ⵥ��
			bvo.setClckje(bvo.getClcksl().multiply(bvo.getXsckdj(),
					Constants.XS_JINE));
			// ��������� ȡI5���۳��ⵥ����*���ⵥ��
			//bvo.setXssrje(bvo.getXssrsl().multiply(bvo.getXsckdj(),
			//		Constants.XS_JINE));
			// ��������� ��ȡI7���շ����<>'ת�����'�������ⵥ��+������Ʒ����*���ⵥ��
			bvo.setQtckje(bvo.getQtcksl().multiply(bvo.getXsckdj(),
					Constants.XS_JINE));
			// ����ϼ��� = ���۳����� + ���ϳ�����+ ί������� +����������
			UFDouble xs_cl1 = bvo.getXscksl().add(bvo.getClcksl(),
					Constants.XS_SHUNLIANG);
			UFDouble xs_cl_ww1 = xs_cl1.add(bvo.getWwcksl(),
					Constants.XS_SHUNLIANG);
			UFDouble xs_cl_ww_qt1 = xs_cl_ww1.add(bvo.getQtcksl(),
					Constants.XS_SHUNLIANG);
			bvo.setCkhjsl(xs_cl_ww_qt1);

			// ����ϼƽ�=���۳���� + ���ϳ����+ ί������ +���������
			bvo.setCkhjje(bvo.getXscdje().add(bvo.getClckje()).add(bvo.getWwckje()).add(bvo.getQtckje()));
			
			//��ĩ�����=�ڳ������+���ϼ���-����ϼ���
			bvo.setQmjcsl(bvo.getQcjcsl().add(bvo.getBqrksl()).sub(bvo.getCkhjsl()));
			
			
			// ���۳ɱ�������� ��ʼ��0
			bvo.setXscbjzje(new UFDouble(0));
			// ���۳ɱ������� = ���۳ɱ��� + �ɱ��������
			bvo.setXscbtzhje(bvo.getXscdje().add(bvo.getXscbjzje(),
					Constants.XS_SHUNLIANG));
			
			/***************************liuys begin *****************************************/
			// add 2010-12-29 �ڵõ���������, ���ݽ���жϵó�"����ǰ��ĩ����","�쳣����"
			//QCJCJ+NINMNY-(XSCKJ+CLJE+WWCKJ+QTCKJ)�൱��ԭ������ĩ�����Ĺ�ʽ
			//����ǰ��ĩ���� = ԭ��ĩ�����				
				bvo.setTzqqmjcj(bvo.getQcjcje().add(bvo.getBqrkje()).sub(bvo.getCkhjje()));
				//�쳣���� ȡ������Ϊ0ʱ��Ϊ0������Ϊ�������Ϊ�������Ľ��*��-1��
				//����������Ϊ0�ҽ�Ϊ0
			if(bvo.getQmjcsl().doubleValue()==0 && bvo.getTzqqmjcj().doubleValue()!=0){
				if(bvo.getTzqqmjcj().doubleValue()>0)
						bvo.setYctz(new UFDouble(-(bvo.getTzqqmjcj().doubleValue())));
				else
					bvo.setYctz(bvo.getTzqqmjcj().abs());
			}
			//�������Ϊ�������Ϊ�������Ľ��
			if(bvo.getQmjcsl().doubleValue()>0 && bvo.getTzqqmjcj().doubleValue()<0)
				bvo.setYctz(bvo.getTzqqmjcj().abs());
			//������  ->��ĩ����=�ڳ�����+���ϼƽ�-����ϼƽ� +�쳣�������
			bvo.setQmjcje(bvo.getQcjcje().add(bvo.getBqrkje()).sub(bvo.getCkhjje()).add(bvo.getYctz()));
			/***************************liuys end *****************************************/
			
			//�����ص��ֶ�
			reList.add(bvo);

		}
		return reList;
	}
	
	 /**
	   * ���ߣ�liuys 
	   * ���ܣ������۱������Լ�С��λ 
	   * ������String pk_corp 
	   * ��˾ID int iflag 
	   * ���أ��� ���⣺��
	   * ���ڣ�(2010-12-30 11:39:21) �޸����ڣ��޸��ˣ��޸�ԭ��
	   */
//	  private List<Number> getRowDigits_ExchangeRate(NcReportYjcbVO pvo) throws MyBsBeanException{
//		  	// ȡ�ñ���
//		String sCurrId = pvo.getCcurrencytypeid();
//		String sPk_corp = pvo.getPk_corp();
//		Integer iDigit = new Integer(2);
//		List hlinfo = new ArrayList();
//		try {
//			BusinessCurrencyRateUtil curTool = new BusinessCurrencyRateUtil(
//					sPk_corp);
//			CurrinfoVO currinfoVO = curTool.getCurrinfoVO(sCurrId,
//					CurrParamQuery.getInstance().getLocalCurrPK(sPk_corp));
//			UFDouble hl = curTool.getAdjustRate(CurrParamQuery.getInstance()
//					.getLocalCurrPK(sPk_corp), sCurrId, null, pvo.getDjnd(),
//					pvo.getDjqj());
//			// �������Ϊ0,��ôֱ��ȡ1
//			if (hl == null || hl.isTrimZero())
//				hlinfo.add(1);
//			else
//				hlinfo.add(hl);
//			//ȡ���ʾ���
//			if (currinfoVO.getRatedigit() != null) {
//				iDigit = currinfoVO.getRatedigit();
//				hlinfo.add(iDigit);
//			}else
//				hlinfo.add(6);
//		} catch (Exception e) {
//			throw new MyBsBeanException(e.getMessage()+"ȡ�����쳣��");
//		}
//		return hlinfo;
//	}
	
	// �������
	public List<NcReportYjcbBVO> Tiaozheng(double jine, NcReportYjcbVO nb)
			throws MyBsBeanException {
		try {
			List<NcReportYjcbBVO> reList = yjcbDMO.getXsList(nb);
			int size = reList.size();
			double total = 0.0;
			double max = 0.0;
			int index = 0;
			for (int i = 0; i < size; i++) {
				double temp = reList.get(i).getXscdje().doubleValue();
				if (jine > 0 && temp > max) {
					max = temp;
					index = i;
				}

				total += temp;
			}
			List<NcReportYjcbBVO> rl = new ArrayList();
			double totalH = 0.0;
			if (total != 0.0) {
				for (int i = 0; i < size; i++) {
					NcReportYjcbBVO t = reList.get(i);

					double tt = jine * (t.getXscdje().doubleValue() / total);
					double temp = tt + t.getXscdje().doubleValue();
					t.setXscbjzje(new UFDouble(tt, Constants.XS_JINE));
					t.setXscbtzhje(new UFDouble(temp, Constants.XS_JINE));
					totalH += t.getXscbjzje().doubleValue();
					rl.add(t);
				}
			} else {
				throw new MyBsBeanException("���۳ɱ����ܶ�Ϊ�㣡");
			}
			if (totalH > jine) {
				double chae = totalH - jine;
				NcReportYjcbBVO t = rl.get(index);
				double cur = t.getXscbjzje().doubleValue();
				double curr = t.getXscbtzhje().doubleValue();
				double now = cur - chae;
				double now1 = curr - chae;
				rl.remove(index);

				t.setXscbjzje(new UFDouble(now, Constants.XS_JINE));
				t.setXscbtzhje(new UFDouble(now1, Constants.XS_JINE));
				rl.add(t);

			} else if (totalH < jine) {
				double chae = jine - totalH;
				NcReportYjcbBVO t = rl.get(index);
				double cur = t.getXscbjzje().doubleValue();
				double curr = t.getXscbtzhje().doubleValue();
				double now = cur + chae;
				double now1 = curr + chae;
				rl.remove(index);
				t.setXscbjzje(new UFDouble(now, Constants.XS_JINE));
				t.setXscbtzhje(new UFDouble(now1, Constants.XS_JINE));
				rl.add(t);
			}
			this.UpdateAfterTz(rl);
			return rl;
		} catch (MyDMOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new MyBsBeanException(e.getMessage());
		}
	}

	// ȷ�Ϻ󱣴浽 blk_sfccb��
	public void SaveAfterQr(NcReportYjcbVO nb) throws MyBsBeanException {
		try {
			List<NcReportYjcbBVO> reList = this.yjcbDMO.getXsList(nb);
			yjcbDMO.saveSfccb(nb, reList);

		} catch (MyDMOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new MyBsBeanException(e.getMessage());
		}
	}

	// ���µ�������ı���
	public void UpdateAfterTz(List<NcReportYjcbBVO> reList)
			throws MyBsBeanException {
		try {
			yjcbDMO.updateVolist(reList);

		} catch (MyDMOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new MyBsBeanException(e.getMessage());
		}
	}

	// ͬ��blk_sfccb��
	public void deleteBlk_sfccb(String year, String month)
			throws MyBsBeanException {
		try {
			yjcbDMO.deleteBlk(year, month);
		} catch (MyDMOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new MyBsBeanException(e.getMessage());
		}
	}
}
