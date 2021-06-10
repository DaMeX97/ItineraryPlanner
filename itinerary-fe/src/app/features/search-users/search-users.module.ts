import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { SearchUsersRoutingModule } from './search-users-routing.module';
import { SearchUsersComponent } from './search-users.component';
import { UikitModule } from 'src/app/shared/uikit/uikit.module';
import { HttpClientModule } from '@angular/common/http';


@NgModule({
  declarations: [SearchUsersComponent],
  imports: [
    CommonModule,
    SearchUsersRoutingModule,
    UikitModule,
    HttpClientModule
  ]
})
export class SearchUsersModule { }
