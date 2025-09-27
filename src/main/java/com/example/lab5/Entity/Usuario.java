package com.example.lab5.Entity;

import com.fasterxml.jackson.annotation.JsonTypeId;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.*;


@Entity
@Getter
@Setter
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(nullable = false, length =  50)

    @NotBlank(message = "El nombre es obligatorio")

    @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+$", message = "El nombre no puede contener números ni caracteres especiales")


    private String nombre;

    @Column(nullable = false, length = 50)

    @NotBlank(message = "El apellido es obligatorio")


    private String apellido;

    @Column(nullable = false, unique = true, length = 100)

    @NotBlank(message = "El correo es obligatorio")

    //va a permitir limitar el formato del correo
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "Formato de correo inválido")

    private String correo;

    @Column(nullable = false)

    @Min(value = 18, message = "La edad mínima es 18 años")

    private int edad;

    @Column(length = 255)
    @Size(min = 10, message = "La descripción debe tener al menos 10 caracteres")

    private String descripcion;

    @Column(length = 255)
    //va a permitir limitar el formato de la contraseña
    @Pattern(regexp = "^(?=.*\\d).{6,}$", message = "La contraseña debe tener al menos 6 caracteres y 1 número")

    private String contrasena;

}
