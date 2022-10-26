package ru.homethings.overall.repositories;

import org.springframework.data.repository.CrudRepository;
import ru.homethings.overall.model.Promise;

public interface PromiseRepository extends CrudRepository<Promise, Long> {
}
