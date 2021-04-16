import { IUser } from 'app/entities/user/user.model';
import { IDemande } from 'app/entities/demande/demande.model';

export interface IClient {
  id?: number;
  firstName?: string;
  lastName?: string;
  email?: string;
  tel?: string | null;
  fonction?: string | null;
  user?: IUser | null;
  demandes?: IDemande[] | null;
}

export class Client implements IClient {
  constructor(
    public id?: number,
    public firstName?: string,
    public lastName?: string,
    public email?: string,
    public tel?: string | null,
    public fonction?: string | null,
    public user?: IUser | null,
    public demandes?: IDemande[] | null
  ) {}
}

export function getClientIdentifier(client: IClient): number | undefined {
  return client.id;
}
