package nc.bs.ic.pub;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import nc.bs.pub.SuperDMO;
import nc.bs.pub.pf.PfUtilTools;
import nc.bs.trade.business.HYPubBO;
import nc.bs.trade.comsave.BillSave;
import nc.bs.wds.tray.lock.LockTrayBO;
import nc.bs.wl.pub.WdsPubResulSetProcesser;
import nc.itf.scm.cenpur.service.TempTableUtil;
import nc.jdbc.framework.SQLParameter;
import nc.ui.scm.util.ObjectUtils;
import nc.vo.ic.other.in.OtherInBillVO;
import nc.vo.ic.other.out.MyBillVO;
import nc.vo.ic.pub.StockInvOnHandVO;
import nc.vo.ic.pub.TbGeneralBBVO;
import nc.vo.ic.pub.TbGeneralBVO;
import nc.vo.ic.pub.TbGeneralHVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
import nc.vo.pub.VOStatus;
import nc.vo.pub.compiler.PfParameterVO;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.trade.pub.IBDACTION;
import nc.vo.trade.voutils.IFilter;
import nc.vo.wds.ic.cargtray.SmallTrayVO;
import nc.vo.wl.pub.WdsWlPubConst;
import nc.vo.wl.pub.WdsWlPubTool;

public class WdsIcInPubBillSave extends BillSave {
	private SuperDMO dmo = new SuperDMO();
	private SuperDMO getSuperDMO(){
		if(dmo == null){
			dmo = new SuperDMO();
		}
		return dmo;
	}
	private IcInPubBO dao = null;
	private IcInPubBO getOutBO(){
		if(dao == null)
			dao = new IcInPubBO();
		return dao;
	}
	
	private TempTableUtil ttutil = null;
	private TempTableUtil getTtutil(){
		if(ttutil == null){
			ttutil = new TempTableUtil();
		}
		return ttutil;
	}
	
	private LockTrayBO lockbo = null;
	private LockTrayBO getLockTrayBO(){
		if(lockbo == null)
			lockbo = new LockTrayBO(getOutBO().getDao(),getOutBO().getStockBO(),getSuperDMO(),getTtutil());
		return lockbo;
	}
	
	class filterDelLine implements IFilter{
		public boolean accept(Object o) {
			// TODO Auto-generated method stub
			if(o == null)
				return false;
			SuperVO vo = (SuperVO)o;
			if(vo.getStatus() == VOStatus.DELETED)
				return false;
			return true;
		}
		
	}
	
