package com.botica.util;

import com.botica.dto.DetalleVentaDTO;
import com.botica.dto.VentaDTO;
import com.botica.entity.DetalleVenta;
import com.botica.entity.Venta;

import java.util.List;
import java.util.stream.Collectors;

public class VentaMapper {

    private VentaMapper() {
    }

    public static VentaDTO toDTO(Venta venta) {
        if (venta == null) {
            return null;
        }
        VentaDTO dto = new VentaDTO();
        dto.setId(venta.getId());
        dto.setFecha(venta.getFecha());
        dto.setTotal(venta.getTotal());
        if (venta.getUsuario() != null) {
            dto.setUsuarioId(venta.getUsuario().getId());
            dto.setUsuarioNombre(venta.getUsuario().getNombre() + " " + venta.getUsuario().getApellido());
        }
        List<DetalleVentaDTO> detalles = venta.getDetalles() == null ? List.of() :
                venta.getDetalles().stream()
                        .map(VentaMapper::toDetalleDTO)
                        .collect(Collectors.toList());
        dto.setDetalles(detalles);
        return dto;
    }

    public static DetalleVentaDTO toDetalleDTO(DetalleVenta detalle) {
        if (detalle == null) {
            return null;
        }
        DetalleVentaDTO dto = new DetalleVentaDTO();
        dto.setId(detalle.getId());
        if (detalle.getProducto() != null) {
            dto.setProductoId(detalle.getProducto().getId());
            dto.setProductoNombre(detalle.getProducto().getNombre());
        }
        dto.setCantidad(detalle.getCantidad());
        dto.setPrecioUnitario(detalle.getPrecioUnitario());
        dto.setSubtotal(detalle.getSubtotal());
        return dto;
    }
}
