/* tslint:disable */
/* eslint-disable */
/**
 * Api Documentation
 * Api Documentation
 *
 * The version of the OpenAPI document: 1.0
 * 
 *
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */


import * as runtime from '../runtime';
import {
    AnimeStatusDto,
    AnimeStatusDtoFromJSON,
    AnimeStatusDtoToJSON,
    PageDtoOfAnimeStatusDto,
    PageDtoOfAnimeStatusDtoFromJSON,
    PageDtoOfAnimeStatusDtoToJSON,
    UpdateAnimeStatusDto,
    UpdateAnimeStatusDtoFromJSON,
    UpdateAnimeStatusDtoToJSON,
} from '../models';

export interface FindAllUsingGET1Request {
    page: number;
    size: number;
}

export interface UpdateStatusUsingPOSTRequest {
    animeId: number;
    updateAnimeStatusDto: UpdateAnimeStatusDto;
}

/**
 * 
 */
export class AnimeStatusControllerApi extends runtime.BaseAPI {

    /**
     * findAll
     */
    async findAllUsingGET1Raw(requestParameters: FindAllUsingGET1Request): Promise<runtime.ApiResponse<PageDtoOfAnimeStatusDto>> {
        if (requestParameters.page === null || requestParameters.page === undefined) {
            throw new runtime.RequiredError('page','Required parameter requestParameters.page was null or undefined when calling findAllUsingGET1.');
        }

        if (requestParameters.size === null || requestParameters.size === undefined) {
            throw new runtime.RequiredError('size','Required parameter requestParameters.size was null or undefined when calling findAllUsingGET1.');
        }

        const queryParameters: runtime.HTTPQuery = {};

        if (requestParameters.page !== undefined) {
            queryParameters['page'] = requestParameters.page;
        }

        if (requestParameters.size !== undefined) {
            queryParameters['size'] = requestParameters.size;
        }

        const headerParameters: runtime.HTTPHeaders = {};

        const response = await this.request({
            path: `/api/v1/anime_status`,
            method: 'GET',
            headers: headerParameters,
            query: queryParameters,
        });

        return new runtime.JSONApiResponse(response, (jsonValue) => PageDtoOfAnimeStatusDtoFromJSON(jsonValue));
    }

    /**
     * findAll
     */
    async findAllUsingGET1(requestParameters: FindAllUsingGET1Request): Promise<PageDtoOfAnimeStatusDto> {
        const response = await this.findAllUsingGET1Raw(requestParameters);
        return await response.value();
    }

    /**
     * updateStatus
     */
    async updateStatusUsingPOSTRaw(requestParameters: UpdateStatusUsingPOSTRequest): Promise<runtime.ApiResponse<AnimeStatusDto>> {
        if (requestParameters.animeId === null || requestParameters.animeId === undefined) {
            throw new runtime.RequiredError('animeId','Required parameter requestParameters.animeId was null or undefined when calling updateStatusUsingPOST.');
        }

        if (requestParameters.updateAnimeStatusDto === null || requestParameters.updateAnimeStatusDto === undefined) {
            throw new runtime.RequiredError('updateAnimeStatusDto','Required parameter requestParameters.updateAnimeStatusDto was null or undefined when calling updateStatusUsingPOST.');
        }

        const queryParameters: runtime.HTTPQuery = {};

        const headerParameters: runtime.HTTPHeaders = {};

        headerParameters['Content-Type'] = 'application/json';

        const response = await this.request({
            path: `/api/v1/anime_status/{animeId}`.replace(`{${"animeId"}}`, encodeURIComponent(String(requestParameters.animeId))),
            method: 'POST',
            headers: headerParameters,
            query: queryParameters,
            body: UpdateAnimeStatusDtoToJSON(requestParameters.updateAnimeStatusDto),
        });

        return new runtime.JSONApiResponse(response, (jsonValue) => AnimeStatusDtoFromJSON(jsonValue));
    }

    /**
     * updateStatus
     */
    async updateStatusUsingPOST(requestParameters: UpdateStatusUsingPOSTRequest): Promise<AnimeStatusDto> {
        const response = await this.updateStatusUsingPOSTRaw(requestParameters);
        return await response.value();
    }

}
