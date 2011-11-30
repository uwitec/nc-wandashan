package nc.bs.wds.tray.lock;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.bs.bank_cvp.compile.registry.BussinessMethods;
import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.pub.SuperDMO;
import nc.bs.wds.ic.stock.StockInvOnHandBO;
import nc.bs.wl.pub.WdsPubResulSetProcesser;
import nc.itf.scm.cenpur.service.TempTableUtil;
import nc.jdbc.framework.SQLParameter;
import nc.jdbc.framework.util.SQLHelper;
import nc.vo.ic.other.out.MyBillVO;
import nc.vo.ic.other.out.TbOutgeneralBVO;
import nc.vo.ic.other.out.TbOutgeneralTVO;
import nc.vo.ic.pub.StockInvOnHandVO;
import nc.vo.ic.pub.TbGeneralBBVO;
import nc.vo.ic.pub.TbGeneralBVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.wds.ic.cargtray.SmallTrayVO;
import nc.vo.wds.xn.XnRelationVO;
import nc.vo.wl.pub.WdsWlPubConst;
import nc.vo.wl.pub.WdsWlPubTool;

public class LockTrayBO {
	
	public LockTrayBO(BaseDAO dao){
		super();
		this.dao = dao;
	}
	
	public LockTrayBO(BaseDAO dao,StockInvOnHandBO stockbo2){
		super();
		this.dao = dao;
		this.stockbo = stockbo2;
	}
	
	public LockTrayBO(BaseDAO dao,StockInvOnHandBO stockbo2,SuperDMO dmo,TempTableUtil tt){
		super();
		this.dao = dao;
		this.stockbo = stockbo2;
		this.dmo = dmo;
		ttutil = tt;
	}
	
	public LockTrayBO(){
		super();
//		this.dao = dao;
	}
	
	private BaseDAO dao = null;
	private BaseDAO getDao(){
		if(dao == null)
			dao = new BaseDAO();
		return dao;
	}

	private SuperDMO dmo = new SuperDMO();
	private SuperDMO getSuperDMO(){
		if(dmo == null){
			dmo = new SuperDMO();
		}
		return dmo;
	}
	
	private TempTableUtil ttutil = null;
	private TempTableUtil getTtutil(){
		if(ttutil == null){
			ttutil = new TempTableUtil();
		}
		return ttutil;
	}
	
	public void lockTray(String chid,String cwareid,String gebbid,java.util.Map lockTrayInfor) throws BusinessException{
		if(PuPubVO.getString_TrimZeroLenAsNull(chid)==null)
			throw new BusinessException("数据异常");
		if(lockTrayInfor == null || lockTrayInfor.size() == 0)
			return;
		if(PuPubVO.getString_TrimZeroLenAsNull(gebbid)==null)
			throw new BusinessException("数据异常");
		TbGeneralBBVO bb = (TbGeneralBBVO)getSuperDMO().queryByPrimaryKey(TbGeneralBBVO.class, gebbid);
		if(bb == null)
			throw new BusinessException("数据异常");
		String bid = bb.getGeb_pk();
		TbGeneralBVO b = (TbGeneralBVO)getSuperDMO().queryByPrimaryKey(TbGeneralBVO.class, bid);
		if(b == null)
			throw new BusinessException("数据异常");
		List<TbGeneralBBVO> ltray = new ArrayList<TbGeneralBBVO>();
		ltray.add(bb);
		b.setTrayInfor(ltray);
		
		checkInBillOnSave(lockTrayInfor, new TbGeneralBVO[]{b}, cwareid, null);
		//		WdsIcInPubBillSave save = new WdsIcInPubBillSave();
		doSaveLockTrayInfor(chid, cwareid, lockTrayInfor);
	}

	private Map<String,UFDouble> inv_tray_volume = null;

	private UFDouble getInvTrayVolume(String key)throws BusinessException{
		if(inv_tray_volume == null){
			inv_tray_volume = new HashMap<String, UFDouble>();
		}
		if(!inv_tray_volume.containsKey(key)){
			inv_tray_volume.put(key, queryInvTrayVolume(key));
		}
		return inv_tray_volume.get(key);
	}	
	
