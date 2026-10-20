package com.example.demo.model;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.bean.dto.ApiDni;
import com.example.demo.bean.dto.ApiRuc;
import com.example.demo.bean.dto.ClienteDto;
import com.example.demo.bean.entity.Cliente;
import com.example.demo.bean.mapper.ClienteMapper;
import com.example.demo.client.ApiPeruClient;
import com.example.demo.repository.ClienteRepository;
import com.example.demo.useCase.ClienteUseCase;

import feign.FeignException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ClienteModel implements ClienteUseCase {

    private final ClienteRepository clienteRepository;
    private final ClienteMapper clienteMapper;
    private final ApiPeruClient apiPeruClient;
    private final Validator validator;

    @Value("${apiperu.token}")
    private String token;

    @Override
    @Transactional
    public ClienteDto registrarDesdeRuc(String ruc, ClienteDto dto) {
        validarContacto(dto);

        ApiRuc response;
        try {
            response = apiPeruClient.consultarRuc(ruc, token);
        } catch (FeignException e) {
            throw new RuntimeException("Error al consultar RUC " + ruc + " (HTTP " + e.status() + ")");
        }
        validar(response);

        Cliente cliente = clienteMapper.toEntity(dto);
        cliente.setTipoDocumento("RUC");
        cliente.setNumeroDocumento(response.getRuc());
        cliente.setNombre(response.getRazonSocial());
        cliente.setDireccion(response.getDireccion());

        return clienteMapper.toDto(clienteRepository.save(cliente));
    }

    @Override
    @Transactional
    public ClienteDto registrarDesdeDni(String dni, ClienteDto dto) {
        validarContacto(dto);

        ApiDni response;
        try {
            response = apiPeruClient.consultarDni(dni, token);
        } catch (FeignException e) {
            throw new RuntimeException("Error al consultar DNI " + dni + " (HTTP " + e.status() + ")");
        }
        validar(response);

        String nombreCompleto = (response.getNombres() + " "
                + response.getApellidoPaterno() + " "
                + (response.getApellidoMaterno() == null ? "" : response.getApellidoMaterno())).trim();

        Cliente cliente = clienteMapper.toEntity(dto);
        cliente.setTipoDocumento("DNI");
        cliente.setNumeroDocumento(response.getDni());
        cliente.setNombre(nombreCompleto);

        return clienteMapper.toDto(clienteRepository.save(cliente));
    }

    @Override
    @Transactional
    public ClienteDto registrarManual(ClienteDto dto) {
        Cliente cliente = clienteMapper.toEntity(dto);
        return clienteMapper.toDto(clienteRepository.save(cliente));
    }

    @Override
    @Transactional(readOnly = true)
    public ClienteDto buscarPorId(Integer id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado: " + id));
        return clienteMapper.toDto(cliente);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ClienteDto> listarTodos() {
        return clienteMapper.toDtoList(clienteRepository.findAll());
    }

    /**
     * Actualiza solo los datos editables (nombre, dirección, correo y teléfono).
     * El tipo y número de documento no se modifican porque las facturas ya emitidas dependen de ellos.
     */
    @Override
    @Transactional
    public ClienteDto actualizar(Integer id, ClienteDto dto) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado: " + id));

        validarContacto(dto);

        cliente.setNombre(dto.getNombre());
        cliente.setDireccion(dto.getDireccion());
        cliente.setCorreo(dto.getCorreo());
        cliente.setTelefono(dto.getTelefono());

        return clienteMapper.toDto(clienteRepository.save(cliente));
    }

    @Override
    @Transactional
    public void eliminar(Integer id) {
        if (!clienteRepository.existsById(id)) {
            throw new RuntimeException("Cliente no encontrado: " + id);
        }
        clienteRepository.deleteById(id);
    }


    private <T> void validar(T respuesta) {
        if (respuesta == null) {
            throw new RuntimeException("La API no devolvió datos");
        }
        Set<ConstraintViolation<T>> errores = validator.validate(respuesta);
        if (!errores.isEmpty()) {
            String detalle = errores.stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.joining("; "));
            throw new RuntimeException("Respuesta inválida de la API: " + detalle);
        }
    }

    private void validarContacto(ClienteDto dto) {
        Set<ConstraintViolation<ClienteDto>> errores = new HashSet<>();
        errores.addAll(validator.validateProperty(dto, "correo"));
        errores.addAll(validator.validateProperty(dto, "telefono"));
        if (!errores.isEmpty()) {
            String detalle = errores.stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.joining("; "));
            throw new RuntimeException(detalle);
        }
    }
}