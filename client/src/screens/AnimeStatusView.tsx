import React, { CSSProperties, FC, useState } from "react";
import { DataGrid, GridColDef, GridRowIdGetter } from "@material-ui/data-grid";
import { useFindEpisodeStatusesQuery } from "../api";
import { CircularProgress, Modal } from "@material-ui/core";
import { EpisodeStatus } from "../domain";
import { resolveEpisodeTitle } from "../service";

interface OwnProps {
    animeId: number;
    style?: CSSProperties;
}

const getRowId: GridRowIdGetter = (data) => (data as EpisodeStatus).episode.id;

const columns: GridColDef[] = [
    {
        field: "id",
        flex: 1,
        valueGetter: (params) => getRowId(params.row),
    },
    {
        field: "progress",
        flex: 5,
    },
    {
        field: "episode.number",
        flex: 2,
        valueGetter: (params) => (params.row as EpisodeStatus).episode.number,
    },
    {
        field: "episode.type",
        flex: 3,
        valueGetter: (params) => (params.row as EpisodeStatus).episode.type,
    },
    {
        field: "episode.titles",
        flex: 10,
        valueGetter: (params) => resolveEpisodeTitle((params.row as EpisodeStatus).episode.titles),
    },
];

const AnimeStatusView: FC<OwnProps> = (props) => {
    const [page, setPage] = useState(0);
    const [pageSize, setPageSize] = useState(10);
    const { data, isLoading } = useFindEpisodeStatusesQuery({ id: props.animeId, page, size: pageSize });

    return (
        <div style={props.style}>
            <Modal open={isLoading}>
                <CircularProgress />
            </Modal>
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
                sortingMode="server"
                checkboxSelection={true}
            />
        </div>
    );
};

export default AnimeStatusView;
