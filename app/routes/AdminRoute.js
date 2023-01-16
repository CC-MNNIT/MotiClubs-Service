const express = require("express");
const controller = require("../controllers/AdminController");
const auth = require("../middlewares/auth");
const router = express.Router();

// Get list of all clubs
router.get("/get_club", auth.superAdmin, controller.getClubs);

// Add a club
router.post("/add_club", auth.superAdmin, controller.saveClub);

router.delete("/delete_club", auth.superAdmin, controller.deleteClub);

// Assign admin role to user with email {req.body.email}
router.put("/add_admin", auth.superAdmin, controller.assignAdmin);

// Unassign admin role to user with email {req.body.email}
router.put("/remove_admin", auth.superAdmin, controller.unassignAdmin);

module.exports = router;
