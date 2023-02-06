// Throw error if any value in array is NULL/UNDEFINED/EMPTY
const validate = (arr) => {
    for (let i = 0; i < arr.length; ++i) {
        if (!arr[i]) {
            console.log(arr[i]);
            throw new Error("Some required properties missing");
        }
    }
};

module.exports = validate;
