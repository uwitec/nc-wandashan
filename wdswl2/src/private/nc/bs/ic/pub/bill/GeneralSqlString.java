package nc.bs.ic.pub.bill;

import java.util.ArrayList;
import java.util.HashMap;

import nc.vo.ic.pub.SmartVOUtilExt;
import nc.vo.ic.pub.bill.GeneralBillHeaderVOMeta;
import nc.vo.ic.pub.bill.GeneralBillItemVOMeta;
import nc.vo.pub.BusinessException;

/**
 * 通用的SQL关联语句
 * 作者：王乃军
 * @version	最后修改日期(2002-2-5 19:34:00)
 * @see		需要参见的其它类
 * @since		从产品的那一个版本，此类被添加进来。（可选）
 * 修改人 + 修改日期 
 * 修改说明
 */
public class GeneralSqlString 
{
	public static String V_IC_ONHANDNUM=" ic_onhandnum ";
	public static String V_IC_ONHANDNUM_B=" ic_onhandnum_b ";

	
	public final static int IN_LIMITED = 100;
	
	static public String sBd_Convert =
		" (SELECT pk_invbasdoc || pk_measdoc AS pk_convert,pk_invbasdoc,pk_measdoc, mainmeasrate AS  mainmeasrate ,fixedflag AS fixedflag from bd_convert UNION  ( SELECT  pk_invbasdoc || pk_measdoc  AS pk_convert,pk_invbasdoc,pk_measdoc,1  AS  mainmeasrate ,'Y'  AS fixedflag FROM bd_invbasdoc)) ";
	static public String sBd_ConvertJoin =
		"(conv.pk_invbasdoc=conv102.pk_invbasdoc OR conv102.pk_invbasdoc IS NULL)";
	//只有主计量单位的转换率	
	static public String sBd_ConvertMainUom =
		" (SELECT  pk_invbasdoc || pk_measdoc AS pk_convert,pk_invbasdoc,pk_measdoc,1 AS mainmeasrate ,'Y' AS fixedflag ,isstorebyconvert  FROM bd_invbasdoc) ";
	//请同步维护getAllBodyFieldName()
	//旧方法 目前在单据参照中使用hanwei 2003-10-26
	public final static String SQL_ALL_BODY_FIELD =
		" /*+ index(body) */ body.fbillrowflag,body.castunitid,    meas2.measname AS castunitname, body.ccorrespondbid,     body.ccorrespondcode, body.ccorrespondhid, body.ccorrespondtype,  \n    body.ccostobject ,invbasz.invname as ccostobjectname, body.cgeneralbid, inv.invcode AS cinventorycode,     body.cinventoryid, invman.wholemanaflag AS isLotMgt,  \n    invman.serialmanaflag AS isSerialMgt,     invman.qualitymanflag AS isValidateMgt, inv.setpartsflag AS isSet,  \n    inv.storeunitnum AS standStoreUOM, inv.pk_measdoc3 AS defaultAstUOM,    invman.sellproxyflag AS isSellProxy, invman.qualitydaynum AS qualityDay,  \n    conv102.fixedflag AS isSolidConvRate, body.cprojectid,     job.jobname AS cprojectname, body.csourcebillbid, body.csourcebillhid,  \n    body.csourcetype, body.cfirstbillbid, body.cfirstbillhid, body.cfirsttype,    body.dbizdate, body.dstandbydate, body.dvalidate, body.fchecked,   body.flargess, conv102.mainmeasrate AS hsl, inv.invname, inv.invspec,  \n    inv.invtype, body.isok, meas1.measname AS measdocname,     body.ninassistnum, body.ninnum, body.nmny, body.nneedinassistnum,  \n    body.noutassistnum, body.noutnum, body.nplannedprice, body.nplannedmny,    body.nprice, body.nshouldinnum, body.nshouldoutassistnum,   body.nshouldoutnum, body.vbatchcode, body.vfree1, body.vfree2,  \n    body.vfree3, body.vfree4, body.vfree5, body.vfree6, body.vfree7, body.vfree8,     body.vfree9, body.vfree10, body.vproductbatch, body.vsourcebillcode, body.vfirstbillcode, \n    body.vuserdef1, body.vuserdef2, body.vuserdef3, body.vuserdef4,    body.vuserdef5, body.vuserdef6, inv.assistunit AS isAstUOMmgt,  \n    invman.pk_invmandoc ,substring(CONVERT(varchar, dateadd(day, - invman.qualitydaynum,body.dvalidate), 21), 1, 10) AS scrq  \n    ,body.vuserdef7, body.vuserdef8, body.vuserdef9, body.vuserdef10 \n    ,bt.billtypename AS csourcetypename ,bt2.billtypename AS ccorrespondtypename ,body.cprojectphaseid ,jobph.jobphasename AS cprojectphasename \n    ,body.ntranoutnum,body.ntranoutastnum    ,job.jobcode AS cprojectcode   ,jobph.jobphasecode AS cprojectphasecode, body.cfreezeid ,invman.outpriority,invman.outtrackin,invman.negallowed ,body.cvendorid, vendbas.custname AS vvendorname, body.ts AS bodyts, body.cwp ,body.vnotebody,body.creceieveid,recustbas.custname as vrevcustname,body.crowno,body.nreplenishednum,body.nreplenishedastnum,body.cinvbasid,body.corder_bb1id ,body.drequiredate,body.drequiretime ,body.vsourcerowno,body.ncountnum,body.pk_packsort,body.bsafeprice,body.breturnprofit,body.nsaleprice,body.nsalemny,body.ntaxprice,body.ntaxmny,body.vbodynote2,btfirst.billtypename as cfirsttypename,body.ccheckstateid,body.bsourcelargess,body.nretnum,body.nretastnum,body.nretgrossnum";
	public final static int SQL_ALL_BODY_FIELD_NUM = 127;
	
