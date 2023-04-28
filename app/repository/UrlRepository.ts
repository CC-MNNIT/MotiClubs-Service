import { OkPacket, QueryError, RowDataPacket } from "mysql2";
import { db } from "../db/db";
import { URL } from "../models/Url";

const getUrls = async (clubId: number): Promise<URL[]> => new Promise((resolve, reject) => {
    db.query(
        "SELECT urlid as urlId, cid, name, color, url FROM url WHERE cid=?",
        [clubId],
        (error, result) => {
            const urls: URL[] = [];
            if (error) {
                reject(error);
                return;
            }
            (<RowDataPacket[]>result).forEach(row => urls.push({
                urlId: row.urlId,
                cid: row.cid,
                name: row.name,
                color: row.color,
                url: row.url
            }));
            resolve(urls);
        }
    );
});

const saveUrl = async (clubId: number, url: URL): Promise<number> => new Promise((resolve, reject) => {
    db.query(
        "INSERT INTO url (urlid, cid, name, color, url) VALUES (?, ?, ?, ?, ?)",
        [url.urlId, clubId, url.name, url.color, url.url],
        (error, result) => {
            if (error) {
                reject(error);
                return;
            }
            resolve((<OkPacket>result).insertId);
        }
    );
});

const updateUrl = async (urlId: number, url: URL): Promise<void> => new Promise((resolve, reject) => {
    db.query(
        "UPDATE url SET name=?, color=?, url=? WHERE urlid=?",
        [url.name, url.color, url.url, urlId],
        (error, _) => {
            if (error) {
                reject(error);
                return;
            }
            resolve();
        }
    );
});

const deleteUrl = async (urlId: number): Promise<void> => new Promise((resolve, reject) => {
    db.query(
        "DELETE FROM url WHERE urlid=?",
        [urlId],
        (error, _) => {
            if (error) {
                reject(error);
                return;
            }
            resolve();
        }
    );
});

export const UrlRepository = {
    getUrls,
    saveUrl,
    updateUrl,
    deleteUrl
};
