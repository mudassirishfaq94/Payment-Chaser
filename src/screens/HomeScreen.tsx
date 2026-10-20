import React from 'react';
import { usePaymentChaser } from '../context/PaymentChaserContext';
import { formatCurrency } from '../utils/formatters';

export const HomeScreen: React.FC = () => {
  const { payments, metrics, navigate, openChaseModal, updatePrefs, prefs, showToast } =
    usePaymentChaser();

  // Find payments requiring urgent attention (overdue or due today)
  const urgentPayments = payments
    .filter((p) => p.status === 'overdue' || p.status === 'due_soon')
    .slice(0, 3);

  const handleToggleAutopilot = () => {
    const nextState = !prefs.autopilotActive;
    updatePrefs({ autopilotActive: nextState });
    showToast(nextState ? 'Smart Chaser Engine: Activated' : 'Smart Chaser Engine: Paused');
  };

  return (
    <div className="flex flex-col w-full px-4 space-y-4 pb-28 pt-2 select-none">
      {/* Greeting & Daily Status Strip */}
      <div className="flex flex-col pt-1">
        <div className="flex items-center justify-between">
          <div>
            <h2 className="text-[22px] font-bold text-[#191c1e] tracking-tight">
              Good morning, {prefs.ownerName.split(' ')[0]}
            </h2>
            <p className="text-[12px] text-[#45464c] mt-0.5">
              Friday, Oct 24 • You have {urgentPayments.length} actions today
            </p>
          </div>
          <button
            onClick={() => navigate('premium')}
            className="flex items-center justify-center w-10 h-10 rounded-full bg-[#e1e0ff] text-[#07006c] hover:bg-[#c0c1ff] transition-all shadow-sm active:scale-95"
            title="Payment Chaser Pro"
          >
            <span className="material-symbols-outlined text-[20px] [font-variation-settings:'FILL'_1]">
              bolt
            </span>
          </button>
        </div>

        {/* Micro Pulse Pill */}
        <div className="mt-2.5 self-start inline-flex items-center gap-1.5 px-3 py-1 rounded-full bg-[#eceef0] text-[#45464c] shadow-xs">
          <span className="w-2 h-2 rounded-full bg-[#4648d4] animate-pulse"></span>
          <span className="text-[11px] font-semibold text-[#191c1e]">
            2 auto-reminders dispatched today
          </span>
        </div>
      </div>

      {/* Hero Balance Card (Deep Navy Container from Stitch Design) */}
      <div className="relative overflow-hidden rounded-3xl bg-[#141b2b] text-white p-6 shadow-xl shadow-[#141b2b]/25">
        {/* Subtle Ambient Graphic Underlay */}
        <div className="absolute -right-12 -top-12 w-44 h-44 rounded-full bg-[#4648d4]/20 blur-2xl pointer-events-none"></div>
        <div className="absolute right-0 bottom-0 opacity-15 pointer-events-none pr-4 pb-2 text-white">
          <svg fill="none" height="70" viewBox="0 0 120 70" width="120">
            <path
              d="M0 60 C30 50, 45 10, 80 25 C100 35, 110 5, 120 0"
              stroke="currentColor"
              strokeLinecap="round"
              strokeWidth="4"
            />
          </svg>
        </div>

        <div className="relative z-10 flex flex-col space-y-3">
          <div className="flex items-center justify-between">
            <span className="text-[11px] tracking-wider uppercase text-[#7d8497] font-semibold">
              Total Outstanding Balance
            </span>
            <span className="inline-flex items-center gap-1 px-2.5 py-0.5 rounded-full bg-[#002113] text-[#6ffbbe] text-[11px] font-semibold">
              <span className="material-symbols-outlined text-[13px]">arrow_upward</span>
              12% vs last mo
            </span>
          </div>

          <div className="flex items-baseline gap-1.5">
            <span className="text-[34px] font-bold text-white tracking-tight tabular-nums">
              {formatCurrency(metrics.totalOutstanding, prefs.defaultCurrency)}
            </span>
            <span className="text-[12px] text-[#7d8497] font-semibold">{prefs.defaultCurrency}</span>
          </div>

          <div className="pt-1 flex flex-col gap-1 text-[#7d8497]">
            <div className="flex items-center gap-2">
              <span className="material-symbols-outlined text-[16px] text-[#6ffbbe]">domain</span>
              <span className="text-[12px] text-[#c0c6db]">
                Across <strong className="text-white font-semibold">{metrics.activeClientsCount} active clients</strong>
              </span>
            </div>
            <div className="flex items-center gap-2">
              <span className="material-symbols-outlined text-[16px] text-[#e1e0ff]">
                event_upcoming
              </span>
              <span className="text-[12px] text-[#c0c6db]">
                Next incoming: <strong className="text-white font-semibold">$3,200</strong> tomorrow
              </span>
            </div>
          </div>
        </div>
      </div>

      {/* 3-Column Quick Metrics Grid */}
      <div className="grid grid-cols-3 gap-2.5">
        {/* Overdue Metric */}
        <div
          onClick={() => navigate('payments')}
          className="flex flex-col p-3 rounded-2xl bg-white shadow-xs space-y-1 cursor-pointer hover:bg-[#f7f9fb] transition-colors"
        >
          <div className="flex items-center justify-between">
            <span className="text-[11px] text-[#ba1a1a] font-semibold flex items-center gap-0.5">
              <span className="material-symbols-outlined text-[13px] [font-variation-settings:'FILL'_1]">
                warning
              </span>
              Overdue
            </span>
            <span className="w-1.5 h-1.5 rounded-full bg-[#ba1a1a]"></span>
          </div>
          <div className="text-[16px] font-bold text-[#ba1a1a] leading-tight tabular-nums">
            {formatCurrency(metrics.totalOverdue, prefs.defaultCurrency)}
          </div>
          <span className="text-[11px] text-[#45464c] leading-none">
            {metrics.overdueInvoicesCount} invoices
          </span>
          <span className="inline-block mt-0.5 px-1.5 py-0.5 rounded-full bg-[#ffdad6] text-[#93000a] text-[10px] text-center font-semibold">
            Needs Action
          </span>
        </div>

        {/* Due Soon Metric */}
        <div
          onClick={() => navigate('payments')}
          className="flex flex-col p-3 rounded-2xl bg-white shadow-xs space-y-1 cursor-pointer hover:bg-[#f7f9fb] transition-colors"
        >
          <div className="flex items-center justify-between">
            <span className="text-[11px] text-[#4648d4] font-semibold flex items-center gap-0.5">
              <span className="material-symbols-outlined text-[13px]">schedule</span>
              Due Soon
            </span>
            <span className="w-1.5 h-1.5 rounded-full bg-[#4648d4]"></span>
          </div>
          <div className="text-[16px] font-bold text-[#191c1e] leading-tight tabular-nums">
            {formatCurrency(metrics.totalDueSoon, prefs.defaultCurrency)}
          </div>
          <span className="text-[11px] text-[#45464c] leading-none">
            {metrics.dueSoonInvoicesCount} in next 5d
          </span>
          <span className="inline-block mt-0.5 px-1.5 py-0.5 rounded-full bg-[#e1e0ff] text-[#07006c] text-[10px] text-center font-semibold">
            Queued
          </span>
        </div>

        {/* Collected Metric */}
        <div className="flex flex-col p-3 rounded-2xl bg-white shadow-xs space-y-1">
          <div className="flex items-center justify-between">
            <span className="text-[11px] text-[#009668] font-semibold flex items-center gap-0.5">
              <span className="material-symbols-outlined text-[13px] [font-variation-settings:'FILL'_1]">
                verified
              </span>
              Collected
            </span>
            <span className="w-1.5 h-1.5 rounded-full bg-[#009668]"></span>
          </div>
          <div className="text-[16px] font-bold text-[#191c1e] leading-tight tabular-nums">
            {formatCurrency(metrics.totalCollected, prefs.defaultCurrency)}
          </div>
          <span className="text-[11px] text-[#45464c] leading-none">This month</span>
          <span className="inline-block mt-0.5 px-1.5 py-0.5 rounded-full bg-[#6ffbbe]/40 text-[#005236] text-[10px] text-center font-semibold">
            100% on-time
          </span>
        </div>
      </div>

      {/* Priority Attention Invoices Section */}
      <div className="flex flex-col space-y-2.5 pt-1">
        <div className="flex items-center justify-between">
          <div className="flex items-center gap-1.5">
            <h3 className="text-[16px] font-bold text-[#191c1e]">Requires Attention</h3>
            <span className="px-2 py-0.5 rounded-full bg-[#ffdad6] text-[#93000a] text-[11px] font-bold">
              {metrics.overdueInvoicesCount}
            </span>
          </div>
          <button
            onClick={() => navigate('payments')}
            className="text-[13px] text-[#4648d4] font-semibold hover:underline"
          >
            View All ({payments.length})
          </button>
        </div>

        {/* Invoices List */}
        {urgentPayments.map((payment) => {
          const remaining = payment.amount - payment.amountPaid;
          const isOverdue = payment.status === 'overdue';

          return (
            <div
              key={payment.id}
              className="flex flex-col p-4 rounded-2xl bg-white shadow-xs space-y-3 border border-[#eceef0]/60 transition-all"
            >
              <div
                onClick={() => navigate('details', payment.id)}
                className="flex items-start justify-between gap-3 cursor-pointer"
              >
                <div className="flex items-center gap-3 min-w-0">
                  <div
                    className={`w-11 h-11 rounded-xl flex items-center justify-center text-[14px] font-bold shrink-0 ${
                      isOverdue
                        ? 'bg-[#ffdad6] text-[#93000a]'
                        : 'bg-[#e1e0ff] text-[#07006c]'
                    }`}
                  >
                    {payment.clientInitials}
                  </div>
                  <div className="flex flex-col min-w-0">
                    <span className="text-[15px] font-bold text-[#191c1e] truncate">
                      {payment.clientName}
                    </span>
                    <span className="text-[12px] text-[#45464c] truncate">
                      {payment.scopeDescription}
                    </span>
                  </div>
                </div>

                <div className="text-right shrink-0">
                  <div className="text-[16px] font-bold text-[#191c1e] tabular-nums">
                    {formatCurrency(remaining, payment.currency)}
                  </div>
                  <span
                    className={`inline-flex items-center gap-1 px-2 py-0.5 rounded-full text-[10px] font-semibold mt-0.5 ${
                      isOverdue
                        ? 'bg-[#ffdad6] text-[#93000a]'
                        : 'bg-[#e1e0ff] text-[#07006c]'
                    }`}
                  >
                    <span className="material-symbols-outlined text-[11px]">
                      {isOverdue ? 'schedule' : 'event'}
                    </span>
                    {isOverdue ? '7d overdue' : 'Due Today'}
                  </span>
                </div>
              </div>

              {/* Quick Action Chase Buttons */}
              <div className="flex items-center gap-2 pt-0.5">
                <button
                  onClick={() => openChaseModal(payment)}
                  className="flex-1 min-h-[42px] px-3 py-2 rounded-full bg-[#4648d4] text-white text-[13px] font-semibold flex items-center justify-center gap-1.5 shadow-sm active:scale-95 transition-transform"
                >
                  <span className="material-symbols-outlined text-[17px]">chat</span>
                  <span>Send Firm WhatsApp</span>
                </button>
                <button
                  onClick={() => openChaseModal(payment)}
                  className="w-10 h-10 rounded-full bg-[#eceef0] text-[#191c1e] flex items-center justify-center shrink-0 hover:bg-[#e0e3e5] active:scale-95 transition-transform"
                  title="Send Email"
                >
                  <span className="material-symbols-outlined text-[19px]">mail</span>
                </button>
                <button
                  onClick={() => navigate('details', payment.id)}
                  className="w-10 h-10 rounded-full bg-[#eceef0] text-[#191c1e] flex items-center justify-center shrink-0 hover:bg-[#e0e3e5] active:scale-95 transition-transform"
                  title="More Details"
                >
                  <span className="material-symbols-outlined text-[19px]">more_vert</span>
                </button>
              </div>
            </div>
          );
        })}
      </div>

      {/* Smart Chaser Automation Queue Banner */}
      <div className="flex flex-col p-4 rounded-2xl bg-[#e6e8ea] text-[#191c1e] space-y-3">
        <div className="flex items-center justify-between">
          <div className="flex items-center gap-2">
            <div className="w-8 h-8 rounded-full bg-[#4648d4] text-white flex items-center justify-center shadow-xs">
              <span className="material-symbols-outlined text-[18px] [font-variation-settings:'FILL'_1]">
                auto_mode
              </span>
            </div>
            <div>
              <h4 className="text-[14px] font-bold text-[#191c1e] leading-tight">
                Smart Chaser Engine
              </h4>
              <span className="text-[11px] text-[#45464c]">
                {prefs.autopilotActive ? 'Active • 3 escalation tiers' : 'Paused by owner'}
              </span>
            </div>
          </div>

          {/* Interactive Switch */}
          <button
            onClick={handleToggleAutopilot}
            aria-pressed={prefs.autopilotActive}
            className={`w-11 h-6 rounded-full transition-colors relative flex items-center px-0.5 ${
              prefs.autopilotActive ? 'bg-[#4648d4]' : 'bg-[#d8dadc]'
            }`}
          >
            <div
              className={`w-5 h-5 rounded-full bg-white shadow-xs transform transition-transform ${
                prefs.autopilotActive ? 'translate-x-5' : 'translate-x-0'
              }`}
            />
          </button>
        </div>

        {/* Next Pulse Preview */}
        <div
          onClick={() => navigate('reminders')}
          className="flex items-center gap-2.5 px-3 py-2 rounded-xl bg-white shadow-xs cursor-pointer hover:bg-[#f7f9fb] transition-colors"
        >
          <span className="material-symbols-outlined text-[20px] text-[#4648d4]">
            forward_to_inbox
          </span>
          <div className="flex flex-col min-w-0">
            <span className="text-[12px] font-semibold text-[#191c1e] truncate">
              Next Action: Friendly nudge to Nexus Digital
            </span>
            <span className="text-[11px] text-[#45464c]">Dispatches automatically in 18 hrs</span>
          </div>
        </div>
      </div>

      {/* Recent Live Activity Feed */}
      <div className="flex flex-col space-y-2 pb-6">
        <h3 className="text-[16px] font-bold text-[#191c1e]">Recent Activity</h3>
        <div className="flex flex-col space-y-2">
          {/* Feed 1: Paid */}
          <div className="flex items-center gap-3 p-3 rounded-2xl bg-white shadow-xs border border-[#eceef0]/60">
            <div className="w-9 h-9 rounded-full bg-[#6ffbbe]/40 text-[#005236] flex items-center justify-center shrink-0">
              <span className="material-symbols-outlined text-[18px]">check_circle</span>
            </div>
            <div className="flex flex-col min-w-0 flex-1">
              <span className="text-[13px] font-semibold text-[#191c1e] truncate">
                Paid: $2,100 from Lumina Media
              </span>
              <span className="text-[11px] text-[#45464c]">
                Cleared via Stripe Transfer • 3h ago
              </span>
            </div>
            <span className="text-[12px] text-[#009668] font-bold shrink-0">+$2,100</span>
          </div>

          {/* Feed 2: Dispatched */}
          <div className="flex items-center gap-3 p-3 rounded-2xl bg-white shadow-xs border border-[#eceef0]/60">
            <div className="w-9 h-9 rounded-full bg-[#e1e0ff] text-[#07006c] flex items-center justify-center shrink-0">
              <span className="material-symbols-outlined text-[18px]">outgoing_mail</span>
            </div>
            <div className="flex flex-col min-w-0 flex-1">
              <span className="text-[13px] font-semibold text-[#191c1e] truncate">
                Auto-reminder sent to Acme Studio
              </span>
              <span className="text-[11px] text-[#45464c]">
                Tone: Firm Reminder (Milestone 2) • 6h ago
              </span>
            </div>
            <span className="material-symbols-outlined text-[16px] text-[#76777d] shrink-0">
              done_all
            </span>
          </div>
        </div>
      </div>

      {/* Floating Android M3 Extended FAB */}
      <div className="fixed right-4 bottom-20 z-30">
        <button
          onClick={() => navigate('add')}
          className="flex items-center gap-2 h-13 px-5 rounded-full bg-[#4648d4] text-white shadow-lg shadow-[#4648d4]/35 hover:bg-[#3b3dbb] active:scale-95 transition-all"
        >
          <span className="material-symbols-outlined text-[22px]">receipt_long</span>
          <span className="text-[14px] font-bold tracking-wide">+ Track Payment</span>
        </button>
      </div>
    </div>
  );
};
