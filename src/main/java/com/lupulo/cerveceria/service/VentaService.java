package com.lupulo.cerveceria.service;

import com.lupulo.cerveceria.dto.VentaRequest;
import com.lupulo.cerveceria.model.Cerveza;
import com.lupulo.cerveceria.model.Venta;
import com.lupulo.cerveceria.repository.CervezaRepository;
import com.lupulo.cerveceria.repository.VentaRepository;
import org.springframework.stereotype.Service;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;
import java.util.List;

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

    String email = SecurityContextHolder.getContext().getAuthentication().getName();
    cerveza.setStock(cerveza.getStock() - request.getCantidad());
    cervezaRepository.save(cerveza);

    Venta venta = Venta.builder()
        .cerveza(cerveza)
        .cantidad(request.getCantidad())
        .fecha(LocalDateTime.now())
        .usuarioEmail(email)
        .build();

    ventaRepository.save(venta);
  }

  public List<Venta> listarTodas() {
    return ventaRepository.findAll();
  }

  public List<Venta> buscarPorRangoDeFechas(LocalDateTime desde, LocalDateTime hasta) {
    return ventaRepository.findByFechaBetween(desde, hasta);
  }
}
