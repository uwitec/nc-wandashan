package nc.itf.wds.w8004040204;

import java.util.List;
import nc.vo.ic.other.out.MyBillVO;
import nc.vo.ic.other.out.TbOutgeneralBVO;
import nc.vo.ic.other.out.TbOutgeneralTVO;
import nc.vo.ic.pub.StockInvOnHandVO;
import nc.vo.pub.AggregatedValueObject;

public interface Iw8004040204 {
	

	/**
	 * ���۳����ERP�еĵ��ݶ�������
	 */
	public Object whs_processAction(String actionName,String actionName2, String billType, String currentDate,
			AggregatedValueObject vo,Object outgeneralVo) throws Exception;

	
	/**
	 * �Զ�ȡ��
	 * 
	 * @param pk_user ��ǰ��¼������
	 * 
	 * @param pk_stordoc
	 *            �ֿ�����
	 * 
	 * @param pk
	 *            ��Ʒ����
	 *            
	 * @param queryVar 
	 * 				�����������ֶ�
	 * @throws Exception
	 */
	public java.util.Map<String,List<TbOutgeneralTVO>> autoPickAction(String pk_user ,TbOutgeneralBVO[] generalb, String pk_stordoc)
			throws Exception;

	public void insertGeneralTVO(List<TbOutgeneralTVO> itemList,
			List<TbOutgeneralTVO> itemList2) throws Exception;

	public void deleteGeneralTVO(List<TbOutgeneralTVO> itemList)
			throws Exception;

	public void saveGeneralVO(MyBillVO myBillVO) throws Exception;

	public void insertWarehousestock(StockInvOnHandVO item) throws Exception;

	public void updateWarehousestock(StockInvOnHandVO item) throws Exception;

	public void queryWarehousestock(List itemList) throws Exception;

	public List queryGeneralTVO(String cinventoryid, String cfirstbillhid,
			String cfirstbillbid) throws Exception;

	public void updateBdcargdocTray(String trayPK) throws Exception;

	public AggregatedValueObject saveBD8004040204(AggregatedValueObject billVO,
			Object userObj) throws Exception;
	
	public AggregatedValueObject saveBD8004040602(AggregatedValueObject billVO,
			Object userObj) throws Exception;

	public AggregatedValueObject deleteBD8004040204(
			AggregatedValueObject billVO, Object userObj) throws Exception;

	/**
	 * ��ѯ������е�ʵ���������͸�����
	 * 
	 * @param pk_cfirst
	 *            ��Դ�����ӱ�����
	 * @param pk_invbasdoc
	 *            ��Ʒ����
	 * @return Object���� 0������ 1������
	 * @throws Exception
	 */
	public Object[] getNoutNum(String pk_cfirst, String pk_invbasdoc)
			throws Exception;
}
