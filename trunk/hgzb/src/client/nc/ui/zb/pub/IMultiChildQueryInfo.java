package nc.ui.zb.pub;

/**
 * <p>���ӱ��ѯ�����ӱ������ѯ�������Ϣ��
 * @author twh
 * @date 2007-1-22 ����10:04:41
 * @version V5.0
 * @��Ҫ����ʹ�ã�
 *  <ul>
 * 		<li><b>���ʹ�ø��ࣺ</b></li>
 *      <li><b>�Ƿ��̰߳�ȫ��</b></li>
 * 		<li><b>������Ҫ��</b></li>
 * 		<li><b>ʹ��Լ����</b></li>
 * 		<li><b>������</b></li>
 * </ul>
 * </p>
 * <p>
 * @��֪��BUG��
 * 	<ul>
 * 		<li></li>
 *  </ul>
 * </p>
 * <p>
 * @�޸���ʷ��
 */
public interface IMultiChildQueryInfo {

	/**
	 * ���ݣ�����ģ���Ӧ���ӱ���뷵�ز�ѯģ���ж�Ӧ
	 * @param tblCode
	 * @return
	 * twh (2007-1-22 ����10:27:37)<br>
	 */
	public abstract String getAliasByTableCode(String tblCode);
}
