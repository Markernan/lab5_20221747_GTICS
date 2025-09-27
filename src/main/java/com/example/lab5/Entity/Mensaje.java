package com.example.lab5.Entity;
import lombok.Getter;
import lombok.Setter;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "mensajes")

@Getter
@Setter
public class Mensaje {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private int id;

    @ManyToOne

    @JoinColumn(name = "remitente_id", nullable = false)

    private Usuario remitente;

    @ManyToOne
    @JoinColumn(name = "destinatario_id", nullable = false)

    private Usuario destinatario;

    @Column(name = "regalo_tipo", nullable = false)

    @Enumerated(EnumType.STRING)

    private TipoRegalo regaloTipo;

    @Column(name = "regalo_color", length = 30)

    private String regaloColor;

    @Column(nullable = false, length = 255)

    @Size(min = 20, message = "El mensaje debe tener al menos 20 caracteres")
    private String contenido;


    @Column(name = "fecha_envio")

    private LocalDateTime fechaEnvio;

    public enum TipoRegalo {

        Flor, Carrito

    }

    @PrePersist
    public void prePersist() {
        this.fechaEnvio = LocalDateTime.now();
    }
}