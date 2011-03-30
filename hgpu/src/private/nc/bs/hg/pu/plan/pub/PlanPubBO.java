package nc.bs.hg.pu.plan.pub;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import nc.bd.accperiod.AccountCalendar;
import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.framework.common.NCLocator;
import nc.bs.hg.pu.plan.year.YearPlanBO;
import nc.bs.hg.pu.pub.HgBsPubTool;
import nc.bs.trade.comsave.BillSave;
import nc.itf.scm.cenpur.service.TempTableUtil;
import nc.jdbc.framework.SQLParameter;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.jdbc.framework.processor.BeanProcessor;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.jdbc.framework.processor.ResultSetProcessor;
import nc.jdbc.framework.util.SQLHelper;
import nc.ui.pub.ClientEnvironment;
import nc.vo.hg.pu.pact.PactItemVO;
import nc.vo.hg.pu.plan.deal.PlanDealVO;
import nc.vo.hg.pu.plan.detail.PlanBBVO;
import nc.vo.hg.pu.plan.year.PlanYearBVO;
import nc.vo.hg.pu.pub.HgPubConst;
import nc.vo.hg.pu.pub.HgPubTool;
import nc.vo.hg.pu.pub.HgYearExcelFileVO;
import nc.vo.hg.pu.pub.PlanApplyInforVO;
import nc.vo.hg.pu.pub.PlanBVO;
import nc.vo.hg.pu.pub.PlanVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
import nc.vo.pub.VOStatus;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.trade.pub.HYBillVO;

/**
 * 
 * @author zhf
 *�ƻ����� ��̨����ҵ������
 */
public class PlanPubBO {	
	String errorlog = "";
	private TempTableUtil tempTaBo = null;
	private BaseDAO dao = null;
	private BaseDAO getBaseDao(){
		if(dao == null){
			dao = new BaseDAO();
		}
		return dao;
	}
	public TempTableUtil getTempTaBO(){
		if(tempTaBo == null){
			tempTaBo = new TempTableUtil();
		}
		return tempTaBo;
	}
	

	private BillSave savebo = null;
	private BillSave getSaveBo(){
		if(savebo==null){
			savebo = new BillSave();
		}
		return savebo;
	}
	
	private YearPlanBO yearplanbo = null;
	private YearPlanBO getYearPlanBO(){
		if(yearplanbo == null)
			return new YearPlanBO();
		return yearplanbo;
	}
	
	public String getSubSql(String[] saID) throws BusinessException{
		return getTempTaBO().getSubSql(saID);
	}
	
	public Object executeQuery(String sql, ResultSetProcessor processor) throws DAOException {
		return getBaseDao().executeQuery(sql, processor);
	}
	public Object executeQuery(String sql, SQLParameter parameter, ResultSetProcessor processor) throws DAOException {
		return getBaseDao().executeQuery(sql, parameter, processor);
	}
	public int executeUpdate(String sql, SQLParameter parameter) throws DAOException {
		return getBaseDao().executeUpdate(sql, parameter);
	}
	public int executeUpdate(String sql) throws DAOException {
		return getBaseDao().executeUpdate(sql);
	}	
	
	public String transIDs(String tarcorp,String sourceValue,String tablename,String basFiledName,String manFiledName,String basdocName) throws Exception {
		StringBuffer sqlb = new StringBuffer();
		sqlb.append("select ");
		sqlb.append(basFiledName);
		sqlb.append(" from ");
		sqlb.append(tablename);
		sqlb.append(" where ");
		sqlb.append(manFiledName);
		sqlb.append(" = '");
		sqlb.append(sourceValue);
		sqlb.append("'");
		Object o = executeQuery(sqlb.toString(), HgBsPubTool.COLUMNPROCESSOR);
		if(o==null){
			throw new SQLException("��ѯ"+basdocName+"�����쳣");
		}
		sqlb = new StringBuffer();
		sqlb.append("select ");
		sqlb.append(manFiledName);
		sqlb.append(" from ");
		sqlb.append(tablename);
		sqlb.append(" where ");
		sqlb.append(basFiledName);
		sqlb.append(" = '");
		sqlb.append(PuPubVO.getString_TrimZeroLenAsNull(o));
		sqlb.append("' and pk_corp = '"+tarcorp+"'");
		o = executeQuery(sqlb.toString(), HgBsPubTool.COLUMNPROCESSOR);
		if(o==null){
			throw new SQLException(basdocName+"�繫˾ת���쳣��δ���䵽Ŀ�깫˾"+sqlb.toString());
		}
		
		return PuPubVO.getString_TrimZeroLenAsNull(o);
	}
	
	

