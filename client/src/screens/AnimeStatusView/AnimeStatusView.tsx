import React, { FC } from "react";
import AnimeStatusDetails from "./AnimeStatusDetails";
import { EpisodeStatusGrid } from "../../components/EpisodeStatus";

interface OwnProps {
    animeId: number;
}

const AnimeStatusView: FC<OwnProps> = (props) => {
    const { animeId } = props;

    return (
        <div className="status-view">
            <AnimeStatusDetails animeId={animeId} />
            <EpisodeStatusGrid animeId={animeId} />
        </div>
    );
};

export default AnimeStatusView;
