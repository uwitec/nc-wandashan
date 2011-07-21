package nc.bs.zb.price.grade.col;

import nc.vo.pub.BusinessException;
import nc.vo.pub.ValidationException;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.zb.bill.deal.DealInvPriceBVO;
import nc.vo.zb.parmset.ParamSetVO;

public abstract class AbstractPriceGradeCol {
	
	/**
	 * 定义以下数据：供应商最低报价a    标底价b   报价分最高分c   合理报价下限 d  系数x  系数y
       供应商品种报价分计算方法如下：

     1、 a=b   c
     2、 a>b   （c-|1-a/b|*c）*x
     3、 a<b & a>d     c-|1-a/b|*c
     4、 a<d  恶意报价   （c-|1-a/b|*c）*y
     
     ---------------------------------------------------
     2011-06-11 客户（王）测试后调整为：
     
     1、 a=b   c
     2、 a>b   （c-|1-a/b|*c）*x
     3、 a<b & a>d     (c-|1-a/b|*c)*y
     4、 a<d  恶意报价   提报不上来  在报价处控制恶意报价 
     
     d  定义为  标底价*(1-e)    e为系统参数  合理报价偏差率 为百分比  形式  用户直接维护为  小数值    保存时校验  该值范围：0<e<1

     供应商报价总分 = 各个品种报价分总和 / 标段品种总数
     
     	--------------------------------------------------------
//	  2011-06-113客户（王）测试后调整为：
 * 
 * 供应商每个品种计算两次报价分   最低价计算一次    最高价计算一次    两次得分的平均值为  该供应商该品种的报价分

	 */

	
	
	protected DealInvPriceBVO body = null;//待计算得分的数据
	protected ParamSetVO para = null;//招标参数设置
	
	private boolean ismax = true;//是否最高价计算得分
	
	public void setIsMax(boolean ismax){
		this.ismax = ismax;
	}
	
	public boolean isMax(){
		return ismax;
	}
	
	protected abstract void col();
	
//	protected void save() throws BusinessException{
//		
//	}
	
	public void doCol() throws BusinessException{
		validation();
		col();
//		save();
	}
	
	public AbstractPriceGradeCol(ParamSetVO ipara){
		super();
//		body = ibody;
		para = ipara;
	}
	
	public void setBody(DealInvPriceBVO body){
		this.body = body;
	}
	
	public void validation() throws ValidationException{
		
	}
	
	protected  UFDouble getA(){
		return PuPubVO.getUFDouble_NullAsZero(ismax?body.getNprice():body.getNllowerprice());
	}
	
	protected UFDouble getB(){
		return PuPubVO.getUFDouble_NullAsZero(body.getNmarkprice());
	}
	
	protected UFDouble getC(){
		return PuPubVO.getUFDouble_NullAsZero(para.getNmaxquotatpoints());
	}
	
	protected UFDouble getD(){
		return PuPubVO.getUFDouble_NullAsZero(para.getNquotationlower());
	}
	
	protected UFDouble getX(){
		return PuPubVO.getUFDouble_NullAsZero(para.getReserve8());
	}
	
	protected UFDouble getY(){
		return PuPubVO.getUFDouble_NullAsZero(para.getNquotationscoring());
	}

}
