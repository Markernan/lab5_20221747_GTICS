package com.example.lab5.controller;

import com.example.lab5.Entity.Usuario;
import com.example.lab5.Entity.Mensaje;
import com.example.lab5.dto.UsuarioRankingDto;
import com.example.lab5.repository.UsuarioRepository;
import com.example.lab5.repository.MensajeRepository;
import com.example.lab5.repository.RankingRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import java.util.List;
import java.util.ArrayList;
import java.util.Optional;

import jakarta.validation.Valid;
import java.util.List;
import java.util.ArrayList;
import java.util.Optional;

@Controller
@RequestMapping("/usuario")
public class UsuarioController {
    private final UsuarioRepository usuarioRepository;
    private final MensajeRepository mensajeRepository;
    private final RankingRepository rankingRepository;

    public UsuarioController(UsuarioRepository usuarioRepository, MensajeRepository mensajeRepository, RankingRepository rankingRepository) {

        this.usuarioRepository = usuarioRepository;

        this.mensajeRepository = mensajeRepository;

        this.rankingRepository = rankingRepository;
    }

    @GetMapping(value = {"", "/", "list"})
    public String listarEmpleados(Model model) {
        model.addAttribute("listaEmpleados", usuarioRepository.findAll());
        model.addAttribute("listaEmpleadosPorRegion", usuarioRepository);
        model.addAttribute("listaEmpleadosPorPais", usuarioRepository);
        return "usuario/list";
    }

    @GetMapping(value = {"", "/"})
    public String inicio(Model model, HttpSession session) {
        Usuario usuarioActivo = (Usuario) session.getAttribute("usuarioLogueado");

        if (usuarioActivo == null) {
            return "redirect:/login";
        }

        model.addAttribute("usuarios", usuarioRepository.buscarTodosOrdenadosPorNombre());
        model.addAttribute("usuarioLogueado", usuarioActivo);
        return "index";
    }

    @GetMapping("/login")
    public String mostrarLogin(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "login";
    }

    @PostMapping("/login")
    public String procesarLogin(@RequestParam String correo,
                                @RequestParam String contrasena,
                                HttpSession session,
                                RedirectAttributes redirectAttributes) {

        Optional<Usuario> usuarioBuscado = usuarioRepository.buscarPorCorreo(correo);

        if (usuarioBuscado.isPresent() && usuarioBuscado.get().getContrasena().equals(contrasena)) {
            session.setAttribute("usuarioLogueado", usuarioBuscado.get());
            redirectAttributes.addFlashAttribute("msg", "Bienvenido " + usuarioBuscado.get().getNombre());
            return "redirect:/";
        } else {
            redirectAttributes.addFlashAttribute("error", "Correo o contraseña incorrectos");
            return "redirect:/login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session, RedirectAttributes redirectAttributes) {
        session.invalidate();

        redirectAttributes.addFlashAttribute("msg", "Sesión cerrada exitosamente");
        return "redirect:/login";
    }

    @GetMapping("/registro")
    public String mostrarRegistro(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "registro";
    }

    @PostMapping("/registro")
    public String registrarUsuario(@Valid @ModelAttribute("usuario") Usuario usuarioNuevo,
                                   BindingResult bindingResult,
                                   HttpSession session,
                                   RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "registro";
        }

        try {
            Usuario usuarioCreado = usuarioRepository.save(usuarioNuevo);
            // Autenticación inmediata tras registro exitoso
            session.setAttribute("usuarioLogueado", usuarioCreado);
            redirectAttributes.addFlashAttribute("msg", "Usuario registrado exitosamente. ¡Bienvenido " + usuarioCreado.getNombre() + "!");
            return "redirect:/";
        } catch (Exception e) {
            bindingResult.rejectValue("correo", "error.usuario", "El correo ya está registrado");
            return "registro";
        }
    }

    @GetMapping("/enviar")
    public String mostrarEnviarMensaje(@RequestParam(required = false) Integer idDestinatario,
                                       HttpSession session,
                                       Model model) {
        Usuario usuarioActivo = (Usuario) session.getAttribute("usuarioLogueado");

        if (usuarioActivo == null) {
            return "redirect:/login";
        }

        model.addAttribute("mensaje", new Mensaje());
        model.addAttribute("usuarios", usuarioRepository.buscarTodosOrdenadosPorNombre());
        model.addAttribute("usuarioLogueado", usuarioActivo);

        if (idDestinatario != null) {
            Optional<Usuario> destinatarioSeleccionado = usuarioRepository.findById(idDestinatario);
            if (destinatarioSeleccionado.isPresent()) {
                model.addAttribute("destinatarioSeleccionado", destinatarioSeleccionado.get());
            }
        }

        return "enviar-mensaje";
    }

