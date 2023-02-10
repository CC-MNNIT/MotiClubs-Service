const urlRepository = require("../repository/UrlRepository");
const validate = require("../utility/validate");

const getUrls = async (clubId) => {
    validate([clubId]);

    const urls = await urlRepository.getUrls(clubId);
    return urls;
};

const saveUrl = async (clubId, url) => {
    validate([clubId, url.name, url.color, url.url]);

    await urlRepository.saveUrl(url);
};

const updateUrl = async (urlId, url) => {
    validate([urlId, url.name, url.color, url.url]);

    await urlRepository.updateUrl(urlId, url);
};

const deleteUrl = async (urlId) => {
    validate([urlId]);

    await urlRepository.deleteUrl(urlId);
};

module.exports = {
    getUrls,
    saveUrl,
    updateUrl,
    deleteUrl,
};
