package nc.bs.zmpub.pub.tool.stock;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.sun.org.apache.bcel.internal.generic.NEW;

import nc.bs.zmpub.pub.tool.SingleVOChangeDataBsTool;
import nc.ui.scm.util.ObjectUtils;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.SuperVO;
import nc.vo.pub.VOStatus;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.trade.pub.HYBillVO;
/**
 * 是对现存量更新类的扩展 
 * 针对 业务单据更新现存量的操作
 * @author mlr
 */
public abstract class BillStockBO extends StockBO{
	//抽象方法的扩展开始
    /**
     * 单据类型 与 数据交换类 对应map
     * @return
     * @throws Exception
     */
   public abstract Map<String,String> getTypetoChangeClass()throws Exception;
   /**
    * 获得各个单据类型 对应的 现存量设置规则
    * @return Map<String,boolean[]> 
    * @throws Exception
    */
   public abstract Map<String,UFBoolean[]> getTypetosetnum()throws Exception; 	
	//抽象方法的扩展结束	
  /**
   * 业务单据更新现存量 传来的是业务单据的数据
   * @param vos 业务单据数据
   * @param pk_billtype 更新现存量单据类型
 * @throws Exception 
   */
  public void updateStockByBill(SuperVO[] vos,String pk_billtype) throws Exception{
	  Map<String,String> map=getTypetoChangeClass();
	  if(map==null || map.size()==0)
		  throw new Exception("没有注册单据类型->现存量的数据交换类");
	  String className=map.get(pk_billtype);
	  if(className==null || className.length()==0)
		  throw new Exception(" 单据类型为:"+pk_billtype+" 没有注册交换类");	
	  String changeClName=getClassName();
	  if(changeClName==null || changeClName.length()==0)
		  throw new Exception("没有注册现存量实现类的全路径");	
	  Class cl=Class.forName(changeClName);
	  SuperVO[] numvos=SingleVOChangeDataBsTool.runChangeVOAry(vos, cl, className);
	  if(numvos==null || numvos.length==0){
		  return;
	  }
	  setAccountNumChange(numvos,pk_billtype);	
	  updateStock(numvos);
  }
  /**
   * 业务单据更新现存量 传来的是业务单据的数据
   * @param vos 业务单据数据
   * @param pk_billtype 更新现存量单据类型
   * @throws Exception 
   */
  public void updateStockByBill(AggregatedValueObject vos,String pk_billtype) throws Exception{
	  Map<String,String> map=getTypetoChangeClass();
	  if(map==null || map.size()==0)
		  throw new Exception("没有注册单据类型->现存量的数据交换类");
	  String className=map.get(pk_billtype);
	  if(className==null || className.length()==0)
		  throw new Exception(" 单据类型为:"+pk_billtype+" 没有注册交换类");	
	  String changeClName=getClassName();
	  if(changeClName==null || changeClName.length()==0)
		  throw new Exception("没有注册现存量实现类的全路径");
	  //处理单据修改操作  删行数据 和 修改行数据
	  dealMod(vos,pk_billtype);	  
	  Class cl=Class.forName(changeClName);
	  SuperVO[] numvos=SingleVOChangeDataBsTool.runChangeVOAry(vos, cl, className);
	  if(numvos==null || numvos.length==0){
		  return;
	  }
	  setAccountNumChange(numvos,pk_billtype);	
	  updateStock(numvos);
  }
  /**
   * 处理单据修改操作  删行数据 和 修改行数据
 * @param pk_billtype 
   * @作者：mlr
   * @说明：完达山物流项目 
   * @时间：2012-6-28下午12:39:13
   *
   */
	private void dealMod(AggregatedValueObject vos, String pk_billtype) throws Exception {
		 if(vos.getParentVO()==null){
			  throw new Exception("单据表头为空");
		  }
		 if(vos.getChildrenVO()==null || vos.getChildrenVO().length==0)
			 throw new Exception("单据表体为空");
		 SuperVO[] bodys=(SuperVO[]) ObjectUtils.serializableClone(vos.getChildrenVO());
		 //存放修改 删除的vo
		 List<SuperVO>  list=new ArrayList<SuperVO>();
		 for(int i=0;i<bodys.length;i++){
			 if(bodys[i].getStatus()==VOStatus.DELETED || bodys[i].getStatus()==VOStatus.UPDATED){
				 list.add(bodys[i]);
			 }			 
		 }
		 //存放修改删除的记录的   数据库对应记录 vo
		 SuperVO[]  ovos=(SuperVO[]) java.lang.reflect.Array.newInstance(list.get(0).getClass(), list.size());
		 for(int i=0;i<list.size();i++){
			 List li=(List) getDao().retrieveByClause(list.get(0).getClass(),list.get(0).getPKFieldName()+" = '"+list.get(i).getPrimaryKey()+"'" );
			 if(li!=null&&li.size()!=0)
			 ovos[i]=(SuperVO) li.get(0);
		 }
		  Map<String,String> map=getTypetoChangeClass();
		  String changeClName=getClassName();
		  Class cl=Class.forName(changeClName);
		  String className=map.get(pk_billtype);
		  SuperVO headVo=(SuperVO) ObjectUtils.serializableClone(vos.getParentVO());
		  SuperVO[] bodyVos=(SuperVO[]) ObjectUtils.serializableClone(ovos);
		  AggregatedValueObject billvo=new  HYBillVO();
		  billvo.setParentVO(headVo);
		  billvo.setChildrenVO(bodyVos);	  
		  SuperVO[] numvos=SingleVOChangeDataBsTool.runChangeVOAry(billvo, cl, className);
		  if(getChangeNums()==null || getChangeNums().length==0)
			  throw new  Exception("没有注册现存量变化字段");
		  for(int i=0;i<numvos.length;i++){
			  for(int j=0;j<getChangeNums().length;j++){
				UFDouble uf= PuPubVO.getUFDouble_NullAsZero(numvos[i].getAttributeValue(getChangeNums()[j]));
				numvos[i].setAttributeValue(getChangeNums()[j], new UFDouble(0).sub(uf));
			  }
		  }
		  setAccountNumChange(numvos,pk_billtype);	
		  updateStock(numvos);
    }
	/**
	 * 设置现存量数据变化量
	 * @param map
	 * @throws Exception
	 */
  public void setAccountNumChange(SuperVO[] vos1,String pk_billtype) throws Exception{
  	if(vos1==null || vos1.length==0)
  		return;
  	Map<String,UFBoolean[]> nmap=getTypetosetnum();
  	if(nmap==null || nmap.size()==0)
  	  throw new Exception(" 没有注册单据类型 对应  现存量变化规则");   
  	String[] fields=getChangeNums();
  	if(fields==null || fields.length==0)
  	  throw new Exception(" 没有注册现存量变化量字段");
  	  UFBoolean[] ufs=nmap.get(pk_billtype);
  	if(ufs==null || ufs.length==0 || ufs.length!=fields.length)
  	  throw new Exception( "单据类型为"+pk_billtype+" 注册的变化量规则为空 或  注册的规则数组和变化量字段数组长度不一致");
  	  setAccountNum(vos1, ufs);
  }
  /** 
	 * @author mlr
	 * @说明：（鹤岗矿业）将矿级单据数据转换为台账参量
	 * 2011-9-14下午03:24:36
	 * @param bill 矿级单据
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
