package nc.bo.other.out;

import java.util.ArrayList;
import java.util.List;

import nc.bs.pub.SuperDMO;
import nc.bs.trade.business.HYPubBO;
import nc.bs.wds.w8004040204.W8004040204Impl;
import nc.bs.wl.pub.WdsPubResulSetProcesser;
import nc.jdbc.framework.SQLParameter;
import nc.vo.dm.confirm.TbFydmxnewVO;
import nc.vo.dm.confirm.TbFydnewVO;
import nc.vo.ic.other.out.MyBillVO;
import nc.vo.ic.other.out.TbOutgeneralBVO;
import nc.vo.ic.other.out.TbOutgeneralHVO;
import nc.vo.ic.other.out.TbOutgeneralTVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.trade.pub.HYBillVO;
import nc.vo.trade.pub.IBDACTION;
import nc.vo.wl.pub.VOTool;
import nc.vo.wl.pub.WdsWlPubConst;

/**
 * 
 * @author Administrator
 * ���������̨��
 */
public class OtherOutSave  extends nc.bs.trade.comsave.BillSave {

	private SuperDMO dmo = new SuperDMO();
	private SuperDMO getSuperDMO(){
		if(dmo == null){
			dmo = new SuperDMO();
		}
		return dmo;
	}
	private OtherOutBO dao = null;
	private OtherOutBO getOutBO(){
		if(dao == null)
			dao = new OtherOutBO();
		return dao;
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
		MyBillVO old_billVo = (MyBillVO)VOTool.aggregateVOClone(billVo);
		boolean isAdd = false;
		boolean bodyChanged = false;
		TbOutgeneralHVO head = (TbOutgeneralHVO)billVo.getParentVO();
		if(PuPubVO.getString_TrimZeroLenAsNull(head.getPrimaryKey())==null)
			isAdd = true;
		TbOutgeneralBVO[] bodys = null;	
		if(billVo.getChildrenVO() != null && billVo.getChildrenVO().length > 0){
			bodyChanged = true;
			bodys = (TbOutgeneralBVO[])billVo.getChildrenVO();
			for(TbOutgeneralBVO body:bodys){
				body.validationOnSave();
			}
		}
		if(!isAdd && bodyChanged){//�޸ı�����ɾ��  �Ѵ��ڵ�������ϸ�ӱ���Ϣ  �� �ظ����̴�����Ϣ
			TbOutgeneralBVO[] bb = (TbOutgeneralBVO[])old_billVo.getChildrenVO();
			for(TbOutgeneralBVO b : bb){
				String wheresql = " general_b_pk = '"+b.getPrimaryKey()+"' and isnull(dr,0)=0";
				List<TbOutgeneralTVO> ltray = (List<TbOutgeneralTVO> )getOutBO().getBaseDAO().retrieveByClause(TbOutgeneralTVO.class, wheresql);
				if(ltray!=null && ltray.size()>0)
					getOutBO().deleteOtherInforOnDelBill(ltray);
			}
		}
		//�����  ��д������Դ
		getOutBO().writeBack(old_billVo,IBDACTION.SAVE,isAdd);
		//---------------------------����ǰУ�����----------------------------------------	
		java.util.ArrayList retAry = super.saveBill(billVo);

		if(retAry == null || retAry.size() == 0){
			throw new BusinessException("����ʧ��");
		}
		MyBillVO newBillVo = (MyBillVO)retAry.get(1);
		if(newBillVo == null){
			throw new BusinessException("����ʧ��");
		}	
		if(bodyChanged){
			//����������Ϣ��ˮ��
			insertTrayInfor(newBillVo, old_billVo);
			//���¿�����״̬��
			W8004040204Impl handbo = new W8004040204Impl();
			try {
				TbOutgeneralBVO[] newbodys = (TbOutgeneralBVO[])newBillVo.getChildrenVO();
				for(TbOutgeneralBVO newbody:newbodys){
					handbo.updateWarehousestock(newbody.getTrayInfor());
				}
//				//���ɷ��˵�
//				if(PuPubVO.getUFBoolean_NullAs(((TbOutgeneralHVO)newBillVo.getParentVO()).getIs_yundan(), UFBoolean.FALSE).booleanValue()){
//					insertFyd(newBillVo);
//				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				if(e instanceof BusinessException){
					throw (BusinessException)e;
				}
				throw new BusinessException(e);
			}		
		}
			

	
		return retAry;
	}
	
	
	
	/**
	 * 
	 * @���ߣ�zhf
	 * @˵�������ɽ������Ŀ 
	 * @ʱ�䣺2011-4-7����04:57:17
	 * @param arg1
	 * @param sLogUser
	 * @param uLogDate
	 * @param sLogCorp	 * @param itype 0 �����Ƶ� 1 ���۶��� 2 �ֳ�ֱ�� 3��ֶ���4 �ϲ����� 8 �������Ƶ������ɵ��˵�
	 * @throws Exception
	 */
	public void insertFyd(AggregatedValueObject arg1) throws Exception {
		if(arg1 == null)
			return;
		MyBillVO billVo = (MyBillVO)arg1;
		TbOutgeneralHVO generalh =(TbOutgeneralHVO) arg1.getParentVO();
		TbOutgeneralBVO[] generalb = (TbOutgeneralBVO[])arg1.getChildrenVO();
		Object[] o = new Object[3];
		o[0] = false;
		TbFydnewVO fydvo = new TbFydnewVO();
		List<TbFydmxnewVO[]> fydmxList = new ArrayList<TbFydmxnewVO[]>();
		HYPubBO hybo = new HYPubBO();

		// ����VOת��/////////////////////////////////////////////

		// ------------ת����ͷ����-----------------//
		
		if (null != generalh && null != generalb && generalb.length > 0) {
			if (null != generalh.getVdiliveraddress()
					&& !"".equals(generalh.getVdiliveraddress())) {
				fydvo.setFyd_shdz(generalh.getVdiliveraddress()); // �ջ���ַ
			}
			if (null != generalh.getVnote() && !"".equals(generalh.getVnote())) {
				fydvo.setFyd_bz(generalh.getVnote()); // ��ע
			}
			if (null != generalh.getCdptid()
					&& !"".equals(generalh.getCdptid())) {
				fydvo.setCdeptid(generalh.getCdptid()); // ����
			}
			// �����˻���ʽ
			fydvo.setFyd_yhfs("����");
			// �������� 0 �����Ƶ� 1 ���۶��� 2 �ֳ�ֱ�� 3��ֶ���4 �ϲ����� 8 �������Ƶ������ɵ��˵�
			fydvo.setBilltype(billVo.getItype());
			fydvo.setVbillstatus(1);
			// ���ݺ�----------------------------------------------------------------
			fydvo.setVbillno(hybo.getBillNo(WdsWlPubConst.BILLTYPE_SEND_CONFIRM, billVo.getSLogCorp(), null, null));
			// �Ƶ�����
			fydvo.setDmakedate(billVo.getULogDate());
			fydvo.setVoperatorid(billVo.getSLogUser()); // �����Ƶ���
			// ���÷���վ
			fydvo.setSrl_pk(generalh.getSrl_pk());
			// ����վ
			fydvo.setSrl_pkr(generalh.getSrl_pkr());
			// --------------ת����ͷ����---------------//
			// --------------ת������----------------//
			List<TbFydmxnewVO> tbfydmxList = new ArrayList<TbFydmxnewVO>();
			for (int j = 0; j < generalb.length; j++) {
				TbFydmxnewVO fydmxnewvo = new TbFydmxnewVO();
				TbOutgeneralBVO genb = generalb[j];
				if (null != genb.getCinventoryid()
						&& !"".equals(genb.getCinventoryid())) {
					fydmxnewvo.setPk_invbasdoc(genb.getCinventoryid()); // ��Ʒ����
				}
				if (null != genb.getNshouldoutnum()
						&& !"".equals(genb.getNshouldoutnum())) {
					fydmxnewvo.setCfd_yfsl(genb.getNshouldoutnum()); // Ӧ������
				}
				if (null != genb.getNshouldoutassistnum()
						&& !"".equals(genb.getNshouldoutassistnum())) {
					fydmxnewvo.setCfd_xs(genb.getNshouldoutassistnum()); // ����
				}
				if (null != genb.getNoutnum() && !"".equals(genb.getNoutnum())) {
					fydmxnewvo.setCfd_sfsl(genb.getNoutnum()); // ʵ������
				}
				if (null != genb.getNoutassistnum()
						&& !"".equals(genb.getNoutassistnum())) {
					fydmxnewvo.setCfd_sffsl(genb.getNoutassistnum()); // ʵ��������
				}
				if (null != genb.getCrowno() && !"".equals(genb.getCrowno())) {
					fydmxnewvo.setCrowno(genb.getCrowno()); // �к�
				}
				if (null != genb.getUnitid() && !"".equals(genb.getUnitid())) {
					fydmxnewvo.setCfd_dw(genb.getUnitid()); // ��λ
				}
				fydmxnewvo.setCfd_pc(genb.getVbatchcode()); // ����		
				fydmxnewvo.setVsourcebillcode(WdsWlPubConst.BILLTYPE_OTHER_OUT);
				fydmxnewvo.setCsourcebillbid(genb.getGeneral_b_pk());
				fydmxnewvo.setCsourcebillhid(genb.getGeneral_pk());
				tbfydmxList.add(fydmxnewvo);
			}
			// ----------------ת���������---------------------//
				TbFydmxnewVO[] fydmxVO = new TbFydmxnewVO[tbfydmxList.size()];
				tbfydmxList.toArray(fydmxVO);
				fydmxList.add(fydmxVO);
				o[0] = true;
		HYBillVO newBillVo = new HYBillVO();
		newBillVo.setParentVO(fydvo);
		newBillVo.setChildrenVO(fydmxVO);
		saveBD(newBillVo, null);
	}
	}
	
	/**
	 * 
	 * @���ߣ�zhf
	 * @˵�������ɽ������Ŀ �����浥��������ϸ��ˮ�ӱ�
	 * @ʱ�䣺2011-4-7����02:28:26
	 * @param newBillVo  ������ ��浥��vo
	 * @param oldBillVo  ����ǰ��浥��vo
	 * @throws BusinessException
	 */
	private void insertTrayInfor(MyBillVO newBillVo,MyBillVO oldBillVo) throws BusinessException{
		if(newBillVo == null || oldBillVo == null){
			return;
		}
		TbOutgeneralBVO[] newbodys = (TbOutgeneralBVO[])newBillVo.getChildrenVO();
		TbOutgeneralBVO[] oldbodys = (TbOutgeneralBVO[])oldBillVo.getChildrenVO();	
		checkTrayUsed(oldbodys);
//		String[] tmps = null;
		for(TbOutgeneralBVO old : oldbodys){
			String oldno = old.getCrowno();
			for(TbOutgeneralBVO newbody:newbodys){
				String newno = newbody.getCrowno();
				if(oldno.equals(newno)){
					List<TbOutgeneralTVO> ltraytmp = old.getTrayInfor();
					if(ltraytmp == null || ltraytmp.size() == 0){
						throw new BusinessException("��������Ϣ������������Ϣʧ�ܣ�");
					}
					for(TbOutgeneralTVO tray:ltraytmp){
						tray.setGeneral_b_pk(newbody.getPrimaryKey());
						tray.setGeneral_pk(newbody.getGeneral_pk());
					}
					getSuperDMO().insertList(ltraytmp);
					TbOutgeneralTVO[] bvo = (TbOutgeneralTVO[])getSuperDMO().queryByWhereClause(TbOutgeneralTVO.class, " general_b_pk = '"+newbody.getPrimaryKey()+"' and isnull(dr,0) = 0 ");
					newbody.setTrayInfor(arrayTolist(bvo));
					break;
				}
			}
		}
	}
	
	public List<TbOutgeneralTVO> arrayTolist(TbOutgeneralTVO[] bvo){
		List<TbOutgeneralTVO> list  = null;
		if(bvo!=null && bvo.length>0){
			list = new ArrayList<TbOutgeneralTVO>();
			for(TbOutgeneralTVO b : bvo){
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
	private void checkTrayUsed(TbOutgeneralBVO[] bodys) throws BusinessException{
		String sql ="select cdt_traycode from bd_cargdoc_tray  " +
				" join tb_warehousestock on tb_warehousestock.pplpt_pk = bd_cargdoc_tray.cdt_pk " +
				" where bd_cargdoc_tray.cdt_pk=? and (bd_cargdoc_tray.cdt_traystatus=0 or  coalesce(tb_warehousestock.whs_stocktonnage,0)<? and tb_warehousestock.whs_stocktonnage>0)";
		SQLParameter para = new SQLParameter();
		for(TbOutgeneralBVO bvo:bodys){
			List<TbOutgeneralTVO> tray = bvo.getTrayInfor();
			for(int i=0;i<tray.size();i++){
				String cdp_pk =tray.get(i).getCdt_pk();
				UFDouble  noutnum = PuPubVO.getUFDouble_NullAsZero(tray.get(i).getNoutnum());
				para.addParam(cdp_pk);
				para.addParam(noutnum);
				Object o =getOutBO().getBaseDAO().executeQuery(sql, para, WdsPubResulSetProcesser.COLUMNPROCESSOR);
				if(o!=null){
					throw new BusinessException("��"+(i+1)+"�У�ָ��������:"+o.toString()+"\n�ѱ�����ʹ�ã�����������������ָ��");
				}
				para.clearParams();
			}
		}
		
	}
	
//	/**
//	 * ���ݳ����ӱ���Ϣɾ��������������
//	 * 
//	 * @param generalb
//	 *            �����ӱ�
//	 * @throws BusinessException
//	 */
//	private void deleteTrayInfor(TbOutgeneralBVO[] generalb)
//			throws BusinessException {
//		if (null != generalb && generalb.length > 0) {
//			for (int i = 0; i < generalb.length; i++) {
//				String strWhere = " pk_invbasdoc='"
//						+ generalb[i].getCinventoryid()
//						+ "' and cfirstbillbid ='"
//						+ generalb[i].getCsourcebillbid() + "'";
//				getBaseDao().deleteByClause(TbOutgeneralTVO.class, strWhere);
//			}
//		}
//	}

}
