import { api as generatedApi } from "./generated";
import { isRejectedWithValue, Middleware, MiddlewareAPI } from "@reduxjs/toolkit";
import { notAuthorized } from "../auth";
import store from "../store";

export const api = generatedApi.enhanceEndpoints({
    addTagTypes: ["AnimeStatus", "EpisodeStatus"],
    endpoints: {
        findAnimeStatuses: {
            providesTags: (result, error, arg) => [
                ...(result?.content || []).map((animeStatus) => ({
                    type: "AnimeStatus" as const,
                    id: animeStatus.anime.id,
                })),
                {
                    type: "AnimeStatus",
                    id: "LIST",
                },
            ],
        },
        updateAnimeStatus: {
            invalidatesTags: (result, error, arg) => [{ type: "AnimeStatus", id: "LIST" }],
        },
        deleteAnimeStatus: {
            invalidatesTags: (result, error, arg) => [{ type: "AnimeStatus", id: "LIST" }],
        },
        findEpisodeStatuses: {
            providesTags: (result, error, arg) => [{ type: "EpisodeStatus", id: "LIST" }],
        },
        updateEpisodeStatus: {
            invalidatesTags: (result, error, arg) => [
                {
                    type: "EpisodeStatus",
                    id: "LIST",
                },
                {
                    type: "AnimeStatus",
                    id: result?.episode.animeId,
                },
            ],
        },
    },
});

export const authErrorHandler: Middleware = (api: MiddlewareAPI) => (next) => (action) => {
    if (isRejectedWithValue(action)) {
        if (401 === action.payload?.status) {
            store.dispatch(notAuthorized());
        }
    }
    return next(action);
};

export const {
    useFindAnimeQuery,
    useGetAnimeQuery,
    useFindEpisodesQuery,
    useGetOAuthClientsQuery,
    useGetCurrentUserQuery,
    useFindAnimeStatusesQuery,
    useFindEpisodeStatusesQuery,
    useUpdateAnimeStatusMutation,
    useDeleteAnimeStatusMutation,
    useUpdateEpisodeStatusMutation,
} = api;
