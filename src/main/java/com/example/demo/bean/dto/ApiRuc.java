package com.example.demo.bean.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApiRuc implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotBlank(message = "El RUC es obligatorio")
    @Pattern(regexp = "^(10|15|16|17|20)\\d{9}$",
             message = "El RUC debe tener 11 dígitos y empezar con 10, 15, 16, 17 o 20")
    private String ruc;

    @NotBlank(message = "La razón social es obligatoria")
    @Size(min = 2, max = 200, message = "La razón social debe tener entre 2 y 200 caracteres")
    private String razonSocial;

    @Size(max = 250, message = "La dirección no puede superar los 250 caracteres")
    private String direccion;

    @Size(max = 50, message = "El estado no puede superar los 50 caracteres")
    private String estado;

    @Size(max = 50, message = "La condición no puede superar los 50 caracteres")
    private String condicion;

    @Size(max = 100, message = "El distrito no puede superar los 100 caracteres")
    private String distrito;

    @Size(max = 100, message = "La provincia no puede superar los 100 caracteres")
    private String provincia;

    @Size(max = 100, message = "El departamento no puede superar los 100 caracteres")
    private String departamento;
}