	/**
	 * 
	 * @作者：zhf
	 * @说明：完达山物流项目 解除锁定
	 * @时间：2011-7-5下午08:05:04
	 * @param cgebbids
	 * @throws BusinessException
	 */
	public void reLockTray(String[] cgebbids) throws BusinessException{
		if(cgebbids == null || cgebbids.length == 0)
			return;
//      查询绑定的实际托盘
		String sql = "select ctrayid from wds_xnrelation where cbbid in "+getTtutil().getSubSql(cgebbids);
		List ldata = (List)getDao().executeQuery(sql, WdsPubResulSetProcesser.COLUMNLISTROCESSOR);
		if(ldata == null || ldata.size() == 0)
			return;
//		校验托盘状态必须是锁定态
		getStockBO().checkTray(StockInvOnHandVO.stock_state_lock, ldata, getTtutil());
//		修改托盘状态为空盘
		getStockBO().updateTrayState(StockInvOnHandVO.stock_state_null, (List<String>)ldata, getTtutil());
//		删除锁定关系表
		sql = "update wds_xnrelation set dr = 1 where cbbid in "+getTtutil().getSubSql(cgebbids);
		getDao().executeUpdate(sql);
	}
	
	/**
	 * 
	 * @作者：zhf
	 * @说明：完达山物流项目 解除锁定
	 * @时间：2011-7-5下午08:05:04
	 * @param cgebbids
	 * @throws BusinessException
	 */
	public void reLockTray2(XnRelationVO[] revos) throws BusinessException{
		if(revos == null || revos.length == 0)
			return;
//      查询绑定的实际托盘
        List<String> ldata = new ArrayList<String>();
        for(XnRelationVO revo:revos){
        	ldata.add(revo.getCtrayid());
        }
		if(ldata == null || ldata.size() == 0)
			return;
//		校验托盘状态必须是锁定态
		getStockBO().checkTray(StockInvOnHandVO.stock_state_lock, ldata, getTtutil());
//		修改托盘状态为空盘
		getStockBO().updateTrayState(StockInvOnHandVO.stock_state_null, ldata, getTtutil());
//		删除锁定关系表
		String sql = "update wds_xnrelation set dr = 1 where cxntrayid = '"+revos[0].getCxntrayid()+"'";
		getDao().executeUpdate(sql);
	}
	
	/**
	 * 
	 * @作者：zhf
	 * @说明：完达山物流项目 获取绑定的实际托盘上的存量
	 * @时间：2011-7-6下午02:48:54
	 * @param trayid
	 * @param isass
	 * @return
	 * @throws BusinessException
	 */
	public UFDouble getTrayNum(String trayid,boolean isass) throws BusinessException{
		if(PuPubVO.getString_TrimZeroLenAsNull(trayid)==null)
			return null;
		StringBuffer sql = new StringBuffer("select ");
		if(isass)
			sql.append("nassisnum");
		else
			sql.append("nnum");
		sql.append(" from wds_xnrelation where isnull(dr,0) = 0 and ctrayid = '"+trayid+"'");
		
		return PuPubVO.getUFDouble_NullAsZero(getDao().executeQuery(sql.toString(), WdsPubResulSetProcesser.COLUMNPROCESSOR));
	}

