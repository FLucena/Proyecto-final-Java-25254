package com.techlab.picadito.service;

import com.techlab.picadito.dto.CalificacionDTO;
import com.techlab.picadito.dto.CalificacionResponseDTO;
import com.techlab.picadito.exception.BusinessException;
import com.techlab.picadito.exception.ResourceNotFoundException;
import com.techlab.picadito.model.Calificacion;
import com.techlab.picadito.model.EstadoPartido;
import com.techlab.picadito.model.Partido;
import com.techlab.picadito.model.Usuario;
import com.techlab.picadito.repository.CalificacionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CalificacionService {

    private static final Logger logger = LoggerFactory.getLogger(CalificacionService.class);

    @Autowired
    private CalificacionRepository calificacionRepository;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private PartidoService partidoService;

    public CalificacionResponseDTO crear(@NonNull Long usuarioId, CalificacionDTO calificacionDTO) {
        logger.info("Creando calificación del usuario {} para el partido {}", usuarioId, calificacionDTO.getPartidoId());
        
        Usuario usuario = usuarioService.obtenerUsuarioEntity(usuarioId);
        Partido partido = partidoService.obtenerPartidoEntity(calificacionDTO.getPartidoId());
        
        // Validar que el partido haya finalizado
        if (partido.getEstado() != EstadoPartido.FINALIZADO) {
            throw new BusinessException("Solo se pueden calificar partidos que han finalizado");
        }
        
        // Validar que el usuario no haya calificado este partido antes
        @SuppressWarnings("null")
        Long partidoId = calificacionDTO.getPartidoId();
        if (calificacionRepository.existsByUsuarioIdAndPartidoId(usuarioId, partidoId)) {
            throw new BusinessException("Ya has calificado este partido");
        }
        
        Calificacion calificacion = new Calificacion();
        calificacion.setPuntuacion(calificacionDTO.getPuntuacion());
        calificacion.setComentario(calificacionDTO.getComentario());
        calificacion.setUsuario(usuario);
        calificacion.setPartido(partido);
        
        calificacion = calificacionRepository.save(calificacion);
        logger.info("Calificación creada exitosamente con id: {}", calificacion.getId());
        return convertirADTO(calificacion);
    }

    public List<CalificacionResponseDTO> obtenerPorPartido(@NonNull Long partidoId) {
        logger.debug("Obteniendo calificaciones del partido {}", partidoId);
        return calificacionRepository.findByPartidoIdOrderByFechaCreacionDesc(partidoId).stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public Double obtenerPromedioPorPartido(@NonNull Long partidoId) {
        logger.debug("Calculando promedio de calificaciones del partido {}", partidoId);
        Double promedio = calificacionRepository.calcularPromedioPorPartido(partidoId);
        return promedio != null ? promedio : 0.0;
    }

    public Double obtenerPromedioPorCreador(@NonNull String creadorNombre) {
        logger.debug("Calculando promedio de calificaciones del creador {}", creadorNombre);
        Double promedio = calificacionRepository.calcularPromedioPorCreador(creadorNombre);
        return promedio != null ? promedio : 0.0;
    }

    public Double obtenerPromedioPorSede(@NonNull Long sedeId) {
        logger.debug("Calculando promedio de calificaciones de la sede {}", sedeId);
        Double promedio = calificacionRepository.calcularPromedioPorSede(sedeId);
        return promedio != null ? promedio : 0.0;
    }

    public CalificacionResponseDTO obtenerPorId(@NonNull Long id) {
        logger.debug("Buscando calificación con id: {}", id);
        Calificacion calificacion = calificacionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Calificación no encontrada con id: " + id));
        return convertirADTO(calificacion);
    }

    public void eliminar(@NonNull Long id) {
        logger.info("Eliminando calificación con id: {}", id);
        if (!calificacionRepository.existsById(id)) {
            throw new ResourceNotFoundException("Calificación no encontrada con id: " + id);
        }
        calificacionRepository.deleteById(id);
        logger.info("Calificación eliminada exitosamente");
    }

    private CalificacionResponseDTO convertirADTO(Calificacion calificacion) {
        CalificacionResponseDTO dto = new CalificacionResponseDTO();
        dto.setId(calificacion.getId());
        dto.setPuntuacion(calificacion.getPuntuacion());
        dto.setComentario(calificacion.getComentario());
        dto.setFechaCreacion(calificacion.getFechaCreacion());
        
        if (calificacion.getUsuario() != null) {
            dto.setUsuarioId(calificacion.getUsuario().getId());
            dto.setUsuarioNombre(calificacion.getUsuario().getNombre());
        }
        
        if (calificacion.getPartido() != null) {
            dto.setPartidoId(calificacion.getPartido().getId());
            dto.setPartidoTitulo(calificacion.getPartido().getTitulo());
        }
        
        return dto;
    }
}

