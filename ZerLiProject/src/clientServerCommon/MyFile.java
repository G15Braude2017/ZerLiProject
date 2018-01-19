package clientServerCommon;

import java.io.Serializable;

public class MyFile implements Serializable {
	
	private String ColumName=null;
	private String fileName=null;	
	private int size=0;
	public	byte[] mybytearray;
	
	
	public void initArray(int size)
	{
		mybytearray = new byte [size];	
	}
	
	public MyFile( String fileName) {
		this.fileName = fileName;
	}
	
	
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public byte[] getMybytearray() {
		return mybytearray;
	}
	
	public byte getMybytearray(int i) {
		return mybytearray[i];
	}

	public void setMybytearray(byte[] mybytearray) {
		
		for(int i=0;i<mybytearray.length;i++)
		this.mybytearray[i] = mybytearray[i];
	}

	public String getColumName() {
		return ColumName;
	}

	public void setColumName(String description) {
		ColumName = description;
	}	
}

