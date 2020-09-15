package dmo.server.api.v1.controller;

import dmo.server.api.v1.dto.AnimeDto;
import dmo.server.api.v1.dto.AnimeLightDto;
import dmo.server.api.v1.dto.EpisodeDto;
import dmo.server.api.v1.dto.PageDto;
import dmo.server.api.v1.mapper.AnimeMapper;
import dmo.server.domain.Anime;
import dmo.server.service.AnimeService;
import dmo.server.service.EpisodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@RestController
@RequestMapping("api/v1/anime")
@Validated
@RequiredArgsConstructor
public class AnimeController {
    private final AnimeService animeService;
    private final EpisodeService episodeService;

    @Autowired
    private AnimeMapper animeMapper;

    @GetMapping
    public PageDto<AnimeLightDto> findAll(@RequestParam("page") @Min(0) int page,
                                          @RequestParam("size") @Min(1) @Max(100) int size) {
        var pageRequest = PageRequest.of(page, size, Sort.by("id"));
        var result = animeService.findAll(pageRequest);
        return animeMapper.toAnimePageDto(result);
    }

    @GetMapping("{id}")
    public AnimeDto getAnime(@PathVariable("id") Long id) {
        // TODO return 404 if no anime
        Anime result = animeService.findById(id).orElse(null);
        return animeMapper.toAnimeDto(result);
    }

    @GetMapping("{id}/episodes")
    public PageDto<EpisodeDto> getEpisodes(@PathVariable("id") Long id,
                                           @RequestParam("page") @Min(0) int page,
                                           @RequestParam("size") @Min(1) @Max(100) int size) {
        var pageRequest = PageRequest.of(page, size, Sort.by("type", "number"));
        var result = episodeService.findAll(pageRequest, id);
        return animeMapper.toEpisodePageDto(result);
    }
}