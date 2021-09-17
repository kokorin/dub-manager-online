import React, { FC, useCallback, useMemo, useState } from "react";
import { DataGrid, GridCellParams, GridColDef, GridFilterModel, GridRowIdGetter } from "@mui/x-data-grid";
import { useFindEpisodeStatusesQuery, useUpdateEpisodeStatusMutation } from "../api";
import { FormControl, FormGroup, Input, InputLabel, MenuItem, Select } from "@material-ui/core";
import { AnimeStatus, EpisodeStatus, EpisodeStatusProgress } from "../domain";
import { resolveAnimeTitle, resolveEpisodeTitle } from "../service";
import EpisodeStatusProgressControl from "../components/EpisodeStatus/EpisodeStatusProgressControl";

interface OwnProps {
    animeStatus: AnimeStatus;
}

const getRowId: GridRowIdGetter = (data) => (data as EpisodeStatus).episode.id;

type ColumnsProvider = (onUpdateProgress: (episodeId: number, progress: EpisodeStatusProgress) => void) => GridColDef[];

const createColumns: ColumnsProvider = (
    onUpdateProgress: (episodeId: number, progress: EpisodeStatusProgress) => void,
) => [
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
        // see https://github.com/mui-org/material-ui-x/issues/951
        // eslint-disable-next-line react/display-name
        renderCell: (params: GridCellParams) => (
            <EpisodeStatusProgressControl
                episodeStatus={params.row as EpisodeStatus}
                onUpdateProgress={onUpdateProgress}
            />
        ),
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

const AnimeStatusView: FC<OwnProps> = (props) => {
    const { animeStatus } = props;
    const { anime } = animeStatus;

    const [updateEpisodeStatus] = useUpdateEpisodeStatusMutation();
    const onUpdateProgress = useCallback(
        (episodeId: number, progress: EpisodeStatusProgress) => {
            updateEpisodeStatus({ id: anime.id, eid: episodeId, updateEpisodeStatusDto: { progress } });
        },
        [anime, updateEpisodeStatus],
    );
    const columns = useMemo(() => createColumns(onUpdateProgress), [onUpdateProgress]);

    const [page, setPage] = useState(0);
    const [pageSize, setPageSize] = useState(10);
    const [episodeType, setEpisodeType] = useState("REGULAR" as const);
    const onFilterModelChange = (model: GridFilterModel) => setEpisodeType(model.items[0].value);
    const { data, isFetching } = useFindEpisodeStatusesQuery({
        id: anime.id,
        page,
        size: pageSize,
        type: episodeType,
    });

    return (
        <div className="status-view">
            <FormGroup>
                <FormControl>
                    <InputLabel htmlFor="titleInput">Title</InputLabel>
                    <Input id="titleInput" contentEditable={false} value={resolveAnimeTitle(anime.titles)} />
                </FormControl>
                <FormControl>
                    <InputLabel htmlFor="progressInput">Progress</InputLabel>
                    <Select id="progressInput" contentEditable={false} value={animeStatus.progress}>
                        <MenuItem value="NOT_STARTED">NOT_STARTED</MenuItem>
                        <MenuItem value="IN_PROGRESS">IN_PROGRESS</MenuItem>
                        <MenuItem value="COMPLETED">COMPLETED</MenuItem>
                    </Select>
                </FormControl>
                <FormControl>
                    <InputLabel htmlFor="episodeProgressInput">Episodes</InputLabel>
                    <Input
                        id="episodeProgressInput"
                        contentEditable={false}
                        value={`${animeStatus.regularEpisodeCompleteCount}/${animeStatus.regularEpisodeTotalCount}`}
                    />
                </FormControl>
                <FormControl>
                    <InputLabel htmlFor="commentInput">Comment</InputLabel>
                    <Input id="commentInput" contentEditable={false} value={animeStatus.comment} />
                </FormControl>
            </FormGroup>
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
        </div>
    );
};

export default AnimeStatusView;