	/**
	 * ����VO���档 ���� ��ɾ��������Ϣ  �ٱ����µ�������Ϣ
	 *    1����vo����  2������Ϣ���� 3��Դ���ݻ�д 4��������ȷ�ϵ�
	 * �������ڣ�(2004-2-27 11:15:29)
	 * @return ArrayList
	 * @param vo nc.vo.pub.AggregatedValueObject

	 */
	public java.util.ArrayList saveBill(nc.vo.pub.AggregatedValueObject billVo)
	throws BusinessException {

		if(billVo==null)
			throw new BusinessException("��������Ϊ��");
		boolean isAdd = false;
		boolean bodyChanged = false;
		
		AggregatedValueObject oldbillVo = null;//����ԭʼ������Ϣ
		try {
			oldbillVo = (AggregatedValueObject)ObjectUtils.serializableClone(billVo);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			throw new BusinessException(e1);
		}
		
		TbGeneralHVO head = (TbGeneralHVO)billVo.getParentVO();
		
		Map<String, SmallTrayVO[]> lockTrayInfor = (Map<String, SmallTrayVO[]>)((OtherInBillVO)billVo).getOUserObj();
		if(PuPubVO.getString_TrimZeroLenAsNull(head.getPrimaryKey())==null)
			isAdd = true;
		
		
		//////////////////////////////////////////////////////////////////////////////////////////
		
		TbGeneralBVO[] obodys = (TbGeneralBVO[])billVo.getChildrenVO();
		
		//���˵�ɾ����  zhf add
		TbGeneralBVO[] bodys = (TbGeneralBVO[])nc.vo.trade.voutils.VOUtil.filter(obodys, new filterDelLine());
		billVo.setChildrenVO(bodys);

		if(null != bodys && bodys.length > 0){
			//yf��¼�����Ƿ��иĶ�
			bodyChanged = true;
			for(TbGeneralBVO body:bodys){
				body.validateOnSave();
			}
		}
		
		
//		zhf add  У��  �������̵İ󶨹�ϵ
		getLockTrayBO().checkInBillOnSave(lockTrayInfor, bodys, head.getGeh_cwarehouseid(), head.getPk_cargdoc());
		
		///////////////////////////////////////////////////////////////////////////////////////////////
		
		if(!isAdd && bodyChanged){//�޸ı�����ɾ��  �Ѵ��ڵ�������ϸ�ӱ���Ϣ  �� �ظ����̴�����Ϣ
			getOutBO().deleteOtherInforOnDelBill(head.getPrimaryKey(),(TbGeneralBVO[])oldbillVo.getChildrenVO());
		}
		
		//��д�����ڱ���֮ǰ������֮������ͬһ�����������ݺ;����ݻ���ȫһ�������ұ���֮��vo��״̬�������޸�״̬
		try {
			WriteBackTool.writeBack((SuperVO[])(billVo.getChildrenVO()), "tb_outgeneral_b", "general_b_pk", new String[]{"geb_nmny"}, new String[]{"ntagnum"});
		} catch (Exception e) {
			
			throw new BusinessException(e);
		}
		
		getOutBO().writeBackForInBill((OtherInBillVO)oldbillVo,IBDACTION.SAVE,isAdd);
		
		java.util.ArrayList retAry = super.saveBill(billVo);

		if(retAry == null || retAry.size() == 0){
			throw new BusinessException("����ʧ��");
		}
		OtherInBillVO newBillVo = (OtherInBillVO)retAry.get(1);
		if(newBillVo == null){
			throw new BusinessException("����ʧ��");
		}	
		if(bodyChanged){
			//����������Ϣ��ˮ��
			insertTrayInfor(newBillVo, (OtherInBillVO)oldbillVo);
			//���������״̬��  ��������״̬Ϊ��ռ��
			TbGeneralBVO[] newbodys = (TbGeneralBVO[])newBillVo.getChildrenVO();
			getOutBO().insertInvBatchState(newbodys, head.getPk_cargdoc());
		}	
		
//		zhf add
		if(lockTrayInfor != null && lockTrayInfor.size() > 0){
			//������ڰ�ʵ��������Ϣ   ����󶨹�ϵ
//			LockTrayBO lockbo = new LockTrayBO();
			getLockTrayBO().doSaveLockTrayInfor(PuPubVO.getString_TrimZeroLenAsNull(retAry.get(0)),newBillVo.getHeaderVo().getGeh_cwarehouseid(),lockTrayInfor);
		}	
		
//		ת�ֲ��������ʱ  �Ƿ��Զ����� ���ƫ����
		//yf����û�иĶ��򲻽����ж�
		if(head.getGeh_billtype().equalsIgnoreCase(WdsWlPubConst.BILLTYPE_OTHER_IN) && bodyChanged){
			saveBillOnAdjust(newBillVo);
		}
		return retAry;
	}
	
	
	
	
	
	
	
	
	
//	/**
//	 * 
//	 * @���ߣ�zhf
//	 * @˵�������ɽ������Ŀ 
//	 * @ʱ�䣺2011-4-7����04:57:17
//	 * @param arg1
//	 * @param sLogUser
//	 * @param uLogDate
//	 * @param sLogCorp
//	 * @param itype 0 �����Ƶ� 1 ���۶��� 2 �ֳ�ֱ�� 3��ֶ���4 �ϲ����� 8 �������Ƶ������ɵ��˵�
//	 * @throws Exception
//	 */
//	public void insertFyd(AggregatedValueObject arg1) throws Exception {
//		if(arg1 == null)
//			return;
//		MyBillVO billVo = (MyBillVO)arg1;
//		TbGeneralHVO generalh =(TbGeneralHVO) arg1.getParentVO();
//		TbGeneralBVO[] generalb = (TbGeneralBVO[])arg1.getChildrenVO();
//		Object[] o = new Object[3];
//		o[0] = false;
//		TbFydnewVO fydvo = new TbFydnewVO();
//		List<TbFydmxnewVO[]> fydmxList = new ArrayList<TbFydmxnewVO[]>();
//		HYPubBO hybo = new HYPubBO();
//
//		// ����VOת��/////////////////////////////////////////////
//
//		// ------------ת����ͷ����-----------------//
//		
//		if (null != generalh && null != generalb && generalb.length > 0) {
//			if (null != generalh.getVdiliveraddress()
//					&& !"".equals(generalh.getVdiliveraddress())) {
//				fydvo.setFyd_shdz(generalh.getVdiliveraddress()); // �ջ���ַ
//			}
//			if (null != generalh.getVnote() && !"".equals(generalh.getVnote())) {
//				fydvo.setFyd_bz(generalh.getVnote()); // ��ע
//			}
//			if (null != generalh.getCdptid()
//					&& !"".equals(generalh.getCdptid())) {
//				fydvo.setCdeptid(generalh.getCdptid()); // ����
//			}
//			// �����˻���ʽ
//			fydvo.setFyd_yhfs("����");
//			// �������� 0 �����Ƶ� 1 ���۶��� 2 �ֳ�ֱ�� 3��ֶ���4 �ϲ����� 8 �������Ƶ������ɵ��˵�
//			fydvo.setBilltype(billVo.getItype());
//			fydvo.setVbillstatus(1);
//			// ���ݺ�----------------------------------------------------------------
//			fydvo.setVbillno(hybo.getBillNo(WdsWlPubConst.BILLTYPE_SEND_CONFIRM, billVo.getSLogCorp(), null, null));
//			// �Ƶ�����
//			fydvo.setDmakedate(billVo.getULogDate());
//			fydvo.setVoperatorid(billVo.getSLogUser()); // �����Ƶ���
//			// ���÷���վ
//			fydvo.setSrl_pk(generalh.getSrl_pk());
//			// ����վ
//			fydvo.setSrl_pkr(generalh.getSrl_pkr());
//			// --------------ת����ͷ����---------------//
//			// --------------ת������----------------//
//			List<TbFydmxnewVO> tbfydmxList = new ArrayList<TbFydmxnewVO>();
//			for (int j = 0; j < generalb.length; j++) {
//				TbFydmxnewVO fydmxnewvo = new TbFydmxnewVO();
//				TbGeneralBVO genb = generalb[j];
//				if (null != genb.getCinventoryid()
//						&& !"".equals(genb.getCinventoryid())) {
//					fydmxnewvo.setPk_invbasdoc(genb.getCinventoryid()); // ��Ʒ����
//				}
//				if (null != genb.getNshouldoutnum()
//						&& !"".equals(genb.getNshouldoutnum())) {
//					fydmxnewvo.setCfd_yfsl(genb.getNshouldoutnum()); // Ӧ������
//				}
//				if (null != genb.getNshouldoutassistnum()
//						&& !"".equals(genb.getNshouldoutassistnum())) {
//					fydmxnewvo.setCfd_xs(genb.getNshouldoutassistnum()); // ����
//				}
//				if (null != genb.getNoutnum() && !"".equals(genb.getNoutnum())) {
//					fydmxnewvo.setCfd_sfsl(genb.getNoutnum()); // ʵ������
//				}
//				if (null != genb.getNoutassistnum()
//						&& !"".equals(genb.getNoutassistnum())) {
//					fydmxnewvo.setCfd_sffsl(genb.getNoutassistnum()); // ʵ��������
//				}
//				if (null != genb.getCrowno() && !"".equals(genb.getCrowno())) {
//					fydmxnewvo.setCrowno(genb.getCrowno()); // �к�
//				}
//				if (null != genb.getUnitid() && !"".equals(genb.getUnitid())) {
//					fydmxnewvo.setCfd_dw(genb.getUnitid()); // ��λ
//				}
//				fydmxnewvo.setCfd_pc(genb.getVbatchcode()); // ����		
//				fydmxnewvo.setVsourcebillcode(WdsWlPubConst.BILLTYPE_OTHER_OUT);
//				fydmxnewvo.setCsourcebillbid(genb.getGeneral_b_pk());
//				fydmxnewvo.setCsourcebillhid(genb.getGeneral_pk());
//				tbfydmxList.add(fydmxnewvo);
//			}
//			// ----------------ת���������---------------------//
//				TbFydmxnewVO[] fydmxVO = new TbFydmxnewVO[tbfydmxList.size()];
//				tbfydmxList.toArray(fydmxVO);
//				fydmxList.add(fydmxVO);
//				o[0] = true;
//		HYBillVO newBillVo = new HYBillVO();
//		newBillVo.setParentVO(fydvo);
//		newBillVo.setChildrenVO(fydmxVO);
//		saveBD(newBillVo, null);
//	}
//	}
	
