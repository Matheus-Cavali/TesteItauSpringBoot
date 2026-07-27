package org.example.itauteste.Service;

import org.example.itauteste.Model.Transacao;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class TransacaoService {
    CopyOnWriteArrayList<Transacao> transacoes = new CopyOnWriteArrayList<>();
    public boolean registrar(Transacao transacao) {
        if(transacao.getDataHora().isAfter(OffsetDateTime.now()) || transacao.getValor() < 0) {
            return false;
        }
        transacoes.add(transacao);
        return true;
    }

    public void deletar() {
        transacoes.clear();
    }
}
