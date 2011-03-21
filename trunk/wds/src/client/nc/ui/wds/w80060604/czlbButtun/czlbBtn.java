package nc.ui.wds.w80060604.czlbButtun;

import nc.ui.wds.w8004040204.ssButtun.ISsButtun;
import nc.vo.trade.button.ButtonVO;

public class czlbBtn {

	public ButtonVO getButtonVO(){
		ButtonVO btnVo = new ButtonVO();
		btnVo.setBtnNo(125);
		btnVo.setBtnCode("cz");
		btnVo.setBtnName("操作类别");
		btnVo.setBtnChinaName("操作类别");
			
				
		btnVo.setChildAry(new int[]{
		      		        		           ISsButtun.cfdd,
		      		        		         ISsButtun.hbdd,
		      		        		       //ISsButtun.fczl,
		      		        		         ISsButtun.zcdd
		        		      		                   
		                });
		                
		return btnVo;
	}
	
}
