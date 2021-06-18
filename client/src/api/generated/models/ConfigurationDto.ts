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
/**
 * 
 * @export
 * @interface ConfigurationDto
 */
export interface ConfigurationDto {
    /**
     * 
     * @type {string}
     * @memberof ConfigurationDto
     */
    googleOAuthClientId?: string;
}

export function ConfigurationDtoFromJSON(json: any): ConfigurationDto {
    return ConfigurationDtoFromJSONTyped(json, false);
}

export function ConfigurationDtoFromJSONTyped(json: any, ignoreDiscriminator: boolean): ConfigurationDto {
    if ((json === undefined) || (json === null)) {
        return json;
    }
    return {
        
        'googleOAuthClientId': !exists(json, 'googleOAuthClientId') ? undefined : json['googleOAuthClientId'],
    };
}

export function ConfigurationDtoToJSON(value?: ConfigurationDto | null): any {
    if (value === undefined) {
        return undefined;
    }
    if (value === null) {
        return null;
    }
    return {
        
        'googleOAuthClientId': value.googleOAuthClientId,
    };
}


