package com.hp.hpls3.defineData;

import android.os.Parcel;
import android.os.Parcelable;

public class DefineData implements Parcelable{
	
	private String metaData;
	private String directory;
	private String type;
	private String model;
	
	
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
			if(value.indexOf("type") != -1)
			{
				type = decodeMetaData(value);
			}
			
			else if(value.indexOf("model") != -1)
			{
				model = decodeMetaData(value);
			}
		}
		
		directory = type + "/" + model;
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
	
	public DefineData(Parcel source) {
		// TODO Auto-generated method stub
//		this.metaData = source.readString();
		this.directory = source.readString();
//		this.type = source.readString();
//		this.model = source.readString();
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		
//		dest.writeStringList(val);
//		dest.writeString(metaData);
		dest.writeString(directory);
//		dest.writeString(type);
//		dest.writeString(model);		
	}
	
	static final Parcelable.Creator<DefineData> CREATOR = 
			new Parcelable.Creator<DefineData>() {
		
		@Override
		public DefineData[] newArray(int size) {
			// TODO Auto-generated method stub
			return new DefineData[size];
		}
		
		@Override
		public DefineData createFromParcel(Parcel source) {
			// TODO Auto-generated method stub
			return new DefineData(source);
		}		
	};
}
