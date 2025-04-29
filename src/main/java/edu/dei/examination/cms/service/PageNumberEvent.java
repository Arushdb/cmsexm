package edu.dei.examination.cms.service;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import java.net.URL;

public class PageNumberEvent implements PdfPageEvent {

    Font font = FontFactory.getFont(FontFactory.HELVETICA, 9, BaseColor.BLACK); // Default font
    private PdfTemplate totalPagesTemplate;
    private BaseFont baseFont;
    private String rollNumber; // Store the roll number dynamically
    private final String WATERMARK_IMAGE_PATH;

    // Constructor to accept roll_number
    public PageNumberEvent(String rollNumber) {
        this.rollNumber = rollNumber;
        // Use getClass().getResource() to dynamically locate the image on the classpath
        URL resourceUrl = getClass().getResource("/images/DEI-WATERMARK.jpg");
        if (resourceUrl != null) {
            this.WATERMARK_IMAGE_PATH = resourceUrl.toString();
        } else {
            throw new IllegalArgumentException("Watermark image not found in the specified path.");
        }
    }

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
        PdfContentByte cb = writer.getDirectContent();

        // Position for page number
        String currentPageText = "Page " + writer.getPageNumber() + " of ";
        float pageNumX = document.right() - 30; // Horizontal position for page number
        float pageNumY = document.bottom() - 60; // Adjusted vertical position for page number

        // Write current page number text
        ColumnText.showTextAligned(cb, Element.ALIGN_RIGHT, new Phrase(currentPageText, font), pageNumX, pageNumY, 0);
        cb.addTemplate(totalPagesTemplate, pageNumX + 5, pageNumY); // Add template for total page number

        // Add dynamic QR code at the bottom-left corner
        addDynamicQrCode(writer, document, pageNumY); // Pass the vertical position for alignment

        // Add watermark image
     //   addWatermark(writer, document);
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

    // Method to add dynamic QR Code (including roll_number)
    private void addDynamicQrCode(PdfWriter writer, Document document, float pageNumY) {
        try {
            // Generate QR Code content with dynamic roll_number
            String qrCodeText = "roll_number=" + rollNumber; // QR code with roll_number
            BarcodeQRCode barcodeQRCode = new BarcodeQRCode(qrCodeText, 80, 80, null);

            // Create image from the barcode
            Image qrCodeImage = barcodeQRCode.getImage();
            qrCodeImage.scaleAbsolute(50, 50); // Scale to appropriate size

            // Set QR code position on the bottom-left corner, ensuring it doesn't overlap content
            qrCodeImage.setAbsolutePosition(document.left() -5, pageNumY); // Align QR code with page number

            PdfContentByte canvas = writer.getDirectContent();
            canvas.addImage(qrCodeImage);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Method to add watermark on each page
  //  private void addWatermark(PdfWriter writer, Document document) {
   //     try {
   //         PdfContentByte canvas = writer.getDirectContentUnder();
   //         Image watermarkImage = Image.getInstance(WATERMARK_IMAGE_PATH);

            // Position watermark at the center of the page
    //        float x = (document.getPageSize().getWidth() - watermarkImage.getScaledWidth()) / 2;
     //       float y = (document.getPageSize().getHeight() - watermarkImage.getScaledHeight()) / 2;
     //       watermarkImage.setAbsolutePosition(x, y);
//            watermarkImage.scaleToFit(300, 300); // Adjust the size if necessary

            // Add the watermark to the canvas
       //     canvas.addImage(watermarkImage);

       // } catch (Exception e) {
      //      e.printStackTrace();
     //   }
  //  }

    // The following methods are optional and not used in this implementation
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
