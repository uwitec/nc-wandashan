package nc.ui.wds.ic.other.in;
import java.io.Serializable;
import nc.vo.trade.pub.IBDGetCheckClass2;
/**
 * @author mlr
 * �ö���Ϊ ui ��getUserObject�������صĶ��� 
 * ���ڼ�¼ǰ��̨У�����·��,����ǰ��̨У�����ʵ����
 * �������ŵ�public ����
 */
public class GetCheck implements Serializable, IBDGetCheckClass2 {
	private static final long serialVersionUID = 428965024363387549L;
	/**
	 * ��̨У����·��
	 */
	public String getCheckClass() {
		return "nc.bs.wds.ic.other.in.BSCheck";
	}
	/**
	 * ǰ̨У����·��
	 */
	public String getUICheckClass() {		
		return "nc.ui.wds.ic.other.in.UICheck";
	}
}