package nc.ui.wds.w80020204;

import java.util.Iterator;
import java.util.List;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.uap.IVOPersistence;
import nc.jdbc.framework.processor.ArrayListProcessor;
import nc.ui.pub.ClientEnvironment;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.SuperVO;
import nc.vo.pub.bill.BillRendererVO;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.lang.UFTime;
import nc.vo.to.pub.ConstVO;
import nc.vo.wds.w80020204.MyBillVO;
import nc.vo.wds.w80020204.TBCARTRACKINGVO;
import nc.vo.wds.w80060406.TbFydnewVO;


/**
 * 
 * ������AbstractMyEventHandler�������ʵ���࣬ ��Ҫ�������˰�ť��ִ�ж������û����Զ���Щ����������Ҫ�����޸�
 * 
 * @author author
 * @version tempProject version
 */

public class MyEventHandler extends AbstractMyEventHandler {
	
	
	private MyClientUI myClientUI;
	
	// ��ѯ�ӿ�
	IUAPQueryBS query = (IUAPQueryBS) NCLocator.getInstance().lookup(
			IUAPQueryBS.class.getName());
	// ����ӿ�
	IVOPersistence ivo = (IVOPersistence) NCLocator.getInstance().lookup(
			IVOPersistence.class.getName());
	
	

	public MyEventHandler(BillManageUI billUI, IControllerBase control) {
		super(billUI, control);
		myClientUI=(MyClientUI)billUI;
	}
	
	
	public void showZeroLikeNull(boolean bShowZeroLikeNull) {
		BillRendererVO vo = getBillCardPanelWrapper().getBillCardPanel()
				.getBodyPanel().getRendererVO();
		vo.setShowZeroLikeNull(bShowZeroLikeNull);
		getBillCardPanelWrapper().getBillCardPanel().getBodyPanel()
				.setShowFlags(vo);
	}
	
