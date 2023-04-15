import { Router } from "express";
import { ClubController as controller } from "../controllers/ClubController";
import { auth } from "../middlewares/auth";

const router = Router();

// Get all clubs
router.get("/", auth.userAuthorization, controller.getClubs);

// Update club details
router.put("/:clubId", auth.clubAuthorization, controller.updateClub);

// Get subscribers
router.get(
    "/subscribers/:clubId",
    auth.userAuthorization,
    controller.subscribers
);

export const ClubRouter = router;
