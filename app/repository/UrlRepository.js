const pool = require("../db/db");

const getUrls = async (clubId) => {
    const [response] = await pool.execute("SELECT urlid as urlId, cid, name, color, url FROM url WHERE cid=?", [
        clubId,
    ]);
    return response;
};

const saveUrl = async (clubId, url) => {
    await pool.execute(
        "INSERT INTO url (urlid, cid, name, color, url) VALUES (?, ?, ?, ?, ?)",
        [url.urlId, clubId, url.name, url.color, url.url]
    );
};

const updateUrl = async (urlId, url) => {
    await pool.execute("UPDATE url SET name=?, color=?, url=? WHERE urlid=?", [
        url.name,
        url.color,
        url.url,
        urlId,
    ]);
};

const deleteUrl = async (urlId) => {
    await pool.execute("DELETE FROM url WHERE urlid=?", [urlId]);
};

module.exports = {
    getUrls,
    saveUrl,
    updateUrl,
    deleteUrl,
};
