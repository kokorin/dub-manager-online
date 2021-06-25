import React from "react";
import { Paper } from "@material-ui/core";
import { ToggleButton, ToggleButtonGroup } from "@material-ui/lab";
import AnimeDetails from "./AnimeDetails";
import EpisodeTable from "./EpisodeTable";

interface AnimeViewProps {
    animeId: number;
}

interface AnimeViewState {
    selectedTab: AnimeViewTabType;
}

enum AnimeViewTabType {
    DETAILS,
    EPISODES,
}

export default class AnimeViewContainer extends React.Component<AnimeViewProps, AnimeViewState> {
    constructor(props: Readonly<AnimeViewProps>) {
        super(props);
        this.state = {
            selectedTab: AnimeViewTabType.DETAILS,
        };
    }

    private handleTabChange = (tab: AnimeViewTabType) => {
        if (tab !== null) {
            this.setState({
                ...this.state,
                selectedTab: tab,
            });
        }
    };

    render(): React.ReactNode {
        let content = <div />;
        switch (this.state.selectedTab) {
            case AnimeViewTabType.DETAILS:
                content = <AnimeDetails animeId={this.props.animeId} />;
                break;
            case AnimeViewTabType.EPISODES:
                content = <EpisodeTable animeId={this.props.animeId} />;
                break;
        }
        return (
            <Paper>
                <AnimeViewTabbar selectedTab={this.state.selectedTab} onTabChange={this.handleTabChange} />
                {content}
            </Paper>
        );
    }
}

interface AnimeViewTabbarProps {
    selectedTab: AnimeViewTabType;
    onTabChange: (tab: AnimeViewTabType) => void;
}

class AnimeViewTabbar extends React.Component<AnimeViewTabbarProps> {
    private handleChange = (event: React.MouseEvent<HTMLElement>, newTab: AnimeViewTabType) => {
        this.props.onTabChange(newTab);
    };

    render() {
        return (
            <ToggleButtonGroup exclusive size="large" value={this.props.selectedTab} onChange={this.handleChange}>
                <ToggleButton value={AnimeViewTabType.DETAILS}>Details</ToggleButton>
                <ToggleButton value={AnimeViewTabType.EPISODES}>Episodes</ToggleButton>
            </ToggleButtonGroup>
        );
    }
}
