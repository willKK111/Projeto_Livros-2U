package ufrn.br.projeto_livros2u.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import ufrn.br.projeto_livros2u.domain.Livro;

public interface LivroRepository extends JpaRepository<Livro, Long> {
    List<Livro> findByIsDeletedNull(); // Busca apenas os livros não deletados
}
