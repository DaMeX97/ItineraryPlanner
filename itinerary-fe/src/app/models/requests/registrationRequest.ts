export class RegistrationRequest {
    public email: string;
    public firstName: string;
    public lastName: string;
    public password: string;
    public confirmPassword: string;

    constructor(email: string, firstName: string, lastName: string, password: string, confirmPassword: string) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.confirmPassword = confirmPassword;
    }
}