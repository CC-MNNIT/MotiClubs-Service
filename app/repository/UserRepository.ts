import { OkPacket, RowDataPacket } from "mysql2";
import { db } from "../db/db";
import { AdminUser, User } from "../models/User";

const saveUser = async (user: User): Promise<number> => new Promise((resolve, reject) => {
    db.query(
        "INSERT INTO user(regno, name, email, course, phone, avatar) VALUES (?,?,?,?,?,?)",
        [
            user.regno,
            user.name,
            user.email,
            user.course,
            user.phone,
            user.avatar,
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

const getUserByUid = async (userId: number): Promise<User | undefined> => new Promise((resolve, reject) => {
    db.query(
        "SELECT * FROM user WHERE uid=?",
        [userId],
        (error, result) => {
            if (error) {
                reject(error);
                return;
            }
            const row = (<RowDataPacket[]>result)[0]
            resolve({
                uid: row.uid,
                regno: row.regno,
                name: row.name,
                email: row.email,
                course: row.course,
                phone: row.phone,
                avatar: row.avatar,
            });
        }
    );
});

const getUserByEmail = async (email: string): Promise<User> => new Promise((resolve, reject) => {
    db.query(
        "SELECT * FROM user WHERE email=?",
        [email],
        (error, result) => {
            if (error) {
                reject(error);
                return;
            }
            const row = (<RowDataPacket[]>result)[0]
            resolve({
                uid: row.uid,
                regno: row.regno,
                name: row.name,
                email: row.email,
                course: row.course,
                phone: row.phone,
                avatar: row.avatar,
            });
        }
    );
});

const getAdmins = async (): Promise<AdminUser[]> => new Promise((resolve, reject) => {
    db.query(
        "SELECT user.uid, regno, name, email, course, phone, avatar, admin.cid FROM user INNER JOIN admin ON user.uid = admin.uid",
        (error, result) => {
            const admins: AdminUser[] = [];
            if (error) {
                reject(error);
                return;
            }

            (<RowDataPacket[]>result).forEach(row => admins.push({
                uid: row.uid,
                regno: row.regno,
                name: row.name,
                email: row.email,
                course: row.course,
                phone: row.phone,
                avatar: row.avatar,
                cid: row.cid,
            }));
            resolve(admins);
        }
    );
});

const updateAvatarByUid = async (userId: number, avatar: string): Promise<void> => new Promise((resolve, reject) => {
    db.query(
        "UPDATE user SET avatar=? WHERE uid=?",
        [avatar, userId],
        (error) => {
            if (error) {
                reject(error);
                return;
            }
            resolve();
        }
    );
});

const userExists = async (userId: number): Promise<boolean> => new Promise((resolve, reject) => {
    db.query(
        "SELECT * FROM user WHERE uid=?",
        [userId],
        (error, result) => {
            if (error) {
                reject(error);
                return;
            }
            resolve((<RowDataPacket[]>result).length > 0);
        }
    );
});

export const UserRepository = {
    saveUser,
    getUserByUid,
    getUserByEmail,
    getAdmins,
    updateAvatarByUid,
    userExists,
};
