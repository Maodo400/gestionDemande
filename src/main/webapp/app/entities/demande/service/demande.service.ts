import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IDemande, getDemandeIdentifier } from '../demande.model';

export type EntityResponseType = HttpResponse<IDemande>;
export type EntityArrayResponseType = HttpResponse<IDemande[]>;

@Injectable({ providedIn: 'root' })
export class DemandeService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/demandes');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(demande: IDemande): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(demande);
    return this.http
      .post<IDemande>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(demande: IDemande): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(demande);
    return this.http
      .put<IDemande>(`${this.resourceUrl}/${getDemandeIdentifier(demande) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(demande: IDemande): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(demande);
    return this.http
      .patch<IDemande>(`${this.resourceUrl}/${getDemandeIdentifier(demande) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IDemande>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IDemande[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addDemandeToCollectionIfMissing(demandeCollection: IDemande[], ...demandesToCheck: (IDemande | null | undefined)[]): IDemande[] {
    const demandes: IDemande[] = demandesToCheck.filter(isPresent);
    if (demandes.length > 0) {
      const demandeCollectionIdentifiers = demandeCollection.map(demandeItem => getDemandeIdentifier(demandeItem)!);
      const demandesToAdd = demandes.filter(demandeItem => {
        const demandeIdentifier = getDemandeIdentifier(demandeItem);
        if (demandeIdentifier == null || demandeCollectionIdentifiers.includes(demandeIdentifier)) {
          return false;
        }
        demandeCollectionIdentifiers.push(demandeIdentifier);
        return true;
      });
      return [...demandesToAdd, ...demandeCollection];
    }
    return demandeCollection;
  }

  protected convertDateFromClient(demande: IDemande): IDemande {
    return Object.assign({}, demande, {
      dateDemande: demande.dateDemande?.isValid() ? demande.dateDemande.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.dateDemande = res.body.dateDemande ? dayjs(res.body.dateDemande) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((demande: IDemande) => {
        demande.dateDemande = demande.dateDemande ? dayjs(demande.dateDemande) : undefined;
      });
    }
    return res;
  }
}
