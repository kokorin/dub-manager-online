import {TableCell, TableRow} from "@material-ui/core";
import axios from "axios";
import {nonNegativeOrDefault} from "../service";
import {EpisodeTableRows} from "./EpisodeTableRows";
import {Table} from "./Table";
import {Episode, Page} from "../domain";
import React, {ReactNode} from "react";

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
                content: []
            }
        };
    }

    private handleChangePage = (newPage: number) => {
        this.fetchEpisodes({ page: newPage });
    };

    private handleChangeRowsPerPage = (newRowsPerPage: number) => {
        this.fetchEpisodes({ page: 0, size: newRowsPerPage });
    };

    private fetchEpisodes = async (fetchParams: { page?: number, size?: number }) => {
        this.setState({ isLoading: true });

        const params = {
            "page": nonNegativeOrDefault(fetchParams.page, this.state.page.number),
            "size": fetchParams.size || this.state.page.size
        };

        const res = await axios.get(`/api/v1/anime/${this.props.animeId}/episodes`, { params: params });
        this.setState({
            ...this.state,
            isLoading: false,
            page: res.data
        });
    };

    componentDidMount() {
        this.fetchEpisodes({ page: 0, size: 10 });
    }

    render() {
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