	/**
	 * 
	 * @���ߣ�zhf
	 * @˵�������ɽ������Ŀ �����浥��������ϸ��ˮ�ӱ�
	 * @ʱ�䣺2011-4-7����02:28:26
	 * @param newBillVo  ������ ��浥��vo
	 * @param oldBillVo  ����ǰ��浥��vo
	 * @throws BusinessException
	 */
	private void insertTrayInfor(OtherInBillVO newBillVo,OtherInBillVO oldBillVo) throws BusinessException{
		if(newBillVo == null || oldBillVo == null){
			return;
		}
		TbGeneralBVO[] newbodys = (TbGeneralBVO[])newBillVo.getChildrenVO();
		TbGeneralBVO[] oldbodys = (TbGeneralBVO[])oldBillVo.getChildrenVO();
		//		�ֳ� ��  ��Ᵽ�� ���ܸ�У��Լ��
		String cware = newBillVo.getHeaderVo().getGeh_cwarehouseid();
		if(PuPubVO.getString_TrimZeroLenAsNull(cware)==null)
			throw new BusinessException("���ֿ�Ϊ��");
		if(WdsWlPubTool.isZc(cware))
			checkTrayUsed(oldbodys);
		for(TbGeneralBVO old : oldbodys ){
			String oldno = old.getGeb_crowno();//�к�
			for(TbGeneralBVO newbody:newbodys){
				String newno = newbody.getGeb_crowno();//���к�
				if(oldno.equals(newno)){
					List<TbGeneralBBVO> ltraytmp = old.getTrayInfor();
					for(TbGeneralBBVO tray:ltraytmp){
						tray.setGeb_pk(newbody.getPrimaryKey());
						tray.setGeh_pk(newbody.getGeh_pk());
					}
					getSuperDMO().insertList(ltraytmp);
					TbGeneralBBVO[] bvo = (TbGeneralBBVO[])getSuperDMO().queryByWhereClause(TbGeneralBBVO.class, " geb_pk = '"+newbody.getPrimaryKey()+"' and isnull(dr,0) = 0 ");
					//					if(ltraytmp==null||tmps.length==0||tmps.length!=ltraytmp.size()){
					//						throw new BusinessException("����������Ϣʧ��");
					//					}
					//					int index2 = 0;
					//					for(String tmp:tmps){
					//						ltraytmp.get(index2).setPrimaryKey(tmp);
					//						index2++;
					//					}

					newbody.setTrayInfor(arrayTolist(bvo));
					//					index++;
				}
			}
		}
	}
	public List<TbGeneralBBVO> arrayTolist(TbGeneralBBVO[] bvo){
		List<TbGeneralBBVO> list  = null;
		if(bvo!=null && bvo.length>0){
			list = new ArrayList<TbGeneralBBVO>();
			for(TbGeneralBBVO b : bvo){
				list.add(b);
			}
		}
		return list;
	}
	/**
	 * 
	 * @throws BusinessException
	 * @���ߣ�zhf
	 * @˵�������ɽ������Ŀ 
	 * @ʱ�䣺2011-4-13����09:02:49
	 */
	private void checkTrayUsed(TbGeneralBVO[] bodys) throws BusinessException{
		String sql ="select cdt_traycode from bd_cargdoc_tray  " +
				" where cdt_pk=? and cdt_traystatus="+StockInvOnHandVO.stock_state_use;
		SQLParameter para = new SQLParameter();
		for(TbGeneralBVO bvo:bodys){
			List<TbGeneralBBVO> tray = bvo.getTrayInfor();
		if(bvo.getPrimaryKey()==null || bvo.getPrimaryKey().length()==0){
			for(int i=0;i<tray.size();i++){
				String cdp_pk =tray.get(i).getCdt_pk();
				para.addParam(cdp_pk);
				Object o =getOutBO().getDao().executeQuery(sql, para, WdsPubResulSetProcesser.COLUMNPROCESSOR);
				if(o!=null && ! (((String)o).substring(0,2)).equalsIgnoreCase(WdsWlPubConst.XN_CARGDOC_TRAY_NAME)){
					throw new BusinessException("��"+(i+1)+"�У�ָ��������:"+o.toString()+"\n�ѱ�����ʹ�ã�������ָ��");
				}
				para.clearParams();
			}
		  }
		}
		
	}
	
