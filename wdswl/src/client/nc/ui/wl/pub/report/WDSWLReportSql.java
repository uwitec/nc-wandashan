package nc.ui.wl.pub.report;

import nc.ui.pub.ClientEnvironment;
import nc.vo.pub.lang.UFBoolean;

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

}
