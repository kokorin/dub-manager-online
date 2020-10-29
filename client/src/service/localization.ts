import AnimeTitle, { AnimeTitleType } from "../domain/AnimeTitle";
import EpisodeTitle from "../domain/EpisodeTitle";

// TODO refactor
export const resolveAnimeTitle = (animeTitles: AnimeTitle[]) => {
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
};

// TODO refactor
export const resolveEpisodeTitle = (episodeTitles: EpisodeTitle[]) => {
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
