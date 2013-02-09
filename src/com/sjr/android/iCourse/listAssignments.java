package com.sjr.android.iCourse;


import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.RadioGroup.OnCheckedChangeListener;


public class listAssignments extends Activity{
	
	private DBAdapter db;
	private Cursor cur;
	
	private ListView listAssigns;
	private Button btByDeadline;
	private Button btByPriority;
	private Button btByCourse;
	private RadioGroup rg;
	
	private String SelectedId;
	private RadioButton rb1;
	private RadioButton rb2;
	
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list_assignments);
		
		db = new DBAdapter(this);
        db.open();
        //db.insertAssignment("大作业1", "...", "大系软", "1990-8-9 12:00:00", "", false, 1);
        //db.insertAssignment("大作业2", "...", "大应软", "3.29", "", false, 2);
        //db.insertAssignment("大作业3", "...", "操作系统", "3.2", "", true, 3);
        
        listAssigns = (ListView)findViewById(R.id.listAssignments);
        //ListText();
        btByDeadline = (Button)findViewById(R.id.btByDeadline);
        btByPriority = (Button)findViewById(R.id.btByPriority);
        btByCourse = (Button)findViewById(R.id.btByCourses);
        rg = (RadioGroup)findViewById(R.id.radiogroup_ls);
        rb1 = (RadioButton)findViewById(R.id.radioUnfinished);
        rb2 = (RadioButton)findViewById(R.id.radioAllassign);
        btByDeadline.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ListByDeadline();
			}});
        
        btByPriority.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ListByPriority();
			}});
        
        btByCourse.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ListByCourse();
			}});
        
        ListByDeadline();
	}
	
	public void ListByDeadline(){

		if (rg.getCheckedRadioButtonId()==R.id.radioUnfinished)
			cur = db.getAllUnfinishedAssignmentByDEADLINE();
		else cur = db.getAllAssignmentByDEADLINE();
		
		listAssigns = (ListView)findViewById(R.id.listAssignments);
		ListAdapter adapter = new SimpleCursorAdapter(this,
				// 定义List中每一行的显示模板
				// 表示每一行包含两个数据项
				R.layout.assignment_listview,
				// 数据库的Cursor对象
				cur,
				new String[] {"deadline","priority","course","name","_id"},
				new int[] { R.id.al_textView1,R.id.priorityView, R.id.al_textView2, 
				R.id.al_textView3, R.id.courseNumView});
		listAssigns.setAdapter(adapter);
		addClick();
	}
	
	public void ListByPriority(){

		if (rg.getCheckedRadioButtonId()==R.id.radioUnfinished)
			cur = db.getAllUnfinishedAssignmentByPriority();
		else cur = db.getAllAssignmentByPriority();
		
		listAssigns = (ListView)findViewById(R.id.listAssignments);
		ListAdapter adapter = new SimpleCursorAdapter(this,
				// 定义List中每一行的显示模板
				// 表示每一行包含两个数据项
				R.layout.assignment_listview,
				// 数据库的Cursor对象
				cur,
				new String[] {"priority","course","deadline","name","_id"},
				new int[] { R.id.priorityView, R.id.al_textView1, R.id.al_textView2, 
				R.id.al_textView3 ,R.id.courseNumView});
		listAssigns.setAdapter(adapter);
		addClick();
	}
	
	public void ListByCourse(){

		if (rg.getCheckedRadioButtonId()==R.id.radioUnfinished)
			cur = db.getAllUnfinishedAssignmentByCourse();
		else cur = db.getAllAssignmentByCourse();
		
		listAssigns = (ListView)findViewById(R.id.listAssignments);
		ListAdapter adapter = new SimpleCursorAdapter(this,
				// 定义List中每一行的显示模板
				// 表示每一行包含两个数据项
				R.layout.assignment_listview,
				// 数据库的Cursor对象
				cur,
				new String[] {"course","priority","deadline","name","_id"},
				new int[] { R.id.al_textView1, R.id.priorityView, R.id.al_textView2, 
				R.id.al_textView3 ,R.id.courseNumView});
		listAssigns.setAdapter(adapter);
		addClick();
	}
	
	
	public void ListText(){
		Cursor cur =db.getAllCourses();
		listAssigns = (ListView)findViewById(R.id.listAssignments);
		ListAdapter adapter = new SimpleCursorAdapter(this,
				// 定义List中每一行的显示模板
				// 表示每一行包含两个数据项
				R.layout.assignment_listview,
				// 数据库的Cursor对象
				cur,
				new String[] {"name"},
				new int[] { R.id.al_textView1});
		listAssigns.setAdapter(adapter);
		addClick();
	}
	
	public void addClick(){
        listAssigns.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				SelectedId = ((TextView)arg1.findViewById(R.id.courseNumView)).getText().toString();
				Toast.makeText(getApplicationContext(),"转入No."+SelectedId+"作业显示界面", 1000).show();
				Intent intent=new Intent();
				intent.putExtra("AssignmentId",SelectedId);
				intent.setClass(listAssignments.this, AssignmentView.class);
				cur.close();
				startActivity(intent);
				
			}});
	}
	
	/*创建menu*/
	public boolean onCreateOptionsMenu(Menu menu)
	{  
		//为menu添加内容
		menu.add(0,0,0,"添加作业");
		menu.add(0,1,1,"清除作业数据库");
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
				Intent intent=new Intent();
				intent.setClass(listAssignments.this, AddAssignment.class);
				this.startActivity(intent);				
				break;
			case 1:
				db.DBHelper.onUpgradeA(db.DBHelper.getWritableDatabase(), 1, 2);
				Intent intentToDis=new Intent();
				intentToDis.setClass(listAssignments.this, DisplaySAT.class);
				this.startActivity(intentToDis);
				finish();
				break;
			case 2:
				Intent intentToStart=new Intent();
				intentToStart.setClass(listAssignments.this, start.class);
				this.startActivity(intentToStart);
				finish();
				break;
		}
		return true;
	}
}