import { Component, Input, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { ShallowUser } from 'src/app/models/shallowUser';
import { User } from 'src/app/models/user';
import { FollowService } from 'src/app/services/follow.service';
import { JwtService } from 'src/app/services/jwt.service';

@Component({
  selector: 'app-user-list',
  templateUrl: './user-list.component.html',
  styleUrls: ['./user-list.component.scss'],
  providers: [FollowService]
})
export class UserListComponent implements OnInit {

  @Input()
  users: ShallowUser[] = [];

  @Input()
  currentUser: User = {} as User;

  followLoader: number = -1;

  constructor(private followService: FollowService, private router: Router, private toastr: ToastrService) { }

  ngOnInit(): void {
  }

  goToUserPage(user: User | ShallowUser) {
    this.router.navigateByUrl("/user?id=" + user.id);
  }

  followUser(user: ShallowUser, index: number) {
    return () => {
      this.followLoader = index;

      this.followService.followUser(user).subscribe((data) => {
        this.followLoader = -1;
        this.toastr.success('Follow request sent');

        this.currentUser.followingRequest.push(user);
      }, (err) => {
        this.followLoader = -1;
        this.toastr.error("An error occured while sending the follow request.");
      })
    }
  }

  isAlreadyFollowing(user: ShallowUser) {
    if(user.id === JwtService.getId()) {
      return true;
    }
    return this.currentUser.following.some((elem) => {
      return elem.id === user.id;
    });
  }

  hasFollowingRequest(user: ShallowUser) {
    if(user.id === JwtService.getId()) {
      return false;
    }
    // search in my following request
    return this.currentUser.followingRequest.some((elem) => {
      return elem.id === user.id;
    });
  }

  hasFollowRequest(user: ShallowUser) {
    if(user.id === JwtService.getId()) {
      return false;
    }
    // search in my following request
    return this.currentUser.followRequest.some((elem) => {
      return elem.id === user.id;
    });
  }

}
