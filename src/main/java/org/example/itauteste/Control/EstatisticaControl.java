package org.example.itauteste.Control;

import org.example.itauteste.Model.Estatistica;
import org.example.itauteste.Service.TransacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/estatistica")
public class EstatisticaControl {
    @Autowired
    TransacaoService transacaoService;

    @GetMapping
    public ResponseEntity<Estatistica> listarTransacoes(){
        return ResponseEntity.ok(transacaoService.getTransacoes());
    }
}
