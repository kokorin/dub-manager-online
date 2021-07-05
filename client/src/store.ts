import { combineReducers, configureStore } from "@reduxjs/toolkit";
import { api, authErrorHandler } from "./api";
import auth from "./auth";

const rootReducers = combineReducers({
    auth: auth,
    [api.reducerPath]: api.reducer,
});

const store = configureStore({
    reducer: rootReducers,
    middleware: (getDefaultMiddleware) => getDefaultMiddleware().concat(api.middleware, authErrorHandler),
    devTools: process.env.NODE_ENV !== "production",
});

export type AppState = ReturnType<typeof store.getState>;

export default store;
