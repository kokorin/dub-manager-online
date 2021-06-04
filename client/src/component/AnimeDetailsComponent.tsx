import {resolveAnimeTitle} from "../service";
import {Anime} from "../domain";
import React from "react";

export interface AnimeDetailsComponentProps {
    anime: Anime;
}

export default class AnimeDetailsComponent extends React.Component<AnimeDetailsComponentProps, any> {

    render() {
        const anime = this.props.anime;

        return (
            <div>
                <div>ID: {anime.id}</div>
                <div>TYPE: {anime.type}</div>
                <div>TITLE: {resolveAnimeTitle(anime.titles)}</div>
            </div>
        );
    }
}
