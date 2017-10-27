package com.UHF.scanlable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ActivityGroup;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ScanMode extends Activity implements OnClickListener, OnItemClickListener{
	
	private String mode;
	private Map<String,Integer> data=new HashMap<String, Integer>();
	private Map<String,Integer> epcData=new HashMap<String, Integer>();
	
	Button scanStock;
	Button scanTray;
	Button scanMaterial;
	Button scan;
	ListView listView;
	TextView txNum;
	TextView documentNo;
	String sacnType;
	private Timer timer;
	private MyAdapter myAdapter;
	private Handler mHandler;
	private boolean isCanceled = true;
	
	private static final int SCAN_INTERVAL = 10;
	
	private static final int MSG_UPDATE_LISTVIEW = 0;
	private static final int MODE_B = 0;
	private static final int MODE_C = 1;
	private boolean Scanflag=false;
	private String type="";
	private Map serialNo=new HashMap();
	private Map serialNoScan=new HashMap();
	private Map<String, String> trayNo=new HashMap<String, String>();
	private Map<String, String> stocklNo=new HashMap();
	private Set<String> trayList=new HashSet();
	private Set stockList;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Log.i("demo",">>>>>>>>>>>>>>>>>>>>> mode onCreate");
		setContentView(R.layout.query);
		mode = getIntent().getStringExtra(MainActivity.EXTRA_MODE);
		scan = (Button)findViewById(R.id.button_scanx);
		scan.setOnClickListener(this);
		scanMaterial = (Button)findViewById(R.id.button_scan);
		scanMaterial.setOnClickListener(this);
		scanStock = (Button)findViewById(R.id.button_stock);
		scanStock.setOnClickListener(this);
		scanTray = (Button)findViewById(R.id.button_Tray);
		scanTray.setOnClickListener(this);
		if(mode.equals(MainActivity.TABLE_6B)){
			scanMaterial.setVisibility(View.INVISIBLE);
			scanTray.setVisibility(View.INVISIBLE);
			scanStock.setText(R.string.scan);
			scan.setText("确定");
		}else{
			scanMaterial.setVisibility(View.VISIBLE);
			scanTray.setVisibility(View.VISIBLE);
			scanStock.setText(R.string.scanStock);
			scan.setText("scan");
		}
		
		listView = (ListView)findViewById(R.id.list);//
		listView.setOnItemClickListener(this);
		data = new HashMap<String, Integer>();
		txNum = (TextView)findViewById(R.id.tx_num);
		mHandler = new Handler(){

			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				if(isCanceled) return;
				switch (msg.what) {
				case MSG_UPDATE_LISTVIEW:
					if(mode.equals(MainActivity.TABLE_6B)){
						data = checkMap(UfhData.scanResult6c);
					}else {
						if("0".equals(sacnType)){
							data = UfhData.scanResult6c;
						}else if("3".equals(sacnType)){//包装详情
							data = checkMap(UfhData.scanResult6c);
						}
//						data = UfhData.scanResult6c;
					}
					if(data==null||data.isEmpty()){
						break;
					}
					if(myAdapter == null){
						myAdapter = new MyAdapter(ScanMode.this, new ArrayList(data.keySet()));
						listView.setAdapter(myAdapter);
					}else{
						myAdapter.mList = new ArrayList(data.keySet());
					}
					txNum.setText(String.valueOf(myAdapter.getCount()));
					myAdapter.notifyDataSetChanged();
					break;

				default:
					break;
				}
				super.handleMessage(msg);
			}

		};
	}
	private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
        		super.handleMessage(msg);
    			JSONObject obj=(JSONObject) msg.obj;
    			int arg1= msg.arg1;
    			Integer arg2= msg.arg2;
                switch (msg.what) {
    			case 2:
    				if(obj==null){
    					break;
    				}
    				try {
    					data.put((String) obj.get("code"),arg1);
    					trayNo.put((String) obj.get("code"),checkHex(Integer.toHexString(Integer.parseInt(obj.get("epcId").toString()))));
    				} catch (JSONException e) {
    					// TODO Auto-generated catch block
    					e.printStackTrace();
    				}
    				if(data==null||data.isEmpty()){
    					break;
    				}
    				if(myAdapter == null){
    					myAdapter = new MyAdapter(ScanMode.this, new ArrayList(data.keySet()));
    					listView.setAdapter(myAdapter);
    				}else{
    					myAdapter.mList = new ArrayList(data.keySet());
    				}
    				txNum.setText(String.valueOf(myAdapter.getCount()));
    				myAdapter.notifyDataSetChanged();
    				break;
    			case 3:
    				if(obj==null){
    					break;
    				}
    				try {
    					data.put((String) obj.get("positionCode"),arg1);
    					stocklNo.put((String) obj.get("positionCode"),checkHex(Integer.toHexString(Integer.parseInt(obj.get("epcId").toString()))));
    				} catch (JSONException e) {
    					// TODO Auto-generated catch block
    					e.printStackTrace();
    				}
    				if(data==null||data.isEmpty()){
    					break;
    				}
    				if(myAdapter == null){
    					myAdapter = new MyAdapter(ScanMode.this, new ArrayList(data.keySet()));
    					listView.setAdapter(myAdapter);
    				}else{
    					myAdapter.mList = new ArrayList(data.keySet());
    				}
    				txNum.setText(String.valueOf(myAdapter.getCount()));
    				myAdapter.notifyDataSetChanged();
    				break;
    			case 7:
    				if(msg.arg1==3){
    					showMsg();
    				}
    				break;
    			default:
    				break;
    			}
            }

    };
    private void showMsg(){
		Toast.makeText(this, "出库扫描成功", Toast.LENGTH_LONG).show();
    }
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Log.i("zhouxin",">>>mode="+mode+"====="+this);
		txNum.setText("0");
		
	}
	private Map checkJson(String json) {
		// TODO Auto-generated method stub
		Map map=new HashMap();
		try {
			JSONArray objList=new JSONArray(json);
            for (int i = 0; i< objList.length(); i++) {
                //循环遍历，依次取出JSONObject对象
                //用getInt和getString方法取出对应键值
                JSONObject obj = objList.getJSONObject(i);
                map.put(checkHex(Integer.toHexString(Integer.valueOf(obj.get("epcId").toString()))),obj.get("packageInventoryNum"));//epc,流水号
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return map;
	}
	
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		int id = arg0.getId();
		/*if(id == R.id.button_scan){
			type="0001";
		}else if(id == R.id.button_Tray){
			type="0002";
		}else if(id == R.id.button_stock){
			type="0003";
		}else if(id == R.id.button_scanx){
			type="";
		}*/
		if(!UfhData.isDeviceOpen()){
			Toast.makeText(this, R.string.open_warning, Toast.LENGTH_LONG).show();
			return;
		}
		if(timer == null){
			data=new HashMap<String, Integer>();
	///////////声音开关初始化
				UfhData.Set_sound(true);
				UfhData.SoundFlag=false;
				//////////
				isCanceled = false;
				timer = new Timer();

				if (myAdapter != null) {
					if(mode.equals(MainActivity.TABLE_6B)){
						UfhData.scanResult6b.clear();
					}else if(mode.equals(MainActivity.TABLE_6C)){
						UfhData.scanResult6c.clear();
					}
					myAdapter.mList.clear();
					myAdapter.notifyDataSetChanged();
					mHandler.removeMessages(MSG_UPDATE_LISTVIEW);
					mHandler.sendEmptyMessage(MSG_UPDATE_LISTVIEW);
				}
			if(id == R.id.button_scanx){
				type="";
//				epcData=new HashMap<String, Integer>();
				sacnType="0";
				if(mode.equals(MainActivity.TABLE_6B)){
			    	UfhData.setSerialNo(serialNoScan);
					TextView documentNo=(TextView)findViewById(R.id.documentNo);
					final String document_No=documentNo.getText().toString();
					new Thread(new Runnable(){
						public void run(){
							List<NameValuePair> nameValuePairs=new ArrayList<NameValuePair>(); 
							HttpUtil h=new HttpUtil();
				    		h.setUrl(UfhData.getIP());
							nameValuePairs.add(new BasicNameValuePair("operType", "7"));//物料入托
							Map serialNo=UfhData.getSerialNo();
					        nameValuePairs.add(new BasicNameValuePair("materialEpcId", checkSerialNo(serialNo)));//物料详情
					        nameValuePairs.add(new BasicNameValuePair("extendOrderCode", document_No));//物料详情
					        String result=h.getHttpResult(nameValuePairs);
							Message m=new Message();
							m.what=7;
							m.arg1=Integer.valueOf(result.replaceAll("\"", ""));
	                        handler.sendMessage(m);
							}
						}).start();
					return;
				}else{
					scan.setText("Stop");
				}
			}else if(id == R.id.button_Tray){
				type="0002";
				epcData=new HashMap<String, Integer>();
				trayNo=new HashMap();
				UfhData.scanResult6c=new HashMap<String, Integer>();
				sacnType="1";
				scanTray.setText("Stop");
				timer.schedule(new TimerTask() {
					@Override
					public void run() {
						if(Scanflag)return;
							Scanflag=true;
						UfhData.read6c1(type,handler);
						Scanflag=false;
						handler.removeMessages(MSG_UPDATE_LISTVIEW);
						handler.sendEmptyMessage(MSG_UPDATE_LISTVIEW);
//						Scanflag=false;
					}
				}, 0, SCAN_INTERVAL);
				return;
			}else if(id == R.id.button_stock){
				scanStock.setText("Stop");//
				if(mode.equals(MainActivity.TABLE_6B)){
					UfhData.scanResult6c=new HashMap<String, Integer>();
					serialNo=new HashMap();
					serialNoScan=new HashMap();
					type="0001";
					TextView documentNo=(TextView)findViewById(R.id.documentNo);
					final String document_No=documentNo.getText().toString();
					if("".equals(document_No)){
						Toast.makeText(this, R.string.noDocumentNo, Toast.LENGTH_LONG).show();
						return;
					}
					new Thread(new Runnable(){
						public void run(){
							List<NameValuePair> nameValuePairs=new ArrayList<NameValuePair>(); 
							HttpUtil h=new HttpUtil();
				    		h.setUrl(UfhData.getIP());
					        nameValuePairs.add(new BasicNameValuePair("operType", "5"));
					        nameValuePairs.add(new BasicNameValuePair("extendOrderCode", document_No));//根据批号扫物料（入库）
							serialNo=checkJson(h.getHttpResult(nameValuePairs));
							}
						}).start();
				}else{
					type="0003";
					epcData=new HashMap<String, Integer>();
					stocklNo=new HashMap();
					UfhData.scanResult6c=new HashMap<String, Integer>();
					sacnType="2";
					timer.schedule(new TimerTask() {
						@Override
						public void run() {
							if(Scanflag)return;
								Scanflag=true;
							UfhData.read6c1(type,handler);
							Scanflag=false;
							handler.removeMessages(MSG_UPDATE_LISTVIEW);
							handler.sendEmptyMessage(MSG_UPDATE_LISTVIEW);
						}
					}, 0, SCAN_INTERVAL);
					return;
				}
			}else if(id == R.id.button_scan){
				type="0001";
				serialNo=new HashMap();
				serialNoScan=new HashMap();
//				epcData=new HashMap<String, Integer>();
				sacnType="3";
				TextView documentNo=(TextView)findViewById(R.id.documentNo);
				final String document_No=documentNo.getText().toString();
				if("".equals(document_No)){
					Toast.makeText(this, R.string.noDocumentNo, Toast.LENGTH_LONG).show();
					return;
				}
				new Thread(new Runnable(){
					public void run(){
						List<NameValuePair> nameValuePairs=new ArrayList<NameValuePair>(); 
						HttpUtil h=new HttpUtil();
			    		h.setUrl(UfhData.getIP());
				        nameValuePairs.add(new BasicNameValuePair("operType", "4"));//根据批号查询包装
				        nameValuePairs.add(new BasicNameValuePair("batchNo", document_No));//根据批号扫物料（入库）
						serialNo=checkJson(h.getHttpResult(nameValuePairs));
						}
					}).start();
				scanMaterial.setText("Stop");
			}
			//
			timer.schedule(new TimerTask() {
				@Override
				public void run() {
					if(Scanflag)return;
						Scanflag=true;
					UfhData.read6c(type);
					mHandler.removeMessages(MSG_UPDATE_LISTVIEW);
					mHandler.sendEmptyMessage(MSG_UPDATE_LISTVIEW);
					Scanflag=false;
				}
			}, 0, SCAN_INTERVAL);
			
			
		}else{
			//cancelScan();
			isCanceled = true;
			if(timer != null){
				timer.cancel();
				timer = null;
				if(id == R.id.button_scanx){
					scan.setText("scan");
				}else if(id == R.id.button_Tray){
					scanTray.setText(R.string.sacnTray);
				}else if(id == R.id.button_stock){
					scanStock.setText(R.string.scanStock);
				}else if(id == R.id.button_scan){
					scanMaterial.setText(R.string.scan);
				}
			}
			UfhData.Set_sound(false);
		}
	}
	
	private void cancelScan(){
		isCanceled = true;
		mHandler.removeMessages(MSG_UPDATE_LISTVIEW);
		if(timer != null){
			timer.cancel();
			timer = null;
			scan.setText("Scan");
			if(mode.equals(MainActivity.TABLE_6B)){
				UfhData.scanResult6b.clear();
			}else if(mode.equals(MainActivity.TABLE_6C)){
				UfhData.scanResult6c.clear();
			}
			if (myAdapter != null) {
				myAdapter.mList.clear();
				myAdapter.notifyDataSetChanged();
			}
			txNum.setText("0");
		}
	}
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
		// TODO Auto-generated method stub
		UfhData.Set_sound(false);
		Log.i("zhouxin","=====onItemClick=========");
		String id = myAdapter.mList.get(position);
		Intent intent = new Intent(this,ReadWriteActivity.class);
		intent.putExtra(MainActivity.EXTRA_MODE, mode);
//		intent.putExtra(MainActivity.EXTRA_EPC, myAdapter.mList.get(position));
		UfhData.setUfh_id(myAdapter.mList.get(position));
		UfhData.setSacnType(sacnType);
	    if(sacnType.equals("0")){
	    	UfhData.setEpc_id(myAdapter.mList.get(position));
	    }else if(sacnType.equals("1")){//点击托盘
			for(Map.Entry<String, String> entry:trayNo.entrySet()){  
				if(entry.getKey().equals(myAdapter.mList.get(position))) {
		  	        String s = entry.getValue();
		  			UfhData.setEpc_id(s);
		  			break;
		  	      }
			}
	    	UfhData.setSerialNo(serialNoScan);
	    }else if(sacnType.equals("2")){//点击库位
	    	for(Map.Entry<String, String> entry:stocklNo.entrySet()){  
				if(entry.getKey().equals(myAdapter.mList.get(position))) {
		  	        String s = entry.getValue();
		  			UfhData.setEpc_id(s);
		  			break;
		  	      }
			}
	    	UfhData.setTrayNo(trayNo);
	    }
	    else if(sacnType.equals("3")){//点击托盘
			Set set = serialNo.entrySet();
		    Iterator it = set.iterator();
	    	while(it.hasNext()) {
	  	      Map.Entry entry = (Map.Entry)it.next();
	  	      if(entry.getValue().equals(myAdapter.mList.get(position))) {
	  	        String s = entry.getKey().toString();
	  			UfhData.setEpc_id(s);
	  			break;
	  	      }
	  	    }
	    }
		goActivty(intent);
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		UfhData.Set_sound(false);
		cancelScan();
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		Log.i("zhouxin",">>>>>>>>>>>>>>>>>>>>> mode onDestroy");
		super.onDestroy();
	}
	
	private void goActivty(Intent intent){
		Log.i("zhouxin","------------------go");
        Window w = ((ActivityGroup)getParent()).getLocalActivityManager()  
                .startActivity("SecondActivity",intent);  
        View view = w.getDecorView();  
        ((ActivityGroup)getParent()).setContentView(view);
        Log.i("zhouxin", "------------------oo");
	}
	
	class MyAdapter extends BaseAdapter{
		
		private Context mContext;
		private List<String> mList;
		private LayoutInflater layoutInflater;
		
		public MyAdapter(Context context, List<String> list) {
			mContext = context;
			mList = list;
			layoutInflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mList.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return mList.get(position);
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View view, ViewGroup viewParent) {
			// TODO Auto-generated method stub
			ItemView iv = null;
			if(view == null){
				iv = new ItemView();
				view = layoutInflater.inflate(R.layout.list, null);
				iv.tvCode = (TextView)view.findViewById(R.id.list_lable);
				iv.tvNum = (TextView)view.findViewById(R.id.list_number);
				view.setTag(iv);
			}else{
				iv = (ItemView)view.getTag();
			}
			iv.tvCode.setText(mList.get(position));
			iv.tvNum.setText(data.get(mList.get(position)).toString());
			return view;
		}
		
		public class ItemView{
			TextView tvCode;
			TextView tvNum;
		}
	}
	private Map checkMap(Map<String, Integer> map){
		if(serialNo==null||serialNo.isEmpty()){
			return null;
		}else{
			Map r=new HashMap<String, Integer>();
			Set s=serialNo.keySet();
			for(Map.Entry<String, Integer> entry:map.entrySet()){  
				String epc=checkHex(Integer.toHexString(Integer.parseInt(entry.getKey().substring(4,entry.getKey().length()),16)));
				if(s.contains(epc)){
					r.put(serialNo.get(epc), map.get(entry.getKey()));
					serialNoScan.put(epc, serialNo.get(epc));
				}
			} 
			return r;
		}
	}
	/**托盘信息 弃用
	 * @param map
	 * @return
	 */
	private Map checkTrayMap(Map<String, Integer> map){
		if(epcData==null||epcData.isEmpty()){
			epcData=map;
		}else{
			for(Map.Entry<String, Integer> entry:epcData.entrySet()){  
				map.remove(entry.getKey());
			}
		}
		if(!map.isEmpty()){
			String key="";
			String value="";
			for(Map.Entry<String, Integer> entry:map.entrySet()){  
				key=entry.getKey();
				value=entry.getValue().toString();
				epcData.put(entry.getKey(),entry.getValue());
				break;
			} 
			Integer epcxx=Integer.parseInt(checkEpc(key),16);
			Integer num=Integer.parseInt(value);
			MyThread m=new MyThread();
			m.setEpc(epcxx);
			m.setHandler(handler);
			m.setNum(num);
			m.setType(2);
			Thread thread = new Thread(m);
	        thread.start();
		}
		return new HashMap();
	}
	private String checkTrayJson(String httpResult) {
		// TODO Auto-generated method stub
		String v = null;
		Log.i("httpResult",httpResult);
		System.out.println(httpResult);

        JSONObject obj;
		try {
			obj = new JSONObject(httpResult);
	        v=(String) obj.get("code");//epc,流水号
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return v;
	}
	private String checkEpc(String epc) {
		// TODO Auto-generated method stub
		epc=epc.substring(4, epc.length());
		return epc;
	}
	/**库位信息
	 * @param map
	 * @return
	 */
	private Map checkStockMap(Map<String, Integer> map){
		Map<String, Integer> r=new HashMap<String, Integer>();
		if(epcData==null||epcData.isEmpty()){
			epcData=map;
		}else{
			for(Map.Entry<String, Integer> entry:epcData.entrySet()){  
				map.remove(entry.getKey());
			}
		}
		for(Map.Entry<String, Integer> entry:map.entrySet()){  
			Integer epcxx=Integer.parseInt(checkEpc(String.valueOf(map.get(entry.getKey()))),16);
			List<NameValuePair> nameValuePairs=new ArrayList<NameValuePair>(); 
			HttpUtil h=new HttpUtil();
    		h.setUrl(UfhData.getIP());
	        nameValuePairs.add(new BasicNameValuePair("operType", "3"));
	        nameValuePairs.add(new BasicNameValuePair("batchNo", String.valueOf(epcxx)));//库位信息
			r=checkStockJson(h.getHttpResult(nameValuePairs));
			stockList.add(epcxx.toString());
		} 
		return r;
	}
	private Map checkStockJson(String httpResult) {
		// TODO Auto-generated method stub
		Log.i("httpResult",httpResult);
		System.out.println(httpResult);
		return null;
	}
	/**
	 * @param val
	 * @return
	 */
	private String checkHex(String val){
		if(val.length()<20){
			String x="";
			for(int i=0;i<20-val.length();i++){
				x+="0";
			}
			val=x+val;
		}
		return val;
	}
	private String checkSerialNo(Map<String, String> m) {
		// TODO Auto-generated method stub
		String v="";
		for(Map.Entry<String, String> entry:m.entrySet()){  
			v+=String.valueOf(Integer.parseInt(entry.getKey().substring(4,entry.getKey().length()),16))+",";
		} 
		return v.substring(0,v.length()-1);
	}
}
