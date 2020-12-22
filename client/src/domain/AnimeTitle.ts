export enum AnimeTitleType {
    SHORT = "SHORT",
    OFFICIAL = "OFFICIAL",
    SYNONYM = "SYNONYM",
    MAIN = "MAIN",
    CARD = "CARD",
    KANA = "KANA"
}

export default interface AnimeTitle {
    lang: string;
    type: AnimeTitleType | string;
    text: string;
}