	//��ȡϵͳ���ɹ�˾����  ��Ϊ���ɹ�˾ֻ��һ��    �Ȼ�Դ�嵥�����еĶ���ɹ���λ
	public String getGatherPoCorp() throws BusinessException{		
		String sql = " select distinct cpurcorp from scm_invsourcelist where isnull(dr,0)=0 and cpurcorp is not null";
		return PuPubVO.getString_TrimZeroLenAsNull(getBaseDao().executeQuery(sql,  HgBsPubTool.COLUMNPROCESSOR));
		//		return null;
	}

	public PlanApplyInforVO getPlanAppInfor(String sLogCorp,String sLogUser) throws BusinessException{
		PlanApplyInforVO infor = null;		

		//������ ���벿��  ������֯   
		String sql =
			" select psn.pk_psndoc capplypsnid,psn.pk_deptdoc capplydeptid,dep.pk_calbody creqcalbodyid,dep.pk_fathedept csupplydeptid" +
			" from bd_psndoc psn inner join sm_UserAndClerk use on use.pk_psndoc = psn.pk_psnbasdoc"+
			" inner join bd_deptdoc dep on dep.pk_deptdoc = psn.pk_deptdoc"+
			" where use.userid = '"+sLogUser+"' and psn.pk_corp = '"+sLogCorp+"'";

		Object o = getBaseDao().executeQuery(sql, new BeanProcessor(PlanApplyInforVO.class));
		if(o == null){
			infor = new PlanApplyInforVO();		
			infor.setM_sLogCorp(sLogCorp);
			infor.setM_sLogUser(sLogUser);
			infor.setM_pocorp(getGatherPoCorp());//ϵͳ�ɹ���˾
			return infor;
		}
//			return null;
		//		��½��Ϣ
		infor = (PlanApplyInforVO)o;
		infor.setM_sLogCorp(sLogCorp);
		infor.setM_sLogUser(sLogUser);
		infor.setM_pocorp(getGatherPoCorp());//ϵͳ�ɹ���˾

		//		 ������λ   ��������   ������֯
		if(!infor.getM_pocorp().equalsIgnoreCase(sLogCorp)){// ��ǰ��˾���Ǽ��ɹ�˾
			if(PuPubVO.getString_TrimZeroLenAsNull(infor.getCsupplydeptid())==null){//��ǰ����Ϊ�ɹ�����
				infor.setCsupplycorpid(infor.getM_pocorp());
				//����  �� ��֯  ����   �ȴ� ʵʩ����
				//��Ӧ��ֻ��һ����֯Ĭ�ϴ���
				String calbody = PuPubVO.getString_TrimZeroLenAsNull(HgBsPubTool.execFomular("pk_calbody->getColValue(bd_calbody,pk_calbody,pk_corp,corpid)", new String[]{"corpid"}, new String[]{infor.getM_pocorp()}));
				if(calbody != null)
					infor.setCsupplycalbodyid(calbody);
			}else{
				sql = "select pk_corp,pk_calbody from bd_deptdoc where pk_deptdoc = '"+infor.getCsupplydeptid()+"'";
				o = getBaseDao().executeQuery(sql, HgBsPubTool.ARRAYPROCESSOR);
				if(o==null)
					throw new BusinessException("��̨���ݲ�ѯ�쳣����ȡ���ŵ�����Ϣ����");
				Object[] os = (Object[])o;
				infor.setCsupplycorpid(PuPubVO.getString_TrimZeroLenAsNull(os[0]));
				infor.setCsupplycalbodyid(PuPubVO.getString_TrimZeroLenAsNull(os[1]));
			}
		}
		return infor;
	}	
	
	/**
	 * ������ƻ�ʱ  �Ƿ��ͷ�����ж�
	 * @param vo
	 * @return
	 */
	private boolean isHead(HgYearExcelFileVO vo){
		return vo.getDbilldate() ==null && vo.getPk_corp() ==null  && vo.getCapplydeptid() ==null  
		&& vo.getCapplypsnid() ==null && vo.getHcreqcalbodyid() ==null && vo.getFisself() ==null 
		&& vo.getCsupplycorpid() ==null && vo.getCsupplydeptid() ==null 
		&& vo.getCyear() ==null  &&	vo.getHvmemo() ==null ;

	}
	
