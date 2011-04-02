package nc.itf.wds.w80060604;

import java.util.List;

import nc.vo.dm.confirm.TbFydmxnewVO;
import nc.vo.dm.confirm.TbFydnewVO;
import nc.vo.wds.w80060604.SoSaleVO;

public interface Iw80060604 {

	/**
	 * ����ǰ̨�����ļ��Ͻ��в��뷢�˵��ͻ�д���������еĴ�ӡ�����ʹ�ӡʱ��
	 * 
	 * @param fydList
	 *            ���˵�������
	 * @param fydmxList
	 *            ���˵��ӱ���
	 * @param saletempList
	 *            ����������
	 * @throws Exception
	 */
	public void insertFyd(List<TbFydnewVO> fydList,
			List<TbFydmxnewVO[]> fydmxList, List<SoSaleVO> saletempList)
			throws Exception;

	/**
	 * ���²������
	 * 
	 * @param list
	 *            ��Ҫ���µļ���
	 * @throws Exception
	 */
	public void updateSosale(List list) throws Exception;

}
