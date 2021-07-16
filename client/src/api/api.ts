import { api as generatedApi } from "./generated";
import { isRejectedWithValue, Middleware, MiddlewareAPI } from "@reduxjs/toolkit";
import { notAuthorized } from "../auth";
import store from "../store";

export const api = generatedApi.enhanceEndpoints({
    endpoints: {},
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
