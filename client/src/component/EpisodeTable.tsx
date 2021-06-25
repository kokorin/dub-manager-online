import { TableCell, TableRow } from "@material-ui/core";
import { nonNegativeOrDefault } from "../service";
import { EpisodeTableRows } from "./EpisodeTableRows";
import { Table } from "./Table";
import { Episode, Page } from "../domain";
import React, { ReactNode } from "react";
import { getEpisodeList } from "../api";

interface EpisodeTableProps {
    animeId: number;
}

interface EpisodeTableState {
    isLoading: boolean;
    page: Page<Episode>;
}

export default class EpisodeTable extends React.Component<EpisodeTableProps, EpisodeTableState> {
    constructor(props: Readonly<EpisodeTableProps>) {
        super(props);
        this.state = {
            isLoading: false,
            page: {
                number: 0,
                size: 10,
                numberOfElements: 0,
                totalElements: 0,
                totalPages: 0,
                content: [],
            },
        };
    }

    private handleChangePage = (newPage: number) => {
        this.fetchEpisodes({ page: newPage });
    };

    private handleChangeRowsPerPage = (newRowsPerPage: number) => {
        this.fetchEpisodes({ page: 0, size: newRowsPerPage });
    };

    private fetchEpisodes = async (fetchParams: { page?: number; size?: number }) => {
        this.setState({ isLoading: true });

        const animeId = this.props.animeId;
        const page = nonNegativeOrDefault(fetchParams.page, this.state.page.number);
        const size = fetchParams.size || this.state.page.size;

        const result = await getEpisodeList(animeId, page, size);
        this.setState({
            ...this.state,
            isLoading: false,
            page: result,
        });
    };

    componentDidMount(): void {
        this.fetchEpisodes({ page: 0, size: 10 });
    }

    render(): ReactNode {
        const { number, size, totalElements, content } = this.state.page;
        const head: ReactNode = (
            <TableRow>
                <TableCell>ID</TableCell>
                <TableCell align="center">NUMBER</TableCell>
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
                onChangePage={this.handleChangePage}
                onChangeRowsPerPage={this.handleChangeRowsPerPage}
            >
                <EpisodeTableRows content={content} />
            </Table>
        );
    }
}
