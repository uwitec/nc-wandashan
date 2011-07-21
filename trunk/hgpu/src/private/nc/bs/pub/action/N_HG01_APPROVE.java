package nc.bs.pub.action;

import java.util.Hashtable;

import nc.bs.hg.pu.plan.year.YearPlanBO;
import nc.bs.pub.compiler.AbstractCompiler2;
import nc.vo.hg.pu.pub.HgPubConst;
import nc.vo.hg.pu.pub.PlanBVO;
import nc.vo.pr.pray.PraybillVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.compiler.PfParameterVO;
import nc.vo.scm.constant.ScmConst;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.trade.pub.HYBillVO;
import nc.vo.uap.pf.PFBusinessException;

/**
 * ������������
 * @author Administrator
 *
 */
public class N_HG01_APPROVE extends AbstractCompiler2 {
	private java.util.Hashtable m_methodReturnHas = new java.util.Hashtable();
	private Hashtable m_keyHas = null;

	public N_HG01_APPROVE() {
		super();
	}

	/*
	 * ��ע��ƽ̨��д������ �ӿ�ִ����
	 */
	public Object runComClass(PfParameterVO vo) throws BusinessException {
		try {
			super.m_tmpVo = vo;
			
			AggregatedValueObject billvo = getVo();
			if(billvo == null){
				throw new BusinessException("��������Ϊ��");
			}
			
			HYBillVO planvo = (HYBillVO)billvo;
			
			//add by zhw 2010-12-25   ���������ʱ�������Ĳ���ͨ�����
//			PlanBVO[] body	=(PlanBVO[])planvo.getChildrenVO();
//			if( body ==null || body.length==0){
//				throw new BusinessException("��ȡ��������ʧ��");
//			}
//			int len= body.length;
//			for(int i=0;i<len;i++){
//				String cinventtoryid =PuPubVO.getString_TrimZeroLenAsNull(body[i].getCinventoryid());
//				String pk_invbasdoc =PuPubVO.getString_TrimZeroLenAsNull(body[i].getPk_invbasdoc());
//				if(cinventtoryid==null && pk_invbasdoc==null){
//					throw new BusinessException("�����"+(i+1)+"�д�����ʱ���ʱ���,����ͨ�����");
//				}
//			}
			//  zhw end 
			
			// ####�����Ϊ����������������ʼ...���ܽ����޸�####
			Object m_sysflowObj = procActionFlow(vo);
			if (m_sysflowObj != null) {
				return m_sysflowObj;
			}
			// ####�����Ϊ�������������������...���ܽ����޸�####
			Object retObj = null;
			setParameter("currentVo", vo.m_preValueVo);
			retObj = runClass("nc.bs.hg.pu.pub.HYBillApprove", "approveHYBill",
					"nc.vo.pub.AggregatedValueObject:01", vo, m_keyHas,
					m_methodReturnHas);
			
			//���ɹ�˾��������ƻ����ɲɹ��ƻ�
			String pocorp = PuPubVO.getString_TrimZeroLenAsNull(getUserObj());
			if(pocorp == null){
//				ϵͳ���ɹ�˾����Ϊ��  Ӧ��δ���
				throw new BusinessException("ϵͳ�ɹ���˾δ����");
			}
			boolean flag = pocorp == vo.m_coId;
			if (flag) {
				
				//�Լƻ��������ڲɹ��ƻ�����֮ǰ  ����ƻ������ٷ����仯
				
//				PraybillVO voPrayBill  = (PraybillVO)changeData(planvo, HgPubConst.PLAN_YEAR_BILLTYPE, ScmConst.PO_Pray);
				PraybillVO[] voPrayBills = (PraybillVO[])changeDataAry(new HYBillVO[]{planvo}, HgPubConst.PLAN_YEAR_BILLTYPE, ScmConst.PO_Pray);
//				PraybillVO[] voPrayBills = new PraybillVO[]{voPrayBill};
//					null;//(nc.vo.pr.pray.PraybillVO[])changeDataAry (vo2PrayBill, "422Y", "20");
				setParameter ( "BILLTYPE", "20");
				setParameter ( "BILLDATE",getUserDate ().toString ());
				setParameter ( "ACTIONNAME", "PUSHSAVEVO");
				setParameter ( "BILLVOS",voPrayBills);
				setParameter ( "P4",null);
				setParameter ( "P5",null);
//				Object[] oPrayBillRet  = (Object[])runClass( "nc.bs.pub.pf.PfUtilBO", "processBatch", "&ACTIONNAME:String,&BILLTYPE:String,&BILLDATE:String,&BILLVOS:nc.vo.pub.AggregatedValueObject[],&P4:Object[],&P5:nc.vo.pub.pf.PfUtilWorkFlowVO",vo,m_keyHas,m_methodReturnHas);
				runClass( "nc.bs.pub.pf.PfUtilBO", "processBatch", "&ACTIONNAME:String,&BILLTYPE:String,&BILLDATE:String,&BILLVOS:nc.vo.pub.AggregatedValueObject[],&P4:Object[],&P5:nc.vo.pub.pf.PfUtilWorkFlowVO",vo,m_keyHas,m_methodReturnHas);
//				mapVirtualIDAndPrayBill  = (HashMap)oPrayBillRet[0];
			}else{
				//��ֳ��¼ƻ�
				YearPlanBO bo = new YearPlanBO();
				bo.splitYearPlan2MonthPlan(planvo, vo.m_coId);
			}
			
			
			return retObj;
		} catch (Exception ex) {
			if (ex instanceof BusinessException)
				throw (BusinessException) ex;
			else
				throw new PFBusinessException(ex.getMessage(), ex);
		}
	}

	/*
	 * ��ע��ƽ̨��дԭʼ�ű�
	 */
	public String getCodeRemark() {
		return "	//####�����Ϊ����������������ʼ...���ܽ����޸�####\nprocActionFlow@@;\n//####�����Ϊ�������������������...���ܽ����޸�####\nObject  retObj  =null;\n setParameter(\"currentVo\",vo.m_preValueVo);           \nretObj =runClassCom@ \"nc.bs.pp.pp0201.ApproveAction\", \"approveHYBill\", \"nc.vo.pub.AggregatedValueObject:01\"@;\n            ArrayList ls = (ArrayList)getUserObj();\n       \n        setParameter(\"userOpt\",ls.get(1));               \n            runClassCom@ \"nc.bs.pp.pp0201.ApproveAction\", \"afterApprove\", \"&userOpt:java.lang.Integer,nc.vo.pub.AggregatedValueObject:01\"@;               \nreturn retObj;\n";
	}

	/*
	 * ��ע�����ýű�������HAS
	 */
	private void setParameter(String key, Object val) {
		if (m_keyHas == null) {
			m_keyHas = new Hashtable();
		}
		if (val != null) {
			m_keyHas.put(key, val);
		}
	}
}
