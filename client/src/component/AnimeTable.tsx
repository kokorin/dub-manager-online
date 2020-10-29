import { TableCell, TableRow } from "@material-ui/core";
import axios from "axios";
import React, { ReactNode } from "react";
import Anime from "../domain/Anime";
import Page from "../domain/Page";
import { nonNegativeOrDefault } from "../service";
import { AnimeTableRows } from "./AnimeTableRows";
import { Table } from "./Table";

interface AnimeTableState {
    isLoading: boolean;
    page: Page<Anime>;
    search: string;
}

export default class AnimeTable extends React.Component<any, AnimeTableState> {
    searchTimeoutId: number | undefined;

    constructor(props: Readonly<any>) {
        super(props);
        this.state = {
            isLoading: false,
            page: {
                number: 0,
                size: 10,
                numberOfElements: 0,
                totalElements: 0,
                totalPages: 0,
                content: []
            },
            search: ""
        };
    }

    private handleChangeSearch = (newSearch: string) => {
        if (this.searchTimeoutId != null) {
            clearTimeout(this.searchTimeoutId);
        }

        this.searchTimeoutId = window.setTimeout(() => {
            this.fetchData({ page: 0, search: newSearch });
            this.searchTimeoutId = 0;
        }, 1_500);
    };

    private handleChangePage = (newPage: number) => {
        this.fetchData({ page: newPage });
    };

    private handleChangeRowsPerPage = (newRowsPerPage: number) => {
        this.fetchData({ page: 0, size: newRowsPerPage });
    };

    private fetchData = async (fetchParams: { page?: number, size?: number, search?: string }) => {
        this.setState({ isLoading: true });

        const search = fetchParams.search || this.state.search;

        const params = {
            page: nonNegativeOrDefault(fetchParams.page, this.state.page.number),
            size: fetchParams.size || this.state.page.size,
            title: search
        };

        const res = await axios.get("/api/v1/anime", { params: params });
        this.setState({
            ...this.state,
            isLoading: false,
            page: res.data,
            search: search
        });
    };

    componentDidMount = () => {
        this.fetchData({ page: 0, size: 10 });
    };

    componentWillUnmount() {
        clearTimeout(this.searchTimeoutId);
    }

    render() {
        const { number, size, totalElements, content } = this.state.page;
        const head: ReactNode = (
            <TableRow>
                <TableCell>ID</TableCell>
                <TableCell align="center">TYPE</TableCell>
                <TableCell align="center">TITLE</TableCell>
            </TableRow>
        );

        return (
            <Table
                head={head}
                number={number}
                size={size}
                totalElements={totalElements}
                onChangeSearch={this.handleChangeSearch}
                onChangePage={this.handleChangePage}
                onChangeRowsPerPage={this.handleChangeRowsPerPage}
            >
                <AnimeTableRows content={content} />
            </Table>
        );
    }
}
