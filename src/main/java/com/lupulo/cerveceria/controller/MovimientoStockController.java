package com.lupulo.cerveceria.controller;

import com.lupulo.cerveceria.dto.ReponerStockRequest;
import com.lupulo.cerveceria.service.MovimientoStockService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/movimientos")
@CrossOrigin(origins = "*")
public class MovimientoStockController {

  private final MovimientoStockService service;

  public MovimientoStockController(MovimientoStockService service) {
    this.service = service;
  }

  @PreAuthorize("hasRole('ADMIN')")
  @PostMapping("/reponer/{cervezaId}")
  public ResponseEntity<String> reponer(@PathVariable Long cervezaId, @RequestBody ReponerStockRequest request) {
    service.reponerStock(cervezaId, request);
    return ResponseEntity.ok("Stock repuesto correctamente");
  }
}