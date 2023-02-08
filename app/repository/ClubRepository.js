const pool = require("../db/db");

const getClubByClubId = async (clubId) => {
    const [response] = await pool.execute("SELECT * FROM club WHERE cid=?", [
        clubId,
    ]);
    return response[0];
};

const getAllClubs = async () => {
    const [response] = await pool.execute("SELECT * FROM club");
    return response;
};

const saveClub = async (name, description, avatar, summary) => {
    const [response] = pool.execute(
        "INSERT INTO club (name, description, avatar, summary) VALUES (?,?,?,?)",
        [name, description, avatar, summary]
    );
    return response.insertId;
};

const deleteClubByCid = async (clubId) => {
    await pool.execute("DELETE FROM club WHERE cid=?", [clubId]);
};

const updateClubByCid = async (clubId, description, avatar, summary) => {
    await pool.execute(
        "UPDATE club SET description=?, avatar=?, summary=? WHERE cid=?",
        [description, avatar, summary, clubId]
    );
};

const clubExists = async (clubId) => {
    const club = await getClubByUid(clubId);
    return club.length > 0;
};

module.exports = {
    getClubByClubId,
    getAllClubs,
    saveClub,
    deleteClubByCid,
    updateClubByCid,
    clubExists,
};
