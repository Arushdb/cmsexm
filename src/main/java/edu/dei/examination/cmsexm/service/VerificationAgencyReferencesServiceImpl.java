package edu.dei.examination.cmsexm.service;

import java.awt.Color;
import java.awt.datatransfer.Clipboard;
import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
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

import javax.imageio.ImageIO;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lowagie.text.Cell;
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
import com.lowagie.text.Table;
import com.lowagie.text.pdf.ColumnText;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfPageEventHelper;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.rtf.RtfWriter2;
import com.lowagie.text.rtf.field.RtfPageNumber;
import com.lowagie.text.rtf.field.RtfTotalPageNumber;
import com.lowagie.text.rtf.headerfooter.RtfHeaderFooter;
import com.lowagie.text.rtf.table.RtfCell;
import com.lowagie.text.rtf.table.RtfTable;

import edu.dei.examination.cmsexm.domain.Verification;
import edu.dei.examination.cmsexm.model.StudentProgram;
import edu.dei.examination.cmsexm.model.UserRoles;
import edu.dei.examination.cmsexm.model.VerificationAgency;
import edu.dei.examination.cmsexm.model.VerificationAgencyReferences;
import edu.dei.examination.cmsexm.model.VerificationEnrolmentno;
import edu.dei.examination.cmsexm.model.VerificationReport;
import edu.dei.examination.cmsexm.model.VerificationRollno;
import edu.dei.examination.cmsexm.repository.VerificationAgencyReferencesRepository;
import edu.dei.examination.cmsexm.repository.VerificationAgencyRepository;
import edu.dei.examination.cmsexm.repository.VerificationEnrolmentRepository;
import edu.dei.examination.cmsexm.repository.VerificationRollnoRepository;

@Service
public class VerificationAgencyReferencesServiceImpl implements VerificationAgencyReferencesService {

	private VerificationAgencyReferencesRepository referencesRepository;
	private VerificationRollnoRepository verificationRollnoRepository;
	private VerificationAgencyRepository verificationAgencyRepository;

//	@Autowired
//	private EntityManager entityManager;

	@Autowired
	EntityManager em;

	@Autowired
	private ServletContext context;

	@Autowired
	public VerificationAgencyReferencesServiceImpl(VerificationAgencyReferencesRepository referencesRepository,
			VerificationRollnoRepository verificationRollnoRepository,
			VerificationAgencyRepository verificationAgencyRepository

	) {

		this.referencesRepository = referencesRepository;
		this.verificationRollnoRepository = verificationRollnoRepository;
		this.verificationAgencyRepository = verificationAgencyRepository;
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

		Optional<VerificationAgencyReferences> result = referencesRepository.findById(theId);

		VerificationAgencyReferences theVerificationreference = null;

		if (result.isPresent()) {

			theVerificationreference = result.get();

		} else {

			throw new RuntimeException("Did not find Verification Agency - " + theId);

		}

		return theVerificationreference;

	}

	@Override
	public void save(VerificationAgencyReferences theverificationreferences) {

		theverificationreferences.setInsert_time(new Date());
		theverificationreferences.setCreator_id("Arush");

		referencesRepository.save(theverificationreferences);

	}

	@Override
	public void deleteById(int theId) {
		referencesRepository.deleteById(theId);

	}

	@Override
	public List<VerificationAgencyReferences> findByAgencyid(int id) {
		// TODO Auto-generated method stub
		return referencesRepository.findByAgencyid(id);
	}

	@Override
	public List<VerificationAgencyReferences> findByProcessstatus(int agencyid, String sts) {
		// TODO Auto-generated method stub
		return referencesRepository.findByAgencyidAndProcessstatus(agencyid, sts);
	}

