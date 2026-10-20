import { Payment, PaymentStatus } from '../types';

export function formatCurrency(amount: number, currency = 'USD'): string {
  const symbolMap: Record<string, string> = {
    USD: '$',
    EUR: '€',
    GBP: '£',
    CAD: 'CA$',
    AUD: 'A$',
  };
  const symbol = symbolMap[currency] || '$';
  return `${symbol}${amount.toLocaleString('en-US', {
    minimumFractionDigits: 2,
    maximumFractionDigits: 2,
  })}`;
}

export function calculateDaysDifference(dueDateString: string): number {
  const target = new Date(dueDateString).getTime();
  const now = new Date().setHours(0, 0, 0, 0);
  const diffTime = target - now;
  return Math.round(diffTime / (1000 * 60 * 60 * 24));
}

export function computePaymentStatus(payment: Payment): PaymentStatus {
  if (payment.amountPaid >= payment.amount) {
    return 'paid';
  }
  const daysDiff = calculateDaysDifference(payment.dueDate);
  if (daysDiff < 0) {
    return payment.amountPaid > 0 ? 'partially_paid' : 'overdue';
  }
  if (daysDiff <= 3) {
    return 'due_soon';
  }
  if (payment.amountPaid > 0) {
    return 'partially_paid';
  }
  return 'pending';
}

export function getStatusBadgeInfo(status: PaymentStatus, dueDate: string, amount: number, amountPaid: number) {
  const daysDiff = calculateDaysDifference(dueDate);
  const remaining = Math.max(0, amount - amountPaid);

  switch (status) {
    case 'overdue': {
      const daysOverdue = Math.abs(daysDiff);
      return {
        label: `${daysOverdue > 0 ? daysOverdue : 7}d overdue`,
        bg: 'bg-[#ffdad6]',
        text: 'text-[#93000a]',
        border: 'border-[#ffdad6]',
        accent: 'bg-[#ba1a1a]',
      };
    }
    case 'due_soon':
      return {
        label: daysDiff === 0 ? 'Due Today' : daysDiff === 1 ? 'Due Tomorrow' : `Due in ${daysDiff} days`,
        bg: 'bg-[#e1e0ff]',
        text: 'text-[#07006c]',
        border: 'border-[#e1e0ff]',
        accent: 'bg-[#4648d4]',
      };
    case 'partially_paid':
      return {
        label: `Paid ${formatCurrency(amountPaid)} (${Math.round((amountPaid / amount) * 100)}%)`,
        bg: 'bg-[#eceef0]',
        text: 'text-[#191c1e]',
        border: 'border-[#e0e3e5]',
        accent: 'bg-[#4648d4]',
      };
    case 'paid':
      return {
        label: 'Paid in Full',
        bg: 'bg-[#6ffbbe]/40',
        text: 'text-[#005236]',
        border: 'border-[#6ffbbe]',
        accent: 'bg-[#009668]',
      };
    case 'pending':
    default:
      return {
        label: daysDiff > 0 ? `Due in ${daysDiff} days` : 'Pending',
        bg: 'bg-[#f2f4f6]',
        text: 'text-[#45464c]',
        border: 'border-[#e0e3e5]',
        accent: 'bg-[#76777d]',
      };
  }
}

export function generateReminderText(
  tone: 'friendly' | 'casual' | 'strict',
  clientName: string,
  amount: number,
  invoiceNumber: string,
  dueDate: string,
  daysOverdue = 0
): string {
  const formattedAmount = formatCurrency(amount);
  if (daysOverdue > 0) {
    if (tone === 'strict') {
      return `Attention ${clientName}: Invoice #${invoiceNumber} for ${formattedAmount} is now ${daysOverdue} days overdue. Immediate payment is required under our contract terms to prevent service suspension. Please confirm execution.`;
    }
    if (tone === 'casual') {
      return `Hey ${clientName}! Following up on invoice #${invoiceNumber} (${formattedAmount}), which was due a few days back. When do you think that'll clear? Let me know if you need another copy of the invoice!`;
    }
    return `Hello ${clientName}, this is a follow-up regarding invoice #${invoiceNumber} for ${formattedAmount}, which was due on ${dueDate} (${daysOverdue} days overdue). We would appreciate an update regarding the expected payment date. Thank you!`;
  }

  if (tone === 'strict') {
    return `Notice: Invoice #${invoiceNumber} for ${formattedAmount} is scheduled for payment on ${dueDate}. Please ensure remittance is executed in accordance with agreed Net terms.`;
  }
  if (tone === 'casual') {
    return `Hi ${clientName}, hope your week is going great! Just a quick heads up that invoice #${invoiceNumber} for ${formattedAmount} is due on ${dueDate}. Thanks so much!`;
  }
  return `Hi ${clientName}, just a friendly reminder that invoice #${invoiceNumber} of ${formattedAmount} is due on ${dueDate}. Here is the direct link to review and settle. Thank you!`;
}
