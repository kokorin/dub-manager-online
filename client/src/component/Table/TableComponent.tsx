import { Paper, TableBody, TableContainer, TableHead, TableRow } from "@material-ui/core";
import Table from "@material-ui/core/Table";
import TableFooter from "@material-ui/core/TableFooter";
import TablePagination from "@material-ui/core/TablePagination";
import React, { FC, ReactNode, useCallback } from "react";
import { Search } from "../Search";

interface OwnProps {
    head: ReactNode;
    number: number;
    size: number;
    totalElements: number;
    onChangeSearch?: (newSearch: string) => void;
    onChangePage: (newPage: number) => void;
    onChangeRowsPerPage: (newRowsPerPage: number) => void;
}

const TableComponent: FC<OwnProps> = (props) => {
    const { head, children, number, size, totalElements, onChangeSearch, onChangePage, onChangeRowsPerPage } = props;

    const handleChangePage = useCallback((_, newPage: number) => onChangePage(newPage), [onChangePage]);
    const handleChangeRowsPerPage = useCallback(
        (event: React.ChangeEvent<HTMLInputElement>) => onChangeRowsPerPage(Number(event.target.value)),
        [onChangeRowsPerPage]
    );

    return (
        <Paper>
            {onChangeSearch && <Search onChangeSearch={onChangeSearch} />}
            <TableContainer>
                <Table stickyHeader={true} aria-label="simple table">
                    <TableHead>{head}</TableHead>
                    <TableBody>{children}</TableBody>
                    <TableFooter>
                        <TableRow>
                            <TablePagination
                                rowsPerPageOptions={[5, 10, 25]}
                                count={totalElements}
                                rowsPerPage={size}
                                page={number}
                                onChangePage={handleChangePage}
                                onChangeRowsPerPage={handleChangeRowsPerPage}
                            />
                        </TableRow>
                    </TableFooter>
                </Table>
            </TableContainer>
        </Paper>
    );
};

export default TableComponent;
