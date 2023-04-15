import { Router } from "express";
import { UrlController as controller } from "../controllers/UrlController";
import { auth } from "../middlewares/auth";

const router = Router();

// Get urls of club by clubId in the query
router.get("/", auth.userAuthorization, controller.getUrls);

// Save / Update urls of club by url objects in body
router.post("/", auth.urlAuthorization, controller.saveUrl);

export const UrlRouter = router;
