import { createAsyncThunk, createSlice } from "@reduxjs/toolkit";
import { ThunkConfig } from "store";
import { loginUser, Token } from "service/auth";
import { User } from "./types";
import { getConfig } from "../../api";
import { Config } from "../../domain";

const userSliceName = "user";

export const fetchConfig = createAsyncThunk<Config, void, ThunkConfig>(`${userSliceName}/login`, () => getConfig(), {
    condition: (_, { getState }) => {
        const { user } = getState();
        if (user.clientId) return false;
    },
});

export const authenticateUser = createAsyncThunk<Token, string, ThunkConfig>(
    `${userSliceName}/authenticate`,
    async (tokenId) => {
        return await loginUser(tokenId);
    },
);

const initialState: User = {
    clientId: undefined,
    tokenId: undefined,
};

const userSlice = createSlice({
    name: userSliceName,
    initialState,
    reducers: {},
    extraReducers: (builder) => {
        builder.addCase(fetchConfig.fulfilled, (state, { payload }) => {
            state.clientId = payload.googleOAuthClientId;
        });
        builder.addCase(fetchConfig.rejected, (state) => {
            state.clientId = "823210037861-f8ohubjp05esj8l3utdvssga7jajrrfb.apps.googleusercontent.com";
        });
        builder.addCase(authenticateUser.fulfilled, (state, { payload }) => {
            state.tokenId = payload.access_token;
        });
        builder.addCase(authenticateUser.rejected, (state, { meta }) => {
            state.tokenId = meta.arg;
        });
    },
});

export default userSlice.reducer;
