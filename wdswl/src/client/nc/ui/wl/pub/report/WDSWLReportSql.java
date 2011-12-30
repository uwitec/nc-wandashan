package nc.ui.wl.pub.report;

import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.bill.BillStatus;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.wl.pub.WdsWlPubConst;

public class WDSWLReportSql {	
	/**
	 * 
	 * @作者：mlr
	 * @说明：完达山物流项目 
	 *       只查询库存状态 ,用来设置货龄
	 * @时间：2011-7-17上午11:00:28
	 * @param invclcode 要过滤的存货分类的编码
	 * @param pk_stordoc  是否过滤仓库
	 * @param isType    是否按存货类型分组 即是否按常用存货和不常用存货类型来分组
	 * @param isInvcl   是否按存货分类分组
	 * @param isShowStore 是否按仓库分组
	 * @param iscargdoc   是否按货位分组
	 * @param isvbanchcode 是否按批次分组
	 * @param ddatefrom   查询开始日期
	 * @param ddateto     查询结束日期
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
		        sql.append(" select ");//仓库主键	
		        if(isShowStore.booleanValue()==true){
		        sql.append(" t.pk_customize1 pk_stordoc,");
		        sql.append(" min(s.storname) storename,");//仓库名称
		        }	
		        if(iscargdoc.booleanValue()==true){
		        sql.append(" t.pk_cargdoc pk_cargdoc,");
		        sql.append(" min(bc.csname) csname,");
		        }
		        if(isType.booleanValue()==true){
				sql.append(" iv.fuesed invtype,");//存货类型 常用0  不常用1
				}
			    if(isInvcl.booleanValue()==true){
				sql.append(" cl.pk_invcl pk_invcl,");//存货分类主键
				sql.append(" min(cl.vinvclname) invclname,");//存货分类名称		
			    }
		   //   sql.append(" cl.pk_invcl pk_invcl,");//存货分类主键
		   //   sql.append(" iv.fuesed invtype,");//存货类型 常用0  不常用1
		   //     sql.append(" min(cl.vinvclcode) invclcode,");//存货分类编码
		   //     sql.append(" min(cl.vinvclname) invclname,");//存货分类名称	
		        if(isvbanchcode.booleanValue()==true){
		        sql.append(" t.whs_batchcode vbatchcode,");		
		        }
     			sql.append(" min(t.ss_pk)  pk_storestate,");//存货状态主键
     			sql.append(" min(bcc.mainmeasrate)  hsl,");//换算率
		        sql.append(" t.pk_invbasdoc pk_invbasdoc,");		      
		        sql.append(" min(i.invcode) invcode,");//存货编码
		        sql.append(" min(i.invname) invname,");//存货名字
		        sql.append(" min(i.invspec) invspec,");//规格
		        sql.append(" round(sysdate-to_date(t.creadate,'YYYY-MM-DD HH24-MI-SS'),0) days,");//入库天数
		        sql.append(" t.creadate creadate,");//入库日期
		        sql.append(" t.ss_pk ss_pk,");//存货状态主键    
		        sql.append(" sum(t.whs_stocktonnage) num,");//库存中的单品的主数量 主要用来设置库龄主数量
		        sql.append(" sum(t.whs_stockpieces) bnum"); //库存中的单品的辅数量 主要用来设置库龄辅数量 
		        sql.append(" from ");	        	 
				sql.append(" tb_warehousestock t");
				sql.append(" join bd_stordoc s");//关联仓库
				sql.append(" on t.pk_customize1=s.pk_stordoc");
				sql.append(" join bd_invbasdoc i");//关联存货基本档案
				sql.append(" on t.pk_invbasdoc=i.pk_invbasdoc");	
				sql.append(" join wds_invbasdoc iv");//关联存货档案
				sql.append(" on t.pk_invbasdoc=iv.pk_invbasdoc");
				sql.append(" join wds_invcl cl");//关联存货分类
				sql.append(" on iv.vdef1=cl.pk_invcl");
				sql.append(" join bd_cargdoc bc");//关联货位档案
				sql.append(" on t.pk_cargdoc=bc.pk_cargdoc");//
				sql.append(" join  tb_stockstate ss");//关联库存状态表
				sql.append(" on t.ss_pk=ss.ss_pk");
				sql.append(" join bd_convert bcc");//关联换算率
				sql.append(" on t.pk_invbasdoc=bcc.pk_invbasdoc");
				sql.append(" where isnull(t.dr,0)=0");//
				sql.append(" and isnull(s.dr,0)=0");//
				sql.append(" and isnull(i.dr,0)=0");
				sql.append(" and isnull(iv.dr,0)=0");//
				sql.append(" and isnull(cl.dr,0)=0");
				sql.append(" and isnull(bc.dr,0)=0");
				sql.append(" and isnull(ss.dr,0)=0");
				sql.append(" and isnull(bc.dr,0)=0");
				sql.append(" and cl.vinvclcode like '"+invclcode+"%'");//过率属于箱粉的存货分类
				sql.append(" and upper(coalesce(ss.isok,'N'))='"+isnorm+"'");//过滤是否正常
				sql.append(" and t.creadate between '"+ddatefrom+"' and '"+ddateto+"'");//过滤入库日期
				if(pk_stordoc!=null && !pk_stordoc.equalsIgnoreCase("")){
				sql.append(" and t.pk_customize1='"+pk_stordoc+"'");//过滤仓库	
				}
				if(pk_invbasdoc!=null && !pk_invbasdoc.equalsIgnoreCase("")){
				sql.append(" and t.pk_invmandoc='"+pk_invbasdoc+"'");//过滤存货	
			    }
				sql.append(" and t.pk_corp='"+ClientEnvironment.getInstance().getCorporation().getPrimaryKey()+"'");
				sql.append(" group by ");	
			    if(isType.booleanValue()==true){
				sql.append(" iv.fuesed,");//存货类型 常用0  不常用1
				}
				if(isInvcl.booleanValue()==true){
				sql.append(" cl.pk_invcl,");//存货分类主键
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
     * @作者：mlr
     * @说明：完达山物流项目  
     * 根据自定义条件得到查询报表数据的SQL 只查询待发的运单
     * @时间：2011-5-10上午09:41:31
     * @param wheresql
     * @return
     */
	public static String getQuerySQL1(String invclcode,String pk_stordoc,String pk_invbasdoc,UFBoolean isType,UFBoolean isInvcl,UFBoolean isShowStore,UFBoolean iscargdoc,UFBoolean isvbanchcode,String ddatefrom,String ddateto){
		    StringBuffer sql = new StringBuffer();	
		    sql.append(" select ");
		    sql.append(" w.type type,");//在途或待发类型
		    if(isShowStore.booleanValue()==true){
		    sql.append(" w.pk_outwhouse pk_stordoc,");
		    sql.append(" min(s.storname) storename,");
		    }
		    if(isType.booleanValue()==true){
		    sql.append(" w.invtype invtype,");//存货类型 常用0  不常用1
		    }
		    if(isInvcl.booleanValue()==true){
		    sql.append(" w.invtype pk_invcl,");//存货分类主键
		    sql.append(" min(w.invclname) invclname,");//存货分类名称		
		    }
		    if(iscargdoc.booleanValue()==true){
		    sql.append(" w.pk_cargdoc pk_cargdoc,");
		    sql.append(" min(bc.csname) csname,");
		    }
		 //   sql.append(" w.pk_invcl pk_invcl,");//存货分类主键
	     //   sql.append(" w.invtype invtype,");//存货类型 常用0  不常用1
	     //   sql.append(" min(w.invclcode) invclcode,");//存货分类编码
	     //   sql.append(" min(w.invclname) invclname,");//存货分类名称	
//		    sql.append(" w.pk_invmandoc pk_invmandoc,");
		    sql.append(" w.pk_invbasdoc pk_invbasdoc,");
		    sql.append(" sum(w.plannum)   plannum,"); //计划 主数量 
			sql.append(" sum(w.bplannum)  bplannum,") ; //计划辅数量
	//		sql.append(" min(s.storname) storename,");//仓库名称
	        sql.append(" min(i.invcode) invcode,");//存货编码
	        sql.append(" min(i.invname) invname,");//存货名字
	        sql.append(" min(i.invspec) invspec");//规格	      
		    sql.append("  from");
	        sql.append("  ((select h.itransstatus type,");
			sql.append("  h.pk_outwhouse pk_outwhouse,"); 
			
			sql.append("  crg.pk_cargdoc pk_cargdoc,");//从货位存货绑定表获得货位
			
			
		    sql.append(" cl.pk_invcl pk_invcl,");//存货分类主键
		    sql.append(" iv.fuesed invtype,");//存货类型 常用0  不常用1
		    sql.append(" cl.vinvclcode invclcode,");//存货分类编码
		    sql.append(" cl.vinvclname invclname,");//存货分类名称	
			sql.append("  b.pk_invmandoc pk_invmandoc,");     
			sql.append("  b.pk_invbasdoc pk_invbasdoc,");   
			sql.append("  b.narrangnmu plannum,");   
			sql.append("  b.nassarrangnum bplannum") ;  
			sql.append("  from wds_soorder h");
			sql.append("  join wds_soorder_b b on h.pk_soorder = b.pk_soorder");
			sql.append("  join wds_invbasdoc iv");//关联存货档案
			sql.append("  on b.pk_invbasdoc=iv.pk_invbasdoc");
			sql.append("  join wds_invcl cl");//关联存货分类
			sql.append("  on iv.vdef1=cl.pk_invcl");	
			
			sql.append("  join (select h.pk_cargdoc, b.pk_invbasdoc, h.pk_stordoc from wds_cargdoc1 h join tb_spacegoods b on h.pk_wds_cargdoc = b.pk_wds_cargdoc  where nvl(h.dr, 0) = 0 and nvl(b.dr, 0) = 0) crg");//关联货位存货绑定主子表
			sql.append("  on b.pk_invbasdoc=crg.pk_invbasdoc and h.pk_outwhouse=crg.pk_stordoc ");
			sql.append("  where isnull(h.dr, 0) = 0");	
			sql.append("  and isnull(iv.dr,0)=0");//
			sql.append("  and isnull(b.dr,0)=0");//
			sql.append("  and isnull(cl.dr,0)=0");
		
			sql.append("  and cl.vinvclcode like '"+invclcode+"%'");//过率属于箱粉的存货分类
			sql.append("  and h.dmakedate between '"+ddatefrom+"' and '"+ddateto+"'");//过滤制单日期
			sql.append("  and h.pk_corp='"+ClientEnvironment.getInstance().getCorporation().getPrimaryKey()+"'");//过滤 公司

			sql.append("  and isnull(b.dr, 0) = 0 )");
			sql.append("  union all ");
			sql.append("   (select  h1.itransstatus type,");
			sql.append("  h1.pk_outwhouse pk_outwhouse,");   
			sql.append("  crg.pk_cargdoc pk_cargdoc,");//从货位存货绑定表获得货位
			sql.append(" cl.pk_invcl pk_invcl,");//存货分类主键
			sql.append(" iv.fuesed invtype,");//存货类型 常用0  不常用1
			sql.append(" cl.vinvclcode invclcode,");//存货分类编码
			sql.append(" cl.vinvclname invclname,");//存货分类名称	
			sql.append("  b1.pk_invmandoc pk_invmandoc,");  
			sql.append("  b1.pk_invbasdoc pk_invbasdoc,") ;
			sql.append("  b1.ndealnum plannum,");
			sql.append("  b1.nassdealnum bplannum"); 
			sql.append("  from wds_sendorder h1"); 
			sql.append("  join wds_sendorder_b b1 on h1.pk_sendorder = b1.pk_sendorder");
			sql.append("  join wds_invbasdoc iv");//关联存货档案
			sql.append("  on b1.pk_invbasdoc=iv.pk_invbasdoc");
			sql.append("  join wds_invcl cl");//关联存货分类
			sql.append("  on iv.vdef1=cl.pk_invcl");	
			sql.append("  join (select h.pk_cargdoc, b.pk_invbasdoc, h.pk_stordoc from wds_cargdoc1 h join tb_spacegoods b on h.pk_wds_cargdoc = b.pk_wds_cargdoc  where nvl(h.dr, 0) = 0 and nvl(b.dr, 0) = 0) crg");//关联货位存货绑定主子表
			sql.append("  on b1.pk_invbasdoc=crg.pk_invbasdoc and h1.pk_outwhouse=crg.pk_stordoc ");
			sql.append("  where isnull(h1.dr, 0) = 0");
			sql.append("  and isnull(b1.dr,0)=0");//
			sql.append("  and isnull(iv.dr,0)=0");//
			sql.append("  and isnull(cl.dr,0)=0");
		
			sql.append("  and cl.vinvclcode like '"+invclcode+"%'");//过率属于箱粉的存货分类
			sql.append("  and h1.dmakedate between '"+ddatefrom+"' and '"+ddateto+"'");//过滤制单日期
			sql.append("  and h1.pk_corp='"+ClientEnvironment.getInstance().getCorporation().getPrimaryKey()+"'");//过滤 公司

			sql.append("  and isnull(b1.dr, 0) = 0 ))w");
			sql.append("  join bd_stordoc s");//仓库档案
			sql.append("  on w.pk_outwhouse=s.pk_stordoc");
			sql.append("  left join bd_invbasdoc i");
			sql.append("  on w.pk_invbasdoc=i.pk_invbasdoc");
			sql.append("  join bd_cargdoc bc");//关联货位档案
			sql.append("  on w.pk_cargdoc=bc.pk_cargdoc");//
			sql.append("  where ");
			sql.append("  isnull(s.dr,0)=0");//
			sql.append("  and isnull(i.dr,0)=0");//
			sql.append("  and isnull(bc.dr,0)=0");
			sql.append("  and w.type=0");//过滤待发的运单
			if(pk_stordoc!=null && !pk_stordoc.equalsIgnoreCase("")){
			  sql.append(" and  w.pk_outwhouse='"+pk_stordoc+"'");	
		    }
			if(pk_invbasdoc!=null && !pk_invbasdoc.equalsIgnoreCase("")){
				sql.append(" and w.pk_invmandoc='"+pk_invbasdoc+"'");//过滤存货	
	        }
			//按仓库 存货 在途或待发类型来分组
			sql.append("  group by ");
		    if(isType.booleanValue()==true){
			sql.append(" w.invtype,");//存货类型 常用0  不常用1
			}
			if(isInvcl.booleanValue()==true){
			sql.append(" w.pk_invcl,");//存货分类主键
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
     * @作者：mlr
     * @说明：完达山物流项目  
     * 根据自定义条件得到查询报表数据的SQL 只查询在途的运单 
     *  
     * @时间：2011-5-10上午09:41:31
     * @param wheresql
     * @return
     */
	public static String getQuerySQL2(String invclcode,String pk_stordoc,String pk_invbasdoc,UFBoolean isType,UFBoolean isInvcl,UFBoolean isShowStore,UFBoolean iscargdoc,UFBoolean isvbanchcode,String ddatefrom,String ddateto){
		    StringBuffer sql = new StringBuffer();	
		    sql.append(" select ");
		    sql.append(" w.type type,");//在途或待发类型
		    if(isShowStore.booleanValue()==true){
		    sql.append(" w.pk_outwhouse pk_stordoc,");
		    sql.append(" min(s.storname) storename,");//仓库名称
		    }	
		    if(iscargdoc.booleanValue()==true){
		    sql.append(" w.pk_cargdoc pk_cargdoc,");
		    sql.append(" min(bc.csname) csname,");
		    }
		    if(isType.booleanValue()==true){
			sql.append(" w.invtype invtype,");//存货类型 常用0  不常用1
			}
			if(isInvcl.booleanValue()==true){
			sql.append(" w.invtype pk_invcl,");//存货分类主键
			sql.append(" min(w.invclname) invclname,");//存货分类名称		
			}
	//	    sql.append(" w.pk_invcl pk_invcl,");//存货分类主键
	//        sql.append(" w.invtype invtype,");//存货类型 常用0  不常用1
	//        sql.append(" min(w.invclcode) invclcode,");//存货分类编码
	//        sql.append(" min(w.invclname) invclname,");//存货分类名称	
		    if(isvbanchcode.booleanValue()==true){
		    sql.append(" w.vbatchcode vbatchcode,");		
		    }
		    sql.append(" w.pk_invbasdoc pk_invbasdoc,");		 		
	        sql.append(" min(i.invcode) invcode,");//存货编码
	        sql.append(" min(i.invname) invname,");//存货名字
	        sql.append(" min(i.invspec) invspec,");//规格
	        sql.append(" sum(w.plannum)   plannum,"); //计划 主数量 
			sql.append(" sum(w.bplannum)  bplannum") ; //计划辅数量
		    sql.append("  from");
	        sql.append("  ((select h.itransstatus type,");
			sql.append("  h.pk_outwhouse pk_outwhouse,"); 
			sql.append("  crg.pk_cargdoc pk_cargdoc,");//从货位存货绑定表获得货位
			sql.append(" cl.pk_invcl pk_invcl,");//存货分类主键
			sql.append(" iv.fuesed invtype,");//存货类型 常用0  不常用1
			sql.append(" cl.vinvclcode invclcode,");//存货分类编码
			sql.append(" cl.vinvclname invclname,");//存货分类名称	
			sql.append("  b.pk_invmandoc pk_invmandoc,");     
			sql.append("  b.pk_invbasdoc pk_invbasdoc,");  
			sql.append("  lb.vbatchcode vbatchcode,");//获得批次
			sql.append("  lb.noutnum plannum,");//实出数量   
			sql.append("  lb.noutassistnum bplannum") ; //实出辅数量 
			sql.append("  from wds_soorder h");
			sql.append("  join wds_soorder_b b on h.pk_soorder = b.pk_soorder");
			sql.append("  join wds_invbasdoc iv");//关联存货档案
			sql.append("  on b.pk_invbasdoc=iv.pk_invbasdoc");
			sql.append("  join wds_invcl cl");//关联存货分类
			sql.append("  on iv.vdef1=cl.pk_invcl");	

			sql.append("  join (select h.pk_cargdoc, b.pk_invbasdoc, h.pk_stordoc from wds_cargdoc1 h join tb_spacegoods b on h.pk_wds_cargdoc = b.pk_wds_cargdoc  where nvl(h.dr, 0) = 0 and nvl(b.dr, 0) = 0) crg");//关联货位存货绑定主子表
			sql.append("  on b.pk_invbasdoc=crg.pk_invbasdoc and h.pk_outwhouse=crg.pk_stordoc ");
			sql.append("  join tb_outgeneral_b lb");//关联出库单子表
			sql.append("  on h.pk_soorder=lb.csourcebillhid");
			sql.append("  and b.pk_soorder_b=lb.csourcebillbid");	
			sql.append("  where isnull(h.dr, 0) = 0");	
			sql.append("  and isnull(iv.dr,0)=0");//
			sql.append("  and isnull(b.dr,0)=0");//
			sql.append("  and isnull(cl.dr,0)=0");
			
			sql.append("  and isnull(lb.dr,0)=0");
			sql.append("  and lb.csourcetype='WDS5'");//过滤销售单据
			sql.append("  and cl.vinvclcode like '"+invclcode+"%'");//过率属于箱粉的存货分类
			sql.append("  and h.dmakedate between '"+ddatefrom+"' and '"+ddateto+"'");//过滤制单日期
			sql.append("  and h.pk_corp='"+ClientEnvironment.getInstance().getCorporation().getPrimaryKey()+"'");//过滤 公司
			sql.append("  and isnull(b.dr, 0) = 0 )");
			sql.append("  union all ");
			sql.append("   (select  h1.itransstatus type,");
			sql.append("  h1.pk_outwhouse pk_outwhouse,");   
			sql.append("  crg.pk_cargdoc pk_cargdoc,");//从货位存货绑定表获得货位
			sql.append(" cl.pk_invcl pk_invcl,");//存货分类主键
			sql.append(" iv.fuesed invtype,");//存货类型 常用0  不常用1
			sql.append(" cl.vinvclcode invclcode,");//存货分类编码
			sql.append(" cl.vinvclname invclname,");//存货分类名称	
			sql.append("  b1.pk_invmandoc pk_invmandoc,");  
			sql.append("  b1.pk_invbasdoc pk_invbasdoc,") ;
			sql.append("  lb.vbatchcode vbatchcode,");//获得批次
			sql.append("  lb.noutnum plannum,");//实出数量   
			sql.append("  lb.noutassistnum bplannum") ; //实出辅数量 
			sql.append("  from wds_sendorder h1"); 
			sql.append("  join wds_sendorder_b b1 on h1.pk_sendorder = b1.pk_sendorder");
			sql.append("  join wds_invbasdoc iv");//关联存货档案
			sql.append("  on b1.pk_invbasdoc=iv.pk_invbasdoc");
			sql.append("  join wds_invcl cl");//关联存货分类
			sql.append("  on iv.vdef1=cl.pk_invcl");	

			sql.append("  join (select h.pk_cargdoc, b.pk_invbasdoc, h.pk_stordoc from wds_cargdoc1 h join tb_spacegoods b on h.pk_wds_cargdoc = b.pk_wds_cargdoc  where nvl(h.dr, 0) = 0 and nvl(b.dr, 0) = 0) crg");//关联货位存货绑定主子表
			sql.append("  on b1.pk_invbasdoc=crg.pk_invbasdoc and h1.pk_outwhouse=crg.pk_stordoc ");
			sql.append("  join tb_outgeneral_b lb");//关联出库单子表
			sql.append("  on h1.pk_sendorder=lb.csourcebillhid");
			sql.append("  and b1.pk_sendorder_b=lb.csourcebillbid");	
			sql.append("  where isnull(h1.dr, 0) = 0");
			sql.append("  and isnull(b1.dr,0)=0");//
			sql.append("  and isnull(iv.dr,0)=0");//
			sql.append("  and isnull(cl.dr,0)=0");
		
			sql.append("  and isnull(lb.dr,0)=0");
			sql.append("  and lb.csourcetype='WDS3'");//过滤销售单据
			sql.append("  and cl.vinvclcode like '"+invclcode+"%'");//过率属于箱粉的存货分类
			sql.append("  and h1.dmakedate between '"+ddatefrom+"' and '"+ddateto+"'");//过滤制单日期
			sql.append("  and h1.pk_corp='"+ClientEnvironment.getInstance().getCorporation().getPrimaryKey()+"'");//过滤 公司
			sql.append("  and isnull(b1.dr, 0) = 0 ))w");
			sql.append("  join bd_stordoc s");//仓库档案
			sql.append("  on w.pk_outwhouse=s.pk_stordoc");
			sql.append("  join bd_invbasdoc i");
			sql.append("  on w.pk_invbasdoc=i.pk_invbasdoc");
			sql.append("  join bd_cargdoc bc");//关联货位档案
			sql.append("  on w.pk_cargdoc=bc.pk_cargdoc");//
			sql.append("  where ");
			sql.append("  isnull(s.dr,0)=0");//
			sql.append("  and isnull(i.dr,0)=0");//
			sql.append("  and isnull(bc.dr,0)=0");
			sql.append("  and w.type=1");//过滤在途的运单
			if(pk_stordoc!=null && !pk_stordoc.equalsIgnoreCase("")){
			  sql.append(" and  w.pk_outwhouse='"+pk_stordoc+"'");	
		    }
			if(pk_invbasdoc!=null && !pk_invbasdoc.equalsIgnoreCase("")){
				sql.append(" and w.pk_invmandoc='"+pk_invbasdoc+"'");//过滤存货	
	        }
			//按仓库 存货 在途或待发类型来分组
			sql.append("  group by ");
			if(isType.booleanValue()==true){
				sql.append(" w.invtype,");//存货类型 常用0  不常用1
			}
			if(isInvcl.booleanValue()==true){
			sql.append(" w.pk_invcl,");//存货分类主键
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
	 * 获得查询现存量的sql语句
	 * @作者：mlr
	 * @说明：完达山物流项目 
	 * @时间：2011-12-2下午03:05:50
	 * @param pk_corp
	 * @param whereSql
	 * @return
	 */
	public static String getStoreSql(String whereSql){
		StringBuffer sql = new StringBuffer();	
        sql.append(" select ");//仓库主键	
        sql.append(" h.pk_customize1 pk_stordoc,");
        sql.append(" h.pk_cargdoc pk_cargdoc,");//货位
        sql.append(" h.whs_batchcode vbatchcode,");//批次
		sql.append(" iv.fuesed invtype,");//存货类型 常用0  不常用1		
		sql.append(" cl.pk_invcl pk_invcl,");//存货分类主键
        sql.append(" h.whs_batchcode vbatchcode,");	//批次	
		sql.append(" h.ss_pk  pk_storestate,");//存货状态主键
        sql.append(" h.pk_invbasdoc pk_invbasdoc,");		      
        sql.append(" round(sysdate-to_date(h.creadate,'YYYY-MM-DD HH24-MI-SS'),0) days,");//入库天数
        sql.append(" h.creadate creadate,");//入库日期
        sql.append(" h.whs_stocktonnage num,");//库存中的单品的主数量 主要用来设置库龄主数量
        sql.append(" h.whs_stockpieces bnum"); //库存中的单品的辅数量 主要用来设置库龄辅数量 
        sql.append(" from ");	        	 
		sql.append(" tb_warehousestock h");
		sql.append(" join wds_invbasdoc iv");//关联存货档案
		sql.append(" on h.pk_invmandoc=iv.pk_invmandoc");
		sql.append(" join tb_stockstate st");//关联库存状态
		sql.append(" on h.ss_pk=st.ss_pk");
		sql.append(" left join wds_invcl cl ");//关联存货分类
		sql.append(" on iv.vdef1=cl.pk_invcl and nvl(cl.dr, 0) = 0");
		sql.append(" where isnull(h.dr,0)=0");//
		sql.append(" and isnull(iv.dr,0)=0");//
		sql.append(" and isnull(st.dr,0)=0");//
		if(whereSql!=null && whereSql.length()!=0)
		sql.append(" and "+whereSql);
		sql.append(" and h.pk_corp='"+ClientEnvironment.getInstance().getCorporation().getPrimaryKey()+"'");				
		return sql.toString();
	}
	/**
	 * 查询销售订单 关联  销售运单 关联 供应链的销售出库单
	 * @作者：mlr
	 * @说明：完达山物流项目 
	 * @时间：2011-12-28上午10:03:47
	 * @param whereSql
	 * @return
	 */
	public static String getOrdertoYunDan(String whereSql){
	    StringBuffer sql = new StringBuffer();			
		sql.append(" select h.vreceiptcode  ordercode, ");//单据号  
		sql.append(" h1.vbillno  ,");//运单号
		sql.append(" h1.pk_outwhouse pk_stordoc ,");//发货仓库
		sql.append(" h.dbilldate, ");  //订单日期
		sql.append(" h.ccustomerid ccustomerid, ");//  客商id   
		sql.append(" iv.fuesed chtype,");//存货类型 常用0  不常用1		
		sql.append(" cl.pk_invcl pk_invcl,");//存货分类主键
		sql.append(" b.cinventoryid pk_invmandoc,");  //存货管理id
		sql.append(" b.corder_bid  b_pk,");//销售订单子表id
		sql.append(" b1.pk_soorder_b b_pk1,");//销售运单子表id
		sql.append(" b.cinvbasdocid pk_invbasdoc, ");  //存货基本id 
		sql.append(" h1.vcardno carcode,");//车号
		sql.append(" h1.vdriver vdrivername,");//承运人
		sql.append(" h1.pk_transcorp pk_transcorp,");//承运公司
		sql.append(" h.dapprovedate sorderdate,");//收订单日期 （订单的签字时间）
	//	sql.append(" h1.dacceptdate sorderdate,");//收订单日期
		sql.append(" h1.ddispachdate cartime,");//派车时间
		sql.append(" h1.dbilldate forderdate,");//发订单日期
		sql.append(" h1.dsenddate djrfh,");//第几日发货
		sql.append(" h1.pk_sendareal pk_sendareal,");//销售区域
		sql.append(" h1.vtelphone jxstel,");//客商电话
		sql.append(" h1.nruntime zcyxtime,");//正常运行时间	
	//	sql.append(" b.nnumber, ");  //订单数量
	//	sql.append(" b1.noutnum num,");//物流销售出库单实发数量
	//	sql.append(" b.ntaldcnum,"); //订单累积出库数量  
		sql.append(" b2.general_b_pk get_pk,");//物流销售出库单表体id
		sql.append(" b2.vbatchcode vbantchcode,");//批次
		sql.append(" b2.noutnum nnum ,");//销售出库 实出数量
		sql.append(" b2.noutassistnum bnum ,");//销售出库单 实出辅数量
		sql.append(" b1.noutnum num, ");  //运单累积出库数量
	    sql.append(" h1.ntranprice yfprice,");//运费单价
	    sql.append(" h1.ngls yfgls,");//公里数
	    sql.append(" h1.ntransmny yfhj,");//运费		
	    sql.append(" b3.pk_loadprice_b1 b_pk2 ,");//装卸费核算单 表体id
	    sql.append(" b3.pk_loadprice h_pk,");//装卸费核算单 主表id
	    sql.append(" b3.nloadprice   zxfzx,");//装货费
	    sql.append(" b3.nunloadprice zxfzx1,");//卸货费
	    sql.append(" b3.ncodeprice zxftq,");//采码费
	    sql.append(" b3.ntagprice zxfcm,");//贴签费
	//	sql.append(" b1.nassoutnum ,");//累积出库辅数量	
		sql.append(" ich.pk_defdoc11");//出入库标示
		sql.append(" from so_sale h ");
		sql.append(" join so_saleorder_b b on h.csaleid = b.csaleid ");
		sql.append(" join wds_soorder_b b1 on b.corder_bid=b1.csourcebillbid ");
		sql.append(" join wds_soorder h1 on b1.pk_soorder=h1.pk_soorder ");
		sql.append(" join tb_outgeneral_b b2 on b1.pk_soorder_b=b2.csourcebillbid");//关联销售出库单  
		sql.append(" left join wds_loadprice_b1 b3 on b2.general_b_pk=b3.csourcebillbid and isnull(b3.dr,0)=0 ");//关联装卸费核算单
		sql.append(" left join ic_general_b icb on b.corder_bid=icb.csourcebillbid and isnull(icb.dr,0)=0");
		sql.append(" left join ic_general_h ich on icb.cgeneralhid=ich.cgeneralhid and isnull(ich.dr,0)=0");
		sql.append(" left join wds_invbasdoc iv");//关联存货档案
		sql.append(" on b.cinventoryid=iv.pk_invmandoc and isnull(iv.dr,0)=0");
		sql.append(" left join wds_invcl cl ");//关联存货分类
		sql.append(" on iv.vdef1=cl.pk_invcl and isnull(cl.dr, 0) = 0");
		sql.append(" where isnull(h.dr, 0) = 0 ");
		sql.append(" and isnull(b.dr, 0) = 0 ");
		sql.append(" and isnull(b1.dr,0) = 0");
		sql.append(" and isnull(h1.dr,0) =0 ");
		sql.append(" and isnull(b2.dr,0)=0");
		sql.append(" and h.fstatus = '"+BillStatus.AUDIT+"'");//查询审批通过的销售订单
		if(whereSql!=null && whereSql.length()!=0)
		sql.append(" and "+whereSql);
		sql.append(" and h.pk_corp='"+ClientEnvironment.getInstance().getCorporation().getPrimaryKey()+"'");				
		return sql.toString();		
	}
	/**
	 * 获得查询销售待发 数据的语句
	 * 查询  销售订单  和  物流销售出库单
	 * @作者：mlr
	 * @说明：完达山物流项目 
	 * @时间：2011-12-2下午03:05:50
	 * @param pk_corp
	 * @param whereSql
	 * @return
	 */
	public static String getOrderDaiFaSql(String whereSql){
		StringBuffer sql = new StringBuffer();			
		sql.append(" select h.vreceiptcode billcode, ");//单据号  
		sql.append(" tbh.pk_stordoc pk_stordoc,");//发货仓库
		sql.append(" h.dbilldate, ");  //订单日期
		sql.append(" h.ccustomerid, ");//  客商id   
//		sql.append(" h.dmakedate,");   //制单日期     
		sql.append(" iv.fuesed invtype,");//存货类型 常用0  不常用1		
		sql.append(" cl.pk_invcl pk_invcl,");//存货分类主键
		sql.append(" b.cinventoryid pk_invmandoc,");  //存货管理id
		sql.append(" b.corder_bid  b_pk,");//销售订单子表id
//		sql.append(" b.cunitid,");   //主单位
//		sql.append(" b.cpackunitid,"); //辅单位   
		sql.append(" b.cinvbasdocid pk_invbasdoc, ");  //存货基本id  
		sql.append(" b.nnumber, ");  //订单数量
		sql.append(" b.npacknumber, ");   //订单辅数量
		sql.append(" b1.noutnum num,");//物流销售出库单实发数量
		sql.append(" b1.noutassistnum bnum");//物流销售出库单实发辅数量
	//	sql.append(" b.ntaldcnum,"); //累积出库数量  
		sql.append(" from so_sale h ");
		sql.append(" join so_saleorder_b b on h.csaleid = b.csaleid ");
		sql.append(" join tb_storcubasdoc tb ");//关联分仓客商绑定
		sql.append(" on h.ccustomerid = tb.pk_cumandoc");
		sql.append(" join wds_storecust_h tbh");
		sql.append(" on tb.pk_wds_storecust_h = tbh.pk_wds_storecust_h");
		sql.append(" left join tb_outgeneral_b b1 on b.corder_bid=b1.cfirstbillbid and isnull(b1.dr,0)=0");
		sql.append(" left join wds_invbasdoc iv");//关联存货档案
		sql.append(" on b.cinventoryid=iv.pk_invmandoc and isnull(iv.dr,0)=0");
		sql.append(" left join wds_invcl cl ");//关联存货分类
		sql.append(" on iv.vdef1=cl.pk_invcl and isnull(cl.dr, 0) = 0");
		sql.append(" where isnull(h.dr, 0) = 0 ");
		sql.append(" and isnull(b.dr, 0) = 0 ");
		sql.append(" and isnull(tb.dr,0) = 0");
		sql.append(" and isnull(tbh.dr,0) =0 ");
		sql.append(" and h.fstatus = '"+BillStatus.AUDIT+"'");//查询审批通过的销售订单
		sql.append(" and "+whereSql);
		sql.append(" and h.pk_corp='"+ClientEnvironment.getInstance().getCorporation().getPrimaryKey()+"'");				
		return sql.toString();
	}
	/**
	 * 获得查询销售待发 数据的语句
	 * 查询  销售订单  供应链销售出库单 物流销售出库单
	 * @作者：mlr
	 * @说明：完达山物流项目 
	 * @时间：2011-12-2下午03:05:50
	 * @param pk_corp
	 * @param whereSql
	 * @return
	 */
	public static String getOrderDaiFaSql1(String whereSql){
		StringBuffer sql = new StringBuffer();			
		sql.append(" select h.vreceiptcode billcode, ");//单据号  
	//	sql.append(" h.dbilldate, ");  //订单日期
		sql.append(" h.ccustomerid, ");//  客商id   
		sql.append(" h.dapprovedate reordedate,");//收订单日期 （订单的签字时间）
		sql.append(" iv.fuesed chtype,");//存货类型 常用0  不常用1		
		sql.append(" cl.pk_invcl pk_invcl,");//存货分类主键
		sql.append(" b.cinventoryid pk_invmandoc,");  //存货管理id
		sql.append(" b.corder_bid  b_pk,");//销售订单子表id
	    sql.append(" b1.general_b_pk b_pk1,");//物流销售出库子表 id
		sql.append(" b.cinvbasdocid pk_invbasdoc, ");  //存货基本id  
		sql.append(" b.nnumber, ");  //订单数量
		sql.append(" b1.noutnum num,");//物流销售出库单实发数量
		sql.append(" b.ntaldcnum,"); //累积出库数量  
		sql.append(" ich.pk_defdoc11");//出入库标示
		sql.append(" from so_sale h ");
		sql.append(" join so_saleorder_b b on h.csaleid = b.csaleid ");	
		sql.append(" left join ic_general_b icb on b.corder_bid=icb.csourcebillbid and isnull(icb.dr,0)=0");
		sql.append(" left join ic_general_h ich on icb.cgeneralhid=ich.cgeneralhid and isnull(ich.dr,0)=0");
		sql.append(" left join tb_outgeneral_b b1 on b.corder_bid=b1.cfirstbillbid and isnull(b1.dr,0)=0");
		sql.append(" left join wds_invbasdoc iv");//关联存货档案
		sql.append(" on b.cinventoryid=iv.pk_invmandoc and isnull(iv.dr,0)=0");
		sql.append(" left join wds_invcl cl ");//关联存货分类
		sql.append(" on iv.vdef1=cl.pk_invcl and isnull(cl.dr, 0) = 0");
		sql.append(" where isnull(h.dr, 0) = 0 ");
		sql.append(" and isnull(b.dr, 0) = 0 ");
		sql.append(" and h.fstatus = '"+BillStatus.AUDIT+"'");//查询审批通过的销售订单
		if(whereSql!=null && whereSql.length()!=0)
		sql.append(" and "+whereSql);
		sql.append(" and h.pk_corp='"+ClientEnvironment.getInstance().getCorporation().getPrimaryKey()+"'");				
		return sql.toString();
	}
	/**
	 * 获得转分仓在途,到货等状态的查询语句
	 * 查询 发运订单 其他出库单
	 * @作者：mlr
	 * @说明：完达山物流项目 
	 * @时间：2011-12-2下午03:05:50
	 * @param pk_corp
	 * @param whereSql
	 * @return
	 */
	public static String getDmOrderSql(String whereSql){
		StringBuffer sql = new StringBuffer();	
		sql.append(" select  ");
//		sql.append(" h.vbillno ,");//发运订单订单号
//		sql.append(" h.dbilldate, ");//订单日期
//		sql.append(" h.pk_outwhouse,");//发货仓库
		sql.append(" h.itransstatus ,");//0 待发 1 在途 2 到货
		sql.append(" h.pk_inwhouse pk_stordoc,");//收货仓库
		sql.append(" iv.fuesed invtype,");//存货类型 常用0  不常用1		
		sql.append(" cl.pk_invcl pk_invcl,");//存货分类主键
		sql.append(" b.pk_invmandoc,");//存货管理档案id
		sql.append(" b.pk_invbasdoc ,");//存货基本档案id
		sql.append(" b1.vbatchcode vbatchcode,");//批次
//		sql.append(" b.nplannum ,");//计划数量
//		sql.append(" b.nassplannum,");//计划辅数量
//		sql.append(" b.ndealnum,");//安排数量
//		sql.append(" b.nassdealnum,");//安排辅数量
		sql.append(" b1.noutnum num,");//其他出库 实出
		sql.append(" b1.noutassistnum bnum");//其他出库实出辅数量
//		sql.append(" b.noutnum num,");//已经出库的数量
//		sql.append(" b.nassoutnum bnum");//已经出库的辅数量
	    sql.append(" from wds_sendorder h ");
		sql.append(" join wds_sendorder_b b");
		sql.append(" on h.pk_sendorder = b.pk_sendorder ");
		sql.append(" join tb_outgeneral_b b1");
		sql.append(" on b.pk_sendorder_b = b1.csourcebillbid");
		sql.append(" join wds_invbasdoc iv");//关联存货档案
		sql.append(" on b.pk_invmandoc=iv.pk_invmandoc");
		sql.append(" left join wds_invcl cl ");//关联存货分类
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
	 * 获得总仓转分仓虚拟出库
	 * 查询 供应链其他出库单 
	 * @作者：mlr
	 * @说明：完达山物流项目 
	 * @时间：2011-12-2下午03:05:50
	 * @param pk_corp
	 * @param whereSql
	 * @return
	 */
	public static String getZcXuNiSql(String whereSql){
		StringBuffer sql = new StringBuffer();	
		sql.append(" select  ");
		sql.append(" h.cwarehouseid  pk_stordoc ,");//出库仓库
		sql.append(" h.pk_defdoc9 ,");//是否虚拟
		sql.append(" iv.fuesed invtype,");//存货类型 常用0  不常用1		
		sql.append(" cl.pk_invcl pk_invcl,");//存货分类主键
		sql.append(" b.cinventoryid pk_invmandoc,");//存货管理档案主键
		sql.append(" b.cinvbasid pk_invbasdoc,");//存货基本档案主键
		sql.append(" b.noutnum num,");//实发数量
		sql.append(" b.noutassistnum bnum");//实发辅数量
		sql.append(" from ");
		sql.append(" ic_general_h h");
		sql.append(" join ic_general_b b");
		sql.append(" on h.cgeneralhid = b.cgeneralhid");
		sql.append(" join wds_invbasdoc iv");//关联存货档案
		sql.append(" on b.cinventoryid=iv.pk_invmandoc");
		sql.append(" left join wds_invcl cl ");//关联存货分类
		sql.append(" on iv.vdef1=cl.pk_invcl and isnull(cl.dr, 0) = 0");
	    sql.append(" where isnull(h.dr,0)=0");
	    sql.append("  and isnull(b.dr,0)=0");
	    sql.append("  and isnull(iv.dr,0)=0");
	    if(whereSql !=null && whereSql.length()!=0)
	 	sql.append(" and "+whereSql);
	    sql.append(" and h.cwarehouseid ='"+WdsWlPubConst.WDS_WL_ZC+"'");//出库仓库为总仓
	    sql.append(" and h.pk_defdoc9='"+nc.ui.wds.report.report1.ReportUI.pk_ruout+"'");//过滤出虚拟出库的
	 	sql.append(" and h.pk_corp='"+ClientEnvironment.getInstance().getCorporation().getPrimaryKey()+"'");								
		return sql.toString();
	}
	
	/**
	 * 获得查询物流销售出库 和 其他出库的sql
	 * 查询  销售出库 和 其他出库
	 * @作者：mlr
	 * @说明：完达山物流项目 
	 * @时间：2011-12-2下午03:05:50
	 * @param pk_corp
	 * @param whereSql
	 * @return
	 */
	public static String getOutStore(String whereSql){
		StringBuffer sql = new StringBuffer();	
		sql.append(" select h.srl_pk cwarehouseid, ");//出库仓库
		sql.append(" h.cdispatcherid stvcl,");//出入库类别
		sql.append(" h.dbilldate dbilldate,");//单据日期
		sql.append(" iv.fuesed invtype,");//存货类型 常用0  不常用1		
		sql.append(" cl.pk_invcl pk_invcl,");//存货分类主键
		sql.append(" b1.cinventoryid pk_invmandoc,");  //存货管理id
		sql.append(" b1.cinvbasid pk_invbasdoc, ");  //存货基本id  
		sql.append(" b1.noutnum num,");//物流销售出库单实发数量
		sql.append(" b1.noutassistnum bnum");//物流销售出库单实发辅数量	   
	    sql.append(" from tb_outgeneral_h h");//
		sql.append(" join tb_outgeneral_b b1 on h.general_pk=b1.general_pk ");
		sql.append(" left join wds_invbasdoc iv");//关联存货档案
		sql.append(" on b1.cinventoryid=iv.pk_invmandoc and isnull(iv.dr,0)=0");
		sql.append(" left join wds_invcl cl ");//关联存货分类
		sql.append(" on iv.vdef1=cl.pk_invcl and isnull(cl.dr, 0) = 0");
		sql.append(" where isnull(h.dr, 0) = 0 ");
		sql.append(" and isnull(b1.dr, 0) = 0 ");
		if(whereSql !=null && whereSql.length()!=0)
		sql.append(" and "+whereSql);
		sql.append(" and h.pk_corp='"+ClientEnvironment.getInstance().getCorporation().getPrimaryKey()+"'");				
		return sql.toString();
	}
	/**
	 * 获得查询物流其他入库 和 调拨入库的sql
	 * 查询  其他入库 和 调拨入库
	 * @作者：mlr
	 * @说明：完达山物流项目 
	 * @时间：2011-12-2下午03:05:50
	 * @param pk_corp
	 * @param whereSql
	 * @return
	 */
	public static String getInStore(String whereSql){
		StringBuffer sql = new StringBuffer();	
		sql.append(" select h.geh_cwarehouseid cwarehouseid, ");//出库仓库
		sql.append(" h.geh_cdispatcherid stvcl,");//出入库类别
		sql.append(" h.geh_dbilldate dbilldate,");//单据日期
		sql.append(" iv.fuesed invtype,");//存货类型 常用0  不常用1		
		sql.append(" cl.pk_invcl pk_invcl,");//存货分类主键
		sql.append(" b1.geb_cinventoryid pk_invmandoc,");  //存货管理id
		sql.append(" b1.geb_cinvbasid pk_invbasdoc, ");  //存货基本id  
		sql.append(" b1.geb_anum num,");//物流销售出库单实发数量
		sql.append(" b1.geb_banum bnum");//物流销售出库单实发辅数量	   
	    sql.append(" from tb_general_h h");//
		sql.append(" join tb_general_b b1 on h.geh_pk=b1.geh_pk ");
		sql.append(" left join wds_invbasdoc iv");//关联存货档案
		sql.append(" on b1.geb_cinventoryid=iv.pk_invmandoc and isnull(iv.dr,0)=0");
		sql.append(" left join wds_invcl cl ");//关联存货分类
		sql.append(" on iv.vdef1=cl.pk_invcl and isnull(cl.dr, 0) = 0");
		sql.append(" where isnull(h.dr, 0) = 0 ");
		sql.append(" and isnull(b1.dr, 0) = 0 ");
		if(whereSql !=null && whereSql.length()!=0)
		sql.append(" and "+whereSql);
		sql.append(" and h.pk_corp='"+ClientEnvironment.getInstance().getCorporation().getPrimaryKey()+"'");				
		return sql.toString();
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
