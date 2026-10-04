package com.example.demo.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.bean.dto.ClienteDto;
import com.example.demo.useCase.ClienteUseCase;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.groups.Default;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/clientes")
@RequiredArgsConstructor
@Validated
public class ClienteController {

    private final ClienteUseCase clienteUseCase;

    // Feign -> DNI. @Valid (grupo Default): valida correo y telefono
    @PostMapping("/registrar-dni/{dni}")
    public ResponseEntity<ClienteDto> registrarDni(
            @PathVariable @Pattern(regexp = "^\\d{8}$", message = "El DNI debe tener 8 dígitos") String dni,
            @RequestBody @Valid ClienteDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(clienteUseCase.registrarDesdeDni(dni, dto));
    }

    // Feign -> RUC. @Valid (grupo Default): valida correo y telefono
    @PostMapping("/registrar-ruc/{ruc}")
    public ResponseEntity<ClienteDto> registrarRuc(
            @PathVariable @Pattern(regexp = "^\\d{11}$", message = "El RUC debe tener 11 dígitos") String ruc,
            @RequestBody @Valid ClienteDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(clienteUseCase.registrarDesdeRuc(ruc, dto));
    }

    // Manual: valida todo (Default + Manual)
    @PostMapping("/registrar-manual")
    public ResponseEntity<ClienteDto> registrarManual(
            @RequestBody @Validated({ Default.class, ClienteDto.Manual.class }) ClienteDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(clienteUseCase.registrarManual(dto));
    }

    @GetMapping
    public ResponseEntity<List<ClienteDto>> listarTodos() {
        return ResponseEntity.ok(clienteUseCase.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClienteDto> buscarPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(clienteUseCase.buscarPorId(id));
    }
}