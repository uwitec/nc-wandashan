package nc.vo.wl.pub;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Dictionary;
import java.util.List;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pub.vosplit.SplitBillVOs;
public class CombinVO {
	  /**
	    * @���ߣ�mlr
	    * @˵����
	    *        �ж����������Ƿ����
	    * @ʱ�䣺2011-7-12����10:34:05
	    * @param o1 Ҫ�ȽϵĶ���
	    * @param o2 Ҫ�ȽϵĶ���
	    */
		private static boolean isEqual(Object o1, Object o2) {
			if(isEmpty(o1) && !isEmpty(o2)){
				return false;
			}
			if(!isEmpty(o1) && isEmpty(o2)){
				return false;
			}
		    if(!isEmpty(o1) && !isEmpty(o2)){
		    	if(!o1.equals(o2)){
		    		return false;
		    	}
		    }
		    return true;
		}
		/**
		 * @���ߣ�mlr
		 * @˵����
		 *        ����ĳ��ά��(����)
		 *        �����������������ֶζ�Ӧֵ��ͬ�ĺϲ�
		 *        ���� ��ֵ�ֶ�������������� �ж���Ҫ��͵��ֶ�
		 *        �����������
		 *        
		 *        ʹ�ñ�������ǰ��������
	     *        ����vo���鰴ά������ֻ�ܲ鵽һ������������vo           
		 * @ʱ�䣺2011-7-11����09:12:25
		 * @param vos  Ҫ�ϲ��ı���vos
		 * @param vos1 Ҫ�ϲ��ı���vos1
		 * @param voCombinConds �����ֶ�����
		 * @param types ��ֵ����
		 * @param combinFields ��ֵ�ֶ�
		 * @return
		 */
		public static CircularlyAccessibleValueObject[] combinVoByFields(CircularlyAccessibleValueObject[] vos, CircularlyAccessibleValueObject[] vos1,
				String[] voCombinConds, int[] types,String[] combinFields) {
			//��¼  vos1���Ѿ����ϲ�����vo
			List<CircularlyAccessibleValueObject> list=new ArrayList<CircularlyAccessibleValueObject>();
			if (isEmpty(vos)) {
				if (!isEmpty(vos1)) {
					return vos1;
				}
			}
			if (isEmpty(vos1)) {
				if (!isEmpty(vos)) {
					return vos;
				}
			}
			if (isEmpty(vos) &&isEmpty(vos1)) {
				return null;
			}
			int size=vos.length;
			int size1=vos1.length;
			//�� vos�е�ÿ��vo ����������vos1
			//������������ vos1�е�vo ��Ӧ��ֵ �ӵ�vos��vo��
			for (int i = 0; i < size; i++) {
				CircularlyAccessibleValueObject avo = vos[i];
				for (int j = 0; j < size1; j++) {
					CircularlyAccessibleValueObject bvo = vos1[j];
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
								int iresult = (resultobj == null ? 0: ((Integer) resultobj).intValue());
								int itmp = (tmpobj == null ? 0: ((Integer) tmpobj).intValue());
								avo.setAttributeValue(combinFields[n],new Integer(iresult + itmp));
								continue;
							case IUFTypes.LONG:
								long lgtmp = (tmpobj == null ? 0:((Long) tmpobj).longValue());
								long lgresult = (resultobj == null ? 0:((Long) resultobj).longValue());
								if (tmpobj != null)
									avo.setAttributeValue(combinFields[n],new Long(lgresult + lgtmp));
								continue;
							case IUFTypes.UFD:
								UFDouble ufdtmp = (tmpobj == null ? new UFDouble("0"): (UFDouble) tmpobj);
								UFDouble ufdResult = (resultobj == null ? new UFDouble("0"):(UFDouble) resultobj);
								avo.setAttributeValue(combinFields[n],ufdResult.add(ufdtmp));
								continue;
							case IUFTypes.STR:
								String strtmp = (tmpobj == null ? "" : tmpobj.toString());
								String strresult = (resultobj == null ? "": resultobj.toString());
								avo.setAttributeValue(combinFields[n], strtmp+ strresult);
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
			List<CircularlyAccessibleValueObject> list1=new ArrayList<CircularlyAccessibleValueObject>();//��¼vos1��û�б�ƥ���vo
			int csize=vos1.length;
			for(int i=0;i<csize;i++){			
				CircularlyAccessibleValueObject avo=vos1[i];				
				int csize1=list.size();
				for(int j=0;j<csize1;j++){
					boolean isEqual = true;
					CircularlyAccessibleValueObject bvo=list.get(j);
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
			    return list1.toArray(new CircularlyAccessibleValueObject[0]);
			  }else{
				return vos;
			 }	
		}			
		/**
		 * 
		 * @���ߣ�mlr
		 * @˵����
		 *        ����ĳ��ά��(����)
		 *        �������������ֶζ�Ӧֵ��ͬ�ĺϲ�
		 *                  
		 * @ʱ�䣺2011-7-11����09:12:25
		 * @param vos
		 * @param voCombinConds �����ֶ�����
		 * @param types ��ֵ����
		 * @param combinFields ����ֶ�
		 * @return
		 */
		public static CircularlyAccessibleValueObject[] combinVoByFields(CircularlyAccessibleValueObject[] vos,String[] voCombinConds, int[] types,String[] combinFields) {

		 if(vos==null || vos.length==0){
			  return vos;
		  }
		 CircularlyAccessibleValueObject[][] voss= SplitBillVOs.getSplitVOs(vos,voCombinConds);
			//new ��ͷ��voΪ������װ��������vo
			CircularlyAccessibleValueObject[] newVos=new CircularlyAccessibleValueObject[voss.length];
			int size=voss.length;
			for(int i=0;i<size;i++){
				CircularlyAccessibleValueObject newVo=null;
				int size1=voss[i].length;
			    for(int j=0;j<size1;j++){
			    	CircularlyAccessibleValueObject oldVo=(CircularlyAccessibleValueObject) voss[i][j];
			    	if(newVo==null){
			    	   newVo=(CircularlyAccessibleValueObject) oldVo.clone();
			        }else{
						int csize = combinFields.length;
						for (int n = 0; n < csize; n++) {
							Object resultobj = newVo.getAttributeValue(combinFields[n]);
							Object tmpobj = oldVo.getAttributeValue(combinFields[n]);
							switch (types[n]) {
							case IUFTypes.INT:
								int iresult = (resultobj == null ? 0: ((Integer) resultobj).intValue());
								int itmp = (tmpobj == null ? 0:((Integer) tmpobj).intValue());
								newVo.setAttributeValue(combinFields[n],new Integer(iresult + itmp));
								continue;
							case IUFTypes.LONG:
								long lgtmp = (tmpobj == null ? 0: ((Long) tmpobj).longValue());
								long lgresult = (resultobj == null ? 0:((Long) resultobj).longValue());
								if (tmpobj != null)
									newVo.setAttributeValue(combinFields[n],new Long(lgresult + lgtmp));
								continue;
							case IUFTypes.UFD:
								UFDouble ufdtmp = (tmpobj == null ? new UFDouble("0"): (UFDouble) tmpobj);
								UFDouble ufdResult = (resultobj == null ? new UFDouble("0"): (UFDouble) resultobj);
								newVo.setAttributeValue(combinFields[n],ufdResult.add(ufdtmp));
								continue;
							case IUFTypes.STR:
								String strtmp = (tmpobj == null ? "" : tmpobj.toString());
								String strresult = (resultobj == null ? "": resultobj.toString());
								newVo.setAttributeValue(combinFields[n], strtmp+ strresult);
								continue;
							}
						}		        	
			        }	    	
			    }
			    newVos[i]=newVo;
			}
			return newVos;				
		}	
	/**
	 * 
	 * @���ߣ�mlr
	 * @˵����
	 *       �жϴ���Ķ����Ƿ�Ϊ��
	 * @ʱ�䣺2011-7-5����09:02:51
	 * @param value
	 * @return
	 */
	public static boolean isEmpty(Object value)
	{
		if (value == null)
			return true;
		if ((value instanceof String)
				&& (((String) value).trim().length() <= 0))
			return true;
		if ((value instanceof Object[]) && (((Object[]) value).length <= 0))
			return true;
		if ((value instanceof Collection) && ((Collection) value).size() <= 0)
			return true;
		if ((value instanceof Dictionary) && ((Dictionary) value).size() <= 0)
			return true;
		return false;
	}		
}
