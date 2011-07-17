package nc.ui.wl.pub.report;
import java.util.ArrayList;
import java.util.List;

import nc.vo.pub.lang.UFDouble;
import nc.vo.wl.pub.report.IUFTypes;
import nc.vo.wl.pub.report.ReportBaseVO;
public class CombinVO {
	 /**
	    * 
	    * @���ߣ�mlr
	    * @˵�������ɽ������Ŀ 
	    *      �ж����������Ƿ����
	    * @ʱ�䣺2011-7-12����10:34:05
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
		 * @���ߣ�mlr
		 * @˵�������ɽ������Ŀ
		 *        ����ĳ��ά��(����)
		 *        �����������������ֶζ�Ӧֵ��ͬ�ĺϲ�
		 *        ���� ��ֵ�ֶ�������������� �ж���Ҫ��͵��ֶ�
		 *        �����������
		 *        
		 *        ʹ�ñ�������ǰ��������
	     *        ����vo���鰴ά������ֻ�ܲ鵽һ������������vo
		 *             
		 * @ʱ�䣺2011-7-11����09:12:25
		 * @param vos
		 * @param vos1
		 * @param voCombinConds �����ֶ�����
		 * @param types ��ֵ����
		 * @param combinFields ��ֵ�ֶ�
		 * @return
		 */
		public static ReportBaseVO[] combinVoByFields(ReportBaseVO[] vos, ReportBaseVO[] vos1,
				String[] voCombinConds, int[] types,String[] combinFields) {
			//��¼�Ѿ��ϲ���vo
			List<ReportBaseVO> list=new ArrayList<ReportBaseVO>();
			if (vos == null || vos.length == 0) {
				if (vos1 != null && vos1.length > 0) {
					return vos1;
				}
			}
			if (vos1 == null || vos1.length == 0) {
				if (vos != null && vos.length > 0) {
					return vos;
				}
			}
			if (vos == null || vos.length == 0 && vos1 == null || vos1.length == 0) {
				return null;
			}
			int size=vos.length;
			int size1=vos1.length;
			//�� vos�е�ÿ��vo ����������vos1
			//������������ vos1�е�vo ��Ӧ��ֵ �ӵ�vos��vo��
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
						list.add(bvo);
						int csize = combinFields.length;
						for (int n = 0; n < csize; n++) {
							Object resultobj = avo.getAttributeValue(combinFields[n]);
							Object tmpobj = bvo.getAttributeValue(combinFields[n]);
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
					    break;
					}
				}
			}
			//��¼vos1��û�б�vosƥ���ϵ�vo ���ж��κϲ�
			//���� vos1 ��  list
		    // ����ĳ��ά������ ��vos1�з���������  �� list�в������������ҳ���
			//������أ�
			//����ѭ��  ÿ����vos1�е�һ��vo ȥlist�а��������ҷ��������� vo
			//����з���������vo 
			//�ͶϿ� list ѭ�� ������һ��ѭ��
			//��ν������������� vo��vos1���ҳ���
			//��ѭ���� list�����һ��Ԫ�� ��û�з���������Ԫ��ʱ, ��vos�еĶ�Ӧvoȡ��������
			
			List<ReportBaseVO> list1=new ArrayList<ReportBaseVO>();
			int csize=vos1.length;
			for(int i=0;i<csize;i++){			
				ReportBaseVO avo=vos1[i];				
				int csize1=list.size();
				for(int j=0;j<csize1;j++){
					boolean isEqual = true;
					ReportBaseVO bvo=list.get(j);
					for (int k = 0; k < voCombinConds.length; k++) {
						Object o1 = avo.getAttributeValue(voCombinConds[k]);
						Object o2 = bvo.getAttributeValue(voCombinConds[k]);
						if (!isEqual(o1, o2)) {
							isEqual = false;
							break;
						}
					}
					if(isEqual){					
						break;
					}
					if(j==csize1-1){
						list1.add(vos1[i]);
					}
				}
			}
			//���list ����Ϊ0 ˵��vos ��vos1 û��һ��ƥ���ϵ�
			if(list.size()==0){
				for(int i=0;i<vos1.length;i++){
					list1.add(vos1[i]);
				}				
			}		
			//��û��ƥ���ϵ�vo�ϲ���
			if(list1.size()>0){
			  for(int i=0;i<vos.length;i++){
				 list1.add(vos[i]);  
			  }
			    return list1.toArray(new ReportBaseVO[0]);
			  }else{
				return vos;
			 }	
		}	
		
	/**
	 * 
	 * @���ߣ�mlr
	 * @˵�������ɽ������Ŀ
	 *        ����ĳ��ά��(����)
	 *        �����������������ֶζ�Ӧֵ��ͬ�ĺϲ�
	 *        ���� ��ֵ�ֶ�������������� �ж���Ҫ��͵��ֶ�
	 *        �����������
	 *        
	 *        ʹ�ñ�������ǰ��������
	 *        ����vo���鰴ά������ֻ�ܲ鵽һ������������vo
	 *        ����vo���鳤�ȸ����� ��ά��(������) ����vo����
	 *        ���ȶ̵�
	 *       
	 * @ʱ�䣺2011-7-11����09:12:25
	 * @param vos
	 * @param vos1
	 * @param voCombinConds �����ֶ�����
	 * @param types ��ֵ����
	 * @param combinFields ��ֵ�ֶ�
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
