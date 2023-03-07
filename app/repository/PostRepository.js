const pool = require("../db/db");

const getPostByPostId = async (postId) => {
    const [response] = await pool.execute("SELECT * FROM post WHERE pid=?", [
        postId,
    ]);
    return response[0];
};

const getPostsByClubAndChannel = async (channelId, page, items) => {
    const [response] = await pool.execute(
        "SELECT * FROM post WHERE chid=? ORDER BY time DESC LIMIT ? OFFSET ?",
        [channelId, items, (page - 1) * items]
    );
    return response;
};

const updatePostByPostId = async (postId, message) => {
    await pool.execute("UPDATE post SET message=? WHERE pid=?", [
        message,
        postId,
    ]);
};

const detelePostByPostId = async (postId) => {
    await pool.execute("DELETE FROM post where pid=?", [postId]);
};

const savePost = async (post) => {
    await pool.execute(
        "INSERT INTO post (pid, chid, message, time, uid, general) VALUES (?,?,?,?,?,?)",
        [post.pid, post.chid, post.message, post.time, post.uid, post.general]
    );
};

const deletePostsByChannelId = async (channelId) => {
    await pool.execute("DELETE FROM post WHERE chid=?", [channelId]);
};

module.exports = {
    getPostByPostId,
    getPostsByClubAndChannel,
    updatePostByPostId,
    detelePostByPostId,
    savePost,
    deletePostsByChannelId,
};