		public final static String SQL_ALL_HEAD_FIELD = //vuserdef1h
	"/*+ index(head) */ head.cgeneralhid, head.cbilltypecode, head.cbizid, 	psn5.psnname AS cbizname, head.cbiztype, biz.businame AS cbiztypename,   head.ccustomerid, cust.custname AS ccustomername, head.cdilivertypeid,    send.sendname AS cdilivertypename, head.cdispatcherid,   \n    rdcl.rdname cdispatchername, head.cdptid, dept.deptname AS cdptname,  \n    head.cinventoryid AS cinventoryidh,   \n    headinv.invname AS cinventorynameh, head.cproviderid,  \n    prov.custname AS cprovidername, head.cwarehouseid,  \n    wh1.storname AS cwarehousename, wh1.csflag AS isLocatorMgt,  \n    wh1.gubflag AS isWasteWh, head.cwastewarehouseid,     wh2.storname AS cwastewarehousename, head.cwhsmanagerid,  \n    psn1.psnname AS cwhsmanagername, wh2.gubflag AS isWasteWh2,    head.daccountdate, head.dauditdate, head.dbilldate, head.fbillflag,  \n    head.fspecialflag, head.pk_corp, head.vbillcode, head.vdiliveraddress,     head.vnote, head.vuserdef1 AS vuserdef1h, head.vuserdef2 AS vuserdef2h,  \n    head.vuserdef3 AS vuserdef3h, head.vuserdef4 AS vuserdef4h,   head.vuserdef5 AS vuserdef5h, head.vuserdef6 AS vuserdef6h,  \n    head.vuserdef7 AS vuserdef7h, head.vuserdef8 AS vuserdef8h,      head.vuserdef9 AS vuserdef9h, head.vuserdef10 AS vuserdef10h,  \n    head.cauditorid, psn2.user_name AS cauditorname, head.coperatorid,   head.pk_calbody, cal.bodyname vcalbodyname, \n    psn3.user_name AS coperatorname, head.cregister,    psn4.user_name AS cregistername ,head.ts AS headts,head.freplenishflag,head.vheadnote2,head.fallocflag,head.coutcorpid,head.coutcalbodyid,head.cothercorpid,head.cothercalbodyid,ndiscountmny,nnetmny,cotherwhid";
	
	//#####################################################
	public final static int SQL_ALL_HEAD_FIELD_NUM = 65;
		public final static String SQL_FROM_BILL =
		" ic_general_h head INNER JOIN ic_general_b body ON head.cgeneralhid=body.cgeneralhid ";

	//IN 数量限制
	public final static int SQL_IN_LIST_LIMIT = 200;
	public final static String SQL_JOIN_BODY_CUST =
		" LEFT OUTER JOIN bd_cumandoc recustman ON   body.creceieveid = recustman.pk_cumandoc INNER  JOIN   bd_cubasdoc recustbas ON  recustman.pk_cubasdoc = recustbas.pk_cubasdoc";
	public final static String SQL_JOIN_JOB =
		"		LEFT OUTER JOIN bd_jobmngfil jobman ON body.cprojectid = jobman.pk_jobmngfil INNER JOIN bd_jobbasfil job ON jobman.pk_jobbasfil = job.pk_jobbasfil ";
	public final static String SQL_JOIN_JOBPHASE =
		"		LEFT OUTER JOIN bd_jobobjpha jobobj ON body.cprojectphaseid=jobobj.pk_jobobjpha INNER JOIN bd_jobphase jobph ON jobobj.pk_jobphase=jobph.pk_jobphase ";
	public final static String SQL_JOIN_BODY_VENDOR =
		" LEFT OUTER JOIN bd_cumandoc vendman ON   body.cvendorid = vendman.pk_cumandoc INNER  JOIN   bd_cubasdoc vendbas ON  vendman.pk_cubasdoc = vendbas.pk_cubasdoc";
	public final static String SQL_JOIN_CALBODY =
		" LEFT OUTER JOIN bd_calbody cal ON head.pk_calbody=cal.pk_calbody ";
	
	/**
	 * 功能：根据voMeta中的持久化属性返回需要查询的数据库字段。
	 * 参数：
	 * 返回：
	 * 例外：
	 * 日期：(2004-11-9 20:35:23)
	 * 修改日期，修改人，修改原因，注释标志：
	 * 
	 * @return java.lang.String
	 * @param voMeta nc.vo.scm.pub.smart.SmartVOMeta
	 * @param sTableAlias String
	 */
	public static String getSelectFields(Class voClass, String sTableAlias) throws BusinessException {
		nc.vo.scm.pub.smart.ISmartVO vo = null;
		
		try {
	    vo = (nc.vo.scm.pub.smart.ISmartVO) voClass.newInstance();
	    
	    StringBuffer sbFields = null;
	    nc.vo.scm.pub.smart.SmartFieldMeta fmeta = null;
	    nc.vo.scm.pub.smart.ISmartVOMeta voMeta = vo.getVOMeta();
	    int ma = 0;
	    if (voMeta != null) {
	    	Object[] fmetas = voMeta.getColumnsIndexByName().values().toArray(); //name
	    	sbFields = new StringBuffer();
	    	if (sTableAlias != null) {
	    		for (int i = 0, j = fmetas.length; i < j; i++) {
	    			fmeta = (nc.vo.scm.pub.smart.SmartFieldMeta) fmetas[i];
	    			if (fmeta.isPersistence() || fmeta.getColumn().toUpperCase().equals("TS")) {
	    				ma++;
	    				if (ma != 1)
	    					sbFields.append(" , ");
	    				sbFields.append(sTableAlias);
	    				sbFields.append(".");
	    				sbFields.append(fmeta.getColumn());

	    			}
	    		}
	    	}else{
	    		for (int i = 0, j = fmetas.length; i < j; i++) {
	    			fmeta = (nc.vo.scm.pub.smart.SmartFieldMeta) fmetas[i];
	    			if (fmeta.isPersistence() || fmeta.getColumn().toUpperCase().equals("TS")) {
	    				ma++;
	    				if (ma != 1)
	    					sbFields.append(" , ");
	    				sbFields.append(fmeta.getColumn());

	    			}
	    		}
	    	}
	    }

	    if (sbFields != null)
	    	return sbFields.toString();
	    else
	    	return null;
	  }
	  catch (Exception e) {
	    //日志异常
	    nc.vo.scm.pub.SCMEnv.out(e);
	    //库存组异常抛出规范
	    throw new BusinessException(e);
//	    throw nc.bs.ic.pub.GenMethod.handleException(e.getMessage(), e);
	  }
	}
	
