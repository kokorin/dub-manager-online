import React from "react";
import EpisodeTitle from "../domain/EpisodeTitle";

export interface EpisodeTitleLabelProps {
    episodeTitles: EpisodeTitle[]
}

// TODO refactor
const getLabel = (episodeTitles: EpisodeTitle[]) => {
    if (!episodeTitles || !episodeTitles.length) {
        return "N/A";
    }

    for (let title of episodeTitles) {
        if (title.lang === "en") {
            return title.text;
        }
    }

    return "[" + episodeTitles[0].text + "]";
}

export default class EpisodeTitleLabel extends React.Component<EpisodeTitleLabelProps, any> {

    render() {
        const label = getLabel(this.props.episodeTitles);

        return (
            <>{label}</>
        );
    }
}
