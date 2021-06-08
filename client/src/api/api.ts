import {AnimeControllerApi, Configuration} from "./generated";
import {Anime, Episode, Page} from "../domain";

const animeControllerApi = new AnimeControllerApi(new Configuration({
    basePath: ""
}))

/**
 * @param page 0-based page number
 * @param size page size
 * @param title anime title to search for
 */
export const getAnimeList = (page: number, size: number, title?: string): Promise<Page<Anime>> => {
    return animeControllerApi.findAllUsingGET({page, size, title})
}

/**
 * @param id anime ID
 */
export const getAnime = (id: number): Promise<Anime> => {
    return animeControllerApi.getAnimeUsingGET({id})
}

/**
 * @param id anime ID
 * @param page 0-based page number
 * @param size page size
 */
export const getEpisodeList = (id: number, page: number, size: number): Promise<Page<Episode>> => {
    return animeControllerApi.getEpisodesUsingGET({id, page, size})
}