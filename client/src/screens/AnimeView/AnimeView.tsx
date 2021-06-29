import React, { FC, ReactNode, useCallback, useState } from "react";
import { Paper } from "@material-ui/core";
import { ToggleButton, ToggleButtonGroup } from "@material-ui/lab";
import AnimeDetails from "../../components/AnimeDetails";
import EpisodeTable from "../../components/EpisodeTable";
import { RouteChildrenProps } from "react-router-dom";

interface OwnProps {
    animeId: string;
}

enum AnimeViewTabType {
    DETAILS,
    EPISODES,
}

const AnimeView: FC<RouteChildrenProps<OwnProps>> = ({ match }) => {
    const [selectedTab, setSelectedTab] = useState(AnimeViewTabType.DETAILS);

    const handleTabChange = useCallback((tab: AnimeViewTabType) => {
        if (tab != null) {
            setSelectedTab(tab);
        }
    }, []);

    let content = <div />;
    const animeId = Number(match?.params.animeId);
    switch (selectedTab) {
        case AnimeViewTabType.DETAILS:
            content = <AnimeDetails animeId={animeId} />;
            break;
        case AnimeViewTabType.EPISODES:
            content = <EpisodeTable animeId={animeId} />;
            break;
    }

    return (
        <Paper>
            <AnimeViewTabbar selectedTab={selectedTab} onTabChange={handleTabChange} />
            {content}
        </Paper>
    );
};

export default AnimeView;

interface AnimeViewTabbarProps {
    selectedTab: AnimeViewTabType;
    onTabChange: (tab: AnimeViewTabType) => void;
}

class AnimeViewTabbar extends React.Component<AnimeViewTabbarProps> {
    private handleChange = (event: React.MouseEvent<HTMLElement>, newTab: AnimeViewTabType) => {
        this.props.onTabChange(newTab);
    };

    render(): ReactNode {
        return (
            <ToggleButtonGroup exclusive size="large" value={this.props.selectedTab} onChange={this.handleChange}>
                <ToggleButton value={AnimeViewTabType.DETAILS}>Details</ToggleButton>
                <ToggleButton value={AnimeViewTabType.EPISODES}>Episodes</ToggleButton>
            </ToggleButtonGroup>
        );
    }
}
