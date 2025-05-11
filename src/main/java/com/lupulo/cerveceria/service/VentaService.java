package com.lupulo.cerveceria.service;

import com.lupulo.cerveceria.dto.VentaRequest;
import com.lupulo.cerveceria.model.Cerveza;
import com.lupulo.cerveceria.model.Venta;
import com.lupulo.cerveceria.repository.CervezaRepository;
import com.lupulo.cerveceria.repository.VentaRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class VentaService {

  private final VentaRepository ventaRepository;
  private final CervezaRepository cervezaRepository;

  public VentaService(VentaRepository ventaRepository, CervezaRepository cervezaRepository) {
    this.ventaRepository = ventaRepository;
    this.cervezaRepository = cervezaRepository;
  }

  public void registrarVenta(VentaRequest request) {
    Cerveza cerveza = cervezaRepository.findById(request.getCervezaId())
        .orElseThrow(() -> new RuntimeException("Cerveza no encontrada"));

    if (cerveza.getStock() < request.getCantidad()) {
      throw new RuntimeException("Stock insuficiente para realizar la venta");
    }

    cerveza.setStock(cerveza.getStock() - request.getCantidad());
    cervezaRepository.save(cerveza);

    Venta venta = Venta.builder()
        .cerveza(cerveza)
        .cantidad(request.getCantidad())
        .fecha(LocalDateTime.now())
        .build();

    ventaRepository.save(venta);
  }
}