	private PlanVO getHeadVoForImport(HgYearExcelFileVO vo,String userid,int index) throws BusinessException{
		PlanVO headvo = new PlanVO();
		//��������
		if(vo.getDbilldate() != null)
			headvo.setDbilldate(new UFDate(vo.getDbilldate()));
		else 
			headvo.setDbilldate(new UFDate(System.currentTimeMillis()));
		
		Object pk_corp = null;
		//���뵥λ
		if(vo.getPk_corp() != null){
			pk_corp = querypkall("pk_corp","bd_corp","unitcode",vo.getPk_corp(),"");
			if(PuPubVO.getString_TrimZeroLenAsNull(pk_corp)==null)
				errorlog += "��˾"+vo.getPk_corp()+"������\n";
			else
				headvo.setPk_corp(pk_corp.toString());
			//��ѯ�ò��Ÿù�˾�Ƿ��иò���Ա,���û��,����ʾ����
			String sql = "select cuserid from sm_user where dr = 0 and cuserid='"+userid+
			"'and pk_corp = '"+pk_corp+"'";
			Object cuserid = getBaseDao().executeQuery(sql, HgBsPubTool.COLUMNPROCESSOR);
			if(PuPubVO.getString_TrimZeroLenAsNull(cuserid)==null)
				errorlog += "��"+index+"��,��ǰ�û��뵼�빫˾��ƥ��!\n";
		}else{
			errorlog += "��"+index+"��,���뵥λ����Ϊ��!";
		}
		
		//���벿��
		if(vo.getCapplydeptid() != null){
			Object pk_deptdoc = querypkall("pk_deptdoc","bd_deptdoc","deptcode",vo.getCapplydeptid(),pk_corp.toString());
			if(PuPubVO.getString_TrimZeroLenAsNull(pk_deptdoc)==null)
				errorlog += "��"+index+"��,����"+vo.getCapplydeptid()+"������\n";
			else
				headvo.setCapplydeptid(pk_deptdoc.toString());
		}else
			errorlog += "��"+index+"��,���벿�Ų���Ϊ��!\n";
		
		
		//������
		if(vo.getCapplypsnid() != null){
			Object pk_psndoc = querypkall("pk_psndoc","bd_psndoc","psncode",vo.getCapplypsnid(),"");
			if(PuPubVO.getString_TrimZeroLenAsNull(pk_psndoc)==null)
				errorlog += "��"+index+"��,������"+vo.getCapplypsnid()+"������\n";
			else
				headvo.setCapplypsnid(pk_psndoc.toString());
		}else
			errorlog += "��"+index+"��,�����˲���Ϊ��!\n";
		
		//������֯
		if(vo.getHcreqcalbodyid() != null){
			Object pk_calbody  = querypkall("pk_calbody","bd_calbody","bodycode",vo.getHcreqcalbodyid(),"");
			if(PuPubVO.getString_TrimZeroLenAsNull(pk_calbody)==null)
				errorlog += "��"+index+"��,������֯"+vo.getHcreqcalbodyid()+"������\n";
			else
				headvo.setCreqcalbodyid(pk_calbody.toString());
		}
		//�Ƿ�����
		headvo.setFisself(new UFBoolean(false));
		//������λ
		if(vo.getCsupplycorpid() != null){
			Object csupplycorpid = querypkall("pk_corp","bd_corp","unitcode",vo.getCsupplycorpid(),"Y");
			if(csupplycorpid !=null)
				headvo.setCsupplycorpid(csupplycorpid.toString());
		}
		//��������
		if(vo.getCsupplydeptid() != null){
			Object csupplydeptid = querypkall("pk_deptdoc","bd_deptdoc","deptcode",vo.getCsupplydeptid(),"Y");
			if(csupplydeptid !=null)
				headvo.setCsupplydeptid(csupplydeptid.toString());
		}
		//���
		if(vo.getCyear() != null)
			headvo.setCyear(vo.getCyear().trim());
		//��ע
		if(vo.getHvmemo() != null)
		headvo.setVmemo(vo.getHvmemo());
		//����״̬��Ϊ����,���ں��洦��У��
		headvo.setVbillstatus(HgPubConst.PLAN_YEAR_IMPORT_BILLSTATUS);
		headvo.setPk_billtype(HgPubConst.PLAN_YEAR_BILLTYPE);
		headvo.setCaccperiodschemeid("0001AA00000000000001");
		headvo.setDmakedate(new UFDate(vo.getDbilldate()));
		headvo.setDapprovedate(new UFDate(vo.getDbilldate()));
		headvo.setVoperatorid(userid);
		headvo.setVapproveid(userid);
		return headvo;
	}
	
