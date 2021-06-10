export class ArrayUtils {
    static removeElementFromArray(array: any[], indexToRemove: number) {
        return array.filter((elem, index) => {
            return index !== indexToRemove;
        });
    }
}