import app from "./app";
import { config } from "dotenv";

config();

const port: string = process.env.PORT || "3000";

app.listen(port, () => {
    console.log(`Server listening on port ${port}`);
});
