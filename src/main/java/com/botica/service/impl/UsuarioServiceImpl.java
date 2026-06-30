package com.botica.service.impl;

import com.botica.dto.UsuarioDTO;
import com.botica.entity.Usuario;
import com.botica.exception.BusinessException;
import com.botica.exception.ResourceNotFoundException;
import com.botica.repository.UsuarioRepository;
import com.botica.service.interfaces.IUsuarioService;
import com.botica.util.UsuarioMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class UsuarioServiceImpl implements IUsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioServiceImpl(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<UsuarioDTO> listarTodos() {
        return usuarioRepository.findAll().stream()
                .map(UsuarioMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public UsuarioDTO buscarPorId(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", id));
        return UsuarioMapper.toDTO(usuario);
    }

    @Override
    public UsuarioDTO crear(UsuarioDTO usuarioDTO) {
        if (usuarioRepository.existsByEmail(usuarioDTO.getEmail())) {
            throw new BusinessException("Ya existe un usuario registrado con el correo: " + usuarioDTO.getEmail());
        }
        Usuario usuario = UsuarioMapper.toEntity(usuarioDTO);
        usuario.setId(null);
        Usuario guardado = usuarioRepository.save(usuario);
        return UsuarioMapper.toDTO(guardado);
    }

    @Override
    public UsuarioDTO actualizar(Long id, UsuarioDTO usuarioDTO) {
        Usuario existente = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", id));

        usuarioRepository.findByEmail(usuarioDTO.getEmail())
                .filter(u -> !u.getId().equals(id))
                .ifPresent(u -> {
                    throw new BusinessException("Ya existe otro usuario con el correo: " + usuarioDTO.getEmail());
                });

        existente.setNombre(usuarioDTO.getNombre());
        existente.setApellido(usuarioDTO.getApellido());
        existente.setEmail(usuarioDTO.getEmail());
        existente.setPassword(usuarioDTO.getPassword());
        existente.setRol(usuarioDTO.getRol());

        Usuario actualizado = usuarioRepository.save(existente);
        return UsuarioMapper.toDTO(actualizado);
    }

    @Override
    public void eliminar(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new ResourceNotFoundException("Usuario", id);
        }
        usuarioRepository.deleteById(id);
    }
}
