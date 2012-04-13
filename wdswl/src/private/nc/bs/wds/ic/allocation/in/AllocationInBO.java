package nc.bs.wds.ic.allocation.in;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.naming.NamingException;

import nc.bs.dao.DAOException;
import nc.bs.framework.common.NCLocator;
import nc.bs.pub.SuperDMO;
import nc.bs.wds.pub.report.ReportDMO;
import nc.itf.uap.pf.IPFBusiAction;
import nc.vo.ic.pub.TbGeneralHVO;
import nc.vo.ic.pub.bill.GeneralBillVO;
import nc.vo.ic.pub.smallbill.SMGeneralBillVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.zmpub.pub.report.ReportBaseVO;
/**
 * 调拨入库
 * @author Administrator
 */
public class AllocationInBO  {
	
	private String s_billtype = "4E";
	
	private SuperDMO dmo = new SuperDMO();
	
	public void updateHVO(TbGeneralHVO hvo) throws BusinessException{
		if(hvo == null){
			return;
		}
		if(hvo.getPrimaryKey() != null)
			hvo.setStatus(VOStatus.UPDATED);
		else
			hvo.setStatus(VOStatus.NEW);
		dmo.update(hvo);
	}

	
	public void pushSign4E(String date, AggregatedValueObject billvo) throws Exception {
		// 调拨入库签字
		if(billvo != null && billvo instanceof GeneralBillVO){//PUSHSAVESIGN推式保存、自动签字 ，存在分单情况 ，存在会去查询计划价。
			GeneralBillVO billVO = (GeneralBillVO)billvo;
			IPFBusiAction bsBusiAction = (IPFBusiAction) NCLocator.getInstance().lookup(IPFBusiAction.class.getName());
			ArrayList retList = (ArrayList)bsBusiAction.processAction("SAVE", s_billtype,date,null,billVO, null,null);
			SMGeneralBillVO smbillvo = (SMGeneralBillVO) retList.get(2);
			billVO.setSmallBillVO(smbillvo);
			//签字检查 <->[签字日期和表体业务日期]
			//当前操作人<->[业务加锁，锁定当前操作人员]
			//空货位检查 bb1表
			bsBusiAction.processAction("SIGN", s_billtype,date,null,billVO, null,null); //签字后续放开
		}
		
	}
	
	public void canelPushSign4E(String date, AggregatedValueObject[] billvo) throws Exception {
		//取消调拨入库签字
		if(billvo != null && billvo[0]!= null && billvo[0] instanceof GeneralBillVO){
			IPFBusiAction bsBusiAction = (IPFBusiAction) NCLocator.getInstance().lookup(IPFBusiAction.class.getName());
			for(int i = 0 ;i < billvo.length;i++){
			ArrayList retList = (ArrayList)bsBusiAction.processAction("CANCELSIGN", s_billtype,date,null,billvo[i], null,null);
				if(retList.get(0) !=null && (Boolean)retList.get(0)){//取消签字成功
					bsBusiAction.processAction("DELETE", s_billtype,date,null,billvo[i], null,null);//执行删除
				}
			}
		}
	}
	

	//支持托盘打印
	public  ReportBaseVO[] getCorpTP(String general) throws DAOException,
	  SQLException, IOException, NamingException {
         if (general == null || "".equalsIgnoreCase(general)) {
	          return null;
           }
         StringBuffer sql = new StringBuffer();
         sql.append(" select tb_general_b_b.*,bd_cargdoc_tray.*  ");//出入库单孙 表
         sql.append(" from tb_general_b_b ");
         sql.append(" join bd_cargdoc_tray ");//货物托盘信息
         sql.append(" on tb_general_b_b.cdt_pk = bd_cargdoc_tray.cdt_pk");
         sql.append(" where  geb_pk='" + general + "'");         
         sql.append(" and isnull(tb_general_b_b.dr,0)=0  and isnull(bd_cargdoc_tray.dr,0)=0");         
         ReportBaseVO[]  vos = new ReportDMO().queryVOBySql(sql.toString());
        if(vos==null||vos.length==0){
        	return null;
        }
         return vos;
      }
}