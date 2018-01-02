package QuarterReports;

import client.Main;
import clientServerCommon.PacketClass;

public class ReportsData {
	

	public static void getComplaintReport(String store,String year,String qnum)
	{
	PacketClass packet = new PacketClass( // , ComplaintReportData
			Main.SELECTCommandStatement + "monthNum,complaintNum" + Main.FROMCommmandStatement + "complaint_reports"
			+Main.WHERECommmandStatement+"storeID="+store+"quarterYear="+year+"quarterNum="+qnum,
			Main.GetComplaintReportData, Main.READ);


	try {
		Main.getClientConsolHandle().sendSqlQueryToServer(packet);
	} catch (Exception e) {
		//updateStatusLabel("Client connection error", true);
		System.out.println("Client connection error");
	}
	}
}
