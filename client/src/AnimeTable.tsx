import React from 'react';
import './App.css';
import {Paper, TableBody, TableCell, TableContainer, TableHead, TableRow} from "@material-ui/core";
import TablePagination from "@material-ui/core/TablePagination";
import Table from "@material-ui/core/Table";
import TableFooter from "@material-ui/core/TableFooter";
import {Anime, Page} from "./domain";

import axios from "axios";

class AnimeTableState {
    constructor(
        public isLoading: boolean = false,
        public page: Page<Anime> = new Page<Anime>(),
        public error: string = ""
    ) {
    }
}

function isNonNegative(value?:number) {
    return typeof value === "number" && value >=0;
}

export class AnimeTableContainer extends React.Component {
    state = {
        isLoading: false
    }

    page = new Page<Anime>();


    private handleChangePage = (newPage:number) => {
        this.fetchData({page:newPage});
    }

    private handleChangeRowsPerPage = (newRowsPerPage:number) => {
        this.fetchData({page: 0, size: newRowsPerPage});
    }

    private fetchData = (fetchParams: { page?: number, size?: number }) => {
        this.setState({isLoading: true});

        let params = {
            "page": isNonNegative(fetchParams.page) ? fetchParams.page : this.page.number,
            "size": fetchParams.size || this.page.size
        };

        let self = this;
        axios.get("/api/v1/anime", {params: params}).then(
            res => {
                this.page = res.data;
                self.setState({
                    isLoading: false
                });
            }
        )
    }

    componentDidMount = () => {
        console.log("AnimeTableContainer DID MOUNT");
        this.fetchData({page: 0, size: 10});
    }

    render() {
        return (<AnimeTable page={this.page}
                            onChangePage={this.handleChangePage}
                            onChangeRowsPerPage={this.handleChangeRowsPerPage}/>);
    }
}

interface AnimeTableProps {
    page:Page<Anime>;

    onChangePage: (newPage: number) => void;

    onChangeRowsPerPage: (newRowsPerPage:number) => void;

}

class AnimeTable extends React.Component<AnimeTableProps> {
    state = new AnimeTableState();

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
