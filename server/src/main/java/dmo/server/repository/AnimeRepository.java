package dmo.server.repository;

import dmo.server.domain.Anime;
import dmo.server.domain.ExternalSystem;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AnimeRepository extends CrudRepository<Anime, Long> {
    @NonNull
    List<Anime> findAllByExternalSystem(@NonNull ExternalSystem externalSystem);
}