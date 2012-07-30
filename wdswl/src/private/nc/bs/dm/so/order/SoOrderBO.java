package nc.bs.dm.so.order;

import java.io.IOException;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.ImageIcon;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.wl.pub.WdsPubResulSetProcesser;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.ui.scm.util.ObjectUtils;
import nc.vo.dm.so.order.SoorderBVO;
import nc.vo.dm.so.order.SoorderVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.VOStatus;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.scm.constant.ScmConst;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.trade.pub.IBDACTION;
import nc.vo.wds.dm.corpseal.CorpsealVO;
import nc.vo.wl.pub.WdsWlPubConst;
import nc.vo.zmpub.pub.tool.WriteBackTool;
import oracle.sql.BLOB;

/**
 * �����˵���̨ ҵ������
 * 
 * @author mlr
 * 
 */
public class SoOrderBO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8194441508716877335L;

	BaseDAO dao = null;

	private BaseDAO getBaseDAO() {
		if (dao == null) {
			dao = new BaseDAO();
		}
		return dao;
	}

	/**
	 * �����˵� ��д���۶���
	 * 
	 * @���ߣ�mlr
	 * @˵�������ɽ������Ŀ
	 * @ʱ�䣺2012-6-26����02:54:51
	 * @param billvo
	 * @param iBdAction
	 * @throws Exception
	 */
	public void writeBack(AggregatedValueObject billvo, int iBdAction)
			throws Exception {
		if (billvo == null || billvo.getParentVO() == null
				|| billvo.getChildrenVO() == null
				|| billvo.getChildrenVO().length == 0)
			return;
		SoorderBVO[] bodys = (SoorderBVO[]) ObjectUtils
				.serializableClone(billvo.getChildrenVO());
		String csourcetype = PuPubVO.getString_TrimZeroLenAsNull(bodys[0]
				.getCsourcetype());
		// ���Ƶ��ݲ���Ҫ��д
		if (csourcetype == null) {
			return;
		}

		WriteBackTool.setVsourcebillid("csourcebillhid");
		WriteBackTool.setVsourcebillrowid("csourcebillbid");
		WriteBackTool.setVsourcebilltype("csourcetype");
		if (iBdAction == IBDACTION.SAVE) {

			if (csourcetype.equals(ScmConst.SO_Order)) {
				WriteBackTool
						.writeBack(
								bodys,
								"so_saleorder_b",
								"corder_bid",
								new String[] { "narrangnmu" },
								new String[] { WdsWlPubConst.DM_SO_DEALNUM_FIELD_NAME },
								new String[] { WdsWlPubConst.DM_SO_DEALNUM_FIELD_NAME });
			}
		} else if (iBdAction == IBDACTION.DELETE) {
			for (int i = 0; i < bodys.length; i++) {
				bodys[i].setStatus(VOStatus.DELETED);
			}
			WriteBackTool.writeBack(bodys, "so_saleorder_b", "corder_bid",
					new String[] { "narrangnmu" },
					new String[] { WdsWlPubConst.DM_SO_DEALNUM_FIELD_NAME },
					new String[] { WdsWlPubConst.DM_SO_DEALNUM_FIELD_NAME });

		}
	}

	/**
	 * 
	 * @���ߣ�lyf
	 * @˵�������ɽ������Ŀ ����ǰУ�� �Ƿ��Ѿ������������۳��ⵥ
	 * @ʱ�䣺2011-3-27����09:44:46
	 * @param Ҫ�����
	 *            ���˼ƻ�����
	 * @throws BusinessException
	 */
	public void beforeUnApprove(AggregatedValueObject obj)
			throws BusinessException {
		if (obj == null) {
			return;
		}
		SoorderVO parent = (SoorderVO) obj.getParentVO();
		String pk_soorder = parent.getPk_soorder();
		StringBuffer sql = new StringBuffer();
		sql.append(" select count(*) ");
		sql.append(" from tb_outgeneral_h ");
		sql.append(" join tb_outgeneral_b ");
		sql
				.append(" on tb_outgeneral_h.general_pk= tb_outgeneral_b.general_pk");
		sql
				.append(" where isnull(tb_outgeneral_h.dr,0)=0 and isnull(tb_outgeneral_b.dr,0)=0 ");
		sql.append(" and tb_outgeneral_b.csourcebillhid ='" + pk_soorder + "'");
		int i = PuPubVO.getInteger_NullAs(getBaseDAO().executeQuery(
				sql.toString(), WdsPubResulSetProcesser.COLUMNPROCESSOR), 0);
		if (i > 0) {
			throw new BusinessException("�����������۳��ⵥ������ɾ�������˲���");
		}
	}

	/**
	 * 
	 * @���ߣ�
	 * @˵�������ɽ������Ŀ
	 * @ʱ�䣺2011-10-31����12:29:20
	 * @param ���ݿ��̹�����id
	 *            ����ȡͼƬ
	 * @return
	 * @throws DAOException
	 * @throws SQLException
	 * @throws IOException
	 */
	public ImageIcon getCorpImag(String pk_cumandoc) throws DAOException,
			SQLException, IOException {
		if (pk_cumandoc == null || "".equalsIgnoreCase(pk_cumandoc)) {
			return null;
		}
		ImageIcon image = null;
		BLOB blob = null;
		StringBuffer sql = new StringBuffer();
		sql.append(" select * from wds_corpseal ");
		sql.append(" where nvl(dr,0)=0 ");
		sql.append(" and pk_cumandoc='" + pk_cumandoc + "'");
		ArrayList<CorpsealVO> list = (ArrayList<CorpsealVO>) getBaseDAO()
				.executeQuery(sql.toString(),
						new BeanListProcessor(CorpsealVO.class));
		if (list != null && list.size() > 0) {
			image = list.get(0).getCorpseal();
		}
		return image;
	}

	public void chekcTranscorp(AggregatedValueObject vo)
			throws BusinessException {
		// add by yf 2012-07-27 �����˵� ����ʱУ�� ��������˵� �����־λreserve16 Ϊtrue ʱ У�� ������
		// ���Ƿ������־λ ������true
		CircularlyAccessibleValueObject head = vo.getParentVO();
		if (head == null) {
			return;
		}
		UFBoolean iszt = PuPubVO.getUFBoolean_NullAs(head
				.getAttributeValue("reserve16"), UFBoolean.FALSE);
		if (iszt.booleanValue()&&PuPubVO.getString_TrimZeroLenAsNull(head.getPrimaryKey())!=null) {
			String pk_transcorp = PuPubVO.getString_TrimZeroLenAsNull(head
					.getAttributeValue("pk_transcorp"));
			checkTranscorp(pk_transcorp);
		}
	}

	private void checkTranscorp(String pk_transcorp)
			throws BusinessException {
		if(pk_transcorp == null){
			throw new BusinessException("������Ϊ��");
		}
		StringBuffer sql = new StringBuffer();
		sql.append(" select ");
		sql.append(" reserve16 ");
		sql.append(" from wds_tanscorp_h ");
		sql.append(" where ");
		sql.append(" isnull(dr,0)=0 ");
		sql.append(" and pk_wds_tanscorp_h = '" + pk_transcorp + "' ");
		Object o = getBaseDAO().executeQuery(sql.toString(),
				WdsPubResulSetProcesser.COLUMNPROCESSOR);
		if (!PuPubVO.getUFBoolean_NullAs(o, UFBoolean.FALSE).booleanValue()) {
			throw new BusinessException("�����̲����������׼");
		}
	}

}
