package nc.ui.zb.pub;

/**
 * <p>���ӱ����VO��Ϣ��
 * <p>���ö��ӱ�����йص���Ϣ����VO���ơ���������룬�Լ�����-VO�������յȡ�
 * @version V5.0
 * @�޸���ʷ��
 */
public interface IMultiChildVOInfo extends IMultiChild {
	/**
	 * ���ؾۺ�VO,����͸����ӱ��vo������
	 */
	public abstract String[] getVONames();

	/**
	 * �����ӱ�����Ӧ��VO������
	 * @param tblCode
	 * @return
	 * twh (2007-1-22 ����09:52:57)<br>
	 */
	public abstract String getVoClassNameByTableCode(String tblCode);
}
