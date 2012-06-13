package nc.vo.dm.so.deal2;

import java.io.Serializable;
import java.util.Comparator;
import nc.vo.dm.so.deal.SoDealVO;
import nc.vo.pub.lang.UFDate;

/**
 * 
 * @author zhf  日期比较器  用于  销售订单安排处
 *
 */
public class DateComparator implements Comparator<SoDealVO>,Serializable{

	public int compare(SoDealVO o1, SoDealVO o2) {
		// TODO Auto-generated method stub
		if(o1 == null)
			return 1;
		if(o2 == null)
			return -1;
		UFDate dbilldate1 = o1.getDbilldate();
		UFDate dbilldate2 = o2.getDbilldate();
		if(dbilldate1 == null || dbilldate2 == null){
			return 0;
		}
		return dbilldate2.compareTo(dbilldate1);
	}
}