	private PlanYearBVO getBodyVoForImport(HgYearExcelFileVO vo,int index) throws BusinessException{
		PlanYearBVO bvo = new PlanYearBVO();
		//�������
		if(vo.getInvcode() != null){
			String sql = "select man.pk_invmandoc, man.pk_invbasdoc from bd_invmandoc man,bd_invbasdoc doc where man.pk_invbasdoc = doc.pk_invbasdoc and man.dr =0 and" +
			" doc.dr=0 and doc.invcode = '"+vo.getInvcode()+"' and man.pk_corp = '"+SQLHelper.getCorpPk()+"'";
			Object list = getBaseDao().executeQuery(sql, HgBsPubTool.ARRAYLISTPROCESSOR);
			if(list!=null ){
				ArrayList ar = (ArrayList)list;
				if(ar.size() == 0)
					errorlog += "��"+index+"��,����:"+vo.getInvcode()+",δ�ҵ��ô��\n";
				else{
					Object[] ar1 = (Object[])ar.get(0);
					bvo.setCinventoryid(ar1[0].toString());
					bvo.setPk_invbasdoc(ar1[1].toString());
				}
			}
		}else{
			errorlog += "��"+index+"��,���ڴ������Ϊ�յ�����\n";
		}
		//����������֯
		if(vo.getBcreqcalbodyid() != null){
			Object bpk_calbody  = querypkall("pk_calbody","bd_calbody","bodycode",vo.getBcreqcalbodyid(),"");
			if(PuPubVO.getString_TrimZeroLenAsNull(bpk_calbody)==null)
				errorlog += "��"+index+"��,����������֯"+vo.getBcreqcalbodyid()+"������\n";
			else
				bvo.setCreqcalbodyid(bpk_calbody.toString());
		}
		//����ֿ�
		if(vo.getCreqwarehouseid() != null){
			Object pk_stordoc  = querypkall("pk_stordoc","bd_stordoc","storcode",vo.getCreqwarehouseid(),"");
			if(PuPubVO.getString_TrimZeroLenAsNull(pk_stordoc)==null)
				errorlog += "��"+index+"��,����ֿ�"+vo.getCreqwarehouseid()+"������\n";
			else
				bvo.setCreqwarehouseid(pk_stordoc.toString());
		}
		//������֯
		if(vo.getCsupplycalbodyid() != null){
			Object csupplycalbodyid  = querypkall("pk_calbody","bd_calbody","bodycode",vo.getCsupplycalbodyid(),"Y");
			if(PuPubVO.getString_TrimZeroLenAsNull(csupplycalbodyid)==null)
				errorlog += "��"+index+"��,������֯"+vo.getCsupplycalbodyid()+"������\n";
			else
				bvo.setCsupplycalbodyid(csupplycalbodyid.toString());
		}
		//�����ֿ�
		if(vo.getCsupplywarehouseid() != null){
			Object csupplywarehouseid  = querypkall("pk_stordoc","bd_stordoc","storcode",vo.getCsupplywarehouseid(),"Y");
			if(PuPubVO.getString_TrimZeroLenAsNull(csupplywarehouseid)!=null)
				bvo.setCsupplywarehouseid(csupplywarehouseid.toString());
		}
		//����λ
		if(vo.getPk_measdoc() != null){
			Object pk_measdoc  = querypkall("pk_measdoc","bd_measdoc","shortname",vo.getPk_measdoc(),"Y");
			if(pk_measdoc !=null)
				bvo.setPk_measdoc(pk_measdoc.toString());
		}
		//ë����
		if(vo.getNnum() != null)
			bvo.setNnum(new UFDouble(vo.getNnum()));
		//����������
		if(vo.getNnetnum() != null)
			bvo.setNnetnum(new UFDouble(vo.getNnetnum()));
		//�ƻ�����
		if(vo.getNprice() != null)
			bvo.setNprice(new UFDouble(vo.getNprice()));
		//�ƻ����
		if(vo.getNmny() != null)
			bvo.setNmny(new UFDouble(vo.getNmny()));
		//��ע
		bvo.setVmemo(vo.getBvmemo());
		if(vo.getNmonnum1() != null)
			bvo.setNmonnum1(new UFDouble(vo.getNmonnum1()));
		if(vo.getNmonnum2() != null)
			bvo.setNmonnum2(new UFDouble(vo.getNmonnum2()));
		if(vo.getNmonnum3() != null)
			bvo.setNmonnum3(new UFDouble(vo.getNmonnum3()));
		if(vo.getNmonnum4() != null)
			bvo.setNmonnum4(new UFDouble(vo.getNmonnum4()));
		if(vo.getNmonnum5() != null)
			bvo.setNmonnum5(new UFDouble(vo.getNmonnum5()));
		if(vo.getNmonnum6() != null)
			bvo.setNmonnum6(new UFDouble(vo.getNmonnum6()));
		if(vo.getNmonnum7() != null)
			bvo.setNmonnum7(new UFDouble(vo.getNmonnum7()));
		if(vo.getNmonnum8() != null)
			bvo.setNmonnum8(new UFDouble(vo.getNmonnum8()));
		if(vo.getNmonnum9() != null)
			bvo.setNmonnum9(new UFDouble(vo.getNmonnum9()));
		if(vo.getNmonnum10() != null)
			bvo.setNmonnum10(new UFDouble(vo.getNmonnum10()));
		if(vo.getNmonnum11() != null)
			bvo.setNmonnum11(new UFDouble(vo.getNmonnum11()));
		if(vo.getNmonnum12() != null)
			bvo.setNmonnum12(new UFDouble(vo.getNmonnum12()));
		return bvo;
	}
	
