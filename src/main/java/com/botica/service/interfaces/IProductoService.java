package com.botica.service.interfaces;

import com.botica.dto.ProductoDTO;

import java.util.List;

public interface IProductoService {

    List<ProductoDTO> listarTodos();

    ProductoDTO buscarPorId(Long id);

    List<ProductoDTO> buscarPorCategoria(Long categoriaId);

    ProductoDTO crear(ProductoDTO productoDTO);

    ProductoDTO actualizar(Long id, ProductoDTO productoDTO);

    void eliminar(Long id);
}