	public final static String SQL_JOIN_ALL_BODY =
		"\n"
			+ SQL_JOIN_BODY_CUST
			+ "\n LEFT OUTER JOIN  bd_billtype btfirst ON body.cfirsttype = btfirst.pk_billtypecode "
			+ "\n LEFT OUTER JOIN bd_invmandoc invman  ON body.cinventoryid = invman.pk_invmandoc INNER  JOIN  bd_invbasdoc inv ON  	invman.pk_invbasdoc=inv.pk_invbasdoc "
			+ "\n  LEFT OUTER JOIN bd_measdoc meas1 ON  inv.pk_measdoc = meas1.pk_measdoc "
			+ "\n  LEFT OUTER JOIN bd_invcl invcl ON inv.pk_invcl = invcl.pk_invcl "
			+ "\n  LEFT OUTER JOIN  bd_measdoc meas2 ON  body.castunitid = meas2.pk_measdoc"
			+ "\n  LEFT OUTER JOIN bd_billtype bt ON body.csourcetype=bt.pk_billtypecode "
			+ "\n  LEFT OUTER JOIN bd_billtype bt2 ON body.ccorrespondtype=bt2.pk_billtypecode "
			+ "\n  LEFT OUTER JOIN  "
			+ sBd_ConvertMainUom
			+ "  conv ON inv.pk_invbasdoc = conv.pk_invbasdoc "
			+ "\n  LEFT OUTER JOIN  "
			+ sBd_Convert
			+ "  conv102 ON body.castunitid=conv102.pk_measdoc \n"
			+ "\n LEFT OUTER JOIN bd_invmandoc invmanz ON body.ccostobject=invmanz.pk_invmandoc  INNER JOIN bd_invbasdoc invbasz 	ON invmanz.pk_invbasdoc=invbasz.pk_invbasdoc  "
			+ "\n "
			+ SQL_JOIN_JOB
			+ "\n "
			+ SQL_JOIN_JOBPHASE
			+ "\n "
			+ SQL_JOIN_BODY_VENDOR;

	public final static String SQL_JOIN_ALL_BODY2 =
		"\n LEFT OUTER JOIN bd_invmandoc invman2  ON body2.cinventoryid = invman2.pk_invmandoc INNER  JOIN  bd_invbasdoc inv2 ON  	invman2.pk_invbasdoc=inv2.pk_invbasdoc "
			+ "\n  LEFT OUTER JOIN bd_measdoc meas12 ON  inv2.pk_measdoc = meas12.pk_measdoc "
			+ "\n  LEFT OUTER JOIN bd_invcl invcl2 ON inv2.pk_invcl = invcl2.pk_invcl "
			+ "\n  LEFT OUTER JOIN  bd_measdoc meas22 ON  body2.castunitid = meas22.pk_measdoc"
			+ "\n  LEFT OUTER JOIN bd_billtype bt10 ON body2.csourcetype=bt10.pk_billtypecode "
			+ "\n  LEFT OUTER JOIN bd_billtype bt20 ON body2.ccorrespondtype=bt20.pk_billtypecode "
			+ "\n  LEFT OUTER JOIN  "
			+ sBd_ConvertMainUom
			+ "  conv2 ON inv.pk_invbasdoc = conv2.pk_invbasdoc "
			+ "\n  LEFT OUTER JOIN  "
			+ sBd_Convert
			+ "  conv202 ON body.castunitid=conv202.pk_measdoc "
			+ "\n	LEFT OUTER JOIN bd_jobmngfil jobman2 ON body2.cprojectid = jobman2.pk_jobmngfil INNER JOIN bd_jobbasfil job2 ON jobman2.pk_jobbasfil = job2.pk_jobbasfil "
			+ "\n	LEFT OUTER JOIN bd_jobobjpha jobobj2 ON body2.cprojectphaseid=jobobj2.pk_jobobjpha INNER JOIN bd_jobphase jobph2 ON jobobj2.pk_jobphase=jobph2.pk_jobphase ";
	public final static String SQL_JOIN_ALL_HEAD =
		"\n LEFT OUTER JOIN bd_rdcl rdcl ON head.cdispatcherid = rdcl.pk_rdcl "
			+ "\n  LEFT OUTER JOIN bd_deptdoc dept ON head.cdptid = dept.pk_deptdoc "
			+ "\n  LEFT OUTER JOIN bd_stordoc wh1 ON head.cwarehouseid = wh1.pk_stordoc "
			+ "\n  LEFT OUTER JOIN bd_stordoc wh2 ON head.cwastewarehouseid = wh2.pk_stordoc "
			+ "\n  LEFT OUTER JOIN bd_cumandoc provman ON   head.cproviderid = provman.pk_cumandoc INNER  JOIN  bd_cubasdoc prov ON   provman.pk_cubasdoc = prov.pk_cubasdoc "
			+ "\n  LEFT OUTER JOIN bd_cumandoc custman ON   head.ccustomerid = custman.pk_cumandoc INNER  JOIN   bd_cubasdoc cust ON  custman.pk_cubasdoc = cust.pk_cubasdoc"
			+ "\n  LEFT OUTER JOIN bd_busitype biz ON head.cbiztype = biz.pk_busitype "
			+ "\n  LEFT OUTER JOIN bd_sendtype send ON head.cdilivertypeid = send.pk_sendtype "
			+ "\n  LEFT OUTER JOIN bd_psndoc psn1 ON head.cwhsmanagerid = psn1.pk_psndoc "
			+ "\n  LEFT OUTER JOIN sm_user psn2 ON head.cauditorid = psn2.cUserID "
			+ "\n  LEFT OUTER JOIN sm_user psn3 ON head.coperatorid = psn3.cUserID "
			+ "\n  LEFT OUTER JOIN sm_user psn4 ON head.cregister = psn4.cUserID "
			+ "\n  LEFT OUTER JOIN bd_psndoc psn5 ON head.cbizid = psn5.pk_psndoc  "
			+ "\n  LEFT OUTER JOIN bd_invmandoc headinvman  ON head.cinventoryid = headinvman.pk_invmandoc INNER    JOIN bd_invbasdoc headinv   ON headinvman.pk_invbasdoc=headinv.pk_invbasdoc"
			+ SQL_JOIN_CALBODY;
//添加corder_bb1id字段，by hanwei 2004-03-08
	//ydy请维护输组IC_GENERAL_B_FIELDS
	private static String SQL_PURE_BODY_FIELD =null;
		//#####################################################
	//新增加字段需要维护下面的代码 
	//请同步维护getPureHeadFieldName()
	//用于对单个单据查询
	private  static String SQL_PURE_HEAD_FIELD =null;
	
	public final static String SQL_PURE_HEAD_FIELD_ALIAS_HEAD =
		"/*+ index(head) */ head.cgeneralhid,head.cbilltypecode,head.cbizid,head.cbiztype,head.ccustomerid,head.cdilivertypeid,head.cdispatcherid,head.cdptid,head.cinventoryid,head.cproviderid,head.cwarehouseid,head.cwastewarehouseid,head.cwhsmanagerid,head.daccountdate,head.dauditdate,head.dbilldate,head.fbillflag,head.fspecialflag,head.pk_corp,head.vbillcode,head.vdiliveraddress,head.vnote,head.vuserdef1,head.vuserdef2,head.vuserdef3,head.vuserdef4,head.vuserdef5,head.vuserdef6,head.vuserdef7,head.vuserdef8,head.vuserdef9,head.vuserdef10,head.cauditorid,head.coperatorid,head.cregister,head.pk_calbody,head.ts,freplenishflag,vheadnote2,head.fallocflag,coutcorpid,coutcalbodyid,cothercorpid,cothercalbodyid,head.bdirecttranflag,head.ndiscountmny,head.nnetmny,head.cotherwhid";
	public final static int PURE_SPECIAL_BODY_NUM = 89;
	//特殊单纯字段，表头 cqw 2003-06-26
	public final static int PURE_SPECIAL_HEAD_NUM = 67;
	
