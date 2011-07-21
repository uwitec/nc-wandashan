package nc.bs.zb.price.grade.col;

import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.zb.parmset.ParamSetVO;
import nc.vo.zb.pub.ZbPubConst;
/**
 * 
 * @author zhf
 * @说明：（鹤岗矿业）报价低于标底价时（不属于恶意报价段） 计算报价分
 * 2011-5-4下午02:45:23
 */
public class PriceMinCol extends AbstractPriceGradeCol {

	public PriceMinCol(ParamSetVO ipara) {
		super(ipara);
//		setIsMax(ismax);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void col() {
		// TODO Auto-generated method stub
		//  a<b & a>d     c-|1-a/b|*c  公式整理后：ac/b
		//		UFDouble dd = PuPubVO.getUFDouble_NullAsZero(getA().multiply(getC(),8).div(getB(),8));
		//		body.setNgrade(dd.add(UFDouble.ZERO_DBL,ZbPubConst.grade_digit));
		UFDouble dd = PuPubVO.getUFDouble_NullAsZero(getA().multiply(getC(),8).multiply(getY(),8).div(getB(),8));
		if(isMax())
			body.setNmaxgrade(dd.add(UFDouble.ZERO_DBL,ZbPubConst.grade_digit));
		else
			body.setNmingrade(dd.add(UFDouble.ZERO_DBL,ZbPubConst.grade_digit));
	}

}
