package com.sjr.android.iCourse;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class addSemester extends Activity {
	
	//声明控件
	private EditText tbyear;//学期开始日期（年）
	private EditText tbmonth;
	private EditText tbday;
	private EditText tltf;//学期持续时间
	private Button okButton;
	private Button cancelButton;
	
	private DBAdapter db;
	
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.semesteradd);
        
        db = new DBAdapter(this);
        db.open();
        
        //获得控件
        tbyear= (EditText) findViewById(R.id.termbeginningyear);
        tbmonth= (EditText) findViewById(R.id.termbeginningmonth);
        tbday= (EditText) findViewById(R.id.termbeginningday);
        tltf= (EditText) findViewById(R.id.termlasttf);
        
        okButton = (Button)findViewById(R.id.Button1);
        cancelButton = (Button)findViewById(R.id.Button2);
        
        //确定按钮
        okButton.setOnClickListener(new OnClickListener()
        {
			public void onClick(View v) 
			{
				String startyear=tbyear.getText().toString();
				String startmonth=tbmonth.getText().toString();
				String startday=tbday.getText().toString();
				String week=tltf.getText().toString();
				
				
		
				if(startyear.length()==0||startmonth.length()==0||startday.length()==0||week.length()==0)
				{
					//如果没有输入开始时间或者持续时间，则不添加记录
					Toast.makeText(getApplicationContext(),"请输入学期开始及持续时间", Toast.LENGTH_SHORT).show();
				}
				else
				{
					db.insertSemester(startyear,startmonth,startday,Integer.parseInt(week));
					Intent intent=new Intent();
					intent.setClass(addSemester.this, DisplaySAT.class);
					startActivity(intent);
					finish();
				}
			}
        	
        });
        
        
        //返回按钮
        cancelButton.setOnClickListener(new OnClickListener()
        {
			public void onClick(View v) 
			{
				Intent backToList=new Intent();
				backToList.setClass(getApplicationContext(), DisplaySAT.class);
				startActivity(backToList);
			}
        });
        
        
    }
}