	//特殊单纯字段，表体 cqw 2003-06-26
	public final static String SQL_PURE_SPECIAL_B =
		"/*+ index(body) */  body.castunitid,body.cfirstbillbid,body.cfirstbillhid,body.cfirsttype,body.cinventoryid,body.csourcebillbid,body.csourcebillhid,body.csourcetype,body.cspaceid,body.cspecialbid,body.cspecialhid,body.cwarehouseid,body.dr,body.dshldtransnum,body.dvalidate,body.fbillrowflag,body.naccountastnum,body.naccountnum,body.nadjustastnum,body.nadjustnum,body.ncheckastnum,body.nchecknum,body.nprice,body.nshldtransastnum,body.ts,body.vbatchcode,body.vsourcebillcode,body.vfree1,body.vfree2,body.vfree3,body.vfree4,body.vfree5,body.vfree6,body.vfree7,body.vfree8,body.vfree9,body.vfree10,body.vuserdef1,body.vuserdef2,body.vuserdef3,body.vuserdef4,body.vuserdef5,body.vuserdef6,body.vuserdef7,body.vuserdef8,body.vuserdef9,body.vuserdef10,body.vuserdef11,body.vuserdef12,body.vuserdef13,body.vuserdef14,body.vuserdef15,body.vuserdef16,body.vuserdef17,body.vuserdef18,body.vuserdef19,body.vuserdef20,body.pk_defdoc1,body.pk_defdoc2,body.pk_defdoc3,body.pk_defdoc4,body.pk_defdoc5,body.pk_defdoc6,body.pk_defdoc7,body.pk_defdoc8,body.pk_defdoc9,body.pk_defdoc10,body.pk_defdoc11,body.pk_defdoc12,body.pk_defdoc13,body.pk_defdoc14,body.pk_defdoc15,body.pk_defdoc16,body.pk_defdoc17,body.pk_defdoc18,body.pk_defdoc19,body.pk_defdoc20,body.crowno,body.nplannedmny,body.nplannedprice,body.vnote AS vbodynote,nperiodnum,nperiodastnum,body.cvendorid,hsl,naccountgrsnum,ncheckgrsnum,nadjustgrsnum,nshldtransgrsnum ";
	public final static String SQL_PURE_SPECIAL_H =
		"/*+ index(head) */ head.cspecialhid, head.cauditorid,head.cbilltypecode,head.cinbsrid,head.cindeptid,head.cinwarehouseid,head.coperatorid,head.coutbsor,head.coutdeptid,head.coutwarehouseid,head.cshlddiliverdate,head.dbilldate,head.dr,head.fbillflag,head.nfixdisassemblymny,head.pk_corp,head.ts,head.vadjuster,head.vbillcode,head.vnote,head.vshldarrivedate,head.vuserdef1 as vuserdef1h,head.vuserdef2 as vuserdef2h,head.vuserdef3 as vuserdef3h,head.vuserdef4 as vuserdef4h,head.vuserdef5 as vuserdef5h,head.vuserdef6 as vuserdef6h,head.vuserdef7 as vuserdef7h,head.vuserdef8 as vuserdef8h,head.vuserdef9 as vuserdef9h,head.vuserdef10 as vuserdef10h, head.vuserdef11 as vuserdef11h ,head.vuserdef12 as vuserdef12h,head.vuserdef13 as vuserdef13h,head.vuserdef14 as vuserdef14h,head.vuserdef15 as vuserdef15h,head.vuserdef16 as vuserdef16h,head.vuserdef17 as vuserdef17h,head.vuserdef18 as vuserdef18h,head.vuserdef19 as vuserdef19h,head.vuserdef20 as vuserdef20h,head.pk_defdoc1 as pk_defdoc1h,head.pk_defdoc2 as pk_defdoc2h,head.pk_defdoc3 as pk_defdoc3h,head.pk_defdoc4 as pk_defdoc4h,head.pk_defdoc5 as pk_defdoc5h,head.pk_defdoc6 pk_defdoc6h,head.pk_defdoc7 as pk_defdoc7h,head.pk_defdoc8 as pk_defdoc8h,head.pk_defdoc9 as pk_defdoc9h,head.pk_defdoc10 as pk_defdoc10h,head.pk_defdoc11 as pk_defdoc11h,head.pk_defdoc12 as pk_defdoc12h,head.pk_defdoc13 as pk_defdoc13h,head.pk_defdoc14 as pk_defdoc14h,head.pk_defdoc15 as pk_defdoc15h,head.pk_defdoc16 as pk_defdoc16h,head.pk_defdoc17 as pk_defdoc17h,head.pk_defdoc18 as pk_defdoc18h,head.pk_defdoc19 as pk_defdoc19h,head.pk_defdoc20 as pk_defdoc20h, fassistantflag,icheckmode, head.iprintcount,head.clastmodiid,head.tlastmoditime, head.tmaketime ";
	//--跟踪入库存货的现存量
	public final static String SQL_TRACK_ONHAND=
	"SELECT h.pk_corp, b.dbizdate, h.cbilltypecode, h.vbillcode, h.cgeneralhid, b.cgeneralbid,    h.pk_calbody AS ccalbodyid, h.cwarehouseid, b.cinventoryid, b.vfree1, b.vfree2,       b.vfree3, b.vfree4, b.vfree5, b.vfree6, b.vfree7, b.vfree8, b.vfree9, b.vfree10,       b.vbatchcode, b.dvalidate, b.castunitid, b.ninnum, b.ninassistnum, b.noutnum,       b.noutassistnum FROM ic_general_h h, ic_general_b b,bd_invmandoc WHERE ((h.cbilltypecode = '40' OR      h.cbilltypecode = '41' OR      h.cbilltypecode = '44' OR      (h.cbilltypecode = '45' AND b.fchecked = 0 AND (h.cbiztype NOT IN          (SELECT pk_busitype         FROM bd_busitype         WHERE verifyrule = 'J') OR      h.cbiztype IS NULL)) OR      h.cbilltypecode = '46' OR      h.cbilltypecode = '47' OR      h.cbilltypecode = '48' OR      h.cbilltypecode = '49' OR      h.cbilltypecode = '4A' OR      h.cbilltypecode = '4B') AND (b.ninnum IS NOT NULL AND b.ninnum > 0) OR      ((h.cbilltypecode = '4C' AND (h.cbiztype NOT IN          (SELECT pk_busitype         FROM bd_busitype         WHERE verifyrule = 'C') OR      h.cbiztype IS NULL)) OR      h.cbilltypecode = '4D' OR      h.cbilltypecode = '4E' OR      h.cbilltypecode = '4F' OR      h.cbilltypecode = '4G' OR      h.cbilltypecode = '4H' OR      h.cbilltypecode = '4I' OR      h.cbilltypecode = '4J' OR      h.cbilltypecode = '4O') AND (b.noutnum IS NOT NULL AND b.noutnum < 0)) AND       h.dr = 0 AND b.dr = 0 AND h.cgeneralhid = b.cgeneralhid 	and b.cinventoryid=bd_invmandoc.pk_invmandoc and outtrackin='Y' UNION ALL (SELECT h.pk_corp, b.dbizdate, b.ccorrespondtype, b.ccorrespondcode, b.ccorrespondhid,       b.ccorrespondbid, h.pk_calbody AS ccalbodyid, h.cwarehouseid, b.cinventoryid,       b.vfree1, b.vfree2, b.vfree3, b.vfree4, b.vfree5, b.vfree6, b.vfree7, b.vfree8, b.vfree9,       b.vfree10, b.vbatchcode, b.dvalidate, b.castunitid, b.ninnum, b.ninassistnum,       b.noutnum, b.noutassistnum FROM ic_general_h h, ic_general_b b,bd_invmandoc WHERE ((h.cbilltypecode = '40' OR      h.cbilltypecode = '41' OR      h.cbilltypecode = '44' OR      (h.cbilltypecode = '45' AND b.fchecked = 0 AND (h.cbiztype NOT IN          (SELECT pk_busitype         FROM bd_busitype         WHERE verifyrule = 'J') OR      h.cbiztype IS NULL)) OR      h.cbilltypecode = '46' OR      h.cbilltypecode = '47' OR      h.cbilltypecode = '48' OR      h.cbilltypecode = '49'  OR      h.cbilltypecode = '4E' OR      h.cbilltypecode = '4A' OR      h.cbilltypecode = '4B') AND (b.ninnum IS NOT NULL AND b.ninnum < 0) OR      ((h.cbilltypecode = '4C' AND (h.cbiztype NOT IN          (SELECT pk_busitype         FROM bd_busitype         WHERE verifyrule = 'C') OR      h.cbiztype IS NULL)) OR      h.cbilltypecode = '4D' OR      h.cbilltypecode = '4Y' OR      h.cbilltypecode = '4F' OR      h.cbilltypecode = '4G' OR      h.cbilltypecode = '4H' OR      h.cbilltypecode = '4I' OR      h.cbilltypecode = '4J' OR      h.cbilltypecode = '4O') AND (b.noutnum IS NOT NULL AND b.noutnum > 0)) AND       h.dr = 0 AND b.dr = 0 AND h.cgeneralhid = b.cgeneralhid and b.cinventoryid=bd_invmandoc.pk_invmandoc and outtrackin='Y') UNION ALL ( SELECT b.pk_corp, b.dtfreezetime, b.cincorrespondtype, b.cincorrespondcode,       b.cincorrespondhid, b.cincorrespondbid, b.ccalbodyid, b.cwarehouseid, b.cinventoryid,        b.vfree1, b.vfree2, b.vfree3, b.vfree4, b.vfree5, b.vfree6, b.vfree7, b.vfree8, b.vfree9,       b.vfree10, b.vbatchcode, b.dvalidate, b.castunitid, 0, 0, b.nfreezenum,       b.nfreezeastnum FROM ic_freeze b,bd_invmandoc WHERE b.cthawpersonid IS NULL AND b.dr = 0 and b.cinventoryid=bd_invmandoc.pk_invmandoc and outtrackin='Y')";
	public final static String v_ic_borrow_refbook =
		"SELECT h.pk_corp, h.pk_calbody, h.cbilltypecode, h.vbillcode, h.cgeneralhid,h.daccountdate, h.cwarehouseid, b.cinventoryid,"+
		" b.vfree1, b.vfree2, b.vfree3, b.vfree4, b.vfree5, b.vfree6, b.vfree7, b.vfree8, b.vfree9, b.vfree10,"+
		" b.vbatchcode, b.castunitid, b.dbizdate, b.cvendorid as cproviderid,"+
		" b.ninnum, b.ninassistnum, b.noutnum,b.noutassistnum, b.nretnum AS ljhhnum, b.nretastnum AS ljhhastnum,"+"b.nretgrossnum as ljhhgrsnum, "+
	    " b.ntranoutnum AS transnum, b.ntranoutastnum AS transastnum, 0.0 AS restnum, 0.0 AS restastnum, b.nprice, h.fbillflag,"+
	    " b.hsl,b.ningrossnum, b.noutgrossnum,b.cinvbasid "+
	    " FROM ic_general_h h INNER JOIN   ic_general_b b ON h.cgeneralhid = b.cgeneralhid "+
	    " WHERE (h.cbilltypecode in('49','41','4J')) AND (h.dr = 0) AND (b.dr = 0)";
//	"SELECT h.pk_corp, h.pk_calbody, h.cbilltypecode, h.vbillcode, h.cgeneralhid,h.daccountdate, h.cwarehouseid, b.cinventoryid, b.vfree1, b.vfree2, b.vfree3, b.vfree4, b.vfree5, b.vfree6, b.vfree7, b.vfree8, b.vfree9, b.vfree10, b.vbatchcode, b.dvalidate, b.castunitid, b.dbizdate, b.cvendorid as cproviderid, b.ninnum, b.ninassistnum, b.noutnum,b.noutassistnum, b.nretnum AS ljhhnum, b.nretastnum AS ljhhastnum, b.ntranoutnum AS transnum, b.ntranoutastnum AS transastnum, 0.0 AS restnum, 0.0 AS restastnum, b.nprice, h.fbillflag FROM ic_general_h h INNER JOIN   ic_general_b b ON h.cgeneralhid = b.cgeneralhid WHERE (h.cbilltypecode in('49','41','4J')) AND (h.dr = 0) AND (b.dr = 0)";
	
