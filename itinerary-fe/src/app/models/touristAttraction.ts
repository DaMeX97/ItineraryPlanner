import { ArrayUtils } from "../utils/arrayUtils";
import { DistanceUtils } from "../utils/distanceUtils";

export class TouristAttraction {
    public id: number = -1;
    public name: string = "";
    public description: string = "";
    public imageUrl: string | null = null;
    public latitude: number = -1;
    public longitude: number = -1;
    public wikidataUrl: string = "";
    public visits: number = 0;
    public visitsDurationMinutes: number = 0;

    constructor(id: number, name: string, description: string, imageUrl: string | null, latitude: number, longitude: number, visits: number, visitsDurationMinutes: number) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.imageUrl = imageUrl;
        this.latitude = latitude;
        this.longitude = longitude;
        this.visits = visits;
        this.visitsDurationMinutes = visitsDurationMinutes;
    }

    static sortByMinimumDistance(data: TouristAttraction[]) {
        let copyOfArray = [...data];
        let newData: TouristAttraction[] = [];

        let lastAttraction = copyOfArray[0];
        newData.push(lastAttraction);
        copyOfArray = ArrayUtils.removeElementFromArray(copyOfArray, 0);

        while(copyOfArray.length > 0) {
            let nearestAttraction: TouristAttraction | null = null;
            let nearestAttractionIndex = 0;

            copyOfArray.forEach((elem, index) => {
                if(nearestAttraction === null) {
                nearestAttraction = elem;
                nearestAttractionIndex = index;
                }
                else {
                if(DistanceUtils.getDistanceFromLatLonInKm(lastAttraction, nearestAttraction) >  DistanceUtils.getDistanceFromLatLonInKm(lastAttraction, elem)) {
                    nearestAttraction = elem;
                    nearestAttractionIndex = index;
                }
                }
            });

            copyOfArray = ArrayUtils.removeElementFromArray(copyOfArray, nearestAttractionIndex);
            if(nearestAttraction !== null) {
                newData.push(nearestAttraction);
                lastAttraction = nearestAttraction;
            }
        }

        return newData;
    }

    static getLessInterestingAttraction(data: TouristAttraction[]) {
        let minimumAttractionIndex = -1;
        let minimumAttraction: TouristAttraction = {} as TouristAttraction;
        data.forEach((elem, index) => {
            if(minimumAttractionIndex === -1) {
                minimumAttractionIndex = index;
                minimumAttraction = elem;
            }
            else {
                if(elem.visits < minimumAttraction.visits) {
                    minimumAttraction = elem;
                    minimumAttractionIndex = index;
                }
            }
        });

        return minimumAttractionIndex;
    }
}