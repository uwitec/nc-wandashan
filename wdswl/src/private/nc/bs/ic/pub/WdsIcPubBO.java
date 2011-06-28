package nc.bs.ic.pub;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import nc.bs.dao.BaseDAO;
import nc.bs.framework.server.util.NewObjectService;
import nc.bs.pub.pf.IQueryData2;
import nc.bs.wl.pub.WdsPubResulSetProcesser;
import nc.bs.wl.pub.WdsWlPubBO;
import nc.itf.scm.cenpur.service.TempTableUtil;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.vo.dm.order.SendorderBVO;
import nc.vo.dm.order.SendorderVO;
import nc.vo.ic.other.out.TbOutgeneralBVO;
import nc.vo.ic.other.out.TbOutgeneralTVO;
import nc.vo.ic.pub.TbGeneralBBVO;
import nc.vo.ic.pub.TbGeneralBVO;
import nc.vo.ic.pub.bill.GeneralBillHeaderVO;
import nc.vo.ic.pub.bill.GeneralBillItemVO;
import nc.vo.ic.pub.bill.GeneralBillVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.ValidationException;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.trade.pub.HYBillVO;
import nc.vo.wl.pub.WdsWlPubConst;
import nc.vo.wl.pub.WdsWlPubTool;

public class WdsIcPubBO {
	private BaseDAO m_dao = null;
	public BaseDAO getDao() {
		if (m_dao == null) {
			m_dao = new BaseDAO();
		}
		return m_dao;
	}
	private WdsWlPubBO pubbo = null;

	public WdsWlPubBO getWdsWLBO(){
		if(pubbo == null){
			pubbo = new WdsWlPubBO();
		}
		return pubbo;
	}
	
	private ArrayList<String> usedTary =null;
	/**
	 * 
	 * @作者：zhf
	 * @说明：完达山物流项目 查询出库单据全子表信息
	 * @时间：2011-4-9下午09:03:27
	 * @param whereSql
	 * @return
	 * @throws BusinessException
	 */
	public TbOutgeneralBVO[] queryIcOutBodyInfor(String whereSql) throws BusinessException{
		Collection c = getDao().retrieveByClause(TbOutgeneralBVO.class, whereSql);
		if(c == null || c.size()==0)
			return null;
		TbOutgeneralBVO[] bodys = (TbOutgeneralBVO[])c.toArray(new TbOutgeneralBVO[0]);
		for(TbOutgeneralBVO body:bodys){
			ArrayList ctmp = (ArrayList)getDao().retrieveByClause(TbOutgeneralTVO.class, " general_b_pk = '"+body.getPrimaryKey()+"'");
			if(ctmp == null||ctmp.size()==0)
				continue;
			body.setTrayInfor(ctmp);
		}
		return bodys;
	}
	/**
	 * 
	 * @作者：zhf
	 * @说明：完达山物流项目 查询入库单据全子表信息
	 * @时间：2011-4-9下午09:03:51
	 * @param whereSql
	 * @return
	 * @throws BusinessException
	 */
	public TbGeneralBVO[] queryIcInBillBodyAllData(String whereSql) throws BusinessException{
		Collection c = getDao().retrieveByClause(TbGeneralBVO.class, whereSql);
		if(c == null || c.size()==0)
			return null;
		TbGeneralBVO[] bodys = (TbGeneralBVO[])c.toArray(new TbGeneralBVO[0]);
		Collection ctmp = null;
		Iterator<TbGeneralBBVO> it = null;
		for(TbGeneralBVO body:bodys){
			ctmp = getDao().retrieveByClause(TbGeneralBBVO.class, " geb_pk = '"+body.getPrimaryKey()+"'");
			if(ctmp == null||ctmp.size()==0)
				continue;
			List<TbGeneralBBVO> ltray = new ArrayList<TbGeneralBBVO>();
			it = ctmp.iterator();
			while(it.hasNext()){
				ltray.add(it.next());
			}
			body.setTrayInfor(ltray);
		}
		return bodys;

	}