	public final static String v_ic_lend_refbook =
		//"SELECT h.pk_corp,h.pk_calbody, h.cbilltypecode, h.vbillcode, h.cgeneralhid, h.daccountdate,  h.ts,      h.cwarehouseid, b.cinventoryid, b.vfree1, b.vfree2, b.vfree3, b.vfree4, b.vfree5,        b.vfree6, b.vfree7, b.vfree8, b.vfree9, b.vfree10, b.vbatchcode, b.dvalidate,        b.castunitid, b.ninnum, b.ninassistnum, b.noutnum, b.noutassistnum, ljhhnum, ljhhastnum, 0.0 AS restnum,        0.0 AS restastnum, ntranoutnum AS transnum, ntranoutastnum AS transastnum, h.ccustomerid, b.dbizdate,        b.nprice,h.fbillflag FROM ic_general_h h INNER JOIN ic_general_b b ON h.cgeneralhid = b.cgeneralhid LEFT OUTER JOIN (SELECT csourcebillbid, SUM(COALESCE (retb.ninnum, 0.0)) ljhhnum,SUM(COALESCE (retb.ninassistnum, 0.0)) ljhhastnum FROM ic_general_b retb,ic_general_h reth WHERE reth.cbilltypecode='4B' and reth.dr = 0 and retb.dr=0 and reth.cgeneralhid=retb.cgeneralhid GROUP BY csourcebillbid ) c ON b.cgeneralbid = c.csourcebillbid WHERE (h.cbilltypecode = '4H' OR  h.cbilltypecode = '42' OR h.cbilltypecode = '4B')  and h.dr=0 and b.dr=0  AND h.cgeneralhid = b.cgeneralhid ";
		" SELECT h.pk_corp, h.pk_calbody, h.cbilltypecode, h.vbillcode, h.cgeneralhid,"+ 
	      " h.daccountdate, h.ts, h.cwarehouseid, b.cinventoryid, b.vfree1, b.vfree2, b.vfree3,"+ 
	      " b.vfree4, b.vfree5, b.vfree6, b.vfree7, b.vfree8, b.vfree9, b.vfree10, b.vbatchcode,"+ 
	      " b.dvalidate, b.castunitid, b.ninnum, b.ninassistnum, b.noutnum, b.noutassistnum,"+ 
	      " b.nretnum AS ljhhnum, b.nretastnum AS ljhhastnum, 0.0 AS restnum,"+ "b.nretgrossnum as ljhhgrsnum,0.0 as nretgrossnum,"+
	      " 0.0 AS restastnum, b.ntranoutnum AS transnum, b.ntranoutastnum AS transastnum,"+ 
	      " h.ccustomerid, b.cvendorid as cproviderid, b.dbizdate, b.nprice, h.fbillflag, b.hsl, b.ningrossnum, b.noutgrossnum,b.cinvbasid "+
		  " FROM ic_general_h h INNER JOIN  ic_general_b b ON h.cgeneralhid = b.cgeneralhid WHERE (h.cbilltypecode IN ('4H', '42','4B')) AND (h.dr = 0) AND (b.dr = 0) ";
	
/**
 * 方法处理 SQL 语句中的IN()问题, 只能对于fieldname 在数据库中定义为 char or vchar 类型的字段!
 * 参  数	String fieldname IN 的数据库字段名, ArrayList fieldvalue IN 中的取值
 * 返回值	拼接的SQL 字串.
 * 创建日期：(2003-01-16 11:06:46)
 */
public static String formInSQL(String fieldname, ArrayList fieldvalue) {
	if (fieldname == null || fieldname.trim().length() == 0 || fieldvalue == null || fieldvalue.size() == 0)
		return " and 1=0 ";

	String[] svalues=new String[fieldvalue.size()];
	fieldvalue.toArray(svalues);
	return formInSQL(fieldname,svalues); 
}
/**
 * 方法处理 SQL 语句中的IN()问题, 只能对于fieldname 在数据库中定义为 char or vchar 类型的字段!
 * 参  数	String fieldname IN 的数据库字段名, ArrayList fieldvalue IN 中的取值
 * 返回值	拼接的SQL 字串.
 * 创建日期：(2003-01-16 11:06:46)
 */
public static String formInSQL2(String fieldname, ArrayList fieldvalue) {
	if (fieldname == null || fieldname.trim().length() == 0 || fieldvalue == null || fieldvalue.size() == 0)
		return " and 1=0 ";

	String[] svalues=new String[fieldvalue.size()];
	fieldvalue.toArray(svalues);
	return " and ("+fieldname+" in "+formSubSql0("temp_ic_forminsql002",fieldname,svalues)+")";
}
/**
 * 方法处理 SQL 语句中的IN()问题, 只能对于fieldname 在数据库中定义为 char or vchar 类型的字段!
 * 参  数	String fieldname IN 的数据库字段名, ArrayList fieldvalue IN 中的取值
 * 返回值	拼接的SQL 字串.
 * 创建日期：(2003-01-16 11:06:46)
 */
public static String formInSQL(String fieldname, String[] fieldvalue) {
	if (fieldname == null
		|| fieldname.trim().length() == 0
		|| fieldvalue == null
		|| fieldvalue.length == 0)
		return " and 1=0 ";

	return " and ("+fieldname+" in "+formSubSql(fieldname,fieldvalue)+")";
}


public static String formInSqlWithoutAnd(String sFieldName, ArrayList alValue, int start, int num)
	{
		if(sFieldName == null || sFieldName.trim().length() == 0 || alValue == null || start < 0 || num < 0 || alValue.size() < start + num)
			return null;
		StringBuffer sbSQL = new StringBuffer(200);
		sbSQL.append("  ( ").append(sFieldName).append(" IN ( ");
		int end = start + num;
		for(int i = start; i < end; i++)
		{
			if(alValue.get(i) != null && alValue.get(i).toString().trim().length() > 0)
			{
				sbSQL.append("'").append(alValue.get(i)).append("'");
				if(i != alValue.size() - 1 && (i <= 0 || i % 200 != 0))
					sbSQL.append(",");
			} else
			{
				return null;
			}
			if(i > 0 && i % 200 == 0)
				sbSQL.append(" ) OR ").append(sFieldName).append(" IN ( ");
		}

		sbSQL.append(" ) )");
		return sbSQL.toString();
	}
	public static String formInSqlWithoutAnd(String sFieldName, String saValue[], int start, int num)
	{
		if(sFieldName == null || sFieldName.trim().length() == 0 || saValue == null || start < 0 || num < 0 || saValue.length < start + num)
			return null;
		StringBuffer sbSQL = new StringBuffer(200);
		sbSQL.append("  ( ").append(sFieldName).append(" IN ( ");
		int end = start + num;
		for(int i = start; i < end; i++)
		{
			if(saValue[i] != null && saValue[i].trim().length() > 0)
			{
				sbSQL.append("'").append(saValue[i]).append("'");
				if(i != saValue.length - 1 && (i <= 0 || i % 200 != 0))
					sbSQL.append(",");
			} else
			{
				return null;
			}
			if(i > 0 && i % 200 == 0)
				sbSQL.append(" ) OR ").append(sFieldName).append(" IN ( ");
		}

		sbSQL.append(" ) )");
		return sbSQL.toString();
	}
/**
 * 方法处理 SQL 语句中的IN()问题, 只能对于fieldname 在数据库中定义为 char or vchar 类型的字段!
 * 参  数	String fieldname IN 的数据库字段名, ArrayList fieldvalue IN 中的取值
 * 返回值	拼接的SQL 字串.
 * 创建日期：(2003-01-16 11:06:46)
 */
public static String formSubSql(String fieldname, ArrayList fieldvalue) {
	if (fieldname == null || fieldname.trim().length() == 0 || fieldvalue == null || fieldvalue.size() == 0)
		return "('') ";

	String[] svalues=new String[fieldvalue.size()];
	fieldvalue.toArray(svalues);
	return formSubSql(fieldname,svalues);
}
/**
 * 方法处理 SQL 语句中的IN()问题, 只能对于fieldname 在数据库中定义为 char or vchar 类型的字段!
 * 参  数	String fieldname IN 的数据库字段名, ArrayList fieldvalue IN 中的取值
 * 返回值	拼接的SQL 字串.
 * 创建日期：(2003-01-16 11:06:46)
 */
public static String formSubSql(String fieldname, String[] fieldvalue) {
	return formSubSql0( "temp_ic_forminsql001",fieldname,fieldvalue);
	}
/**
 * 方法处理 SQL 语句中的IN()问题, 只能对于fieldname 在数据库中定义为 char or vchar 类型的字段!
 * 参  数	String fieldname IN 的数据库字段名, ArrayList fieldvalue IN 中的取值
 * 返回值	拼接的SQL 字串.
 * 创建日期：(2003-01-16 11:06:46)
 */
private static String formSubSql0(String tablename,String fieldname, String[] fieldvalue) {
	if (fieldname == null
		|| fieldname.trim().length() == 0
		|| fieldvalue == null
		|| fieldvalue.length == 0)
		return " ('') ";

	ArrayList alvalue=new ArrayList();
	HashMap hm=new HashMap();
	for(int i=0;i<fieldvalue.length;i++){
		if(hm.containsKey(fieldvalue[i]))
			continue;
		hm.put(fieldvalue[i],fieldvalue[i]);
		alvalue.add(fieldvalue[i]);
		
	
	}
	StringBuffer sbSQL = new StringBuffer();
	String tempsql = null;
	try {
		nc.bs.scm.pub.TempTableDMO dmo = new nc.bs.scm.pub.TempTableDMO();
		
		
		tempsql =
			dmo.insertTempTable(alvalue,tablename, "id","varchar(200)");
	} catch (Exception e) {
		/**needn't ydy*/
		nc.vo.scm.pub.SCMEnv.error(e);

	} 
	if (tempsql != null)
		return tempsql;
		
	else {

		sbSQL.append(" ( ");
		//循环写入条件    
		for (int i = 0; i < fieldvalue.length; i++) {
			if (fieldvalue[i] != null && fieldvalue[i].trim().length() > 0) {
				sbSQL.append("'").append(fieldvalue[i].toString()).append("'");
				//单独处理 每个取值后面的",", 对于最后一个取值后面不能添加"," 并且兼容 oracle 的 IN 254 限制。每 200 个 数据 or 一次。时也不能添加","
				if (i != (fieldvalue.length - 1)
					&& !(i > 0 && i % nc.bs.ic.pub.bill.GeneralSqlString.SQL_IN_LIST_LIMIT == 0)) {
					sbSQL.append(",");
				}
			} else {
				return null;
			}

			//兼容 oracle 的 IN 254 限制。每 200 个 数据 or 一次。
			if (i > 0 && i % nc.bs.ic.pub.bill.GeneralSqlString.SQL_IN_LIST_LIMIT == 0) {
				sbSQL.append(" ) OR ").append(fieldname).append(" IN ( ");
			}
		}
		sbSQL.append(" ) ");
	}
	return sbSQL.toString();
}
/**
 * 此处插入方法说明。
 * 功能：得到纯字段名，用于外层汇总，排序之用。
	"inv.invname",--------->"invname"
	"invcode",--------->"invcode"
 
 * 参数： 
 * 返回：
 * 例外：
 * 日期：(2002-10-30 14:24:40)
 * 修改日期，修改人，修改原因，注释标志：
 */
public static String[] getPureField(String saField[]) {
	//String[] saField = new String[]{"inv.invcode","invname","inv.kk"};
	String[] saNewField = null;
	int index = 0;
	if (saField != null && saField.length > 0) {
		saNewField = new String[saField.length];
		for (int i = 0; i < saField.length; i++)
			if (saField[i] != null) {
				index = saField[i].indexOf(".");
				if (index >= 0 && index < saField[i].length())
					saNewField[i] = saField[i].substring(index + 1);
				else
					saNewField[i] = saField[i];
				//nc.vo.scm.pub.SCMEnv.out(saNewField[i]);
			}
	}
	return saNewField;
}
/**
 * ?user>
 * 功能：
 * 参数：返回公共的字符串,替代SQL_PURE_BODY_FIELD
 * 返回：
 * 例外：
 * 日期：(2004-6-1 14:35:08)
 * 修改日期，修改人，修改原因，注释标志：
 * 
 * @return java.lang.String
 */
public static String getSQL_PURE_BODY_FIELD() {
	if(SQL_PURE_BODY_FIELD==null){
		
		String[] IC_GENERAL_B_FIELDS=SmartVOUtilExt.getDbFields(new GeneralBillItemVOMeta());
		
		if(IC_GENERAL_B_FIELDS!=null){
			
		StringBuffer s=new StringBuffer();
		
		s.append(IC_GENERAL_B_FIELDS[0]);
		for(int i=1;i<IC_GENERAL_B_FIELDS.length;i++){
			s.append(",").append(IC_GENERAL_B_FIELDS[i]);
			
			}
		SQL_PURE_BODY_FIELD=s.toString();
		}
		}
	
	return "/*+ index(body) */ "+SQL_PURE_BODY_FIELD;
}
/**
 * ?user>
 * 功能：
 * 参数：返回公共的字符串,替代SQL_PURE_HEAD_FIELD
 * 返回：
 * 例外：
 * 日期：(2004-6-1 14:35:08)
 * 修改日期，修改人，修改原因，注释标志：
 * 
 * @return java.lang.String
 */
public static String getSQL_PURE_HEAD_FIELD() {
	if(SQL_PURE_HEAD_FIELD==null){
		String[] IC_GENERAL_H_FIELDS=SmartVOUtilExt.getDbFields(new GeneralBillHeaderVOMeta());
		
		if(IC_GENERAL_H_FIELDS!=null){
	
		StringBuffer s=new StringBuffer();
		s.append(IC_GENERAL_H_FIELDS[0]);
		for(int i=1;i<IC_GENERAL_H_FIELDS.length;i++){
			s.append(",").append(IC_GENERAL_H_FIELDS[i]);
			
			}
		SQL_PURE_HEAD_FIELD=s.toString();
		}
		}
	
	return "/*+ index(head) */ "+SQL_PURE_HEAD_FIELD;
}
/**
 * 此处插入方法说明。
 * 功能：所有的表体字段的string形式
 * 参数： 
 * 返回：
 * 例外：
 * 日期：(2002-4-24 16:18:23)
 * 修改日期，修改人，修改原因，注释标志：
 * @return java.lang.String
 * @param sItemKey java.lang.String
 * @param sBillTypeCode java.lang.String
 */
public static String getStr(String saStr[]) {
	StringBuffer sbRet = new StringBuffer();
	int i = 0;
	for (; i < saStr.length - 1; i++) {
		sbRet.append(saStr[i]);
		sbRet.append(",");
	}
	sbRet.append(saStr[i]);
	return sbRet.toString();
}
/**
 * 此处插入方法说明。
 * 功能：返回   读已还回数量的SQL
 * 参数： 
 * 返回：
 * 例外：
 * 日期：(2002-4-24 16:18:23)
 * 修改日期，修改人，修改原因，注释标志：
 * @return java.lang.String
 * @param sItemKey java.lang.String
 * @param sBillTypeCode java.lang.String
 */
public static String[] getUnionStringArray(
	String saBase[],
	String saAppend[]) {
	int iAppendNum = 0;
	if (saAppend != null)
		iAppendNum = saAppend.length;
	String saAllFields[] = new String[saBase.length + iAppendNum];
	int i = 0;
	for (; i < saBase.length; i++)
		saAllFields[i] = saBase[i];
	//append!    
	for (; i < saBase.length + iAppendNum; i++)
		saAllFields[i] = saAppend[i - saBase.length];
	return saAllFields;
}
/**
 * 此处插入方法说明。
 * 功能：实现左关联两个换算率bd_convert
 * 参数：两个表的别名。 
 * 返回：
 * 例外：
 * 日期：(2002-2-5 20:26:26)
 * 修改日期，修改人，修改原因，注释标志：
 * @return java.lang.String
 * @param conv1 java.lang.String
 * @param conv2 java.lang.String
 */
public static String leftOuterJoinConvert(String conv1, String conv2) {
	return "("
		+ conv1
		+ ".pk_invbasdoc="
		+ conv2
		+ ".pk_invbasdoc OR "
		+ conv2
		+ ".pk_invbasdoc IS NULL)";
}
/**
 * 此处插入方法说明。
 * 功能：实现左关联项目
 * 参数：表别名。 
 * 返回：
 * 例外：
 * 日期：(2002-2-5 20:26:26)
 * 修改日期，修改人，修改原因，注释标志：
 * @return java.lang.String
 * @param conv1 java.lang.String
 * @param conv2 java.lang.String
 */
public static String leftOuterJoinJob(String sTableAlias) {
	return 		"		LEFT OUTER JOIN bd_jobmngfil jobman ON "+sTableAlias+".cprojectid = jobman.pk_jobmngfil INNER JOIN bd_jobbasfil job ON jobman.pk_jobbasfil = job.pk_jobbasfil ";

}
/**
 * 此处插入方法说明。
 * 功能：实现左关联项目阶段
 * 参数：表别名。 
 * 返回：
 * 例外：
 * 日期：(2002-2-5 20:26:26)
 * 修改日期，修改人，修改原因，注释标志：
 * @return java.lang.String
 * @param conv1 java.lang.String
 * @param conv2 java.lang.String
 */
public static String leftOuterJoinJobphase(String sTableAlias) {
	return 		"		LEFT OUTER JOIN bd_jobobjpha jobobj ON "+sTableAlias+".cprojectphaseid=jobobj.pk_jobobjpha INNER JOIN bd_jobphase jobph ON jobobj.pk_jobphase=jobph.pk_jobphase ";

}
/**
 * GeneralSQL 构造子注解。
 */
public GeneralSqlString() {
	super();
}

public static String[] getFields_bb1(){
	return new String[]{"cgeneralbb1","cgeneralbid","cspaceid","ninspaceassistnum","ninspacenum","noutspaceassistnum","noutspacenum","ningrossnum","noutgrossnum"};
	
}
public static String[] getFields_bb2(){
	return new String[]{"cserialid","cgeneralbid",
			"vserialcode",
			"cwarehouseid",
			"cinventoryid",
			"cproviderid", 
			"vfree1", 
			"vfree2",  
			"vfree3",  
			"vfree4",  
			"vfree6",  
			"vfree7",  
			"vfree8",  
			"vfree9",  
			"vfree10", 
			"vfree5",  
			"cspaceid", 
			"vbatchcode",
			"ccustomerid",
			"cinbilltypecode",  
			"vinbillcode",        
			"cinbillheadid",      
			"cinbillbodyid",      
			"coutbilltype",       
			"voutbillcode",       
			"coutbillheadid",     
			"coutbillbodyid",     
			"cfreezeid",          
			"pk_corp",            
			"dbillindate",        
			"dbilloutdate" };
	
}
public static String[] getFields_bb3(){
	return new String[]{"cgeneralhid", 
			"cgeneralbid", 
			"pk_corp",     
			"naccountmny", 
			"naccountnum2",
			"caccountunitid", 
			"naccountnum1",   
			"nsignnum",       
			"npprice",        
			"cgeneralbb3",    
			"bsettleendflag", 
			"nmaterialmoney", 
			"ndmsignnum",     
			"nrsvnum1",  
			"nrsvnum2",  
			"cpk1",      
			"cpk2",      
			"vrsv1",     
			"vrsv2",     
			"ntoaccountnum",
			"ntoaccountmny",
			"btoaccountflag",
			"coperatorid",   
			"vsettledbillcode2", 
			"vsettledbillcode",  
			"csettlerid2",       
			"csettlerid",        
			"nzgyfprice",        
			"nzgyfmoney",        
			"nsettlenum1",       
			"nsettlemny1",       
			"bendsettle1",       
			"nzgprice1",         
			"nzgmny1",           
			"naccumwashnum"};
	
}
public static String[] getFields_bbc(){
	return new String[]{"cgeneralbbcid",     
			"cgeneralbid",        
			"pk_corp",         
			"vbarcode",        
			"vboxbarcode",     
			"vbarcodesub",    
			"bexpandcodeflag",  
			"nnumber",         
			"bsingletypeflag"};
	
}
}
