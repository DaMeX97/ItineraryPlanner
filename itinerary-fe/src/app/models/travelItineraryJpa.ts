import * as moment from "moment";
import { City } from "./city";
import { TravelItineraryDayJpa } from "./travelItineraryDayJpa";

export class TravelItineraryJpa {
    public id: number | undefined;
    public name: string;
    public status: string;
    public hoursPerDay: number;
    public breakMinutes: number;
    public startDate: moment.Moment;
    public days: TravelItineraryDayJpa[];
    public city: City;

    constructor(id: number | undefined, name: string, hoursPerDay: number, startDate: moment.Moment, days: TravelItineraryDayJpa[], city: City, breakMinutes: number, status: string) {
        this.id = id;
        this.hoursPerDay = hoursPerDay;
        this.startDate = startDate;
        this.days = days;
        this.name = name;
        this.city = city;
        this.breakMinutes = breakMinutes;
        this.status = status;
    }
}