	/**
	 * 
	 * @���ߣ�zhf
	 * @˵�������ɽ������Ŀ 
	 * @ʱ�䣺2011-7-13����10:10:34
	 * @param billVo
	 * @return
	 * @throws BusinessException
	 */
	public void saveBillOnAdjust(nc.vo.pub.AggregatedValueObject billVo)
	throws BusinessException {

		if(billVo==null)
			throw new BusinessException("��������Ϊ��");
		
		String cinstoreid = PuPubVO.getString_TrimZeroLenAsNull(((TbGeneralHVO)billVo.getParentVO()).getGeh_cwarehouseid());
		if(cinstoreid == null){
			throw new BusinessException("���ֿ�Ϊ��");
		}
		boolean flag = WdsWlPubTool.isAutoAdjustStore(cinstoreid); 
		if(!flag)
			return;
		
//		���ɵ�����ⵥ
		OtherInBillVO inbill = creatAdjustInBill(billVo);
		if(inbill == null)
			return;
//		���������ⵥ
		List lret = super.saveBill(inbill);
		getOutBO().writeBackForInBill(inbill,IBDACTION.SAVE,true);
		OtherInBillVO newin = (OtherInBillVO)lret.get(1);
//		���ɵ������ⵥ
		MyBillVO outbillvo = creatAdjustOutBill(newin, inbill.getHeaderVo().getCoperatorid(), inbill.getHeaderVo().getCopetadate().toString(), inbill.getHeaderVo().getPk_corp());
		
		if(outbillvo == null){
			throw new BusinessException("�����쳣");
		}
//		����������ⵥ
		super.saveBill(outbillvo);
	}
	
