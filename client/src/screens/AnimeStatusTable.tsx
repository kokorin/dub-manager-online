import React, { CSSProperties, FC, useMemo, useState } from "react";
import { useFindAnimeStatusesQuery } from "../api";
import { DataGrid, GridCellParams, GridColDef, GridRowId, GridRowIdGetter } from "@material-ui/data-grid";
import { Search } from "../components/Search";
import { Button, CircularProgress, Modal } from "@material-ui/core";
import { AnimeStatus } from "../domain";
import { resolveAnimeTitle } from "../service";
import { Edit } from "@material-ui/icons";

const getRowId: GridRowIdGetter = (data) => (data as AnimeStatus).anime.id;

type ColumnsProvider = (onEditAnimeStatus: (status: AnimeStatus) => void) => GridColDef[];

const createColumns: ColumnsProvider = (onEditAnimeStatus: (status: AnimeStatus) => void) => {
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
                    style={{ textTransform: "none" }}
                    onClick={(event) => {
                        event.stopPropagation();
                        onEditAnimeStatus(params.row as AnimeStatus);
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
                return `${status.completedRegularEpisodes}/${status.totalRegularEpisodes}`;
            },
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
    onAnimeStatusEdit: (status: AnimeStatus) => void;
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

    const columns = useMemo(() => createColumns(props.onAnimeStatusEdit), [props.onAnimeStatusEdit]);

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
