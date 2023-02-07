const pool = require("../db/db");

const getPostByPostId = async (postId) => {
    const [response] = await pool.execute("SELECT * FROM post WHERE pid=?", [
        postId,
    ]);
    return response;
};

const getPostsByClubAndChannel = async (clubId, channelId) => {
    const [response] = await pool.execute(
        "SELECT * FROM post WHERE cid=? AND chid=? SORT BY time DESC",
        [clubId, channelId]
    );
    return response;
};

const updatePostByPostId = async (postId, message) => {
    const response = await pool.execute(
        "UPDATE post SET message=? WHERE pid=?",
        [message, postId]
    );
    return response;
};

const detelePostByPostId = async (postId) => {
    const response = await pool.execute("DELETE FROM post where pid=?", [
        postId,
    ]);
    return response;
};

const savePost = async (userId, clubId, channelId, message, general) => {
    const [response] = await pool.execute(
        "INSERT INTO post (cid, chid, message, time, uid, general) VALUES (?,?,?,?,?,?)",
        [clubId, channelId, message, Date.now(), userId, general]
    );
    return response.insertId;
};

module.exports = {
    getPostByPostId,
    getPostsByClubAndChannel,
    updatePostByPostId,
    detelePostByPostId,
    savePost,
};
