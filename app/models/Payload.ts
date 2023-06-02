export enum Type {
    POST = "0", DELTE_POST = "1", REPLY = "2", DELETE_REPLY = "3"
}

export interface Payload {
    type: Type;
    [key: string]: string;
};
