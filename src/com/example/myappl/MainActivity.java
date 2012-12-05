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


	// определяем все элементы

	DBHelper dbHelper;
	
	// создаем объект для данных
    ContentValues cv;
 
	SQLiteDatabase db;
  	
	Cursor c;
	
	
    // add task
	Button button1;
	//edit task
	Button button2;
	
	// del task(s)
	Button button_del;
		
	// listview с заданиями
	ListView lvMain;
	
    // опредляем список заданий как список
    ArrayList<String> Tasks = new ArrayList<String>();

    // создаем адаптер
    ArrayAdapter<String> adapter;
	
	// edit shorttext - заголовок задания
	EditText shorttext;
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
     
        
     // создаем объект для создания и управления версиями БД
        dbHelper = new DBHelper(this);
    	
        
        
        //
        // находим все элементы
        // 
        
        lvMain = (ListView) findViewById(R.id.lvMain);
        // список с возможность единственного выбора - может быть выбран только один элемент
        lvMain.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        
        // определяем и настраиваем адаптер
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_single_choice, Tasks);
        //adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, Tasks);
        lvMain.setAdapter(adapter);
     
       
        button1 = (Button) findViewById(R.id.button1);
        button1.setOnClickListener(this);
        
        button2 = (Button) findViewById(R.id.button2);
        button2.setOnClickListener(this);
       
        button_del = (Button) findViewById(R.id.button_del);
        button_del.setOnClickListener(this);
     
        
        ///////  подключаемся и читаем из БД
        
   	 	
	     // подключаемся к БД
	     SQLiteDatabase db = dbHelper.getWritableDatabase();
	     Cursor c = db.query("mytable", null, null, null, null, null, null);
	     // ставим позицию курсора на первую строку выборки
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
        
        
        
      ///////// предлагаем создать задание при первом запуске программы (когда список пустой)
	  	if ( Tasks.size() == 0 )
    	{
	  		AlertDialog.Builder dialog_onstart = new AlertDialog.Builder(MainActivity.this);
	    	dialog_onstart.setTitle("Список пуст");
	    	dialog_onstart.setMessage("Добавить задание?");
	    	dialog_onstart.setPositiveButton("Да", new DialogInterface.OnClickListener() {
				
				public void onClick(DialogInterface dialog, int which) {
	    	
					Intent intent_onstart = new Intent(MainActivity.this, add_task.class);
		        	startActivityForResult(intent_onstart, 0);
					
				}
			});
	    	
	    	dialog_onstart.setNegativeButton("Нет", new DialogInterface.OnClickListener() {
				
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					Toast.makeText(getApplicationContext(), "Список пуст", Toast.LENGTH_SHORT).show();
				}
			});
	    	
	    	AlertDialog alertdialog = dialog_onstart.create();
	    	alertdialog.show();
	 	}
		////////////
        
        // показываем сколько всего заданий при включении
	  	else
	  	{
	  		Toast.makeText(getApplicationContext(), "Всего заданий: " + Tasks.size(), Toast.LENGTH_SHORT).show();
	  		// выбираем эту строчку программно
    		lvMain.performItemClick(lvMain, 0, lvMain.getItemIdAtPosition(0));
	  	}
   
    }
    
   

    class DBHelper extends SQLiteOpenHelper {

        public DBHelper(Context context) {
          // конструктор суперкласса
          super(context, "myDB", null, 1);
        }
        @Override
        public void onCreate(SQLiteDatabase db) {
          // создаем таблицу с полями
          db.execSQL( "create table mytable (id integer primary key autoincrement, task_name text);" );
        }
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub
		}}
    
    
 
    // обработка нажатия кнопок
	public void onClick(View v) {
	        switch (v.getId()) {
	        
	        case R.id.button1: // кнопка добавить задание
	        	
	        	Intent intent = new Intent(this, add_task.class);
	        	startActivityForResult(intent, 1);
	        	
	        break;
	       
	        case R.id.button2: // кнопка изменить задание
	        	if ( Tasks.isEmpty() )
	        	{
	        		Toast.makeText(getApplicationContext(), "Добавьте задание", Toast.LENGTH_SHORT).show();
	        		return;
	        	}
	        	
	        	Intent intent2 = new Intent(this, edit_task.class);
	        	intent2.putExtra("string", Tasks.get( lvMain.getCheckedItemPosition() ).toString() );  // получаем выделенный
	        	//элемент, преобразуем в строчку и отправляем в edit_task 
	        	startActivityForResult(intent2, 2);
	        	
	        break;
	       
	        case R.id.button_del: // кнопка удалить задание
	        	obd_del();
	
	        	break;
	       	
	        default:
	        		break;
	           }
	        }
	    
   
	
 // обработка кнопки удаления заданий - диалог
    private void obd_del()
    {
    	if ( Tasks.isEmpty() )
    	{
    		Toast.makeText(getApplicationContext(), "Добавьте задание", Toast.LENGTH_SHORT).show();
    		return;
    	}
    	
    	else
    	{
    	AlertDialog.Builder dbi = new AlertDialog.Builder(this);
    	dbi.setTitle("Удаление");
    	dbi.setMessage("Удалить задание?");
    	dbi.setPositiveButton("Да", new DialogInterface.OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				
				// удаляем выделенное задание из списка
				Tasks.remove(lvMain.getCheckedItemPosition());
				
				 // создаем объект для данных
	    	    ContentValues cv = new ContentValues();
	    	     // подключаемся к БД
	    	     SQLiteDatabase db = dbHelper.getWritableDatabase();
	    	     
	    	    Cursor c = db.query("mytable", null, null, null, null, null, null);
	    	 
	    	    // встаем на позицию которую нужно удалить
	    	    c.moveToPosition(lvMain.getCheckedItemPosition());
	    	  
	    	    // получаем индекс
	    	    int idColIndex = c.getColumnIndex("id");
		        int ID = c.getInt(idColIndex); 
	    	  
	    	    // удаляем запись из бд	    	  	    	    
	    	    db.delete("mytable", "id = " + ID, null);
	    	    c.close();
	    	    
				
	    	    // говорим адаптеру, что изменились данные
	    	    adapter.notifyDataSetChanged();

				dbHelper.close();
				
				   // если в списке 1 элемент, выбираем его специально
		    	   if ( Tasks.size() == 1 )
		    	   {
		    		   lvMain.performItemClick(lvMain, 0, lvMain.getItemIdAtPosition(0));
		    		   
		    	   }
		    	   
		    		// выбираем строчку программно
		    	   else
		    		   {
		    		   lvMain.performItemClick(lvMain, Tasks.size()-1, lvMain.getItemIdAtPosition(Tasks.size()-1));
		    		   }
				
				
				///////// если удалили последнее задание, предлагаем добавить новое
			  	if ( Tasks.size() == 0 )
	        	{
			  		AlertDialog.Builder dbi2 = new AlertDialog.Builder(MainActivity.this);
			    	dbi2.setTitle("Список пуст");
			    	dbi2.setMessage("Список пуст, добавить задание?");
			    	dbi2.setPositiveButton("Да", new DialogInterface.OnClickListener() {
						
						public void onClick(DialogInterface dialog, int which) {
			    	
							Intent intent_dialog = new Intent(MainActivity.this, add_task.class);
				        	startActivityForResult(intent_dialog, 0);
							
						}
					});
			    	
			    	dbi2.setNegativeButton("Нет", new DialogInterface.OnClickListener() {
						
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							Toast.makeText(getApplicationContext(), "Список пуст", Toast.LENGTH_SHORT).show();
							
						}
					});
			    	
			    	AlertDialog alertdialog = dbi2.create();
			    	alertdialog.show();
			 	}
				////////////
			  	
		
				// всплывающее сообщение
				Toast.makeText(getApplicationContext(), "Удалено. Всего заданий: " + Tasks.size(), Toast.LENGTH_SHORT).show();
				
			}
		});
    	// если в диалоге нажата клавиша "нет"
    	dbi.setNegativeButton("Нет", new DialogInterface.OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
							
			}
		});
    	
    	AlertDialog alertdialog = dbi.create();
        	alertdialog.show();
    }}
      
      
    // передача заголовка(задания) в мейн-активити для создания поля(задания) в листвью. Приходит либо от add_task либо от edit_task
   @Override
   protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    String shorttext_s = data.getStringExtra("shorttext");
    
    // если пришло от edit_task (изменяли задание)
    if ( requestCode == 2 )
    {
    	   Tasks.set(lvMain.getCheckedItemPosition(), shorttext_s); // изменяем запись
    	   
    	// создаем объект для данных
   	    ContentValues cv = new ContentValues();
   	    // подключаемся к БД
   	    SQLiteDatabase db = dbHelper.getWritableDatabase();
   	    Cursor c = db.query("mytable", null, null, null, null, null, null);
   	    // встаем на позицию которую нужно изменить
   	    c.moveToPosition(lvMain.getCheckedItemPosition());
   	    // получаем индекс
   	    int idColIndex = c.getColumnIndex("id");
	    int ID = c.getInt(idColIndex); 
   	    // изменяем запись в бд
	    cv.put("task_name", shorttext_s);
   	    db.update("mytable", cv, "id = " + ID, null);
	    //db.delete("mytable", "id = " + ID, null);
   	    c.close();
   	    // говорим адаптеру, что изменились данные
   	    adapter.notifyDataSetChanged();
		dbHelper.close();
    	   
    	   Intent upd = new Intent(this, widget.class);
   	    upd.setAction("android.appwidget.action.APPWIDGET_UPDATE");
   	    	 int ids[] = AppWidgetManager.getInstance(getApplication()).getAppWidgetIds(new ComponentName(getApplication(), widget.class));
   	    upd.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS,ids);
   	    sendBroadcast(upd);
		
    	   // если в списке 1 элемент, выбираем его специально
    	   if ( Tasks.size() == 1 )
    	   {
    		   lvMain.performItemClick(lvMain, 0, lvMain.getItemIdAtPosition(0));
    	   }
    	   
    	   
           Toast.makeText(getApplicationContext(), "Изменено", Toast.LENGTH_SHORT).show();
          	
    }
    
    // если пришло от add_task (добавляли задание)
    if ( requestCode == 1 )
    {
    		Tasks.add(shorttext_s);
    		adapter.notifyDataSetChanged();  // говорим адаптеру, что у нас изменились данные
    		
    		 // создаем объект для данных
    	     ContentValues cv = new ContentValues();
    	     // подключаемся к БД
    	     SQLiteDatabase db = dbHelper.getWritableDatabase();
    	     
    	     //Cursor c = db.query("mytable", null, null, null, null, null, null);
    		
    	     // подготовим данные для вставки в виде пар: наименование столбца - значение
    	      cv.put("task_name", shorttext_s);
    	      
    	      // вставляем запись и получаем ее ID
    	     db.insert("mytable", null, cv);
    	         		
    	     dbHelper.close();
    	
    		// выбираем эту строчку программно
    		lvMain.performItemClick(lvMain, Tasks.size()-1, lvMain.getItemIdAtPosition(Tasks.size()-1));
    		
    		   Intent upd = new Intent(this, widget.class);
    	   	    upd.setAction("android.appwidget.action.APPWIDGET_UPDATE");
    	   	    	 int ids[] = AppWidgetManager.getInstance(getApplication()).getAppWidgetIds(new ComponentName(getApplication(), widget.class));
    	   	    upd.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS,ids);
    	   	    sendBroadcast(upd);
    		
    		Toast.makeText(getApplicationContext(), "Добавлено. Всего заданий: " + Tasks.size(), Toast.LENGTH_SHORT).show();
     }
    
    // если пришло из первого запуска программы или из диалога после удаления последнего задания из списка
    if ( requestCode == 0 )
    {
    		Tasks.add(shorttext_s);
    		adapter.notifyDataSetChanged();  // говорим адаптеру, что у нас изменились данные
    	
    		 // создаем объект для данных
    	    ContentValues cv = new ContentValues();
    	     // подключаемся к БД
    	     SQLiteDatabase db = dbHelper.getWritableDatabase();
    	     
    	     //Cursor c = db.query("mytable", null, null, null, null, null, null);
    		
    	     // подготовим данные для вставки в виде пар: наименование столбца - значение
    	      cv.put("task_name", shorttext_s);
    	      
    	      // вставляем запись
    	     db.insert("mytable", null, cv);
    		
    	     dbHelper.close();
    		
    		// выбираем эту строчку программно
    		lvMain.performItemClick(lvMain, Tasks.size()-1, lvMain.getItemIdAtPosition(Tasks.size()-1));
    		
    		   Intent upd = new Intent(this, widget.class);
    	   	    upd.setAction("android.appwidget.action.APPWIDGET_UPDATE");
    	   	    	 int ids[] = AppWidgetManager.getInstance(getApplication()).getAppWidgetIds(new ComponentName(getApplication(), widget.class));
    	   	    upd.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS,ids);
    	   	    sendBroadcast(upd);
    		
    		Toast.makeText(getApplicationContext(), "Добавлено. Всего заданий: " + Tasks.size(), Toast.LENGTH_SHORT).show();
     }
    
   }
 
   
} // последняя закрывающая скобка

