package nc.bs.wds.ic.allo.in.close;

import java.util.ArrayList;
import java.util.List;
import nc.bs.dao.BaseDAO;
import nc.bs.wl.pub.WdsPubResulSetProcesser;
import nc.bs.wl.pub.WdsWlPubBO;
import nc.itf.scm.cenpur.service.TempTableUtil;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.jdbc.framework.util.SQLHelper;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.scm.pub.vosplit.SplitBillVOs;
import nc.vo.wds.ic.allo.in.close.AlloCloseBVO;
import nc.vo.wds.ic.allo.in.close.AlloCloseBillVO;
import nc.vo.wds.ic.allo.in.close.AlloCloseHVO;
import nc.vo.wds.ic.allo.in.close.AlloCloseVO;
import nc.vo.wl.pub.WdsWlPubConst;
import nc.vo.wl.pub.WdsWlPubTool;

public class AlloCloseBO {

	private BaseDAO m_dao = null;
	private BaseDAO getDao() {
		if (m_dao == null) {
			m_dao = new BaseDAO();
		}
		return m_dao;
	}

	private StringBuffer getBaseQrySql(){
		AlloCloseHVO head = new AlloCloseHVO();
		AlloCloseBVO body = new AlloCloseBVO();

		StringBuffer sql = new StringBuffer();

		sql.append("select  ");
		String[] names = head.getAttributeNames();

		for (String name : names) {
			sql.append(" h."+name + ", ");
		}
		names = body.getAttributeNames();
		for (String name : names) {
			sql.append(" b."+name + ", ");
		}
		sql.append(" 'aaa' ");
		sql.append(" from ic_general_h h  " );
		sql.append(" inner join ic_general_b b on h.cgeneralhid = b.cgeneralhid");
		sql.append(" where ");
		sql.append("  isnull(h.dr,0)=0  and isnull(b.dr,0)=0  ");
		return sql;
	}

	public AlloCloseBillVO[] doQuery(String whereSql,String pk_storedoc,String userid,UFBoolean isclose)
	throws BusinessException{

		StringBuffer sql = getBaseQrySql();

		//		------------------------------------------------zhf add
		sql.append(" and coalesce(h.bisclose,'N') = ");
		if(isclose.booleanValue())
			sql.append(" 'Y' ");
		else
			sql.append(" 'N' ");
		//		------------------------------------------------

		if (whereSql != null && whereSql.length() > 0) {
			sql.append(whereSql);
		}

		//		默认条件
		sql.append("  and h.cothercorpid ='"+SQLHelper.getCorpPk()+"' and h.cbilltypecode = '4Y' ");//and head.fbillflag=3 //查询 供应链调拨出库 ----调入公司等于当前公司，单据类型为4Y
		if(!WdsWlPubTool.isZc(pk_storedoc)){
			sql.append("and h.cotherwhid='"+pk_storedoc+"'");//分仓只能看到自己的，总仓可以看到总仓+分仓的
		}
//		sql.append(" and coalesce(b.noutnum,0)-coalesce(b."+WdsWlPubConst.erp_allo_outnum_fieldname+",0)>0");//应入数量-转出数量>0

		//保管员绑定货位对应的存货
		WdsWlPubBO wlbo = new WdsWlPubBO();

		String[] spaces = wlbo.getSpaceByLogUser(userid);
		if(spaces == null || spaces.length == 0)
			throw new BusinessException("当前操作员未绑定到仓库");
		if(spaces.length == 1){
			String[] inv_Pks = wlbo.getInvbasdocIDsBySpaceID(spaces[0]);

			sql.append("and h.cgeneralhid in");//只能看到包含当前登录人绑定货位下存货的单据
			sql.append("(");
			sql.append("select distinct cgeneralhid from ic_general_b where isnull(ic_general_b.dr,0)=0");
			sql.append(" and cinvbasid in "+getTempTableUtil().getSubSql(inv_Pks)+")");
		}	


		//		查询数据
		Object o = getDao().executeQuery(sql.toString(),
				new BeanListProcessor(AlloCloseVO.class));
		if (o == null)
			return null;
		ArrayList<AlloCloseVO> list = (ArrayList<AlloCloseVO>) o;
		AlloCloseVO[] datas = list.toArray(new AlloCloseVO[0]);

		if(datas == null || datas.length == 0)
			return null;

		//		数据转换
		CircularlyAccessibleValueObject[][] vos = SplitBillVOs.getSplitVOs(datas, new String[]{"cgeneralhid"});

		int len = vos.length;
		AlloCloseVO[] tmps = null;
		AlloCloseBillVO[] bills = new AlloCloseBillVO[len];
		AlloCloseBillVO bill = null;
		for(int i=0;i<len;i++){
			tmps = (AlloCloseVO[])(vos[i]);
			bill = new AlloCloseBillVO();
			bill.setHead(getHead(tmps[0]));
			bill.setBodys(getBodys(tmps));
			bills[i] = bill;
		}

		return bills;
	}

	private AlloCloseBVO[] getBodys(AlloCloseVO[] vos){
		if(vos == null || vos.length == 0)
			return null;
		AlloCloseBVO[] bodys = new AlloCloseBVO[vos.length];
		AlloCloseBVO body = null;
		int index = 0;
		String[] names = new AlloCloseBVO().getAttributeNames();
		for(AlloCloseVO vo:vos){
			body = new AlloCloseBVO();
			for(String name:names){
				body.setAttributeValue(name, vo.getAttributeValue(name));
			}
			bodys[index] = body;
			index++;
		}

		return bodys;
	}

	private AlloCloseHVO getHead(AlloCloseVO vo){
		if(vo == null)
			return null;
		AlloCloseHVO head = new AlloCloseHVO();
		String[] names = head.getAttributeNames();
		for(String name:names){
			head.setAttributeValue(name, vo.getAttributeValue(name));
		}
		return head;
	}

	private TempTableUtil m_tempUtil = null;
	private TempTableUtil getTempTableUtil(){
		if(m_tempUtil == null)
			m_tempUtil = new TempTableUtil();
		return m_tempUtil;
	}


	//	zhf add ---------------------------2012 1 4
	public void doCloseOrOpen(String[] outids,UFBoolean isclose) throws BusinessException{
		if(outids == null || outids.length == 0)
			return;
		//		TempTableUtil ut = new TempTableUtil();
		String sflag = isclose.booleanValue()?"N":"Y";//---------------
		String sql = "select cgeneralhid from ic_general_h where isnull(dr,0) = 0 " +
		" and coalesce(bisclose,'N') = '"+sflag+"'" +
		" and cgeneralhid in "+getTempTableUtil().getSubSql(outids);
		List ldata = (List)getDao().executeQuery(sql, WdsPubResulSetProcesser.COLUMNLISTROCESSOR);
		if(ldata == null || ldata.size() == 0)
			return;
		sflag = isclose.booleanValue()?"Y":"N";//-------------------
		sql = "update ic_general_h set bisclose = '"+sflag+"' where cgeneralhid in "+getTempTableUtil().getSubSql((ArrayList)ldata);
		getDao().executeUpdate(sql);
	}

}
