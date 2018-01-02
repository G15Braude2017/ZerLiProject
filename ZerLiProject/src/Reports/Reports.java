package Reports;

public class Reports {
private String storeID;
private String  quarterYear;
private String  quarterNum;
public Reports()
{
	
}
public Reports(String  sid,String  qyear,String  qnum)
{
	this.storeID=sid;
	this.quarterYear=qyear;
	this.quarterNum=qnum;
}

public String  getStoreID()
{
	return this.storeID;
}
public String  getQyear()
{
	return this.quarterYear;
}
public String  getQnum()
{
	return this.quarterNum;
}
public void setStoreID(String  id)
{
	this.storeID=id;
}
public void setQyear(String  year)
{
	this.quarterYear=year;
}
public void setQnum(String  qnum)
{
	this.quarterNum=qnum;
}
}
