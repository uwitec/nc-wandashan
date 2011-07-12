package nc.ui.wl.pub.report;
import nc.vo.pub.lang.UFDouble;
import nc.vo.wl.pub.report.IUFTypes;
import nc.vo.wl.pub.report.ReportBaseVO;
public class CombinVO {
	 /**
	    * 
	    * @作者：mlr
	    * @说明：完达山物流项目 
	    *      判断两个对象是否相等
	    * @时间：2011-7-12上午10:34:05
	    * @param o1
	    * @param o2
	    */
		private static boolean isEqual(Object o1, Object o2) {
			if(o1 !=null && o2==null){
				return false;
			}
			if(o1 ==null && o2!=null){
				return false;
			}
		    if(o1!=null && o2!=null){
		    	if(!o1.equals(o2)){
		    		return false;
		    	}
		    }
		    return true;
		}
	/**
	 * 
	 * @作者：mlr
	 * @说明：完达山物流项目
	 *        根据某个维度(条件)
	 *        将两个数组中条件字段对应值相同的合并
	 *        根据 求值字段数组和类型数组 判断需要求和的字段
	 *        进行求和运算
	 *        
	 *        使用本方法的前提条件：
	 *        两个vo数组按维度条件只能查到一个符合条件的vo
	 *       
	 * @时间：2011-7-11下午09:12:25
	 * @param vos
	 * @param vos1
	 * @param voCombinConds 条件字段数组
	 * @param types 求值类型
	 * @param combinFields 求值字段
	 * @return
	 */
	public static ReportBaseVO[] combinVoByCondition(ReportBaseVO[] vos, ReportBaseVO[] vos1,
			String[] voCombinConds, int[] types,String[] combinFields) {
		if (vos == null || vos.length == 0) {
			if (vos1 != null && vos1.length > 0) {
				return vos1;
			}
		}
		if (vos1 == null || vos1.length == 0) {
			if (vos != null && vos.length > 1) {
				return vos;
			}
		}
		if (vos == null || vos.length == 0 && vos1 == null || vos1.length == 0) {
			return null;
		}
		int size = 0;
		int size1 = 0;
		int location = -1;
		if (vos.length >= vos1.length) {
			location = 1;
			size = vos.length;
			size1 = vos1.length;
		} else {
			location = 2;
			size = vos1.length;
			size1 = vos.length;
		}
		if (location == 1) {
			for (int i = 0; i < size; i++) {
				ReportBaseVO avo = vos[i];
				for (int j = 0; j < size1; j++) {
					ReportBaseVO bvo = vos1[j];
					boolean isEqual = true;
					for (int k = 0; k < voCombinConds.length; k++) {
						Object o1 = avo.getAttributeValue(voCombinConds[k]);
						Object o2 = bvo.getAttributeValue(voCombinConds[k]);
						if (!isEqual(o1, o2)) {
							isEqual = false;
							break;
						}
					}
					if (isEqual) {
						int csize = combinFields.length;
						for (int n = 0; n < csize; n++) {
							Object resultobj = avo
									.getAttributeValue(combinFields[n]);
							Object tmpobj = bvo
									.getAttributeValue(combinFields[n]);
							switch (types[n]) {
							case IUFTypes.INT:
								int iresult = (resultobj == null ? 0
										: ((Integer) resultobj).intValue());
								int itmp = (tmpobj == null ? 0
										: ((Integer) tmpobj).intValue());
								avo.setAttributeValue(combinFields[n],
										new Integer(iresult + itmp));
								continue;
							case IUFTypes.LONG:
								long lgtmp = (tmpobj == null ? 0
										: ((Long) tmpobj).longValue());
								long lgresult = (resultobj == null ? 0
										: ((Long) resultobj).longValue());
								if (tmpobj != null)
									avo.setAttributeValue(combinFields[n],
											new Long(lgresult + lgtmp));
								continue;
							case IUFTypes.UFD:
								UFDouble ufdtmp = (tmpobj == null ? new UFDouble(
										"0")
										: (UFDouble) tmpobj);
								UFDouble ufdResult = (resultobj == null ? new UFDouble(
										"0")
										: (UFDouble) resultobj);
								avo.setAttributeValue(combinFields[n],
										ufdResult.add(ufdtmp));
								continue;
							case IUFTypes.STR:
								String strtmp = (tmpobj == null ? "" : tmpobj
										.toString());
								String strresult = (resultobj == null ? ""
										: resultobj.toString());
								avo.setAttributeValue(combinFields[n], strtmp
										+ strresult);
								continue;
							}
						}
					}
				}
			}
			return vos;
		} else {
			for (int i = 0; i < size; i++) {
				ReportBaseVO avo = vos1[i];
				for (int j = 0; j < size1; j++) {
					ReportBaseVO bvo = vos[j];
					boolean isEqual = true;
					for (int k = 0; k < voCombinConds.length; k++) {
						Object o1 = avo.getAttributeValue(voCombinConds[k]);
						Object o2 = bvo.getAttributeValue(voCombinConds[k]);
						if (!isEqual(o1, o2)) {
							isEqual = false;
							break;
						}
					}
					if (isEqual) {
						int csize = combinFields.length;
						for (int n = 0; n < csize; n++) {
							Object resultobj = avo
									.getAttributeValue(combinFields[n]);
							Object tmpobj = bvo
									.getAttributeValue(combinFields[n]);
							switch (types[n]) {
							case IUFTypes.INT:
								int iresult = (resultobj == null ? 0
										: ((Integer) resultobj).intValue());
								int itmp = (tmpobj == null ? 0
										: ((Integer) tmpobj).intValue());
								avo.setAttributeValue(combinFields[n],
										new Integer(iresult + itmp));
								continue;
							case IUFTypes.LONG:
								long lgtmp = (tmpobj == null ? 0
										: ((Long) tmpobj).longValue());
								long lgresult = (resultobj == null ? 0
										: ((Long) resultobj).longValue());
								if (tmpobj != null)
									avo.setAttributeValue(combinFields[n],
											new Long(lgresult + lgtmp));
								continue;
							case IUFTypes.UFD:
								UFDouble ufdtmp = (tmpobj == null ? new UFDouble(
										"0")
										: (UFDouble) tmpobj);
								UFDouble ufdResult = (resultobj == null ? new UFDouble(
										"0")
										: (UFDouble) resultobj);
								avo.setAttributeValue(combinFields[n],
										ufdResult.add(ufdtmp));
								continue;
							case IUFTypes.STR:
								String strtmp = (tmpobj == null ? "" : tmpobj
										.toString());
								String strresult = (resultobj == null ? ""
										: resultobj.toString());
								avo.setAttributeValue(combinFields[n], strtmp
										+ strresult);
								continue;
							}
						}
					}
				}
			}
			return vos1;
		}		
	}
}
