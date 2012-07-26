package nc.vo.wdsnew.pub;
import java.util.HashMap;
import java.util.Map;
import nc.bs.zmpub.pub.tool.stock.AvailNumBoTool;
import nc.vo.wl.pub.WdsWlPubConst;
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
		}		
		return typetofields;
	}

	@Override
	public Map<String, String> getTypetoQuerySql() throws Exception {
		if(typetosql==null || typetosql.size()==0){
			typetosql.put(WdsWlPubConst.WDS3, " select  from h.pk_corp pk_corp,h.pk_outwhouse pk_customize1,b.pk_invmandoc pk_invmandoc,b.pk_invbasdoc pk_invbasdoc,b.vdef1 ss_pk," +
					" coalesce(b.ndealnum,0)-coalesce(b.noutnum,0) whs_stocktonnage,coalesce(nassdealnum,0)-coalesce(nassoutnum,0)  whs_stockpieces  from " +
					" wds_sendorder h join  wds_sendorder_b b  on h.pk_sendorder=b.pk_sendorder " +
					" where isnull(h.dr,0)=0 and isnull(b.dr,0)=0 and coalesce(reserve14,'N')='N'" );		
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
}
