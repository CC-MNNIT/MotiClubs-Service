const urlRepository = require("../repository/UrlRepository");
const validate = require("../utility/validate");

const getUrls = async (clubId) => {
    validate([clubId]);

    const urls = await urlRepository.getUrls(clubId);
    return urls;
};

const saveUrl = async (clubId, urls) => {
    validate([clubId, urls]);

    const currentUrls = await urlRepository.getUrls(clubId);
    for (let i = 0; i < currentUrls.length; ++i) {
        await urlRepository.deleteUrl(currentUrls[i].urlId);
    }

    for (let i = 0; i < urls.length; ++i) {
        await urlRepository.saveUrl(clubId, urls[i]);
    }
};

module.exports = {
    getUrls,
    saveUrl,
};
