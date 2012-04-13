package nc.bs.wds.ic.other.in;

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
import nc.vo.ic.pub.TbGeneralBBVO;
import nc.vo.ic.pub.TbGeneralBVO;
import nc.vo.ic.pub.TbGeneralHVO;
import nc.vo.ic.pub.bill.GeneralBillVO;
import nc.vo.ic.pub.smallbill.SMGeneralBillVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.VOStatus;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.zmpub.pub.report.IUFTypes;
import nc.vo.zmpub.pub.report.ReportBaseVO;
import nc.vo.zmpub.pub.report2.CombinVO;
/**
 * �������(WDS7)
 * @author Administrator
 */
public class OtherInBO  {
	
	private String s_billtype = "4A";
	
	private SuperDMO dmo = new SuperDMO();
	private BaseDAO dao = null;

	BaseDAO getBaseDAO() {
		if (dao == null) {
			dao = new BaseDAO();
		}
		return dao;
	}
	
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
	/**
     * 
     * @���ߣ�mlr
     * @˵�������ɽ������Ŀ 
     *      	��������γɹ�Ӧ������������ʱ ���尴 ��������κ� ���кϲ�  
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
		 UFBoolean isVbanchCode=PuPubVO.getUFBoolean_NullAs(hvo.getAttributeValue("fisnewcode"), new UFBoolean(false));
		 if(isVbanchCode.booleanValue()==true){
			 return billvo;
		 }
		 if(billvo.getChildrenVO()==null){
			 return billvo;
		 }
		 CircularlyAccessibleValueObject[] vos= billvo.getChildrenVO();
		 CircularlyAccessibleValueObject[] svos=CombinVO.combinVoByFields(vos,new String[]{"geb_cinventoryid"},new int[]{IUFTypes.UFD,IUFTypes.UFD,IUFTypes.UFD,IUFTypes.UFD},new String[]{"geb_snum","geb_bsnum","geb_anum","geb_banum"});		
	   	  setSpaceAllon(svos);
		 billvo.setChildrenVO(svos);
		 return billvo;				
	}
	/**
	 * 
	 * @���ߣ�mlr
	 * @˵�������ɽ������Ŀ 
	 *        ��Ҫ���ı��� �Ĵ�����л�λ ����
	 * @ʱ�䣺2011-7-30����02:07:38
	 * @param svos
	 */
	private void setSpaceAllon(CircularlyAccessibleValueObject[] svos) throws Exception{
		if(svos==null ||svos.length==0){
			return;
		}
		int size=svos.length;
		for(int i=0;i<size;i++){
			TbGeneralBVO vo=(TbGeneralBVO) svos[i];
		   if(vo.getTrayInfor()==null)
			   throw new Exception("û�з����λ");		   
		   List<TbGeneralBBVO>  trayInfor=vo.getTrayInfor();
		   if(trayInfor==null || trayInfor.size()==0)
			   throw new Exception("û�з����λ");
		   TbGeneralBBVO tvo=trayInfor.get(0);
		   tvo.setGebb_num(PuPubVO.getUFDouble_NullAsZero(vo.getAttributeValue("geb_anum")));
		   tvo.setNinassistnum(PuPubVO.getUFDouble_NullAsZero(vo.getAttributeValue("geb_banum")));
		   trayInfor.clear();
		   trayInfor.add(tvo);
		   vo.setTrayInfor(trayInfor);  
		}	
	}	
	public void pushSign4A(String date, AggregatedValueObject billvo) throws Exception {
		// �������ǩ��
		if(billvo != null && billvo instanceof GeneralBillVO){//PUSHSAVESIGN��ʽ���桢�Զ�ǩ�� �����ڷֵ���� �����ڻ�ȥ��ѯ�ƻ��ۡ�
			GeneralBillVO billVO = (GeneralBillVO)billvo;
			IPFBusiAction bsBusiAction = (IPFBusiAction) NCLocator.getInstance().lookup(IPFBusiAction.class.getName());
			ArrayList retList = (ArrayList)bsBusiAction.processAction("SAVE", s_billtype,date,null,billVO, null,null);
			SMGeneralBillVO smbillvo = (SMGeneralBillVO) retList.get(2);
			billVO.setSmallBillVO(smbillvo);
			//ǩ�ּ�� <->[ǩ�����ںͱ���ҵ������]
			//��ǰ������<->[ҵ�������������ǰ������Ա]
			//�ջ�λ��� bb1��
			bsBusiAction.processAction("SIGN", s_billtype,date,null,billVO, null,null); //ǩ�ֺ����ſ�
		}
		
	}
	
	public void canelPushSign4A(String date, AggregatedValueObject[] billvo) throws Exception {
		//ȡ���������ǩ��
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
         sql.append(" select tb_general_b_b.*,bd_cargdoc_tray.*  ");//����ⵥ�� ��
         sql.append(" from tb_general_b_b ");
         sql.append(" join bd_cargdoc_tray ");//����������Ϣ
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