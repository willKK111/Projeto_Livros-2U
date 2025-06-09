package ufrn.br.projeto_livros2u.controller;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ufrn.br.projeto_livros2u.domain.Livro;
import ufrn.br.projeto_livros2u.repository.LivroRepository;

import ufrn.br.projeto_livros2u.service.LivroService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
public class LivroController {

    @Autowired
    private LivroRepository livroRepository;

    @Autowired
    private LivroService livroService;

    @GetMapping("/index")
    public String index(Model model, HttpSession session) {
        List<Livro> livros = livroRepository.findByIsDeletedNull();
        model.addAttribute("livros", livros);

        List<Livro> carrinho = (List<Livro>) session.getAttribute("carrinho");
        model.addAttribute("qtdCarrinho", carrinho != null ? carrinho.size() : 0);

        return "index";
    }


    @GetMapping("/adicionarCarrinho")
    public String adicionarCarrinho(@RequestParam Long id, HttpSession session) {
        Livro livro = livroRepository.findById(id).orElseThrow();
        List<Livro> carrinho = (List<Livro>) session.getAttribute("carrinho");

        if (carrinho == null) {
            carrinho = new ArrayList<>();
            session.setAttribute("carrinho", carrinho);
        }

        carrinho.add(livro);
        return "redirect:/index";
    }


    @GetMapping("/verCarrinho")
    public String verCarrinho(HttpSession session, Model model, RedirectAttributes ra) {
        List<Livro> carrinho = (List<Livro>) session.getAttribute("carrinho");

        if (carrinho == null || carrinho.isEmpty()) {
            ra.addFlashAttribute("mensagem", "Carrinho vazio!");
            return "redirect:/index";
        }

        model.addAttribute("carrinho", carrinho);
        return "carrinho";
    }

    @GetMapping("/finalizarCompra")
    public String finalizar(HttpSession session) {
        session.invalidate();
        return "redirect:/index";
    }



    //PARTE DE ADMIN


    @GetMapping("/admin")
    public String admin(Model model) {
        model.addAttribute("livros", livroService.listarTodos());
        return "admin";
    }

    @GetMapping("/cadastro")
    public String formCadastro(Model model) {
        model.addAttribute("livro", new Livro());
        return "cadastro";
    }

    @GetMapping("/editar")
    public String editar(@RequestParam Long id, Model model) {
        Livro livro = livroRepository.findById(id).orElseThrow();
        model.addAttribute("livro", livro);
        return "form";
    }

    @PostMapping("/salvar")
    public String salvar(@Valid Livro livro, BindingResult result, RedirectAttributes ra) {
        if (result.hasErrors()) return "cadastro";

       livroService.salvar(livro);
        ra.addFlashAttribute("mensagem", "Livro salvo com sucesso!");
        return "redirect:/admin";
    }


    @GetMapping("/deletar")
    public String deletar(@RequestParam Long id) {
        livroService.deletarLogicamente(id);
        return "redirect:/index";
    }

    @GetMapping("/restaurar")
    public String restaurar(@RequestParam Long id) {
       livroService.restaurar(id);
        return "redirect:/index";
    }



}