	public String generateVerificationReport(int refid) {

		VerificationAgencyReferences reflist = findById(refid);

		// Get list of Enrollment numbers

		List<VerificationRollno> rollnos = reflist.getRollno();

		// Get Student detail

		VerificationAgency veragency = verificationAgencyRepository.getById(reflist.getAgencyid());

		String sep = System.getProperty("file.separator");

		String directory = context.getRealPath("/") + "REPORTS";
		// String directory=context.getContextPath()+"/"+"REPORTS";

		File file = new File(directory);
		file.mkdirs();

		Document document = new Document(PageSize.A4);

		String filepath = directory + sep + refid + ".pdf";

		try {

			PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(filepath));
			Custompageevent event = new Custompageevent();
			writer.setPageEvent(event);

			Paragraph ph = new Paragraph(
					"Assistant Registrar (Examination)" + "            " + "Dealing Assistant       ",
					FontFactory.getFont(FontFactory.COURIER, 12, new Color(255, 0, 0)));

			HeaderFooter footer = new HeaderFooter(ph, true);

			footer.setAlignment(Element.ALIGN_BOTTOM);
			footer.setBorder(Rectangle.NO_BORDER);
			document.setFooter(footer);

			HeaderFooter header = new HeaderFooter(new Phrase(Chunk.NEWLINE), false);

			document.setHeader(header);

			document.open();

			Paragraph para = new Paragraph(new Phrase("C O N F I D E N T I A L"));
			para.setAlignment(Element.ALIGN_CENTER);
			document.add(para);

			PdfPCell cell;

			// document.add(Chunk.NEWLINE);
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
			// Paragraph paragraph= new Paragraph(new Phrase("C O N F I D E N T I A L"));
			// paragraph.setAlignment(1);
			Paragraph agency = new Paragraph(
					"Request No:" + refid + "                                                               "
							+ "                                      " + dateFormat.format(new Date()) + "\nTo:\n The "
							+ veragency.getName() + "\n" + veragency.getAddress() + "\n" + veragency.getCity() + "\n"
							+ veragency.getState());

			Paragraph subject = new Paragraph("Subject: Verification of Result(s) / Degree(s).");

			Paragraph salute = new Paragraph("Dear Sir/Madam:" + "\n"
					+ "With reference to the above, the verified result of the concerned student(s) is"
					+ " being sent to you with following details:");

			Phrase dashline = new Phrase("- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -"
					+ " - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -");

			PdfPTable studentdetail;

			studentdetail = new PdfPTable(new float[] { 0.5f, 3, 2, 3, 2, 2, 2 });

			studentdetail.setWidthPercentage(100);
			cell = new PdfPCell(new Phrase("S.N"));
			cell.setBorderWidth(1);
			studentdetail.addCell(cell);

			cell = new PdfPCell(new Phrase("Name of student"));
			cell.setBorderWidth(1);
			studentdetail.addCell(cell);

			cell = new PdfPCell(new Phrase("Roll No."));
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

			// document.add(paragraph);
			document.add(agency);
			document.add(salute);
			document.add(dashline);

			int sno = 0;
			String program="";
			for (VerificationRollno rollno : rollnos) {

				// String enrolment=String.valueOf(enr.getEnrolmentno());

				List<Verification> results = em.createNamedQuery("getstudentdetail1")
						.setParameter("rollno", rollno.getRollno()).getResultList();

				// results.forEach(x ->
				// {System.out.println(x.getEnrollment_number()+x.getProgram_name());});

				for (Verification studentdet : results) {
					program="";

					sno++;
					cell = new PdfPCell(new Phrase(String.valueOf(sno)));
					cell.setBorderWidth(1);
					studentdetail.addCell(cell);

					cell = new PdfPCell(new Phrase(studentdet.getStudent_first_name()));
					cell.setBorderWidth(1);
					studentdetail.addCell(cell);

					cell = new PdfPCell(new Phrase(studentdet.getRoll_number()));
					cell.setBorderWidth(1);
					studentdetail.addCell(cell);

					program = studentdet.getProgram_name();

					if (!(studentdet.getBranch().equalsIgnoreCase("NONE"))) {
						program = program + " " + studentdet.getBranch();
					}

					if (!(studentdet.getSpeclization().equalsIgnoreCase("NONE"))) {
						program = program + " " + studentdet.getSpeclization();
					}
					cell = new PdfPCell(new Phrase(program));

					
					cell.setBorderWidth(1);

					studentdetail.addCell(cell);

					// print session
					String session = studentdet.getPassed_from_session().toString();
					session = session.substring(0, 4);
					String toyear = String.valueOf(Integer.parseInt(session.substring(2, 4)) + 1);
					session = session + "-" + toyear;
					cell = new PdfPCell(new Phrase(session));
					cell.setBorderWidth(1);
					studentdetail.addCell(cell);

					cell = new PdfPCell(new Phrase(studentdet.getCgpa().toString()));
					cell.setBorderWidth(1);
					studentdetail.addCell(cell);

					// Print Division
					cell = new PdfPCell(new Phrase(studentdet.getDivision()));
					cell.setBorderWidth(1);
					studentdetail.addCell(cell);

				}

			}

			document.add(studentdetail);
			// document.add(footer);
			document.close();
//			

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

		// PdfWriter writer = PdfWriter.getInstance(document, new
		// FileOutputStream(directory + sep + input.getRequestNo()+ ".pdf"));

		return filepath;
	}

