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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
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
		Date rundate =new Date();
		
		String rundt = new SimpleDateFormat("yyyy-MM-dd").format(rundate);
		//List<HashMap<String, String>> studentstaticdata  = new ArrayList<HashMap<String, String>>();
		
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
		String currentUsersHomeDir = System.getProperty("user.home");
		String dglocker = currentUsersHomeDir + File.separator + "dglocker";
		
		//File file = new File("C:\\Arush\\"+session+"_"+course_name+"_"+roman+"_"+"Rundate_"+rundt+".csv");
		File theDir = new File(dglocker);
		if (!theDir.exists()){
		    theDir.mkdirs();
		}
		
		for (Dgmain pckobj:pcklist) {
			
			String pck = pckobj.getProgramCourseKey();
			String date = pckobj.getSemesterStartDate().toString();
			
			
			 
			 // create FileWriter object with file as parameter 
		        FileWriter outputfile;
		        String course_name="";
		        String session="";
		        String sem="";
		        String roman="";
				try {
					studentstaticdata = theDgExtractRepository.getstudentlist
							(pckobj.getProgramCourseKey(),pckobj.getSemesterStartDate());
					course_name =(String) studentstaticdata.get(0).get("course_name");
					session =(String) studentstaticdata.get(0).get("SESSION");
					sem =(String) studentstaticdata.get(0).get("SEM");
					roman=getroman(sem);
					
					maxsubject=theDgExtractRepository.getmaxsubject(pck, pckobj.getSemesterStartDate());
					
					totsubc=(String)maxsubject.get(0).get("totsub");
					totsub = Integer.parseInt(totsubc);
		        // create CSVWriter object filewriter object as parameter 
					
					File file = new File(dglocker+File.separator+session+"_"+course_name+"_"+roman+"_"+"Rundate_"+rundt+".csv");
					
					outputfile = new FileWriter(file);
					CSVWriter writer = new CSVWriter(outputfile);
     
			writecsvheader(writer,format,totsub);
			
			
			for(Map<String, Object> student:studentstaticdata) {
				
				String rollno = (String )student.get("rroll");
				subjectdata = theDgExtractRepository.getsubjectlist(rollno,
						pckobj.getProgramCourseKey(),pckobj.getSemesterStartDate());
				
				totcreditpoint=theDgExtractRepository.gettotcreditpoint(rollno,pckobj.getProgramCourseKey(), pckobj.getSemesterStartDate());
				
				
				writecsv(student,format,writer,subjectdata,totcreditpoint,totsub,roman);
				//System.out.println(student.get(format.get(0).getField()));
			}
			
			theDgExtractRepository.updatestatus(rundate, pckobj.getProgramCourseKey(), pckobj.getSemesterStartDate());
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
	
	private String getroman(String sem) {
	// TODO Auto-generated method stub
		
		String romstr="";
		String[] sm= {"SM1","SM2","SM3","SM4","SM5","SM6","SM7"
				,"SM8","SM9","SM10","SM11","SM12","SM13","SM14","SM15","SM16"};
		
		String[] rom= {"I","II","III","IV","V","VI","VII"
				,"VIII","IX","X","XI","XII","XIII","XIV","XV","XVI"};
              for(int i=0;i<sm.length;i++) {
            	  if(sem.equalsIgnoreCase(sm[i])) {
            		  romstr=rom[i];
            		  break;
            	  }
              }
		
		return romstr;
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
		int n=-1;
		System.out.println("Array length="+arr.length);
		Iterator <DgFormat> fmt = format.iterator();
		for (int i =0;i<format.size();i++) {
			keyfield= fmt.next().getField();
			if (keyfield.equalsIgnoreCase("subject")) {
			   for(int j=1;j<=totsub;j++) {
				   	   
					   
			          convertheader(tempheader, header, j);
			          for (int m=0;m<tempheader.length;m++) {
			        	  n++;
			        	  arr[n]=tempheader[m];
			        	  
			          }
			          
				   
				   
				   
				   
			   }
				
				
			}else {
				n++;
				arr[n]=keyfield;	
			}
			
		}
		
		writer.writeNext(arr);
	}
	
	private void convertheader(String[] tempheader ,String[] header,int j){
		
		for (int i=0;i<header.length;i++) {
			tempheader[i]	=header[i].replace("*", String.valueOf(j));
		}
	}
	

	private void writecsv(Map<String, Object> student,List <DgFormat> format,
			CSVWriter writer,List<Map<String, Object>> subjects,Map<String, Object> creditpoint,int totsub,String roman) {
		String keyfield;
		String[]  arr= new String[format.size()+(totsub*15)];
		//String[]  arr= new String[300];
		Iterator <DgFormat> fmt = format.iterator();
		int subcount = totsub-subjects.size();
		subcount= subcount*15;
		int n=-1;
        		
		for (int i =0;i<format.size();i++) {
			
			keyfield= fmt.next().getField();
			
			if (keyfield.equalsIgnoreCase("SEM")) {
				n++;
				arr[n]=roman;
				continue ;
			}
		
			if (keyfield.equalsIgnoreCase("subject")) {
				
				for(Map<String, Object> stdsubjects:subjects) {
				    n++;
					arr[n]=(String)stdsubjects.get("course_name");
					n++;
				arr[n]=(String)stdsubjects.get("course_code");
							
				for(int j=0;j<8;j++) {
					n++;
					arr[n]="";
					
				}
								
				n++;
				arr[n]=(String)stdsubjects.get("gradepoint");
				n++;
				arr[n]=(String)stdsubjects.get("credits");
				n++;
				arr[n]=(String)stdsubjects.get("creditpoint");
				
				for(int j=0;j<2;j++) {
					n++;
					arr[n]="";
					
				}
				//i++;
				
				}
				continue;
			}
		       System.out.println("Keyfield:"+keyfield);
		       System.out.println("Value:"+student.get(keyfield));
		       if (keyfield.equalsIgnoreCase("TOT_CREDIT_POINTS")) {
		    	   n++;              
		    	   arr[n]=(String)creditpoint.get("TOT_CREDIT_POINTS");
		    	   
		       }else if(keyfield.equalsIgnoreCase("TOT_CREDIT")){
		    	   n++;
		    	   arr[n]=(String)creditpoint.get("TOT_CREDIT");
		    	   
		       }else {
		    	   if(keyfield.equalsIgnoreCase("AADHAAR_NAME")){
		    		   for(int q=0;q < subcount;q++) {
			    		   n++;
							arr[n]="";
			    	   }   
		    	   }
		    	   
		    	   
		    	   n++;
		    	   arr[n]=(String)student.get(keyfield);
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


