package com.lupulo.cerveceria.service;

import com.lupulo.cerveceria.dto.*;
import com.lupulo.cerveceria.model.MovimientoStock;
import com.lupulo.cerveceria.repository.MovimientoStockRepository;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MovimientoStockService {

  private final MovimientoStockRepository movimientoRepository;

  public MovimientoStockService(MovimientoStockRepository movimientoRepository) {
    this.movimientoRepository = movimientoRepository;
  }

  public MovimientoStockRespuestaDTO filtrarMovimientos(String tipo, LocalDateTime desde, LocalDateTime hasta,
      String email, String cervezaNombre, int page, int size) {
    Pageable pageable = PageRequest.of(page, size, Sort.by("fecha").descending());
    Page<MovimientoStock> pagina;

    // NUEVO: filtro por nombre de cerveza y tipo (si ambos están presentes)
    if (cervezaNombre != null) {
      pagina = movimientoRepository.buscarPorNombreYTipo(cervezaNombre, tipo, pageable);
    }
    // Otros filtros existentes
    else if (tipo != null && desde != null && hasta != null && email != null) {
      pagina = movimientoRepository.findByTipoIgnoreCaseAndFechaBetweenAndUsuarioEmailIgnoreCase(tipo, desde, hasta,
          email, pageable);
    } else if (tipo != null && email != null) {
      pagina = movimientoRepository.findByTipoIgnoreCaseAndUsuarioEmailIgnoreCase(tipo, email, pageable);
    } else if (tipo != null && desde != null && hasta != null) {
      pagina = movimientoRepository.findByTipoIgnoreCaseAndFechaBetween(tipo, desde, hasta, pageable);
    } else if (desde != null && hasta != null && email != null) {
      pagina = movimientoRepository.findByFechaBetweenAndUsuarioEmailIgnoreCase(desde, hasta, email, pageable);
    } else if (tipo != null) {
      pagina = movimientoRepository.findByTipoIgnoreCase(tipo, pageable);
    } else if (desde != null && hasta != null) {
      pagina = movimientoRepository.findByFechaBetween(desde, hasta, pageable);
    } else if (email != null) {
      pagina = movimientoRepository.findByUsuarioEmailIgnoreCase(email, pageable);
    } else {
      pagina = movimientoRepository.findAll(pageable);
    }

    List<MovimientoStockDTO> movimientos = pagina.getContent().stream().map(m -> MovimientoStockDTO.builder()
        .nombreCerveza(m.getCerveza().getNombre())
        .cantidad(m.getCantidad())
        .tipo(m.getTipo())
        .fecha(m.getFecha())
        .usuarioEmail(m.getUsuarioEmail())
        .build()).toList();

    return new MovimientoStockRespuestaDTO(
        movimientos,
        pagina.getNumber(),
        pagina.getTotalPages(),
        pagina.getTotalElements());
  }

  public List<MovimientoStock> listarTodos() {
    return movimientoRepository.findAll();
  }

}
