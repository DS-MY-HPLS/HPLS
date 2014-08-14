package com.alicia.defineData;

import java.security.spec.PSSParameterSpec;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

public class DefineData implements Parcelable{
	
	private String metaData;
//	private String ar_img;
	private String ar_type;
	private String code;
	private String title;
	private String price;
	private String description;
	private String url_datasheet;
	private String url_video;
	private String url_buynow;
	private String url_reseller;
	private String url_images;	
	
	public DefineData()
	{
		
	}
	
	public DefineData(String metaData)
	{
		this.metaData = metaData;
		defineMetaData();
	}
	
	public void defineMetaData(String JsonObject)
	{
		this.metaData = JsonObject;
		defineMetaData();
	}
	
	private void defineMetaData()
	{
		try
		{
			JSONObject json = new JSONObject(metaData);
//			this.ar_img = json.getString("ar_img");
			this.ar_type = json.getString("ar_type");
			this.code = json.getString("code");
			this.title = json.getString("title");
			this.price = json.getString("price");
			this.description = json.getString("description");
			this.url_datasheet = json.getString("url_datasheet");
			this.url_video = json.getString("url_video");
			this.url_buynow = json.getString("url_buynow");
			this.url_reseller = json.getString("url_reseller");
			this.url_images = json.getString("url_images");
//			String value = json.getString("any");
			
			displayData();
		}
		catch(JSONException ex)
		{
			ex.printStackTrace();
			Log.e("Json Error", ex.getLocalizedMessage());
		}
	}
	
	public void displayData()
	{
		Log.d("meataData", metaData);
//		Log.d("ar_img", ar_img);
		Log.d("ar_type", ar_type);
		Log.d("code", code);
		Log.d("title", title);
		Log.d("price", price);
		Log.d("description", description);
		Log.d("url_datasheet", url_datasheet);
		Log.d("url_video", url_video);
		Log.d("url_buynow", url_buynow);
		Log.d("url_reseller", url_reseller);
		Log.d("url_images", url_images);
	}
	
//	public String getAr_img()
//	{
////		return this.ar_img;
//	}
	
	public String getAr_type()
	{
		return this.ar_type;
	}
	
	public String getCode()
	{
		return this.code;
	}
	
	public String getTitle()
	{
		return this.title;
	}
	
	public String getPrice()
	{
		return this.price;
	}
	
	public String getDescription()
	{
		return this.description;
	}
	
	public String getUrl_datasheet()
	{
		return this.url_datasheet;
	}
	
	public String getUrl_video()
	{
		return this.url_video;
	}
	
	public String getUrl_buynow()
	{
		return this.url_buynow;
	}
	
	public String getUrl_reseller()
	{
		return this.url_reseller;
	}
	
	public String getUrl_images()
	{
		return this.url_images;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		
		dest.writeString(this.metaData);
		dest.writeString(this.ar_type);
		dest.writeString(this.code);
		dest.writeString(this.title);
		dest.writeString(this.price);
		dest.writeString(this.description);
		dest.writeString(this.url_datasheet);
		dest.writeString(this.url_video);
		dest.writeString(this.url_buynow);
		dest.writeString(this.url_reseller);
		dest.writeString(this.url_images);
	}
	
	public static final Parcelable.Creator<DefineData> CREATOR = new Creator<DefineData>() {

		@Override
		public DefineData createFromParcel(Parcel source) {
			// TODO Auto-generated method stub
			DefineData passDd = new DefineData();
			passDd.metaData = source.readString();
			passDd.ar_type = source.readString();
			passDd.code = source.readString();
			passDd.title = source.readString();
			passDd.price = source.readString();
			passDd.description = source.readString();
			passDd.url_datasheet = source.readString();
			passDd.url_video = source.readString();
			passDd.url_buynow = source.readString();
			passDd.url_reseller = source.readString();
			passDd.url_images = source.readString();
			return passDd;
		}

		@Override
		public DefineData[] newArray(int size) {
			// TODO Auto-generated method stub
			return new DefineData[size];
		}
	};
}
