package ufrn.br.projeto_livros2u.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import ufrn.br.projeto_livros2u.domain.Usuario;
import ufrn.br.projeto_livros2u.repository.UsuarioRepository;


@Controller
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/cadusuario")
    public String formCadastro(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "cadusuario";
    }

    @PostMapping("/cadusuario")
    public String salvarUsuario(@ModelAttribute Usuario usuario) {
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        usuarioRepository.save(usuario);
        return "redirect:/login";
    }
}
