package nc.ui.zb.comments;

import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.print.PrintEntry;
import nc.ui.zb.pub.ZbPubPrintDataSou;
import nc.vo.pub.AggregatedValueObject;

public class CommentsPrintTool {
	private String sLogCorp = null;
	private String sLogUser  = null;//登陆用户
	private String funcode = null;//功能节点
	private String nodeKey = null;//打印节点标识
	private String moduname = null;
	private BillCardPanel printDataModel = null;
	ZbPubPrintDataSou dataSource = null;
	nc.ui.pub.print.PrintEntry print = null;//new nc.ui.pub.print.PrintEntry(null,	dataSource);
	
	public CommentsPrintTool(String corp,String user,String funcode,String nodekey,String moduname,BillCardPanel pringDataModel){
		initTool(corp, user, funcode, nodekey, moduname, pringDataModel);
	}
	
	public CommentsPrintTool(String corp,String user,String funcode,String nodekey,String moduname){
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

	private ZbPubPrintDataSou getDataSource(){
		if(dataSource == null){
			dataSource = new ZbPubPrintDataSou(moduname,printDataModel);
		}
		return dataSource;
	}
	public void setDataSource(ZbPubPrintDataSou ds){
		dataSource = ds;
	}
	private nc.ui.pub.print.PrintEntry getPringEntry(){
//		if(print == null){
			print = new PrintEntry(null,getDataSource());
			print.setTemplateID(sLogCorp, funcode,sLogUser,null, nodeKey);
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
