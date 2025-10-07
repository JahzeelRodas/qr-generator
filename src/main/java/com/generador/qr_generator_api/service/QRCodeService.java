package com.generador.qr_generator_api.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.slf4j.Logger; 
import org.slf4j.LoggerFactory; 
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
public class QRCodeService {

    
    private static final Logger logger = LoggerFactory.getLogger(QRCodeService.class);

    public byte[] generateQRCodeImage(String content, int width, int height)
            throws WriterException, IOException {
        
            logger.info("Generando QR para contenido: '{}' con dimensiones {}x{}", content, width, height);

    
        if (content == null || content.trim().isEmpty()) {
                logger.warn("El contenido del QR está vacío o nulo.");
            throw new IllegalArgumentException("El contenido a codificar no puede estar vacío.");
        }
        
        try {
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(content, BarcodeFormat.QR_CODE, width, height);
            
            ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
            
            logger.info("QR generado exitosamente. Tamaño del byte array: {} bytes", pngOutputStream.size());
            return pngOutputStream.toByteArray();

        } catch (WriterException e) {
            logger.error("Error al codificar el contenido '{}' en QR. Causa: {}", content, e.getMessage(), e);
            throw new IOException("Fallo en el proceso de codificación de Zxing.", e);
        }
    }
}