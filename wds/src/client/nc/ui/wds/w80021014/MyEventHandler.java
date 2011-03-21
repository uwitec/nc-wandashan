package nc.ui.wds.w80021014;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.sun.org.apache.bcel.internal.generic.RETURN;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ArrayListProcessor;
import nc.ui.pub.ButtonObject;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessRuntimeException;
import nc.vo.pub.SuperVO;
import nc.vo.pub.bill.BillRendererVO;
import nc.vo.wds.w80021014.TbInvclBasVO;
import nc.vo.wds.w80021014.TbInvclVO;

/**
  *
  *该类是AbstractMyEventHandler抽象类的实现类，
  *主要是重载了按钮的执行动作，用户可以对这些动作根据需要进行修改
  *@author author
  *@version tempProject version
  */
  
  public class MyEventHandler 
                                          extends AbstractMyEventHandler{

	public MyEventHandler(BillManageUI billUI, IControllerBase control){
		super(billUI,control);		
	}
	public void showZeroLikeNull(boolean bShowZeroLikeNull) {
		BillRendererVO vo = getBillCardPanelWrapper().getBillCardPanel()
				.getBodyPanel().getRendererVO();
		vo.setShowZeroLikeNull(bShowZeroLikeNull);
		getBillCardPanelWrapper().getBillCardPanel().getBodyPanel()
				.setShowFlags(vo);
	}
	
	@Override
	public void onBoAdd(ButtonObject bo) throws Exception {
		// TODO Auto-generated method stub
		showZeroLikeNull(false);
		super.onBoAdd(bo);
	}
	
	@Override
	protected void onBoEdit() throws Exception {
		// TODO Auto-generated method stub
		showZeroLikeNull(false);
		super.onBoEdit();
	}
	@Override
	protected void onBoLineDel() throws Exception {
		// TODO Auto-generated method stub
		super.onBoLineDel();
	}
	@Override
	protected void onBoSave() throws Exception {
		// TODO Auto-generated method stub
		showZeroLikeNull(false);
	
		String invcode =getBillCardPanelWrapper().getBillCardPanel().getHeadItem("invcode").getValue();
		if(invcode==null||"".equals(invcode)){
			getBillUI().showErrorMessage("产品编号不能为空！");
			return;
		}
		 AggregatedValueObject mybillVO1;
			try {
				mybillVO1 = getBillUI().getVOFromUI();
			
			 TbInvclVO tbInvclVO = (TbInvclVO) mybillVO1.getParentVO();
			 TbInvclBasVO[] TbInvclBasVOs = (TbInvclBasVO[]) mybillVO1
						.getChildrenVO();
			 
			 ArrayList arr = new ArrayList();
			 if(TbInvclBasVOs!=null&&!"".equals(TbInvclBasVOs)){
				 for(int i=0;i<TbInvclBasVOs.length;i++){
					 if(null==TbInvclBasVOs[i].getPk_invbasdoc()||"".equals(TbInvclBasVOs[i].getPk_invbasdoc())){
						 getBillUI().showErrorMessage("存货明细不能为空！");
						 return;
					 }
					 else if(arr.contains(TbInvclBasVOs[i].getPk_invbasdoc())){
						 getBillUI().showErrorMessage("添加的产品已经和本系列绑定，请您重新选择！");
						 return;
					 }
					 else{
						 arr.add(TbInvclBasVOs[i].getPk_invbasdoc());
					 }
				 }
				 
			 }
			
			 
			 Object pk_invbasdoc = getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(
					 getBillCardPanelWrapper().getBillCardPanel().getBillTable().getSelectedRow(),
						"pk_invbasdoc");
				String sql ="select pk_invbasdoc from tb_invcl_bas where dr=0 ";
				if(tbInvclVO.getPk_invcl()!=null&&!"".equals(tbInvclVO.getPk_invcl())){
					sql +=" and pk_invcl!='"+tbInvclVO.getPk_invcl()+"'";
				}
				IUAPQueryBS query = (IUAPQueryBS) NCLocator.getInstance().lookup(
						IUAPQueryBS.class.getName());
				
				List list = (List)query.executeQuery(sql, new ArrayListProcessor());
				Iterator it = list.iterator();
				while(it.hasNext()){
					Object[] o = (Object[])it.next();
					if(pk_invbasdoc!=null&&!"".equals(pk_invbasdoc)){
						for(int i=0;i<o.length;i++){
							if(pk_invbasdoc.toString().equals(o[i])){
								getBillUI().showErrorMessage("添加的产品已经和其它系列绑定，请您先解除绑定关系！");
								return;
							}
							
						}
					}
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		super.onBoSave();
	}
	
	@Override
	protected void onBoQuery() throws Exception {
		// TODO Auto-generated method stub
		StringBuffer strWhere = new StringBuffer();
		
		if (askForQueryCondition(strWhere) == false)
			return;// 用户放弃了查询
		strWhere.append(" order by invcode ");
		SuperVO[] queryVos = queryHeadVOs(strWhere.toString());

		getBufferData().clear();
		// 增加数据到Buffer
		addDataToBuffer(queryVos);

		updateBuffer();
	}
	
	
		
}