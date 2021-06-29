import { TableCell, TableRow } from "@material-ui/core";
import { resolveEpisodeTitle } from "../../service";
import { Episode } from "../../domain";
import { FC } from "react";
import React from "react";

interface OwnProps {
    content: Episode[];
}

const EpisodeTableRows: FC<OwnProps> = ({ content }) => (
    <>
        {content.map((episode: Episode, index: number) => (
            <TableRow key={index}>
                <TableCell component="th" scope="row">
                    {episode.id}
                </TableCell>
                <TableCell align="center">{episode.number}</TableCell>
                <TableCell align="center">{episode.type}</TableCell>
                <TableCell align="center">{resolveEpisodeTitle(episode.titles)}</TableCell>
            </TableRow>
        ))}
    </>
);

export default EpisodeTableRows;
