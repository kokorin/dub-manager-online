import React, { CSSProperties, FC, useState } from "react";
import { DataGrid, GridColDef, GridRowIdGetter } from "@material-ui/data-grid";
import { useFindEpisodeStatusesQuery } from "../api";
import {
    Box,
    CircularProgress,
    FormControl,
    FormGroup,
    Input,
    InputLabel,
    MenuItem,
    Modal,
    Select,
} from "@material-ui/core";
import { AnimeStatus, EpisodeStatus } from "../domain";
import { resolveAnimeTitle, resolveEpisodeTitle } from "../service";

interface OwnProps {
    animeStatus: AnimeStatus;
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
    const { animeStatus } = props;
    const { anime } = animeStatus;

    const [page, setPage] = useState(0);
    const [pageSize, setPageSize] = useState(10);
    const { data, isLoading } = useFindEpisodeStatusesQuery({ id: anime.id, page, size: pageSize });

    return (
        <div className="anime-status-view" style={props.style}>
            <Modal open={isLoading}>
                <CircularProgress />
            </Modal>
            <div style={{ height: "100%", display: "flex", flexDirection: "column" }}>
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
                            value={`${animeStatus.completedRegularEpisodes}/${animeStatus.totalRegularEpisodes}`}
                        />
                    </FormControl>
                    <FormControl>
                        <InputLabel htmlFor="commentInput">Comment</InputLabel>
                        <Input id="commentInput" contentEditable={false} value={animeStatus.comment} />
                    </FormControl>
                </FormGroup>
                <DataGrid
                    style={{ flexGrow: 1 }}
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
        </div>
    );
};

export default AnimeStatusView;
