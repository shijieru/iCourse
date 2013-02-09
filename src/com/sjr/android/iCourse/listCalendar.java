package com.sjr.android.iCourse;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.sjr.android.iCourse.DBAdapter.DatabaseHelper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;

public class listCalendar extends Activity {
	
	private ListView cal_Class_ListView;


	
	private ArrayList<DateWidgetDayCell> days = new ArrayList<DateWidgetDayCell>();

	private Calendar calStartDate = Calendar.getInstance();

	private Calendar calToday = Calendar.getInstance();

	private Calendar calCalendar = Calendar.getInstance();

	private Calendar calSelected = Calendar.getInstance();

	LinearLayout layContent = null;

	Button btnPrev = null;
	Button btnToday = null;
	Button btnNext = null;
	
	private int iFirstDayOfWeek = Calendar.MONDAY;
	private int iMonthViewCurrentMonth = 0;
	private int iMonthViewCurrentYear = 0;
	
	public static final int SELECT_DATE_REQUEST = 111;
	private static final int iDayCellSize = 38;
	private static final int iDayHeaderHeight = 24;
	private static final int iTotalWidth = (iDayCellSize * 7);
	
//	private TextView tv;
	private int mYear;
	private int mMonth;
	private int mDay;
	
	

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		iFirstDayOfWeek = Calendar.MONDAY;
		mYear = calSelected.get(Calendar.YEAR);
		mMonth = calSelected.get(Calendar.MONTH);
		mDay = calSelected.get(Calendar.DAY_OF_MONTH);
		
		setContentView(generateContentView());
		calStartDate = getCalendarStartDate();
		
		
		DateWidgetDayCell daySelected = updateCalendar();
		
		updateControlsState();
		if (daySelected != null)
			daySelected.requestFocus();
	}

	@Override
	public void onStart() {
		super.onStart();

	}

	private LinearLayout createLayout(int iOrientation) {
		LinearLayout lay = new LinearLayout(this);
		lay.setLayoutParams(new LayoutParams(
				android.view.ViewGroup.LayoutParams.FILL_PARENT,
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT));
		lay.setOrientation(iOrientation);
		return lay;
	}

	private Button createButton(String sText, int iWidth, int iHeight) {
		Button btn = new Button(this);
		btn.setText(sText);
		btn.setLayoutParams(new LayoutParams(iWidth, iHeight));
		return btn;
	}

	private void generateTopButtons(LinearLayout layTopControls) {
		final int iHorPadding = 24;
		final int iSmallButtonWidth = 60;
		
		btnToday = createButton("Locate Today", iTotalWidth - iSmallButtonWidth
				- iSmallButtonWidth,
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		btnToday.setPadding(iHorPadding, btnToday.getPaddingTop(), iHorPadding,
				btnToday.getPaddingBottom());
		btnToday.setBackgroundResource(android.R.drawable.btn_default_small);
		
		SymbolButton btnPrev = new SymbolButton(this,
				SymbolButton.symbol.arrowLeft);
		btnPrev.setLayoutParams(new LayoutParams(iSmallButtonWidth,
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT));
		btnPrev.setBackgroundResource(android.R.drawable.btn_default_small);
		
		SymbolButton btnNext = new SymbolButton(this,
				SymbolButton.symbol.arrowRight);
		btnNext.setLayoutParams(new LayoutParams(iSmallButtonWidth,
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT));
		btnNext.setBackgroundResource(android.R.drawable.btn_default_small);

		
		btnPrev.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View arg0) {
				setPrevViewItem();
			}
		});
		btnToday.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View arg0) {

				setTodayViewItem();
			}
		});
		btnNext.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View arg0) {
				setNextViewItem();
			}
		});

		layTopControls.setGravity(Gravity.CENTER_HORIZONTAL);
		layTopControls.addView(btnPrev);
		layTopControls.addView(btnToday);
		layTopControls.addView(btnNext);
	}
