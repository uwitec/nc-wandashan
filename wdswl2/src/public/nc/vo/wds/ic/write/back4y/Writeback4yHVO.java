  /***************************************************************\
  *     The skeleton of this class is generated by an automatic *
  * code generator for NC product. It is based on Velocity.     *
  \***************************************************************/
      	package nc.vo.wds.ic.write.back4y;
   	
	import java.util.ArrayList;
	import nc.vo.pub.*;
import nc.vo.pub.lang.*;
	
/**
 * <b> �ڴ˴���Ҫ��������Ĺ��� </b>
 *
 * <p>
 *     �ڴ˴����Ӵ����������Ϣ
 * </p>
 *
 * ��������:2011-11-3
 * @author author
 * @version Your Project 1.0
 */
     public class Writeback4yHVO extends SuperVO {
           
             /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	        //Ԥ���ֶ�
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
           //�Զ�����
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
             //��ע
             public String vmemo;  
        	 //���ݺ�
        	 public String vbillno;//���������ⵥ��
        	 //��������
        	 public UFDate dbilldate;
        	 //��������
        	 public String pk_billtype;
        	 //����״̬
        	 public Integer vbillstatus;
        	 //ҵ������
        	 public String pk_busitype;
        	//ҵ��Աid
        	 public String vemployeeid;
        	 //����id
        	 public String pk_deptdoc;  
        	 //�Ƶ���
        	 public String voperatorid;
        	 //�Ƶ�����
        	 public UFDate dmakedate;         
        	 //������
        	 public String vapproveid;
        	 //��������
        	 public UFDate dapprovedate;
        	 //��������
        	 public String vapprovenote;
            //�����������
             public String cgeneralhid;
            //����
             public String pk_wds_writeback4Y_h;
             //������˾
        	 public String pk_corp;   
             //���������֯
             public String pk_calbody;
             //�����ֿ�
             public String cwarehouseid;
             //���빫˾
             public String cothercorpid;
             //��������֯
             public String cothercalbodyid;
             //����ֿ�
             public String cotherwhid;
             
             public UFBoolean fisvbatchcontorl;//�Ƿ����ι���
    
        public UFBoolean getFisvbatchcontorl() {
				return fisvbatchcontorl;
			}

			public void setFisvbatchcontorl(UFBoolean fisvbatchcontorl) {
				this.fisvbatchcontorl = fisvbatchcontorl;
			}

		/**
	   * ����pk_corp��Getter����.
	   *
	   * ��������:2011-11-3
	   * @return String
	   */
	 public String getPk_corp() {
		 return pk_corp;
	  }   
	  
     /**
	   * ����pk_corp��Setter����.
	   *
	   * ��������:2011-11-3
	   * @param newPk_corp String
	   */
	public void setPk_corp(String newPk_corp) {
		
		pk_corp = newPk_corp;
	 } 	  
       
        /**
	   * ����reserve5��Getter����.
	   *
	   * ��������:2011-11-3
	   * @return String
	   */
	 public String getReserve5() {
		 return reserve5;
	  }   
	  
     /**
	   * ����reserve5��Setter����.
	   *
	   * ��������:2011-11-3
	   * @param newReserve5 String
	   */
	public void setReserve5(String newReserve5) {
		
		reserve5 = newReserve5;
	 } 	  
       
        /**
	   * ����reserve4��Getter����.
	   *
	   * ��������:2011-11-3
	   * @return String
	   */
	 public String getReserve4() {
		 return reserve4;
	  }   
	  
     /**
	   * ����reserve4��Setter����.
	   *
	   * ��������:2011-11-3
	   * @param newReserve4 String
	   */
	public void setReserve4(String newReserve4) {
		
		reserve4 = newReserve4;
	 } 	  
       
        /**
	   * ����vdef4��Getter����.
	   *
	   * ��������:2011-11-3
	   * @return String
	   */
	 public String getVdef4() {
		 return vdef4;
	  }   
	  
     /**
	   * ����vdef4��Setter����.
	   *
	   * ��������:2011-11-3
	   * @param newVdef4 String
	   */
	public void setVdef4(String newVdef4) {
		
		vdef4 = newVdef4;
	 } 	  
       
        /**
	   * ����dmakedate��Getter����.
	   *
	   * ��������:2011-11-3
	   * @return UFDate
	   */
	 public UFDate getDmakedate() {
		 return dmakedate;
	  }   
	  
     /**
	   * ����dmakedate��Setter����.
	   *
	   * ��������:2011-11-3
	   * @param newDmakedate UFDate
	   */
	public void setDmakedate(UFDate newDmakedate) {
		
		dmakedate = newDmakedate;
	 } 	  
       
        /**
	   * ����vdef7��Getter����.
	   *
	   * ��������:2011-11-3
	   * @return String
	   */
	 public String getVdef7() {
		 return vdef7;
	  }   
	  
     /**
	   * ����vdef7��Setter����.
	   *
	   * ��������:2011-11-3
	   * @param newVdef7 String
	   */
	public void setVdef7(String newVdef7) {
		
		vdef7 = newVdef7;
	 } 	  
       
        /**
	   * ����dr��Getter����.
	   *
	   * ��������:2011-11-3
	   * @return Integer
	   */
	 public Integer getDr() {
		 return dr;
	  }   
	  
     /**
	   * ����dr��Setter����.
	   *
	   * ��������:2011-11-3
	   * @param newDr Integer
	   */
	public void setDr(Integer newDr) {
		
		dr = newDr;
	 } 	  
       
        /**
	   * ����voperatorid��Getter����.
	   *
	   * ��������:2011-11-3
	   * @return String
	   */
	 public String getVoperatorid() {
		 return voperatorid;
	  }   
	  
     /**
	   * ����voperatorid��Setter����.
	   *
	   * ��������:2011-11-3
	   * @param newVoperatorid String
	   */
	public void setVoperatorid(String newVoperatorid) {
		
		voperatorid = newVoperatorid;
	 } 	  
       
        /**
	   * ����vapprovenote��Getter����.
	   *
	   * ��������:2011-11-3
	   * @return String
	   */
	 public String getVapprovenote() {
		 return vapprovenote;
	  }   
	  
     /**
	   * ����vapprovenote��Setter����.
	   *
	   * ��������:2011-11-3
	   * @param newVapprovenote String
	   */
	public void setVapprovenote(String newVapprovenote) {
		
		vapprovenote = newVapprovenote;
	 } 	  
       
        /**
	   * ����pk_billtype��Getter����.
	   *
	   * ��������:2011-11-3
	   * @return String
	   */
	 public String getPk_billtype() {
		 return pk_billtype;
	  }   
	  
     /**
	   * ����pk_billtype��Setter����.
	   *
	   * ��������:2011-11-3
	   * @param newPk_billtype String
	   */
	public void setPk_billtype(String newPk_billtype) {
		
		pk_billtype = newPk_billtype;
	 } 	  
       
        /**
	   * ����vbillstatus��Getter����.
	   *
	   * ��������:2011-11-3
	   * @return Integer
	   */
	 public Integer getVbillstatus() {
		 return vbillstatus;
	  }   
	  
     /**
	   * ����vbillstatus��Setter����.
	   *
	   * ��������:2011-11-3
	   * @param newVbillstatus Integer
	   */
	public void setVbillstatus(Integer newVbillstatus) {
		
		vbillstatus = newVbillstatus;
	 } 	  
       
        /**
	   * ����vemployeeid��Getter����.
	   *
	   * ��������:2011-11-3
	   * @return String
	   */
	 public String getVemployeeid() {
		 return vemployeeid;
	  }   
	  
     /**
	   * ����vemployeeid��Setter����.
	   *
	   * ��������:2011-11-3
	   * @param newVemployeeid String
	   */
	public void setVemployeeid(String newVemployeeid) {
		
		vemployeeid = newVemployeeid;
	 } 	  
       
        /**
	   * ����vdef2��Getter����.
	   *
	   * ��������:2011-11-3
	   * @return String
	   */
	 public String getVdef2() {
		 return vdef2;
	  }   
	  
     /**
	   * ����vdef2��Setter����.
	   *
	   * ��������:2011-11-3
	   * @param newVdef2 String
	   */
	public void setVdef2(String newVdef2) {
		
		vdef2 = newVdef2;
	 } 	  
       
        /**
	   * ����reserve12��Getter����.
	   *
	   * ��������:2011-11-3
	   * @return UFDate
	   */
	 public UFDate getReserve12() {
		 return reserve12;
	  }   
	  
     /**
	   * ����reserve12��Setter����.
	   *
	   * ��������:2011-11-3
	   * @param newReserve12 UFDate
	   */
	public void setReserve12(UFDate newReserve12) {
		
		reserve12 = newReserve12;
	 } 	  
       
        /**
	   * ����reserve11��Getter����.
	   *
	   * ��������:2011-11-3
	   * @return UFDate
	   */
	 public UFDate getReserve11() {
		 return reserve11;
	  }   
	  
     /**
	   * ����reserve11��Setter����.
	   *
	   * ��������:2011-11-3
	   * @param newReserve11 UFDate
	   */
	public void setReserve11(UFDate newReserve11) {
		
		reserve11 = newReserve11;
	 } 	  
       
        /**
	   * ����vmemo��Getter����.
	   *
	   * ��������:2011-11-3
	   * @return String
	   */
	 public String getVmemo() {
		 return vmemo;
	  }   
	  
     /**
	   * ����vmemo��Setter����.
	   *
	   * ��������:2011-11-3
	   * @param newVmemo String
	   */
	public void setVmemo(String newVmemo) {
		
		vmemo = newVmemo;
	 } 	  
       
        /**
	   * ����ts��Getter����.
	   *
	   * ��������:2011-11-3
	   * @return UFDateTime
	   */
	 public UFDateTime getTs() {
		 return ts;
	  }   
	  
     /**
	   * ����ts��Setter����.
	   *
	   * ��������:2011-11-3
	   * @param newTs UFDateTime
	   */
	public void setTs(UFDateTime newTs) {
		
		ts = newTs;
	 } 	  
       
        /**
	   * ����pk_busitype��Getter����.
	   *
	   * ��������:2011-11-3
	   * @return String
	   */
	 public String getPk_busitype() {
		 return pk_busitype;
	  }   
	  
     /**
	   * ����pk_busitype��Setter����.
	   *
	   * ��������:2011-11-3
	   * @param newPk_busitype String
	   */
	public void setPk_busitype(String newPk_busitype) {
		
		pk_busitype = newPk_busitype;
	 } 	  
       
        /**
	   * ����reserve6��Getter����.
	   *
	   * ��������:2011-11-3
	   * @return String
	   */
	 public String getReserve6() {
		 return reserve6;
	  }   
	  
     /**
	   * ����reserve6��Setter����.
	   *
	   * ��������:2011-11-3
	   * @param newReserve6 String
	   */
	public void setReserve6(String newReserve6) {
		
		reserve6 = newReserve6;
	 } 	  
       
        /**
	   * ����vdef1��Getter����.
	   *
	   * ��������:2011-11-3
	   * @return String
	   */
	 public String getVdef1() {
		 return vdef1;
	  }   
	  
     /**
	   * ����vdef1��Setter����.
	   *
	   * ��������:2011-11-3
	   * @param newVdef1 String
	   */
	public void setVdef1(String newVdef1) {
		
		vdef1 = newVdef1;
	 } 	  
       
        /**
	   * ����reserve3��Getter����.
	   *
	   * ��������:2011-11-3
	   * @return String
	   */
	 public String getReserve3() {
		 return reserve3;
	  }   
	  
     /**
	   * ����reserve3��Setter����.
	   *
	   * ��������:2011-11-3
	   * @param newReserve3 String
	   */
	public void setReserve3(String newReserve3) {
		
		reserve3 = newReserve3;
	 } 	  
       
        /**
	   * ����reserve14��Getter����.
	   *
	   * ��������:2011-11-3
	   * @return UFBoolean
	   */
	 public UFBoolean getReserve14() {
		 return reserve14;
	  }   
	  
     /**
	   * ����reserve14��Setter����.
	   *
	   * ��������:2011-11-3
	   * @param newReserve14 UFBoolean
	   */
	public void setReserve14(UFBoolean newReserve14) {
		
		reserve14 = newReserve14;
	 } 	  
       
        /**
	   * ����dbilldate��Getter����.
	   *
	   * ��������:2011-11-3
	   * @return UFDate
	   */
	 public UFDate getDbilldate() {
		 return dbilldate;
	  }   
	  
     /**
	   * ����dbilldate��Setter����.
	   *
	   * ��������:2011-11-3
	   * @param newDbilldate UFDate
	   */
	public void setDbilldate(UFDate newDbilldate) {
		
		dbilldate = newDbilldate;
	 } 	  
       
        /**
	   * ����vbillno��Getter����.
	   *
	   * ��������:2011-11-3
	   * @return String
	   */
	 public String getVbillno() {
		 return vbillno;
	  }   
	  
     /**
	   * ����vbillno��Setter����.
	   *
	   * ��������:2011-11-3
	   * @param newVbillno String
	   */
	public void setVbillno(String newVbillno) {
		
		vbillno = newVbillno;
	 } 	  
       
        /**
	   * ����reserve13��Getter����.
	   *
	   * ��������:2011-11-3
	   * @return UFDate
	   */
	 public UFDate getReserve13() {
		 return reserve13;
	  }   
	  
     /**
	   * ����reserve13��Setter����.
	   *
	   * ��������:2011-11-3
	   * @param newReserve13 UFDate
	   */
	public void setReserve13(UFDate newReserve13) {
		
		reserve13 = newReserve13;
	 } 	  
       
        /**
	   * ����reserve10��Getter����.
	   *
	   * ��������:2011-11-3
	   * @return UFDouble
	   */
	 public UFDouble getReserve10() {
		 return reserve10;
	  }   
	  
     /**
	   * ����reserve10��Setter����.
	   *
	   * ��������:2011-11-3
	   * @param newReserve10 UFDouble
	   */
	public void setReserve10(UFDouble newReserve10) {
		
		reserve10 = newReserve10;
	 } 	  
       
        /**
	   * ����cgeneralhid��Getter����.
	   *
	   * ��������:2011-11-3
	   * @return String
	   */
	 public String getCgeneralhid() {
		 return cgeneralhid;
	  }   
	  
     /**
	   * ����cgeneralhid��Setter����.
	   *
	   * ��������:2011-11-3
	   * @param newCgeneralhid String
	   */
	public void setCgeneralhid(String newCgeneralhid) {
		
		cgeneralhid = newCgeneralhid;
	 } 	  
       
        /**
	   * ����pk_deptdoc��Getter����.
	   *
	   * ��������:2011-11-3
	   * @return String
	   */
	 public String getPk_deptdoc() {
		 return pk_deptdoc;
	  }   
	  
     /**
	   * ����pk_deptdoc��Setter����.
	   *
	   * ��������:2011-11-3
	   * @param newPk_deptdoc String
	   */
	public void setPk_deptdoc(String newPk_deptdoc) {
		
		pk_deptdoc = newPk_deptdoc;
	 } 	  
       
        /**
	   * ����reserve9��Getter����.
	   *
	   * ��������:2011-11-3
	   * @return UFDouble
	   */
	 public UFDouble getReserve9() {
		 return reserve9;
	  }   
	  
     /**
	   * ����reserve9��Setter����.
	   *
	   * ��������:2011-11-3
	   * @param newReserve9 UFDouble
	   */
	public void setReserve9(UFDouble newReserve9) {
		
		reserve9 = newReserve9;
	 } 	  
       
        /**
	   * ����reserve8��Getter����.
	   *
	   * ��������:2011-11-3
	   * @return UFDouble
	   */
	 public UFDouble getReserve8() {
		 return reserve8;
	  }   
	  
     /**
	   * ����reserve8��Setter����.
	   *
	   * ��������:2011-11-3
	   * @param newReserve8 UFDouble
	   */
	public void setReserve8(UFDouble newReserve8) {
		
		reserve8 = newReserve8;
	 } 	  
       
        /**
	   * ����vdef3��Getter����.
	   *
	   * ��������:2011-11-3
	   * @return String
	   */
	 public String getVdef3() {
		 return vdef3;
	  }   
	  
     /**
	   * ����vdef3��Setter����.
	   *
	   * ��������:2011-11-3
	   * @param newVdef3 String
	   */
	public void setVdef3(String newVdef3) {
		
		vdef3 = newVdef3;
	 } 	  
       
        /**
	   * ����vdef9��Getter����.
	   *
	   * ��������:2011-11-3
	   * @return String
	   */
	 public String getVdef9() {
		 return vdef9;
	  }   
	  
     /**
	   * ����vdef9��Setter����.
	   *
	   * ��������:2011-11-3
	   * @param newVdef9 String
	   */
	public void setVdef9(String newVdef9) {
		
		vdef9 = newVdef9;
	 } 	  
       
        /**
	   * ����reserve1��Getter����.
	   *
	   * ��������:2011-11-3
	   * @return String
	   */
	 public String getReserve1() {
		 return reserve1;
	  }   
	  
     /**
	   * ����reserve1��Setter����.
	   *
	   * ��������:2011-11-3
	   * @param newReserve1 String
	   */
	public void setReserve1(String newReserve1) {
		
		reserve1 = newReserve1;
	 } 	  
       
        /**
	   * ����dapprovedate��Getter����.
	   *
	   * ��������:2011-11-3
	   * @return UFDate
	   */
	 public UFDate getDapprovedate() {
		 return dapprovedate;
	  }   
	  
     /**
	   * ����dapprovedate��Setter����.
	   *
	   * ��������:2011-11-3
	   * @param newDapprovedate UFDate
	   */
	public void setDapprovedate(UFDate newDapprovedate) {
		
		dapprovedate = newDapprovedate;
	 } 	  
       
        /**
	   * ����vdef8��Getter����.
	   *
	   * ��������:2011-11-3
	   * @return String
	   */
	 public String getVdef8() {
		 return vdef8;
	  }   
	  
     /**
	   * ����vdef8��Setter����.
	   *
	   * ��������:2011-11-3
	   * @param newVdef8 String
	   */
	public void setVdef8(String newVdef8) {
		
		vdef8 = newVdef8;
	 } 	  
       
        /**
	   * ����reserve16��Getter����.
	   *
	   * ��������:2011-11-3
	   * @return UFBoolean
	   */
	 public UFBoolean getReserve16() {
		 return reserve16;
	  }   
	  
     /**
	   * ����reserve16��Setter����.
	   *
	   * ��������:2011-11-3
	   * @param newReserve16 UFBoolean
	   */
	public void setReserve16(UFBoolean newReserve16) {
		
		reserve16 = newReserve16;
	 } 	  
       
        /**
	   * ����reserve7��Getter����.
	   *
	   * ��������:2011-11-3
	   * @return String
	   */
	 public String getReserve7() {
		 return reserve7;
	  }   
	  
     /**
	   * ����reserve7��Setter����.
	   *
	   * ��������:2011-11-3
	   * @param newReserve7 String
	   */
	public void setReserve7(String newReserve7) {
		
		reserve7 = newReserve7;
	 } 	  
       
        /**
	   * ����vapproveid��Getter����.
	   *
	   * ��������:2011-11-3
	   * @return String
	   */
	 public String getVapproveid() {
		 return vapproveid;
	  }   
	  
     /**
	   * ����vapproveid��Setter����.
	   *
	   * ��������:2011-11-3
	   * @param newVapproveid String
	   */
	public void setVapproveid(String newVapproveid) {
		
		vapproveid = newVapproveid;
	 } 	  
       
        /**
	   * ����reserve15��Getter����.
	   *
	   * ��������:2011-11-3
	   * @return UFBoolean
	   */
	 public UFBoolean getReserve15() {
		 return reserve15;
	  }   
	  
     /**
	   * ����reserve15��Setter����.
	   *
	   * ��������:2011-11-3
	   * @param newReserve15 UFBoolean
	   */
	public void setReserve15(UFBoolean newReserve15) {
		
		reserve15 = newReserve15;
	 } 	  
       
        /**
	   * ����reserve2��Getter����.
	   *
	   * ��������:2011-11-3
	   * @return String
	   */
	 public String getReserve2() {
		 return reserve2;
	  }   
	  
     /**
	   * ����reserve2��Setter����.
	   *
	   * ��������:2011-11-3
	   * @param newReserve2 String
	   */
	public void setReserve2(String newReserve2) {
		
		reserve2 = newReserve2;
	 } 	  
       
        /**
	   * ����vdef6��Getter����.
	   *
	   * ��������:2011-11-3
	   * @return String
	   */
	 public String getVdef6() {
		 return vdef6;
	  }   
	  
     /**
	   * ����vdef6��Setter����.
	   *
	   * ��������:2011-11-3
	   * @param newVdef6 String
	   */
	public void setVdef6(String newVdef6) {
		
		vdef6 = newVdef6;
	 } 	  
       
        /**
	   * ����vdef10��Getter����.
	   *
	   * ��������:2011-11-3
	   * @return String
	   */
	 public String getVdef10() {
		 return vdef10;
	  }   
	  
     /**
	   * ����vdef10��Setter����.
	   *
	   * ��������:2011-11-3
	   * @param newVdef10 String
	   */
	public void setVdef10(String newVdef10) {
		
		vdef10 = newVdef10;
	 } 	  
       
        /**
	   * ����vdef5��Getter����.
	   *
	   * ��������:2011-11-3
	   * @return String
	   */
	 public String getVdef5() {
		 return vdef5;
	  }   
	  
     /**
	   * ����vdef5��Setter����.
	   *
	   * ��������:2011-11-3
	   * @param newVdef5 String
	   */
	public void setVdef5(String newVdef5) {
		
		vdef5 = newVdef5;
	 } 	  
       
        /**
	   * ����pk_wds_writeback4Y_h��Getter����.
	   *
	   * ��������:2011-11-3
	   * @return String
	   */
	 public String getPk_wds_writeback4Y_h() {
		 return pk_wds_writeback4Y_h;
	  }   
	  
     /**
	   * ����pk_wds_writeback4Y_h��Setter����.
	   *
	   * ��������:2011-11-3
	   * @param newPk_wds_writeback4Y_h String
	   */
	public void setPk_wds_writeback4Y_h(String newPk_wds_writeback4Y_h) {
		
		pk_wds_writeback4Y_h = newPk_wds_writeback4Y_h;
	 } 	  
       
       
    /**
	  * ��֤���������֮��������߼���ȷ��.
	  *
	  * ��������:2011-11-3
	  * @exception nc.vo.pub.ValidationException �����֤ʧ��,�׳�
	  * ValidationException,�Դ�����н���.
	 */
	 public void validate() throws ValidationException {
	
	 	ArrayList errFields = new ArrayList(); // errFields record those null

                                                      // fields that cannot be null.
       		  // ����Ƿ�Ϊ�������յ��ֶθ��˿�ֵ,�������Ҫ�޸��������ʾ��Ϣ:
	
	   		if (pk_wds_writeback4Y_h == null) {
			errFields.add(new String("pk_wds_writeback4Y_h"));
			  }	
	   	
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
	  * ��������:2011-11-3
	  * @return java.lang.String
	  */
	public java.lang.String getParentPKFieldName() {
	  	 
	 	    return null;
	 	
	}   
    
    /**
	  * <p>ȡ�ñ�����.
	  * <p>
	  * ��������:2011-11-3
	  * @return java.lang.String
	  */
	public java.lang.String getPKFieldName() {
	 	  return "pk_wds_writeback4Y_h";
	 	}
    
	/**
      * <p>���ر�����.
	  * <p>
	  * ��������:2011-11-3
	  * @return java.lang.String
	 */
	public java.lang.String getTableName() {
				
		return "wds_writeback4y_h";
	}    
    
    /**
	  * ����Ĭ�Ϸ�ʽ����������.
	  *
	  * ��������:2011-11-3
	  */
	public Writeback4yHVO() {
			
			   super();	
	  }    
    
            /**
	 * ʹ���������г�ʼ���Ĺ�����.
	 *
	 * ��������:2011-11-3
	 * @param newPk_wds_writeback4Y_h ����ֵ
	 */
	 public Writeback4yHVO(String newPk_wds_writeback4Y_h) {
		
		// Ϊ�����ֶθ�ֵ:
		 pk_wds_writeback4Y_h = newPk_wds_writeback4Y_h;
	
    	}
    
     
     /**
	  * ���ض����ʶ,����Ψһ��λ����.
	  *
	  * ��������:2011-11-3
	  * @return String
	  */
	   public String getPrimaryKey() {
				
		 return pk_wds_writeback4Y_h;
	   
	   }

     /**
	  * ���ö����ʶ,����Ψһ��λ����.
	  *
	  * ��������:2011-11-3
	  * @param newPk_wds_writeback4Y_h  String    
	  */
	 public void setPrimaryKey(String newPk_wds_writeback4Y_h) {
				
				pk_wds_writeback4Y_h = newPk_wds_writeback4Y_h; 
				
	 } 
           
	  /**
       * ������ֵ�������ʾ����.
	   *
	   * ��������:2011-11-3
	   * @return java.lang.String ������ֵ�������ʾ����.
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