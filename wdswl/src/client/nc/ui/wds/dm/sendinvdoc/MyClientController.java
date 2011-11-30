package nc.ui.wds.dm.sendinvdoc;

import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.bill.ISingleController;
import nc.ui.trade.businessaction.IBusinessActionType;
import nc.ui.trade.button.IBillButton;
import nc.vo.trade.pub.HYBillVO;
import nc.vo.wds.dm.sendinvdoc.SendinvdocVO;
import nc.vo.wl.pub.WdsWlPubConst;

public class MyClientController extends AbstractManageController implements ISingleController {

	public String[] getCardBodyHideCol() {//等到卡片式隐藏主体
		
		return null;
	}

	public boolean isShowCardRowNo() {//是否显示行
		
		return true;
	}

	public boolean isShowCardTotal() {//是否显示表体
		
		return true;
	}

	public String getBillType() {//返回账单类型
		
		return WdsWlPubConst.DM_PLAN_BASDOC_NODECODE;
	}

	public String[] getBillVoName() {//得到账单名字
		
		return new String[]{
			HYBillVO.class.getName(),//聚合VO名
			SendinvdocVO.class.getName(),	//主表类名		
			SendinvdocVO.class.getName()	//字表类名			
		};
	}

	public String getBodyCondition() {//得到主体条件
		
		return null;
	}

	public String getBodyZYXKey() {//得到主体KEY值
		
		return null;
	}

	public int getBusinessActionType() {//得到事物活动类型
		
		return IBusinessActionType.BD;
	}

	public String getChildPkField() {
		
		return null;
	}

	public String getHeadZYXKey() {
		
		return null;
	}

	public String getPkField() {
	
		return "pk_wds_sendinvdoc";
	}

	public Boolean isEditInGoing() throws Exception {
		
		return null;
	}

	public boolean isExistBillStatus() {
		
		return false;
	}

	public boolean isLoadCardFormula() {
		
		return true;
	}

	public boolean isSingleDetail() {
		
		return false;
	}

	public String[] getListBodyHideCol() {
		// TODO Auto-generated method stub
		return null;
	}

	public int[] getListButtonAry() {//等到按钮
		return new int[]{
			IBillButton.Add,
			IBillButton.Delete,
			IBillButton.Query,
			IBillButton.Edit,
			IBillButton.Save,
			IBillButton.Cancel,
			IBillButton.Card,
			IBillButton.Print
		};
	}

	public String[] getListHeadHideCol() {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isShowListRowNo() {
		// TODO Auto-generated method stub
		return true;
	}

	public boolean isShowListTotal() {
		// TODO Auto-generated method stub
		return false;
	}
	public int[] getCardButtonAry() {
		return new int[]{
				IBillButton.Add,
				IBillButton.Delete,
				IBillButton.Query,
				IBillButton.Edit,
				IBillButton.Save,
				IBillButton.Cancel,
				IBillButton.Return,
				IBillButton.Print
			};
	}

}
