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

class AnimeTableState {
    constructor(
        public isLoading:boolean = false
    ) {
    }
}

export default class AnimeTable extends React.Component<any, AnimeTableState> {
    state = new AnimeTableState();

    page = new Page<Anime>();


    private handleChangePage = (newPage: number) => {
        this.fetchData({page: newPage});
    }

    private handleChangeRowsPerPage = (newRowsPerPage: number) => {
        this.fetchData({page: 0, size: newRowsPerPage});
    }

    private fetchData = (fetchParams: { page?: number, size?: number }) => {
        this.setState({isLoading: true});

        let params = {
            "page": nonNegativeOrDefault(fetchParams.page, this.page.number),
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
        this.fetchData({page: 0, size: 10});
    }

    render() {
        return (<AnimeTableComponent page={this.page}
                                     onChangePage={this.handleChangePage}
                                     onChangeRowsPerPage={this.handleChangeRowsPerPage}/>);
    }
}
