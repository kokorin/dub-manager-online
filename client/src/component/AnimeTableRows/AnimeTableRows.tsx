import {TableCell, TableRow} from "@material-ui/core";
import {resolveAnimeTitle} from "../../service";
import {Anime} from "../../domain";
import React, {FC} from "react";
import {Link} from "react-router-dom";

interface OwnProps {
    content: Anime[];
}

const AnimeTableRows: FC<OwnProps> = ({ content }) => (
    <>
        {
            content.map((anime: Anime, index: number) => (
                <TableRow key={index}>
                    <TableCell component="th" scope="row">
                        <Link to={`/anime/${anime.id}`}>
                            {anime.id}
                        </Link>
                    </TableCell>
                    <TableCell align="center">
                        <Link to={`/anime/${anime.id}`}>
                            {anime.type}
                        </Link>
                    </TableCell>
                    <TableCell align="center">
                        <Link to={`/anime/${anime.id}`}>
                            {resolveAnimeTitle(anime.titles)}
                        </Link>
                    </TableCell>
                </TableRow>
            ))
        }
    </>
);

export default AnimeTableRows;
