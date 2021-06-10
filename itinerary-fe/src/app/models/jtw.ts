export class Jwt {
    public accessToken: string;
    public type: string;
    public id: number;
    public email: string;
    public firstName: string;
    public lastName: string;
    public expirationDate: number;

    constructor(accessToken: string, type: string, id: number, email: string, firstName: string, lastName: string, expirationDate: number) {
        this.accessToken = accessToken;
        this.type = type;
        this.id = id;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.expirationDate = expirationDate;
    }
}