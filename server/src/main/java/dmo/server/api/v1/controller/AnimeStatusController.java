package dmo.server.api.v1.controller;

import dmo.server.api.v1.dto.AnimeStatusDto;
import dmo.server.api.v1.dto.PageDto;
import dmo.server.api.v1.dto.UpdateAnimeStatusDto;
import dmo.server.api.v1.mapper.AnimeMapper;
import dmo.server.domain.AnimeStatus;
import dmo.server.security.DubUserDetails;
import dmo.server.service.AnimeStatusService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.function.Consumer;

@RestController
@RequestMapping("api/v1/anime_status")
@Validated
@RequiredArgsConstructor
public class AnimeStatusController {
    @NonNull
    private final AnimeStatusService animeStatusService;
    @NonNull
    private final AnimeMapper animeMapper;

    @GetMapping
    public PageDto<AnimeStatusDto> findAll(@AuthenticationPrincipal DubUserDetails userDetails,
                                           @RequestParam("page") @Min(0) int page,
                                           @RequestParam("size") @Min(1) @Max(100) int size) {
        var pageRequest = PageRequest.of(page, size);
        var result = animeStatusService.findAll(userDetails.getId(), pageRequest);

        return animeMapper.toAnimeStatusPageDto(result);
    }

    @PostMapping("{animeId}")
    public AnimeStatusDto updateStatus(@AuthenticationPrincipal DubUserDetails userDetails,
                                       @PathVariable("animeId") @NotNull Long animeId,
                                       @RequestBody UpdateAnimeStatusDto updateAnimeStatusDto) {
        Consumer<AnimeStatus> updater = animeStatus -> animeMapper.updateAnimeStatus(updateAnimeStatusDto, animeStatus);

        var animeStatus = animeStatusService.updateAnimeStatus(
                userDetails.getId(),
                animeId,
                updater
        );

        return animeMapper.toAnimeStatusDto(animeStatus);
    }
}
