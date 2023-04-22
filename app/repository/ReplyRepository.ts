import { OkPacket, RowDataPacket } from "mysql2";
import { db } from "../db/db";
import { Reply } from "../models/Reply";

const saveReply = async (reply: Reply): Promise<number> => new Promise((resolve, reject) => {
    db.query(
        "INSERT INTO reply(pid, uid, to_uid, message, time) VALUES (?,?,?,?,?)",
        [
            reply.pid,
            reply.uid,
            reply.to_uid,
            reply.message,
            reply.time,
        ],
        (error, result) => {
            if (error) {
                reject(error);
                return;
            }
            resolve((<OkPacket>result).insertId);
        }
    );
});

const getRepliesByPid = async (postId: number): Promise<Reply[]> => new Promise((resolve, reject) => {
    db.query(
        "SELECT * FROM reply WHERE pid=?",
        [postId],
        (error, result) => {
            if (error) {
                reject(error);
                return;
            }
            const replies: Reply[] = [];
            (<RowDataPacket[]>result).forEach(row => replies.push({
                pid: row.pid,
                uid: row.uid,
                to_uid: row.to_uid,
                message: row.message,
                time: row.time,
            }));
            resolve(replies);
        }
    );
});

const getReplyByTime = async (time: number): Promise<Reply> => new Promise((resolve, reject) => {
    db.query(
        "SELECT * FROM reply WHERE time=?",
        [time],
        (error, result) => {
            if (error) {
                reject(error);
                return;
            }
            const row = (<RowDataPacket[]>result)[0];
            resolve({
                pid: row.pid,
                uid: row.uid,
                to_uid: row.to_uid,
                message: row.message,
                time: row.time,
            });
        }
    );
});

const deleteReply = async (replyID: number): Promise<void> => new Promise((resolve, reject) => {
    db.query(
        "DELETE FROM reply WHERE time=?",
        [replyID],
        (error, _) => {
            if (error) {
                reject(error);
                return;
            }
            resolve();
        }
    );
});

export const ReplyRepository = {
    saveReply,
    getRepliesByPid,
    getReplyByTime,
    deleteReply,
};
