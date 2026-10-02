package br.com.fiap.petfiap.model;

// Padrao Singleton (Aula 14): uma unica instancia em toda a aplicacao,
// responsavel por gerar os protocolos sequenciais dos atendimentos.
// Thread-safe para o uso concorrente do pet shop.
//Correção: aplica o padrao Singleton para garantir que o objeto criado fosse guardado no atributo estático
public class GeradorProtocolo {

    private static GeradorProtocolo instancia;
    private int contador;

    private GeradorProtocolo() {
        this.contador = 0;
    }

    // Correção: permitir chamadas simultâneas
    public static synchronized GeradorProtocolo getInstancia() {
        if (instancia == null) {
            instancia = new GeradorProtocolo();
        }
        return instancia;
    }

    public synchronized int proximo() {
        contador++;
        return contador;
    }
}