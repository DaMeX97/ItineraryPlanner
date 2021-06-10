import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MaterialModule } from '../material/material.module';
import { NavbarComponent } from './navbar/navbar.component';
import { RouterModule } from '@angular/router';
import { FlexLayoutModule } from '@angular/flex-layout';
import { LoginModalComponent } from './login-modal/login-modal.component';
import { HttpClientModule } from '@angular/common/http';
import { InfoModalComponent } from './info-modal/info-modal.component';
import { LoaderComponent } from './loader/loader.component';
import { NgxSpinnerModule } from "ngx-spinner";
import {NgxMaterialTimepickerModule} from 'ngx-material-timepicker';
import { ImageWithLoaderComponent } from './image-with-loader/image-with-loader.component';
import { PostListComponent } from './post-list/post-list.component';
import { UserListComponent } from './user-list/user-list.component';
import { ItineraryListComponent } from './itinerary-list/itinerary-list.component';
import { FooterComponent } from './footer/footer.component';
import { ButtonSpinnerComponent } from './button-spinner/button-spinner.component';

@NgModule({
  declarations: [
    NavbarComponent,
    LoginModalComponent,
    InfoModalComponent,
    LoaderComponent,
    ImageWithLoaderComponent,
    PostListComponent,
    UserListComponent,
    ItineraryListComponent,
    FooterComponent,
    ButtonSpinnerComponent
  ],
  exports: [
    MaterialModule,
    NavbarComponent,
    FlexLayoutModule,
    HttpClientModule,
    InfoModalComponent,
    LoaderComponent,
    ImageWithLoaderComponent,
    NgxSpinnerModule,
    NgxMaterialTimepickerModule,
    PostListComponent,
    UserListComponent,
    ItineraryListComponent,
    FooterComponent,
    ButtonSpinnerComponent
  ],
  imports: [
    CommonModule,
    RouterModule,
    MaterialModule,
    FlexLayoutModule,
    NgxSpinnerModule,
    NgxMaterialTimepickerModule
  ]
})
export class UikitModule { }
