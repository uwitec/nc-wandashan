package nc.bs.hg.pu.conversion;

import nc.bs.hg.pu.pub.HgBsPubTool;
import nc.bs.ml.NCLangResOnserver;
import nc.bs.pu.conversion.TranPrayTools;
import nc.bs.pu.pub.PubDMO;
import nc.bs.scm.pub.BillRowNoDMO;
import nc.vo.hg.pu.pub.HgPubConst;
import nc.vo.po.pub.OrderPubVO;
import nc.vo.pr.pray.PraybillHeaderVO;
import nc.vo.pr.pray.PraybillItemVO;
import nc.vo.pr.pray.PraybillVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pu.BillTypeConst;
import nc.vo.scm.pub.vosplit.SplitBillVOs;

public class PlanVOTOPPrayVO implements nc.vo.pf.change.IchangeVO {
	/**
	 * SOrderVOTOPPrayVO ������ע�⡣
	 */
	public PlanVOTOPPrayVO() {
		super();
	}

	/**
	 * ���ߣ������ ���ܣ���֤VO�Ϸ��� ������ ���أ� ���⣺ ���ڣ�(2002-6-13 10:50:43) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 2002-06-13 wyf �޸��ж�����յĴ���
	 */
	private void checkVO(PraybillVO voPray) throws BusinessException {
		if (voPray.getBodyVO() == null) {
			PubDMO
					.throwBusinessException(new BusinessException(
							nc.bs.ml.NCLangResOnserver.getInstance()
									.getStrByID("4004pub", "UPP4004pub-000060")/*
																				 * @res
																				 * "�빺������Ϊ�գ��޷����ɣ�"
																				 */));
		}
		try{
			for (int i = 0; i < voPray.getChildNum(); i++) {
				PraybillItemVO item = (PraybillItemVO) voPray.getChildrenVO()[i];
				UFDouble nassistnum = item.getNassistnum();
				UFDouble npraynum = item.getNpraynum();
				if (nassistnum != null && npraynum != null) {
					if (nassistnum.doubleValue() > 0 && npraynum.doubleValue() < 0) {
						PubDMO
								.throwBusinessException(new BusinessException(
										nc.bs.ml.NCLangResOnserver.getInstance()
												.getStrByID("4004pub",
														"UPP4004pub-000061")/*
																			 * @res
																			 * "�빺�����븨����������һ�£��޷������빺����"
																			 */));
					}
					if (nassistnum.doubleValue() < 0 && npraynum.doubleValue() > 0) {
						PubDMO
								.throwBusinessException(new BusinessException(
										nc.bs.ml.NCLangResOnserver.getInstance()
												.getStrByID("4004pub",
														"UPP4004pub-000061")/*
																			 * @res
																			 * "�빺�����븨����������һ�£��޷������빺����"
																			 */));
					}
					//������Ƿ��Ѿ����䵽�˹�˾
					if (item.getCmangid() == null || item.getCmangid().trim().length() ==0){
						Object[][] retObj = new PubDMO().queryArrayValue(
									"bd_invbasdoc", "pk_invbasdoc",
									new String[] { "invname" }, new String[] { item
											.getCbaseid() });
						String invname = "";
						if(retObj != null && retObj[0]!=null && retObj[0][0] != null){
							invname =  retObj[0][0].toString();
						}
						retObj = new PubDMO().queryArrayValue(
								"bd_corp", "pk_corp",
								new String[] { "unitname" }, new String[] { voPray.getPk_corp()});
						String unitname = "";
						if(retObj != null && retObj[0]!=null && retObj[0][0] != null){
							unitname =  retObj[0][0].toString();
						}
						PubDMO.throwBusinessException(new BusinessException(
								NCLangResOnserver.getInstance().getStrByID(
										"common", "4004COMMON000000117",
										null,
										new String[]{invname,unitname}/* @res"���������Ϊ�գ���������{0}���Ƿ���䵽��˾{1} */)));
					}
				}
			}
		}catch(Exception e){
			PubDMO.throwBusinessException(e);
		}
	}
	/**
	 * ���ת�����VO�� �������ڣ�(2001-10-11 9:52:28)
	 * 
	 * @return java.lang.String[]
	 */
	public nc.vo.pub.AggregatedValueObject retChangeBusiVO(
			nc.vo.pub.AggregatedValueObject sorceVO,
			nc.vo.pub.AggregatedValueObject tagVO) throws BusinessException {
		try {
			PraybillVO voPray = (PraybillVO) tagVO;
			PraybillHeaderVO head = voPray.getHeadVO();
			PraybillItemVO[] items = voPray.getBodyVO();
			checkVO(voPray);
			//�������
			// �빺����PUSHSAVE�ű����Ѿ�����������
//			TranPrayTools.setCenterPurInfos(voPray,new Integer(8));
			//�빺����
//			TranPrayTools.setBusiAndPrayTypes(voPray,new Integer(8),false);
			head.setIpraytype(HgPubConst.IPRAYTYPE);
			for(PraybillItemVO item:items){
				item.setIpraytype(HgPubConst.IPRAYTYPE);
			}
			//��״̬
			PubDMO.setLineStatus(tagVO, new Integer(nc.vo.pub.VOStatus.NEW));
			//���鵥��
//			TranPrayTools.getInstance().setRulePrice(voPray);
			//���鶩������
//			TranPrayTools.setSuggestDate(voPray);
		} catch (Exception e) {
			PubDMO.throwBusinessException(e);
		}
		return tagVO;
	}

