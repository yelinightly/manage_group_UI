package com.example.ipgroup;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class EditGroupActivity extends Activity implements OnClickListener{

	/** Properties **/
	protected EditText editgroupname;
	protected EditText editgroupnumber;
	protected Button updategroupButton;
	String id_g_name = "";
	String id_g_number = "";
    long id_g = 0;
	  
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SysApplication.getInstance().addActivity(this);
		setContentView(R.layout.edit_group);
		
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		GroupData groupValue = (GroupData)bundle.getSerializable("groupDB_ID");
		id_g_name = groupValue.get_group_name();
		id_g_number = groupValue.get_group_number();
		id_g = groupValue.get_group_gid(); 
		
		editgroupname = (EditText) findViewById(R.id.edit_group_name);
		editgroupnumber = (EditText) findViewById(R.id.edit_group_number);
		updategroupButton = (Button) findViewById(R.id.update_group_button);
		
		editgroupname.setText(id_g_name);
		editgroupnumber.setText(id_g_number);
		
		updategroupButton.setOnClickListener(this);
	}
	
	
	public boolean update_group_function() {
		// Read values from the interface
		String update_name_g_text = editgroupname.getText().toString();
		String update_number_g_text = editgroupnumber.getText().toString();
		    
		// Validate a name has been entered for the tea. If not, display
		// an AlertDialog informing the user.
		if(update_name_g_text.length() < 2) {
			AlertDialog.Builder dialog = new AlertDialog.Builder(this);
		    dialog.setTitle(R.string.invalid_group_title);
		    dialog.setMessage(R.string.invalid_group_name);
		    dialog.show();
		      
		    return false;
		}
		    
		GroupsDB update_g_data = new GroupsDB(this);
		MembersDB update_m_data = new MembersDB(this);
		
		update_g_data.update(update_name_g_text, update_number_g_text, id_g);	
		update_g_data.close();
		
  		Cursor cursor_u = update_m_data.all(this);
  		while(cursor_u.moveToNext())
  		{
  			String name_u = cursor_u.getString(cursor_u.getColumnIndex("name"));
  			String number_u = cursor_u.getString(cursor_u.getColumnIndex("number"));
  			String tag_u = cursor_u.getString(cursor_u.getColumnIndex("tag"));
  			String id_u = cursor_u.getString(cursor_u.getColumnIndex("_id"));
  			if(tag_u.equals(id_g_name))
  				update_m_data.update(name_u, number_u, update_name_g_text, Integer.parseInt(id_u));
  		}
		Toast.makeText(EditGroupActivity.this, "已更新", Toast.LENGTH_SHORT).show();   
		return true;
	} 
	
	public void onClick(View v) {
		  updategroupButton.setPressed(true);
		  update_group_function();
//		  finish();
		  Intent intent_managegroups = new Intent(EditGroupActivity.this, ManageGroupsActivity.class);
		  startActivity(intent_managegroups); 
	  }
}

