package nc.ui.wds.w8004040204.ssButtun;

import nc.vo.trade.button.ButtonVO;

public class zjBtn {
	public ButtonVO getButtonVO(){
		ButtonVO btnVo = new ButtonVO();
		btnVo.setBtnNo(113);
		btnVo.setBtnCode("zj");
		btnVo.setBtnName("增加");
		btnVo.setBtnChinaName("增加");
			
				
		btnVo.setChildAry(new int[]{
		      		           ISsButtun.zk,
		      		           ISsButtun.cgqy,
		      		           ISsButtun.zzdj
		                });
		                
		return btnVo;
	}
}
