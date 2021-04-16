import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IChefService } from '../chef-service.model';

@Component({
  selector: 'jhi-chef-service-detail',
  templateUrl: './chef-service-detail.component.html',
})
export class ChefServiceDetailComponent implements OnInit {
  chefService: IChefService | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ chefService }) => {
      this.chefService = chefService;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
