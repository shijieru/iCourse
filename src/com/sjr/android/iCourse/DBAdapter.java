package com.sjr.android.iCourse;

import java.util.Calendar;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBAdapter
{
	//Courses表
    public static final String COURSE_UCODE = " _id";  //int 主键 自增长
    public static final String COURSE_UNAME = "name";  //String not null unique
    public static final String COURSE_INFO = "info";  //String
    public static final String COURSE_TEACHER = "teacher";  //String 匹配teacher表name
    public static final String COURSE_SEMESTER = "semester";  //String not null 格式：2011-1
    
	//teacher表
	public static final String TEACHER_UCODE = "_id";  //int 主键 自增长
    public static final String TEACHER_UNAME = "name";  //String not null
    public static final String TEACHER_GENDER = "gender";  //String not null "男"or"女"
    public static final String TEACHER_TITLE = "title";  //String
    public static final String TEACHER_COLLEGE = "college";  //String
    public static final String TEACHER_PHONE = "phone";  //String
    public static final String TEACHER_EMAIL = "email";  //String
    public static final String TEACHER_OFFICE = "office";  //String
    public static final String TEACHER_FTP = "ftp";  //String
    public static final String TEACHER_TIME = "officetime";  //String
    
    //assignment表
    public static final String ASSIGNMENT_UCODE = " _id";  //int 主键 自增长
    public static final String ASSIGNMENT_NAME = "name";  //String not null
    public static final String ASSIGNMENT_INFO = "info";  //String
    public static final String ASSIGNMENT_COURSE = "course";  //String not null 匹配course表name
    public static final String ASSIGNMENT_DEADLINE = "deadline";  //String not null 格式2010-08-01 2:00
    public static final String ASSIGNMENT_SUBMIT = "submitway";   //String
    public static final String ASSIGNMENT_SUBMITORNOT = "submitornot";  //boolean not null 默认为否
    public static final String ASSIGNMENT_PRIORITY = "priority";  //double
    
  //Classes表
    public static final String CLASS_UCODE = "_id";  //int 主键 自增长
    public static final String CLASS_COURSE = "course";  //String not null 匹配course表
    public static final String CLASS_DAY = "day";  //
    public static final String CLASS_TIME = "time";
    public static final String CLASS_WEEK = "week";
    public static final String CLASS_START = "start";
    public static final String CLASS_END = "end";
    public static final String CLASS_ROOM ="classroom";
     
    //semester表
	public static final String SEMESTER_UCODE = "_id";
    public static final String SEMESTER_START = "start";
    public static final String SEMESTER_DURATION = "duration";
    public static final String SEMESTER_END = "end";
	
	private static final String TAG = "DBAdapter";
	//数据库表名声明
	private static final String DATABASE_NAME = "iCourse";
	private static final String TABLE_COURSE = "Courses";
	private static final String TABLE_CLASS = "Classes";
	private static final String TABLE_TEACHER = "teachers";
	private static final String TABLE_SEMESTER = "semesters";
	private static final String TABLE_ASSIGNMENT = "assignments";
	private static final int DATABASE_VERSION = 1;
	//建立数据库表文件
	private static final String COURSES_CREATE =
    	"CREATE TABLE Courses (_id integer primary key autoincrement," +
    	"name varchar NOT NULL UNIQUE,info varchar," +
    	"teacher varchar,semester varchar NOT NULL);";
	
	private static final String CLASSES_CREATE =
    	"CREATE TABLE Classes (_id integer primary key autoincrement" +
    	" UNIQUE,course varchar NOT NULL,day varchar NOT NULL," +
    	"time varchar NOT NULL,week varchar NOT NULL,start integer NOT NULL," +
    	"end integer NOT NULL,classroom varchar NOT NULL);";
	
	private static final String TEACHER_CREATE ="create table teachers (_id integer primary key autoincrement, "
		+ "name text not null, gender text not null, title text, college text, phone text, email text, office text, ftp text, officetime text);";
	
	private static final String ASSIGNMENT_CREATE ="create table assignments (_id integer primary key autoincrement, "
		+ "name text not null, info text, course text not null, deadline text, submitway text, submitornot boolean not null, priority double);";
	
	private static final String SEMESTER_CREATE ="create table semesters (_id varchar primary key, "
		+ "start text not null,duration text not null,end text not null);";
	
	private final Context context;
	DatabaseHelper DBHelper;
	private SQLiteDatabase db;
	
	public DBAdapter(Context ctx)
	{
		this.context = ctx;
		DBHelper = new DatabaseHelper(context);
	}
	//Android辅助类，主要用于数据库创建和版本管理
	public class DatabaseHelper extends SQLiteOpenHelper
	{
		DatabaseHelper(Context context)
		{
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}
		@Override
		public void onCreate(SQLiteDatabase db)
		{
			db.execSQL(COURSES_CREATE); 
			db.execSQL(CLASSES_CREATE);
			db.execSQL(TEACHER_CREATE);
			db.execSQL(ASSIGNMENT_CREATE);
			db.execSQL(SEMESTER_CREATE);
		}
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion,int newVersion)
		{
			db.execSQL("DROP TABLE IF EXISTS teachers");
			db.execSQL("DROP TABLE IF EXISTS assignments");
			db.execSQL("DROP TABLE IF EXISTS Courses");
			db.execSQL("DROP TABLE IF EXISTS Classes"); 
			db.execSQL("DROP TABLE IF EXISTS semesters");
			db.execSQL(TEACHER_CREATE);
			db.execSQL(ASSIGNMENT_CREATE);
			db.execSQL(COURSES_CREATE);
			db.execSQL(CLASSES_CREATE);
			db.execSQL(SEMESTER_CREATE);
		}
		public void onUpgradeT(SQLiteDatabase db, int oldVersion,int newVersion)
		{
			db.execSQL("DROP TABLE IF EXISTS teachers");
			db.execSQL(TEACHER_CREATE);
		}
		public void onUpgradeS(SQLiteDatabase db, int oldVersion,int newVersion)
		{
			db.execSQL("DROP TABLE IF EXISTS semesters");
			db.execSQL(SEMESTER_CREATE);
		}
		public void onUpgradeA(SQLiteDatabase db, int oldVersion,int newVersion)
		{
			db.execSQL("DROP TABLE IF EXISTS assignments");
			db.execSQL(ASSIGNMENT_CREATE);
		}
		
	}
	//打开数据库
	public DBAdapter open() throws SQLException
	{
		db = DBHelper.getWritableDatabase();
		return this;
	}
	//关闭数据库
	public void close()
	{
		DBHelper.close();
	}
	//向数据库插入一个教师
	public long insertTeacher(String name, String gender, String title,String college,String phone,String email,String office,String ftp,String officetime)
	{
		ContentValues initialValues = new ContentValues();
		initialValues.put(TEACHER_UNAME, name);
		initialValues.put(TEACHER_GENDER, gender);
		initialValues.put(TEACHER_TITLE, title);
		initialValues.put(TEACHER_COLLEGE, college);
		initialValues.put(TEACHER_PHONE, phone);
		initialValues.put(TEACHER_EMAIL, email);
		initialValues.put(TEACHER_OFFICE, office);
		initialValues.put(TEACHER_FTP, ftp);
		initialValues.put(TEACHER_TIME, officetime);
		return db.insert(TABLE_TEACHER, null, initialValues);
	}
	//向数据库中插入一个作业
	public long insertAssignment(String name, String info, String course,String deadline,String submitway,Boolean submitornot,double priority)
	{
		ContentValues initialValues = new ContentValues();
		initialValues.put(ASSIGNMENT_NAME, name);
		initialValues.put(ASSIGNMENT_INFO, info);
		initialValues.put(ASSIGNMENT_COURSE, course);
		initialValues.put(ASSIGNMENT_DEADLINE, deadline);
		initialValues.put(ASSIGNMENT_SUBMIT, submitway);
		initialValues.put(ASSIGNMENT_SUBMITORNOT, submitornot);
		initialValues.put(ASSIGNMENT_PRIORITY, priority);
		return db.insert(TABLE_ASSIGNMENT, null, initialValues);
	}
	// 向数据库中插入一个课程
	public long insertCourse(String name, String info, 
			String teacher, String semester) {
		ContentValues initialValues = new ContentValues();
		//initialValues.put(COURSE_UCODE, _id);
		initialValues.put(COURSE_UNAME, name);
		initialValues.put(COURSE_INFO, info);
		initialValues.put(COURSE_TEACHER, teacher);
		initialValues.put(COURSE_SEMESTER, semester);
		return db.insert(TABLE_COURSE, null, initialValues);
	}
	//向数据库插入一个学期
	public long insertSemester(String year,String month,String day, int duration)
	{
		ContentValues initialValues = new ContentValues();
		String ucode;
		int yearint=Integer.parseInt(year);
		int monthint=Integer.parseInt(month);
		int dayint=Integer.parseInt(day);
		
		if(monthint<6)
			ucode=Integer.toString(yearint-1)+"-2";
		else ucode=year+"-1";
		
		String startdate=year;
		
		if(month.length()==1)
			startdate=startdate+"0"+month;
		else
			startdate=startdate+month;
		if(day.length()==1)
			startdate=startdate+"0"+day;
		else
			startdate=startdate+day;
		
		
		Calendar endday=Calendar.getInstance();
		String enddate;
		endday.set(yearint, monthint-1, dayint);
		endday.add(Calendar.DATE,duration*7-1);
		enddate=Integer.toString(endday.get(Calendar.YEAR));
		if(endday.get(Calendar.MONTH)<9)
			enddate=enddate+"0"+Integer.toString(endday.get(Calendar.MONTH)+1);
		else
			enddate=enddate+Integer.toString(endday.get(Calendar.MONTH)+1);
		if(endday.get(Calendar.DAY_OF_MONTH)<10)
			enddate=enddate+"0"+Integer.toString(endday.get(Calendar.DAY_OF_MONTH));
		else
			enddate=enddate+Integer.toString(endday.get(Calendar.DAY_OF_MONTH));
		
		initialValues.put(SEMESTER_UCODE, ucode);
		initialValues.put(SEMESTER_START, startdate);
		initialValues.put(SEMESTER_DURATION, duration);
		initialValues.put(SEMESTER_END, enddate);
		return db.insert(TABLE_SEMESTER, null, initialValues);
	}
	// 向数据库中插入一个课程信息
	public long insertClass(String course, String day, String time, String week, 
			int start, int end, String classroom) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(CLASS_COURSE, course);
		initialValues.put(CLASS_DAY, day);
		initialValues.put(CLASS_TIME, time);
		initialValues.put(CLASS_WEEK, week);
		initialValues.put(CLASS_START, start);
		initialValues.put(CLASS_END, end);
		initialValues.put(CLASS_ROOM, classroom);
		return db.insert(TABLE_CLASS, null, initialValues);
	}

	// 删除一个指定上课时间
	public boolean deleteClassById(int id) {
		return db.delete(TABLE_CLASS, CLASS_UCODE + "=" + id, null) > 0;
	}
	//删除一个指定的教师
	public boolean deleteATeacher(String rowId)
	{
		return db.delete(TABLE_TEACHER, TEACHER_UCODE + "='" + rowId+"'", null) > 0;
	}
	// 删除一个指定课程
	public boolean deleteCourseByName(String name) {
		return db.delete(TABLE_COURSE, COURSE_UNAME + "='" + name+"'", null) > 0;
	}
	//删除一个指定的学期
	public boolean deleteSemester(String semesterId)
	{
		return db.delete(TABLE_SEMESTER, SEMESTER_UCODE + "='" + semesterId+"'", null) > 0;
	}
	//删除一个指定的作业
	public boolean deleteAssignment(String rowId)
	{
		return db.delete(TABLE_ASSIGNMENT, ASSIGNMENT_UCODE + "='" + rowId+"'", null) > 0;
	}
	// 检索所有课程信息
	public Cursor getAllCourses() {
		return db.query(TABLE_COURSE, new String[] { COURSE_UCODE, COURSE_UNAME,
				COURSE_INFO, COURSE_TEACHER, COURSE_SEMESTER}, null, null, null, null, null);
	}
	// 检索所有课程名称
	public Cursor getAllCoursesName() {
		Cursor mCursor=db.query(TABLE_COURSE, new String[] {COURSE_UCODE,COURSE_UNAME}, null, null, null, null, null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}

	// 检索所有课程
	public Cursor getAllClasses() {
		return db.query(TABLE_CLASS, new String[] { CLASS_UCODE, CLASS_COURSE,
				CLASS_DAY, CLASS_TIME, CLASS_WEEK, CLASS_START, CLASS_END, 
				CLASS_ROOM}, null, null, null, null, null);
	}
	// 检索某个课程的上课信息
	public Cursor getClassesByCourse(String name) throws SQLException {
		Cursor mCursor = db.query(true, TABLE_CLASS, new String[] {
				CLASS_UCODE, CLASS_COURSE, CLASS_DAY, CLASS_TIME, 
				CLASS_WEEK, CLASS_START, CLASS_END, CLASS_ROOM},
				CLASS_COURSE + "='" + name+"'", null, null, null, null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}
	public Cursor getAssignmentsByCourse(String name) throws SQLException {
		Cursor mCursor = db.query(true, TABLE_ASSIGNMENT, new String[] {
				ASSIGNMENT_UCODE,ASSIGNMENT_NAME,ASSIGNMENT_INFO,
				ASSIGNMENT_COURSE,ASSIGNMENT_DEADLINE,ASSIGNMENT_SUBMIT,ASSIGNMENT_SUBMITORNOT,
				ASSIGNMENT_PRIORITY},
				ASSIGNMENT_COURSE + "='" + name+"'", null, null, null, null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}
	

	//检索所有教师行
	public Cursor getAllTeachers()
	{
		return db.query(TABLE_TEACHER, new String[] {TEACHER_UCODE,TEACHER_UNAME},null,null,null,null,null);
	}
	// 检索某个名字的课程信息
	public Cursor getCourseByName(String name) throws SQLException {
		Cursor mCursor = db.query(true, TABLE_COURSE, new String[] {
				COURSE_UCODE, COURSE_UNAME, COURSE_INFO, COURSE_TEACHER, COURSE_SEMESTER},
				COURSE_UNAME + "='" + name+"'", null, null, null, null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}
	// 检索某个ID的课程信息
	public Cursor getCourseByID(int id) throws SQLException {
		Cursor mCursor = db.query(true, TABLE_COURSE, new String[] {
				COURSE_UCODE, COURSE_UNAME, COURSE_INFO, COURSE_TEACHER, COURSE_SEMESTER},
				COURSE_UCODE + "='" + id+"'", null, null, null, null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}
	//检索一个指定的教师
	public Cursor getATeacher(String rowId) throws SQLException
	{
		Cursor mCursor =db.query(true, TABLE_TEACHER, new String[] {TEACHER_UCODE,TEACHER_UNAME,TEACHER_GENDER,TEACHER_TITLE,TEACHER_COLLEGE,TEACHER_PHONE,TEACHER_EMAIL,TEACHER_OFFICE,TEACHER_FTP,TEACHER_TIME},TEACHER_UCODE + "='" + rowId+"'",null,null,null,null,null);
		if (mCursor != null) 
		{
			mCursor.moveToFirst();
		}
		return mCursor;
	}
	//检索所有学期
	public Cursor getAllSemesters()
	{
		return db.query(TABLE_SEMESTER, new String[] {SEMESTER_UCODE,SEMESTER_START,SEMESTER_END},null,null,null,null,null);
	}
	//按学期检索课程
	public Cursor getCourseBySemester(String Semester) throws SQLException {
		Cursor mCursor = db.query(true, TABLE_COURSE, new String[] {
				COURSE_UCODE, COURSE_UNAME, COURSE_INFO, COURSE_TEACHER, COURSE_SEMESTER},
				COURSE_SEMESTER + "='" + Semester+"'", null, null, null, null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}
	//检索一个指定的学期
	public Cursor getASemester(String semesterId) throws SQLException
	{
		Cursor mCursor =db.query(true, TABLE_SEMESTER, new String[] {SEMESTER_UCODE,SEMESTER_START,SEMESTER_DURATION,SEMESTER_END},SEMESTER_UCODE + "='" + semesterId+"'",null,null,null,null,null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}
	//检索所有作业
	public Cursor getAllAssignment()
	{
		return db.query(TABLE_ASSIGNMENT, new String[] {ASSIGNMENT_UCODE,ASSIGNMENT_NAME,ASSIGNMENT_INFO,
				ASSIGNMENT_COURSE,ASSIGNMENT_DEADLINE,ASSIGNMENT_SUBMIT,ASSIGNMENT_SUBMITORNOT,
				ASSIGNMENT_PRIORITY},null,null,null,null,null);
	}
	
	//检索一个作业
	public Cursor getAssignmentById(String id){
		Cursor cs=db.query(true,TABLE_ASSIGNMENT, new String[] {ASSIGNMENT_UCODE,ASSIGNMENT_NAME,ASSIGNMENT_INFO,
				ASSIGNMENT_COURSE,ASSIGNMENT_DEADLINE,ASSIGNMENT_SUBMIT,ASSIGNMENT_SUBMITORNOT,
				ASSIGNMENT_PRIORITY}, ASSIGNMENT_UCODE + "='" + id+"'", null, null, null, null, null);
		if (cs != null) {
			cs.moveToFirst();
		}
		return cs;
	}
	//更新一个教师
	public boolean updateTeacher(String rowId, String name, String gender, String title,String college,String phone,String email,String office,String ftp,String officetime)
	{
		ContentValues args = new ContentValues();
		args.put(TEACHER_UNAME, name);
		args.put(TEACHER_GENDER, gender);
		args.put(TEACHER_TITLE, title);
		args.put(TEACHER_COLLEGE, college);
		args.put(TEACHER_PHONE, phone);
		args.put(TEACHER_EMAIL, email);
		args.put(TEACHER_OFFICE, office);
		args.put(TEACHER_FTP, ftp);
		args.put(TEACHER_TIME, officetime);
		return db.update(TABLE_TEACHER, args,TEACHER_UCODE + "='" + rowId+"'", null) > 0;
	}
	// 更新一个课程的信息
	public boolean updateCourse( String name, String info, 
			String teacher, int semester) {
		ContentValues args = new ContentValues();
		//args.put(COURSE_UCODE, _id);
		//args.put(COURSE_UNAME, name);
		args.put(COURSE_INFO, info);
		args.put(COURSE_TEACHER, teacher);
		args.put(COURSE_SEMESTER, semester);
		return db.update(TABLE_COURSE, args, COURSE_UNAME + "='" + name+"'", null) > 0;
	}
	// 更新一节课的上课信息
	public boolean updateClass(int _id, String course, String day, String time, String week, 
			int start, int end, String classroom) {
		ContentValues args = new ContentValues();
		//args.put(CLASS_UCODE, _id);
		args.put(CLASS_COURSE, course);
		args.put(CLASS_DAY, day);
		args.put(CLASS_TIME, time);
		args.put(CLASS_WEEK, week);
		args.put(CLASS_START, start);
		args.put(CLASS_END, end);
		args.put(CLASS_ROOM, classroom);
		return db.update(TABLE_CLASS, args, CLASS_UCODE + "=" + _id, null) > 0;
	}
	/*Cursor作为一个指针从数据库查询返回结果集，使用Cursor允许Android更有效地管理它们需要的行和列，
	 * 你使用ContentValues对象存储键/值对，它的put()方法允许你插入不同数据类型的键值。
	 */
	public Cursor getAllUnfinishedAssignmentByPriority(){
		return db.query(TABLE_ASSIGNMENT, new String[] {ASSIGNMENT_UCODE,ASSIGNMENT_NAME,ASSIGNMENT_INFO,
				ASSIGNMENT_COURSE,ASSIGNMENT_DEADLINE,ASSIGNMENT_SUBMIT,ASSIGNMENT_SUBMITORNOT,
				ASSIGNMENT_PRIORITY}, ASSIGNMENT_SUBMITORNOT + "= 0" , null, null, null, ASSIGNMENT_PRIORITY);
		
	}
	
	public Cursor getAllUnfinishedAssignmentByCourse(){
		return db.query(TABLE_ASSIGNMENT, new String[] {ASSIGNMENT_UCODE,ASSIGNMENT_NAME,ASSIGNMENT_INFO,
				ASSIGNMENT_COURSE,ASSIGNMENT_DEADLINE,ASSIGNMENT_SUBMIT,ASSIGNMENT_SUBMITORNOT,
				ASSIGNMENT_PRIORITY}, ASSIGNMENT_SUBMITORNOT + "= 0" , null, null, null, ASSIGNMENT_COURSE);
	}
	
	public Cursor getAllUnfinishedAssignmentByDEADLINE(){
		return db.query(TABLE_ASSIGNMENT, new String[] {ASSIGNMENT_UCODE,ASSIGNMENT_NAME,ASSIGNMENT_INFO,
				ASSIGNMENT_COURSE,ASSIGNMENT_DEADLINE,ASSIGNMENT_SUBMIT,ASSIGNMENT_SUBMITORNOT,
				ASSIGNMENT_PRIORITY}, ASSIGNMENT_SUBMITORNOT + "= 0" , null, null, null, ASSIGNMENT_DEADLINE);
	}
	
	public Cursor getAllAssignmentByPriority(){
		return db.query(TABLE_ASSIGNMENT, new String[] {ASSIGNMENT_UCODE,ASSIGNMENT_NAME,ASSIGNMENT_INFO,
				ASSIGNMENT_COURSE,ASSIGNMENT_DEADLINE,ASSIGNMENT_SUBMIT,ASSIGNMENT_SUBMITORNOT,
				ASSIGNMENT_PRIORITY}, null , null, null, null, ASSIGNMENT_PRIORITY);
		
	}
	
	public Cursor getAllAssignmentByCourse(){
		return db.query(TABLE_ASSIGNMENT, new String[] {ASSIGNMENT_UCODE,ASSIGNMENT_NAME,ASSIGNMENT_INFO,
				ASSIGNMENT_COURSE,ASSIGNMENT_DEADLINE,ASSIGNMENT_SUBMIT,ASSIGNMENT_SUBMITORNOT,
				ASSIGNMENT_PRIORITY}, null , null, null, null, ASSIGNMENT_COURSE);
	}
	
	public Cursor getAllAssignmentByDEADLINE(){
		return db.query(TABLE_ASSIGNMENT, new String[] {ASSIGNMENT_UCODE,ASSIGNMENT_NAME,ASSIGNMENT_INFO,
				ASSIGNMENT_COURSE,ASSIGNMENT_DEADLINE,ASSIGNMENT_SUBMIT,ASSIGNMENT_SUBMITORNOT,
				ASSIGNMENT_PRIORITY}, null , null, null, null, ASSIGNMENT_DEADLINE);
	}
	
	//删除一门课程所有的作业
	public boolean deleteAssignmentsByCourse(String course)
	{
		return db.delete(TABLE_ASSIGNMENT, ASSIGNMENT_COURSE + "='" + course+"'", null) > 0;
	}
	//删除一门课程所有的上课时间
	public boolean deleteClassesByCourse(String course)
	{
		return db.delete(TABLE_CLASS, CLASS_COURSE + "='" + course+"'", null) > 0;
	}


	

	public Cursor getClassesByDayofWeek(Calendar calSelected){
		displaySemester xx = new displaySemester();
	
		int numWeek = xx.WeekInASemester(calSelected, context);
		Cursor mCursor = null;
		//Cursor
		if(numWeek == -1 || numWeek == -2){
			return null;
		}
		else{
			Cursor cs=getAllSemesters();
			cs.moveToFirst();
			int semy=Integer.parseInt(cs.getString(cs.getColumnIndex("start")).substring(0, 4));  //学期开始年份
			int semm=Integer.parseInt(cs.getString(cs.getColumnIndex("start")).substring(4,6));   //学期开始月份
			int selmonth=calSelected.get(Calendar.MONTH)+1;    //选择的月份
			int selyear=calSelected.get(Calendar.YEAR);       //选择的年份


			while((semy!=selyear)||(semm<6&&selmonth>=6)||(semm>=6&&selmonth<6))
			{
				if((selmonth<6)&&(semy==selyear-1)&&(semm>=6))
				{
					break;
				}
				if((selmonth>=6)&&(semy==selyear+1)&&(semm<6))
				{
					break;
				}
				
				if(cs.moveToNext())
				{
					semy=Integer.parseInt(cs.getString(cs.getColumnIndex("start")).substring(0, 4));
					semm=Integer.parseInt(cs.getString(cs.getColumnIndex("start")).substring(4,6));
				}else
				{
					break;
				}
			}

			String semId=cs.getString(cs.getColumnIndex("_id"));
			
			String dayofWeek=null;
			String classWeek=null;


			switch(calSelected.get(Calendar.DAY_OF_WEEK)){
			case 1: dayofWeek = "星期天";
				break;
			case 2: dayofWeek = "星期一";
				break;
			case 3: dayofWeek = "星期二";
				break;
			case 4: dayofWeek = "星期三";
				break;
			case 5: dayofWeek = "星期四";
				break;
			case 6:	dayofWeek = "星期五";
				break;
			case 7: dayofWeek = "星期六";
				break;
				default:dayofWeek =null;break;
			}
		
			switch(numWeek%2){
			case -1: classWeek = "单周";
				break;
			case 0: classWeek = "双周";
				break;
			case 1: classWeek = "单周";
			break;
			default:classWeek=null;break;
			}
			
			String sql="select a.semester,a.name,b.* from courses a,classes b where a.name=b.course and a.semester='"+semId+"' and b.day='"+dayofWeek+"' and(week='"+classWeek+"' or week='单双周')and start<"+(numWeek+1)+" and end>"+(numWeek-1);
			mCursor=db.rawQuery(sql, null);
		
		if (mCursor != null) {
			mCursor.moveToFirst();
			}
		return mCursor;
		}
		
	}

}












