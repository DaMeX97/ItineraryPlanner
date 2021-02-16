import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule } from '@angular/common/http'

import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { NavbarComponent } from './common/navbar/navbar.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MaterialModule } from './material/material.module';

import { FlexLayoutModule } from '@angular/flex-layout';
import { HeroSearchBannerComponent } from './common/hero-search-banner/hero-search-banner.component';
import { SearchPageComponent } from './pages/search-page/search-page.component';
import { SearchResultsComponent } from './common/search-results/search-results.component'

@NgModule({
  declarations: [
    AppComponent,
    NavbarComponent,
    HeroSearchBannerComponent,
    SearchPageComponent,
    SearchResultsComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    MaterialModule,
    FlexLayoutModule,
    FormsModule,
    ReactiveFormsModule,
    HttpClientModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
