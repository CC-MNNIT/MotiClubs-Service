import { OkPacket, RowDataPacket } from "mysql2";
import { db } from "../db/db";
import { Post } from "../models/Post";

const getPostByPostId = async (postId: number): Promise<Post> => new Promise((resolve, reject) => {
    db.query(
        "SELECT * FROM post WHERE pid=?",
        [postId],
        (error, result) => {
            if (error) {
                reject(error);
                return;
            }

            const row = (<RowDataPacket[]>result)[0];
            resolve({ pid: row.pid, chid: row.chid, uid: row.uid, message: row.message, time: row.time, general: row.general });
        }
    );
});

const getPostsByClubAndChannel = async (channelId: number, page: number, items: number): Promise<Post[]> => new Promise((resolve, reject) => {
    db.query(
        "SELECT * FROM post WHERE chid=? ORDER BY time DESC LIMIT ? OFFSET ?",
        [channelId, items, (page - 1) * items],
        (error, result) => {
            const posts: Post[] = [];
            if (error) {
                reject(error);
                return;
            }

            (<RowDataPacket[]>result).forEach(row => posts.push({
                pid: row.pid,
                chid: row.chid,
                uid: row.uid,
                message: row.message,
                time: row.time,
                general: row.general
            }));
            resolve(posts);
        }
    );
});

const updatePostByPostId = async (postId: number, message: string): Promise<void> => new Promise((resolve, reject) => {
    db.query(
        "UPDATE post SET message=? WHERE pid=?",
        [message, postId],
        (error, _) => {
            if (error) {
                reject(error);
                return;
            }
            resolve();
        }
    );
});

const detelePostByPostId = async (postId: number): Promise<void> => new Promise((resolve, reject) => {
    db.query(
        "DELETE FROM post where pid=?",
        [postId],
        (error, _) => {
            if (error) {
                reject(error);
                return;
            }
            resolve();
        }
    );
});

const savePost = async (post: Post): Promise<number> => new Promise((resolve, reject) => {
    db.query(
        "INSERT INTO post (pid, chid, message, time, uid, general) VALUES (?,?,?,?,?,?)",
        [post.pid, post.chid, post.message, post.time, post.uid, post.general],
        (error, result) => {
            if (error) {
                reject(error);
                return;
            }
            resolve((<OkPacket>result).insertId);
        }
    );
});

const deletePostsByChannelId = async (channelId: number): Promise<void> => new Promise((resolve, reject) => {
    db.query(
        "DELETE FROM post WHERE chid=?",
        [channelId],
        (error, _) => {
            if (error) {
                reject(error);
                return;
            }
            resolve();
        }
    );
});

export const PostRepository = {
    getPostByPostId,
    getPostsByClubAndChannel,
    updatePostByPostId,
    detelePostByPostId,
    savePost,
    deletePostsByChannelId
};
