package com.botica.controller;

import com.botica.dto.DetalleVentaDTO;
import com.botica.service.interfaces.IDetalleVentaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/detalles-venta")
public class DetalleVentaController {

    private final IDetalleVentaService detalleVentaService;

    public DetalleVentaController(IDetalleVentaService detalleVentaService) {
        this.detalleVentaService = detalleVentaService;
    }

    @GetMapping("/venta/{ventaId}")
    public ResponseEntity<List<DetalleVentaDTO>> listarPorVenta(@PathVariable Long ventaId) {
        return ResponseEntity.ok(detalleVentaService.listarPorVenta(ventaId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DetalleVentaDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(detalleVentaService.buscarPorId(id));
    }
}
