export class GenericResponse<T> {
    public data: T;
    public error: boolean = false;
    public errorMessage: string = "";

    constructor(data: T) {
        this.data = data;
    }
}