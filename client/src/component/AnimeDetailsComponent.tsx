import React from "react";
import Anime from "../domain/Anime";
import AnimeTitleLabel from "./AnimeTitleLabel";

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
                <div>TITLE: <AnimeTitleLabel animeTitles={anime.titles}/></div>
            </div>
        );
    }

}
