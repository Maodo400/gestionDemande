import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IChefService, getChefServiceIdentifier } from '../chef-service.model';

export type EntityResponseType = HttpResponse<IChefService>;
export type EntityArrayResponseType = HttpResponse<IChefService[]>;

@Injectable({ providedIn: 'root' })
export class ChefServiceService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/chef-services');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(chefService: IChefService): Observable<EntityResponseType> {
    return this.http.post<IChefService>(this.resourceUrl, chefService, { observe: 'response' });
  }

  update(chefService: IChefService): Observable<EntityResponseType> {
    return this.http.put<IChefService>(`${this.resourceUrl}/${getChefServiceIdentifier(chefService) as number}`, chefService, {
      observe: 'response',
    });
  }

  partialUpdate(chefService: IChefService): Observable<EntityResponseType> {
    return this.http.patch<IChefService>(`${this.resourceUrl}/${getChefServiceIdentifier(chefService) as number}`, chefService, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IChefService>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IChefService[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addChefServiceToCollectionIfMissing(
    chefServiceCollection: IChefService[],
    ...chefServicesToCheck: (IChefService | null | undefined)[]
  ): IChefService[] {
    const chefServices: IChefService[] = chefServicesToCheck.filter(isPresent);
    if (chefServices.length > 0) {
      const chefServiceCollectionIdentifiers = chefServiceCollection.map(chefServiceItem => getChefServiceIdentifier(chefServiceItem)!);
      const chefServicesToAdd = chefServices.filter(chefServiceItem => {
        const chefServiceIdentifier = getChefServiceIdentifier(chefServiceItem);
        if (chefServiceIdentifier == null || chefServiceCollectionIdentifiers.includes(chefServiceIdentifier)) {
          return false;
        }
        chefServiceCollectionIdentifiers.push(chefServiceIdentifier);
        return true;
      });
      return [...chefServicesToAdd, ...chefServiceCollection];
    }
    return chefServiceCollection;
  }
}
