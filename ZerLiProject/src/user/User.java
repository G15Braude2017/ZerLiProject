package user;

import client.Main;
import client.Main.Permission;

public class User 
{
	private String userName;
	private String password;
	private boolean connected;
	private Permission permission;
	
	private int storeID;
	
	
	public User(String Uname, String pass, boolean conn, int per, String Sid )
	{
		userName = Uname;
		password = pass;
		connected = conn;
		permission = Main.Permission.values()[per];
		
		try {
			storeID = Integer.parseInt(Sid);
			
			if(storeID < 0)
				storeID = -1;
		}catch (Exception e) {
			storeID = -1;
		}
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
	
	public Permission getPermission()
	{
		return permission;
	}
	
	public int getStoreID()
	{
		return storeID;
	}
}
