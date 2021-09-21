import React, { FC, useCallback, useMemo, useState } from "react";
import { DataGrid, GridCellParams, GridColDef, GridFilterModel, GridRowIdGetter } from "@mui/x-data-grid";
import { useFindEpisodeStatusesQuery } from "../../api";
import { EpisodeStatus } from "../../domain";
import EpisodeStatusProgressControl from "./EpisodeStatusProgressControl";
import { resolveEpisodeTitle } from "../../service";

const getRowId: GridRowIdGetter = (data) => (data as EpisodeStatus).episode.id;

const createColumns: () => GridColDef[] = () => [
    {
        field: "id",
        headerName: "ID",
        sortable: false,
        filterable: false,
        flex: 1,
        valueGetter: (params) => getRowId(params.row),
    },
    {
        field: "progress",
        headerName: "Progress",
        sortable: false,
        filterable: false,
        flex: 2,
        renderCell: function EpisodeStatusProgressCell(params: GridCellParams) {
            return <EpisodeStatusProgressControl episodeStatus={params.row as EpisodeStatus} />;
        },
    },
    {
        field: "episode.number",
        headerName: "Episode",
        sortable: false,
        filterable: false,
        flex: 2,
        valueGetter: (params) => (params.row as EpisodeStatus).episode.number,
    },
    {
        field: "episode.airDate",
        headerName: "Date",
        sortable: false,
        filterable: false,
        flex: 2,
        valueGetter: (params) => (params.row as EpisodeStatus).episode.airDate,
    },
    {
        field: "episode.type",
        headerName: "Type",
        sortable: false,
        filterable: true,
        flex: 3,
        valueGetter: (params) => (params.row as EpisodeStatus).episode.type,
    },
    {
        field: "episode.titles",
        headerName: "Title",
        sortable: false,
        filterable: false,
        flex: 10,
        valueGetter: (params) => resolveEpisodeTitle((params.row as EpisodeStatus).episode.titles),
    },
];

interface OwnProps {
    animeId: number;
}

const EpisodeStatusGrid: FC<OwnProps> = (props) => {
    const { animeId } = props;

    const columns = useMemo(() => createColumns(), []);

    const [page, setPage] = useState(0);
    const [pageSize, setPageSize] = useState(10);
    const [episodeType, setEpisodeType] = useState("REGULAR" as const);
    const onFilterModelChange = useCallback(
        (model: GridFilterModel) => setEpisodeType(model.items[0].value),
        [setEpisodeType],
    );
    const { data, isFetching } = useFindEpisodeStatusesQuery({
        id: animeId,
        page,
        size: pageSize,
        type: episodeType,
    });

    return (
        <DataGrid
            className="episode-table"
            columns={columns}
            getRowId={getRowId}
            page={page}
            pageSize={pageSize}
            rows={data?.content || []}
            rowCount={data?.totalElements || 0}
            onPageChange={setPage}
            onPageSizeChange={setPageSize}
            paginationMode="server"
            checkboxSelection={false}
            disableSelectionOnClick={true}
            filterMode="server"
            filterModel={{ items: [{ columnField: "episode.type", operatorValue: "equals", value: "REGULAR" }] }}
            onFilterModelChange={onFilterModelChange}
            loading={isFetching}
        />
    );
};

export default EpisodeStatusGrid;