//
	private View generateContentView() {
		LinearLayout layMain = createLayout(LinearLayout.VERTICAL);
		layMain.setPadding(8, 8, 8, 8);
		LinearLayout layTopControls = createLayout(LinearLayout.HORIZONTAL);
		layContent = createLayout(LinearLayout.VERTICAL);
		
		
		layContent.setPadding(20, 0, 20, 0);
		generateTopButtons(layTopControls);
		
		generateCalendar(layContent);
		
		layMain.addView(layTopControls);
		
		layMain.addView(layContent);
		layMain.setBackgroundResource(R.drawable.background1);

		TextView course_textView=new TextView(this);
		course_textView.setText("当日课程");
		
		
		TextView assignment_textView=new TextView(this);
		assignment_textView.setText("未完成作业");
		
		
		cal_Class_ListView = new ListView(this);

		layMain.addView(cal_Class_ListView);
		
		return layMain;
	}

	private View generateCalendarRow() {
		LinearLayout layRow = createLayout(LinearLayout.HORIZONTAL);
		for (int iDay = 0; iDay < 7; iDay++) {
			DateWidgetDayCell dayCell = new DateWidgetDayCell(this,
					iDayCellSize, iDayCellSize);
			dayCell.setItemClick(mOnDayCellClick);
			days.add(dayCell);
			layRow.addView(dayCell);
		}
		return layRow;
	}

	private View generateCalendarHeader() {
		LinearLayout layRow = createLayout(LinearLayout.HORIZONTAL);
		for (int iDay = 0; iDay < 7; iDay++) {
			DateWidgetDayHeader day = new DateWidgetDayHeader(this,
					iDayCellSize, iDayHeaderHeight);
			final int iWeekDay = DayStyle.getWeekDay(iDay, iFirstDayOfWeek);
			day.setData(iWeekDay);
			layRow.addView(day);
		}
		return layRow;
	}

	private void generateCalendar(LinearLayout layContent) {
		layContent.addView(generateCalendarHeader());
		days.clear();
		for (int iRow = 0; iRow < 6; iRow++) {
			layContent.addView(generateCalendarRow());
		}
	}

	private Calendar getCalendarStartDate() {
		calToday.setTimeInMillis(System.currentTimeMillis());
		calToday.setFirstDayOfWeek(iFirstDayOfWeek);

		if (calSelected.getTimeInMillis() == 0) {
			calStartDate.setTimeInMillis(System.currentTimeMillis());
			calStartDate.setFirstDayOfWeek(iFirstDayOfWeek);
		} else {
			calStartDate.setTimeInMillis(calSelected.getTimeInMillis());
			calStartDate.setFirstDayOfWeek(iFirstDayOfWeek);
		}

		UpdateStartDateForMonth();

		return calStartDate;
	}

	private DateWidgetDayCell updateCalendar() {
		DateWidgetDayCell daySelected = null;
		boolean bSelected = false;
		final boolean bIsSelection = (calSelected.getTimeInMillis() != 0);
		final int iSelectedYear = calSelected.get(Calendar.YEAR);
		final int iSelectedMonth = calSelected.get(Calendar.MONTH);
		final int iSelectedDay = calSelected.get(Calendar.DAY_OF_MONTH);
		calCalendar.setTimeInMillis(calStartDate.getTimeInMillis());
		for (int i = 0; i < days.size(); i++) {
			final int iYear = calCalendar.get(Calendar.YEAR);
			final int iMonth = calCalendar.get(Calendar.MONTH);
			final int iDay = calCalendar.get(Calendar.DAY_OF_MONTH);
			final int iDayOfWeek = calCalendar.get(Calendar.DAY_OF_WEEK);
			DateWidgetDayCell dayCell = days.get(i);
			// check today
			boolean bToday = false;
			if (calToday.get(Calendar.YEAR) == iYear)
				if (calToday.get(Calendar.MONTH) == iMonth)
					if (calToday.get(Calendar.DAY_OF_MONTH) == iDay)
						bToday = true;
			// check holiday
			boolean bHoliday = false;
			if ((iDayOfWeek == Calendar.SATURDAY)
					|| (iDayOfWeek == Calendar.SUNDAY))
				bHoliday = true;
			if ((iMonth == Calendar.JANUARY) && (iDay == 1))
				bHoliday = true;

			dayCell.setData(iYear, iMonth, iDay, bToday, bHoliday,
					iMonthViewCurrentMonth);
			bSelected = false;
			if (bIsSelection)
				if ((iSelectedDay == iDay) && (iSelectedMonth == iMonth)
						&& (iSelectedYear == iYear)) {
					bSelected = true;
				}
			dayCell.setSelected(bSelected);
			if (bSelected)
				daySelected = dayCell;
			calCalendar.add(Calendar.DAY_OF_MONTH, 1);
		}
		layContent.invalidate();
		return daySelected;
	}

	private void UpdateStartDateForMonth() {
		iMonthViewCurrentMonth = calStartDate.get(Calendar.MONTH);
		iMonthViewCurrentYear = calStartDate.get(Calendar.YEAR);
		calStartDate.set(Calendar.DAY_OF_MONTH, 1);
		UpdateCurrentMonthDisplay();
		// update days for week
		int iDay = 0;
		int iStartDay = iFirstDayOfWeek;
		if (iStartDay == Calendar.MONDAY) {
			iDay = calStartDate.get(Calendar.DAY_OF_WEEK) - Calendar.MONDAY;
			if (iDay < 0)
				iDay = 6;
		}
		if (iStartDay == Calendar.SUNDAY) {
			iDay = calStartDate.get(Calendar.DAY_OF_WEEK) - Calendar.SUNDAY;
			if (iDay < 0)
				iDay = 6;
		}
		calStartDate.add(Calendar.DAY_OF_WEEK, -iDay);
	}

	private void UpdateCurrentMonthDisplay() {
		String s = calCalendar.get(Calendar.YEAR) + "/"
				+ (calCalendar.get(Calendar.MONTH) + 1);// dateMonth.format(calCalendar.getTime());
		mYear = calCalendar.get(Calendar.YEAR);
	}

	private void setPrevViewItem() {
		iMonthViewCurrentMonth--;
		if (iMonthViewCurrentMonth == -1) {
			iMonthViewCurrentMonth = 11;
			iMonthViewCurrentYear--;
		}
		calStartDate.set(Calendar.DAY_OF_MONTH, 1);
		calStartDate.set(Calendar.MONTH, iMonthViewCurrentMonth);
		calStartDate.set(Calendar.YEAR, iMonthViewCurrentYear);
		UpdateStartDateForMonth();
		updateCalendar();

	}

	private void setTodayViewItem() {
		calToday.setTimeInMillis(System.currentTimeMillis());
		calToday.setFirstDayOfWeek(iFirstDayOfWeek);
		calStartDate.setTimeInMillis(calToday.getTimeInMillis());
		calStartDate.setFirstDayOfWeek(iFirstDayOfWeek);
		UpdateStartDateForMonth();
		updateCalendar();
	}

	private void setNextViewItem() {
		iMonthViewCurrentMonth++;
		if (iMonthViewCurrentMonth == 12) {
			iMonthViewCurrentMonth = 0;
			iMonthViewCurrentYear++;
		}
		calStartDate.set(Calendar.DAY_OF_MONTH, 1);
		calStartDate.set(Calendar.MONTH, iMonthViewCurrentMonth);
		calStartDate.set(Calendar.YEAR, iMonthViewCurrentYear);
		UpdateStartDateForMonth();
		updateCalendar();

	}

	private DateWidgetDayCell.OnItemClick mOnDayCellClick = new DateWidgetDayCell.OnItemClick() {
		public void OnClick(DateWidgetDayCell item) {
			calSelected.setTimeInMillis(item.getDate().getTimeInMillis());
			item.setSelected(true);
			updateCalendar();
			updateControlsState();
		}
	};
