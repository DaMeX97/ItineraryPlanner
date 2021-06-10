import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { Comment } from '../models/comment';
import { Post } from '../models/post';
import { JwtService } from './jwt.service';

@Injectable({
  providedIn: 'root'
})
export class CommentService {
  readonly API_PATH = "/comment";

  constructor(private http: HttpClient) { }

  createComment(bodyToSend: string, post: Post) : Observable<Comment> {
    return this.http.post<Comment>(
      environment.apiUrl + this.API_PATH + "/" + post.id,
      { body: bodyToSend },
      {
        headers: JwtService.getAuthorizedHeaders()
      }
    );
  }

  deleteComment(commentId: number): Observable<any> {
    return this.http.delete<any>(
      environment.apiUrl + this.API_PATH + "/" + commentId,
      {
        headers: JwtService.getAuthorizedHeaders()
      }
    )
  }

  updateComment(commentId: number, bodyToSend: string): Observable<any> {
    return this.http.put<Post>(
      environment.apiUrl + this.API_PATH,
      { body: bodyToSend, id: commentId},
      {
        headers: JwtService.getAuthorizedHeaders()
      }
    );
  }

}
