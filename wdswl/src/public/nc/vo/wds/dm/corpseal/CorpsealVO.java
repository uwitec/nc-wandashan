package nc.vo.wds.dm.corpseal;

import java.util.ArrayList;

import javax.swing.ImageIcon;

import nc.vo.pub.NullFieldException;
import nc.vo.pub.SuperVO;
import nc.vo.pub.ValidationException;
import nc.vo.pub.lang.UFDateTime;

public class CorpsealVO extends SuperVO{
    //��˾����
		public String pk_corp;
        public UFDateTime ts;
        public Integer dr;
        public String pk_wds_corpseal;//����
        public String pk_cumandoc;//���̹���id
        public String pk_cubasdoc;//���̻���id
        public ImageIcon corpseal;//��˾ͼ��
        //--δ����
        public String listname;
        public String custname;
        public String custcode;
        //--δ����
      
       
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
 * ��֤���������֮��������߼���ȷ��.
 *
 * ��������:2011-4-26
 * @exception nc.vo.pub.ValidationException �����֤ʧ��,�׳�
 * ValidationException,�Դ�����н���.
*/
public void validate() throws ValidationException {

	ArrayList errFields = new ArrayList(); // errFields record those null

                                                 // fields that cannot be null.
  		  // ����Ƿ�Ϊ������յ��ֶθ��˿�ֵ,�������Ҫ�޸��������ʾ��Ϣ:
   StringBuffer message = new StringBuffer();
	message.append("�����ֶβ���Ϊ��:");
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
 * <p>ȡ�ø�VO�����ֶ�.
 * <p>
 * ��������:2011-4-26
 * @return java.lang.String
 */
public java.lang.String getParentPKFieldName() {
 	 
	    return null;
	
}   

/**
 * <p>ȡ�ñ�����.
 * <p>
 * ��������:2011-4-26
 * @return java.lang.String
 */
public java.lang.String getPKFieldName() {
	  return "pk_wds_corpseal";
	}

/**
 * <p>���ر�����.
 * <p>
 * ��������:2011-4-26
 * @return java.lang.String
*/
public java.lang.String getTableName() {
			
	return "wds_corpseal";
}    

/**
 * ����Ĭ�Ϸ�ʽ����������.
 *
 * ��������:2011-4-26
 */
public CorpsealVO() {	
		   super();	
 }    

      
 /**
  * ������ֵ�������ʾ����.
  *
  * ��������:2011-4-26
  * @return java.lang.String ������ֵ�������ʾ����.
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
