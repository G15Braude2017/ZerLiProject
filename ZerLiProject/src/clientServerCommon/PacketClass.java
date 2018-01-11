package clientServerCommon;

import java.io.Serializable;
import java.util.ArrayList;

public class PacketClass implements Serializable{

	private String sqlCommand;
	private ArrayList<ArrayList<String>> DataList = new ArrayList<ArrayList<String>>();
	
	private boolean returnWithResult = true;
	private boolean succeedCommand = false;
	private int guiHandle;
	
	// Images data
	public ArrayList<MyFile> fileList = new ArrayList<MyFile>();
	
	
	public PacketClass(String sqlstr, int Handle , boolean readSql)
	{
		sqlCommand = sqlstr;
		guiHandle = Handle;
		returnWithResult = readSql;
		
	}
	
	public String getSqlCommand()
	{
		return new String(sqlCommand);
	}
	
	public boolean getReturnWithResult()
	{
		return returnWithResult;
	}
	
	public void setSuccessSql(boolean sqlSucceed)
	{
		succeedCommand = sqlSucceed;
	}
	
	
	public int getGuiHandle()
	{
		return guiHandle;
	}

	
	public boolean getSuccessSql()
	{
		return succeedCommand;
	}
	
	public void setResults (ArrayList<String> rawData)
	{
		ArrayList<String> internalList = new ArrayList<String>();
		
		for(String str: rawData)
		{
			internalList.add(str);
		}
		
		this.DataList.add(internalList);
		
	}
	
	public Object getResults ()
	{
		
		if(DataList.size() == 0)
			return null;
		
		// TODO should be cloned
		return DataList;
	}
	
	
}
