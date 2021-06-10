import { TouristAttraction } from "./touristAttraction";

export class TravelItineraryDayJpa {
    public attractions: TouristAttraction[];

    constructor(attractions: TouristAttraction[]) {
        this.attractions = attractions;
    }
}