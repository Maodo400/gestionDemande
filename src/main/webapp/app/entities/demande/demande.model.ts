import * as dayjs from 'dayjs';
import { IClient } from 'app/entities/client/client.model';
import { StatusDemande } from 'app/entities/enumerations/status-demande.model';
import { EnumPriorite } from 'app/entities/enumerations/enum-priorite.model';
import { EnumCause } from 'app/entities/enumerations/enum-cause.model';
import { EnumDepartement } from 'app/entities/enumerations/enum-departement.model';
import { EnumService } from 'app/entities/enumerations/enum-service.model';

export interface IDemande {
  id?: number;
  statut?: StatusDemande | null;
  dateDemande?: dayjs.Dayjs | null;
  lieu?: string | null;
  duree?: number | null;
  priorite?: EnumPriorite | null;
  causeDefaillance?: EnumCause | null;
  autresCauses?: string | null;
  departement?: EnumDepartement | null;
  typeDefaillance?: EnumService | null;
  client?: IClient | null;
}

export class Demande implements IDemande {
  constructor(
    public id?: number,
    public statut?: StatusDemande | null,
    public dateDemande?: dayjs.Dayjs | null,
    public lieu?: string | null,
    public duree?: number | null,
    public priorite?: EnumPriorite | null,
    public causeDefaillance?: EnumCause | null,
    public autresCauses?: string | null,
    public departement?: EnumDepartement | null,
    public typeDefaillance?: EnumService | null,
    public client?: IClient | null
  ) {}
}

export function getDemandeIdentifier(demande: IDemande): number | undefined {
  return demande.id;
}
