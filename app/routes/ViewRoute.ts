import { Router } from "express";
import { ViewController as controller } from "../controllers/ViewController";
import { auth } from "../middlewares/auth";

const router = Router();

// Get list of views of a post
router.get("/", auth.userAuthorization, controller.getViews);

// Add a view
router.post("/", auth.userAuthorization, controller.addView);

export const ViewRouter = router;
