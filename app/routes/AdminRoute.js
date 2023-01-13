const express = require("express");
const controller = require("../controllers/AdminController");
const auth = require("../middlewares/auth");
const app = express();

// Get list of all clubs
app.get("/get_club", auth.superAdmin, controller.getClubs);

// Add a club
app.post("/add_club", auth.superAdmin, controller.saveClub);

app.delete("/delete_club", auth.superAdmin, controller.deleteClub);

// Assign admin role to user with email {req.body.email}
app.put("/add_admin", auth.superAdmin, controller.assignAdmin);

// Unassign admin role to user with email {req.body.email}
app.put("/remove_admin", auth.superAdmin, controller.unassignAdmin);

module.exports = app;
