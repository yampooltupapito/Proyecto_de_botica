package com.botica.service.impl;

import com.botica.dto.DetalleVentaDTO;
import com.botica.dto.VentaDTO;
import com.botica.entity.DetalleVenta;
import com.botica.entity.Producto;
import com.botica.entity.Usuario;
import com.botica.entity.Venta;
import com.botica.exception.BusinessException;
import com.botica.exception.ResourceNotFoundException;
import com.botica.repository.ProductoRepository;
import com.botica.repository.UsuarioRepository;
import com.botica.repository.VentaRepository;
import com.botica.service.interfaces.IVentaService;
import com.botica.util.VentaMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class VentaServiceImpl implements IVentaService {

    private final VentaRepository ventaRepository;
    private final UsuarioRepository usuarioRepository;
    private final ProductoRepository productoRepository;

    public VentaServiceImpl(VentaRepository ventaRepository,
                             UsuarioRepository usuarioRepository,
                             ProductoRepository productoRepository) {
        this.ventaRepository = ventaRepository;
        this.usuarioRepository = usuarioRepository;
        this.productoRepository = productoRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<VentaDTO> listarTodas() {
        return ventaRepository.findAll().stream()
                .map(VentaMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public VentaDTO buscarPorId(Long id) {
        Venta venta = ventaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Venta", id));
        return VentaMapper.toDTO(venta);
    }

    @Override
    public VentaDTO crear(VentaDTO ventaDTO) {
        Usuario usuario = usuarioRepository.findById(ventaDTO.getUsuarioId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", ventaDTO.getUsuarioId()));

        Venta venta = new Venta();
        venta.setFecha(LocalDateTime.now());
        venta.setUsuario(usuario);

        List<DetalleVenta> detalles = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;

        for (DetalleVentaDTO detalleDTO : ventaDTO.getDetalles()) {
            Producto producto = productoRepository.findById(detalleDTO.getProductoId())
                    .orElseThrow(() -> new ResourceNotFoundException("Producto", detalleDTO.getProductoId()));

            if (producto.getStock() < detalleDTO.getCantidad()) {
                throw new BusinessException("Stock insuficiente para el producto: " + producto.getNombre());
            }

            BigDecimal subtotal = producto.getPrecio().multiply(BigDecimal.valueOf(detalleDTO.getCantidad()));

            DetalleVenta detalle = new DetalleVenta();
            detalle.setProducto(producto);
            detalle.setCantidad(detalleDTO.getCantidad());
            detalle.setPrecioUnitario(producto.getPrecio());
            detalle.setSubtotal(subtotal);
            detalle.setVenta(venta);

            producto.setStock(producto.getStock() - detalleDTO.getCantidad());
            productoRepository.save(producto);

            detalles.add(detalle);
            total = total.add(subtotal);
        }

        venta.setDetalles(detalles);
        venta.setTotal(total);

        Venta guardada = ventaRepository.save(venta);
        return VentaMapper.toDTO(guardada);
    }

    @Override
    public VentaDTO actualizar(Long id, VentaDTO ventaDTO) {
        Venta existente = ventaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Venta", id));

        Usuario usuario = usuarioRepository.findById(ventaDTO.getUsuarioId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", ventaDTO.getUsuarioId()));

        for (DetalleVenta detalleAnterior : existente.getDetalles()) {
            Producto producto = detalleAnterior.getProducto();
            producto.setStock(producto.getStock() + detalleAnterior.getCantidad());
            productoRepository.save(producto);
        }
        existente.getDetalles().clear();

        BigDecimal total = BigDecimal.ZERO;
        for (DetalleVentaDTO detalleDTO : ventaDTO.getDetalles()) {
            Producto producto = productoRepository.findById(detalleDTO.getProductoId())
                    .orElseThrow(() -> new ResourceNotFoundException("Producto", detalleDTO.getProductoId()));

            if (producto.getStock() < detalleDTO.getCantidad()) {
                throw new BusinessException("Stock insuficiente para el producto: " + producto.getNombre());
            }

            BigDecimal subtotal = producto.getPrecio().multiply(BigDecimal.valueOf(detalleDTO.getCantidad()));

            DetalleVenta nuevoDetalle = new DetalleVenta();
            nuevoDetalle.setProducto(producto);
            nuevoDetalle.setCantidad(detalleDTO.getCantidad());
            nuevoDetalle.setPrecioUnitario(producto.getPrecio());
            nuevoDetalle.setSubtotal(subtotal);
            nuevoDetalle.setVenta(existente);

            producto.setStock(producto.getStock() - detalleDTO.getCantidad());
            productoRepository.save(producto);

            existente.getDetalles().add(nuevoDetalle);
            total = total.add(subtotal);
        }

        existente.setUsuario(usuario);
        existente.setTotal(total);

        Venta actualizada = ventaRepository.save(existente);
        return VentaMapper.toDTO(actualizada);
    }

    @Override
    public void eliminar(Long id) {
        Venta venta = ventaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Venta", id));

        for (DetalleVenta detalle : venta.getDetalles()) {
            Producto producto = detalle.getProducto();
            producto.setStock(producto.getStock() + detalle.getCantidad());
            productoRepository.save(producto);
        }

        ventaRepository.deleteById(id);
    }
}
