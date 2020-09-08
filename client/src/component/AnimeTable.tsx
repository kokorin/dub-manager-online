import React from "react";
import axios from "axios";
import Page from "../domain/Page";
import Anime from "../domain/Anime";
import AnimeTableComponent from "./AnimeTableComponent";

function nonNegativeOrDefault(value: number | any, defValue: number): number {
    if (typeof value === "number" && value >= 0) {
        return value;
    }
    return defValue;
}

interface AnimeTableState {
    isLoading: boolean;
    page: Page<Anime>
}

export default class AnimeTable extends React.Component<any, AnimeTableState> {

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
            }
        };
    }

    private handleChangePage = (newPage: number) => {
        this.fetchData({page: newPage});
    }

    private handleChangeRowsPerPage = (newRowsPerPage: number) => {
        this.fetchData({page: 0, size: newRowsPerPage});
    }

    private fetchData = async (fetchParams: { page?: number, size?: number }) => {
        this.setState({isLoading: true});

        const params = {
            "page": nonNegativeOrDefault(fetchParams.page, this.state.page.number),
            "size": fetchParams.size || this.state.page.size
        };

        const res = await axios.get("/api/v1/anime", {params: params});
        this.setState({
            ...this.state,
            isLoading: false,
            page: res.data
        });
    }

    componentDidMount = () => {
        this.fetchData({page: 0, size: 10});
    }

    render() {
        return (
            <AnimeTableComponent page={this.state.page}
                                 onChangePage={this.handleChangePage}
                                 onChangeRowsPerPage={this.handleChangeRowsPerPage}/>
        );
    }
}
