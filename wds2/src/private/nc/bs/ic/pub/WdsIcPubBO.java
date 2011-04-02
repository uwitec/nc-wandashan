package nc.bs.ic.pub;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import nc.bs.dao.BaseDAO;
import nc.bs.wl.pub.WdsPubResulSetProcesser;
import nc.bs.wl.pub.WdsWlPubBO;
import nc.itf.scm.cenpur.service.TempTableUtil;
import nc.vo.ic.pub.TbGeneralBBVO;
import nc.vo.ic.pub.TbGeneralBVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.ValidationException;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.wds.w8004040204.TbOutgeneralBVO;
import nc.vo.wl.pub.WdsWlPubTool;

public class WdsIcPubBO {
	private BaseDAO m_dao = null;

	private BaseDAO getDao() {
		if (m_dao == null) {
			m_dao = new BaseDAO();
		}
		return m_dao;
	}

//	/**
//	 * 
//	 * @作者：zhf
//	 * @说明：完达山物流项目 校验货位空间是否够用
//	 * @时间：2011-3-31下午04:17:10
//	 * @param bodys
//	 * @param iszc
//	 * @throws ValidationException
//	 */
//	private void checkSpaceIsEnable(TbGeneralBVO[] bodys, String cwhid,
//			String cspaceid) throws Exception {}
	private void doSaveTrayInfor(List<TbGeneralBBVO>lallbbvos) throws BusinessException{
		if(lallbbvos == null || lallbbvos.size() == 0)
			return;
		List<String> lbid = new ArrayList<String>();
		String tmp = null;
		for(TbGeneralBBVO body:lallbbvos){
			tmp = body.getGeb_pk();
			if(lbid.contains(tmp))
				continue;
			lbid.add(tmp);
		}
		String sql = " update tb_general_b_b set dr = 1 where geb_pk in "+new TempTableUtil().getSubSql(lbid.toArray(new String[0]));
		getDao().executeUpdate(sql);
		getDao().insertVOArray(lallbbvos.toArray(new TbGeneralBBVO[0]));
	}

	/**
	 * 
	 * @作者：zhf
	 * @说明：完达山物流项目 将本次数量分摊到托盘  分摊方式 一个托盘只能放货物一次如果托盘不为空则不能使用
	 *        结果即是：一个托盘只能存在一个批次的货物      托盘状态=0 空  托盘状态=1 存在货物
	 * @时间：2011-3-31下午07:24:10
	 * @param tmpTrays 本次可用托盘id数组
	 * @param body 库存单据体
	 * @param iVolumn 当前物资托盘容积
	 * 
	 */
	private List<TbGeneralBBVO> shareNumToTrays(String[] tmpTrays, TbGeneralBVO body,
			int iVolumn,boolean iszc) {
		UFDouble geb_bsnumd = PuPubVO.getUFDouble_NullAsZero(body.getGeb_bsnum());
//				WdsWlPubTool.INTEGER_ZERO_VALUE);
		int k = 0;
		ArrayList<TbGeneralBBVO> lbbvos = new ArrayList<TbGeneralBBVO>();
		TbGeneralBBVO tbgbbvo = null;
		if(iszc){
			while (geb_bsnumd.doubleValue() > iVolumn) {
				tbgbbvo = new TbGeneralBBVO();
				// 批次
				tbgbbvo.setGebb_vbatchcode(body.getGeb_vbatchcode());
				// 回写批次
				tbgbbvo.setGebb_lvbatchcode(body.getGeb_backvbatchcode());
				// 行号
				tbgbbvo.setGebb_rowno(k + 1 + "0");
				// 出库单表体主键
				tbgbbvo.setPwb_pk(body.getGeb_cgeneralbid());//------------------
				// 换算率
				tbgbbvo.setGebb_hsl(body.getGeb_hsl());
				// 运货档案主键
				tbgbbvo.setPk_invbasdoc(body.getGeb_cinvbasid());
				// 入库单子表主键
				tbgbbvo.setGeb_pk(body.getGeb_pk());//-----------------
				// 单价
				tbgbbvo.setGebb_nprice(body.getGeb_nprice());
				// 金额
				tbgbbvo.setGebb_nmny(body.getGeb_nmny());
				// 托盘实际存放数量
				tbgbbvo.setGebb_num(PuPubVO.getUFDouble_NullAsZero(iVolumn));
				geb_bsnumd = geb_bsnumd.sub(iVolumn);
				// 托盘主键
				tbgbbvo.setCdt_pk(tmpTrays[k]);
				tbgbbvo.setDr(0);
				tbgbbvo.setGeb_pk(body.getGeb_pk());
				lbbvos.add(tbgbbvo);
				k++;
			}
		}

		TbGeneralBBVO tbgbbvo1 = new TbGeneralBBVO();
		// 批次
		tbgbbvo1.setGebb_vbatchcode(body.getGeb_vbatchcode());
		// 回写批次
		tbgbbvo1.setGebb_lvbatchcode(body.getGeb_backvbatchcode());
		// 行号
		tbgbbvo1.setGebb_rowno(k + 1 + "0");
		// 出库单表体主键
		tbgbbvo1.setPwb_pk(body.getGeb_cgeneralbid());
		// 换算率
		tbgbbvo1.setGebb_hsl(body.getGeb_hsl());
		// 运货档案主键
		tbgbbvo1.setPk_invbasdoc(body.getGeb_cinvbasid());
		// 入库单子表主键
		tbgbbvo1.setGeb_pk(body.getGeb_pk());
		// 单价
		tbgbbvo1.setGebb_nprice(body.getGeb_nprice());
		// 金额
		tbgbbvo1.setGebb_nmny(body.getGeb_nmny());
		// 托盘实际存放数量
		tbgbbvo1.setGebb_num(PuPubVO.getUFDouble_NullAsZero(geb_bsnumd));
		// 托盘主键
		tbgbbvo1.setCdt_pk(tmpTrays[k]);
		// DR
		tbgbbvo1.setDr(0);
		lbbvos.add(tbgbbvo1);

		//
		return lbbvos;
	}

