package dmo.server.repository;

import dmo.server.domain.TrackedEpisode;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrackedEpisodeRepository extends CrudRepository<TrackedEpisode, TrackedEpisode.ID> {
}
