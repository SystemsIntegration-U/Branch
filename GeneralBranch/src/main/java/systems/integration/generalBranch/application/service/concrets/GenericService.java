package systems.integration.generalBranch.application.service.concrets;

import org.springframework.data.jpa.repository.JpaRepository;
import systems.integration.generalBranch.application.service.interfaces.IService;

import java.util.List;
import java.util.Optional;

public abstract class GenericService<T, ID> implements IService<T, ID> {

    protected final JpaRepository<T, ID> repository;

    public GenericService(JpaRepository<T, ID> repository) {
        this.repository = repository;
    }

    @Override
    public List<T> findAll() {
        return repository.findAll();
    }

    @Override
    public Optional<T> findById(ID id) {
        return repository.findById(id);
    }

    @Override
    public T save(T entity) {
        return repository.save(entity);
    }

    @Override
    public T update(ID id, T entity) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Entidad no encontrada con id: " + id);
        }
        return repository.save(entity);
    }

    @Override
    public boolean deleteById(ID id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }
}