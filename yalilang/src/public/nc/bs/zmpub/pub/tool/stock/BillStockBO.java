package nc.bs.zmpub.pub.tool.stock;
import java.util.Map;

import nc.bs.zmpub.pub.tool.SingleVOChangeDataBsTool;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pu.PuPubVO;
/**
 * �Ƕ��ִ������������չ 
 * ��� ҵ�񵥾ݸ����ִ����Ĳ���
 * @author mlr
 */
public abstract class BillStockBO extends StockBO{
	//���󷽷�����չ��ʼ
    /**
     * �������� �� ���ݽ����� ��Ӧmap
     * @return
     * @throws Exception
     */
   public abstract Map<String,String> getTypetoChangeClass()throws Exception;
   /**
    * ��ø����������� ��Ӧ�� �ִ������ù���
    * @return Map<String,boolean[]> 
    * @throws Exception
    */
   public abstract Map<String,UFBoolean[]> getTypetosetnum()throws Exception; 	
	//���󷽷�����չ����	
  /**
   * ҵ�񵥾ݸ����ִ��� ��������ҵ�񵥾ݵ�����
   * @param vos ҵ�񵥾�����
   * @param pk_billtype �����ִ�����������
 * @throws Exception 
   */
  public void updateStockByBill(SuperVO[] vos,String pk_billtype) throws Exception{
	  Map<String,String> map=getTypetoChangeClass();
	  if(map==null || map.size()==0)
		  throw new Exception("û��ע�ᵥ������->�ִ��������ݽ�����");
	  String className=map.get(pk_billtype);
	  if(className==null || className.length()==0)
		  throw new Exception(" ��������Ϊ:"+pk_billtype+" û��ע�ύ����");	
	  String changeClName=getClassName();
	  if(changeClName==null || changeClName.length()==0)
		  throw new Exception("û��ע���ִ���ʵ�����ȫ·��");	
	  Class cl=Class.forName(changeClName);
	  SuperVO[] numvos=SingleVOChangeDataBsTool.runChangeVOAry(vos, cl, className);
	  if(numvos==null || numvos.length==0){
		  return;
	  }
	  setAccountNumChange(numvos,pk_billtype);	
	  updateStock(numvos);
  }
  /**
   * ҵ�񵥾ݸ����ִ��� ��������ҵ�񵥾ݵ�����
   * @param vos ҵ�񵥾�����
   * @param pk_billtype �����ִ�����������
   * @throws Exception 
   */
  public void updateStockByBill(AggregatedValueObject vos,String pk_billtype) throws Exception{
	  Map<String,String> map=getTypetoChangeClass();
	  if(map==null || map.size()==0)
		  throw new Exception("û��ע�ᵥ������->�ִ��������ݽ�����");
	  String className=map.get(pk_billtype);
	  if(className==null || className.length()==0)
		  throw new Exception(" ��������Ϊ:"+pk_billtype+" û��ע�ύ����");	
	  String changeClName=getClassName();
	  if(changeClName==null || changeClName.length()==0)
		  throw new Exception("û��ע���ִ���ʵ�����ȫ·��");
	 //
	  
	  
	  Class cl=Class.forName(changeClName);
	  SuperVO[] numvos=SingleVOChangeDataBsTool.runChangeVOAry(vos, cl, className);
	  if(numvos==null || numvos.length==0){
		  return;
	  }
	  setAccountNumChange(numvos,pk_billtype);	
	  updateStock(numvos);
  }
	/**
	 * �����ִ������ݱ仯��
	 * @param map
	 * @throws Exception
	 */
  public void setAccountNumChange(SuperVO[] vos1,String pk_billtype) throws Exception{
  	if(vos1==null || vos1.length==0)
  		return;
  	Map<String,UFBoolean[]> nmap=getTypetosetnum();
  	if(nmap==null || nmap.size()==0)
  	  throw new Exception(" û��ע�ᵥ������ ��Ӧ  �ִ����仯����");   
  	String[] fields=getChangeNums();
  	if(fields==null || fields.length==0)
  	  throw new Exception(" û��ע���ִ����仯���ֶ�");
  	  UFBoolean[] ufs=nmap.get(pk_billtype);
  	if(ufs==null || ufs.length==0 || ufs.length!=fields.length)
  	  throw new Exception( "��������Ϊ"+pk_billtype+" ע��ı仯������Ϊ�� ��  ע��Ĺ�������ͱ仯���ֶ����鳤�Ȳ�һ��");
  	  setAccountNum(vos1, ufs);
  }
  /** 
	 * @author mlr
	 * @˵�������׸ڿ�ҵ�����󼶵�������ת��Ϊ̨�˲���
	 * 2011-9-14����03:24:36
	 * @param bill �󼶵���
	 * @return
	 * @throws Exception
	 */                                                        
	public void setAccountNum(SuperVO[] vos,UFBoolean[] isNumCirl) throws Exception{
		if(vos == null || vos.length==0)
			return ;		
	  for(int j=0;j<vos.length;j++){			  
		 String[] fields=getChangeNums();
		 for(int i=0;i<fields.length;i++){
		   if(isNumCirl[i]==null){
			   vos[j].setAttributeValue(fields[i], new UFDouble(0.0));
		   }else if(isNumCirl[i].booleanValue()==true){
			   UFDouble num=PuPubVO.getUFDouble_NullAsZero(vos[j].getAttributeValue(fields[i]));
			   vos[j].setAttributeValue(fields[i],num.multiply(-1) );
	       }				
		 }
	  }
	}
}
