package nc.bs.zb.price.grade.col;

import nc.vo.pub.lang.UFDouble;
import nc.vo.zb.parmset.ParamSetVO;
import nc.vo.zb.pub.ZbPubConst;
/**
 * 
 * @author zhf
 * @˵�������׸ڿ�ҵ�����۸��ڱ�׼�ʱ ���㱨�۷�
 * 2011-5-4����02:45:23
 */
public class PriceMaxCol extends AbstractPriceGradeCol {

	public PriceMaxCol(ParamSetVO ipara) {
		super(ipara);
//		setIsMax(ismax);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void col() {
		// TODO Auto-generated method stub
//  ���㹫ʽ����c-|1-a/b|*c��*x   ��ʽ�����Ľ����2cx-acx/b = cx*(2-a/b)
		UFDouble dd = getC().multiply(getX(), 8).multiply(getA().div(getB(), 8).multiply(-1).add(2));
	if(isMax()){
		body.setNmaxgrade(dd.add(UFDouble.ZERO_DBL,ZbPubConst.grade_digit));	
	}else
		body.setNmingrade(dd.add(UFDouble.ZERO_DBL,ZbPubConst.grade_digit));	
//		body.setNadjgrade(getC().multiply(getX(), 8).multiply(getA().div(getB(), 8).multiply(-1).add(2)));
//		int i = 2+5;
//		body.setNgrade(new UFDouble(-380));
	}

}
