package com.example.demo.bean.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.example.demo.bean.dto.ClienteDto;
import com.example.demo.bean.entity.Cliente;

@Mapper(componentModel = "spring")
public interface ClienteMapper {

    ClienteDto toDto(Cliente cliente);

    @Mapping(target = "idCliente", ignore = true)
    Cliente toEntity(ClienteDto dto);

    List<ClienteDto> toDtoList(List<Cliente> clientes);
}