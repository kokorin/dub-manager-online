import { createSlice } from "@reduxjs/toolkit";
import { AppState } from "./store";

export interface Auth {
    isAuthenticated: boolean;
}

const initialState: Auth = {
    isAuthenticated: true,
};

const authSlice = createSlice({
    name: "auth",
    initialState,
    reducers: {
        notAuthorized: (state) => {
            state.isAuthenticated = false;
        },
    },
});

export const selectAuth: (state: AppState) => Auth = (state: AppState) => state.auth;

export const { notAuthorized } = authSlice.actions;

export default authSlice.reducer;
