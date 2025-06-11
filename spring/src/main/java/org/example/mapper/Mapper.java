package org.example.mapper;

import org.modelmapper.ModelMapper;

import java.util.List;

/**
 * Interface for convert Entities and DTOs in both ways
 *
 * @param <E> entity for convert
 * @param <D> dto for convert
 */
public interface Mapper<E, D> {

    ModelMapper mapper = new ModelMapper();

    /**
     * Map entity to dto
     *
     * @param entity source will be convert to dto
     * @param type   the class type to convert to
     * @return DTO of type <i>type</i>
     */
    D toDto(E entity, Class<D> type);

    /**
     * Map entity to dto
     *
     * @param dto  source will be convert to entity
     * @param type the class type to convert to
     * @return Entity of type <i>type</i>
     */
    E toEntity(D dto, Class<E> type);

    /**
     * Map entity to dto
     *
     * @param entities source list will be convert to list dtos
     * @param type     the class type to convert to
     * @return List DTOs of type <i>type</i>
     */
    List<D> toDtos(List<E> entities, Class<D> type);

    /**
     * Map entity to dto
     *
     * @param dtos source list will be convert to list entities
     * @param type the class type to convert to
     * @return List Entities of type <i>type</i>
     */
    List<E> toEntities(List<D> dtos, Class<E> type);

}