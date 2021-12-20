package edu.dei.examination.cmsexm.service;

import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Stream;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.FontFactory;
import com.lowagie.text.HeaderFooter;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfPageEventHelper;
import com.lowagie.text.pdf.PdfWriter;

import edu.dei.examination.cmsexm.domain.Verification;
import edu.dei.examination.cmsexm.model.StudentProgram;
import edu.dei.examination.cmsexm.model.UserRoles;
import edu.dei.examination.cmsexm.model.VerificationAgency;
import edu.dei.examination.cmsexm.model.VerificationAgencyReferences;
import edu.dei.examination.cmsexm.model.VerificationEnrolmentno;
import edu.dei.examination.cmsexm.model.VerificationReport;
import edu.dei.examination.cmsexm.repository.VerificationAgencyReferencesRepository;
import edu.dei.examination.cmsexm.repository.VerificationAgencyRepository;
import edu.dei.examination.cmsexm.repository.VerificationEnrolmentRepository;

@Service
public class VerificationAgencyReferencesServiceImpl implements VerificationAgencyReferencesService {
	
	private VerificationAgencyReferencesRepository referencesRepository;
	private VerificationEnrolmentRepository enrolmentrepository;
	private VerificationAgencyRepository verificationAgencyRepository;
	
	
//	@Autowired
//	private EntityManager entityManager;
	
	@Autowired
	EntityManager em;
	
	@Autowired
	private ServletContext context;
	
	@Autowired
	public VerificationAgencyReferencesServiceImpl(VerificationAgencyReferencesRepository referencesRepository,
			VerificationEnrolmentRepository enrolmentrepository,
			VerificationAgencyRepository verificationAgencyRepository
			
			) {
	
		this.referencesRepository = referencesRepository;
		this.enrolmentrepository = enrolmentrepository;
		this.verificationAgencyRepository=verificationAgencyRepository;
	}
	
	
//   @Autowired
//	public VerificationAgencyReferencesServiceImpl(VerificationEnrolmentRepository enrolmentrepository) {
//	
//	
//}



	@Override
	public List<VerificationAgencyReferences> findAll() {
		// TODO Auto-generated method stub
		return referencesRepository.findAll();
	}

	@Override
	public VerificationAgencyReferences findById(int theId) {
		
		Optional<VerificationAgencyReferences> result=    referencesRepository.findById(theId);
		
		VerificationAgencyReferences  theVerificationreference = null;
		
		if(result.isPresent()) {
			
			theVerificationreference = result.get();
			
		}else {
			
			throw new RuntimeException("Did not find Verification Agency - " + theId);
			
		}
			
		
		return  theVerificationreference ;
		
	}

	@Override
	public void save(VerificationAgencyReferences  theverificationreferences) {
		
		theverificationreferences.setInsert_time(new Date());
		theverificationreferences.setCreator_id("Arush");
		
		
		referencesRepository.save(theverificationreferences);
			
	}

	@Override
	public void deleteById(int theId) {
		referencesRepository.deleteById(theId);
		
	}

	



	@Override
	public void deletenEnrolmentno(int theId) {
		enrolmentrepository.deleteById(theId);
		
	}


	@Override
	public List<VerificationAgencyReferences> findByAgencyid(int id) {
		// TODO Auto-generated method stub
		return referencesRepository.findByAgencyid(id);
	}


	@Override
	public List<VerificationAgencyReferences> findByProcessstatus(int agencyid,String sts) {
		// TODO Auto-generated method stub
		return referencesRepository.findByAgencyidAndProcessstatus(agencyid, sts);
	}
	
