package Reports;

import java.util.ArrayList;

public class ComplaintReport extends Reports {
	
private ArrayList<String>  monthNum;
private ArrayList<String>  complaintAmount;
public ComplaintReport()
{
	super();
	this.monthNum=new ArrayList<String>();
	this.complaintAmount=new ArrayList<String>();
}
public ComplaintReport(String  sid,String  qyear,String  qnum,ArrayList<String>  monthNum,ArrayList<String>  complaintAmount)
	{
	
		super(sid,qyear,qnum);
		 this.monthNum=new ArrayList<String>();
		 this.monthNum=monthNum;
		 this.complaintAmount=new ArrayList<String>();
		this.complaintAmount=complaintAmount;
	}
	
public ArrayList<String>  getMonthNum()
{
	return this.monthNum;
}
public ArrayList<String>  getComplaintAmount()
{
	return this.complaintAmount; 
}
public void setMonthNum(String  month)
{
	this.monthNum.add(month);
}
public void setComplaintAmount(String  complaint)
{
	this.complaintAmount.add(complaint); 
}
}
