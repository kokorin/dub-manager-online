import AnimeDetailsComponent from "./AnimeDetailsComponent";
import React, { FC } from "react";
import { useGetAnimeQuery } from "../api";
import Loader from "./Loader";

interface OwnProps {
    animeId: number;
}

const AnimeDetails: FC<OwnProps> = (props) => {
    const { data: anime, isLoading } = useGetAnimeQuery({ id: props.animeId });

    if (!anime || isLoading) {
        return <Loader />;
    }

    return <AnimeDetailsComponent anime={anime} />;
};

export default AnimeDetails;
