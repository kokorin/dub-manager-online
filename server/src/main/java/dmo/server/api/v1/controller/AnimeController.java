package dmo.server.api.v1.controller;

import dmo.server.api.v1.dto.AnimeDto;
import dmo.server.api.v1.dto.AnimeLightDto;
import dmo.server.api.v1.dto.EpisodeDto;
import dmo.server.api.v1.dto.PageDto;
import dmo.server.api.v1.mapper.AnimeMapper;
import dmo.server.domain.Anime;
import dmo.server.service.AnimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;

@RestController
@RequestMapping("api/v1/anime")
@Validated
public class AnimeController {

    @Autowired
    private AnimeService animeService;

    @Autowired
    private AnimeMapper animeMapper;

    @GetMapping
    public PageDto<AnimeLightDto> findAll(@RequestParam("page") int page,
                                          @RequestParam("size") @Min(1) int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Anime> result = animeService.findAll(pageRequest);
        return animeMapper.toPageDto(result);
    }

    @GetMapping("{id}")
    public AnimeDto getAnime(@PathVariable("id") Long id) {
        // TODO return 404 if no anime
        Anime result = animeService.findById(id).orElse(null);
        return animeMapper.toDto(result);
    }

    @GetMapping("{id}/episodes")
    public PageDto<EpisodeDto> getEpisodes(@PathVariable("id") Long id,
                                           @RequestParam("page") int page,
                                           @RequestParam("size") @Min(1) int size) {
        return null;
    }
}