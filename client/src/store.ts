import { combineReducers, configureStore, Dispatch } from "@reduxjs/toolkit";
import userReducer from "screens/Login/userSlice";

const rootReducers = combineReducers({
    user: userReducer,
});

const store = configureStore({
    reducer: rootReducers,
    devTools: process.env.NODE_ENV !== "production",
});

export type StateType = ReturnType<typeof store.getState>;

export interface ThunkConfig {
    state: StateType;
    // eslint-disable-next-line
    dispatch: Dispatch<any>;
}

export default store;
