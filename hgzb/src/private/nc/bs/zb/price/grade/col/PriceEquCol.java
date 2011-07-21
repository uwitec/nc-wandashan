package nc.bs.zb.price.grade.col;

import nc.vo.zb.parmset.ParamSetVO;

/**
 * 
 * @author zhf
 * @说明：（鹤岗矿业）标底价和平均报价相等时 计算报价分
 * 2011-5-4下午02:45:23
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
//  等于最高分
		if(isMax()){
			body.setNmaxgrade(getC());
		}else
			body.setNmingrade(getC());
		
	}

}
