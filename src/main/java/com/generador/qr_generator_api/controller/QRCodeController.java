package com.generador.qr_generator_api.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.generador.qr_generator_api.service.QRCodeService;
import com.google.zxing.WriterException;

@RestController
public class QRCodeController {
    @Autowired
    private QRCodeService qrCodeService;
    @GetMapping(value = "/generate", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> generateQRCode(@RequestParam String content,
            @RequestParam(defaultValue = "200") int width,
            @RequestParam(defaultValue = "200") int height) {
        try {
            byte[] qrCodeImage = qrCodeService.generateQRCodeImage(content, width, height);
            return ResponseEntity.ok(qrCodeImage);
        } catch (WriterException | IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
