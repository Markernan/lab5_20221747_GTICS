package com.example.lab5.repository;

import com.example.lab5.Entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    @Query("SELECT u FROM Usuario u ORDER BY u.nombre")
    List<Usuario> buscarTodosOrdenadosPorNombre();

    @Query("SELECT u FROM Usuario u WHERE u.correo = ?1")
    Optional<Usuario> buscarPorCorreo(String correo);
}
