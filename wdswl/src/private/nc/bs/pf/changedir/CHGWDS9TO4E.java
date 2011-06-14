package nc.bs.pf.changedir;

import nc.vo.pf.change.UserDefineFunction;

/**
 * 本地调拨入库->供应链调拨入库
 * @author zpm
 *
 */
public class CHGWDS9TO4E extends nc.bs.pf.change.VOConversion {

	public CHGWDS9TO4E() {
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
	  return "nc.ui.ic.pub.pfconv.ChgAft4KNLMR24IA";
	}

	public String[] getField() {
		return (
				new String[] {
						"H_pk_corp->H_pk_corp",//入公司
						"H_pk_calbody->H_geh_calbody",//入库存组织
						"H_cwarehouseid->H_geh_cwarehouseid",//入库仓库
						
						"H_cothercorpid->H_geh_cothercorpid",//出公司
						"H_cothercalbodyid->H_geh_cothercalbodyid",//出库存组织
						"H_cotherwhid->H_geh_cotherwhid",//出库仓库
						
						"H_fallocflag->H_geh_fallocflag",//调拨类型标志
						
						"H_cbiztype->H_geh_cbiztype",//业务类型
						"H_cdispatcherid->H_geh_cdispatcherid",//收发类别
						"H_cwhsmanagerid->H_geh_cwhsmanagerid",//库管员
						"H_cdptid->H_geh_cdptid",//部门
						"H_cbizid->H_geh_cbizid",//业务员
//						"H_ccustomerid->",//客户
//						"H_vdiliveraddress->",//收货地址[发运地址]
						
						"H_coperatorid->H_coperatorid",//制单人
						
//						"B_creceieveid->",//收货单位管理id
						
						"B_cinvbasid->B_geb_cinvbasid",//存货基本档案ID   
						"B_cinventoryid->B_geb_cinventoryid",//存货管理ID  	
						"B_pk_measdoc->B_pk_measdoc",//主单位
						"B_castunitid->B_castunitid",//辅单位
						"B_hsl->B_geb_hsl",//换算率 
						"B_scrq->B_geb_proddate",//生产日期-----------------------------zpm
						"B_dvalidate->B_geb_dvalidate",//失效日期------------------------------zpm
						
						
						"B_nshouldinnum->B_geb_snum",//应入数量
						"B_nneedinassistnum->B_geb_bsnum",//应入辅数量  
						"B_ninnum->B_geb_anum",//实入数量
						"B_ninassistnum->B_geb_banum",//实入辅数量   
						
						
						"B_nprice->B_geb_nprice",//单价
						"B_nmny->B_geb_nmny",//金额
					//	"B_vbatchcode->B_geb_vbatchcode",//批次号	
						"B_vbatchcode->B_geb_backvbatchcode",//原批次号回写
						
						"B_flargess->B_geb_flargess",//是否赠品
						"B_cspaceid->B_geb_space",//货位ID

						"B_csourcebillhid->B_gylbillhid",//[保存 供应链 其他出字段]
						"B_csourcebillbid->B_gylbillbid",//[保存  供应链 其他出字段]
						"B_vsourcebillcode->B_gylbillcode",//[保存 供应链 其他出字段]
						"B_csourcetype->B_gylbilltype",//[保存  供应链 其他出字段]
						
						"B_cfirstbillhid->B_geh_pk",// [保存  物流 其他入字段]，
						"B_cfirstbillbid->B_geb_pk",//   [保存  物流 其他入字段]
						"B_vfirstbillcode->H_geh_billcode",//[保存  物流 其他入字段 ]
						"B_cfirsttype->H_geh_billtype",//[保存  物流 其他入字段 ]
						
						"B_dbizdate->B_geb_dbizdate",//入库日期--业务日期
						
		
//						"H_coperatoridnow->SYSOPERATOR",
//						"H_coperatorid->SYSOPERATOR",
						
//						"B_nplannedmny->B_jhje",//计划金额
//						"B_nplannedprice->B_jhdj",//计划单价
					
				});
	}     

		public String[] getFormulas()
		{ 
			return (new String[] {
					"H_cbilltypecode->\"4E\"", 
//					"B_csourcetype->\"WDS7\"",					
				});
		}
	/**
	* 返回用户自定义函数。
	*/
	public UserDefineFunction[] getUserDefineFunction() {
		return null;
	}
}
