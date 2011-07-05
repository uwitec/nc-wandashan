package nc.bs.wl.pub;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Dictionary;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.NullFieldException;
import nc.vo.pub.SuperVO;
	/**
	 * author:mlr
	 * ����Ϊ����ǰ�� ��̨У���� 
	 * 
	 * */
	public  class BsBeforeSaveValudate{
		
	/**
	 * 	
	 * @���ߣ�mlr
	 * @˵�������ɽ������Ŀ 
	 *        ����ļ����ֶι���Ψһ�ԣ�ÿһ�еļ�¼�ļ����ֶι�����Ψһ�Լ�¼���������бȽ� ������Ψһ��У��
	 * @ʱ�䣺2011-7-5����07:26:48
	 * @param chs
	 * @param fields
	 * @param displays
	 * @throws Exception
	 */	
	public static void beforeSaveBodyUnique(CircularlyAccessibleValueObject[] chs, String[] fields,String[] displays) throws Exception {
		if (chs == null || chs.length == 0) {
			return;
		}
		int num = chs.length;
		if (fields == null || fields.length == 0) {
			return;
		}
		if (num > 0) {
			ArrayList<String> list = new ArrayList<String>();
			for (int i = 0; i < num; i++) {
				String key = "";
				for (String str : fields) {
					Object o1 = chs[i].getAttributeValue(str);
					key = key + "," + String.valueOf(o1);
				}
				String dis = "";
				for (int j = 0; j < displays.length; j++) {
					dis = dis + "[ " + displays[j] + " ]";
				}
				if (list.contains(key)) {
					throw new BusinessException("��[" + (i + 1) + "]�б����ֶ� "+ dis + " �����ظ�!");
				} else {
					list.add(key);
				}
			}

		}
	   }
	/**
	 * 
	 * @���ߣ�mlr
	 * @˵�������ɽ������Ŀ 
	 *        ���岻����Ϊ�յ�У��
	 * @ʱ�䣺2011-7-5����08:37:54
	 * @param vos
	 * @throws Exception
	 */
		public static void BodyNotNULL(CircularlyAccessibleValueObject[] vos) throws Exception{
			if(vos==null || vos.length==0){
				throw new Exception("���岻����Ϊ��");
			}
		}
		/**
		 * 
		 * @���ߣ�mlr
		 * @˵�������ɽ������Ŀ 
		 *        �ֶβ�Ϊ��У��(ֻУ��һ���ֶ�)
		 * @ʱ�䣺2011-7-5����08:41:08
		 * @param bodys
		 * @param checkField
		 * @param displayName
		 * @throws Exception
		 */
		public static void bodyFieldNotNull(CircularlyAccessibleValueObject[] bodys,String checkField,String displayName)throws Exception{
			
			  for(int i=0;i<bodys.length;i++){
	              if(isEmpty(bodys[i].getAttributeValue(checkField))){            	  
	            	  throw new Exception("�����"+(i+1)+"��"+displayName+"����Ϊ��");
	              }
	          }    	
		}		
		/**
		 * 	
		 * @���ߣ�mlr
		 * @˵�������ɽ������Ŀ 
		 *        �ֶβ�Ϊ�յ�У�� 
		 *        ����ǰ̨�ı�����У��
		 * 
		 * @ʱ�䣺2011-7-5����04:59:49
		 * @param vos ҪУ���vos
		 * @param fields ҪУ����ֶ�����
		 * @param fields Ҫ����������ʾ���ֶ�����
		 * @throws BusinessException
		 */
		public static void FieldNotNull(SuperVO[] vos,String[] fields,String[] names)throws BusinessException{
			if(isEmpty(vos) || isEmpty(fields)|| isEmpty(names)){
				return;
			}
			if(fields.length !=names.length){
				throw new BusinessException("У���ֶκ���ʾ�ֶ����鳤�Ȳ�һ��");
			}
			StringBuffer message=null;
			for(int i=0;i<vos.length;i++){
				SuperVO vo=vos[i];
				for(int j=0;j<fields.length;j++){
				Object o=vo.getAttributeValue(fields[j]);
				if(isNULL(o)){
					if (message == null)
						message = new StringBuffer();
					message.append("[");
					message.append(names[i]);
					message.append("]");
					message.append(",");
				  }
				}
			}
			if (message != null) {
				message.deleteCharAt(message.length() - 1);
				throw new NullFieldException(message.toString());
			}	
		}
     /**
      * 
      * @���ߣ�mlr
      * @˵�������ɽ������Ŀ 
      *       �ж϶����Ƿ�Ϊ��
      * @ʱ�䣺2011-7-5����08:43:29
      * @param value
      * @return
      */
		public static boolean isEmpty(Object value)
		{
			if (value == null)
				return true;
			if ((value instanceof String)
					&& (((String) value).trim().length() <= 0))
				return true;
			if ((value instanceof Object[]) && (((Object[]) value).length <= 0))
				return true;
			if ((value instanceof Collection) && ((Collection) value).size() <= 0)
				return true;
			if ((value instanceof Dictionary) && ((Dictionary) value).size() <= 0)
				return true;
			return false;
		}	
		/**
		 * 
		 * @���ߣ�mlr
		 * @˵�������ɽ������Ŀ 
		 *       �ж϶����Ƿ�Ϊ��
		 * @ʱ�䣺2011-7-5����08:44:48
		 * @param o
		 * @return
		 */
		public static boolean isNULL(Object o) {
			if (o == null || o.toString().trim().equals(""))
				return true;
			return false;
		}
	}


