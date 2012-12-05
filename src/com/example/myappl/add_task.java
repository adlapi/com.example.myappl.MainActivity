package com.example.myappl;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;



public class add_task extends Activity implements OnClickListener  {

	// cancel button
	Button button5;
	
	// add button
	Button button4;
	
	// edit shorttext - заголовок задания
	EditText shorttext;

	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_task);
        
        shorttext = (EditText) findViewById(R.id.shorttext);
        
        button4 = (Button) findViewById(R.id.button4);
        button4.setOnClickListener(this);
        
        button5 = (Button) findViewById(R.id.button5);
        button5.setOnClickListener(this);
    
       shorttext.setLines(6);
               
        // подсказка при создании
        shorttext.setHint("Добавить задание...");
       
    }

    
    
    // обработка кнопок
    public void onClick(View v)
    {
     	
    switch (v.getId()) 
    {
    case R.id.button5: //cancel
    	finish();
    	break;
    	
    case R.id.button4: //apply
    	// запрещаем добавлять пустые задания
    	if ( shorttext.length() == 0 )
	     {
    		Toast.makeText(getApplicationContext(), "Введите заголовок", Toast.LENGTH_SHORT).show();
	    	 return;
	     }
    	
    	Intent intent2 = new Intent();
    	intent2.putExtra("shorttext", shorttext.getText().toString());
    	
   
    	finish();
      break;

    default:
    	  break;
    }
    
    }
    

}
