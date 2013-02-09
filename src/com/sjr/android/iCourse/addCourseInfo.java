package com.sjr.android.iCourse;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class addCourseInfo extends Activity{
	
	private EditText editCourseName;
	private EditText editCourseTeacher;
	private Spinner selectCourseSemester;
	private EditText editCourseInfo;
	
	private Button OkToAdd;
	private Button CancelToAdd;
	
	private DBAdapter db;
	private SimpleCursorAdapter mAdapter;
	
	private String CourseName;
	private String CourseTeacher;
	private String CourseInfo;
	private String CourseSemester="";
		
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_course_info);
		
        db = new DBAdapter(this);
        db.open();
        
		editCourseName = (EditText) findViewById(R.id.editCourseName);
		editCourseTeacher = (EditText) findViewById(R.id.editCourseTeacher);
		selectCourseSemester = (Spinner) findViewById (R.id.selectCourseSemester);
		editCourseInfo = (EditText) findViewById (R.id.editCourseInfo);
		
		Cursor cur = db.getAllSemesters();
		mAdapter = new SimpleCursorAdapter(this, android.R.layout.simple_spinner_item, cur, 
				new String[]{"_id"}, new int[]{android.R.id.text1});
		mAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		selectCourseSemester.setAdapter(mAdapter);
		selectCourseSemester.setOnItemSelectedListener(new OnItemSelectedListener(){

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				CourseSemester = ((TextView)arg1).getText().toString();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}});
		OkToAdd = (Button) findViewById(R.id.OkToAddCourse);
		CancelToAdd = (Button) findViewById(R.id.CancelToAddCourse);
		OkToAdd.setOnClickListener(new OnClickListener(){

			

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				CourseName = editCourseName.getText().toString();
				CourseTeacher = editCourseTeacher.getText().toString();
				CourseInfo = editCourseInfo.getText().toString();
				
				if(CourseName.length()==0)
				{
					Toast.makeText(getApplicationContext(),"请输入课程名称", 1000).show();
				}
				else if(CourseSemester.length()==0)
				{
					Dialog dialog=new AlertDialog.Builder(addCourseInfo.this)
					.setTitle("提示").setMessage("您还没有添加学期，转入添加学期页面").setPositiveButton("确定", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(
								DialogInterface dialog,
								int which) {
							Intent intent=new Intent();
							intent.setClass(addCourseInfo.this, addSemester.class);
							startActivity(intent);
							finish();
							
						}
					}).create();
					dialog.show();
				}
				else if (CourseTeacher.length()==0)
				{
					Toast.makeText(getApplicationContext(),"请输入授课教师", 1000).show();
				}
				else 
				{
					boolean CourseExist = false;
					Cursor cur = db.getAllCourses();
					for(cur.moveToFirst();!cur.isAfterLast();cur.moveToNext())
					{
						int nameColumn = cur.getColumnIndex("name");
						String name = cur.getString(nameColumn);
						if (CourseName.equals(name))
						{CourseExist = true;}
					}
					cur.close();
					if(CourseExist)
					{
						Toast.makeText(getApplicationContext(),"课程已存在，请重新输入课程名称", 1000).show();

					}else
					{
						boolean TeacherExist = false;
						Cursor cur2 = db.getAllTeachers();
						for(cur2.moveToFirst();!cur2.isAfterLast();cur2.moveToNext())
						{
							int teacherColumn = cur2.getColumnIndex("name");
							String teacher = cur2.getString(teacherColumn);
							if (CourseTeacher.equals(teacher))
							{TeacherExist = true;}
					    }
						cur2.close();
						if(!TeacherExist)
						{
							showDialog(addCourseInfo.this,CourseTeacher);
						}
						else
						{
							GotoCourseInfo();
						}

					}
				}
			}
			
		});
		
		CancelToAdd.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
			
		});
	}
	
    private void showDialog(Context context,final String CourseTeacher) {  
        AlertDialog.Builder builder = new AlertDialog.Builder(context);  
        builder.setTitle("新教师");  
        builder.setMessage("是否现在补充教师信息");  
        builder.setPositiveButton("确定",  
                new DialogInterface.OnClickListener() {  
                    public void onClick(DialogInterface dialog, int whichButton) {   
                    	Toast.makeText(getApplicationContext(),"转入教师添加界面", 1000).show();
                    	Intent intent=new Intent();
						intent.putExtra("CourseTeacher",CourseTeacher);
						intent.setClass(addCourseInfo.this, Addteacher.class);
						startActivity(intent);
                    }  
                });  
        builder.setNegativeButton("以后再写",  
                new DialogInterface.OnClickListener() {  
                    public void onClick(DialogInterface dialog, int whichButton) {
                    	db.insertTeacher(CourseTeacher, "男", null, null, null, null, null, null, null);
                    	GotoCourseInfo();
                    }  
                });  
        builder.show();  
    }  
    
    public void GotoCourseInfo(){
		//int Semester = Integer.parseInt(CourseSemester);
		db.insertCourse(CourseName, CourseInfo, CourseTeacher, CourseSemester);
		Toast.makeText(getApplicationContext(), "添加课程成功，转入添加上课时间页面", 1000).show();
		Intent intent=new Intent();
		intent.putExtra("CourseName",CourseName);
		intent.setClass(addCourseInfo.this, showCourseInfo.class);
		startActivity(intent);
		finish();
    }
}