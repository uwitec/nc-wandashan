package nc.bs.wds.ic.stock;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.wl.pub.WdsPubResulSetProcesser;
import nc.itf.scm.cenpur.service.TempTableUtil;
import nc.jdbc.framework.util.SQLHelper;
import nc.vo.ic.other.out.TbOutgeneralTVO;
import nc.vo.ic.pub.StockInvOnHandVO;
import nc.vo.ic.pub.TbGeneralBBVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pub.ValidationException;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.query.ConditionVO;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.trade.pub.IBillStatus;
import nc.vo.wl.pub.WdsWlPubConst;
import nc.vo.wl.pub.WdsWlPubTool;
/**
 * 
 * @作者：zhf
 * @说明  现存量的业务操作类
 * @时间：2011-6-28下午04:20:27

 * @throws BusinessException
 */

public class StockInvOnHandBO {
	
	private BaseDAO dao = null;
	private BaseDAO getDao(){
		if(dao == null)
			dao = new BaseDAO();
		return dao;
	}
	
	public StockInvOnHandBO(BaseDAO dao2){
		super();
		dao = dao2;
	}
	public StockInvOnHandBO(){
		super();
//		dao = dao2;
	}
	
	/**
	 * 
	 * @作者：zhf
	 * @说明：完达山物流项目 托盘状态校验  可用于 对托盘占用的并发校验
	 * @时间：2011-7-5下午08:57:09
	 * @param state
	 * @param ltray
	 * @param tt
	 * @throws BusinessException
	 */
	public void checkTray(int state,List<String> ltray,TempTableUtil tt) throws BusinessException{
		if(ltray == null || ltray.size() ==0)
			return;
		String sql = "select count(0) from bd_cargdoc_tray where isnull(dr,0) = 0 and cdt_pk in "+ tt.getSubSql((ArrayList)ltray)
		+" and cdt_traystatus = "+state;
		int num = PuPubVO.getInteger_NullAs(getDao().executeQuery(sql, WdsPubResulSetProcesser.COLUMNPROCESSOR),WdsWlPubTool.INTEGER_ZERO_VALUE);
		if(num!=ltray.size()){
			String error = null;
			if(state == StockInvOnHandVO.stock_state_lock){
				error = "绑定的托盘已被释放";
			}else if(state == StockInvOnHandVO.stock_state_null){
				error = "指定托盘已被占用";
			}
			throw new BusinessException(error);
		}
			
	}
	
	/**
	 * 
	 * @作者：zhf
	 * @说明：完达山物流项目 获取库存状态数据
	 * @时间：2011-6-28下午04:20:27
	 * @param corp 公司
	 * @param cwarehouseid 仓库
	 * @param pk_cargdoc  货位
	 * @param ctrayid 托盘
	 * @param cinvbasid 存货id
	 * @param vbatchcode 批次
	 * @return
	 * @throws BusinessException
	 */
	public StockInvOnHandVO[] getStockInvDatas(String corp,String cwarehouseid,
			String pk_cargdoc,String ctrayid,String cinvbasid,String vbatchcode) throws BusinessException{
		StringBuffer whereSql = new StringBuffer();
		if(PuPubVO.getString_TrimZeroLenAsNull(pk_cargdoc)==null){
			throw new BusinessException("货位不能为空");
		}
		whereSql.append(" isnull(dr,0)=0 and pk_cargdoc = '"+pk_cargdoc+"' and pk_corp = '"+corp+"'");
		if(PuPubVO.getString_TrimZeroLenAsNull(cwarehouseid)!=null)
			whereSql.append(" and pk_customize1 = '"+cwarehouseid+"'");
		if(PuPubVO.getString_TrimZeroLenAsNull(ctrayid)!=null){
			whereSql.append(" and pplpt_pk = '"+ctrayid+"'");
		}
		if(PuPubVO.getString_TrimZeroLenAsNull(cinvbasid)!=null){
			whereSql.append(" and pk_invbasdoc = '"+cinvbasid+"'");
		}
		if(PuPubVO.getString_TrimZeroLenAsNull(vbatchcode)!=null){
			whereSql.append(" and whs_batchcode = '"+vbatchcode+"'");
		}

		List<StockInvOnHandVO> linv = (List<StockInvOnHandVO>)getDao().retrieveByClause(StockInvOnHandVO.class, whereSql.toString());
		if(linv == null||linv.size() ==0)
			return null;
		return linv.toArray(new StockInvOnHandVO[0]);

	}
	
