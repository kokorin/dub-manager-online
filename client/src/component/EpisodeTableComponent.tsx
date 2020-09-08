import React from "react";
import {Paper, TableBody, TableCell, TableContainer, TableHead, TableRow} from "@material-ui/core";
import Table from "@material-ui/core/Table";
import TableFooter from "@material-ui/core/TableFooter";
import TablePagination from "@material-ui/core/TablePagination";
import Episode from "../domain/Episode";
import Page from "../domain/Page";

interface EpisodeTableComponentProps {
    page: Page<Episode>;
    onChangePage: (newPage: number) => void;
    onChangeRowsPerPage: (newRowsPerPage: number) => void;
}

export default class EpisodeTableComponent extends React.Component<EpisodeTableComponentProps, any> {

    private handleChangePage = (event: any, newPage: number) => {
        this.props.onChangePage(newPage);
    }

    private handleChangeRowsPerPage = (event: React.ChangeEvent<HTMLInputElement>) => {
        let size = parseInt(event.target.value);
        this.props.onChangeRowsPerPage(size);
    }

    render() {
        let rows = this.props.page?.content?.map(episode => (
            <TableRow key={episode.id}>
                <TableCell component="th" scope="row">{episode.id}</TableCell>
                <TableCell align="center">{episode.number}</TableCell>
                <TableCell align="center">{episode.type}</TableCell>
                <TableCell align="center">{episode.titles[0].text}</TableCell>
            </TableRow>
        ));

        return (
            <Paper>
                <TableContainer>
                    <Table stickyHeader={true} aria-label="simple table">
                        <TableHead>
                            <TableRow>
                                <TableCell>ID</TableCell>
                                <TableCell align="center">NUMBER</TableCell>
                                <TableCell align="center">TYPE</TableCell>
                                <TableCell align="center">TITLE</TableCell>
                            </TableRow>
                        </TableHead>
                        <TableBody>
                            {rows}
                        </TableBody>
                        {<TableFooter>
                            <TableRow>
                                <TablePagination
                                    rowsPerPageOptions={[5, 10, 25]}
                                    count={this.props.page?.totalElements}
                                    rowsPerPage={this.props.page?.size}
                                    page={this.props.page?.number}
                                    onChangePage={this.handleChangePage}
                                    onChangeRowsPerPage={this.handleChangeRowsPerPage}
                                />
                            </TableRow>
                        </TableFooter>}
                    </Table>
                </TableContainer>
            </Paper>
        );
    }
}
