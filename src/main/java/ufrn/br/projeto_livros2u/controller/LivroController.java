package ufrn.br.projeto_livros2u.controller;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ufrn.br.projeto_livros2u.domain.Livro;
import ufrn.br.projeto_livros2u.repository.LivroRepository;

import ufrn.br.projeto_livros2u.service.LivroService;

import java.util.*;

@Controller
public class LivroController {

    @Autowired
    private LivroRepository livroRepository;

    @Autowired
    private LivroService livroService;

    @GetMapping("/index")
    public String index(Model model, HttpSession session) {
        List<Livro> livros = livroService.listarNaoDeletados();
        model.addAttribute("livros", livros);

        Map<Long, Integer> carrinho = (Map<Long, Integer>) session.getAttribute("carrinho");
        if (carrinho == null) {
            carrinho = new HashMap<>();
        }

        int qtdCarrinho = carrinho.values().stream().mapToInt(Integer::intValue).sum();

        Map<Long, Integer> quantidades = new HashMap<>();
        carrinho.forEach(quantidades::put);

        model.addAttribute("quantidades", quantidades);

        model.addAttribute("carrinho", carrinho);
        model.addAttribute("qtdCarrinho", qtdCarrinho);

        return "index";
    }

    @GetMapping("/adicionarCarrinho")
    public String adicionarCarrinho(@RequestParam Long id, HttpSession session) {
        livroRepository.findById(id).orElseThrow();

        Map<Long, Integer> carrinho = (Map<Long, Integer>) session.getAttribute("carrinho");
        if (carrinho == null) {
            carrinho = new HashMap<>();
        }

        carrinho.put(id, carrinho.getOrDefault(id, 0) + 1);
        session.setAttribute("carrinho", carrinho);

        return "redirect:/index";
    }


    @GetMapping("/verCarrinho")
    public String verCarrinho(HttpSession session, Model model, RedirectAttributes ra) {
        Map<Long, Integer> carrinho = (Map<Long, Integer>) session.getAttribute("carrinho");

        if (carrinho == null || carrinho.isEmpty()) {
            ra.addFlashAttribute("mensagem", "Carrinho vazio!");
            return "redirect:/index";
        }

        List<Livro> livrosCarrinho = livroRepository.findAllById(carrinho.keySet());

        Map<Long, Integer> carrinhoQuantidades = new HashMap<>();
        for (Livro livro : livrosCarrinho) {
            carrinhoQuantidades.put(livro.getId(), carrinho.getOrDefault(livro.getId(), 0));
        }

        // Adicionar corretamente ao modelo
        model.addAttribute("livrosCarrinho", livrosCarrinho);
        model.addAttribute("carrinhoQuantidades", carrinhoQuantidades);
        model.addAttribute("carrinho", carrinho);

        return "verCarrinho";
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
        return "editar";
    }

    @PostMapping("/salvar")
    public String salvar(@Valid @ModelAttribute("livro") Livro livro, BindingResult result, RedirectAttributes ra) {
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

    @PostMapping("/atualizarQuantidade")
    public String atualizarQuantidade(@RequestParam Long id,
                                      @RequestParam int quantidade,
                                      HttpSession session,
                                      RedirectAttributes ra) {

        Optional<Livro> optLivro = livroService.buscarPorId(id);
        if (optLivro.isEmpty()) {
            ra.addFlashAttribute("mensagem", "Livro não encontrado.");
            return "redirect:/verCarrinho";
        }

        Livro livro = optLivro.get();

        if (livro.getEstoque() < quantidade) {
            ra.addFlashAttribute("mensagem", "Estoque insuficiente para " + livro.getTitulo());
            return "redirect:/verCarrinho";
        }

        // Atualiza quantidades
        Map<Long, Integer> carrinhoQuantidades = (Map<Long, Integer>) session.getAttribute("carrinhoQuantidades");
        if (carrinhoQuantidades == null) {
            carrinhoQuantidades = new HashMap<>();
        }
        carrinhoQuantidades.put(id, quantidade);
        session.setAttribute("carrinhoQuantidades", carrinhoQuantidades);

        ra.addFlashAttribute("mensagem", "Quantidade atualizada com sucesso.");
        return "redirect:/verCarrinho";
    }


}

