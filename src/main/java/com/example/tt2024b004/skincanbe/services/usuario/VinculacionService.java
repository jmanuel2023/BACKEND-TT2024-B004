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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.tt2024b004.skincanbe.model.usuario.Vinculacion;
import com.example.tt2024b004.skincanbe.repository.usuario.VinculacionRepository;

@Service
public class VinculacionService {

    @Autowired
    private VinculacionRepository vinculacionRepository;

    public Vinculacion crearVinculo(Vinculacion vinculo){
        System.out.println("Entre al metodo crearVinculo del servicio");
        return vinculacionRepository.save(vinculo);
    }

    public Vinculacion actualizarEstadoVinculacion(Long id, EstadoVinculacion nuevoEstado) {
        Vinculacion vinculo = vinculacionRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Vinculación no encontrada"));
        vinculo.setStatus(nuevoEstado);
        return vinculacionRepository.save(vinculo);
    }


}
