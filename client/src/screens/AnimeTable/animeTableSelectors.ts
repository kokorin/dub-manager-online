import { createSelector } from "@reduxjs/toolkit";
import { StateType } from "../../store";
import { AnimeTableSate } from "./types";
import { Anime } from "../../domain";

export const selectNumber = createSelector<StateType, AnimeTableSate, number>(
    (state) => state.animeList,
    (animeList) => animeList.number,
);

export const selectSize = createSelector<StateType, AnimeTableSate, number>(
    (state) => state.animeList,
    (animeList) => animeList.size,
);

export const selectTotalElements = createSelector<StateType, AnimeTableSate, number>(
    (state) => state.animeList,
    (animeList) => animeList.totalElements,
);

export const selectAnimeList = createSelector<StateType, AnimeTableSate, Anime[]>(
    (state) => state.animeList,
    (animeList) => animeList.content,
);
