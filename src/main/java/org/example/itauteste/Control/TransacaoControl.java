package org.example.itauteste.Control;

import jakarta.validation.Valid;
import org.example.itauteste.Model.Transacao;
import org.example.itauteste.Service.TransacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/transacao")
public class TransacaoControl {
    @Autowired
    TransacaoService transacaoService;

    @PostMapping
    public ResponseEntity<Void> transacao(@RequestBody @Valid Transacao transacao) {
        if(transacaoService.registrar(transacao))
            return ResponseEntity.status(HttpStatus.CREATED).build();
        else
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deletarTransacao(){
        transacaoService.deletar();
        return ResponseEntity.ok().build();
    }

}
