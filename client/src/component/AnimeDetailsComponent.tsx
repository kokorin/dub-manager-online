import React from "react";
import Anime from "../domain/Anime";

export interface AnimeDetailsComponentProps {
    anime: Anime;
}

export default class AnimeDetailsComponent extends React.Component<AnimeDetailsComponentProps, any> {

    render() {
        return (
            <div>
                <div>ID: {this.props.anime.id}</div>
                <div>TYPE: {this.props.anime.type}</div>
                <div>TITLE: {this.props.anime.titles[0]?.text}</div>
            </div>
        );
    }

}
