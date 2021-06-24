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
    EpisodeDto,
    EpisodeDtoFromJSON,
    EpisodeDtoFromJSONTyped,
    EpisodeDtoToJSON,
} from './';

/**
 * 
 * @export
 * @interface EpisodeStatusDto
 */
export interface EpisodeStatusDto {
    /**
     * 
     * @type {EpisodeDto}
     * @memberof EpisodeStatusDto
     */
    episode: EpisodeDto;
    /**
     * 
     * @type {string}
     * @memberof EpisodeStatusDto
     */
    progress: EpisodeStatusDtoProgressEnum;
}

export function EpisodeStatusDtoFromJSON(json: any): EpisodeStatusDto {
    return EpisodeStatusDtoFromJSONTyped(json, false);
}

export function EpisodeStatusDtoFromJSONTyped(json: any, ignoreDiscriminator: boolean): EpisodeStatusDto {
    if ((json === undefined) || (json === null)) {
        return json;
    }
    return {
        
        'episode': EpisodeDtoFromJSON(json['episode']),
        'progress': json['progress'],
    };
}

export function EpisodeStatusDtoToJSON(value?: EpisodeStatusDto | null): any {
    if (value === undefined) {
        return undefined;
    }
    if (value === null) {
        return null;
    }
    return {
        
        'episode': EpisodeDtoToJSON(value.episode),
        'progress': value.progress,
    };
}

/**
* @export
* @enum {string}
*/
export enum EpisodeStatusDtoProgressEnum {
    COMPLETED = 'COMPLETED',
    NOTSTARTED = 'NOT_STARTED'
}


