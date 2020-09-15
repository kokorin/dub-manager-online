import React from "react";
import AnimeTitle, {AnimeTitleType} from "../domain/AnimeTitle";

export interface AnimeTitleLabelProps {
    animeTitles: AnimeTitle[]
}

// TODO refactor
const getLabel = (animeTitles: AnimeTitle[]) => {
    if (!animeTitles || !animeTitles.length) {
        return "N/A";
    }

    for (let title of animeTitles) {
        if (title.type === AnimeTitleType.OFFICIAL && title.lang === "en") {
            return title.text;
        }
    }

    for (let title of animeTitles) {
        if (title.type === AnimeTitleType.MAIN && title.lang === "en") {
            return title.text;
        }
    }

    for (let title of animeTitles) {
        if ((title.type === AnimeTitleType.SYNONYM || title.type === AnimeTitleType.SHORT) && title.lang === "en") {
            return title.text;
        }
    }

    return "[" + animeTitles[0].text + "]";
}

export default class AnimeTitleLabel extends React.Component<AnimeTitleLabelProps, any> {

    render() {
        const label = getLabel(this.props.animeTitles);

        return (
            <>{label}</>
        );
    }
}