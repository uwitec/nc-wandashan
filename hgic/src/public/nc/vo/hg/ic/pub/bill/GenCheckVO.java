/***************************************************************\
 *     The skeleton of this class is generated by an automatic *
 * code generator for NC product. It is based on Velocity.     *
\***************************************************************/
package nc.vo.hg.ic.pub.bill;
	
import nc.vo.pub.*;
import nc.vo.pub.lang.*;
	
/**
 * <b>  </b>
 * <p>
 *     验收信息VO
 * </p>
 * 创建日期:2011-12-06 17:00:03
 * @author zhw
 * @version NCPrj 1.0
 */
@SuppressWarnings("serial")
public class GenCheckVO extends SuperVO {
		private String vqualityproblem;
		private UFDateTime ts;
		private UFBoolean fisdeal;
		private String vproblemcause;
		private UFDate dcheckdate;
		private String cdealid;
		private String ccheckid;
		private UFDouble dr;
		private UFDate ddealdate;
		private UFBoolean fisqualified;
		private String pk_icgencheck;

		public static final String VQUALITYPROBLEM = "vqualityproblem";
		public static final String FISDEAL = "fisdeal";
		public static final String VPROBLEMCAUSE = "vproblemcause";
		public static final String DCHECKDATE = "dcheckdate";
		public static final String CDEALID = "cdealid";
		public static final String CCHECKID = "ccheckid";
		public static final String DDEALDATE = "ddealdate";
		public static final String FISQUALIFIED = "fisqualified";
		public static final String PK_ICGENCHECK = "pk_icgencheck";
				