	@Override
	protected void onAdd() {
		// TODO Auto-generated method stub
		//param1 ��¼�˹�˾���� 2��¼�˵����� 3�������� 5���ݺ� 6��ѯģ��
		CarTrackingbillDlg prodWaybillDlg = new CarTrackingbillDlg(myClientUI);
			AggregatedValueObject[] vos = prodWaybillDlg.getReturnVOs(
					ClientEnvironment.getInstance().getCorporation()
							.getPrimaryKey(), ClientEnvironment.getInstance()
							.getUser().getPrimaryKey(), "02020400",
					ConstVO.m_sBillDRSQ, "80020204", "020204", myClientUI);
			
			
			
			try {

				if (null == vos || vos.length == 0) {

					return;
				}
				if (vos.length > 2) {
					getBillUI().showErrorMessage("һ��ֻ��ѡ��һ���˵���");
					return;
				}
				// TbProdwaybillVO genh = (TbProdwaybillVO) vos[0].getParentVO();
				// if (((BillManageUI)getBillUI()).isListPanelSelected())
				// ((BillManageUI)getBillUI()).setCurrentPanel(BillTemplateWrapper.CARDPANEL);

				MyBillVO voForSave = changeTbPwbtoTbgen(vos);
				
				getBufferData().clear();
				getBufferData().addVOToBuffer(voForSave);
				updateBuffer();
				// getBillUI().setBillOperate(IBillOperate.OP_EDIT);
				super.onBoEdit();
				getButtonManager().getButton(IBillButton.Edit).setEnabled(false);

				getButtonManager().getButton(IBillButton.Save).setEnabled(true);
				getButtonManager().getButton(IBillButton.Cancel).setEnabled(true);
				getButtonManager().getButton(IBillButton.Refresh).setEnabled(false);
				getButtonManager().getButton(IBillButton.Query).setEnabled(false);
				
					getButtonManager().getButton(
							nc.ui.wds.w80020204.ISsButtun.Adbtn)
							.setEnabled(true);
				
				
			
				getBillUI().updateButtonUI();

			} catch (Exception e) {
				getBillUI().showErrorMessage(e.getMessage());
			}
			showZeroLikeNull(false);
	}

	
	private MyBillVO changeTbPwbtoTbgen(AggregatedValueObject[] vos) {
		
		MyBillVO myBillVO = new MyBillVO();
		try{
			
		// ������������VO
		TBCARTRACKINGVO TBCARTRACKINGVO = new TBCARTRACKINGVO();
		// ���˵�����VO
		TbFydnewVO firstVO = (TbFydnewVO) vos[0].getParentVO();

		


		// ��ӷ��˵�����
		TBCARTRACKINGVO.setPk_fydnew(firstVO.getFyd_pk());

		// ��ӷ��˵����ݺ�
		TBCARTRACKINGVO.setDdh(firstVO.getVbillno());
		// ��ӷ��˵��ͻ�����
		TBCARTRACKINGVO.setCustcode(firstVO.getFyd_khdm());
		//��Ӿ���������
		TBCARTRACKINGVO.setCustname(firstVO.getPk_kh());
		// ��ӷ��˵����˳���
		if (null != firstVO.getSrl_pk()) {
			TBCARTRACKINGVO.setStorname(firstVO.getSrl_pk());
			 }
		// ��ӷ��˵�����ʱ��
		TBCARTRACKINGVO.setQydate(new UFDate(firstVO.getDmaketime().substring(0, 10)));
		// ��ӷ��˵�������˾����
		if (null != firstVO.getTc_pk()) {
			TBCARTRACKINGVO.setTc_comname(firstVO.getTc_pk());
			 }
		// ��ӷ��˵����ݺŽӶ���ʱ��
		TBCARTRACKINGVO.setJddate(firstVO.getDmakedate());
		// ��ӷ��˵��ջ��绰
		TBCARTRACKINGVO.setFyd_lxdh(firstVO.getFyd_lxdh());
		// ��ӷ��˵��ջ���ַ
		TBCARTRACKINGVO.setFyd_shdz(firstVO.getFyd_shdz());
		// �õ��ӱ������������ĺ� ����ӷ��˵�����������
		List list = (List) query
		.executeQuery(
				"select sum(tb_fydmxnew.cfd_sfsl),sum(tb_fydmxnew.cfd_sffsl) from tb_fydmxnew where tb_fydmxnew.fyd_pk ='"+firstVO.getFyd_pk()+"' ",
				new ArrayListProcessor());
Iterator it = list.iterator();
while(it.hasNext()){
	Object[] o = (Object[])it.next();
	TBCARTRACKINGVO.setClzz_zl(new UFDouble(Double.parseDouble(o[0].toString())));
	TBCARTRACKINGVO.setClzz_sl(new UFDouble(Double.parseDouble(o[1].toString())));
}
		
		
		// �������VO
		myBillVO.setParentVO(TBCARTRACKINGVO);

		
		}catch(Exception e){
			e.printStackTrace();
		}
		return myBillVO;
	}
	
	
	
