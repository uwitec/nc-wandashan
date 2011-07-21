package nc.ui.zb.price.grade;

import java.util.HashMap;
import java.util.Map;

import nc.ui.am.inventory.command.ShowEqualCommand;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.ToftPanel;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.trade.pub.TableTreeNode;
import nc.uif.pub.exception.UifException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.SuperVO;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.zb.bill.deal.DealInvPriceBVO;
import nc.vo.zb.bill.deal.DealVendorBillVO;
import nc.vo.zb.bill.deal.DealVendorPriceBVO;
import nc.vo.zb.parmset.ParamSetVO;
import nc.vo.zb.pub.ZbPubConst;
import nc.vo.zb.pub.ZbPubTool;

public class PriceGradeDataBuffer {
	private ToftPanel ui = null;
	private String pk_corp = null;
	private Map<String, DealVendorBillVO[]> vendorInfor = null;//标书供货商信息  key:cbiddingid
	private String key = null;//当前选中的标书id
	private int iVHeadSelRow = -1;//供应商页签  表头选中行
	private TableTreeNode selectnode = null;
	
	private ParamSetVO para = null;//招标参数设置
	
	private boolean isAdjust = false;
	
	public void setAdjustFlag(boolean isadjust){
		isAdjust = isadjust;
	}
	
	public boolean isAdjust(){
		return this.isAdjust;
	}
	
	public ParamSetVO getPara(){
		return para;
	}
	
	public TableTreeNode getSelNode(){
		return selectnode;
	}
	public void setSelNode(TableTreeNode node){
		selectnode = node;
	}

	
	public void clearOnTreeSel(){
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
	}
	
	public void clear(){
		clearOnDeleteNode();
		vendorInfor.clear();
	}
	
	public void clearOnCol(){
//		clearOnDeleteNode();
		vendorInfor.clear();
		getVendorInfor(key);
	}

	public PriceGradeDataBuffer(ToftPanel ui){
		this.ui = ui;
		pk_corp = ClientEnvironment.getInstance().getCorporation().getPrimaryKey();
		initPara();
	}
	
	public void initPara(){
		String whereSql = " isnull(dr,0) = 0 and pk_corp = '"+pk_corp+"'";
		SuperVO[] vos = null;
		try {
			vos = HYPubBO_Client.queryByCondition(ParamSetVO.class, whereSql);
		} catch (UifException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			para = null;
			return;
		}
		if(vos==null || vos.length==0 || vos.length>1){
			ui.showErrorMessage("报价参数设置异常");
			para = null;
			return;
		}
			
		para = (ParamSetVO)vos[0];	
	}
	

	public void setVendorSelRow(int row){
		iVHeadSelRow = row;
		//刷新表体数据
	}
	public int getVendorSelRow(){
		return iVHeadSelRow;
//		刷新表体数据
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

	
	public DealVendorBillVO getCurrentVendor(){
		if(getVendorSelRow()<0)
			return null;
		return getVendorInfor(getCurrentBidding())[getVendorSelRow()];
	}

	public DealVendorBillVO[] getVendorInfor(String cbiddingid){
		if(vendorInfor == null){
			vendorInfor = new HashMap<String, DealVendorBillVO[]>();
		}
		if(PuPubVO.getString_TrimZeroLenAsNull(cbiddingid)==null)
			cbiddingid = getCurrentBidding();
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
	
	
	public String getCorp(){
		return pk_corp;
	}
	
	public void initBodyData(String cbiddingid){//初始时只加载标书数据  选中节点后  加载  标书相关信息：品种信息  供货商信息  报价信息
		Object o = null;
		try{
			o = PriceGradeHelper.initBodyData(ui, cbiddingid);
		}catch(Exception e){
			e.printStackTrace();
			ui.showErrorMessage(ZbPubTool.getString_NullAsTrimZeroLen(e.getMessage()));
			return;
		}
		Object[] os = (Object[])o;
		setVendorInfor(cbiddingid, (DealVendorBillVO[])os);
	}

}