    @PostMapping("/enviar")
    public String enviarMensaje(@Valid @ModelAttribute("mensaje") Mensaje mensaje,
                                BindingResult bindingResult,
                                @RequestParam("destinatarioId") int idDestinatario,
                                @RequestParam("tipoRegalo") String tipoRegalo,
                                @RequestParam(value = "colorCarrito", required = false) String colorCarrito,
                                HttpSession session,
                                RedirectAttributes redirectAttributes,
                                Model model) {

        Usuario usuarioActivo = (Usuario) session.getAttribute("usuarioLogueado");

        if (usuarioActivo == null) {

            return "redirect:/login";
        }

        // Filtrar contenido no apropiado
        if (mensaje.getContenido() != null &&
                (mensaje.getContenido().toLowerCase().contains("odio") ||
                        mensaje.getContenido().toLowerCase().contains("feo"))) {
            bindingResult.rejectValue("contenido", "error.mensaje", "El mensaje no puede contener palabras inapropiadas");
        }

        if (bindingResult.hasErrors()) {

            model.addAttribute("usuarios", usuarioRepository.buscarTodosOrdenadosPorNombre());
            model.addAttribute("usuarioLogueado", usuarioActivo);

            return "enviar-mensaje";

        }

        Optional<Usuario> destinatarioElegido = usuarioRepository.findById(idDestinatario);

        if (destinatarioElegido.isPresent()) {
            mensaje.setRemitente(usuarioActivo); // El que envía es siempre el usuario autenticado
            mensaje.setDestinatario(destinatarioElegido.get());
            mensaje.setRegaloTipo(Mensaje.TipoRegalo.valueOf(tipoRegalo));

            if ("Flor".equals(tipoRegalo)) {

                mensaje.setRegaloColor("Amarillo");

            } else if ("Carrito".equals(tipoRegalo) && colorCarrito != null) {
                mensaje.setRegaloColor(colorCarrito);
            }

            mensajeRepository.save(mensaje);
            redirectAttributes.addFlashAttribute("msg", "Mensaje enviado exitosamente a " + destinatarioElegido.get().getNombre());
        } else {
            redirectAttributes.addFlashAttribute("error", "Error al enviar el mensaje");
        }

        return "redirect:/";
    }

    @GetMapping("/ranking")
    public String mostrarRanking(Model model, HttpSession session) {
        Usuario usuarioActivo = (Usuario) session.getAttribute("usuarioLogueado");

        if (usuarioActivo == null) {

            return "redirect:/login";
        }

        List<Object[]> resultadosConsulta = rankingRepository.obtenerUsuariosConConteoRegalos();
        List<UsuarioRankingDto> clasificacion = new ArrayList<>();

        for (Object[] registro : resultadosConsulta) {
            Usuario usuario = (Usuario) registro[0];
            Long totalRegalos = (Long) registro[1];

            UsuarioRankingDto elementoRanking = new UsuarioRankingDto();
            elementoRanking.setId(usuario.getId());

            elementoRanking.setNombre(usuario.getNombre());


            elementoRanking.setApellido(usuario.getApellido());

            elementoRanking.setCorreo(usuario.getCorreo());
            elementoRanking.setDescripcion(usuario.getDescripcion());
            elementoRanking.setTotalRegalos(totalRegalos);

            clasificacion.add(elementoRanking);
        }

        model.addAttribute("ranking", clasificacion);

        model.addAttribute("usuarioLogueado", usuarioActivo);
        return "ranking";
    }

    @GetMapping("/mensajes-recibidos")
    public String mostrarMensajesRecibidos(@RequestParam int usuarioId, Model model, HttpSession session) {
        Usuario usuarioActivo = (Usuario) session.getAttribute("usuarioLogueado");

        if (usuarioActivo == null) {
            return "redirect:/login";
        }

        Optional<Usuario> usuarioBuscado = usuarioRepository.findById(usuarioId);

        if (usuarioBuscado.isPresent()) {

            List<Mensaje> mensajesRecibidos = mensajeRepository.obtenerMensajesPorDestinatario(usuarioId);
            Long totalMensajes = mensajeRepository.contarMensajesPorDestinatario(usuarioId);

            model.addAttribute("usuario", usuarioBuscado.get());

            model.addAttribute("mensajes", mensajesRecibidos);

            model.addAttribute("totalMensajes", totalMensajes);
            model.addAttribute("usuarioLogueado", usuarioActivo);
        }

        return "mensajes-recibidos";
    }
}

