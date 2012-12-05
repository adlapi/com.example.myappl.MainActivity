package com.example.myappl;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.view.View.OnClickListener;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;



public class edit_task extends Activity implements OnClickListener  {

	// cancel button
	Button button7;
	
	// ok button
	Button button6;
	
	// edit shorttext - заголовок задания
	EditText shorttext_edit;
	EditText shorttext;
	
	ListView lvMain;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_task);
        
        
        shorttext_edit = (EditText) findViewById(R.id.shorttext_edit);
        
           shorttext = (EditText) findViewById(R.id.shorttext);
        
         
        button6 = (Button) findViewById(R.id.button6);
        button6.setOnClickListener(this);
        
        button7 = (Button) findViewById(R.id.button7);
        button7.setOnClickListener(this);
    
        shorttext_edit.setLines(6);
          
       
        // получаем данные из интента из мейн-активити и сеттекстим в едит-поле для редактирования 
        
        Intent intent0 = getIntent();
        String string_s = intent0.getStringExtra("string");
        shorttext_edit.setText(string_s);
        
   }
        

    // обработка кнопок
    public void onClick(View v)
    {
    switch (v.getId()) 
    {
    case R.id.button7: // cancel
    	finish();
    	break;
    	
    case R.id.button6: // ok
    	
    		// запрещаем при редактировании оставлять пустой заголовок
    		if ( shorttext_edit.length() == 0 )
   	     	{
    			 Toast.makeText(getApplicationContext(), "Введите заголовок", Toast.LENGTH_SHORT).show();
   	    	 return;
   	     	}
    		
    		Intent intent2 = new Intent();
    	    intent2.putExtra("shorttext", shorttext_edit.getText().toString());
    	    setResult(RESULT_OK, intent2);
    	    
    	    finish();
      break;
      
      default:
    	  break;
    }}
  

} // последняя закрывающая скобка
