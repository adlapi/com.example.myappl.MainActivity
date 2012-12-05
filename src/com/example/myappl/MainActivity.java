package com.example.myappl;


import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;




public class MainActivity extends Activity implements OnClickListener  {


	// ���������� ��� ��������

	DBHelper dbHelper;
	
	// ������� ������ ��� ������
    ContentValues cv;
 
	SQLiteDatabase db;
  	
	Cursor c;
	
	
    // add task
	Button button1;
	//edit task
	Button button2;
	
	// del task(s)
	Button button_del;
		
	// listview � ���������
	ListView lvMain;
	
    // ��������� ������ ������� ��� ������
    ArrayList<String> Tasks = new ArrayList<String>();

    // ������� �������
    ArrayAdapter<String> adapter;
	
	// edit shorttext - ��������� �������
	EditText shorttext;
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
     
        
     // ������� ������ ��� �������� � ���������� �������� ��
        dbHelper = new DBHelper(this);
    	
        
        
        //
        // ������� ��� ��������
        // 
        
        lvMain = (ListView) findViewById(R.id.lvMain);
        // ������ � ����������� ������������� ������ - ����� ���� ������ ������ ���� �������
        lvMain.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        
        // ���������� � ����������� �������
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_single_choice, Tasks);
        //adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, Tasks);
        lvMain.setAdapter(adapter);
     
       
        button1 = (Button) findViewById(R.id.button1);
        button1.setOnClickListener(this);
        
        button2 = (Button) findViewById(R.id.button2);
        button2.setOnClickListener(this);
       
        button_del = (Button) findViewById(R.id.button_del);
        button_del.setOnClickListener(this);
     
        
        ///////  ������������ � ������ �� ��
        
   	 	
	     // ������������ � ��
	     SQLiteDatabase db = dbHelper.getWritableDatabase();
	     Cursor c = db.query("mytable", null, null, null, null, null, null);
	     // ������ ������� ������� �� ������ ������ �������
	      if ( c.moveToFirst() ) {
	        int task_nameColIndex = c.getColumnIndex("task_name");
	        do {
	        	if ( c.getString(task_nameColIndex) != null )
	        	{	
	        			 Tasks.add( c.getString(task_nameColIndex).toString() );
	        	     	 adapter.notifyDataSetChanged();
	        	}
	 	        } while (c.moveToNext());
	      } else
	      {
	        c.close();
	      }
	     dbHelper.close();
        
        
        
      ///////// ���������� ������� ������� ��� ������ ������� ��������� (����� ������ ������)
	  	if ( Tasks.size() == 0 )
    	{
	  		AlertDialog.Builder dialog_onstart = new AlertDialog.Builder(MainActivity.this);
	    	dialog_onstart.setTitle("������ ����");
	    	dialog_onstart.setMessage("�������� �������?");
	    	dialog_onstart.setPositiveButton("��", new DialogInterface.OnClickListener() {
				
				public void onClick(DialogInterface dialog, int which) {
	    	
					Intent intent_onstart = new Intent(MainActivity.this, add_task.class);
		        	startActivityForResult(intent_onstart, 0);
					
				}
			});
	    	
	    	dialog_onstart.setNegativeButton("���", new DialogInterface.OnClickListener() {
				
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					Toast.makeText(getApplicationContext(), "������ ����", Toast.LENGTH_SHORT).show();
				}
			});
	    	
	    	AlertDialog alertdialog = dialog_onstart.create();
	    	alertdialog.show();
	 	}
		////////////
        
        // ���������� ������� ����� ������� ��� ���������
	  	else
	  	{
	  		Toast.makeText(getApplicationContext(), "����� �������: " + Tasks.size(), Toast.LENGTH_SHORT).show();
	  		// �������� ��� ������� ����������
    		lvMain.performItemClick(lvMain, 0, lvMain.getItemIdAtPosition(0));
	  	}
   
    }
    
   

    class DBHelper extends SQLiteOpenHelper {

        public DBHelper(Context context) {
          // ����������� �����������
          super(context, "myDB", null, 1);
        }
        @Override
        public void onCreate(SQLiteDatabase db) {
          // ������� ������� � ������
          db.execSQL( "create table mytable (id integer primary key autoincrement, task_name text);" );
        }
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub
		}}
    
    
 
    // ��������� ������� ������
	public void onClick(View v) {
	        switch (v.getId()) {
	        
	        case R.id.button1: // ������ �������� �������
	        	
	        	Intent intent = new Intent(this, add_task.class);
	        	startActivityForResult(intent, 1);
	        	
	        break;
	       
	        case R.id.button2: // ������ �������� �������
	        	if ( Tasks.isEmpty() )
	        	{
	        		Toast.makeText(getApplicationContext(), "�������� �������", Toast.LENGTH_SHORT).show();
	        		return;
	        	}
	        	
	        	Intent intent2 = new Intent(this, edit_task.class);
	        	intent2.putExtra("string", Tasks.get( lvMain.getCheckedItemPosition() ).toString() );  // �������� ����������
	        	//�������, ����������� � ������� � ���������� � edit_task 
	        	startActivityForResult(intent2, 2);
	        	
	        break;
	       
	        case R.id.button_del: // ������ ������� �������
	        	obd_del();
	
	        	break;
	       	
	        default:
	        		break;
	           }
	        }
	    
   
	
 // ��������� ������ �������� ������� - ������
    private void obd_del()
    {
    	if ( Tasks.isEmpty() )
    	{
    		Toast.makeText(getApplicationContext(), "�������� �������", Toast.LENGTH_SHORT).show();
    		return;
    	}
    	
    	else
    	{
    	AlertDialog.Builder dbi = new AlertDialog.Builder(this);
    	dbi.setTitle("��������");
    	dbi.setMessage("������� �������?");
    	dbi.setPositiveButton("��", new DialogInterface.OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				
				// ������� ���������� ������� �� ������
				Tasks.remove(lvMain.getCheckedItemPosition());
				
				 // ������� ������ ��� ������
	    	    ContentValues cv = new ContentValues();
	    	     // ������������ � ��
	    	     SQLiteDatabase db = dbHelper.getWritableDatabase();
	    	     
	    	    Cursor c = db.query("mytable", null, null, null, null, null, null);
	    	 
	    	    // ������ �� ������� ������� ����� �������
	    	    c.moveToPosition(lvMain.getCheckedItemPosition());
	    	  
	    	    // �������� ������
	    	    int idColIndex = c.getColumnIndex("id");
		        int ID = c.getInt(idColIndex); 
	    	  
	    	    // ������� ������ �� ��	    	  	    	    
	    	    db.delete("mytable", "id = " + ID, null);
	    	    c.close();
	    	    
				
	    	    // ������� ��������, ��� ���������� ������
	    	    adapter.notifyDataSetChanged();

				dbHelper.close();
				
				   // ���� � ������ 1 �������, �������� ��� ����������
		    	   if ( Tasks.size() == 1 )
		    	   {
		    		   lvMain.performItemClick(lvMain, 0, lvMain.getItemIdAtPosition(0));
		    		   
		    	   }
		    	   
		    		// �������� ������� ����������
		    	   else
		    		   {
		    		   lvMain.performItemClick(lvMain, Tasks.size()-1, lvMain.getItemIdAtPosition(Tasks.size()-1));
		    		   }
				
				
				///////// ���� ������� ��������� �������, ���������� �������� �����
			  	if ( Tasks.size() == 0 )
	        	{
			  		AlertDialog.Builder dbi2 = new AlertDialog.Builder(MainActivity.this);
			    	dbi2.setTitle("������ ����");
			    	dbi2.setMessage("������ ����, �������� �������?");
			    	dbi2.setPositiveButton("��", new DialogInterface.OnClickListener() {
						
						public void onClick(DialogInterface dialog, int which) {
			    	
							Intent intent_dialog = new Intent(MainActivity.this, add_task.class);
				        	startActivityForResult(intent_dialog, 0);
							
						}
					});
			    	
			    	dbi2.setNegativeButton("���", new DialogInterface.OnClickListener() {
						
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							Toast.makeText(getApplicationContext(), "������ ����", Toast.LENGTH_SHORT).show();
							
						}
					});
			    	
			    	AlertDialog alertdialog = dbi2.create();
			    	alertdialog.show();
			 	}
				////////////
			  	
		
				// ����������� ���������
				Toast.makeText(getApplicationContext(), "�������. ����� �������: " + Tasks.size(), Toast.LENGTH_SHORT).show();
				
			}
		});
    	// ���� � ������� ������ ������� "���"
    	dbi.setNegativeButton("���", new DialogInterface.OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
							
			}
		});
    	
    	AlertDialog alertdialog = dbi.create();
        	alertdialog.show();
    }}
      
      
    // �������� ���������(�������) � ����-�������� ��� �������� ����(�������) � �������. �������� ���� �� add_task ���� �� edit_task
   @Override
   protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    String shorttext_s = data.getStringExtra("shorttext");
    
    // ���� ������ �� edit_task (�������� �������)
    if ( requestCode == 2 )
    {
    	   Tasks.set(lvMain.getCheckedItemPosition(), shorttext_s); // �������� ������
    	   
    	// ������� ������ ��� ������
   	    ContentValues cv = new ContentValues();
   	    // ������������ � ��
   	    SQLiteDatabase db = dbHelper.getWritableDatabase();
   	    Cursor c = db.query("mytable", null, null, null, null, null, null);
   	    // ������ �� ������� ������� ����� ��������
   	    c.moveToPosition(lvMain.getCheckedItemPosition());
   	    // �������� ������
   	    int idColIndex = c.getColumnIndex("id");
	    int ID = c.getInt(idColIndex); 
   	    // �������� ������ � ��
	    cv.put("task_name", shorttext_s);
   	    db.update("mytable", cv, "id = " + ID, null);
	    //db.delete("mytable", "id = " + ID, null);
   	    c.close();
   	    // ������� ��������, ��� ���������� ������
   	    adapter.notifyDataSetChanged();
		dbHelper.close();
    	   
    	   Intent upd = new Intent(this, widget.class);
   	    upd.setAction("android.appwidget.action.APPWIDGET_UPDATE");
   	    	 int ids[] = AppWidgetManager.getInstance(getApplication()).getAppWidgetIds(new ComponentName(getApplication(), widget.class));
   	    upd.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS,ids);
   	    sendBroadcast(upd);
		
    	   // ���� � ������ 1 �������, �������� ��� ����������
    	   if ( Tasks.size() == 1 )
    	   {
    		   lvMain.performItemClick(lvMain, 0, lvMain.getItemIdAtPosition(0));
    	   }
    	   
    	   
           Toast.makeText(getApplicationContext(), "��������", Toast.LENGTH_SHORT).show();
          	
    }
    
    // ���� ������ �� add_task (��������� �������)
    if ( requestCode == 1 )
    {
    		Tasks.add(shorttext_s);
    		adapter.notifyDataSetChanged();  // ������� ��������, ��� � ��� ���������� ������
    		
    		 // ������� ������ ��� ������
    	     ContentValues cv = new ContentValues();
    	     // ������������ � ��
    	     SQLiteDatabase db = dbHelper.getWritableDatabase();
    	     
    	     //Cursor c = db.query("mytable", null, null, null, null, null, null);
    		
    	     // ���������� ������ ��� ������� � ���� ���: ������������ ������� - ��������
    	      cv.put("task_name", shorttext_s);
    	      
    	      // ��������� ������ � �������� �� ID
    	     db.insert("mytable", null, cv);
    	         		
    	     dbHelper.close();
    	
    		// �������� ��� ������� ����������
    		lvMain.performItemClick(lvMain, Tasks.size()-1, lvMain.getItemIdAtPosition(Tasks.size()-1));
    		
    		   Intent upd = new Intent(this, widget.class);
    	   	    upd.setAction("android.appwidget.action.APPWIDGET_UPDATE");
    	   	    	 int ids[] = AppWidgetManager.getInstance(getApplication()).getAppWidgetIds(new ComponentName(getApplication(), widget.class));
    	   	    upd.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS,ids);
    	   	    sendBroadcast(upd);
    		
    		Toast.makeText(getApplicationContext(), "���������. ����� �������: " + Tasks.size(), Toast.LENGTH_SHORT).show();
     }
    
    // ���� ������ �� ������� ������� ��������� ��� �� ������� ����� �������� ���������� ������� �� ������
    if ( requestCode == 0 )
    {
    		Tasks.add(shorttext_s);
    		adapter.notifyDataSetChanged();  // ������� ��������, ��� � ��� ���������� ������
    	
    		 // ������� ������ ��� ������
    	    ContentValues cv = new ContentValues();
    	     // ������������ � ��
    	     SQLiteDatabase db = dbHelper.getWritableDatabase();
    	     
    	     //Cursor c = db.query("mytable", null, null, null, null, null, null);
    		
    	     // ���������� ������ ��� ������� � ���� ���: ������������ ������� - ��������
    	      cv.put("task_name", shorttext_s);
    	      
    	      // ��������� ������
    	     db.insert("mytable", null, cv);
    		
    	     dbHelper.close();
    		
    		// �������� ��� ������� ����������
    		lvMain.performItemClick(lvMain, Tasks.size()-1, lvMain.getItemIdAtPosition(Tasks.size()-1));
    		
    		   Intent upd = new Intent(this, widget.class);
    	   	    upd.setAction("android.appwidget.action.APPWIDGET_UPDATE");
    	   	    	 int ids[] = AppWidgetManager.getInstance(getApplication()).getAppWidgetIds(new ComponentName(getApplication(), widget.class));
    	   	    upd.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS,ids);
    	   	    sendBroadcast(upd);
    		
    		Toast.makeText(getApplicationContext(), "���������. ����� �������: " + Tasks.size(), Toast.LENGTH_SHORT).show();
     }
    
   }
 
   
} // ��������� ����������� ������

