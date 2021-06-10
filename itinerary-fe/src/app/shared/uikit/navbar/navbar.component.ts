import { Component, Input, OnInit } from '@angular/core';
import { FormControl } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { User } from 'src/app/models/user';
import { FollowService } from 'src/app/services/follow.service';
import { JwtService } from 'src/app/services/jwt.service';
import { UserService } from 'src/app/services/user.service';
import { GlobalConstants } from 'src/global-constants';
import { LoginModalComponent } from '../login-modal/login-modal.component';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.scss'],
  providers: [FollowService]
})
export class NavbarComponent implements OnInit {

  siteName = GlobalConstants.siteName;
  icon = GlobalConstants.siteIcon;
  url = window.location.pathname;

  logged : boolean = false;

  @Input()
  query = "";

  searchBarFormControl = new FormControl();

  username: string = "";

  sidenav: boolean = false;

  followRequests: User[] = [];

  constructor(private toastr: ToastrService, public dialog: MatDialog, private router: Router, private followService: FollowService) { 
    this.router.routeReuseStrategy.shouldReuseRoute = function () {
      return false;
    };
    this.router.onSameUrlNavigation = 'reload';

    this.username = JwtService.getName();
  }

  goToProfilePage() {
    this.router.navigateByUrl("/user?id=" + JwtService.getId());
  }

  ngOnInit(): void {
    this.logged = JwtService.isLogged();

    if(this.logged) {
      this.getFollowerRequests();
    }

    if(this.query !== "") {
      this.searchBarFormControl.setValue(this.query);
    }
  }

  openLogin() {
    const dialogRef = this.dialog.open(LoginModalComponent);
  }

  logout() {
    JwtService.logout();
    this.router.navigate(["/home"]);
  }

  searchUser() {
    this.router.navigateByUrl("/search-users?q=" + this.searchBarFormControl.value);
  }

  getFollowerRequests() {
    this.followService.getFollowerRequests().subscribe((data) => {
      this.followRequests = data;
    }, (err) => {
      this.toastr.error('An error has occured while loading all the follow requests.');
    })
  }

  answerFollowRequest(user: User, accept: boolean) {
    if(accept) {
      this.followService.acceptFollowRequest(user).subscribe((data) => {
        this.toastr.success('Request accepted.');

        this.followRequests = this.followRequests.filter((u) => {
          return u.id !== user.id;
        });
      }, (err) => {
        this.toastr.error('An error has occured while accepting the request.');
      })
    }
    else {
      this.followService.denyFollowRequest(user).subscribe((data) => {
        this.toastr.success('Request rejected.');

        this.followRequests = this.followRequests.filter((u) => {
          return u.id !== user.id;
        });
      }, (err) => {
        this.toastr.error('An error has occured while rejecting the request.');
      })
    }
  }

  toggleSidenav() {
    this.sidenav = !this.sidenav;
  }

}
