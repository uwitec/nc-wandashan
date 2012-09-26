package nc.ui.wds.trans;
import nc.ui.trade.manage.ManageEventHandler;
import nc.vo.pub.CircularlyAccessibleValueObject;
/**
 * 调拨入库运费计算  基础信息维护 
 * 
 * 维护信息  调出公司  调入仓库   货品类型 
 *  
 * 运费计算时  按 这三个维度  来查询找该运价表   计算运费
 *  
 * @author mlr
 *
 */
 public class MyClientUI extends AbstractMyClientUI{      
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected ManageEventHandler createEventHandler() {
		return new MyEventHandler(this, getUIControl());
	}
       
	public void setBodySpecialData(CircularlyAccessibleValueObject[] vos)
			throws Exception {}

	protected void setHeadSpecialData(CircularlyAccessibleValueObject vo,
			int intRow) throws Exception {}

	protected void setTotalHeadSpecialData(CircularlyAccessibleValueObject[] vos)
			throws Exception {	}

	protected void initSelfData() {	}

	public void setDefaultData() throws Exception {
	   getBillCardWrapper().getBillCardPanel().getHeadItem("pk_corp").setValue(getCorpPrimaryKey());
	}
}
