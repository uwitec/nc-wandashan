package nc.bs.wds.ic.other.out;

import java.util.ArrayList;
import java.util.List;

import nc.bs.dao.BaseDAO;
import nc.bs.framework.common.NCLocator;
import nc.bs.pub.SuperDMO;
import nc.bs.wl.pub.WdsPubResulSetProcesser;
import nc.itf.uap.pf.IPFBusiAction;
import nc.ui.scm.util.ObjectUtils;
import nc.vo.ic.other.out.TbOutgeneralBVO;
import nc.vo.ic.other.out.TbOutgeneralHVO;
import nc.vo.ic.other.out.TbOutgeneralTVO;
import nc.vo.ic.pub.bill.GeneralBillVO;
import nc.vo.ic.pub.smallbill.SMGeneralBillVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.SuperVO;
import nc.vo.pub.VOStatus;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.wl.pub.CombinVO;
import nc.vo.wl.pub.IUFTypes;
/**
 * 其它出库(WDS8)
 * @author Administrator
 */
public class OtherOutBO  {
	
	private String s_billtype = "4I";
	private BaseDAO dao = null;

	BaseDAO getBaseDAO() {
		if (dao == null) {
			dao = new BaseDAO();
		}
		return dao;
	}
	private SuperDMO dmo = new SuperDMO();
	
	public void updateHVO(TbOutgeneralHVO hvo) throws BusinessException{
		if(hvo == null){
			return;
		}
		if(hvo.getPrimaryKey() != null)
			hvo.setStatus(VOStatus.UPDATED);
		else
			hvo.setStatus(VOStatus.NEW);
		dmo.update(hvo);
	}
    /**
     * 
     * @作者：mlr
     * @说明：完达山物流项目 
     *      	其他出库形成供应链的其他出库时 表体按 存货和批次号 进行合并  
                如果是否回写新批次被选中的话 不进行存货 和 批次号的合并
                如果没有选中，按存货合并 回写 2009 批次

     * @时间：2011-7-30上午10:13:16
     * @param billvo
     * @return
     * @throws Exception
     */
	public AggregatedValueObject combinVO(AggregatedValueObject bvo)throws Exception{
		
		AggregatedValueObject billvo=(AggregatedValueObject) ObjectUtils.serializableClone(bvo);
	     if(billvo.getParentVO()==null){
	    	 return billvo;
	     }
	     CircularlyAccessibleValueObject hvo=billvo.getParentVO();
		 UFBoolean isVbanchCode=PuPubVO.getUFBoolean_NullAs(hvo.getAttributeValue("is_yundan"), new UFBoolean(false));
		 if(isVbanchCode.booleanValue()==true){
			 return billvo;
		 }
		 if(billvo.getChildrenVO()==null){
			 return billvo;
		 }
	   	 SuperVO[] vos=(SuperVO[]) billvo.getChildrenVO();
		 SuperVO[] svos=(SuperVO[])CombinVO.combinVoByFields(vos,new String[]{"cinventoryid"},new int[]{IUFTypes.UFD,IUFTypes.UFD,IUFTypes.UFD,IUFTypes.UFD,IUFTypes.UFD,IUFTypes.UFD,IUFTypes.UFD},new String[]{"noutnum","noutassistnum","nacceptnum","nassacceptnum","nshouldoutnum","nshouldoutassistnum","ntagnum"});
		 setSpaceAllon(svos);
		 billvo.setChildrenVO(svos);
		 return billvo;				
	}
	/**
	 * 
	 * @作者：mlr
	 * @说明：完达山物流项目 
	 *        对要出库的表体 的存货进行货位 分配
	 * @时间：2011-7-30下午02:07:38
	 * @param svos
	 */
	private void setSpaceAllon(SuperVO[] svos) throws Exception{
		if(svos==null ||svos.length==0){
			return;
		}
		int size=svos.length;
		for(int i=0;i<size;i++){
			TbOutgeneralBVO vo=(TbOutgeneralBVO) svos[i];
		   if(vo.getTrayInfor()==null)
			   throw new Exception("没有分配货位");		   
		   List<TbOutgeneralTVO>  trayInfor=vo.getTrayInfor();
		   if(trayInfor==null || trayInfor.size()==0)
			   throw new Exception("没有分配货位");
		   TbOutgeneralTVO tvo=trayInfor.get(i);
		    tvo.setNoutnum(PuPubVO.getUFDouble_NullAsZero(vo.getAttributeValue("noutnum")));
		    tvo.setNoutassistnum(PuPubVO.getUFDouble_NullAsZero(vo.getAttributeValue("noutassistnum")));
		    trayInfor.clear();
		    trayInfor.add(tvo);
		    vo.setTrayInfor(trayInfor);  
		}	
	}
	public void pushSign4I(String date, AggregatedValueObject billvo) throws Exception {
		// 其它出库签字
		if(billvo != null && billvo instanceof GeneralBillVO){
			GeneralBillVO billVO = (GeneralBillVO)billvo;
			IPFBusiAction bsBusiAction = (IPFBusiAction) NCLocator.getInstance().lookup(IPFBusiAction.class.getName());
			ArrayList retList = (ArrayList)bsBusiAction.processAction("SAVE", s_billtype,date,null,billVO, null,null);
			SMGeneralBillVO smbillvo = (SMGeneralBillVO) retList.get(2);
			billVO.setSmallBillVO(smbillvo);
			//签字检查 <->[签字日期和表体业务日期]
			//当前操作人<->[业务加锁，锁定当前操作人员]
			//空货位检查 bb1表
			bsBusiAction.processAction("SIGN", s_billtype,date,null,billVO, null,null);
		}
		
	}
	
	public void canelPushSign4I(String date, AggregatedValueObject[] billvo) throws Exception {
		//取消其它出库签字
		if(billvo != null && billvo[0]!= null && billvo[0] instanceof GeneralBillVO){
			IPFBusiAction bsBusiAction = (IPFBusiAction) NCLocator.getInstance().lookup(IPFBusiAction.class.getName());
			for(int i = 0 ;i < billvo.length;i++){
				ArrayList retList = (ArrayList)bsBusiAction.processAction("CANCELSIGN", s_billtype,date,null,billvo[i], null,null);
				if(retList.get(0) !=null && (Boolean)retList.get(0)){//取消签字成功
					String sql = "select ts from ic_general_h where cgeneralhid = '"
						+ billvo[i].getParentVO().getPrimaryKey() + "'";
				String ts = PuPubVO.getString_TrimZeroLenAsNull(getBaseDAO()
						.executeQuery(sql, WdsPubResulSetProcesser.COLUMNPROCESSOR));
				billvo[i].getParentVO().setAttributeValue("ts", new UFDateTime(ts));
					bsBusiAction.processAction("DELETE", s_billtype,date,null,billvo[i], null,null);//执行删除
				}
			}
		}
	}
}