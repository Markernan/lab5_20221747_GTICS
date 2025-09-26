package com.example.lab5.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/usuario")
public class UsuarioController {
    final UsuarioRepository usuarioRepository;

    public UsuarioController(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @GetMapping(value = {"", "/", "list"})
    public String listarEmpleados(Model model) {
        model.addAttribute("listaEmpleados", usuarioRepository.findAll());
        model.addAttribute("listaEmpleadosPorRegion", usuarioRepository.obtenerEmpleadosPorRegion());
        model.addAttribute("listaEmpleadosPorPais", usuarioRepository.obtenerEmpleadosPorPais());
        return "employee/list";
    }

}

