package ufrn.br.projeto_livros2u.repository;


import ufrn.br.projeto_livros2u.domain.Livro;
import ufrn.br.projeto_livros2u.domain.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findUsuarioByUsername(String username);


}