	/**
	 * 
	 * @作者：zhf
	 * @说明：完达山物流项目 绑定了实际托盘的虚拟托盘出库时 对 实际托盘的 指定解锁操作
	 * 此处可能存在漏洞  如果出现 一批次货品  多次录入到同一虚拟托盘上时   部分绑定的情况下  
	 * 考虑这种情况
	 * 比如 该批次存货 出货：100   库存量100 满足  60个绑定了实际托盘  40个没有绑定实际托盘
	 * 
	 * 如何出库？  如果出现这种情况 如果用户指定了实际托盘  系统将实际托盘的锁定状态清除
	 * 实际托盘未用完的继续锁定  等待下次的出库 
	 * 
	 * 在解锁过程中 不进行  存量不足的校验
	 * 
	 * 如何处理？
	 * @时间：2011-7-6下午02:47:58
	 * @param chid
	 * @param cwarehouseid
	 * @param lockTrayInfor2
	 * @throws BusinessException
	 */
	public void doDelLockTrayInfor(String chid,String cwarehouseid,Map lockTrayInfor2) throws BusinessException{
		//	       组装数据
		Map<String,SmallTrayVO[]> lockTrayInfor = (Map<String,SmallTrayVO[]>)lockTrayInfor2;
		if(chid == null || lockTrayInfor == null || lockTrayInfor.size() == 0)
			return;
		//		查询出   所有虚拟托盘的 入库流水表  信息
		List<String> ltrayid = new ArrayList<String>();
		String[] ss = null;
		for(String key:lockTrayInfor.keySet()){
			ss = key.split(",");
			if(PuPubVO.getString_TrimZeroLenAsNull(ss[0])==null)
				throw new BusinessException("绑定关系数据异常");
			ltrayid.add(ss[0]);
		}

		String sql = " isnull(dr,0) = 0 and general_pk = '"+chid+"' and cdt_pk in "+getTtutil().getSubSql((ArrayList)ltrayid);
		TbOutgeneralTVO[] trays = (TbOutgeneralTVO[])getSuperDMO().queryByWhereClause(TbOutgeneralTVO.class, sql);

		if(trays == null || trays.length == 0)
			return;
		
//		根据流水表 实际出库数量  从  绑定的实际托盘上拣货  出库   实际托盘不足报错    按顺序依次拣货  未检空 托盘状态仍为锁定
		
		List<String> lacttrayid = new ArrayList<String>();//需要解锁的实际托盘的id
		String key = null;
//		String[] 
		SmallTrayVO[] actTrays = null;
		
//		托盘容量
		UFDouble nvolume = null;
		UFDouble nallnum = null;
		UFDouble nsy = null;
		
		Object[] relaNumInfor = null;
		
		for(TbOutgeneralTVO tray:trays){
			key = WdsWlPubTool.getString_NullAsTrimZeroLen(tray.getCdt_pk())
			+","+WdsWlPubTool.getString_NullAsTrimZeroLen(tray.getVbatchcode());
			actTrays = lockTrayInfor.get(key);
			if(actTrays == null || actTrays.length == 0){
				throw new BusinessException("存在未指定实际托盘");
			}
			
//			实际出  数量
			nallnum = PuPubVO.getUFDouble_NullAsZero(tray.getNoutassistnum());
			nsy = nallnum;
			nvolume = getInvTrayVolume(tray.getPk_invmandoc());
			if(nvolume.equals(WdsWlPubTool.DOUBLE_ZERO)){
				throw new BusinessException("存货档案托盘容量未设置或为0");
			}
			
			if(nallnum.doubleValue()>nvolume.multiply(actTrays.length).doubleValue()){
				throw new BusinessException("托盘存量不足");
			}			
			
//			拣货出库
			for(SmallTrayVO actTray:actTrays){
				
				/**
				 * zhf 说明  虚拟托盘绑定实际托盘  实际托盘的存货信息并不写入  库存状态表内  因为虚拟托盘
				 * 统一在库存状态表内  写入了   但绑定关系表需要维护 量 因为可能存在实际托盘在绑定时没有全部占用的情况
				 */
				nvolume = getTrayNum(actTray.getCdt_pk(), true);
				nsy = nsy.sub(nvolume);
				if(nsy.doubleValue()<0){
//					调整库存状态表的存量------------最后一个托盘未出完量得情况  需要调整量  但状态仍为锁定
					relaNumInfor = new Object[]{actTray.getCdt_pk(), nsy.abs()};
					break;
				}else if(nsy.doubleValue() == 0){
					lacttrayid.add(actTray.getCdt_pk());
					break;
				}
				lacttrayid.add(actTray.getCdt_pk());				
			}
			if(nsy.doubleValue()>0)
				throw new BusinessException("指定托盘存量不足");
		}
		

		if(lacttrayid.size()>0){
//			调整实际托盘状态
			getStockBO().checkTray(StockInvOnHandVO.stock_state_lock, lacttrayid, getTtutil());
			getStockBO().updateTrayState(StockInvOnHandVO.stock_state_null, lacttrayid, getTtutil());
//			删除关系表
			sql = "update wds_xnrelation set dr = 1 where ctrayid in "+getTtutil().getSubSql((ArrayList)lacttrayid);
			getDao().executeUpdate(sql);
		}

//		调整关系表数量
		if(relaNumInfor != null && relaNumInfor.length > 0){
			sql = "update wds_xnrelation set nassisnum = '"+PuPubVO.getUFDouble_NullAsZero(relaNumInfor[1]).toBigDecimal()+"' where ctrayid = '"+relaNumInfor[1].toString()+"'";
			getDao().executeUpdate(sql);
		}
	}
	
	
	/**
	 * 
	 * @作者：zhf
	 * @说明：完达山物流项目 保存入库  虚拟托盘和实际托盘绑定关系表
	 * @时间：2011-7-5下午02:53:39
	 * @param bill 入库单据
	 * @param lockTrayInfor 虚拟托盘锁定关系  key 托盘id+，+存货id
	 * @throws BusinessException
	 */
	public void doSaveLockTrayInfor(String chid,String cwarehouseid,Map lockTrayInfor2) throws BusinessException{
		//	       组装数据
		Map<String,SmallTrayVO[]> lockTrayInfor = (Map<String,SmallTrayVO[]>)lockTrayInfor2;
		if(chid == null || lockTrayInfor == null || lockTrayInfor.size() == 0)
			return;
		//		查询出   所有虚拟托盘的 入库流水表  信息
		List<String> ltrayid = new ArrayList<String>();
		String[] ss = null;
		for(String key:lockTrayInfor.keySet()){
			ss = key.split(",");
			if(PuPubVO.getString_TrimZeroLenAsNull(ss[0])==null)
				throw new BusinessException("绑定关系数据异常");
			ltrayid.add(ss[0]);
		}

		String sql = " isnull(dr,0) = 0 and geh_pk = '"+chid+"' and cdt_pk in "+getTtutil().getSubSql((ArrayList)ltrayid);
		TbGeneralBBVO[] trays = (TbGeneralBBVO[])getSuperDMO().queryByWhereClause(TbGeneralBBVO.class, sql);

		if(trays == null || trays.length == 0)
			return;
		

		//		考虑到存在 绑定 调整的情况    保存时 先  解除之前的关系  然后 建立 全新的绑定关系
		List<String> lbbid = new ArrayList<String>();
		for(TbGeneralBBVO tray:trays){
			lbbid.add(tray.getPrimaryKey());
		}
		reLockTray(lbbid.toArray(new String[0]));
//		-------------------------------------------------------

		String key = null;
		SmallTrayVO[] traylocks = null;
		List<XnRelationVO> lrelation = new ArrayList<XnRelationVO>();
		XnRelationVO relation = null;
		String pk_corp = SQLHelper.getCorpPk();


		//		托盘容量
		UFDouble nvolume = null;
		UFDouble nallnum = null;
		UFDouble nsy = null;

		//		占用的实际托盘
		List<String> lactTray = new ArrayList<String>();

		for(TbGeneralBBVO tray:trays){
			key = WdsWlPubTool.getString_NullAsTrimZeroLen(tray.getCdt_pk())
			+","+WdsWlPubTool.getString_NullAsTrimZeroLen(tray.getGebb_vbatchcode());
			traylocks = lockTrayInfor.get(key);
			if(traylocks == null || traylocks.length ==0)
				return;
			//			实际入  数量
			nallnum = PuPubVO.getUFDouble_NullAsZero(tray.getNinassistnum());
			nsy = nallnum;
			//			存货推盘容量
			nvolume = getInvTrayVolume(tray.getPk_invmandoc());
			if(nvolume.equals(WdsWlPubTool.DOUBLE_ZERO)){
				throw new BusinessException("存货档案托盘容量未设置或为0");
			}

//			int index = 1;
//			boolean flag = false;
			for(SmallTrayVO traylock:traylocks){				
				relation = new XnRelationVO();
				relation.setPk_corp(pk_corp);
				relation.setCcalbodyid(null);
				relation.setCwarehousid(cwarehouseid);
				relation.setCxncargdocid(tray.getPk_cargdoc());
				relation.setCxntrayid(tray.getCdt_pk());
				relation.setCcargdocid(traylock.getPk_cargdoc());
				relation.setCtrayid(traylock.getCdt_pk());
				
				//				relation.setNnum();
				relation.setChid(tray.getGeh_pk());
				relation.setCbid(tray.getGeb_pk());
				relation.setCbbid(tray.getGebb_pk());
				relation.setPk_invbasdoc(tray.getPk_invbasdoc());
				relation.setPk_invmandoc(tray.getPk_invmandoc());
				relation.setVbatchcode(tray.getGebb_vbatchcode());
				relation.setStatus(VOStatus.NEW);
			
				lrelation.add(relation);
				lactTray.add(traylock.getCdt_pk());
				
				relation.setNassisnum(nvolume);
				nsy = nsy.sub(nvolume);
				if(nsy.doubleValue()<0){
					relation.setNassisnum(nsy.add(nvolume));
					break;
				}else if(nsy.doubleValue() == 0){
					break;
				}
			}
			if(nsy.doubleValue()>0)
				throw new BusinessException("绑定的托盘容量不足");
		}
		//		调整实际托盘状态为锁定
		if(lactTray.size()>0){	
//			先进行托盘状态的并发校验  是否 已被其他  线程 前线占用
			getStockBO().checkTray(StockInvOnHandVO.stock_state_null, lactTray, getTtutil());
			getStockBO().updateTrayState(StockInvOnHandVO.stock_state_lock, lactTray, getTtutil());
		}
		//		插入绑定关系表
		if(lrelation.size()>0)
			getSuperDMO().insertArray(lrelation.toArray(new XnRelationVO[0]));
	}
	
