package com.sjr.android.iCourse;

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


public class listSemester extends Activity{
	LinearLayout	se_LinearLayout;
	ListView		se_ListView;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		/* 创建LinearLayout布局对象 */
		se_LinearLayout = new LinearLayout(this);
		/* 设置布局LinearLayout的属性 */
		se_LinearLayout.setOrientation(LinearLayout.VERTICAL);
		se_LinearLayout.setBackgroundResource(R.drawable.background1);

		/* 创建ListView对象 */
		se_ListView = new ListView(this);
		LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

		/* 添加se_ListView到se_LinearLayout布局 */
		se_LinearLayout.addView(se_ListView, param);

		/* 设置显示se_LinearLayout布局 */
		setContentView(se_LinearLayout);
		
		DBAdapter db=new DBAdapter(this);
		db.open();
		
		// 获取数据库Semester的Cursor
		Cursor cur=db.getAllSemesters(); 

		// ListAdapter是ListView和后台数据的桥梁
		ListAdapter adapter = new SimpleCursorAdapter(this,android.R.layout.simple_list_item_1,
				cur,new String[] {"_id"},new int[] { android.R.id.text1 });
		
		/* 将adapter添加到se_ListView中 */
		se_ListView.setAdapter(adapter);


		/* 为se_ListView视图添加setOnItemClickListener监听 */
		se_ListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
			{
				//于对选中的项进行处理
				Intent intent=new Intent();
				final TextView semesterId=(TextView)arg1.findViewById(android.R.id.text1);
				intent.putExtra("semesterId", semesterId.getText().toString());
				intent.setClass(listSemester.this, displaySemester.class);
				startActivity(intent);
			}

		});
		
	}
	/*创建menu*/
	public boolean onCreateOptionsMenu(Menu menu)
	{  
		//为menu添加内容
		menu.add(0,0,0,R.string.add_semester);
		menu.add(0,1,1,R.string.clear_database);
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
				intent.setClass(listSemester.this, addSemester.class);
				this.startActivity(intent);
				finish();
				break;
			case 1:
				DBAdapter db=new DBAdapter(this);
				db.open();
				db.DBHelper.onUpgradeS(db.DBHelper.getWritableDatabase(), 1, 2);
				Intent intentToDis=new Intent();
				intentToDis.setClass(listSemester.this, DisplaySAT.class);
				this.startActivity(intentToDis);
				finish();
				break;
			case 2:
				Intent intentToStart=new Intent();
				intentToStart.setClass(listSemester.this, start.class);
				this.startActivity(intentToStart);
				finish();
				break;
		}
		return true;
	}
	
}
