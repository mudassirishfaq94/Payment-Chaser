import React, { useState, useEffect } from 'react';
import { usePaymentChaser } from '../../context/PaymentChaserContext';
import { formatCurrency, generateReminderText } from '../../utils/formatters';

export const QuickChaseModal: React.FC = () => {
  const { chaseModalPayment, closeChaseModal, dispatchReminder, showToast } = usePaymentChaser();
  const [selectedChannel, setSelectedChannel] = useState<'Email' | 'WhatsApp' | 'SMS'>('WhatsApp');
  const [selectedTone, setSelectedTone] = useState<'friendly' | 'casual' | 'strict'>('friendly');
  const [previewText, setPreviewText] = useState('');
  const [copied, setCopied] = useState(false);

  useEffect(() => {
    if (chaseModalPayment) {
      const remaining = chaseModalPayment.amount - chaseModalPayment.amountPaid;
      const daysOverdue = Math.max(
        0,
        Math.round((Date.now() - new Date(chaseModalPayment.dueDate).getTime()) / 86400000)
      );
      const generated = generateReminderText(
        selectedTone,
        chaseModalPayment.clientName,
        remaining,
        chaseModalPayment.invoiceNumber,
        chaseModalPayment.dueDate,
        daysOverdue
      );
      setPreviewText(generated);
    }
  }, [chaseModalPayment, selectedTone]);

  if (!chaseModalPayment) return null;

  const remaining = Math.max(0, chaseModalPayment.amount - chaseModalPayment.amountPaid);
  const daysDiff = Math.round(
    (Date.now() - new Date(chaseModalPayment.dueDate).getTime()) / 86400000
  );
  const isOverdue = daysDiff > 0;

  const handleSend = () => {
    dispatchReminder(chaseModalPayment.id, selectedChannel, selectedTone, previewText);
    closeChaseModal();
  };

  const handleCopy = () => {
    navigator.clipboard.writeText(previewText);
    setCopied(true);
    showToast('Reminder message copied to clipboard!');
    setTimeout(() => setCopied(false), 2000);
  };

  return (
    <div className="fixed inset-0 z-50 flex flex-col justify-end bg-black/40 backdrop-blur-sm transition-opacity duration-200">
      <div
        className="fixed inset-0"
        onClick={closeChaseModal}
        aria-hidden="true"
      />

      <div className="relative w-full max-w-lg mx-auto bg-white rounded-t-3xl p-5 shadow-2xl flex flex-col gap-4 pb-8 z-10 max-h-[85vh] overflow-y-auto no-scrollbar animate-in slide-in-from-bottom duration-200">
        {/* Handle */}
        <div className="w-10 h-1.5 bg-[#c6c6cd] rounded-full mx-auto self-center shrink-0"></div>

        {/* Sheet Header */}
        <div className="flex items-center justify-between">
          <div className="flex flex-col">
            <span className="text-[11px] text-[#4648d4] font-semibold tracking-wider uppercase">
              Quick Chaser Dispatch
            </span>
            <h2 className="text-[18px] font-bold text-[#191c1e]">
              {chaseModalPayment.clientName}
            </h2>
          </div>
          <button
            onClick={closeChaseModal}
            className="w-8 h-8 rounded-full bg-[#eceef0] flex items-center justify-center text-[#45464c] hover:bg-[#e0e3e5] active:scale-95 transition-all"
          >
            <span className="material-symbols-outlined text-[20px]">close</span>
          </button>
        </div>

        {/* Invoice Info Card */}
        <div className="flex items-center justify-between p-3 rounded-2xl bg-[#f2f4f6]">
          <div className="flex flex-col">
            <span className="text-[12px] text-[#45464c] font-mono">
              #{chaseModalPayment.invoiceNumber}
            </span>
            <span className="text-[16px] font-bold text-[#191c1e] tabular-nums">
              {formatCurrency(remaining, chaseModalPayment.currency)}
            </span>
          </div>
          <span
            className={`px-2.5 py-1 rounded-full text-[11px] font-semibold ${
              isOverdue
                ? 'bg-[#ffdad6] text-[#93000a]'
                : 'bg-[#e1e0ff] text-[#07006c]'
            }`}
          >
            {isOverdue ? `${daysDiff} days overdue` : 'Due soon'}
          </span>
        </div>

        {/* Channel Selector */}
        <div className="flex flex-col gap-1.5">
          <span className="text-[12px] text-[#191c1e] font-semibold">Send Via Channel</span>
          <div className="grid grid-cols-3 gap-2">
            <button
              onClick={() => setSelectedChannel('WhatsApp')}
              className={`flex flex-col items-center justify-center py-2.5 px-2 rounded-2xl text-[12px] font-semibold gap-1 transition-all ${
                selectedChannel === 'WhatsApp'
                  ? 'bg-[#111827] text-white shadow-sm'
                  : 'bg-[#eceef0] text-[#191c1e] hover:bg-[#e0e3e5]'
              }`}
            >
              <span className="material-symbols-outlined text-[20px] text-[#25D366]">chat</span>
              <span>WhatsApp</span>
            </button>

            <button
              onClick={() => setSelectedChannel('Email')}
              className={`flex flex-col items-center justify-center py-2.5 px-2 rounded-2xl text-[12px] font-semibold gap-1 transition-all ${
                selectedChannel === 'Email'
                  ? 'bg-[#111827] text-white shadow-sm'
                  : 'bg-[#eceef0] text-[#191c1e] hover:bg-[#e0e3e5]'
              }`}
            >
              <span className="material-symbols-outlined text-[20px] text-[#4648d4]">mail</span>
              <span>Email</span>
            </button>

            <button
              onClick={() => setSelectedChannel('SMS')}
              className={`flex flex-col items-center justify-center py-2.5 px-2 rounded-2xl text-[12px] font-semibold gap-1 transition-all ${
                selectedChannel === 'SMS'
                  ? 'bg-[#111827] text-white shadow-sm'
                  : 'bg-[#eceef0] text-[#191c1e] hover:bg-[#e0e3e5]'
              }`}
            >
              <span className="material-symbols-outlined text-[20px]">sms</span>
              <span>SMS Link</span>
            </button>
          </div>
        </div>

        {/* Tone Selector */}
        <div className="flex flex-col gap-1.5">
          <span className="text-[12px] text-[#191c1e] font-semibold">Reminder Tone</span>
          <div className="flex gap-1.5 overflow-x-auto no-scrollbar py-0.5">
            <button
              onClick={() => setSelectedTone('friendly')}
              className={`px-3 py-1.5 rounded-full text-[12px] font-medium shrink-0 transition-colors ${
                selectedTone === 'friendly'
                  ? 'bg-[#e1e0ff] text-[#07006c] font-semibold'
                  : 'bg-[#eceef0] text-[#45464c] hover:bg-[#e0e3e5]'
              }`}
            >
              Friendly & Polite
            </button>
            <button
              onClick={() => setSelectedTone('casual')}
              className={`px-3 py-1.5 rounded-full text-[12px] font-medium shrink-0 transition-colors ${
                selectedTone === 'casual'
                  ? 'bg-[#e1e0ff] text-[#07006c] font-semibold'
                  : 'bg-[#eceef0] text-[#45464c] hover:bg-[#e0e3e5]'
              }`}
            >
              Casual Freelancer
            </button>
            <button
              onClick={() => setSelectedTone('strict')}
              className={`px-3 py-1.5 rounded-full text-[12px] font-medium shrink-0 transition-colors ${
                selectedTone === 'strict'
                  ? 'bg-[#ffdad6] text-[#93000a] font-semibold'
                  : 'bg-[#eceef0] text-[#45464c] hover:bg-[#e0e3e5]'
              }`}
            >
              Firm & Urgent
            </button>
          </div>
        </div>

        {/* Message Preview Box */}
        <div className="flex flex-col gap-1.5">
          <div className="flex items-center justify-between">
            <span className="text-[12px] text-[#191c1e] font-semibold">Message Preview</span>
            <button
              onClick={handleCopy}
              className="text-[11px] font-semibold text-[#4648d4] hover:underline flex items-center gap-1"
            >
              <span className="material-symbols-outlined text-[14px]">
                {copied ? 'check' : 'content_copy'}
              </span>
              <span>{copied ? 'Copied' : 'Copy Text'}</span>
            </button>
          </div>
          <div className="p-3 bg-[#f7f9fb] border border-[#e6e8ea] rounded-2xl">
            <textarea
              value={previewText}
              onChange={(e) => setPreviewText(e.target.value)}
              rows={3}
              className="w-full bg-transparent text-[13px] text-[#191c1e] leading-relaxed outline-none resize-none font-sans"
            />
          </div>
        </div>

        {/* Actions */}
        <div className="flex items-center gap-2 pt-2">
          <button
            onClick={handleSend}
            className="flex-1 h-12 rounded-full bg-[#4648d4] hover:bg-[#3b3dbb] text-white text-[14px] font-bold flex items-center justify-center gap-2 shadow-md active:scale-95 transition-transform"
          >
            <span className="material-symbols-outlined text-[20px]">send</span>
            <span>Send via {selectedChannel}</span>
          </button>
        </div>
      </div>
    </div>
  );
};
