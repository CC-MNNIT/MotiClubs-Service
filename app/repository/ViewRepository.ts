import { QueryError, RowDataPacket } from "mysql2";
import { db } from "../db/db";
import { View } from "../models/View";

const getViews = async (postId: number): Promise<View[]> => new Promise((resolve, reject) => {
    db.query(
        "SELECT * FROM view WHERE pid=?",
        [postId],
        (error, result) => {
            const views: View[] = [];
            if (error) {
                reject(error);
                return;
            }
            (<RowDataPacket[]>result).forEach(row => views.push({ pid: row.pid, uid: row.uid }));
            resolve(views);
        }
    );
});

const addView = async (view: View): Promise<void> => new Promise((resolve, reject) => {
    db.query(
        "INSERT INTO view (pid, uid) VALUES (?,?)",
        [view.pid, view.uid],
        (error, _) => {
            if (error) {
                reject(error);
                return;
            }
            resolve();
        }
    );
});

export const ViewRepository = { getViews, addView };
