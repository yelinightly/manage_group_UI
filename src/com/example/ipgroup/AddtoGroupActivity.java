package com.example.ipgroup;


import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class AddtoGroupActivity extends Activity implements OnClickListener, OnItemSelectedListener {

	protected Spinner groupsSpinner;
	protected Button user_add_to_group_button;
	protected EditText membername_atog;
	protected EditText membernumber_atog;

	long id_member;
	protected GroupsDB groupsdb;

	String tag_m_name;
	String tag_m_number;
	String member_tag_added;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SysApplication.getInstance().addActivity(this);
		setContentView(R.layout.add_to_group);
		
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		MemberData memberValue = (MemberData)bundle.getSerializable("memberDB_ID_ATOG");
		id_member = memberValue.get_member_id();
		
		membername_atog = (EditText) findViewById(R.id.memname_atog);
		membernumber_atog = (EditText) findViewById(R.id.memnumber_atog);
		
		membername_atog.setText(memberValue.get_member_name());
		membernumber_atog.setText(memberValue.get_member_number());
		
		
		user_add_to_group_button = (Button) findViewById(R.id.addtogroup_button);
		groupsSpinner = (Spinner) findViewById(R.id.group_spinner);
		
		user_add_to_group_button.setOnClickListener(this);

		groupsdb = new GroupsDB(this);
        Cursor cursor = groupsdb.all(this);
        
        SimpleCursorAdapter groupsCursorAdapter = new SimpleCursorAdapter(
          this,
          android.R.layout.simple_spinner_item,
          cursor,
          new String[] { GroupsDB.NAME },
          new int[] { android.R.id.text1 }
        );
    
        groupsCursorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        
        // Finally, connect the Tea Spinner to the TeaData cursor.
        groupsSpinner.setAdapter(groupsCursorAdapter);
        groupsSpinner.setOnItemSelectedListener(this);
	
	}
	
	public void set_group_tag(String g_tag) {
		Toast.makeText(this, g_tag, Toast.LENGTH_SHORT).show();
		member_tag_added = g_tag;

	}
	
	
    public void onClick(View v) {
		tag_m_name = membername_atog.getText().toString();
		tag_m_number = membernumber_atog.getText().toString();
    	user_add_to_group_button.setPressed(true);	
		MembersDB update_m_data = new MembersDB(this);
		update_m_data.update(tag_m_name, tag_m_number, member_tag_added, id_member);		
	    update_m_data.close();
	    member_tag_added = "";
	    Toast.makeText(this, "已添加到组", Toast.LENGTH_SHORT).show();
	    finish();
    	Intent intent_main = new Intent(AddtoGroupActivity.this, MainActivity.class);
    	startActivity(intent_main); 
    }
	
    
    /* (non-Javadoc)
     * @see android.widget.AdapterView.OnItemSelectedListener#onItemSelected(android.widget.AdapterView, android.view.View, int, long)
     */
    public void onItemSelected(AdapterView<?> spinner, View view, int position, long id) {
       if(spinner == groupsSpinner) {
            Cursor cursor = (Cursor) spinner.getSelectedItem();
            set_group_tag(cursor.getString(1));
       }
    }

    /* (non-Javadoc)
     * @see android.widget.AdapterView.OnItemSelectedListener#onNothingSelected(android.widget.AdapterView)
     */
    public void onNothingSelected(AdapterView<?> spinner) {
    }
}
	
	
	
