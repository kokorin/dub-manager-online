import React from "react";
import axios from "axios";
import Page from "../domain/Page";
import Episode from "../domain/Episode";
import EpisodeTableComponent from "./EpisodeTableComponent";

interface EpisodeTableProps {
    animeId: number;
}

interface EpisodeTableState {
    isLoading: boolean;
    page: Page<Episode>;
}

function nonNegativeOrDefault(value: number | any, defValue: number): number {
    if (typeof value === "number" && value >= 0) {
        return value;
    }
    return defValue;
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
        this.fetchEpisodes({page: newPage});
    }

    private handleChangeRowsPerPage = (newRowsPerPage: number) => {
        this.fetchEpisodes({page: 0, size: newRowsPerPage});
    }

    private fetchEpisodes = async (fetchParams: { page?: number, size?: number }) => {
        this.setState({isLoading: true});

        const params = {
            "page": nonNegativeOrDefault(fetchParams.page, this.state.page.number),
            "size": fetchParams.size || this.state.page.size
        };

        const res = await axios.get(`/api/v1/anime/${this.props.animeId}/episodes`, {params: params});
        this.setState({
            ...this.state,
            isLoading: false,
            page: res.data
        });
    }

    componentDidMount() {
        this.fetchEpisodes({page: 0, size: 10});
    }

    render() {
        return (
            <EpisodeTableComponent page={this.state.page}
                                   onChangePage={this.handleChangePage}
                                   onChangeRowsPerPage={this.handleChangeRowsPerPage}/>
        );
    }
}