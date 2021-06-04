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

import { exists, mapValues } from '../runtime';
import {
    AnimeDto,
    AnimeDtoFromJSON,
    AnimeDtoFromJSONTyped,
    AnimeDtoToJSON,
} from './';

/**
 * 
 * @export
 * @interface PageDtoOfAnimeDto
 */
export interface PageDtoOfAnimeDto {
    /**
     * 
     * @type {Array<AnimeDto>}
     * @memberof PageDtoOfAnimeDto
     */
    content: Array<AnimeDto>;
    /**
     * 
     * @type {number}
     * @memberof PageDtoOfAnimeDto
     */
    number: number;
    /**
     * 
     * @type {number}
     * @memberof PageDtoOfAnimeDto
     */
    numberOfElements: number;
    /**
     * 
     * @type {number}
     * @memberof PageDtoOfAnimeDto
     */
    size: number;
    /**
     * 
     * @type {number}
     * @memberof PageDtoOfAnimeDto
     */
    totalElements: number;
    /**
     * 
     * @type {number}
     * @memberof PageDtoOfAnimeDto
     */
    totalPages: number;
}

export function PageDtoOfAnimeDtoFromJSON(json: any): PageDtoOfAnimeDto {
    return PageDtoOfAnimeDtoFromJSONTyped(json, false);
}

export function PageDtoOfAnimeDtoFromJSONTyped(json: any, ignoreDiscriminator: boolean): PageDtoOfAnimeDto {
    if ((json === undefined) || (json === null)) {
        return json;
    }
    return {
        
        'content': ((json['content'] as Array<any>).map(AnimeDtoFromJSON)),
        'number': json['number'],
        'numberOfElements': json['numberOfElements'],
        'size': json['size'],
        'totalElements': json['totalElements'],
        'totalPages': json['totalPages'],
    };
}

export function PageDtoOfAnimeDtoToJSON(value?: PageDtoOfAnimeDto | null): any {
    if (value === undefined) {
        return undefined;
    }
    if (value === null) {
        return null;
    }
    return {
        
        'content': ((value.content as Array<any>).map(AnimeDtoToJSON)),
        'number': value.number,
        'numberOfElements': value.numberOfElements,
        'size': value.size,
        'totalElements': value.totalElements,
        'totalPages': value.totalPages,
    };
}

