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
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

public class AssignmentView extends Activity
{
	//声明控件
    private TextView nameText;   
    private TextView infoText; 
    private TextView courseText;  
    private TextView deadlineText;  
    private TextView submitText;   
    private RadioGroup submit;
    private RadioButton submitY,submitN;
    private RatingBar priorityText;   
   
    private Button deleteButton;
    private Button cancelButton;
    private DBAdapter db;
    
    public void onCreate(Bundle savedInstanceState) 
	{
        super.onCreate(savedInstanceState);
        
		setContentView(R.layout.assignmentview);   //布局文件
        db=new DBAdapter(AssignmentView.this);
        db.open();
        
        //获得控件
        nameText=(TextView)findViewById(R.id.a_nameView02);
        infoText=(TextView)findViewById(R.id.a_infoView02);
        courseText=(TextView)findViewById(R.id.a_courseView02);
        deadlineText=(TextView)findViewById(R.id.a_deadlineView02);
        submitText=(TextView)findViewById(R.id.submitwayView);
        submit=(RadioGroup)findViewById(R.id.submitGroupView);
        submitY=(RadioButton)findViewById(R.id.submitYView);
        submitN=(RadioButton)findViewById(R.id.submitNView);
        priorityText=(RatingBar)findViewById(R.id.rb2);
        
        deleteButton=(Button)findViewById(R.id.delete_assignmentButton);
        cancelButton=(Button)findViewById(R.id.cancelButton03);
       
        Intent intent=getIntent();
        final String id=intent.getStringExtra("AssignmentId");
        
        Cursor cs=db.getAssignmentById(id);
        
        
        nameText.setText(cs.getString(1));
        infoText.setText(cs.getString(2));
        courseText.setText(cs.getString(3));
        deadlineText.setText(cs.getString(4));
        submitText.setText(cs.getString(5));
        String submit=cs.getString(6); 
        if(submit.equals("true"))
        {
        	submitY.setChecked(true);
        }else
        {
        	submitN.setChecked(true);
        }
        float rating=Float.parseFloat(cs.getString(7));
        priorityText.setRating(rating);
        
        deleteButton.setOnClickListener(new Button.OnClickListener(){

			@Override
			public void onClick(
					View v) {
				db.deleteAssignment(id);
				db.close();
				Intent intent=new Intent();
				intent.setClass(AssignmentView.this,DisplaySAT.class);
				startActivity(intent);
				finish();
				
			}
        	
        });
        
        cancelButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(
					View v) {
				finish();
				
			}
        	
        });
	}           
}