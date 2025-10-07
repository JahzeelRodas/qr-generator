package com.generador.qr_generator_api.controller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.generador.qr_generator_api.service.QRCodeService;
import com.google.zxing.WriterException;

import java.io.IOException;

@RestController
public class QRCodeController {

    private static final Logger logger = LoggerFactory.getLogger(QRCodeController.class);

    @Autowired
    private QRCodeService qrCodeService;

    @GetMapping(value = "/generate", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<?> generateQRCode(
            @RequestParam String content,
            @RequestParam(defaultValue = "300") int width,
            @RequestParam(defaultValue = "300") int height) throws WriterException {

        try {
            byte[] qrCodeImage = qrCodeService.generateQRCodeImage(content, width, height);
            logger.debug("Devolviendo imagen QR ({} bytes) al cliente.", qrCodeImage.length);

            return ResponseEntity.ok(qrCodeImage);

        } catch (IllegalArgumentException e) {
            logger.warn("Solicitud inválida del cliente: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage()); 

        } catch (IOException e) {
            logger.error("Error fatal en el servidor durante la generación del QR.", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno del servidor. Consulte los logs.");
        }
    }
}