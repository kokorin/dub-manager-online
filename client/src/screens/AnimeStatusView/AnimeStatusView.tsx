import React, { FC } from "react";
import AnimeStatusDetails from "./AnimeStatusDetails";
import { EpisodeStatusGrid } from "../../components/EpisodeStatus";
import { CommonProps } from "@mui/material/OverridableComponent";

interface OwnProps extends CommonProps {
    animeId: number;
}

const AnimeStatusView: FC<OwnProps> = (props) => {
    const { animeId } = props;

    return (
        <div className={`flex-column ${props.className}`} style={props.style}>
            <AnimeStatusDetails animeId={animeId} />
            <EpisodeStatusGrid className="flex-grow" animeId={animeId} />
        </div>
    );
};

export default AnimeStatusView;