//controlState
	private void updateControlsState() {
		Toast.makeText(listCalendar.this, calSelected.getTime().toString(), Toast.LENGTH_SHORT).show();
		
		DBAdapter db=new DBAdapter(this);
		db.open();
		//*********
		Cursor curClass = db.getClassesByDayofWeek(calSelected);
		Cursor curAssignment = db.getAllUnfinishedAssignmentByDEADLINE();
		if(curClass !=null){
		
		ListAdapter adapterofClass = new SimpleCursorAdapter(this,
			android.R.layout.simple_list_item_2,
			curClass,
			new String[] {"course","time"},
			new int[] { android.R.id.text1, android.R.id.text2 });
		
		cal_Class_ListView.setAdapter(adapterofClass);
		}
		if(curAssignment !=null){
		ListAdapter adapterofAssignment = new SimpleCursorAdapter(this,
				android.R.layout.simple_expandable_list_item_2,
				curAssignment,
				new String[]{"name","info","deadline"},
				new int[]{android.R.id.text1, android.R.id.text2,android.R.id.text2});
		
		}
	}
	

	private String format(int x) {
		String s = "" + x;
		if (s.length() == 1)
			s = "0" + s;
		return s;
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
				course_intent.setClass(listCalendar.this, addCourseInfo.class);
				this.startActivity(course_intent);
				finish();
				break;
			case 1:
				finish();
		}
		return true;
	}

}
