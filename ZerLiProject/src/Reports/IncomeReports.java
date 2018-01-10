package Reports;

public class IncomeReports extends Reports{
private int income;

public IncomeReports()
{
	super();
}
public IncomeReports(String  sid,String  qyear,String  qnum,int income)
{
	super(sid,qyear,qnum);
	this.income=income;
}

public int getIncome() {
	return income;
}
public void setIncome(int income) {
	this.income = income;
}

}
