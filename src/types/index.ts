export type PaymentStatus = 'pending' | 'due_soon' | 'overdue' | 'partially_paid' | 'paid';

export interface Client {
  id: string;
  name: string;
  company?: string;
  email: string;
  phone: string;
  initials: string;
  avatarColor?: string;
  createdAt: string;
  notes?: string;
  isArchived?: boolean;
}

export interface PaymentTimelineEvent {
  id: string;
  timestamp: string;
  type: 'created' | 'reminder_scheduled' | 'reminder_sent' | 'payment_partial' | 'payment_settled' | 'status_change';
  title: string;
  description: string;
  badge?: string;
  channel?: 'Email' | 'WhatsApp' | 'SMS';
  openedAt?: string;
}

export interface ClientNote {
  id: string;
  timestamp: string;
  text: string;
  author: string;
}

export interface Payment {
  id: string;
  invoiceNumber: string;
  clientId: string;
  clientName: string;
  clientEmail: string;
  clientPhone: string;
  clientInitials: string;
  scopeDescription: string;
  amount: number;
  amountPaid: number;
  currency: string;
  issueDate: string; // ISO date YYYY-MM-DD
  dueDate: string;   // ISO date YYYY-MM-DD
  status: PaymentStatus;
  smartSequenceEnabled: boolean;
  channels: {
    email: boolean;
    whatsapp: boolean;
    sms: boolean;
  };
  tone: 'friendly' | 'casual' | 'strict';
  lastReminderDate?: string;
  lastReminderChannel?: string;
  timeline: PaymentTimelineEvent[];
  notes: ClientNote[];
  attachmentName?: string;
  isDraft?: boolean;
}

export interface SmartSequenceStep {
  id: string;
  stepNumber: number;
  title: string;
  tag: string;
  timingDays: number; // e.g. -3 for 3 days before, 0 for on due date, 3 for 3 days overdue, 7 for 7+ days overdue
  channels: ('Email' | 'SMS' | 'WhatsApp')[];
  templateCopy: string;
  isActive: boolean;
}

export interface UserPreferences {
  businessName: string;
  ownerName: string;
  ownerEmail: string;
  ownerAvatar: string;
  defaultCurrency: string;
  autopilotActive: boolean;
  activeTone: 'friendly' | 'casual' | 'strict';
  tier: 'free' | 'pro';
  activePaymentsLimit: number;
}
