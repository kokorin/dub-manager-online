import { createApi } from "@reduxjs/toolkit/query/react";
import { fetchBaseQuery } from "@reduxjs/toolkit/query";
export const api = createApi({
    baseQuery: fetchBaseQuery({ baseUrl: "http://localhost:8080" }),
    tagTypes: [],
    endpoints: (build) => ({
        findAnime: build.query<FindAnimeApiResponse, FindAnimeApiArg>({
            query: (queryArg) => ({
                url: `/api/v1/anime`,
                params: {
                    page: queryArg.page,
                    size: queryArg.size,
                    title: queryArg.title,
                },
            }),
        }),
        getAnime: build.query<GetAnimeApiResponse, GetAnimeApiArg>({
            query: (queryArg) => ({ url: `/api/v1/anime/${queryArg.id}` }),
        }),
        findEpisodes: build.query<FindEpisodesApiResponse, FindEpisodesApiArg>({
            query: (queryArg) => ({
                url: `/api/v1/anime/${queryArg.id}/episodes`,
                params: { page: queryArg.page, size: queryArg.size },
            }),
        }),
        getConfiguration: build.query<GetConfigurationApiResponse, GetConfigurationApiArg>({
            query: () => ({ url: `/api/v1/conf` }),
        }),
        getCurrentUser: build.query<GetCurrentUserApiResponse, GetCurrentUserApiArg>({
            query: () => ({ url: `/api/v1/users/current` }),
        }),
        findAnimeStatuses: build.query<FindAnimeStatusesApiResponse, FindAnimeStatusesApiArg>({
            query: (queryArg) => ({
                url: `/api/v1/users/current/anime`,
                params: { page: queryArg.page, size: queryArg.size },
            }),
        }),
        updateAnimeStatus: build.mutation<UpdateAnimeStatusApiResponse, UpdateAnimeStatusApiArg>({
            query: (queryArg) => ({
                url: `/api/v1/users/current/anime/${queryArg.animeId}`,
                method: "POST",
                body: queryArg.updateAnimeStatusDto,
            }),
        }),
        findEpisodeStatuses: build.query<FindEpisodeStatusesApiResponse, FindEpisodeStatusesApiArg>({
            query: (queryArg) => ({
                url: `/api/v1/users/current/anime/${queryArg.id}/episodes`,
                params: { page: queryArg.page, size: queryArg.size },
            }),
        }),
    }),
});
export type FindAnimeApiResponse = /** status 200 OK */ PageDtoOfAnimeDto;
export type FindAnimeApiArg = {
    /** page */
    page: number;
    /** size */
    size: number;
    /** title */
    title?: string;
};
export type GetAnimeApiResponse = /** status 200 OK */ AnimeDto;
export type GetAnimeApiArg = {
    /** id */
    id: number;
};
export type FindEpisodesApiResponse = /** status 200 OK */ PageDtoOfEpisodeDto;
export type FindEpisodesApiArg = {
    /** id */
    id: number;
    /** page */
    page: number;
    /** size */
    size: number;
};
export type GetConfigurationApiResponse = /** status 200 OK */ ConfigurationDto;
export type GetConfigurationApiArg = {};
export type GetCurrentUserApiResponse = /** status 200 OK */ UserDto;
export type GetCurrentUserApiArg = {};
export type FindAnimeStatusesApiResponse = /** status 200 OK */ PageDtoOfAnimeStatusDto;
export type FindAnimeStatusesApiArg = {
    /** page */
    page: number;
    /** size */
    size: number;
};
export type UpdateAnimeStatusApiResponse /** status 200 OK */ = AnimeStatusDto | /** status 201 Created */ undefined;
export type UpdateAnimeStatusApiArg = {
    /** animeId */
    animeId: number;
    /** updateAnimeStatusDto */
    updateAnimeStatusDto: UpdateAnimeStatusDto;
};
export type FindEpisodeStatusesApiResponse = /** status 200 OK */ PageDtoOfEpisodeStatusDto;
export type FindEpisodeStatusesApiArg = {
    /** id */
    id: number;
    /** page */
    page: number;
    /** size */
    size: number;
};
export type AnimeTitleDto = {
    lang: string;
    text: string;
    type: "CARD" | "KANA" | "MAIN" | "OFFICIAL" | "SHORT" | "SYNONYM";
};
export type AnimeDto = {
    id: number;
    titles: AnimeTitleDto[];
    type: "DELETED" | "MOVIE" | "MUSIC_VIDEO" | "OTHER" | "OVA" | "TV_SERIES" | "TV_SPECIAL" | "UNKNOWN" | "WEB";
};
export type PageDtoOfAnimeDto = {
    content: AnimeDto[];
    number: number;
    numberOfElements: number;
    size: number;
    totalElements: number;
    totalPages: number;
};
export type EpisodeTitleDto = {
    lang: string;
    text: string;
};
export type EpisodeDto = {
    airDate: string;
    id: number;
    length: number;
    number: number;
    titles: EpisodeTitleDto[];
    type: "CREDIT" | "OTHER" | "PARODY" | "REGULAR" | "SPECIAL" | "TRAILER";
};
export type PageDtoOfEpisodeDto = {
    content: EpisodeDto[];
    number: number;
    numberOfElements: number;
    size: number;
    totalElements: number;
    totalPages: number;
};
export type ConfigurationDto = {
    googleOAuthClientId: string;
};
export type UserDto = {
    email?: string;
    id?: number;
};
export type AnimeStatusDto = {
    anime: AnimeDto;
    comment: string;
    completedRegularEpisodes: number;
    progress: "COMPLETED" | "IN_PROGRESS" | "NOT_STARTED";
    totalRegularEpisodes: number;
};
export type PageDtoOfAnimeStatusDto = {
    content: AnimeStatusDto[];
    number: number;
    numberOfElements: number;
    size: number;
    totalElements: number;
    totalPages: number;
};
export type UpdateAnimeStatusDto = {
    comment: string;
    progress: "COMPLETED" | "IN_PROGRESS" | "NOT_STARTED";
};
export type EpisodeStatusDto = {
    episode: EpisodeDto;
    progress: "COMPLETED" | "NOT_STARTED";
};
export type PageDtoOfEpisodeStatusDto = {
    content: EpisodeStatusDto[];
    number: number;
    numberOfElements: number;
    size: number;
    totalElements: number;
    totalPages: number;
};
export const {
    useFindAnimeQuery,
    useGetAnimeQuery,
    useFindEpisodesQuery,
    useGetConfigurationQuery,
    useGetCurrentUserQuery,
    useFindAnimeStatusesQuery,
    useUpdateAnimeStatusMutation,
    useFindEpisodeStatusesQuery,
} = api;
