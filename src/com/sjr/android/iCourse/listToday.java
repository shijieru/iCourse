package com.sjr.android.iCourse;

import java.util.Calendar;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class listToday extends Activity{
	
	private DBAdapter db;
	
	LinearLayout	to_LinearLayout;
	ListView		to_Class_ListView;
	ListView		to_Assignment_ListView;
	
	
	
	private Calendar calToday = Calendar.getInstance();
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		
		to_LinearLayout = new LinearLayout(this);
		
		to_LinearLayout.setOrientation(LinearLayout.VERTICAL);
		to_LinearLayout.setBackgroundResource(R.drawable.background1);

		
		to_Class_ListView = new ListView(this);
		to_Assignment_ListView = new ListView(this);
		LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		
		TextView course_textView=new TextView(this);
		course_textView.setText("当日课程");
		
		
		TextView assignment_textView=new TextView(this);
		assignment_textView.setText("当日作业");
		
		
		to_LinearLayout.addView(course_textView);
		to_LinearLayout.addView(to_Class_ListView, param);
		to_LinearLayout.addView(assignment_textView);
		to_LinearLayout.addView(to_Assignment_ListView, param);

		
		setContentView(to_LinearLayout);
		
		DBAdapter db=new DBAdapter(this);
		db.open();
		
		Calendar calSelected =Calendar.getInstance();
		calToday.setTimeInMillis(System.currentTimeMillis());
		
		Cursor curClass = db.getClassesByDayofWeek(calSelected);//按星期几选择课程
		Cursor curAssignment = db.getAllUnfinishedAssignmentByDEADLINE(); //所有作业

		
		
		
		ListAdapter adapterofClass = new SimpleCursorAdapter(this,
			
			android.R.layout.simple_list_item_2,
			
			curClass,
			new String[] {"course","time"},
			new int[] { android.R.id.text1, android.R.id.text2 });
		
		ListAdapter adapterofAssignment = new SimpleCursorAdapter(this,
				android.R.layout.simple_expandable_list_item_2,
				curAssignment,
				new String[]{"name","_id"},
				new int[]{android.R.id.text1, android.R.id.text2});
		

		to_Class_ListView.setAdapter(adapterofClass);
		to_Assignment_ListView.setAdapter(adapterofAssignment);



		to_Class_ListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
			{
				Intent intent=new Intent();
				final TextView CourseName=(TextView)arg1.findViewById(android.R.id.text1);
				intent.putExtra("CourseName", CourseName.getText().toString());
				intent.setClass(listToday.this, showCourseInfo.class);
				startActivity(intent);
			}
		});
		to_Assignment_ListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				String SelectedId = ((TextView)arg1.findViewById(android.R.id.text2)).getText().toString();
				Toast.makeText(getApplicationContext(),"转入No."+SelectedId+"作业显示界面", 1000).show();
				Intent intent=new Intent();
				intent.putExtra("AssignmentId",SelectedId);
				intent.setClass(listToday.this, AssignmentView.class);
				startActivity(intent);
				
			}});

		
	}
	
	public boolean onCreateOptionsMenu(Menu menu)
	{  
		
		menu.add(0,0,0,"添加新课程");
		menu.add(0,1,1,"返回主菜单");
		return true;
	}
	
	public boolean onOptionsItemSelected(MenuItem item)
	{
		int item_id=item.getItemId();
		switch(item_id)
		{
			case 0:
				Intent course_intent=new Intent();
				course_intent.setClass(listToday.this, addCourseInfo.class);
				this.startActivity(course_intent);
				finish();
				break;
			
			case 1:
				finish();
		}
		return true;
	}
}
	


