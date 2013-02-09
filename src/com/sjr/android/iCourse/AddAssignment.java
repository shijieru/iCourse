package com.sjr.android.iCourse;

import java.util.Calendar;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.RatingBar.OnRatingBarChangeListener;

public class AddAssignment extends Activity
{
	//声明控件
    private EditText nameText;   //标题
    private EditText infoText;  //作业具体内容
    private Spinner  courseText;  //所属课程  
    private DatePicker deadlineDate; //截止日期
    private TimePicker deadlineTime; //截止时间
    private Button setDate;
    private Button setTime;
    private EditText submitText;   //提交方式
    private RadioGroup submit;
    private RadioButton submitY,submitN;
    private EditText submitornot; //是否提交  groupradio
    private RatingBar priority;   //优先级 spiner
    private String course_name="";    //所属课程
    private double priority_num;   //优先级标识
    private String deadDate="";
    private TableRow tableRow;
    private Cursor cs;
    private boolean submitTemp;
    
    private Button addButton;
    private Button cancelButton;
    private DBAdapter db;
    
    private ArrayAdapter<String> adapter;
    //Java中的Calendar类
    Calendar c;
    Calendar calendar;
    
	public void onCreate(Bundle savedInstanceState) 
	{
        super.onCreate(savedInstanceState);
        setContentView(R.layout.assignmentadd);   //布局文件
        
        //数据库辅助类实例
        db = new DBAdapter(this);
        db.open();
        
        cs=db.getAllCoursesName();
        //简单游标匹配器
        SimpleCursorAdapter adapter=new SimpleCursorAdapter(this,android.R.layout.simple_spinner_item,cs, new String[]{"name"},
        new int[]{android.R.id.text1});
        
        c=Calendar.getInstance();
        calendar=Calendar.getInstance();
        //获得控件
        nameText=(EditText)findViewById(R.id.a_nameView01);
        infoText=(EditText)findViewById(R.id.a_infoView01);
        tableRow=(TableRow)findViewById(R.id.TableRow01);
        courseText=(Spinner)findViewById(R.id.Spinner1);
        setDate=(Button)findViewById(R.id.setDate01);
        setTime=(Button)findViewById(R.id.setTime01);
        submitText=(EditText)findViewById(R.id.a_submit01);
        submit=(RadioGroup)findViewById(R.id.RadioGroup02);
        submitY=(RadioButton)findViewById(R.id.submitY);
        submitN=(RadioButton)findViewById(R.id.submitN);
        priority=(RatingBar)findViewById(R.id.rb1);
        addButton=(Button)findViewById(R.id.addButton01);
        cancelButton=(Button)findViewById(R.id.cancelButton01);
        deadlineDate=(DatePicker)findViewById(R.id.DatePicker01);
        //获取TimePicker对象
        deadlineTime=(TimePicker)findViewById(R.id.TimePicker01);
        deadlineTime.setIs24HourView(true);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //将adapter添加到spinner中
        courseText.setAdapter(adapter);
        submitN.setChecked(true);
        tableRow.setVisibility(View.GONE);
        priority.setOnRatingBarChangeListener(new OnRatingBarChangeListener(){

			@Override
			public void onRatingChanged(
					RatingBar ratingBar,
					float rating,
					boolean fromUser) {
				priority_num=priority.getRating();
			}
        	
        });
        
        
        //将日历初始化为当前系统时间，并设置其时间监听
        deadlineDate.init(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH), new DatePicker.OnDateChangedListener() {
			
			@Override
			public void onDateChanged(
					DatePicker view,
					int year,
					int monthOfYear,
					int dayOfMonth) {
				
			}
		});
        deadlineDate.setVisibility(View.GONE);
        
        //监听时间改变
        deadlineTime.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
			
