package com.example.tt2024b004.skincanbe.services.reporte;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Service;

import com.example.tt2024b004.skincanbe.model.Lesion.Lesion;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.draw.SolidLine;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.LineSeparator;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.properties.HorizontalAlignment;
import com.itextpdf.layout.properties.TextAlignment;



@Service
public class GenerarPdfService {

    public ByteArrayOutputStream generarPdfDeLesion(Lesion lesion) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        // Crear el PDF Writer
        PdfWriter writer = new PdfWriter(baos);
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document document = new Document(pdfDoc, PageSize.A4);

        //Se agregan variables de los estilos mas comunes en el pdf
        Text titulo = new Text("Reporte de la lesión").setBold().setFontSize(20);
        Text fechaActual = new Text("Fecha"+ LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        LineSeparator lineadivisora = new LineSeparator(new SolidLine(1f));

        //Aqui se llama al metodo para agregar el encabezado con el logo de Skincanbe y con la fecha actual.
        addSeccionEncabezado(document, fechaActual);
        //Se agrega el titulo del reporte
        document.add(new Paragraph(titulo).setTextAlignment(TextAlignment.CENTER));
        //linea divisora
        document.add(lineadivisora);

        //Aqui se llama al metodo para agregar datos del paciente
        addSeccionDatosPaciente(document, lesion);
        //linea divisora
        document.add(lineadivisora);

        //Datos de la lesion
        addSeccionDatosLesion(document, lesion);
        document.add(lineadivisora);

        //Informacion adicional
        pdfDoc.addNewPage();
        addSeccionInfoAdic(document);

        document.add(lineadivisora);

        
        // Cerrar el documento
        document.close();

        return baos; // Devolvemos el PDF en un stream

    }

    private void addSeccionEncabezado(Document document, Text fechaActual) throws IOException {
        Image logo = new Image(ImageDataFactory.create("src/main/resources/images/logo/logo.png")).scaleToFit(100, 100).setHorizontalAlignment(HorizontalAlignment.LEFT);
        Paragraph header = new Paragraph().add(logo).add(new Paragraph(fechaActual));
        document.add(header);
    }
    private void addSeccionDatosPaciente(Document document, Lesion lesion){
        document.add(new Paragraph("Datos del paciente").setBold().setFontSize(14));
        document.add(new Paragraph("Nombre del paciente" + lesion.getUsuario().getNombre()+" "+ lesion.getUsuario().getApellidos()));
        document.add(new Paragraph("Edad" + lesion.getUsuario().getEdad()));
        document.add(new Paragraph("Correo electrónico del paciente" +lesion.getUsuario().getCorreo()));

    }
    private void addSeccionDatosLesion(Document document, Lesion lesion) throws IOException{
        document.add(new Paragraph("Datos de la lesión").setBold().setFontSize(14));
        document.add(new Paragraph("Nombre de la lesión"+ lesion.getNombre_lesion()));
        document.add(new Paragraph("Descripción de la lesión"+lesion.getDescripcion()));
    
        // Agregar imagen (si existe)
        if (lesion.getImagen() != null) {
            String imagePath = "src/main/resources/static/images/" + lesion.getImagen(); // Ruta a la imagen guardada
            ImageData imageData = ImageDataFactory.create(imagePath);
            Image image = new Image(imageData).scaleToFit(150,150).setHorizontalAlignment(HorizontalAlignment.LEFT);
            document.add(image);
        }
    
    }
    private void addSeccionInfoAdic(Document document){
        document.add(new Paragraph("Información Adicional").setBold().setFontSize(14));
        document.add(new Paragraph("Cuidados de la piel y prevención de enfermedades o cáncer de piel."));
        document.add(new Paragraph("SkinCanBe es una aplicación dedicada a ayudar en la detección temprana de lesiones en la piel..."));

    }
}
