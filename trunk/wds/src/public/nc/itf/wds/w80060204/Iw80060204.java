package nc.itf.wds.w80060204;

import nc.vo.pub.AggregatedValueObject;
import nc.vo.wds.w8004040204.TbOutgeneralBVO;

public interface Iw80060204 {

	/**
	 * �ϲ������ı��淽�� ���ݴ�����BillVO���к�̨���� ���ȱ������һ���ϲ���ĵ��� ����ǲ𿪺�ĵ��� ����Ǹ����������л�д״̬
	 * 
	 * @param billVO
	 *            �ۺ�VO
	 * @throws Exception
	 */
	public void saveFyd(AggregatedValueObject billVO) throws Exception;

	
}
