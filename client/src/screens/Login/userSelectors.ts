import { createSelector } from "@reduxjs/toolkit";
import { StateType } from "store";
import { User } from "./types";

export const selectClientId = createSelector<StateType, User, string | undefined>(
    (state) => state.user,
    (user) => user.clientId,
);

export const selectIsAuthenticated = createSelector<StateType, User, boolean>(
    (state) => state.user,
    (user) => user.tokenId != null,
);
