package ufrn.br.projeto_livros2u.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ufrn.br.projeto_livros2u.domain.Livro;
import ufrn.br.projeto_livros2u.repository.LivroRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class LivroService {

    @Autowired
    private LivroRepository livroRepository;

    private final String[] imagens = {
            "/static/livro1.jpg.png",
            "/static/livro2.jpg.png",
            "/static/livro3.jpg.png"
    };

    public List<Livro> listarNaoDeletados() {
        return livroRepository.findByIsDeletedNull();
    }

    public List<Livro> listarTodos() {
        return livroRepository.findAll();
    }

    public Optional<Livro> buscarPorId(Long id) {
        return livroRepository.findById(id);
    }

    public Livro salvar(Livro livro) {
        // Se o livro for novo ou não tiver imagem, define uma aleatória
        if (livro.getImageUrl() == null || livro.getImageUrl().isBlank()) {
            livro.setImageUrl(imagens[new Random().nextInt(imagens.length)]);
        }

        return livroRepository.save(livro);
    }

    public void deletarLogicamente(Long id) {
        Optional<Livro> opt = livroRepository.findById(id);
        if (opt.isPresent()) {
            Livro livro = opt.get();
            livro.setIsDeleted(LocalDateTime.now());
            livroRepository.save(livro);
        }
    }

    public void restaurar(Long id) {
        Optional<Livro> opt = livroRepository.findById(id);
        if (opt.isPresent()) {
            Livro livro = opt.get();
            livro.setIsDeleted(null);
            livroRepository.save(livro);
        }
    }
}
