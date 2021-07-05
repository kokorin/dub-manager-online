import { TableCell, TableRow } from "@material-ui/core";
import { EpisodeTableRows } from "./EpisodeTableRows";
import { TableWithSearch } from "./TableWithSearch";
import React, { FC, ReactNode, useState } from "react";
import { useFindEpisodesQuery } from "../api";

interface OwnProps {
    animeId: number;
}

const EpisodeTable: FC<OwnProps> = (props) => {
    const [page, setPage] = useState(0);
    const [size, setSize] = useState(10);
    const { data, isLoading } = useFindEpisodesQuery({ id: props.animeId, page, size });

    if (!data || isLoading) {
        return <div>Loading</div>;
    }

    const head: ReactNode = (
        <TableRow>
            <TableCell>ID</TableCell>
            <TableCell align="center">NUMBER</TableCell>
            <TableCell align="center">TYPE</TableCell>
            <TableCell align="center">TITLE</TableCell>
        </TableRow>
    );

    return (
        <TableWithSearch
            head={head}
            number={data.number}
            size={size}
            totalElements={data.totalElements}
            onChangePage={setPage}
            onChangeRowsPerPage={setSize}
        >
            <EpisodeTableRows content={data.content} />
        </TableWithSearch>
    );
};

export default EpisodeTable;
