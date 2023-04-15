import { Request, Response } from "express";
import { ClubService } from "../services/ClubService";

const getClubs = async (_: Request, res: Response) => {
    try {
        const clubs = await ClubService.getClubs();
        res.status(200).send(clubs);
    } catch (error) {
        console.log(error);
        res.status(400).send({ message: error.message });
    }
};

const updateClub = async (req: Request, res: Response) => {
    try {
        const { summary, description, avatar } = req.body;
        await ClubService.updateClub(Number(req.params.clubId), String(description), String(avatar), String(summary));
        res.status(200).send({});
    } catch (error) {
        console.log(error);
        res.status(400).send({ message: error.message });
    }
};

const subscribers = async (req: Request, res: Response) => {
    try {
        const subscribers = await ClubService.subscribers(Number(req.params.clubId));
        res.status(200).send(subscribers);
    } catch (error) {
        console.log(error);
        res.status(400).send({ message: error.message });
    }
};

export const ClubController = {
    getClubs,
    updateClub,
    subscribers,
};
