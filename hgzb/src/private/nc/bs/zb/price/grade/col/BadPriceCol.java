package nc.bs.zb.price.grade.col;

import nc.vo.pub.lang.UFDouble;
import nc.vo.zb.parmset.ParamSetVO;
/**
 * 
 * @author zhf
 * @˵�������׸ڿ�ҵ�����۵��ں������������ڶ��ⱨ��ʱ ���㱨�۷�
 * 2011-5-4����02:45:23
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
//  ���ⱨ��   ��c-|1-a/b|*c��*y  ����ʽ��ã�acy/b
//		UFDouble dd = getA().multiply(getC(),8).multiply(getY(),8).div(getB(),8);
//		body.setNgrade(dd.add(UFDouble.ZERO_DBL,ZbPubConst.grade_digit));
		if(isMax())
		body.setNmaxgrade(UFDouble.ZERO_DBL);//�����϶��ⱨ�۲����ߵ�����   ���������ֶ��ⱨ�� ���  ���۷��ݶ�λ0
		else
			body.setNmingrade(UFDouble.ZERO_DBL);
	}

}
