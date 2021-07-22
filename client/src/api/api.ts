import { api as generatedApi } from "./generated";
import { isRejectedWithValue, Middleware, MiddlewareAPI } from "@reduxjs/toolkit";
import { notAuthorized } from "../auth";
import store from "../store";

export const api = generatedApi.enhanceEndpoints({
    addTagTypes: ["AnimeStatus"],
    endpoints: {
        findAnimeStatuses: {
            providesTags: (result, error, arg) => [{ type: "AnimeStatus", id: "LIST" }],
        },
        updateAnimeStatus: {
            invalidatesTags: (result, error, arg) => [{ type: "AnimeStatus", id: "LIST" }],
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
    useUpdateAnimeStatusMutation,
    useFindEpisodeStatusesQuery,
} = api;
