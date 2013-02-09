package com.sjr.android.iCourse;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class start extends Activity
{
	private ImageButton imageButton01;
	private ImageButton imageButton02;
	private ImageButton imageButton03;
	private ImageButton imageButton04;
	private String tempString="本课程表软件由上海交通大学信息安全学院学生开发，旨在" +
			"为在校学生提供操作简单，大方美观的随身课程表。如对本软件有任何建议，请联系：cronyshi@yahoo.com.cn";
	
	 public void onCreate(Bundle savedInstanceState) 
		{
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.start);   //布局文件
			
	        imageButton01=(ImageButton)findViewById(R.id.imageButton01);
	        imageButton02=(ImageButton)findViewById(R.id.imageButton02);
	        imageButton03=(ImageButton)findViewById(R.id.imageButton03);
	        imageButton04=(ImageButton)findViewById(R.id.imageButton04);
	        
	        //监听器
	        imageButton01.setOnClickListener(new Button.OnClickListener()
	        {

				@Override
				public void onClick(
						View v) {
					Intent intent=new Intent();
					intent.setClass(start.this, listCalendar.class);
					startActivity(intent);
					
				}
	        	
	        });
	        
	        imageButton02.setOnClickListener(new Button.OnClickListener()
	        {

				@Override
				public void onClick(
						View v) {
					Intent intent=new Intent();
					intent.setClass(start.this,listToday.class);
					startActivity(intent);
					
				}
	        	
	        });
	        
	        imageButton03.setOnClickListener(new Button.OnClickListener()
	        {

				@Override
				public void onClick(
						View v) {
					Intent intent=new Intent();
					intent.setClass(start.this, DisplaySAT.class);
					startActivity(intent);
					
				}
	        	
	        });
	        
	        imageButton04.setOnClickListener(new Button.OnClickListener()
	        {

				@Override
				public void onClick(
						View v) {
					//对话框
					Dialog dialog=new AlertDialog.Builder(start.this)
					.setTitle("关于本软件").setMessage(tempString).setPositiveButton("确定", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(
								DialogInterface dialog,
								int which) {
							
							
						}
					}).create();
					dialog.show();
				}
	        	
	        });
		}
}