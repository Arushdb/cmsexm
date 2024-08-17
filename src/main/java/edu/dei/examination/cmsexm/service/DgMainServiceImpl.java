package edu.dei.examination.cmsexm.service;

import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.opencsv.CSVWriter;

import edu.dei.examination.cmsexm.model.DgExtract;
import edu.dei.examination.cmsexm.model.DgFormat;
import edu.dei.examination.cmsexm.model.Dgmain;
import edu.dei.examination.cmsexm.model.UserRoles;
import edu.dei.examination.cmsexm.repository.DgExtractRepository;
import edu.dei.examination.cmsexm.repository.DgMainRepository;
import javassist.expr.NewArray;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;



@PropertySource("classpath:digilocker.properties")
@Service

public class DgMainServiceImpl implements DgMainService {
	
	@Autowired
	EntityManager em;
	
	@Autowired
	private DgMainRepository  theDgMainRepository  ;
	
	@Autowired
	private DgExtractRepository  theDgExtractRepository  ;
	
	
	
	
//	public DgMainServiceImpl(DgMainRepository theDgMainRepository) {
//	
//		this.theDgMainRepository = theDgMainRepository;
//	}

//	public static void main(String[] str) {
//		DgMainServiceImpl main = new DgMainServiceImpl();
//		
//		main.dgmain();
//		
//	}
	
	@Transactional()
	@Scheduled(fixedRateString = "${run-frquency.minutes}", timeUnit = TimeUnit.MINUTES)
	public void dgmain() {
		
		
		// Read Dg controller for  unprocessed data
	    
	    
		List <Dgmain> pcklist = getDgpcklist("N");
		DgExtract ex = new DgExtract();
		List<DgExtract> exlist=null;
		List<Map<String, Object>> studentstaticdata  = new ArrayList<>();
		List<Map<String, Object>> subjectdata  = new ArrayList<>();
		List<Map<String, Object>> maxsubject  = new ArrayList<>();
		Map<String, Object> totcreditpoint  = null;
		
		List<Map<String, Object>> dgformat =theDgExtractRepository.getdgformat();
		Gson gson1 = new Gson();
		JsonElement jsonElement1 = gson1.toJsonTree(dgformat);
		Type collectionType1 = new TypeToken<List<DgFormat>>(){}.getType();
		
		List <DgFormat> format = (List<DgFormat>)gson1.fromJson(jsonElement1, collectionType1);
		
		int totsub =0;
		String totsubc ;
		
		for (Dgmain pckobj:pcklist) {
			
			String pck = pckobj.getProgramCourseKey();
			String date = pckobj.getSemesterStartDate().toString();
			 File file = new File("C:\\Arush\\"+pck+"_"+date+".csv");
			 // create FileWriter object with file as parameter 
		        FileWriter outputfile;
				try {
					outputfile = new FileWriter(file);
					CSVWriter writer = new CSVWriter(outputfile);
				    
					maxsubject=theDgExtractRepository.getmaxsubject(pck, pckobj.getSemesterStartDate());
					
					totsubc=(String)maxsubject.get(0).get("totsub");
					totsub = Integer.parseInt(totsubc);
		        // create CSVWriter object filewriter object as parameter 
		         
			writecsvheader(writer,format,totsub);
			studentstaticdata = theDgExtractRepository.getstudentlist
					(pckobj.getProgramCourseKey(),pckobj.getSemesterStartDate());
			
			for(Map<String, Object> student:studentstaticdata) {
				
				String rollno = (String )student.get("rroll");
				subjectdata = theDgExtractRepository.getsubjectlist(rollno,
						pckobj.getProgramCourseKey(),pckobj.getSemesterStartDate());
				
				totcreditpoint=theDgExtractRepository.gettotcreditpoint(rollno,pckobj.getProgramCourseKey(), pckobj.getSemesterStartDate());
				
				
				writecsv(student,format,writer,subjectdata,totcreditpoint);
				//System.out.println(student.get(format.get(0).getField()));
			}
			

			writer.close();
		}

		 catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
		}
		
		
		
		//  Get student digilocker  static data  like name ,father name ... of each student
		
		
		
		// Get student digi locker  variable  data of each student
		
		
		
		// insert data into dg extract
		
	}
	
