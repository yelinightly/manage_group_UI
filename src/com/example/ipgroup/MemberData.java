package com.example.ipgroup;

import java.io.Serializable;

public class MemberData implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private String member_name="";
	private String member_number="";
	private long member_id=0;
	
	private String member_grouptag="";
	
	public String get_member_name(){
		return member_name;
	}
	public void set_member_name(String member_name){
		this.member_name = member_name;
	}
	public String get_member_number(){
		return member_number;
	}
	public void set_member_number(String member_number){
		this.member_number = member_number;
	}
	public long get_member_id(){
		return member_id;
	}
	public void set_member_id(long member_id){
		this.member_id = member_id;
	}
	
	public void set_member_grouptag(String member_grouptag){
		this.member_grouptag = member_grouptag;
	}
	
	public String get_member_grouptag(){
		return member_grouptag;
	}
	
}
