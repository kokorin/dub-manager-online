import EpisodeTitle from "./EpisodeTitle";

export default interface Episode {
    id: number;
    type: string;
    number: number;
    titles: EpisodeTitle[];
}
