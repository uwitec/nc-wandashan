package nc.ui.wds.w8000;

import java.util.ArrayList;

import nc.bs.framework.common.NCLocator; 
import nc.itf.wds.w80060401.Iw80060401;
import nc.ui.pf.pub.PfUIDataCache;
import nc.ui.pub.pf.IUIBeforeProcAction;
import nc.ui.trade.base.AbstractBillUI;
import nc.ui.trade.businessaction.BdBusinessAction;
import nc.ui.trade.businessaction.IPFACTION;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.billtype.BilltypeVO;
import nc.vo.trade.pub.IBDGetCheckClass2;

public class W80060401Action extends BdBusinessAction {
	private AbstractBillUI billUI=null;

	public W80060401Action(AbstractBillUI billUI){
		super();
		this.billUI=billUI;
	}
	Iw80060401 iw = (Iw80060401) NCLocator.getInstance().lookup(
			Iw80060401.class.getName());
	 /**
     * save 方法注解。
     */
	public AggregatedValueObject save(AggregatedValueObject billVO,
			String billType, String billDate, Object userObj,
			AggregatedValueObject checkVo) throws Exception {
		ArrayList param=(ArrayList)userObj;
		if (param.get(0) instanceof IBDGetCheckClass2) {
            RunUIBeforeCheck(((IBDGetCheckClass2) param.get(0)).getUICheckClass(),
                    billType, IPFACTION.SAVE, param.get(0), checkVo);
        } else {
            //如果数据库注册，按照数据库执行，如果未注册，按照以前机制运行
            BilltypeVO billVo = PfUIDataCache.getBillType(billType);
            //动作执行前的事前处理
            String strClassName =billVo==null?null:billVo.getDef3();
            RunUIBeforeCheck(strClassName, billType, IPFACTION.SAVE, param.get(0),
                    checkVo);

        } 
		AggregatedValueObject retVo = iw.saveBD80060401(billVO, userObj); 
        if (((ArrayList)userObj).get(0) instanceof nc.vo.trade.pub.IRetCurrentDataAfterSave) {
            fillUITotalVO(checkVo.getChildrenVO(), retVo.getChildrenVO());
            checkVo.setParentVO(retVo.getParentVO());
            retVo = checkVo;
        }
        return retVo;
	}
	
	/**
     * delete 方法注解。
     */
    public nc.vo.pub.AggregatedValueObject delete(
            nc.vo.pub.AggregatedValueObject billVO, String billType,
            String billDate, Object userObj) throws Exception {
    	ArrayList param=(ArrayList)userObj;
        if (param.get(0) instanceof IBDGetCheckClass2) {
            RunUIBeforeCheck(((IBDGetCheckClass2) param.get(0)).getUICheckClass(),
                    billType, IPFACTION.DELETE, param.get(0), billVO);
        } else {
            //如果数据库注册，按照数据库执行，如果未注册，按照以前机制运行
            BilltypeVO billVo = PfUIDataCache.getBillType(billType);
            //动作执行前的事前处理
            String strClassName =billVo==null?null:billVo.getDef3();
            RunUIBeforeCheck(strClassName, billType, IPFACTION.DELETE, param.get(0),
                    billVO);
        } 
		AggregatedValueObject retVo = iw.deleteBD80060401(billVO, userObj);
        return null;
    }
	
	private void RunUIBeforeCheck(String checkClsName, String billType,
            String actionName, Object userObj, AggregatedValueObject checkVo)
            throws Exception {
        if (checkClsName == null || checkClsName.length() == 0)
            return;
        Class c = Class.forName(checkClsName);
        Object o = c.newInstance();
        if (o instanceof IUIBeforeProcAction) {
            ((IUIBeforeProcAction) o).runClass(billUI, billType, actionName,
                    checkVo, userObj);
        }
    }
	
}
