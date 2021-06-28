import { Anime } from "../../domain";

export interface AnimeTableSate {
    isLoading: boolean;
    delayTimer: number | null;
    search: string;
    number: number;
    size: number;
    totalElements: number;
    content: Anime[];
}
