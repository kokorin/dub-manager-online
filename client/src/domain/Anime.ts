import AnimeTitle from "./AnimeTitle";

export default interface Anime {
    id: number;
    type: string;
    titles: AnimeTitle[];
}
