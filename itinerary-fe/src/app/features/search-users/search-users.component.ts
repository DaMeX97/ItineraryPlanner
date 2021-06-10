import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { ShallowUser } from 'src/app/models/shallowUser';
import { User } from 'src/app/models/user';
import { FollowService } from 'src/app/services/follow.service';
import { JwtService } from 'src/app/services/jwt.service';
import { UserService } from 'src/app/services/user.service';

@Component({
  selector: 'app-search-users',
  templateUrl: './search-users.component.html',
  styleUrls: ['./search-users.component.scss'],
  providers: [UserService, FollowService]
})
export class SearchUsersComponent implements OnInit {

  constructor(private toastr: ToastrService, private userService: UserService, private followService: FollowService, private route: ActivatedRoute) { }

  query: string = "";

  users: ShallowUser[] = [];

  user: User | null = null;

  ngOnInit(): void {
    this.loadUser(JwtService.getId())

    if(this.route.snapshot.queryParamMap.has('q')) {
      // Load a saved itinerary
      this.query = this.route.snapshot.queryParamMap.get('q') || "";
      this.loadUsers();
    }
  }

  loadUsers() {
    this.userService.getUsers(this.query).subscribe((data) => {
      this.users = data;
    }, (err) => {
      this.toastr.error("Error while loading the users.");
    })
  }

  loadUser(id: number) {
    this.userService.getUser(id).subscribe((data) => {
      this.user = data;
    }, (err) => {
      this.toastr.error("Error while getting user information.");
    });
  }

}
