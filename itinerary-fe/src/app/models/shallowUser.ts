export class ShallowUser {
    public id: number;
    public email: string;
    public firstName: string;
    public lastName: string;
    public numberOfPosts: number;
    public numberOfFollowers: number;
    public numberOfFollowing: number;

    constructor(id: number, email: string, firstName: string, lastName: string, numberOfPosts: number, numberOfFollowers: number, numberOfFollowing: number) {
        this.id = id;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.numberOfPosts = numberOfPosts;
        this.numberOfFollowers = numberOfFollowers;
        this.numberOfFollowing = numberOfFollowing;
    }
}