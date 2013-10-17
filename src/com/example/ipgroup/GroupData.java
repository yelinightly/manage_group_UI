package com.example.ipgroup;

import java.io.Serializable;

public class GroupData implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private String group_name="";
	private String group_number="";
	private long group_id=0;
	
	public String get_group_name(){
		return group_name;
	}
	public void set_group_name(String group_name){
		this.group_name = group_name;
	}
	public String get_group_number(){
		return group_number;
	}
	public void set_group_number(String group_number){
		this.group_number = group_number;
	}
	public long get_group_gid(){
		return group_id;
	}
	public void set_group_gid(long group_id){
		this.group_id =group_id;
	}

}
