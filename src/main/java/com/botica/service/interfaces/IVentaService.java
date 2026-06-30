package com.botica.service.interfaces;

import com.botica.dto.VentaDTO;

import java.util.List;

public interface IVentaService {

    List<VentaDTO> listarTodas();

    VentaDTO buscarPorId(Long id);

    VentaDTO crear(VentaDTO ventaDTO);

    VentaDTO actualizar(Long id, VentaDTO ventaDTO);

    void eliminar(Long id);
}
