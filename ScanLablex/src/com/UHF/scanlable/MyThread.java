package com.UHF.scanlable;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class MyThread implements Runnable
{
    private Integer epc;
    private Integer type=0;
    private int num=0;
    private Handler handler;
    public synchronized void run(){
        	List<NameValuePair> nameValuePairs=new ArrayList<NameValuePair>(); 
    		HttpUtil h=new HttpUtil();
    		h.setUrl(UfhData.getIP());
            nameValuePairs.add(new BasicNameValuePair("operType", type.toString()));
            nameValuePairs.add(new BasicNameValuePair("epcId", String.valueOf(epc)));//Õ–≈Ã–≈œ¢
    		Message m=new Message();
    		Log.i("httpobj",String.valueOf(epc));
    		JSONObject obj = null;
    		try {
    			obj = new JSONObject(h.getHttpResult(nameValuePairs));
    		} catch (JSONException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
    		Log.i("httpobj",obj.toString());
    		m.obj=obj;
    		m.arg1=num;
    		m.what=type;
    		handler.sendMessage(m);
    }
    
	public Integer getEpc() {
		return epc;
	}

	public void setEpc(Integer epc) {
		this.epc = epc;
	}

	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	public Handler getHandler() {
		return handler;
	}
	public void setHandler(Handler handler) {
		this.handler = handler;
	}
    
}
