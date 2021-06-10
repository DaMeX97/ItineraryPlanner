import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { City } from '../models/city';
import { JwtService } from './jwt.service';

@Injectable({
  providedIn: 'root'
})
export class CityService {
  readonly API_PATH = "/city";

  constructor(private http: HttpClient) { }

  getCities(name: string) : Observable<City[]> {
    return this.http.get<City[]>( environment.apiUrl + this.API_PATH + "?name=" + name, {
      headers: JwtService.getDefaultHeaders()
    });
  }
}
