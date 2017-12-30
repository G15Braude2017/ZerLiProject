package CustomerService;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import client.Main;
import javafx.application.Platform;

public class UpdateTimerComplaint extends Thread {

	private boolean timerFlag = true;
	private String timeStampStr = null;

	public UpdateTimerComplaint(String timeStamp) {
		timeStampStr = timeStamp;
	}

	public void run() {
		if (timeStampStr != null) {

			try {
				updateTimeLeftTimer(timeStampStr);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else
			Main.getCustomerServiceMainControl().getFollowComplaintControl().updateTimeComplaint("No time info", true);

	}

	private void updateTimeLeftTimer(String SqlFullDate) throws Exception {
		
		timerFlag = true;

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

				Main.getCustomerServiceMainControl().getFollowComplaintControl().updateTimeComplaint(
						cal.getTime().getHours() + ":" + cal.getTime().getMinutes() + ":" + cal.getTime().getSeconds(),
						false);

				Thread.sleep(500);
			}
		}

		timerFlag = true;
		timeStampStr = null;
	}
	
	public boolean isTimerFlag() {
		return timerFlag;
	}

	public void setTimerFlag(boolean timerFlag) {
		this.timerFlag = timerFlag;
	}

}
