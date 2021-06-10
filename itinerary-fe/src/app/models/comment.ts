import * as moment from "moment";
import { User } from "./user";

export class Comment {
    public id: number;
    public author: User;
    public body: string;
    public createdAt: moment.Moment;

    constructor(id: number, author: User, body: string, createdAt: moment.Moment) {
        this.id = id;
        this.author = author;
        this.body = body;
        this.createdAt = createdAt;
    }
}