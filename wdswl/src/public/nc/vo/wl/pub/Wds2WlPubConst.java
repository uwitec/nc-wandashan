package nc.vo.wl.pub;

public class Wds2WlPubConst {
	
//	zhf  wds 二期工程  2012-06-19 
	
	//单据类型  
//	存货状态调整单
	public final static  String  billtype_statusupdate = "WS20";   //处理状态变更单保存  状态变化前减少量
	
	public final static  String  billtype_alloinsendorder = "WS21";   //调入运单
	
	public final static String virtual = "virtual";
	public final static String other = "other";
	
	
	//库存现存量更新
	public final static  String  billtype_statusupdate_1 = "WS20_1";//处理状态变更单  保存 状态变化后 新增量
	public final static  String  billtype_statusupdate_2 = "WS20_2";//处理状态变更单删除  状态变化前减少量
	public final static  String  billtype_statusupdate_3 = "WS20_3";//处理状态变更单 删除   状态变化后 新增量


	
}
