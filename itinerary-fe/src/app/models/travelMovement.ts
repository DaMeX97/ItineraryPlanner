import { TouristAttraction } from "./touristAttraction";

export class TravelMovement {
    public attraction: TouristAttraction;
    public timeFromPrev: number;

    constructor(attraction: TouristAttraction, timeFromPrev: number) {
        this.attraction = attraction;
        this.timeFromPrev = timeFromPrev;
    }

    setAttraction(attraction: TouristAttraction) {
        this.attraction = attraction;
    }

    setTime(timeFromPrev: number) {
        this.timeFromPrev = timeFromPrev;
    }
}