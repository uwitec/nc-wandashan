package nc.ui.zb.price.local;

import nc.ui.zb.price.pub.AbstractPriceDataBuffer;

public class LocalPriceDataBuffer extends AbstractPriceDataBuffer{
	
	private String ccirclenoid = null;//��ǰ���۴���    �б�����Ϊ �ֳ����۵� ��� ���һ�� ���۴����ӱ�
//	ͬ �б�����Ϊ�����б�� �ִ��ӱ�   �����ֳ�����������ɱ����ִ�����   ���ֶ�Ϊ�ϴα��۵��ִ�id
	public void setCcirclenoid(String ccirclenoid) {
		this.ccirclenoid = ccirclenoid;
	}

	@Override
	public String getCurrentCircalID() {
		// TODO Auto-generated method stub
		return ccirclenoid;
	}	
}
