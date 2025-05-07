package com.lupulo.cerveceria.service;

import com.lupulo.cerveceria.model.Cerveza;
import com.lupulo.cerveceria.repository.CervezaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.*;

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

  public List<Cerveza> buscarPorEstilo(String estilo) {
    return repository.findByEstiloIgnoreCase(estilo);
  }

  public List<Cerveza> buscarPorNombre(String nombre) {
    return repository.findByNombreContainingIgnoreCase(nombre);
  }

  public List<Cerveza> listarOrdenadas(String orden, String direccion) {
    Sort sort = direccion.equalsIgnoreCase("desc") ? Sort.by(orden).descending() : Sort.by(orden).ascending();

    return repository.findAll(sort);
  }

  public Page<Cerveza> buscarConFiltros(int page, int size, String orden, String direccion, String estilo,
      String nombre) {
    Sort sort = direccion.equalsIgnoreCase("desc") ? Sort.by(orden).descending() : Sort.by(orden).ascending();

    Pageable pageable = PageRequest.of(page, size, sort);

    if (estilo != null) {
      return repository.findByEstiloIgnoreCase(estilo, pageable);
    } else if (nombre != null) {
      return repository.findByNombreContainingIgnoreCase(nombre, pageable);
    } else {
      return repository.findAll(pageable);
    }
  }

  public Optional<Cerveza> buscarPorCodigoBarras(String codigoBarras) {
    return repository.findByCodigoBarras(codigoBarras);
  }
}
