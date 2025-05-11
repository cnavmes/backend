package com.lupulo.cerveceria.service;

import com.lupulo.cerveceria.model.Usuario;
import com.lupulo.cerveceria.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UsuarioService {

  private final UsuarioRepository usuarioRepository;
  private final PasswordEncoder passwordEncoder;

  public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
    this.usuarioRepository = usuarioRepository;
    this.passwordEncoder = passwordEncoder;
  }

  public Usuario registrarUsuario(Usuario usuario) {
    // Codificar la contraseña antes de guardar
    usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
    return usuarioRepository.save(usuario);
  }

  public Optional<Usuario> buscarPorEmail(String email) {
    return usuarioRepository.findByEmail(email);
  }
}
