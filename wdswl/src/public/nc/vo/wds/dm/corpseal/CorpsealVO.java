package nc.vo.wds.dm.corpseal;

import java.util.ArrayList;

import javax.swing.ImageIcon;

import nc.vo.pub.NullFieldException;
import nc.vo.pub.SuperVO;
import nc.vo.pub.ValidationException;
import nc.vo.pub.lang.UFDateTime;

public class CorpsealVO extends SuperVO{
    //公司主键
		public String pk_corp;
        public UFDateTime ts;
        public Integer dr;
        public String pk_wds_corpseal;//主键
        public String pk_cumandoc;//客商管理id
        public String pk_cubasdoc;//客商基本id
        public ImageIcon corpseal;//公司图章
        //--未存库表
        public String listname;
        public String custname;
        public String custcode;
        //--未存库表
      
       
		public String getListname() {
			return listname;
		}

		public void setListname(String listname) {
			this.listname = listname;
		}

		public String getCustcode() {
			return custcode;
		}

		public void setCustcode(String custcode) {
			this.custcode = custcode;
		}

		public String getPk_wds_corpseal() {
			return pk_wds_corpseal;
		}

		public void setPk_wds_corpseal(String pk_wds_corpseal) {
			this.pk_wds_corpseal = pk_wds_corpseal;
		}

		

		public String getPk_cumandoc() {
			return pk_cumandoc;
		}

		public void setPk_cumandoc(String pk_cumandoc) {
			this.pk_cumandoc = pk_cumandoc;
		}

		public String getPk_cubasdoc() {
			return pk_cubasdoc;
		}

		public void setPk_cubasdoc(String pk_cubasdoc) {
			this.pk_cubasdoc = pk_cubasdoc;
		}

		public ImageIcon getCorpseal() {
			return corpseal;
		}

		public void setCorpseal(ImageIcon corpseal) {
			this.corpseal = corpseal;
		}

		public UFDateTime getTs() {
			return ts;
		}

		public void setTs(UFDateTime ts) {
			this.ts = ts;
		}

		public Integer getDr() {
			return dr;
		}

		public void setDr(Integer dr) {
			this.dr = dr;
		}
  
  
/**
 * 验证对象各属性之间的数据逻辑正确性.
 *
 * 创建日期:2011-4-26
 * @exception nc.vo.pub.ValidationException 如果验证失败,抛出
 * ValidationException,对错误进行解释.
*/
public void validate() throws ValidationException {

	ArrayList errFields = new ArrayList(); // errFields record those null

                                                 // fields that cannot be null.
  		  // 检查是否为不允许空的字段赋了空值,你可能需要修改下面的提示信息:
   StringBuffer message = new StringBuffer();
	message.append("下列字段不能为空:");
	if (errFields.size() > 0) {
	String[] temp = (String[]) errFields.toArray(new String[0]);
	message.append(temp[0]);
	for ( int i= 1; i < temp.length; i++ ) {
		message.append(",");
		message.append(temp[i]);
	}
	throw new NullFieldException(message.toString());
	}
}
		   
  
	/**
 * <p>取得父VO主键字段.
 * <p>
 * 创建日期:2011-4-26
 * @return java.lang.String
 */
public java.lang.String getParentPKFieldName() {
 	 
	    return null;
	
}   

/**
 * <p>取得表主键.
 * <p>
 * 创建日期:2011-4-26
 * @return java.lang.String
 */
public java.lang.String getPKFieldName() {
	  return "pk_wds_corpseal";
	}

/**
 * <p>返回表名称.
 * <p>
 * 创建日期:2011-4-26
 * @return java.lang.String
*/
public java.lang.String getTableName() {
			
	return "wds_corpseal";
}    

/**
 * 按照默认方式创建构造子.
 *
 * 创建日期:2011-4-26
 */
public CorpsealVO() {	
		   super();	
 }    

      
 /**
  * 返回数值对象的显示名称.
  *
  * 创建日期:2011-4-26
  * @return java.lang.String 返回数值对象的显示名称.
  */
public String getEntityName() {
			
  return "wds_corpseal"; 
			
} 

 public String getPrimaryKey() {
		
   return pk_wds_corpseal;
	   
}

public void setPk_corp(String pk_corp) {
	this.pk_corp = pk_corp;
}

public String getPk_corp() {
	return pk_corp;
}

public String getCustname() {
	return custname;
}

public void setCustname(String custname) {
	this.custname = custname;
}

}
