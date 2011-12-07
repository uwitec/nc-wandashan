package nc.ui.wl.pub.report;

import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.bill.BillStatus;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.wl.pub.WdsWlPubConst;

public class WDSWLReportSql {	
	/**
	 * 
	 * @���ߣ�mlr
	 * @˵�������ɽ������Ŀ 
	 *       ֻ��ѯ���״̬ ,�������û���
	 * @ʱ�䣺2011-7-17����11:00:28
	 * @param invclcode Ҫ���˵Ĵ������ı���
	 * @param pk_stordoc  �Ƿ���˲ֿ�
	 * @param isType    �Ƿ񰴴�����ͷ��� ���Ƿ񰴳��ô���Ͳ����ô������������
	 * @param isInvcl   �Ƿ񰴴���������
	 * @param isShowStore �Ƿ񰴲ֿ����
	 * @param iscargdoc   �Ƿ񰴻�λ����
	 * @param isvbanchcode �Ƿ����η���
	 * @param ddatefrom   ��ѯ��ʼ����
	 * @param ddateto     ��ѯ��������
	 * @return
	 */
	public static String getQuerySQL(String invclcode,UFBoolean isNormal,String pk_stordoc,String pk_invbasdoc,UFBoolean isType,UFBoolean isInvcl,UFBoolean isShowStore,UFBoolean iscargdoc,UFBoolean isvbanchcode,String ddatefrom,String ddateto){
		StringBuffer sql = new StringBuffer();	
		        String isnorm=null;
		        if(isNormal.booleanValue()==true){
		        	isnorm="Y";
		        }else{
		        	isnorm="N";
		        }
		        sql.append(" select ");//�ֿ�����	
		        if(isShowStore.booleanValue()==true){
		        sql.append(" t.pk_customize1 pk_stordoc,");
		        sql.append(" min(s.storname) storename,");//�ֿ�����
		        }	
		        if(iscargdoc.booleanValue()==true){
		        sql.append(" t.pk_cargdoc pk_cargdoc,");
		        sql.append(" min(bc.csname) csname,");
		        }
		        if(isType.booleanValue()==true){
				sql.append(" iv.fuesed invtype,");//������� ����0  ������1
				}
			    if(isInvcl.booleanValue()==true){
				sql.append(" cl.pk_invcl pk_invcl,");//�����������
				sql.append(" min(cl.vinvclname) invclname,");//�����������		
			    }
		   //   sql.append(" cl.pk_invcl pk_invcl,");//�����������
		   //   sql.append(" iv.fuesed invtype,");//������� ����0  ������1
		   //     sql.append(" min(cl.vinvclcode) invclcode,");//����������
		   //     sql.append(" min(cl.vinvclname) invclname,");//�����������	
		        if(isvbanchcode.booleanValue()==true){
		        sql.append(" t.whs_batchcode vbatchcode,");		
		        }
     			sql.append(" min(t.ss_pk)  pk_storestate,");//���״̬����
     			sql.append(" min(bcc.mainmeasrate)  hsl,");//������
		        sql.append(" t.pk_invbasdoc pk_invbasdoc,");		      
		        sql.append(" min(i.invcode) invcode,");//�������
		        sql.append(" min(i.invname) invname,");//�������
		        sql.append(" min(i.invspec) invspec,");//���
		        sql.append(" round(sysdate-to_date(t.creadate,'YYYY-MM-DD HH24-MI-SS'),0) days,");//�������
		        sql.append(" t.creadate creadate,");//�������
		        sql.append(" t.ss_pk ss_pk,");//���״̬����    
		        sql.append(" sum(t.whs_stocktonnage) num,");//����еĵ�Ʒ�������� ��Ҫ�������ÿ���������
		        sql.append(" sum(t.whs_stockpieces) bnum"); //����еĵ�Ʒ�ĸ����� ��Ҫ�������ÿ��丨���� 
		        sql.append(" from ");	        	 
				sql.append(" tb_warehousestock t");
				sql.append(" join bd_stordoc s");//�����ֿ�
				sql.append(" on t.pk_customize1=s.pk_stordoc");
				sql.append(" join bd_invbasdoc i");//���������������
				sql.append(" on t.pk_invbasdoc=i.pk_invbasdoc");	
				sql.append(" join wds_invbasdoc iv");//�����������
				sql.append(" on t.pk_invbasdoc=iv.pk_invbasdoc");
				sql.append(" join wds_invcl cl");//�����������
				sql.append(" on iv.vdef1=cl.pk_invcl");
				sql.append(" join bd_cargdoc bc");//������λ����
				sql.append(" on t.pk_cargdoc=bc.pk_cargdoc");//
				sql.append(" join  tb_stockstate ss");//�������״̬��
				sql.append(" on t.ss_pk=ss.ss_pk");
				sql.append(" join bd_convert bcc");//����������
				sql.append(" on t.pk_invbasdoc=bcc.pk_invbasdoc");
				sql.append(" where isnull(t.dr,0)=0");//
				sql.append(" and isnull(s.dr,0)=0");//
				sql.append(" and isnull(i.dr,0)=0");
				sql.append(" and isnull(iv.dr,0)=0");//
				sql.append(" and isnull(cl.dr,0)=0");
				sql.append(" and isnull(bc.dr,0)=0");
				sql.append(" and isnull(ss.dr,0)=0");
				sql.append(" and isnull(bc.dr,0)=0");
				sql.append(" and cl.vinvclcode like '"+invclcode+"%'");//����������۵Ĵ������
				sql.append(" and upper(coalesce(ss.isok,'N'))='"+isnorm+"'");//�����Ƿ�����
				sql.append(" and t.creadate between '"+ddatefrom+"' and '"+ddateto+"'");//�����������
				if(pk_stordoc!=null && !pk_stordoc.equalsIgnoreCase("")){
				sql.append(" and t.pk_customize1='"+pk_stordoc+"'");//���˲ֿ�	
				}
				if(pk_invbasdoc!=null && !pk_invbasdoc.equalsIgnoreCase("")){
				sql.append(" and t.pk_invmandoc='"+pk_invbasdoc+"'");//���˴��	
			    }
				sql.append(" and t.pk_corp='"+ClientEnvironment.getInstance().getCorporation().getPrimaryKey()+"'");
				sql.append(" group by ");	
			    if(isType.booleanValue()==true){
				sql.append(" iv.fuesed,");//������� ����0  ������1
				}
				if(isInvcl.booleanValue()==true){
				sql.append(" cl.pk_invcl,");//�����������
			    }
				if(isShowStore.booleanValue()==true){
				sql.append(" t.pk_customize1,");
				}					
				sql.append(" t.pk_invbasdoc,t.ss_pk,t.creadate");
			    if(iscargdoc.booleanValue()==true){
				sql.append(" ,t.pk_cargdoc");	
				}
				if(isvbanchcode.booleanValue()==true){
				sql.append(" ,t.whs_batchcode");		
				}
				return sql.toString();
	}
	/**
     * 
     * @���ߣ�mlr
     * @˵�������ɽ������Ŀ  
     * �����Զ��������õ���ѯ�������ݵ�SQL ֻ��ѯ�������˵�
     * @ʱ�䣺2011-5-10����09:41:31
     * @param wheresql
     * @return
     */
	public static String getQuerySQL1(String invclcode,String pk_stordoc,String pk_invbasdoc,UFBoolean isType,UFBoolean isInvcl,UFBoolean isShowStore,UFBoolean iscargdoc,UFBoolean isvbanchcode,String ddatefrom,String ddateto){
		    StringBuffer sql = new StringBuffer();	
		    sql.append(" select ");
		    sql.append(" w.type type,");//��;���������
		    if(isShowStore.booleanValue()==true){
		    sql.append(" w.pk_outwhouse pk_stordoc,");
		    sql.append(" min(s.storname) storename,");
		    }
		    if(isType.booleanValue()==true){
		    sql.append(" w.invtype invtype,");//������� ����0  ������1
		    }
		    if(isInvcl.booleanValue()==true){
		    sql.append(" w.invtype pk_invcl,");//�����������
		    sql.append(" min(w.invclname) invclname,");//�����������		
		    }
		    if(iscargdoc.booleanValue()==true){
		    sql.append(" w.pk_cargdoc pk_cargdoc,");
		    sql.append(" min(bc.csname) csname,");
		    }
		 //   sql.append(" w.pk_invcl pk_invcl,");//�����������
	     //   sql.append(" w.invtype invtype,");//������� ����0  ������1
	     //   sql.append(" min(w.invclcode) invclcode,");//����������
	     //   sql.append(" min(w.invclname) invclname,");//�����������	
//		    sql.append(" w.pk_invmandoc pk_invmandoc,");
		    sql.append(" w.pk_invbasdoc pk_invbasdoc,");
		    sql.append(" sum(w.plannum)   plannum,"); //�ƻ� ������ 
			sql.append(" sum(w.bplannum)  bplannum,") ; //�ƻ�������
	//		sql.append(" min(s.storname) storename,");//�ֿ�����
	        sql.append(" min(i.invcode) invcode,");//�������
	        sql.append(" min(i.invname) invname,");//�������
	        sql.append(" min(i.invspec) invspec");//���	      
		    sql.append("  from");
	        sql.append("  ((select h.itransstatus type,");
			sql.append("  h.pk_outwhouse pk_outwhouse,"); 
			
			sql.append("  crg.pk_cargdoc pk_cargdoc,");//�ӻ�λ����󶨱��û�λ
			
			
		    sql.append(" cl.pk_invcl pk_invcl,");//�����������
		    sql.append(" iv.fuesed invtype,");//������� ����0  ������1
		    sql.append(" cl.vinvclcode invclcode,");//����������
		    sql.append(" cl.vinvclname invclname,");//�����������	
			sql.append("  b.pk_invmandoc pk_invmandoc,");     
			sql.append("  b.pk_invbasdoc pk_invbasdoc,");   
			sql.append("  b.narrangnmu plannum,");   
			sql.append("  b.nassarrangnum bplannum") ;  
			sql.append("  from wds_soorder h");
			sql.append("  join wds_soorder_b b on h.pk_soorder = b.pk_soorder");
			sql.append("  join wds_invbasdoc iv");//�����������
			sql.append("  on b.pk_invbasdoc=iv.pk_invbasdoc");
			sql.append("  join wds_invcl cl");//�����������
			sql.append("  on iv.vdef1=cl.pk_invcl");	
			
			sql.append("  join (select h.pk_cargdoc, b.pk_invbasdoc, h.pk_stordoc from wds_cargdoc1 h join tb_spacegoods b on h.pk_wds_cargdoc = b.pk_wds_cargdoc  where nvl(h.dr, 0) = 0 and nvl(b.dr, 0) = 0) crg");//������λ��������ӱ�
			sql.append("  on b.pk_invbasdoc=crg.pk_invbasdoc and h.pk_outwhouse=crg.pk_stordoc ");
			sql.append("  where isnull(h.dr, 0) = 0");	
			sql.append("  and isnull(iv.dr,0)=0");//
			sql.append("  and isnull(b.dr,0)=0");//
			sql.append("  and isnull(cl.dr,0)=0");
		
			sql.append("  and cl.vinvclcode like '"+invclcode+"%'");//����������۵Ĵ������
			sql.append("  and h.dmakedate between '"+ddatefrom+"' and '"+ddateto+"'");//�����Ƶ�����
			sql.append("  and h.pk_corp='"+ClientEnvironment.getInstance().getCorporation().getPrimaryKey()+"'");//���� ��˾

			sql.append("  and isnull(b.dr, 0) = 0 )");
			sql.append("  union all ");
			sql.append("   (select  h1.itransstatus type,");
			sql.append("  h1.pk_outwhouse pk_outwhouse,");   
			sql.append("  crg.pk_cargdoc pk_cargdoc,");//�ӻ�λ����󶨱��û�λ
			sql.append(" cl.pk_invcl pk_invcl,");//�����������
			sql.append(" iv.fuesed invtype,");//������� ����0  ������1
			sql.append(" cl.vinvclcode invclcode,");//����������
			sql.append(" cl.vinvclname invclname,");//�����������	
			sql.append("  b1.pk_invmandoc pk_invmandoc,");  
			sql.append("  b1.pk_invbasdoc pk_invbasdoc,") ;
			sql.append("  b1.ndealnum plannum,");
			sql.append("  b1.nassdealnum bplannum"); 
			sql.append("  from wds_sendorder h1"); 
			sql.append("  join wds_sendorder_b b1 on h1.pk_sendorder = b1.pk_sendorder");
			sql.append("  join wds_invbasdoc iv");//�����������
			sql.append("  on b1.pk_invbasdoc=iv.pk_invbasdoc");
			sql.append("  join wds_invcl cl");//�����������
			sql.append("  on iv.vdef1=cl.pk_invcl");	
			sql.append("  join (select h.pk_cargdoc, b.pk_invbasdoc, h.pk_stordoc from wds_cargdoc1 h join tb_spacegoods b on h.pk_wds_cargdoc = b.pk_wds_cargdoc  where nvl(h.dr, 0) = 0 and nvl(b.dr, 0) = 0) crg");//������λ��������ӱ�
			sql.append("  on b1.pk_invbasdoc=crg.pk_invbasdoc and h1.pk_outwhouse=crg.pk_stordoc ");
			sql.append("  where isnull(h1.dr, 0) = 0");
			sql.append("  and isnull(b1.dr,0)=0");//
			sql.append("  and isnull(iv.dr,0)=0");//
			sql.append("  and isnull(cl.dr,0)=0");
		
			sql.append("  and cl.vinvclcode like '"+invclcode+"%'");//����������۵Ĵ������
			sql.append("  and h1.dmakedate between '"+ddatefrom+"' and '"+ddateto+"'");//�����Ƶ�����
			sql.append("  and h1.pk_corp='"+ClientEnvironment.getInstance().getCorporation().getPrimaryKey()+"'");//���� ��˾

			sql.append("  and isnull(b1.dr, 0) = 0 ))w");
			sql.append("  join bd_stordoc s");//�ֿ⵵��
			sql.append("  on w.pk_outwhouse=s.pk_stordoc");
			sql.append("  left join bd_invbasdoc i");
			sql.append("  on w.pk_invbasdoc=i.pk_invbasdoc");
			sql.append("  join bd_cargdoc bc");//������λ����
			sql.append("  on w.pk_cargdoc=bc.pk_cargdoc");//
			sql.append("  where ");
			sql.append("  isnull(s.dr,0)=0");//
			sql.append("  and isnull(i.dr,0)=0");//
			sql.append("  and isnull(bc.dr,0)=0");
			sql.append("  and w.type=0");//���˴������˵�
			if(pk_stordoc!=null && !pk_stordoc.equalsIgnoreCase("")){
			  sql.append(" and  w.pk_outwhouse='"+pk_stordoc+"'");	
		    }
			if(pk_invbasdoc!=null && !pk_invbasdoc.equalsIgnoreCase("")){
				sql.append(" and w.pk_invmandoc='"+pk_invbasdoc+"'");//���˴��	
	        }
			//���ֿ� ��� ��;���������������
			sql.append("  group by ");
		    if(isType.booleanValue()==true){
			sql.append(" w.invtype,");//������� ����0  ������1
			}
			if(isInvcl.booleanValue()==true){
			sql.append(" w.pk_invcl,");//�����������
		    }
			if(isShowStore.booleanValue()==true){
		    sql.append(" w.pk_outwhouse,");			  
		    }	
			sql.append(" w.pk_invbasdoc,w.type");				
			if(iscargdoc.booleanValue()==true){
		      sql.append(",w.pk_cargdoc ");	
		    }
			return sql.toString();
	}
	/**
     * 
     * @���ߣ�mlr
     * @˵�������ɽ������Ŀ  
     * �����Զ��������õ���ѯ�������ݵ�SQL ֻ��ѯ��;���˵� 
     *  
     * @ʱ�䣺2011-5-10����09:41:31
     * @param wheresql
     * @return
     */
	public static String getQuerySQL2(String invclcode,String pk_stordoc,String pk_invbasdoc,UFBoolean isType,UFBoolean isInvcl,UFBoolean isShowStore,UFBoolean iscargdoc,UFBoolean isvbanchcode,String ddatefrom,String ddateto){
		    StringBuffer sql = new StringBuffer();	
		    sql.append(" select ");
		    sql.append(" w.type type,");//��;���������
		    if(isShowStore.booleanValue()==true){
		    sql.append(" w.pk_outwhouse pk_stordoc,");
		    sql.append(" min(s.storname) storename,");//�ֿ�����
		    }	
		    if(iscargdoc.booleanValue()==true){
		    sql.append(" w.pk_cargdoc pk_cargdoc,");
		    sql.append(" min(bc.csname) csname,");
		    }
		    if(isType.booleanValue()==true){
			sql.append(" w.invtype invtype,");//������� ����0  ������1
			}
			if(isInvcl.booleanValue()==true){
			sql.append(" w.invtype pk_invcl,");//�����������
			sql.append(" min(w.invclname) invclname,");//�����������		
			}
	//	    sql.append(" w.pk_invcl pk_invcl,");//�����������
	//        sql.append(" w.invtype invtype,");//������� ����0  ������1
	//        sql.append(" min(w.invclcode) invclcode,");//����������
	//        sql.append(" min(w.invclname) invclname,");//�����������	
		    if(isvbanchcode.booleanValue()==true){
		    sql.append(" w.vbatchcode vbatchcode,");		
		    }
		    sql.append(" w.pk_invbasdoc pk_invbasdoc,");		 		
	        sql.append(" min(i.invcode) invcode,");//�������
	        sql.append(" min(i.invname) invname,");//�������
	        sql.append(" min(i.invspec) invspec,");//���
	        sql.append(" sum(w.plannum)   plannum,"); //�ƻ� ������ 
			sql.append(" sum(w.bplannum)  bplannum") ; //�ƻ�������
		    sql.append("  from");
	        sql.append("  ((select h.itransstatus type,");
			sql.append("  h.pk_outwhouse pk_outwhouse,"); 
			sql.append("  crg.pk_cargdoc pk_cargdoc,");//�ӻ�λ����󶨱��û�λ
			sql.append(" cl.pk_invcl pk_invcl,");//�����������
			sql.append(" iv.fuesed invtype,");//������� ����0  ������1
			sql.append(" cl.vinvclcode invclcode,");//����������
			sql.append(" cl.vinvclname invclname,");//�����������	
			sql.append("  b.pk_invmandoc pk_invmandoc,");     
			sql.append("  b.pk_invbasdoc pk_invbasdoc,");  
			sql.append("  lb.vbatchcode vbatchcode,");//�������
			sql.append("  lb.noutnum plannum,");//ʵ������   
			sql.append("  lb.noutassistnum bplannum") ; //ʵ�������� 
			sql.append("  from wds_soorder h");
			sql.append("  join wds_soorder_b b on h.pk_soorder = b.pk_soorder");
			sql.append("  join wds_invbasdoc iv");//�����������
			sql.append("  on b.pk_invbasdoc=iv.pk_invbasdoc");
			sql.append("  join wds_invcl cl");//�����������
			sql.append("  on iv.vdef1=cl.pk_invcl");	

			sql.append("  join (select h.pk_cargdoc, b.pk_invbasdoc, h.pk_stordoc from wds_cargdoc1 h join tb_spacegoods b on h.pk_wds_cargdoc = b.pk_wds_cargdoc  where nvl(h.dr, 0) = 0 and nvl(b.dr, 0) = 0) crg");//������λ��������ӱ�
			sql.append("  on b.pk_invbasdoc=crg.pk_invbasdoc and h.pk_outwhouse=crg.pk_stordoc ");
			sql.append("  join tb_outgeneral_b lb");//�������ⵥ�ӱ�
			sql.append("  on h.pk_soorder=lb.csourcebillhid");
			sql.append("  and b.pk_soorder_b=lb.csourcebillbid");	
			sql.append("  where isnull(h.dr, 0) = 0");	
			sql.append("  and isnull(iv.dr,0)=0");//
			sql.append("  and isnull(b.dr,0)=0");//
			sql.append("  and isnull(cl.dr,0)=0");
			
			sql.append("  and isnull(lb.dr,0)=0");
			sql.append("  and lb.csourcetype='WDS5'");//�������۵���
			sql.append("  and cl.vinvclcode like '"+invclcode+"%'");//����������۵Ĵ������
			sql.append("  and h.dmakedate between '"+ddatefrom+"' and '"+ddateto+"'");//�����Ƶ�����
			sql.append("  and h.pk_corp='"+ClientEnvironment.getInstance().getCorporation().getPrimaryKey()+"'");//���� ��˾
			sql.append("  and isnull(b.dr, 0) = 0 )");
			sql.append("  union all ");
			sql.append("   (select  h1.itransstatus type,");
			sql.append("  h1.pk_outwhouse pk_outwhouse,");   
			sql.append("  crg.pk_cargdoc pk_cargdoc,");//�ӻ�λ����󶨱��û�λ
			sql.append(" cl.pk_invcl pk_invcl,");//�����������
			sql.append(" iv.fuesed invtype,");//������� ����0  ������1
			sql.append(" cl.vinvclcode invclcode,");//����������
			sql.append(" cl.vinvclname invclname,");//�����������	
			sql.append("  b1.pk_invmandoc pk_invmandoc,");  
			sql.append("  b1.pk_invbasdoc pk_invbasdoc,") ;
			sql.append("  lb.vbatchcode vbatchcode,");//�������
			sql.append("  lb.noutnum plannum,");//ʵ������   
			sql.append("  lb.noutassistnum bplannum") ; //ʵ�������� 
			sql.append("  from wds_sendorder h1"); 
			sql.append("  join wds_sendorder_b b1 on h1.pk_sendorder = b1.pk_sendorder");
			sql.append("  join wds_invbasdoc iv");//�����������
			sql.append("  on b1.pk_invbasdoc=iv.pk_invbasdoc");
			sql.append("  join wds_invcl cl");//�����������
			sql.append("  on iv.vdef1=cl.pk_invcl");	

			sql.append("  join (select h.pk_cargdoc, b.pk_invbasdoc, h.pk_stordoc from wds_cargdoc1 h join tb_spacegoods b on h.pk_wds_cargdoc = b.pk_wds_cargdoc  where nvl(h.dr, 0) = 0 and nvl(b.dr, 0) = 0) crg");//������λ��������ӱ�
			sql.append("  on b1.pk_invbasdoc=crg.pk_invbasdoc and h1.pk_outwhouse=crg.pk_stordoc ");
			sql.append("  join tb_outgeneral_b lb");//�������ⵥ�ӱ�
			sql.append("  on h1.pk_sendorder=lb.csourcebillhid");
			sql.append("  and b1.pk_sendorder_b=lb.csourcebillbid");	
			sql.append("  where isnull(h1.dr, 0) = 0");
			sql.append("  and isnull(b1.dr,0)=0");//
			sql.append("  and isnull(iv.dr,0)=0");//
			sql.append("  and isnull(cl.dr,0)=0");
		
			sql.append("  and isnull(lb.dr,0)=0");
			sql.append("  and lb.csourcetype='WDS3'");//�������۵���
			sql.append("  and cl.vinvclcode like '"+invclcode+"%'");//����������۵Ĵ������
			sql.append("  and h1.dmakedate between '"+ddatefrom+"' and '"+ddateto+"'");//�����Ƶ�����
			sql.append("  and h1.pk_corp='"+ClientEnvironment.getInstance().getCorporation().getPrimaryKey()+"'");//���� ��˾
			sql.append("  and isnull(b1.dr, 0) = 0 ))w");
			sql.append("  join bd_stordoc s");//�ֿ⵵��
			sql.append("  on w.pk_outwhouse=s.pk_stordoc");
			sql.append("  join bd_invbasdoc i");
			sql.append("  on w.pk_invbasdoc=i.pk_invbasdoc");
			sql.append("  join bd_cargdoc bc");//������λ����
			sql.append("  on w.pk_cargdoc=bc.pk_cargdoc");//
			sql.append("  where ");
			sql.append("  isnull(s.dr,0)=0");//
			sql.append("  and isnull(i.dr,0)=0");//
			sql.append("  and isnull(bc.dr,0)=0");
			sql.append("  and w.type=1");//������;���˵�
			if(pk_stordoc!=null && !pk_stordoc.equalsIgnoreCase("")){
			  sql.append(" and  w.pk_outwhouse='"+pk_stordoc+"'");	
		    }
			if(pk_invbasdoc!=null && !pk_invbasdoc.equalsIgnoreCase("")){
				sql.append(" and w.pk_invmandoc='"+pk_invbasdoc+"'");//���˴��	
	        }
			//���ֿ� ��� ��;���������������
			sql.append("  group by ");
			if(isType.booleanValue()==true){
				sql.append(" w.invtype,");//������� ����0  ������1
			}
			if(isInvcl.booleanValue()==true){
			sql.append(" w.pk_invcl,");//�����������
		    }
			if(isShowStore.booleanValue()==true){
			sql.append(" w.pk_outwhouse,");
		    }
			sql.append(" w.pk_invbasdoc,w.type");	
			if(iscargdoc.booleanValue()==true){
		      sql.append(",w.pk_cargdoc ");	
		    }
			if(isvbanchcode.booleanValue()==true){
			  sql.append(",w.vbatchcode ");		
		    }
			return sql.toString();
	}
	/**
	 * ��ò�ѯ�ִ�����sql���
	 * @���ߣ�mlr
	 * @˵�������ɽ������Ŀ 
	 * @ʱ�䣺2011-12-2����03:05:50
	 * @param pk_corp
	 * @param whereSql
	 * @return
	 */
	public static String getStoreSql(String whereSql){
		StringBuffer sql = new StringBuffer();	
        sql.append(" select ");//�ֿ�����	
        sql.append(" h.pk_customize1 pk_stordoc,");
        sql.append(" h.pk_cargdoc pk_cargdoc,");//��λ
        sql.append(" h.whs_batchcode vbatchcode,");//����
		sql.append(" iv.fuesed invtype,");//������� ����0  ������1		
		sql.append(" cl.pk_invcl pk_invcl,");//�����������
        sql.append(" h.whs_batchcode vbatchcode,");	//����	
		sql.append(" h.ss_pk  pk_storestate,");//���״̬����
        sql.append(" h.pk_invbasdoc pk_invbasdoc,");		      
        sql.append(" round(sysdate-to_date(h.creadate,'YYYY-MM-DD HH24-MI-SS'),0) days,");//�������
        sql.append(" h.creadate creadate,");//�������
        sql.append(" h.whs_stocktonnage num,");//����еĵ�Ʒ�������� ��Ҫ�������ÿ���������
        sql.append(" h.whs_stockpieces bnum"); //����еĵ�Ʒ�ĸ����� ��Ҫ�������ÿ��丨���� 
        sql.append(" from ");	        	 
		sql.append(" tb_warehousestock h");
		sql.append(" join wds_invbasdoc iv");//�����������
		sql.append(" on h.pk_invmandoc=iv.pk_invmandoc");
		sql.append(" join tb_stockstate st");//�������״̬
		sql.append(" on h.ss_pk=st.ss_pk");
		sql.append(" left join wds_invcl cl ");//�����������
		sql.append(" on iv.vdef1=cl.pk_invcl and nvl(cl.dr, 0) = 0");
		sql.append(" where isnull(h.dr,0)=0");//
		sql.append(" and isnull(iv.dr,0)=0");//
		sql.append(" and isnull(st.dr,0)=0");//
		if(whereSql!=null && whereSql.length()!=0)
		sql.append(" and "+whereSql);
		sql.append(" and h.pk_corp='"+ClientEnvironment.getInstance().getCorporation().getPrimaryKey()+"'");				
		sql.append(" and st.isok='Y'");//���˳����Գ���Ĵ��
		return sql.toString();
	}
	/**
	 * ��ò�ѯ���۴��� ���ݵ����
	 * ��ѯ  ���۶���  �� �������۳��ⵥ
	 * @���ߣ�mlr
	 * @˵�������ɽ������Ŀ 
	 * @ʱ�䣺2011-12-2����03:05:50
	 * @param pk_corp
	 * @param whereSql
	 * @return
	 */
	public static String getOrderDaiFaSql(String whereSql){
		StringBuffer sql = new StringBuffer();	
		sql.append(" select h.vreceiptcode billcode, ");//���ݺ�  
//		sql.append(" h.dbilldate, ");  //��������
//		sql.append(" h.ccustomerid, ");//  ����id   
//		sql.append(" h.dmakedate,");   //�Ƶ�����     
		sql.append(" iv.fuesed invtype,");//������� ����0  ������1		
		sql.append(" cl.pk_invcl pk_invcl,");//�����������
		sql.append(" tbh.pk_stordoc pk_stordoc,");//�����ֿ�
		sql.append(" b.cinventoryid pk_invmandoc,");  //�������id
		sql.append(" b.corder_bid  b_pk,");//���۶����ӱ�id
//		sql.append(" b.cunitid,");   //����λ
//		sql.append(" b.cpackunitid,"); //����λ   
		sql.append(" b.cinvbasdocid pk_invbasdoc, ");  //�������id  
		sql.append(" b.nnumber, ");  //��������
		sql.append(" b.npacknumber, ");   //����������
		sql.append(" b1.noutnum num,");//�������۳��ⵥʵ������
		sql.append(" b1.noutassistnum bnum");//�������۳��ⵥʵ��������
	//	sql.append(" b.ntaldcnum,"); //�ۻ���������  
		sql.append(" from so_sale h ");
		sql.append(" join so_saleorder_b b on h.csaleid = b.csaleid ");
		sql.append(" join tb_storcubasdoc tb ");//�����ֲֿ��̰�
		sql.append(" on h.ccustomerid = tb.pk_cumandoc");
		sql.append(" join wds_storecust_h tbh");
		sql.append(" on tb.pk_wds_storecust_h = tbh.pk_wds_storecust_h");
		sql.append(" left join tb_outgeneral_b b1 on b.corder_bid=b1.cfirstbillbid and isnull(b1.dr,0)=0");
		sql.append(" left join wds_invbasdoc iv");//�����������
		sql.append(" on b.cinventoryid=iv.pk_invmandoc and isnull(iv.dr,0)=0");
		sql.append(" left join wds_invcl cl ");//�����������
		sql.append(" on iv.vdef1=cl.pk_invcl and isnull(cl.dr, 0) = 0");
		sql.append(" where isnull(h.dr, 0) = 0 ");
		sql.append(" and isnull(b.dr, 0) = 0 ");
		sql.append(" and isnull(tb.dr,0) = 0");
		sql.append(" and isnull(tbh.dr,0) =0 ");
		sql.append(" and h.fstatus = '"+BillStatus.AUDIT+"'");//��ѯ����ͨ�������۶���
		sql.append(" and "+whereSql);
		sql.append(" and h.pk_corp='"+ClientEnvironment.getInstance().getCorporation().getPrimaryKey()+"'");				
		return sql.toString();
	}
	/**
	 * ���ת�ֲ���;,������״̬�Ĳ�ѯ���
	 * ��ѯ ���˶��� �������ⵥ
	 * @���ߣ�mlr
	 * @˵�������ɽ������Ŀ 
	 * @ʱ�䣺2011-12-2����03:05:50
	 * @param pk_corp
	 * @param whereSql
	 * @return
	 */
	public static String getDmOrderSql(String whereSql){
		StringBuffer sql = new StringBuffer();	
		sql.append(" select  ");
//		sql.append(" h.vbillno ,");//���˶���������
//		sql.append(" h.dbilldate, ");//��������
//		sql.append(" h.pk_outwhouse,");//�����ֿ�
		sql.append(" h.itransstatus ,");//0 ���� 1 ��; 2 ����
		sql.append(" h.pk_inwhouse pk_stordoc,");//�ջ��ֿ�
		sql.append(" iv.fuesed invtype,");//������� ����0  ������1		
		sql.append(" cl.pk_invcl pk_invcl,");//�����������
		sql.append(" b.pk_invmandoc,");//���������id
		sql.append(" b.pk_invbasdoc ,");//�����������id
		sql.append(" b1.vbatchcode vbatchcode,");//����
//		sql.append(" b.nplannum ,");//�ƻ�����
//		sql.append(" b.nassplannum,");//�ƻ�������
//		sql.append(" b.ndealnum,");//��������
//		sql.append(" b.nassdealnum,");//���Ÿ�����
		sql.append(" b1.noutnum num,");//�������� ʵ��
		sql.append(" b1.noutassistnum bnum");//��������ʵ��������
//		sql.append(" b.noutnum num,");//�Ѿ����������
//		sql.append(" b.nassoutnum bnum");//�Ѿ�����ĸ�����
	    sql.append(" from wds_sendorder h ");
		sql.append(" join wds_sendorder_b b");
		sql.append(" on h.pk_sendorder = b.pk_sendorder ");
		sql.append(" join tb_outgeneral_b b1");
		sql.append(" on b.pk_sendorder_b = b1.csourcebillbid");
		sql.append(" join wds_invbasdoc iv");//�����������
		sql.append(" on b.pk_invmandoc=iv.pk_invmandoc");
		sql.append(" left join wds_invcl cl ");//�����������
		sql.append(" on iv.vdef1=cl.pk_invcl and isnull(cl.dr, 0) = 0");
        sql.append(" where isnull(h.dr,0)=0");
        sql.append("  and isnull(b.dr,0)=0");
        sql.append("  and isnull(iv.dr,0)=0");
        sql.append("  and isnull(b1.dr,0)=0");
        sql.append("  and h.itransstatus=1");
        if(whereSql !=null && whereSql.length()!=0)
		sql.append(" and "+whereSql);
		sql.append(" and h.pk_corp='"+ClientEnvironment.getInstance().getCorporation().getPrimaryKey()+"'");				
		return sql.toString();
	}
	
