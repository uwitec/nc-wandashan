package nc.vo.wdsnew.pub;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.bs.zmpub.pub.tool.stock.AvailNumBoTool;
import nc.vo.pub.SuperVO;
import nc.vo.wl.pub.WdsWlPubConst;
import nc.vo.zmpub.pub.report.ReportBaseVO;
/**
 * 库存可用量bo
 * @author mlr
 */
public class AvailNumBO extends AvailNumBoTool{
	private static final long serialVersionUID = -568997233504221598L;
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
    
    private Map<String, String[]> typetofields=new HashMap<String, String[]>();
    
    private Map<String ,String> typetosql=new HashMap<String ,String>();

	@Override
	public Map<String, String[]> getTypetoAccountFields() throws Exception {
		if(typetofields==null || typetofields.size()==0){
			typetofields.put(WdsWlPubConst.WDS3, new String[]{"h.pk_corp","h.pk_outwhouse",null,"b.pk_invmandoc","b.pk_invbasdoc",null,"b.vdef1",null,});			
			typetofields.put(WdsWlPubConst.WDSG, new String[]{"h.pk_corp","h.pk_outwhouse",null,"b.pk_invmandoc","b.pk_invbasdoc",null,"b.vdef1",null,});			
			typetofields.put(WdsWlPubConst.WDSS, new String[]{"h.pk_corp","h.pk_outwhouse",null,"b.pk_invmandoc","b.pk_invbasdoc",null,"b.vdef1",null,});			
			typetofields.put(WdsWlPubConst.WDS5, new String[]{"h.pk_corp","h.pk_outwhouse",null,"b.pk_invmandoc","b.pk_invbasdoc",null,"b.vdef1",null,});			
		}		
		return typetofields;
	}

	@Override
	public Map<String, String> getTypetoQuerySql() throws Exception {
		if(typetosql==null || typetosql.size()==0){
			typetosql.put(WdsWlPubConst.WDS3, " select   h.pk_corp pk_corp,h.pk_outwhouse pk_customize1,b.pk_invmandoc pk_invmandoc,b.pk_invbasdoc pk_invbasdoc,b.vdef1 ss_pk," +
					" coalesce(b.ndealnum,0)-coalesce(b.noutnum,0) whs_stocktonnage,coalesce(b.nassdealnum,0)-coalesce(b.nassoutnum,0)  whs_stockpieces  from " +
					" wds_sendorder h join  wds_sendorder_b b  on h.pk_sendorder=b.pk_sendorder " +
					" where isnull(h.dr,0)=0 and isnull(b.dr,0)=0 and coalesce(h.reserve14,'N')='N'" +
					" and h.pk_billtype='"+WdsWlPubConst.WDS3+"'"
					);	
			typetosql.put(WdsWlPubConst.WDSG, " select   h.pk_corp pk_corp,h.pk_outwhouse pk_customize1,b.pk_invmandoc pk_invmandoc,b.pk_invbasdoc pk_invbasdoc,b.vdef1 ss_pk," +
					" coalesce(b.ndealnum,0)-coalesce(b.noutnum,0) whs_stocktonnage,coalesce(b.nassdealnum,0)-coalesce(b.nassoutnum,0)  whs_stockpieces  from " +
					" wds_sendorder h join  wds_sendorder_b b  on h.pk_sendorder=b.pk_sendorder " +
					" where isnull(h.dr,0)=0 and isnull(b.dr,0)=0 and coalesce(h.reserve14,'N')='N'" +
					" and h.pk_billtype='"+WdsWlPubConst.WDSG+"'"
					);	
			typetosql.put(WdsWlPubConst.WDSS, " select   h.pk_corp pk_corp,h.pk_outwhouse pk_customize1,b.pk_invmandoc pk_invmandoc,b.pk_invbasdoc pk_invbasdoc,b.vdef1 ss_pk," +
					" coalesce(b.ndealnum,0)-coalesce(b.noutnum,0) whs_stocktonnage,coalesce(b.nassdealnum,0)-coalesce(b.nassoutnum,0)  whs_stockpieces  from " +
					" wds_sendorder h join  wds_sendorder_b b  on h.pk_sendorder=b.pk_sendorder " +
					" where isnull(h.dr,0)=0 and isnull(b.dr,0)=0 and coalesce(h.reserve14,'N')='N'" +
					" and h.pk_billtype='"+WdsWlPubConst.WDSS+"'"
					);	
			typetosql.put(WdsWlPubConst.WDS5, " select   h.pk_corp pk_corp,h.pk_outwhouse pk_customize1,b.pk_invmandoc pk_invmandoc,b.pk_invbasdoc pk_invbasdoc,b.vdef1 ss_pk," +
					" coalesce(b.narrangnmu,0)-coalesce(b.noutnum,0) whs_stocktonnage,coalesce(b.nassarrangnum,0)-coalesce(b.nassoutnum,0)  whs_stockpieces  from " +
					" wds_soorder h join  wds_soorder_b b  on h.pk_soorder=b.pk_soorder " +
					" where isnull(h.dr,0)=0 and isnull(b.dr,0)=0 and coalesce(h.reserve15,'N')='N'" +
					" and h.pk_billtype='"+WdsWlPubConst.WDS5+"'"+
					" and h.icoltype=0 "//出入库类型为 出库
					);	
		}
		return typetosql;
	}

	@Override
	public String getThisClassName() {
		return AvailNumBO.class.getName();
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
	public ReportBaseVO[] getOrderNumsForClient(SuperVO[] vos,
			Map<String, List<String>> sqls) throws Exception {	
	      return super.getOrderNumsForClient(vos,sqls);			
	}
	/**
	 * 得到定单占用量
	 * @throws Exception 
	 * @作者：mlr
	 * @说明：完达山物流项目 
	 * @时间：2012-7-26上午10:29:40
	 *
	 */
   public ReportBaseVO[] getOrderNums(SuperVO[] vos, Map<String, List<String>> sqls) throws Exception {
   
		return super.getOrderNums(vos,sqls);
	}
    /**
     * 根据传入的现存量vo  取出维度 查询现存量   
 	 * SuperVO[]  存放每个查询维度查询出来的现存量(按查询维度合并后)
  * @throws Exception 
     * @作者：mlr
     * @说明：完达山物流项目 
     * @时间：2012-7-2下午12:27:29
     *
     */
 	public  SuperVO[] queryStockCombinForClient(SuperVO[] vos) throws Exception {
	       return super.queryStockCombinForClient(vos);
	    }
	/**
	    * 根据传入的现存量vo  取出维度 查询现存量   
		 * SuperVO[]  存放每个查询维度查询出来的现存量(按查询维度合并后)
	 * @throws Exception 
	    * @作者：mlr
	    * @说明：完达山物流项目 
	    * @时间：2012-7-2下午12:27:29
	    *
	    */
		public SuperVO[] queryStockCombin(SuperVO[] vos) throws Exception {
			return super.queryStockCombin(vos);
		}
}
