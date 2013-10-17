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
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;
import android.widget.AdapterView.AdapterContextMenuInfo;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import java.util.ArrayList;
import java.util.HashMap;
import android.widget.TextView;

public class ManageGroupsActivity extends SherlockActivity {

	//Button button;
	private static final String TAG = "ManageGroupsActivity";
	String itemid ="";
	int i=0;
	int GroupDB_count = 0;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SysApplication.getInstance().addActivity(this);
		setContentView(R.layout.manage_groups);
	
		ListView listView = (ListView) this.findViewById(android.R.id.list);  
		GroupsDB group_info = new GroupsDB(this);
		GroupDB_count =(int)group_info.count();
		System.out.println("GroupDB length is " + GroupDB_count);
		Cursor cursor = group_info.all(this);
//		SimpleCursorAdapter groupCursorAdapter = new SimpleCursorAdapter(
//			this,
//	    	R.layout.list_g_layout,
//	    	cursor,
//	    	new String[] { GroupsDB.NAME, GroupsDB.NUMBER},
//	    	new int[] { R.id.name_list_G, R.id.number_list_G}
//	    	);
//		listView.setAdapter(groupCursorAdapter);
		String[] mFrom = new String[]{"IMG", GroupsDB.NAME, GroupsDB.NUMBER};  
		int[] mTo = new int[]{R.id.groupimageview, R.id.name_list_G, R.id.number_list_G};  
		ArrayList<HashMap<String,Object>> mList = new ArrayList<HashMap<String,Object>>();  
		HashMap<String,Object> mMap = null;  
    	while(cursor.moveToNext()){  
            String g_name = cursor.getString(cursor.getColumnIndex("name"));  
            String g_number = cursor.getString(cursor.getColumnIndex("number"));
//            System.out.println("GroupDB length is " + g_name+"----"+g_number);
		    mMap = new HashMap<String,Object>();
		    mMap.put("IMG", R.drawable.ic_menu_members); 		     
		    mMap.put(GroupsDB.NAME, g_name);  
		    mMap.put(GroupsDB.NUMBER, g_number);
		    
		    mList.add(mMap);  
		}  
        SimpleAdapter adapter = new SimpleAdapter(
        		this,
        		mList,
        		R.layout.list_g_layout,
        		mFrom,
        		mTo); 
        listView.setAdapter(adapter);
		
		GroupsDB group_db = new GroupsDB(this);
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

	        public void onItemClick(AdapterView<?> parentAdapter, View view, int position, long id) {
				String list_item_name = ((TextView) view.findViewById(R.id.name_list_G)).getText().toString();
				System.out.println("ClickItemName is " + list_item_name);
				Bundle bundle_tagid = new Bundle();
		  		bundle_tagid.putSerializable("GROUPTAG_ID", list_item_name);
	        	Intent intent_m_in_g = new Intent(ManageGroupsActivity.this, MembersinGroupActivity.class);
	        	intent_m_in_g.putExtras(bundle_tagid);
	        	startActivity(intent_m_in_g);
	            
//	            setTitle("点击第"+id+"个项目");
//	            Toast.makeText(ManageGroupsActivity.this, "Item with id ["+id+"] - Position ["+position+"]", Toast.LENGTH_SHORT).show();

	        }
	   });
		
		
	    listView.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {  
	        
	        @Override  
	        public void onCreateContextMenu(ContextMenu menu, View v,ContextMenuInfo menuInfo) {  
	            menu.setHeaderTitle("Member Options");
	            menu.add(0, 0, 0, "Edit");
	            menu.add(0, 1, 0, "Delete");  
	            menu.add(0, 2, 0, "Details");   
	        }  
	    }); 
	}
	
	@Override  
	public boolean onContextItemSelected(android.view.MenuItem item) {
		GroupData group_data = new GroupData();
		GroupsDB group_db = new GroupsDB(this);
		MembersDB m_db_del = new MembersDB(this);
		AdapterContextMenuInfo menuInfo = (AdapterContextMenuInfo) item.getMenuInfo();
		
		switch(item.getItemId()) {
		  	case 0:		//Edit
//		  		group_db.ask(group_data, menuInfo.id);  		
//		  		group_data.set_group_id(menuInfo.id);
		  		String menulistname_edt = ((TextView)menuInfo.targetView.findViewById(R.id.name_list_G)).getText().toString();
		  		String menulistnumber_edt = ((TextView)menuInfo.targetView.findViewById(R.id.number_list_G)).getText().toString();
		  		long menulistgroup_id_edt = group_db.check_id(menulistname_edt);
//		  		System.out.println("****** group_id is"+ String.valueOf(menulistgroup_id_edt));
		  		group_data.set_group_name(menulistname_edt);
		  		group_data.set_group_number(menulistnumber_edt);
		  		group_data.set_group_gid(menulistgroup_id_edt);
		  		
		  		Bundle bundle= new Bundle();
				bundle.putSerializable("groupDB_ID", group_data);
				
				Intent intent_edit=new Intent(this,EditGroupActivity.class);
				intent_edit.putExtras(bundle);
				startActivity(intent_edit);
		  		break;
		  	
		  	case 1:		//Delete
		  		String menulistname_del = ((TextView)menuInfo.targetView.findViewById(R.id.name_list_G)).getText().toString();
		  		group_db.delete_by_name(menulistname_del);
		  		Cursor cursor_del = m_db_del.all(this);
		  		while(cursor_del.moveToNext())
		  		{
		  			String name_del = cursor_del.getString(cursor_del.getColumnIndex("name"));
		  			String number_del = cursor_del.getString(cursor_del.getColumnIndex("number"));
		  			String tag_del = cursor_del.getString(cursor_del.getColumnIndex("tag"));
		  			String id_del = cursor_del.getString(cursor_del.getColumnIndex("_id"));
		  			if(tag_del.equals(menulistname_del))
		  				tag_del = "";
		  			m_db_del.update(name_del, number_del, tag_del, Integer.parseInt(id_del));
		  		}
//		        finish();  
//		        Intent intent_refresh = new Intent(ManageGroupsActivity.this, ManageGroupsActivity.class);  
//		        startActivity(intent_refresh); 
		        onCreate(null);
		  		break;
		  		
		  	case 2:		//Details
		  		
		  		break;
		  		
			default:
				break;
		  }
		return super.onContextItemSelected(item); 
	}

	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		getSupportMenuInflater().inflate(R.menu.main_groups, menu);
		return super.onCreateOptionsMenu(menu);

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		final Context context = this;
		
		switch (item.getItemId()) {

		case R.id.g_menu_add:
	    	Intent intent_addgroups = new Intent(context, AddGroupsActivity.class);
	    	startActivity(intent_addgroups);  
			break;

		case R.id.g_menu_back:
	    	Intent intent_back = new Intent(context, MainActivity.class);
	    	startActivity(intent_back); 
			break;
		
		case R.id.g_menu_exit:
			SysApplication.getInstance().exit();
			break;
		
		default:
			break;
		}

		return super.onOptionsItemSelected(item); 
	}

}