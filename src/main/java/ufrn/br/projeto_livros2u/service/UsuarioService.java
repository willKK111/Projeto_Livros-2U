package ufrn.br.projeto_livros2u.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ufrn.br.projeto_livros2u.domain.Usuario;
import ufrn.br.projeto_livros2u.repository.UsuarioRepository;

import java.util.Optional;

@Service
public class UsuarioService implements UserDetailsService {
    private final UsuarioRepository repository;

    public UsuarioService(UsuarioRepository repository){
        this.repository = repository;
    }
    public Optional<Usuario> findById(Long id){
        return  repository.findById(id);
    }
    public Usuario update(Usuario u){
        return repository.saveAndFlush(u);
    }
    public Usuario create(Usuario u){
        return repository.save(u);

    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Usuario> optional = repository.findUsuarioByUsername(username);
        if(optional.isPresent()) {
            return optional.get();
        }
        throw new UsernameNotFoundException("User not found");
    }

}