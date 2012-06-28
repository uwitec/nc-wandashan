package nc.ui.wdsnew.pub;
import java.util.ArrayList;
import java.util.List;
import nc.jdbc.framework.processor.ArrayListProcessor;
import nc.ui.dbcache.DBCacheFacade;
import nc.ui.pub.ClientEnvironment;
import nc.ui.wl.pub.LoginInforHelper;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.scm.pu.PuPubVO;
/**
 * ���ɽ���� Ȩ�޹��˹�����
 * ʵ�ַ�����
 * ���ݲ���Ա ��ѯ ����Ĵ����������
 * ���ݲ���Ա ���� �ò���Ա���������Ĳ�ѯsql��� 
 * ���ݲ���Ա ���� ����ⵥ�������� �����˳�����Ա����Ͻ�Ĵ���ĳ���ⵥ�������ݣ� 
 * @author mlr
 */
public class PowerGetTool {
	private static LoginInforHelper login;
	public static LoginInforHelper getLoginInfor(){
		if(login==null ){
			login=new LoginInforHelper();
		}
		return login;
	}	
	/**
	 * ���ĳ�û� ���������� ��ѯsql
	 * @param pk_user
	 *            �û�
	 * @return
	 * @throws Exception 
	 */
	public static String queryClassPowerSql(String pk_user) throws Exception {
		if(pk_user==null || pk_user.length()==0)
			throw new Exception("����ԱΪ��");
		//������Ǳ���Ա ����
		if(getLoginInfor().getLogInfor(pk_user)==null){
			throw new Exception("�ò���Աû�а󶨻�λ");
		}
		
//		if(getLoginInfor().getLogInfor(pk_user).getType()!=0){
//			throw new Exception("�ò���Ա���Ǳ���Ա");
//		}
		String[] pk_cargdocs=getLoginInfor().getSpaceByLogUser(pk_user);
		if(pk_cargdocs==null ||pk_cargdocs.length==0)
			throw new Exception("�ò���Աû�а󶨻�λ");
		String sql=" select b.pk_invmandoc " +
				"   from wds_cargdoc1 h  " +
				"join tb_spacegoods b" +
				" on h.pk_wds_cargdoc=b.pk_wds_cargdoc " +
				" where isnull(h.dr,0)=0  "+
				"  and  isnull(b.dr,0)=0 "+
				"  and  h.pk_corp='"+ClientEnvironment.getInstance().getCorporation().getPrimaryKey()+"'"+
				"  and  h.pk_cargdoc='"+pk_cargdocs[0]+"' ";     
		return sql;
	}
	/**
	 * ���� ����Ա ���� ���
	 * @param tmpBodyVo  Ҫ���˵�vo����
	 * @param pk_user
	 * @return
	 * @throws Exception 
	 */
	    public static CircularlyAccessibleValueObject[] spiltByPower(CircularlyAccessibleValueObject[] tmpBodyVo,String pk_invmandocName,String pk_user) throws Exception {
            //ȡ�øò���Ա������Ĵ������id
	    	ArrayList pks=getPowerByUser(pk_user);
	    	   //��Ź��˺�ı�������
	        List<CircularlyAccessibleValueObject> list=new ArrayList<CircularlyAccessibleValueObject>();
	        for(int i=0;i<tmpBodyVo.length;i++){
	            String pk=PuPubVO.getString_TrimZeroLenAsNull(
	        			tmpBodyVo[i].getAttributeValue(pk_invmandocName));
	            if(exist(pk,pks))            	
	        	        list.add(tmpBodyVo[i]);           	
	        }
	       return  list.toArray((CircularlyAccessibleValueObject[])java.lang.reflect.Array.newInstance(tmpBodyVo[0].getClass(), list.size()));	     
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
		 * @throws Exception 
		 */
		public static ArrayList getPowerByUser(String pk_user) throws Exception {
			String sql = queryClassPowerSql(pk_user);
			ArrayListProcessor bc = new ArrayListProcessor();
			ArrayList alGroup = (ArrayList) DBCacheFacade.runQuery(sql, bc);
			return alGroup;
		}
	    
}
