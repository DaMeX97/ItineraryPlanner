import { Component, OnInit } from '@angular/core';

import { GlobalConstants } from '../../../global-constants';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.sass']
})
export class NavbarComponent implements OnInit {
  siteName = GlobalConstants.siteName;
  icon = GlobalConstants.siteIcon;
  navigation = GlobalConstants.siteNavigation;

  constructor() { }

  ngOnInit(): void {
  }

}
