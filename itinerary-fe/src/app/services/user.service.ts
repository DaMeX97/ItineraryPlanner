import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { TravelItineraryJpa } from '../models/travelItineraryJpa';
import { GenericResponse } from '../models/responses/genericResponse';
import { TravelItinerary } from '../models/travelItinerary';
import { JwtService } from './jwt.service';
import { User } from '../models/user';
import { ShallowUser } from '../models/shallowUser';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  readonly API_PATH = "/user";

  constructor(private http: HttpClient) { }

  getUsers(q: string) : Observable<ShallowUser[]> {
    return this.http.get<ShallowUser[]>(
      environment.apiUrl + this.API_PATH + "/search?q=" + q,
      {
        headers: JwtService.getAuthorizedHeaders()
      }
    );
  }

  getUser(id: number): Observable<User> {
    return this.http.get<User>(
      environment.apiUrl + this.API_PATH + "/" + id,
      {
        headers: JwtService.getAuthorizedHeaders()
      }
    )
  }
}
