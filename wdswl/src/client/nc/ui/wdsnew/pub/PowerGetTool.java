package nc.ui.wdsnew.pub;
import java.util.ArrayList;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
/**
 * ���ɽ���� Ȩ�޹��˹�����
 * ʵ�ַ�����
 * ���ݲ���Ա ��ѯ ����Ĵ����������
 * ���ݲ���Ա ���� �ò���Ա���������Ĳ�ѯsql��� 
 * ���ݲ���Ա ���� ����ⵥ�������� �����˳�����Ա����Ͻ�Ĵ���ĳ���ⵥ�������ݣ� 
 * @author mlr
 */
public class PowerGetTool {
	
	/**
	 * ���ĳ�û� ���������� ��ѯsql
	 * @param pk_user
	 *            �û�
	 * @return
	 */
	public static String queryClassPowerSql(String pk_user) {
        //д��ʱ������˹��˹�˾
		return null;
	}
	/**
	 * ���� ����Ա ���� ���
	 * @param tmpBodyVo  Ҫ���˵�vo����
	 * @param pk_user
	 * @return
	 * @throws BusinessException
	 */
	    public static CircularlyAccessibleValueObject[] spiltByPower(CircularlyAccessibleValueObject[] tmpBodyVo,String pk_invmandocName,String pk_user) throws BusinessException {
  
	       return  null;
		}
	    /**
	     * �ж�pkֵ�Ƿ���arrayList�д���
	     * @param pk
	     * @param arrayList2
	     * @return
	     */
		public static boolean exist(String pk, ArrayList arrayList) {
			if(pk==null || pk.length()==0)
				return false;
			boolean isf=false;
			for(int i=0;i<arrayList.size();i++){
				Object[] os=(Object[]) arrayList.get(i);
				if(pk.equals(os[0])){
					isf=true;
					return true;
				}
			}		
			return false;
		}

		/**
		 * ��ò���Ա����Ȩ���� �Ĵ����������������
		 * 
		 * @param tableName
		 * @param pk_corp
		 * @param pk_user
		 * @return
		 */
		public static ArrayList<String> getPowerByUser(String pk_user) {
//			String sql = queryClassPowerSql(tableName, pk_corp, pk_user);
//			ArrayListProcessor bc = new ArrayListProcessor();
//			ArrayList alGroup = (ArrayList) DBCacheFacade.runQuery(sql, bc);
			return null;
		}
	    
}
