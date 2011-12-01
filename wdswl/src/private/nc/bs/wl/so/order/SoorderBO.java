package nc.bs.wl.so.order;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.ImageIcon;

import oracle.sql.BLOB;
import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.wl.pub.WdsPubResulSetProcesser;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.vo.dm.so.order.SoorderBVO;
import nc.vo.dm.so.order.SoorderVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.wds.dm.corpseal.CorpsealVO;
import nc.vo.wl.pub.WdsWlPubConst;
/**
 * 销售运单（WDS5）后台查询类
 * @author Administrator
 *
 */
public class SoorderBO {

	
	BaseDAO dao = null;
	private BaseDAO getBaseDAO(){
		if(dao==null){
			dao = new BaseDAO();
		}
		return dao;
	}
	
	/**
	 * 
	 * @作者：lyf
	 * @说明：完达山物流项目 
	 *   弃审前校验 是否已经安排生产销售出库单
	 * @时间：2011-3-27上午09:44:46
	 * @param 要弃审的 发运计划单据
	 * @throws BusinessException 
	 */
	public void beforeUnApprove(AggregatedValueObject obj) throws BusinessException{
		if(obj ==null){
			return;
		}
		SoorderVO parent =(SoorderVO) obj.getParentVO();
		String pk_soorder = parent.getPk_soorder();
		StringBuffer sql = new StringBuffer();	
		sql.append(" select count(*) ");
		sql.append(" from tb_outgeneral_h ");
		sql.append(" join tb_outgeneral_b ");
		sql.append(" on tb_outgeneral_h.general_pk= tb_outgeneral_b.general_pk");
		sql.append(" where isnull(tb_outgeneral_h.dr,0)=0 and isnull(tb_outgeneral_b.dr,0)=0 ");
		sql.append(" and tb_outgeneral_b.csourcebillhid ='"+pk_soorder+"'");
		int i = PuPubVO.getInteger_NullAs(getBaseDAO().executeQuery(sql.toString(), WdsPubResulSetProcesser.COLUMNPROCESSOR), 0);
		if( i>0){
			throw new BusinessException("已有下游销售出库单，请先删除再做此操作");
		}
	}
	
	//销售运单作废,回写销售订单
	public void backSoleOrder(AggregatedValueObject obj)throws BusinessException{
		if(obj ==null){
			return;
		}
		try{
			SoorderBVO[] bvo = (SoorderBVO[])obj.getChildrenVO();
			if(bvo!=null && bvo.length>0){
				for(int i = 0 ;i< bvo.length; i++){
					String sql = "update so_saleorder_b set "+WdsWlPubConst.DM_SO_DEALNUM_FIELD_NAME+" = coalesce("+WdsWlPubConst.DM_SO_DEALNUM_FIELD_NAME+",0)-"+
					PuPubVO.getUFDouble_NullAsZero(bvo[i].getNarrangnmu()).doubleValue()+" where corder_bid='"+bvo[i].getCsourcebillbid()+"' ";
					if(getBaseDAO().executeUpdate(sql)==0){
						throw new BusinessException("数据异常,回写销售订单错误，请重新查询数据");
					};
				}
			}
		}catch(Exception e){
			throw new BusinessException("数据异常,回写销售订单错误，请重新查询数据");
		}

	}
	

	/**
	 * 
	 * @作者：
	 * @说明：完达山物流项目
	 * @时间：2011-10-31下午12:29:20
	 * @param 根据客商管理档案id，获取图片
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
		ArrayList<CorpsealVO> list = (ArrayList<CorpsealVO>) getBaseDAO().executeQuery(
				sql.toString(), new BeanListProcessor(CorpsealVO.class));
		if (list != null && list.size() > 0) {
			image = list.get(0).getCorpseal();
		}
		return image;
	}
}
