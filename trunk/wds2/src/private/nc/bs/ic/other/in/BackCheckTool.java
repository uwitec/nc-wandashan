package nc.bs.ic.other.in;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import nc.bs.dao.BaseDAO;
import nc.bs.trade.business.IBDBusiCheck;
import nc.bs.wl.pub.WdsPubResulSetProcesser;
import nc.itf.scm.cenpur.service.TempTableUtil;
import nc.jdbc.framework.SQLParameter;
import nc.vo.ic.pub.StockInvOnHandVO;
import nc.vo.ic.pub.TbGeneralBBVO;
import nc.vo.ic.pub.TbGeneralBVO;
import nc.vo.ic.pub.TbGeneralHVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.trade.pub.IBDACTION;
import nc.vo.wl.pub.WdsWlPubConst;

public class BackCheckTool implements IBDBusiCheck {
	private BaseDAO dao = null;
	private BaseDAO getBaseDao(){
		if(dao == null){
			dao = new BaseDAO();
		}
		return dao;
	}
	public void check(int intBdAction, AggregatedValueObject vo, Object userObj)
			throws Exception {
		// TODO Auto-generated method stub
//		新增保存校验  生成单据号   存货+批次不能重复
		
		if(intBdAction == IBDACTION.SAVE){
			TbGeneralHVO head = (TbGeneralHVO)vo.getParentVO();
			if(PuPubVO.getString_TrimZeroLenAsNull(head.getGeh_billcode())==null)
				throw new BusinessException("单据号为空");
			TbGeneralBVO[] bodys = (TbGeneralBVO[])vo.getChildrenVO();
			//校验存货+批次号不能重复
//			BaseDAO dao = new BaseDAO();
			String sql = " select count(0) from tb_general_b where geb_cinvbasid = ? and geb_vbatchcode = ? and isnull(dr,0)=0";
			SQLParameter parameter = null;
			for(TbGeneralBVO body:bodys){
				parameter = new SQLParameter();
				parameter.addParam(body.getGeb_cinvbasid());
				parameter.addParam(body.getGeb_vbatchcode());
				int index = PuPubVO.getInteger_NullAs(getBaseDao().executeQuery(sql, parameter, WdsPubResulSetProcesser.COLUMNPROCESSOR),-1);
				if(index>0)
					throw new BusinessException("批次号："+body.getGeb_vbatchcode()+"重复");
			}
		}
		
	}

	public void dealAfter(int intBdAction, AggregatedValueObject billVo,
			Object userObj) throws Exception {
		// TODO Auto-generated method stub
		//生成存货状态信息   如果该存货该批次存货状态已存在 保持不变
		
		if(intBdAction == IBDACTION.SAVE){
			TbGeneralHVO head = (TbGeneralHVO)billVo.getParentVO();
			if(PuPubVO.getString_TrimZeroLenAsNull(head.getPrimaryKey())==null){
				//插入存货状态表数据  维护现存量
				insertInvBatchState((TbGeneralBVO[])billVo.getChildrenVO(),head.getPk_cargdoc());
			}else{
				//前台控制  其他入库单修改时 ：行操作  存货  批次  都不能修改
			}
				
		}
	}
	
	private void insertInvBatchState(TbGeneralBVO[] bodys,String cargdocPK) throws BusinessException{
		if(bodys == null || bodys.length == 0){
			return;
		}

		//获取托盘明细信息
		String[] bids = new String[bodys.length];
		for(int i=0;i<bodys.length;i++){
			bids[i] = bodys[i].getGeb_pk();
		}

		String whereSql = " isnull(dr,0)=0 and geb_pk in "+new TempTableUtil().getSubSql(bids);

		Collection<TbGeneralBBVO> bbodys = (Collection<TbGeneralBBVO>)getBaseDao().retrieveByClause(TbGeneralBBVO.class, whereSql);

		if(bbodys == null || bbodys.size() ==0)
			throw new BusinessException("获取存放托盘信息异常");

		StockInvOnHandVO[] tbWarehousestockVO = new StockInvOnHandVO[bbodys.size()];

		Iterator<TbGeneralBBVO> it = bbodys.iterator();
		TbGeneralBBVO tbbbvo = null;
		StockInvOnHandVO tbWarehousestockVO1 = null;
		ArrayList<StockInvOnHandVO> ldata = new ArrayList<StockInvOnHandVO>();
		while(it.hasNext()){
			tbWarehousestockVO1 = new StockInvOnHandVO();



			// 托盘主键

			tbWarehousestockVO1.setPplpt_pk(tbbbvo.getCdt_pk());

			// dr
			tbWarehousestockVO1.setDr(0);
			// 辅数量

			tbWarehousestockVO1.setWhs_stockpieces(tbbbvo
					.getGebb_num());
			tbWarehousestockVO1.setWhs_oanum(tbbbvo.gebb_num);

			// 换算率

			tbWarehousestockVO1
			.setWhs_hsl(tbbbvo.getGebb_hsl());

			// 单价

			tbWarehousestockVO1.setWhs_nprice(tbbbvo
					.getGebb_nprice());

			// 金额

			tbWarehousestockVO1.setWhs_nmny(tbbbvo
					.getGebb_nmny());

			// 主数量

			tbWarehousestockVO1.setWhs_stocktonnage(PuPubVO.getUFDouble_NullAsZero(tbbbvo
					.getGebb_num()).multiply(
							tbbbvo.getGebb_hsl()
					));
			tbWarehousestockVO1.setWhs_omnum(PuPubVO.getUFDouble_NullAsZero(tbbbvo
					.getGebb_num()).multiply(
							tbbbvo.getGebb_hsl()
					));
			// 库存检查状态(默认合适)
			tbWarehousestockVO1.setSs_pk(WdsWlPubConst.default_inv_state);
			// 库存表状态
			tbWarehousestockVO1.setWhs_status(0);
			// 类型
			tbWarehousestockVO1.setWhs_type(1);
			//

			tbWarehousestockVO1.setPk_bodysource(tbbbvo
					.getPwb_pk());

			// 存货档案主键

			tbWarehousestockVO1.setPk_invbasdoc(tbbbvo
					.getPk_invbasdoc());

			// 批次号

			tbWarehousestockVO1.setWhs_batchcode(tbbbvo
					.getGebb_vbatchcode());

			// 回写批次号
			if (null != tbbbvo.getGebb_lvbatchcode()
					&& !"".equals(tbbbvo.getGebb_lvbatchcode())) {
				tbWarehousestockVO1.setWhs_lbatchcode(tbbbvo
						.getGebb_lvbatchcode());
			} else {
				tbWarehousestockVO1.setWhs_lbatchcode("2009");
			}
			tbWarehousestockVO1.setPk_cargdoc(cargdocPK);
			// 来源单据表体主键， 缓存表主键
			tbWarehousestockVO1.setPk_bodysource(tbbbvo
					.getGebb_pk());
			
			ldata.add(tbWarehousestockVO1);

		}

		if(ldata.size()>0){
			getBaseDao().insertVOArray(ldata.toArray(new StockInvOnHandVO[0]));
		}
	}
	

}
