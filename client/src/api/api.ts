import { api as generatedApi } from "./generated";
import { createAction, createReducer } from "@reduxjs/toolkit";
import { AppState } from "../store";

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

interface RejectedPayload {
    status: number;
}

export const queryPending = createAction("api/executeQuery/pending");
export const queryFulfilled = createAction("api/executeQuery/fulfilled");
export const queryRejected = createAction<RejectedPayload>("api/executeQuery/rejected");

export const mutationPending = createAction("api/executeMutation/pending");
export const mutationFulfilled = createAction("api/executeMutation/fulfilled");
export const mutationRejected = createAction<RejectedPayload>("api/executeMutation/rejected");

interface ApiStats {
    fetched: number;
    completed: number;
}

const initialApiStatus: ApiStats = {
    fetched: 0,
    completed: 0,
};

export const apiStats = createReducer(initialApiStatus, (builder) => {
    builder
        .addCase(queryPending, (state, action) => {
            state.fetched++;
        })
        .addCase(queryFulfilled, (state, action) => {
            state.completed++;
        })
        .addCase(queryRejected, (state, action) => {
            state.completed++;
        })
        .addCase(mutationPending, (state, action) => {
            state.fetched++;
        })
        .addCase(mutationFulfilled, (state, action) => {
            state.completed++;
        })
        .addCase(mutationRejected, (state, action) => {
            state.completed++;
        });
});

export const selectIsFetching: (_: AppState) => boolean = (state: AppState) =>
    state.apiStats.fetched > state.apiStats.completed;

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
