import { baseApi as api } from "./base";
const injectedRtkApi = api.injectEndpoints({
    endpoints: (build) => ({
        findAnimeStatus: build.query<FindAnimeStatusApiResponse, FindAnimeStatusApiArg>({
            query: (queryArg) => ({
                url: `/api/v1/users/current/anime/${queryArg.id}`,
                params: { user: queryArg.user },
            }),
        }),
        updateAnimeStatus: build.mutation<UpdateAnimeStatusApiResponse, UpdateAnimeStatusApiArg>({
            query: (queryArg) => ({
                url: `/api/v1/users/current/anime/${queryArg.id}`,
                method: "POST",
                body: queryArg.body,
            }),
        }),
        deleteAnimeStatus: build.mutation<DeleteAnimeStatusApiResponse, DeleteAnimeStatusApiArg>({
            query: (queryArg) => ({
                url: `/api/v1/users/current/anime/${queryArg.id}`,
                method: "DELETE",
                body: queryArg.oidcUser,
            }),
        }),
        updateEpisodeStatus: build.mutation<UpdateEpisodeStatusApiResponse, UpdateEpisodeStatusApiArg>({
            query: (queryArg) => ({
                url: `/api/v1/users/current/anime/${queryArg.id}/episodes/${queryArg.eid}`,
                method: "POST",
                body: queryArg.body,
            }),
        }),
        getCurrentUser: build.query<GetCurrentUserApiResponse, GetCurrentUserApiArg>({
            query: (queryArg) => ({ url: `/api/v1/users/current`, params: { user: queryArg.user } }),
        }),
        findAnimeStatuses: build.query<FindAnimeStatusesApiResponse, FindAnimeStatusesApiArg>({
            query: (queryArg) => ({
                url: `/api/v1/users/current/anime`,
                params: { user: queryArg.user, page: queryArg.page, size: queryArg.size },
            }),
        }),
        findEpisodeStatuses: build.query<FindEpisodeStatusesApiResponse, FindEpisodeStatusesApiArg>({
            query: (queryArg) => ({
                url: `/api/v1/users/current/anime/${queryArg.id}/episodes`,
                params: { user: queryArg.user, page: queryArg.page, size: queryArg.size, type: queryArg["type"] },
            }),
        }),
        getOAuthClients: build.query<GetOAuthClientsApiResponse, GetOAuthClientsApiArg>({
            query: () => ({ url: `/api/v1/conf/oauth/clients` }),
        }),
        findAnime: build.query<FindAnimeApiResponse, FindAnimeApiArg>({
            query: (queryArg) => ({
                url: `/api/v1/anime`,
                params: { title: queryArg.title, page: queryArg.page, size: queryArg.size },
            }),
        }),
        getAnime: build.query<GetAnimeApiResponse, GetAnimeApiArg>({
            query: (queryArg) => ({ url: `/api/v1/anime/${queryArg.id}` }),
        }),
        findEpisodes: build.query<FindEpisodesApiResponse, FindEpisodesApiArg>({
            query: (queryArg) => ({
                url: `/api/v1/anime/${queryArg.id}/episodes`,
                params: { page: queryArg.page, size: queryArg.size },
            }),
        }),
    }),
    overrideExisting: false,
});
export { injectedRtkApi as generatedApi };
export type FindAnimeStatusApiResponse = /** status 200 OK */ AnimeStatusDto;
export type FindAnimeStatusApiArg = {
    user: OidcUser;
    id: number;
};
export type UpdateAnimeStatusApiResponse = /** status 200 OK */ AnimeStatusDto;
export type UpdateAnimeStatusApiArg = {
    id: number;
    body: {
        user?: OidcUser;
        updateAnimeStatusDto?: UpdateAnimeStatusDto;
    };
};
export type DeleteAnimeStatusApiResponse = /** status 200 OK */ undefined;
export type DeleteAnimeStatusApiArg = {
    id: number;
    oidcUser: OidcUser;
};
export type UpdateEpisodeStatusApiResponse = /** status 200 OK */ EpisodeStatusDto;
export type UpdateEpisodeStatusApiArg = {
    id: number;
    eid: number;
    body: {
        user?: OidcUser;
        updateEpisodeStatusDto?: UpdateEpisodeStatusDto;
    };
};
export type GetCurrentUserApiResponse = /** status 200 OK */ UserDto;
export type GetCurrentUserApiArg = {
    user: OidcUser;
};
export type FindAnimeStatusesApiResponse = /** status 200 OK */ PageDtoAnimeStatusDto;
export type FindAnimeStatusesApiArg = {
    user: OidcUser;
    page: number;
    size: number;
};
export type FindEpisodeStatusesApiResponse = /** status 200 OK */ PageDtoEpisodeStatusDto;
export type FindEpisodeStatusesApiArg = {
    user: OidcUser;
    id: number;
    page: number;
    size: number;
    type?: "REGULAR" | "SPECIAL" | "CREDIT" | "TRAILER" | "PARODY" | "OTHER";
};
export type GetOAuthClientsApiResponse = /** status 200 OK */ string[];
export type GetOAuthClientsApiArg = void;
export type FindAnimeApiResponse = /** status 200 OK */ PageDtoAnimeDto;
export type FindAnimeApiArg = {
    title?: string;
    page: number;
    size: number;
};
export type GetAnimeApiResponse = /** status 200 OK */ AnimeDto;
export type GetAnimeApiArg = {
    id: number;
};
export type FindEpisodesApiResponse = /** status 200 OK */ PageDtoEpisodeDto;
export type FindEpisodesApiArg = {
    id: number;
    page: number;
    size: number;
};
export type AnimeTitleDto = {
    type: "SHORT, OFFICIAL, SYNONYM, MAIN, CARD, KANA";
    lang: string;
    text: string;
};
export type AnimeDto = {
    id: number;
    titles: AnimeTitleDto[];
    type: "MOVIE" | "OVA" | "TV_SERIES" | "TV_SPECIAL" | "WEB" | "MUSIC_VIDEO" | "OTHER" | "UNKNOWN" | "DELETED";
};
export type AnimeStatusDto = {
    anime: AnimeDto;
    progress: "NOT_STARTED" | "IN_PROGRESS" | "COMPLETED";
    comment: string;
    regularEpisodeCompleteCount: number;
    regularEpisodeTotalCount: number;
    regularEpisodeNextAirDate?: string;
};
export type ErrorDto = {
    message: string;
};
export type AddressStandardClaim = {
    country?: string;
    region?: string;
    formatted?: string;
    postalCode?: string;
    streetAddress?: string;
    locality?: string;
};
export type OidcUserInfo = {
    claims?: {
        [key: string]: object;
    };
    address?: AddressStandardClaim;
    fullName?: string;
    locale?: string;
    zoneInfo?: string;
    givenName?: string;
    familyName?: string;
    middleName?: string;
    picture?: string;
    nickName?: string;
    email?: string;
    phoneNumberVerified?: boolean;
    subject?: string;
    preferredUsername?: string;
    emailVerified?: boolean;
    phoneNumber?: string;
    birthdate?: string;
    updatedAt?: string;
    profile?: string;
    gender?: string;
    website?: string;
};
export type OidcIdToken = {
    tokenValue?: string;
    issuedAt?: string;
    expiresAt?: string;
    claims?: {
        [key: string]: object;
    };
    authorizationCodeHash?: string;
    authenticationContextClass?: string;
    authenticationMethods?: string[];
    subject?: string;
    audience?: string[];
    nonce?: string;
    authenticatedAt?: string;
    accessTokenHash?: string;
    authorizedParty?: string;
    issuer?: string;
    address?: AddressStandardClaim;
    fullName?: string;
    locale?: string;
    zoneInfo?: string;
    givenName?: string;
    familyName?: string;
    middleName?: string;
    picture?: string;
    nickName?: string;
    email?: string;
    phoneNumberVerified?: boolean;
    preferredUsername?: string;
    emailVerified?: boolean;
    phoneNumber?: string;
    birthdate?: string;
    updatedAt?: string;
    profile?: string;
    gender?: string;
    website?: string;
};
export type GrantedAuthority = {
    authority?: string;
};
export type OidcUser = {
    userInfo?: OidcUserInfo;
    claims?: {
        [key: string]: object;
    };
    idToken?: OidcIdToken;
    attributes?: {
        [key: string]: object;
    };
    authorities?: GrantedAuthority[];
    name?: string;
    authorizationCodeHash?: string;
    authenticationContextClass?: string;
    authenticationMethods?: string[];
    subject?: string;
    audience?: string[];
    nonce?: string;
    issuedAt?: string;
    authenticatedAt?: string;
    accessTokenHash?: string;
    authorizedParty?: string;
    expiresAt?: string;
    issuer?: string;
    address?: AddressStandardClaim;
    fullName?: string;
    locale?: string;
    zoneInfo?: string;
    givenName?: string;
    familyName?: string;
    middleName?: string;
    picture?: string;
    nickName?: string;
    email?: string;
    phoneNumberVerified?: boolean;
    preferredUsername?: string;
    emailVerified?: boolean;
    phoneNumber?: string;
    birthdate?: string;
    updatedAt?: string;
    profile?: string;
    gender?: string;
    website?: string;
};
export type UpdateAnimeStatusDto = {
    progress: "NOT_STARTED" | "IN_PROGRESS" | "COMPLETED";
    comment: string;
};
export type EpisodeTitleDto = {
    lang: string;
    text: string;
};
export type EpisodeDto = {
    id: number;
    animeId: number;
    number: number;
    titles: EpisodeTitleDto[];
    type: "REGULAR" | "SPECIAL" | "CREDIT" | "TRAILER" | "PARODY" | "OTHER";
    length: number;
    airDate: string;
};
export type EpisodeStatusDto = {
    episode: EpisodeDto;
    progress: "NOT_STARTED" | "COMPLETED";
};
export type UpdateEpisodeStatusDto = {
    progress: "NOT_STARTED" | "COMPLETED";
};
export type UserDto = {
    email: string;
    fullName: string;
    givenName: string;
    familyName: string;
    middleName: string;
    nickName: string;
    preferredUsername: string;
    picture: string;
    locale: string;
};
export type PageDtoAnimeStatusDto = {
    number: number;
    size: number;
    numberOfElements: number;
    totalPages: number;
    totalElements: number;
    content: AnimeStatusDto[];
};
export type PageDtoEpisodeStatusDto = {
    number: number;
    size: number;
    numberOfElements: number;
    totalPages: number;
    totalElements: number;
    content: EpisodeStatusDto[];
};
export type PageDtoAnimeDto = {
    number: number;
    size: number;
    numberOfElements: number;
    totalPages: number;
    totalElements: number;
    content: AnimeDto[];
};
export type PageDtoEpisodeDto = {
    number: number;
    size: number;
    numberOfElements: number;
    totalPages: number;
    totalElements: number;
    content: EpisodeDto[];
};
export const {
    useFindAnimeStatusQuery,
    useUpdateAnimeStatusMutation,
    useDeleteAnimeStatusMutation,
    useUpdateEpisodeStatusMutation,
    useGetCurrentUserQuery,
    useFindAnimeStatusesQuery,
    useFindEpisodeStatusesQuery,
    useGetOAuthClientsQuery,
    useFindAnimeQuery,
    useGetAnimeQuery,
    useFindEpisodesQuery,
} = injectedRtkApi;
