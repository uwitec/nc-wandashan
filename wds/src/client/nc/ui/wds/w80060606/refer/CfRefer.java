package nc.ui.wds.w80060606.refer;

import java.util.Hashtable;
import nc.ui.bd.ref.AbstractRefGridTreeModel;

public class CfRefer extends AbstractRefGridTreeModel {

	public CfRefer() {
		setRootName("���乫˾��Ϣ"); // ����
		setClassFieldCode(new String[] { "tc_comcode","tc_comname","tc_pk"}); // �ֶ�������
		setFieldName(new String[]{"����","���ƺ�"});
		setClassJoinField("tc_pk"); // Ҫ�ͱ�������ֶ���
		
		setClassTableName("tb_transcompany"); // ���乫˾��
		
		setClassWherePart(" tb_transcompany.dr = 0 ");
		
		setClassDefaultFieldCount(2); // ��ֵ
		
		setFieldCode(new String[] {"tb_carinf.cif_carowner", "tb_carinf.cif_carnum" });
		// ���ò�����ʾ���ֶ�
		setHiddenFieldCode(new String[] {"tb_carinf.cif_pk" });
//		// 38��
		setDocJoinField("tb_carinf.tc_pk"); // i. �趨�����ڵ����ݹ������ֶ�
		setPkFieldCode("tb_carinf.cif_pk"); // �趨����
		// �趨select ��� ��
		setTableName("tb_carinf ");
		// �趨Where����  car left join tb_transcompany comy on car.tc_pk=comy.tc_pk
		setWherePart(" tb_carinf.dr = 0");
	}


}
