package com.example.demo.bean.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApiDni implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotBlank(message = "El DNI es obligatorio")
    @Pattern(regexp = "^\\d{8}$", message = "El DNI debe tener 8 dígitos")
    private String dni;

    @NotBlank(message = "Los nombres son obligatorios")
    @Size(max = 100, message = "Los nombres no pueden superar los 100 caracteres")
    @Pattern(regexp = "^[\\p{L} .'-]+$",
             message = "Los nombres solo pueden contener letras, espacios, puntos, guiones o apóstrofes")
    private String nombres;

    @JsonProperty("apellidoPaterno")
    @NotBlank(message = "El apellido paterno es obligatorio")
    @Size(max = 100, message = "El apellido paterno no puede superar los 100 caracteres")
    @Pattern(regexp = "^[\\p{L} .'-]+$",
             message = "El apellido paterno solo puede contener letras, espacios, puntos, guiones o apóstrofes")
    private String apellidoPaterno;


    @JsonProperty("apellidoMaterno")
    @Size(max = 100, message = "El apellido materno no puede superar los 100 caracteres")
    @Pattern(regexp = "^[\\p{L} .'-]*$",
             message = "El apellido materno solo puede contener letras, espacios, puntos, guiones o apóstrofes")
    private String apellidoMaterno;

    @Pattern(regexp = "^[0-9A-Za-z]?$",
             message = "El código de verificación debe ser un solo carácter alfanumérico")
    private String codVerifica;
}