export const RequestStatus = {
  Idle: 'IDLE',
  Loading: 'LOADING',
  Success: 'SUCCESS',
  Error: 'ERROR',
} as const;

export type RequestStatus = (typeof RequestStatus)[keyof typeof RequestStatus];
