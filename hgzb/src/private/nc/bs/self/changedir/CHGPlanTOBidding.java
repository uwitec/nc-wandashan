package nc.bs.self.changedir;

import nc.bs.pf.change.VOConversion;

/**
 * �ɹ��ƻ����ɱ����
 * @author Administrator
 *
 */

public class CHGPlanTOBidding extends VOConversion {

	public String[] getField() {
		return new String[]{
//				private String crowno;//�к�
//				"H_cunitid->H_"//������λ
//				,"H_ naverageprice->H_"//��ʷƽ����
				"H_cinvbasid->H_cbaseid"//�������ID
//				,"H_ nmarkprice->H_"//��׼�
//				,"H_cupsourcebillrowid->H_"//
				
//				,"H_cinvclid->H_"//�������ID
//				,"H_ unitweight->H_"//����
//				,"H_ nplanprice->H_"//�ƻ���
//				,"H_ nmarketprice->H_"//�м�
//				,"H_cbiddingbid->H_"//�ӱ�����
				,"H_cinvmanid->H_cmangid"//�������ID
//				,"H_cupsourcebilltype->"
				,"H_cupsourcebillid->H_cpraybillid"
				,"H_cupsourcebillrowid->H_cpraybill_bid"//
				,"H_csourcetype->H_csourcebilltype"
				,"H_csourcebillhid->H_csourcebillid"
				,"H_csourcebillbid->H_csourcebillrowid"
//				,""
		};
	}
	public String[] getFormulas() {
		return new String[]{
				"H_cupsourcebilltype->\"20\"" 
				,"H_nzbnum->H_npraynum"//�б�����  �����Ѿ���  npraynum - �ۼƶ�����
				,"H_cunitid->getColValue(bd_invbasdoc,pk_measdoc,pk_invbasdoc,H_cbaseid)"
				,"H_unitweight->getColValue(bd_invbasdoc,unitweight,pk_invbasdoc,H_cbaseid)"
				,"H_nplanprice->getColValue(bd_invmandoc,planprice,pk_invmandoc,H_cmangid)"
				,"H_invclcode->getColValue(bd_invbasdoc,invcode,pk_invbasdoc,H_cbaseid)"
		};
	}
}
