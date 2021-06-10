import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { GenericResponse } from '../models/responses/genericResponse';
import { ShallowUser } from '../models/shallowUser';
import { User } from '../models/user';
import { JwtService } from './jwt.service';

@Injectable({
  providedIn: 'root'
})
export class FollowService {
  readonly API_PATH = "/follow";

  constructor(private http: HttpClient) { }

  getFollowerRequests() : Observable<User[]> {
    return this.http.get<User[]>(
      environment.apiUrl + this.API_PATH,
      {
        headers: JwtService.getAuthorizedHeaders()
      }
    );
  }

  followUser(user: ShallowUser): Observable<GenericResponse<null>> {
    return this.http.post<GenericResponse<null>>(
      environment.apiUrl + this.API_PATH + "/" + user.id,
      {},
      {
        headers: JwtService.getAuthorizedHeaders()
      }
    );
  }

  acceptFollowRequest(user: User): Observable<GenericResponse<null>> {
    return this.http.put<GenericResponse<null>>(
      environment.apiUrl + this.API_PATH + "/" + user.id,
      {},
      {
        headers: JwtService.getAuthorizedHeaders()
      }
    );
  }

  denyFollowRequest(user: User): Observable<GenericResponse<null>> {
    return this.http.delete<GenericResponse<null>>(
      environment.apiUrl + this.API_PATH + "/" + user.id,
      {
        headers: JwtService.getAuthorizedHeaders()
      }
    );
  }
}
