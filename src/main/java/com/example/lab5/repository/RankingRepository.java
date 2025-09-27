package com.example.lab5.repository;

import com.example.lab5.Entity.Ranking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

public interface RankingRepository extends JpaRepository<Ranking, Integer> {

    @Query("SELECT r FROM Ranking r JOIN r.usuario u ORDER BY r.totalRegalos DESC")
    List<Ranking> obtenerRankingOrdenadoPorRegalos();

    @Query("SELECT u, COALESCE(COUNT(m), 0) as totalRegalos " +
            "FROM Usuario u LEFT JOIN Mensaje m ON u.id = m.destinatario.id " +
            "GROUP BY u.id " +
            "ORDER BY totalRegalos DESC")
    List<Object[]> obtenerUsuariosConConteoRegalos();

}
