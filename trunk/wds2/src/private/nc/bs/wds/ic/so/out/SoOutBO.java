package nc.bs.wds.ic.so.out;

import java.util.ArrayList;
import java.util.List;

import nc.bs.trade.business.HYPubBO;
import nc.bs.trade.business.IBDBusiCheck;
import nc.ui.pub.ClientEnvironment;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.trade.pub.HYBillVO;
import nc.vo.wds.w8004040204.TbOutgeneralBVO;
import nc.vo.wds.w8004040204.TbOutgeneralHVO;
import nc.vo.wds.w80060406.TbFydmxnewVO;
import nc.vo.wds.w80060406.TbFydnewVO;
/**
 * 
 * @author Administrator
 * ���ɽ��Ŀ--���۳���(WDS8)
 */
public class SoOutBO  implements IBDBusiCheck{

	public void check(int arg0, AggregatedValueObject arg1, Object arg2)
			throws Exception {
		
		
	}

	public void dealAfter(int arg0, AggregatedValueObject arg1, Object arg2)
			throws Exception {
		if(arg1 == null)
			return;
		insertFyd(arg1);
		
	}
	
	/**
	 * ���ɷ��˵�
	 * 
	 * @throws Exception
	 */
	private void insertFyd(AggregatedValueObject arg1) throws Exception {
		TbOutgeneralHVO generalh =(TbOutgeneralHVO) arg1.getParentVO();
		TbOutgeneralBVO[] generalb = (TbOutgeneralBVO[])arg1.getChildrenVO();
		Object[] o = new Object[3];
		o[0] = false;
		TbFydnewVO fydvo = new TbFydnewVO();
		List<TbFydmxnewVO[]> fydmxList = new ArrayList<TbFydmxnewVO[]>();

		// ����VOת��/////////////////////////////////////////////

		// ------------ת����ͷ����-----------------//
		
		if (null != generalh && null != generalb && generalb.length > 0) {
			if (null != generalh.getVdiliveraddress()
					&& !"".equals(generalh.getVdiliveraddress())) {
				fydvo.setFyd_shdz(generalh.getVdiliveraddress()); // �ջ���ַ
			}
			if (null != generalh.getVnote() && !"".equals(generalh.getVnote())) {
				fydvo.setFyd_bz(generalh.getVnote()); // ��ע
			}
			if (null != generalh.getCdptid()
					&& !"".equals(generalh.getCdptid())) {
				fydvo.setCdeptid(generalh.getCdptid()); // ����
			}
			// �����˻���ʽ
			fydvo.setFyd_yhfs("����");
			// �������� 0 �����Ƶ� 1 ���۶��� 2 �ֳ�ֱ�� 3��ֶ���4 �ϲ����� 8 �������Ƶ������ɵ��˵�
			fydvo.setBilltype(8);
			fydvo.setVbillstatus(1);
			// ���ݺ�
			fydvo.setVbillno(generalh.getVbillcode());
			// �Ƶ�����
			fydvo.setDmakedate(generalh.getDbilldate());
			fydvo.setVoperatorid(ClientEnvironment.getInstance().getUser()
					.getPrimaryKey()); // �����Ƶ���
			// ���÷���վ
			fydvo.setSrl_pk(generalh.getSrl_pk());
			// ����վ
			fydvo.setSrl_pkr(generalh.getSrl_pkr());
			// --------------ת����ͷ����---------------//
			// --------------ת������----------------//
			List<TbFydmxnewVO> tbfydmxList = new ArrayList<TbFydmxnewVO>();
			for (int j = 0; j < generalb.length; j++) {
				TbFydmxnewVO fydmxnewvo = new TbFydmxnewVO();
				TbOutgeneralBVO genb = generalb[j];
				if (null != genb.getCinventoryid()
						&& !"".equals(genb.getCinventoryid())) {
					fydmxnewvo.setPk_invbasdoc(genb.getCinventoryid()); // ��Ʒ����
				}
				if (null != genb.getNshouldoutnum()
						&& !"".equals(genb.getNshouldoutnum())) {
					fydmxnewvo.setCfd_yfsl(genb.getNshouldoutnum()); // Ӧ������
				}
				if (null != genb.getNshouldoutassistnum()
						&& !"".equals(genb.getNshouldoutassistnum())) {
					fydmxnewvo.setCfd_xs(genb.getNshouldoutassistnum()); // ����
				}
				if (null != genb.getNoutnum() && !"".equals(genb.getNoutnum())) {
					fydmxnewvo.setCfd_sfsl(genb.getNoutnum()); // ʵ������
				}
				if (null != genb.getNoutassistnum()
						&& !"".equals(genb.getNoutassistnum())) {
					fydmxnewvo.setCfd_sffsl(genb.getNoutassistnum()); // ʵ��������
				}
				if (null != genb.getCrowno() && !"".equals(genb.getCrowno())) {
					fydmxnewvo.setCrowno(genb.getCrowno()); // �к�
				}
				if (null != genb.getUnitid() && !"".equals(genb.getUnitid())) {
					fydmxnewvo.setCfd_dw(genb.getUnitid()); // ��λ
				}
				fydmxnewvo.setCfd_pc(genb.getVbatchcode()); // ����				
				tbfydmxList.add(fydmxnewvo);
			}
			// ----------------ת���������---------------------//
				TbFydmxnewVO[] fydmxVO = new TbFydmxnewVO[tbfydmxList.size()];
				tbfydmxList.toArray(fydmxVO);
				fydmxList.add(fydmxVO);
				o[0] = true;
		HYBillVO billVo = new HYBillVO();
		billVo.setParentVO(fydvo);
		billVo.setChildrenVO(fydmxVO);
		new HYPubBO().saveBD(billVo, null);
	}
	}
}