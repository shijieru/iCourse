package com.sjr.android.iCourse;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class Addteacher extends Activity
{
	//声明控件
    private EditText nameText;   //姓名
    private RadioGroup genderGroup;
    private RadioButton genderM,genderF;//性别
    private EditText titleText;  //职称
    private EditText collegeText;  //院系
    private EditText phoneText;   //手机号码
    private EditText officeText;  //办公室地址
    private EditText emailText;   //邮箱
    private EditText timeText; //空闲时间
    private EditText ftpText;   //交大ftp用户名和密码
    
    private Button okButton;
    private Button cancelButton;
    private DBAdapter db;
    
	public void onCreate(Bundle savedInstanceState) 
	{
        super.onCreate(savedInstanceState);
        //数据库辅助类实例
        db = new DBAdapter(this);
        db.open();
        
		setContentView(R.layout.teacheradd);   //布局文件
		
		/*Intent intentFromC=getIntent();
		String teachername=intentFromC.getStringExtra("CourseTeacher");*/
        
        //获得控件
		nameText = (EditText) findViewById(R.id.EditText01);
        genderGroup=(RadioGroup)findViewById(R.id.RadioGroup01);
        genderM=(RadioButton)findViewById(R.id.RadioButton1);
        genderF=(RadioButton)findViewById(R.id.RadioButton2);
        titleText = (EditText) findViewById(R.id.EditText03);
        collegeText = (EditText) findViewById(R.id.EditText04);
        phoneText = (EditText) findViewById(R.id.EditText05);
        officeText = (EditText) findViewById(R.id.EditText06);
        emailText = (EditText) findViewById(R.id.EditText07);
        timeText = (EditText) findViewById(R.id.EditText08);
        ftpText = (EditText) findViewById(R.id.EditText09);
   
        okButton = (Button)findViewById(R.id.Button01);
        cancelButton = (Button)findViewById(R.id.Button02);
        
        genderM.setChecked(true);
        
        /*if(teachername.length()!=0)
        {
           nameText.setText(teachername);
        }*/
        //ok按钮监听器
        okButton.setOnClickListener(new OnClickListener()
        {
			public void onClick(View v) 
			{
				String name=nameText.getText().toString();
				String gender;
				int temp=genderGroup.getCheckedRadioButtonId();
				if(temp==R.id.RadioButton1)
				{
					gender="男";
				}else
				{
					gender="女";
				}
				String title=titleText.getText().toString();
				String college=collegeText.getText().toString();
				String phone=phoneText.getText().toString();
				String email=emailText.getText().toString();
				String office=officeText.getText().toString();
				String ftp=ftpText.getText().toString();
				String officetime=timeText.getText().toString();
				if(name.length()==0)
				{
					//如果没有输入教师名字，则不添加记录
					Toast.makeText(getApplicationContext(),"请输入教师名字", Toast.LENGTH_SHORT).show();
				}
				else
				{
					db.insertTeacher(name, gender, title, college, phone, email, office, ftp, officetime);
					Intent intent=new Intent();
					intent.setClass(Addteacher.this, DisplaySAT.class);
					startActivity(intent);
					finish();
				}
			}
        	
        });
        //cancel按钮监听器
        cancelButton.setOnClickListener(new OnClickListener()
        {
			public void onClick(View v) 
			{
				Intent backToList=new Intent();
				backToList.setClass(getApplicationContext(), DisplaySAT.class);
				startActivity(backToList);
				finish();
			}
        });
        
        
	}
}
	