	/**
	 * ���ת�����VO�� �������ڣ�(2001-10-11 9:52:28)
	 * 
	 * @return java.lang.String[]
	 */
	public nc.vo.pub.AggregatedValueObject[] retChangeBusiVOs(
			nc.vo.pub.AggregatedValueObject[] sorceVOs,
			nc.vo.pub.AggregatedValueObject[] tagVOs) throws BusinessException {
		PraybillVO[] voaRet = null;
		try {
			int iLen = sorceVOs.length;
			for (int i = 0; i < iLen; i++) {
				retChangeBusiVO(sorceVOs[i], tagVOs[i]);
			}
			//���·ֵ�(���ΰ������ɶ����빺�����˴���ҵ��������������빺��)
			voaRet = (PraybillVO[]) SplitBillVOs.getSplitVOs(PraybillVO.class.getName(),
					PraybillHeaderVO.class.getName(),
					PraybillItemVO.class.getName(),
					tagVOs,
					null,
					new String[]{"cbiztype","ipraytype",HgPubConst.PRAY_BILL_PURTYPE_FIELDNAME});
			
			//���òɹ���ʽ����
			PraybillHeaderVO head = null;
			PraybillItemVO[] items = null;
			for (PraybillVO vo : voaRet) {//���ɹ���ʽ�ӱ���ת�Ƶ���ͷ�Զ�����
				head = vo.getHeadVO();
				items = vo.getBodyVO();
				head.setAttributeValue(
								HgPubConst.PRAY_BILL_PURTYPE_FIELDNAME2,
								items[0].getAttributeValue(HgPubConst.PRAY_BILL_PURTYPE_FIELDNAME));

				head.setAttributeValue(
								HgPubConst.PRAY_BILL_PURTYPE_FIELDNAME,
								HgBsPubTool.getDefDoc_PuchType1()
										.get(items[0].getAttributeValue(HgPubConst.PRAY_BILL_PURTYPE_FIELDNAME)));
				for (PraybillItemVO item : items) {
					item.setAttributeValue(
							HgPubConst.PRAY_BILL_PURTYPE_FIELDNAME, null);
				}
			}
			
			// �����к�
			BillRowNoDMO.setVOsRowNoByRule(voaRet,BillTypeConst.PO_PRAY, "crowno");
			//����������
			TranPrayTools.setDefaultAssMeas(voaRet,true);
			//�����빺�ˡ��빺���š��ƻ�Ա
			TranPrayTools.setPsnDpt(voaRet);
			// ����ҵ�����ͼ��빺����
			//TranPrayTools.setBusiAndPrayTypesToHead(voaRet);
      //�繫˾��ĿIDת��
      OrderPubVO.chgDataForPrayCorp(voaRet);
		} catch (Exception e) {
			PubDMO.throwBusinessException(e);
		}
		return voaRet;
	}
	
}