	private UFDouble queryInvTrayVolume(String cinvmanid) throws BusinessException{
		if(PuPubVO.getString_TrimZeroLenAsNull(cinvmanid)==null)
			return null;
		String sql = "select tray_volume from wds_invbasdoc where isnull(dr,0)=0 and pk_invmandoc = '"+cinvmanid+"'";
		return PuPubVO.getUFDouble_NullAsZero(getDao().executeQuery(sql, WdsPubResulSetProcesser.COLUMNPROCESSOR));
	}
	
	
	private 	StockInvOnHandBO stockbo = new StockInvOnHandBO();
	private 	StockInvOnHandBO getStockBO(){
		if(stockbo == null){
			stockbo = new StockInvOnHandBO(getDao());
		}
		return stockbo;
	}
	
	/**
	 * 
	 * @作者：zhf
	 * @说明：完达山物流项目 完达山物流项目 托盘流水信息是否绑定实际托盘  该批物资出库时通过该方法校验  必须 解除绑定 虚拟托盘同一批次存货 只能入库一次
	 * 此处也有漏洞：及  若同一批次货物 分多次入虚拟推盘 若部分绑定  部分不绑定  出库时将无法处理
	 * @时间：2011-7-6下午08:02:47
	 * @param ctrayid 虚拟托盘ID
	 * @param pk_invmanid 存货ID
	 * @param vbatchcode 批次号
	 * @return
	 * @throws BusinessException
	 */
	public UFBoolean isLock(String ctrayid,String pk_invmanid,String vbatchcode) throws BusinessException{
		if(PuPubVO.getString_TrimZeroLenAsNull(ctrayid)==null||PuPubVO.getString_TrimZeroLenAsNull(pk_invmanid)==null||PuPubVO.getString_TrimZeroLenAsNull(vbatchcode)==null)
			return UFBoolean.FALSE;
		String sql = "select count(0) from wds_xnrelation where isnull(dr,0)=0 and cxntrayid = '"+ctrayid+"'" +
				" and pk_invmandoc = '"+pk_invmanid+"' and vbatchcode = '"+vbatchcode+"'";
		return PuPubVO.getInteger_NullAs(getDao().
				executeQuery(sql, WdsPubResulSetProcesser.COLUMNPROCESSOR), 0)>0
				?UFBoolean.TRUE:UFBoolean.FALSE;
	}
	
