package nc.bs.wds.dm.sendinvdoc;

import java.util.List;

import nc.bs.dao.BaseDAO;
import nc.bs.trade.business.IBDBusiCheck;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.trade.pub.IBDACTION;
import nc.vo.wds.dm.sendinvdoc.SendinvdocVO;

public class BSCheck implements IBDBusiCheck {
	private BaseDAO dao;

	private BaseDAO getDao() {
		if (dao == null) {
			dao = new BaseDAO();
		}
		return dao;
	}

	public void check(int intBdAction, AggregatedValueObject vo, Object userObj)
			throws Exception {
		if (intBdAction != IBDACTION.SAVE) {
			return;
		}
		if (vo == null || vo.getParentVO() == null) {
			return;
		}

		// �ж����޸ĺ�ı��滹��������ı���
		SendinvdocVO ivo = (SendinvdocVO) vo.getParentVO();
		// �����������ı���ִ������Ĵ���
		if (ivo.getPrimaryKey() == null
				|| ivo.getPrimaryKey().trim().equals("")) {

			String condition = " pk_invbasdoc='" + ivo.getPk_invbasdoc()
					+ "' and  isnull(dr,0)=0";
			List list = (List) getDao().retrieveByClause(SendinvdocVO.class,
					condition);
			if (list == null || list.size() == 0) {
				// �жϸ��к��Ƿ������ݿ��д���
				String cond = " crow='" + ivo.getCrow()
						+ "' and  isnull(dr,0)=0";
				List lis = (List) getDao().retrieveByClause(SendinvdocVO.class,
						cond);
				if (lis == null || lis.size() == 0) {
					return;
				} else {
					throw new BusinessException(" ���к������ݿ����Ѿ����� ");
				}

			} else {
				throw new BusinessException(" �ô�������ݿ����Ѿ����� ");
			}
		} else {
			// ����ִ������Ĵ���
			String condition = " pk_wds_sendinvdoc='" + ivo.getPrimaryKey()
					+ "' and  isnull(dr,0)=0";
			List list = (List) getDao().retrieveByClause(SendinvdocVO.class,
					condition);

			if (list == null) {
				return;
			}
			// �ж��޸ĺ�ļ�¼���Ƿ�ı��˴�����������ݿ��еļ�¼��ui�еĵ�ǰ��¼���бȽϣ�
			SendinvdocVO ivo2 = (SendinvdocVO) list.get(0);
			if (ivo.getPk_invbasdoc().equalsIgnoreCase(ivo2.getPk_invbasdoc())) {
				// �ж��޸ĺ�ļ�¼���Ƿ�ı����к�
				if (ivo.getCrow().equals(ivo2.getCrow())) {
					return;
				} else {
					
					// �жϸ��к��Ƿ������ݿ��д���
					String cond = " crow='" + ivo.getCrow()
							+ "' and  isnull(dr,0)=0";
					List lis = (List) getDao().retrieveByClause(
							SendinvdocVO.class, cond);
					if (lis == null || lis.size() == 0) {
						return;
					} else {
						throw new BusinessException(" ���к������ݿ����Ѿ����� ");
					}
				}
			}

			String condition1 = " pk_invbasdoc='" + ivo.getPk_invbasdoc()
					+ "' and  isnull(dr,0)=0";
			List list1 = (List) getDao().retrieveByClause(SendinvdocVO.class,
					condition1);
			if (list1 == null || list1.size() == 0) {
				// �ж��޸ĺ�ļ�¼���Ƿ�ı����к�
				if (ivo.getCrow().equals(ivo2.getCrow())) {
					return;
				} else {
					// �жϸ��к��Ƿ������ݿ��д���
					String cond = " crow='" + ivo.getCrow()
							+ "' and  isnull(dr,0)=0";
					List lis = (List) getDao().retrieveByClause(
							SendinvdocVO.class, cond);
					if (lis == null || lis.size() == 0) {
						return;
					} else {
						throw new BusinessException(" ���к������ݿ����Ѿ����� ");
					}
				}
			} else {
				throw new BusinessException(" �ô�������ݿ����Ѿ����� ");
			}
		}

	}

	public void dealAfter(int intBdAction, AggregatedValueObject billVo,
			Object userObj) throws Exception {
		// TODO Auto-generated method stub

	}

}
