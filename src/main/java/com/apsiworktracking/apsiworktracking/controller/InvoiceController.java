package com.apsiworktracking.apsiworktracking.controller;


import com.apsiworktracking.apsiworktracking.model.Job;
import com.apsiworktracking.apsiworktracking.service.InvoiceService;
import com.itextpdf.text.DocumentException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

@CrossOrigin(origins={"https://ashy-ground-0223e9e03.1.azurestaticapps.net", "http://localhost:4200"})
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/invoice")
public class InvoiceController {

    @Autowired
    private InvoiceService invoiceService;

    @GetMapping(path = "/get", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<InputStreamResource> getInvoice () throws FileNotFoundException, DocumentException, IOException {
        ByteArrayInputStream bis = invoiceService.getInvoice();
        var headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=jobsreport.pdf");

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(bis));
    }
}