	/**
	 * 
	 * @作者：zhf
	 * @说明：完达山物流项目 货物托盘的当前存量
	 * @时间：2011-7-6下午01:16:22
	 * @param trayid 托盘id
	 * @param isass 是否辅数量
	 * @return
	 * @throws BusinessException
	 */
	public UFDouble getTrayNum(String trayid,boolean isass) throws BusinessException{
		if(PuPubVO.getString_TrimZeroLenAsNull(trayid)==null)
			return null;
		StringBuffer sql = new StringBuffer("select ");
		if(isass)
			sql.append("whs_stockpieces");
		else
			sql.append("whs_stocktonnage");
		sql.append(" from tb_warehousestock where isnull(dr,0) = 0 and pplpt_pk = '"+trayid+"'");
		
		return PuPubVO.getUFDouble_NullAsZero(getDao().executeQuery(sql.toString(), WdsPubResulSetProcesser.COLUMNPROCESSOR));
	}
	
	
	/**
	 * 
	 * @作者：zhf
	 * @说明：完达山物流项目 存量校验   无论是对于 总仓实际托盘  虚拟托盘 还是分仓  该校验都包括了
	 * @时间：2011-6-28下午03:10:47
	 * @param corp 公司
	 * @param pk_cargdoc  货位
	 * @param ctrayid 托盘
	 * @param cinvbasid 存货
	 * @param vbatchcode 批次
	 * @param nassnum 数量   辅数量
	 * @throws BusinessException
	 */
	public void checkOnHandNum(String corp,String cwarehouseid,String pk_cargdoc,String ctrayid,String cinvbasid,String vbatchcode,UFDouble nassnum) throws BusinessException{

		StockInvOnHandVO[] stocks = getStockInvDatas(corp, cwarehouseid, pk_cargdoc, ctrayid, cinvbasid, vbatchcode);
		
		if(stocks == null || stocks.length == 0){
			throw new BusinessException("货位存量不足");
		}
		UFDouble naAllAssNum = WdsWlPubTool.DOUBLE_ZERO;
		for(StockInvOnHandVO stock:stocks){
			naAllAssNum = naAllAssNum.add(PuPubVO.getUFDouble_NullAsZero(stock.getWhs_stockpieces()));
		}
		if(nassnum.doubleValue()>naAllAssNum.doubleValue()){
			
		}
			
			//throw new  BusinessException("货位存量不足");
	}
	
	/**
	 * 
	 * @作者：zhf
	 * @说明：完达山物流项目 根据入流水信息表 更新 库存存量状态表  用于  作废入库单时
	 * @时间：2011-4-7下午08:39:17
	 * @param ltray
	 * @throws Exception
	 */
	public  void updateStockOnDelForIn(List<TbGeneralBBVO> ltray) throws Exception {
		// TODO Auto-generated method stub
		if (null == ltray || ltray.size() == 0) {			
			return;
		}
		UFDouble noutnum = WdsWlPubTool.DOUBLE_ZERO; // 实出数量
		UFDouble noutassistnum = WdsWlPubTool.DOUBLE_ZERO; // 实出辅数量
//		List linvhand = null;
		StockInvOnHandVO[] stocks = null;
		String pk_corp = SQLHelper.getCorpPk();
		for (TbGeneralBBVO tray:ltray) {				
			noutassistnum = tray.getNinassistnum();
			noutnum = tray.getGebb_num();				

			stocks = getStockInvDatas(pk_corp, null, tray.getPk_cargdoc(), tray.getCdt_pk(), tray.getPk_invbasdoc(), tray.getGebb_vbatchcode());
			// 判断结果集是否为空
			//				if (null != linvhand && generaltList.size() > 0) {
			if(stocks == null || stocks.length == 0)
				return;
			if(stocks.length > 1){
				throw new BusinessException("获取库存状态数据异常");
			}

			StockInvOnHandVO item = stocks[0];
			UFDouble nhandnum = PuPubVO.getUFDouble_NullAsZero(item.getWhs_stocktonnage());
			UFDouble nhandassnum = PuPubVO.getUFDouble_NullAsZero(item.getWhs_stockpieces());
			if (noutassistnum.equals(nhandassnum)							
					&& noutnum.equals(nhandnum)){
				item.setWhs_status(1);//------------这个更新为  1  对  虚拟托盘  和  分仓来说  不严密
				updateBdcargdocTray(item.getPplpt_pk(),StockInvOnHandVO.stock_state_null);//将托盘状态更新为  空盘  
			}
			item.setWhs_stockpieces(nhandassnum.sub(noutassistnum));
			item.setWhs_stocktonnage(nhandnum.sub(noutnum));
			item.setStatus(VOStatus.UPDATED);
			this.updateWarehousestock(item);
		}

	}
	
