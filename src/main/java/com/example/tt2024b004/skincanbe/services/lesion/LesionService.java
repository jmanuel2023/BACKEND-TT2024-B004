package com.example.tt2024b004.skincanbe.services.lesion;



import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.io.IOException;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.example.tt2024b004.skincanbe.model.Usuario;
import com.example.tt2024b004.skincanbe.model.Lesion.Lesion;
import com.example.tt2024b004.skincanbe.repository.UsuarioRepository;
import com.example.tt2024b004.skincanbe.repository.lesion.LesionRepository;

@Service
public class LesionService {
    private final String uploadDir = "src/main/resources/static/images"; 
    
    @Autowired
    private LesionRepository lesionRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    //Guardar lesion
    @Transactional
    public Lesion guardarLesion(Long idUsuario, String nombreLesion, String descripcion, MultipartFile imagen) throws IOException{
        
        Usuario usuario = usuarioRepository.findById(idUsuario).
        orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));


        Lesion lesion= new Lesion();

        lesion.setNombre_lesion(nombreLesion);
        lesion.setDescripcion(descripcion);

        if(!imagen.isEmpty()){
            String imageFileName = System.currentTimeMillis() + "_"+ imagen.getOriginalFilename();
            Path imagePath = Paths.get(uploadDir + imageFileName);
            Files.copy(imagen.getInputStream(), imagePath, StandardCopyOption.REPLACE_EXISTING);
            lesion.setImagen(imageFileName); 

        }

        lesion.setUsuario(usuario);

        System.out.println("Lesion a guardar: " + lesion);
        return lesionRepository.save(lesion);
    }

    @Transactional(readOnly = true)
    public List<Lesion> obtenerLesionesPorUsuarioId(Long usuarioId) {
        return lesionRepository.findByUsuario_Id(usuarioId);
    }

}
