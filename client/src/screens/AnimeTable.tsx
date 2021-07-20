import React, { FC, useState } from "react";
import { useFindAnimeQuery } from "../api";
import { DataGrid, GridColDef, GridRowId } from "@material-ui/data-grid";
import Loader from "../components/Loader";
import { resolveAnimeTitle } from "../service";
import { Anime, AnimeTitle } from "../domain";
import { Search } from "../components/Search";
import { Button, Paper } from "@material-ui/core";

const columns: GridColDef[] = [
    { field: "id", headerName: "ID", width: 100 },
    { field: "type", headerName: "TYPE", width: 200 },
    {
        field: "titles",
        headerName: "TITLE",
        valueGetter: (params) => resolveAnimeTitle(params.row.titles as AnimeTitle[]),
    },
];

export const AnimeTable: FC = () => {
    const [page, setPage] = useState(0);
    const [pageSize, setPageSize] = useState(10);
    const [filter, setFilter] = useState("");
    const [selectedRows, setSelectedRows] = useState([] as GridRowId[]);

    const handleSearchChange = (text: string) => {
        setPage(0);
        setFilter(text);
    };

    const { data, isLoading } = useFindAnimeQuery({ page, size: pageSize, title: filter });

    if (!data || isLoading) {
        return <Loader />;
    }

    console.log(`Selected ${JSON.stringify(selectedRows)}`);

    return (
        <Paper>
            <Search label="Anime Title" text={filter} onChangeSearch={handleSearchChange} />

            <DataGrid
                autoHeight={true}
                rowsPerPageOptions={[5, 10, 25]}
                columns={columns}
                page={page}
                pageSize={pageSize}
                rows={data.content}
                rowCount={data.totalElements}
                onPageChange={(params) => setPage(params.page)}
                onPageSizeChange={(params) => setPageSize(params.pageSize)}
                paginationMode="server"
                checkboxSelection={true}
                onSelectionModelChange={(params) => setSelectedRows(params.selectionModel)}
            />
            <Button onClick={(event) => console.log(`${JSON.stringify(event)}`)}>SELECT</Button>
        </Paper>
    );
};
