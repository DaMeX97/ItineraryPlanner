import { Component, OnInit } from '@angular/core';
import { FormControl, Validators } from '@angular/forms';
import { LoginRequest } from 'src/app/models/requests/loginRequest';
import { Jwt } from 'src/app/models/jtw';
import { AuthService } from 'src/app/services/auth.service';
import * as sha512util from 'js-sha512';
import { JwtService } from 'src/app/services/jwt.service';
import { MatDialog } from '@angular/material/dialog';
import { InfoModalComponent } from '../info-modal/info-modal.component';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login-modal',
  templateUrl: './login-modal.component.html',
  styleUrls: ['./login-modal.component.scss'],
  providers: [AuthService]
})
export class LoginModalComponent implements OnInit {

  email = new FormControl('', [Validators.required, Validators.email]);
  password = new FormControl('', [Validators.required]);

  loading: boolean = false;

  constructor(private authService: AuthService, public dialog: MatDialog, private router: Router) {
    this.router.routeReuseStrategy.shouldReuseRoute = function () {
      return false;
    };
    this.router.onSameUrlNavigation = 'reload';
   }

  ngOnInit(): void {
    
  }

  getEmailMessage() {
    if (this.email.hasError('required')) {
      return 'Questo campo è obbligatorio.';
    }

    return this.email.hasError('email') ? 'Not a valid email' : '';
  }

  getPasswordMessage() {
    if (this.password.hasError('required')) {
      return 'Questo campo è obbligatorio.';
    }

    return '';
  }

  private getLoginRequest() : LoginRequest {
    return {
      email: this.email.value,
      password: sha512util.sha512(this.password.value)
    }
  }

  goToRegister() {
    this.dialog.closeAll();
    this.router.navigate(["/register"]);
  }

  login = () => {
    if(this.email.value === "" || this.password.value === "") {
      return;
    }

    this.loading = true;
    this.authService.login(this.getLoginRequest()).subscribe((response: Jwt) => {
      this.loading = false;
      JwtService.setToken(response);
      this.dialog.closeAll();
      this.router.navigate(["/home"]);
    },
    (err) => {
      this.loading = false;
      if(err.status === 404) {
        // Utente non esistente
        const dialogRef = this.dialog.open(InfoModalComponent, {
          data: {
            "title": "User not found",
            "message": "We don't have an account with this email address. Check and retry, please.",
            "showDeny": false,
            "acceptButtonText": "OK"
          }
        });
      }
      else if(err.status === 401) {
        // Password errata
        const dialogRef = this.dialog.open(InfoModalComponent, {
          data: {
            "title": "Wrong Password",
            "message": "It seems that the password that you are typing is incorrect! Check and retry, please.",
            "showDeny": false,
            "acceptButtonText": "OK"
          }
        });
      }
      else {
        // Errore generico
        const dialogRef = this.dialog.open(InfoModalComponent, {
          data: {
            "title": "Ops!",
            "message": "Something went wrong. Retry later!",
            "showDeny": false,
            "acceptButtonText": "OK"
          }
        });
      }
    });
  }
}