	public void doSaveTrayInfor(List<TbGeneralBBVO>lallbbvos) throws BusinessException{
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
		//当前表体要入到托盘的 辅数量  将该辅数量分摊到一个或多个托盘上  
		UFDouble geb_bsnumd = PuPubVO.getUFDouble_NullAsZero(body.getGeb_bsnum());	    
		int k = 0;
		ArrayList<TbGeneralBBVO> lbbvos = new ArrayList<TbGeneralBBVO>();
		TbGeneralBBVO tbgbbvo = null;
		if(iszc){
			while (geb_bsnumd.doubleValue() > iVolumn) {
				tbgbbvo = new TbGeneralBBVO();
				//生产日期
				tbgbbvo.setCreadate(body.getGeb_dbizdate());
				//失效日期
				tbgbbvo.setExpdate(body.getGeb_dvalidate());
				// 批次
				tbgbbvo.setGebb_vbatchcode(body.getGeb_vbatchcode());
				// 回写批次
				tbgbbvo.setGebb_lvbatchcode(body.getGeb_backvbatchcode());
				// 行号
				tbgbbvo.setGebb_rowno((k + 1) + "0");
				//货位
				tbgbbvo.setPk_cargdoc(body.getGeb_space());
				// 出库单表体主键
				tbgbbvo.setPwb_pk(body.getGeb_cgeneralbid());//------------------
				// 换算率
				tbgbbvo.setGebb_hsl(body.getGeb_hsl());
				//存货管理id
				tbgbbvo.setPk_invmandoc(body.getGeb_cinventoryid());
				// 存货档案主键
				tbgbbvo.setPk_invbasdoc(body.getGeb_cinvbasid());
				// 入库单子表主键
				tbgbbvo.setGeb_pk(body.getGeb_pk());//-----------------
				// 单价
				tbgbbvo.setGebb_nprice(body.getGeb_nprice());
				// 金额
				tbgbbvo.setGebb_nmny(body.getGeb_nmny());
				//
				tbgbbvo.setGebb_num(PuPubVO.getUFDouble_NullAsZero(iVolumn).multiply(PuPubVO.getUFDouble_NullAsZero(body.getGeb_hsl())));
				// 托盘实际存放数量
				tbgbbvo.setNinassistnum(PuPubVO.getUFDouble_NullAsZero(iVolumn));
				geb_bsnumd = geb_bsnumd.sub(iVolumn);
				// 托盘主键
				tbgbbvo.setCdt_pk(tmpTrays[k]);
				usedTary.add(tmpTrays[k]);
				tbgbbvo.setDr(0);
				tbgbbvo.setGeb_pk(body.getGeb_pk());
				lbbvos.add(tbgbbvo);
				k++;
				
			}
		}

		TbGeneralBBVO tbgbbvo1 = new TbGeneralBBVO();
		
		//生产日期
		tbgbbvo1.setCreadate(body.getGeb_dbizdate());
		//失效日期
		tbgbbvo1.setExpdate(body.getGeb_dvalidate());
		// 批次
		tbgbbvo1.setGebb_vbatchcode(body.getGeb_vbatchcode());
		// 回写批次
		tbgbbvo1.setGebb_lvbatchcode(body.getGeb_backvbatchcode());
		// 行号
		tbgbbvo1.setGebb_rowno(k + 1 + "0");
		//货位
		tbgbbvo1.setPk_cargdoc(body.getGeb_space());
		// 出库单表体主键
		tbgbbvo1.setPwb_pk(body.getGeb_cgeneralbid());
		// 换算率
		tbgbbvo1.setGebb_hsl(body.getGeb_hsl());
		//存货管理id
		tbgbbvo1.setPk_invmandoc(body.getGeb_cinventoryid());
		// 运货档案主键
		tbgbbvo1.setPk_invbasdoc(body.getGeb_cinvbasid());
		// 入库单子表主键
		tbgbbvo1.setGeb_pk(body.getGeb_pk());
		// 单价
		tbgbbvo1.setGebb_nprice(body.getGeb_nprice());
		// 金额
		tbgbbvo1.setGebb_nmny(body.getGeb_nmny());
		// 托盘实际存放数量
		tbgbbvo1.setNinassistnum(PuPubVO.getUFDouble_NullAsZero(geb_bsnumd));
		tbgbbvo1.setGebb_num(PuPubVO.getUFDouble_NullAsZero(geb_bsnumd).multiply(PuPubVO.getUFDouble_NullAsZero(body.getGeb_hsl())));

		// 托盘主键
		tbgbbvo1.setCdt_pk(tmpTrays[k]);
		usedTary.add(tmpTrays[k]);
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
	 * @param bodys cwhid=仓库  cspaceid=货位
	 * @throws BusinessException
	 */
	public Object autoInTray(TbGeneralBVO[] bodys, String cwhid, String cspaceid)
	throws BusinessException {
		if (bodys == null || bodys.length == 0)
			throw new BusinessException("传入表体数据为空");
//<行号，不能够指定托盘的具体原因>
		Map<String, Integer> retInfor = new HashMap<String, Integer>();		
//<行号，托盘>
		Map<String,List<TbGeneralBBVO>> trayInfor= new HashMap<String,List<TbGeneralBBVO>>();
//当前已经表体存货提前占有的托盘id
		usedTary = new ArrayList<String>();
		List<TbGeneralBBVO> ltray = null;
//能放指定存货的本货位内的所有的托盘id
		String[] tmpTrays = null;
//托盘容量
		int iVolumn = 0;
//当前货位下空托盘数*托盘容量
		int iAllVolumn = 0;
		boolean iszc = WdsWlPubTool.isZc(cwhid);
		if (!iszc) {
//总仓分托盘存放，分仓只有一个虚拟托盘
			String sql = "select count(0) from bd_cargdoc_tray where  isnull(dr,0)=0 and pk_cargdoc = '"
				+ cspaceid + "'";
			int index = PuPubVO.getInteger_NullAs(getDao().executeQuery(sql,
					WdsPubResulSetProcesser.COLUMNPROCESSOR), 0);
			if (index <= 0)
				throw new ValidationException("分仓没有虚拟托盘");
			//			return null;
			String fcTaryId = getWdsWLBO().getFcTrayInfor(cspaceid);
			if(fcTaryId == null){
				throw new ValidationException("分仓没有虚拟托盘");
			}
			iVolumn=WdsWlPubConst.XN_CARGDOC_TRAY_VO;
			for (TbGeneralBVO body : bodys) {
				ltray = new ArrayList<TbGeneralBBVO>();
				ltray.addAll(shareNumToTrays(new String[]{fcTaryId}, body, iVolumn, iszc));
				trayInfor.put(body.getGeb_crowno(), ltray);
			}
		}else{
			for (TbGeneralBVO body : bodys) {
				String notIn = " and cdt_pk not in"+getTempTableUtil().getSubSql(usedTary)
				+" and bd_cargdoc_tray.cdt_traycode not like '"+WdsWlPubConst.XN_CARGDOC_TRAY_NAME+"%'";
				String oldCdt= getOldCdt(body);
// 获取满足条件的托盘信息
				tmpTrays = getWdsWLBO().getTrayInfor(cspaceid, body.getGeb_cinvbasid(),notIn,oldCdt);
				if (tmpTrays == null || tmpTrays.length == 0) {
// 没有闲置的托盘了
					retInfor.put(body.getGeb_crowno(), 0);
					continue;
				}
//从存货档案中获取托盘存放容量	
				iVolumn = getWdsWLBO().getTrayVolumeByInvbasid(body.getGeb_cinvbasid());				
//计算当前货位下存放该单品的托盘总容量				
				iAllVolumn = iVolumn * tmpTrays.length;
//总容积 a 与 当前物品的应收辅数量比较b
// 如果	a>b 则能够存放该单品
// 如果   a<b 则不够存放该单品												
				UFDouble nRes = body.getGeb_bsnum().sub(PuPubVO.getUFDouble_NullAsZero(iAllVolumn));
				if(trayInfor.containsKey(body.getGeb_crowno())){
					ltray = trayInfor.get(body.getGeb_crowno());
				}else
					ltray = new ArrayList<TbGeneralBBVO>();
				if (nRes.doubleValue() > 0) {
					retInfor.put(body.getGeb_crowno(), 1);// 要放的货物超出当前货位下可存放该存货的托盘承受量----------------
					continue;
				} else if (nRes.equals(WdsWlPubTool.DOUBLE_ZERO)) {// 托盘正好满足本次需求
					ltray.addAll(shareNumToTrays(tmpTrays, body, iVolumn, iszc));
				} else {// 托盘绰绰有余 如何使用托盘 托盘使用是否有优先级 按托盘编码？
					ltray.addAll(shareNumToTrays(tmpTrays, body, iVolumn, iszc));
				}

				trayInfor.put(body.getGeb_crowno(), ltray);

			}
		}
		Object[] retObj = new Object[]{trayInfor,retInfor}; 
		return retObj;	
	}
	/**
	 * 
	 * @作者：lyf
	 * @说明：完达山物流项目 
	 * @时间：2011-4-20下午04:10:09
	 * @param body
	 * @return  当前占用的托盘id
	 * @throws BusinessException 
	 */
	private String getOldCdt(TbGeneralBVO body ) throws BusinessException{
		StringBuffer strWhere =  new StringBuffer(" or cdt_pk in ");
		if(body.getGeb_pk() !=null && !"".equalsIgnoreCase(body.getGeb_pk())){
			String sql ="select cdt_pk  from tb_general_b_b where geb_pk='"+body.getGeb_pk()+"'";
			ArrayList<String> list = (ArrayList<String>)getDao().executeQuery(sql, WdsPubResulSetProcesser.COLUMNLISTROCESSOR);
			strWhere.append(getTempTableUtil().getSubSql(list));
		}else{
			strWhere.append("('aa')");
		}
		return strWhere.toString();
	}
	
	


	/**
	 * 
	 * @作者：zhf
	 * @说明：完达山物流项目 其他出库单 转仓参照时  查询  发运订单的数据
	 * @时间：2011-4-8上午11:06:42
	 * @param swhere  用户查询条件
	 * @param cwhid 出货仓库
	 * @param cuserid 当前登录用户
	 * @return
	 * @throws BusinessException
	 */
	public HYBillVO[] querySendOrderForOtherOut(String swhere,String cwhid,String cuserid) throws BusinessException{
		//查询满足条件的  发运订单
		/**
		 * 条件：
		 * 1、审批通过  2、可出库数量不为0 3、当前保管员权限货位 4、考虑是总仓还是分仓
		 */
		boolean iszc = WdsWlPubTool.isZc(cwhid);
		SendorderVO head = new SendorderVO();

		// 获取查询条
		StringBuffer sqlBuffer = new StringBuffer();
		sqlBuffer.append(" select distinct ");
		String[] names = head.getAttributeNames();
		for(String name:names){
			sqlBuffer.append("wds_sendorder."+name+",");
		}
		sqlBuffer.append("'aaa' from wds_sendorder inner join wds_sendorder_b on wds_sendorder.pk_sendorder = wds_sendorder_b.pk_sendorder ");
		sqlBuffer.append(" where isnull(wds_sendorder.dr,0)=0 and isnull(wds_sendorder_b.dr,0)=0 ");
		sqlBuffer.append(" and wds_sendorder.vbillstatus =1");
		sqlBuffer.append(" and (wds_sendorder.denddate>='"+ new UFDate(System.currentTimeMillis()).toString()+"' or wds_sendorder.denddate is null )");

		WdsWlPubBO pubbo = null;
		String[] invids = null;
		if(iszc){			
			pubbo = new WdsWlPubBO();
			String[] spaceids = pubbo.getSpaceByLogUser(cuserid);
			if(spaceids == null || spaceids.length == 0 || spaceids.length>1){
				throw new BusinessException("当前登录人非保管员");
			}
			invids = pubbo.getInvBasdocIDsBySpaceID(spaceids[0]);
			if(invids == null || invids.length ==0)
				return null;
		}else{//不是总仓人员的只能看到调出仓库是本仓库的
			sqlBuffer.append(" and wds_sendorder.pk_outwhouse = '" + cwhid + "'");
		}

		sqlBuffer.append("  and coalesce(wds_sendorder_b.ndealnum,0)-coalesce(wds_sendorder_b.noutnum,0)>0  and coalesce(wds_sendorder_b.nassdealnum,0)-coalesce(wds_sendorder_b.nassoutnum,0)>0");
		if(PuPubVO.getString_TrimZeroLenAsNull(swhere)!=null)
			sqlBuffer.append(" and "+swhere);
		//查询可以出库的表头
		List<SendorderVO> lhead = (List<SendorderVO>)getDao().executeQuery(sqlBuffer.toString(), new BeanListProcessor(SendorderVO.class));
		if(lhead == null || lhead.size() == 0)
			return null;


		//		查询表体    如果是总仓出库 需要根据  人员权限存货  对表体行进行过滤
		StringBuffer bodyWhereBuffer = new StringBuffer();
		List<String> lid = new ArrayList<String>();
		for(SendorderVO hvo:lhead){
			if(lid.contains(hvo.getPrimaryKey()))
				continue;
			lid.add(hvo.getPrimaryKey());
		}
		bodyWhereBuffer.append(" pk_sendorder in "+getTempTableUtil().getSubSql((ArrayList)lid));
		if(iszc){//过滤库管员权限存货
			bodyWhereBuffer.append(" and pk_invbasdoc in "+getTempTableUtil().getSubSql(invids));
		}
		List<SendorderBVO> lbody = (List<SendorderBVO>)getDao().retrieveByClause(SendorderBVO.class, bodyWhereBuffer.toString());

		if(lbody == null || lbody.size() == 0)
			return null;		

		//组合表头表体
		return combinToBillVO(lhead,lbody);		
	}
	private TempTableUtil ttutil = null;
	private TempTableUtil getTempTableUtil(){
		if(ttutil == null)
			ttutil = new TempTableUtil();
		return ttutil;
	}
	private HYBillVO[] combinToBillVO(List<SendorderVO> lhead,List<SendorderBVO> lbody){
		if(lhead == null||lhead.size() == 0 ||lbody == null ||lbody.size() == 0){
			return null;
		}
		Map<String,List<SendorderBVO>> bodyInfor = new HashMap<String, List<SendorderBVO>>();
		List<SendorderBVO> ltmpbody = null;
		String key = null;
		for(SendorderBVO body:lbody){
			key = body.getPk_sendorder();
			if(bodyInfor.containsKey(key)){
				ltmpbody = bodyInfor.get(key);
			}
			else 
				ltmpbody = new ArrayList<SendorderBVO>();
			ltmpbody.add(body);
			bodyInfor.put(key, ltmpbody);
		}

		List<HYBillVO> lbill = new ArrayList<HYBillVO>();
		HYBillVO tmpbill = null;
		//		ltmpbody = new ArrayList<SendorderBVO>();
		for(SendorderVO head:lhead){
			tmpbill = new HYBillVO();
			tmpbill.setParentVO(head);
			ltmpbody = bodyInfor.get(head.getPrimaryKey());
			if(ltmpbody == null || ltmpbody.size() == 0)
				continue;
			tmpbill.setChildrenVO(ltmpbody.toArray(new SendorderBVO[0]));
			lbill.add(tmpbill);
		}
		if(lbill.size()>0)
			return lbill.toArray(new HYBillVO[0]);
		return null;
	}

	/**
	 * 
	 * @作者：zhf
	 * @说明：完达山物流项目 调拨入库单参照erp调拨出库单的查询组件
	 * @时间：2011-4-9下午12:10:17
	 * @param wheresql
	 * @param cwhid
	 * @param cuserid
	 * @return
	 * @throws BusinessException
	 */
	public GeneralBillVO[] queryAlloOutBillVoForAlloIn(String wheresql,String cuserid) throws BusinessException{
		StringBuffer sBuffer = new StringBuffer(
				"select distinct head.vbillcode,head.dbilldate ,head.cwarehouseid ,head.cwhsmanagerid ,head.pk_corp ,"
				+ "head.cdptid ,head.pk_calbody ,head.cotherwhid ,head.cothercorpid ,head.cothercalbodyid ,"
				+ "head.cproviderid,head.cdispatcherid ,head.cdilivertypeid ,head.ccustomerid, "
				+ "head.cinventoryid,head.cbizid,head.cbiztype,head.cgeneralhid,head.fallocflag  "
				+ " from ic_general_h head inner join ic_general_b body "
				+ " on head.cgeneralhid = body.cgeneralhid  "
				+ " left outer join ic_general_bb3 bb3 "
				+ " ON body.cgeneralbid = bb3.cgeneralbid AND bb3.dr = 0"
				+ " inner join to_bill_b tobill on body.cfirstbillbid = tobill.cbill_bid"
				+ " WHERE head.cbilltypecode = '4Y'"
				+ " and head.fallocflag <> 0"
				+ " and (tobill.frowstatuflag <> 7 and tobill.frowstatuflag <> 9)"
				+ " and coalesce(head.boutretflag, 'N') = 'N'"
				+ " and ((coalesce(body.noutnum, 0) > 0 "
				+ " and coalesce(body.noutnum, 0) - coalesce(body.ntranoutnum, 0) - coalesce(body.naccumwastnum, 0) > 0)"
				+ " or (coalesce(body.noutnum, 0) < 0"
				+ " and coalesce(body.noutnum, 0) - coalesce(body.ntranoutnum, 0) - coalesce(body.naccumwastnum, 0) < 0))"
				+ " and head.dr = 0"
				+ " and body.dr = 0"
				+ " and tobill.dr = 0"
				+ " and (head.cothercorpid = '1021'  "
				+ " and  (head.cothercorpid = '1021'"
				+ " AND (head.fbillflag = 3 OR head.fbillflag = 4) "
				+ " and COALESCE(head.boutretflag, 'N') = 'N') and (cbilltypecode = '4Y'))");
		if(PuPubVO.getString_TrimZeroLenAsNull(wheresql)!=null)
			sBuffer.append(" and "+wheresql);



		WdsWlPubBO pubbo = new WdsWlPubBO();
		String[] spaceids = pubbo.getSpaceByLogUser(cuserid);
		if(spaceids == null || spaceids.length == 0 || spaceids.length>1){
			throw new BusinessException("当前登录人非保管员");
		}
		String[]	invids = pubbo.getInvBasdocIDsBySpaceID(spaceids[0]);
		if(invids == null || invids.length ==0)
			return null;

		sBuffer.append(" and body.cinvbasid in "+getTempTableUtil().getSubSql(invids));

		List<GeneralBillHeaderVO> lhead = (List<GeneralBillHeaderVO>)getDao().executeQuery(sBuffer.toString(), WdsPubResulSetProcesser.ARRAYLISTPROCESSOR);
		if(lhead == null || lhead.size() == 0)
			return null;
		//查询表体
		return getGeneralBillVosByHeadVos(lhead.toArray(new GeneralBillHeaderVO[0]),"  cinvbasid in "+getTempTableUtil().getSubSql(invids));
	}
	/**
	 * 
	 * @作者：zhf
	 * @说明：完达山物流项目 根据表头获取单据数据  按条件过滤
	 * @时间：2011-4-9上午11:57:25
	 * @param heads
	 * @param whereSql
	 * @return
	 * @throws BusinessException
	 */
	public GeneralBillVO[] getGeneralBillVosByHeadVos(GeneralBillHeaderVO[] heads,String whereSql) throws BusinessException{
		List<GeneralBillVO> lbillVo = new ArrayList<GeneralBillVO>(); 
		GeneralBillItemVO[] items = null;
		GeneralBillVO billvo = null;
		for(GeneralBillHeaderVO head:heads){
			if(PuPubVO.getString_TrimZeroLenAsNull(head.getPrimaryKey())==null)
				continue;
			items = queryGeneralBodyVos(head.getPrimaryKey(), whereSql);
			if(items == null||items.length == 0)
				continue;
			billvo = new GeneralBillVO();
			billvo.setParentVO(head);
			billvo.setChildrenVO(items);
			lbillVo.add(billvo);
		}
		if(lbillVo.size()>0)
			return lbillVo.toArray(new GeneralBillVO[0]);
		return null;
	}
	/**
	 * 
	 * @作者：zhf
	 * @说明：完达山物流项目 获取满足条件的库存单据体数据
	 * @时间：2011-4-9上午11:58:38
	 * @param sHeadID
	 * @param whereSql
	 * @return
	 * @throws BusinessException
	 */
	public GeneralBillItemVO[] queryGeneralBodyVos(String sHeadID,String whereSql) throws BusinessException{
		//跨模块 实例化 ic  私有类-------跨模块调用

		return (GeneralBillItemVO[])getGeneralQueryBO().queryAllBodyData(sHeadID, whereSql);
	}

	private IQueryData2 generalQuery  = null;
	private IQueryData2 getGeneralQueryBO() throws BusinessException{
		if(generalQuery == null){
			Object o =NewObjectService.newInstance("ic", "nc.bs.ic.pub.bill.GeneralBillDMO");
			if(!(o instanceof IQueryData2))
				throw new BusinessException("实例化库存私有查询组件异常");
			generalQuery = (IQueryData2)o;
		}
		return generalQuery;
	}


}
