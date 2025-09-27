package com.example.lab5.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class UsuarioRankingDto {
    private int id;
    private String nombre;
    private String apellido;
    private String correo;
    private String descripcion;
    private long totalRegalos;
}
