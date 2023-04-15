import { OkPacket, RowDataPacket } from "mysql2";
import { db } from "../db/db";
import { Admin, AdminID, ClubID } from "../models/Admin";

const getAdminsFromClubId = async (clubId: number): Promise<AdminID[]> => new Promise((resolve, reject) => {
    db.query(
        "SELECT uid as userId FROM admin WHERE cid=?",
        [clubId],
        (error, result) => {
            const adminIds: AdminID[] = []
            if (error) {
                reject(error);
                return;
            }

            (<RowDataPacket[]>result).forEach(row => adminIds.push({ id: row.userId }));
            resolve(adminIds);
        }
    );
});


const getClubsWithUidAsAdmin = async (userId: number): Promise<ClubID[]> => new Promise((resolve, reject) => {
    db.query(
        "SELECT cid as clubId FROM admin WHERE uid=?",
        [userId],
        (error, result) => {
            const clubIds: ClubID[] = []
            if (error) {
                reject(error);
                return;
            }

            (<RowDataPacket[]>result).forEach(row => clubIds.push({ id: row.clubId }));
            resolve(clubIds);
        }
    )
});

const addAdmin = async (admin: Admin): Promise<number> => new Promise((resolve, reject) => {
    db.query(
        "INSERT INTO admin (cid, uid) VALUES (?,?)",
        [admin.cid, admin.uid],
        (error, result) => {
            if (error) {
                reject(error);
                return;
            }
            resolve((<OkPacket>result).insertId);
        }
    )
});

const removeAdmin = async (admin: Admin): Promise<void> => new Promise((resolve, reject) => {
    db.query(
        "DELETE FROM admin WHERE uid=? AND cid=?",
        [admin.uid, admin.cid],
        (error, _) => {
            if (error) {
                reject(error);
                return;
            }
            resolve();
        }
    );
});

export const AdminRepository = {
    getAdminsFromClubId,
    getClubsWithUidAsAdmin,
    addAdmin,
    removeAdmin
};
