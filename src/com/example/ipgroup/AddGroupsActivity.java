package com.example.ipgroup;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddGroupsActivity extends Activity implements OnClickListener {
	//Button button;
	private static final String TAG = "AddGroupsActivity";
	
	
	/** Properties **/
	protected EditText user_group_name;
	protected EditText user_group_number;
	protected Button user_group_save_button;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SysApplication.getInstance().addActivity(this);
		setContentView(R.layout.add_groups);
		
    // Connect interface elements to properties
	user_group_name = (EditText) findViewById(R.id.group_name);
	user_group_number = (EditText) findViewById(R.id.group_number);
    user_group_save_button = (Button) findViewById(R.id.group_save_button);
    
    // Setup Listeners
    user_group_save_button.setOnClickListener(this);
}


@Override
public void onClick(View v) {
	user_group_save_button.setPressed(true);

	if(save_member_data()) {
		user_group_name.setText("");
		user_group_number.setText("");
	}
//	finish();	    	
	Intent intent_managegroups = new Intent(AddGroupsActivity.this, ManageGroupsActivity.class);
	startActivity(intent_managegroups); 
}

public boolean save_member_data() {
	// Read values from the interface
	String groupname_text = user_group_name.getText().toString();
	String groupnumber_text = user_group_number.getText().toString();
	    
	// Validate a name has been entered for the tea. If not, display
	// an AlertDialog informing the user.
	if(groupname_text.length() < 2) {
		AlertDialog.Builder dialog = new AlertDialog.Builder(this);
	    dialog.setTitle(R.string.invalid_group_title);
	    dialog.setMessage(R.string.invalid_group_name);
	    dialog.show();
	      
	    return false;
	}
	    
	// The tea is valid, so connect to the tea database and insert the tea
	GroupsDB group_data = new GroupsDB(this);
	group_data.insert(groupname_text, groupnumber_text);
	group_data.close();
	Toast.makeText(AddGroupsActivity.this, "已保存", Toast.LENGTH_SHORT).show(); 
	  
	return true;
}

}