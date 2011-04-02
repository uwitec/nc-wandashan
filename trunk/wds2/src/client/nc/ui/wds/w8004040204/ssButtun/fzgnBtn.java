package nc.ui.wds.w8004040204.ssButtun;

import nc.ui.trade.base.IBillOperate;
import nc.vo.trade.button.ButtonVO;
import nc.vo.trade.pub.IBillStatus;
import nc.ui.wds.w8004040204.ssButtun.ISsButtun;  
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

public class fzgnBtn {

	public ButtonVO getButtonVO(){
		ButtonVO btnVo = new ButtonVO();
		btnVo.setBtnNo(120);
		btnVo.setBtnCode("fzgn");
		btnVo.setBtnName("辅助功能");
		btnVo.setBtnChinaName("辅助功能");
			
				
		btnVo.setChildAry(new int[]{
		      		        		          ISsButtun.tpzd,
		        		      		        		          ISsButtun.zdqh,
		        		      		        		           ISsButtun.ckmx
		        		      		                   
		                });
		                
		return btnVo;
	}

}
