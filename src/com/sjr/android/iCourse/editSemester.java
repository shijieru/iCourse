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
	//�����ؼ�
	private EditText tid;
	private EditText tbyear;//ѧ�ڿ�ʼ���ڣ��꣩
	private EditText tbmonth;
	private EditText tbday;
	private EditText tltf;//ѧ�ڳ���ʱ��
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
        
      //��ÿؼ�
        tid = (EditText) findViewById(R.id.termid);
        tbyear= (EditText) findViewById(R.id.termbeginningyear);
        tbmonth= (EditText) findViewById(R.id.termbeginningmonth);
        tbday= (EditText) findViewById(R.id.termbeginningday);
        tltf= (EditText) findViewById(R.id.termlasttf);
        
        okButton = (Button)findViewById(R.id.Button1);
        deleteButton = (Button)findViewById(R.id.Button2);
        cancelButton = (Button)findViewById(R.id.Button3);
        
       //����ѧ����Ϣ
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
        
        //ȷ����ť
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
					//���û�����뿪ʼʱ����߳���ʱ�䣬�򲻸��¼�¼
					Toast.makeText(getApplicationContext(),"������ѧ�ڿ�ʼ������ʱ��", Toast.LENGTH_SHORT).show();
					finish();
				}
				if(oldstarty==Integer.parseInt(startyear)&&((oldstartm<6)&&(Integer.parseInt(startmonth)>=6)||(oldstartm>=6)&&(Integer.parseInt(startmonth)<6)))
				{
					//ͬһ�꣬��ͬʱ��6��ǰ����6�º󣬽����ɲ�ͬѧ��ID���
						Toast.makeText(getApplicationContext(),"ѧ�ڿ�ʼ�������̫����ɾ����ѧ�����½�ѧ��", Toast.LENGTH_SHORT).show();
						finish();
				}
				else if((oldstarty==Integer.parseInt(startyear)+1)&&(oldstartm>=6||Integer.parseInt(startmonth)<6))
				{
					//��ѧ��year=��ѧ��year-1,�����ɲ�ͬѧ��ID�����
						Toast.makeText(getApplicationContext(),"ѧ�ڿ�ʼ�������̫����ɾ����ѧ�����½�ѧ��", Toast.LENGTH_SHORT).show();
						finish();
				}
				else if((oldstarty==Integer.parseInt(startyear)-1)&&(oldstartm<6||Integer.parseInt(startmonth)>=6))
				{
					//��ѧ��year=��ѧ��year+1,�����ɲ�ͬѧ��ID�����
						Toast.makeText(getApplicationContext(),"ѧ�ڿ�ʼ�������̫����ɾ����ѧ�����½�ѧ��", Toast.LENGTH_SHORT).show();
						finish();
				}
				else if(oldstarty>(Integer.parseInt(startyear)+1)||oldstarty<(Integer.parseInt(startyear)-1))
				{
					//��ѧ�����ѧ��������2�������ϣ������ɲ�ͬѧ��ID�����
					Toast.makeText(getApplicationContext(),"ѧ�ڿ�ʼ�������̫����ɾ����ѧ�����½�ѧ��", Toast.LENGTH_SHORT).show();
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
        
      //ɾ����ť
        deleteButton.setOnClickListener(new OnClickListener()
        {
			public void onClick(View v) 
			{
				Cursor cur=db.getCourseBySemester(semesterId);//��ø�ѧ�ڵ����пγ�
				Cursor temp=cur;//tempָ��ָ����ɾ���Ŀγ�
				int i=cur.getCount();//��ѧ�ڿγ����������ڼ���
				cur.moveToFirst();
				
				if(i>0)
				{
					Toast.makeText(editSemester.this, "����ɾ��ѧ����ȫ���γ�", Toast.LENGTH_LONG).show();
				}
				db.deleteSemester(semesterId);//ɾ��ѧ������
				while(i>0)
				{
					//����ѧ�ڿγ�����i>0ʱ��ɾ���γ���Ϣ��ָ����һ���γ�
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
        
        
      //���ذ�ť
        cancelButton.setOnClickListener(new OnClickListener()
        {
			public void onClick(View v) 
			{
				finish();
			}
        });
        
        
    }
}