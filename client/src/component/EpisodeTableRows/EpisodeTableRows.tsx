import { TableCell, TableRow } from "@material-ui/core";
import React, { FC } from "react";
import Episode from "../../domain/Episode";
import { resolveEpisodeTitle } from "../../service";

interface OwnProps {
    content: Episode[];
}

const EpisodeTableRows: FC<OwnProps> = ({ content }) => (
    <>
        {
            content.map((episode: Episode, index: number) => (
                <TableRow key={index}>
                    <TableCell component="th" scope="row">{episode.id}</TableCell>
                    <TableCell align="center">{episode.number}</TableCell>
                    <TableCell align="center">{episode.type}</TableCell>
                    <TableCell align="center">{resolveEpisodeTitle(episode.titles)}</TableCell>
                </TableRow>
            ))
        }
    </>
);

export default EpisodeTableRows;
