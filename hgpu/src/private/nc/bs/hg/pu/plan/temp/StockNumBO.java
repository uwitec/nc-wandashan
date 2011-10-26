package nc.bs.hg.pu.plan.temp;

import java.util.ArrayList;

import nc.bs.dao.BaseDAO;
import nc.bs.framework.common.NCLocator;
import nc.bs.hg.pu.pub.HgBsPubTool;
import nc.itf.ic.pub.IInvOnHand;
import nc.vo.hg.pu.plan.temp.PlanInventoryVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pu.PuPubVO;

public class StockNumBO {
	
	private IInvOnHand  onHandBO = null;
	private IInvOnHand getOnHandBO(){
		if(onHandBO == null){
			onHandBO = (IInvOnHand)NCLocator.getInstance().lookup(IInvOnHand.class.getName());
		}
		return onHandBO;
	}
	
	private BaseDAO dao = null;
	private BaseDAO getDao(){
		if(dao == null){
			dao = new BaseDAO();
		}
		return dao;
	}
	
	public PlanInventoryVO[] getOnhandNum(String key,ArrayList<String> linvman) throws BusinessException{
		if(key==null)
			return null;
		String sql=" select pk_corp,creqcalbodyid  from hg_plan where pk_plan ='"+key+"' and  isnull(dr,0)=0";
		Object o =getDao().executeQuery(sql,HgBsPubTool.ARRAYPROCESSOR);
		
		if(o==null)
			return null;
		Object[] os =(Object[])o;
		if(os==null || os.length==0)
			return null;
		if(linvman ==null || linvman.size()==0)
			throw new BusinessException("获取存量异常，存货信息为空");
		String[] invmanids = linvman.toArray(new String[0]);
		
		UFDouble[] onHandNums = getOnHandBO().getOnhandNums(PuPubVO.getString_TrimZeroLenAsNull(os[0]).trim(),PuPubVO.getString_TrimZeroLenAsNull(os[1]),null,invmanids);
		if(onHandNums == null||onHandNums.length ==0 || onHandNums.length!=invmanids.length)
			throw new BusinessException("获取库存存量异常");
		
		int length=onHandNums.length ;
		PlanInventoryVO[] vos = new PlanInventoryVO[length];
		for(int i=0;i<length;i++){
			PlanInventoryVO vo = new PlanInventoryVO();
			vo.setPk_invmandoc(invmanids[i]);
			vo.setNinvnum(onHandNums[i]);
			vos[i]=vo;
		}
		return vos;
	}
	
	public PlanInventoryVO[] getOnhandNum1(ArrayList<String> linvman) throws BusinessException{
		
		if(linvman ==null || linvman.size()==0)
			throw new BusinessException("获取存量异常，存货信息为空");
		String[] invmanids = linvman.toArray(new String[0]);
		
		UFDouble[] onHandNums = getOnHandBO().getOnhandNums("1002","1002Q1100000000002BE",null,invmanids);
		if(onHandNums == null||onHandNums.length ==0 || onHandNums.length!=invmanids.length)
			throw new BusinessException("获取库存存量异常");
		
		int length=onHandNums.length ;
		PlanInventoryVO[] vos = new PlanInventoryVO[length];
		for(int i=0;i<length;i++){
			PlanInventoryVO vo = new PlanInventoryVO();
			vo.setPk_invmandoc(invmanids[i]);
			vo.setNinvnum(onHandNums[i]);
			vos[i]=vo;
		}
		return vos;
	}

}
