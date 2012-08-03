package nc.bs.wds.dm.storebing;

import nc.bs.zmpub.pub.excel.AbstractExcetBO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.ValidationException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.zmpub.excel.CodeToIDInfor;
import nc.vo.zmpub.pub.report.ReportBaseVO;

public class StoreBingExcelBO extends AbstractExcetBO {
	
	public StoreBingExcelBO(){
		super();
		initInfor();
	}
	/**
	 * �Ե���excel��������ݽ��к�̨����
	 * @param vos
	 * @throws BusinessException
	 */
	public void dealSingleImportDatas(CircularlyAccessibleValueObject[] vos) throws BusinessException{
        super.dealSingleImportDatas(vos);
	}

	@Override
	protected void afterSave(AggregatedValueObject[] newBills) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void afterTransData(ReportBaseVO[] rvos,
			AggregatedValueObject[] bills) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void appData(AggregatedValueObject[] newBills)
			throws BusinessException {
		// TODO Auto-generated method stub

	}

	@Override
	protected void beForeTransData(ReportBaseVO[] rvos) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void check(CircularlyAccessibleValueObject[] vos)
			throws ValidationException {
		// TODO Auto-generated method stub

	}

	@Override
	protected void check(AggregatedValueObject[] newBills)
			throws ValidationException {
		// TODO Auto-generated method stub

	}

	@Override
	protected void doSingleSave(CircularlyAccessibleValueObject[] vos)
			throws BusinessException {
		// TODO Auto-generated method stub

	}
	
	private CodeToIDInfor[] headInfor = null;
	
	private CodeToIDInfor[] bodyInfor = null;
	
	private void initInfor(){
		headInfor = new CodeToIDInfor[]{
				new CodeToIDInfor(),
				new CodeToIDInfor(),
				new CodeToIDInfor()
				
		};
//		��˾
		headInfor[0].setCodename("unitcode");
		headInfor[0].setIdname("pk_corp");
		headInfor[0].setTablename("bd_corp");
		headInfor[0].setThiscodename("pk_corp");
		headInfor[0].isCorpField = UFBoolean.TRUE;
		headInfor[0].isBasic = UFBoolean.TRUE;
//		�����ֿ�
		headInfor[1].setCodename("storcode");
		headInfor[1].setIdname("pk_stordoc");
		headInfor[1].setTablename("bd_stordoc");
		headInfor[1].setThiscodename("pk_stordoc");
		headInfor[1].isCorp = UFBoolean.TRUE;
		headInfor[1].isBasic = UFBoolean.TRUE;
		headInfor[1].setCorpname("pk_corp");
//		��������
		headInfor[2].setCodename("areaclcode");
		headInfor[2].setIdname("pk_areacl");
		headInfor[2].setTablename("bd_areacl");
		headInfor[2].setThiscodename("pk_sendareacl");
		headInfor[2].isCorp = UFBoolean.TRUE;
		headInfor[2].isBasic = UFBoolean.TRUE;
		headInfor[2].setCorpname("pk_corp");
		
		
		bodyInfor = new CodeToIDInfor[]{
				new CodeToIDInfor(),
				new CodeToIDInfor(),
				new CodeToIDInfor(),
				new CodeToIDInfor(),
				new CodeToIDInfor()
		};		
		
		
//		��˾
		bodyInfor[0].setCodename("unitcode");//����û�й�˾�ֶ� �������ֶ� ��ʱ��Ź�˾��Ϣ
		bodyInfor[0].setIdname("pk_corp");
		bodyInfor[0].setTablename("bd_corp");
		bodyInfor[0].setThiscodename("pk_custom1");
		bodyInfor[0].isCorpField = UFBoolean.TRUE;
		bodyInfor[0].isBasic = UFBoolean.TRUE;
		bodyInfor[0].isSave = UFBoolean.FALSE;//���ֶβ�����
//		�����ֿ�
		bodyInfor[1].setCodename("storcode");
		bodyInfor[1].setIdname("pk_stordoc");
		bodyInfor[1].setTablename("bd_stordoc");
		bodyInfor[1].setThiscodename("pk_stordoc");
		bodyInfor[1].isCorp = UFBoolean.TRUE;
		bodyInfor[1].isBasic = UFBoolean.TRUE;
		bodyInfor[1].setCorpname("pk_corp");
//		���̱���----�Զ��崦��
		bodyInfor[2].setCodename("custcode");
		bodyInfor[2].setIdname("pk_cumandoc");
		bodyInfor[2].setTablename("bd_cumandoc");
		bodyInfor[2].setThiscodename("pk_cumandoc");
		bodyInfor[2].isCorp = UFBoolean.TRUE;
		bodyInfor[2].isBasic = UFBoolean.TRUE;
		bodyInfor[2].isDefTran = UFBoolean.TRUE;
		bodyInfor[2].isCache = UFBoolean.FALSE;
		bodyInfor[2].setDefTranClassName("nc.bs.wds.dm.storebing.StoreBingCustTranBO");
		bodyInfor[2].setCorpname("pk_corp");
//		�ֱֲ���
		bodyInfor[3].setCodename("storcode");
		bodyInfor[3].setIdname("pk_stordoc");
		bodyInfor[3].setTablename("bd_stordoc");
		bodyInfor[3].setThiscodename("pk_stordoc1");
		bodyInfor[3].isCorp = UFBoolean.TRUE;
		bodyInfor[3].isBasic = UFBoolean.TRUE;
		bodyInfor[3].setCorpname("pk_corp");
//		���ڵ���
		bodyInfor[4].setCodename("areaclcode");
		bodyInfor[4].setIdname("pk_areacl");
		bodyInfor[4].setTablename("bd_areacl");
		bodyInfor[4].setThiscodename("custareaid");
		bodyInfor[4].isCorp = UFBoolean.TRUE;
		bodyInfor[4].isBasic = UFBoolean.TRUE;
		bodyInfor[4].setCorpname("pk_corp");		
	}

	@Override
	protected CodeToIDInfor[] getBodyTransFieldInfor() {
		// TODO Auto-generated method stub
		return bodyInfor;
	}

	@Override
	protected CodeToIDInfor[] getHeadTransFieldInfor() {
		// TODO Auto-generated method stub
		return headInfor;
	}

	@Override
	protected String getPushSaveActionName() throws BusinessException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected boolean isPushSaveBatch() {
		// TODO Auto-generated method stub
		return false;
	}
	
	protected void pushBillSave(AggregatedValueObject[] newBills,String billtype) throws Exception{
//		��ͷ���������������   
		
//		��ͷ�����ͬά�ȵĴ���  ��Ϊ  ����   ������У��
		
		
//		if(newBills == null || newBills.length == 0)
//			return;
//		HYPubBO bo = new HYPubBO();
//		bo.saveBDs(newBills, null);
	}

	@Override
	public void dealBillImportDatas(ReportBaseVO[] rvos, String billtype,
			String tmpSourBilltype) throws Exception {
		// TODO Auto-generated method stub
		super.dealBillImportDatas(rvos, billtype, tmpSourBilltype);
	}
	@Override
	protected String getBillBodyCorpFieldName() {
		// TODO Auto-generated method stub
		return "pk_custom1";
	}
}
