package com.example.lab5.Entity;
import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.*;
@Entity
@Table(name = "ranking")

@Getter
@Setter
public class Ranking {
    @Id

    @Column(name = "usuario_id")

    private int usuarioId;


    @OneToOne

    @MapsId

    @JoinColumn(name = "usuario_id")

    private Usuario usuario;


    @Column(name = "total_regalos")

    private int totalRegalos = 0;

}
