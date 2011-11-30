package nc.bs.wds.ic.other.out;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.NamingException;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.framework.common.NCLocator;
import nc.bs.pub.SuperDMO;
import nc.bs.wds.pub.report.ReportDMO;
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
import nc.vo.pub.VOStatus;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.scm.pub.DynamicVO;
import nc.vo.wl.pub.CombinVO;
import nc.vo.wl.pub.IUFTypes;
import nc.vo.wl.pub.report.ReportBaseVO;
/**
 * ��������(WDS8)
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
     * @���ߣ�mlr
     * @˵�������ɽ������Ŀ 
     *      	���������γɹ�Ӧ������������ʱ ���尴 ��������κ� ���кϲ�  
                ����Ƿ��д�����α�ѡ�еĻ� �����д�� �� ���κŵĺϲ�
                ���û��ѡ�У�������ϲ� ��д 2009 ����

     * @ʱ�䣺2011-7-30����10:13:16
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
		 CircularlyAccessibleValueObject[] vos=billvo.getChildrenVO();
		 CircularlyAccessibleValueObject[] svos=CombinVO.combinVoByFields(vos,new String[]{"cinventoryid"},new int[]{IUFTypes.UFD,IUFTypes.UFD,IUFTypes.UFD,IUFTypes.UFD,IUFTypes.UFD,IUFTypes.UFD,IUFTypes.UFD},new String[]{"noutnum","noutassistnum","nacceptnum","nassacceptnum","nshouldoutnum","nshouldoutassistnum","ntagnum"});
		 setSpaceAllon(svos);
		 billvo.setChildrenVO(svos);
		 return billvo;				
	}
	/**
	 * 
	 * @���ߣ�mlr
	 * @˵�������ɽ������Ŀ 
	 *        ��Ҫ����ı��� �Ĵ�����л�λ ����
	 * @ʱ�䣺2011-7-30����02:07:38
	 * @param svos
	 */
	private void setSpaceAllon(CircularlyAccessibleValueObject[] svos) throws Exception{
		if(svos==null ||svos.length==0){
			return;
		}
		int size=svos.length;
		for(int i=0;i<size;i++){
			TbOutgeneralBVO vo=(TbOutgeneralBVO) svos[i];
		   if(vo.getTrayInfor()==null)
			   throw new Exception("û�з����λ");		   
		   List<TbOutgeneralTVO>  trayInfor=vo.getTrayInfor();
		   if(trayInfor==null || trayInfor.size()==0)
			   throw new Exception("û�з����λ");
		   TbOutgeneralTVO tvo=trayInfor.get(0);
		    tvo.setNoutnum(PuPubVO.getUFDouble_NullAsZero(vo.getAttributeValue("noutnum")));
		    tvo.setNoutassistnum(PuPubVO.getUFDouble_NullAsZero(vo.getAttributeValue("noutassistnum")));
		    trayInfor.clear();
		    trayInfor.add(tvo);
		    vo.setTrayInfor(trayInfor);  
		}	
	}
	public void pushSign4I(String date, AggregatedValueObject billvo) throws Exception {
		// ��������ǩ��
		if(billvo != null && billvo instanceof GeneralBillVO){
			GeneralBillVO billVO = (GeneralBillVO)billvo;
			IPFBusiAction bsBusiAction = (IPFBusiAction) NCLocator.getInstance().lookup(IPFBusiAction.class.getName());
			ArrayList retList = (ArrayList)bsBusiAction.processAction("SAVE", s_billtype,date,null,billVO, null,null);
			SMGeneralBillVO smbillvo = (SMGeneralBillVO) retList.get(2);
			billVO.setSmallBillVO(smbillvo);
			//ǩ�ּ�� <->[ǩ�����ںͱ���ҵ������]
			//��ǰ������<->[ҵ�������������ǰ������Ա]
			//�ջ�λ��� bb1��
			bsBusiAction.processAction("SIGN", s_billtype,date,null,billVO, null,null);
		}
		
	}
	
	public void canelPushSign4I(String date, AggregatedValueObject[] billvo) throws Exception {
		//ȡ����������ǩ��
		if(billvo != null && billvo[0]!= null && billvo[0] instanceof GeneralBillVO){
			IPFBusiAction bsBusiAction = (IPFBusiAction) NCLocator.getInstance().lookup(IPFBusiAction.class.getName());
			for(int i = 0 ;i < billvo.length;i++){
				ArrayList retList = (ArrayList)bsBusiAction.processAction("CANCELSIGN", s_billtype,date,null,billvo[i], null,null);
				if(retList.get(0) !=null && (Boolean)retList.get(0)){//ȡ��ǩ�ֳɹ�
					String sql = "select ts from ic_general_h where cgeneralhid = '"
						+ billvo[i].getParentVO().getPrimaryKey() + "'";
				String ts = PuPubVO.getString_TrimZeroLenAsNull(getBaseDAO()
						.executeQuery(sql, WdsPubResulSetProcesser.COLUMNPROCESSOR));
				billvo[i].getParentVO().setAttributeValue("ts", new UFDateTime(ts));
					bsBusiAction.processAction("DELETE", s_billtype,date,null,billvo[i], null,null);//ִ��ɾ��
				}
			}
		}
	}
	
	//֧�����̴�ӡ
	public  ReportBaseVO[] getCorpTP(String general) throws DAOException,
	  SQLException, IOException, NamingException {
         if (general == null || "".equalsIgnoreCase(general)) {
	          return null;
           }
         StringBuffer sql = new StringBuffer();
         sql.append(" select tb_outgeneral_t.*,bd_cargdoc_tray.*  ");//����ⵥ�� ��
         sql.append(" from tb_outgeneral_t ");
         sql.append(" join bd_cargdoc_tray ");//����������Ϣ
         sql.append(" on tb_outgeneral_t.cdt_pk = bd_cargdoc_tray.cdt_pk");
         sql.append(" where  general_b_pk='" + general + "'");
         sql.append(" and isnull(tb_outgeneral_t.dr,0)=0  and isnull(bd_cargdoc_tray.dr,0)=0");
         ReportBaseVO[]  vos = new ReportDMO().queryVOBySql(sql.toString());
        if(vos==null||vos.length==0){
        	return null;
        }
         return vos;
      }
	
}