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
	 * @���ߣ�mlr
	 * @˵�������ɽ������Ŀ 
	 *      ����ǰ��У�� ,ֻ�����������    
	 * @ʱ�䣺2011-7-19����06:22:48
	 */	
	private void BeforeSaveValudate() throws Exception{
		AggregatedValueObject vo=getBillUI().getVOFromUI();
		if(vo.getChildrenVO()==null || vo.getChildrenVO().length==0)
			throw new ValidationException("����Ϊ��");
		/*
		 *        ���Ƴ����̵�У��       
		 * 	                 ���� һ������ֻ�����������һ��
		 *          ��У�飺���������������ܴ����Ƴ����̵�����
		 *          һ���������뵽�������
		 *          ��У��: ��������е��������� ���ܴ����Ƴ����̵���  
		 */ 
		SuperVO[] vos=(SuperVO[]) vo.getChildrenVO();
		if(vos == null || vos.length == 0)
			throw new ValidationException("��������Ϊ��");
		valuteOutValuteVbanchCode(vos);
		valuteOutCdt(vos);
		valuteInCdt(vos);;	
	}
	/**
	 * 
	 * @���ߣ�mlr
	 * @˵�������ɽ������Ŀ 
	 *      �ԴӶ�������Ƴ�,����ͬһ��
	 * @ʱ�䣺2011-7-21����11:22:41
	 */
   private void valuteOutValuteVbanchCode(SuperVO[] vos) throws Exception{
	   if(vos==null && vos.length==0){
		   throw new Exception("��������Ϊ��"); 
	   }
	   SuperVO[][] voss=(SuperVO[][]) SplitBillVOs.getSplitVOs(vos,new String[]{"pk_trayin"});
	   if(voss==null || voss.length==0){
		  throw new Exception("��������Ϊ��"); 
	   }
	   for(int i=0;i<voss.length;i++){
		   String vbatchcode=PuPubVO.getString_TrimZeroLenAsNull(voss[i][0].getAttributeValue("vbanchcode"));
		   if(vbatchcode==null){
			   throw new Exception("���ڿ���������");
		   }
		   for(int j=0;j<voss[i].length;j++){
			  if(!vbatchcode.equalsIgnoreCase(PuPubVO.getString_TrimZeroLenAsNull(voss[i][j].getAttributeValue("vbanchcode")))){
				throw new Exception("����Ϊ ["+PuPubVO.getString_TrimZeroLenAsNull(voss[i][j].getAttributeValue("intarycode"))+"]���������̴��ڲ�ͬ����,��������һ�����̷Ų�ͬ���εĴ��");  			  
			  }			   
		   }		   
	   }		
	}

/**
    * 
    * @���ߣ�mlr
    * @˵�������ɽ������Ŀ 
    *      ���������̵�У��
    * @ʱ�䣺2011-7-21����10:39:05
    * @param vos
    */
	private void valuteInCdt(SuperVO[] vos) throws Exception{
		/*
		 *       ���������̵�У��
		 *       ͬһ�����̣���������� ���� ������ǰ���̵�����	       
		 */ 
		SuperVO[] newVos1=CombinVO.combinVoByFields(vos,new String[]{"pk_trayin"},new int[]{nc.vo.wl.pub.IUFTypes.UFD,nc.vo.wl.pub.IUFTypes.UFD}, new String[]{"nmovenum","nmoveassnum"});	
		int  size1=newVos1.length;
		UFDouble rnum = null;//�ƶ�������	
		UFDouble rbnum = null;	//�ƶ�������	
		for(int i=0;i<size1;i++){				  
			//�Ƴ����̸�����
		     rbnum=PuPubVO.getUFDouble_NullAsZero(newVos1[i].getAttributeValue("nmoveassnum"));
			//�Ӵ��������ѯ��������
			String pk_invmandoc=PuPubVO.getString_TrimZeroLenAsNull(newVos1[i].getAttributeValue("pk_invmandoc"));
			UFDouble tray_volume=PuPubVO.getUFDouble_NullAsZero(WdsWlPubTool.execFomularClient("tray_volume->getColValue(wds_invbasdoc,tray_volume,pk_invmandoc,pk_invmandoc)", new String[]{"pk_invmandoc"}, new String[]{pk_invmandoc}));				  
			if((rbnum.sub(tray_volume)).doubleValue()>0){				 
				throw new Exception("����Ϊ  ["+newVos1[i].getAttributeValue("intarycode")+"]��������������ǰ���̵��������");
			}			  				
		}		
	}
   /**
    * 
    * @���ߣ�mlr
    * @˵�������ɽ������Ŀ 
    *     ���Ƴ����̵�У��  
    * @ʱ�䣺2011-7-21����10:39:27
    * @param vos
    */
	private void valuteOutCdt(SuperVO[] vos) throws Exception {
		//���Ƴ����̱�����ͬ�ĺϲ���һ��   ���������� ���
		SuperVO[] newVos=CombinVO.combinVoByFields(vos,new String[]{"outtraycode"},new int[]{nc.vo.wl.pub.IUFTypes.UFD,nc.vo.wl.pub.IUFTypes.UFD}, new String[]{"nmovenum","nmoveassnum"});	
		if(newVos==null || newVos.length==0)
			throw new ValidationException("����Ϊ��");
		int  size=newVos.length;	
		UFDouble cnum = null;//�Ƴ����̿��������	
		UFDouble cbnum = null;//�Ƴ����̿�渨����
		UFDouble rnum = null;//�ƶ�������	
		UFDouble rbnum = null;	//�ƶ�������	
		for(int i=0;i<size;i++){			
			 cnum=PuPubVO.getUFDouble_NullAsZero(newVos[i].getAttributeValue("noutnum"));			
			 cbnum=PuPubVO.getUFDouble_NullAsZero(newVos[i].getAttributeValue("noutassnum"));			
			 rnum=PuPubVO.getUFDouble_NullAsZero(newVos[i].getAttributeValue("nmovenum"));		
			 rbnum=PuPubVO.getUFDouble_NullAsZero(newVos[i].getAttributeValue("nmoveassnum"));
			if((cnum.sub(rnum)).doubleValue()<0||(cbnum.sub(rbnum)).doubleValue()<0){
				throw new Exception("����Ϊ  ["+newVos[i].getAttributeValue("outtraycode")+"]�Ƴ�����������ǰ���̵Ĵ���");
			}			  				
		}		
	}

	/**
	 * 
	 * @���ߣ�lyf
	 * @˵�������ɽ������Ŀ 
	 *       ���屣��ǰУ��
	 *       
	 * @ʱ�䣺2011-3-23����09:05:20
	 * @throws Exception
	 */
	protected void beforeSaveCheck() throws Exception{
		if(getBillUI().getVOFromUI()==null)
			throw new ValidationException("����Ϊ��");
		if(getBillUI().getVOFromUI().getChildrenVO()==null||
				getBillUI().getVOFromUI().getChildrenVO().length==0	){
			throw new BusinessException("���岻����Ϊ��");
		}
	}
}
