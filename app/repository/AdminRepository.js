const getConnection = require("../db/db");

let con = null;

getConnection().then((connection) => {
    con = connection;
});

const getAdminsFromClubId = async (clubId) => {
    const response = await con.execute("SELECT uid FROM admin WHERE cid=?", [
        clubId,
    ]);
    return response;
};

const getClubsWithUidAsAdmin = async (userId) => {
    const response = await con.execute("SELECT cid FROM admin WHERE uid=?", [
        userId,
    ]);
    return response;
};

const addAdmin = async (clubId, userId) => {
    const response = await con.execute(
        "INSERT INTO admin (cid, uid) VALUES (?,?)",
        [userId, clubId]
    );
    return response;
};

const removeAdmin = async (clubId, userId) => {
    const response = await con.execute(
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
