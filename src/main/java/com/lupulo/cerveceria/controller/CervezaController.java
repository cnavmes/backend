package com.lupulo.cerveceria.controller;

import com.lupulo.cerveceria.dto.CervezaRespuestaDTO;
import com.lupulo.cerveceria.model.Cerveza;
import com.lupulo.cerveceria.service.CervezaService;
import com.lupulo.cerveceria.util.CsvExporter;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;

import org.springframework.http.MediaType;
import org.springframework.http.HttpHeaders;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/cervezas")
@CrossOrigin(origins = "*")
public class CervezaController {

  private final CervezaService service;

  public CervezaController(CervezaService service) {
    this.service = service;
  }

  @GetMapping("/{id}")
  public Optional<Cerveza> obtenerPorId(@PathVariable Long id) {
    return service.buscarPorId(id);
  }

  @PreAuthorize("hasRole('ADMIN')")
  @PostMapping
  public Cerveza crearCerveza(@Valid @RequestBody Cerveza cerveza) {
    return service.guardar(cerveza);
  }

  @PreAuthorize("hasRole('ADMIN')")
  @PutMapping("/{id}")
  public Cerveza actualizarCerveza(@PathVariable Long id, @Valid @RequestBody Cerveza cerveza) {
    cerveza.setId(id);
    return service.guardar(cerveza);
  }

  @PreAuthorize("hasRole('ADMIN')")
  @DeleteMapping("/{id}")
  public void eliminarCerveza(@PathVariable Long id) {
    service.eliminar(id);
  }

  @GetMapping
  public CervezaRespuestaDTO obtenerCervezas(
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "5") int size,
      @RequestParam(defaultValue = "id") String orden,
      @RequestParam(defaultValue = "asc") String direccion,
      @RequestParam(required = false) String estilo,
      @RequestParam(required = false) String nombre,
      @RequestParam(required = false) String paisOrigen,
      @RequestParam(required = false) Double precioMin,
      @RequestParam(required = false) Double precioMax,
      @RequestParam(required = false) Double graduacionMin,
      @RequestParam(required = false) Double graduacionMax,
      @RequestParam(required = false) String codigoBarras) {
    if (codigoBarras != null) {
      Optional<Cerveza> cerveza = service.buscarPorCodigoBarras(codigoBarras);
      List<Cerveza> lista = cerveza.map(List::of).orElse(List.of());
      return new CervezaRespuestaDTO(lista, 0, 1, lista.size());
    }

    Page<Cerveza> resultado = service.buscarConFiltros(
        page, size, orden, direccion,
        estilo, nombre, paisOrigen,
        precioMin, precioMax,
        graduacionMin, graduacionMax);

    return new CervezaRespuestaDTO(
        resultado.getContent(),
        resultado.getNumber(),
        resultado.getTotalPages(),
        resultado.getTotalElements());
  }

  @GetMapping("/barcode/{codigoBarras}")
  public Cerveza obtenerPorCodigoBarras(@PathVariable String codigoBarras) {
    return service.buscarPorCodigoBarras(codigoBarras)
        .orElseThrow(() -> new ResponseStatusException(
            HttpStatus.NOT_FOUND,
            "No se encontró ninguna cerveza con el código de barras: " + codigoBarras));
  }

  @GetMapping("/exportar")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<byte[]> exportarCervezasCsv() {
    List<Cerveza> lista = service.listarTodas();
    String contenido = CsvExporter.generarCsvCervezas(lista);
    byte[] datos = contenido.getBytes();

    return ResponseEntity.ok()
        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=cervezas.csv")
        .contentType(MediaType.parseMediaType("text/csv"))
        .body(datos);
  }
}
