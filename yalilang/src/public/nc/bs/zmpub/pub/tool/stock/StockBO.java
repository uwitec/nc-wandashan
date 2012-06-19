package nc.bs.zmpub.pub.tool.stock;
import java.io.Serializable;
import java.util.Collection;

import nc.bs.dao.BaseDAO;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.zmpub.pub.tool.CombinVO;
/**
 * 定义台账 库存现存量的公共处理方法
 * 什么是现存量？
 * 现存量就是 库存状态的时时变化的数据 的展现
 * 做入库单 现存量肯定要增
 * 做出库单 现存量肯定要加
 * 
 * 用来用记录这个当前库存量的表 就叫做现存量表
 * 
 * 本工具类是用来 操作现存量变化的：
 * 什么时候用呢？ 当你做到单据要影响到现存量的时候
 * 就可以用本工具类 来更新现存量
 * 
 * 也可以用本工具类来按最小维度查询现存量
 * 
 * @author zhf mlr  定义现存量 公共处理方法
 */
public abstract class StockBO implements Serializable{	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3849756175968282000L;
	public StockBO(){
		super();
	}	
	public StockBO(BaseDAO dao){
		super();
		this.dao = dao;
	}
	/**该组件应该实现以下功能
	 * 1、新增现存量（新增、更新）
	 * 2、获取现存量态
	 * 3、增加在用量
	 * 4、减少在用量
	 * 5、减少在籍量
	 * 6、减少在用量
	 */	
	private BaseDAO dao = null;
	public BaseDAO getDao(){
		if(dao == null){
			dao = new BaseDAO();
		}
		return dao;
	}
	/**
	 * 获得现存量最小维度定义字段
	 * @return
	 */
	public abstract String[] getDef_Fields();
	/**
	 * 获得影响现存量变化的字段数组(仅支持UFDouble类型)
	 * @return
	 */
	public abstract String[] getChangeNums();
	/**
	 * 获取现存量类的全路径
	 * @return
	 */
	public abstract String getClassName();
	/**
	 * 更新现存量
	 * @param accounts
	 * @throws Exception 
	 */
	public void updateStock(SuperVO[] accounts1) throws Exception{
		if(getDef_Fields()==null || getDef_Fields().length==0){
			throw new Exception("没有注册现存量最小维度数组");
		}
		if(getChangeNums()==null || getChangeNums().length==0){
			throw new Exception("没有注册影响现存量变化的数组");
		}
		if(getClassName()==null || getClassName().length()==0){
			throw new Exception("没有注册现存量实现类的全路径");
		}		
		Class cl=Class.forName(getClassName());
		//首先按 最小的维度进行数据合并
	   SuperVO[] accounts=(SuperVO[]) CombinVO.combinData(accounts1, getDef_Fields(), getChangeNums(), cl);
	   if(accounts==null || accounts.length==0)
		   return;
	   int size=accounts.length;
	   String whereSql=null;
	   for(int i=0;i<size;i++){
		   whereSql=getWhereSql(accounts[i]);
		   whereSql=whereSql+" and isnull(dr,0)=0 ";
		   SuperVO[] vos=queryStock(whereSql);
		   //数据库中没有该维度的数据 就新插入一条
		   if(vos==null || vos.length==0){
			    getDao().insertVO(accounts[i]);
			 }
		   else if(vos.length>1){
			   throw new Exception("数据异常 按最小维度获取现存量数据 数量超过一条");
		   }
		   //如果按最小维度查询有一条的话 就将新的数据加到旧的数据上执行 更新操作
		   else  if( vos.length==1){
		     SuperVO oldvo=vos[0];//从数据库中查询出来的数据
		     SuperVO newvo=accounts[i];
		     String[] changeNums=getChangeNums();
		     for(int j=0;j<changeNums.length;j++){
			   UFDouble old=PuPubVO.getUFDouble_NullAsZero(oldvo.getAttributeValue(changeNums[j]));
			   UFDouble newo=PuPubVO.getUFDouble_NullAsZero(newvo.getAttributeValue(changeNums[j]));			   
			   oldvo.setAttributeValue(changeNums[j], old.add(newo));
		     }
		     getDao().updateVO(oldvo);
		   }
	   }
	   //现存量更新完了之后,校验现存量是否会出现负值
	   //如果出现负值 进行数据回滚 
	   check(accounts);
	}
	private String getWhereSql(SuperVO superVO) throws Exception {
	   String whereSql="";
	   String[] fields=getDef_Fields();
	   if(fields==null || fields.length==0)
		   throw new Exception("没有注册现存量最小维度字段");
	   for(int i=0;i<fields.length;i++){
		   whereSql=whereSql +" "+fields[i]+" = '"+superVO.getAttributeValue(fields[i])+"'";
		   if(i!=fields.length-1){
			   whereSql=whereSql+" and " ;
		   }
	   }
		return whereSql;
	}
	/**
	 * 通过where条件查询现存量
	 * @param whereSql
	 * @return
	 */
	public SuperVO[] queryStock(String whereSql)throws Exception{
		String clname=getClassName();
		if(clname==null || clname.length()==0)
			throw new Exception("没有注册现存量实现类全路径");
		Class cl=Class.forName(clname);
		Collection list= getDao().retrieveByClause(cl, whereSql);
        if(list==null || list.size()==0)
        	return null;
        SuperVO[] vos=(SuperVO[]) list.toArray((SuperVO[])java.lang.reflect.Array.newInstance(cl, list.size()));		
		return vos;
		
	}
	/**
	 * 校验现存量数据不能为负值
	 * @param whereSql
	 * @return
	 * @throws Exception 
	 */
	public void  check(SuperVO[] vos) throws Exception{
		if(vos==null || vos.length==0)
			return ;
		for(int i=0;i<vos.length;i++){
			String whereSql=getWhereSql(vos[i]);
			SuperVO[] ols=queryStock(whereSql);
			if(ols==null || ols.length==0)
				return ;
			for(int j=0;j<ols.length;j++){
				String[] fields=getChangeNums();
				if(fields==null || fields.length==0){
					throw new Exception("没有注册现存量变化字段");
				}
				for(int k=0;k<fields.length;k++){
				    UFDouble uf=PuPubVO.getUFDouble_NullAsZero(ols[j].getAttributeValue(fields[k]));
				    if(uf.compareTo(new UFDouble(0.0))<0){
				    	throw new Exception("现存量出现负值");
				    }
				}
			}			
		}		
	}
	
	
	
	
	
	
	
}
