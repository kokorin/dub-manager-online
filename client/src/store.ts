import { combineReducers, configureStore } from "@reduxjs/toolkit";
import { api } from "./api";
import authSlice from "./auth";

const rootReducers = combineReducers({
    auth: authSlice.reducer,
    [api.reducerPath]: api.reducer,
});

const store = configureStore({
    reducer: rootReducers,
    middleware: (getDefaultMiddleware) => getDefaultMiddleware().concat(api.middleware),
    devTools: process.env.NODE_ENV !== "production",
});

export type AppState = ReturnType<typeof store.getState>;
export type AppDispatch = typeof store.dispatch;

export default store;
