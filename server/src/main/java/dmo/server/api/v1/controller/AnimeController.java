package dmo.server.api.v1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import dmo.server.api.v1.dto.AnimeDto;
import dmo.server.api.v1.dto.PageDto;
import dmo.server.api.v1.mapper.AnimeMapper;
import dmo.server.domain.Anime;
import dmo.server.service.AnimeService;

@RestController
@RequestMapping(value = "api/v1/anime")
public class AnimeController {
    
    @Autowired
    private AnimeService animeService;

    @Autowired
    private AnimeMapper animeMapper;

    @GetMapping
    public PageDto<AnimeDto> findAll(@RequestParam int page, @RequestParam int size) {
        
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Anime> result = animeService.findAll(pageRequest);
        return animeMapper.toPageDto(result);
    }
}