package br.com.fiap.petfiap.builder;

import java.time.LocalDateTime;

import br.com.fiap.petfiap.factory.AtendimentoFactory;
import br.com.fiap.petfiap.model.Atendimento;

// Padrao Builder (Aula 14): monta um atendimento complexo passo a passo,
// sem construtor gigante no controller.
public class AtendimentoBuilder {

    private String tipo;
    private String petNome;
    private String petPorte;
    private String tutorNome;
    private LocalDateTime dataHora;

    public AtendimentoBuilder comTipo(String tipo) {
        this.tipo = tipo;
        return this;
    }

    public AtendimentoBuilder comPet(String petNome, String petPorte) {
        this.petNome = petNome;
        this.petPorte = petPorte;
        return this;
    }

    public AtendimentoBuilder comTutor(String tutorNome) {
        this.tutorNome = tutorNome;
        return this;
    }

    public AtendimentoBuilder comDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
        return this;
    }

    /* 
    Correção: aplicação do conceito Fail Fast, validação e do padrão Builder para garantir que o objeto nasça válido e não
    permita a passagem de dados inválidos.
    */
    public Atendimento construir(int protocolo) {
    if (this.petNome == null || this.petNome.isBlank()) {
        throw new IllegalArgumentException("Nome do pet e obrigatorio");
    }
    if (this.petPorte == null || this.petPorte.isBlank()) {
        throw new IllegalArgumentException("Porte do pet e obrigatorio");
    }
    return AtendimentoFactory.criar(protocolo, tipo, petNome, petPorte, tutorNome, dataHora);
}
}