	/**
	 * liuys ������ƻ�����ת��vo
	 * @throws DAOException 
	 */
	public void vHgChangeVo(nc.vo.hg.pu.pub.HgYearExcelFileVO[] vos,String userid) throws Exception{
		
		if(vos==null||vos.length==0){
			throw  new BusinessException("���ݼ����쳣,�����²���");
		}

		Map<Integer,List<SuperVO>> dataMap = new HashMap<Integer, List<SuperVO>>();		
		List<SuperVO> tmpList = null;		
		int index = 0;		
		PlanVO headvo = null;
		PlanYearBVO bvo  = null;

		for(int x=0;x<vos.length;x++){	
			if(vos[x] == null)
				continue;
			if(!isHead(vos[x])){
				index++;				
				headvo = getHeadVoForImport(vos[x],userid,x+1);
				bvo = getBodyVoForImport(vos[x],x+1);
			}else{
				bvo = getBodyVoForImport(vos[x],x+1);
			}

			if(dataMap.containsKey(index)){
				tmpList = dataMap.get(index);
			}else{
				tmpList = new ArrayList<SuperVO>();
				tmpList.add(headvo);
			}
			tmpList.add(bvo);
			dataMap.put(index, tmpList);
		}
		if(!errorlog.equals("")||errorlog.length() !=0)
			throw new BusinessException(errorlog);
		if(dataMap.size()<=0)
			throw new BusinessException("����ת���쳣,ת��������Ϊ��");

		List<HYBillVO> lbillvo = new ArrayList<HYBillVO>();	

		HYBillVO tmpbillvo = null;
		for(List<SuperVO> l:dataMap.values()){
			if(l.size()<=1){
				throw new BusinessException("���������쳣,�����ޱ���ƻ�");
			}
			tmpbillvo = new HYBillVO();
			tmpbillvo.setParentVO(l.get(0));
			l.remove(0);
			tmpbillvo.setChildrenVO(l.toArray(new SuperVO[0]));			
			lbillvo.add(tmpbillvo);
		}

		if(lbillvo.size()==0){
			throw new BusinessException("����ת���쳣,ת��������Ϊ��");
		}

		HYBillVO[] newbills = (HYBillVO[])pushSavePlans(lbillvo.toArray(new HYBillVO[0]));

		for(HYBillVO newbill:newbills){
			getYearPlanBO().splitYearPlan2MonthPlan(newbill, SQLHelper.getCorpPk());
		}		
	}
	
	
	/**
	 * liuys
	 * ���ݸ��ֵ��������ѯ��pKֵ
	 * @param tablename
	 * @param selectname
	 * @param passvalue
	 * @return
	 * @throws DAOException
	 */
	public Object querypkall(String selectname,String tablename,String codename,String passvalue,String bag)throws DAOException{
		String sqlpkcorp = "select "+selectname+" from "+tablename+" where dr = 0 and "+codename+" = '"+passvalue;
		if(bag.equals("Y"))
			sqlpkcorp+="';";
		else if(bag.length()>2){
			sqlpkcorp += "' and pk_corp = '"+bag+"'";
		}else
			sqlpkcorp+="' and pk_corp = '"+SQLHelper.getCorpPk()+"'";
		Object obj = getBaseDao().executeQuery(sqlpkcorp, HgBsPubTool.COLUMNPROCESSOR);
		return obj;
	}
	
