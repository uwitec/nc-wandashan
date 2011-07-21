package nc.ui.zb.price.local;

import nc.ui.zb.price.pub.AbstractPriceDataBuffer;

public class LocalPriceDataBuffer extends AbstractPriceDataBuffer{
	
	private String ccirclenoid = null;//当前报价次数    招标类型为 现场报价的 标段 设计一个 报价次数子表
//	同 招标类型为网上招标的 轮次子表   根据现场报价清空生成报价轮次数据   该字段为上次报价的轮次id
	public void setCcirclenoid(String ccirclenoid) {
		this.ccirclenoid = ccirclenoid;
	}

	@Override
	public String getCurrentCircalID() {
		// TODO Auto-generated method stub
		return ccirclenoid;
	}	
}
