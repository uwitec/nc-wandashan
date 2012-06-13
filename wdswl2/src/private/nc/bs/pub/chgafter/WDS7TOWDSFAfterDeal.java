package nc.bs.pub.chgafter;
import nc.vo.ic.other.in.OtherInBillVO;
import nc.vo.pf.change.IchangeVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
import nc.vo.wds.load.account.ExaggLoadPricVO;
import nc.vo.wds.load.account.LoadpriceB2VO;
import nc.bs.wl.pub.SingleVOChangeDataBsTool;
public class WDS7TOWDSFAfterDeal  implements IchangeVO{
	/**
	 * 根据产品组自己的需求，把源VO中信息通过运算进行转换
	 * 
	 * @param preVo 源单据聚合VO
	 * @param nowVo 目的单据聚合VO
	 * @return 目的单据聚合VO
	 * @throws BusinessException
	 */
	public AggregatedValueObject retChangeBusiVO(AggregatedValueObject preVo,
			AggregatedValueObject nowVo) throws BusinessException {
       if(preVo==null || nowVo==null){
    	   return nowVo;
       }
       OtherInBillVO pvo=(OtherInBillVO) preVo;
       ExaggLoadPricVO nvo=(ExaggLoadPricVO) nowVo;
       if(pvo.getTableVO(pvo.getTableCodes()[1])!=null && pvo.getTableVO(pvo.getTableCodes()[1]).length!=0){
    	   SuperVO[] vos=(SuperVO[]) pvo.getTableVO(pvo.getTableCodes()[1]);
    	   LoadpriceB2VO[] newvos=null;
    	   if(vos.length>0){
    		   try {
    			   newvos=(LoadpriceB2VO[]) SingleVOChangeDataBsTool.runChangeVOAry(vos, LoadpriceB2VO.class, "nc.bs.pf.changedir.CHGSALETOLOAD");
    			   } catch (Exception e) {
    				   e.printStackTrace();   
    				  throw new BusinessException(e.getMessage());			
    			  } 		   
    	   }
    	  
//    	   for(int i=0;i<vos.length;i++){
//    		   LoadpriceB2VO newvo=new LoadpriceB2VO();
//    		   BeanProTool.copyBeanParm(vos[i], newvo);
//    		   newvo.setPrimaryKey(null);
//    		   newvo.setPk_loadprice(null);
//    		   newvos[i]=newvo; 		   
//    	   }
    	   nvo.setTableVO(nvo.getTableCodes()[1], newvos);
       }	
       nvo.setTableVO(nvo.getTableCodes()[0],nvo.getChildrenVO()); 
		return nvo;
	}

	public AggregatedValueObject[] retChangeBusiVOs(
			AggregatedValueObject[] preVos, AggregatedValueObject[] nowVos)
			throws BusinessException {
		if(nowVos ==null || nowVos.length==0){
			return null;
		}
		return nowVos;
	}


}
