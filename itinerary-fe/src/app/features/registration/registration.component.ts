import { Component, OnInit } from '@angular/core';
import {FormControl, Validators} from '@angular/forms';
import { RegistrationRequest } from 'src/app/models/requests/registrationRequest';
import { AuthService } from 'src/app/services/auth.service';
import * as sha512util from 'js-sha512';
import { MatDialog } from '@angular/material/dialog';
import { InfoModalComponent } from 'src/app/shared/uikit/info-modal/info-modal.component';
import { Jwt } from 'src/app/models/jtw';
import { JwtService } from 'src/app/services/jwt.service';
import { Router } from "@angular/router"

@Component({
  selector: 'app-registration',
  templateUrl: './registration.component.html',
  styleUrls: ['./registration.component.scss'],
  providers: [AuthService]
})
export class RegistrationComponent implements OnInit {

  email = new FormControl('', [Validators.required, Validators.email]);
  firstName = new FormControl('', [Validators.required]);
  lastName = new FormControl('', [Validators.required]);
  password = new FormControl('', [Validators.required, Validators.pattern("^(?=.*[a-z])(?=.*[A-Z]).{8,}$")]);
  confirmPassword = new FormControl('', [Validators.required]);

  loaderRegistration: boolean = false;

  constructor(private authService : AuthService, public dialog: MatDialog, private router: Router) { }


  ngOnInit(): void {
    this.email = new FormControl('', [Validators.required, Validators.email]);
    this.firstName = new FormControl('', [Validators.required]);
    this.lastName = new FormControl('', [Validators.required]);
    this.password = new FormControl('', [Validators.required, Validators.pattern("^(?=.*[a-z])(?=.*[A-Z]).{8,}$")]);
    this.confirmPassword = new FormControl('', [Validators.required]);

    this.loaderRegistration = false;
  }

  getEmailMessage() {
    if (this.email.hasError('required')) {
      return 'This field is required.';
    }

    return this.email.hasError('email') ? 'Not a valid email' : '';
  }

  getFirstNameMessage() {
    if (this.firstName.hasError('required')) {
      return 'This field is required.';
    }

    return '';
  }

  getLastNameMessage() {
    if (this.lastName.hasError('required')) {
      return 'This field is required.';
    }

    return '';
  }

  getPasswordMessage() {
    if (this.password.hasError('required')) {
      return 'This field is required.';
    }
    else if(this.password.hasError('pattern')) {
      return "Password must have at least one upper case letter and one lower case letter."
    }

    return '';
  }

  getConfirmPasswordMessage() {
    if (this.confirmPassword.hasError('required')) {
      return 'This field is required.';
    }

    return '';
  }

  private getRegistrationRequest() : RegistrationRequest {
    return new RegistrationRequest(this.email.value, this.firstName.value, this.lastName.value, sha512util.sha512(this.password.value), sha512util.sha512(this.confirmPassword.value));
  }

  register = () => {
    if(this.email.value === "" || this.firstName.value === "" || this.lastName.value === "" || this.password.value === "" || this.confirmPassword.value === "") {
      return;
    }

    this.loaderRegistration = true;
    this.authService.register(this.getRegistrationRequest()).subscribe((response: Jwt) => {
      this.loaderRegistration = false;
      JwtService.setToken(response);
      this.router.navigate(["/home"]);
    }, (err) => {
      this.loaderRegistration = false;
      if(err.status === 409) {
        // Utente già esistente
        const dialogRef = this.dialog.open(InfoModalComponent, {
          data: {
            "title": "User already exists",
            "message": "This email addess is already in use.",
            "showDeny": false,
            "acceptButtonText": "OK"
          }
        });
      }
      else if(err.status === 400) {
        // Utente già esistente
        const dialogRef = this.dialog.open(InfoModalComponent, {
          data: {
            "title": "Ops! Password mismatch",
            "message": "The confirm password is not equal to the password",
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
    })
  }

}
