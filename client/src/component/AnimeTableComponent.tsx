import React from 'react';
import {Paper, TableBody, TableCell, TableContainer, TableHead, TableRow} from "@material-ui/core";
import TablePagination from "@material-ui/core/TablePagination";
import Table from "@material-ui/core/Table";
import TableFooter from "@material-ui/core/TableFooter";
import Page from "../domain/Page";
import Anime from "../domain/Anime";
import {Link} from "react-router-dom";
import AnimeTitleLabel from "./AnimeTitleLabel";

export interface AnimeTableComponentProps {
    page: Page<Anime>;
    onChangePage: (newPage: number) => void;
    onChangeRowsPerPage: (newRowsPerPage: number) => void;
}

export default class AnimeTableComponent extends React.Component<AnimeTableComponentProps, any> {

    private handleChangePage = (event: any, newPage: number) => {
        this.props.onChangePage(newPage);
    }

    private handleChangeRowsPerPage = (event: React.ChangeEvent<HTMLInputElement>) => {
        let size = parseInt(event.target.value);
        this.props.onChangeRowsPerPage(size);
    }

    render() {
        const rows = this.props.page.content.map(anime => (
            <TableRow key={anime.id}>
                <TableCell component="th" scope="row">
                    <Link to={`/anime/${anime.id}`}>
                        {anime.id}
                    </Link>
                </TableCell>
                <TableCell align="center">
                    <Link to={`/anime/${anime.id}`}>
                        {anime.type}
                    </Link>
                </TableCell>
                <TableCell align="center">
                    <Link to={`/anime/${anime.id}`}>
                        <AnimeTitleLabel animeTitles={anime.titles}/>
                    </Link>
                </TableCell>
            </TableRow>
        ));

        return (
            <Paper>
                <TableContainer>
                    <Table stickyHeader={true} aria-label="simple table">
                        <TableHead>
                            <TableRow>
                                <TableCell>ID</TableCell>
                                <TableCell align="center">TYPE</TableCell>
                                <TableCell align="center">TITLE</TableCell>
                            </TableRow>
                        </TableHead>
                        <TableBody>
                            {rows}
                        </TableBody>
                        <TableFooter>
                            <TableRow>
                                <TablePagination
                                    rowsPerPageOptions={[5, 10, 25]}
                                    count={this.props.page.totalElements}
                                    rowsPerPage={this.props.page.size}
                                    page={this.props.page.number}
                                    onChangePage={this.handleChangePage}
                                    onChangeRowsPerPage={this.handleChangeRowsPerPage}/>
                            </TableRow>
                        </TableFooter>
                    </Table>
                </TableContainer>
            </Paper>
        );
    }
}
