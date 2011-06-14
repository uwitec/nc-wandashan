package nc.bs.wds.tranprice.tonkilometre;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.wds.tranprice.tonkilometre.TranspriceHVO;
import nc.vo.wl.pub.WdsWlPubConst;

/**
 * 
 * @author Administrator �ֹ����˼۱��̨У����
 */
public class TonKilometerBs implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private BaseDAO dao = null;

	BaseDAO getBaseDAO() {
		if (dao == null) {
			dao = new BaseDAO();
		}
		return dao;
	}

	/**
	 * 
	 * @throws DAOException
	 * @���ߣ�lyf
	 * @˵�������ɽ������Ŀ ����ǰУ�� �Ѿ��������ĵ����Ƿ���� ���ڷ�Χ����
	 * @ʱ�䣺2011-5-24����01:05:16
	 */
	public void beforApprove(AggregatedValueObject vo) throws BusinessException {
		if (vo == null)
			return;
		TranspriceHVO hvo = (TranspriceHVO) vo.getParentVO();
		StringBuffer sql = new StringBuffer();
		sql.append("select * from wds_transprice_h where isnull(dr,0)=0  ");
		sql.append(" and vbillstatus='1'");// ����ͨ����
		sql.append(" and reserve1='"+hvo.getReserve1()+"'");//�ֿ�
		sql.append(" and carriersid='"+hvo.getCarriersid()+"'");//������ 		
		sql.append(" and pk_billtype='" + WdsWlPubConst.WDSI + "'");
		sql.append(" and( (dstartdate<='" + hvo.getDstartdate()
				+ "' and denddate>='" + hvo.getDstartdate() + "')");
		sql.append(" or (dstartdate<='" + hvo.getDenddate()
				+ "' and denddate>='" + hvo.getDenddate() + "')");
		sql.append(" or (dstartdate>='" + hvo.getDstartdate()
				+ "' and denddate<='" + hvo.getDenddate() + "'))");
		List<TranspriceHVO> list = (ArrayList<TranspriceHVO>) getBaseDAO()
				.executeQuery(sql.toString(),
						new BeanListProcessor(TranspriceHVO.class));
		if (list.size() > 0) {
			TranspriceHVO oldHvo = list.get(0);
			throw new BusinessException("���Ѿ���������ͬ�ֿ����ͬ������ �ֹ����˼۱�������ڽ���:\n���ݱ��="
					+ oldHvo.getVbillno() +"\n�˼۱���="+oldHvo.getVpricecode()+"\n�˼�����="+oldHvo.getVpricename()+"\n��ʼ����=" + oldHvo.getDstartdate()
					+ "\n��ֹ����=" + oldHvo.getDenddate());
		}

	}

}
