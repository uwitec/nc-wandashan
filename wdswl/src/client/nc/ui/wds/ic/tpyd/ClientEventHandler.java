package nc.ui.wds.ic.tpyd;

import nc.ui.pub.beans.UIDialog;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.wl.pub.WdsPubEnventHandler;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
import nc.vo.pub.ValidationException;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.scm.pub.vosplit.SplitBillVOs;
import nc.vo.wl.pub.CombinVO;
import nc.vo.wl.pub.WdsWlPubTool;

public class ClientEventHandler extends WdsPubEnventHandler {

	public ClientUIQueryDlg queryDialog = null;

	public ClientEventHandler(ClientUI clientUI, IControllerBase control) {
		super(clientUI, control);
	}

	protected UIDialog createQueryUI() {
		if (queryDialog == null) {
			queryDialog=new ClientUIQueryDlg(	getBillUI(),
					null,
					_getCorp().getPrimaryKey(),
					getBillUI()._getModuleCode(),
					_getOperator(),
					getBillUI().getBusinessType(),
					getBillUI().getNodeKey());
			//queryDialog = new ClientUIQueryDlg(getBillUI(), null, tempinfo);
		}
		return queryDialog;
	}
	
	@Override
	protected void onBoSave() throws Exception {
		
		beforeSaveCheck();
		BeforeSaveValudate();
		super.onBoSave();
	}
	/**
	 * 
	 * @作者：mlr
	 * @说明：完达山物流项目 
	 *      保存前的校验 ,只能移入空托盘    
	 * @时间：2011-7-19下午06:22:48
	 */	
	private void BeforeSaveValudate() throws Exception{
		AggregatedValueObject vo=getBillUI().getVOFromUI();
		if(vo.getChildrenVO()==null || vo.getChildrenVO().length==0)
			throw new ValidationException("数据为空");
		/*
		 *        对移出托盘的校验       
		 * 	                 表体 一个托盘只移入另个托盘一次
		 *          需校验：移入托盘数量不能大于移出托盘的数量
		 *          一个托盘移入到多个托盘
		 *          需校验: 移入的所有的托盘总量 不能大于移出托盘的量  
		 */ 
		SuperVO[] vos=(SuperVO[]) vo.getChildrenVO();
		if(vos == null || vos.length == 0)
			throw new ValidationException("表体数据为空");
		valuteOutValuteVbanchCode(vos);
		valuteOutCdt(vos);
		valuteInCdt(vos);;	
	}
	/**
	 * 
	 * @作者：mlr
	 * @说明：完达山物流项目 
	 *      对从多个托盘移出,移入同一个
	 * @时间：2011-7-21上午11:22:41
	 */
   private void valuteOutValuteVbanchCode(SuperVO[] vos) throws Exception{
	   if(vos==null && vos.length==0){
		   throw new Exception("表体数据为空"); 
	   }
	   SuperVO[][] voss=(SuperVO[][]) SplitBillVOs.getSplitVOs(vos,new String[]{"pk_trayin"});
	   if(voss==null || voss.length==0){
		  throw new Exception("表体数据为空"); 
	   }
	   for(int i=0;i<voss.length;i++){
		   String vbatchcode=PuPubVO.getString_TrimZeroLenAsNull(voss[i][0].getAttributeValue("vbanchcode"));
		   if(vbatchcode==null){
			   throw new Exception("存在空批次托盘");
		   }
		   for(int j=0;j<voss[i].length;j++){
			  if(!vbatchcode.equalsIgnoreCase(PuPubVO.getString_TrimZeroLenAsNull(voss[i][j].getAttributeValue("vbanchcode")))){
				throw new Exception("编码为 ["+PuPubVO.getString_TrimZeroLenAsNull(voss[i][j].getAttributeValue("intarycode"))+"]的移入托盘存在不同批次,不允许往一个托盘放不同批次的存货");  			  
			  }			   
		   }		   
	   }		
	}

/**
    * 
    * @作者：mlr
    * @说明：完达山物流项目 
    *      对移入托盘的校验
    * @时间：2011-7-21上午10:39:05
    * @param vos
    */
	private void valuteInCdt(SuperVO[] vos) throws Exception{
		/*
		 *       对移入托盘的校验
		 *       同一个托盘，移入的总量 不能 超出当前托盘的容量	       
		 */ 
		SuperVO[] newVos1=CombinVO.combinVoByFields(vos,new String[]{"pk_trayin"},new int[]{nc.vo.wl.pub.IUFTypes.UFD,nc.vo.wl.pub.IUFTypes.UFD}, new String[]{"nmovenum","nmoveassnum"});	
		int  size1=newVos1.length;
		UFDouble rnum = null;//移动主数量	
		UFDouble rbnum = null;	//移动辅数量	
		for(int i=0;i<size1;i++){				  
			//移出托盘辅数量
		     rbnum=PuPubVO.getUFDouble_NullAsZero(newVos1[i].getAttributeValue("nmoveassnum"));
			//从存货档案查询托盘容量
			String pk_invmandoc=PuPubVO.getString_TrimZeroLenAsNull(newVos1[i].getAttributeValue("pk_invmandoc"));
			UFDouble tray_volume=PuPubVO.getUFDouble_NullAsZero(WdsWlPubTool.execFomularClient("tray_volume->getColValue(wds_invbasdoc,tray_volume,pk_invmandoc,pk_invmandoc)", new String[]{"pk_invmandoc"}, new String[]{pk_invmandoc}));				  
			if((rbnum.sub(tray_volume)).doubleValue()>0){				 
				throw new Exception("编码为  ["+newVos1[i].getAttributeValue("intarycode")+"]移入数量超出当前托盘的最大容量");
			}			  				
		}		
	}
   /**
    * 
    * @作者：mlr
    * @说明：完达山物流项目 
    *     对移出托盘的校验  
    * @时间：2011-7-21上午10:39:27
    * @param vos
    */
	private void valuteOutCdt(SuperVO[] vos) throws Exception {
		//将移出托盘编码相同的合并到一起   将移入数量 求和
		SuperVO[] newVos=CombinVO.combinVoByFields(vos,new String[]{"outtraycode"},new int[]{nc.vo.wl.pub.IUFTypes.UFD,nc.vo.wl.pub.IUFTypes.UFD}, new String[]{"nmovenum","nmoveassnum"});	
		if(newVos==null || newVos.length==0)
			throw new ValidationException("数据为空");
		int  size=newVos.length;	
		UFDouble cnum = null;//移出托盘库存主数量	
		UFDouble cbnum = null;//移出托盘库存辅数量
		UFDouble rnum = null;//移动主数量	
		UFDouble rbnum = null;	//移动辅数量	
		for(int i=0;i<size;i++){			
			 cnum=PuPubVO.getUFDouble_NullAsZero(newVos[i].getAttributeValue("noutnum"));			
			 cbnum=PuPubVO.getUFDouble_NullAsZero(newVos[i].getAttributeValue("noutassnum"));			
			 rnum=PuPubVO.getUFDouble_NullAsZero(newVos[i].getAttributeValue("nmovenum"));		
			 rbnum=PuPubVO.getUFDouble_NullAsZero(newVos[i].getAttributeValue("nmoveassnum"));
			if((cnum.sub(rnum)).doubleValue()<0||(cbnum.sub(rbnum)).doubleValue()<0){
				throw new Exception("编码为  ["+newVos[i].getAttributeValue("outtraycode")+"]移出数量超出当前托盘的存量");
			}			  				
		}		
	}

	/**
	 * 
	 * @作者：lyf
	 * @说明：完达山物流项目 
	 *       表体保存前校验
	 *       
	 * @时间：2011-3-23下午09:05:20
	 * @throws Exception
	 */
	protected void beforeSaveCheck() throws Exception{
		if(getBillUI().getVOFromUI()==null)
			throw new ValidationException("数据为空");
		if(getBillUI().getVOFromUI().getChildrenVO()==null||
				getBillUI().getVOFromUI().getChildrenVO().length==0	){
			throw new BusinessException("表体不允许为空");
		}
	}
}