	/**
	 * ����ܲ�ת�ֲ��������
	 * ��ѯ ��Ӧ���������ⵥ 
	 * @���ߣ�mlr
	 * @˵�������ɽ������Ŀ 
	 * @ʱ�䣺2011-12-2����03:05:50
	 * @param pk_corp
	 * @param whereSql
	 * @return
	 */
	public static String getZcXuNiSql(String whereSql){
		StringBuffer sql = new StringBuffer();	
		sql.append(" select  ");
		sql.append(" h.cwarehouseid  pk_stordoc ,");//����ֿ�
		sql.append(" h.pk_defdoc9 ,");//�Ƿ�����
		sql.append(" iv.fuesed invtype,");//������� ����0  ������1		
		sql.append(" cl.pk_invcl pk_invcl,");//�����������
		sql.append(" b.cinventoryid pk_invmandoc,");//�������������
		sql.append(" b.cinvbasid pk_invbasdoc,");//���������������
		sql.append(" b.noutnum num,");//ʵ������
		sql.append(" b.noutassistnum bnum");//ʵ��������
		sql.append(" from ");
		sql.append(" ic_general_h h");
		sql.append(" join ic_general_b b");
		sql.append(" on h.cgeneralhid = b.cgeneralhid");
		sql.append(" join wds_invbasdoc iv");//�����������
		sql.append(" on b.cinventoryid=iv.pk_invmandoc");
		sql.append(" left join wds_invcl cl ");//�����������
		sql.append(" on iv.vdef1=cl.pk_invcl and isnull(cl.dr, 0) = 0");
	    sql.append(" where isnull(h.dr,0)=0");
	    sql.append("  and isnull(b.dr,0)=0");
	    sql.append("  and isnull(iv.dr,0)=0");
	    if(whereSql !=null && whereSql.length()!=0)
	 	sql.append(" and "+whereSql);
	    sql.append(" and h.cwarehouseid ='"+WdsWlPubConst.WDS_WL_ZC+"'");//����ֿ�Ϊ�ܲ�
	    sql.append(" and h.pk_defdoc9='"+nc.ui.wds.report.report1.ReportUI.pk_ruout+"'");//���˳���������
	 	sql.append(" and h.pk_corp='"+ClientEnvironment.getInstance().getCorporation().getPrimaryKey()+"'");								
		return sql.toString();
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
