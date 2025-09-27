package com.example.lab5.repository;
import com.example.lab5.Entity.Mensaje;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

public interface MensajeRepository extends JpaRepository<Mensaje, Integer> {
    @Query("SELECT m FROM Mensaje m WHERE m.destinatario.id = :usuarioId ORDER BY m.fechaEnvio DESC")
    List<Mensaje> obtenerMensajesPorDestinatario(@Param("usuarioId") int usuarioId);

    @Query("SELECT COUNT(m) FROM Mensaje m WHERE m.destinatario.id = :usuarioId")
    Long contarMensajesPorDestinatario(@Param("usuarioId") int usuarioId);
}
