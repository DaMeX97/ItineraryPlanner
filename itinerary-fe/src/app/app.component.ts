import { Component, OnInit } from '@angular/core';
import * as moment from 'moment';
import { environment } from 'src/environments/environment';
import { AuthService } from './services/auth.service';
import { JwtService } from './services/jwt.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss'],
  providers: [AuthService]
})
export class AppComponent implements OnInit {
  title = 'itinerary-fe';

  constructor(private authService: AuthService) {}

  ngOnInit() {
    if(JwtService.logged || JwtService.isLogged()) {
      setTimeout(() => {
        this.authService.renewJwt().subscribe((data) => {
          JwtService.setToken(data);
        }, (err) => {
          console.log("Error renewing JWT");
        })
      }, JwtService.getExpire() - environment.expireBackoff);
    }
  }
}
