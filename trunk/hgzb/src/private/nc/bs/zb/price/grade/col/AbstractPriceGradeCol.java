package nc.bs.zb.price.grade.col;

import nc.vo.pub.BusinessException;
import nc.vo.pub.ValidationException;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.zb.bill.deal.DealInvPriceBVO;
import nc.vo.zb.parmset.ParamSetVO;

public abstract class AbstractPriceGradeCol {
	
	/**
	 * �����������ݣ���Ӧ����ͱ���a    ��׼�b   ���۷���߷�c   ���������� d  ϵ��x  ϵ��y
       ��Ӧ��Ʒ�ֱ��۷ּ��㷽�����£�

     1�� a=b   c
     2�� a>b   ��c-|1-a/b|*c��*x
     3�� a<b & a>d     c-|1-a/b|*c
     4�� a<d  ���ⱨ��   ��c-|1-a/b|*c��*y
     
     ---------------------------------------------------
     2011-06-11 �ͻ����������Ժ����Ϊ��
     
     1�� a=b   c
     2�� a>b   ��c-|1-a/b|*c��*x
     3�� a<b & a>d     (c-|1-a/b|*c)*y
     4�� a<d  ���ⱨ��   �ᱨ������  �ڱ��۴����ƶ��ⱨ�� 
     
     d  ����Ϊ  ��׼�*(1-e)    eΪϵͳ����  ������ƫ���� Ϊ�ٷֱ�  ��ʽ  �û�ֱ��ά��Ϊ  С��ֵ    ����ʱУ��  ��ֵ��Χ��0<e<1

     ��Ӧ�̱����ܷ� = ����Ʒ�ֱ��۷��ܺ� / ���Ʒ������
     
     	--------------------------------------------------------
//	  2011-06-113�ͻ����������Ժ����Ϊ��
 * 
 * ��Ӧ��ÿ��Ʒ�ּ������α��۷�   ��ͼۼ���һ��    ��߼ۼ���һ��    ���ε÷ֵ�ƽ��ֵΪ  �ù�Ӧ�̸�Ʒ�ֵı��۷�

	 */

	
	
	protected DealInvPriceBVO body = null;//������÷ֵ�����
	protected ParamSetVO para = null;//�б��������
	
	private boolean ismax = true;//�Ƿ���߼ۼ���÷�
	
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
