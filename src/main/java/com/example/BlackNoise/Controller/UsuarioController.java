package com.example.blacknoise.controller;

import com.example.blacknoise.model.Rol;
import com.example.blacknoise.model.Usuario;
import com.example.blacknoise.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/usuario")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/{id}")
    public String obtenerUsuarioPorId(@PathVariable String id, Model model) {
        Usuario usuario = usuarioService.obtenerUsuarioPorId(id);
        if (usuario != null) {
            model.addAttribute("usuario", usuario);
            
            switch(usuario.getRol()) {
                case ADMINISTRADOR:
                    List<Usuario> todosUsuarios = usuarioService.obtenerTodosLosUsuarios();
                    model.addAttribute("usuarios", todosUsuarios);
                    return "admin_dashboard";
                case EMPLEADO:
                    List<Usuario> usuariosEmpleado = usuarioService.obtenerTodosLosUsuarios();
                    model.addAttribute("usuarios", usuariosEmpleado);
                    return "empleado_dashboard";
                default:
                    return "bienvenida";
            }
        } else {
            return "error";
        }
    }

    @PostMapping("/editar")
    public String editarUsuario(@ModelAttribute Usuario usuarioEditado, Model model) {
        try {
            usuarioService.editarUsuario(usuarioEditado);
            return "redirect:/usuario/" + usuarioEditado.getId();
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "error";
        }
    }

    @PostMapping("/eliminar/{id}")
    public String eliminarUsuario(@PathVariable String id, Model model) {
        try {
            usuarioService.eliminarUsuario(id);
            return "redirect:/usuario/" + id;
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "error";
        }
    }
}