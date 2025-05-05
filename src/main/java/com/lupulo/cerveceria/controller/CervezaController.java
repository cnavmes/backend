package com.lupulo.cerveceria.controller;

import com.lupulo.cerveceria.model.Cerveza;
import com.lupulo.cerveceria.service.CervezaService;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/cervezas")
@CrossOrigin(origins = "*") // Para permitir peticiones desde Angular
public class CervezaController {

  private final CervezaService service;

  public CervezaController(CervezaService service) {
    this.service = service;
  }

  // @GetMapping
  // public List<Cerveza> obtenerTodas() {
  // return service.listarTodas();
  // }

  @GetMapping("/{id}")
  public Optional<Cerveza> obtenerPorId(@PathVariable Long id) {
    return service.buscarPorId(id);
  }

  @PostMapping
  public Cerveza crearCerveza(@Valid @RequestBody Cerveza cerveza) {
    return service.guardar(cerveza);
  }

  @PutMapping("/{id}")
  public Cerveza actualizarCerveza(@PathVariable Long id, @Valid @RequestBody Cerveza cerveza) {
    cerveza.setId(id);
    return service.guardar(cerveza);
  }

  @DeleteMapping("/{id}")
  public void eliminarCerveza(@PathVariable Long id) {
    service.eliminar(id);
  }

  @GetMapping
  public List<Cerveza> obtenerCervezas(
      @RequestParam(required = false) String estilo,
      @RequestParam(required = false) String nombre) {

    if (estilo != null) {
      return service.buscarPorEstilo(estilo);
    } else if (nombre != null) {
      return service.buscarPorNombre(nombre);
    } else {
      return service.listarTodas();
    }
  }

}
