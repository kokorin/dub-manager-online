import React, { ChangeEvent, FC, useCallback } from "react";
import { FormControlLabel, Switch } from "@material-ui/core";
import { EpisodeStatus, EpisodeStatusProgress } from "../../domain";

interface OwnProps {
    episodeStatus: EpisodeStatus;
    onUpdateProgress: (episodeId: number, progress: EpisodeStatusProgress) => void;
}

const EpisodeStatusProgressControl: FC<OwnProps> = (props) => {
    const onCheckedChange = useCallback(
        (event: ChangeEvent<HTMLInputElement>) => {
            const checked = event.target.checked;
            props.onUpdateProgress(
                props.episodeStatus.episode.id,
                checked ? EpisodeStatusProgress.COMPLETED : EpisodeStatusProgress.NOT_STARTED,
            );
        },
        [props],
    );

    const switchControl = (
        <Switch
            color="secondary"
            checked={props.episodeStatus.progress === EpisodeStatusProgress.COMPLETED}
            onChange={onCheckedChange}
        />
    );

    return <FormControlLabel control={switchControl} label={props.episodeStatus.progress} />;
};

export default EpisodeStatusProgressControl;
