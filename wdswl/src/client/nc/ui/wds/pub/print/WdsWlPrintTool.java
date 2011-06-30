package nc.ui.wds.pub.print;

import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.print.PrintEntry;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.scm.pu.PuPubVO;

public class WdsWlPrintTool {
	private String sLogCorp = null;
	private String sLogUser  = null;//登陆用户
	private String funcode = null;//功能节点
	private String nodeKey = null;//打印节点标识
	private String moduname = null;
	private BillCardPanel printDataModel = null;
	WdsPubPrintDataSou dataSource = null;
	private String m_printTempletID = null;
	nc.ui.pub.print.PrintEntry print = null;//new nc.ui.pub.print.PrintEntry(null,	dataSource);
	
	public void setPrintTempletID(String temID){
		this.m_printTempletID = temID;
	}
	protected String getPrintTempletID(){
		return this.m_printTempletID;
	}
	public WdsWlPrintTool(String corp,String user,String funcode,String nodekey,String moduname,BillCardPanel pringDataModel){
		initTool(corp, user, funcode, nodekey, moduname, pringDataModel);
	}
	
	public WdsWlPrintTool(String corp,String user,String funcode,String nodekey,String moduname){
		initTool(corp, user, funcode, nodekey, moduname, null);
	}
	
	public void initTool(String corp,String user,String funcode,String nodekey,String moduname,BillCardPanel pringDataModel){
		sLogCorp = corp;
		sLogUser = user;
		this.funcode = funcode;
		this.nodeKey = nodekey;
		this.moduname = moduname;
		this.printDataModel = pringDataModel;
	}
	
	public void setPringdataModel(BillCardPanel printDataModel){
		this.printDataModel = printDataModel;
	}
	
	protected WdsPubPrintDataSou createDataSource(String modu,BillCardPanel printmodel){
		moduname = modu;
		printDataModel = printmodel;
		return new WdsPubPrintDataSou(modu,printmodel);
	}

	private WdsPubPrintDataSou getDataSource(){
		if(dataSource == null){
			dataSource = createDataSource(moduname,printDataModel);
		}
		return dataSource;
	}
	public void setDataSource(WdsPubPrintDataSou ds){
		dataSource = ds;
	}
	private nc.ui.pub.print.PrintEntry getPringEntry(){
		//		if(print == null){
		print = new PrintEntry(null,getDataSource());
		if(PuPubVO.getString_TrimZeroLenAsNull(getPrintTempletID())==null)
			print.setTemplateID(sLogCorp, funcode,sLogUser,null, nodeKey);
		else
			print.setTemplateID(getPrintTempletID());
		//		}
		return print;
	}
	public void clear(){
		print = null;
	}
	public void print(AggregatedValueObject pringdata){
		
	}
	
	public void priview(AggregatedValueObject pringdata){
		getDataSource().setPrintDatas(pringdata);
//		getPringEntry()
		if(getPringEntry().selectTemplate()==1)
			getPringEntry().preview();
	}
	
	public void batchPrint(AggregatedValueObject[] pringdata){
		
	}
}
