package com.lupulo.cerveceria.controller;

import com.lupulo.cerveceria.dto.MovimientoStockRespuestaDTO;
import com.lupulo.cerveceria.model.MovimientoStock;
import com.lupulo.cerveceria.service.MovimientoStockService;
import com.lupulo.cerveceria.util.CsvExporter;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

@RestController
@RequestMapping("/api/movimientos")
@CrossOrigin(origins = "*")
public class MovimientoStockController {

  private final MovimientoStockService service;

  public MovimientoStockController(MovimientoStockService service) {
    this.service = service;
  }

  @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
  @GetMapping("/filtro")
  public MovimientoStockRespuestaDTO filtrar(
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "5") int size,
      @RequestParam(required = false) String tipo,
      @RequestParam(required = false) String desde,
      @RequestParam(required = false) String hasta,
      @RequestParam(required = false) String usuarioEmail,
      @RequestParam(required = false) String cervezaNombre) {

    LocalDateTime desdeFecha = (desde != null) ? LocalDate.parse(desde).atStartOfDay() : null;
    LocalDateTime hastaFecha = (hasta != null) ? LocalDate.parse(hasta).atTime(23, 59, 59) : null;

    return service.filtrarMovimientos(tipo, desdeFecha, hastaFecha, usuarioEmail, cervezaNombre, page, size);
  }

  @PreAuthorize("hasRole('ADMIN')")
  @GetMapping("/exportar")
  public ResponseEntity<byte[]> exportarMovimientosCsv() {
    List<MovimientoStock> lista = service.listarTodos(); // CORREGIDO: usamos el nombre correcto del atributo
    String contenido = CsvExporter.generarCsvMovimientos(lista);
    byte[] datos = contenido.getBytes();

    return ResponseEntity.ok()
        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=movimientos_stock.csv")
        .contentType(MediaType.parseMediaType("text/csv"))
        .body(datos);
  }
}