	private HYPubBO hybo = null;
	private HYPubBO getHyBO(){
		if(hybo == null)
			hybo = new HYPubBO();
		return hybo;
	}
	
	/**
	 * 
	 * @���ߣ�zhf
	 * @˵�������ɽ������Ŀ ת�ֲ���ⵥ�����  ����ʵ��������Ӧ��������һ��  ��ƫ��������ϵͳ�Զ����� ��һ�� һ����
	 * @ʱ�䣺2011-7-13����10:50:08
	 * @param billVo
	 * @return
	 * @throws BusinessException
	 */
	private OtherInBillVO creatAdjustInBill(nc.vo.pub.AggregatedValueObject billVo) throws BusinessException{
		if(billVo == null)
			return null;
		TbGeneralHVO head = (TbGeneralHVO)billVo.getParentVO();
		TbGeneralBVO[] bodys = (TbGeneralBVO[])billVo.getChildrenVO();
		if(head == null || bodys == null||bodys.length ==0){			
			throw new BusinessException("����Ϊ��");			
		}
		if(PuPubVO.getString_TrimZeroLenAsNull(bodys[0].getCsourcetype())==null || !PuPubVO.getString_TrimZeroLenAsNull(bodys[0].getCsourcetype()).equalsIgnoreCase(WdsWlPubConst.BILLTYPE_OTHER_OUT)){
			return null;
		}
		List<TbGeneralBVO> lnewbody = new ArrayList<TbGeneralBVO>();
		UFDouble nnum = null;
		UFDouble nassnum = null;
		TbGeneralBVO newbody = null;
		OtherInBillVO bill = null;
//		HYPubBO hybo = new HYPubBO();
		for(TbGeneralBVO body:bodys){
			if(PuPubVO.getString_TrimZeroLenAsNull(body.getCsourcetype())==null || !PuPubVO.getString_TrimZeroLenAsNull(body.getCsourcetype()).equalsIgnoreCase(WdsWlPubConst.BILLTYPE_OTHER_OUT)){
				continue;
			}
			newbody = (TbGeneralBVO)body.clone();
			newbody.validateOnZdrk(true);
			nnum = PuPubVO.getUFDouble_NullAsZero(newbody.getGeb_snum()).sub(PuPubVO.getUFDouble_NullAsZero(newbody.getGeb_anum()));
			nassnum = PuPubVO.getUFDouble_NullAsZero(newbody.getGeb_bsnum()).sub(PuPubVO.getUFDouble_NullAsZero(newbody.getGeb_banum()));
			if(nnum.doubleValue()<=0)
				continue;
			if(nassnum.doubleValue()<=0)
				continue;
			newbody.setGeb_snum(nnum);
			newbody.setGeb_bsnum(nassnum);
			newbody.setGeb_anum(nnum);
			newbody.setGeb_banum(nassnum);
			newbody.setGeb_nmny(newbody.getGeb_anum().multiply(PuPubVO.getUFDouble_NullAsZero(newbody.getGeb_nprice())));
			
//			��Դ��Ϣ  ��¼  Ϊ�� ��д ���ⵥ  ���ۼƳ�������  ��˸�ֵ  zhf
			newbody.setCsourcebillbid(body.getCsourcebillbid());
			newbody.setCsourcebillhid(body.getCsourcebillhid());
			newbody.setCsourcetype(body.getCsourcetype());
			
			newbody.setCfirstbillbid(body.getPrimaryKey());
			newbody.setCfirstbillhid(body.getGeh_pk());
			newbody.setCfirsttype(WdsWlPubConst.BILLTYPE_OTHER_IN);
			
			newbody.setGeh_pk(null);
			newbody.setGeb_pk(null);
			
			body.setStatus(VOStatus.NEW);
			lnewbody.add(newbody);
		}
		
		if(lnewbody.size()>0){
			bill = new OtherInBillVO();
			TbGeneralHVO newhead = (TbGeneralHVO)head.clone();
			newhead.setGeh_pk(null);
			newhead.setGeh_customize7("Y");
			newhead.setGeh_billcode(getHyBO().getBillNo(WdsWlPubConst.BILLTYPE_OTHER_IN, head.getPk_corp(), null, null));
			bill.setParentVO(newhead);
			bill.setChildrenVO(lnewbody.toArray(new TbGeneralBVO[0]));
			return bill;
		}
		return null;
	}	
	
	private MyBillVO creatAdjustOutBill(OtherInBillVO inBillVo,
			String coprator, String date, String corp) throws BusinessException {
		if (inBillVo == null)
			return null;
		PfParameterVO paraVo = new PfParameterVO();
		paraVo.m_operator = coprator;
		paraVo.m_currentDate = date;
		paraVo.m_coId = corp;
		MyBillVO outbillvo = (MyBillVO) PfUtilTools.runChangeData(
				WdsWlPubConst.BILLTYPE_OTHER_IN,
				WdsWlPubConst.BILLTYPE_OTHER_OUT, inBillVo, paraVo);
		if(outbillvo == null)
			return null;
		String vbillno = getHyBO().getBillNo(WdsWlPubConst.BILLTYPE_OTHER_OUT, corp, null, null);
		if(vbillno == null){
			throw new BusinessException("��ȡ�������ⵥ���ݺų���");
		}
		outbillvo.getParentVO().setAttributeValue("vbillcode", vbillno);
		return outbillvo;
	}
}
