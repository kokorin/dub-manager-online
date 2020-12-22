import {AnimeControllerApi, Configuration} from "./generated";
import Page from "../../domain/Page";
import Anime from "../../domain/Anime";

const animeControllerApi = new AnimeControllerApi(new Configuration({
    basePath: ""
}))

export const getAnimeList = (page: number, size: number, title?: string): Promise<Page<Anime>> => {
    return animeControllerApi.findAllUsingGET({page, size, title})
}