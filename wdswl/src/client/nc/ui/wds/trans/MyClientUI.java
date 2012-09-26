package nc.ui.wds.trans;
import nc.ui.trade.manage.ManageEventHandler;
import nc.vo.pub.CircularlyAccessibleValueObject;
/**
 * ��������˷Ѽ���  ������Ϣά�� 
 * 
 * ά����Ϣ  ������˾  ����ֿ�   ��Ʒ���� 
 *  
 * �˷Ѽ���ʱ  �� ������ά��  ����ѯ�Ҹ��˼۱�   �����˷�
 *  
 * @author mlr
 *
 */
 public class MyClientUI extends AbstractMyClientUI{      
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected ManageEventHandler createEventHandler() {
		return new MyEventHandler(this, getUIControl());
	}
       
	public void setBodySpecialData(CircularlyAccessibleValueObject[] vos)
			throws Exception {}

	protected void setHeadSpecialData(CircularlyAccessibleValueObject vo,
			int intRow) throws Exception {}

	protected void setTotalHeadSpecialData(CircularlyAccessibleValueObject[] vos)
			throws Exception {	}

	protected void initSelfData() {	}

	public void setDefaultData() throws Exception {
	   getBillCardWrapper().getBillCardPanel().getHeadItem("pk_corp").setValue(getCorpPrimaryKey());
	}
}
