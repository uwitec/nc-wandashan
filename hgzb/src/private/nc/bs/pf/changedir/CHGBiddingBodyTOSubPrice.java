package nc.bs.pf.changedir;

import nc.vo.pf.change.UserDefineFunction;

public class CHGBiddingBodyTOSubPrice extends nc.bs.pf.change.VOConversion {
	/**
     * �����������  ������ϸ zhf
     */
    public CHGBiddingBodyTOSubPrice() {
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
    			
//    			private String csubmitpriceid->"//id
    			"H_cbiddingid->H_cbiddingid"//����id
    			,"H_cinvclid->H_cinvclid"//Ʒ�ַ���
//    			,"H_pk_corp->"//������˾
    			,"H_cinvbasid->H_cinvbasid"//Ʒ�ֻ���id
    			,"H_cinvmanid->H_cinvmanid"//Ʒ�ֹ���id
    			,"H_cunitid->H_cunitid"//��������λ
//    			,"H_castunitid->H_castunitid"//��������λ
    			,"H_nnum->H_nzbnum"//�б�����->"
//    			,"H_nasnum->"//�б긨����
//    			,"H_cvendorid->"//��Ӧ�̹���id
//    			,"H_ccircalnoid->"//�ִν׶�id  Ĭ�ϴ�������һ�Ρ��ڶ��Ρ�������
    			
//    			private Integer isubmittype->"//�������� 0web 1local 2�ֹ�¼�� 3���ⱨ�ۣ�ֻ�������б��ж��ⱨ�ۣ�
//    			,"H_nprice->"//����
//    			,"H_nlastprice->"//���ֱ���
//    			,"H_nllowerprice->"//������ͱ���
    			
    			,"H_nmarkprice->H_nmarkprice"//��׼�  zhf 
    			
//    			zhf add  �ֳ����۵�����  ����׷��  ����Ҫ���뱨����ϸ����
    			,"H_nplanprice->H_nplanprice"//�ƻ���
    			,"H_nmarketprice->H_nmarketprice"//�м�
    			,"H_naverageprice->H_naverageprice"//��ʷƽ����
    			
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
