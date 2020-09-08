import React from "react";
import {Paper, TableBody, TableCell, TableContainer, TableHead, TableRow} from "@material-ui/core";
import {Anime, Episode, Page} from "./domain";
import {ToggleButton, ToggleButtonGroup} from "@material-ui/lab";
import axios from "axios";
import Table from "@material-ui/core/Table";
import TableFooter from "@material-ui/core/TableFooter";
import TablePagination from "@material-ui/core/TablePagination";

interface AnimeViewProps {
    animeId: number;
}

class AnimeViewState {
    selectedTab: AnimeViewTabType = AnimeViewTabType.DETAILS;
    isLoading: boolean = false;
    anime: Anime = new Anime();
    episodePage: Page<Episode> = new Page<Episode>();
}

enum AnimeViewTabType {
    DETAILS,
    EPISODES
}

function isNonNegative(value?:number) {
    return typeof value === "number" && value >=0;
}

export default class AnimeViewContainer extends React.Component<AnimeViewProps, AnimeViewState> {
    state = new AnimeViewState();

    private handleTabChange = (tab: AnimeViewTabType) => {
        switch (tab) {
            case AnimeViewTabType.DETAILS:
                this.setState({...this.state, selectedTab: tab});
                this.fetchAnime();
                break;
            case AnimeViewTabType.EPISODES:
                this.setState({...this.state, selectedTab: tab});
                this.fetchEpisodes({page: 0, size: 10});
                break;
            case null:
                // TODO
                break;
            default:
                throw new Error("Unknown tab: " + tab);
        }
    }

    private handleEpisodeChangePage = (newPage:number) => {
        this.fetchEpisodes({page:newPage});
    }

    private handleEpisodeChangeRowsPerPage = (newRowsPerPage:number) => {
        this.fetchEpisodes({page: 0, size: newRowsPerPage});
    }

    private fetchAnime = () => {
        this.setState({isLoading: true});

        let self = this;
        axios.get(`/api/v1/anime/${this.props.animeId}`).then(
            res => {
                self.setState({
                    ...this.state,
                    isLoading: false,
                    anime: res.data
                });
            }
        )
    }

    private fetchEpisodes = (fetchParams: {page?: number, size?: number}) => {
        this.setState({isLoading: true});

        let params = {
            "page": isNonNegative(fetchParams.page) ? fetchParams.page : this.state.episodePage.number,
            "size": fetchParams.size || this.state.episodePage.size
        };

        axios.get(`/api/v1/anime/${this.props.animeId}/episodes`, {params: params}).then(
            res => {
                this.setState({
                    ...this.state,
                    isLoading: false,
                    episodePage: res.data
                });
            }
        )
    }

    componentDidMount() {
        this.fetchAnime();
    }

    render() {
        var content = <div/>;
        switch (this.state.selectedTab) {
            case AnimeViewTabType.DETAILS:
                content = <AnimeDetailsView anime={this.state.anime}/>;
                break;
            case AnimeViewTabType.EPISODES:
                content = <AnimeEpisodesView page={this.state.episodePage}
                                             onChangePage={this.handleEpisodeChangePage}
                                             onChangeRowsPerPage={this.handleEpisodeChangeRowsPerPage}/>;
                break;

        }
        return (<Paper>
            <AnimeViewTabbar selectedTab={this.state.selectedTab}
                             onTabChange={this.handleTabChange}/>
            {content}
        </Paper>);
    }
}

interface AnimeViewTabbarProps {
    selectedTab: AnimeViewTabType;
    onTabChange: (tab: AnimeViewTabType) => void;
}

class AnimeViewTabbar extends React.Component<AnimeViewTabbarProps, any> {
    private handleChange = (event: any, newTab: AnimeViewTabType) => {
        this.props.onTabChange(newTab);
    }

    render() {
        return <ToggleButtonGroup exclusive
                                  value={this.props.selectedTab}
                                  onChange={this.handleChange}>
            <ToggleButton value={AnimeViewTabType.DETAILS}>Details</ToggleButton>
            <ToggleButton value={AnimeViewTabType.EPISODES}>Episodes</ToggleButton>
        </ToggleButtonGroup>;
    }
}

interface AnimeDetailsViewProps {
    anime: Anime;
}

class AnimeDetailsView extends React.Component<AnimeDetailsViewProps, any> {

    render() {
        return (<div>
            <div>ID: {this.props.anime.id}</div>
            <div>TYPE: {this.props.anime.type}</div>
            <div>TITLE: {this.props.anime.titles[0]?.text}</div>
        </div>);
    }
}

interface AnimeEpisodesViewProps {
    page: Page<Episode>;

    onChangePage: (newPage: number) => void;

    onChangeRowsPerPage: (newRowsPerPage: number) => void;
}


class AnimeEpisodesView extends React.Component<AnimeEpisodesViewProps, any> {

    private handleChangePage = (event: any, newPage: number) => {
        this.props.onChangePage(newPage);
    }

    private handleChangeRowsPerPage = (event: React.ChangeEvent<HTMLInputElement>) => {
        let size = parseInt(event.target.value);
        this.props.onChangeRowsPerPage(size);
    }

    render() {
        let rows = this.props.page.content?.map(episode => (
                <TableRow key={episode.id}>
                    <TableCell component="th" scope="row">{episode.id}</TableCell>
                    <TableCell align="center">{episode.number}</TableCell>
                    <TableCell align="center">{episode.type}</TableCell>
                    <TableCell align="center">{episode.titles[0].text}</TableCell>
                </TableRow>
            )
        );
        return (<Paper>
            <TableContainer>
                <Table stickyHeader={true} aria-label="simple table">
                    <TableHead>
                        <TableRow>
                            <TableCell>ID</TableCell>
                            <TableCell align="center">NUMBER</TableCell>
                            <TableCell align="center">TYPE</TableCell>
                            <TableCell align="center">TITLE</TableCell>
                        </TableRow>
                    </TableHead>
                    <TableBody>
                        {rows}
                    </TableBody>
                    {<TableFooter>
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
                    </TableFooter>}
                </Table>
            </TableContainer>
        </Paper>);
    }
}
