package nc.ui.wds.w8004040204.ssButtun;

import nc.ui.trade.base.IBillOperate;
import nc.vo.trade.button.ButtonVO;
import nc.vo.trade.pub.IBillStatus;
import nc.ui.wds.w8004040204.ssButtun.ISsButtun;  
/**
 * <b> �ڴ˴���Ҫ��������Ĺ��� </b><br>
 *
 * <p>
 *     �ڴ˴���Ӵ����������Ϣ
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
		btnVo.setBtnName("��������");
		btnVo.setBtnChinaName("��������");
			
				
		btnVo.setChildAry(new int[]{
		      		        		          ISsButtun.tpzd,
		        		      		        		          ISsButtun.zdqh,
		        		      		        		           ISsButtun.ckmx
		        		      		                   
		                });
		                
		return btnVo;
	}

}
