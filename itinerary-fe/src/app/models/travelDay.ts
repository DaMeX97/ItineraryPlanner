import { TouristAttraction } from "./touristAttraction";
import { TravelMovement } from "./travelMovement";

export class TravelDay {
    public timeForItinerary: number = 0;
    public loading: number = 0;
    public itinerary: TravelMovement[] = []

    constructor() {

    }

    increaseTimeForItinerary(time: number) {
        this.timeForItinerary += time;
    }

    addAttraction(touristAttraction: TouristAttraction) {
        this.itinerary.push(new TravelMovement(touristAttraction, -1));

        this.timeForItinerary += touristAttraction.visitsDurationMinutes;
    }

    addAttractionWithTimeFromPrev(touristAttraction: TouristAttraction, timeFromPrev : number) {
        this.itinerary.push(new TravelMovement(touristAttraction, timeFromPrev));

        this.timeForItinerary += touristAttraction.visitsDurationMinutes + timeFromPrev;
    }

    setLoading(loading: number) {
        this.loading = loading;
    }

    getAttractions() : TouristAttraction[] {
        const attractions: TouristAttraction[] = [];
        this.itinerary.forEach((movement: TravelMovement) => {
            attractions.push(movement.attraction);
        });

        return attractions;
    }


    
}