package com.lupulo.cerveceria.repository;

import com.lupulo.cerveceria.model.Venta;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VentaRepository extends JpaRepository<Venta, Long> {
}