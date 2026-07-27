package org.example.itauteste.Model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;

import java.time.OffsetDateTime;


public class Transacao {
    @NotNull
    private Double valor;

    @NotNull
    private OffsetDateTime dataHora;

    public Transacao(double valor, OffsetDateTime data) {
        this.valor = valor;
        this.dataHora = data;
    }

    public Transacao() {}

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public OffsetDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(OffsetDateTime dataHora) {
        this.dataHora = dataHora;
    }
}
