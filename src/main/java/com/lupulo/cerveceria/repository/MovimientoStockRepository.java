package com.lupulo.cerveceria.repository;

import com.lupulo.cerveceria.model.MovimientoStock;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovimientoStockRepository extends JpaRepository<MovimientoStock, Long> {
}