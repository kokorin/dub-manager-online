import React, { FC, useState } from "react";
import { useFindAnimeStatusesQuery } from "../api";
import { DataGrid, GridColDef, GridRowIdGetter } from "@material-ui/data-grid";
import { Search } from "../components/Search";
import { CircularProgress, Modal } from "@material-ui/core";
import { AnimeStatus } from "../domain";
import { resolveAnimeTitle } from "../service";

const getRowId: GridRowIdGetter = (data) => (data as AnimeStatus).anime.id;
const columns: GridColDef[] = [
    {
        field: "anime.id",
        headerName: "ID",
        flex: 1,
        valueGetter: (params) => getRowId(params.row),
    },
    {
        field: "anime.titles",
        headerName: "TITLE",
        flex: 10,
        valueGetter: (params) => resolveAnimeTitle((params.row as AnimeStatus).anime.titles),
    },
    {
        field: "progress",
        flex: 5,
    },
    {
        field: "comment",
        flex: 5,
    },
];

const AnimeStatusTable: FC = () => {
    const [page, setPage] = useState(0);
    const [pageSize, setPageSize] = useState(10);
    const [filter, setFilter] = useState("");
    const { data, isLoading } = useFindAnimeStatusesQuery({ page, size: pageSize });

    return (
        <div className="anime_status_table" style={{ height: "100%", width: "100%" }}>
            <Modal open={isLoading}>
                <CircularProgress />
            </Modal>
            <Search text={filter} onChangeSearch={setFilter} />

            <DataGrid
                columns={columns}
                getRowId={getRowId}
                page={page}
                pageSize={pageSize}
                rows={data?.content || []}
                rowCount={data?.totalElements || 0}
                onPageChange={(params) => setPage(params.page)}
                onPageSizeChange={(params) => setPageSize(params.pageSize)}
                paginationMode="server"
                pagination={undefined}
            />
        </div>
    );
};

export default AnimeStatusTable;
