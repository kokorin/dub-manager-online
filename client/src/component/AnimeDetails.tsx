import AnimeDetailsComponent from "./AnimeDetailsComponent";
import {Anime, AnimeType} from "../domain";
import React from "react";
import {getAnime} from "../api";

export interface AnimeDetailsProps {
    animeId: number
}

interface AnimeDetailsState {
    isLoading: boolean;
    anime: Anime;
}

export default class AnimeDetails extends React.Component<AnimeDetailsProps, AnimeDetailsState> {
    constructor(props: Readonly<AnimeDetailsProps>) {
        super(props);
        this.state = {
            isLoading: false,
            anime: {
                id: 0,
                type: AnimeType.TVSERIES,
                titles: []
            }
        };
    }

    private fetchAnime = async () => {
        this.setState({isLoading: true});

        const anime = await getAnime(this.props.animeId);
        this.setState({
            // TODO check if ...this.state is really needed
            ...this.state,
            isLoading: false,
            anime
        });
    }

    componentDidMount() {
        this.fetchAnime();
    }

    render() {
        return (
            <AnimeDetailsComponent anime={this.state.anime}/>
        );
    }
}