import React, { FC, useState } from "react";
import { useFindAnimeQuery } from "../api";
import { DataGrid, GridColDef, GridRowId } from "@mui/x-data-grid";
import { resolveAnimeTitle } from "../service";
import { Anime } from "../domain";
import { Search } from "../components/Search";
import { CommonProps } from "@mui/material/OverridableComponent";

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

interface OwnProps extends CommonProps {
    onAnimeSelected: (animeIds: number[]) => void;
}

export const AnimeTable: FC<OwnProps> = (props) => {
    const [page, setPage] = useState(0);
    const [pageSize, setPageSize] = useState(10);
    const [filter, setFilter] = useState("");

    const { data } = useFindAnimeQuery({ page, size: pageSize, title: filter });

    const handleSearchChange = (text: string) => {
        setPage(0);
        setFilter(text);
    };

    const handleSelectionModelChange = (selectionModel: GridRowId[]) => {
        props.onAnimeSelected(selectionModel as number[]);
    };

    return (
        <div className={`flex-column ${props.className}`} style={props.style}>
            <Search label="Anime Title" text={filter} onChangeSearch={handleSearchChange} />
            <DataGrid
                className="flex-grow"
                rowsPerPageOptions={[5, 10, 25]}
                columns={columns}
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
