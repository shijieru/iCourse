package com.sjr.android.iCourse;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;

public class DisplaySAT extends TabActivity{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		TabHost tabHost=getTabHost();
		tabHost.setBackgroundResource(R.drawable.background1);
		Intent semesterIntent=new Intent();
		semesterIntent.setClass(this, listSemester.class);
		tabHost.addTab(tabHost.newTabSpec(this.getString(R.string.list_semester)).setIndicator(this.getString(R.string.list_semester)).setContent(semesterIntent));
		Intent teacherIntent=new Intent();
		teacherIntent.setClass(this, ListTeacher.class);
		tabHost.addTab(tabHost.newTabSpec(this.getString(R.string.list_teacher)).setIndicator(this.getString(R.string.list_teacher)).setContent(teacherIntent));
		Intent assignmentIntent=new Intent();
		assignmentIntent.setClass(this, listAssignments.class);
		tabHost.addTab(tabHost.newTabSpec(this.getString(R.string.list_assignment)).setIndicator(this.getString(R.string.list_assignment)).setContent(assignmentIntent));
	}
}