			@Override
			public void onTimeChanged(
					TimePicker view,
					int hourOfDay,
					int minute) {
				
			}
		});
        deadlineTime.setVisibility(View.GONE);
        
        //添加课程下拉菜单
        courseText.setOnItemSelectedListener(new Spinner.OnItemSelectedListener()
        {

			@Override
			public void onItemSelected(
					AdapterView<?> arg0,
					View view,
					int position,
					long id) {
				cs.moveToPosition(position);
				course_name=cs.getString(1);
				
			}

			@Override
			public void onNothingSelected(
					AdapterView<?> arg0) {
				
			}
        	
        });
      
        //为设置日期设置监听按钮+闹铃
        setDate.setOnClickListener(new Button.OnClickListener()
        {

			@Override
			public void onClick(
					View v) {
				new DatePickerDialog(AddAssignment.this,
						new DatePickerDialog.OnDateSetListener() {
							
							@Override
							public void onDateSet(
									DatePicker view,
									int year,
									int monthOfYear,
									int dayOfMonth) {
								  calendar.setTimeInMillis(System.currentTimeMillis());
					              calendar.set(Calendar.YEAR,year);
					              calendar.set(Calendar.MONTH,monthOfYear);
					              calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
					              calendar.set(Calendar.HOUR_OF_DAY,8);
					              calendar.set(Calendar.MINUTE,0);
					              calendar.set(Calendar.SECOND,0);
					              calendar.set(Calendar.MILLISECOND,0);
					              calendar.set(Calendar.SECOND,0);
					              calendar.set(Calendar.MILLISECOND,0);
					              /* 建立Intent和PendingIntent，来调用目标组件 */
					              Intent intent = new Intent(AddAssignment.this, AlarmReceiver.class);
					              PendingIntent pendingIntent=PendingIntent.getBroadcast(AddAssignment.this,0, intent, 0);
					              AlarmManager am;
					              /* 获取闹钟管理的实例 */
					              am = (AlarmManager)getSystemService(ALARM_SERVICE);
					              /* 设置闹钟 */
					              am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
					              deadDate=year+"-"+(++monthOfYear)+"-"+dayOfMonth;
					              Toast.makeText(getApplicationContext(),deadDate, Toast.LENGTH_LONG).show();
							}
						},c.get(Calendar.YEAR),c.get(Calendar.MONTH),c.get(Calendar.DAY_OF_MONTH)).show();
				
			}
        	
        });
        //为设置时间设置监听按钮
        setTime.setOnClickListener(new Button.OnClickListener()
        {

			@Override
			public void onClick(
					View v) {
		        new TimePickerDialog(AddAssignment.this,
		          new TimePickerDialog.OnTimeSetListener()
		          {                
		            public void onTimeSet(TimePicker view,int hourOfDay,int minute)
		            {
		            	deadDate+=" "+hourOfDay+":"+minute+":"+"00";
		            	Toast.makeText(getApplicationContext(),deadDate, Toast.LENGTH_LONG).show();
		            }          
		          },c.get(Calendar.HOUR_OF_DAY),c.get(Calendar.MINUTE),true).show();

			}
        	
        });
        //添加信息按钮设置监听器
        addButton.setOnClickListener(new Button.OnClickListener()
        {

			@Override
			public void onClick(
					View v) {
				String name=nameText.getText().toString();
				String info=infoText.getText().toString();
				String course=course_name;
				String deadline=deadDate;
				String submitway=submitText.getText().toString();
				int temp=submit.getCheckedRadioButtonId();
				if(temp==R.id.submitY)
				{
					submitTemp=true;
				}else
				{
					submitTemp=false;
				}
				double priority=priority_num;
				
				if(name.equals(""))
				{
					Toast.makeText(getApplicationContext(),"请输入作业名称", Toast.LENGTH_LONG).show();
				}
				else if(course.equals(""))
				{
					Dialog dialog=new AlertDialog.Builder(AddAssignment.this)
					.setTitle("提示").setMessage("您还没有添加课程，转入添加课程页面").setPositiveButton("确定", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(
								DialogInterface dialog,
								int which) {
							Intent intent=new Intent();
							intent.setClass(AddAssignment.this, addCourseInfo.class);
							startActivity(intent);
							finish();
							
						}
					}).create();
					dialog.show();
				}else
				{
					if(deadDate.equals(""))
					{
						Toast.makeText(AddAssignment.this, "请设置截止日期", Toast.LENGTH_SHORT).show();
					}else
					{
						db.insertAssignment(name, info, course, deadline, submitway, submitTemp, priority);
						Intent intent=new Intent();
						intent.putExtra("CourseName",course);
						intent.setClass(AddAssignment.this, showCourseInfo.class);
						startActivity(intent);
						finish();
					}
				}
			}  	
        });
    	//返回按钮设置监听器
		cancelButton.setOnClickListener(new Button.OnClickListener()
		{

			@Override
			public void onClick(
					View v) {
				Intent backToList=new Intent();
				backToList.putExtra("CourseName",course_name);
				backToList.setClass(getApplicationContext(), showCourseInfo.class);
				startActivity(backToList);
				finish();
				
			}
			
		});
	}
}
	