	public String generateVerificationReportinword(int refid) {

		VerificationAgencyReferences reflist = findById(refid);

		// Get list of Enrollment numbers

		List<VerificationRollno> rollnos = reflist.getRollno();

		// Get Student detail

		VerificationAgency veragency = verificationAgencyRepository.getById(reflist.getAgencyid());

		String sep = System.getProperty("file.separator");

		String directory = context.getRealPath("/") + "REPORTS";
		// String directory=context.getContextPath()+"/"+"REPORTS";

		File file = new File(directory);
		file.mkdirs();

		Document document = new Document(PageSize.A4);

		String filepath = directory + sep + refid + ".doc";

		try {

			RtfWriter2 writer = RtfWriter2.getInstance(document, new FileOutputStream(filepath));

			String filename = "/images/deiLogoHeader.png";
			// File file = new File(getClass().getResource(filename).getFile());

			Image headerimg = Image.getInstance(
					java.awt.Toolkit.getDefaultToolkit().createImage(getClass().getResource(filename)), null);
			
			

			Paragraph ph = new Paragraph(
					"Assistant Registrar (Examination)" + "            " + "Dealing Assistant       ",
					FontFactory.getFont(FontFactory.COURIER, 12, new Color(255, 0, 0)));

			ph.add("Page:");
			ph.add(new RtfPageNumber());
			ph.add(" of ");
			ph.add(new RtfTotalPageNumber());
			HeaderFooter footer = new RtfHeaderFooter(ph);

//			footer.setAlignment(Element.ALIGN_BOTTOM);
//			footer.setBorder(Rectangle.NO_BORDER);
			writer.setFooter(footer);
			document.setMargins(50, 50, 100, 100);

			headerimg.setAlignment(Element.ALIGN_MIDDLE);
			headerimg.setAbsolutePosition(0, 750);
			headerimg.scalePercent(25f, 16f);
			
			Phrase  headerphrase = new Phrase();
			Chunk ch = new Chunk(headerimg, 0, 0);
			
			headerphrase.add(ch);
			document.setHeader(new HeaderFooter(headerphrase, false));

			document.open();

		
			Paragraph para = new Paragraph(new Phrase("C O N F I D E N T I A L"));
			para.setAlignment(Element.ALIGN_CENTER);
			document.add(para);

			Cell cell;

			SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
			Paragraph agency = new Paragraph(
					"Request No:" + refid + "                                                               "
							+ "                                      " + dateFormat.format(new Date()) + "\nTo:\n The "
							+ veragency.getName() + "\n" + veragency.getAddress() + "\n" + veragency.getCity() + "\n"
							+ veragency.getState());

			Paragraph subject = new Paragraph("Subject: Verification of Result(s) / Degree(s).");

			Paragraph salute = new Paragraph("Dear Sir/Madam:" + "\n"
					+ "With reference to the above, the verified result of the concerned student(s) is"
					+ " being sent to you with following details:");

			Phrase dashline = new Phrase("- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -"
					+ " - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -");

			float[] columnWidths = { 1, 3, 2, 3, 2, 2, 2 };

			Table studentdetail = new Table(7);// = new Table(columnWidths); // 2 rows, 2 columns
			studentdetail.setWidths(columnWidths);

			studentdetail.setBorderWidth(1);
			studentdetail.setBorderColor(new Color(0, 0, 255));
			studentdetail.setPadding(5);
			// studentdetail.setSpacing(5);
			studentdetail.setWidth(100);

			// studentdetail = new PdfPTable(new float[] {0.5f,3, 2, 3,2,2,2 });

			// studentdetail.setWidthPercentage(100);
			cell = new Cell(new Phrase("S.N"));
			cell.setBorderColor(new Color(0, 0, 255));
			cell.setBorderWidth(1);
			cell.setHeader(true);

			studentdetail.addCell(cell);

			cell = new Cell(new Phrase("Name of student"));
			cell.setBorderColor(new Color(0, 0, 255));

			cell.setBorderWidth(1);
			cell.setHeader(true);

			studentdetail.addCell(cell);

			cell = new Cell(new Phrase("Roll No."));
			cell.setBorderColor(new Color(0, 0, 255));

			cell.setBorderWidth(1);
			cell.setHeader(true);

			studentdetail.addCell(cell);

			cell = new Cell(new Phrase("Passed Examination"));
			cell.setBorderColor(new Color(0, 0, 255));

			cell.setHeader(true);
			cell.setBorderWidth(1);

			studentdetail.addCell(cell);

			cell = new Cell(new Phrase("Session"));
			cell.setBorderColor(new Color(0, 0, 255));

			cell.setHeader(true);
			cell.setBorderWidth(1);

			studentdetail.addCell(cell);

			cell = new Cell(new Phrase("Cumulative Grade Point Average"));
			cell.setBorderColor(new Color(0, 0, 255));

			cell.setHeader(true);
			cell.setBorderWidth(1);

			studentdetail.addCell(cell);

			cell = new Cell(new Phrase("Division"));
			cell.setBorderColor(new Color(0, 0, 255));

			cell.setHeader(true);
			cell.setBorderWidth(1);

			studentdetail.addCell(cell);

			// document.add(paragraph);
			document.add(agency);
			document.add(salute);
			document.add(dashline);

			int sno = 0;
			for (VerificationRollno rollno : rollnos) {

				// String enrolment=String.valueOf(enr.getEnrolmentno());

				List<Verification> results = em.createNamedQuery("getstudentdetail1")
						.setParameter("rollno", rollno.getRollno()).getResultList();

				// results.forEach(x ->
				// {System.out.println(x.getEnrollment_number()+x.getProgram_name());});
				// studentdetail = new PdfPTable(new float[] {0.5f,3, 2, 3,2,2,2 });

				String program = "";

				for (Verification studentdet : results) {

					program = "";

					sno++;
					cell = new Cell(new Phrase(String.valueOf(sno)));
					cell.setBorderWidth(1);

					cell.setBorderColor(new Color(0, 0, 255));

					studentdetail.addCell(cell);

					cell = new Cell(new Phrase(studentdet.getStudent_first_name()));
					cell.setBorderWidth(1);

					cell.setBorderColor(new Color(0, 0, 255));

					studentdetail.addCell(cell);

					cell = new Cell(new Phrase(studentdet.getRoll_number()));
					cell.setBorderWidth(1);

					cell.setBorderColor(new Color(0, 0, 255));

					studentdetail.addCell(cell);
					program = studentdet.getProgram_name();

					if (!(studentdet.getBranch().equalsIgnoreCase("NONE"))) {
						program = program + " " + studentdet.getBranch();
					}

					if (!(studentdet.getSpeclization().equalsIgnoreCase("NONE"))) {
						program = program + " " + studentdet.getSpeclization();
					}
					cell = new Cell(new Phrase(program));
					cell.setBorderWidth(1);

					cell.setBorderColor(new Color(0, 0, 255));

					studentdetail.addCell(cell);

					// print session
					String session = studentdet.getPassed_from_session().toString();
					session = session.substring(0, 4);
					String toyear = String.valueOf(Integer.parseInt(session.substring(2, 4)) + 1);
					session = session + "-" + toyear;
					cell = new Cell(new Phrase(session));
					cell.setBorderWidth(1);

					cell.setBorderColor(new Color(0, 0, 255));
					studentdetail.addCell(cell);

					cell = new Cell(new Phrase(studentdet.getCgpa().toString()));
					cell.setBorderWidth(1);

					cell.setBorderColor(new Color(0, 0, 255));
					studentdetail.addCell(cell);

					// Print Division
					cell = new Cell(new Phrase(studentdet.getDivision()));

					cell.setBorderWidth(1);

					cell.setBorderColor(new Color(0, 0, 255));
					studentdetail.addCell(cell);

				}

			}

			document.add(studentdetail);

			document.close();
//			

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

		// PdfWriter writer = PdfWriter.getInstance(document, new
		// FileOutputStream(directory + sep + input.getRequestNo()+ ".pdf"));

		return filepath;
	}

	protected class Custompageevent extends PdfPageEventHelper {
		int pagenumber = 0;

		@Override
		public void onStartPage(PdfWriter writer, Document document) {
			try {
				pagenumber++;
				document.add(new Phrase("\n\n\n"));

				String filename = "/images/deiLogoHeader.png";
				// File file = new File(getClass().getResource(filename).getFile());

				Image headerimg = Image.getInstance(
						java.awt.Toolkit.getDefaultToolkit().createImage(getClass().getResource(filename)), null);

				headerimg.setAlignment(Element.ALIGN_MIDDLE);
				headerimg.setAbsolutePosition(0, 750);
				headerimg.scalePercent(25f, 16f);

				writer.getDirectContent().addImage(headerimg);

			} catch (Exception x) {
				x.printStackTrace();
				System.out.println("error in start page " + x.getMessage());
			}
		}

//		@Override
//		public void onEndPage(PdfWriter writer, Document document) {
//			
//			Rectangle rect = writer.getBoxSize("art");
//		
//		ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_RIGHT,
//				new Phrase(String.format("page %d", pagenumber)), rect.getRight(), rect.getLeft(), 0);
//		}
//		

	}

	@Override
	public void deletenRollno(int id) {
		// TODO Auto-generated method stub
		verificationRollnoRepository.deleteById(id);

	}

}
