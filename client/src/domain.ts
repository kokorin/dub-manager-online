export class AnimeTitle {
    constructor(
        public lang: string = "",
        public type: string = "",
        public text: string = ""
    ) {
    }
}

export class Anime {
    constructor(
        public id: number = 0,
        public type: string = "",
        public titles: Array<AnimeTitle> = []
    ) {

    }
}

export class EpisodeTitle {
    constructor(
        public lang: string = "",
        public text: string = ""
    ) {
    }
}

export class Episode {
    constructor(
        public id:number = 0,
        public type:string = "",
        public number:number = 0,
        public titles:Array<EpisodeTitle> = []
    ) {
    }
}

export class Page<T> {
    constructor(
        public number: number = 0,
        public size: number = 0,
        public numberOfElements: number = 0,
        public totalPages: number = 0,
        public totalElements: number = 0,
        public content: Array<T> = []
    ) {
    }
}