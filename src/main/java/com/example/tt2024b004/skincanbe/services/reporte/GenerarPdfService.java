package com.example.tt2024b004.skincanbe.services.reporte;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.springframework.stereotype.Service;

import com.example.tt2024b004.skincanbe.model.Lesion.Lesion;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;



@Service
public class GenerarPdfService {

    public ByteArrayOutputStream generarPdfDeLesion(Lesion lesion) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        // Crear el PDF Writer
        PdfWriter writer = new PdfWriter(baos);
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document document = new Document(pdfDoc);

        // Añadir contenido al PDF
        document.add(new Paragraph("Reporte de Lesion"));
        document.add(new Paragraph("Nombre de la Lesión: " + lesion.getNombre_lesion()));
        document.add(new Paragraph("Descripción: " + lesion.getDescripcion()));

        // Agregar imagen (si existe)
        if (lesion.getImagen() != null) {
            String imagePath = "src/main/resources/static/images/" + lesion.getImagen(); // Ruta a la imagen guardada
            ImageData imageData = ImageDataFactory.create(imagePath);
            Image image = new Image(imageData);
            document.add(image);
        }

        // Cerrar el documento
        document.close();

        return baos; // Devolvemos el PDF en un stream
    }
}
