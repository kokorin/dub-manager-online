import {AnimeControllerApi, Configuration, ConfigurationControllerApi} from "./generated";
import {Anime, Config, Episode, Page} from "../domain";

const catchError = (response: Response): any => {
    console.log("error: " + response)
    if (response.status === 401) {
        const redirect = encodeURIComponent(window.location.pathname)
        window.location.href = `/login?redirect=${redirect}`
    }
    //show "something happened
}

// openapi description specifies 8080 port
// this configuration overrides port
const controllerConfiguration = new Configuration({
    basePath: ""
})

const animeControllerApi = new AnimeControllerApi(controllerConfiguration)

/**
 * @param page 0-based page number
 * @param size page size
 * @param title anime title to search for
 */
export const getAnimeList = (page: number, size: number, title?: string): Promise<Page<Anime>> => {
    return animeControllerApi.findAllUsingGET({page, size, title}).catch(catchError)
}

/**
 * @param id anime ID
 */
export const getAnime = (id: number): Promise<Anime> => {
    return animeControllerApi.getAnimeUsingGET({id}).catch(catchError)
}

/**
 * @param id anime ID
 * @param page 0-based page number
 * @param size page size
 */
export const getEpisodeList = (id: number, page: number, size: number): Promise<Page<Episode>> => {
    return animeControllerApi.getEpisodesUsingGET({id, page, size}).catch(catchError)
}

const confControllerApi = new ConfigurationControllerApi(controllerConfiguration)

export const getConfig = (): Promise<Config> => {
    return confControllerApi.getConfigurationUsingGET();
}