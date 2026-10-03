package com.example.demo.bean.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ClienteDto implements Serializable {

    private static final long serialVersionUID = 1L;

    
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Integer idCliente;

    @NotBlank(message = "El tipo de documento es obligatorio")
    @Pattern(regexp = "^(DNI|RUC|CARNET_EXTRANJERIA)$",
             message = "El tipo de documento debe ser DNI, RUC o CARNET_EXTRANJERIA")
    private String tipoDocumento;

    @NotBlank(message = "El número de documento es obligatorio")
    @Pattern(regexp = "^[A-Za-z0-9]{8,12}$",
             message = "El número de documento debe tener entre 8 y 12 caracteres alfanuméricos")
    private String numeroDocumento;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 2, max = 200, message = "El nombre debe tener entre 2 y 200 caracteres")
    private String nombre;

    @Size(max = 250, message = "La dirección no puede superar los 250 caracteres")
    private String direccion;

    @Size(max = 100, message = "El correo no puede superar los 100 caracteres")
    @Pattern(regexp = "^$|^[\\w.+-]+@[\\w-]+(\\.[\\w-]+)+$",
             message = "El correo no tiene un formato válido")
    private String correo;

    @Pattern(regexp = "^$|^9\\d{8}$",
             message = "El teléfono debe tener 9 dígitos y empezar con 9")
    private String telefono;

    /**
     * DNI: 8 dígitos | RUC: 11 dígitos | CARNET_EXTRANJERIA: 9 a 12 caracteres alfanuméricos.
     */
    @JsonIgnore
    @AssertTrue(message = "El número de documento no coincide con el tipo "
            + "(DNI: 8 dígitos, RUC: 11 dígitos, carnet de extranjería: 9 a 12 caracteres)")
    public boolean isDocumentoConsistente() {
        if (tipoDocumento == null || numeroDocumento == null) {
            return true;
        }
        return switch (tipoDocumento) {
            case "DNI" -> numeroDocumento.matches("^\\d{8}$");
            case "RUC" -> numeroDocumento.matches("^(10|15|16|17|20)\\d{9}$");
            case "CARNET_EXTRANJERIA" -> numeroDocumento.matches("^[A-Za-z0-9]{9,12}$");
            default -> true;
        };
    }
}