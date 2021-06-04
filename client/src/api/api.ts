import {AnimeControllerApi, Configuration} from "./generated";
import {Anime, Page} from "../domain";

const animeControllerApi = new AnimeControllerApi(new Configuration({
    basePath: ""
}))

export const getAnimeList = (page: number, size: number, title?: string): Promise<Page<Anime>> => {
    return animeControllerApi.findAllUsingGET({page, size, title})
}
export const getAnime = (id: number): Promise<Anime> => {
    return animeControllerApi.getAnimeUsingGET({id})
}