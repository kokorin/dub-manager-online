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
    UserDto,
    UserDtoFromJSON,
    UserDtoToJSON,
} from '../models';

/**
 * 
 */
export class UserControllerApi extends runtime.BaseAPI {

    /**
     * getUser
     */
    async getUserUsingGETRaw(): Promise<runtime.ApiResponse<UserDto>> {
        const queryParameters: runtime.HTTPQuery = {};

        const headerParameters: runtime.HTTPHeaders = {};

        const response = await this.request({
            path: `/api/v1/users/current`,
            method: 'GET',
            headers: headerParameters,
            query: queryParameters,
        });

        return new runtime.JSONApiResponse(response, (jsonValue) => UserDtoFromJSON(jsonValue));
    }

    /**
     * getUser
     */
    async getUserUsingGET(): Promise<UserDto> {
        const response = await this.getUserUsingGETRaw();
        return await response.value();
    }

}