	/**
	 * 
	 * @作者：zhf
	 * @说明：完达山物流项目 根据出库流水信息表 更新 库存存量状态表
	 * @时间：2011-4-7下午08:39:17
	 * @param ltray
	 * @throws Exception
	 */
	public  void updateStockForOut(String corp,String warehousid,List<TbOutgeneralTVO> ltray) throws Exception {
		// TODO Auto-generated method stub
		if (null == ltray || ltray.size() == 0) {			
			return;
		}
		UFDouble noutnum = WdsWlPubTool.DOUBLE_ZERO; // 实出数量
		UFDouble noutassistnum = WdsWlPubTool.DOUBLE_ZERO; // 实出辅数量
        StockInvOnHandVO[] stocks = null;
		for (TbOutgeneralTVO tray:ltray) {				
			noutassistnum = PuPubVO.getUFDouble_NullAsZero(tray.getNoutassistnum());
			noutnum = PuPubVO.getUFDouble_NullAsZero(tray.getNoutnum());				
			stocks = getStockInvDatas(corp, warehousid, tray.getPk_cargdoc(), tray.getCdt_pk(), tray.getPk_invbasdoc(), tray.getVbatchcode());
			if(stocks == null || stocks.length == 0)
				throw new BusinessException("存量不足");
			if(stocks.length>1)
				throw new BusinessException("数据异常");

			StockInvOnHandVO stock = stocks[0];
			UFDouble nhandnum = PuPubVO.getUFDouble_NullAsZero(stock.getWhs_stocktonnage());
			UFDouble nhandassnum = PuPubVO.getUFDouble_NullAsZero(stock.getWhs_stockpieces());
			if (noutassistnum.equals(nhandassnum)							
					&& noutnum.equals(nhandnum)){
				stock.setWhs_status(1);
				if(warehousid.equalsIgnoreCase(WdsWlPubConst.WDS_WL_ZC))//只有总仓  维护托盘状态  for add mlr
				updateBdcargdocTray(stock.getPplpt_pk(),StockInvOnHandVO.stock_state_null);//将托盘状态更新为  空盘  
			}
			stock.setWhs_stockpieces(nhandassnum.sub(noutassistnum));
			stock.setWhs_stocktonnage(nhandnum.sub(noutnum));
			stock.setStatus(VOStatus.UPDATED);
			this.updateWarehousestock(stock);
		}
	}
	
	/**
	 * 
	 * @作者：mlr
	 * @说明：现存量表要求 仓库， 货位， 托盘，存货，批次， 这个维度保持唯一 
	          所以 每次要入库的货， 都要按这个维度查询 已存在的这个维度的现存量
	          将本次入库的货与现存量合并形成一条记录
	 * @时间：2011-4-8下午06:52:43
	 * @param newBillVo 要操作的单据数据
	 * @param iBdAction 操作状态
	 * @param isNew  是否新增保存
	 * @throws Exception
	 */
	public void inserStockForIn(List<StockInvOnHandVO> linvInfor) throws Exception {		
		StockInvOnHandVO[] tmps = null;
		for(StockInvOnHandVO stock:linvInfor){
			tmps = getStockInvDatas(stock.getPk_corp(), stock.getPk_customize1()
					, stock.getPk_cargdoc(), stock.getPplpt_pk(), stock.getPk_invbasdoc(), stock.getWhs_batchcode());
			if(tmps == null || tmps.length ==0){
				getDao().insertVO(stock);
			}else if(tmps.length == 1){
				stock.setWhs_oanum(PuPubVO.getUFDouble_NullAsZero(stock.getWhs_oanum()).add(PuPubVO.getUFDouble_NullAsZero(tmps[0].getWhs_oanum())));
				stock.setWhs_omnum(PuPubVO.getUFDouble_NullAsZero(stock.getWhs_omnum()).add(PuPubVO.getUFDouble_NullAsZero(tmps[0].getWhs_omnum())));
				stock.setWhs_stockpieces(PuPubVO.getUFDouble_NullAsZero(stock.getWhs_stockpieces()).add(PuPubVO.getUFDouble_NullAsZero(tmps[0].getWhs_stockpieces())));
				stock.setWhs_stocktonnage(PuPubVO.getUFDouble_NullAsZero(stock.getWhs_stocktonnage()).add(PuPubVO.getUFDouble_NullAsZero(tmps[0].getWhs_stocktonnage())));
				updateWarehousestock(stock);
			}else
				throw new BusinessException("获取存货状态异常");
		}		
	}
	
