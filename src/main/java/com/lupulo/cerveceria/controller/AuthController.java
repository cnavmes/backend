package com.lupulo.cerveceria.controller;

import com.lupulo.cerveceria.auth.*;
import com.lupulo.cerveceria.model.Usuario;
import com.lupulo.cerveceria.service.UsuarioService;
import com.lupulo.cerveceria.security.JwtService;

import java.util.Map;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class AuthController {

  private final UsuarioService usuarioService;
  private final JwtService jwtService;
  private final AuthenticationManager authManager;

  public AuthController(UsuarioService usuarioService, JwtService jwtService, AuthenticationManager authManager) {
    this.usuarioService = usuarioService;
    this.jwtService = jwtService;
    this.authManager = authManager;
  }

  @PostMapping("/register")
  public AuthResponse register(@RequestBody RegisterRequest request) {
    Usuario usuario = Usuario.builder()
        .email(request.getEmail())
        .password(request.getPassword())
        .rol(request.getRol())
        .build();

    usuarioService.registrarUsuario(usuario);

    String jwt = jwtService.generateToken(
        Map.of("rol", usuario.getRol().name()),
        usuario.getEmail());

    return new AuthResponse(jwt);
  }

  @PostMapping("/login")
  public AuthResponse login(@RequestBody AuthRequest request) {

    // Autenticación realizada, variable 'auth' reservada para posibles futuras
    // implementaciones
    Authentication auth = authManager.authenticate(
        new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

    Usuario usuario = usuarioService.buscarPorEmail(request.getEmail()).orElseThrow();
    String jwt = jwtService.generateToken(
        Map.of("rol", usuario.getRol().name()),
        usuario.getEmail());

    return new AuthResponse(jwt);
  }
}
