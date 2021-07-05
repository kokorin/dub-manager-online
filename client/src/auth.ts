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
    reducers: {},
});

export const selectAuth: (state: AppState) => Auth = (state: AppState) => state.auth;

export default authSlice;
