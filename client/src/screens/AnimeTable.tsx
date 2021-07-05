import React, { FC, useState } from "react";
import { TableWithSearch } from "components/TableWithSearch";
import { useFindAnimeQuery } from "../api";
import { TableCell, TableRow } from "@material-ui/core";
import { Link } from "react-router-dom";
import { resolveAnimeTitle } from "../service";

export const AnimeTable: FC = () => {
    const [page, setPage] = useState(0);
    const [size, setSize] = useState(10);
    const [title, setTitle] = useState("");
    const { data, isLoading } = useFindAnimeQuery({ page, size, title });

    const animeTableHead = (
        <TableRow>
            <TableCell>ID</TableCell>
            <TableCell align="center">TYPE</TableCell>
            <TableCell align="center">TITLE</TableCell>
        </TableRow>
    );

    if (!data || isLoading) {
        return <div>Loading...</div>;
    }

    const animeTableRows = data.content.map((anime) => (
        <TableRow key={anime.id}>
            <TableCell component="th" scope="row">
                <Link to={`/anime/${anime.id}`}>{anime.id}</Link>
            </TableCell>
            <TableCell align="center">
                <Link to={`/anime/${anime.id}`}>{anime.type}</Link>
            </TableCell>
            <TableCell align="center">
                <Link to={`/anime/${anime.id}`}>{resolveAnimeTitle(anime.titles)}</Link>
            </TableCell>
        </TableRow>
    ));

    return (
        <TableWithSearch
            head={animeTableHead}
            number={page}
            size={size}
            totalElements={data.totalElements}
            onChangeSearch={setTitle}
            onChangePage={setPage}
            onChangeRowsPerPage={setSize}
        >
            {animeTableRows}
        </TableWithSearch>
    );
};