	/**
	 * 更新库存表
	 */
	public void updateWarehousestock(StockInvOnHandVO item) throws Exception {
		// TODO Auto-generated method stub
		if (null != item) {
			if(PuPubVO.getUFDouble_NullAsZero(item.getWhs_stockpieces()).doubleValue()<0|| PuPubVO.getUFDouble_NullAsZero(item.getWhs_stocktonnage()).doubleValue()<0){
			//	throw new BusinessException("出现负结存");
			}
//			this.getIvo().updateVO(item);
			getDao().updateVO(item);
		}
	}
	
	// 更新托盘状态
	public void updateBdcargdocTray(String trayPK,int state) throws BusinessException {
		if(PuPubVO.getString_TrimZeroLenAsNull(trayPK)==null)
			return;
		String sql = "update bd_cargdoc_tray set cdt_traystatus = " + state
		+ " where cdt_pk='" + trayPK + "'";
		if(state == StockInvOnHandVO.stock_state_null){//对于虚拟托盘永远不更新为 空
			sql = sql + " and cdt_traycode not like '"+WdsWlPubConst.XN_CARGDOC_TRAY_NAME+"%'";
		}
		getDao().executeUpdate(sql);
	}
	

	/**
	 * 
	 * @作者：zhf
	 * @说明：完达山物流项目 更新托盘状态
	 * @时间：2011-4-9下午08:39:44
	 * @param state
	 * @param ltrayid
	 * @throws BusinessException
	 */
	public void updateTrayState(int state,List<String> ltrayid,TempTableUtil tempUtil) throws BusinessException{
		String sql = "update bd_cargdoc_tray set cdt_traystatus = "+state+" where cdt_pk in "+tempUtil.getSubSql(ltrayid.toArray(new String[0]));
		if(state == StockInvOnHandVO.stock_state_null){//对于虚拟托盘永远不更新为 空
			sql = sql + " and cdt_traycode not like '"+WdsWlPubConst.XN_CARGDOC_TRAY_NAME+"%'";
		}
		getDao().executeUpdate(sql);
	}
	
