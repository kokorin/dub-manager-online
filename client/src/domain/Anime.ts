import AnimeTitle from "./AnimeTitle";

export default class Anime {
    constructor(
        public id: number = 0,
        public type: string = "",
        public titles: Array<AnimeTitle> = []
    ) {

    }
}
