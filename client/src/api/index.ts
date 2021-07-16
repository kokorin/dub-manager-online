import { api } from "./api";

export * from "./api";
export * from "./generated";

export { api } from "./api";

export const {
    useFindAnimeQuery,
    useGetAnimeQuery,
    useFindEpisodesQuery,
    useGetOAuthClientsQuery,
    useGetCurrentUserQuery,
    useFindAnimeStatusesQuery,
    useUpdateAnimeStatusMutation,
    useFindEpisodeStatusesQuery,
} = api;
