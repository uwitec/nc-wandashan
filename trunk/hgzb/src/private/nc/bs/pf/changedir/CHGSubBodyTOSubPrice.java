package nc.bs.pf.changedir;

import nc.vo.pf.change.UserDefineFunction;

public class CHGSubBodyTOSubPrice extends nc.bs.pf.change.VOConversion {
	/**
     * ���۵������ɱ�����ϸ zhf
     */
    public CHGSubBodyTOSubPrice() {
        super();
    }

    /**
     * ��ú������ȫ¼�����ơ�
     * 
     * @return java.lang.String[]
     */
    public String getAfterClassName() {
//        return "nc.bs.zb.conversion.Zb01toZb02AfterChange";
    	return null;
    }

    /**
     * �����һ���������ȫ¼�����ơ�
     * 
     * @return java.lang.String[]
     */
    public String getOtherClassName() {
        return null;
    }

    /**
     * ����ֶζ�Ӧ��
     * 
     * @return java.lang.String[]
     */
    public String[] getField() {
    	return new String[] {  
//    			"H_csubmitpriceid->"//id
//    			,"H_cbiddingid->"//����id
    			"H_cinvclid->H_cinvclid"//Ʒ�ַ���
//    			,"H_pk_corp->"//������˾
    			,"H_cinvbasid->H_cinvbasid"//Ʒ�ֻ���id
    			,"H_cinvmanid->H_cinvmanid"//Ʒ�ֹ���id
    			,"H_cunitid->H_cunitid"//��������λ
    			,"H_castunitid->H_castunitid"//��������λ
    			,"H_nnum->H_nnum"//�б�������
    			,"H_nasnum->H_nasnum"//�б긨����
    			,"H_vdef1->H_csubmitbill_bid"
    			,"H_nprice->H_nprice"
//    			,"H_cvendorid->"//��Ӧ�̹���id
//    			,"H_ccircalnoid->"//�ִν׶�id  Ĭ�ϴ�������һ�Ρ��ڶ��Ρ�������
    			
    	};
    }

    /**
     * ��ù�ʽ��
     * @return java.lang.String[]
     */
    public String[] getFormulas() {
    	return null;
    }

    /**
     * �����û��Զ��庯����
     */
    public UserDefineFunction[] getUserDefineFunction() {
        return null;
    }
}
