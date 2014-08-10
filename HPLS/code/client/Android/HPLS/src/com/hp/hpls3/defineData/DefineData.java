package com.hp.hpls3.defineData;

public class DefineData {
	
	private String metaData;
	private String type;
	private String model;
	private String directory;
	
	
	public DefineData()
	{
		
	}
	
	public DefineData(String metaData)
	{
		this.metaData = metaData;
		defineMetaData();
	}
	
	private void defineMetaData()
	{
		String[] values = this.metaData.split(",");
		System.out.println("values: " + values);
		for(String value : values)
		{
			if(value.equalsIgnoreCase("model"))
			{
				this.model = decodeMetaData(value);
			}
			
			else if(value.equalsIgnoreCase("type"))
			{
				this.type = decodeMetaData(value);
			}
		}
		
		this.directory = this.type + "/" + this.model;
		System.out.println("directory: " + this.directory);
	}
	
	private String decodeMetaData(String value)
	{		
		String[] details = value.split(":");
		System.out.println("details: " + details);
		String detail = details[1];
		System.out.println("detail: " + detail);
		return detail;
	}
	
	public String getType()
	{
		return this.type;
	}
	
	public String getModel()
	{
		return this.model;
	}
	
	public String getDirectory()
	{
		return this.directory;
	}
	
	
}
