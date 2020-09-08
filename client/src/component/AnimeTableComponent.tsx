import React from 'react';
import {Paper, TableBody, TableCell, TableContainer, TableHead, TableRow} from "@material-ui/core";
import TablePagination from "@material-ui/core/TablePagination";
import Table from "@material-ui/core/Table";
import TableFooter from "@material-ui/core/TableFooter";
import Page from "../domain/Page";
import Anime from "../domain/Anime";

export interface AnimeTableComponentProps {
    page:Page<Anime>;
    onChangePage: (newPage: number) => void;
    onChangeRowsPerPage: (newRowsPerPage:number) => void;

}

class AnimeTableComponentState {
    constructor(
        public isLoading: boolean = false,
        public page: Page<Anime> = new Page<Anime>(),
        public error: string = ""
    ) {
    }
}

export default class AnimeTableComponent extends React.Component<AnimeTableComponentProps, AnimeTableComponentState> {
    state = new AnimeTableComponentState();

    private handleChangePage = (event:any, newPage:number) => {
        this.props.onChangePage(newPage);
    }

    private handleChangeRowsPerPage = (event: React.ChangeEvent<HTMLInputElement>) => {
        let size = parseInt(event.target.value);
        this.props.onChangeRowsPerPage(size);
    }

    render() {
        let rows = this.props.page.content.map(anime => (
                <TableRow key={anime.id}>
                    <TableCell component="th" scope="row">{anime.id}</TableCell>
                    <TableCell align="center">{anime.type}</TableCell>
                    <TableCell align="center">{anime.titles[0].text}</TableCell>
                </TableRow>
            )
        );

        return (<Paper>
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
                                onChangeRowsPerPage={this.handleChangeRowsPerPage}
                            />
                        </TableRow>
                    </TableFooter>
                </Table>
            </TableContainer>
        </Paper>);
    }
}
