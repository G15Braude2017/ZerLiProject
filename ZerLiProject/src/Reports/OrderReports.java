package Reports;

public class OrderReports extends Reports{
private String productID;
private String pname;
private int order;

public OrderReports()
{
	super();
}
public OrderReports(String  sid,String  qyear,String  qnum,String productID,String pname,int order)
{
	super(sid,qyear,qnum);
	this.productID=productID;
	this.pname=pname;
	this.order=order;
}
public String getProductID() {
	return productID;
}
public void setProductID(String productID) {
	this.productID = productID;
}
public String getPname() {
	return pname;
}
public void setPname(String pname) {
	this.pname = pname;
}
public int getOrder() {
	return order;
}
public void setOrder(int order) {
	this.order = order;
}


}
