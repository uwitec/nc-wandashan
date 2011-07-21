package nc.ui.zb.bill.deal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.ToftPanel;
import nc.ui.trade.pub.TableTreeNode;
import nc.ui.trade.pub.VOTreeNode;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.zb.bill.deal.DealInvBillVO;
import nc.vo.zb.bill.deal.DealInvPriceBVO;
import nc.vo.zb.bill.deal.DealVendorBillVO;
import nc.vo.zb.bill.deal.DealVendorPriceBVO;
import nc.vo.zb.pub.ZbPubConst;
import nc.vo.zb.pub.ZbPubTool;

public class BillDataBuffer {
	private ToftPanel ui = null;
	private String pk_corp = null;
	private Map<String, DealVendorBillVO[]> vendorInfor = null;//标书供货商信息  key:cbiddingid
	private Map<String, DealInvBillVO[]> invInfor = null;//标书品种信息 key:cbiddingid
	private UFBoolean isinv = UFBoolean.TRUE;
	
	private String key = null;//当前选中的标书id
	private int iVHeadSelRow = -1;//供应商页签  表头选中行
	private int iIheadSelRow = -1;//品种页签 表头选中行
	
	private List<DealVendorBillVO> lSelData = null;
	
	private TableTreeNode selectnode = null;
	
	/**
	 * 获取当前选中标书设置的入围供应商数量
	 */
	public Integer getIVendorNum(){
		return PuPubVO.getInteger_NullAs(((VOTreeNode)selectnode).getData().getAttributeValue("nvendornum"),ZbPubTool.INTEGER_ZERO_VALUE);
	}
	
	public TableTreeNode getSelNode(){
		return selectnode;
	}
	public void setSelNode(TableTreeNode node){
		selectnode = node;
	}
	
	public List<DealVendorBillVO> getSelVendorInfor(){
		if(lSelData == null){
			lSelData = new ArrayList<DealVendorBillVO>();
		}
		return lSelData;
	}
	
	public void clearSelVendorInfor(){
		if(getSelVendorInfor().size()>0)
			getSelVendorInfor().clear();
	}
	
	public void addSelVendor(DealVendorBillVO vendor){
		if(vendor == null){
			vendor = getCurrentVendor();
			if(vendor == null)
				return;
		}		
		getSelVendorInfor().add(vendor);
	}
	
	public void clearOnTreeSel(){
		if(getSelVendorInfor().size()>0)
			getSelVendorInfor().clear();
		iIheadSelRow = -1;
		iVHeadSelRow = -1;
	}
	
	public void clearOnTreeSelRoot(){
		clearOnTreeSel();
		setKey(ZbPubConst.TREE_ROOT_TAG);
	}
	
	public void clearOnDeleteNode(){
		clearOnTreeSel();
		setKey(null);
		setSelNode(null);
		setIsinv(UFBoolean.TRUE);
	}
	
	public void clear(){
		clearOnDeleteNode();
		vendorInfor.clear();
		invInfor.clear();
	}
	
	public void removeSelVendor(int index){
		getSelVendorInfor().remove(index);
	}
	
	public void removeSelVendor(DealVendorBillVO vendor){
		if(vendor == null){
			vendor = getCurrentVendor();
			if(vendor == null)
				return;
		}
		getSelVendorInfor().remove(vendor);
	}
	
	public BillDataBuffer(ToftPanel ui){
		this.ui = ui;
		pk_corp = ClientEnvironment.getInstance().getCorporation().getPrimaryKey();
	}
	
	public UFBoolean getIsinv() {
		return isinv;
	}

	public void setIsinv(UFBoolean isinv) {
		this.isinv = isinv;
	}

	public void setVendorSelRow(int row){
		iVHeadSelRow = row;
		//刷新表体数据
	}
	public int getVendorSelRow(){
		return iVHeadSelRow;
//		刷新表体数据
	}
	public void setInvSelRow(int row){
		iIheadSelRow = row;
	}
	public int getInvSelRow(){
		return iIheadSelRow;
	}
	public void setKey(String cbiddingid){
		key = cbiddingid;
	}
	public String getCurrentBidding(){
		return key;
	}
	
	/**
	 * 
	 * @author zhf
	 * @说明：（鹤岗矿业）获取当前选中标书的 所有供应商信息
	 * 2011-5-5上午10:23:46
	 * @return
	 */
	public CircularlyAccessibleValueObject[] getCurrAllVendors(){
		
//		if(vendorInfor == null || vendorInfor.size() == 0){
//			return  null;
//		}
//		DealVendorBillVO[] billvos = ;
		return ZbPubTool.getParentVOFromAggBillVo(getVendorInfor(key),DealVendorPriceBVO.class);
	}
	
