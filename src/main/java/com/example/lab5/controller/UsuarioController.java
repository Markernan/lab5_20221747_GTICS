package com.example.lab5.controller;

import com.example.lab5.Entity.Mensaje;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.example.lab5.Entity.Usuario;
import com.example.lab5.repository.UsuarioRepository;
import com.example.lab5.repository.MensajeRepository;
import com.example.lab5.repository.RankingRepository;

import.com.example.lab5.repository.MensajeRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    public String inicio(Model model) {
        model.addAttribute("usuarios", usuarioRepository.findAllOrderByNombre());
        return "index";
    }

    @GetMapping("/registro")
    public String mostrarRegistro(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "registro";
    }

    @PostMapping("/registro")
    public String registrarUsuario(@Valid @ModelAttribute("usuario") Usuario nuevoUsuario, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {

            return "registro";
        }

        try {
            usuarioRepository.save(nuevoUsuario);
            redirectAttributes.addFlashAttribute("msg", "Usuario registrado exitosamente");
            return "redirect:/";
        } catch (Exception e) {
            bindingResult.rejectValue("correo", "error.usuario", "El correo ya está registrado");
            return "registro";
        }
    }


    @PostMapping("/enviar")
    public String enviarMensaje(@Valid @ModelAttribute("mensaje") Mensaje mensaje, BindingResult bindingResult, @RequestParam("remitenteId") int idRemitente, @RequestParam("destinatarioId") int idDestinatario, @RequestParam("tipoRegalo") String tipoRegalo, @RequestParam(value = "colorCarrito", required = false) String colorCarrito, RedirectAttributes redirectAttributes, Model model) {

        if (mensaje.getContenido() != null &&
                (mensaje.getContenido().toLowerCase().contains("odio") ||
                        mensaje.getContenido().toLowerCase().contains("feo"))) {
            bindingResult.rejectValue("contenido", "error.mensaje", "El mensaje no puede contener palabras de mala educación");
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("usuarios", usuarioRepository.findAll());
            return "enviar-mensaje";
        }



        return "redirect:/";
    }



    @GetMapping("/mensajes-recibidos")
    public String mostrarMensajesRecibidos(@RequestParam int usuarioId, Model model) {

        Optional<Usuario> usuarioSeleccionado = usuarioRepository.findById(usuarioId);

     

        return "mensajes-recibidos";
    }
}

