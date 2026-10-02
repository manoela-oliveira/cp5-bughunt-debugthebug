package br.com.fiap.petfiap.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;

// Tosa: preco por porte, 30 pontos, 60 minutos.
@Entity
public class Tosa extends Atendimento {

    public static final String TIPO = "TOSA";

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
            return 70.0;
        } else if ("MEDIO".equals(getPetPorte())) {
            return 90.0;
        }
        return 120.0;
    }

    @Override
    public int calcularPontosFidelidade() {
        return 30;
    }

    // Correção: remove String porte e adiciona @Override a fim de não violar o contrato estabelecido na classe mãe
    @Override
    public int getDuracaoMinutos() {
        return 60;
    }
}
