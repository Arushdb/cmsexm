package edu.dei.examination.cmsexm.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;


@Entity
@Table(name="ver_agency" ,uniqueConstraints =@UniqueConstraint(columnNames = "name") )
public class VerificationAgency {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	private int id;
		
	private String name;
	private String address;
	private String city;
	private String state;
	private String pincode;
	private String contactno;
	private String email;
	private String website;
	
	@Column(columnDefinition = "TINYINT" ,name = "authentic")
	
	private boolean authentic=false;
	
	private String referenceno;
		
	@Column(name="insert_time" ,columnDefinition = "DATETIME")
	@Temporal(TemporalType.TIMESTAMP)
	private Date insertime;
	@Column(name="modification_time",columnDefinition = "DATETIME" )
	@Temporal(TemporalType.TIMESTAMP)
	private Date modificationtime;
	
	private String creator_id;
	private String modifier_id;
	
//	///  one to many  relation with RequesterReferences 
//	@OneToMany(fetch=FetchType.LAZY,
//			   mappedBy="requester",
//			   cascade= {CascadeType.PERSIST, CascadeType.MERGE,
//						 CascadeType.DETACH, CascadeType.REFRESH})
//	@JsonManagedReference
//	private List<VerificationAgencyReferences> requestref; 
	
	public VerificationAgency() {
	
	}
	

	public VerificationAgency(String name, String address, String city, String state, String pincode, String contactno,
			String email, String website, boolean authentic, String referenceno, Date insertime, Date modificationtime,
			String creator_id, String modifier_id) {
		super();
		this.name = name;
		this.address = address;
		this.city = city;
		this.state = state;
		this.pincode = pincode;
		this.contactno = contactno;
		this.email = email;
		this.website = website;
		this.authentic = authentic;
		this.referenceno = referenceno;
		this.insertime = insertime;
		this.modificationtime = modificationtime;
		this.creator_id = creator_id;
		this.modifier_id = modifier_id;
	}



	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getPincode() {
		return pincode;
	}
	public void setPincode(String pincode) {
		this.pincode = pincode;
	}
	public String getContactno() {
		return contactno;
	}
	public void setContactno(String contactno) {
		this.contactno = contactno;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Date getInsertime() {
		return insertime;
	}
	public void setInsertime(Date insertime) {
		this.insertime = insertime;
	}
	public Date getModificationtime() {
		return modificationtime;
	}
	public void setModificationtime(Date modificationtime) {
		this.modificationtime = modificationtime;
	}
	public String getCreator_id() {
		return creator_id;
	}
	public void setCreator_id(String creator_id) {
		this.creator_id = creator_id;
	}
	public String getModifier_id() {
		return modifier_id;
	}
	public void setModifier_id(String modifier_id) {
		this.modifier_id = modifier_id;
	}
	
	
	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	

		   	
//	public List<VerificationAgencyReferences> getRequestref() {
//		return requestref;
//	}
//
//
//
//
//	public void setRequestref(List<VerificationAgencyReferences> requestref) {
//		this.requestref = requestref;
//	}


	public boolean isAuthentic() {
		return authentic;
	}


	public void setAuthentic(boolean authentic) {
		this.authentic = authentic;
	}




	public String getWebsite() {
		return website;
	}


	public void setWebsite(String website) {
		this.website = website;
	}
	public String getReferenceno() {
		return referenceno;
	}
	public void setReferenceno(String referenceno) {
		this.referenceno = referenceno;
	}


	
	// add convenience methods for bi-directional relationship










//	public void add (VerificationAgencyReferences tempref) {
//		
//		if (requestref ==null) {
//			requestref = new ArrayList<VerificationAgencyReferences>();
//		}
//		
//		requestref.add(tempref);
//		
//		tempref.setRequester(this);
//		
//	}
	
//		public void add(RequesterReferences tempref) {
//			
//			if (requestref == null) {
//				requestref = new ArrayList<>();
//			}
//			
//			requestref.add(tempref);
//			
//			tempref.setRequester(this);
//		}
	
	
	
}
