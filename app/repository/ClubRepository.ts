import { OkPacket, RowDataPacket } from "mysql2";
import { db } from "../db/db";
import { Club } from "../models/Club";

const getClubByClubId = async (clubId: number): Promise<Club> => new Promise((resolve, reject) => {
    db.query(
        "SELECT * FROM club WHERE cid=?",
        [clubId],
        (error, result) => {
            if (error) {
                reject(error);
                return;
            }
            const row = (<RowDataPacket[]>result)[0];
            resolve({
                cid: row.cid,
                name: row.name,
                description: row.description,
                avatar: row.avatar,
                summary: row.summary
            });
        }
    );
});

const getAllClubs = async (): Promise<Club[]> => new Promise((resolve, reject) => {
    db.query(
        "SELECT * FROM club",
        [],
        (error, result) => {
            const clubs: Club[] = [];
            if (error) {
                reject(error);
                return;
            }

            (<RowDataPacket[]>result).forEach(row => clubs.push({
                cid: row.cid,
                name: row.name,
                description: row.description,
                avatar: row.avatar,
                summary: row.summary
            }));
            resolve(clubs);
        }
    );
});

const saveClub = async (club: Club): Promise<number> => new Promise((resolve, reject) => {
    db.query(
        "INSERT INTO club (name, description, avatar, summary) VALUES (?,?,?,?)",
        [club.name, club.description, club.avatar, club.summary],
        (error, result) => {
            if (error) {
                reject(error);
                return;
            }
            resolve((<OkPacket>result).insertId);
        }
    );
});

const deleteClubByCid = async (clubId: number): Promise<void> => new Promise((resolve, reject) => {
    db.query(
        "DELETE FROM club WHERE cid=?",
        [clubId],
        (error, _) => {
            if (error) {
                reject(error);
                return;
            }
            resolve();
        }
    );
});

const updateClubByCid = async (cid: number, description: string, avatar: string, summary: string): Promise<void> => new Promise((resolve, reject) => {
    db.query(
        "UPDATE club SET description=?, avatar=?, summary=? WHERE cid=?",
        [description, avatar, summary, cid],
        (error, _) => {
            if (error) {
                reject(error);
                return;
            }
            resolve();
        }
    );
});

const clubExists = async (clubId: number): Promise<boolean> => new Promise((resolve, reject) => {
    db.query(
        "SELECT * FROM club WHERE cid=?",
        [clubId],
        (error, result) => {
            if (error) {
                reject(error);
                return;
            }
            resolve((<RowDataPacket[]>result).length > 0);
        }
    );
});

export const ClubRepository = {
    getClubByClubId,
    getAllClubs,
    saveClub,
    deleteClubByCid,
    updateClubByCid,
    clubExists,
};
