package CustomerService;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import client.Main;
import javafx.application.Platform;

public class UpdateTimerComplaint extends Thread {

	private static boolean timerFlag = true;

	public void run() {
		try {
			updateTimeLeftTimer("2017-12-27 20:15:00");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void updateTimeLeftTimer(String SqlFullDate) throws Exception {

		String timeToString = "";
		int Fhour, Fminuts, Fseconds;
		int totalSeconds;

		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		while (timerFlag) {
			
			Date date = format.parse(SqlFullDate);

			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			cal.add(Calendar.HOUR_OF_DAY, 24);
			
			if (Calendar.getInstance().after(cal)) {
				Main.getCustomerServiceMainControl().getFollowComplaintControl().updateTimeComplaint("24h over", true);
				timerFlag = false;
			} else {

				cal.add(cal.HOUR_OF_DAY, -(Calendar.getInstance().getTime().getHours()));
				cal.add(cal.MINUTE, -(Calendar.getInstance().getTime().getMinutes()));
				cal.add(cal.SECOND, -(Calendar.getInstance().getTime().getSeconds()));

				Main.getCustomerServiceMainControl().getFollowComplaintControl()
						.updateTimeComplaint(cal.getTime().getHours() + ":" + cal.getTime().getMinutes() + ":"
								+ cal.getTime().getSeconds(), false);

				Thread.sleep(500);
			}
		}

		timerFlag = true;
	}

}
