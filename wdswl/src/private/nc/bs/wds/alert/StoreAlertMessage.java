package nc.bs.wds.alert;
import java.util.List;
import nc.bs.dao.BaseDAO;
import nc.bs.ml.NCLangResOnserver;
import nc.bs.pub.pa.html.IAlertMessage;
import nc.bs.pub.pa.html.IAlertMessage2;
import nc.jdbc.framework.processor.ArrayListProcessor;
import nc.ui.wl.pub.LoginInforHelper;
/**
 * @author mlr
 * 预警消息格式
 */
public class StoreAlertMessage implements IAlertMessage2{
	private static final long serialVersionUID = 1932855774969661891L;
	private BaseDAO dao=null;
	private String pk_corp;
	private String userid;
	private LoginInforHelper login=new LoginInforHelper();
	
	
	private BaseDAO getDao(){
		if(dao==null){
			dao=new BaseDAO();
		}
		return dao;	   
	}
	public StoreAlertMessage(String pk_corp,String userid) {
		super();
		this.pk_corp=pk_corp;
		this.userid=userid;
	}
	public int[] getBodyColumnType() {		
		return new int[]{
		  IAlertMessage.TYPE_STRING,
		  IAlertMessage.TYPE_STRING,
		  IAlertMessage.TYPE_STRING,
		  IAlertMessage.TYPE_STRING,
		  IAlertMessage.TYPE_STRING,
		  IAlertMessage.TYPE_FLOAT,
		  IAlertMessage.TYPE_FLOAT,
		};
	}
	public String getNullPresent() {	
		return "";
	}
	public String getOmitPresent() {		
		return "omitted";
	}
	public String[] getBodyFields() {
		// 仓库 货位 存货 存货编码  批次  库存主数量 库存辅数量	
		return new String[]{
		 "仓库",
		 "货位",
		 "存货",
		 "存货编码",
		 "批次",
		 "库存主数量",
		 "库存辅数量"
		};
	}
	public Object[][] getBodyValue() {		
		try {
			return queryOverDateStore(pk_corp,userid);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	public float[] getBodyWidths() {
		
		return new float[]{0.1f,0.1f,0.1f,0.2f,0.1f,0.2f,0.2f};
	}
	public String[] getBottom() {		
		return null;
	}
	public String getTitle() {		
		 return NCLangResOnserver.getInstance().getStrByID("101502", "UPP101502-000261");
	}
	public String[] getTop() {		
		return null;
	}
	/**
	 * 
	 * @作者：mlr
	 * @说明：完达山物流项目 
	 *      从库存状态中查询大日期 存货
	 * @时间：2011-8-2上午10:52:47
	 * @return
	 */
   private Object[][] queryOverDateStore(String pk_corp,String userid) throws Exception{
	  Object o=getDao().executeQuery(getSql(pk_corp,userid),new ArrayListProcessor());
	  if(o==null){
		  return null;
	  }  
	  List list=(List)o;
	  if(list==null||list.size()==0){
		  return null;
	  } 
	  int size=list.size();
	  Object[][] arr=new Object[size][getBodyFields().length];//符合条件的数据
	  Object[] arr1=null;//临时数组
	  int size1=0;//临时数组的长度
	  for(int i=0;i<size;i++){
		  arr1=(Object[]) list.get(i);
		  size1=arr1.length;
		  for(int j=0;j<size1;j++){
			 arr[i][j]=arr1[j]; 		  
		  }
	  }
	  return  arr;
   }
   /**
    * 
    * @作者：mlr
    * @说明：完达山物流项目 
    *       获得查询语句
    * @时间：2011-8-2上午11:13:20
    * @return
    */
   private String getSql(String pk_corp,String userid){
	   // 仓库 货位 存货 存货编码  批次  库存主数量 库存辅数量
	   StringBuffer sql=new StringBuffer();
	   sql.append(" select min(s.storname),");
	   sql.append(" min(c.csname),");
	   sql.append(" min(bs.invcode),");
	   sql.append(" min(bs.invname),");
	   sql.append(" t.whs_batchcode,");	   
	   sql.append(" sum(t.whs_stocktonnage) ,");
	   sql.append(" sum(t.whs_stockpieces) ");	 
	   sql.append(" from ");
	   sql.append(" tb_warehousestock t ");
	   sql.append(" join wds_invbasdoc w  ");
	   sql.append(" on t.pk_invmandoc = w.pk_invmandoc");
	   sql.append(" join bd_stordoc s");
	   sql.append(" on t.pk_customize1=s.pk_stordoc");
	   sql.append(" join bd_cargdoc c");
	   sql.append(" on t.pk_cargdoc=c.pk_cargdoc");
	   sql.append(" join bd_invbasdoc bs");
	   sql.append(" on t.pk_invbasdoc =bs.pk_invbasdoc");
	   sql.append(" join tb_stockstaff ff ");
	   sql.append(" on t.pk_customize1=ff.pk_stordoc and t.pk_cargdoc=ff.pk_cargdoc");
	   sql.append(" where");
	   sql.append(" t.pk_corp='"+pk_corp+"'");
	   sql.append(" and ff.cuserid='"+userid+"'");
	   sql.append(" and isnull(t.dr,0)=0");
	   sql.append(" and isnull(ff.dr,0)=0");
	   sql.append(" and isnull(w.dr,0)=0");   
	   sql.append(" and isnull(s.dr,0)=0");	   
	   sql.append(" and isnull(c.dr,0)=0");	   
	   sql.append(" and isnull(bs.dr,0)=0");
	   sql.append(" and sysdate-to_date(t.creadate,'yyyy-mm-dd')>=w.so_ywaring_days ");
	   sql.append(" group by ");
	   sql.append(" s.pk_stordoc,c.pk_cargdoc,bs.pk_invbasdoc,t.whs_batchcode");
	   sql.append(" having ");
	   sql.append(" sum(t.whs_stocktonnage)>0");      
	   return sql.toString();   
   }
}