	/**
	 * 
	 * @作者：zhf
	 * @说明：完达山物流项目 自动分配托盘
	 * @时间：2011-3-31下午04:07:51
	 * @param bodys
	 * @throws BusinessException
	 */
	public Object autoInTray(TbGeneralBVO[] bodys, String cwhid, String cspaceid)
			throws BusinessException {
		if (bodys == null || bodys.length == 0)
			throw new BusinessException("传入表体数据为空");

		Map<String, Integer> retInfor = new HashMap<String, Integer>();
		boolean iszc = WdsWlPubTool.isZc(cwhid);
		// WdsWlPubBO wlbo = new WdsWlPubBO();
		if (!iszc) {// 分仓 托盘状态=0 托盘是否 一次装满 或 一次清空
			String sql = "select count(0) from bd_cargdoc_tray where cdt_traystatus=0 and isnull(dr,0)=0 and pk_cargdoc = '"
					+ cspaceid + "'";
			int index = PuPubVO.getInteger_NullAs(getDao().executeQuery(sql,
					WdsPubResulSetProcesser.COLUMNPROCESSOR), -1);
			if (index <= 0)
				throw new ValidationException("分仓没有设置虚拟托盘");
//			return null;
		}

		String[] tmpTrays = null;//能放指定存货的本货位内的所有的托盘
		int iVolumn = 0;//一个托盘可用放多少货品
		int iAllVolumn = 0;//托盘剩余可容纳货品总量
		
		List<TbGeneralBBVO> lallbbvos = new ArrayList<TbGeneralBBVO>();
		// 总仓判断
		for (TbGeneralBVO body : bodys) {
			// 获取满足条件的托盘信息
			tmpTrays = WdsWlPubBO.getTrayInfor(cspaceid, body
					.getGeb_cinvbasid());
			if (tmpTrays == null || tmpTrays.length == 0) {
				retInfor.put(body.getGeb_cinvbasid(), -1);// 没有闲置的托盘了
				continue;
			}
			// 计算托盘容积
			iVolumn = WdsWlPubBO.getTrayVolumeByInvbasid(body
					.getGeb_cinvbasid());
			iAllVolumn = iVolumn * tmpTrays.length;// 总容积
			// 判断 总容积不能小于 当前物品的应收辅数量
			UFDouble nRes = body.getGeb_bsnum().sub(
					PuPubVO.getUFDouble_NullAsZero(iAllVolumn));
			if (nRes.doubleValue() > 0) {
				retInfor.put(body.getGeb_cinvbasid(), 1);// 要放的货物超出托盘承受量
				continue;
			} else if (nRes.equals(WdsWlPubTool.DOUBLE_ZERO)) {// 托盘正好满足本次需求

				lallbbvos.addAll(shareNumToTrays(tmpTrays, body, iVolumn, iszc));
			} else {// 托盘绰绰有余 如何使用托盘 托盘使用是否有优先级 按托盘编码？
				lallbbvos.addAll(shareNumToTrays(tmpTrays, body, iVolumn, iszc));
			}
		}
		
		if(lallbbvos == null || lallbbvos.size()==0)
			throw new BusinessException("数据处理异常");
		
		//保存托盘信息(先删后存)
		doSaveTrayInfor(lallbbvos);	
		return retInfor;	
	}
}
