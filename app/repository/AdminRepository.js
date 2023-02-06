const pool = require("../db/db");

const getAdminsFromClubId = async (clubId) => {
    const response = await pool.execute("SELECT uid FROM admin WHERE cid=?", [
        clubId,
    ]);
    return response;
};

const getClubsWithUidAsAdmin = async (userId) => {
    const response = await pool.execute("SELECT cid FROM admin WHERE uid=?", [
        userId,
    ]);
    return response;
};

const addAdmin = async (clubId, userId) => {
    const response = await pool.execute(
        "INSERT INTO admin (cid, uid) VALUES (?,?)",
        [userId, clubId]
    );
    return response;
};

const removeAdmin = async (clubId, userId) => {
    const response = await pool.execute(
        "DELETE FROM admin WHERE uid=? AND cid=?",
        [userId, clubId]
    );
    return response;
};

module.exports = {
    getAdminsFromClubId,
    getClubsWithUidAsAdmin,
    addAdmin,
    removeAdmin,
};
