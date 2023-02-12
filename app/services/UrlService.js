const urlRepository = require("../repository/UrlRepository");
const validate = require("../utility/validate");

const getUrls = async (clubId) => {
    validate([clubId]);

    const urls = await urlRepository.getUrls(clubId);
    return urls;
};

const saveUrl = async (clubId, urls) => {
    validate([clubId, urls]);
    for (let i = 0; i < urls.length; ++i)
        await urlRepository.saveUrl(clubId, urls[i]);
};

const updateUrl = async (urls) => {
    validate([urls]);
    for (let i = 0; i < urls.length; ++i)
        await urlRepository.updateUrl(urls[i].urlId, urls[i]);
};

const deleteUrl = async (urlIds) => {
    validate([urlIds]);
    for (let i = 0; i < urlIds.length; ++i)
        await urlRepository.deleteUrl(urlIds[i]);
};

module.exports = {
    getUrls,
    saveUrl,
    updateUrl,
    deleteUrl,
};
