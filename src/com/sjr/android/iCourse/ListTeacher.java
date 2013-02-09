package com.sjr.android.iCourse;


import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.PhoneLookup;
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

public class ListTeacher extends Activity
{
	LinearLayout	m_LinearLayout;
	ListView		m_ListView;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		/* ����LinearLayout���ֶ��� */
		m_LinearLayout = new LinearLayout(this);
		/* ���ò���LinearLayout������ */
		m_LinearLayout.setOrientation(LinearLayout.VERTICAL);
		m_LinearLayout.setBackgroundResource(R.drawable.background1);
		/* ����ListView���� */
		m_ListView = new ListView(this);
		LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

		/* ���m_ListView��m_LinearLayout���� */
		m_LinearLayout.addView(m_ListView, param);

		/* ������ʾm_LinearLayout���� */
		setContentView(m_LinearLayout);
		
		DBAdapter db=new DBAdapter(this);
		db.open();
		
		// ��ȡ���ݿ�Teachers��Cursor
		Cursor cur=db.getAllTeachers(); 

		// ListAdapter��ListView�ͺ�̨���ݵ�����
		ListAdapter adapter = new SimpleCursorAdapter(this,
			// ����List��ÿһ�е���ʾģ��
			// ��ʾÿһ�а�������������
			android.R.layout.simple_list_item_2,
			// ���ݿ��Cursor����
			cur,
			new String[] {"_id","name"},
			new int[] { android.R.id.text1, android.R.id.text2 });
		
		
		/* ��adapter��ӵ�m_ListView�� */
		m_ListView.setAdapter(adapter);
		

		/* Ϊm_ListView��ͼ���setOnItemClickListener���� */
		m_ListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
			{
				//�ڶ�ѡ�е�����д���
				Intent intent=new Intent();
				final TextView rowId=(TextView)arg1.findViewById(android.R.id.text1);
				intent.putExtra("rowId", rowId.getText().toString());
				intent.setClass(ListTeacher.this, TeacherView.class);
				startActivity(intent);
			}

		});
		
	}
	/*����menu*/
	public boolean onCreateOptionsMenu(Menu menu)
	{  
		//Ϊmenu�������
		menu.add(0,0,0,R.string.add_teacher);
		menu.add(0,1,1,R.string.clear_database);
		menu.add(0,2,2,"�������˵�");
		return true;
	}
	/*����menu�¼� */
	public boolean onOptionsItemSelected(MenuItem item)
	{
		int item_id=item.getItemId();
		switch(item_id)
		{
			case 0:
				Intent intent=new Intent();
				intent.setClass(ListTeacher.this, Addteacher.class);
				this.startActivity(intent);
				break;
			case 1:
				DBAdapter db=new DBAdapter(this);
				db.open();
				db.DBHelper.onUpgradeT(db.DBHelper.getWritableDatabase(), 1, 2);
				Intent intentToDis=new Intent();
				intentToDis.setClass(ListTeacher.this, DisplaySAT.class);
				this.startActivity(intentToDis);
				finish();
				break;
			case 2:
				Intent intentToStart=new Intent();
				intentToStart.setClass(ListTeacher.this, start.class);
				this.startActivity(intentToStart);
				finish();
				break;
		}
		return true;
	}
}