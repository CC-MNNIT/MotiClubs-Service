import { Request, Response } from "express";
import { URL } from "../models/Url";
import { UrlService as service } from "../services/UrlService";

const getUrls = async (req: Request, res: Response) => {
    try {
        const urls = await service.getUrls(Number(req.query.clubId));
        res.status(200).send(urls);
    } catch (error) {
        console.log(error);
        res.status(400).send({ message: error.message });
    }
};

const saveUrl = async (req: Request, res: Response) => {
    try {
        const clubId = Number(req.query.clubId);
        const { urls } = req.body;
        const urlList: URL[] = urls.map((it: { urlId: any; name: any; color: any; url: any; }) => {
            const url: URL = {
                urlId: Number(it.urlId),
                cid: clubId,
                name: String(it.name),
                color: String(it.color),
                url: String(it.url),
            }
            return url;
        });

        await service.saveUrl(Number(req.query.clubId), urlList);
        res.status(200).send({});
    } catch (error) {
        console.log(error);
        res.status(400).send({ message: error.message });
    }
};

export const UrlController = {
    getUrls,
    saveUrl,
};
