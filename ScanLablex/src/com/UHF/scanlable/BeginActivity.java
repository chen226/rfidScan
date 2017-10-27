package com.UHF.scanlable;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.app.ActivityGroup;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.UHF.scanlable.ScanMode.MyAdapter;

public class BeginActivity extends Activity implements OnClickListener{

	Button outMButton;
	Button incomingMButton;
	public static final String EXTRA_MODE = "mode";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Log.i("zhouxin",">>>>>>>>>>>>>>>>>>>>> mode onCreate");
		setContentView(R.layout.begin);
		outMButton = (Button)findViewById(R.id.outMaterial);
		outMButton.setOnClickListener(this);
		incomingMButton = (Button)findViewById(R.id.incomingMaterial);
		incomingMButton.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v.getId()==incomingMButton.getId()){
			UfhData.Set_sound(false);
			Log.i("BeginActivity:incomingMButton","=====onItemClick=========");
			Intent intent = new Intent(this,ScanModeGroup.class);
			intent.putExtra(EXTRA_MODE, "6C");
//			intent.putExtra(MainActivity.EXTRA_EPC, myAdapter.mList.get(position));
			goActivty(intent);
		}
		if(v.getId()==outMButton.getId()){
			UfhData.Set_sound(false);
			Log.i("BeginActivity:outMButton","=====onItemClick=========");
			Intent intent = new Intent(this,ReadWriteActivity.class);
//			intent.putExtra(MainActivity.EXTRA_EPC, myAdapter.mList.get(position));
			goActivty(intent);
		}
	}
	private void goActivty(Intent intent){
		Log.i("goActivty","------------------go");
        Window w = ((ActivityGroup)getParent()).getLocalActivityManager()  
                .startActivity("SecondActivity",intent);  
        View view = w.getDecorView();  
        ((ActivityGroup)getParent()).setContentView(view);
        Log.i("goActivty", "------------------oo");
	}

}
