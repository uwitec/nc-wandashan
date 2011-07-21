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
	private Map<String, DealVendorBillVO[]> vendorInfor = null;//���鹩������Ϣ  key:cbiddingid
	private String key = null;//��ǰѡ�еı���id
	private int iVHeadSelRow = -1;//��Ӧ��ҳǩ  ��ͷѡ����
	private TableTreeNode selectnode = null;
	
	private ParamSetVO para = null;//�б��������
	
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
			ui.showErrorMessage("���۲��������쳣");
			para = null;
			return;
		}
			
		para = (ParamSetVO)vos[0];	
	}
	

	public void setVendorSelRow(int row){
		iVHeadSelRow = row;
		//ˢ�±�������
	}
	public int getVendorSelRow(){
		return iVHeadSelRow;
//		ˢ�±�������
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
	 * @˵�������׸ڿ�ҵ����ȡ��ǰѡ�б���� ���й�Ӧ����Ϣ
	 * 2011-5-5����10:23:46
	 * @return
	 */
	public CircularlyAccessibleValueObject[] getCurrAllVendors(){
		return ZbPubTool.getParentVOFromAggBillVo(getVendorInfor(key),DealVendorPriceBVO.class);
	}
	
	/**
	 * 
	 * @author zhf
	 * @˵�������׸ڿ�ҵ����ȡѡ�й�Ӧ�̵�Ʒ�ֱ�����ϸ
	 * 2011-5-5����10:32:05
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
	
	public void initBodyData(String cbiddingid){//��ʼʱֻ���ر�������  ѡ�нڵ��  ����  ���������Ϣ��Ʒ����Ϣ  ��������Ϣ  ������Ϣ
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
