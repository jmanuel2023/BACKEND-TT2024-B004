package com.example.tt2024b004.skincanbe.services.observacion;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.tt2024b004.skincanbe.DTO.ObservacionConUsuarioDTO;
import com.example.tt2024b004.skincanbe.model.Lesion.Lesion;
import com.example.tt2024b004.skincanbe.model.Observacion.Observacion;
import com.example.tt2024b004.skincanbe.model.usuario.Usuario;
import com.example.tt2024b004.skincanbe.repository.lesion.LesionRepository;
import com.example.tt2024b004.skincanbe.repository.observacion.ObservacionRepository;
import com.example.tt2024b004.skincanbe.repository.usuario.UsuarioRepository;

@Service
public class ObservacionService {

    @Autowired
    private ObservacionRepository observacionRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private LesionRepository lesionRepository;

    public Observacion agregarObservacion(Map<String,String> payload, Long lesion_id,Long id_especialista){
        String descripcion = (String) payload.get("descripcion");
        Observacion observacion = new Observacion();
        observacion.setDescripcion(descripcion);
        Optional<Lesion> lesion = lesionRepository.findById(lesion_id);
        if(lesion.isPresent()){
            System.out.println("Se ha encontrado la lesión");
            observacion.setLesion(lesion.get());
            Optional<Usuario> usuarioOpcional = usuarioRepository.findById(id_especialista);
            if (usuarioOpcional.isPresent()) { 
                System.out.println("Se ha encontrado el usuario");
                observacion.setUsuario(usuarioOpcional.get());
                observacion.setFecha(LocalDate.now());
                return observacionRepository.save(observacion);
            }
            else{
                System.out.println("No se encontro la lesion XD");
                return null;
            }
        }
        else{
            System.out.println("No se encontro la lesion XD");
            return null;
        }
    }

    public List<Observacion> obtenerHistorialObservaciones(Long lesion_id, List<Long> usuariosIds){
        return observacionRepository.findHistorialLesionesParaReportes(lesion_id, usuariosIds);
    }
    
    public List<Observacion> obtenerHistorialObservacionesParaPantalla(Long lesion_id, Long id_especialista){
        return observacionRepository.findHistorialLesionesParaHistorial(lesion_id, id_especialista);
    }
    public List<ObservacionConUsuarioDTO> obtenerHistorialObservacionesConUsuario(Long lesion_id) {
        List<Object[]> resultados = observacionRepository.findHistorialLesionesTotal(lesion_id);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); // Ajusta el formato según sea necesario
    
        List<ObservacionConUsuarioDTO> observacionesConUsuario = resultados.stream()
            .map(obj -> {
                Long idObservacion = ((Number) obj[0]).longValue();
                String descripcion = (String) obj[1];
                String fechaFormateada = null;
                
                // Convertimos java.sql.Date a String usando SimpleDateFormat
                if (obj[2] != null) {
                    fechaFormateada = dateFormat.format((Date) obj[2]);
                }
    
                String nombreUsuario = (String) obj[5];
                String apellidoUsuario = (String) obj[6];
    
                return new ObservacionConUsuarioDTO(idObservacion, descripcion, fechaFormateada, nombreUsuario, apellidoUsuario);
            })
            .collect(Collectors.toList());
    
        return observacionesConUsuario;
    }
    

}
