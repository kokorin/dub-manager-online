package dmo.server.api.v1.controller;

import dmo.server.api.v1.dto.*;
import dmo.server.api.v1.mapper.AnimeMapper;
import dmo.server.domain.AnimeStatus;
import dmo.server.domain.Episode;
import dmo.server.domain.EpisodeStatus;
import dmo.server.security.JwtUser;
import dmo.server.service.AnimeStatusService;
import dmo.server.service.EpisodeStatusService;
import io.swagger.annotations.ApiOperation;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Optional;
import java.util.function.Consumer;

@RestController
@RequestMapping("api/v1/users/current/anime")
@Validated
@RequiredArgsConstructor
public class AnimeStatusController {
    @NonNull
    private final AnimeStatusService animeStatusService;
    @NonNull
    private final EpisodeStatusService episodeStatusService;
    @NonNull
    private final AnimeMapper animeMapper;

    @GetMapping
    @ApiOperation(value = "Find all Anime tracked by current user", nickname = "findAnimeStatuses")
    public PageDto<AnimeStatusDto> findAll(@AuthenticationPrincipal JwtUser user,
                                           @RequestParam("page") @Min(0) int page,
                                           @RequestParam("size") @Min(1) @Max(100) int size) {
        var pageRequest = PageRequest.of(page, size);
        var result = animeStatusService.findAll(user.getEmail(), pageRequest);

        return animeMapper.toAnimeStatusPageDto(result);
    }

    @GetMapping("{id}/episodes")
    @ApiOperation(value = "Find all Episode of Anime tracked by current user", nickname = "findEpisodeStatuses")
    public PageDto<EpisodeStatusDto> getEpisodes(@AuthenticationPrincipal JwtUser user,
                                                 @PathVariable("id") Long animeId,
                                                 @RequestParam("page") @Min(0) int page,
                                                 @RequestParam("size") @Min(1) @Max(100) int size,
                                                 @RequestParam(value = "type", required = false) EpisodeTypeDto type) {
        var pageRequest = PageRequest.of(page, size);
        var episodeType = animeMapper.toEpisodeType(type);
        var result = episodeStatusService.findByAnimeAndUser(animeId, user.getEmail(), episodeType, pageRequest);

        return animeMapper.toEpisodeStatusPageDto(result);
    }

    @PostMapping("{id}")
    @ApiOperation(value = "Update status of Anime tracked by current user", nickname = "updateAnimeStatus")
    public AnimeStatusDto updateStatus(@AuthenticationPrincipal JwtUser user,
                                       @PathVariable("id") @NotNull Long animeId,
                                       @RequestBody UpdateAnimeStatusDto updateAnimeStatusDto) {
        Consumer<AnimeStatus> updater = animeStatus -> animeMapper.updateAnimeStatus(updateAnimeStatusDto, animeStatus);

        var animeStatus = animeStatusService.updateAnimeStatus(
                user.getEmail(),
                animeId,
                updater
        );

        return animeMapper.toAnimeStatusDto(animeStatus);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Delete status of Anime tracked by current user", nickname = "deleteAnimeStatus")
    public void deleteStatus(@AuthenticationPrincipal JwtUser user,
                             @PathVariable("id") @NotNull Long animeId) {

        animeStatusService.deleteAnimeStatus(
                user.getEmail(),
                animeId
        );
    }

    @PostMapping("{id}/episodes/{eid}")
    @ApiOperation(value = "Update status of Anime Episode tracked by current user", nickname = "updateEpisodeStatus")
    public EpisodeStatusDto updateEpisodeStatus(@AuthenticationPrincipal JwtUser user,
                                                @PathVariable("id") @NotNull Long animeId,
                                                @PathVariable("eid") @NotNull Long episodeId,
                                                @RequestBody UpdateEpisodeStatusDto updateEpisodeStatusDto) {
        Consumer<EpisodeStatus> updater = episodeStatus -> animeMapper.updateEpisodeStatus(updateEpisodeStatusDto, episodeStatus);

        var episodeStatus = episodeStatusService.updateEpisodeStatus(
                animeId,
                episodeId,
                user.getEmail(),
                updater
        );

        return animeMapper.toEpisodeStatusDto(episodeStatus);
    }
}
