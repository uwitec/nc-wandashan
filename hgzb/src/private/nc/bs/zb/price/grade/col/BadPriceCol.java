package nc.bs.zb.price.grade.col;

import nc.vo.pub.lang.UFDouble;
import nc.vo.zb.parmset.ParamSetVO;
/**
 * 
 * @author zhf
 * @说明：（鹤岗矿业）报价低于合理报价下限属于恶意报价时 计算报价分
 * 2011-5-4下午02:45:23
 */
public class BadPriceCol extends AbstractPriceGradeCol {

	public BadPriceCol(ParamSetVO ipara) {
		super(ipara);
//		setIsMax(ismax);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void col() {
		// TODO Auto-generated method stub
//  恶意报价   （c-|1-a/b|*c）*y  整理公式后得：acy/b
//		UFDouble dd = getA().multiply(getC(),8).multiply(getY(),8).div(getB(),8);
//		body.setNgrade(dd.add(UFDouble.ZERO_DBL,ZbPubConst.grade_digit));
		if(isMax())
		body.setNmaxgrade(UFDouble.ZERO_DBL);//理论上恶意报价不会走到这里   如果这里出现恶意报价 情况  报价分暂定位0
		else
			body.setNmingrade(UFDouble.ZERO_DBL);
	}

}
