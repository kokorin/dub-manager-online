import { createAsyncThunk, createSlice } from "@reduxjs/toolkit";
import { ThunkConfig } from "store";
import { nonNegativeOrDefault } from "service/numberUtils";
import { getAnimeList } from "../../api";
import { Anime, Page } from "../../domain";
import { AnimeTableSate } from "./types";

const animeTableName = "animeTable";

export const loadAnimeList = createAsyncThunk<Page<Anime>, void, ThunkConfig>(
    `${animeTableName}/load`,
    async (_, { getState }) => {
        const { animeList } = getState();
        const { number, size, search } = animeList;
        return await getAnimeList(number, size, search);
    },
);

export const updateSearch = createAsyncThunk<number, string, ThunkConfig>(
    `${animeTableName}/updateSearch`,
    (search, { dispatch, getState }) => {
        return window.setTimeout(() => {
            const { delayTimer } = getState().animeList;
            if (delayTimer != null) {
                dispatch(loadAnimeList());
                window.clearTimeout(delayTimer);
            }
        }, 1_500);
    },
);

export const updatePage = createAsyncThunk<void, number, ThunkConfig>(
    `${animeTableName}/updatePage`,
    async (page, { dispatch }) => {
        dispatch(loadAnimeList());
    },
);

export const updateRowsPerPage = createAsyncThunk<void, number, ThunkConfig>(
    `${animeTableName}/updateRowsPerPage`,
    async (rowsPerPage, { dispatch }) => {
        dispatch(loadAnimeList());
    },
);

const initialState: AnimeTableSate = {
    isLoading: false,
    delayTimer: null,
    search: "",
    number: 0,
    size: 10,
    totalElements: 0,
    content: [],
};

const animeTableSlice = createSlice({
    name: animeTableName,
    initialState,
    reducers: {},
    extraReducers: (builder) => {
        builder.addCase(loadAnimeList.pending, (state) => {
            state.isLoading = true;
        });
        builder.addCase(loadAnimeList.fulfilled, (state, { payload }) => {
            state.isLoading = false;
            state.delayTimer = initialState.delayTimer;
            if (payload == null) return;
            const { number, size, totalElements, content } = payload;
            state.number = number;
            state.size = size;
            state.totalElements = totalElements;
            state.content = content;
        });
        builder.addCase(loadAnimeList.rejected, (state) => {
            state.isLoading = initialState.isLoading;
            state.delayTimer = initialState.delayTimer;
        });
        builder.addCase(updateSearch.pending, (state, { meta }) => {
            state.search = meta.arg;
            state.number = initialState.number;
        });
        builder.addCase(updateSearch.fulfilled, (state, { payload }) => {
            state.delayTimer = payload;
        });
        builder.addCase(updatePage.pending, (state, { meta }) => {
            state.number = nonNegativeOrDefault(meta.arg, state.number);
        });
        builder.addCase(updateRowsPerPage.pending, (state, { meta }) => {
            state.size = meta.arg;
            state.number = initialState.number;
        });
    },
});

export default animeTableSlice.reducer;
