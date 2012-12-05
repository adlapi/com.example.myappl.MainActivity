package com.example.myappl;


import java.util.ArrayList;

import com.example.myappl.MainActivity.DBHelper;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v4.view.ViewPager.LayoutParams;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;


public class widget extends AppWidgetProvider 
{
	SQLiteDatabase db;
	Cursor c;
	
	
	// опредл€ем список заданий как список
    ArrayList<String> Tasks = new ArrayList<String>();
	
	public static String ACTION_WIDGET_RECEIVER = "ActionReceiverWidget";
	
	
	 class DBHelper extends SQLiteOpenHelper {

	        public DBHelper(Context context) {
	          // конструктор суперкласса
	          super(context, "myDB", null, 1);
	        }

	        @Override
	        public void onCreate(SQLiteDatabase db) {
	         
	          // создаем таблицу с пол€ми
	          db.execSQL( "create table mytable (id integer primary key autoincrement, task_name text);" );
	        }

			@Override
			public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
				// TODO Auto-generated method stub
				
			}
	    }
	    
    
    
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager,
            int[] appWidgetIds) {
    	super.onUpdate(context, appWidgetManager, appWidgetIds);
    		
    	 int count_tasks=0;
    	 
       	 
    	final DBHelper Helper = new DBHelper(context);
    	// подключаемс€ к Ѕƒ
        SQLiteDatabase db1 = Helper.getReadableDatabase();
        
        
       Cursor c = db1.query("mytable", null, null, null, null, null, null);
 
       if ( c.moveToFirst() ) 
           {
         int task_nameColIndex = c.getColumnIndex("task_name");
           
             do {
            	 
             	if ( c.getString(task_nameColIndex) != null )
             	{	
             		Tasks.add( c.getString(task_nameColIndex).toString() );
             		count_tasks++;
           	}
                 // переход на следующую строку 
               // а если следующей нет (текуща€ - последн€€), то false - выходим из цикла
             } while (c.moveToNext());
           } else
           {
             c.close();
           }
           
           Helper.close();
    
         
      	
for (int i = 0; i < appWidgetIds.length; i++) 
{

	int appWidgetId = appWidgetIds[i];

// переход на мейн при нажатии на область виджета
Intent intent = new Intent(context, MainActivity.class);
PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.layout);

views.setOnClickPendingIntent(R.id.Widget, pendingIntent);
appWidgetManager.updateAppWidget(appWidgetId, views);
int number = (count_tasks);
RemoteViews remoteViews = new RemoteViews(context
        .getApplicationContext().getPackageName(),
        R.layout.layout);
    Log.w("WidgetExample", String.valueOf(number));
    // Set the text
    remoteViews.setTextViewText(R.id.button4,
        "Ќа данный момент заданий: " + String.valueOf(number));

    // Register an onClickListener
    Intent clickIntent = new Intent(context.getApplicationContext(),
        widget.class);

    clickIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
    clickIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS,
        appWidgetIds);

    PendingIntent pendingIntent5 = PendingIntent.getBroadcast(context, 0, clickIntent,
        PendingIntent.FLAG_UPDATE_CURRENT);
    remoteViews.setOnClickPendingIntent(R.id.button4, pendingIntent);
    appWidgetManager.updateAppWidget(appWidgetIds, remoteViews);
 if ( Tasks.size() > 0 )
{
	if ( Tasks.get(0) != null ) {views.setTextViewText(R.id.task1, Tasks.get(0));}
	if ( Tasks.size()>1 ) {views.setTextViewText(R.id.task2, Tasks.get(1));}
	if ( Tasks.size()>2 ) {views.setTextViewText(R.id.task3, Tasks.get(2));}
	if ( Tasks.size()>3 ) {views.setTextViewText(R.id.task4, Tasks.get(3));}
	if ( Tasks.size()>4 ) {views.setTextViewText(R.id.task5, Tasks.get(4));}
	}
appWidgetManager.updateAppWidget(appWidgetId, views);
}
    	

}  
    
    @Override
    public void onReceive(Context context, Intent intent) {
    	
    	  //Ћовим наш Broadcast, провер€ем и выводим сообщение
        final String action = intent.getAction();
        if (ACTION_WIDGET_RECEIVER.equals(action)) {
             String msg = "null";
            
             try {
                   msg = intent.getStringExtra("msg");
             } catch (NullPointerException e) {
                   Log.e("Error", "msg");
             }
             Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
        } 
        super.onReceive(context, intent);
  
   }
    
}




