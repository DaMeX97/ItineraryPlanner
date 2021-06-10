import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { RegistrationRequest } from '../models/requests/registrationRequest';
import { LoginRequest } from '../models/requests/loginRequest';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { Jwt } from '../models/jtw';
import { JwtService } from './jwt.service';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  readonly API_PATH = "/auth";

  constructor(private http: HttpClient) { 

  }

  register(user : RegistrationRequest) : Observable<Jwt> {
    return this.http.post<Jwt>( environment.apiUrl + this.API_PATH + "/register", user, {
      headers: JwtService.getDefaultHeaders()
    });
  }

  login(user : LoginRequest) : Observable<Jwt> {
    return this.http.post<Jwt>(environment.apiUrl + this.API_PATH + "/login", user, {
      headers: JwtService.getDefaultHeaders()
    });
  }

  renewJwt() : Observable<Jwt> {
    return this.http.get<Jwt>(environment.apiUrl + this.API_PATH + "/refreshToken", {
      headers: JwtService.getAuthorizedHeaders()
    });
  }
}
