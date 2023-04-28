import { OkPacket, QueryError, RowDataPacket } from "mysql2";
import { db } from "../db/db";
import { Subscriber } from "../models/Subscriber";

const subscribe = async (subscriber: Subscriber): Promise<number> => new Promise((resolve, reject) => {
    db.query(
        "INSERT INTO subscribers (uid, cid) VALUES (?,?)",
        [subscriber.uid, subscriber.cid],
        (error, result) => {
            if (error) {
                reject(error);
                return;
            }
            resolve((<OkPacket>result).insertId);
        }
    )
});

const unsubscribe = async (subscriber: Subscriber): Promise<void> => new Promise((resolve, reject) => {
    db.query(
        "DELETE FROM subscribers where uid=? AND cid=?",
        [subscriber.uid, subscriber.cid],
        (error, _) => {
            if (error) {
                reject(error);
                return;
            }
            resolve();
        }
    )
});

const getSubscribersByCid = async (clubId: number): Promise<Subscriber[]> => new Promise((resolve, reject) => {
    db.query(
        "SELECT * FROM subscribers WHERE cid=?",
        [clubId],
        (error, result) => {
            const subscribers: Subscriber[] = []
            if (error) {
                reject(error);
                return;
            }

            (<RowDataPacket[]>result).forEach(row => subscribers.push({ uid: row.uid, cid: row.cid }));
            resolve(subscribers);
        }
    );
});

const getSubscribedClubsByUid = async (userId: number): Promise<Subscriber[]> => new Promise((resolve, reject) => {
    db.query(
        "SELECT * FROM subscribers WHERE uid=?",
        [userId],
        (error, result) => {
            const subscribedClubs: Subscriber[] = []
            if (error) {
                reject(error);
                return;
            }

            (<RowDataPacket[]>result).forEach(row => subscribedClubs.push({ uid: row.uid, cid: row.cid }));
            resolve(subscribedClubs);
        }
    );
});

export const SubscribersRepository = {
    subscribe,
    unsubscribe,
    getSubscribersByCid,
    getSubscribedClubsByUid,
};