	public Object pushSavePlans(HYBillVO[] billvos) throws BusinessException{
		if(billvos == null||billvos.length ==0)
			return null;		
		
		boolean isimport = ((PlanVO)billvos[0].getParentVO()).getVbillstatus()==HgPubConst.PLAN_YEAR_IMPORT_BILLSTATUS;
		
		if(isimport){
			int intNum = billvos.length;
			String pk_corp = ((PlanVO)billvos[0].getParentVO()).getPk_corp();
			String[] nos = HgPubTool.getBatchBillNo(HgPubConst.PLAN_YEAR_BILLTYPE,pk_corp , null, intNum);
			int index = 0;
			PlanVO head = null;
			for(HYBillVO billvo:billvos){
				head = ((PlanVO)billvos[0].getParentVO());
				head.setVbillno(nos[index]);
				//����Ĭ�ϻ���ڼ�
				head.setCaccperiodschemeid(AccountCalendar.getInstance().getYearVO().getPk_accperiodscheme());
				index ++;
			}
		}
		
		checkBeforeSave(billvos);
		AggregatedValueObject[] billvos2 = getSaveBo().saveBillComVos(billvos);
		//��д��Դ�ƻ���Ӧ��������Ϣ�ֶ�

		if(isimport)
			return billvos2;
		
		if(billvos2==null||billvos2.length!=billvos.length)
			throw new BusinessException("�������");

		Map<String,String> souceInfor = new HashMap<String, String>();
		//		String stmp = null;
		PlanBVO[] tmpbodys = null;
		PlanBVO[] tmpbodys2 =null;
		List<String> ltmp = null;
		String tmpHeadID = null;
		int index = 0;
		for(HYBillVO billvo:billvos){
			tmpbodys = (PlanBVO[])billvo.getChildrenVO();
			tmpbodys2 = (PlanBVO[])billvos2[index].getChildrenVO();
			tmpHeadID = billvos2[index].getParentVO().getPrimaryKey();
			int index2 = 0;
			for(PlanBVO body:tmpbodys){
				ltmp = body.getLsourceid();
				if(ltmp == null||ltmp.size()==0)
					throw new BusinessException("���������ȡ��Դ��Ϣʧ��");
				int size = ltmp.size();
				for(int i=0;i<size;i++){
					souceInfor.put(ltmp.get(i), tmpHeadID+","+tmpbodys2[index2].getPrimaryKey());
				}
				index2++;				
			}
			index++;
		}

		if(souceInfor.size()==0){
			throw new BusinessException("���������ȡ��Դ��Ϣʧ��");
		}

		reWriteSouceInfor(souceInfor,tmpbodys[0]);
		return null;
	}

	private void reWriteSouceInfor(Map<String,String> souceInfor,PlanBVO body) throws BusinessException{
		String sql = " update "+body.getTableName()+" set cnextbillid = ? ,cnextbillbid = ? ,irowstatus = "+
		HgPubConst.PLAN_ROW_STATUS_COMMIT+" where "+
		body.getPKFieldName()+" = ?";

		SQLParameter param = null;
		String[] stmps =null;
		for(String key:souceInfor.keySet()){
			stmps = souceInfor.get(key).split(",");
			param = new SQLParameter();
			param.addParam(stmps[0]);
			param.addParam(stmps[1]);
			param.addParam(key);
			getBaseDao().executeUpdate(sql, param);
		}		
	}

	private void checkBeforeSave(HYBillVO[] billvos) throws BusinessException{
		PlanBVO[] bodys = null;
		PlanVO head = null;
		for(HYBillVO bill:billvos){
			head = (PlanVO)bill.getParentVO();
			head.validata();
			bodys = (PlanBVO[])bill.getChildrenVO();
			for(PlanBVO body:bodys){
				body.validataServer();
			}
		}
	}


