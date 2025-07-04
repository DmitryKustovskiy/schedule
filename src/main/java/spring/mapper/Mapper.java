package spring.mapper;

import java.util.List;

public interface Mapper<D, E> {

	D toDto(E entity);

	E toEntity(D dto);

	default List<D> toDtoList(List<E> entityList) {
		if (entityList == null)
			return null;
		return entityList.stream().map(this::toDto).toList();
	}

	default List<E> toEntityList(List<D> dtoList) {
		if (dtoList == null)
			return null;
		return dtoList.stream().map(this::toEntity).toList();
	}

}
