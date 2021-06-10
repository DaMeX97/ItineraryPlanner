import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { GenericResponse } from '../models/responses/genericResponse';
import { TravelItineraryJpa } from '../models/travelItineraryJpa';
import { JwtService } from './jwt.service';

@Injectable({
  providedIn: 'root'
})
export class ItineraryService {
  readonly API_PATH = "/itinerary";

  constructor(private http: HttpClient) { }

  
  getItinerary(id: number): Observable<TravelItineraryJpa> {
    return this.http.get<TravelItineraryJpa>(
      environment.apiUrl + this.API_PATH + "/" + id,
      {
        headers: JwtService.getAuthorizedHeaders()
      }
    );
  }

  saveItinerary(itinerary: TravelItineraryJpa) : Observable<TravelItineraryJpa> {
    return this.http.post<TravelItineraryJpa>(
      environment.apiUrl + this.API_PATH,
      itinerary,
      {
        headers: JwtService.getAuthorizedHeaders()
      }
    );
  }

  updateItinerary(itinerary: TravelItineraryJpa) : Observable<TravelItineraryJpa> {
    return this.http.put<TravelItineraryJpa>(
      environment.apiUrl + this.API_PATH,
      itinerary,
      {
        headers: JwtService.getAuthorizedHeaders()
      }
    );
  }

  deleteItinerary(itinerary: TravelItineraryJpa) : Observable<GenericResponse<any>> {
    return this.http.delete<GenericResponse<any>>(
      environment.apiUrl + this.API_PATH + "/" + itinerary.id,
      {
        headers: JwtService.getAuthorizedHeaders()
      }
    );
  }

  confirmItinerary(itinerary: TravelItineraryJpa): Observable<TravelItineraryJpa> {
    return this.http.put<TravelItineraryJpa>(
      environment.apiUrl + this.API_PATH + "/" + itinerary.id + "/confirm",
      {},
      {
        headers: JwtService.getAuthorizedHeaders()
      }
    );
  }

  getItineraries() : Observable<TravelItineraryJpa[]> {
    return this.http.get<TravelItineraryJpa[]>(
      environment.apiUrl + this.API_PATH + "/itineraries",
      {
        headers: JwtService.getAuthorizedHeaders()
      }
    );
  }
}
