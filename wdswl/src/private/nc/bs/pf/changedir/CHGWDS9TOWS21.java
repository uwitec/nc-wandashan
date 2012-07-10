package nc.bs.pf.changedir;

import nc.vo.pf.change.UserDefineFunction;
import nc.vo.wl.pub.Wds2WlPubConst;
import nc.vo.wl.pub.WdsWlPubConst;

/**
 * 本地调拨入库->调拨运单
 * @author zhf
 *
 */
public class CHGWDS9TOWS21 extends nc.bs.pf.change.VOConversion {

	public CHGWDS9TOWS21() {
		super();
	}
	/**
	* 获得后续类的全录经名称。
	* @return java.lang.String[]
	*/
	public String getAfterClassName() {
		return null;
	}
	/**
	* 获得另一个后续类的全录径名称。
	* @return java.lang.String[]
	*/
	public String getOtherClassName() {
	  return null;
	}

	public String[] getField() {
		return (
				new String[] {
						
						"H_pk_corp->H_pk_corp",//调入入公司
						"H_pk_inwhouse->H_geh_cwarehouseid",//入库仓库
						"H_vdef1->H_geh_cothercorpid",//调出公司
						"H_pk_outwhouse->H_geh_cotherwhid",//出库仓库						
						"H_pk_busitype->H_geh_cbiztype",//业务类型
						
						"B_pk_invbasdoc->B_geb_cinvbasid",//存货基本档案ID   
						"B_pk_invmandoc->B_geb_cinventoryid",//存货管理ID  	
						"B_unit->B_pk_measdoc",//主单位
						"B_assunit->B_castunitid",//辅单位
						"B_nhsl->B_geb_hsl",//换算率 
//						"B_scrq->B_geb_proddate",//生产日期-----------------------------zpm
//						"B_dvalidate->B_geb_dvalidate",//失效日期------------------------------zpm
						
						
						"B_noutnum->B_geb_snum",//应入数量
						"B_nnassoutnum->B_geb_bsnum",//应入辅数量  
						"B_ninacceptnum->B_geb_anum",//实入数量
//						"B_ninassistnum->B_geb_banum",//实入辅数量   
						
						
//						"B_nprice->B_geb_nprice",//单价
//						"B_nmny->B_geb_nmny",//金额
					//	"B_vbatchcode->B_geb_vbatchcode",//批次号	
//						"B_vbatchcode->B_geb_backvbatchcode",//原批次号回写
						
//						"B_flargess->B_geb_flargess",//是否赠品
//						"B_cspaceid->B_geb_space",//货位ID B_cfirsttype

						"B_cfirstbillhid->B_gylbillhid",//[保存 供应链 调拨出库单]
						"B_cfirstbillbid->B_gylbillbid",//[保存 供应链 调拨出库单]
						"B_vfirstbillcode->B_gylbillcode",//[保存 供应链 调拨出库单]
						"B_cfirsttype->B_gylbilltype",//[保存 供应链 调拨出库单]
//						"B_"+WdsWlPubConst.csourcehid_wds+"->B_geb_pk",//Lyf:ERP出入库单，记录物流系统来源单据主键,以便物流的单据能够联查到ERP单据
//						"B_"+WdsWlPubConst.csourcebid_wds+"->B_geb_pk",//Lyf:ERP出入库单，记录物流系统来源单据主键,以便物流的单据能够联查到ERP单据
						"B_csourcebillhid->B_geh_pk",// [保存  物流 调拨入库单字段]，
						"B_csourcebillbid->B_geb_pk",//   [保存  物流 调拨入库单字段]
//						"B_vfirstbillcode->B_vfirstbillcode",//[保存  物流 调拨入库单字段]
						"B_csourcetype->H_geh_billtype",//[保存  物流 调拨入库单字段]
						
						"B_vdef1->B_vdef1",//存货状态
						
//						"B_dbizdate->B_geb_dbizdate",//入库日期--业务日期
						
		
//						"H_coperatoridnow->SYSOPERATOR",
						"H_voperatorid->SYSOPERATOR",
						
						 "H_dmakedate->SYSDATE",
						 "H_dbilldate->SYSDATE",
						 "H_denddate->SYSDATE",
						
//						"B_nplannedmny->B_jhje",//计划金额
//						"B_nplannedprice->B_jhdj",//计划单价
					
				});
	}     

		public String[] getFormulas()
		{	return new String[] {
//				"H_pk_corp->\""+m_strCorp+"\"",
//				"H_voperatorid->\""+m_strOperator+"\"",
				"H_pk_billtype->\""+Wds2WlPubConst.billtype_alloinsendorder+"\"",
				"H_vbillstatus->int(8)",
			    "B_csourcetype->\""+WdsWlPubConst.BILLTYPE_ALLO_IN+"\"",
			    "H_fisbigglour->\"N\"",
//			    "H_dmakedate->\""+m_strDate+"\"",
//			    "H_dbilldate->\""+m_strDate+"\"",
//			    "H_dbegindate->\""+m_strDate+"\"",
			    "H_itransstatus->\""+2+"\""
		};}
	/**
	* 返回用户自定义函数。
	*/
	public UserDefineFunction[] getUserDefineFunction() {
		return null;
	}
}
