import axios from "axios";
import React from "react";
import Anime from "../domain/Anime";
import AnimeDetailsComponent from "./AnimeDetailsComponent";

export interface AnimeDetailsProps {
    animeId?:number
}

interface AnimeDetailsState {
    isLoading: boolean;
    anime:Anime;
}

export default class AnimeDetails extends React.Component<AnimeDetailsProps, AnimeDetailsState> {

    private fetchAnime = () => {
        this.setState({isLoading: true});

        let self = this;
        axios.get(`/api/v1/anime/${this.props.animeId}`).then(
            res => {
                self.setState({
                    ...this.state,
                    isLoading: false,
                    anime: res.data
                });
            }
        )
    }

    componentDidMount() {
        this.fetchAnime();
    }


    render() {
        return (<AnimeDetailsComponent anime={this.state?.anime} />);
    }
}