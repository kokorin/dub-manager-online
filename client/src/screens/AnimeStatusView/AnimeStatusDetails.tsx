import React, { FC } from "react";
import { FormControl, FormGroup, Input, InputLabel, MenuItem, Select } from "@mui/material";
import { resolveAnimeTitle } from "../../service";
import { useFindAnimeStatusQuery } from "../../api";

interface OwnProps {
    animeId: number;
}

const AnimeStatusDetails: FC<OwnProps> = (props) => {
    const { data: animeStatus } = useFindAnimeStatusQuery({ id: props.animeId });
    if (!animeStatus) {
        return <div />;
    }

    const anime = animeStatus.anime;

    return (
        <FormGroup className="status-details">
            <FormControl>
                <InputLabel htmlFor="titleInput">Title</InputLabel>
                <Input id="titleInput" contentEditable={false} value={resolveAnimeTitle(anime.titles)} />
            </FormControl>
            <FormControl>
                <InputLabel htmlFor="progressInput">Progress</InputLabel>
                <Select id="progressInput" contentEditable={false} value={animeStatus.progress}>
                    <MenuItem value="NOT_STARTED">NOT_STARTED</MenuItem>
                    <MenuItem value="IN_PROGRESS">IN_PROGRESS</MenuItem>
                    <MenuItem value="COMPLETED">COMPLETED</MenuItem>
                </Select>
            </FormControl>
            <FormControl>
                <InputLabel htmlFor="episodeProgressInput">Episodes</InputLabel>
                <Input
                    id="episodeProgressInput"
                    contentEditable={false}
                    value={`${animeStatus.regularEpisodeCompleteCount}/${animeStatus.regularEpisodeTotalCount}`}
                />
            </FormControl>
            <FormControl>
                <InputLabel htmlFor="commentInput">Comment</InputLabel>
                <Input id="commentInput" contentEditable={true} value={animeStatus.comment} />
            </FormControl>
        </FormGroup>
    );
};

export default AnimeStatusDetails;
