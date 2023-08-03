package br.com.med.voll.api.domain;

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

    public Endereco(DadosEndereco dto) {
        this.logradouro = dto.logradouro();
        this.bairro = dto.bairro();
        this.cep = dto.cep();
        this.uf = dto.uf();
        this.cidade = dto.cidade();
        this.numero = dto.numero();
        this.complemento = dto.complemento();
    }

    public void updateInfoEndereco(DadosEndereco endereco) {
        if(endereco.logradouro() != null) {
            this.logradouro = endereco.logradouro();
        }
        if(endereco.bairro() != null) {
            this.bairro = endereco.bairro();
        }
        if(endereco.cep() != null) {
            this.cep = endereco.cep();
        }
        if(endereco.uf() != null) {
            this.uf = endereco.uf();
        }
        if(endereco.cidade() != null) {
            this.cidade = endereco.cidade();
        }
        if(endereco.numero() != null) {
            this.numero = endereco.numero();
        }
        if(endereco.complemento() != null) {
            this.complemento = endereco.complemento();
        }
    }
}