	public String generateVerificationReport(int refid) {
		
		VerificationAgencyReferences reflist= findById(refid);
	
		// Get list of Enrollment numbers
		
		List<VerificationEnrolmentno> enrolement =reflist.getEnrolmentno();
		
		
		//Get Student detail
		
		
		
		
		VerificationAgency veragency =verificationAgencyRepository.getById(reflist.getAgencyid());
		
		String sep = System.getProperty("file.separator");
		
		String directory = context.getRealPath("/")+"REPORTS" ;
		//String directory=context.getContextPath()+"/"+"REPORTS";
	
		File file = new File(directory);
		file.mkdirs();	
		
		Document document = new Document(PageSize.A4);
	
		
		
		
		try {
			PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(directory + sep +refid+ ".pdf"));

			Custompageevent event = new Custompageevent();
			writer.setPageEvent(event);
//			HeaderFooter header = new HeaderFooter(new Phrase("C O N F I D E N T I A L"), false);
//	    	document.setHeader(header);
//			
			// Printing Header in image format
			
			Phrase ph= new Phrase("Assistant Registrar (Examination)"+"            "+"Dealing Assistant       ",
					FontFactory.getFont(FontFactory.COURIER, 12, new Color(255,0,0)));
			HeaderFooter footer = new HeaderFooter(ph, true);
			footer.setAlignment(Element.ALIGN_BOTTOM);
			footer.setBorder(Rectangle.NO_BORDER);
			document.setFooter(footer);
			
		    	
	    	
	    	HeaderFooter header = new HeaderFooter(new Phrase(Chunk.NEWLINE), false);
	    	
	    	
	    	document.setHeader(header);
	    
	    	
	    	
	    	document.open();
	    	
	    	Paragraph para= new Paragraph(new Phrase("C O N F I D E N T I A L"));
	    	para.setAlignment(Element.ALIGN_CENTER);
	    	document.add(para);
					
			PdfPCell cell;
			
			
			//ph.add(footertable);
			
			
			
			
			
			//document.add(Chunk.NEWLINE);
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
			//Paragraph paragraph= new Paragraph(new Phrase("C O N F I D E N T I A L"));
			//paragraph.setAlignment(1);
			Paragraph agency = new Paragraph("Request No:"+refid+"                                                               "+
					"                                      "+dateFormat.format(new Date())
			+"\nTo:\n The "+veragency.getName()+
					"\n"+veragency.getAddress()+"\n"+veragency.getCity()+"\n"+veragency.getState() );
			
			Paragraph subject = new Paragraph("Subject: Verification of Result(s) / Degree(s).");
			
			Paragraph salute = new Paragraph("Dear Sir/Madam:"+"\n"+
			"With reference to the above, the verified result of the concerned student(s) is"+
			" being sent to you with following details:");

			Phrase dashline = new Phrase("- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -" +
					" - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -" );
			
			PdfPTable studentdetail;
			
			
			studentdetail = new PdfPTable(new float[] {0.5f,3, 2, 3,2,2,2 });
			
			studentdetail.setWidthPercentage(100);
			 cell = new PdfPCell(new Phrase("S.N"));
			cell.setBorderWidth(1);
			studentdetail.addCell(cell);
		
			 cell = new PdfPCell(new Phrase("Name of student"));
			cell.setBorderWidth(1);
			studentdetail.addCell(cell);
			
			cell = new PdfPCell(new Phrase("Enrolment No."));
			cell.setBorderWidth(1);
			studentdetail.addCell(cell);
			
			cell = new PdfPCell(new Phrase("Passed Examination"));
			cell.setBorderWidth(1);
			
			studentdetail.addCell(cell);
			
			cell = new PdfPCell(new Phrase("Session"));
			cell.setBorderWidth(1);
			studentdetail.addCell(cell);
			
			cell = new PdfPCell(new Phrase("Cumulative Grade Point Average"));
			cell.setBorderWidth(1);
			studentdetail.addCell(cell);
			
			cell = new PdfPCell(new Phrase("Division"));
			cell.setBorderWidth(1);
			studentdetail.addCell(cell);
						
			//document.add(paragraph);
			document.add(agency);
			document.add(salute);
			document.add(dashline);
			
						
			int sno=0;
			for (VerificationEnrolmentno enr:enrolement) {
				
				String enrolment=String.valueOf(enr.getEnrolmentno());
				
				
				List<Verification> results=em.createNamedQuery("getstudentdetail1")
				.setParameter("enrolmentno",enrolment )
				.getResultList();
				
				
				//results.forEach(x -> {System.out.println(x.getEnrollment_number()+x.getProgram_name());});
				
				for ( Verification studentdet:results) {
					
					
					
					sno++;
					 cell = new PdfPCell(new Phrase(String.valueOf(sno)));
						cell.setBorderWidth(1);
						studentdetail.addCell(cell);
					

						 cell = new PdfPCell(new Phrase(studentdet.getStudent_first_name()));
						cell.setBorderWidth(1);
						studentdetail.addCell(cell);
						
						cell = new PdfPCell(new Phrase(studentdet.getEnrollment_number()));
						cell.setBorderWidth(1);
						studentdetail.addCell(cell);
						
						cell = new PdfPCell(new Phrase(studentdet.getProgram_name()));
						cell.setBorderWidth(1);
						
						studentdetail.addCell(cell);
						
						// print session
						String session=studentdet.getPassed_from_session().toString();
						session=session.substring(0, 4);
						String toyear= String.valueOf(Integer.parseInt(session.substring(2,4))+1);
						session= session+"-"+toyear;
						cell = new PdfPCell(new Phrase(session));
						cell.setBorderWidth(1);
						studentdetail.addCell(cell);
						
						cell = new PdfPCell(new Phrase(studentdet.getCgpa().toString()));
						cell.setBorderWidth(1);
						studentdetail.addCell(cell);
						
						// Print Division
						cell = new PdfPCell(new Phrase(studentdet.getComponent_description()));
						cell.setBorderWidth(1);
						studentdetail.addCell(cell);
									
					
				}
				
				
				
				
			}
			
						
            
			document.add(studentdetail);
			document.add(footer);
			document.close();
//			
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(directory + sep + input.getRequestNo()+ ".pdf"));
		
		
		return null;
	}
	protected class Custompageevent extends PdfPageEventHelper {
		
		@Override
		public void onStartPage(PdfWriter writer, Document document) {
			try{
				document.add(new Phrase("\n\n\n"));
				
				String filename = "/images/deiLogoHeader.png";
				//File file = new File(getClass().getResource(filename).getFile());
				
				Image headerimg = Image.getInstance(java.awt.Toolkit.getDefaultToolkit().createImage(getClass().getResource(filename)),null);
				
				headerimg.setAlignment(Element.ALIGN_MIDDLE);
				headerimg.setAbsolutePosition(0, 750);
				headerimg.scalePercent(25f, 16f);
				
					    	
		    	writer.getDirectContent().addImage(headerimg);
		    	
		    	
		    	
		    	
				}
				catch(Exception x) {
				      x.printStackTrace();
				      System.out.println("error in start page " + x.getMessage());
				 }	
		}
		
//		@Override
//		public void onEndPage(PdfWriter writer, Document document) {
//			try{
//				document.add(new Phrase("\n\n\n"));
//				
//				String filename = "/images/deiLogoHeader.png";
//				//File file = new File(getClass().getResource(filename).getFile());
//				
//				Image headerimg = Image.getInstance(java.awt.Toolkit.getDefaultToolkit().createImage(getClass().getResource(filename)),null);
//				
//				headerimg.setAlignment(Element.ALIGN_MIDDLE);
//				headerimg.setAbsolutePosition(0, 750);
//				headerimg.scalePercent(25f, 16f);
//				
//					    	
//		    	writer.getDirectContent().addImage(headerimg);
//		    	
//		    	
//		    	
//		    	
//				}
//				catch(Exception x) {
//				      x.printStackTrace();
//				      System.out.println("error in start page " + x.getMessage());
//				 }
//		}
//		
		//public void onStartPage(PdfWriter writer, Document document) {
//			try{
//			document.add(new Phrase("\n\n\n"));
//			
//			String filename = "/images/deiLogoHeader.png";
//			//File file = new File(getClass().getResource(filename).getFile());
//			
//			Image headerimg = Image.getInstance(java.awt.Toolkit.getDefaultToolkit().createImage(getClass().getResource(filename)),null);
//			
//			headerimg.setAlignment(Element.ALIGN_MIDDLE);
//			headerimg.setAbsolutePosition(0, 750);
//			headerimg.scalePercent(25f, 16f);
//			
//				    	
//	    	writer.getDirectContent().addImage(headerimg);
//	    	
////	    	document.add(Chunk.NEWLINE);
////	    	Paragraph paragraph= new Paragraph(new Phrase("C O N F I D E N T I A L"));
////	    	paragraph.setAlignment(1);
//	    	
//	    	//document.add(paragraph);
//			}
//			catch(Exception x) {
//			      x.printStackTrace();
//			      System.out.println("error in start page " + x.getMessage());
//			 }
	    //}
		} 


	
	
	
	
}

	

