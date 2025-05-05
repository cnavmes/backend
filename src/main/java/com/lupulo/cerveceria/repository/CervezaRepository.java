package com.lupulo.cerveceria.repository;

import com.lupulo.cerveceria.model.Cerveza;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CervezaRepository extends JpaRepository<Cerveza, Long> {
  List<Cerveza> findByEstiloIgnoreCase(String estilo);

  List<Cerveza> findByNombreContainingIgnoreCase(String nombre);
}