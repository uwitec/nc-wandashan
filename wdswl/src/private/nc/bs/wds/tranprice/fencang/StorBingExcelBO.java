package nc.bs.wds.tranprice.fencang;

import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.trade.business.HYPubBO;
import nc.bs.zmpub.pub.excel.AbstractExcetBO;
import nc.jdbc.framework.util.SQLHelper;
import nc.vo.ic.pub.monthsum.SqlHelper;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.SuperVO;
import nc.vo.pub.VOStatus;
import nc.vo.pub.ValidationException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.trade.pub.IBillStatus;
import nc.vo.wds.load.account.LoadpriceB1VO;
import nc.vo.wl.pub.WdsWlPubConst;
import nc.vo.zmpub.excel.CodeToIDInfor;
import nc.vo.zmpub.pub.report.ReportBaseVO;

public class StorBingExcelBO extends AbstractExcetBO {
	
	public StorBingExcelBO(){
		super();
		initInfor();
	}
	/**
	 * 对单体excel导入的数据进行后台处理
	 * @param vos
	 * @throws BusinessException
	 */
	public void dealSingleImportDatas(CircularlyAccessibleValueObject[] vos) throws BusinessException{
        super.dealSingleImportDatas(vos);
	}

	@Override
	protected void afterSave(AggregatedValueObject[] newBills) {

	}

	@Override
	protected void afterTransData(ReportBaseVO[] rvos,
			AggregatedValueObject[] bills) {

	}

	@Override
	protected void appData(AggregatedValueObject[] newBills)
			throws BusinessException {

	}

	@Override
	protected void beForeTransData(ReportBaseVO[] rvos) {

	}

	@Override
	protected void check(CircularlyAccessibleValueObject[] vos)
			throws ValidationException {

	}

	@Override
	protected void check(AggregatedValueObject[] newBills)
			throws ValidationException {

	}

	@Override
	protected void doSingleSave(CircularlyAccessibleValueObject[] vos)
			throws BusinessException {

	}
	
	private CodeToIDInfor[] headInfor = null;
	
	private CodeToIDInfor[] bodyInfor = null;
	
	private void initInfor(){
		headInfor = new CodeToIDInfor[]{
				new CodeToIDInfor(),
				new CodeToIDInfor(),
				new CodeToIDInfor(),
				
		};
//		公司
		headInfor[0].setCodename("unitcode");
		headInfor[0].setIdname("pk_corp");
		headInfor[0].setTablename("bd_corp");
		headInfor[0].setThiscodename("pk_corp");
		headInfor[0].isCorpField = UFBoolean.TRUE;
		headInfor[0].isBasic = UFBoolean.TRUE;
//		发货仓库
		headInfor[1].setCodename("storcode");
		headInfor[1].setIdname("pk_stordoc");
		headInfor[1].setTablename("bd_stordoc");
		headInfor[1].setThiscodename("reserve1");
		headInfor[1].isCorp = UFBoolean.TRUE;
		headInfor[1].isBasic = UFBoolean.TRUE;
		headInfor[1].setCorpname("pk_corp");
//		承运商
		headInfor[2].setCodename("ctranscorpcode");
		headInfor[2].setIdname("pk_wds_tanscorp_h");
		headInfor[2].setTablename("wds_tanscorp_h");
		headInfor[2].setThiscodename("carriersid");
		headInfor[2].isCorp = UFBoolean.TRUE;
		headInfor[2].isBasic = UFBoolean.FALSE;
		headInfor[2].setCorpname("pk_corp");
		
		
		bodyInfor = new CodeToIDInfor[]{
				new CodeToIDInfor(),
				new CodeToIDInfor(),
			};		
		
		
//		公司
		bodyInfor[0].setCodename("unitcode");//表体没有公司字段 用无用字段 临时存放公司信息
		bodyInfor[0].setIdname("pk_corp");
		bodyInfor[0].setTablename("bd_corp");
		bodyInfor[0].setThiscodename("pk_custom1");
		bodyInfor[0].isCorpField = UFBoolean.TRUE;
		bodyInfor[0].isBasic = UFBoolean.TRUE;
		bodyInfor[0].isSave = UFBoolean.FALSE;//该字段不保存
//		收货地区
		bodyInfor[1].setCodename("areaclname");
		bodyInfor[1].setIdname("pk_areacl");
		bodyInfor[1].setTablename("bd_areacl");
		bodyInfor[1].setThiscodename("pk_replace");
		bodyInfor[1].isCorp = UFBoolean.TRUE;
		bodyInfor[1].isBasic = UFBoolean.TRUE;
		bodyInfor[1].setCorpname("pk_corp");	
	}

	@Override
	protected CodeToIDInfor[] getBodyTransFieldInfor() {
		return bodyInfor;
	}

	@Override
	protected CodeToIDInfor[] getHeadTransFieldInfor() {
		return headInfor;
	}

	@Override
	protected String getPushSaveActionName() throws BusinessException {
		return null;
	}

	@Override
	protected boolean isPushSaveBatch() {
		return false;
	}
	
	protected void pushBillSave(AggregatedValueObject[] newBills,String billtype) throws Exception{
//		表头表体基本档案保存   
		
//		表头如果相同维度的存在  改为  更新   对数据校验
		HYPubBO bo= new HYPubBO();
		
		if(newBills == null || newBills.length == 0)
			return;
		for(int i=0;i<newBills.length;i++){
			AggregatedValueObject bill=newBills[i];
			
			//设置新增单据的基本信息
			bill.getParentVO().setAttributeValue("vbillstatus", IBillStatus.FREE);
			bill.getParentVO().setAttributeValue("pk_billtype", WdsWlPubConst.WDSK);
			bill.getParentVO().setAttributeValue("dbilldate", new UFDate("2012-11-10"));
			bill.getParentVO().setAttributeValue("pk_corp", SQLHelper.getCorpPk());
			bill.getParentVO().setAttributeValue("voperatorid", InvocationInfoProxy.getInstance().getUserCode());
			bill.getParentVO().setAttributeValue("dmakedate", new UFDate());
			bill.getParentVO().setAttributeValue("vbillno", bo.getBillNo(billtype, SQLHelper.getCorpPk(), null, null));
		    
		
			
			bill.getParentVO().setStatus(VOStatus.NEW);//设置vo状态	   
			
		    SuperVO[] vos=(SuperVO[]) bill.getChildrenVO();
		    if(vos==null){
		    	continue;
		    }
		 
		    for(int j=0;j<vos.length;j++){
		    	vos[j].setAttributeValue("ifw", 1);
		    	vos[j].setAttributeValue("icoltype", 0);
		    	vos[j].setAttributeValue("denddate", new UFDouble(2));
		   }
			
			
		}
		for(int i=0;i<newBills.length;i++){
			bo.saveBill(newBills[i]);
		}	
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
		return  null;
	}
}
