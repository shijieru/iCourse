package com.sjr.android.iCourse;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class TeacherView extends Activity
{
	//声明控件
    private EditText nameText;   //姓名
    private RadioGroup genderGroup;
    private RadioButton genderM,genderF;
    private EditText titleText;  //职称
    private EditText collegeText;  //院系
    private EditText phoneText;   //手机号码
    private EditText officeText;  //办公室地址
    private EditText emailText;   //邮箱
    private EditText timeText; //空闲时间
    private EditText ftpText;   //交大ftp用户名和密码
    
    private Button updateButton;
    private Button deleteButton;
    private Button cancelButton;
    private DBAdapter db;
    
    
    public void onCreate(Bundle savedInstanceState) 
	{
        super.onCreate(savedInstanceState);
        //数据库辅助类实例
        db = new DBAdapter(this);
        db.open();
        
		setContentView(R.layout.teacherview);   //布局文件
        
        //获得控件
        nameText = (EditText) findViewById(R.id.nameView02);
        genderGroup=(RadioGroup)findViewById(R.id.genderGroup01);
        genderM=(RadioButton)findViewById(R.id.genderButton01);
        genderF=(RadioButton)findViewById(R.id.genderButton02);
        titleText = (EditText) findViewById(R.id.titleView02);
        collegeText = (EditText) findViewById(R.id.collegeView02);
        phoneText = (EditText) findViewById(R.id.phoneView02);
        officeText = (EditText) findViewById(R.id.officeView02);
        emailText = (EditText) findViewById(R.id.emailView02);
        timeText = (EditText) findViewById(R.id.timeView02);
        ftpText = (EditText) findViewById(R.id.ftpView02);
   
        updateButton = (Button)findViewById(R.id.EditButton);
        deleteButton=(Button)findViewById(R.id.DeleteButton);
        cancelButton = (Button)findViewById(R.id.cancelButton);
        
        Intent intent=getIntent();
        final String rowId=intent.getStringExtra("rowId");
        Cursor cs=db.getATeacher(rowId);
        nameText.setText(cs.getString(1));
        String gender=cs.getString(2);
        if(gender.equals("男"))
        {
        	genderM.setChecked(true);
        }else if(gender.equals("女"))
        {
        	genderF.setChecked(true);
        }
        titleText.setText(cs.getString(3));
        collegeText.setText(cs.getString(4));
        phoneText.setText(cs.getString(5));
        emailText.setText(cs.getString(6));
        officeText.setText(cs.getString(7));
        ftpText.setText(cs.getString(8));
        timeText.setText(cs.getString(9));
        
        
        //更新教师信息按钮设置监听器
        updateButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) 
			{
				String name=nameText.getText().toString();
				int temp=genderGroup.getCheckedRadioButtonId();
				String gender;
				if(temp==R.id.genderButton01)
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
				db.updateTeacher(rowId, name, gender, title, college, phone, email, office, ftp, officetime);
				Toast.makeText(TeacherView.this, "教师"+name+"信息已更新", Toast.LENGTH_LONG).show();
				finish();
			}
        	
        });
        
        //删除教师信息按钮设置监听器
        
        deleteButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) 
			{
				db.deleteATeacher(rowId);
				db.close();
				Intent intent=new Intent();
				intent.setClass(TeacherView.this,DisplaySAT.class);
				startActivity(intent);
				finish();
			}
        	
        });
        
        //返回
        cancelButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) 
			{
				Intent intent=new Intent();
				intent.setClass(TeacherView.this,DisplaySAT.class);
				startActivity(intent);
				finish();
			}
        	
        });
	}      
        
}