    /**
     * 
     * @作者：zhf
     * @说明：完达山物流项目 校验  进行了 绑定实际托盘的虚拟托盘 必须  指定  解除绑定信息
     * @时间：2011-7-6下午08:23:30
     * @param billVo
     * @throws BusinessException
     */
	public void checkIsLock(MyBillVO billVo) throws BusinessException{
		if(billVo == null)
			return;
		Map<String,SmallTrayVO[]> lockTrayInfor = (Map<String,SmallTrayVO[]>)billVo.getOUserObj();
//		获取单据虚拟托盘
		TbOutgeneralBVO[] bodys = (TbOutgeneralBVO[])billVo.getChildrenVO();
		if(bodys == null || bodys.length <=0){
			if(lockTrayInfor == null || lockTrayInfor.size() == 0)
				return;
			else
				throw new BusinessException("数据异常");
		}
		List<TbOutgeneralTVO> ltray = null;
		String key = null;
		for(TbOutgeneralBVO body:bodys){
			ltray = body.getTrayInfor();
			for(TbOutgeneralTVO tray:ltray){
				if(isLock(tray.getCdt_pk(), body.getCinventoryid(), body.getVbatchcode()).booleanValue()){
					key = WdsWlPubTool.getString_NullAsTrimZeroLen(tray.getCdt_pk())+","+WdsWlPubTool.getString_NullAsTrimZeroLen(tray.getVbatchcode());
					if(!lockTrayInfor.containsKey(key))
						throw new BusinessException("虚拟托盘已绑定了实际托盘,但未指定实际托盘");
				}
			}
		}
	}
	
