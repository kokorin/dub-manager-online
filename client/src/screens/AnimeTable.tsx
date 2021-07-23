import React, { CSSProperties, FC, useState } from "react";
import { useFindAnimeQuery } from "../api";
import { DataGrid, GridColDef, GridSelectionModelChangeParams } from "@material-ui/data-grid";
import { resolveAnimeTitle } from "../service";
import { Anime } from "../domain";
import { Search } from "../components/Search";
import { CircularProgress, Modal } from "@material-ui/core";

const columns: GridColDef[] = [
    { field: "id", headerName: "ID", flex: 1 },
    { field: "type", headerName: "TYPE", flex: 3 },
    {
        field: "titles",
        headerName: "TITLE",
        flex: 10,
        valueGetter: (params) => resolveAnimeTitle((params.row as Anime).titles),
    },
];

interface OwnProps {
    onAnimeSelected: (animeIds: number[]) => void;
    style?: CSSProperties;
}

export const AnimeTable: FC<OwnProps> = (props) => {
    const [page, setPage] = useState(0);
    const [pageSize, setPageSize] = useState(10);
    const [filter, setFilter] = useState("");

    const { data, isLoading } = useFindAnimeQuery({ page, size: pageSize, title: filter });

    const handleSearchChange = (text: string) => {
        setPage(0);
        setFilter(text);
    };

    const handleSelectionModelChange = (params: GridSelectionModelChangeParams) => {
        props.onAnimeSelected(params.selectionModel as number[]);
    };

    return (
        <div className="anime_table" style={props.style}>
            <Modal open={isLoading}>
                <CircularProgress />
            </Modal>
            <Search label="Anime Title" text={filter} onChangeSearch={handleSearchChange} />
            <DataGrid
                rowsPerPageOptions={[5, 10, 25]}
                columns={columns}
                page={page}
                pageSize={pageSize}
                rows={data?.content || []}
                rowCount={data?.totalElements || 0}
                onPageChange={(params) => setPage(params.page)}
                onPageSizeChange={(params) => setPageSize(params.pageSize)}
                paginationMode="server"
                checkboxSelection={true}
                onSelectionModelChange={handleSelectionModelChange}
            />
        </div>
    );
};
