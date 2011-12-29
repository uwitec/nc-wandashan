package nc.bs.wl.pub;

import nc.bs.logging.Logger;
import nc.bs.pf.change.AbstractConversion;
import nc.bs.pf.change.VOConversion;
import nc.vo.pf.change.IchangeVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.trade.pub.HYBillVO;

public class SingleVOChangeDataBsTool {
	/**
	 * 
	 * @author zhf
	 * @˵�� ��ȡvoת����ʵ��
	 * @ʱ�� 2010-9-26����10:47:52
	 * @param classname
	 * @return
	 */
	private static nc.vo.pf.change.IchangeVO getChangeClass(String classname)
			throws Exception {
		if (PuPubVO.getString_TrimZeroLenAsNull(classname) == null)
			return null;
		try {
			Class c = Class.forName(classname);
			Object o = c.newInstance();
			return (nc.vo.pf.change.IchangeVO) o;
		} catch (Exception ex) {
			Logger.error(ex.getMessage(), ex);
			throw ex;
		}
	}

	/**
	 * 
	 * @author zhf
	 * @˵�� ��vo���ݽ��� �÷�����̨���� ǰ̨����ʹ��
	 * @ʱ�� 2010-9-26����11:30:42
	 * @param souVo
	 *            ��Դvo
	 * @param tarVo
	 *            Ŀ��vo
	 * @param chanclassname
	 *            vo������
	 * @throws Exception
	 */
	public static void runChangeVO(
			SuperVO souVo, SuperVO tarVo, String chanclassname)
			throws Exception {
		IchangeVO change = null;
		try {
			change = getChangeClass(chanclassname);
		} catch (Exception e) {// ���ܴ�������ת���쳣 �˴�Ҫ��
								// changeClassName����Ҫ�̳�VOConversion
			e.printStackTrace();
			throw new BusinessException(e);
		}

		if (!(change instanceof VOConversion)) {
			throw new BusinessException("����ת������쳣��" + change.toString());
		}
		AggregatedValueObject preBillVo = getTmpBIllVo1();
		AggregatedValueObject tarBillVo = getTmpBIllVo2();

		preBillVo.setParentVO(souVo);
		tarBillVo.setParentVO(tarVo);
		AbstractConversion achange = (AbstractConversion) change;
		achange.retChangeBusiVO(preBillVo, tarBillVo);

	}
	
	public static SuperVO[] runChangeVOAry(
			SuperVO[] souVos, Class tarVoClass, String chanclassname)
	throws Exception {

		int len = souVos.length;
		if(len<=0)
			return null;
		SuperVO[] tarVos = (SuperVO[])java.lang.reflect.Array.newInstance(tarVoClass, len);
		SuperVO tmp = null;
		for(int i = 0;i<len;i++){
			tmp = (SuperVO)tarVoClass.newInstance();
			tarVos[i] = tmp;
		}
		runChangeVOAry(souVos, tarVos, chanclassname);

		return tarVos;
	}
	
	public static void runChangeVOAry(
			SuperVO[] souVos, SuperVO[] tarVos, String chanclassname)
	throws Exception {
		IchangeVO change = null;
		try {
			change = getChangeClass(chanclassname);
		} catch (Exception e) {// ���ܴ�������ת���쳣 �˴�Ҫ��
			// changeClassName����Ҫ�̳�VOConversion
			e.printStackTrace();
			throw new BusinessException(e);
		}

		if (!(change instanceof VOConversion)) {
			throw new BusinessException("����ת������쳣��" + change.toString());
		}
		AggregatedValueObject preBillVo = getTmpBIllVo1();
		AggregatedValueObject tarBillVo = getTmpBIllVo2();
		int index = 0;
		for(SuperVO souVo:souVos){
			preBillVo.setParentVO(souVo);
			tarBillVo.setParentVO(tarVos[index]);
			AbstractConversion achange = (AbstractConversion) change;
			achange.retChangeBusiVO(preBillVo, tarBillVo);
			index ++;
		}

	}

	private static HYBillVO tmpBillVo1 = null;

	private static HYBillVO getTmpBIllVo1() {
		if (tmpBillVo1 == null) {
			tmpBillVo1 = new HYBillVO();
		}
		return tmpBillVo1;
	}

	private static HYBillVO tmpBillVo2 = null;

	private static HYBillVO getTmpBIllVo2() {
		if (tmpBillVo2 == null) {
			tmpBillVo2 = new HYBillVO();
		}
		return tmpBillVo2;
	}
}
