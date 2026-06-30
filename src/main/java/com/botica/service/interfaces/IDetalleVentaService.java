package com.botica.service.interfaces;

import com.botica.dto.DetalleVentaDTO;

import java.util.List;

public interface IDetalleVentaService {

    List<DetalleVentaDTO> listarPorVenta(Long ventaId);

    DetalleVentaDTO buscarPorId(Long id);
}
