/**
 ********************************
 ** Proyecto: Skincanbe        **
 ** Integrantes:               **
 ** Joan Hanzka Manuel Morales **
 ** Angelo Mihaelle Ojeda Gomez**
 ** Israel Rodriguez Juarez     **
 ******************************** 
 * Descripción: Clase servicio para implementar toda la lógica de negocios de las tareas que realiza la tabla Usuario.
 */
package com.example.tt2024b004.skincanbe.services.usuario;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.tt2024b004.skincanbe.model.usuario.Especialista;
import com.example.tt2024b004.skincanbe.model.usuario.Paciente;
import com.example.tt2024b004.skincanbe.model.usuario.TokenVerificacion;
import com.example.tt2024b004.skincanbe.model.usuario.Usuario;
import com.example.tt2024b004.skincanbe.repository.usuario.TokenVerificacionRepository;
import com.example.tt2024b004.skincanbe.repository.usuario.UsuarioRepository;

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

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Optional<Usuario> findById(Long id) {
        return usuarioRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<Especialista> obtenerTodosLosEspecialistas() {
        return usuarioRepository.findSpecialist();

    }


    @Transactional(readOnly = true)
    public List<Especialista> obtenerEspecialistasPorNomYCed(String filtro) {
        System.out.println(filtro);
        return usuarioRepository.findSpecialistByNombreYCedula(filtro);
    }

    

    public Usuario resetNewPassword(String correo, String newPassword) {
        System.out.println("Entre al metodo resetNewPassword del servicio de usuario");
        Usuario usuario = usuarioRepository.encontrarCorreo(correo);
        System.out.println(usuario);
        if (usuario == null) {
            System.out.println("Entre al if de usuario null, del servicio de usuario");
            return null;
        } else {
            System.out.println("Entre al else del usuario null, del servicio de usuario");
            usuario.setPassword(passwordEncoder.encode(newPassword));
            return usuarioRepository.save(usuario);
        }

    }

    // Servicio para obtener un usuario con un cierto correo
    public boolean existsByCorreo(String correo) {
        return usuarioRepository.existsByCorreo(correo);
    }

    // Obtener todos los usuarios. Aqui usamos un metodo de CrudRepository
    public List<Usuario> obtenerTodosLosUsuarios() {
        return usuarioRepository.findAll();
    }

    // Guardar o actualizar un usuario
    @Transactional
    public Usuario guardarUsuario(Usuario usuario) throws MessagingException {
        // usuario.setStatus("Pendiente");
        Usuario regtmpUsuario = usuarioRepository.save(usuario);
        System.out.println("Usuario a guardar: " + regtmpUsuario);
        crearTokenVerificacion(regtmpUsuario);
        return usuarioRepository.save(regtmpUsuario);
    }

    @Transactional
    public void crearTokenVerificacion(Usuario usuario) throws MessagingException {
        TokenVerificacion tk = new TokenVerificacion(usuario);
        tkVrfRepository.save(tk);
        System.out.println("Se creo el token");
        enviarCorreoValidacion(usuario, tk.getToken());
    }

    public void enviarCorreoValidacion(Usuario usuario, String token) throws MessagingException {
        System.out.println("Entro al metodo de envio de correo");
        MimeMessage mensaje = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mensaje, true);

        helper.setTo(usuario.getCorreo());
        helper.setSubject("SKINCANBE: VALIDA TU CORREO ELECTRONICO");

        String contenidoHtml = "<html>"
                + "<body>"
                + "<h1 style='color: #2e6c80;'>Valida tu correo electrónico</h1>"
                + "<p>Hola, " + usuario.getNombre() + "</p>"
                + "<p>Gracias por registrarte en SkinCanBe. Haz clic en el siguiente enlace para validar tu correo electrónico:</p>"
                + "<a href='http://192.168.100.63:8080/validar?token=" + token
                + "' style='padding: 10px 20px; background-color: #4CAF50; color: white; text-decoration: none;'>Validar correo</a>"
                + "<p>Si no solicitaste este registro, puedes ignorar este mensaje.</p>"
                + "<p>Saludos,</p>"
                + "<p>Equipo de SkinCanBe</p>"
                + "</body>"
                + "</html>";

        System.out.println("Ya lo construyo" + contenidoHtml);
        // Configura el contenido como HTML
        helper.setText(contenidoHtml, true);

        // Enviar el correo
        mailSender.send(mensaje);
        System.out.println("Ya se envio");
    }

    @Transactional
    public Usuario actualizarUsuario(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    public Usuario crearUsuario(Map<String, Object> payload) throws MessagingException {
        String tipoUsuario = (String) payload.get("tipo_usuario");
        Usuario usuario;

        if ("Especialista".equals(tipoUsuario)) {
            Especialista especialista = new Especialista();
            especialista.setCedula((String) payload.get("cedula"));
            usuario = especialista;
        } else if ("Paciente".equals(tipoUsuario)) {
            usuario = new Paciente();
        } else {
            usuario = new Usuario();
        }
        // Verificamos si el correo ya existe, ya que debe de ser unico.
        if (existsByCorreo((String) payload.get("correo"))) {
            throw new IllegalArgumentException("Esta correo ya esta registrado!");
        } else {
            int edadN = (Integer) payload.get("edad");
            usuario.setNombre((String) payload.get("nombre"));
            usuario.setApellidos((String) payload.get("apellidos"));
            usuario.setEdad(edadN);
            usuario.setCorreo((String) payload.get("correo"));
            String passwordN = (String) payload.get("password");
            String passwordEncoded = passwordEncoder.encode(passwordN);
            usuario.setPassword(passwordEncoded);

            // Guardar el usuario en la base de datos
            return guardarUsuario(usuario);
        }
    }

    public void sendPasswordResetEmail(Usuario usuario, String token) throws MessagingException {
        MimeMessage mensaje = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mensaje, true);

        helper.setTo(usuario.getCorreo());
        helper.setSubject("SKINCANBE: RECUPERACIÓN DE CONTRASEÑA");

        String contenidoHtml = "<html>"
                + "<body>"
                + "<h1 style='color: #2e6c80;'>Recupera tu contraseña</h1>"
                + "<p>Hola, " + usuario.getNombre() + "</p>"
                + "<p>Gracias por recuperar tu contraseña. SkinCanbe agradece que te preocupas por tu piel<p>"
                + " <p>Haz clic en el siguiente enlace para recuperar tu contraseña:</p>"
                + "<a href='http://192.168.100.63:8080/reset-password?token=" + token
                + "' style='padding: 10px 20px; background-color: #4CAF50; color: white; text-decoration: none;'>Recuperar mi contraseña</a>"
                + "<p>Si no solicitaste esta recuperación de contraseña, puedes ignorar este mensaje.</p>"
                + "<p>Saludos,</p>"
                + "<p>Equipo de SkinCanBe</p>"
                + "</body>"
                + "</html>";

        // Configura el contenido como HTML
        helper.setText(contenidoHtml, true);

        // Enviar el correo
        mailSender.send(mensaje);
    }

    public Usuario existsByEmailUsuario(String correo) {
        return usuarioRepository.encontrarCorreo(correo);
    }
}
