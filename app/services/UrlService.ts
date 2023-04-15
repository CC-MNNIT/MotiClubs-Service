import { URL } from "../models/Url";
import { UrlRepository } from "../repository/UrlRepository";
import validate from "../utility/validate";

const getUrls = async (clubId: number) => {
    validate([clubId]);

    return (await UrlRepository.getUrls(clubId));
};

const saveUrl = async (clubId: number, urls: URL[]) => {
    validate([clubId]);
    urls.forEach(url => validate([url.cid, url.name, url.color, url.url, url.urlId]));

    const currentUrls = await UrlRepository.getUrls(clubId);
    for (let i = 0; i < currentUrls.length; ++i) {
        await UrlRepository.deleteUrl(currentUrls[i].urlId);
    }

    for (let i = 0; i < urls.length; ++i) {
        await UrlRepository.saveUrl(clubId, urls[i]);
    }
};

export const UrlService = {
    getUrls,
    saveUrl,
};
