import { Component, Input, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { Router } from '@angular/router';
import * as moment from 'moment';
import { relativeTimeRounding } from 'moment';
import { ToastrService } from 'ngx-toastr';
import { Post } from 'src/app/models/post';
import { ShallowUser } from 'src/app/models/shallowUser';
import { User } from 'src/app/models/user';
import { CommentService } from 'src/app/services/comment.service';
import { JwtService } from 'src/app/services/jwt.service';
import { PostService } from 'src/app/services/post.service';
import { DateUtils } from 'src/app/utils/dateUtils';
import { InfoModalComponent } from '../info-modal/info-modal.component';

@Component({
  selector: 'app-post-list',
  templateUrl: './post-list.component.html',
  styleUrls: ['./post-list.component.scss'],
  providers: [PostService, CommentService]
})
export class PostListComponent implements OnInit {
  @Input()
  posts: Post[] = [];

  newComments: string[] = [];

  post_text: string = "";

  editPostText: string = "";
  editCommentText: string = "";

  newPostLoader: boolean = false;

  newCommentLoader: number = -1;

  editPostLoader: number = -1;
  editCommentLoader = {
    postIndex: -1,
    commentIndex: -1
  };

  editPostSpin: boolean = false;
  editCommentSpin: boolean = false;

  @Input()
  showCreatePost: boolean = true;

  dateUtils = DateUtils;

  jwtService = JwtService;

  constructor(private router: Router, private postService: PostService, private commentService: CommentService, private toastr: ToastrService, public dialog: MatDialog) { }

  ngOnInit(): void {
  }


  goToUserPage(user: User | ShallowUser) {
    this.router.navigateByUrl("/user?id=" + user.id);
  }

  createPost = () => {
    this.newPostLoader = true;
    this.postService.createPost(this.post_text).subscribe((data) => {
      this.newPostLoader = false;
      this.posts.unshift(data);
      this.post_text = "";

      this.toastr.success('Post created');
    }, (err) => {
      this.newPostLoader = false;

      this.toastr.error("An error occured while creating the post.");
    })
  }

  createComment(postIndex: number) {
    return () => {
      this.newCommentLoader = postIndex;
      this.commentService.createComment(this.newComments[postIndex], this.posts[postIndex]).subscribe((data) => {
        this.newCommentLoader = -1;

        this.newComments[postIndex] = "";
  
        this.posts[postIndex].comments.push(data);
        this.toastr.success('Comment created');
      }, (err) => {
        this.newCommentLoader = -1;
        this.toastr.error("An error occured while creating the comment.");
      })
    }
  }

  deletePost(postIndex: number) {
    const dialogRef = this.dialog.open(InfoModalComponent, {
      data: {
        "title": "Are you sure that you want to delete the post?",
        "message": "After that, the post cannot be restored.",
        "showDeny": true,
        "acceptButtonText": "Yes",
        "denyButtonText": "No"
      }
    });

    dialogRef.afterClosed().subscribe(result => {
      if(result) {
        this.postService.deletePost(this.posts[postIndex].id).subscribe((data) => {
          this.toastr.success("Post deleted.");

          this.posts = this.posts.filter((e, index) => index !== postIndex);
        }, (err) => {
          this.toastr.error("An error has occured while trying to delete this post.");
        })
      }
    });
  }

  deleteComment(postIndex: number, commentIndex: number) {
    const dialogRef = this.dialog.open(InfoModalComponent, {
      data: {
        "title": "Are you sure that you want to delete the comment?",
        "message": "After that, the comment cannot be restored.",
        "showDeny": true,
        "acceptButtonText": "Yes",
        "denyButtonText": "No"
      }
    });

    dialogRef.afterClosed().subscribe(result => {
      if(result) {
        this.commentService.deleteComment(this.posts[postIndex].comments[commentIndex].id).subscribe((data) => {
          this.toastr.success("Comment deleted.");

          this.posts[postIndex].comments = this.posts[postIndex].comments.filter((e, index) => index !== commentIndex);
        }, (err) => {
          this.toastr.error("An error has occured while trying to delete this comment.");
        })
      }
    });
  }

  editPost(postIndex: number) {
    this.editPostLoader = postIndex;
    this.editPostText = this.posts[postIndex].body;
  }

  stopEditPost() {
    this.editPostLoader = -1;
    this.editPostText = "";
  }

  editComment(postIndex: number, commentIndex: number) {
    this.editCommentLoader = {
      postIndex: postIndex,
      commentIndex: commentIndex
    };

    this.editCommentText = this.posts[postIndex].comments[commentIndex].body;
  }

  stopEditComment() {
    this.editCommentLoader = {
      postIndex: -1,
      commentIndex: -1
    };
    this.editCommentText = "";
  }

  updatePost(postIndex: number) {
    return () => {
      this.posts[postIndex].body = this.editPostText;
      this.editPostSpin = true;
      this.postService.updatePost(this.posts[postIndex].id, this.posts[postIndex].body).subscribe((data) => {
        this.editPostLoader = -1;
        this.editPostSpin = false;
        this.toastr.success("Post update correctly.");
      }, (err) => {
        this.editPostSpin = false;
        this.toastr.error("An error has occured while updating the post.");
      })
    }
  }

  updateComment(postIndex: number, commentIndex: number) {
    return () => {
      this.editCommentSpin = true;
      this.posts[postIndex].comments[commentIndex].body = this.editCommentText;

      this.commentService.updateComment(this.posts[postIndex].comments[commentIndex].id, this.posts[postIndex].comments[commentIndex].body).subscribe((data) => {
        this.stopEditComment();
        this.editCommentSpin = false;
        this.toastr.success("Comment update correctly.");
      }, (err) => {
        this.editCommentSpin = false;
        this.toastr.error("An error has occured while updating the comment.");
      })
    }
  }

}