	/**
	 * 
	 * @作者：zhf
	 * @说明：完达山物流项目 校验入库单据  存放在虚拟托盘的存货 
	 * 同一仓库同一货位同一虚拟托盘同一批次的存货   要绑定了实际托盘必须都绑定 要没有绑定必须都没有绑定
	 * @时间：2011-7-18下午08:56:54
	 * @param bill
	 * @throws BusinessException
	 */
	public void checkInBillOnSave(Map<String, SmallTrayVO[]> lockTrayInfor,TbGeneralBVO[] allbodys,String cwhid,String pk_cargdoc) throws BusinessException{
		if(allbodys == null || allbodys.length == 0)
			return;
		List<TbGeneralBBVO> ltray = null;
		List<String> ltrayid = new ArrayList<String>();
		for(TbGeneralBVO body:allbodys){
			ltray = body.getTrayInfor();
			for(TbGeneralBBVO tray:ltray){
				if(ltrayid.contains(tray.getCdt_pk()))
					continue;
				ltrayid.add(tray.getCdt_pk());
			}			
		}
		
		String sql = "select cdt_pk from bd_cargdoc_tray where isnull(dr,0)=0 and cdt_traycode like '"+WdsWlPubConst.XN_CARGDOC_TRAY_NAME+"%'" +
				" and cdt_pk in "+getTtutil().getSubSql((ArrayList<String>)ltrayid);
		List ldata = (List)getDao().executeQuery(sql, WdsPubResulSetProcesser.COLUMNLISTROCESSOR);
		boolean flag1 = ldata==null||ldata.size()==0;
		boolean flag2 = lockTrayInfor==null||lockTrayInfor.size()==0;
		if(flag1&&flag2)
			return;
		if(flag1&&!flag2)
			throw new BusinessException("不存在虚拟推盘,不需要进行绑定");
		
//		Set<String> sxntrayid = new HashSet<String>();
		
//		if(flag2&&!flag1){
//			return;
//		}
//		查询同一虚拟托盘 同一存货  同一批次  若存在绑定信息  本次也需要存在绑定信息  若不存在绑定信息本次也不能存在绑定
		sql = "select count(0) from wds_xnrelation where isnull(dr,0)=0 and cxntrayid = ?" +
				" and pk_invmandoc = ? and vbatchcode = ?";
		
		String corp = SQLHelper.getCorpPk();
		
	//	SQLParameter para = new SQLParameter();
		int iflag = 0;
		String key = null;
		for(TbGeneralBVO body:allbodys){
			ltray = body.getTrayInfor();
			for(TbGeneralBBVO tray:ltray){
				if(!ldata.contains(tray.getCdt_pk()))
					continue;
				SQLParameter para = new SQLParameter();
				para.addParam(tray.getCdt_pk());
				para.addParam(tray.getPk_invmandoc());
				para.addParam(body.getGeb_vbatchcode());			
				iflag = PuPubVO.getInteger_NullAs(getDao().executeQuery(sql,para, WdsPubResulSetProcesser.COLUMNPROCESSOR), 0);
				key = WdsWlPubTool.getString_NullAsTrimZeroLen(tray.getCdt_pk())+","+WdsWlPubTool.getString_NullAsTrimZeroLen(body.getGeb_vbatchcode());
				if(iflag>0){//存在绑定关系
					if(!lockTrayInfor.containsKey(key))
						throw new BusinessException("该托盘上该批次存货进行了绑定实际托盘,本次入库也必须绑定实际托盘");
				}else{//不存在绑定关系  如果是该批次的第一次  存入  可以绑定 否则不能绑定
//					判断  该批存货 是否  已存在
					UFDouble[] nums = getStockBO().getInvStockNum(corp, cwhid, pk_cargdoc, body.getGeb_cinvbasid(), body.getGeb_vbatchcode(), tray.getCdt_pk(),null);
					boolean isexit = nums!=null && nums.length>0 && nums[0].doubleValue()>0;
					if(lockTrayInfor.containsKey(key)&&isexit){
						throw new BusinessException("该托盘上该批次存货未进行绑定,本次入库也不能绑定实际托盘");
					}
				}
			}			
		}
		
	}
	
	
}
