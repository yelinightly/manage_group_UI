package com.example.ipgroup;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class EditMemberActivity extends Activity implements OnClickListener {

	/** Properties **/
	protected EditText editmembername;
	protected EditText editmembernumber;
	protected Button updatememberButton;
	long id_member;
	String edit_m_gtag = "";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SysApplication.getInstance().addActivity(this);
		setContentView(R.layout.edit_member);
		
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		MemberData memberValue = (MemberData)bundle.getSerializable("memberDB_ID_EDIT");
		id_member = memberValue.get_member_id();
		
		editmembername = (EditText) findViewById(R.id.edit_member_name);
		editmembernumber = (EditText) findViewById(R.id.edit_member_number);
		updatememberButton = (Button) findViewById(R.id.update_member_button);
		
		editmembername.setText(memberValue.get_member_name());
		editmembernumber.setText(memberValue.get_member_number());
		edit_m_gtag = memberValue.get_member_grouptag();
		
		updatememberButton.setOnClickListener(this);

	}
	
	
	public boolean update_member_function() {
		// Read values from the interface
		String update_name_m_text = editmembername.getText().toString();
		String update_number_m_text = editmembernumber.getText().toString();
		String update_grouptag_m_text = "";   
		// Validate a name has been entered for the tea. If not, display
		// an AlertDialog informing the user.
		if(update_name_m_text.length() < 2) {
			AlertDialog.Builder dialog = new AlertDialog.Builder(this);
		    dialog.setTitle(R.string.invalid_member_title);
		    dialog.setMessage(R.string.invalid_member_name);
		    dialog.show();
		      
		    return false;
		}
		    
		MembersDB update_m_data = new MembersDB(this);
		update_m_data.update(update_name_m_text, update_number_m_text, edit_m_gtag, id_member);		
		
	    update_m_data.close();
	    Toast.makeText(EditMemberActivity.this, "已更新", Toast.LENGTH_SHORT).show();
		   
		return true;
	} 
	
	public void onClick(View v) {
		  updatememberButton.setPressed(true);
		  update_member_function();
		  finish();
	      Intent intent_main = new Intent(EditMemberActivity.this, MainActivity.class);
	      startActivity(intent_main); 
	  }
	
}

