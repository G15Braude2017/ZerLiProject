package server;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;


public class ReportsThread extends Thread {

	private static String timeStampStr = null;
	private static boolean reportThreadEnable = true;

	public void run() {
		
		// TODO put here the query to get date of the last report generation
		
		if (reportThreadEnable) {

			try {
				reportsGenerator(timeStampStr);

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	private static void reportsGenerator(String SqlFullDate) throws Exception {

		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		while (reportThreadEnable) {

			Date date = format.parse(SqlFullDate);

			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			cal.add(Calendar.MONTH, 3);

			if (Calendar.getInstance().after(cal)) {
				
				// TODO generate your reports , don't forget about csv file.

			}

			TimeUnit.HOURS.sleep(24);
		}

	}

	public static String getTimeStampStr() {
		return timeStampStr;
	}

	public static void setTimeStampStr(String timeStampStr) {
		ReportsThread.timeStampStr = timeStampStr;
	}

	public static boolean isReportThreadEnable() {
		return reportThreadEnable;
	}

	public static void setReportThreadEnable(boolean reportThreadEnable) {
		ReportsThread.reportThreadEnable = reportThreadEnable;
	}

}
