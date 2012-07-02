package nc.vo.wdsnew.pub;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import nc.bs.zmpub.pub.tool.stock.BillStockBO;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.wl.pub.WdsWlPubConst;
/**
 * 完达山项目 业务单据先存量更新类
 * 其他出库 其他入库 调拨出库 调拨入库 都通过该入口 更新现存量
 * 一般查询现存量 也通过该入口
 * @author mlr
 */
public class BillStockBO1 extends BillStockBO{
	/**
	 * 单据类型 ->交换类 对应关系
	 */
	private Map<String ,String > typetoChangeclass=new HashMap<String, String>();
	/**
	 *  单据类型->现存量 数量变化规则
	 */
    private Map<String,UFBoolean[]> typetosetnum=new HashMap<String, UFBoolean[]>();
    /**
     * 现存量 数量变化字段
     * 库存主数量 库存辅数量
     */
    private String[] changeNums =new String[]{"whs_stocktonnage","whs_stockpieces"};
    /**
     * 现存量实现类 路径
     */
    private String  className="nc.vo.ic.pub.StockInvOnHandVO";
    /**
     * 先存量定义的最小维度
     *维度为： 公司 仓库 货位 存货 批次 存货状态 入库日期
     */
    private String[] def_fields=new String[]{
    		"pk_corp","pk_customize1","pk_cargdoc",
    		"pk_invmandoc","pk_invbasdoc","whs_batchcode",
    		"ss_pk","creadate"};
	@Override
	public Map<String, String> getTypetoChangeClass() throws Exception {
		if(typetoChangeclass.size()==0){
			typetoChangeclass.put(WdsWlPubConst.BILLTYPE_OTHER_IN, "nc.bs.wds.self.changedir.CHGWDS7TOACCOUNTNUM");//处理其他入库保存
			typetoChangeclass.put(WdsWlPubConst.BILLTYPE_OTHER_IN_1, "nc.bs.wds.self.changedir.CHGWDS7TOACCOUNTNUM");//处理其他入库删除
			typetoChangeclass.put(WdsWlPubConst.BILLTYPE_ALLO_IN, "nc.bs.wds.self.changedir.CHGWDS9TOACCOUNTNUM");//处理调拨入库保存
			typetoChangeclass.put(WdsWlPubConst.BILLTYPE_ALLO_IN_1, "nc.bs.wds.self.changedir.CHGWDS9TOACCOUNTNUM");//处理调拨入库删除
			typetoChangeclass.put(WdsWlPubConst.BILLTYPE_OTHER_OUT, "nc.bs.wds.self.changedir.CHGWDS6TOACCOUNTNUM");//处理其他出库保存
			typetoChangeclass.put(WdsWlPubConst.BILLTYPE_OTHER_OUT_1, "nc.bs.wds.self.changedir.CHGWDS6TOACCOUNTNUM");//处理其他出库删除
			typetoChangeclass.put(WdsWlPubConst.BILLTYPE_SALE_OUT, "nc.bs.wds.self.changedir.CHGWDS8TOACCOUNTNUM");//处理销售出库保存
			typetoChangeclass.put(WdsWlPubConst.BILLTYPE_SALE_OUT_1, "nc.bs.wds.self.changedir.CHGWDS8TOACCOUNTNUM");//处理销售出库删除			
		}	
		return typetoChangeclass;
	}

	@Override
	public Map<String, UFBoolean[]> getTypetosetnum() throws Exception {
		if(typetosetnum.size()==0){
			typetosetnum.put(WdsWlPubConst.BILLTYPE_OTHER_IN, new UFBoolean[]{new UFBoolean(false),new UFBoolean(false)});
			typetosetnum.put(WdsWlPubConst.BILLTYPE_OTHER_IN_1, new UFBoolean[]{new UFBoolean(true),new UFBoolean(true)});
			typetosetnum.put(WdsWlPubConst.BILLTYPE_ALLO_IN, new UFBoolean[]{new UFBoolean(false),new UFBoolean(false)});
			typetosetnum.put(WdsWlPubConst.BILLTYPE_ALLO_IN_1, new UFBoolean[]{new UFBoolean(true),new UFBoolean(true)});
			typetosetnum.put(WdsWlPubConst.BILLTYPE_OTHER_OUT, new UFBoolean[]{new UFBoolean(true),new UFBoolean(true)});
			typetosetnum.put(WdsWlPubConst.BILLTYPE_OTHER_OUT_1, new UFBoolean[]{new UFBoolean(false),new UFBoolean(false)});
			typetosetnum.put(WdsWlPubConst.BILLTYPE_SALE_OUT, new UFBoolean[]{new UFBoolean(true),new UFBoolean(true)});
			typetosetnum.put(WdsWlPubConst.BILLTYPE_SALE_OUT_1, new UFBoolean[]{new UFBoolean(false),new UFBoolean(false)});

		}
		return typetosetnum;
	}
	/**
	 * 通过where条件查询现存量
	 * @param whereSql
	 * @return
	 */
	public SuperVO[] queryStock(String whereSql)throws Exception{
		String clname=getClassName();
		if(clname==null || clname.length()==0)
			throw new Exception("没有注册现存量实现类全路径");
		Class cl=Class.forName(clname);
		Collection list= getDao().retrieveByClause(cl, whereSql);
        if(list==null || list.size()==0)
        	return null;
        SuperVO[] vos=(SuperVO[]) list.toArray((SuperVO[])java.lang.reflect.Array.newInstance(cl, list.size()));		
		return vos;
		
	}
	@Override
	public String[] getChangeNums() {
		
		return changeNums;
	}

	@Override
	public String getClassName() {
		
		return className;
	}

	@Override
	public String[] getDef_Fields() {
		
		return def_fields;
	}

}
