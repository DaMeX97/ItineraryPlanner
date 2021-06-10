import { BingRoute } from "./BingRoute";
import { TouristAttraction } from "./touristAttraction";
import { TravelDay } from "./travelDay";
import { SearchService } from 'src/app/services/search.service';
import { ArrayUtils } from "../utils/arrayUtils";
import { TravelMovement } from "./travelMovement";
import * as moment from "moment";
import { TravelItineraryJpa } from "./travelItineraryJpa";
import { TravelItineraryDayJpa } from "./travelItineraryDayJpa";
import { City } from "./city";
import { User } from "./user";
import { ToastrService } from "ngx-toastr";

export class TravelItinerary {
    public days: TravelDay[] = [];

    public id : number | undefined = undefined;

    private data: TouristAttraction[] = [];
    public hoursPerDay: number = 8;
    private numberOfDays: number = 0;

    public breakMinutes: number = 10;

    public name: string = "";

    private city: City;

    startDay: moment.Moment;

    static distancesMap: Record<number, Record<number, number>> = {};

    static searchDone: boolean = false;
    static countOfUpdate: number = 0;

    static error: boolean = false;

    private static searchService: SearchService;

    private static toastrService: ToastrService;

    constructor(data: TouristAttraction[], hoursPerDay: number, numberOfDays: number, startDay: moment.Moment, city: City, breakMinutes: number) {
        this.id = undefined;
        this.data = data;
        this.hoursPerDay = hoursPerDay;
        this.numberOfDays = numberOfDays;
        this.startDay = startDay;
        this.city = city;
        this.breakMinutes = breakMinutes;

        this.name = "Travel to " + this.city.name;
    }

    addDay(day: TravelDay) {
        this.days.push(day);
    }

    getAttractions() : TouristAttraction[] {
        let attractions: TouristAttraction[] = [];

        this.days.forEach((day) => {
            attractions = attractions.concat(day.getAttractions());
        })

        return attractions;
    }

    // Recaltulate the TravelItinerary from scratch
    updateTravelItinerary() {
        this.populateItinerary();
        while(this.days.length > this.numberOfDays) {
            let minimumAttractionIndex = TouristAttraction.getLessInterestingAttraction(this.data);

            this.data = ArrayUtils.removeElementFromArray(this.data, minimumAttractionIndex);

            this.populateItinerary();
        }

        TravelItinerary.countOfUpdate--;

        if(TravelItinerary.countOfUpdate === 0) {
            TravelItinerary.searchDone = true;
        }
    }

    // Divide the itinerary in days
    populateItinerary() {
        this.data = TouristAttraction.sortByMinimumDistance(this.data);
        this.days = [];

        const minutesPerDay = this.hoursPerDay * 60;

        let copyOfData = [...this.data];

        if(copyOfData.length > 0) {
            let travelDay = new TravelDay();
            this.addDay(travelDay);
            let lastTouristAttraction = null;

            while(copyOfData.length > 0) {
                const touristAttraction = copyOfData[0];
                copyOfData = copyOfData.slice(1);

                const time = TravelItinerary.getTimeFromAToB(lastTouristAttraction, touristAttraction, minutesPerDay - (travelDay.timeForItinerary + touristAttraction.visitsDurationMinutes), this);

                if((travelDay.timeForItinerary + touristAttraction.visitsDurationMinutes + 2*this.breakMinutes + (time === -1 ? 0 : time)) > minutesPerDay) {
                    travelDay = new TravelDay();
                    this.addDay(travelDay);
                    lastTouristAttraction = null;
                }

                if(time === -1) {
                    travelDay.addAttraction(touristAttraction);
                }
                else if(lastTouristAttraction !== null) {
                    travelDay.addAttractionWithTimeFromPrev(touristAttraction, time);
                }
                else {
                    travelDay.addAttraction(touristAttraction);
                }

                lastTouristAttraction = touristAttraction;
            }
        }
    }