	@Override
	protected void onBoQuery() throws Exception {
		// TODO Auto-generated method stub

	
		TBCARTRACKINGVO tb = null;
		/*
		 * List list1 = (List) i.retrieveByClause(TBCARTRACKINGVO.class, "");
		 * Iterator it = list1.iterator(); // �ȵõ����乫˾׷�ٹ���voList
		 * 
		 * while (it.hasNext()) { double zl = 0.0; double sl = 0.0; tb =
		 * (TBCARTRACKINGVO) it.next(); List list3 = (List)
		 * i.retrieveByClause(TbFydmxnewVO.class, "fyd_pk='" + tb.getPk_fydnew() +
		 * "'"); Iterator it3 = list3.iterator();
		 */
		// �õ����˵���ϸvoList
		/*
		 * TbFydmxnewVO tb3 = null; while (it3.hasNext()) { tb3 = (TbFydmxnewVO)
		 * it3.next(); if(tb3.getCfd_sfsl()==null){ zl=0; }else{ zl +=
		 * tb3.getCfd_sfsl().doubleValue(); } if(tb3.getCfd_sffsl()==null){
		 * sl=0; }else{ sl += tb3.getCfd_sffsl().doubleValue(); } }
		 * tb.setClzz_sl(new UFDouble(sl)); tb.setClzz_zl(new UFDouble(zl));
		 */
	/*	String sql = "select e.custcode custcode,e.custname custname, "
				+ " case when b.fyd_ddh is not null then b.fyd_ddh "
				+ " when b.splitvbillno is not null then b.splitvbillno else b.vbillno  end ddh,"
				+ "sum(g.cfd_sffsl) sl,sum(g.cfd_sfsl) zl,b.srl_pk fhz, "
				+ " b.fyd_shdz dz,b.fyd_lxdh dh,b.dmaketime jjdate,f.tc_pk,c.pk_cartracking,c.pk_fydnew "
				+ "from tb_fydnew b,tb_cartracking c,bd_cumandoc d,bd_cubasdoc e,tb_transcompany f,tb_fydmxnew g "
				+ "where b.fyd_pk=c.pk_fydnew and b.pk_kh=d.pk_cumandoc and d.pk_cubasdoc=e.pk_cubasdoc and b.tc_pk=f.tc_pk and b.fyd_pk=g.fyd_pk "
				+ " group by e.custcode,e.custname, b.fyd_ddh,b.splitvbillno,b.vbillno,b.srl_pk,b.fyd_shdz,b.fyd_lxdh,b.dmaketime,f.tc_pk,c.pk_cartracking,c.pk_fydnew";
		List ll = (List) i.executeQuery(sql, new ArrayListProcessor());
		Iterator itt = ll.iterator();
		while (itt.hasNext()) {
			tb= new TBCARTRACKINGVO();
			Object a[] = (Object[]) itt.next();
			if (a != null && a.length > 0 && a[0] != null) {
				if (null != a[0] && !"".equals(a[0])) {
					tb.setCustcode(a[0].toString());
				}
				if (null != a[1] && !"".equals(a[1])) {
					tb.setCustname(a[1].toString());
				}
				if (null != a[2] && !"".equals(a[5])) {
					tb.setDdh(a[2].toString());
				}
				if (null != a[3] && !"".equals(a[3])) {
					tb.setClzz_sl(new UFDouble(a[3].toString()));
				}
				if (null != a[4] && !"".equals(a[4])) {
					tb.setClzz_zl(new UFDouble(a[4].toString()));
				}
				if (null != a[5] && !"".equals(a[5])) {
					tb.setStorname(a[5].toString());
				}
				if (null != a[6] && !"".equals(a[6])) {
					tb.setFyd_shdz(a[6].toString());
				}
				if (null != a[7] && !"".equals(a[7])) {
					tb.setFyd_lxdh(a[7].toString());
				}
				if (null != a[8] && !"".equals(a[8])) {
					tb.setJddate(new UFDate(a[8].toString()));
				}
				if (null != a[9] && !"".equals(a[9])) {
					tb.setTc_comname(a[9].toString());
				}
				if (null != a[10] && !"".equals(a[10])) {
					tb.setPk_cartracking(a[10].toString());
				}
				if (null != a[11] && !"".equals(a[11])) {
					tb.setPk_fydnew(a[11].toString());
				}
			}
			ivo.updateVO(tb);
		}*/

		StringBuffer strWhere = new StringBuffer();

		if (askForQueryCondition(strWhere) == false)
			return;// �û������˲�ѯ

		SuperVO[] queryVos = queryHeadVOs(strWhere.toString());

		getBufferData().clear();
		// �������ݵ�Buffer
		addDataToBuffer(queryVos);

		updateBuffer();
	}

}