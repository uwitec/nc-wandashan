package nc.ui.hg.pu.pub;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.ui.pub.ClientEnvironment;
import nc.ui.trade.business.HYPubBO_Client;
import nc.vo.hg.pu.pub.PlanApplyInforVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pu.PuPubVO;

/*
 * pk转换成编码及其他
 *author:spf 
 */
public class TransletFactory {
	public String TFactory(String id, String name) throws Exception {
			if ("公司档案".equalsIgnoreCase(name)) {// 公司档案
				String st = PuPubVO.getString_TrimZeroLenAsNull(HYPubBO_Client
						.findColValue("bd_corp", "unitcode", "pk_corp ='" + id
								+ "'"));
				if (st != null)
					return st;

			} else if ("部门档案".equalsIgnoreCase(name)) {// 部门档案
				String st = PuPubVO.getString_TrimZeroLenAsNull(HYPubBO_Client
						.findColValue("bd_deptdoc", "deptcode", "pk_deptdoc ='"
								+ id + "'"));
				if (st != null) {
					return st;
				}

			} else if ("人员管理档案 ".equalsIgnoreCase(name)) {// 人员管理档案
				String st = PuPubVO.getString_TrimZeroLenAsNull(HYPubBO_Client
						.findColValue("bd_psndoc", "psncode", "pk_psndoc ='"
								+ id + "'"));

				if (st != null) {
					return st;
				}

			} else if ("仓库档案".equalsIgnoreCase(name)) {// 仓库档案
				String st = PuPubVO.getString_TrimZeroLenAsNull(HYPubBO_Client
						.findColValue("bd_stordoc", "storcode", "pk_stordoc ='"
								+ id + "'"));
				if (st != null) {
					return st;
				}

			} else if ("发运方式".equalsIgnoreCase(name)) {// 发运方式
				String st = PuPubVO.getString_TrimZeroLenAsNull(HYPubBO_Client
						.findColValue("bd_sendtype", "sendcode",
								"pk_sendtype ='" + id + "'"));
				if (st != null) {
					return st;
				}

			} else if ("存货档案".equalsIgnoreCase(name)) {// 存货档案
				String sql = PuPubVO.getString_TrimZeroLenAsNull(HYPubBO_Client
						.findColValue("bd_invmandoc", "pk_invbasdoc",
								"pk_invmandoc ='" + id + "'"));
				String st = PuPubVO.getString_TrimZeroLenAsNull(HYPubBO_Client
						.findColValue("bd_invbasdoc", "invcode",
								"pk_invbasdoc ='" + sql + "'"));
				if (st != null) {
					return st;
				}

			} else if ("库存组织".equalsIgnoreCase(name)) {// 库存组织
				String st = PuPubVO.getString_TrimZeroLenAsNull(HYPubBO_Client
						.findColValue("bd_calbody", "bodycode", "pk_calbody ='"
								+ id + "'"));
				if (st != null) {
					return st;
				}

			} else if ("操作员".equalsIgnoreCase(name)) {// 操作员
				String st = PuPubVO.getString_TrimZeroLenAsNull(HYPubBO_Client
						.findColValue("sm_user", "user_name", "cuserid ='" + id
								+ "'"));
				if (st != null) {
					return st;
				}
			} else if ("采购方式".equalsIgnoreCase(name)) {// 采购方式
				String sql = PuPubVO.getString_TrimZeroLenAsNull(HYPubBO_Client
						.findColValue("bd_invmandoc", "pk_invbasdoc",
								"pk_invmandoc ='" + id + "'"));
				String st = PuPubVO.getString_TrimZeroLenAsNull(HYPubBO_Client
						.findColValue("bd_invbasdoc", "def1",
								"pk_invbasdoc ='" + sql + "'"));
				if (st != null) {
					return st;
				}
			} else if ("申请部门".equalsIgnoreCase(name)) {// 采购方式
		       PlanApplyInforVO  m_appInfor = PlanPubHelper.getAppInfor(ClientEnvironment.getInstance().getCorporation().getPrimaryKey(),id);
		      String st = PuPubVO.getString_TrimZeroLenAsNull(HYPubBO_Client
					.findColValue("bd_deptdoc", "deptcode", "pk_deptdoc ='"
							+ m_appInfor.getCapplydeptid() + "'"));
				if (st != null) {
					return st;
				}
			} else if ("采购部门".equalsIgnoreCase(name)) {// 采购部门
				String sql = PuPubVO.getString_TrimZeroLenAsNull(HYPubBO_Client
						.findColValue("hg_plan", "csupplydeptid","pk_plan ='" + id + "'"));
				 String st = PuPubVO.getString_TrimZeroLenAsNull(HYPubBO_Client
							.findColValue("bd_deptdoc", "deptcode", "pk_deptdoc ='"+ sql + "'"));
				if (st != null) {
					return st;
				}
			}else if ("计量档案".equalsIgnoreCase(name)) {// 计量档案
				 String st = PuPubVO.getString_TrimZeroLenAsNull(HYPubBO_Client
							.findColValue("bd_measdoc", "measname", "pk_measdoc ='"+ id + "'"));
				if (st != null) {
					return st;
				}
			}
			return null;
	}
	
	public UFDouble getNplanPrice(String pk_invmandoc ) throws BusinessException{
		String sql= "select planprice from bd_invmandoc where  pk_invmandoc ='"+pk_invmandoc+"' and isnull(dr,0)=0";
		IUAPQueryBS query = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		Object o =query.executeQuery(sql, new ColumnProcessor());
		return PuPubVO.getUFDouble_NullAsZero(o);
	}
	
	/**
	 * 解析编码规则。
	 * 创建日期：(2011-5-24 14:32:51)
	 */
	public static  String[] splitCode(String value) throws BusinessException{
		java.util.StringTokenizer st = new java.util.StringTokenizer(value, ".", false);
		int count = st.countTokens();
		String[] showvalues = new String[count];
		int index = 0;
		try{
			while(st.hasMoreTokens()){
				showvalues[index++] = st.nextToken().trim();
			}
		}
		catch(Exception e){
			System.out.println("解析比例出错！");
			throw new BusinessException("解析比例出错！"+getString_NullAsTrimZeroLen(e.getMessage()));
		}

		return showvalues;
	}
	
	public static String getString_NullAsTrimZeroLen(Object value) {
		if (value == null) {
			return "";
		}
		return value.toString().trim();
	}
	
}
