export interface Account {
    email: string;
    name: string;
    picture: string;
    locale: string;
}

export interface User extends Partial<Account> {
    clientId: string | undefined;
    tokenId: string | undefined;
}
