package com.sjr.android.iCourse;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class showCourseInfo extends Activity{
	
	private DBAdapter db;
	
	private TextView showCourseName; //课程名称
	private TextView showCourseTeacher; //授课教师
	private TextView showCourseSemester; //上课学期
	private TextView showCourseInfo;  //课程信息
	private Button btAddCourseTime;  //添加上课时间的按钮
	private Button btAddCourseAssignment;  //添加课程作业的按钮
	private Button btReturn;  //返回按钮
	private ListView ListCourseTime;  //显示上课时间的列表
	private ListView ListCourseAssignment;  //显示课程作业的列表
	
	private View viewAdd;  //添加上课时间的界面
	private String CourseName;  //课程名称

	private Dialog dialogAdd;  //添加上课时间的对话框
	
	/** 添加上课时间对话框的变量 **/
	private EditText editClassroom; //上课地点
	private Spinner spinClassday;  //上课日期-星期几
	private Spinner spinClassweek; //单双周
	private EditText editTimestart;  //第几节课开始
	private EditText editTimeend;  //第几节课结束
	private EditText editWeekstart;  //第几周开始
	private EditText editWeekend;  //第几周结束
	private Button btnOK;  //确定添加
	private Button btnCancel;  //取消
	
	private String Classday;  //星期几上课
	private String Classweek;  //单双周

	private SimpleCursorAdapter mAdapter;  //上课时间列表的Adapter
	private SimpleCursorAdapter nAdapter;  //课程作业列表的Adapter
	
	private String deleteId;  //待删除的上课时间ID

	
	
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.show_course_info);
		
		db = new DBAdapter(this);
        db.open();
        
        //从上一个界面获取此界面所显示的课程名称
        Intent intent=getIntent();
        CourseName=intent.getStringExtra("CourseName");
        
        //将使用的控件与xml文件中的绑定
        showCourseName = (TextView)findViewById(R.id.showCourseName);
        showCourseTeacher = (TextView)findViewById(R.id.showCourseTeacher);
        showCourseSemester = (TextView)findViewById(R.id.showCourseSemester);
        showCourseInfo = (TextView)findViewById(R.id.showCourseInfo);
        btAddCourseTime = (Button)findViewById(R.id.btAddCourseTime);
        btAddCourseAssignment = (Button)findViewById(R.id.btAddCourseAssignment);
        btReturn = (Button) findViewById(R.id.ReturnFromCourseInfo);
        
        //从数据库中选出对应CourseName的课程信息
        Cursor cursor1 = db.getCourseByName(CourseName);
        showCourseName.setText("课程名称："+cursor1.getString(1));
        showCourseTeacher.setText("授课教师："+cursor1.getString(3));
        showCourseSemester.setText("上课学期："+cursor1.getString(4));
        showCourseInfo.setText("课程信息："+cursor1.getString(2));
        cursor1.close();

        showCourseTimeList();  //显示上课时间列表
        showCourseAssignmentList();  //显示课程作业列表
        

        /** 为上课时间列表ListCourseTime添加单击事件 
         *  单击后删除该上课时间 **/
        ListCourseTime.setOnItemClickListener(new OnItemClickListener() {  
  
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,  
                    long arg3) {  
            	deleteId = ((TextView)arg1.findViewById(R.id.deletedTime)).getText().toString();
                showDialog(showCourseInfo.this, deleteId);
            }  
            private void showDialog(Context context, String id) {  
                AlertDialog.Builder builder = new AlertDialog.Builder(context);  
                builder.setTitle("删除记录");  
                builder.setMessage("是否决定删除这条上课时间");  
                builder.setPositiveButton("确定",  
                        new DialogInterface.OnClickListener() {  
                            public void onClick(DialogInterface dialog, int whichButton) {   
                                db.deleteClassById(Integer.parseInt(deleteId));
                            	Toast.makeText(getApplicationContext(),"删除成功", 1000).show();
                            	showCourseTimeList();
                            }  
                        });  
                builder.setNegativeButton("取消",  
                        new DialogInterface.OnClickListener() {  
                            public void onClick(DialogInterface dialog, int whichButton) {  
                            }  
                        });  
                builder.show();  
            }  
        });  
        
        /**为添加上课时间按钮btAddCourseTime添加单击事件**/
        btAddCourseTime.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				createDialogAdd();
			}
        	
        });
        
        /**为添加课程作业按钮btAddCourseAssignment添加单击事件**/
        btAddCourseAssignment.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent=new Intent();
				intent.setClass(showCourseInfo.this, AddAssignment.class);
				startActivity(intent);
				finish();
			}
        	
        });
        
        /**为返回按钮btReturn添加单击事件**/
        btReturn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(
					View v) {
				// TODO Auto-generated method stub
				finish();
			}
        	
        });
        
	}
	
	/**显示上课时间列表所调用的函数**/
	public void showCourseTimeList(){
        ListCourseTime = (ListView)findViewById(R.id.ListCourseTime);
        Cursor cursor2 = db.getClassesByCourse(CourseName);
        mAdapter = new SimpleCursorAdapter(this,
    			// 定义List中每一行的显示模板
    			// 表示每一行包含两个数据项
    			R.layout.coursetime_listview,
    			// 数据库的Cursor对象
    			cursor2,
    			new String[] {"_id","day","classroom","time","end","start","week"},
    			new int[] { R.id.deletedTime,R.id.lClassDay, R.id.lClassRoom, R.id.lClassTime,
        		R.id.lweek_end, R.id.lweek_start, R.id.lClassWeek});
        
        ListCourseTime.setAdapter(mAdapter);
    }
	
	/**显示课程作业列表所调用的函数**/
	public void showCourseAssignmentList(){
		ListCourseAssignment = (ListView)findViewById(R.id.ListCourseAssignment);
		Cursor cursor3 = db.getAssignmentsByCourse(CourseName);
		nAdapter = new SimpleCursorAdapter(this,
				R.layout.assignment_listview,
				// 数据库的Cursor对象
				cursor3,
				new String[] {"deadline","priority","course","name","_id"},
				new int[] { R.id.al_textView1,R.id.priorityView, R.id.al_textView2, 
				R.id.al_textView3, R.id.courseNumView});
		ListCourseAssignment.setAdapter(nAdapter);
		//为列表添加单击事件，单击后转入对应作业显示界面
		ListCourseAssignment.setOnItemClickListener(new OnItemClickListener(){

			private String SelectedId;

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				SelectedId = ((TextView)arg1.findViewById(R.id.courseNumView)).getText().toString();
				Toast.makeText(getApplicationContext(),"转入No."+SelectedId+"作业显示界面", 1000).show();
				Intent intent=new Intent();
				intent.putExtra("AssignmentId",SelectedId);
				intent.setClass(showCourseInfo.this, AssignmentView.class);
				startActivity(intent);
				
			}});
	}
	
	/** 创建添加上课信息的对话框  **/
	public void createDialogAdd(){
		viewAdd = this.getLayoutInflater().inflate(R.layout.dialog_addcourse, null);
		dialogAdd = new Dialog(this);
		dialogAdd.show();
		dialogAdd.setContentView(viewAdd);
		dialogAdd.setTitle("添加新的上课信息");
		
		editClassroom = (EditText)viewAdd.findViewById(R.id.dl_classroom_edit);
		spinClassday = (Spinner)viewAdd.findViewById(R.id.dl_ClassDaySpinner);
		spinClassweek = (Spinner)viewAdd.findViewById(R.id.dl_ClassWeekSpinner);
		editTimestart = (EditText)viewAdd.findViewById(R.id.dl_time_start);
		editTimeend = (EditText)viewAdd.findViewById(R.id.dl_time_end);
		editWeekstart = (EditText)viewAdd.findViewById(R.id.dl_week_start);
		editWeekend = (EditText)viewAdd.findViewById(R.id.dl_week_end);
		
		btnOK = (Button)viewAdd.findViewById(R.id.dl_BtnOK);
		btnCancel = (Button)viewAdd.findViewById(R.id.dl_BtnCancel);
		
		ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(  
                this, R.array.weekdays, android.R.layout.simple_spinner_item);  
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);  
        spinClassday.setAdapter(adapter1);  
		spinClassday.setOnItemSelectedListener(new OnItemSelectedListener(){

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				Classday = ((TextView)arg1).getText().toString();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}			
		});
		
		ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(  
                this, R.array.classweeks, android.R.layout.simple_spinner_item);  
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);  
        spinClassweek.setAdapter(adapter2);  
		spinClassweek.setOnItemSelectedListener(new OnItemSelectedListener(){

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				Classweek = ((TextView)arg1).getText().toString();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}			
		});
		
		/** 确认添加的按钮事件 **/
		btnOK.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String time = editTimestart.getText().toString() + '-' + editTimeend.getText().toString();
				String start = editWeekstart.getText().toString();
				String end = editWeekend.getText().toString();
				String classroom = editClassroom.getText().toString();
				db.insertClass(CourseName, Classday, time, Classweek, Integer.parseInt(start), Integer.parseInt(end), classroom);
				showCourseTimeList();
				dialogAdd.dismiss();
				
			}
			
		});
		/** 取消添加的按钮事件 **/
		btnCancel.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialogAdd.dismiss();
			}
			
		});
	}
	
	/*创建menu*/
	public boolean onCreateOptionsMenu(Menu menu)
	{  
		//为menu添加内容
		menu.add(0,0,0,"删除此课程");
		menu.add(0,1,1,"清除所有作业");
		menu.add(0,2,2,"清除所有上课时间");
		menu.add(0,2,2,"返回主菜单");
		return true;
	}
	/*处理menu事件 */
	public boolean onOptionsItemSelected(MenuItem item)
	{
		int item_id=item.getItemId();
		switch(item_id)
		{
			case 0:
				/*db.deleteCourseByName(CourseName);
				db.deleteAssignmentsByCourse(CourseName);
				db.deleteClassesByCourse(CourseName);
				Intent intent=new Intent();
				intent.setClass(showCourseInfo.this, DisplaySAT.class);
				this.startActivity(intent);
				finish();*/
				showDialog1(showCourseInfo.this);
				break;
			case 1:
				db.deleteAssignmentsByCourse(CourseName);
				showCourseAssignmentList();
				break;
			case 2:
				db.deleteClassesByCourse(CourseName);
				showCourseTimeList();
				break;
			case 3:
				Intent intentToStart=new Intent();
				intentToStart.setClass(showCourseInfo.this, start.class);
				this.startActivity(intentToStart);
				finish();
				break;
		}
		return true;
	}
	
	private void showDialog1(Context context) {  
        AlertDialog.Builder builder = new AlertDialog.Builder(context);  
        builder.setMessage("删除课程同时会删除所有上课时间和课程作业，是否确定？");  
        builder.setPositiveButton("确定",  
                new DialogInterface.OnClickListener() {  
                    public void onClick(DialogInterface dialog, int whichButton) {   
                    	db.deleteCourseByName(CourseName);
        				db.deleteAssignmentsByCourse(CourseName);
        				db.deleteClassesByCourse(CourseName);
        				Intent intent=new Intent();
        				intent.setClass(showCourseInfo.this, DisplaySAT.class);
        				startActivity(intent);
        				finish();
                    }  
                });  
        builder.setNegativeButton("取消",  
                new DialogInterface.OnClickListener() {  
                    public void onClick(DialogInterface dialog, int whichButton) {
                    	//
                    }  
                });  
        builder.show();  
    }  

}

