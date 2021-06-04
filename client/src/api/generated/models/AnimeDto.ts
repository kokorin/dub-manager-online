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
    AnimeTitleDto,
    AnimeTitleDtoFromJSON,
    AnimeTitleDtoFromJSONTyped,
    AnimeTitleDtoToJSON,
} from './';

/**
 * 
 * @export
 * @interface AnimeDto
 */
export interface AnimeDto {
    /**
     * 
     * @type {number}
     * @memberof AnimeDto
     */
    id: number;
    /**
     * 
     * @type {Array<AnimeTitleDto>}
     * @memberof AnimeDto
     */
    titles: Array<AnimeTitleDto>;
    /**
     * 
     * @type {string}
     * @memberof AnimeDto
     */
    type: AnimeDtoTypeEnum;
}

export function AnimeDtoFromJSON(json: any): AnimeDto {
    return AnimeDtoFromJSONTyped(json, false);
}

export function AnimeDtoFromJSONTyped(json: any, ignoreDiscriminator: boolean): AnimeDto {
    if ((json === undefined) || (json === null)) {
        return json;
    }
    return {
        
        'id': json['id'],
        'titles': ((json['titles'] as Array<any>).map(AnimeTitleDtoFromJSON)),
        'type': json['type'],
    };
}

export function AnimeDtoToJSON(value?: AnimeDto | null): any {
    if (value === undefined) {
        return undefined;
    }
    if (value === null) {
        return null;
    }
    return {
        
        'id': value.id,
        'titles': ((value.titles as Array<any>).map(AnimeTitleDtoToJSON)),
        'type': value.type,
    };
}

/**
* @export
* @enum {string}
*/
export enum AnimeDtoTypeEnum {
    MOVIE = 'MOVIE',
    MUSICVIDEO = 'MUSIC_VIDEO',
    OTHER = 'OTHER',
    OVA = 'OVA',
    TVSERIES = 'TV_SERIES',
    TVSPECIAL = 'TV_SPECIAL',
    UNKNOWN = 'UNKNOWN',
    WEB = 'WEB'
}

