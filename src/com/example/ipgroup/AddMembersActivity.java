package com.example.ipgroup;

import android.app.Activity;
import android.os.Bundle;
import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.view.View.OnClickListener;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class AddMembersActivity extends Activity implements OnClickListener {
	private static final String TAG = "AddMemberActivity";
	
	/** Properties **/
	protected EditText user_name;
	protected EditText user_number;
	protected Button user_save_button;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SysApplication.getInstance().addActivity(this);
		setContentView(R.layout.add_members);
		
	    // Connect interface elements to properties
	    user_name = (EditText) findViewById(R.id.member_name);
	    user_number = (EditText) findViewById(R.id.member_number);
	    user_save_button = (Button) findViewById(R.id.member_save_button);

	    // Setup Listeners
	    user_save_button.setOnClickListener(this);
	}
	
	
	@Override
	public void onClick(View v) {
		user_save_button.setPressed(true);
	
		if(save_member_data()) {
			user_name.setText("");
			user_number.setText("");
		}
		finish();
    	Intent intent_main = new Intent(AddMembersActivity.this, MainActivity.class);
    	startActivity(intent_main); 
	}
	
	public boolean save_member_data() {
		// Read values from the interface
		String user_name_text = user_name.getText().toString();
		String user_number_text = user_number.getText().toString();
		String user_grouptag_text = "";    
		// Validate a name has been entered for the tea. If not, display
		// an AlertDialog informing the user.
		if(user_name_text.length() < 2) {
			AlertDialog.Builder dialog = new AlertDialog.Builder(this);
		    dialog.setTitle(R.string.invalid_member_title);
		    dialog.setMessage(R.string.invalid_member_name);
		    dialog.show();
		      
		    return false;
		}
		    
		// The tea is valid, so connect to the tea database and insert the tea
		MembersDB user_data = new MembersDB(this);
		user_data.insert(user_name_text, user_number_text, user_grouptag_text);
		user_data.close();
		Toast.makeText(AddMembersActivity.this, "已保存", Toast.LENGTH_SHORT).show(); 
		   
		return true;
	}
	
}
