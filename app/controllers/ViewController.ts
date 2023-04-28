import { Request, Response } from "express";
import { ViewService as service } from "../services/ViewService";

const getViews = async (req: Request, res: Response) => {
    try {
        const views = await service.getViews(Number(req.query ? req.query.postId : -1));
        res.status(200).send(views);
    } catch (error) {
        console.log(error);
        res.status(400).send({ message: error.message });
    }
};

const addView = async (req: Request, res: Response) => {
    try {
        await service.addView(Number(req.body.pid), Number(req.userId));
    } catch (_) {
    } finally {
        res.status(200).send({});
    }
};

export const ViewController = {
    getViews,
    addView,
};
