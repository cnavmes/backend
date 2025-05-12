package com.lupulo.cerveceria.service;

import com.lupulo.cerveceria.dto.ReponerStockRequest;
import com.lupulo.cerveceria.model.Cerveza;
import com.lupulo.cerveceria.model.MovimientoStock;
import com.lupulo.cerveceria.repository.CervezaRepository;
import com.lupulo.cerveceria.repository.MovimientoStockRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class MovimientoStockService {

  private final CervezaRepository cervezaRepository;
  private final MovimientoStockRepository movimientoRepository;

  public MovimientoStockService(CervezaRepository cervezaRepository, MovimientoStockRepository movimientoRepository) {
    this.cervezaRepository = cervezaRepository;
    this.movimientoRepository = movimientoRepository;
  }

  public void reponerStock(Long cervezaId, ReponerStockRequest request) {
    Cerveza cerveza = cervezaRepository.findById(cervezaId)
        .orElseThrow(() -> new RuntimeException("Cerveza no encontrada"));

    cerveza.setStock(cerveza.getStock() + request.getCantidad());
    cervezaRepository.save(cerveza);

    String email = SecurityContextHolder.getContext().getAuthentication().getName();

    MovimientoStock movimiento = MovimientoStock.builder()
        .cerveza(cerveza)
        .cantidad(request.getCantidad())
        .tipo("REPOSICION")
        .fecha(LocalDateTime.now())
        .usuarioEmail(email)
        .build();

    movimientoRepository.save(movimiento);
  }
}