	private void writecsvheader(CSVWriter writer,List <DgFormat> format, int totsub){
		String[] tempheader = new String[15];
		String[] header = new String[15];
		header[0]="SUB*NM";
		header[1]="SUB*";
		header[2]="SUB*_TH_MAX";
		header[3]="SUB*_PR_MAX";
		header[4]="SUB*_CE_MAX";
		header[5]="SUB*_TH_MRKS";
		header[6]="SUB*_PR_MRKS";
		header[7]="SUB*_CE_MRKS";
		header[8]="SUB*_TOT";
		header[9]="SUB*_GRADE";
		header[10]="SUB*_GRADE_POINTS";
		header[11]="SUB*_CREDIT";
		header[12]="SUB*_CREDIT_POINTS";
		header[13]="SUB*_REMARKS";
		header[14]="SUB*_CREDIT_ELIGIBILITY";
		
		
		
		
          		
		String keyfield;
		String[]  arr= new String[format.size()+totsub*15-1];
		System.out.println("Array length="+arr.length);
		Iterator <DgFormat> fmt = format.iterator();
		for (int i =0;i<format.size();i++) {
			keyfield= fmt.next().getField();
			if (keyfield.equalsIgnoreCase("subject")) {
			   for(int j=1;j<=totsub;j++) {
				   	   
					   
			          convertheader(tempheader, header, j);
			          for (int m=0;m<tempheader.length;m++) {
			        	  arr[i]=tempheader[m];
			        	  i++;
			          }
			          
				   
				   
				   
				   
			   }
				
				
			}else {
				arr[i]=keyfield;	
			}
			
		}
		
		writer.writeNext(arr);
	}
	
	private void convertheader(String[] tempheader ,String[] header,int j){
		
		for (int i=0;i<header.length;i++) {
			tempheader[i]	=header[i].replace("*", String.valueOf(j));
		}
	}
	

	private void writecsv(Map<String, Object> student,List <DgFormat> format,CSVWriter writer,List<Map<String, Object>> subjects,Map<String, Object> creditpoint) {
		String keyfield;
		String[]  arr= new String[format.size()+(subjects.size()*15)];
		//String[]  arr= new String[300];
		Iterator <DgFormat> fmt = format.iterator();
		
        		
		for (int i =0;i<format.size();i++) {
			
			keyfield= fmt.next().getField();
			
		
			if (keyfield.equalsIgnoreCase("subject")) {
				
				for(Map<String, Object> stdsubjects:subjects) {
				
					arr[i]=(String)stdsubjects.get("course_name");
					i++;
				arr[i]=(String)stdsubjects.get("course_code");
							
				for(int j=0;j<8;j++) {
					i++;
					arr[i]="";
					
				}
								
				i++;
				arr[i]=(String)stdsubjects.get("gradepoint");
				i++;
				arr[i]=(String)stdsubjects.get("credits");
				i++;
				arr[i]=(String)stdsubjects.get("creditpoint");
				
				for(int j=0;j<2;j++) {
					i++;
					arr[i]="";
					
				}
				i++;
				
				}
				continue;
			}
		       System.out.println("Keyfield:"+keyfield);
		       System.out.println("Value:"+student.get(keyfield));
		       if (keyfield.equalsIgnoreCase("TOT_CREDIT_POINTS")) {
		    	                 
		    	   arr[i]=(String)creditpoint.get("TOT_CREDIT_POINTS");
		    	   
		       }else if(keyfield.equalsIgnoreCase("TOT_CREDIT")){
		    	   arr[i]=(String)creditpoint.get("TOT_CREDIT");
		    	   
		       }else {
		    	   arr[i]=(String)student.get(keyfield);
		       }
				
				System.out.println(student.get(keyfield));
			}
						  
		writer.writeNext(arr);  
			    
		   }
		
	@Override
	public List<Dgmain> getDgpcklist(String status) {
		// TODO Auto-generated method stub
		return theDgMainRepository.findByStatus("N");
		
	}

	// Get student list of pck to be processed
	//public List<DgExtract> getstudentlist(){
//		 List<DgExtract> studentList =(List<DgExtract>)em.createNamedQuery("getstudentlist")
//					//.setParameter("userid",id)
//					//.setParameter("defaultrole", true)
//					
//					.getResultList();
		 
		// return studentList;
		//}
	

	
}


