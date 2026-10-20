import React, { useState } from 'react';
import { usePaymentChaser } from '../context/PaymentChaserContext';
import { formatCurrency } from '../utils/formatters';

export const PaymentDetailsScreen: React.FC = () => {
  const {
    payments,
    selectedPaymentId,
    navigate,
    openChaseModal,
    recordPartialPayment,
    markPaymentPaid,
    addNoteToPayment,
    showToast,
  } = usePaymentChaser();

  const [newNote, setNewNote] = useState('');
  const [showRecordModal, setShowRecordModal] = useState(false);
  const [recordAmount, setRecordAmount] = useState('');

  // Fallback to first payment if none selected
  const payment = payments.find((p) => p.id === selectedPaymentId) || payments[0];

  if (!payment) {
    return (
      <div className="p-8 text-center">
        <p className="text-gray-500">No payment selected.</p>
        <button
          onClick={() => navigate('payments')}
          className="mt-4 px-4 py-2 bg-[#4648d4] text-white rounded-full text-sm font-semibold"
        >
          Back to Payments
        </button>
      </div>
    );
  }

  const remaining = Math.max(0, payment.amount - payment.amountPaid);
  const isSettled = payment.status === 'paid';
  const isOverdue = payment.status === 'overdue';
  const daysDiff = Math.round(
    (Date.now() - new Date(payment.dueDate).getTime()) / 86400000
  );

  const handleAddNote = (e: React.FormEvent) => {
    e.preventDefault();
    if (!newNote.trim()) return;
    addNoteToPayment(payment.id, newNote);
    setNewNote('');
  };

  const handleRecordSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    const val = parseFloat(recordAmount);
    if (!isNaN(val) && val > 0) {
      recordPartialPayment(payment.id, val);
      setShowRecordModal(false);
      setRecordAmount('');
    }
  };

  const handleExportPDF = () => {
    showToast(`Generating PDF invoice #${payment.invoiceNumber}...`);
    setTimeout(() => {
      window.print();
    }, 600);
  };

  return (
    <div className="flex flex-col w-full pb-28 select-none">
      {/* Top Bar Actions */}
      <div className="flex items-center justify-between px-4 py-2.5 bg-[#f7f9fb]">
        <div className="flex items-center gap-1.5">
          <span className="text-[11px] font-semibold text-[#45464c] uppercase tracking-wider">
            Invoice Details
          </span>
          <span className="w-1.5 h-1.5 rounded-full bg-[#c6c6cd]"></span>
          <span className="text-[13px] font-bold text-[#191c1e] font-mono">
            #{payment.invoiceNumber}
          </span>
        </div>

        <div className="flex items-center gap-1">
          <button
            onClick={handleExportPDF}
            aria-label="Share PDF"
            title="Export / Print PDF"
            className="w-9 h-9 flex items-center justify-center rounded-full text-[#45464c] hover:bg-[#eceef0] transition-colors"
          >
            <span className="material-symbols-outlined text-[19px]">picture_as_pdf</span>
          </button>
          <button
            onClick={() => markPaymentPaid(payment.id)}
            aria-label="Mark Settled"
            title="Mark Settled"
            className="w-9 h-9 flex items-center justify-center rounded-full text-[#45464c] hover:bg-[#eceef0] transition-colors"
          >
            <span className="material-symbols-outlined text-[19px]">check_circle</span>
          </button>
        </div>
      </div>

      <div className="px-4 flex flex-col gap-3.5">
        {/* Client & Status Header Card */}
        <div className="bg-white rounded-2xl p-4 shadow-xs border border-[#eceef0]">
          <div className="flex items-start gap-3">
            <div className="w-12 h-12 rounded-xl bg-[#141b2b] text-white flex items-center justify-center text-[16px] font-bold flex-shrink-0 shadow-xs">
              {payment.clientInitials}
            </div>
            <div className="flex flex-col flex-1 min-w-0">
              <div className="flex items-center justify-between gap-1">
                <h2 className="text-[16px] font-bold text-[#191c1e] truncate">
                  {payment.clientName}
                </h2>
                <span className="material-symbols-outlined text-[#4648d4] text-[18px]">
                  verified
                </span>
              </div>
              <p className="text-[12px] text-[#45464c] mt-0.5 truncate">
                {payment.scopeDescription}
              </p>

              <div className="mt-2.5 pt-2 border-t border-[#eceef0] flex flex-col gap-1 text-[12px]">
                <div className="flex items-center gap-1 text-[#45464c]">
                  <span className="material-symbols-outlined text-[14px]">person</span>
                  <span className="font-semibold text-[#191c1e]">Marcus Vance</span>
                  <span className="text-[#76777d]">• Billing Contact</span>
                </div>
                <div className="flex items-center gap-2 text-[#45464c] truncate">
                  <a
                    href={`mailto:${payment.clientEmail}`}
                    className="flex items-center gap-1 hover:underline text-[#4648d4]"
                  >
                    <span className="material-symbols-outlined text-[13px]">mail</span>
                    <span className="truncate">{payment.clientEmail}</span>
                  </a>
                  <span className="text-[#c6c6cd]">•</span>
                  <a
                    href={`tel:${payment.clientPhone}`}
                    className="flex items-center gap-1 hover:underline text-[#191c1e]"
                  >
                    <span className="material-symbols-outlined text-[13px]">call</span>
                    <span>{payment.clientPhone}</span>
                  </a>
                </div>
              </div>
            </div>
          </div>
        </div>

        {/* Hero Balance Breakdown Card */}
        <div className="bg-[#141b2b] text-white rounded-3xl p-5 shadow-lg relative overflow-hidden">
          <div className="absolute -top-12 -right-12 w-44 h-44 rounded-full bg-[#4648d4]/20 blur-2xl pointer-events-none"></div>

          <div className="relative z-10 flex flex-col gap-3">
            <div className="flex items-center justify-between">
              <span className="text-[11px] text-[#7d8497] uppercase tracking-wider font-semibold">
                Total Outstanding
              </span>
              <span
                className={`inline-flex items-center gap-1 px-2.5 py-0.5 rounded-full text-[11px] font-semibold ${
                  isSettled
                    ? 'bg-[#002113] text-[#6ffbbe]'
                    : isOverdue
                    ? 'bg-[#ba1a1a]/20 text-[#ffdad6]'
                    : 'bg-[#4648d4]/20 text-[#e1e0ff]'
                }`}
              >
                {!isSettled && (
                  <span
                    className={`w-1.5 h-1.5 rounded-full ${
                      isOverdue ? 'bg-[#ba1a1a] animate-pulse' : 'bg-[#4648d4]'
                    }`}
                  />
                )}
                {isSettled
                  ? 'Paid in Full'
                  : isOverdue
                  ? `${daysDiff > 0 ? daysDiff : 7} Days Overdue`
                  : 'Due Soon'}
              </span>
            </div>

            <div className="flex items-baseline gap-2">
              <span className="text-[34px] font-extrabold tracking-tight tabular-nums">
                {formatCurrency(isSettled ? payment.amount : remaining, payment.currency)}
              </span>
              <span className="text-[12px] text-[#7d8497]">{payment.currency}</span>
            </div>

            {/* Metric pill grid */}
            <div className="grid grid-cols-3 gap-2 pt-1 text-[11px]">
              <div className="bg-white/5 rounded-xl p-2.5 flex flex-col">
                <span className="text-[#7d8497]">Due Date</span>
                <span className="font-semibold text-white mt-0.5 truncate">{payment.dueDate}</span>
              </div>
              <div className="bg-white/5 rounded-xl p-2.5 flex flex-col">
                <span className="text-[#7d8497]">Paid so far</span>
                <span className="font-semibold text-[#6ffbbe] mt-0.5 tabular-nums">
                  {formatCurrency(payment.amountPaid, payment.currency)}
                </span>
              </div>
              <div className="bg-white/5 rounded-xl p-2.5 flex flex-col">
                <span className="text-[#7d8497]">Terms</span>
                <span className="font-semibold text-white mt-0.5">Net 14 Days</span>
              </div>
            </div>
          </div>
        </div>

        {/* Primary Action Bar */}
        <div className="flex flex-col gap-2">
          {!isSettled && (
            <button
              onClick={() => openChaseModal(payment)}
              className="w-full h-12 rounded-full bg-[#4648d4] hover:bg-[#3b3dbb] text-white flex items-center justify-center gap-2 text-[14px] font-bold shadow-md active:scale-[0.99] transition-transform"
            >
              <span className="material-symbols-outlined text-[19px]">bolt</span>
              <span>Send Reminder Now</span>
            </button>
          )}

          <div className="grid grid-cols-2 gap-2">
            <button
              onClick={() => setShowRecordModal(true)}
              className="h-11 rounded-full bg-[#eceef0] hover:bg-[#e0e3e5] text-[#191c1e] text-[13px] font-semibold flex items-center justify-center gap-1.5 transition-colors"
            >
              <span className="material-symbols-outlined text-[18px]">payments</span>
              <span>Record Payment</span>
            </button>
            <button
              onClick={() => navigate('reminders')}
              className="h-11 rounded-full bg-[#eceef0] hover:bg-[#e0e3e5] text-[#191c1e] text-[13px] font-semibold flex items-center justify-center gap-1.5 transition-colors"
            >
              <span className="material-symbols-outlined text-[18px]">tune</span>
              <span>Sequence Cadence</span>
            </button>
          </div>
        </div>

        {/* Comprehensive Vertical Interaction Timeline */}
        <div className="bg-white rounded-2xl p-4 shadow-xs border border-[#eceef0]">
          <div className="flex items-center justify-between pb-3 border-b border-[#eceef0]">
            <div className="flex items-center gap-2">
              <span className="material-symbols-outlined text-[#4648d4] text-[18px]">
                timeline
              </span>
              <h3 className="text-[14px] font-bold text-[#191c1e]">Timeline & Audit Trail</h3>
            </div>
            <span className="text-[11px] font-semibold text-[#45464c] bg-[#f2f4f6] px-2.5 py-0.5 rounded-full">
              {payment.timeline.length} Events
            </span>
          </div>

          {/* Timeline Stem & Items */}
          <div className="relative pl-6 flex flex-col gap-4 mt-3">
            <div className="absolute left-2.5 top-2 bottom-2 w-0.5 bg-[#eceef0]"></div>

            {payment.timeline.map((event) => (
              <div key={event.id} className="relative flex flex-col text-[12px]">
                <div
                  className={`absolute -left-6 top-0 w-5 h-5 rounded-full flex items-center justify-center shadow-xs text-white ${
                    event.type === 'payment_settled'
                      ? 'bg-[#009668]'
                      : event.type === 'reminder_sent'
                      ? 'bg-[#4648d4]'
                      : 'bg-[#76777d]'
                  }`}
                >
                  <span className="material-symbols-outlined text-[12px]">
                    {event.type === 'payment_settled'
                      ? 'check'
                      : event.type === 'reminder_sent'
                      ? 'outgoing_mail'
                      : 'schedule'}
                  </span>
                </div>

                <div className="flex items-center justify-between">
                  <span className="font-bold text-[#191c1e]">{event.title}</span>
                  <span className="text-[10px] text-[#76777d] font-medium">{event.timestamp}</span>
                </div>
                <p className="text-[#45464c] mt-0.5">{event.description}</p>
                {event.openedAt && (
                  <div className="mt-1 flex items-center gap-1 text-[11px] text-[#009668] font-semibold">
                    <span className="material-symbols-outlined text-[13px]">done_all</span>
                    <span>{event.openedAt}</span>
                  </div>
                )}
              </div>
            ))}
          </div>
        </div>

        {/* Client Notes & Promise Log */}
        <div className="bg-white rounded-2xl p-4 shadow-xs border border-[#eceef0] flex flex-col gap-2.5">
          <div className="flex items-center gap-2">
            <span className="material-symbols-outlined text-[#4648d4] text-[18px]">
              edit_note
            </span>
            <span className="text-[13px] font-bold text-[#191c1e]">
              Client Notes &amp; Promise Log
            </span>
          </div>

          {payment.notes.length > 0 && (
            <div className="flex flex-col gap-2">
              {payment.notes.map((n) => (
                <div key={n.id} className="p-2.5 rounded-xl bg-[#f2f4f6] text-[12px]">
                  <div className="flex justify-between items-center text-[10px] text-[#76777d] font-semibold mb-0.5">
                    <span>{n.author}</span>
                    <span>{n.timestamp}</span>
                  </div>
                  <p className="text-[#191c1e]">{n.text}</p>
                </div>
              ))}
            </div>
          )}

          <form onSubmit={handleAddNote} className="relative flex items-center mt-1">
            <input
              type="text"
              value={newNote}
              onChange={(e) => setNewNote(e.target.value)}
              placeholder="Add note (e.g. 'Marcus promised check by Friday')..."
              className="w-full h-11 bg-[#f2f4f6] text-[#191c1e] placeholder:text-[#76777d] rounded-xl pl-3 pr-11 text-[13px] focus:outline-none focus:bg-white border border-[#e6e8ea]"
            />
            <button
              type="submit"
              className="absolute right-1 w-9 h-9 rounded-lg bg-[#4648d4] text-white flex items-center justify-center hover:bg-[#3b3dbb] transition-colors"
            >
              <span className="material-symbols-outlined text-[18px]">send</span>
            </button>
          </form>
        </div>
      </div>

      {/* Record Payment Modal */}
      {showRecordModal && (
        <div className="fixed inset-0 z-50 flex items-center justify-center bg-black/40 backdrop-blur-xs p-4">
          <div className="w-full max-w-sm bg-white rounded-3xl p-5 shadow-2xl flex flex-col gap-4">
            <div className="flex items-center justify-between">
              <h3 className="text-[16px] font-bold text-[#191c1e]">Record Payment</h3>
              <button
                onClick={() => setShowRecordModal(false)}
                className="w-8 h-8 rounded-full bg-[#eceef0] flex items-center justify-center text-[#45464c]"
              >
                <span className="material-symbols-outlined text-[18px]">close</span>
              </button>
            </div>
            <p className="text-[13px] text-[#45464c]">
              Outstanding balance for #{payment.invoiceNumber} is{' '}
              <strong className="text-[#191c1e]">{formatCurrency(remaining, payment.currency)}</strong>.
            </p>
            <form onSubmit={handleRecordSubmit} className="flex flex-col gap-3">
              <div>
                <label className="text-[12px] font-medium text-[#76777d]">Amount Received</label>
                <input
                  type="number"
                  step="0.01"
                  required
                  autoFocus
                  placeholder={remaining.toFixed(2)}
                  value={recordAmount}
                  onChange={(e) => setRecordAmount(e.target.value)}
                  className="w-full h-12 px-3 mt-1 rounded-xl bg-[#f2f4f6] text-[16px] font-bold text-[#191c1e] outline-none border border-[#e6e8ea]"
                />
              </div>
              <div className="flex gap-2">
                <button
                  type="button"
                  onClick={() => setRecordAmount(remaining.toFixed(2))}
                  className="px-3 py-1.5 rounded-lg bg-[#eceef0] text-[#191c1e] text-[12px] font-semibold"
                >
                  Full Amount
                </button>
              </div>
              <div className="flex gap-2 pt-2">
                <button
                  type="button"
                  onClick={() => setShowRecordModal(false)}
                  className="flex-1 h-11 rounded-full bg-[#eceef0] text-[#191c1e] text-[13px] font-semibold"
                >
                  Cancel
                </button>
                <button
                  type="submit"
                  className="flex-1 h-11 rounded-full bg-[#4648d4] text-white text-[13px] font-semibold shadow-sm"
                >
                  Save Payment
                </button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  );
};
