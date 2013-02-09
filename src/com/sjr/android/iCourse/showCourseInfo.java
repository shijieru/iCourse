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
	
	private TextView showCourseName; //�γ�����
	private TextView showCourseTeacher; //�ڿν�ʦ
	private TextView showCourseSemester; //�Ͽ�ѧ��
	private TextView showCourseInfo;  //�γ���Ϣ
	private Button btAddCourseTime;  //����Ͽ�ʱ��İ�ť
	private Button btAddCourseAssignment;  //��ӿγ���ҵ�İ�ť
	private Button btReturn;  //���ذ�ť
	private ListView ListCourseTime;  //��ʾ�Ͽ�ʱ����б�
	private ListView ListCourseAssignment;  //��ʾ�γ���ҵ���б�
	
	private View viewAdd;  //����Ͽ�ʱ��Ľ���
	private String CourseName;  //�γ�����

	private Dialog dialogAdd;  //����Ͽ�ʱ��ĶԻ���
	
	/** ����Ͽ�ʱ��Ի���ı��� **/
	private EditText editClassroom; //�Ͽεص�
	private Spinner spinClassday;  //�Ͽ�����-���ڼ�
	private Spinner spinClassweek; //��˫��
	private EditText editTimestart;  //�ڼ��ڿο�ʼ
	private EditText editTimeend;  //�ڼ��ڿν���
	private EditText editWeekstart;  //�ڼ��ܿ�ʼ
	private EditText editWeekend;  //�ڼ��ܽ���
	private Button btnOK;  //ȷ�����
	private Button btnCancel;  //ȡ��
	
	private String Classday;  //���ڼ��Ͽ�
	private String Classweek;  //��˫��

	private SimpleCursorAdapter mAdapter;  //�Ͽ�ʱ���б��Adapter
	private SimpleCursorAdapter nAdapter;  //�γ���ҵ�б��Adapter
	
	private String deleteId;  //��ɾ�����Ͽ�ʱ��ID

	
	
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.show_course_info);
		
		db = new DBAdapter(this);
        db.open();
        
        //����һ�������ȡ�˽�������ʾ�Ŀγ�����
        Intent intent=getIntent();
        CourseName=intent.getStringExtra("CourseName");
        
        //��ʹ�õĿؼ���xml�ļ��еİ�
        showCourseName = (TextView)findViewById(R.id.showCourseName);
        showCourseTeacher = (TextView)findViewById(R.id.showCourseTeacher);
        showCourseSemester = (TextView)findViewById(R.id.showCourseSemester);
        showCourseInfo = (TextView)findViewById(R.id.showCourseInfo);
        btAddCourseTime = (Button)findViewById(R.id.btAddCourseTime);
        btAddCourseAssignment = (Button)findViewById(R.id.btAddCourseAssignment);
        btReturn = (Button) findViewById(R.id.ReturnFromCourseInfo);
        
        //�����ݿ���ѡ����ӦCourseName�Ŀγ���Ϣ
        Cursor cursor1 = db.getCourseByName(CourseName);
        showCourseName.setText("�γ����ƣ�"+cursor1.getString(1));
        showCourseTeacher.setText("�ڿν�ʦ��"+cursor1.getString(3));
        showCourseSemester.setText("�Ͽ�ѧ�ڣ�"+cursor1.getString(4));
        showCourseInfo.setText("�γ���Ϣ��"+cursor1.getString(2));
        cursor1.close();

        showCourseTimeList();  //��ʾ�Ͽ�ʱ���б�
        showCourseAssignmentList();  //��ʾ�γ���ҵ�б�
        

        /** Ϊ�Ͽ�ʱ���б�ListCourseTime��ӵ����¼� 
         *  ������ɾ�����Ͽ�ʱ�� **/
        ListCourseTime.setOnItemClickListener(new OnItemClickListener() {  
  
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,  
                    long arg3) {  
            	deleteId = ((TextView)arg1.findViewById(R.id.deletedTime)).getText().toString();
                showDialog(showCourseInfo.this, deleteId);
            }  
            private void showDialog(Context context, String id) {  
                AlertDialog.Builder builder = new AlertDialog.Builder(context);  
                builder.setTitle("ɾ����¼");  
                builder.setMessage("�Ƿ����ɾ�������Ͽ�ʱ��");  
                builder.setPositiveButton("ȷ��",  
                        new DialogInterface.OnClickListener() {  
                            public void onClick(DialogInterface dialog, int whichButton) {   
                                db.deleteClassById(Integer.parseInt(deleteId));
                            	Toast.makeText(getApplicationContext(),"ɾ���ɹ�", 1000).show();
                            	showCourseTimeList();
                            }  
                        });  
                builder.setNegativeButton("ȡ��",  
                        new DialogInterface.OnClickListener() {  
                            public void onClick(DialogInterface dialog, int whichButton) {  
                            }  
                        });  
                builder.show();  
            }  
        });  
        
        /**Ϊ����Ͽ�ʱ�䰴ťbtAddCourseTime��ӵ����¼�**/
        btAddCourseTime.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				createDialogAdd();
			}
        	
        });
        
        /**Ϊ��ӿγ���ҵ��ťbtAddCourseAssignment��ӵ����¼�**/
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
        
        /**Ϊ���ذ�ťbtReturn��ӵ����¼�**/
        btReturn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(
					View v) {
				// TODO Auto-generated method stub
				finish();
			}
        	
        });
        
	}
	
	/**��ʾ�Ͽ�ʱ���б������õĺ���**/
	public void showCourseTimeList(){
        ListCourseTime = (ListView)findViewById(R.id.ListCourseTime);
        Cursor cursor2 = db.getClassesByCourse(CourseName);
        mAdapter = new SimpleCursorAdapter(this,
    			// ����List��ÿһ�е���ʾģ��
    			// ��ʾÿһ�а�������������
    			R.layout.coursetime_listview,
    			// ���ݿ��Cursor����
    			cursor2,
    			new String[] {"_id","day","classroom","time","end","start","week"},
    			new int[] { R.id.deletedTime,R.id.lClassDay, R.id.lClassRoom, R.id.lClassTime,
        		R.id.lweek_end, R.id.lweek_start, R.id.lClassWeek});
        
        ListCourseTime.setAdapter(mAdapter);
    }
	
	/**��ʾ�γ���ҵ�б������õĺ���**/
	public void showCourseAssignmentList(){
		ListCourseAssignment = (ListView)findViewById(R.id.ListCourseAssignment);
		Cursor cursor3 = db.getAssignmentsByCourse(CourseName);
		nAdapter = new SimpleCursorAdapter(this,
				R.layout.assignment_listview,
				// ���ݿ��Cursor����
				cursor3,
				new String[] {"deadline","priority","course","name","_id"},
				new int[] { R.id.al_textView1,R.id.priorityView, R.id.al_textView2, 
				R.id.al_textView3, R.id.courseNumView});
		ListCourseAssignment.setAdapter(nAdapter);
		//Ϊ�б���ӵ����¼���������ת���Ӧ��ҵ��ʾ����
		ListCourseAssignment.setOnItemClickListener(new OnItemClickListener(){

			private String SelectedId;

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				SelectedId = ((TextView)arg1.findViewById(R.id.courseNumView)).getText().toString();
				Toast.makeText(getApplicationContext(),"ת��No."+SelectedId+"��ҵ��ʾ����", 1000).show();
				Intent intent=new Intent();
				intent.putExtra("AssignmentId",SelectedId);
				intent.setClass(showCourseInfo.this, AssignmentView.class);
				startActivity(intent);
				
			}});
	}
	
	/** ��������Ͽ���Ϣ�ĶԻ���  **/
	public void createDialogAdd(){
		viewAdd = this.getLayoutInflater().inflate(R.layout.dialog_addcourse, null);
		dialogAdd = new Dialog(this);
		dialogAdd.show();
		dialogAdd.setContentView(viewAdd);
		dialogAdd.setTitle("����µ��Ͽ���Ϣ");
		
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
		
		/** ȷ����ӵİ�ť�¼� **/
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
		/** ȡ����ӵİ�ť�¼� **/
		btnCancel.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialogAdd.dismiss();
			}
			
		});
	}
	
	/*����menu*/
	public boolean onCreateOptionsMenu(Menu menu)
	{  
		//Ϊmenu�������
		menu.add(0,0,0,"ɾ���˿γ�");
		menu.add(0,1,1,"���������ҵ");
		menu.add(0,2,2,"��������Ͽ�ʱ��");
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
        builder.setMessage("ɾ���γ�ͬʱ��ɾ�������Ͽ�ʱ��Ϳγ���ҵ���Ƿ�ȷ����");  
        builder.setPositiveButton("ȷ��",  
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
        builder.setNegativeButton("ȡ��",  
                new DialogInterface.OnClickListener() {  
                    public void onClick(DialogInterface dialog, int whichButton) {
                    	//
                    }  
                });  
        builder.show();  
    }  

}

