import { Paper } from "@material-ui/core";
import React, { FC, useCallback } from "react";
import { Search } from "./Search";
import { DataGrid, GridColDef, GridPageChangeParams } from "@material-ui/data-grid";

interface OwnProps {
    columns: GridColDef[];
    page: number;
    pageSize: number;
    title?: string;
    rows: Record<string, unknown>[];
    rowCount: number;
    onSearchChange?: (newSearch: string) => void;
    onPageChange: (newPage: number) => void;
    onPageSizeChange: (newRowsPerPage: number) => void;
}

const GridWithSearch: FC<OwnProps> = (props) => {
    const { page, pageSize, title, rows, columns, rowCount, onSearchChange, onPageChange, onPageSizeChange } = props;

    const handleChangeSearch = useCallback((text) => onSearchChange && onSearchChange(text), [onSearchChange]);
    const handlePageChange = useCallback((params: GridPageChangeParams) => onPageChange(params.page), [onPageChange]);
    const handlePageSizeChange = useCallback(
        (params: GridPageChangeParams) => onPageSizeChange(Number(params.pageSize)),
        [onPageSizeChange],
    );

    return (
        <Paper>
            {onSearchChange && <Search onChangeSearch={handleChangeSearch} text={title || ""} />}

            <DataGrid
                autoHeight={true}
                columns={columns}
                rows={rows}
                page={page}
                pageSize={pageSize}
                rowCount={rowCount}
                rowsPerPageOptions={[5, 10, 25]}
                onPageChange={handlePageChange}
                onPageSizeChange={handlePageSizeChange}
                paginationMode="server"
            />
        </Paper>
    );
};

export default GridWithSearch;
