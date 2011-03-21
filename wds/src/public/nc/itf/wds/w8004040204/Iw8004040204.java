package nc.itf.wds.w8004040204;

import java.util.List;

import nc.ui.trade.base.AbstractBillUI;
import nc.ui.trade.businessaction.IBusinessController;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.ui.wds.w8004040204.MyClientUI;
import nc.ui.wds.w8004040204.MyEventHandler;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.pf.PfUtilWorkFlowVO;
import nc.vo.wds.w8004040204.MyBillVO;
import nc.vo.wds.w8004040204.TbOutgeneralBVO;
import nc.vo.wds.w8004040204.TbOutgeneralTVO;
import nc.vo.wds.w8004040204.TbWarehousestockVO;

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
	public String autoPickAction(String pk_user ,TbOutgeneralBVO[] generalb, String pk_stordoc,String queryVar)
			throws Exception;

	public void insertGeneralTVO(List<TbOutgeneralTVO> itemList,
			List<TbOutgeneralTVO> itemList2) throws Exception;

	public void deleteGeneralTVO(List<TbOutgeneralTVO> itemList)
			throws Exception;

	public void saveGeneralVO(MyBillVO myBillVO) throws Exception;

	public void insertWarehousestock(TbWarehousestockVO item) throws Exception;

	public void updateWarehousestock(TbWarehousestockVO item) throws Exception;

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
