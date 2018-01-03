package user;


import java.util.EnumSet;

import client.Main;
import client.Main.Premission;

public class User 
{
	private String userName;
	private String password;
	private boolean connected;
	public enum permission { ShopWorker, Customer, CustomerService, Expert, SystemManager, CompanyManager, StoreManager, CompenyEmployee;};
	private permission Permission;
	private Premission permission;
	
	private int storeID;
	
	
	public User(String Uname, String pass, boolean conn, int per, int Sid )
	{
		userName = Uname;
		password = pass;
		connected = conn;
		permission = Main.Premission.values()[per];
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
	
	public Premission getPermission()
	{
		return permission;
	}
	
	public int getStoreID()
	{
		return storeID;
	}
}
