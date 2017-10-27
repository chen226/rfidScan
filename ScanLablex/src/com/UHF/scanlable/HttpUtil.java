package com.UHF.scanlable;

import java.io.IOException;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;


public class HttpUtil {
	private String url="http://10.4.4.199:8080/gmp-fy";
	
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getHttpResult(List<NameValuePair> nameValuePairs){

		HttpClient httpClient = new DefaultHttpClient();
		HttpPost post = new HttpPost(url+"/servlet/RFIDInterfaceServlet");
    	HttpResponse rsp;
		try {
			post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			rsp = httpClient.execute(post);
			if(rsp.getStatusLine().getStatusCode() == 200)  
            {  
                HttpEntity httpEntity = rsp.getEntity();  
    			String displayString = EntityUtils.toString(httpEntity);
    			if (httpEntity != null) {
    				httpEntity.consumeContent();
    			}
    			return displayString;
            } 
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
