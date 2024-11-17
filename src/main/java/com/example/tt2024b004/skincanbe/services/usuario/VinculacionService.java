/**
 ********************************
 ** Proyecto: Skincanbe        **
 ** Integrantes:               **
 ** Joan Hanzka Manuel Morales **
 ** Angelo Mihaelle Ojeda Gomez**
 ** Israel Rodriguez Juarez     **
 ******************************** 
 * Descripción: Clase servicio para implementar toda la lógica de negocios de las tareas que realiza la tabla Vinculación
 */
package com.example.tt2024b004.skincanbe.services.usuario;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.tt2024b004.skincanbe.enums.EstadoVinculacion;
import com.example.tt2024b004.skincanbe.model.usuario.Usuario;
import com.example.tt2024b004.skincanbe.model.usuario.Vinculacion;
import com.example.tt2024b004.skincanbe.repository.usuario.VinculacionRepository;

@Service
public class VinculacionService {

    @Autowired
    private VinculacionRepository vinculacionRepository;

    public Vinculacion crearVinculo(Vinculacion vinculo){
        System.out.println("Entre al metodo crearVinculo del servicio");
        vinculo.setStatus(EstadoVinculacion.PENDIENTE);
        return vinculacionRepository.save(vinculo);
    }

    //Metodo para actualizar el estado de la vinculacion
    @Transactional
    public void actualizarEstadoVinculacion(Long pacienteId, Long especialistaId, EstadoVinculacion nuevoEstado) {
        Vinculacion vinculacion = vinculacionRepository.findByIds(pacienteId, especialistaId);
        if (vinculacion != null) {
            vinculacion.setStatus(nuevoEstado);
            vinculacionRepository.save(vinculacion);
        }
    }


    @Transactional(readOnly = true)
    public List<Vinculacion> obtenerEstadoVinculacion(Long pacienteId, Long especialistaId){
        return vinculacionRepository.findByIdentificadores(pacienteId, especialistaId);
    }
    public Vinculacion actualizarEstadoVinculacion(Long id, EstadoVinculacion nuevoEstado) {
        Vinculacion vinculo = vinculacionRepository.findById(id)
            .orElseThrow(() -> new NoSuchElementException("Vinculación no encontrada"));
        vinculo.setStatus(nuevoEstado);
        return vinculacionRepository.save(vinculo);
    }

   public List<Usuario> obtenerSolicitudesPendientes(Long especialistaId) {
    List<Vinculacion> vinculaciones = vinculacionRepository.findByEspecialistaIdAndStatus(especialistaId, EstadoVinculacion.PENDIENTE);
    return vinculaciones.stream()
                        .map(Vinculacion::getPaciente)  // Suponiendo que `getPaciente()` devuelve el objeto `Usuario` relacionado
                        .collect(Collectors.toList());
}



}
