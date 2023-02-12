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

    const toRemove = currentUrls.filter((url) => {
        for (let i = 0; i < urls.length; ++i) {
            if (urls[i].urlId === url.urlId) {
                return false;
            }
        }
        return true;
    });

    for (let i = 0; i < toRemove.length; ++i)
        await urlRepository.deleteUrl(toRemove[i].urlId);

    for (let i = 0; i < urls.length; ++i) {
        if (urls[i].urlId) await urlRepository.saveUrl(clubId, urls[i]);
        else await urlRepository.updateUrl(urls[i].urlId, urls[i]);
    }
};

module.exports = {
    getUrls,
    saveUrl,
};
