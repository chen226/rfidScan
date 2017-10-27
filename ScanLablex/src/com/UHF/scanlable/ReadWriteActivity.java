package com.UHF.scanlable;

import com.UHF.scanlable.ScanMode.MyAdapter;
import com.UHF.scanlable.UfhData.UhfGetData;

import android.R.integer;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityGroup;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SimpleCursorAdapter.ViewBinder;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

public class ReadWriteActivity extends Activity implements OnClickListener, OnItemSelectedListener{
	
	private int mode;
	private static final int MODE_6B = 0;
	private static final int MODE_6C = 1;
	static SoundPool soundpool = new SoundPool(1, AudioManager.STREAM_NOTIFICATION, 100);;
	static int soundid = soundpool.load("/etc/Scan_new.ogg", 1);
	EditText edENum0;
//	EditText edENum1;
//	EditText edENum2;
//	EditText edENum3;
//	EditText[] edENums;
	int selectedEd = 3;
	int selectedWhenPause = 0;
	
	EditText et_name;
	EditText materialType;
	EditText et_materialCode;
	EditText et_batchNum;
	EditText c_ptr;

	TextView et_name0;
	TextView et_materialCode0;
	TextView et_batchNum0;
	TextView et_SerialNumber0;
	EditText b_id;
	EditText b_addr;
	EditText b_num;
	
