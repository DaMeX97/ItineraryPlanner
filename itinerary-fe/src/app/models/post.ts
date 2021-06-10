import * as moment from "moment";
import { Comment } from "./comment";
import { User } from "./user";

export class Post {
    public id: number;
    public author: User;
    public body: string;
    public createdAt: moment.Moment;
    public comments: Comment[];

    constructor(id: number, author: User, body: string, createdAt: moment.Moment, comments: Comment[]) {
        this.id = id;
        this.author = author;
        this.body = body;
        this.createdAt = createdAt;
        this.comments = comments;
    }
}