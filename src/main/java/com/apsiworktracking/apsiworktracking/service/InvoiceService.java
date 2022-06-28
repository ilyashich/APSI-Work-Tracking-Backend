package com.apsiworktracking.apsiworktracking.service;


import com.apsiworktracking.apsiworktracking.model.InvoiceJob;
import com.apsiworktracking.apsiworktracking.model.Job;
import com.apsiworktracking.apsiworktracking.model.ShortJob;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


@Service
@RequiredArgsConstructor
public class InvoiceService {

    @Autowired
    private JobService jobService;

    public ByteArrayInputStream getInvoice () throws DocumentException, IOException {
        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        PdfWriter writer = PdfWriter.getInstance(document, out);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -1);
        Date date = calendar.getTime();
        SimpleDateFormat format = new SimpleDateFormat("MM/yyyy");
        String dateString = format.format(date);
        String invoice = "Faktura za miesiąc " + dateString;



        PdfPTable table = new PdfPTable(3);
        table.setWidthPercentage(60);
        table.setWidths(new int[]{1, 3, 3});

        BaseFont helvetica = BaseFont.createFont(BaseFont.HELVETICA_BOLD, BaseFont.CP1250, BaseFont.EMBEDDED);
        Font headFont=new Font(helvetica,16);

        BaseFont row = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1250, BaseFont.EMBEDDED);
        Font fontRow =new Font(row,12);

//        Font headFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD);



        PdfPCell hcell;
        hcell = new PdfPCell(new Phrase("Nazwa", headFont));
        hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(hcell);

        hcell = new PdfPCell(new Phrase("Opis", headFont));
        hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(hcell);

        hcell = new PdfPCell(new Phrase("Cena", headFont));
        hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(hcell);

        InvoiceJob invoiceJob = jobService.getJobsForInvoice();

        for (ShortJob job : invoiceJob.getJobs()) {

            PdfPCell cell;

            cell = new PdfPCell(new Phrase(job.getName(), fontRow));
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(job.getDescription(), fontRow));
            cell.setPaddingLeft(5);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(String.valueOf(job.getPrice()), fontRow));
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setPaddingRight(5);
            table.addCell(cell);
        }

        document.open();
        Paragraph para1 = new Paragraph(invoice);
        para1.setSpacingAfter(50);
        document.add(para1);

        document.add(table);

        String sum = "Suma: " + String.valueOf(invoiceJob.getSum());

        Paragraph para2 = new Paragraph(sum);
        para2.setSpacingBefore(50);
        document.add(para2);

        document.close();
        writer.close();
        return new ByteArrayInputStream(out.toByteArray());

    }
}
