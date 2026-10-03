package com.example.demo.useCase;

import java.util.List;

import com.example.demo.bean.dto.ClienteDto;

public interface ClienteUseCase {
	 ClienteDto registrarDesdeRuc(String ruc, ClienteDto dto);
	    ClienteDto registrarDesdeDni(String dni, ClienteDto dto);
	    ClienteDto registrarManual(ClienteDto dto);
	    ClienteDto buscarPorId(Integer id);
	    List<ClienteDto> listarTodos();
}
