package com.lupulo.cerveceria.service;

import com.lupulo.cerveceria.model.Cerveza;
import com.lupulo.cerveceria.repository.CervezaRepository;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CervezaService {

  private final CervezaRepository repository;

  public CervezaService(CervezaRepository repository) {
    this.repository = repository;
  }

  public List<Cerveza> listarTodas() {
    return repository.findAll();
  }

  public Optional<Cerveza> buscarPorId(Long id) {
    return repository.findById(id);
  }

  public Cerveza guardar(Cerveza cerveza) {
    return repository.save(cerveza);
  }

  public void eliminar(Long id) {
    repository.deleteById(id);
  }

  public Optional<Cerveza> buscarPorCodigoBarras(String codigoBarras) {
    return repository.findAll().stream()
        .filter(c -> c.getCodigoBarras().equalsIgnoreCase(codigoBarras))
        .findFirst();
  }

  public Page<Cerveza> buscarConFiltros(
      int page, int size, String orden, String direccion,
      String estilo, String nombre,
      String paisOrigen,
      Double precioMin, Double precioMax,
      Double graduacionMin, Double graduacionMax) {

    Sort sort = direccion.equalsIgnoreCase("desc") ? Sort.by(orden).descending() : Sort.by(orden).ascending();
    Pageable pageable = PageRequest.of(page, size, sort);

    List<Cerveza> todas = repository.findAll();

    List<Cerveza> filtradas = todas.stream()
        .filter(c -> estilo == null || c.getEstilo().equalsIgnoreCase(estilo))
        .filter(c -> nombre == null || c.getNombre().toLowerCase().contains(nombre.toLowerCase()))
        .filter(c -> paisOrigen == null || c.getPaisOrigen().equalsIgnoreCase(paisOrigen))
        .filter(c -> precioMin == null || c.getPrecio() >= precioMin)
        .filter(c -> precioMax == null || c.getPrecio() <= precioMax)
        .filter(c -> graduacionMin == null || c.getGraduacion() >= graduacionMin)
        .filter(c -> graduacionMax == null || c.getGraduacion() <= graduacionMax)
        .toList();

    int start = (int) pageable.getOffset();
    int end = Math.min(start + pageable.getPageSize(), filtradas.size());

    List<Cerveza> pageContent = start <= end ? filtradas.subList(start, end) : List.of();

    return new PageImpl<>(pageContent, pageable, filtradas.size());
  }
}
