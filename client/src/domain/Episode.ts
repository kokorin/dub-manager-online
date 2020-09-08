import EpisodeTitle from "./EpisodeTitle";

export default class Episode {
    constructor(
        public id:number = 0,
        public type:string = "",
        public number:number = 0,
        public titles:Array<EpisodeTitle> = []
    ) {
    }
}
