package nc.ui.pf.changedir;

import nc.ui.wl.pub.LoginInforHelper;
import nc.vo.pf.change.UserDefineFunction;
import nc.vo.pub.lang.UFDate;
/**
 * 销售订单--运费核算单
 * @author Administrator
 *
 */
public class CHGWDS5TOWDSM extends nc.ui.pf.change.VOConversionUI{
	public CHGWDS5TOWDSM() {
		super();
	}
	/**
	* 获得后续类的全录经名称。
	* @return java.lang.String[]
	*/
	public String getAfterClassName() {
		return "nc.ui.pf.changedir.after.ChgWDS5TOWDSMAfter";
	}
	/**
	* 获得另一个后续类的全录径名称。
	* @return java.lang.String[]
	*/
	public String getOtherClassName() {
		return null;
	}
	/**
	* 获得字段对应。
	* @return java.lang.String[]
	*/
	public String[] getField() {
		return new String[] {				
				"H_pk_corp->H_pk_corp",//公司				
				"H_pk_busitype->H_pk_busitype",//业务类型				
				//"H_->H_dbilldate",//单据日期
				"H_pk_deptdoc->H_pk_deptdoc",//部门
				"H_vemployeeid->H_vemployeeid",//业务员	
				
//				"H_->H_pk_cubasdoc",//客商基本id
				"B_pk_trader->H_pk_cumandoc",//客商管理id
//				"H_->H_custname",//客商名字
//				"H_->H_denddate",//需货时间
//				"H_->H_dbegindate",//装车时间
//				"H_->H_itranstype",//运输方式
				
//				"H_->H_pk_manageperson",//监管员
//				"H_->H_pk_yedb",//业务代表
//				"H_->H_vyedbtel",//业务代表电话
//				"H_->H_pk_transer",//调拨员
//				"H_->H_transername",//调拨员名字
//				"H_->H_creceiptcustomerid",//收获单位
				
				
//				"H_->H_pk_receiveperson",//联系人
//				"H_->H_vtelphone",//联系电话				
//				"H_->H_fisbigflour",//是否大包粉				
				"H_vdiliveraddress->H_vinaddress",//收货地址				
				"H_vmemo->H_vmemo",	//备注					
				"B_cinvmandocid->B_pk_invmandoc",//存货管理id
				"B_cinvbasdocid->B_pk_invbasdoc",//存货基本id	
				"B_pk_destore->H_pk_outwhouse",//出库仓库
				"B_pk_restore->H_pk_inwhouse",//入库仓库
				"B_invcode->B_invcode",//存货编码
				"B_invname->B_invname",//存货日期
				"B_invspec->B_invspec",//规格
				"B_invtype->B_invtype",//型号	
//				"B_->B_picicode",//批次
//				"B_->B_unitvolume",//存货体积
				"B_cunitid->B_uint",//主计量id
				"B_cassunitid->B_assunit",//辅计量id
				"B_unitname->B_unitname",//主计量名字
				"B_assunitname->B_assunitname",//辅计量名字
				"B_nhsl->B_nhgrate",//换算率
				"B_nnum->B_noutnum",//实发数量
				"B_nassnum->B_nassoutnum",//实发福数量	
			//	"B_pk_cardoc->",//车辆
				"B_csourcetype->H_pk_billtype",//单据类型
				"B_csourcebillhid->H_pk_sendorder",//来源发运订单主键
				"B_csourcebillbid->B_pk_sendorder_b",//附表主键
				"B_vsourcebillcode->H_vbillno",//订单号(来源订单号)
				
				"B_cupsourcebillrowid->B_cfirstbillbid",//源头单据表体id
				"B_cupsourcebillid->B_cfirstbillhid",//源头单据表头id
				"B_cupsourcebilltype->B_cfirsttype",//源头单据类型
				"B_vupbillcode->B_vfirstbillcode",//源头单据号					
		};
	}
	private LoginInforHelper helper = null;
	public LoginInforHelper getLoginInforHelper(){
		if(helper == null){
			helper = new LoginInforHelper();
		}
		return helper;
	}
	/**
	* 获得公式。
	* @return java.lang.String[]
	*/
	public String[] getFormulas() {
		new UFDate(System.currentTimeMillis());
		super.setSysDate(new UFDate(System.currentTimeMillis()).toString());
		return new String[] {
				"H_icoltype->int(1)",//
				"B_creceiverealid->getColValue2(tb_storcubasdoc, custareaid,pk_stordoc,H_pk_outwhouse,pk_cumandoc ,H_pk_cumandoc)",
				"B_ngl->getColValue2(tb_storcubasdoc, kilometer,pk_stordoc,H_pk_outwhouse,pk_cumandoc ,H_pk_cumandoc)"
		};
	}
	/**
	* 返回用户自定义函数。
	*/
	public UserDefineFunction[] getUserDefineFunction() {
		return null;
	}
	
}
