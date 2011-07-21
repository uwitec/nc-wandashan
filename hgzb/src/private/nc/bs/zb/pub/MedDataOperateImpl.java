package nc.bs.zb.pub;

import java.util.ArrayList;

import nc.bs.logging.Logger;
import nc.bs.pub.SuperDMO;
import nc.itf.zb.pub.IMedDataOperate;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
import nc.vo.zb.pub.freeitem.DefdefHeaderVO;
import nc.vo.zb.pub.freeitem.DefdefItemVO;
import nc.vo.zb.pub.freeitem.DefdefVO;
import nc.vo.zb.pub.freeitem.VInvVO;


public class MedDataOperateImpl implements IMedDataOperate{
	
	private SuperDMO superDmo = null;
	
	public SuperDMO getSuperDmo() {
		if(superDmo == null){
			superDmo = new SuperDMO();
		}
		return superDmo;
	}
	
	public void setSuperDmo(SuperDMO superDmo) {
		this.superDmo = superDmo;
	}
	
	/**
	 * 批量更新
	 */
	public void updateSuperVOs(SuperVO[] vos) throws BusinessException {
		if(vos!=null && vos.length>0){
			ArrayList<String> al = new ArrayList<String>();
			for(SuperVO vo : vos){
				String pk = vo.getPrimaryKey();
				al.add(pk);
			}
			LockBO lock = new LockBO();
			lock.lockPKs(null, al);	
			getSuperDmo().updateArray(vos);
		}
	}   
	/**
	 * 自由项使用，查询自定义项VO
	 * 
	 */
	public DefdefVO findByPrimaryKey(String key) throws BusinessException {
		DefdefVO vo = null;
		try{
			vo = new DefdefVO();
			ReportDMO dmo = new ReportDMO();
			String sql = " select defcode, defname, pk_defdoclist, type, lengthnum, digitnum, ts, dr from bd_defdef where pk_defdef = '"+key+"' ";
			ArrayList list = dmo.queryCircuarlyVO(DefdefHeaderVO.class, sql);
			if(list!=null && list.size()>0){
				DefdefHeaderVO header = (DefdefHeaderVO)list.get(0);
				DefdefItemVO[] items = null;
				if (header != null) {
					String hql = "select bd_defdoc.pk_defdoc,bd_defdoc.pk_defdoclist, bd_defdoc.pk_defdoc1, bd_defdoc.doccode,"
						+ " bd_defdoc.docname, bd_defdoc.docsystype,bd_defdoc.ts, bd_defdoc.dr from "
						+ " bd_defdef inner join bd_defdoclist on bd_defdef.pk_defdoclist=bd_defdoclist.pk_defdoclist "
						+ " inner join bd_defdoc on bd_defdoc.pk_defdoclist=bd_defdoclist.pk_defdoclist where bd_defdef.pk_defdef = '"+key+"' ";
					ArrayList clist = dmo.queryCircuarlyVO(DefdefItemVO.class, hql);
					if(clist!=null && clist.size()>0){
						items = new DefdefItemVO[clist.size()];
						clist.toArray(items);
					}
				}
				vo.setParentVO(header);
				vo.setChildrenVO(items);
			}
		}catch(Exception e){
			throw new BusinessException(e);
		}
		return vo;
	}
	/**
	 * 根据存货管理ID，生成相应的存货vo信息<表体自由项信息>
	 */
	public VInvVO[] getInvVO(String[] cinvtoryids) throws BusinessException {
		VInvVO[] invos = null;
		try{
			ReportDMO dmo = new ReportDMO();
			StringBuffer sql = new StringBuffer();
			sql.append(" select ");
			sql.append(" bd_invmandoc.pk_invmandoc cinventoryid, ");
			sql.append(" bd_invbasdoc.invcode cinventorycode, ");
			sql.append(" bd_invbasdoc.invname invname, ");
			sql.append(" bd_invbasdoc.invspec invspec, ");
			sql.append(" bd_invbasdoc.invtype invtype ");
			sql.append("  from bd_invmandoc ");
			sql.append(" join bd_invbasdoc on bd_invmandoc.pk_invbasdoc  = bd_invbasdoc.pk_invbasdoc ");
			sql.append(" join bd_defdef on bd_invbasdoc.free1 = bd_defdef.pk_defdef ");
			//
			ArrayList clist = dmo.queryCircuarlyVO(VInvVO.class, sql.toString());
			if(clist!=null && clist.size()>0){
				invos = new VInvVO[clist.size()];
				clist.toArray(invos);
			}
		}catch(Exception e){
			Logger.error(e);
		}
		return invos;
	}
}
