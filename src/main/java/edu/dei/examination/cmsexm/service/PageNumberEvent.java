package edu.dei.examination.cmsexm.service;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPageEvent;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;

public class PageNumberEvent implements PdfPageEvent {

    Font font = FontFactory.getFont(FontFactory.HELVETICA, 9, BaseColor.BLACK); // Default font
    private PdfTemplate totalPagesTemplate;
    private BaseFont baseFont;

    @Override
    public void onOpenDocument(PdfWriter writer, Document document) {
        // Initialize template and base font for the total pages
        totalPagesTemplate = writer.getDirectContent().createTemplate(50, 50);
        try {
            baseFont = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.WINANSI, BaseFont.NOT_EMBEDDED);
        } catch (Exception e) {
            throw new RuntimeException("Error initializing font: " + e.getMessage());
        }
    }

    @Override
    public void onStartPage(PdfWriter writer, Document document) {
        // No action required for onStartPage
    }

    @Override
    public void onEndPage(PdfWriter writer, Document document) {
        // Get the direct content of the PDF
        PdfContentByte cb = writer.getDirectContent();
        
        // Set the current page number text
        String currentPageText = "Page " + writer.getPageNumber() + " of ";

        // Align the page number text
        float x = document.right() - 50; // Adjust to control horizontal position
        float y = document.bottom() - 20; // Adjust to control vertical position

        // Write the current page number placeholder text on the PDF
        ColumnText.showTextAligned(cb, Element.ALIGN_RIGHT, new Phrase(currentPageText, font), x, y, 0);

        // Store the position where the total number of pages will be inserted later
        cb.addTemplate(totalPagesTemplate, x + 5, y); // Leave space for the total page number
    }

    @Override
    public void onCloseDocument(PdfWriter writer, Document document) {
        // Replace the placeholder with the actual total page number on closing the document
        totalPagesTemplate.beginText();
        totalPagesTemplate.setFontAndSize(baseFont, 9); // Use BaseFont for total page number
        totalPagesTemplate.setTextMatrix(0, 0);
        totalPagesTemplate.showText(String.valueOf(writer.getPageNumber()));
        totalPagesTemplate.endText();
    }

    // The following methods are optional and not used in this implementation but included to fulfill the PdfPageEvent interface
    @Override
    public void onParagraph(PdfWriter writer, Document document, float paragraphPosition) {}

    @Override
    public void onParagraphEnd(PdfWriter writer, Document document, float paragraphPosition) {}

    @Override
    public void onChapter(PdfWriter writer, Document document, float paragraphPosition, Paragraph title) {}

    @Override
    public void onChapterEnd(PdfWriter writer, Document document, float paragraphPosition) {}

    @Override
    public void onSection(PdfWriter writer, Document document, float paragraphPosition, int depth, Paragraph title) {}

    @Override
    public void onSectionEnd(PdfWriter writer, Document document, float paragraphPosition) {}

    @Override
    public void onGenericTag(PdfWriter writer, Document document, Rectangle rect, String text) {}
}