	/**
	 * 
	 * @author zhf
	 * @说明：（鹤岗矿业）获取选中供应商的品种报价明细
	 * 2011-5-5上午10:32:05
	 * @return
	 */
	public DealInvPriceBVO[] getCurrPricesOnSelVendor(){
		if(vendorInfor == null || vendorInfor.size() == 0){
			return null;
		}
		if(iVHeadSelRow>=0)
			return vendorInfor.get(key)[iVHeadSelRow].getBodys();
		return null;
	}
	
    /**
     * 
     * @author zhf
     * @说明：（鹤岗矿业）获取选中品种的供应商报价明细
     * 2011-5-5上午10:32:35
     * @return
     */
	public DealVendorPriceBVO[] getCurrPricesOnSelInv(){
		if(invInfor== null || invInfor.size() == 0){
			return null;
		}
		if(iIheadSelRow>=0)
			return invInfor.get(key)[iIheadSelRow].getBodys();
		return null;
	}
	
	public DealVendorBillVO getCurrentVendor(){
		if(getVendorSelRow()<0)
			return null;
		return getVendorInfor(getCurrentBidding())[getVendorSelRow()];
	}
	public DealInvBillVO getCurrentInv(){
		if(getInvSelRow()<0)
			return null;
		return getInvInfor(getCurrentBidding())[getInvSelRow()];
	}
	
	/**
	 * 
	 * @author zhf
	 * @说明：（鹤岗矿业）获取当前选中标书的 所有品种信息
	 * 2011-5-5上午10:24:12
	 * @return
	 */
	public CircularlyAccessibleValueObject[] getCurrAllInvs(){
//		if(invInfor == null || invInfor.size() == 0){
//			return null;
//		}
		return ZbPubTool.getParentVOFromAggBillVo(getInvInfor(key),DealInvPriceBVO.class);
	}
	
//	public  BillDataBuffer(){
//		pk_corp = ClientEnvironment.getInstance().getCorporation().getPrimaryKey();
//		initData();
//	}
	
	public DealInvBillVO[] getInvInfor(String cbiddingid){
		if(invInfor == null){
			invInfor = new HashMap<String, DealInvBillVO[]>();
		}
		if(invInfor.size() == 0 || !invInfor.containsKey(cbiddingid)){
			initBodyData(cbiddingid);
		}
		return invInfor.get(cbiddingid);
	}
	public DealVendorBillVO[] getVendorInfor(String cbiddingid){
		if(vendorInfor == null){
			vendorInfor = new HashMap<String, DealVendorBillVO[]>();
		}
		if(vendorInfor.size() == 0 || !vendorInfor.containsKey(cbiddingid)){
			initBodyData(cbiddingid);
		}
		return vendorInfor.get(cbiddingid);
	}
	
	public void setVendorInfor(String key,DealVendorBillVO[] infors){
		if(vendorInfor == null)
			vendorInfor=new HashMap<String, DealVendorBillVO[]>();
		vendorInfor.put(key, infors);
	}
	
	public void setInvInfor(String key,DealInvBillVO[] value){
		if(invInfor == null)
			invInfor = new HashMap<String, DealInvBillVO[]>();
		for(DealInvBillVO bill:value){
			DealInvBillVO.sortAndDealBodys(bill.getBodys());
		}
		
		invInfor.put(key, value);
	}
	
//	public BiddingHeaderVO[] getBiddingdata() {
//		return m_biddingdata;
//	}
//
//	public void setM_biddingdata(BiddingHeaderVO[] m_biddingdata) {
//		this.m_biddingdata = m_biddingdata;
//	}
	
//	public BiddingHeaderVO[] initData() throws Exception{//初始化数据
//		return 
//	}
	
	public String getCorp(){
		return pk_corp;
	}
	
	public void initBodyData(String cbiddingid){//初始时只加载标书数据  选中节点后  加载  标书相关信息：品种信息  供货商信息  报价信息
		Object o = null;
		try{
			o = BillDealHelper.initBodyData(ui, cbiddingid,getIsinv());
		}catch(Exception e){
			e.printStackTrace();
			ui.showErrorMessage(ZbPubTool.getString_NullAsTrimZeroLen(e.getMessage()));
			return;
		}
		Object[] os = (Object[])o;
		setVendorInfor(cbiddingid, (DealVendorBillVO[])os[0]);
		setInvInfor(cbiddingid,(DealInvBillVO[]) os[1]);
	}

}
