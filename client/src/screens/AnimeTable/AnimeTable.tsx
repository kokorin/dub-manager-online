import { TableCell, TableRow } from "@material-ui/core";
import { nonNegativeOrDefault } from "service/numberUtils";
import { AnimeTableRows } from "../../components/AnimeTableRows";
import { Table } from "../../components/Table";
import { getAnimeList } from "../../api";
import { Anime, Page } from "../../domain";
import React, { ReactNode } from "react";

interface AnimeTableState {
    isLoading: boolean;
    data: Page<Anime>;
    search: string;
}

export default class AnimeTable extends React.Component<unknown, AnimeTableState> {
    searchTimeoutId: number | undefined;

    constructor(props: Readonly<unknown>) {
        super(props);
        this.state = {
            isLoading: false,
            data: {
                number: 0,
                size: 10,
                numberOfElements: 0,
                totalElements: 0,
                totalPages: 0,
                content: [],
            },
            search: "",
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

    private fetchData = async (fetchParams: { page?: number; size?: number; search?: string }) => {
        this.setState({ isLoading: true });

        const page = nonNegativeOrDefault(fetchParams.page, this.state.data.number);
        const size = fetchParams.size || this.state.data.size;
        const title = fetchParams.search || this.state.search;

        const res = await getAnimeList(page, size, title);

        this.setState({
            ...this.state,
            isLoading: false,
            data: res,
            search: title,
        });
    };

    componentDidMount = (): void => {
        this.fetchData({ page: 0, size: 10 });
    };

    componentWillUnmount(): void {
        clearTimeout(this.searchTimeoutId);
    }

    render(): ReactNode {
        const { number, size, totalElements, content } = this.state.data;
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
