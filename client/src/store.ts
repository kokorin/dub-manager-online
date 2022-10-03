import { combineReducers, configureStore } from "@reduxjs/toolkit";
import { api, apiStats } from "./api";
import auth from "./auth";

const rootReducers = combineReducers({
    auth,
    apiStats,
    [api.reducerPath]: api.reducer,
});

const store = configureStore({
    reducer: rootReducers,
    middleware: (getDefaultMiddleware) => getDefaultMiddleware().concat(api.middleware),
    devTools: process.env.NODE_ENV !== "production",
});

export type AppState = ReturnType<typeof store.getState>;

export default store;
