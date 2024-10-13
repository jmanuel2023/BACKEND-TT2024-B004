package com.example.tt2024b004.skincanbe.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.tt2024b004.skincanbe.model.Especialista;
import com.example.tt2024b004.skincanbe.model.Paciente;
import com.example.tt2024b004.skincanbe.model.TokenVerificacion;
import com.example.tt2024b004.skincanbe.model.Usuario;
import com.example.tt2024b004.skincanbe.repository.TokenVerificacionRepository;
import com.example.tt2024b004.skincanbe.repository.UsuarioRepository;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private TokenVerificacionRepository tkVrfRepository;

    @Autowired
    private JavaMailSender mailSender;

    
    //Servicio para obtener un usuario con un cierto correo
    public boolean existsByCorreo(String correo){
        return usuarioRepository.existsByCorreo(correo);
    }
    // Obtener todos los usuarios. Aqui usamos un metodo de CrudRepository
    public List<Usuario> obtenerTodosLosUsuarios() {
        return usuarioRepository.findAll();
    }

    // Guardar o actualizar un usuario
    @Transactional
    public Usuario guardarUsuario(Usuario usuario) throws MessagingException {
        //usuario.setStatus("Pendiente");
        Usuario regtmpUsuario = usuarioRepository.save(usuario);
        System.out.println("Usuario a guardar: " + regtmpUsuario);
        crearTokenVerificacion(regtmpUsuario);
        return usuarioRepository.save(regtmpUsuario);
    }
    @Transactional
    public void crearTokenVerificacion(Usuario usuario) throws MessagingException{
        TokenVerificacion tk= new TokenVerificacion(usuario);
        tkVrfRepository.save(tk);
        enviarCorreoValidacion(usuario, tk.getToken());
    }

    public void enviarCorreoValidacion(Usuario usuario, String token) throws MessagingException {
        MimeMessage mensaje = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mensaje, true);

        helper.setTo(usuario.getCorreo());
        helper.setSubject("SKINCANBE: VALIDA TU CORREO ELECTRONICO");

        String contenidoHtml = "<html>"
                + "<body>"
                + "<h1 style='color: #2e6c80;'>Valida tu correo electrónico</h1>"
                + "<p>Hola, " + usuario.getNombre() + "</p>"
                + "<p>Gracias por registrarte en SkinCanBe. Haz clic en el siguiente enlace para validar tu correo electrónico:</p>"
                + "<a href='http://192.168.100.63:8080/validar?token=" + token + "' style='padding: 10px 20px; background-color: #4CAF50; color: white; text-decoration: none;'>Validar correo</a>"
                + "<p>Si no solicitaste este registro, puedes ignorar este mensaje.</p>"
                + "<p>Saludos,</p>"
                + "<p>Equipo de SkinCanBe</p>"
                + "</body>"
                + "</html>";

        // Configura el contenido como HTML
        helper.setText(contenidoHtml, true);

        // Enviar el correo
        mailSender.send(mensaje);
    }
    @Transactional
    public Usuario actualizarUsuario(Usuario usuario){
        return usuarioRepository.save(usuario); 
    }

    // Obtener usuarios por nombre. Aqui usamos un metodo de UsuarioRepository
    public List<Usuario> obtenerUsuariosPorNombre(String nombre){
        return usuarioRepository.findByNombre(nombre);
    }

    // Obtener un especialista por cedula. Aqui usamos un metodo de UsuarioRepository
    public Especialista obtenerEspecialistaPorCedula (String cedula){
        List<Especialista> especialista = usuarioRepository.findByCedula(cedula);
        return especialista.isEmpty() ? null: especialista.get(0);
    }

    // Obtener todos los pacientes. Aqui usamos un metodo de UsuarioRepository
    public List<Paciente> obtenerTodosLosPacientes() {
        return usuarioRepository.findAllPacientes();
    }
}
