package nc.ui.zb.pub;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import nc.bs.logging.Logger;
import nc.ui.pf.pub.PfUIDataCache;
import nc.ui.trade.bsdelegate.BusinessDelegator;
import nc.vo.pub.SuperVO;
import nc.vo.pub.billtype.BilltypeVO;
import nc.vo.zb.pub.JoinTableInfo;

/**
 * <p>
 * ���ӱ��ҵ��ί���ࡣ
 * <p>
 * ʵ�ֹ��ܣ�
 * <li>���ݱ�ͷ��ѯ���б��壻</li>
 * <li>���ӱ����飻</li>
 * 
 * @author twh
 * @date 2007-1-19 ����04:10:20
 * @version V5.0
 * @��Ҫ����ʹ�ã�
 *          <ul>
 *          <li><b>���ʹ�ø��ࣺ</b>ͨ��ʵ�ֱ�����������Ĭ��֧�ָ��ݱ�ͷ��ѯ���б��壬�Լ����ӱ�����ӱ����鹦�ܡ�</li>
 *          <li><b>�Ƿ��̰߳�ȫ��</b></li>
 *          <li><b>������Ҫ��</b></li>
 *          <li><b>ʹ��Լ����</b> ���������ʵ�������ʵ�ֽӿ�IMultiChildVOInfo��IMultiChildQueryInfo��ָ���йص�VO��Ϣ�Ͳ�ѯģ�������Ϣ��</li>
 *          <li><b>������</b></li>
 *          </ul>
 *          </p>
 *          <p>
 * @��֪��BUG��
 *          <ul>
 *          <li></li>
 *          </ul>
 *          </p>
 *          <p>
 * @�޸���ʷ��
 */
public abstract class MultiChildBusinessDelegator extends BusinessDelegator {

	public MultiChildBusinessDelegator() {
		super();
	}

	/**
	 * ���ӱ����VO��Ϣ
	 * 
	 * @return twh (2007-1-22 ����09:55:24)<br>
	 */
	public abstract IMultiChildVOInfo getMultiChildVoInfo();

	/**
	 * ���ӱ�������Ϣ��
	 * 
	 * @return twh (2007-1-22 ����10:37:44)<br>
	 */
	public abstract IMultiChildQueryInfo getMultiChildQueryInfo();

	@Override
	public Hashtable loadChildDataAry(String[] tableCodes, String key) throws Exception {
		Hashtable<String, SuperVO[]> dataHas = new Hashtable<String, SuperVO[]>();
		try {
			for (int i = 0; i < tableCodes.length; i++) {
				String clsName = getMultiChildVoInfo().getVoClassNameByTableCode(tableCodes[i]);
				if (clsName != null) {
					Class voClass = Class.forName(clsName);
					SuperVO vo = (SuperVO) voClass.newInstance();
					String strWhere = vo.getParentPKFieldName() + "='" + key + "' and isnull(dr,0)=0 ";
					SuperVO[] vos = queryByCondition(voClass, strWhere);
					if (vos != null && vos.length > 0)
						dataHas.put(tableCodes[i], vos);
				}
			}
		} catch (Exception e) {
			Logger.error("���ر�����Ϣ����", e);
			throw new Exception("���ر�����Ϣ����", e);
		}
		return dataHas;
	}

	@Override
	public SuperVO[] queryHeadAllData(Class headClass, String strBillType, String strWhere) throws Exception {
		// ���ӵ������͹����ж���Ĺ̶���ѯ������
		BilltypeVO billVo = PfUIDataCache.getBillType(strBillType);
		if (billVo.getWherestring() != null && billVo.getWherestring().length() != 0) {
			if (strWhere != null)
				strWhere = strWhere + " and (" + billVo.getWherestring() + ")";
			else
				strWhere = billVo.getWherestring();
		}

		// �������ӱ������ѯ
		return ClientHelper.queryVOWithJoinTable(headClass, getJoinTableInfo(strWhere), strWhere);
	}

	/**
	 * �������Ϣ��
	 * 
	 * @param strWhere
	 * @return
	 * @throws Exception
	 * @author: twh (2007-4-11 ����04:39:36)<br>
	 */
	protected JoinTableInfo[] getJoinTableInfo(String strWhere) throws Exception {
		JoinTableInfo[] jtis = null;
		List<JoinTableInfo> lstJTI = new ArrayList<JoinTableInfo>();
		for (int i = 0; i < getMultiChildVoInfo().getTableCodes().length; i++) {
			String tblCode = getMultiChildVoInfo().getTableCodes()[i];
			// ������Ӧ�ı���
			String alias = getMultiChildQueryInfo().getAliasByTableCode(tblCode);
			// ������Ӧ��VO����
			String voClsName = getMultiChildVoInfo().getVoClassNameByTableCode(tblCode);
			// ��ѯ�������иñ��Ӧ�ı�����
			if (alias != null && strWhere.indexOf(alias) >= 0 && voClsName != null) {
				JoinTableInfo jti = new JoinTableInfo(Class.forName(voClsName));
				jti.setAlias(alias);
				jti.setInnerjoin(false);
				// �ӱ�������������ֶ�
				jti.setJoinFieldName(((SuperVO) Class.forName(voClsName).newInstance()).getParentPKFieldName());
				// add to list
				lstJTI.add(jti);
			}
		}
		if (lstJTI.size() > 0)
			jtis = (JoinTableInfo[]) (lstJTI.toArray(new JoinTableInfo[0]));
		return jtis;
	}

	/**
	 * ����ƽ̨Ĭ�ϵ�queryHeadAllData������
	 * 
	 * @param headClass
	 * @param strBillType
	 * @param strWhere
	 * @return
	 * @throws Exception
	 *             twh (2007-1-22 ����10:33:46)<br>
	 */
	public SuperVO[] queryHeadAllDataOrig(Class headClass, String strBillType, String strWhere) throws Exception {
		return super.queryHeadAllData(headClass, strBillType, strWhere);
	}

}
