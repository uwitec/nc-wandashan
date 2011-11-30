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
			throw new BusinessException("�����쳣");
		if(lockTrayInfor == null || lockTrayInfor.size() == 0)
			return;
		if(PuPubVO.getString_TrimZeroLenAsNull(gebbid)==null)
			throw new BusinessException("�����쳣");
		TbGeneralBBVO bb = (TbGeneralBBVO)getSuperDMO().queryByPrimaryKey(TbGeneralBBVO.class, gebbid);
		if(bb == null)
			throw new BusinessException("�����쳣");
		String bid = bb.getGeb_pk();
		TbGeneralBVO b = (TbGeneralBVO)getSuperDMO().queryByPrimaryKey(TbGeneralBVO.class, bid);
		if(b == null)
			throw new BusinessException("�����쳣");
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
	 * @���ߣ�zhf
	 * @˵�������ɽ������Ŀ �������
	 * @ʱ�䣺2011-7-5����08:05:04
	 * @param cgebbids
	 * @throws BusinessException
	 */
	public void reLockTray(String[] cgebbids) throws BusinessException{
		if(cgebbids == null || cgebbids.length == 0)
			return;
//      ��ѯ�󶨵�ʵ������
		String sql = "select ctrayid from wds_xnrelation where cbbid in "+getTtutil().getSubSql(cgebbids);
		List ldata = (List)getDao().executeQuery(sql, WdsPubResulSetProcesser.COLUMNLISTROCESSOR);
		if(ldata == null || ldata.size() == 0)
			return;
//		У������״̬����������̬
		getStockBO().checkTray(StockInvOnHandVO.stock_state_lock, ldata, getTtutil());
//		�޸�����״̬Ϊ����
		getStockBO().updateTrayState(StockInvOnHandVO.stock_state_null, (List<String>)ldata, getTtutil());
//		ɾ��������ϵ��
		sql = "update wds_xnrelation set dr = 1 where cbbid in "+getTtutil().getSubSql(cgebbids);
		getDao().executeUpdate(sql);
	}
	
	/**
	 * 
	 * @���ߣ�zhf
	 * @˵�������ɽ������Ŀ �������
	 * @ʱ�䣺2011-7-5����08:05:04
	 * @param cgebbids
	 * @throws BusinessException
	 */
	public void reLockTray2(XnRelationVO[] revos) throws BusinessException{
		if(revos == null || revos.length == 0)
			return;
//      ��ѯ�󶨵�ʵ������
        List<String> ldata = new ArrayList<String>();
        for(XnRelationVO revo:revos){
        	ldata.add(revo.getCtrayid());
        }
		if(ldata == null || ldata.size() == 0)
			return;
//		У������״̬����������̬
		getStockBO().checkTray(StockInvOnHandVO.stock_state_lock, ldata, getTtutil());
//		�޸�����״̬Ϊ����
		getStockBO().updateTrayState(StockInvOnHandVO.stock_state_null, ldata, getTtutil());
//		ɾ��������ϵ��
		String sql = "update wds_xnrelation set dr = 1 where cxntrayid = '"+revos[0].getCxntrayid()+"'";
		getDao().executeUpdate(sql);
	}
	
	/**
	 * 
	 * @���ߣ�zhf
	 * @˵�������ɽ������Ŀ ��ȡ�󶨵�ʵ�������ϵĴ���
	 * @ʱ�䣺2011-7-6����02:48:54
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
	 * @���ߣ�zhf
	 * @˵�������ɽ������Ŀ ����ʵ�����̵��������̳���ʱ �� ʵ�����̵� ָ����������
	 * �˴����ܴ���©��  ������� һ���λ�Ʒ  ���¼�뵽ͬһ����������ʱ   ���ְ󶨵������  
	 * �����������
	 * ���� �����δ�� ������100   �����100 ����  60������ʵ������  40��û�а�ʵ������
	 * 
	 * ��γ��⣿  �������������� ����û�ָ����ʵ������  ϵͳ��ʵ�����̵�����״̬���
	 * ʵ������δ����ļ�������  �ȴ��´εĳ��� 
	 * 
	 * �ڽ��������� ������  ���������У��
	 * 
	 * ��δ���
	 * @ʱ�䣺2011-7-6����02:47:58
	 * @param chid
	 * @param cwarehouseid
	 * @param lockTrayInfor2
	 * @throws BusinessException
	 */
	public void doDelLockTrayInfor(String chid,String cwarehouseid,Map lockTrayInfor2) throws BusinessException{
		//	       ��װ����
		Map<String,SmallTrayVO[]> lockTrayInfor = (Map<String,SmallTrayVO[]>)lockTrayInfor2;
		if(chid == null || lockTrayInfor == null || lockTrayInfor.size() == 0)
			return;
		//		��ѯ��   �����������̵� �����ˮ��  ��Ϣ
		List<String> ltrayid = new ArrayList<String>();
		String[] ss = null;
		for(String key:lockTrayInfor.keySet()){
			ss = key.split(",");
			if(PuPubVO.getString_TrimZeroLenAsNull(ss[0])==null)
				throw new BusinessException("�󶨹�ϵ�����쳣");
			ltrayid.add(ss[0]);
		}

		String sql = " isnull(dr,0) = 0 and general_pk = '"+chid+"' and cdt_pk in "+getTtutil().getSubSql((ArrayList)ltrayid);
		TbOutgeneralTVO[] trays = (TbOutgeneralTVO[])getSuperDMO().queryByWhereClause(TbOutgeneralTVO.class, sql);

		if(trays == null || trays.length == 0)
			return;
		
//		������ˮ�� ʵ�ʳ�������  ��  �󶨵�ʵ�������ϼ��  ����   ʵ�����̲��㱨��    ��˳�����μ��  δ��� ����״̬��Ϊ����
		
		List<String> lacttrayid = new ArrayList<String>();//��Ҫ������ʵ�����̵�id
		String key = null;
//		String[] 
		SmallTrayVO[] actTrays = null;
		
//		��������
		UFDouble nvolume = null;
		UFDouble nallnum = null;
		UFDouble nsy = null;
		
		Object[] relaNumInfor = null;
		
		for(TbOutgeneralTVO tray:trays){
			key = WdsWlPubTool.getString_NullAsTrimZeroLen(tray.getCdt_pk())
			+","+WdsWlPubTool.getString_NullAsTrimZeroLen(tray.getVbatchcode());
			actTrays = lockTrayInfor.get(key);
			if(actTrays == null || actTrays.length == 0){
				throw new BusinessException("����δָ��ʵ������");
			}
			
//			ʵ�ʳ�  ����
			nallnum = PuPubVO.getUFDouble_NullAsZero(tray.getNoutassistnum());
			nsy = nallnum;
			nvolume = getInvTrayVolume(tray.getPk_invmandoc());
			if(nvolume.equals(WdsWlPubTool.DOUBLE_ZERO)){
				throw new BusinessException("���������������δ���û�Ϊ0");
			}
			
			if(nallnum.doubleValue()>nvolume.multiply(actTrays.length).doubleValue()){
				throw new BusinessException("���̴�������");
			}			
			
//			�������
			for(SmallTrayVO actTray:actTrays){
				
				/**
				 * zhf ˵��  �������̰�ʵ������  ʵ�����̵Ĵ����Ϣ����д��  ���״̬����  ��Ϊ��������
				 * ͳһ�ڿ��״̬����  д����   ���󶨹�ϵ����Ҫά�� �� ��Ϊ���ܴ���ʵ�������ڰ�ʱû��ȫ��ռ�õ����
				 */
				nvolume = getTrayNum(actTray.getCdt_pk(), true);
				nsy = nsy.sub(nvolume);
				if(nsy.doubleValue()<0){
//					�������״̬��Ĵ���------------���һ������δ�����������  ��Ҫ������  ��״̬��Ϊ����
					relaNumInfor = new Object[]{actTray.getCdt_pk(), nsy.abs()};
					break;
				}else if(nsy.doubleValue() == 0){
					lacttrayid.add(actTray.getCdt_pk());
					break;
				}
				lacttrayid.add(actTray.getCdt_pk());				
			}
			if(nsy.doubleValue()>0)
				throw new BusinessException("ָ�����̴�������");
		}
		

		if(lacttrayid.size()>0){
//			����ʵ������״̬
			getStockBO().checkTray(StockInvOnHandVO.stock_state_lock, lacttrayid, getTtutil());
			getStockBO().updateTrayState(StockInvOnHandVO.stock_state_null, lacttrayid, getTtutil());
//			ɾ����ϵ��
			sql = "update wds_xnrelation set dr = 1 where ctrayid in "+getTtutil().getSubSql((ArrayList)lacttrayid);
			getDao().executeUpdate(sql);
		}

//		������ϵ������
		if(relaNumInfor != null && relaNumInfor.length > 0){
			sql = "update wds_xnrelation set nassisnum = '"+PuPubVO.getUFDouble_NullAsZero(relaNumInfor[1]).toBigDecimal()+"' where ctrayid = '"+relaNumInfor[1].toString()+"'";
			getDao().executeUpdate(sql);
		}
	}
	
	
	/**
	 * 
	 * @���ߣ�zhf
	 * @˵�������ɽ������Ŀ �������  �������̺�ʵ�����̰󶨹�ϵ��
	 * @ʱ�䣺2011-7-5����02:53:39
	 * @param bill ��ⵥ��
	 * @param lockTrayInfor ��������������ϵ  key ����id+��+���id
	 * @throws BusinessException
	 */
	public void doSaveLockTrayInfor(String chid,String cwarehouseid,Map lockTrayInfor2) throws BusinessException{
		//	       ��װ����
		Map<String,SmallTrayVO[]> lockTrayInfor = (Map<String,SmallTrayVO[]>)lockTrayInfor2;
		if(chid == null || lockTrayInfor == null || lockTrayInfor.size() == 0)
			return;
		//		��ѯ��   �����������̵� �����ˮ��  ��Ϣ
		List<String> ltrayid = new ArrayList<String>();
		String[] ss = null;
		for(String key:lockTrayInfor.keySet()){
			ss = key.split(",");
			if(PuPubVO.getString_TrimZeroLenAsNull(ss[0])==null)
				throw new BusinessException("�󶨹�ϵ�����쳣");
			ltrayid.add(ss[0]);
		}

		String sql = " isnull(dr,0) = 0 and geh_pk = '"+chid+"' and cdt_pk in "+getTtutil().getSubSql((ArrayList)ltrayid);
		TbGeneralBBVO[] trays = (TbGeneralBBVO[])getSuperDMO().queryByWhereClause(TbGeneralBBVO.class, sql);

		if(trays == null || trays.length == 0)
			return;
		

		//		���ǵ����� �� ���������    ����ʱ ��  ���֮ǰ�Ĺ�ϵ  Ȼ�� ���� ȫ�µİ󶨹�ϵ
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


		//		��������
		UFDouble nvolume = null;
		UFDouble nallnum = null;
		UFDouble nsy = null;

		//		ռ�õ�ʵ������
		List<String> lactTray = new ArrayList<String>();

		for(TbGeneralBBVO tray:trays){
			key = WdsWlPubTool.getString_NullAsTrimZeroLen(tray.getCdt_pk())
			+","+WdsWlPubTool.getString_NullAsTrimZeroLen(tray.getGebb_vbatchcode());
			traylocks = lockTrayInfor.get(key);
			if(traylocks == null || traylocks.length ==0)
				return;
			//			ʵ����  ����
			nallnum = PuPubVO.getUFDouble_NullAsZero(tray.getNinassistnum());
			nsy = nallnum;
			//			�����������
			nvolume = getInvTrayVolume(tray.getPk_invmandoc());
			if(nvolume.equals(WdsWlPubTool.DOUBLE_ZERO)){
				throw new BusinessException("���������������δ���û�Ϊ0");
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
				throw new BusinessException("�󶨵�������������");
		}
		//		����ʵ������״̬Ϊ����
		if(lactTray.size()>0){	
//			�Ƚ�������״̬�Ĳ���У��  �Ƿ� �ѱ�����  �߳� ǰ��ռ��
			getStockBO().checkTray(StockInvOnHandVO.stock_state_null, lactTray, getTtutil());
			getStockBO().updateTrayState(StockInvOnHandVO.stock_state_lock, lactTray, getTtutil());
		}
		//		����󶨹�ϵ��
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
	 * @���ߣ�zhf
	 * @˵�������ɽ������Ŀ ���ɽ������Ŀ ������ˮ��Ϣ�Ƿ��ʵ������  �������ʳ���ʱͨ���÷���У��  ���� ����� ��������ͬһ���δ�� ֻ�����һ��
	 * �˴�Ҳ��©������  ��ͬһ���λ��� �ֶ������������ �����ְ�  ���ֲ���  ����ʱ���޷�����
	 * @ʱ�䣺2011-7-6����08:02:47
	 * @param ctrayid ��������ID
	 * @param pk_invmanid ���ID
	 * @param vbatchcode ���κ�
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
     * @���ߣ�zhf
     * @˵�������ɽ������Ŀ У��  ������ ��ʵ�����̵��������� ����  ָ��  �������Ϣ
     * @ʱ�䣺2011-7-6����08:23:30
     * @param billVo
     * @throws BusinessException
     */
	public void checkIsLock(MyBillVO billVo) throws BusinessException{
		if(billVo == null)
			return;
		Map<String,SmallTrayVO[]> lockTrayInfor = (Map<String,SmallTrayVO[]>)billVo.getOUserObj();
//		��ȡ������������
		TbOutgeneralBVO[] bodys = (TbOutgeneralBVO[])billVo.getChildrenVO();
		if(bodys == null || bodys.length <=0){
			if(lockTrayInfor == null || lockTrayInfor.size() == 0)
				return;
			else
				throw new BusinessException("�����쳣");
		}
		List<TbOutgeneralTVO> ltray = null;
		String key = null;
		for(TbOutgeneralBVO body:bodys){
			ltray = body.getTrayInfor();
			for(TbOutgeneralTVO tray:ltray){
				if(isLock(tray.getCdt_pk(), body.getCinventoryid(), body.getVbatchcode()).booleanValue()){
					key = WdsWlPubTool.getString_NullAsTrimZeroLen(tray.getCdt_pk())+","+WdsWlPubTool.getString_NullAsTrimZeroLen(tray.getVbatchcode());
					if(!lockTrayInfor.containsKey(key))
						throw new BusinessException("���������Ѱ���ʵ������,��δָ��ʵ������");
				}
			}
		}
	}
	
	/**
	 * 
	 * @���ߣ�zhf
	 * @˵�������ɽ������Ŀ У����ⵥ��  ������������̵Ĵ�� 
	 * ͬһ�ֿ�ͬһ��λͬһ��������ͬһ���εĴ��   Ҫ����ʵ�����̱��붼�� Ҫû�а󶨱��붼û�а�
	 * @ʱ�䣺2011-7-18����08:56:54
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
			throw new BusinessException("��������������,����Ҫ���а�");
		
//		Set<String> sxntrayid = new HashSet<String>();
		
//		if(flag2&&!flag1){
//			return;
//		}
//		��ѯͬһ�������� ͬһ���  ͬһ����  �����ڰ���Ϣ  ����Ҳ��Ҫ���ڰ���Ϣ  �������ڰ���Ϣ����Ҳ���ܴ��ڰ�
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
				if(iflag>0){//���ڰ󶨹�ϵ
					if(!lockTrayInfor.containsKey(key))
						throw new BusinessException("�������ϸ����δ�������˰�ʵ������,�������Ҳ�����ʵ������");
				}else{//�����ڰ󶨹�ϵ  ����Ǹ����εĵ�һ��  ����  ���԰� �����ܰ�
//					�ж�  ������� �Ƿ�  �Ѵ���
					UFDouble[] nums = getStockBO().getInvStockNum(corp, cwhid, pk_cargdoc, body.getGeb_cinvbasid(), body.getGeb_vbatchcode(), tray.getCdt_pk(),null);
					boolean isexit = nums!=null && nums.length>0 && nums[0].doubleValue()>0;
					if(lockTrayInfor.containsKey(key)&&isexit){
						throw new BusinessException("�������ϸ����δ��δ���а�,�������Ҳ���ܰ�ʵ������");
					}
				}
			}			
		}
		
	}
	
	
}
