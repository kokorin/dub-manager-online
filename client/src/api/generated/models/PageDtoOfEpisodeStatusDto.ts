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
    EpisodeStatusDto,
    EpisodeStatusDtoFromJSON,
    EpisodeStatusDtoFromJSONTyped,
    EpisodeStatusDtoToJSON,
} from './';

/**
 * 
 * @export
 * @interface PageDtoOfEpisodeStatusDto
 */
export interface PageDtoOfEpisodeStatusDto {
    /**
     * 
     * @type {Array<EpisodeStatusDto>}
     * @memberof PageDtoOfEpisodeStatusDto
     */
    content: Array<EpisodeStatusDto>;
    /**
     * 
     * @type {number}
     * @memberof PageDtoOfEpisodeStatusDto
     */
    number: number;
    /**
     * 
     * @type {number}
     * @memberof PageDtoOfEpisodeStatusDto
     */
    numberOfElements: number;
    /**
     * 
     * @type {number}
     * @memberof PageDtoOfEpisodeStatusDto
     */
    size: number;
    /**
     * 
     * @type {number}
     * @memberof PageDtoOfEpisodeStatusDto
     */
    totalElements: number;
    /**
     * 
     * @type {number}
     * @memberof PageDtoOfEpisodeStatusDto
     */
    totalPages: number;
}

export function PageDtoOfEpisodeStatusDtoFromJSON(json: any): PageDtoOfEpisodeStatusDto {
    return PageDtoOfEpisodeStatusDtoFromJSONTyped(json, false);
}

export function PageDtoOfEpisodeStatusDtoFromJSONTyped(json: any, ignoreDiscriminator: boolean): PageDtoOfEpisodeStatusDto {
    if ((json === undefined) || (json === null)) {
        return json;
    }
    return {
        
        'content': ((json['content'] as Array<any>).map(EpisodeStatusDtoFromJSON)),
        'number': json['number'],
        'numberOfElements': json['numberOfElements'],
        'size': json['size'],
        'totalElements': json['totalElements'],
        'totalPages': json['totalPages'],
    };
}

export function PageDtoOfEpisodeStatusDtoToJSON(value?: PageDtoOfEpisodeStatusDto | null): any {
    if (value === undefined) {
        return undefined;
    }
    if (value === null) {
        return null;
    }
    return {
        
        'content': ((value.content as Array<any>).map(EpisodeStatusDtoToJSON)),
        'number': value.number,
        'numberOfElements': value.numberOfElements,
        'size': value.size,
        'totalElements': value.totalElements,
        'totalPages': value.totalPages,
    };
}

