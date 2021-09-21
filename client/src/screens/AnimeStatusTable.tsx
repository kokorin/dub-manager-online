import React, { FC, useMemo, useState } from "react";
import { useFindAnimeStatusesQuery } from "../api";
import { DataGrid, GridCellParams, GridColDef, GridRowId, GridRowIdGetter } from "@mui/x-data-grid";
import { Search } from "../components/Search";
import { Button } from "@material-ui/core";
import { AnimeStatus } from "../domain";
import { resolveAnimeTitle } from "../service";
import { Edit } from "@material-ui/icons";

const getRowId: GridRowIdGetter = (data) => (data as AnimeStatus).anime.id;

type ColumnsProvider = (onEditAnimeStatus: (animeId: number) => void) => GridColDef[];

const createColumns: ColumnsProvider = (onEditAnimeStatus: (animeId: number) => void) => {
    const columns: GridColDef[] = [
        {
            field: "anime.id",
            headerName: "ID",
            flex: 1,
            valueGetter: (params) => getRowId(params.row),
        },
        {
            field: "edit",
            headerName: "",
            flex: 1,
            // see https://github.com/mui-org/material-ui-x/issues/951
            // eslint-disable-next-line react/display-name
            renderCell: (params: GridCellParams) => (
                <Button
                    startIcon={<Edit />}
                    onClick={(event) => {
                        event.stopPropagation();
                        onEditAnimeStatus((params.row as AnimeStatus).anime.id);
                    }}
                >
                    edit
                </Button>
            ),
        },
        {
            field: "anime.titles",
            headerName: "TITLE",
            flex: 5,
            valueGetter: (params) => resolveAnimeTitle((params.row as AnimeStatus).anime.titles),
        },
        {
            field: "progress",
            flex: 2,
        },
        {
            field: "episodes",
            flex: 2,
            valueGetter: (params) => {
                const status = params.row as AnimeStatus;
                return `${status.regularEpisodeCompleteCount}/${status.regularEpisodeTotalCount}`;
            },
        },
        {
            field: "regularEpisodeNextAirDate",
            headerName: "NEXT",
            flex: 2,
        },
        {
            field: "comment",
            flex: 5,
        },
    ];

    return columns;
};

interface OwnProps {
    onAnimeStatusSelected: (animeIds: number[]) => void;
    onAnimeStatusEdit: (animeId: number) => void;
}

const AnimeStatusTable: FC<OwnProps> = (props) => {
    const [page, setPage] = useState(0);
    const [pageSize, setPageSize] = useState(10);
    const [filter, setFilter] = useState("");
    const { data } = useFindAnimeStatusesQuery({ page, size: pageSize });

    const handleSelectionModelChange = (selectionModel: GridRowId[]) => {
        props.onAnimeStatusSelected(selectionModel as number[]);
    };

    const columns = useMemo(() => createColumns(props.onAnimeStatusEdit), [props.onAnimeStatusEdit]);

    return (
        <div className="status-table">
            <Search text={filter} onChangeSearch={setFilter} />

            <DataGrid
                columns={columns}
                getRowId={getRowId}
                page={page}
                pageSize={pageSize}
                rows={data?.content || []}
                rowCount={data?.totalElements || 0}
                onPageChange={setPage}
                onPageSizeChange={setPageSize}
                paginationMode="server"
                checkboxSelection={true}
                onSelectionModelChange={handleSelectionModelChange}
            />
        </div>
    );
};

export default AnimeStatusTable;
