import { ShallowUser } from "./shallowUser";
import { Post } from "./post";
import { TravelItineraryJpa } from "./travelItineraryJpa";

export class User {
    public id: number;
    public email: string;
    public firstName: string;
    public lastName: string;
    public followers: ShallowUser[];
    public following: ShallowUser[];
    public followRequest: ShallowUser[];
    public followingRequest: ShallowUser[];
    public posts: Post[];
    public itineraries: TravelItineraryJpa[];

    constructor(id: number, email: string, firstName: string, lastName: string, followers: ShallowUser[], following: ShallowUser[], followRequest: ShallowUser[], followingRequest: ShallowUser[], posts: Post[], itineraries: TravelItineraryJpa[]) {
        this.id = id;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.followers = followers;
        this.following = following;
        this.followRequest = followRequest;
        this.followingRequest = followingRequest;
        this.posts = posts;
        this.itineraries = itineraries;
    }
}