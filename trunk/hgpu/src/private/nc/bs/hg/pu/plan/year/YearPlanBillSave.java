package nc.bs.hg.pu.plan.year;

import java.util.ArrayList;

import nc.bs.dao.BaseDAO;
import nc.bs.hg.pu.pub.HYBillSave;
import nc.bs.hg.pu.pub.HgBsPubTool;
import nc.bs.scm.pub.TempTableDMO;
import nc.vo.hg.pu.pub.HgPubTool;
import nc.vo.hg.pu.pub.PlanVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.scm.pu.PuPubVO;

/**
 * ���ݱ���ǰ����
 * 
 * @author
 * 
 */
public class YearPlanBillSave extends HYBillSave {

	private BaseDAO dao = null;
	private BaseDAO getBaseDao(){
		if(dao == null){
			dao = new BaseDAO();
		}
		return dao;
	}

	/**
	 * ���ݱ���ǰ����
	 * 
	 * @param billVo
	 * @throws BusinessException
	 *             (2006-11-30 ����10:39:22)<br>
	 */
	protected void beforeSave(AggregatedValueObject billVo)
	throws BusinessException {
		if (billVo == null)
			throw new BusinessException("��������Ϊ��");

		PlanVO head = (PlanVO) billVo.getParentVO();
		String pk_plan = head.getPrimaryKey();
		ArrayList<String> cinventoryid = getArrayByParse(billVo,"cinventoryid");//�������ID
		StringBuffer sql = new StringBuffer();
		sql.append(" select count(*) cum from hg_plan h,hg_planother_b b where h.pk_plan = b.pk_plan and isnull(h.dr,0)=0 and isnull(b.dr,0) = 0 ");
		sql.append( " and h.cyear = '"+head.getCyear()+"' and h.pk_corp = '"+head.getPk_corp()+"'and h.pk_billtype ='HG02' ");
		sql.append(" and h.capplydeptid = '"+head.getCapplydeptid()+"'");
		if(pk_plan != null && !"".equals(pk_plan)){//�޸�ʱ����У�鵱ǰ����
			sql.append("	and h.pk_plan <> '"+pk_plan+"' ");
		}
		if(cinventoryid!=null && cinventoryid.size()>0){
			//������ʱ�����
			TempTableDMO tdmo = null;
			try {
				tdmo = new TempTableDMO();
				sql.append("	and b.cinventoryid in "+tdmo.insertTempTable(cinventoryid,"temp_hg_plan", "cinventoryid","varchar(20)"));
			} catch (Exception e) {
				e.printStackTrace();
				throw new BusinessException("��ʱ�����������");
			}
		}

		int o = PuPubVO.getInteger_NullAs(getBaseDao().executeQuery(sql.toString(), HgBsPubTool.COLUMNPROCESSOR),HgPubTool.INTEGER_ZERO_VALUE);
		if(o>0){
			throw new BusinessException("�������ڱ�����Ѿ�����!");
			}
		}
	
	/**
	 * ȡ����������Ҫ��ID����������Ӧ������
	 */
	public ArrayList<String> getArrayByParse(nc.vo.pub.AggregatedValueObject vo,String parse){
		ArrayList<String> list = new ArrayList<String>();
		CircularlyAccessibleValueObject[] childvos = vo.getChildrenVO();
		if(vo != null && childvos	!= null && childvos.length>0){
			for(CircularlyAccessibleValueObject cvo : childvos){
				list.add(String.valueOf(cvo.getAttributeValue(parse)));
			}
		}
		return list;
	}
}
