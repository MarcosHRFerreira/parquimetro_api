package postech.fiap.com.br.parquimetro_api.domain.endereco;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Endereco {

    private String logradouro;
    private String bairro;
    private String cep;
    private String numero;
    private String complemento;
    private String cidade;
    private String uf;

    public Endereco(DadosEndereco dados) {

        if (dados.logradouro() != null) {
            this.logradouro = dados.logradouro().toUpperCase();
        }
        if (dados.bairro() != null) {
            this.bairro = dados.bairro().toUpperCase();
        }
        if (dados.cep() != null) {
            this.cep = dados.cep().toUpperCase();
        }
        if (dados.uf() != null) {
            this.uf = dados.uf().toUpperCase();
        }
        if (dados.cidade() != null) {
            this.cidade = dados.cidade().toUpperCase();
        }
        if (dados.numero() != null) {
            this.numero = dados.numero();
        }
        if (dados.complemento() != null) {
            this.complemento = dados.complemento().toUpperCase();;
        }
    }

    public void atualizarInformacoes(DadosEndereco dados) {
        if (dados.logradouro() != null) {
            this.logradouro = dados.logradouro().toUpperCase();
        }
        if (dados.bairro() != null) {
            this.bairro = dados.bairro().toUpperCase();
        }
        if (dados.cep() != null) {
            this.cep = dados.cep();
        }
        if (dados.uf() != null) {
            this.uf = dados.uf().toUpperCase();
        }
        if (dados.cidade() != null) {
            this.cidade = dados.cidade().toUpperCase();
        }
        if (dados.numero() != null) {
            this.numero = dados.numero();
        }
        if (dados.complemento() != null) {
            this.complemento = dados.complemento().toUpperCase();
        }
    }
}