	/**
	 * 
	 * @作者：zhf
	 * @说明：完达山物流项目 获取现存量
	 * @时间：2011-7-7下午07:18:34
	 * @param corp
	 * @param cwarehouseid
	 * @param pk_cargdoc
	 * @param cinvbasid
	 * @param vbatchcode
	 * @param ctrayid
	 * @param strWhere 自定义查询条件
	 * @return
	 * @throws BusinessException
	 */
	public UFDouble[] getInvStockNum(String corp,String cwarehouseid,
			String pk_cargdoc,String cinvbasid,String vbatchcode,String ctrayid ,String strWhere) throws BusinessException{
		StringBuffer sql = new StringBuffer();
		if(PuPubVO.getString_TrimZeroLenAsNull(cwarehouseid)==null){
			throw new BusinessException("仓库不能为空");
		}
		if(PuPubVO.getString_TrimZeroLenAsNull("cinvbasid")==null)
			throw new BusinessException("存货为空");
		
		sql.append("select sum(whs_stocktonnage) nnum,sum(whs_stockpieces) nassnum ");
		sql.append(" from tb_warehousestock");
		sql.append(" where isnull(dr,0) = 0");
		if(strWhere!= null && !"".equalsIgnoreCase(strWhere)){
			sql.append(" and "+strWhere);
		}
		if(PuPubVO.getString_TrimZeroLenAsNull(corp)!=null){
			sql.append(" and pk_corp = '"+corp+"'");
		}
		if(PuPubVO.getString_TrimZeroLenAsNull(cwarehouseid)!=null){
			sql.append(" and pk_customize1 = '"+cwarehouseid+"'");
		}
		if(PuPubVO.getString_TrimZeroLenAsNull(pk_cargdoc)!=null){
			sql.append(" and pk_cargdoc = '"+pk_cargdoc+"'");
		}
		if(PuPubVO.getString_TrimZeroLenAsNull(vbatchcode)!=null){
			sql.append(" and whs_batchcode = '"+vbatchcode+"'");
		}
		if(PuPubVO.getString_TrimZeroLenAsNull(ctrayid)!=null)
			sql.append(" and pplpt_pk = '"+ctrayid+"'");
		
		sql.append(" and pk_invbasdoc = '"+cinvbasid+"'");
		sql.append(" group by ");
		
		if(PuPubVO.getString_TrimZeroLenAsNull(corp)!=null){
			sql.append(" pk_corp");
		}
		if(PuPubVO.getString_TrimZeroLenAsNull(cwarehouseid)!=null){
			sql.append(",pk_customize1");
		}
		if(PuPubVO.getString_TrimZeroLenAsNull(pk_cargdoc)!=null){
			sql.append(",pk_cargdoc");
		}
		sql.append(",pk_invbasdoc");
		if(PuPubVO.getString_TrimZeroLenAsNull(vbatchcode)!=null){
			sql.append(",whs_batchcode");
		}
		if(PuPubVO.getString_TrimZeroLenAsNull(ctrayid)!=null)
			sql.append(",pplpt_pk");
		/**
		 * zhf  托盘和 批次的先后位置  可能存在问题 
		 * 因为  实际托盘为 一个批次放多个托盘  但对于虚拟托盘 却是 一个托盘对多个批次
		 */
		
		Object o =  getDao().executeQuery(sql.toString(), WdsPubResulSetProcesser.ARRAYROCESSOR);
        if(o == null)
        	return null;
        Object[] os = (Object[])o;
        UFDouble[] us = new UFDouble[2];
        us[0] = PuPubVO.getUFDouble_NullAsZero(os[0]);
        us[1] = PuPubVO.getUFDouble_NullAsZero(os[1]);
        return us;
	}
	
	private void dealResultSet(Map<String,UFDouble[]> invNumInfor,List ldata){
		if(ldata == null || ldata.size() ==0)
			return;
		//		if(ldata != null && ldata.size()!=0){
		if(invNumInfor == null){
			invNumInfor = new HashMap<String, UFDouble[]>();
		}

		String key = null;
		Map oMap = null;
		int len = ldata.size();
		UFDouble[] nums = null;
		for(int i=0;i<len;i++){
			oMap = (Map)ldata.get(i);
			key =// WdsWlPubTool.getString_NullAsTrimZeroLen(oMap.get("store"))
//			+
			WdsWlPubTool.getString_NullAsTrimZeroLen(oMap.get("inv"));

			if(invNumInfor.containsKey(key)){
				nums = invNumInfor.get(key);
			}else{
				nums = new UFDouble[]{WdsWlPubTool.DOUBLE_ZERO,WdsWlPubTool.DOUBLE_ZERO};
			}
			nums[0] = nums[0].add(PuPubVO.getUFDouble_NullAsZero(oMap.get("nnum")));
			nums[1] = nums[1].add(PuPubVO.getUFDouble_NullAsZero(oMap.get("nassnum")));
			
			invNumInfor.put(key, nums);
		}
		//		}
	}
	
