const pool = require("../db/db");

const getClubByClubId = async (clubId) => {
    const [response] = await pool.execute("SELECT * FROM club WHERE cid=?", [
        clubId,
    ]);
    return response;
};

const getAllClubs = async () => {
    const response = await pool.execute("SELECT * FROM club");
    return response;
};

const saveClub = async (name, description, avatar, summary) => {
    const response = pool.execute(
        "INSERT INTO club (name, description, avatar, summary) VALUES (?,?,?,?)",
        [name, description, avatar, summary]
    );
    return response;
};

const deleteClubByCid = async (clubId) => {
    const response = await pool.execute("DELETE FROM club WHERE cid=?", [
        clubId,
    ]);
    return response;
};

const updateClubByCid = async (clubId, description, avatar, summary) => {
    const response = await pool.execute(
        "UPDATE club SET description=?, avatar=?, summary=? WHERE cid=?",
        [description, avatar, summary, clubId]
    );
    return response;
};

module.exports = {
    getClubByClubId,
    getAllClubs,
    saveClub,
    deleteClubByCid,
    updateClubByCid,
};
