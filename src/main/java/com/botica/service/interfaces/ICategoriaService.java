package com.botica.service.interfaces;

import com.botica.dto.CategoriaDTO;

import java.util.List;

public interface ICategoriaService {

    List<CategoriaDTO> listarTodas();

    CategoriaDTO buscarPorId(Long id);

    CategoriaDTO crear(CategoriaDTO categoriaDTO);

    CategoriaDTO actualizar(Long id, CategoriaDTO categoriaDTO);

    void eliminar(Long id);
}