    deletePrevTime(movementIndex: number, dayIndex: number) {
        const movement = this.days[dayIndex].itinerary[movementIndex];
        movement.setTime(-1);
    }

    updateTravelTimeMovement = (fromIndex: number, toIndex: number, dayIndex: number) => {
        const from = this.days[dayIndex].itinerary[fromIndex];
        const to = this.days[dayIndex].itinerary[toIndex];

        if(TravelItinerary.isTimeCached(from.attraction, to.attraction)) {
          to.setTime(TravelItinerary.distancesMap[from.attraction.id][to.attraction.id]);
        }
        else {
            // need to load
    
            this.days[dayIndex].setLoading(this.days[dayIndex].loading + 1);

            TravelItinerary.searchService.getTimeFromAToB(from.attraction, to.attraction, "10:42:00").subscribe((data: BingRoute[]) => {
                const time = data[0].durationInMinutes;
                TravelItinerary.setTime(from.attraction, to.attraction, time);
                to.setTime(time);
            
                // check loader
                this.days[dayIndex].setLoading(this.days[dayIndex].loading - 1);
            }, (err) => {
                TravelItinerary.toastrService.error("An error has occured while loading the itinerary.");
                TravelItinerary.error = true;
            })
        }
    }

    addNewAttraction(attraction: TouristAttraction, dayIndex: number) {
        this.days[dayIndex].addAttraction(attraction);

        this.updateTravelTimeMovement(this.days[dayIndex].itinerary.length - 2, this.days[dayIndex].itinerary.length - 1, dayIndex);
    }

    removeAttraction(attractionIndex: number, dayIndex: number) {
        this.days[dayIndex].itinerary = ArrayUtils.removeElementFromArray(this.days[dayIndex].itinerary, attractionIndex);

        if(attractionIndex === 0) {
            // first elem
            this.deletePrevTime(0, dayIndex);
        }
        else if(attractionIndex === this.days[dayIndex].itinerary.length) {
            // was last element
            
        }
        else {
            // inner elem
            this.updateTravelTimeMovement(attractionIndex - 1, attractionIndex, dayIndex);
            this.updateTravelTimeMovement(attractionIndex, attractionIndex + 1, dayIndex);
        }
    }

    getDateString(dayIndex: number) {
        return this.getDate(dayIndex).format("DD/MM/YYYY");
    }

    getDate(dayIndex: number) {
        const date = this.startDay.clone();
        date.add(dayIndex + 1, 'days');

        return date;
    }

    getStartAttractionTime(dayIndex: number, index: number, day: TravelMovement[]) {
        const date = this.startDay.clone();
        date.add(dayIndex + 1, 'days');
    
        let timeInMinutes: number = 0;
        for(let i = 0; i < index; i++) {
          timeInMinutes += day[i].attraction.visitsDurationMinutes + 2*this.breakMinutes;
          if(i > 0) {
            if(day[i].timeFromPrev !== -1) {
              timeInMinutes += day[i].timeFromPrev;
            }
            else {
              // TODO: no calculation for prev time
            }
          }
        }
    
        // Consider also this movement from attraction (index-1) to attraction (index)
        if(index > 0) {
          if(day[index].timeFromPrev !== -1) {
            timeInMinutes += day[index].timeFromPrev;
          }
          else {
            // TODO: no calculation for prev time
          }
        }
    
        date.add(timeInMinutes, 'minutes');

        return date;
    }

    getEndAttractionTime(dayIndex: number, index: number, day: TravelMovement[]) {
        const date = this.getStartAttractionTime(dayIndex, index, day);
        date.add(day[index].attraction.visitsDurationMinutes, 'minutes');

        return date;
    }
    
    static setSearchService(searchService: SearchService) {
        this.searchService = searchService;
    }

    static setToastrService(toastrService: ToastrService) {
        this.toastrService = toastrService;
    }

