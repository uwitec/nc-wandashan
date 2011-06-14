package nc.bs.wds.load.teamdoc;

import java.util.List;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.trade.business.IBDBusiCheck;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.trade.pub.IBDACTION;
import nc.vo.wds.dm.sendinvdoc.SendinvdocVO;
import nc.vo.wds.load.teamdoc.TeamdocBVO;
import nc.vo.wds.load.teamdoc.TeamdocHVO;

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

		validateTeamCode(vo);

	}
	//������ĳ�ֶε�Ψһ�Ե�У��
	private void validateTeamCode(AggregatedValueObject vo) throws Exception{
		// �ж����޸ĺ�ı��滹��������ı���
		TeamdocHVO ivo = (TeamdocHVO) vo.getParentVO();
		// �����������ı���ִ������Ĵ���
		if (ivo.getPrimaryKey() == null	|| ivo.getPrimaryKey().trim().equals("")) {

			String condition = " teamcode='" + ivo.getTeamcode()
					+ "' and  isnull(dr,0)=0";
			List list = (List) getDao().retrieveByClause(TeamdocHVO.class,
					condition);
			if (list == null || list.size() == 0) {
				//�Ա�����Ա����Ψһ�Ե�У��
				validatePsn(vo);
				
			}else{
				throw new BusinessException(" �ð�����������ݿ����Ѿ����� ");
			}
		}else{
			// ����ִ������Ĵ���
			String condition = " pk_wds_teamdoc_h='" + ivo.getPrimaryKey()
					+ "' and  isnull(dr,0)=0";
			List list = (List) getDao().retrieveByClause(TeamdocHVO.class,
					condition);

			if (list == null) {
				return;
			}
			// �ж��޸ĺ�ļ�¼���Ƿ�ı��˴�����������ݿ��еļ�¼��ui�еĵ�ǰ��¼���бȽϣ�
			TeamdocHVO ivo2 = (TeamdocHVO) list.get(0);
			if (ivo.getTeamcode().equalsIgnoreCase(ivo2.getTeamcode())) {
				
				// �ж���Ա�����Ƿ������ݿ��д���
				validatePsn(vo);

			}else{

				String condition1 = " teamcode='" + ivo.getTeamcode()
						+ "' and  isnull(dr,0)=0";
				List list1 = (List) getDao().retrieveByClause(TeamdocHVO.class,
						condition1);
				if (list1 == null || list1.size() == 0) {
					//�ж���Ա�����Ƿ������ݿ��д���
					validatePsn(vo);

				} else {
					throw new BusinessException(" �ð�����������ݿ����Ѿ����� ");
				}
			}
		}
		
		
		
	}
	

   //���ӱ�ĳ�ֶε�Ψһ�Ե�У�飬Ƕ������������
	private void validatePsn(AggregatedValueObject vo) throws Exception{	
		TeamdocHVO h = (TeamdocHVO) vo.getParentVO();
		// �ж���Ա�����Ƿ������ݿ��д���
		TeamdocBVO[] bvos = (TeamdocBVO[]) vo.getChildrenVO();
		for (int i = 0; i < bvos.length; i++) {
			
			//�жϱ�������ʱ������ı��滹�Ǳ༭�󱣴�					
			if (bvos[i].getPrimaryKey() == null
					|| bvos[i].getPrimaryKey().trim().equals("")) {

				String cond = " psncode='" + bvos[i].getPsncode()
						+ "' and  isnull(dr,0)=0 and pk_wds_teamdoc_h='"+h.getPrimaryKey()+"'";
				List lis = (List) getDao().retrieveByClause(
						TeamdocBVO.class, cond);
				if (lis == null || lis.size() == 0) {
					continue;
				} else {
					throw new BusinessException(" ͬһ����,��Ա���벻���ظ� ");
				}
			}else{
				String cond1= " pk_wds_teamdoc_b='" + bvos[i].getPrimaryKey()
				+ "' and  isnull(dr,0)=0 and pk_wds_teamdoc_h='"+h.getPrimaryKey()+"'";
				List lis1 = (List) getDao().retrieveByClause(TeamdocBVO.class, cond1);
				if(lis1==null ||lis1.size()==0){
					return;
				}
				TeamdocBVO tbo=(TeamdocBVO) lis1.get(0);
				if(tbo.getPsncode().equalsIgnoreCase(bvos[i].getPsncode())){
					return;
				} else {
					String cond = " psncode='" + bvos[i].getPsncode()
							+ "' and  isnull(dr,0)=0 and pk_wds_teamdoc_h='"+h.getPrimaryKey()+"'";
					List lis = (List) getDao().retrieveByClause(
							TeamdocBVO.class, cond);
					if (lis == null || lis.size() == 0) {
						continue;
					} else {
						throw new BusinessException(" ͬһ����,��Ա���벻���ظ� ");
					}							
				}						
			}
		}		
	}

	public void dealAfter(int intBdAction, AggregatedValueObject billVo,
			Object userObj) throws Exception {
		// TODO Auto-generated method stub

	}

}
