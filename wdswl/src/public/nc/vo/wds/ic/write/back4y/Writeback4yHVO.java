  /***************************************************************\
  *     The skeleton of this class is generated by an automatic *
  * code generator for NC product. It is based on Velocity.     *
  \***************************************************************/
      	package nc.vo.wds.ic.write.back4y;
   	
	import java.util.ArrayList;
	import nc.vo.pub.*;
import nc.vo.pub.lang.*;
	
/**
 * <b> 在此处简要描述此类的功能 </b>
 *
 * <p>
 *     在此处添加此类的描述信息
 * </p>
 *
 * 创建日期:2011-11-3
 * @author author
 * @version Your Project 1.0
 */
     public class Writeback4yHVO extends SuperVO {
           
             /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	        //预留字段
             public String reserve5;
             public String reserve4;
             public String reserve1;
             public UFDate reserve12;
             public UFDate reserve11;
             public UFDouble reserve9;
             public UFDouble reserve8;
             public UFBoolean reserve15;
             public String reserve2;
             public String reserve6;
             public String reserve3;
             public UFBoolean reserve14;
             public UFBoolean reserve16;
             public String reserve7;
             public UFDate reserve13;
             public UFDouble reserve10;
           //自定义项
             public String vdef1;
             public String vdef2;
             public String vdef3;
             public String vdef4;
             public String vdef7;
             public String vdef8;
             public String vdef9;
             public String vdef6;
             public String vdef10;
             public String vdef5;
             public UFDateTime ts;
             public Integer dr;
             //备注
             public String vmemo;  
        	 //单据号
        	 public String vbillno;//保存调拨入库单号
        	 //单据日期
        	 public UFDate dbilldate;
        	 //单据类型
        	 public String pk_billtype;
        	 //单据状态
        	 public Integer vbillstatus;
        	 //业务类型
        	 public String pk_busitype;
        	//业务员id
        	 public String vemployeeid;
        	 //部门id
        	 public String pk_deptdoc;  
        	 //制单人
        	 public String voperatorid;
        	 //制单日期
        	 public UFDate dmakedate;         
        	 //审批人
        	 public String vapproveid;
        	 //审批日期
        	 public UFDate dapprovedate;
        	 //审批批语
        	 public String vapprovenote;
            //调拨入库主键
             public String cgeneralhid;
            //主表
             public String pk_wds_writeback4Y_h;
             //调出公司
        	 public String pk_corp;   
             //调出库存组织
             public String pk_calbody;
             //调出仓库
             public String cwarehouseid;
             //调入公司
             public String cothercorpid;
             //调入库存组织
             public String cothercalbodyid;
             //调入仓库
             public String cotherwhid;
    
        /**
	   * 属性pk_corp的Getter方法.
	   *
	   * 创建日期:2011-11-3
	   * @return String
	   */
	 public String getPk_corp() {
		 return pk_corp;
	  }   
	  
     /**
	   * 属性pk_corp的Setter方法.
	   *
	   * 创建日期:2011-11-3
	   * @param newPk_corp String
	   */
	public void setPk_corp(String newPk_corp) {
		
		pk_corp = newPk_corp;
	 } 	  
       
        /**
	   * 属性reserve5的Getter方法.
	   *
	   * 创建日期:2011-11-3
	   * @return String
	   */
	 public String getReserve5() {
		 return reserve5;
	  }   
	  
     /**
	   * 属性reserve5的Setter方法.
	   *
	   * 创建日期:2011-11-3
	   * @param newReserve5 String
	   */
	public void setReserve5(String newReserve5) {
		
		reserve5 = newReserve5;
	 } 	  
       
        /**
	   * 属性reserve4的Getter方法.
	   *
	   * 创建日期:2011-11-3
	   * @return String
	   */
	 public String getReserve4() {
		 return reserve4;
	  }   
	  
     /**
	   * 属性reserve4的Setter方法.
	   *
	   * 创建日期:2011-11-3
	   * @param newReserve4 String
	   */
	public void setReserve4(String newReserve4) {
		
		reserve4 = newReserve4;
	 } 	  
       
        /**
	   * 属性vdef4的Getter方法.
	   *
	   * 创建日期:2011-11-3
	   * @return String
	   */
	 public String getVdef4() {
		 return vdef4;
	  }   
	  
     /**
	   * 属性vdef4的Setter方法.
	   *
	   * 创建日期:2011-11-3
	   * @param newVdef4 String
	   */
	public void setVdef4(String newVdef4) {
		
		vdef4 = newVdef4;
	 } 	  
       
        /**
	   * 属性dmakedate的Getter方法.
	   *
	   * 创建日期:2011-11-3
	   * @return UFDate
	   */
	 public UFDate getDmakedate() {
		 return dmakedate;
	  }   
	  
     /**
	   * 属性dmakedate的Setter方法.
	   *
	   * 创建日期:2011-11-3
	   * @param newDmakedate UFDate
	   */
	public void setDmakedate(UFDate newDmakedate) {
		
		dmakedate = newDmakedate;
	 } 	  
       
        /**
	   * 属性vdef7的Getter方法.
	   *
	   * 创建日期:2011-11-3
	   * @return String
	   */
	 public String getVdef7() {
		 return vdef7;
	  }   
	  
     /**
	   * 属性vdef7的Setter方法.
	   *
	   * 创建日期:2011-11-3
	   * @param newVdef7 String
	   */
	public void setVdef7(String newVdef7) {
		
		vdef7 = newVdef7;
	 } 	  
       
        /**
	   * 属性dr的Getter方法.
	   *
	   * 创建日期:2011-11-3
	   * @return Integer
	   */
	 public Integer getDr() {
		 return dr;
	  }   
	  
     /**
	   * 属性dr的Setter方法.
	   *
	   * 创建日期:2011-11-3
	   * @param newDr Integer
	   */
	public void setDr(Integer newDr) {
		
		dr = newDr;
	 } 	  
       
        /**
	   * 属性voperatorid的Getter方法.
	   *
	   * 创建日期:2011-11-3
	   * @return String
	   */
	 public String getVoperatorid() {
		 return voperatorid;
	  }   
	  
     /**
	   * 属性voperatorid的Setter方法.
	   *
	   * 创建日期:2011-11-3
	   * @param newVoperatorid String
	   */
	public void setVoperatorid(String newVoperatorid) {
		
		voperatorid = newVoperatorid;
	 } 	  
       
        /**
	   * 属性vapprovenote的Getter方法.
	   *
	   * 创建日期:2011-11-3
	   * @return String
	   */
	 public String getVapprovenote() {
		 return vapprovenote;
	  }   
	  
     /**
	   * 属性vapprovenote的Setter方法.
	   *
	   * 创建日期:2011-11-3
	   * @param newVapprovenote String
	   */
	public void setVapprovenote(String newVapprovenote) {
		
		vapprovenote = newVapprovenote;
	 } 	  
       
        /**
	   * 属性pk_billtype的Getter方法.
	   *
	   * 创建日期:2011-11-3
	   * @return String
	   */
	 public String getPk_billtype() {
		 return pk_billtype;
	  }   
	  
     /**
	   * 属性pk_billtype的Setter方法.
	   *
	   * 创建日期:2011-11-3
	   * @param newPk_billtype String
	   */
	public void setPk_billtype(String newPk_billtype) {
		
		pk_billtype = newPk_billtype;
	 } 	  
       
        /**
	   * 属性vbillstatus的Getter方法.
	   *
	   * 创建日期:2011-11-3
	   * @return Integer
	   */
	 public Integer getVbillstatus() {
		 return vbillstatus;
	  }   
	  
     /**
	   * 属性vbillstatus的Setter方法.
	   *
	   * 创建日期:2011-11-3
	   * @param newVbillstatus Integer
	   */
	public void setVbillstatus(Integer newVbillstatus) {
		
		vbillstatus = newVbillstatus;
	 } 	  
       
        /**
	   * 属性vemployeeid的Getter方法.
	   *
	   * 创建日期:2011-11-3
	   * @return String
	   */
	 public String getVemployeeid() {
		 return vemployeeid;
	  }   
	  
     /**
	   * 属性vemployeeid的Setter方法.
	   *
	   * 创建日期:2011-11-3
	   * @param newVemployeeid String
	   */
	public void setVemployeeid(String newVemployeeid) {
		
		vemployeeid = newVemployeeid;
	 } 	  
       
        /**
	   * 属性vdef2的Getter方法.
	   *
	   * 创建日期:2011-11-3
	   * @return String
	   */
	 public String getVdef2() {
		 return vdef2;
	  }   
	  
     /**
	   * 属性vdef2的Setter方法.
	   *
	   * 创建日期:2011-11-3
	   * @param newVdef2 String
	   */
	public void setVdef2(String newVdef2) {
		
		vdef2 = newVdef2;
	 } 	  
       
        /**
	   * 属性reserve12的Getter方法.
	   *
	   * 创建日期:2011-11-3
	   * @return UFDate
	   */
	 public UFDate getReserve12() {
		 return reserve12;
	  }   
	  
     /**
	   * 属性reserve12的Setter方法.
	   *
	   * 创建日期:2011-11-3
	   * @param newReserve12 UFDate
	   */
	public void setReserve12(UFDate newReserve12) {
		
		reserve12 = newReserve12;
	 } 	  
       
        /**
	   * 属性reserve11的Getter方法.
	   *
	   * 创建日期:2011-11-3
	   * @return UFDate
	   */
	 public UFDate getReserve11() {
		 return reserve11;
	  }   
	  
     /**
	   * 属性reserve11的Setter方法.
	   *
	   * 创建日期:2011-11-3
	   * @param newReserve11 UFDate
	   */
	public void setReserve11(UFDate newReserve11) {
		
		reserve11 = newReserve11;
	 } 	  
       
        /**
	   * 属性vmemo的Getter方法.
	   *
	   * 创建日期:2011-11-3
	   * @return String
	   */
	 public String getVmemo() {
		 return vmemo;
	  }   
	  
     /**
	   * 属性vmemo的Setter方法.
	   *
	   * 创建日期:2011-11-3
	   * @param newVmemo String
	   */
	public void setVmemo(String newVmemo) {
		
		vmemo = newVmemo;
	 } 	  
       
        /**
	   * 属性ts的Getter方法.
	   *
	   * 创建日期:2011-11-3
	   * @return UFDateTime
	   */
	 public UFDateTime getTs() {
		 return ts;
	  }   
	  
     /**
	   * 属性ts的Setter方法.
	   *
	   * 创建日期:2011-11-3
	   * @param newTs UFDateTime
	   */
	public void setTs(UFDateTime newTs) {
		
		ts = newTs;
	 } 	  
       
        /**
	   * 属性pk_busitype的Getter方法.
	   *
	   * 创建日期:2011-11-3
	   * @return String
	   */
	 public String getPk_busitype() {
		 return pk_busitype;
	  }   
	  
     /**
	   * 属性pk_busitype的Setter方法.
	   *
	   * 创建日期:2011-11-3
	   * @param newPk_busitype String
	   */
	public void setPk_busitype(String newPk_busitype) {
		
		pk_busitype = newPk_busitype;
	 } 	  
       
        /**
	   * 属性reserve6的Getter方法.
	   *
	   * 创建日期:2011-11-3
	   * @return String
	   */
	 public String getReserve6() {
		 return reserve6;
	  }   
	  
     /**
	   * 属性reserve6的Setter方法.
	   *
	   * 创建日期:2011-11-3
	   * @param newReserve6 String
	   */
	public void setReserve6(String newReserve6) {
		
		reserve6 = newReserve6;
	 } 	  
       
        /**
	   * 属性vdef1的Getter方法.
	   *
	   * 创建日期:2011-11-3
	   * @return String
	   */
	 public String getVdef1() {
		 return vdef1;
	  }   
	  
     /**
	   * 属性vdef1的Setter方法.
	   *
	   * 创建日期:2011-11-3
	   * @param newVdef1 String
	   */
	public void setVdef1(String newVdef1) {
		
		vdef1 = newVdef1;
	 } 	  
       
        /**
	   * 属性reserve3的Getter方法.
	   *
	   * 创建日期:2011-11-3
	   * @return String
	   */
	 public String getReserve3() {
		 return reserve3;
	  }   
	  
     /**
	   * 属性reserve3的Setter方法.
	   *
	   * 创建日期:2011-11-3
	   * @param newReserve3 String
	   */
	public void setReserve3(String newReserve3) {
		
		reserve3 = newReserve3;
	 } 	  
       
        /**
	   * 属性reserve14的Getter方法.
	   *
	   * 创建日期:2011-11-3
	   * @return UFBoolean
	   */
	 public UFBoolean getReserve14() {
		 return reserve14;
	  }   
	  
     /**
	   * 属性reserve14的Setter方法.
	   *
	   * 创建日期:2011-11-3
	   * @param newReserve14 UFBoolean
	   */
	public void setReserve14(UFBoolean newReserve14) {
		
		reserve14 = newReserve14;
	 } 	  
       
        /**
	   * 属性dbilldate的Getter方法.
	   *
	   * 创建日期:2011-11-3
	   * @return UFDate
	   */
	 public UFDate getDbilldate() {
		 return dbilldate;
	  }   
	  
     /**
	   * 属性dbilldate的Setter方法.
	   *
	   * 创建日期:2011-11-3
	   * @param newDbilldate UFDate
	   */
	public void setDbilldate(UFDate newDbilldate) {
		
		dbilldate = newDbilldate;
	 } 	  
       
        /**
	   * 属性vbillno的Getter方法.
	   *
	   * 创建日期:2011-11-3
	   * @return String
	   */
	 public String getVbillno() {
		 return vbillno;
	  }   
	  
     /**
	   * 属性vbillno的Setter方法.
	   *
	   * 创建日期:2011-11-3
	   * @param newVbillno String
	   */
	public void setVbillno(String newVbillno) {
		
		vbillno = newVbillno;
	 } 	  
       
        /**
	   * 属性reserve13的Getter方法.
	   *
	   * 创建日期:2011-11-3
	   * @return UFDate
	   */
	 public UFDate getReserve13() {
		 return reserve13;
	  }   
	  
     /**
	   * 属性reserve13的Setter方法.
	   *
	   * 创建日期:2011-11-3
	   * @param newReserve13 UFDate
	   */
	public void setReserve13(UFDate newReserve13) {
		
		reserve13 = newReserve13;
	 } 	  
       
        /**
	   * 属性reserve10的Getter方法.
	   *
	   * 创建日期:2011-11-3
	   * @return UFDouble
	   */
	 public UFDouble getReserve10() {
		 return reserve10;
	  }   
	  
     /**
	   * 属性reserve10的Setter方法.
	   *
	   * 创建日期:2011-11-3
	   * @param newReserve10 UFDouble
	   */
	public void setReserve10(UFDouble newReserve10) {
		
		reserve10 = newReserve10;
	 } 	  
       
        /**
	   * 属性cgeneralhid的Getter方法.
	   *
	   * 创建日期:2011-11-3
	   * @return String
	   */
	 public String getCgeneralhid() {
		 return cgeneralhid;
	  }   
	  
     /**
	   * 属性cgeneralhid的Setter方法.
	   *
	   * 创建日期:2011-11-3
	   * @param newCgeneralhid String
	   */
	public void setCgeneralhid(String newCgeneralhid) {
		
		cgeneralhid = newCgeneralhid;
	 } 	  
       
        /**
	   * 属性pk_deptdoc的Getter方法.
	   *
	   * 创建日期:2011-11-3
	   * @return String
	   */
	 public String getPk_deptdoc() {
		 return pk_deptdoc;
	  }   
	  
     /**
	   * 属性pk_deptdoc的Setter方法.
	   *
	   * 创建日期:2011-11-3
	   * @param newPk_deptdoc String
	   */
	public void setPk_deptdoc(String newPk_deptdoc) {
		
		pk_deptdoc = newPk_deptdoc;
	 } 	  
       
        /**
	   * 属性reserve9的Getter方法.
	   *
	   * 创建日期:2011-11-3
	   * @return UFDouble
	   */
	 public UFDouble getReserve9() {
		 return reserve9;
	  }   
	  
     /**
	   * 属性reserve9的Setter方法.
	   *
	   * 创建日期:2011-11-3
	   * @param newReserve9 UFDouble
	   */
	public void setReserve9(UFDouble newReserve9) {
		
		reserve9 = newReserve9;
	 } 	  
       
        /**
	   * 属性reserve8的Getter方法.
	   *
	   * 创建日期:2011-11-3
	   * @return UFDouble
	   */
	 public UFDouble getReserve8() {
		 return reserve8;
	  }   
	  
     /**
	   * 属性reserve8的Setter方法.
	   *
	   * 创建日期:2011-11-3
	   * @param newReserve8 UFDouble
	   */
	public void setReserve8(UFDouble newReserve8) {
		
		reserve8 = newReserve8;
	 } 	  
       
        /**
	   * 属性vdef3的Getter方法.
	   *
	   * 创建日期:2011-11-3
	   * @return String
	   */
	 public String getVdef3() {
		 return vdef3;
	  }   
	  
     /**
	   * 属性vdef3的Setter方法.
	   *
	   * 创建日期:2011-11-3
	   * @param newVdef3 String
	   */
	public void setVdef3(String newVdef3) {
		
		vdef3 = newVdef3;
	 } 	  
       
        /**
	   * 属性vdef9的Getter方法.
	   *
	   * 创建日期:2011-11-3
	   * @return String
	   */
	 public String getVdef9() {
		 return vdef9;
	  }   
	  
     /**
	   * 属性vdef9的Setter方法.
	   *
	   * 创建日期:2011-11-3
	   * @param newVdef9 String
	   */
	public void setVdef9(String newVdef9) {
		
		vdef9 = newVdef9;
	 } 	  
       
        /**
	   * 属性reserve1的Getter方法.
	   *
	   * 创建日期:2011-11-3
	   * @return String
	   */
	 public String getReserve1() {
		 return reserve1;
	  }   
	  
     /**
	   * 属性reserve1的Setter方法.
	   *
	   * 创建日期:2011-11-3
	   * @param newReserve1 String
	   */
	public void setReserve1(String newReserve1) {
		
		reserve1 = newReserve1;
	 } 	  
       
        /**
	   * 属性dapprovedate的Getter方法.
	   *
	   * 创建日期:2011-11-3
	   * @return UFDate
	   */
	 public UFDate getDapprovedate() {
		 return dapprovedate;
	  }   
	  
     /**
	   * 属性dapprovedate的Setter方法.
	   *
	   * 创建日期:2011-11-3
	   * @param newDapprovedate UFDate
	   */
	public void setDapprovedate(UFDate newDapprovedate) {
		
		dapprovedate = newDapprovedate;
	 } 	  
       
        /**
	   * 属性vdef8的Getter方法.
	   *
	   * 创建日期:2011-11-3
	   * @return String
	   */
	 public String getVdef8() {
		 return vdef8;
	  }   
	  
     /**
	   * 属性vdef8的Setter方法.
	   *
	   * 创建日期:2011-11-3
	   * @param newVdef8 String
	   */
	public void setVdef8(String newVdef8) {
		
		vdef8 = newVdef8;
	 } 	  
       
        /**
	   * 属性reserve16的Getter方法.
	   *
	   * 创建日期:2011-11-3
	   * @return UFBoolean
	   */
	 public UFBoolean getReserve16() {
		 return reserve16;
	  }   
	  
     /**
	   * 属性reserve16的Setter方法.
	   *
	   * 创建日期:2011-11-3
	   * @param newReserve16 UFBoolean
	   */
	public void setReserve16(UFBoolean newReserve16) {
		
		reserve16 = newReserve16;
	 } 	  
       
        /**
	   * 属性reserve7的Getter方法.
	   *
	   * 创建日期:2011-11-3
	   * @return String
	   */
	 public String getReserve7() {
		 return reserve7;
	  }   
	  
     /**
	   * 属性reserve7的Setter方法.
	   *
	   * 创建日期:2011-11-3
	   * @param newReserve7 String
	   */
	public void setReserve7(String newReserve7) {
		
		reserve7 = newReserve7;
	 } 	  
       
        /**
	   * 属性vapproveid的Getter方法.
	   *
	   * 创建日期:2011-11-3
	   * @return String
	   */
	 public String getVapproveid() {
		 return vapproveid;
	  }   
	  
     /**
	   * 属性vapproveid的Setter方法.
	   *
	   * 创建日期:2011-11-3
	   * @param newVapproveid String
	   */
	public void setVapproveid(String newVapproveid) {
		
		vapproveid = newVapproveid;
	 } 	  
       
        /**
	   * 属性reserve15的Getter方法.
	   *
	   * 创建日期:2011-11-3
	   * @return UFBoolean
	   */
	 public UFBoolean getReserve15() {
		 return reserve15;
	  }   
	  
     /**
	   * 属性reserve15的Setter方法.
	   *
	   * 创建日期:2011-11-3
	   * @param newReserve15 UFBoolean
	   */
	public void setReserve15(UFBoolean newReserve15) {
		
		reserve15 = newReserve15;
	 } 	  
       
        /**
	   * 属性reserve2的Getter方法.
	   *
	   * 创建日期:2011-11-3
	   * @return String
	   */
	 public String getReserve2() {
		 return reserve2;
	  }   
	  
     /**
	   * 属性reserve2的Setter方法.
	   *
	   * 创建日期:2011-11-3
	   * @param newReserve2 String
	   */
	public void setReserve2(String newReserve2) {
		
		reserve2 = newReserve2;
	 } 	  
       
        /**
	   * 属性vdef6的Getter方法.
	   *
	   * 创建日期:2011-11-3
	   * @return String
	   */
	 public String getVdef6() {
		 return vdef6;
	  }   
	  
     /**
	   * 属性vdef6的Setter方法.
	   *
	   * 创建日期:2011-11-3
	   * @param newVdef6 String
	   */
	public void setVdef6(String newVdef6) {
		
		vdef6 = newVdef6;
	 } 	  
       
        /**
	   * 属性vdef10的Getter方法.
	   *
	   * 创建日期:2011-11-3
	   * @return String
	   */
	 public String getVdef10() {
		 return vdef10;
	  }   
	  
     /**
	   * 属性vdef10的Setter方法.
	   *
	   * 创建日期:2011-11-3
	   * @param newVdef10 String
	   */
	public void setVdef10(String newVdef10) {
		
		vdef10 = newVdef10;
	 } 	  
       
        /**
	   * 属性vdef5的Getter方法.
	   *
	   * 创建日期:2011-11-3
	   * @return String
	   */
	 public String getVdef5() {
		 return vdef5;
	  }   
	  
     /**
	   * 属性vdef5的Setter方法.
	   *
	   * 创建日期:2011-11-3
	   * @param newVdef5 String
	   */
	public void setVdef5(String newVdef5) {
		
		vdef5 = newVdef5;
	 } 	  
       
        /**
	   * 属性pk_wds_writeback4Y_h的Getter方法.
	   *
	   * 创建日期:2011-11-3
	   * @return String
	   */
	 public String getPk_wds_writeback4Y_h() {
		 return pk_wds_writeback4Y_h;
	  }   
	  
     /**
	   * 属性pk_wds_writeback4Y_h的Setter方法.
	   *
	   * 创建日期:2011-11-3
	   * @param newPk_wds_writeback4Y_h String
	   */
	public void setPk_wds_writeback4Y_h(String newPk_wds_writeback4Y_h) {
		
		pk_wds_writeback4Y_h = newPk_wds_writeback4Y_h;
	 } 	  
       
       
    /**
	  * 验证对象各属性之间的数据逻辑正确性.
	  *
	  * 创建日期:2011-11-3
	  * @exception nc.vo.pub.ValidationException 如果验证失败,抛出
	  * ValidationException,对错误进行解释.
	 */
	 public void validate() throws ValidationException {
	
	 	ArrayList errFields = new ArrayList(); // errFields record those null

                                                      // fields that cannot be null.
       		  // 检查是否为不允许空的字段赋了空值,你可能需要修改下面的提示信息:
	
	   		if (pk_wds_writeback4Y_h == null) {
			errFields.add(new String("pk_wds_writeback4Y_h"));
			  }	
	   	
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
	  * 创建日期:2011-11-3
	  * @return java.lang.String
	  */
	public java.lang.String getParentPKFieldName() {
	  	 
	 	    return null;
	 	
	}   
    
    /**
	  * <p>取得表主键.
	  * <p>
	  * 创建日期:2011-11-3
	  * @return java.lang.String
	  */
	public java.lang.String getPKFieldName() {
	 	  return "pk_wds_writeback4Y_h";
	 	}
    
	/**
      * <p>返回表名称.
	  * <p>
	  * 创建日期:2011-11-3
	  * @return java.lang.String
	 */
	public java.lang.String getTableName() {
				
		return "wds_writeback4y_h";
	}    
    
    /**
	  * 按照默认方式创建构造子.
	  *
	  * 创建日期:2011-11-3
	  */
	public Writeback4yHVO() {
			
			   super();	
	  }    
    
            /**
	 * 使用主键进行初始化的构造子.
	 *
	 * 创建日期:2011-11-3
	 * @param newPk_wds_writeback4Y_h 主键值
	 */
	 public Writeback4yHVO(String newPk_wds_writeback4Y_h) {
		
		// 为主键字段赋值:
		 pk_wds_writeback4Y_h = newPk_wds_writeback4Y_h;
	
    	}
    
     
     /**
	  * 返回对象标识,用来唯一定位对象.
	  *
	  * 创建日期:2011-11-3
	  * @return String
	  */
	   public String getPrimaryKey() {
				
		 return pk_wds_writeback4Y_h;
	   
	   }

     /**
	  * 设置对象标识,用来唯一定位对象.
	  *
	  * 创建日期:2011-11-3
	  * @param newPk_wds_writeback4Y_h  String    
	  */
	 public void setPrimaryKey(String newPk_wds_writeback4Y_h) {
				
				pk_wds_writeback4Y_h = newPk_wds_writeback4Y_h; 
				
	 } 
           
	  /**
       * 返回数值对象的显示名称.
	   *
	   * 创建日期:2011-11-3
	   * @return java.lang.String 返回数值对象的显示名称.
	   */
	 public String getEntityName() {
				
	   return "wds_writeback4y_h"; 
				
	 }

	public String getPk_calbody() {
		return pk_calbody;
	}

	public void setPk_calbody(String pk_calbody) {
		this.pk_calbody = pk_calbody;
	}

	public String getCwarehouseid() {
		return cwarehouseid;
	}

	public void setCwarehouseid(String cwarehouseid) {
		this.cwarehouseid = cwarehouseid;
	}

	public String getCothercorpid() {
		return cothercorpid;
	}

	public void setCothercorpid(String cothercorpid) {
		this.cothercorpid = cothercorpid;
	}

	public String getCothercalbodyid() {
		return cothercalbodyid;
	}

	public void setCothercalbodyid(String cothercalbodyid) {
		this.cothercalbodyid = cothercalbodyid;
	}

	public String getCotherwhid() {
		return cotherwhid;
	}

	public void setCotherwhid(String cotherwhid) {
		this.cotherwhid = cotherwhid;
	} 
} 
