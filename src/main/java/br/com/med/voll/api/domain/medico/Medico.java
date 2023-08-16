package br.com.med.voll.api.domain.medico;

import br.com.med.voll.api.domain.Endereco;
import jakarta.persistence.*;
import lombok.*;

@Table(name = "medicos")
@Entity(name = "Medico")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Medico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;
    private String nome;
    private String email;
    private String telefone;
    private String crm;
    @Enumerated(EnumType.STRING)
    private Especialidade especialidade;
    @Embedded
    private Endereco endereco;
    private Boolean ativo;

    public Medico(DadosCadastroMedico dto) {
        this.ativo = true;
        this.nome = dto.nome();
        this.email = dto.email();
        this.telefone = dto.telefone();
        this.crm = dto.crm();
        this.especialidade = dto.especialidade();
        this.endereco = new Endereco(dto.endereco());
    }

    public void updateInfoMedico(DadosAtualizacaoMedico dados) {
        if(dados.nome() != null) {
            this.nome = dados.nome();
        }
        if(dados.email() != null) {
            this.email = dados.email();
        }
        if(dados.telefone() != null) {
            this.telefone = dados.telefone();
        }
        if(dados.crm() != null) {
            this.crm = dados.crm();
        }
        if (dados.endereco() != null) {
            this.endereco.updateInfoEndereco(dados.endereco());
        }
    }

    public void delete() {
        this.ativo = false;
    }
}
