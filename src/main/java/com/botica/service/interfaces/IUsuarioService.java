package com.botica.service.interfaces;

import com.botica.dto.UsuarioDTO;

import java.util.List;

public interface IUsuarioService {

    List<UsuarioDTO> listarTodos();

    UsuarioDTO buscarPorId(Long id);

    UsuarioDTO crear(UsuarioDTO usuarioDTO);

    UsuarioDTO actualizar(Long id, UsuarioDTO usuarioDTO);

    void eliminar(Long id);
}
