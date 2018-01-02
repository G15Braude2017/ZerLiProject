package user;

import java.util.EnumSet;

public class User 
{
	private String userName;
	private String password;
	private boolean connected;
	public enum permission { ShopWorker, Customer, CustomerService, Expert, SystemManager, CompanyManager, StoreManager, CompenyEmployee};
	private permission Permission;
	
	private String storeID;
	
	public User(String Uname, String pass, boolean conn, int per, String Sid )
	{
		userName = Uname;
		password = pass;
		connected = conn;
		Permission = permission.values()[per];
		storeID = Sid;
	}
	
	public String getUserName()
	{
		return userName;
	}
	
	public String getPassword()
	{
		return password;
	}
	
	public boolean getConnected()
	{
		return connected;
	}
	
	public permission getPermission()
	{
		return Permission;
	}
	
	public String getStoreID()
	{
		return storeID;
	}
}
