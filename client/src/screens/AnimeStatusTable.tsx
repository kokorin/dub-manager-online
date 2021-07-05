import React, { FC, useState } from "react";
import { TableCell, TableRow } from "@material-ui/core";
import { useFindAnimeStatusesQuery } from "../api";
import Loader from "../components/Loader";
import { resolveAnimeTitle } from "../service";
import { TableWithSearch } from "../components/TableWithSearch";
import { Link } from "react-router-dom";

const AnimeStatusTable: FC = () => {
    const [page, setPage] = useState(0);
    const [size, setSize] = useState(10);
    const { data, isLoading } = useFindAnimeStatusesQuery({ page, size });

    if (!data || isLoading) {
        return <Loader />;
    }

    const head = (
        <TableRow>
            <TableCell align="center">ID</TableCell>
            <TableCell align="center">Type</TableCell>
            <TableCell align="center">Title</TableCell>
            <TableCell align="center">Progress</TableCell>
            <TableCell align="center">Progress</TableCell>
        </TableRow>
    );

    const rows = data.content.map((animeStatus) => {
        const to = `/anime/${animeStatus.anime.id}`;
        return (
            <TableRow key={animeStatus.anime.id}>
                <TableCell>
                    <Link to={to}>{animeStatus.anime.id}</Link>
                </TableCell>
                <TableCell>{resolveAnimeTitle(animeStatus.anime.titles)}</TableCell>
                <TableCell>{animeStatus.anime.type}</TableCell>
                <TableCell>{animeStatus.progress}</TableCell>
                <TableCell>
                    {animeStatus.completedRegularEpisodes}/{animeStatus.totalRegularEpisodes}
                </TableCell>
            </TableRow>
        );
    });

    return (
        <TableWithSearch
            head={head}
            number={page}
            size={size}
            totalElements={data.totalElements}
            onChangePage={setPage}
            onChangeRowsPerPage={setSize}
        >
            {rows}
        </TableWithSearch>
    );
};

export default AnimeStatusTable;
