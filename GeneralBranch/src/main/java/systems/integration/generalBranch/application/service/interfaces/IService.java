package systems.integration.generalBranch.application.service.interfaces;

import java.util.List;
import java.util.Optional;

public interface IService<T, ID> {
    List<T> findAll();
    Optional<T> findById(ID id);
    T save(T entity);
    T update(ID id, T entity);
    boolean deleteById(ID id);
}
