package org.example.itauteste.Service;

import org.example.itauteste.Model.Estatistica;
import org.example.itauteste.Model.Transacao;
import org.springframework.stereotype.Service;
import java.util.DoubleSummaryStatistics;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
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

    public Estatistica getTransacoes() {
        Estatistica estatistica = new Estatistica();
        DoubleSummaryStatistics ds = new DoubleSummaryStatistics();
        Duration duration;
        List<Transacao> t = new ArrayList<>();
        for(Transacao transacao : transacoes) {
            duration = Duration.between(transacao.getDataHora(), OffsetDateTime.now());
            if(duration.getSeconds() <= 60){
                t.add(transacao);
                ds.accept(transacao.getValor());
            }
        }
        if(t.isEmpty()) {
            estatistica.setMin(0.0);
            estatistica.setMax(0.0);
        }
        else{
            estatistica.setMin(ds.getMin());
            estatistica.setMax(ds.getMax());
        }
        estatistica.setCount(t.size());
        estatistica.setSum(ds.getSum());
        estatistica.setAvg(ds.getAverage());
        return estatistica;
    }
}
