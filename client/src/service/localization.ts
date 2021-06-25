import { AnimeTitle, AnimeTitleType, EpisodeTitle } from "../domain";

// TODO refactor
export const resolveAnimeTitle = (animeTitles: AnimeTitle[]): string => {
    if (!animeTitles || !animeTitles.length) {
        return "N/A";
    }

    for (const title of animeTitles) {
        if (title.type === AnimeTitleType.OFFICIAL && title.lang === "en") {
            return title.text;
        }
    }

    for (const title of animeTitles) {
        if (title.type === AnimeTitleType.MAIN && title.lang === "en") {
            return title.text;
        }
    }

    for (const title of animeTitles) {
        if ((title.type === AnimeTitleType.SYNONYM || title.type === AnimeTitleType.SHORT) && title.lang === "en") {
            return title.text;
        }
    }

    return "[" + animeTitles[0].text + "]";
};

// TODO refactor
export const resolveEpisodeTitle = (episodeTitles: EpisodeTitle[]): string => {
    if (!episodeTitles || !episodeTitles.length) {
        return "N/A";
    }

    for (const title of episodeTitles) {
        if (title.lang === "en") {
            return title.text;
        }
    }

    return "[" + episodeTitles[0].text + "]";
};
