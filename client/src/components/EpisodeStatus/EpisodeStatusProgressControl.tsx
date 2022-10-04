import React, { ChangeEvent, FC, useCallback } from "react";
import { EpisodeStatus, EpisodeStatusProgress } from "../../domain";
import { UpdateEpisodeStatusDto, useUpdateEpisodeStatusMutation } from "../../api";
import { FormControlLabel, Switch } from "@mui/material";

interface OwnProps {
    episodeStatus: EpisodeStatus;
}

const EpisodeStatusProgressControl: FC<OwnProps> = (props) => {
    const [updateEpisodeStatus] = useUpdateEpisodeStatusMutation();
    const onCheckedChange = useCallback(
        (event: ChangeEvent<HTMLInputElement>) => {
            const update: UpdateEpisodeStatusDto = {
                progress: event.target.checked ? EpisodeStatusProgress.COMPLETED : EpisodeStatusProgress.NOT_STARTED,
            };
            const { animeId, id: episodeId } = props.episodeStatus.episode;
            updateEpisodeStatus({ id: animeId, eid: episodeId, updateEpisodeStatusDto: update });
        },
        [updateEpisodeStatus, props.episodeStatus],
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
