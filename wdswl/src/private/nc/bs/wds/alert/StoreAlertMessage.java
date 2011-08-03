package nc.bs.wds.alert;
import java.util.List;
import nc.bs.dao.BaseDAO;
import nc.bs.ml.NCLangResOnserver;
import nc.bs.pub.pa.html.IAlertMessage;
import nc.bs.pub.pa.html.IAlertMessage2;
import nc.jdbc.framework.processor.ArrayListProcessor;
/**
 * @author mlr
 * Ԥ����Ϣ��ʽ
 */
public class StoreAlertMessage implements IAlertMessage2{

	private static final long serialVersionUID = 1932855774969661891L;
	private BaseDAO dao=null;
	
	private BaseDAO getDao(){
		if(dao==null){
			dao=new BaseDAO();
		}
		return dao;	   
	}
	
	public StoreAlertMessage() {
		super();
		
	}

	public int[] getBodyColumnType() {
		
		return new int[]{
		  IAlertMessage.TYPE_STRING,
		  IAlertMessage.TYPE_STRING,
		  IAlertMessage.TYPE_FLOAT,
		  IAlertMessage.TYPE_FLOAT,
		  IAlertMessage.TYPE_DATE,
		};
	}

	public String getNullPresent() {
	
		return "";
	}

	public String getOmitPresent() {
		
		return "omitted";
	}

	public String[] getBodyFields() {
		
		return new String[]{
		 "���̱���",
		 "��������",
		 "���������",
		 "��渨����",
		 "�������"
		};
	}

	public Object[][] getBodyValue() {
		
		return null;
	}

	public float[] getBodyWidths() {
		
		return new float[]{0.2f,0.2f,0.2f,0.2f,0.2f};
	}

	public String[] getBottom() {
		
		return new String[]{ "AAAAAAAAA", "", "BBBBB", "CCCCC", "DDDDD", "EEEE", "FFF", "GGGG", "HHHH",
				"II", "JJJJJJJJJ" };

	}

	public String getTitle() {		
		 return NCLangResOnserver.getInstance().getStrByID("101502", "UPP101502-000261");
	}

	public String[] getTop() {
		
		return new String[] { "AAAAA", "PPPPPPP", "BBBBB", "CCCC", "DDDDD", "EEEEEE", "FFFFFFFFF","GGGGGGGGMMMMM", "NN" };
	}
	/**
	 * 
	 * @���ߣ�mlr
	 * @˵�������ɽ������Ŀ 
	 *      �ӿ��״̬�в�ѯ������ ���
	 * @ʱ�䣺2011-8-2����10:52:47
	 * @return
	 */
   private Object[][] queryOverDateStore() throws Exception{
	  Object o=getDao().executeQuery(getSql(),new ArrayListProcessor());
	  if(o==null){
		  return null;
	  }  
	  List list=(List)o;
	  if(list==null||list.size()==0){
		  return null;
	  } 
	  int size=list.size();
	  Object[][] arr=new Object[size][];//��������������
	  Object[] arr1=null;//��ʱ����
	  int size1=0;//��ʱ����ĳ���
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
    * @���ߣ�mlr
    * @˵�������ɽ������Ŀ 
    *       ��ò�ѯ���
    * @ʱ�䣺2011-8-2����11:13:20
    * @return
    */
   private String getSql(){
	   StringBuffer sql=new StringBuffer();
	   sql.append(" select t.pplpt_pk,");
	   sql.append(" 	t.pplpt_pk ,");
	   sql.append(" 	t.whs_stocktonnage ,");
	   sql.append(" 	t.whs_stockpieces ,");
	   sql.append(" 	t.creadate");
	   sql.append("  from ");
	   sql.append("  tb_warehousestock t ");
	   sql.append("  join wds_invbasdoc w  ");
	   sql.append("  on t.pk_invmandoc = w.pk_invmandoc");
//	   sql.append("  where isnull(t.dr, 0) = 0 ");
//	   sql.append("  and isnull(w.dr, 0) = 0 ");   

	   return sql.toString();   
   }
}
