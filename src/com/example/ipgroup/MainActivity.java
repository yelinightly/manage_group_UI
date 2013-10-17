package com.example.ipgroup;


import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Context;
import android.database.Cursor;
import android.view.ContextMenu;
//import android.view.Menu;
//import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnCreateContextMenuListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;
import android.widget.AdapterView.AdapterContextMenuInfo;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;


public class MainActivity extends SherlockActivity {
	private static final String TAG = "MainActivity";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SysApplication.getInstance().addActivity(this);
		setContentView(R.layout.activity_main);
		
		ListView listView = (ListView) this.findViewById(android.R.id.list);  
		MembersDB member_info = new MembersDB(this);
		Cursor cursor = member_info.all(this);
	  
		SimpleCursorAdapter memberCursorAdapter = new SimpleCursorAdapter(
			this,
	    	R.layout.list_m_layout,
	    	cursor,
	    	new String[] { MembersDB.NAME, MembersDB.NUMBER, MembersDB.GROUP_TAG },
//	    	new String[] { MembersDB.NAME, MembersDB.NUMBER },
	    	new int[] { R.id.name_list, R.id.number_list, R.id.grouptag_list }
//			new int[] { R.id.name_list, R.id.number_list }
	    	);
		listView.setAdapter(memberCursorAdapter);	
		
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parentAdapter, View view, int position, long id) {
            	AlertDialog.Builder builderSingle = new AlertDialog.Builder(MainActivity.this);
                builderSingle.setTitle("Options");
                final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                		MainActivity.this,
                        android.R.layout.select_dialog_singlechoice);
                arrayAdapter.add("Call");
                arrayAdapter.add("Message");

                builderSingle.setAdapter(arrayAdapter,
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String str_item= arrayAdapter.getItem(which);
                            	Toast.makeText(getBaseContext(), str_item, Toast.LENGTH_SHORT).show();
                            }
                        });
                builderSingle.show();
            }
       });
	    
	    listView.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {  
	        
	        @Override  
	        public void onCreateContextMenu(ContextMenu menu, View v,ContextMenuInfo menuInfo) {  
	            menu.setHeaderTitle("Member Options");
	            menu.add(0, 0, 0, "Add to Group");
	            menu.add(0, 1, 0, "Edit"); 
	            menu.add(0, 2, 0, "Delete");
	            menu.add(0, 3, 0, "Details");
	        }  
	    }); 
	}

	
	@Override  
	public boolean onContextItemSelected(android.view.MenuItem item) {
		MemberData member_data = new MemberData();
		MembersDB member_db = new MembersDB(this);
		AdapterContextMenuInfo menuInfo = (AdapterContextMenuInfo) item.getMenuInfo();
		  switch(item.getItemId()) {		
		  	case 0:		//Add to Group
		  		member_db.ask(member_data, menuInfo.id);  		
		  		member_data.set_member_id(menuInfo.id);

		  		Bundle bundle_a= new Bundle();
				bundle_a.putSerializable("memberDB_ID_ATOG", member_data);
				
				Intent intent_addtogroup=new Intent(this,AddtoGroupActivity.class);
				intent_addtogroup.putExtras(bundle_a);
				startActivity(intent_addtogroup);
				break;
		  	case 1:		//Edit
		  		member_db.ask(member_data, menuInfo.id);  		
		  		member_data.set_member_id(menuInfo.id);

		  		Bundle bundle= new Bundle();
				bundle.putSerializable("memberDB_ID_EDIT", member_data);
				
				Intent intent_edit=new Intent(this,EditMemberActivity.class);
				intent_edit.putExtras(bundle);
				startActivity(intent_edit);
		  		break;
		  	case 2:		//Delete
		  		member_db.delete(menuInfo.id);
		  		onCreate(null);
				break;
		  	case 3:		//Details

		  		break;
		  		
			default:
				break;
		  }
		return super.onContextItemSelected(item); 
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		
		getSupportMenuInflater().inflate(R.menu.main_members, menu);
		return super.onCreateOptionsMenu(menu);

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		final Context context = this;
		
		switch (item.getItemId()) {

		case R.id.m_menu_invite:
	    	Intent intent_addmembers = new Intent(context, AddMembersActivity.class);
	    	startActivity(intent_addmembers);  
			break;

		case R.id.m_menu_forward:
	    	Intent intent_managegroups = new Intent(context, ManageGroupsActivity.class);
	    	startActivity(intent_managegroups); 
			break;
			
		case R.id.m_menu_exit:
			SysApplication.getInstance().exit();
			break;
		default:
			break;
		}

		return super.onOptionsItemSelected(item);  

	}

}
