package com.lupulo.cerveceria.controller;

import com.lupulo.cerveceria.dto.VentaRequest;
import com.lupulo.cerveceria.service.VentaService;
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
}