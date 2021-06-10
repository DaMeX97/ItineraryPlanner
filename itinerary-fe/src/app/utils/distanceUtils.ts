import { TouristAttraction } from "../models/touristAttraction";

export class DistanceUtils {
    static getDistanceFromLatLonInKm(touristAttractionA: TouristAttraction, touristAttractionB: TouristAttraction) {
        const lat1 = touristAttractionA.latitude;
        const lon1 = touristAttractionA.longitude;
    
        const lat2 = touristAttractionB.latitude;
        const lon2 = touristAttractionB.longitude;
    
        const R = 6371; // Radius of the earth in km
        const dLat = this.deg2rad(lat2-lat1);  // deg2rad below
        const dLon = this.deg2rad(lon2-lon1); 
        const a = 
          Math.sin(dLat/2) * Math.sin(dLat/2) +
          Math.cos(this.deg2rad(lat1)) * Math.cos(this.deg2rad(lat2)) * 
          Math.sin(dLon/2) * Math.sin(dLon/2)
          ; 
        const c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a)); 
        const d = R * c; // Distance in km
        return d;
    }
      
    static deg2rad(deg: number) {
        return deg * (Math.PI/180)
    }
}