	/**
	 * 
	 * @作者：zhf
	 * @说明：完达山物流项目 获取仓库内已安排未出库的存货量
	 * @时间：2011-7-7下午07:51:37
	 * @param corp
	 * @param cstoreid
	 * @param cinvids
	 * @return
	 * @throws BusinessException
	 */
	public Map<String, UFDouble[]> getNdealNumInfor(String corp,String cstoreid,String[] cinvids,
			TempTableUtil tt) throws BusinessException{
		Map<String,UFDouble[]> retInfor = new HashMap<String, UFDouble[]>();
		String sql = null;
		//		1、销售运单占用量
		sql = " select b.pk_invbasdoc inv,coalesce(b.narrangnmu,0.0)-coalesce(b.noutnum,0) nnum ,coalesce(b.nassarrangnum,0.0)-coalesce(b.nassoutnum,0.0) nassnum" +
		" from wds_soorder_b b inner join wds_soorder h on h.pk_soorder = b.pk_soorder " +
		" where isnull(h.dr,0)=0 and isnull(b.dr,0)=0 and h.pk_corp = '"+corp+"'" +
		" and h.pk_outwhouse = '"+cstoreid+"' and h.vbillstatus=8 and b.pk_invbasdoc in "+tt.getSubSql(cinvids);
		List ldata = (List)getDao().executeQuery(sql, WdsPubResulSetProcesser.MAPLISTROCESSOR);
		dealResultSet(retInfor, ldata);
		//		2、发运运单占用量
		sql = " select b.pk_invbasdoc inv,coalesce(b.ndealnum,0.0)-coalesce(b.noutnum,0) nnum ,coalesce(b.nassdealnum,0.0)-coalesce(b.nassoutnum,0.0) nassnum" +
		" from wds_sendorder_b b inner join wds_sendorder h on h.pk_sendorder = b.pk_sendorder " +
		" where isnull(h.dr,0)=0 and isnull(b.dr,0)=0 and h.pk_corp = '"+corp+"'" +
		" and h.pk_outwhouse = '"+cstoreid+"' and h.vbillstatus=8 and b.pk_invbasdoc in "+tt.getSubSql(cinvids);
		ldata = (List)getDao().executeQuery(sql, WdsPubResulSetProcesser.MAPLISTROCESSOR);
		dealResultSet(retInfor, ldata);
		return retInfor;
	}
	
	/**
	 * 
	 * @作者：lyf
	 * @说明：完达山物流项目 
	 * @时间：2011-11-29上午09:58:57
	 * @param corp:公司
	 * @param strWhere：自定义where条件
	 * @return Map<String, UFDouble[]>:<存货基本id，{主数量,辅数量}>
	 * @throws BusinessException
	 */
	public Map<String, UFDouble[]> getSoOrderNdealNumInfor(String corp,String strWhere) throws BusinessException{
		Map<String,UFDouble[]> retInfor = new HashMap<String, UFDouble[]>();
		StringBuffer sql= new StringBuffer();
		sql.append(" select ");
		sql.append(" b.pk_invbasdoc inv, ");//存货基本档案主键
		sql.append(" coalesce(b.narrangnmu,0.0)-coalesce(b.noutnum,0) nnum ,");//存货未出库数量
		sql.append(" coalesce(b.nassarrangnum,0.0)-coalesce(b.nassoutnum,0.0) nassnum ");//存货未出库辅数量
		sql.append(" from wds_soorder_b b ");
		sql.append("  inner join wds_soorder h ");
		sql.append("  on h.pk_soorder = b.pk_soorder ");
		sql.append("  where isnull(h.dr,0)=0 and isnull(b.dr,0)=0 ");
		sql.append(" and h.pk_corp = '"+corp+"'");
		sql.append(" and h.vbillstatus="+IBillStatus.FREE);
		if(strWhere !=null && !"".equalsIgnoreCase(strWhere)){
			sql.append(" and "+ strWhere);
		}
		List ldata = (List)getDao().executeQuery(sql.toString(), WdsPubResulSetProcesser.MAPLISTROCESSOR);
		dealResultSet(retInfor, ldata);
		return retInfor;
	}
	/**
	 * 
	 * @作者：lyf
	 * @说明：完达山物流项目 :获得发运订单未发运量
	 * @时间：2011-11-29上午09:58:57
	 * @param corp:公司
	 * @param strWhere：自定义where条件
	 * @return
	 * @throws BusinessException
	 */
	public Map<String, UFDouble[]> getPlanOrderNdealNumInfor(String corp,String strWhere) throws BusinessException{
		Map<String,UFDouble[]> retInfor = new HashMap<String, UFDouble[]>();
		StringBuffer sql= new StringBuffer();
		sql.append(" select ");
		sql.append(" b.pk_invbasdoc inv, ");//存货基本档案主键
		sql.append("  coalesce(b.ndealnum,0.0)-coalesce(b.noutnum,0) nnum ,");//存货未出库数量
		sql.append(" coalesce(b.nassdealnum,0.0)-coalesce(b.nassoutnum,0.0) nassnum ");//存货未出库辅数量
		sql.append(" from wds_sendorder_b b ");
		sql.append("  inner join wds_sendorder h ");
		sql.append(" on h.pk_sendorder = b.pk_sendorder ");
		sql.append("  where isnull(h.dr,0)=0 and isnull(b.dr,0)=0 ");
		sql.append(" and h.pk_corp = '"+corp+"'");
		sql.append(" and h.vbillstatus="+IBillStatus.FREE);
		if(strWhere !=null && !"".equalsIgnoreCase(strWhere)){
			sql.append(" and "+ strWhere);
		}
		List ldata = (List)getDao().executeQuery(sql.toString(), WdsPubResulSetProcesser.MAPLISTROCESSOR);
		dealResultSet(retInfor, ldata);
		return retInfor;
	}
	/**
	 * 
	 * @作者：zhf
	 * @说明：完达山物流项目 
	 * @时间：2011-7-11下午08:12:07
	 * @param corp
	 * @param cstoreid
	 * @param cinvids
	 * @param tt
	 * @throws BusinessException
	 */
	public void checkNumForOut(String corp,String cstoreid,Map<String,UFDouble[]> invNumInfor,
			TempTableUtil tt) throws BusinessException{

		//		可用量=现场量-占用量
		//		1、获取占用量
		if(invNumInfor == null || invNumInfor.size() == 0)
			return;
		if(PuPubVO.getString_TrimZeroLenAsNull(corp)==null)
			corp = SQLHelper.getCorpPk();
		if(PuPubVO.getString_TrimZeroLenAsNull(cstoreid)==null){
			throw new ValidationException("仓库为空");
		}
		String[] cinvids = invNumInfor.keySet().toArray(new String[0]);
		Map<String, UFDouble[]> dealNumInfor = getNdealNumInfor(corp, cstoreid, cinvids, tt);
		if(dealNumInfor == null)
			dealNumInfor = new HashMap<String, UFDouble[]>();

			UFDouble[] nnum = null;//待发货量
			UFDouble[] nstocknum = null;//库存量
			UFDouble[] ndealnum = null;//已占用量
			for(String invid:cinvids){
				nnum = invNumInfor.get(invid);
				if(nnum == null || nnum.length == 0)
					throw new ValidationException("发货量为空");
				nstocknum = getInvStockNum(corp, cstoreid, null, invid, null, null,null);
				ndealnum = dealNumInfor.get(invid);
				for(int i=0;i<2;i++){
					if(PuPubVO.getUFDouble_NullAsZero(nnum[i]).compareTo(
							PuPubVO.getUFDouble_NullAsZero(nstocknum[i])
							.sub(PuPubVO.getUFDouble_NullAsZero(ndealnum[i])))>0)
						throw new ValidationException("存货["+WdsWlPubTool.getInvCodeByInvid(invid)+"]库存可用量不足");
				}
			}
	}	
	
