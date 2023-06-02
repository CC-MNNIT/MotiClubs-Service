import { OkPacket, RowDataPacket } from "mysql2";
import { db } from "../db/db";
import { FCM } from "../models/FCM";

const setTokenByUid = async (fcm: FCM): Promise<number> => new Promise((resolve, reject) => {
    db.query(
        "INSERT INTO fcm (uid, token) VALUES (?,?)",
        [fcm.uid, fcm.token],
        (error, result) => {
            if (error) {
                reject(error);
                return;
            }
            resolve((<OkPacket>result).insertId);
        }
    );
});

const updateTokenByUid = async (fcm: FCM): Promise<void> => new Promise((resolve, reject) => {
    db.query(
        "UPDATE fcm SET token=? WHERE uid=?",
        [fcm.token, fcm.uid],
        (error, _) => {
            if (error) {
                reject(error);
                return;
            }
            resolve();
        }
    );
});

const getAllTokens = async (): Promise<FCM[]> => new Promise((resolve, reject) => {
    db.query(
        "SELECT * FROM fcm",
        (error, result) => {
            const tokens: FCM[] = [];
            if (error) {
                reject(error);
                return;
            }

            (<RowDataPacket[]>result).forEach(row => tokens.push({
                uid: row.uid,
                token: row.token
            }));
            resolve(tokens);
        }
    );
});

const getTokensOfSubscribers = async (clubId: number): Promise<FCM[]> => new Promise((resolve, reject) => {
    db.query(
        "SELECT A.uid, token FROM fcm INNER JOIN (SELECT * FROM subscribers WHERE cid=?) A ON A.uid = fcm.uid",
        [clubId],
        (error, result) => {
            const tokens: FCM[] = [];
            if (error) {
                reject(error);
                return;
            }

            (<RowDataPacket[]>result).forEach(row => tokens.push({
                uid: row.uid,
                token: row.token
            }));
            resolve(tokens);
        }
    )
});

const getTokensOfPostParticipants = async (postId: number): Promise<FCM[]> => new Promise((resolve, reject) => {
    db.query(
        "SELECT DISTINCT(A.uid), token FROM fcm INNER JOIN (SELECT uid FROM reply WHERE pid=?) A ON A.uid = fcm.uid;",
        [postId],
        (error, result) => {
            const tokens: FCM[] = [];
            if (error) {
                reject(error);
                return;
            }

            (<RowDataPacket[]>result).forEach(row => tokens.push({
                uid: row.uid,
                token: row.token
            }));
            resolve(tokens);
        }
    )
});

export const FcmRepository = {
    setTokenByUid,
    updateTokenByUid,
    getAllTokens,
    getTokensOfSubscribers,
    getTokensOfPostParticipants
};
