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
	 * 该类为保存前的 后台校验类 
	 * 
	 * */
	public  class BsBeforeSaveValudate{
		
	/**
	 * 	
	 * @作者：mlr
	 * @说明：完达山物流项目 
	 *        表体的几个字段构成唯一性，每一行的记录的几个字段构成行唯一性记录，与其它行比较 ，进行唯一性校验
	 * @时间：2011-7-5下午07:26:48
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
					throw new BusinessException("第[" + (i + 1) + "]行表体字段 "+ dis + " 存在重复!");
				} else {
					list.add(key);
				}
			}

		}
	   }
	/**
	 * 
	 * @作者：mlr
	 * @说明：完达山物流项目 
	 *        表体不允许为空的校验
	 * @时间：2011-7-5下午08:37:54
	 * @param vos
	 * @throws Exception
	 */
		public static void BodyNotNULL(CircularlyAccessibleValueObject[] vos) throws Exception{
			if(vos==null || vos.length==0){
				throw new Exception("表体不允许为空");
			}
		}
		/**
		 * 
		 * @作者：mlr
		 * @说明：完达山物流项目 
		 *        字段不为空校验(只校验一个字段)
		 * @时间：2011-7-5下午08:41:08
		 * @param bodys
		 * @param checkField
		 * @param displayName
		 * @throws Exception
		 */
		public static void bodyFieldNotNull(CircularlyAccessibleValueObject[] bodys,String checkField,String displayName)throws Exception{
			
			  for(int i=0;i<bodys.length;i++){
	              if(isEmpty(bodys[i].getAttributeValue(checkField))){            	  
	            	  throw new Exception("表体第"+(i+1)+"行"+displayName+"不能为空");
	              }
	          }    	
		}		
		/**
		 * 	
		 * @作者：mlr
		 * @说明：完达山物流项目 
		 *        字段不为空的校验 
		 *        类似前台的必输项校验
		 * 
		 * @时间：2011-7-5下午04:59:49
		 * @param vos 要校验的vos
		 * @param fields 要校验的字段数组
		 * @param fields 要用来错误提示的字段数组
		 * @throws BusinessException
		 */
		public static void FieldNotNull(SuperVO[] vos,String[] fields,String[] names)throws BusinessException{
			if(isEmpty(vos) || isEmpty(fields)|| isEmpty(names)){
				return;
			}
			if(fields.length !=names.length){
				throw new BusinessException("校验字段和显示字段数组长度不一致");
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
      * @作者：mlr
      * @说明：完达山物流项目 
      *       判断对象是否为空
      * @时间：2011-7-5下午08:43:29
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
		 * @作者：mlr
		 * @说明：完达山物流项目 
		 *       判断对象是否为空
		 * @时间：2011-7-5下午08:44:48
		 * @param o
		 * @return
		 */
		public static boolean isNULL(Object o) {
			if (o == null || o.toString().trim().equals(""))
				return true;
			return false;
		}
	}


