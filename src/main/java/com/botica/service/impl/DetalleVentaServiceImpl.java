package com.botica.service.impl;

import com.botica.dto.DetalleVentaDTO;
import com.botica.entity.DetalleVenta;
import com.botica.exception.ResourceNotFoundException;
import com.botica.repository.DetalleVentaRepository;
import com.botica.repository.VentaRepository;
import com.botica.service.interfaces.IDetalleVentaService;
import com.botica.util.VentaMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class DetalleVentaServiceImpl implements IDetalleVentaService {

    private final DetalleVentaRepository detalleVentaRepository;
    private final VentaRepository ventaRepository;

    public DetalleVentaServiceImpl(DetalleVentaRepository detalleVentaRepository, VentaRepository ventaRepository) {
        this.detalleVentaRepository = detalleVentaRepository;
        this.ventaRepository = ventaRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<DetalleVentaDTO> listarPorVenta(Long ventaId) {
        if (!ventaRepository.existsById(ventaId)) {
            throw new ResourceNotFoundException("Venta", ventaId);
        }
        return detalleVentaRepository.findAll().stream()
                .filter(detalle -> detalle.getVenta().getId().equals(ventaId))
                .map(VentaMapper::toDetalleDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public DetalleVentaDTO buscarPorId(Long id) {
        DetalleVenta detalle = detalleVentaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("DetalleVenta", id));
        return VentaMapper.toDetalleDTO(detalle);
    }
}
