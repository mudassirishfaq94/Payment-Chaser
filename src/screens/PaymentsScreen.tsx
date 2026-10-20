import React, { useState, useMemo } from 'react';
import { usePaymentChaser } from '../context/PaymentChaserContext';
import { Payment, PaymentStatus } from '../types';
import { formatCurrency, getStatusBadgeInfo } from '../utils/formatters';

export const PaymentsScreen: React.FC = () => {
  const { payments, metrics, navigate, openChaseModal, recordPartialPayment, markPaymentPaid } =
    usePaymentChaser();

  const [searchQuery, setSearchQuery] = useState('');
  const [statusFilter, setStatusFilter] = useState<'all' | 'overdue' | 'pending' | 'paid' | 'drafts'>('all');
  const [sortOrder, setSortOrder] = useState<'asc' | 'desc'>('asc');
  const [partialModalPayment, setPartialModalPayment] = useState<Payment | null>(null);
  const [partialAmount, setPartialAmount] = useState('');

  // Filter and sort
  const filteredPayments = useMemo(() => {
    return payments
      .filter((p) => {
        // Status filter
        if (statusFilter === 'overdue' && p.status !== 'overdue') return false;
        if (statusFilter === 'pending' && p.status !== 'pending' && p.status !== 'due_soon') return false;
        if (statusFilter === 'paid' && p.status !== 'paid') return false;
        if (statusFilter === 'drafts' && !p.isDraft) return false;

        // Search query
        if (!searchQuery.trim()) return true;
        const q = searchQuery.toLowerCase();
        return (
          p.clientName.toLowerCase().includes(q) ||
          p.invoiceNumber.toLowerCase().includes(q) ||
          p.scopeDescription.toLowerCase().includes(q) ||
          p.amount.toString().includes(q)
        );
      })
      .sort((a, b) => {
        const dateA = new Date(a.dueDate).getTime();
        const dateB = new Date(b.dueDate).getTime();
        return sortOrder === 'asc' ? dateA - dateB : dateB - dateA;
      });
  }, [payments, statusFilter, searchQuery, sortOrder]);

  const handlePartialSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    if (!partialModalPayment) return;
    const val = parseFloat(partialAmount);
    if (!isNaN(val) && val > 0) {
      recordPartialPayment(partialModalPayment.id, val);
      setPartialModalPayment(null);
      setPartialAmount('');
    }
  };

  return (
    <div className="flex flex-col w-full pb-28 select-none">
      {/* Search & Filter Area */}
      <div className="px-4 pt-3 pb-1 flex flex-col gap-2.5">
        {/* M3 Search Bar */}
        <div className="relative w-full flex items-center bg-white rounded-full px-4 h-12 shadow-xs border border-[#e6e8ea]">
          <span className="material-symbols-outlined text-[#76777d] text-[22px] select-none">
            search
          </span>
          <input
            value={searchQuery}
            onChange={(e) => setSearchQuery(e.target.value)}
            className="w-full bg-transparent border-0 outline-none px-3 text-[14px] text-[#191c1e] placeholder:text-[#76777d]"
            placeholder="Search client, invoice #, amount..."
            type="text"
          />
          {searchQuery && (
            <button
              onClick={() => setSearchQuery('')}
              className="w-7 h-7 flex items-center justify-center text-[#76777d] hover:text-[#191c1e] mr-1"
            >
              <span className="material-symbols-outlined text-[18px]">cancel</span>
            </button>
          )}
          <div className="flex items-center gap-1">
            <button
              onClick={() => setSortOrder(sortOrder === 'asc' ? 'desc' : 'asc')}
              aria-label="Sort direction"
              title="Toggle sort direction"
              className="w-8 h-8 flex items-center justify-center rounded-full text-[#45464c] hover:bg-[#eceef0] transition-colors active:scale-95"
            >
              <span className="material-symbols-outlined text-[19px]">
                {sortOrder === 'asc' ? 'arrow_downward' : 'arrow_upward'}
              </span>
            </button>
          </div>
        </div>

        {/* Scrollable Filter Chips */}
        <div className="flex items-center gap-2 overflow-x-auto no-scrollbar py-1 -mx-4 px-4">
          <button
            onClick={() => setStatusFilter('all')}
            className={`flex-shrink-0 flex items-center gap-1.5 h-9 px-4 rounded-full text-[13px] font-medium shadow-xs transition-all ${
              statusFilter === 'all'
                ? 'bg-[#111827] text-white font-semibold'
                : 'bg-white text-[#191c1e] border border-[#e6e8ea]'
            }`}
          >
            <span>All</span>
            <span className="bg-white/20 px-1.5 py-0.5 rounded-full text-[11px] font-semibold">
              {payments.length}
            </span>
          </button>

          <button
            onClick={() => setStatusFilter('overdue')}
            className={`flex-shrink-0 flex items-center gap-1.5 h-9 px-3.5 rounded-full text-[13px] font-medium shadow-xs transition-all ${
              statusFilter === 'overdue'
                ? 'bg-[#ba1a1a] text-white font-semibold'
                : 'bg-white text-[#191c1e] border border-[#e6e8ea]'
            }`}
          >
            <span className="w-2 h-2 rounded-full bg-[#ba1a1a]"></span>
            <span>Overdue</span>
            <span className="text-[#ba1a1a] font-bold text-[11px]">
              ({metrics.overdueInvoicesCount})
            </span>
          </button>

          <button
            onClick={() => setStatusFilter('pending')}
            className={`flex-shrink-0 flex items-center gap-1.5 h-9 px-3.5 rounded-full text-[13px] font-medium shadow-xs transition-all ${
              statusFilter === 'pending'
                ? 'bg-[#4648d4] text-white font-semibold'
                : 'bg-white text-[#191c1e] border border-[#e6e8ea]'
            }`}
          >
            <span className="w-2 h-2 rounded-full bg-[#4648d4]"></span>
            <span>Pending</span>
            <span className="text-[#4648d4] font-bold text-[11px]">
              ({payments.filter((p) => p.status === 'pending' || p.status === 'due_soon').length})
            </span>
          </button>

          <button
            onClick={() => setStatusFilter('paid')}
            className={`flex-shrink-0 flex items-center gap-1.5 h-9 px-3.5 rounded-full text-[13px] font-medium shadow-xs transition-all ${
              statusFilter === 'paid'
                ? 'bg-[#009668] text-white font-semibold'
                : 'bg-white text-[#191c1e] border border-[#e6e8ea]'
            }`}
          >
            <span className="w-2 h-2 rounded-full bg-[#009668]"></span>
            <span>Paid</span>
            <span className="text-[#009668] font-bold text-[11px]">
              ({payments.filter((p) => p.status === 'paid').length})
            </span>
          </button>
        </div>
      </div>

      {/* Summary Banner Bar */}
      <div className="px-4 py-2.5 my-1 flex items-center justify-between bg-[#f2f4f6]">
        <div className="flex items-center gap-2">
          <div className="w-2 h-2 rounded-full bg-[#ba1a1a] animate-pulse"></div>
          <div className="flex flex-col">
            <span className="text-[12px] font-bold text-[#191c1e] tabular-nums">
              {formatCurrency(metrics.totalOutstanding)} uncollected
            </span>
            <span className="text-[11px] text-[#45464c]">
              {payments.filter((p) => p.status !== 'paid').length} active invoices
            </span>
          </div>
        </div>
        <button
          onClick={() => setSortOrder(sortOrder === 'asc' ? 'desc' : 'asc')}
          className="flex items-center gap-1 px-2.5 py-1 rounded-lg bg-[#eceef0] text-[#191c1e] text-[11px] font-semibold active:bg-[#e0e3e5] transition-colors"
        >
          <span>Due date</span>
          <span className="material-symbols-outlined text-[15px]">
            {sortOrder === 'asc' ? 'arrow_downward' : 'arrow_upward'}
          </span>
        </button>
      </div>

      {/* Stacked Payment Cards List */}
      <div className="px-4 flex flex-col gap-3 pt-1">
        {filteredPayments.length === 0 ? (
          <div className="py-12 flex flex-col items-center justify-center text-center">
            <span className="material-symbols-outlined text-[44px] text-[#c6c6cd]">
              search_off
            </span>
            <p className="text-[15px] font-semibold text-[#191c1e] mt-2">No invoices found</p>
            <p className="text-[12px] text-[#45464c] max-w-xs mt-1">
              Try adjusting your search terms or filter chips.
            </p>
          </div>
        ) : (
          filteredPayments.map((payment) => {
            const remaining = Math.max(0, payment.amount - payment.amountPaid);
            const isSettled = payment.status === 'paid';
            const isOverdue = payment.status === 'overdue';
            const isPartial = payment.amountPaid > 0 && !isSettled;
            const progressPercent = Math.min(100, Math.round((payment.amountPaid / payment.amount) * 100));
            const badge = getStatusBadgeInfo(
              payment.status,
              payment.dueDate,
              payment.amount,
              payment.amountPaid
            );

            return (
              <div
                key={payment.id}
                className={`w-full bg-white rounded-2xl p-4 shadow-xs flex flex-col gap-3 relative overflow-hidden transition-all duration-200 border border-[#eceef0] ${
                  isSettled ? 'opacity-90' : ''
                }`}
              >
                {/* Status indicator bar on the left */}
                <div
                  className={`absolute left-0 top-0 bottom-0 w-1 ${
                    isOverdue ? 'bg-[#ba1a1a]' : isSettled ? 'bg-[#009668]' : 'bg-[#4648d4]'
                  }`}
                />

                {/* Top Row: Client + Balance */}
                <div
                  onClick={() => navigate('details', payment.id)}
                  className="flex items-start justify-between gap-3 cursor-pointer"
                >
                  <div className="flex items-center gap-3 min-w-0">
                    <div
                      className={`w-10 h-10 rounded-full flex items-center justify-center text-[13px] font-bold flex-shrink-0 ${
                        isOverdue
                          ? 'bg-[#ffdad6] text-[#93000a]'
                          : isSettled
                          ? 'bg-[#6ffbbe]/40 text-[#005236]'
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
                        {payment.scopeDescription} •{' '}
                        <span className="font-mono font-medium text-[#191c1e]">
                          #{payment.invoiceNumber}
                        </span>
                      </span>
                    </div>
                  </div>

                  <div className="flex flex-col items-end flex-shrink-0">
                    <span className="text-[17px] font-bold text-[#191c1e] leading-tight tabular-nums">
                      {formatCurrency(isSettled ? payment.amount : remaining, payment.currency)}
                    </span>
                    <span
                      className={`inline-flex items-center px-2 py-0.5 rounded-full text-[10px] font-semibold mt-1 ${badge.bg} ${badge.text}`}
                    >
                      {badge.label}
                    </span>
                  </div>
                </div>

                {/* Sub status row */}
                {isOverdue && (
                  <div className="flex items-center gap-2 px-3 py-1.5 rounded-xl bg-[#f2f4f6] text-[#45464c] text-[12px]">
                    <span className="material-symbols-outlined text-[16px] text-[#ba1a1a]">
                      notification_important
                    </span>
                    <span className="truncate">
                      Last reminder: {payment.lastReminderDate || '3 days ago'} via{' '}
                      {payment.lastReminderChannel || 'Email'}
                    </span>
                  </div>
                )}

                {/* Partial Progress Meter */}
                {isPartial && (
                  <div className="flex flex-col gap-1 pt-0.5">
                    <div className="flex justify-between items-center text-[12px]">
                      <span className="text-[#45464c]">
                        Paid:{' '}
                        <span className="font-semibold text-[#191c1e] tabular-nums">
                          {formatCurrency(payment.amountPaid, payment.currency)}
                        </span>{' '}
                        of {formatCurrency(payment.amount, payment.currency)}
                      </span>
                      <span className="font-semibold text-[#4648d4] tabular-nums">
                        {progressPercent}%
                      </span>
                    </div>
                    <div className="w-full h-2 rounded-full bg-[#e6e8ea] overflow-hidden">
                      <div
                        className="h-full bg-[#4648d4] rounded-full transition-all duration-300"
                        style={{ width: `${progressPercent}%` }}
                      />
                    </div>
                  </div>
                )}

                {/* Settled Full Banner */}
                {isSettled && (
                  <div className="flex items-center justify-between text-[12px] pt-0.5">
                    <span className="text-[#45464c] flex items-center gap-1.5">
                      <span className="material-symbols-outlined text-[17px] text-[#009668]">
                        verified
                      </span>
                      Settled via Stripe Instant
                    </span>
                    <button
                      onClick={() => openChaseModal(payment)}
                      className="text-[12px] text-[#4648d4] font-semibold hover:underline"
                    >
                      Send Thank-You
                    </button>
                  </div>
                )}

                {/* Action Buttons */}
                {!isSettled && (
                  <div className="flex items-center gap-2 pt-0.5">
                    <button
                      onClick={() => openChaseModal(payment)}
                      className="flex-1 h-10 px-3 rounded-full bg-[#4648d4] text-white text-[13px] font-semibold flex items-center justify-center gap-1.5 shadow-xs active:scale-95 transition-transform"
                    >
                      <span className="material-symbols-outlined text-[17px]">bolt</span>
                      <span>Chase Now</span>
                    </button>
                    <button
                      onClick={() => {
                        setPartialModalPayment(payment);
                        setPartialAmount('');
                      }}
                      className="h-10 px-3.5 rounded-full bg-[#e6e8ea] text-[#191c1e] text-[13px] font-medium hover:bg-[#e0e3e5] active:scale-95 transition-transform"
                    >
                      Record Partial
                    </button>
                    <button
                      onClick={() => markPaymentPaid(payment.id)}
                      className="h-10 px-3.5 rounded-full bg-[#eceef0] text-[#191c1e] text-[13px] font-medium hover:bg-[#e0e3e5] active:scale-95 transition-transform"
                    >
                      Mark Paid
                    </button>
                  </div>
                )}
              </div>
            );
          })
        )}
      </div>

      {/* Record Partial Modal */}
      {partialModalPayment && (
        <div className="fixed inset-0 z-50 flex items-center justify-center bg-black/40 backdrop-blur-xs p-4">
          <div className="w-full max-w-sm bg-white rounded-3xl p-5 shadow-2xl flex flex-col gap-4">
            <div className="flex items-center justify-between">
              <h3 className="text-[16px] font-bold text-[#191c1e]">Record Partial Payment</h3>
              <button
                onClick={() => setPartialModalPayment(null)}
                className="w-8 h-8 rounded-full bg-[#eceef0] flex items-center justify-center text-[#45464c]"
              >
                <span className="material-symbols-outlined text-[18px]">close</span>
              </button>
            </div>
            <p className="text-[13px] text-[#45464c]">
              Client: <strong>{partialModalPayment.clientName}</strong> (#{partialModalPayment.invoiceNumber})
              <br />
              Total amount: {formatCurrency(partialModalPayment.amount, partialModalPayment.currency)} (
              {formatCurrency(partialModalPayment.amount - partialModalPayment.amountPaid)} remaining)
            </p>
            <form onSubmit={handlePartialSubmit} className="flex flex-col gap-3">
              <div>
                <label className="text-[12px] font-medium text-[#76777d]">Amount Received ($)</label>
                <input
                  type="number"
                  step="0.01"
                  required
                  autoFocus
                  placeholder="e.g. 500.00"
                  value={partialAmount}
                  onChange={(e) => setPartialAmount(e.target.value)}
                  className="w-full h-12 px-3 mt-1 rounded-xl bg-[#f2f4f6] text-[16px] font-bold text-[#191c1e] outline-none border border-[#e6e8ea]"
                />
              </div>
              <div className="flex gap-2 pt-2">
                <button
                  type="button"
                  onClick={() => setPartialModalPayment(null)}
                  className="flex-1 h-11 rounded-full bg-[#eceef0] text-[#191c1e] text-[13px] font-semibold"
                >
                  Cancel
                </button>
                <button
                  type="submit"
                  className="flex-1 h-11 rounded-full bg-[#4648d4] text-white text-[13px] font-semibold shadow-sm"
                >
                  Confirm Payment
                </button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  );
};
