package nc.ui.wds.dm.corpseal;

import java.awt.Container;

import nc.ui.trade.check.BeforeActionCHK;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.wds.dm.corpseal.CorpsealVO;
/**
 * �ͻ���˾ͼ�� ǰ̨У����
 * @author Administrator
 *xjx  add
 */
public class BeforeActionCheck extends BeforeActionCHK {

	public void runBatchClass(Container parent, String billType,
			String actionName, AggregatedValueObject[] vos, Object[] obj)
			throws Exception {
		// TODO Auto-generated method stub

	}

	public void runClass(Container parent, String billType, String actionName,
			AggregatedValueObject vo, Object obj) throws Exception {

		//У����巢����������ǰ���������ԭ���ܴ��ڽ���
		if (vo == null) {
			return;
		}
		//��ͷ����У��
		CorpsealVO head=(CorpsealVO) vo.getParentVO();
		String corpseal=PuPubVO.getString_TrimZeroLenAsNull(head.getPk_cubasdoc());
		if(corpseal==null){
			throw new BusinessException("�ͻ���˾ͼ�²���Ϊ��");
		}
	}
}
