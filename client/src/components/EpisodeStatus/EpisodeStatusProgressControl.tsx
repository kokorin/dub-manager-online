import React, { FC } from "react";
import { FormControlLabel, Switch } from "@material-ui/core";
import { EpisodeStatus, EpisodeStatusProgress } from "../../domain";

interface OwnProps {
    episodeStatus: EpisodeStatus;
    onUpdateProgress: (episodeId: number, progress: EpisodeStatusProgress) => void;
}

const EpisodeStatusProgressControl: FC<OwnProps> = (props) => {
    const onCheckedChange = (checked: boolean) => {
        props.onUpdateProgress(
            props.episodeStatus.episode.id,
            checked ? EpisodeStatusProgress.COMPLETED : EpisodeStatusProgress.NOT_STARTED,
        );
    };

    const switchControl = (
        <Switch
            color="secondary"
            checked={props.episodeStatus.progress === EpisodeStatusProgress.COMPLETED}
            onChange={(event) => onCheckedChange(event.target.checked)}
        />
    );

    return <FormControlLabel control={switchControl} label={props.episodeStatus.progress} />;
};

export default EpisodeStatusProgressControl;
