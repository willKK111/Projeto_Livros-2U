package ufrn.br.projeto_livros2u.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity(name="livro")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Livro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O título não pode estar em branco.")
    private String titulo;

    @NotBlank(message = "O autor não pode estar em branco.")
    private String autor;

    @NotNull(message = "Ano de publicação é obrigatório.")
    @Min(value = 1000, message = "Ano de publicação inválido.")
    private Integer anoPublicacao;

    @NotBlank(message = "O gênero é obrigatório.")
    private String genero;

    private String imageUrl;

    private LocalDateTime isDeleted;

    @NotNull(message = "O estoque é obrigatório.")
    @Min(value = 0, message = "O estoque não pode ser negativo.")
    private Integer estoque;

    @NotNull(message = "O preço é obrigatório.")
    @DecimalMin(value = "0.0", inclusive = false, message = "O preço deve ser maior que zero.")
    private BigDecimal preco;
}
