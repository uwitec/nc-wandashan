package nc.bs.ic.tpyd;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.wl.pub.WdsPubResulSetProcesser;
import nc.jdbc.framework.SQLParameter;
import nc.vo.ic.tpyd.TpydBVO;
import nc.vo.ic.tpyd.TpydHVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.wl.pub.WdsWlPubTool;

public class TpydBO {
	
	private BaseDAO dao = null;

	BaseDAO getBaseDAO(){
		if(dao==null){
			dao = new BaseDAO();
		}
		return dao;
	}
	/**
	 * 
	 * @throws Exception 
	 * @���ߣ�lyf
	 * @˵�������ɽ������Ŀ 
	 * �����ƶ� ������д��
	 * ��� �Ƴ������Ƿ��Ѿ��仯���󶨴��û�иı�,�������û�б仯,�ƶ�����û�г�����;
	 * ��� Ŀ�������ǿ��ã��󶨴��û�иı䣬��ȻΪ��,�ƶ�����û�г�����;
	 * ���ͨ������ȥ�Ƴ����̿��������������������ȫ���ߣ���ı�״̬Ϊ����;
	 *         �����������ӿ���������ı�״̬Ϊռ��;
	 * @ʱ�䣺2011-4-10����06:59:34
	 */
	public void writeBack(AggregatedValueObject obj ) throws Exception{
		if(obj == null || obj.getParentVO()==null
				|| obj.getChildrenVO() == null
				|| obj.getChildrenVO().length ==0){
			return ;
		}
		TpydHVO hvo = (TpydHVO )obj.getParentVO();
		TpydBVO[] bvos = (TpydBVO[])obj.getChildrenVO();
		StringBuffer sql = new StringBuffer();
		sql.append("select count(0) from tb_warehousestock where isnull(dr,0)=0 and pk_corp='"+hvo.getPk_corp()+"'");
		sql.append(" and pk_cargdoc=? and pplpt_pk=? and pk_invmandoc=? and whs_stocktonnage=? and whs_stockpieces=?");
		SQLParameter para = new SQLParameter() ;
		int i=1;
		for(TpydBVO bvo :bvos){
			para.addParam(hvo.getPk_cargedoc());
			para.addParam(bvo.getPk_trayout());
			para.addParam(bvo.getPk_invmandoc());
			para.addParam(bvo.getNoutnum());
			para.addParam(bvo.getNoutassnum());
			Integer count =PuPubVO.getInteger_NullAs(getBaseDAO().executeQuery(sql.toString(), para, WdsPubResulSetProcesser.COLUMNPROCESSOR), 0);
			if(count == 0){
				throw new BusinessException("�ڱ����"+i+"�У��Ƴ�������Ϣ�󶨴���������̿�������Ѿ��ı�");
			}
			
			para.clearParams();
			para.addParam(hvo.getPk_cargedoc());
			para.addParam(bvo.getPk_trayin());
			para.addParam(bvo.getPk_invmandoc());
			para.addParam(PuPubVO.getUFDouble_NullAsZero(bvo.getNinnum()));
			para.addParam(PuPubVO.getUFDouble_NullAsZero(bvo.getNinassnum()));
			count =PuPubVO.getInteger_NullAs(getBaseDAO().executeQuery(sql.toString(), para, WdsPubResulSetProcesser.COLUMNPROCESSOR), 0);
			if(count == 0){
				throw new BusinessException("�ڱ����"+i+"�У�����������Ϣ�󶨴���ı����������ռ��");
			}
			i++;
			para.clearParams();
		}
		
		String upStockInv = "update tb_warehousestock set whs_stocktonnage=coalesce(whs_stocktonnage,0)+?,whs_stockpieces=coalesce(whs_stockpieces,0)+?" +
				" where pk_cargdoc=? and pplpt_pk=? and pk_invmandoc=? ";
		String upCargeTary =" update bd_cargdoc_tray set cdt_traystatus=? " +
				" where pk_cargdoc=? and cdt_pk=?";
		for(TpydBVO bvo :bvos){
			UFDouble noutnum = PuPubVO.getUFDouble_NullAsZero(bvo.getNoutnum());
//			UFDouble noutassnum = PuPubVO.getUFDouble_NullAsZero(bvo.getNoutassnum());
			UFDouble nmovenum = PuPubVO.getUFDouble_NullAsZero(bvo.getNmovenum());
			UFDouble nmoveassnum = PuPubVO.getUFDouble_NullAsZero(bvo.getNmoveassnum());
			para.addParam(nmovenum.multiply(-1));
			para.addParam(nmoveassnum.multiply(-1));
			para.addParam(hvo.getPk_cargedoc());
			para.addParam(bvo.getPk_trayout());
			para.addParam(bvo.getPk_invmandoc());
			getBaseDAO().executeUpdate(upStockInv,para);
			para.clearParams();
			if((nmovenum.sub(noutnum)).doubleValue()==0){//��������ƿգ���ı�����״̬Ϊ��
				para.addParam(0);
				para.addParam(hvo.getPk_cargedoc());
				para.addParam(bvo.getPk_trayout());
				getBaseDAO().executeUpdate(upCargeTary,para);
				para.clearParams();
			}
			para.addParam(nmovenum);
			para.addParam(nmoveassnum);
			para.addParam(hvo.getPk_cargedoc());
			para.addParam(bvo.getPk_trayin());
			para.addParam(bvo.getPk_invmandoc());
			getBaseDAO().executeUpdate(upStockInv,para);
			para.clearParams();
			para.addParam(1);
			para.addParam(hvo.getPk_cargedoc());
			para.addParam(bvo.getPk_trayin());
			getBaseDAO().executeUpdate(upCargeTary,para);
			para.clearParams();
			
		}
	}

}
