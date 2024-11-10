package com.example.tt2024b004.skincanbe.controllers.reporte;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.example.tt2024b004.skincanbe.services.reporte.ReporteService;

@WebMvcTest(ReporteController.class)
@AutoConfigureMockMvc(addFilters = false)
public class ReporteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReporteService reporteService;

    @Test
    void testGeneraryenviarReporte_Success() throws Exception {
        Long lesionId = 1L;

        // No se necesita configurar el comportamiento de reporteService porque no se espera una excepción

        mockMvc.perform(post("/generar-reporte/" + lesionId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Reporte generado y enviado correctamente."));
    }

    @Test
    void testGeneraryenviarReporte_Error() throws Exception {
        Long lesionId = 1L;

        // Configurar el mock para que lance una excepción cuando se llame al método generarYEnviarReporte
        Mockito.doThrow(new RuntimeException("Error al generar el reporte")).when(reporteService).generarYEnviarReporte(lesionId);

        mockMvc.perform(post("/generar-reporte/" + lesionId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Error al generar o enviar el reporte."));
    }
}
