import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { Post } from 'src/app/models/post';
import { User } from 'src/app/models/user';
import { JwtService } from 'src/app/services/jwt.service';
import { PostService } from 'src/app/services/post.service';
import { UserService } from 'src/app/services/user.service';

@Component({
  selector: 'app-user',
  templateUrl: './user.component.html',
  styleUrls: ['./user.component.scss'],
  providers: [UserService]
})
export class UserComponent implements OnInit {

  user: User | null = null;

  jwtService = JwtService;

  constructor(private toastr: ToastrService, private userService: UserService, private route: ActivatedRoute) { }

  ngOnInit(): void {
    if(JwtService.isLogged() && this.route.snapshot.queryParamMap.has('id')) {
      this.loadUser(Number(this.route.snapshot.queryParamMap.get('id'))); 
    }
  }

  loadUser(id: number) {
    this.userService.getUser(id).subscribe((data) => {
      this.user = data;
    }, (err) => {
      this.toastr.error('An error has occured while getting the user information.');
    });
  }

}
