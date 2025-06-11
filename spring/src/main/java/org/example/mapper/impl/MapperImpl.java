package org.example.mapper.impl;

import org.example.mapper.Mapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MapperImpl<E, D>
        implements Mapper<E, D> {

    static {
        Mapper.mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
    }

    @Override
    public D toDto(E entity, Class<D> type) {
        return mapper.map(entity, type);
    }

    @Override
    public E toEntity(D dto, Class<E> type) {
        return mapper.map(dto, type);
    }

    @Override
    public List<D> toDtos(List<E> entities, Class<D> type) {
        return entities.stream().map(e -> mapper.map(e, type)).toList();
    }

    @Override
    public List<E> toEntities(List<D> dtos, Class<E> type) {
        return dtos.stream().map(d -> toEntity(d, type)).toList();
    }
}
