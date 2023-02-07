const pool = require("../db/db");

const getChannelByChannelId = async (clubId) => {
    const [response] = await pool.execute(
        "SELECT * FROM channel WHERE chid=?",
        [clubId]
    );
    return response;
};

module.exports = { getChannelByChannelId };
