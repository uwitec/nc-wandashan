package nc.ui.self.changedir;

import nc.ui.pf.change.VOConversionUI;

/**
 * ���ⵥ����ת��Ϊ�˷�����
 * @author Administrator
 *
 */

public class CHGBiBodyTOPriceBody extends VOConversionUI {

	public String[] getField() {
		return new String[]{
				 "H_cinvclid->H_cinvclid"//Ʒ�ַ���
				,"H_cinvbasid->H_cinvbasid"//Ʒ�ֻ���id
				,"H_cinvmanid->H_cinvmanid"//Ʒ�ֹ���id
				,"H_cunitid->H_cunitid"//��������λ
//				,"H_castunitid->H_"//��������λ
				,"H_nnum->H_nzbnum"//�б�������
//				,"H_nasnum= null;//�б긨����	
				,"H_csourcebillhid->H_cbiddingid"//��ԴID
				,"H_csourcebillbid->H_cbiddingbid"//��Դ��ID
				//,"H_csourcetype->\"ZB01\""//��Դ����
		};
	}
	public String[] getFormulas() {
		return new String[]{"H_csourcetype->\"ZB01\""};
	}
}
