import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IChefService, ChefService } from '../chef-service.model';
import { ChefServiceService } from '../service/chef-service.service';

@Injectable({ providedIn: 'root' })
export class ChefServiceRoutingResolveService implements Resolve<IChefService> {
  constructor(protected service: ChefServiceService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IChefService> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((chefService: HttpResponse<ChefService>) => {
          if (chefService.body) {
            return of(chefService.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new ChefService());
  }
}
