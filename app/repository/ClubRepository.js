const getConnection = require("../db/db");

let con = null;

getConnection().then((connection) => {
    con = connection;
});

const getAllClubs = async () => {
    const response = await con.execute("SELECT * FROM club");
    return response;
};

const saveClub = async (name, description, avatar, summary) => {
    const response = con.execute(
        "INSERT INTO club (name, description, avatar, summary) VALUES (?,?,?,?)",
        [name, description, avatar, summary]
    );
    return response;
};

const deleteClubByCid = async (clubId) => {
    const response = await con.execute("DELETE FROM club WHERE cid=?", [
        clubId,
    ]);
    return response;
};

const updateClubByCid = async (clubId, description, avatar, summary) => {
    const response = await con.execute(
        "UPDATE club SET description=?, avatar=?, summary=? WHERE cid=?",
        [description, avatar, summary, clubId]
    );
    return response;
};

module.exports = {
    getAllClubs,
    saveClub,
    deleteClubByCid,
    updateClubByCid,
};
