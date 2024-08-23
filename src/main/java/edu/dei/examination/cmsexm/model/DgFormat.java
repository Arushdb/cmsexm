package edu.dei.examination.cmsexm.model;



public class DgFormat {
	
	private String sqno;

   
    private String field;
    

    private String table;
    
    private String subject;


	public String getSqno() {
		return sqno;
	}


	public void setSqno(String sqno) {
		this.sqno = sqno;
	}


	public String getField() {
		return field;
	}


	public void setField(String field) {
		this.field = field;
	}


	public String getTable() {
		return table;
	}


	public void setTable(String table) {
		this.table = table;
	}


	public DgFormat() {
		
	}


	public DgFormat(String sqno, String field, String table) {
		
		this.sqno = sqno;
		this.field = field;
		this.table = table;
	}


	public String getSubject() {
		return subject;
	}


	public void setSubject(String subject) {
		this.subject = subject;
	}
    
    
    
}
