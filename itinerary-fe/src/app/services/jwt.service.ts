import { HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';

import jwtDecode from 'jwt-decode';
import * as moment from 'moment';
import { Jwt } from '../models/jtw';

@Injectable({
  providedIn: 'root'
})
export class JwtService {

  static logged: boolean = false;

  constructor() { }

  static setToken(token: Jwt) {
    this.logged = true;
    sessionStorage.setItem('itinerary_jwt', JSON.stringify(token));
  }

  static logout() {
    this.logged = false;
    sessionStorage.clear();
  }

  static getToken() : Jwt {
    const lsTk : string | null = sessionStorage.getItem('itinerary_jwt') ? sessionStorage.getItem('itinerary_jwt') : null;

    return lsTk ? JSON.parse(lsTk) : null;
  }

  static getName() : string {
    const token = this.getToken();
    if(token === null) {
      return "";
    }
    return token.firstName + " " + token.lastName;
  }

  static getId() : number {
    const token = this.getToken();
    if(token === null) {
      return -1;
    }
    return token.id;
  }

  static getCryptedToken() {
    const tk : Jwt = this.getToken();
    return tk ? tk.accessToken : null;
  }

  static isLogged() {
    return JwtService.getCryptedToken() !== null;
  }

  static getExpire() : number {
    const token = this.getToken();
    if(token === null) {
      return moment().subtract(1, 'hours').toDate().getTime();
    }
    return moment(token.expirationDate).toDate().getTime();
  }

  static getDefaultHeaders() {
    return new HttpHeaders({
        'cache-control': 'no-cache',
        'Content-Type':  'application/json',
        'Access-Control-Allow-Origin': '*'
    });
  }

  static getAuthorizedHeaders() {
    return new HttpHeaders({
        'Content-Type':  'application/json',
        'Access-Control-Allow-Origin': '*',
        'Authorization': 'Bearer ' + this.getCryptedToken()
    });
  }
}
