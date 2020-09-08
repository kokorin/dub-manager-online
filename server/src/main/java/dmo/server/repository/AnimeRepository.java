package dmo.server.repository;

import dmo.server.domain.Anime;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnimeRepository extends JpaRepository<Anime, Long> {

    @Query("FROM Anime a JOIN a.titles t")
    @EntityGraph(type = EntityGraph.EntityGraphType.LOAD, attributePaths = "titles")
    List<Anime> findAllWithTitles();

    /* TODO add anime search by title with pagination:

    select a.*, t.* from anime_title t
    join (select a.*
     from anime a
     where a.id in (
         select t.anime_id
         from anime_title t
         where match(t.text) against('Sword Art Online Alici' in boolean mode )
     )
     order by id
     limit 10 offset 10
    ) a on a.id = t.anime_id
    order by a.id;

    select count(distinct t.anime_id)
    from anime_title t
    where match(t.text) against('+Sword +Art +Online Alici')

     */
}