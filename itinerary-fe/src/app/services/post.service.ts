import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { Post } from '../models/post';
import { JwtService } from './jwt.service';

@Injectable({
  providedIn: 'root'
})
export class PostService {
  readonly API_PATH = "/post";

  constructor(private http: HttpClient) { }

  getPosts(): Observable<Post[]> {
    return this.http.get<Post[]>(
      environment.apiUrl + this.API_PATH + "/posts",
      {
        headers: JwtService.getAuthorizedHeaders()
      }
    );
  }

  getUserPosts(userId: number): Observable<Post[]> {
    return this.http.get<Post[]>(
      environment.apiUrl + this.API_PATH + "/posts/" + userId,
      {
        headers: JwtService.getAuthorizedHeaders()
      }
    );
  }

  createPost(bodyToSend: string): Observable<Post> {
    return this.http.post<Post>(
      environment.apiUrl + this.API_PATH,
      { body: bodyToSend},
      {
        headers: JwtService.getAuthorizedHeaders()
      }
    );
  }

  deletePost(postId: number): Observable<any> {
    return this.http.delete<any>(
      environment.apiUrl + this.API_PATH + "/" + postId,
      {
        headers: JwtService.getAuthorizedHeaders()
      }
    );
  }

  updatePost(postId: number, bodyToSend: string): Observable<Post> {
    return this.http.put<Post>(
      environment.apiUrl + this.API_PATH,
      { body: bodyToSend, id: postId},
      {
        headers: JwtService.getAuthorizedHeaders()
      }
    );
  }
}
