package com.sjr.android.iCourse;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.view.View;
import android.view.View.OnClickListener;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class editSemester extends Activity {
	//声明控件
	private EditText tid;
	private EditText tbyear;//学期开始日期（年）
	private EditText tbmonth;
	private EditText tbday;
	private EditText tltf;//学期持续时间
	private Button okButton;
	private Button cancelButton;
	private Button deleteButton;
	
	
	private DBAdapter db;
	
	 /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.semesteredit);
        
        db = new DBAdapter(this);
        db.open();
        
      //获得控件
        tid = (EditText) findViewById(R.id.termid);
        tbyear= (EditText) findViewById(R.id.termbeginningyear);
        tbmonth= (EditText) findViewById(R.id.termbeginningmonth);
        tbday= (EditText) findViewById(R.id.termbeginningday);
        tltf= (EditText) findViewById(R.id.termlasttf);
        
        okButton = (Button)findViewById(R.id.Button1);
        deleteButton = (Button)findViewById(R.id.Button2);
        cancelButton = (Button)findViewById(R.id.Button3);
        
       //填充该学期信息
        Intent intent=getIntent();
        final String semesterId=intent.getStringExtra("semesterId");
        final DBAdapter db=new DBAdapter(this);
        db.open();
        final Cursor cs=db.getASemester(semesterId);
        
        tid.setText(cs.getString(cs.getColumnIndex("_id")));
        String startdate=cs.getString(cs.getColumnIndex("start"));
        tbyear.setText(startdate.substring(0, 4));
        tbmonth.setText(startdate.substring(4, 6));
        tbday.setText(startdate.substring(6, 8));
        tltf.setText(cs.getString(cs.getColumnIndex("duration")));
        
        //确定按钮
        okButton.setOnClickListener(new OnClickListener()
        {
			public void onClick(View v) 
			{
				String startyear=tbyear.getText().toString();
				String startmonth=tbmonth.getText().toString();
				String startday=tbday.getText().toString();
				String week=tltf.getText().toString();
				int oldstarty=Integer.parseInt(cs.getString(cs.getColumnIndex("start")).substring(0, 4));
				int oldstartm=Integer.parseInt(cs.getString(cs.getColumnIndex("start")).substring(4, 6));
				
		
				if(startyear.length()==0||startmonth.length()==0||startday.length()==0||week.length()==0)
				{
					//如果没有输入开始时间或者持续时间，则不更新记录
					Toast.makeText(getApplicationContext(),"请输入学期开始及持续时间", Toast.LENGTH_SHORT).show();
					finish();
				}
				if(oldstarty==Integer.parseInt(startyear)&&((oldstartm<6)&&(Integer.parseInt(startmonth)>=6)||(oldstartm>=6)&&(Integer.parseInt(startmonth)<6)))
				{
					//同一年，不同时在6月前或者6月后，将生成不同学期ID情况
						Toast.makeText(getApplicationContext(),"学期开始日期相差太大，请删除本学期再新建学期", Toast.LENGTH_SHORT).show();
						finish();
				}
				else if((oldstarty==Integer.parseInt(startyear)+1)&&(oldstartm>=6||Integer.parseInt(startmonth)<6))
				{
					//新学期year=旧学期year-1,将生成不同学期ID的情况
						Toast.makeText(getApplicationContext(),"学期开始日期相差太大，请删除本学期再新建学期", Toast.LENGTH_SHORT).show();
						finish();
				}
				else if((oldstarty==Integer.parseInt(startyear)-1)&&(oldstartm<6||Integer.parseInt(startmonth)>=6))
				{
					//新学期year=旧学期year+1,将生成不同学期ID的情况
						Toast.makeText(getApplicationContext(),"学期开始日期相差太大，请删除本学期再新建学期", Toast.LENGTH_SHORT).show();
						finish();
				}
				else if(oldstarty>(Integer.parseInt(startyear)+1)||oldstarty<(Integer.parseInt(startyear)-1))
				{
					//新学期与旧学期年份相差2或者以上，将生成不同学期ID的情况
					Toast.makeText(getApplicationContext(),"学期开始日期相差太大，请删除本学期再新建学期", Toast.LENGTH_SHORT).show();
					finish();
				}
				else
				{
					db.deleteSemester(semesterId);
					db.insertSemester(startyear,startmonth,startday,Integer.parseInt(week));
					Intent intent=new Intent();
					intent.setClass(editSemester.this, DisplaySAT.class);
					intent.putExtra("semesterId", semesterId);
					startActivity(intent);
					finish();
				}
			}
        	
        });
        
      //删除按钮
        deleteButton.setOnClickListener(new OnClickListener()
        {
			public void onClick(View v) 
			{
				Cursor cur=db.getCourseBySemester(semesterId);//获得该学期的所有课程
				Cursor temp=cur;//temp指针指向需删除的课程
				int i=cur.getCount();//该学期课程数量，用于计数
				cur.moveToFirst();
				
				if(i>0)
				{
					Toast.makeText(editSemester.this, "即将删除学期内全部课程", Toast.LENGTH_LONG).show();
				}
				db.deleteSemester(semesterId);//删除学期数据
				while(i>0)
				{
					//当该学期课程数量i>0时，删除课程信息并指向下一个课程
					if(i!=1)
					{
						cur.moveToNext();
					}
					db.deleteAssignmentsByCourse(temp.getString(1));
					db.deleteClassesByCourse(temp.getString(1));
					db.deleteCourseByName(temp.getString(1));
					i--;
					temp=cur;
				}
				Intent intent=new Intent();
				intent.setClass(editSemester.this, DisplaySAT.class);
				intent.putExtra("semesterId", semesterId);
				startActivity(intent);
				finish();
			}
        });
        
        
      //返回按钮
        cancelButton.setOnClickListener(new OnClickListener()
        {
			public void onClick(View v) 
			{
				finish();
			}
        });
        
        
    }
}