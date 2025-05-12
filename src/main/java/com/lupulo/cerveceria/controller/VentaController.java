package com.lupulo.cerveceria.controller;

import com.lupulo.cerveceria.dto.VentaRequest;
import com.lupulo.cerveceria.model.Venta;
import com.lupulo.cerveceria.service.VentaService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ventas")
@CrossOrigin(origins = "*")
public class VentaController {

  private final VentaService ventaService;

  public VentaController(VentaService ventaService) {
    this.ventaService = ventaService;
  }

  @PreAuthorize("hasRole('ADMIN')")
  @PostMapping
  public ResponseEntity<String> registrarVenta(@RequestBody VentaRequest request) {
    ventaService.registrarVenta(request);
    return ResponseEntity.ok("Venta registrada correctamente");
  }

  @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
  @GetMapping
  public List<Venta> obtenerVentas() {
    return ventaService.listarTodas();
  }

  @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
  @GetMapping("/filtro-fechas")
  public List<Venta> ventasPorFechas(
      @RequestParam String desde,
      @RequestParam String hasta) {

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    LocalDateTime desdeFecha = LocalDate.parse(desde, formatter).atStartOfDay();
    LocalDateTime hastaFecha = LocalDate.parse(hasta, formatter).atTime(23, 59, 59);

    return ventaService.buscarPorRangoDeFechas(desdeFecha, hastaFecha);
  }
}