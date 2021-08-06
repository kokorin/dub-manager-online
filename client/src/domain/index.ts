import { AnimeDto, AnimeStatusDto, AnimeTitleDto, EpisodeDto, EpisodeStatusDto, EpisodeTitleDto } from "../api";

export type Anime = AnimeDto;

export enum AnimeType {
    DELETED = "DELETED",
    MOVIE = "MOVIE",
    MUSIC_VIDEO = "MUSIC_VIDEO",
    OTHER = "OTHER",
    OVA = "OVA",
    TV_SERIES = "TV_SERIES",
    TV_SPECIAL = "TV_SPECIAL",
    UNKNOWN = "UNKNOWN",
    WEB = "WEB",
}

export type AnimeTitle = AnimeTitleDto;

export enum AnimeTitleType {
    CARD = "CARD",
    KANA = "KANA",
    MAIN = "MAIN",
    OFFICIAL = "OFFICIAL",
    SHORT = "SHORT",
    SYNONYM = "SYNONYM",
}

export type Episode = EpisodeDto;
export type EpisodeTitle = EpisodeTitleDto;

export type AnimeStatus = AnimeStatusDto;
export type EpisodeStatus = EpisodeStatusDto;

export enum EpisodeStatusProgress {
    COMPLETED = "COMPLETED",
    NOT_STARTED = "NOT_STARTED",
}

export * from "./Page";
