import { Request, Response } from "express";
import { UserService as service } from "../services/UserService";
import { User } from "../models/User";

const getUser = async (req: Request, res: Response) => {
    try {
        const user = await service.getUser(Number(req.userId));
        res.status(200).send(user);
    } catch (error) {
        console.log(error);
        res.status(400).send({ message: error.message });
    }
};

const getUserByUid = async (req: Request, res: Response) => {
    try {
        const user = await service.getUserByUid(Number(req.params.userId));
        res.status(200).send(user);
    } catch (error) {
        console.log(error);
        res.status(400).send({ message: error.message });
    }
};

const getAdmins = async (req: Request, res: Response) => {
    try {
        const admins = await service.getAdmins();
        res.status(200).send(admins);
    } catch (error) {
        console.log(error);
        res.status(400).send({ message: error.message });
    }
};

const saveUser = async (req: Request, res: Response) => {
    try {
        const { regno, name, email, course, phone, avatar } = req.body;
        const user: User = {
            uid: -1,
            regno: String(regno),
            name: String(name),
            email: String(email),
            course: String(course),
            phone: String(phone),
            avatar: String(avatar),
        }

        await service.saveUser(user);
        res.status(200).send({});
    } catch (error) {
        console.log(error);
        res.status(400).send({ message: error.message });
    }
};

const updateAvatar = async (req: Request, res: Response) => {
    try {
        await service.updateAvatar(Number(req.userId), String(req.body.avatar));
        res.status(200).send({});
    } catch (error) {
        console.log(error);
        res.status(400).send({ message: error.message });
    }
};

const updateFcmToken = async (req: Request, res: Response) => {
    try {
        await service.updateFcmToken(Number(req.userId), String(req.body.token));
        res.status(200).send({});
    } catch (error) {
        console.log(error);
        res.status(400).send({ message: error.message });
    }
};

const subscribe = async (req: Request, res: Response) => {
    try {
        await service.subscribe(Number(req.userId), Number(req.body.clubId));
        res.status(200).send({});
    } catch (error) {
        console.log(error);
        res.status(400).send({ message: error.message });
    }
};

const unsubscribe = async (req: Request, res: Response) => {
    try {
        await service.unsubscribe(Number(req.userId), Number(req.body.clubId));
        res.status(200).send({});
    } catch (error) {
        console.log(error);
        res.status(400).send({ message: error.message });
    }
};

export const UserController = {
    getUser,
    getUserByUid,
    getAdmins,
    saveUser,
    updateAvatar,
    updateFcmToken,
    subscribe,
    unsubscribe,
};
