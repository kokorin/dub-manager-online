import { resolveAnimeTitle } from "../service";
import { Anime } from "../domain";
import React, { FC } from "react";

export interface OwnProps {
    anime: Anime;
}

const AnimeDetailsComponent: FC<OwnProps> = (props) => {
    const anime = props.anime;

    return (
        <div>
            <div>ID: {anime.id}</div>
            <div>TYPE: {anime.type}</div>
            <div>TITLE: {resolveAnimeTitle(anime.titles)}</div>
        </div>
    );
};

export default AnimeDetailsComponent;
