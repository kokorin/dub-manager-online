import { createReducer } from "@reduxjs/toolkit";
import { AppState } from "./store";
import { queryRejected } from "./api";

export interface Auth {
    isAuthenticated: boolean;
}

const initialState: Auth = {
    isAuthenticated: true,
};

const auth = createReducer(initialState, (builder) => {
    builder.addCase(queryRejected, (state, action) => {
        if (action.payload?.status === 401) {
            state.isAuthenticated = false;
        }
    });
});

export const selectAuth: (_: AppState) => Auth = (state: AppState) => state.auth;

export default auth;
