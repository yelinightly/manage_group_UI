package com.example.ipgroup;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnCreateContextMenuListener;
import android.widget.Toast;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.ArrayAdapter;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.app.Dialog;
import java.util.ArrayList;
import java.util.HashMap;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.BaseAdapter;
import android.view.LayoutInflater;
import android.view.ViewGroup;

public class MembersinGroupActivity extends SherlockActivity {

	String tag_value = "";

	MemberData m_data = new MemberData();
	MembersDB  m_db = new MembersDB(this);
	MemberData [] m_in_g_data = new MemberData[10];
	int i=0;
	Dialog Pop_Dialog;
	 
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SysApplication.getInstance().addActivity(this);
		setContentView(R.layout.members_in_group);
		
		ListView listView = (ListView) this.findViewById(android.R.id.list); 
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();

		tag_value = (String)bundle.getSerializable("GROUPTAG_ID");
        System.out.println("value is " + tag_value);
        m_in_g_data = m_db.checktagsout(tag_value);
//        System.out.println("array length is " + m_in_g_data.length);
//        for(i = 0; i< m_in_g_data.length; i++)
//        {
//        	if(m_in_g_data[i] == null)
//        		break;
//        	System.out.println("Query------->" + String.valueOf(i)
//        									   + "姓名："+m_in_g_data[i].get_member_name()+" "
//        		                               + "号码："+m_in_g_data[i].get_member_number()+" "
//        		                               + "组名："+m_in_g_data[i].get_member_grouptag());
//        }
        ArrayList< HashMap< String, Object>> data = new ArrayList< HashMap< String, Object>>();
        for(int i=0;i<10;i++)  
        {  
        	if(m_in_g_data[i] == null)
        		break;
        	HashMap<String, Object> map = new HashMap<String, Object>();  
            map.put("name", m_in_g_data[i].get_member_name());
            map.put("number", m_in_g_data[i].get_member_number());  
            map.put("group", m_in_g_data[i].get_member_grouptag());  
            data.add(map);  
        } 
        
        SimpleAdapter adapter = new SimpleAdapter(
        		this,
        		data,
        		R.layout.list_m_layout,
        		new String[]{"name", "number", "group"},
        		new int[]{R.id.name_list, R.id.number_list, R.id.grouptag_list}); 
        listView.setAdapter(adapter);
        
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parentAdapter, View view, int position, long id) {
            	AlertDialog.Builder builderSingle = new AlertDialog.Builder(MembersinGroupActivity.this);
                builderSingle.setTitle("Options");
                final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                		MembersinGroupActivity.this,
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
	            menu.add(0, 0, 0, "Delete");
	            menu.add(0, 1, 0, "Details");
	        }  
	    });
	}

	@Override  
	public boolean onContextItemSelected(android.view.MenuItem item) {
		MemberData member_data_del_g = new MemberData();
		MembersDB member_db_del_g = new MembersDB(this);
		AdapterContextMenuInfo menuInfo = (AdapterContextMenuInfo) item.getMenuInfo();
		  switch(item.getItemId()) {
		  	case 0:		//Delete
		  		String menulistname = ((TextView)menuInfo.targetView.findViewById(R.id.name_list)).getText().toString();
		  		String menulistnumber = ((TextView)menuInfo.targetView.findViewById(R.id.number_list)).getText().toString();
		  		String menulistgrouptag = ((TextView)menuInfo.targetView.findViewById(R.id.grouptag_list)).getText().toString();
		  		
		  		Toast.makeText(this,"内容为:" + menulistname+"----"
		  									 + menulistnumber+"----"
		  									 + menulistgrouptag+"----"
		  									,Toast.LENGTH_LONG).show(); // 显示那条数据
		  		long itemid_update = member_db_del_g.check_id(menulistname);
		  		String name_update = menulistname;
		  		String number_update = menulistnumber;
		  		String grouptag_update = "";
		  		member_db_del_g.update(name_update, number_update, grouptag_update, itemid_update);
		  		member_db_del_g.close();
		  		onCreate(null);
		  		break;
		  	case 1:		//Details

				break;
			default:
				break;
		  }
		return super.onContextItemSelected(item); 
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		
		getSupportMenuInflater().inflate(R.menu.menu_m_in_g, menu);
		return super.onCreateOptionsMenu(menu);

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		final Context context = this;
		MembersDB member_info = new MembersDB(this);
		Cursor cursor_find = m_db.all(this);
		switch (item.getItemId()) {

		case R.id.ming_menu_back:
	    	Intent intent_managegroups = new Intent(context, ManageGroupsActivity.class);
	    	startActivity(intent_managegroups); 
			break;
		case R.id.ming_menu_add:
			
        	AlertDialog.Builder builderSingle_add = new AlertDialog.Builder(MembersinGroupActivity.this);
            builderSingle_add.setTitle("Add members");
            final ArrayAdapter<String> arrayAdapter_add = new ArrayAdapter<String>(
            		MembersinGroupActivity.this,
                    android.R.layout.select_dialog_singlechoice);
            while(cursor_find.moveToNext())
            {
            	String name_find = cursor_find.getString(cursor_find.getColumnIndex("name"));
            	String tag_find = cursor_find.getString(cursor_find.getColumnIndex("tag"));
            	if(!tag_find.equals(tag_value))
            		arrayAdapter_add.add(name_find);
            }
            builderSingle_add.setAdapter(arrayAdapter_add,
                    new DialogInterface.OnClickListener() {
            	
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String name_item_add= arrayAdapter_add.getItem(which);
                            long id_item_add = m_db.check_id(name_item_add);
                            String number_item_add = m_db.check_number(name_item_add);
                            m_db.update(name_item_add, number_item_add, tag_value, id_item_add);
//                        	Toast.makeText(getBaseContext(), str_item_add, Toast.LENGTH_SHORT).show();
            		        onCreate(null);
                        }
                    });
            builderSingle_add.show();
			break;
		case R.id.menu_group_options:
        	AlertDialog.Builder builderSingle = new AlertDialog.Builder(MembersinGroupActivity.this);
            builderSingle.setTitle("Group Options");
            final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
            		MembersinGroupActivity.this,
                    android.R.layout.select_dialog_singlechoice);
            arrayAdapter.add("Group Call");
            arrayAdapter.add("Group Message");

            builderSingle.setAdapter(arrayAdapter,
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String str_item= arrayAdapter.getItem(which);
                        	Toast.makeText(getBaseContext(), str_item, Toast.LENGTH_SHORT).show();
                        }
                    });
            builderSingle.show();
			break;
			
		case R.id.ming_menu_exit:
			SysApplication.getInstance().exit();
			break;
			
		default:
			break;

		}

		return super.onOptionsItemSelected(item);  

	}
	
}
