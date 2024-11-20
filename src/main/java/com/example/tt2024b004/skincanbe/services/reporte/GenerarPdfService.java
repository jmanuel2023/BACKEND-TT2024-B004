/**
 ********************************
 ** Proyecto: Skincanbe        **
 ** Integrantes:               **
 ** Joan Hanzka Manuel Morales **
 ** Angelo Mihaelle Ojeda Gomez**
 ** Israel Rodriguez Juarez     **
 ******************************** 
 * Descripción: Clase servicio para la generación del reporte de la lesión en formato PDF. 
 */
package com.example.tt2024b004.skincanbe.services.reporte;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.tt2024b004.skincanbe.model.Lesion.Lesion;
import com.example.tt2024b004.skincanbe.model.Observacion.Observacion;
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

    public ByteArrayOutputStream generarPdfDeLesion(Lesion lesion, List<Observacion> listaObservaciones)
            throws IOException {
        System.out.println("ENTRE A DONDE SE HACE EL PDF");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        // Crear el PDF Writer
        PdfWriter writer = new PdfWriter(baos);
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document document = new Document(pdfDoc, PageSize.A4);

        // Se agregan variables de los estilos mas comunes en el pdf
        Text titulo = new Text("Reporte de la lesión").setBold().setFontSize(20);
        Text fechaActual = new Text("Fecha: " + LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        LineSeparator lineadivisora = new LineSeparator(new SolidLine(1f));

        // Aqui se llama al metodo para agregar el encabezado con el logo de Skincanbe y
        // con la fecha actual.
        addSeccionEncabezado(document, fechaActual);

        // Se agrega el titulo del reporte
        document.add(new Paragraph(titulo).setTextAlignment(TextAlignment.CENTER));

        // linea divisora
        document.add(lineadivisora);

        // Aqui se llama al metodo para agregar datos del paciente
        addSeccionDatosPaciente(document, lesion);

        // linea divisora
        document.add(lineadivisora);

        // Datos de la lesion
        addSeccionDatosLesion(document, lesion);
        document.add(lineadivisora);

        addSeccionObservaciones(document, listaObservaciones);
        document.add(lineadivisora);

        // Informacion adicional
        pdfDoc.addNewPage();

        addSeccionInfoAdic(document);

        document.add(lineadivisora);
        // Cerrar el documento
        document.close();

        return baos; // Devolvemos el PDF en un stream

    }

    private void addSeccionEncabezado(Document document, Text fechaActual) throws IOException {
        Image logo = new Image(ImageDataFactory.create("src/main/resources/static/logo.png")).scaleToFit(100, 100)
                .setHorizontalAlignment(HorizontalAlignment.LEFT);
        Paragraph header = new Paragraph().add(logo).add(new Paragraph(fechaActual));
        document.add(header);
    }

    private void addSeccionDatosPaciente(Document document, Lesion lesion) {
        document.add(new Paragraph("Datos del paciente: ").setBold().setFontSize(14));
        document.add(
                new Paragraph("Nombre: " + lesion.getUsuario().getNombre() + " " + lesion.getUsuario().getApellidos()));
        document.add(new Paragraph("Edad: " + lesion.getUsuario().getEdad()));
        document.add(new Paragraph("Correo electrónico: " + lesion.getUsuario().getCorreo()));

    }

    private void addSeccionDatosLesion(Document document, Lesion lesion) throws IOException {
        document.add(new Paragraph("Datos de la lesión").setBold().setFontSize(14));
        document.add(new Paragraph("Nombre: " + lesion.getNombre_lesion()));
        document.add(new Paragraph("Descripción: " + lesion.getDescripcion()));

        // Agregar imagen (si existe)
        if (lesion.getImagen() != null) {
            String imagePath = "src/main/resources/static/images/" + lesion.getImagen(); // Ruta a la imagen guardada
            ImageData imageData = ImageDataFactory.create(imagePath);
            Image image = new Image(imageData).scaleToFit(150, 150).setHorizontalAlignment(HorizontalAlignment.LEFT);
            document.add(image);
        }

    }

    private void addSeccionObservaciones(Document document, List<Observacion> observaciones) {
        document.add(new Paragraph("Historial de Observaciones").setBold().setFontSize(14));

        if (observaciones.isEmpty()) {
            document.add(new Paragraph("No hay observaciones registradas para esta lesión"));
        } else {
            for (Observacion observacion : observaciones) {
                // Se agrega detalles de cada observacion
                document.add(new Paragraph("Observación: " + observacion.getDescripcion()));
                document.add(new Paragraph("Fecha: " + observacion.getFecha()));
                document.add(new Paragraph("Realizada por: " + observacion.getUsuario().getNombre() + " "
                        + observacion.getUsuario().getApellidos()));
                document.add(new Paragraph(""));
            }
        }
    }

    private void addSeccionInfoAdic(Document document) {
        document.add(new Paragraph("Información Adicional").setBold().setFontSize(14));
        document.add(new Paragraph("CUIDADOS SOBRE LA PIEL:\r\n" + //
                "•\t1. Transforma en un hábito la higiene diaria de tu rostro. \r\n" + //
                "Es fundamental que limpies tu cara dos veces al día –por la mañana y antes de acostarte-, con el fin de eliminar toxinas y maquillaje. Para que esta limpieza sea efectiva y no agresiva, debes utilizar productos específicos para el rostro.\r\n"
                + //
                "•\t2. Nutre la piel en profundidad. \r\n" + //
                "La hidratación constituye otra de las claves de una piel sana y elástica, no sólo en el rostro, sino en todo el cuerpo. \r\n"
                + //
                "•\t3. Bebe agua de manera abundante. \r\n" + //
                "El agua mantiene la piel hidratada y te ayuda a eliminar toxinas. Por si fuera poco, contiene nutrientes, vitaminas y minerales excelentes para la piel como el calcio, el magnesio y el sodio. El truco está en ingerir líquidos regularmente y sin esperar a tener sed.\r\n"
                + //
                "•\t4. La edad influye. \r\n" + //
                "Las necesidades de tu piel evolucionan con la edad: si eres joven, demandará sobre todo hidratación y foto protección. Y conforme vas cumpliendo años, debes dedicarle más tiempo, cuidados antioxidantes y una buena dosis de nutrición..\r\n"
                + //
                "•\t5. Foto protección durante todo el año. \r\n" + //
                "No sólo en verano debes resguardar la piel de las radiaciones solares; también tienes que tomar precauciones el resto del año y en zonas de montaña. Junto a los correspondientes fotoprotectores de alta graduación, serán bienvenidas gafas de sol, gorros y sombreros. Las zonas de la cara donde más frecuentemente aparecen las lesiones provocadas por el sol son la nariz y los labios, por lo que deberás prestar especial atención para aplicar el fotoprotector y utilizar un protector labial con SPF alto.\r\n"
                + //
                "•\t6. Vigila la aparición de manchas. \r\n" + //
                "Frecuentemente producidas por una incorrecta exposición al sol, has de mantenerte alerta frente a las pigmentaciones con el fin de evitar su extensión u oscurecimiento. Sobre todo, no olvides que no existe mejor medicina que una buena prevención. También debes prestar atención a las pecas y lunares y, en caso de percibir variaciones importantes en su tamaño o color, acudir a un dermatólogo.\r\n"
                + //
                "•\t7. Mima tus manos. \r\n" + //
                "La delicada piel del dorso es una de las partes del cuerpo que más refleja la edad. Y al estar tan expuestas al sol deberás hidratar y proteger tus manos constantemente.\r\n"
                + //
                "PREVENCIONES PARA EVITAR EL DESARROLLO DE CÁNCER DE PIEL:\r\n" + //
                "•\tManténgase alejado del sol lo más posible entre las 10 de la mañana y las 4 de la tarde, cuando los rayos del sol son más fuertes\r\n"
                + //
                "•\tCúbrase con ropa de mangas largas, pantalones largos o falda larga, sombrero y gafas de sol\r\n" + //
                "•\tUse protector solar de factor 15 o más (SPF 15, en inglés)\r\n" + //
                "•\tNo use las máquinas de bronceado artificial\r\n" + //
                "•\tExaminar su piel regularmente puede ayudar a identificar cualquier nuevo crecimiento o área anormal, y mostrarlo a su médico antes de que tenga la probabilidad de convertirse en cáncer de piel.\r\n"
                + //
                "•\tLa exposición a ciertos químicos, tal como el arsénico, puede aumentar el riesgo de padecer cáncer de piel.\r\n"
                + //
                ""));
        document.add(new Paragraph(
                "SkinCanBe es una aplicación dedicada a ayudar en la detección temprana de lesiones en la piel. Este servicio tiene fines informativos y no reemplaza la consulta con un profesional de la salud. SkinCanBe utiliza un algoritmo basado en redes neuronales convolucionales entrenado con imágenes de lesiones cutáneas. Sin embargo, los resultados no constituyen un diagnostico medico definitivo y están sujetos a revisión profesional."));

    }
}
