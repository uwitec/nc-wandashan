package nc.ui.wds.w80040602.refer;

import java.util.Hashtable;
import nc.ui.bd.ref.AbstractRefGridTreeModel ;

public class STRefer extends AbstractRefGridTreeModel  {

	public STRefer() {
		setRefTitle("�ֿ��λ��Ϣ");
		setRootName("�ֿ���Ϣ"); // ����
		setClassFieldCode(new String[] { "storcode","storname","pk_stordoc"}); // �ֶ�������
		
		setClassJoinField("pk_stordoc"); // Ҫ�ͱ�������ֶ���
		
		setClassTableName("bd_stordoc"); // ���乫˾��
		
		setClassWherePart(" bd_stordoc.pk_stordoc in ('1021A91000000004YZ0P','1021A91000000004UXCH'," +
				"'1021A91000000004UWVU','1021A91000000004UXCM','1021A91000000004UXCN','1021A91000000004UWVO'," +
				"'1021A91000000004UWVT') and bd_stordoc.dr=0 ");
		
		setClassDefaultFieldCount(2); // ��ֵ
		setFieldName(new String[]{"��λ����","��λ����"});
		setFieldCode(new String[] {"bd_cargdoc.cscode", "bd_cargdoc.csname" });
		// ���ò�����ʾ���ֶ�
		setHiddenFieldCode(new String[] {"bd_cargdoc.pk_cargdoc" });
//		// 38��
		setDocJoinField("bd_cargdoc.pk_stordoc"); // i. �趨�����ڵ����ݹ������ֶ�
		setPkFieldCode("bd_cargdoc.pk_cargdoc"); // �趨����
		// �趨select ��� ��
		setTableName("bd_cargdoc ");
		// �趨Where����  car left join tb_transcompany comy on car.tc_pk=comy.tc_pk
		setWherePart(" bd_cargdoc.dr = 0");
	}

	
}
