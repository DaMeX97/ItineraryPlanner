import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { environment } from './../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class SearchService {

  constructor(private http: HttpClient) { }

  getPlaces(luogo: string) : Observable<any> {
    return this.http.get( environment.apiUrl + '/search/' + luogo, {
      headers: new HttpHeaders({
        'cache-control': 'no-cache',
        'Content-Type':  'application/json',
        'Access-Control-Allow-Origin': '*'
      })
    });
  }
}
