import axios from "axios";
import React from "react";
import Anime from "../domain/Anime";
import AnimeDetailsComponent from "./AnimeDetailsComponent";

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
                type: "",
                titles: []
            }
        };
    }

    private fetchAnime = async () => {
        this.setState({isLoading: true});

        const res = await axios.get(`/api/v1/anime/${this.props.animeId}`);
        this.setState({
            ...this.state,
            isLoading: false,
            anime: res.data
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