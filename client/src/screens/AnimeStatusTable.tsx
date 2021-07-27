import React, { CSSProperties, FC, useState } from "react";
import { useFindAnimeStatusesQuery } from "../api";
import { DataGrid, GridColDef, GridRowId, GridRowIdGetter } from "@material-ui/data-grid";
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
        field: "episodes",
        flex: 2,
        valueGetter: (params) => {
            const status = params.row as AnimeStatus;
            return `${status.completedRegularEpisodes}/${status.totalRegularEpisodes}`;
        },
    },
    {
        field: "comment",
        flex: 5,
    },
];

interface OwnProps {
    onAnimeStatusSelected: (animeIds: number[]) => void;
    style?: CSSProperties;
}

const AnimeStatusTable: FC<OwnProps> = (props) => {
    const [page, setPage] = useState(0);
    const [pageSize, setPageSize] = useState(10);
    const [filter, setFilter] = useState("");
    const { data, isLoading } = useFindAnimeStatusesQuery({ page, size: pageSize });

    const handleSelectionModelChange = (selectionModel: GridRowId[]) => {
        props.onAnimeStatusSelected(selectionModel as number[]);
    };

    return (
        <div className="anime_status_table" style={props.style}>
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
                checkboxSelection={true}
                onSelectionModelChange={handleSelectionModelChange}
            />
        </div>
    );
};

export default AnimeStatusTable;
