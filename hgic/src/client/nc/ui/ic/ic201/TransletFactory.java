package nc.ui.ic.ic201;

import nc.ui.hg.pu.pub.PlanPubHelper;
import nc.ui.pub.ClientEnvironment;
import nc.ui.trade.business.HYPubBO_Client;
import nc.uif.pub.exception.UifException;
import nc.vo.hg.pu.pub.PlanApplyInforVO;
import nc.vo.pub.BusinessException;
import nc.vo.scm.pu.PuPubVO;

/*
 * pkת���ɱ��뼰����
 *author:spf 
 */
public class TransletFactory {
	public String TFactory(String id) {
		int falg = 1;
		try {
			while (true) {
				if (falg == 1) {//��˾����
					String st = (String) HYPubBO_Client.findColValue("bd_corp", "unitcode","pk_corp ='"+id+"'");
					if (st != null || "".equals(st)) {
						return st;
					}
					falg++;
				} else if (falg == 2) {//���ŵ���
					String st = (String) HYPubBO_Client.findColValue("bd_deptdoc", "deptcode","pk_deptdoc ='"+id+"'");
					if (st != null || "".equals(st)) {
						return st;
					}
					falg++;
				} else if (falg == 3) {//��Ա������  
					String st = (String) HYPubBO_Client.findColValue("bd_psndoc", "psncode","pk_psndoc ='"+id+"'");
					
					if (st != null || "".equals(st)) {
						return st;
					}
					falg++;
				} else if (falg == 4) {//�ֿ⵵��
					String st = (String) HYPubBO_Client.findColValue("bd_stordoc", "storcode","pk_stordoc ='"+id+"'");
					if (st != null || "".equals(st)) {
						return st;
					}
					falg++;
				} else if (falg == 5) {//���˷�ʽ
					String st = (String) HYPubBO_Client.findColValue("bd_sendtype", "sendcode","pk_sendtype ='"+id+"'");
					if (st != null || "".equals(st)) {
						return st;
					}
					falg++;
				} else if (falg == 6) {//�������
					String sql = (String) HYPubBO_Client.findColValue("bd_invmandoc", "pk_invbasdoc","pk_invmandoc ='"+id+"'");
					String st = (String) HYPubBO_Client.findColValue("bd_invbasdoc", "invcode","pk_invbasdoc ='"+sql+"'");
					if (st != null || "".equals(st)) {
						return st;
					}
					falg++;
				} else if (falg == 7) {//�����֯
					String st = (String) HYPubBO_Client.findColValue("bd_calbody", "bodycode","pk_calbody ='"+id+"'");
					if (st != null || "".equals(st)) {
						return st;
					}
					falg++;
					break;
				}
			}
		} catch (UifException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public String TFactory1(String id, String name) throws Exception {
		if ("��˾����".equalsIgnoreCase(name)) {// ��˾����
			String st = PuPubVO.getString_TrimZeroLenAsNull(HYPubBO_Client
					.findColValue("bd_corp", "unitcode", "pk_corp ='" + id
							+ "'"));
			if (st != null)
				return st;

		} else if ("���ŵ���".equalsIgnoreCase(name)) {// ���ŵ���
			String st = PuPubVO.getString_TrimZeroLenAsNull(HYPubBO_Client
					.findColValue("bd_deptdoc", "deptcode", "pk_deptdoc ='"
							+ id + "'"));
			if (st != null) {
				return st;
			}

		} else if ("��Ա������ ".equalsIgnoreCase(name)) {// ��Ա������
			String st = PuPubVO.getString_TrimZeroLenAsNull(HYPubBO_Client
					.findColValue("bd_psndoc", "psncode", "pk_psndoc ='"
							+ id + "'"));

			if (st != null) {
				return st;
			}

		} else if ("�ֿ⵵��".equalsIgnoreCase(name)) {// �ֿ⵵��
			String st = PuPubVO.getString_TrimZeroLenAsNull(HYPubBO_Client
					.findColValue("bd_stordoc", "storcode", "pk_stordoc ='"
							+ id + "'"));
			if (st != null) {
				return st;
			}

		} else if ("���˷�ʽ".equalsIgnoreCase(name)) {// ���˷�ʽ
			String st = PuPubVO.getString_TrimZeroLenAsNull(HYPubBO_Client
					.findColValue("bd_sendtype", "sendcode",
							"pk_sendtype ='" + id + "'"));
			if (st != null) {
				return st;
			}

		} else if ("�������".equalsIgnoreCase(name)) {// �������
			String sql = PuPubVO.getString_TrimZeroLenAsNull(HYPubBO_Client
					.findColValue("bd_invmandoc", "pk_invbasdoc",
							"pk_invmandoc ='" + id + "'"));
			String st = PuPubVO.getString_TrimZeroLenAsNull(HYPubBO_Client
					.findColValue("bd_invbasdoc", "invcode",
							"pk_invbasdoc ='" + sql + "'"));
			if (st != null) {
				return st;
			}

		} else if ("�����֯".equalsIgnoreCase(name)) {// �����֯
			String st = PuPubVO.getString_TrimZeroLenAsNull(HYPubBO_Client
					.findColValue("bd_calbody", "bodycode", "pk_calbody ='"
							+ id + "'"));
			if (st != null) {
				return st;
			}

		} else if ("����Ա".equalsIgnoreCase(name)) {// ����Ա
			String st = PuPubVO.getString_TrimZeroLenAsNull(HYPubBO_Client
					.findColValue("sm_user", "user_name", "cuserid ='" + id
							+ "'"));
			if (st != null) {
				return st;
			}
		} else if ("�ɹ���ʽ".equalsIgnoreCase(name)) {// �ɹ���ʽ
			String sql = PuPubVO.getString_TrimZeroLenAsNull(HYPubBO_Client
					.findColValue("bd_invmandoc", "pk_invbasdoc",
							"pk_invmandoc ='" + id + "'"));
			String st = PuPubVO.getString_TrimZeroLenAsNull(HYPubBO_Client
					.findColValue("bd_invbasdoc", "def1",
							"pk_invbasdoc ='" + sql + "'"));
			if (st != null) {
				return st;
			}
		} else if ("���벿��".equalsIgnoreCase(name)) {// �ɹ���ʽ
	       PlanApplyInforVO  m_appInfor = PlanPubHelper.getAppInfor(ClientEnvironment.getInstance().getCorporation().getPrimaryKey(),id);
	      String st = PuPubVO.getString_TrimZeroLenAsNull(HYPubBO_Client
				.findColValue("bd_deptdoc", "deptcode", "pk_deptdoc ='"
						+ m_appInfor.getCapplydeptid() + "'"));
			if (st != null) {
				return st;
			}
		} else if ("�ɹ�����".equalsIgnoreCase(name)) {// �ɹ�����
			String sql = PuPubVO.getString_TrimZeroLenAsNull(HYPubBO_Client
					.findColValue("hg_plan", "csupplydeptid","pk_plan ='" + id + "'"));
			 String st = PuPubVO.getString_TrimZeroLenAsNull(HYPubBO_Client
						.findColValue("bd_deptdoc", "deptcode", "pk_deptdoc ='"+ sql + "'"));
			if (st != null) {
				return st;
			}
		}else if ("��������".equalsIgnoreCase(name)) {// ��������
			 String st = PuPubVO.getString_TrimZeroLenAsNull(HYPubBO_Client
						.findColValue("bd_measdoc", "measname", "pk_measdoc ='"+ id + "'"));
			if (st != null) {
				return st;
			}
		}
		return null;
}
	
	/**
	 * �����������
	 * �������ڣ�(2011-5-24 14:32:51)
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
			System.out.println("������������");
			throw new BusinessException("������������"+getString_NullAsTrimZeroLen(e.getMessage()));
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