	/**
	 * 
	 * @author zhf
	 * @˵�������׸ڿ�ҵ���ƻ�����ʱУ��
	 * 2011-1-29����12:50:23
	 * @param spocorp
	 * @param sLogCorp
	 * @param uLogdate
	 * @param billvo
	 * @throws BusinessException
	 */
	public void checkPlanOnUnApprove(String spocorp,String sLogCorp,UFDate uLogdate,AggregatedValueObject billvo) throws BusinessException{

		PlanVO headvo = (PlanVO)billvo.getParentVO();
		PlanBVO[] bodys = (PlanBVO[])billvo.getChildrenVO();
		boolean flag = false;
		for(PlanBVO body:bodys){
			if(PuPubVO.getInteger_NullAs(body.getIrowstatus(), HgPubTool.INTEGER_ZERO_VALUE).intValue() 
					== HgPubConst.PLAN_ROW_STATUS_COMMIT){//�������ύ��
				throw new BusinessException("�ƻ��������ϱ�������������");
			}

		}

		flag = spocorp == sLogCorp;
		if(flag){
			//У�����βɹ��ƻ��Ƿ����������ڲ�������
			String sql = "select count(*) from  po_praybill_b where csourcebillid = '"+headvo.getPrimaryKey()+"' and isnull(dr,0)=0";
			Object o = getBaseDao().executeQuery(sql, new ColumnProcessor());
			if(PuPubVO.getInteger_NullAs(o, HgPubConst.IPRAYTYPE).intValue()>0)
				throw new BusinessException("�������βɹ��ƻ�����������");
		}else{
			//			У�����ɵ��¼ƻ��Ƿ��Ѿ�ִ��  ����   ��ͬ�ƻ�
			//			��ƻ����ж� ��ǰ��ͼƻ��� ��ϵ ��ǰ��<�ƻ��� ������  ��ǰ��=�ƻ��� �ж��Ƿ���ִ��  ��ǰ��>�ƻ��� ���ܲ���
			//			��ʱ�ƻ����ж��Ƿ���ִ��
			if(headvo.getPk_billtype().equalsIgnoreCase(HgPubConst.PLAN_YEAR_BILLTYPE)){
//				YearPlanBO bo = new YearPlanBO();
				getYearPlanBO().checkYearPlanOnUnapprove(uLogdate, headvo, dao);
			}

		}
	}
	/**
	 * 
	 * @author zhf
	 * @˵�������׸ڿ�ҵ���ƻ�����ʱ��д
	 * 2011-1-29����01:48:13
	 * @param billvo
	 * @throws BusinessException
	 */
	public void reWriteNextBillOnUnapprove(HYBillVO billvo) throws BusinessException{
		if(billvo == null)
			return;
		PlanVO head = (PlanVO)billvo.getParentVO();
		if(head.getPk_billtype().equalsIgnoreCase(HgPubConst.PLAN_YEAR_BILLTYPE)){
			getYearPlanBO().delMonthPlans(head.getPrimaryKey(), getBaseDao());
		}
	}
	/**
	 * 
	 * @author zhf
	 * @˵�������׸ڿ�ҵ���ƻ�ɾ��ʱ��д
	 * 2011-1-29����01:48:13
	 * @param billvo
	 * @throws BusinessException
	 */
	public void reWriteNextBillOnDel(AggregatedValueObject billvo) throws BusinessException {
		if (billvo == null)
			return;
		PlanVO head = (PlanVO) billvo.getParentVO();
//		List<String> lids = new ArrayList<String>();
//		PlanBVO[] bodys = (PlanBVO[]) billvo.getChildrenVO();
//		for (PlanBVO body : bodys) {
//			if (lids.contains(body.getPrimaryKey()))
//				continue;
//			lids.add(body.getPrimaryKey());
//		}
//		if (lids.size() <= 0)
//			return;

		String sql = " update "
				+ HgPubTool.getPlanBTableName(head.getPk_billtype())
				+ " set irowstatus = " + HgPubConst.PLAN_ROW_STATUS_FREE
				+ " where isnull(dr,0)=0 and cnextbillid = "
				+ "'"+head.getPrimaryKey()+"'";

		getBaseDao().executeUpdate(sql);
	}
	
