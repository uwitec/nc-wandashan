package nc.ui.wds.w80060206.pwButtun;

import nc.ui.trade.base.IBillOperate;
import nc.vo.trade.button.ButtonVO;
import nc.vo.trade.pub.IBillStatus;

/**
 * <b> 在此处简要描述此类的功能 </b><br>
 *
 * <p>
 *     在此处添加此类的描述信息
 * </p>
 *
 *
 * @author authorName
 * @version tempProject version
 */

public class PwAddBtn {

	public ButtonVO getButtonVO(){
		ButtonVO btnVo = new ButtonVO();
		btnVo.setBtnNo(116);
		btnVo.setBtnCode("PwAdd");
		btnVo.setBtnName("增加运单");
		btnVo.setBtnChinaName("增加运单");
			
				
		btnVo.setChildAry(new int[]{
		      		                   
		                });
		                
		return btnVo;
	}

}
