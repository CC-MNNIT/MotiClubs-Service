// Throw error if any value in array is NULL/UNDEFINED/EMPTY

type VDat = string | number | undefined | null;

const validate = (arr: VDat[]) => {
    for (let i = 0; i < arr.length; ++i) {
        if (arr[i] === null || arr[i] === undefined) {
            console.log(arr[i]);
            throw new Error("Some required properties missing");
        }
        if (typeof arr[i] === "number" && isNaN(Number(arr[i]))) {
            console.log(arr[i]);
            throw new Error("Some required properties missing");
        }
        if (typeof arr[i] === "string" && arr[i].toString().trim() === "") {
            console.log(arr[i]);
            throw new Error("Some required properties missing");
        }
    }
};

export default validate;