	/**
	 * 
	 * @author zhf
	 * @˵�������׸ڿ�ҵ�� �ƻ�����������  ���� ������ϸ
	 * 2011-4-3����05:12:16
	 * @param items
	 * @param oldNum
	 * @param coprator
	 * @param dLogDate
	 * @throws BusinessException
	 */
	public void adjustPlanNetNum(PlanBVO[] items,Object useObj,String coprator,UFDate dLogDate) throws BusinessException{
		if(items == null||items.length == 0||useObj == null)
			return;
		if(!(useObj instanceof Map))
			return;
		Map<String,UFDouble> oldNum = (Map<String,UFDouble>)useObj;
		if(oldNum.size()==0||oldNum.size()!=items.length)
			return;
		PlanBBVO[] adjustVos = new PlanBBVO[items.length];
		int index = 0;
		PlanBBVO tmp = null;
		for(PlanBVO item:items){
			tmp = new PlanBBVO();
			tmp.setPk_planyear_b(item.getPrimaryKey());
			tmp.setCupdateid(coprator);
			tmp.setDupdate(dLogDate);
			tmp.setNafternum(PuPubVO.getUFDouble_NullAsZero(item.getNnetnum()));
			tmp.setNupdatenum(PuPubVO.getUFDouble_NullAsZero(oldNum.get(item.getPrimaryKey())));
			tmp.setStatus(VOStatus.NEW);
			adjustVos[index] = tmp;
			index ++;
		}
		getBaseDao().insertVOArray(adjustVos);
	}	
	/*
	 * �����ض���.
	 */
	private String getClassDataPowerShowNameByTableName(String tableName) {
		String dataPowerName = null;
		if (tableName == null || tableName.trim().length() == 0) {
			return null;
		}
		String dataPowerTableName = tableName.trim().split(" ")[0];

		if ("bd_areacl".equalsIgnoreCase(dataPowerTableName)) {
			dataPowerName = "��������";

		} else if ("bd_invcl".equalsIgnoreCase(dataPowerTableName)) {
			dataPowerName = "�������";

		} else if ("bd_deptdoc".equalsIgnoreCase(dataPowerTableName)) {
			dataPowerName = "���ŵ���";

		} else if ("bd_glbook".equalsIgnoreCase(dataPowerTableName)) {
			dataPowerName = "�����˲�";
		}

		return dataPowerName;
	}
	
	/**
	 * 
	 * @author zhf
	 * @˵�������׸ڿ�ҵ����ѯ�û���ɫȨ��
	 * 2011-4-6����03:19:07
	 * @param tableName
	 * @param tableShowName
	 * @param pk_corp
	 * @param pk_user
	 * @return
	 */
	public String queryClassPowerSql(String tableName, String pk_corp,
			String pk_user) {
		
		nc.ui.bd.ref.IRefUtilService refUtil = (nc.ui.bd.ref.IRefUtilService) NCLocator
				.getInstance().lookup("nc.ui.bd.ref.IRefUtilService");

		return refUtil.queryClassPowerSql(tableName,
				getClassDataPowerShowNameByTableName(tableName), pk_corp,
				pk_user);
	}
	
	public  PactItemVO[] getPactItemsForPO(String corderid) throws BusinessException{
		StringBuffer strbff = new StringBuffer();
		String[] names = HgPubTool.getPactItems();
		strbff.append("select ");
		for(String name:names){
			strbff.append(name+",");
		}
		strbff.append("'aa'");//�������һ��       ��   ����
		
		strbff.append(" from CT_TERM_BB4 where isnull(dr,0)=0 and pk_ct_manage = '"+corderid+"'");
		Object o = getBaseDao().executeQuery(strbff.toString(), new BeanListProcessor(PactItemVO.class));
		if(o == null)
			return null;
		List ldata = (List)o;
		if(ldata.size()==0)
			return null;
		return (PactItemVO[])ldata.toArray(new PactItemVO[0]);	
	}
	
	/**
	 * 
	 * @author zhf
	 * @˵�������׸ڿ�ҵ�������ͬ����ҳǩ
	 * 2012-2-23����03:00:11
	 * @param pacts
	 * @return
	 * @throws BusinessException
	 */
	public Object savePactItemsForPO(PactItemVO[] pacts) throws BusinessException{
		if(pacts == null||pacts.length == 0){
			return null;
		}
		List<PactItemVO> lnew = new ArrayList<PactItemVO>();
		List<PactItemVO> lupdate = new ArrayList<PactItemVO>();
		List<PactItemVO> ldel = new ArrayList<PactItemVO>();
		
		for(PactItemVO pact:pacts){
			if(pact.getStatus() == VOStatus.NEW){
				lnew.add(pact);
			}else if(pact.getStatus() == VOStatus.UPDATED){
				lupdate.add(pact);
			}else if(pact.getStatus() == VOStatus.DELETED){
				ldel.add(pact);
			}
		}
		
		if(lnew.size()>0){
			getBaseDao().insertVOArray(lnew.toArray(new PactItemVO[0]));
		}
		if(lupdate.size()>0){
			getBaseDao().updateVOArray(lupdate.toArray(new PactItemVO[0]));
		}
		if(ldel.size()>0){
			getBaseDao().deleteVOArray(ldel.toArray(new PactItemVO[0]));
		}
		
		String headid = pacts[0].getPk_ct_manage();
		
		Collection c = getBaseDao().retrieveByClause(PactItemVO.class, " pk_ct_manage = '"+headid+"' and isnull(dr,0)=0");
		if(c==null||c.size()==0)
			return null;
		
		return c.toArray(new PactItemVO[0]);
	}
}
