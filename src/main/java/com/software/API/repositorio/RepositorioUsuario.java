package com.software.API.repositorio;


import com.software.API.modelo.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RepositorioUsuario extends JpaRepository<Usuario, Long>  {
    Optional<Usuario> findByCuil(Long cuil);
    Optional<Usuario> findByEmail(String email);
    Boolean existsByEmail(String email);
}
