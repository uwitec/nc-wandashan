package nc.bs.zb.price.grade.col;

import nc.vo.zb.parmset.ParamSetVO;

/**
 * 
 * @author zhf
 * @˵�������׸ڿ�ҵ����׼ۺ�ƽ���������ʱ ���㱨�۷�
 * 2011-5-4����02:45:23
 */
public class PriceEquCol extends AbstractPriceGradeCol {

	public PriceEquCol(ParamSetVO ipara) {
		super(ipara);
//		setIsMax(ismax);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void col() {
		// TODO Auto-generated method stub
//  ������߷�
		if(isMax()){
			body.setNmaxgrade(getC());
		}else
			body.setNmingrade(getC());
		
	}

}
