package br.com.fiap.petfiap.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;

// Tosa: preco por porte, 30 pontos, 60 minutos.
@Entity
public class Tosa extends Atendimento {

    public static final String TIPO = "TOSA";

    private static final double PRECO_PORTE_PEQUENO = 70.0;
    private static final double PRECO_PORTE_MEDIO = 90.0;
    private static final double PRECO_PORTE_GRANDE = 120.0;
    private static final int PONTOS_FIDELIDADE = 30;
    private static final int DURACAO_MINUTOS = 60;

    public Tosa() {
    }

    public Tosa(int protocolo, String petNome, String petPorte, String tutorNome, LocalDateTime dataHora) {
        super(protocolo, petNome, petPorte, tutorNome, dataHora);
    }

    @Override
    public String getTipo() {
        return TIPO;
    }

    @Override
    public double calcularPreco() {
        if ("PEQUENO".equals(getPetPorte())) {
            return PRECO_PORTE_PEQUENO;
        } else if ("MEDIO".equals(getPetPorte())) {
            return PRECO_PORTE_MEDIO;
        }
        return PRECO_PORTE_GRANDE;
    }

    @Override
    public int calcularPontosFidelidade() {
        return PONTOS_FIDELIDADE;
    }

    // Correção: remove String porte e adiciona @Override a fim de não violar o contrato estabelecido na classe mãe
    @Override
    public int getDuracaoMinutos() {
        return DURACAO_MINUTOS;
    }
}
