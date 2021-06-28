import { combineReducers, configureStore } from "@reduxjs/toolkit";
import userReducer from "screens/Login/UserSlice";

const rootReducers = combineReducers({
    user: userReducer,
});

const store = configureStore({
    reducer: rootReducers,
    devTools: process.env.NODE_ENV !== "production",
});

export type StateType = ReturnType<typeof store.getState>;

export default store;