	EditText et_SerialNumber;
	Button dButton;
	Button wButton;
	String epcValue;
	String sacnType;
	private ProgressDialog dialog = null;
	Context context = getBaseContext();
	private static final int CHECK_W_6B = 0;
	private static final int CHECK_R_6B = 1;
	private static final int CHECK_W_6C = 2;
	private static final int CHECK_R_6C = 3;
	private boolean isShow=false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}
	
	private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
			JSONObject json =(JSONObject) msg.obj;
            switch (msg.what) {
			case 1:
				try {
					et_name0.setText("托盘名称");
					et_name.setText(json.getString("name"));
					if(et_name.getVisibility()!=View.VISIBLE){
						et_name.setVisibility(View.VISIBLE); 
					}
					if(et_name0.getVisibility()!=View.VISIBLE){
						et_name0.setVisibility(View.VISIBLE); 
					}
//					materialType = (EditText)findViewById(R.id.et_materialType);
//					materialType.setText(json.getString("materialType"));
					et_materialCode0.setText("托盘编号");
					et_materialCode.setText(json.getString("code"));
					et_batchNum0.setText("状态");
					String state=json.getString("state");
					if("0".equals(state)){
						et_batchNum.setText("可用");
					}else if("1".equals(state)){
						et_batchNum.setText("正在使用");
					}else if("-1".equals(state)){
						et_batchNum.setText("停用");
					}
					if(et_SerialNumber0.getVisibility()!=View.GONE){
						et_SerialNumber0.setVisibility(View.GONE); 
					}

					if(et_SerialNumber.getVisibility()!=View.GONE){
						et_SerialNumber.setVisibility(View.GONE); 
					}
				} catch (JSONException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				break;
			case 2:
				try {
					if(et_name0.getVisibility()!=View.GONE){
						et_name0.setVisibility(View.GONE); 
					}
					if(et_name.getVisibility()!=View.GONE){
						et_name.setVisibility(View.GONE); 
					}
					et_materialCode0.setText("库位编号");
					et_materialCode.setText(json.getString("positionCode"));
					et_batchNum0.setText("状态");
					String positionState=json.getString("positionState");
					if("0".equals(positionState)){
						et_batchNum.setText("空");
					}else if("1".equals(positionState)){
						et_batchNum.setText("非空");
					}else if("2".equals(positionState)){
						et_batchNum.setText("冻结");
					}
					if(et_SerialNumber0.getVisibility()!=View.GONE){
						et_SerialNumber0.setVisibility(View.GONE); 
					}
					if(et_SerialNumber.getVisibility()!=View.GONE){
						et_SerialNumber.setVisibility(View.GONE); 
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			case 3:
				try {
					if(et_name.getVisibility()!=View.VISIBLE){
						et_name.setVisibility(View.VISIBLE); 
					}
					if(et_name0.getVisibility()!=View.VISIBLE){
						et_name0.setVisibility(View.VISIBLE); 
					}
					et_name0.setText(R.string.materialName);
					et_materialCode0.setText(R.string.materialCode);
					et_batchNum0.setText(R.string.batchNum);
					et_name.setText(json.getString("materialName"));
					et_materialCode.setText(json.getString("materialNum"));
					et_batchNum.setText(json.getString("inventoryNumber"));
					et_SerialNumber.setText(json.getString("packageInventoryNum"));
					if(et_SerialNumber0.getVisibility()!=View.VISIBLE){
						et_SerialNumber0.setVisibility(View.VISIBLE); 
						et_SerialNumber0.setText(R.string.SerialNumber); 
					}
					if(et_SerialNumber.getVisibility()!=View.VISIBLE){
						et_SerialNumber.setVisibility(View.VISIBLE);
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				break;
			case 8:
		        if("0".equals(String.valueOf(msg.arg1))){
		        	edENum0.setText("物料入托成功");
				}else{
		        	edENum0.setText("物料入托失败");
				}
				break;
			case 9:
		        if("0".equals(String.valueOf(msg.arg1))){
		        	edENum0.setText( "托盘入库成功");
				}else{
		        	edENum0.setText("托盘入库失败");
				}
				break;
			default:
				break;
			}
            
        }

    };
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		Log.i("zhouxin",">>>>>>>>>>>>>>>>>>>>>>rw onResume");
		Log.i("zhouxin", ">>>>>>>>>>>>>>>>>>>>>rw oncreate" + this);
		if(getIntent().getStringExtra(MainActivity.EXTRA_MODE).equals(MainActivity.TABLE_6B)){
			setContentView(R.layout.rw_6b);
			mode = MODE_6B;
		}else{
			setContentView(R.layout.rw_6c);
			mode = MODE_6C;
		}
		edENum0 = (EditText)findViewById(R.id.epc0);
		switch (mode) {
		case MODE_6B:
			if (!UfhData.getUfh_id().equals(b_id.getText().toString())) {
				b_id.setText(UfhData.getUfh_id());
			}
//			et_SerialNumber.setText("");
			break;
		case MODE_6C:
			/*if(!UfhData.getUfh_id().equals(edENum0.getText().toString())){
				edENum0.setText(UfhData.getUfh_id());
				epcValue=UfhData.getEpc_id();
			}*/
			edENum0.setText(UfhData.getUfh_id());
			epcValue=UfhData.getEpc_id();
			sacnType=UfhData.getSacnType();
			initView();
//			et_SerialNumber.setText("");
			break;
		default:
			break;
		}
		super.onResume();
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		selectedWhenPause = selectedEd;
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		Log.i("zhouxin", ">>>>>>>>>>>>>>>>>>>>>rw onDestroy");
		super.onDestroy();
	}
	
	private void initView(){
		switch (mode) {
		case MODE_6B:
			b_id = (EditText)findViewById(R.id.et_id);
//			b_id.setText(getIntent().getStringExtra(MainActivity.EXTRA_EPC));
//			b_id.setText(UfhData.getUfh_id());
			b_addr = (EditText)findViewById(R.id.et_addr);
			b_addr.setText("8");
			b_num = (EditText)findViewById(R.id.et_num);
			b_num.setText("8");
			et_SerialNumber = (EditText)findViewById(R.id.et_SerialNumber);
			dButton = (Button)findViewById(R.id.button_read_6b);
			wButton = (Button)findViewById(R.id.button_write_6b);
			
			dButton.setOnClickListener(this);
			wButton.setOnClickListener(this);
			
			break;
		
		case MODE_6C:
			edENum0 = (EditText)findViewById(R.id.epc0);
			et_name0 = (TextView)findViewById(R.id.et_name0);
			et_name = (EditText)findViewById(R.id.et_name);
			et_materialCode0 = (TextView)findViewById(R.id.et_materialCode0);
			et_materialCode = (EditText)findViewById(R.id.et_materialCode);
			et_batchNum0 = (TextView)findViewById(R.id.et_batchNum0);
			et_batchNum = (EditText)findViewById(R.id.et_batchNum);
			et_SerialNumber = (EditText)findViewById(R.id.et_SerialNumber);
			et_SerialNumber0 = (TextView)findViewById(R.id.et_SerialNumber0);
//			edENum1 = (EditText)findViewById(R.id.epc1);
//			edENum2 = (EditText)findViewById(R.id.epc2);
//			edENum3 = (EditText)findViewById(R.id.epc3);
//			edENum0.setText(getIntent().getStringExtra(MainActivity.EXTRA_EPC));
//			edENum0.setText(UfhData.getUfh_id());
			ArrayAdapter<CharSequence> adapter =  ArrayAdapter.createFromResource(this, R.array.men_select, android.R.layout.simple_spinner_item);
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

			if("0".equals(sacnType)){
				
			}else if("1".equals(sacnType)){//托盘信息
				new Thread(new Runnable(){
					public void run(){
						List<NameValuePair> nameValuePairs=new ArrayList<NameValuePair>(); 
						HttpUtil h=new HttpUtil();
			    		h.setUrl(UfhData.getIP());
						nameValuePairs.add(new BasicNameValuePair("operType", "2"));//物料详情
				        nameValuePairs.add(new BasicNameValuePair("epcId", String.valueOf(Integer.parseInt(epcValue.substring(4,epcValue.length()),16))));//物料详情
				        JSONObject json;
				        try {
							json = new JSONObject(h.getHttpResult(nameValuePairs));
							Message m=new Message();
							m.obj=json;
							m.what=1;
							handler.sendMessage(m);
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						}
					}).start();
			}else if("2".equals(sacnType)){//库位信息
				new Thread(new Runnable(){
					public void run(){
							List<NameValuePair> nameValuePairs=new ArrayList<NameValuePair>(); 
							HttpUtil h=new HttpUtil();
				    		h.setUrl(UfhData.getIP());
							nameValuePairs.add(new BasicNameValuePair("operType", "3"));//物料详情
					        nameValuePairs.add(new BasicNameValuePair("epcId", String.valueOf(Integer.parseInt(epcValue.substring(4,epcValue.length()),16))));//物料详情
					        JSONObject json;
							try {
								json = new JSONObject(h.getHttpResult(nameValuePairs));
								Message m=new Message();
								m.obj=json;
								m.what=2;
								handler.sendMessage(m);
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}).start();
			}else if("3".equals(sacnType)){//包装详情
				new Thread(new Runnable(){
					public void run(){
						List<NameValuePair> nameValuePairs=new ArrayList<NameValuePair>(); 
						HttpUtil h=new HttpUtil();
			    		h.setUrl(UfhData.getIP());
						nameValuePairs.add(new BasicNameValuePair("operType", "1"));//物料详情
				        nameValuePairs.add(new BasicNameValuePair("epcId", String.valueOf(Integer.parseInt(epcValue.substring(4,epcValue.length()),16))));//物料详情
				        JSONObject json;
				        try {
							json = new JSONObject(h.getHttpResult(nameValuePairs));
							Message m=new Message();
							m.obj=json;
							m.what=3;
							handler.sendMessage(m);
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						}
					}).start();
			}
			
			
			dButton = (Button)findViewById(R.id.button_delete);
			wButton = (Button)findViewById(R.id.button_write_6c);
//			edENums = new EditText[]{edENum0, edENum1, edENum2, edENum3};
			
	/*		edENum0.setOnClickListener(this);
			edENum1.setOnClickListener(this);
			edENum2.setOnClickListener(this);
			edENum3.setOnClickListener(this);*/
			dButton.setOnClickListener(this);
			wButton.setOnClickListener(this);
			break;

		default:
			break;
		}
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		Intent intent = new Intent(this, ScanMode.class);  
        intent.putExtra(MainActivity.EXTRA_MODE, getIntent().getStringExtra(MainActivity.EXTRA_MODE));
        Window w = ((ActivityGroup)getParent()).getLocalActivityManager().startActivity(  
                "FirstActivity", intent);  
        View view = w.getDecorView();  
        ((ActivityGroup)getParent()).setContentView(view); 
        //((ActivityGroup)getParent()).getLocalActivityManager().destroyActivity("SecondActivity", false);
	}
	
	@SuppressLint("ResourceAsColor")
	@Override
	public void onClick(View view) {
		Log.i("chj", "----onclick----");
		System.out.println(1);
		switch (mode) {
		case MODE_6B:
			if(view == wButton){
//				if(!checkContent(CHECK_W_6B))return;
				int result=UhfGetData.Write6B(
						UhfGetData.hexStringToBytes(b_id.getText().toString()), 
//						UhfGetData.hexStringToBytes(b_addr.getText().toString())[0], 
						(byte)(int)Integer.valueOf(b_addr.getText().toString()),
						UhfGetData.hexStringToBytes(et_SerialNumber.getText().toString()), 
						(byte)(int)Integer.valueOf(b_num.getText().toString()));
				/*if(result==0){
					ExecutorService thread = Executors.newSingleThreadExecutor();
					thread.execute(new Runnable(){
						public void run(){
							soundpool.play(soundid, 1, 1, 0, 0, 1);
						}
					});
				}*/
			}else if(view == dButton){
//				if(!checkContent(CHECK_R_6B))return;
				int result=UhfGetData.Read6B(UhfGetData.hexStringToBytes(b_id.getText().toString()),
//						UhfGetData.hexStringToBytes(b_addr.getText().toString())[0], 
						(byte)(int)Integer.valueOf(b_addr.getText().toString()),
//						UhfGetData.hexStringToBytes(b_num.getText().toString())[0]
						(byte)(int)Integer.valueOf(b_num.getText().toString())
						);
				if(result==0){
					String temp=UhfGetData.bytesToHexString(UhfGetData.getRead6Bdata(),0,Integer.valueOf(b_num.getText().toString())).toUpperCase();
					if(Integer.valueOf(b_num.getText().toString())==0)
						temp="";
					et_SerialNumber.setText(temp.toUpperCase());
					/*ExecutorService thread = Executors.newSingleThreadExecutor();
					thread.execute(new Runnable(){
						public void run(){
							soundpool.play(soundid, 1, 1, 0, 0, 1);
						}
					});*/
				}
				else{
					et_SerialNumber.setText("");
				}
			}
			break;
		case MODE_6C:
			if(view == wButton){
//				if(!checkContent(CHECK_W_6C))return;
				/*if()

				Log.i("zhouxin",">>>"+(byte)(content.getText().length()/2)+"======="+UhfGetData.hexStringToBytes(c_pwd.getText().toString()).length);
				*/
				if("0".equals(sacnType)&&!epcValue.equals(edENum0.getText().toString())){
					String v=checkHex(Long.toHexString(Long.valueOf(edENum0.getText().toString())));
					int result=UhfGetData.Write6c(
//							UhfGetData.hexStringToBytes(c_len.getText().toString())[0], 
							(byte)(int)Integer.valueOf("6"),
							(byte)(6), 
							UfhData.epcBytes.get(epcValue), 
							(byte)1, 
//							UhfGetData.hexStringToBytes(c_wordPtr.getText().toString())[0],
							(byte)(int)Integer.valueOf("2"),
							UhfGetData.hexStringToBytes(v), 
							UhfGetData.hexStringToBytes("00000000"));
					onBackPressed();
				}else if("1".equals(sacnType)){//托盘信息
					new Thread(new Runnable(){
						public void run(){
							List<NameValuePair> nameValuePairs=new ArrayList<NameValuePair>(); 
							HttpUtil h=new HttpUtil();
				    		h.setUrl(UfhData.getIP());
							nameValuePairs.add(new BasicNameValuePair("operType", "8"));//物料入托
							Map serialNo=UfhData.getSerialNo();
					        nameValuePairs.add(new BasicNameValuePair("materialEpcId", checkSerialNo(serialNo)));//物料详情
					        nameValuePairs.add(new BasicNameValuePair("trayEpcId", String.valueOf(Integer.parseInt(epcValue.substring(4,epcValue.length()),16))));//物料详情
					        String result=h.getHttpResult(nameValuePairs);
							Message m=new Message();
							m.what=8;
							m.arg1=Integer.valueOf(result.replaceAll("\"", ""));
	                        handler.sendMessage(m);
							}

						}).start();
				}else if("2".equals(sacnType)){//库位
					new Thread(new Runnable(){
						public void run(){
							List<NameValuePair> nameValuePairs=new ArrayList<NameValuePair>(); 
							HttpUtil h=new HttpUtil();
				    		h.setUrl(UfhData.getIP());
//							Set trayList=UfhData.getTrayList();
							Map trayNo=UfhData.getTrayNo();
							nameValuePairs.add(new BasicNameValuePair("operType", "9"));//托盘入库
					        nameValuePairs.add(new BasicNameValuePair("trayEpcId", checkSerialNo(trayNo)));//物料详情
					        nameValuePairs.add(new BasicNameValuePair("stockEpcId", String.valueOf(Integer.parseInt(epcValue.substring(4,epcValue.length()),16))));//物料详情
							String result=h.getHttpResult(nameValuePairs);
							Message m=new Message();
							m.what=9;
							m.arg1=Integer.valueOf(result.replaceAll("\"", ""));
	                        handler.sendMessage(m);
							}
						}).start();
				}
				/*if(result==0){
					ExecutorService thread = Executors.newSingleThreadExecutor();
					thread.execute(new Runnable(){
						public void run(){
							soundpool.play(soundid, 1, 1, 0, 0, 1);
						}
					});
				}*/
				
			}else if(view == dButton){
//				if(!checkContent(CHECK_R_6C))return;
//				Log.i("zhouxin",">>>"+UhfGetData.hexStringToBytes(et_batchNum.getText().toString()).length);
////				String pwd = c_pwd.getText().toString();
////				byte[] bytePwd = new byte[pwd.length()];
////				for(int i = 0; i < pwd.length(); i++){
////					bytePwd[i] = Byte.valueOf(pwd.substring(i, i+1));
////				}
//				UhfGetData.Read6C((byte)(UfhData.epcBytes.get(edENum0.getText().toString()).length/2),
//						UfhData.epcBytes.get(edENum0.getText().toString()),
//						(byte)selectedEd,
//						Byte.valueOf(materialType.getText().toString()),
//						Byte.valueOf(et_materialCode.getText().toString()),
//						UhfGetData.hexStringToBytes(et_batchNum.getText().toString()));
//				String temp=UhfGetData.bytesToHexString(UhfGetData.getRead6Cdata(),0,UhfGetData.getRead6CLen()).toUpperCase();
//				int m= UhfGetData.getRead6CLen();
//				if(m==0){
//					et_SerialNumber.setText("");
//				}
//				else{
//					et_SerialNumber.setText(temp.toUpperCase());
//					/*ExecutorService thread = Executors.newSingleThreadExecutor();
//					thread.execute(new Runnable(){
//						public void run(){
//							soundpool.play(soundid, 1, 1, 0, 0, 1);
//						}
//					});*/
//				}
				onBackPressed();
			}
			
			break;

		default:
			break;
		}
	}
	private String checkSerialNo(Map<String, String> m) {
		// TODO Auto-generated method stub
		String v="";
		for(Map.Entry<String, String> entry:m.entrySet()){  
			v+=String.valueOf(Integer.parseInt(entry.getKey().substring(4,entry.getKey().length()),16))+",";
		} 
		return v.substring(0,v.length()-1);
	}
	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int position,
			long arg3) {
		Log.i("zhouxin",">>>>>>>>>position>>>>>>"+position);
		selectedEd = position;
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}

	private boolean checkContent(int check){
		switch (check) {
		case CHECK_W_6C:
			if(Util.isEtEmpty(et_SerialNumber)) return Util.showWarning(this,R.string.content_empty_warning);
			if(Integer.valueOf(et_materialCode.getText().toString()) != et_SerialNumber.getText().toString().length()/4) 
				return Util.showWarning(this,R.string.length_content_warning);
			if(!(Util.isLenLegal(et_SerialNumber)))
				return Util.showWarning(this,R.string.str_lenght_odd_warning);
		case CHECK_R_6C:
			if(Util.isEtEmpty(materialType)) return Util.showWarning(this,R.string.wordptr_empty_warning);
			if(Util.isEtEmpty(et_materialCode)) return Util.showWarning(this,R.string.length_empty_warning);
			if(Util.isEtEmpty(et_batchNum)) return Util.showWarning(this,R.string.pwd_empty_warning);
			
			if(!(Util.isLenLegal(et_batchNum)))
				return Util.showWarning(this,R.string.str_lenght_odd_warning);
			
			break;

		case CHECK_R_6B:
//			if(!Util.isEtsLegal(new EditText[]{b_addr,b_num})){
//				return Util.showWarning(this, R.string.data_ilegal);
//			}
			
			if(Util.isEtEmpty(b_addr) || Util.isEtEmpty(b_num)){
				return Util.showWarning(this, R.string.data_ilegal);
			}
			
			break;
		case CHECK_W_6B:
			if(Util.isEtEmpty(et_SerialNumber) || !Util.isLenLegal(et_SerialNumber) || Util.isEtEmpty(b_addr)){
				return Util.showWarning(this, R.string.data_ilegal);
			}
			
			break;
		default:
			break;
		}
		return true;
	}
	private String checkHex(String val){
		et_name = (EditText)findViewById(R.id.et_name);
		if(val.length()<20){
			String x="";
			for(int i=0;i<20-val.length();i++){
				x+="0";
			}
			val=et_name.getText().toString()+x+val;
		}
		return val;
	}
}