    static getTime(touristAttractionA: TouristAttraction, touristAttractionB: TouristAttraction) {
        return TravelItinerary.distancesMap[touristAttractionA.id][touristAttractionB.id];
    }

    static isTimeCached(touristAttractionA: TouristAttraction, touristAttractionB: TouristAttraction) {
        return TravelItinerary.distancesMap[touristAttractionA.id] && TravelItinerary.distancesMap[touristAttractionA.id].hasOwnProperty(touristAttractionB.id);
    }

    static isTimeCaching(touristAttractionA: TouristAttraction, touristAttractionB: TouristAttraction) {
        if(this.isTimeCached(touristAttractionA, touristAttractionB)) {
            return this.getTime(touristAttractionA, touristAttractionB) === -1;
        }

        return false;
    }

    static setTime(touristAttractionA: TouristAttraction, touristAttractionB: TouristAttraction, time: number) {
        if(!TravelItinerary.distancesMap[touristAttractionA.id]) {
            TravelItinerary.distancesMap[touristAttractionA.id] = {};
        }
        TravelItinerary.distancesMap[touristAttractionA.id][touristAttractionB.id] = time;
    }

    static getTimeFromAToB(touristAttractionA: TouristAttraction | null, touristAttractionB: TouristAttraction, limit: number, itinerary: TravelItinerary) {
        if(touristAttractionA === null) {
          return 0;
        }
        else if(TravelItinerary.isTimeCached(touristAttractionA, touristAttractionB)) {
          // return distance
          return TravelItinerary.getTime(touristAttractionA, touristAttractionB);
        }
        else {
            // call async API
            if(!this.isTimeCaching(touristAttractionA, touristAttractionB)) {
                // call API only if no one is not calling the API!

                TravelItinerary.setTime(touristAttractionA, touristAttractionB, -1);

                TravelItinerary.countOfUpdate++;
                TravelItinerary.searchService.getTimeFromAToB(touristAttractionA, touristAttractionB, "10:42:00").subscribe((data: BingRoute[]) => {
                    if(!TravelItinerary.isTimeCached(touristAttractionA, touristAttractionB) || TravelItinerary.getTime(touristAttractionA, touristAttractionB) === -1) {
                        // update only if necessary
                        const time = data[0].durationInMinutes;
                    
                        TravelItinerary.setTime(touristAttractionA, touristAttractionB, Math.ceil(time));

                        if(time > limit) {
                            itinerary.updateTravelItinerary();
                        }
                        else {
                            itinerary.days.forEach(day => {
                                // go and update the time in the itinerary
                                let lastAttractionId = -1;
                                day.itinerary.forEach(itineraryInDay => {
                                    if(itineraryInDay.attraction.id === touristAttractionB.id && lastAttractionId === touristAttractionA.id) {
                                        itineraryInDay.timeFromPrev = Math.ceil(time);
                                    }

                                    lastAttractionId = itineraryInDay.attraction.id;
                                })
                            })

                            TravelItinerary.countOfUpdate--;
                            if(TravelItinerary.countOfUpdate === 0) {
                                this.searchDone = true;
                            }
                        }
                    }
                    else {
                        TravelItinerary.countOfUpdate--;
                    }
                }, (err) => {
                    TravelItinerary.countOfUpdate--;
                    TravelItinerary.toastrService.error("An error has occured while loading the itinerary.");
                    TravelItinerary.error = true;
                })
            }
        }
        
        return -1;
    }

    getTravelItineraryRequest() : TravelItineraryJpa {
        const daysRequest: TravelItineraryDayJpa[] = [];

        this.days.forEach((value: TravelDay) => {
            const dayRequest = new TravelItineraryDayJpa(value.getAttractions());
            daysRequest.push(dayRequest);
        });

        const itineraryRequest = new TravelItineraryJpa(this.id, this.name, this.hoursPerDay, this.startDay, daysRequest, this.city, this.breakMinutes, "DRAFT");

        return itineraryRequest;
    }
}