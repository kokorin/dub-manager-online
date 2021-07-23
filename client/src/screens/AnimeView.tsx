import React, { FC, useState } from "react";
import { resolveAnimeTitle, resolveEpisodeTitle } from "../service";
import { useFindEpisodesQuery, useGetAnimeQuery } from "../api";
import Loader from "../components/Loader";
import { Episode } from "../domain";
import { TableCell, TableRow } from "@material-ui/core";
import { TableWithSearch } from "../components/TableWithSearch";

interface OwnProps {
    animeId: number;
}

const AnimeView: FC<OwnProps> = (props) => {
    const { data: anime, isLoading: isAnimeLoading } = useGetAnimeQuery({ id: props.animeId });
    const [page, setPage] = useState(0);
    const [size, setSize] = useState(10);
    const { data: episodePage, isLoading: areEpisodesLoading } = useFindEpisodesQuery({
        id: props.animeId,
        page,
        size,
    });

    if (!anime || isAnimeLoading || !episodePage || areEpisodesLoading) {
        return <Loader />;
    }

    const head = (
        <TableRow>
            <TableCell>ID</TableCell>
            <TableCell align="center">NUMBER</TableCell>
            <TableCell align="center">TYPE</TableCell>
            <TableCell align="center">TITLE</TableCell>
        </TableRow>
    );

    const rows = episodePage.content.map((episode: Episode, index: number) => (
        <TableRow key={index}>
            <TableCell component="th" scope="row">
                {episode.id}
            </TableCell>
            <TableCell align="center">{episode.number}</TableCell>
            <TableCell align="center">{episode.type}</TableCell>
            <TableCell align="center">{resolveEpisodeTitle(episode.titles)}</TableCell>
        </TableRow>
    ));

    return (
        <>
            <div>
                <div>ID: {anime.id}</div>
                <div>TYPE: {anime.type}</div>
                <div>TITLE: {resolveAnimeTitle(anime.titles)}</div>
            </div>
            <TableWithSearch
                head={head}
                number={episodePage.number}
                size={size}
                totalElements={episodePage.totalElements}
                onChangePage={setPage}
                onChangeRowsPerPage={setSize}
            >
                {rows}
            </TableWithSearch>
        </>
    );
};

export default AnimeView;
