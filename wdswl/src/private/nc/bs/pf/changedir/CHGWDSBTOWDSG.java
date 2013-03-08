package nc.bs.pf.changedir;
import nc.vo.pf.change.UserDefineFunction;
import nc.vo.pub.lang.UFDate;
import nc.vo.wl.pub.WdsWlPubConst;
/**
 * ���ڵ���  ����ʱ ����  ��������->�����˵����ݽ��� *
 * �������ڣ�(2004-11-18)
 * @author��ƽ̨�ű�����
 */
public class CHGWDSBTOWDSG extends nc.bs.pf.change.VOConversion {
/**
 * CHG20TO21 ������ע�⡣
 */
public CHGWDSBTOWDSG() {
	super();
}
/**
* ��ú������ȫ¼�����ơ�
* @return java.lang.String[]
*/
public String getAfterClassName() {
	return "nc.bs.wds2.conversion.AfterWDSBTOWDSG";
}
/**
* �����һ���������ȫ¼�����ơ�
* @return java.lang.String[]
*/
public String getOtherClassName() {
	return null;
}
public String[] getField() {
	return new String[] {		
			"H_pk_outwhouse->B_coutwhid",//�����ֿ� --->�����ֿ�
			"H_vdef9->H_ctakeoutspaceid",//������λ			
			"H_vmemo->H_vnote",//��ע
			"H_vdef3->B_coutcorpid",//������˾
			"H_vdef2->B_coutcbid",//���������֯			
			"H_vdef1->B_cincbid",//��������֯
			"H_vdef5->B_cincorpid", //���빫˾			
			"H_vdef7->B_cinwhid",//����ֿ�
    //		"H_vdef6->B_fallocflag",//�������ͱ�־	
    //		"H_vdef4->B_coutdeptid",//��������
	//		"H_vdef8->B_coutpsnid",//��������ҵ��Ա
	//	    "H_reserve14->B_bretractflag",//�Ƿ��˻�			
			"B_csourcebillhid->B_cbillid",
			"B_csourcebillbid->B_cbill_bid",
			"B_vsourcebillcode->B_vcode",
			"B_csourcetype->B_ctypecode",				
			"B_cfirstbillhid->B_cbillid",
			"B_cfirstbillbid->B_cbill_bid",
			"B_vfirstbillcode->H_vcode",	
			"B_cfirsttype->B_ctypecode",			
			"B_pk_invmandoc->B_ctakeoutinvid",//���������id
			"B_pk_invbasdoc->B_cinvbasid",//�����������id
			"B_vdef1->B_vdef1",//���״̬	
			"B_vdef2->B_vbatch",//����	
			"B_bisdate->B_flargess",//����Ʒ		
			"B_nhsl->B_nchangeratel",//������
			"B_assunit->B_castunitid",//��������λ
			"B_nassplannum->B_nassistnum",//��������������
			"B_nplannum->B_nnum",//������������
			"B_ndealnum->B_num",//���ΰ�������
			"B_nassdealnum->B_nassnum",//���ΰ��Ÿ�����															
			"B_vdef9->B_cinwhid",//����ֿ�
			"B_vdef7->B_cincbid",//��������֯
			"B_vdef5->B_cincorpid", //���빫˾	   		
			"B_vdef3->B_creceieveid",//�ջ���λ			
			"B_reserve1->B_vreceiveaddress",//�ջ���ַ
	//		"B_vdef10->B_pk_sendtype",//���ͷ�ʽ			
	//		"B_vdef6->B_fallocflag",//�������ͱ�־		
    //		"B_vdef4->B_coutdeptid",//��������		
	//		"B_vdef8->B_coutpsnid",//��������ҵ��Ա
	//	    "B_reserve14->B_bretractflag",//�Ƿ��˻�	
	//		"B_reserve2->B_cprojectphase",//��Ŀ�׶�
	//		"B_reserve3->B_cprojectid",//��Ŀ����
	//		"B_reserve4->B_cvendorid",//��Ӧ��
	//      "B_reserve5->B_ctypecode",//��������	
		};
}
/**
* ��ù�ʽ��
* @return java.lang.String[]
*/
public String[] getFormulas() {
	if(m_strDate == null){
		new UFDate(System.currentTimeMillis());
		super.setSysDate(new UFDate(System.currentTimeMillis()).toString());
	}
	return new String[] {
			"H_pk_corp->\""+m_strCorp+"\"",
			"H_voperatorid->\""+m_strOperator+"\"",
			"H_pk_billtype->\""+WdsWlPubConst.WDSG+"\"",
			"H_vbillstatus->int(8)",
		    "H_fisbigglour->\"N\"",
		    "H_dmakedate->\""+m_strDate+"\"",
		    "H_dbilldate->\""+m_strDate+"\"",
		    "H_dbegindate->\""+m_strDate+"\"",
		    "H_itransstatus->\""+0+"\""
	};
}
/**
* �����û��Զ��庯����
*/
public UserDefineFunction[] getUserDefineFunction() {
	return null;
}
}
