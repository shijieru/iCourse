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

	//�����ؼ�
	private EditText tid;//ѧ��ID
	private EditText tbtime;
	private EditText tetime;
	private ListView list;//ѧ�ڳ���ʱ��
	private Button Button00;
	private Button Button01;
	private Button Button02;
	
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
  
        setContentView(R.layout.semesterdisplay);
        
        
        //��ÿؼ�
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
        
        //�����ʾ��Ϣ
        tid.setText(semesterId);
        tbtime.setText(secur.getString(secur.getColumnIndex("start")));
        tetime.setText(secur.getString(secur.getColumnIndex("end")));
      
        //�г��γ�
        ListAdapter adapter = new SimpleCursorAdapter(this,android.R.layout.simple_list_item_1,
        cur, new String[] {"name"}, new int[] {android.R.id.text1});
        
        list.setAdapter(adapter);
        
		list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
			{
				//��ѡ�е�����д�������γ̽���γ���ϸ��Ϣ
				Intent intent=new Intent();
				final TextView CourseName=(TextView)arg1.findViewById(android.R.id.text1);
				intent.putExtra("CourseName", CourseName.getText().toString());
				intent.setClass(displaySemester.this, showCourseInfo.class);
				startActivity(intent);
				finish();
			}
 
		});
       
		//��ӿγ̰�ť������addCourseInfo
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
        
        //�༭��ť������editSemester
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
        
        //���ذ�ť
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

        Cursor cs= db.getAllSemesters();//�õ�����ѧ�ڵĿ�ʼ������ʱ����Ϣ��csָ���ͷ
        Calendar startdate=Calendar.getInstance();
        Calendar enddate=Calendar.getInstance();
        
        if(cs != null){
        	
        	int i=cs.getCount();//ѧ����������ѭ������
        	if(i!=0)
        	{
        		cs.moveToFirst();
            
        		//��ʼ���ڲ��Ϊ�����յ�intģʽ����������Calendar����
        		int startyear=Integer.parseInt(cs.getString(cs.getColumnIndex("start")).substring(0, 4));
        		int startmonth=Integer.parseInt(cs.getString(cs.getColumnIndex("start")).substring(4, 6));
        		int startday=Integer.parseInt(cs.getString(cs.getColumnIndex("start")).substring(6, 8));
            
        		//�������ڲ��Ϊ�����յ�intģʽ����������Calendar����
        		int endyear=Integer.parseInt(cs.getString(cs.getColumnIndex("end")).substring(0,4));
        		int endmonth=Integer.parseInt(cs.getString(cs.getColumnIndex("end")).substring(4, 6));
        		int endday=Integer.parseInt(cs.getString(cs.getColumnIndex("end")).substring(6, 8));
           
          
        		//����ѧ�ڿ�ʼ���������ڵ�Calendar����
        		startdate.set(startyear, startmonth-1, startday);
        		enddate.set(endyear, endmonth-1, endday);
     
        		while(date.before(startdate)||date.after(enddate))//�����ж����ڲ��ڸ�ѧ�ڷ�Χ��
        		{
        			i--;//ʣ��ѧ������1
        			if(i==0)
        			{
        				//����ѧ���ж��꣬��������ָ������
        				cs.moveToFirst();
        				return -1;
        			}
        			else{
        				//ָ������һλ�����»�ȡѧ�ڿ�ʼ���������������Ϣ
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
        	
        		//���ж�������ĳһѧ��ʱ�䷶Χ��
        		long start=startdate.getTimeInMillis();//��ʼ����ת��Ϊms
        		long thedate=date.getTimeInMillis();//���ж�����ת��Ϊms
        		long days=(thedate-start)/(24*60*60*1000);//�������
        		weeknum=(int)(days/7+1);//���ж�����������ѧ�ڵ�����
        		return weeknum;
        	}else
        	{
        		return -1;
        	}
        }
        
        else{
        	return -2;//��ָ���򷵻�-2
        }
	}

}
