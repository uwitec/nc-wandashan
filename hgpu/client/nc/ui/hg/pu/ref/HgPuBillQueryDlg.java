package nc.ui.hg.pu.ref;

import nc.ui.pub.pf.IinitQueryData;
import nc.ui.pub.query.QueryConditionClient;

/**
 * ���ղ�ѯ����
 * @author Administrator
 *
 */
public abstract class HgPuBillQueryDlg extends QueryConditionClient  implements IinitQueryData{
	
	 public HgPuBillQueryDlg(java.awt.Container parent) {
	        super(parent);
//	        hideNormal();
	 }

	@Override
	public String getWhereSQL() {
		return super.getWhereSQL();
	}
	
}
