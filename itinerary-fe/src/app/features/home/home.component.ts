import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { Post } from 'src/app/models/post';
import { User } from 'src/app/models/user';
import { JwtService } from 'src/app/services/jwt.service';
import { PostService } from 'src/app/services/post.service';
import { DateUtils } from 'src/app/utils/dateUtils';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss'],
  providers: [PostService]
})
export class HomeComponent implements OnInit {

  logged: boolean = false;
  posts: Post[] = [];

  constructor(private toastr: ToastrService, private postService: PostService) { }

  ngOnInit(): void {
    this.logged = JwtService.isLogged();
    
    if(JwtService.isLogged()) {
      this.loadPosts(); 
    }
  }

  loadPosts() {
    this.postService.getPosts().subscribe((data) => {
      this.posts = data;
    }, (err) => {
      this.toastr.error('An error has occured while getting all the posts.');
    })
  }

}