		/**
		 * 属性vqualityproblem的Getter方法.
		 * 创建日期:2011-12-06 17:11:19
		 * @return String
		 */
		public String getVqualityproblem () {
			return vqualityproblem;
		}   
		/**
		 * 属性vqualityproblem的Setter方法.
		 * 创建日期:2011-12-06 17:11:19
		 * @param newVqualityproblem String
		 */
		public void setVqualityproblem (String newVqualityproblem ) {
		 	this.vqualityproblem = newVqualityproblem;
		} 	  
		/**
		 * 属性ts的Getter方法.
		 * 创建日期:2011-12-06 17:11:19
		 * @return UFDateTime
		 */
		public UFDateTime getTs () {
			return ts;
		}   
		/**
		 * 属性ts的Setter方法.
		 * 创建日期:2011-12-06 17:11:19
		 * @param newTs UFDateTime
		 */
		public void setTs (UFDateTime newTs ) {
		 	this.ts = newTs;
		} 	  
		/**
		 * 属性fisdeal的Getter方法.
		 * 创建日期:2011-12-06 17:11:19
		 * @return UFBoolean
		 */
		public UFBoolean getFisdeal () {
			return fisdeal;
		}   
		/**
		 * 属性fisdeal的Setter方法.
		 * 创建日期:2011-12-06 17:11:19
		 * @param newFisdeal UFBoolean
		 */
		public void setFisdeal (UFBoolean newFisdeal ) {
		 	this.fisdeal = newFisdeal;
		} 	  
		/**
		 * 属性vproblemcause的Getter方法.
		 * 创建日期:2011-12-06 17:11:19
		 * @return String
		 */
		public String getVproblemcause () {
			return vproblemcause;
		}   
		/**
		 * 属性vproblemcause的Setter方法.
		 * 创建日期:2011-12-06 17:11:19
		 * @param newVproblemcause String
		 */
		public void setVproblemcause (String newVproblemcause ) {
		 	this.vproblemcause = newVproblemcause;
		} 	  
		/**
		 * 属性dcheckdate的Getter方法.
		 * 创建日期:2011-12-06 17:11:19
		 * @return UFDate
		 */
		public UFDate getDcheckdate () {
			return dcheckdate;
		}   
		/**
		 * 属性dcheckdate的Setter方法.
		 * 创建日期:2011-12-06 17:11:19
		 * @param newDcheckdate UFDate
		 */
		public void setDcheckdate (UFDate newDcheckdate ) {
		 	this.dcheckdate = newDcheckdate;
		} 	  
		/**
		 * 属性cdealid的Getter方法.
		 * 创建日期:2011-12-06 17:11:19
		 * @return String
		 */
		public String getCdealid () {
			return cdealid;
		}   
		/**
		 * 属性cdealid的Setter方法.
		 * 创建日期:2011-12-06 17:11:19
		 * @param newCdealid String
		 */
		public void setCdealid (String newCdealid ) {
		 	this.cdealid = newCdealid;
		} 	  
		/**
		 * 属性ccheckid的Getter方法.
		 * 创建日期:2011-12-06 17:11:19
		 * @return String
		 */
		public String getCcheckid () {
			return ccheckid;
		}   
		/**
		 * 属性ccheckid的Setter方法.
		 * 创建日期:2011-12-06 17:11:19
		 * @param newCcheckid String
		 */
		public void setCcheckid (String newCcheckid ) {
		 	this.ccheckid = newCcheckid;
		} 	  
		/**
		 * 属性dr的Getter方法.
		 * 创建日期:2011-12-06 17:11:19
		 * @return UFDouble
		 */
		public UFDouble getDr () {
			return dr;
		}   
		/**
		 * 属性dr的Setter方法.
		 * 创建日期:2011-12-06 17:11:19
		 * @param newDr UFDouble
		 */
		public void setDr (UFDouble newDr ) {
		 	this.dr = newDr;
		} 	  
		/**
		 * 属性ddealdate的Getter方法.
		 * 创建日期:2011-12-06 17:11:19
		 * @return UFDate
		 */
		public UFDate getDdealdate () {
			return ddealdate;
		}   
		/**
		 * 属性ddealdate的Setter方法.
		 * 创建日期:2011-12-06 17:11:19
		 * @param newDdealdate UFDate
		 */
		public void setDdealdate (UFDate newDdealdate ) {
		 	this.ddealdate = newDdealdate;
		} 	  
		/**
		 * 属性fisqualified的Getter方法.
		 * 创建日期:2011-12-06 17:11:19
		 * @return UFBoolean
		 */
		public UFBoolean getFisqualified () {
			return fisqualified;
		}   
		/**
		 * 属性fisqualified的Setter方法.
		 * 创建日期:2011-12-06 17:11:19
		 * @param newFisqualified UFBoolean
		 */
		public void setFisqualified (UFBoolean newFisqualified ) {
		 	this.fisqualified = newFisqualified;
		} 	  
		/**
		 * 属性pk_icgencheck的Getter方法.
		 * 创建日期:2011-12-06 17:11:19
		 * @return String
		 */
		public String getPk_icgencheck () {
			return pk_icgencheck;
		}   
		/**
		 * 属性pk_icgencheck的Setter方法.
		 * 创建日期:2011-12-06 17:11:19
		 * @param newPk_icgencheck String
		 */
		public void setPk_icgencheck (String newPk_icgencheck ) {
		 	this.pk_icgencheck = newPk_icgencheck;
		} 	  
	 
		/**
		  * <p>取得父VO主键字段.
		  * <p>
		  * 创建日期:2011-12-06 17:11:19
		  * @return java.lang.String
		  */
		public java.lang.String getParentPKFieldName() {
		    return null;
		}   
	    
		/**
		  * <p>取得表主键.
		  * <p>
		  * 创建日期:2011-12-06 17:11:19
		  * @return java.lang.String
		  */
		public java.lang.String getPKFieldName() {
		  return "pk_icgencheck";
		}
	    
		/**
		 * <p>返回表名称.
		 * <p>
		 * 创建日期:2011-12-06 17:11:19
		 * @return java.lang.String
		 */
		public java.lang.String getTableName() {
			return "IC_GENCHECK";
		}    
	    
	    /**
		  * 按照默认方式创建构造子.
		  *
		  * 创建日期:2011-12-06 17:11:19
		  */
	     public GenCheckVO() {
			super();	
		}    
	} 
