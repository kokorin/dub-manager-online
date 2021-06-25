import {
    AnimeDto,
    AnimeDtoTypeEnum,
    AnimeTitleDto,
    AnimeTitleDtoTypeEnum,
    ConfigurationDto,
    EpisodeDto,
    EpisodeDtoTypeEnum,
    EpisodeTitleDto,
} from "../api";

export type Anime = AnimeDto;
export const AnimeType = AnimeDtoTypeEnum;
export type AnimeTitle = AnimeTitleDto;
export const AnimeTitleType = AnimeTitleDtoTypeEnum;

export type Episode = EpisodeDto;
export const EpisodeType = EpisodeDtoTypeEnum;
export type EpisodeTitle = EpisodeTitleDto;

export type Config = ConfigurationDto;

export * from "./Page";
