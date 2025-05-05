package com.lupulo.cerveceria.service;

import com.lupulo.cerveceria.model.Cerveza;
import com.lupulo.cerveceria.repository.CervezaRepository;
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

  public List<Cerveza> buscarPorEstilo(String estilo) {
    return repository.findByEstiloIgnoreCase(estilo);
  }

  public List<Cerveza> buscarPorNombre(String nombre) {
    return repository.findByNombreContainingIgnoreCase(nombre);
  }
}
