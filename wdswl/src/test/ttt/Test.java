package ttt;

import java.util.Calendar;
import java.util.Locale;

import nc.vo.pub.lang.UFDate;

public class Test {

	/**
	 * @���ߣ�zhf
	 * @˵�������ɽ������Ŀ 
	 * @ʱ�䣺2013-3-6����04:50:29
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		UFDate dbilldate = new UFDate("2013-02-25");
		Calendar calendar = Calendar.getInstance(Locale.CHINESE);
		calendar.setTimeInMillis(dbilldate.getMillis());
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.add(Calendar.MONTH, 1);
		System.out.println( new UFDate(calendar.getTime()));
	

	}

}
