package com.example.tt2024b004.skincanbe.controllers.lesion;

/*Librerias para el uso de metodos estaticos de Mockito y Spring MVC*/
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*; 
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/*Librerias para el manejo de listas de objetos*/
import java.util.Arrays;
import java.util.List; 


/*Librerias para el uso de herramientas para pruebas unitarias y simulacion de dependencias*/
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/*Librerias para probar controladores web*/
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@WebMvcTest(LesionController.class) //Indica que que se esta probando un controlador web especifico
public class LesionControllerTest {

    @Autowired //Inyecta automaticamente el objeto MockMvc para realizar solicitudes HTTP simuladas.
    private MockMvc mockMvc;

    @Mock //Se crea un objeto simulado de LesionService
    private LesionService lesionService;

    @InjectMocks //Inyecta mocks en el controlador LesionController
    private LesionController lesionController;

    @BeforeEach //Con esta anotación, este metodo se ejecuta antes de cada prueba para inicializar los mocks y configurar MockMvc
    public void setup() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(lesionController).build();
    }

    @Test //Indica que este metodo es una prueba unitaria
    public void testRegistrarLesion() throws Exception {
        //Se crea un archivo simulado para esta prueba
        MockMultipartFile imagen = new MockMultipartFile("imagen", "imagen.jpg", "image/jpeg", "imagen".getBytes());
        //Se crea una instancia de Lesion para simular el retorno del servicio
        Lesion lesion = new Lesion();
        //Configura el comportamiento del mock lesionService para que devuelva la lesion simulada.
        when(lesionService.guardarLesion(anyLong(), anyString(), anyString(), any(MultipartFile.class))).thenReturn(lesion);

        //Se realiza una solicitud HTTP simulada
        //Se define la solicitud como multipart a la URL "/register-injury"
        mockMvc.perform(multipart("/register-injury") 
                //Se Añade el archivo simulado a la solicitud
                .file(imagen)
                //Se añeden parametros a la solicitud
                .param("id_usuario", "1")
                .param("nombre_lesion", "Lesion Test")
                .param("descripcion", "Descripcion Test")
                //Se define el tipo de contenido de la solicitud
                .contentType(MediaType.MULTIPART_FORM_DATA))
                //Se verifican las expectativas sobre las respuestas
                //Se espera un estado HTTP 201 (CREATED)
                .andExpect(status().isCreated())
                //Se verifica que el JSON de respuesta contiene un campo id.
                .andExpect(jsonPath("$.id").exists());
    }

    @Test
    public void testObtenerLesionesPorUsuarioId() throws Exception {
        //Se crea una lista simulada de lesiones
        List<Lesion> lesiones = Arrays.asList(new Lesion(), new Lesion());
        //Configura el mock lesionService para que devuelva la lista simulada.
        when(lesionService.obtenerLesionesPorUsuarioId(1L)).thenReturn(lesiones);
        
        //Se realiza la solicitud HTTP solicitada
        //Se define la solicitud como un GET a la URL /1
        mockMvc.perform(get("/1"))
        //Se verifican las expectativas sobre la respuesta
                //Se verifica que se espera un estado HTTP 200 (OK)
                .andExpect(status().isOk())
                //Se verifica que la respuesta es un array JSON
                .andExpect(jsonPath("$").isArray())
                //Se verifica que el primer elemento del array contiene un campo id.
                .andExpect(jsonPath("$[0].id").exists());
    }
}

