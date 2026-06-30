package com.botica.service.impl;

import com.botica.dto.CategoriaDTO;
import com.botica.entity.Categoria;
import com.botica.exception.BusinessException;
import com.botica.exception.ResourceNotFoundException;
import com.botica.repository.CategoriaRepository;
import com.botica.service.interfaces.ICategoriaService;
import com.botica.util.CategoriaMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CategoriaServiceImpl implements ICategoriaService {

    private final CategoriaRepository categoriaRepository;

    public CategoriaServiceImpl(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoriaDTO> listarTodas() {
        return categoriaRepository.findAll().stream()
                .map(CategoriaMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public CategoriaDTO buscarPorId(Long id) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria", id));
        return CategoriaMapper.toDTO(categoria);
    }

    @Override
    public CategoriaDTO crear(CategoriaDTO categoriaDTO) {
        Categoria categoria = CategoriaMapper.toEntity(categoriaDTO);
        categoria.setId(null);
        Categoria guardada = categoriaRepository.save(categoria);
        return CategoriaMapper.toDTO(guardada);
    }

    @Override
    public CategoriaDTO actualizar(Long id, CategoriaDTO categoriaDTO) {
        Categoria existente = categoriaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria", id));

        existente.setNombre(categoriaDTO.getNombre());
        existente.setDescripcion(categoriaDTO.getDescripcion());

        Categoria actualizada = categoriaRepository.save(existente);
        return CategoriaMapper.toDTO(actualizada);
    }

    @Override
    public void eliminar(Long id) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria", id));
        if (!categoria.getProductos().isEmpty()) {
            throw new BusinessException("No se puede eliminar la categoria porque tiene productos asociados");
        }
        categoriaRepository.deleteById(id);
    }
}
