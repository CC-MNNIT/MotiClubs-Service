import { RowDataPacket } from "mysql2";
import { db } from "../db/db";

const isSuperAdmin = async (userId: number): Promise<boolean> => new Promise((resolve, reject) => {
    db.query(
        "SELECT uid as userId FROM super_admin WHERE uid=?",
        [userId],
        (error, result) => {
            if (error) {
                reject(false);
                return;
            }
            resolve((<RowDataPacket[]>result).length > 0);
        }
    );
});

export const SuperAdminRepository = { isSuperAdmin };
