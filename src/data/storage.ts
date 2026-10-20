import { Client, Payment, SmartSequenceStep, UserPreferences } from '../types';
import { INITIAL_CLIENTS, INITIAL_PAYMENTS, INITIAL_PREFERENCES, INITIAL_SEQUENCE_STEPS } from './mockData';

const STORAGE_KEYS = {
  CLIENTS: 'payment_chaser_clients_v1',
  PAYMENTS: 'payment_chaser_payments_v1',
  STEPS: 'payment_chaser_steps_v1',
  PREFS: 'payment_chaser_prefs_v1',
};

export function loadStoredClients(): Client[] {
  try {
    const data = localStorage.getItem(STORAGE_KEYS.CLIENTS);
    if (data) return JSON.parse(data);
  } catch (e) {
    console.error('Failed to load clients', e);
  }
  return INITIAL_CLIENTS;
}

export function saveStoredClients(clients: Client[]): void {
  try {
    localStorage.setItem(STORAGE_KEYS.CLIENTS, JSON.stringify(clients));
  } catch (e) {
    console.error('Failed to save clients', e);
  }
}

export function loadStoredPayments(): Payment[] {
  try {
    const data = localStorage.getItem(STORAGE_KEYS.PAYMENTS);
    if (data) return JSON.parse(data);
  } catch (e) {
    console.error('Failed to load payments', e);
  }
  return INITIAL_PAYMENTS;
}

export function saveStoredPayments(payments: Payment[]): void {
  try {
    localStorage.setItem(STORAGE_KEYS.PAYMENTS, JSON.stringify(payments));
  } catch (e) {
    console.error('Failed to save payments', e);
  }
}

export function loadStoredSteps(): SmartSequenceStep[] {
  try {
    const data = localStorage.getItem(STORAGE_KEYS.STEPS);
    if (data) return JSON.parse(data);
  } catch (e) {
    console.error('Failed to load steps', e);
  }
  return INITIAL_SEQUENCE_STEPS;
}

export function saveStoredSteps(steps: SmartSequenceStep[]): void {
  try {
    localStorage.setItem(STORAGE_KEYS.STEPS, JSON.stringify(steps));
  } catch (e) {
    console.error('Failed to save steps', e);
  }
}

export function loadStoredPrefs(): UserPreferences {
  try {
    const data = localStorage.getItem(STORAGE_KEYS.PREFS);
    if (data) return JSON.parse(data);
  } catch (e) {
    console.error('Failed to load prefs', e);
  }
  return INITIAL_PREFERENCES;
}

export function saveStoredPrefs(prefs: UserPreferences): void {
  try {
    localStorage.setItem(STORAGE_KEYS.PREFS, JSON.stringify(prefs));
  } catch (e) {
    console.error('Failed to save prefs', e);
  }
}

export function resetAllDataToDefault(): void {
  localStorage.removeItem(STORAGE_KEYS.CLIENTS);
  localStorage.removeItem(STORAGE_KEYS.PAYMENTS);
  localStorage.removeItem(STORAGE_KEYS.STEPS);
  localStorage.removeItem(STORAGE_KEYS.PREFS);
}
