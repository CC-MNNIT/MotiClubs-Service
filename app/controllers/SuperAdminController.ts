import { Request, Response } from "express";
import { SuperAdminService } from "../services/SuperAdminService";
import { Club } from "../models/Club";

const login = async (req: Request, res: Response) => {
    res.status(200).send({});
};

const getClubs = async (req: Request, res: Response) => {
    try {
        const clubs = await SuperAdminService.getClubs();
        res.status(200).send(clubs);
    } catch (error) {
        console.log(error);
        res.status(400).send({ message: error.message });
    }
};

const saveClub = async (req: Request, res: Response) => {
    try {
        const { name, description, avatar, summary } = req.body;
        const club: Club = {
            cid: -1,
            name: String(name),
            description: String(description),
            avatar: String(avatar),
            summary: String(summary),
        }

        const clubId = await SuperAdminService.saveClub(club);
        console.log(clubId);
        res.status(200).send({ cid: clubId });
    } catch (error) {
        console.log(error);
        res.status(400).send({ message: error.message });
    }
};

const deleteClub = async (req: Request, res: Response) => {
    try {
        await SuperAdminService.deleteClub(Number(req.query.clubId));
        res.status(200).send({});
    } catch (error) {
        console.log(error);
        res.status(400).send({ message: error.message });
    }
};

const assignAdmin = async (req: Request, res: Response) => {
    try {
        // Extract email and club id from body
        const clubId: number = req.body.clubId;
        const email: string = req.body.email;

        await SuperAdminService.addAdmin(email, clubId);

        res.status(200).send({});
    } catch (error) {
        console.log(error);
        res.status(500).send({ message: error.message });
    }
};

const unassignAdmin = async (req: Request, res: Response) => {
    try {
        // Extract email and club id from body
        const userId: number = req.body.userId;
        const clubId: number = req.body.clubId;

        await SuperAdminService.removeAdmin(userId, clubId);

        res.status(200).send({});
    } catch (error) {
        console.log(error);
        res.status(400).send({ message: error.message });
    }
};

export const SuperAdminController = {
    login,
    getClubs,
    saveClub,
    deleteClub,
    assignAdmin,
    unassignAdmin,
};
