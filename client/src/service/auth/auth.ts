import axios from "axios";
import qs from "qs";
import { Token } from "./types";

export const loginUser = async (tokenId: string): Promise<Token> => {
    try {
        const { data } = await axios.post<Token>("/login/google", qs.stringify({ id_token: tokenId }));
        return data;
    } catch (err) {
        throw Error(err.message);
    }
};
