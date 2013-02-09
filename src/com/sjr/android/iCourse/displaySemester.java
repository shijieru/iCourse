package com.sjr.android.iCourse;

import java.util.*;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;





public class displaySemester extends Activity {

	//声明控件
	private EditText tid;//学期ID
	private EditText tbtime;
	private EditText tetime;
	private ListView list;//学期持续时间
	private Button Button00;
	private Button Button01;
	private Button Button02;
	
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
  
        setContentView(R.layout.semesterdisplay);
        
        
        //获得控件
        tid= (EditText) findViewById(R.id.termid);
        tbtime= (EditText) findViewById(R.id.termbeginningtime);
        tetime= (EditText) findViewById(R.id.termendtime);
        list= (ListView) findViewById(R.id.list);
        
        Button00 = (Button)findViewById(R.id.Button1);
        Button01 = (Button)findViewById(R.id.Button2);
        Button02 = (Button)findViewById(R.id.Button3);
        
        Intent intent=getIntent();
        final String semesterId=intent.getStringExtra("semesterId");
        final DBAdapter db=new DBAdapter(this);
        db.open();
        
        Cursor cur =db.getCourseBySemester(semesterId);
        Cursor secur = db.getASemester(semesterId);
        
        //填充显示信息
        tid.setText(semesterId);
        tbtime.setText(secur.getString(secur.getColumnIndex("start")));
        tetime.setText(secur.getString(secur.getColumnIndex("end")));
      
        //列出课程
        ListAdapter adapter = new SimpleCursorAdapter(this,android.R.layout.simple_list_item_1,
        cur, new String[] {"name"}, new int[] {android.R.id.text1});
        
        list.setAdapter(adapter);
        
		list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
			{
				//对选中的项进行处理，点击课程进入课程详细信息
				Intent intent=new Intent();
				final TextView CourseName=(TextView)arg1.findViewById(android.R.id.text1);
				intent.putExtra("CourseName", CourseName.getText().toString());
				intent.setClass(displaySemester.this, showCourseInfo.class);
				startActivity(intent);
				finish();
			}
 
		});
       
		//添加课程按钮，调用addCourseInfo
        Button00.setOnClickListener(new OnClickListener()
        {
			public void onClick(View v) 
			{
				Intent toaddCourse = new Intent();
				toaddCourse.setClass(displaySemester.this, addCourseInfo.class);
				startActivity(toaddCourse);
				finish();
			}
        });
        
        //编辑按钮，调用editSemester
        Button01.setOnClickListener(new OnClickListener()
        {
			public void onClick(View v) 
			{
				Intent toedit=new Intent();
        		toedit.setClass(displaySemester.this, editSemester.class);
        		toedit.putExtra("semesterId", semesterId);
        		startActivity(toedit);
        		finish();
			}
        });
        
        //返回按钮
        Button02.setOnClickListener(new OnClickListener()
        {
			public void onClick(View v) 
			{
				finish();
			}
        });
        
        
	}
	
	public int WeekInASemester(Calendar date){
		return WeekInASemester(date, this);
	}

	public int WeekInASemester(Calendar date, Context ctx)
	{	
		DBAdapter db = new DBAdapter(ctx) ;
        db.open();
        int weeknum;

        Cursor cs= db.getAllSemesters();//得到所有学期的开始、结束时间信息，cs指向表头
        Calendar startdate=Calendar.getInstance();
        Calendar enddate=Calendar.getInstance();
        
        if(cs != null){
        	
        	int i=cs.getCount();//学期数，用以循环计数
        	if(i!=0)
        	{
        		cs.moveToFirst();
            
        		//开始日期拆分为年月日的int模式，便于设置Calendar变量
        		int startyear=Integer.parseInt(cs.getString(cs.getColumnIndex("start")).substring(0, 4));
        		int startmonth=Integer.parseInt(cs.getString(cs.getColumnIndex("start")).substring(4, 6));
        		int startday=Integer.parseInt(cs.getString(cs.getColumnIndex("start")).substring(6, 8));
            
        		//结束日期拆分为年月日的int模式，便于设置Calendar变量
        		int endyear=Integer.parseInt(cs.getString(cs.getColumnIndex("end")).substring(0,4));
        		int endmonth=Integer.parseInt(cs.getString(cs.getColumnIndex("end")).substring(4, 6));
        		int endday=Integer.parseInt(cs.getString(cs.getColumnIndex("end")).substring(6, 8));
           
          
        		//设置学期开始、结束日期的Calendar变量
        		startdate.set(startyear, startmonth-1, startday);
        		enddate.set(endyear, endmonth-1, endday);
     
        		while(date.before(startdate)||date.after(enddate))//所需判断日期不在该学期范围内
        		{
        			i--;//剩余学期数减1
        			if(i==0)
        			{
        				//所有学期判断完，均不包含指定日期
        				cs.moveToFirst();
        				return -1;
        			}
        			else{
        				//指针下移一位，重新获取学期开始日期与结束日期信息
        				cs.moveToNext();
        				startyear=Integer.parseInt(cs.getString(cs.getColumnIndex("start")).substring(0, 4));
        				startmonth=Integer.parseInt(cs.getString(cs.getColumnIndex("start")).substring(4, 6));
        				startday=Integer.parseInt(cs.getString(cs.getColumnIndex("start")).substring(6, 8));
        				endyear=Integer.parseInt(cs.getString(cs.getColumnIndex("end")).substring(0,4));
        				endmonth=Integer.parseInt(cs.getString(cs.getColumnIndex("end")).substring(4, 6));
        				endday=Integer.parseInt(cs.getString(cs.getColumnIndex("end")).substring(6, 8));
        				startdate.set(startyear, startmonth, startday);
        				enddate.set(endyear, endmonth, endday);
        			}
        		}
        	
        		//待判断日期在某一学期时间范围内
        		long start=startdate.getTimeInMillis();//开始日期转化为ms
        		long thedate=date.getTimeInMillis();//待判断日期转化为ms
        		long days=(thedate-start)/(24*60*60*1000);//相差天数
        		weeknum=(int)(days/7+1);//待判断日期在所属学期的周数
        		return weeknum;
        	}else
        	{
        		return -1;
        	}
        }
        
        else{
        	return -2;//空指针则返回-2
        }
	}

}
