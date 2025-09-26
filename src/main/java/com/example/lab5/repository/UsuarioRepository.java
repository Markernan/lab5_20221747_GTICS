package com.example.lab5.repository;

import com.example.lab5.Entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
}
