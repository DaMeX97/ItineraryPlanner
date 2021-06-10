import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { BingRoute } from '../models/BingRoute';
import { City } from '../models/city';
import { TouristAttraction } from '../models/touristAttraction';
import { JwtService } from './jwt.service';

@Injectable({
  providedIn: 'root'
})
export class SearchService {
  readonly API_PATH = "/search";

  constructor(private http: HttpClient) { }

  getSuggerimenti(city: City, avoid: TouristAttraction[], name: string = "") : Observable<TouristAttraction[]> {
    let avoidString : string = avoid.map(attraction => attraction.id).join(","); 
    
    return this.http.get<TouristAttraction[]>(
      environment.apiUrl + this.API_PATH + "/suggestions?cityId=" + city.id + "&avoid=" + avoidString + ((name.length > 0) ? "&name=" + name : ''), 
      {
      headers: JwtService.getDefaultHeaders()
    });
  }

  search(city: City, startDate: string, endDate: string) : Observable<TouristAttraction[]> {
    return this.http.get<TouristAttraction[]>(
      environment.apiUrl + this.API_PATH + "?cityId=" + city.id + "&radiusKm=10&date-start=" + startDate + "&date-end=" + endDate, 
      {
      headers: JwtService.getDefaultHeaders()
    });
  }

  getTimeFromAToB(attractionA: TouristAttraction, attractionB: TouristAttraction, startTime: string) : Observable<BingRoute[]> {
    return this.http.get<BingRoute[]>(
      environment.apiUrl + this.API_PATH + "/routes/?startId=" + attractionA.id + "&endId=" + attractionB.id +"&startTime=" + startTime, 
      {
      headers: JwtService.getDefaultHeaders()
    });
  }

  
}