	/**
	 * 
	 * @作者：yf
	 * @说明：完达山物流项目 
	 * 			库存状态表 清理 按钮 处理方法
	 * @时间：2011-7-17下午05:49:57
	 * @return
	 * @throws DAOException 
	 */
	public Integer cleanStockZero(ConditionVO[] vos) throws BusinessException{		
		//whs_stocktonnage;库存主数量
		//whs_stockpieces; 辅数量
		StringBuffer sql = new StringBuffer();
		sql.append( " update tb_warehousestock set dr = 1 where isnull(dr,0)=0 and coalesce(whs_stocktonnage,0) = 0" +
				" and coalesce(whs_stockpieces,0) = 0 ");	
		int size = vos.length;
		for(int i=0; i <size; i ++){
			ConditionVO vo = vos[i];
			if("wds_invcl".equals(vo.getFieldCode())){
				
				sql.append(" and pk_invbasdoc in " +
						"(select pk_invbasdoc from wds_invbasdoc where vdef2 like '"+vo.getValue()+"%')");
			}else{
				sql.append(vo.getSQLStr());
			}
		}
		//liuys add 根据出库单的表体来判断存货状态是否清理(如果出库单未审核且已经拣货,那么将不清理该存货的存货状态)
		sql.append(" and tb_warehousestock.whs_pk not in (select distinct t.whs_pk from tb_outgeneral_h h,tb_outgeneral_t t where  h.general_pk = t.general_pk and nvl(t.dr,0) = 0 and nvl(h.dr, 0) = 0 and h.vbillstatus = 8) ");
		return getDao().executeUpdate(sql.toString());
	}

}
