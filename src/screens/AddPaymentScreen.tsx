import React, { useState } from 'react';
import { usePaymentChaser } from '../context/PaymentChaserContext';
import { Client } from '../types';

export const AddPaymentScreen: React.FC = () => {
  const { clients, addPayment, addClient, navigate, prefs, showToast } = usePaymentChaser();

  // State
  const [amount, setAmount] = useState<string>('');
  const [currency, setCurrency] = useState<string>(prefs.defaultCurrency);
  const [selectedClient, setSelectedClient] = useState<Client>(clients[0] || {
    id: 'default',
    name: 'Acme Studio',
    company: 'Acme Studio',
    email: 'contact@acmestudio.com',
    phone: '+1 (555) 019-2834',
    initials: 'AS',
    createdAt: '2025-10-01',
  });
  const [clientSearch, setClientSearch] = useState<string>(clients[0]?.name || 'Acme Studio');
  const [showNewClientForm, setShowNewClientForm] = useState(false);
  const [newClientName, setNewClientName] = useState('');
  const [newClientEmail, setNewClientEmail] = useState('');
  const [newClientPhone, setNewClientPhone] = useState('');

  // Date and presets
  const today = new Date();
  const defaultDueDate = new Date(today.getTime() + 14 * 86400000).toISOString().split('T')[0];
  const [dueDate, setDueDate] = useState<string>(defaultDueDate);
  const [dueTag, setDueTag] = useState<string>('Net 14 Days');

  // Metadata
  const [invoiceNumber, setInvoiceNumber] = useState<string>(
    `INV-${today.getFullYear()}-${Math.floor(100 + Math.random() * 900)}`
  );
  const [scopeDescription, setScopeDescription] = useState<string>(
    'Q4 Design System Sprint & Brand Tokens'
  );
  const [attachmentFileName, setAttachmentFileName] = useState<string | null>(null);

  // Smart sequence settings
  const [smartSequence, setSmartSequence] = useState<boolean>(true);
  const [emailChannel, setEmailChannel] = useState<boolean>(true);
  const [whatsappChannel, setWhatsappChannel] = useState<boolean>(true);
  const [smsChannel, setSmsChannel] = useState<boolean>(false);

  // Submission state
  const [isSubmitting, setIsSubmitting] = useState(false);

  const handleSetAmountPreset = (val: number) => {
    const current = parseFloat(amount) || 0;
    setAmount((current + val).toFixed(2));
  };

  const handleSetPresetDate = (label: string, days: number | null) => {
    setDueTag(label);
    if (days === null) {
      // Custom date focus
      return;
    }
    const d = new Date();
    d.setDate(d.getDate() + days);
    setDueDate(d.toISOString().split('T')[0]);
  };

  const handleCreateNewClient = (e: React.FormEvent) => {
    e.preventDefault();
    if (!newClientName.trim()) return;
    const newId = addClient({
      name: newClientName.trim(),
      email: newClientEmail.trim() || 'billing@client.com',
      phone: newClientPhone.trim() || '+1 (555) 000-0000',
      initials: newClientName.slice(0, 2).toUpperCase(),
    });
    const created = clients.find((c) => c.id === newId) || {
      id: newId,
      name: newClientName.trim(),
      email: newClientEmail.trim(),
      phone: newClientPhone.trim(),
      initials: newClientName.slice(0, 2).toUpperCase(),
      createdAt: new Date().toISOString(),
    };
    setSelectedClient(created);
    setClientSearch(created.name);
    setShowNewClientForm(false);
  };

  const handleFileUpload = (e: React.ChangeEvent<HTMLInputElement>) => {
    const file = e.target.files?.[0];
    if (file) {
      setAttachmentFileName(file.name);
      showToast(`Attached: ${file.name}`);
    }
  };

  const handleSubmit = (isDraft = false) => {
    const parsedAmount = parseFloat(amount);
    if (isNaN(parsedAmount) || parsedAmount <= 0) {
      showToast('Please enter an amount to collect.');
      return;
    }

    setIsSubmitting(true);
    setTimeout(() => {
      addPayment({
        invoiceNumber,
        clientId: selectedClient.id,
        clientName: selectedClient.name,
        clientEmail: selectedClient.email,
        clientPhone: selectedClient.phone,
        clientInitials: selectedClient.initials,
        scopeDescription,
        amount: parsedAmount,
        currency,
        dueDate,
        smartSequenceEnabled: smartSequence,
        channels: {
          email: emailChannel,
          whatsapp: whatsappChannel,
          sms: smsChannel,
        },
        attachmentName: attachmentFileName || undefined,
        isDraft,
      });

      setIsSubmitting(false);
      navigate('payments');
    }, 500);
  };

  return (
    <div className="flex flex-col w-full pb-36 select-none">
      {/* Top App Sub-Bar for Quick Context */}
      <div className="flex items-center justify-between px-4 py-2.5 bg-[#f7f9fb]">
        <div className="flex items-center gap-2">
          <button
            onClick={() => navigate('home')}
            className="w-9 h-9 flex items-center justify-center rounded-full bg-[#eceef0] text-[#191c1e] hover:bg-[#e0e3e5] transition-colors"
          >
            <span className="material-symbols-outlined text-[19px]">close</span>
          </button>
          <div>
            <p className="text-[14px] font-bold text-[#191c1e]">Track New Payment</p>
            <span className="text-[11px] text-[#45464c] flex items-center gap-1">
              <span className="w-1.5 h-1.5 rounded-full bg-[#6063ee]"></span>
              Lightning Log &lt; 30s
            </span>
          </div>
        </div>

        <button
          onClick={() => handleSubmit(true)}
          className="h-8 px-3 rounded-full bg-[#e1e0ff] text-[#07006c] text-[11px] font-semibold flex items-center gap-1 shadow-xs active:scale-95 transition-transform"
        >
          <span className="material-symbols-outlined text-[15px]">drafts</span>
          Save Draft
        </button>
      </div>

      <div className="flex flex-col gap-3.5 px-4 mt-2">
        {/* Fast Amount Hero Card */}
        <section className="bg-white rounded-2xl p-4 shadow-xs border border-[#eceef0] flex flex-col gap-2 relative overflow-hidden">
          <div className="flex items-center justify-between">
            <label className="text-[12px] font-medium text-[#45464c]">Amount to Collect</label>
            {/* Currency Pill Dropdown */}
            <div className="relative">
              <select
                value={currency}
                onChange={(e) => setCurrency(e.target.value)}
                className="h-8 pl-3 pr-7 rounded-full bg-[#eceef0] text-[#191c1e] text-[12px] font-semibold appearance-none cursor-pointer outline-none border-0"
              >
                <option value="USD">USD ($)</option>
                <option value="EUR">EUR (€)</option>
                <option value="GBP">GBP (£)</option>
                <option value="CAD">CAD (CA$)</option>
              </select>
              <span className="material-symbols-outlined absolute right-1.5 top-1/2 -translate-y-1/2 text-[16px] pointer-events-none text-[#45464c]">
                arrow_drop_down
              </span>
            </div>
          </div>

          {/* Massive Quick Numerical Entry */}
          <div className="flex items-baseline gap-1 py-1">
            <span className="text-[34px] font-bold text-[#111827] select-none">$</span>
            <input
              type="number"
              inputMode="decimal"
              step="0.01"
              autoFocus
              placeholder="0.00"
              value={amount}
              onChange={(e) => setAmount(e.target.value)}
              className="text-[36px] font-bold text-[#111827] bg-transparent focus:outline-none w-full placeholder:text-[#c6c6cd] tracking-tight tabular-nums"
            />
          </div>

          {/* Presets Horizon Ribbon */}
          <div className="flex items-center gap-2 overflow-x-auto no-scrollbar py-1">
            {[500, 1000, 2500, 5000].map((preset) => (
              <button
                key={preset}
                type="button"
                onClick={() => handleSetAmountPreset(preset)}
                className="h-8 px-3 rounded-full bg-[#f2f4f6] text-[#191c1e] text-[12px] font-medium hover:bg-[#e6e8ea] active:bg-[#111827] active:text-white transition-colors flex-shrink-0"
              >
                +${preset.toLocaleString()}
              </button>
            ))}
          </div>
        </section>

        {/* Client Selection & Quick Add */}
        <section className="bg-white rounded-2xl p-4 shadow-xs border border-[#eceef0] flex flex-col gap-2.5">
          <div className="flex items-center justify-between">
            <label className="text-[13px] font-bold text-[#191c1e] flex items-center gap-1.5">
              <span className="material-symbols-outlined text-[17px] text-[#4648d4]">
                person_search
              </span>
              Client Counterparty
            </label>
            <span className="text-[11px] text-[#76777d]">Tap to autofill</span>
          </div>

          {/* Searchable Field */}
          <div className="relative w-full">
            <span className="material-symbols-outlined absolute left-3 top-1/2 -translate-y-1/2 text-[#76777d] text-[18px]">
              search
            </span>
            <input
              type="text"
              value={clientSearch}
              onChange={(e) => setClientSearch(e.target.value)}
              placeholder="Search or type client name..."
              className="w-full h-11 pl-9 pr-8 rounded-xl bg-[#f2f4f6] text-[#191c1e] text-[13px] focus:outline-none focus:bg-white focus:ring-1 focus:ring-[#4648d4] transition-all"
            />
            {clientSearch && (
              <button
                onClick={() => setClientSearch('')}
                className="material-symbols-outlined absolute right-2.5 top-1/2 -translate-y-1/2 text-[#76777d] hover:text-[#191c1e] text-[16px]"
              >
                cancel
              </button>
            )}
          </div>

          {/* Quick Recent Clients */}
          <div className="flex items-center gap-1.5 overflow-x-auto no-scrollbar py-1">
            {clients.slice(0, 4).map((c) => {
              const isSelected = selectedClient.id === c.id;
              return (
                <button
                  key={c.id}
                  type="button"
                  onClick={() => {
                    setSelectedClient(c);
                    setClientSearch(c.name);
                  }}
                  className={`h-8 px-3 rounded-full text-[12px] font-medium flex-shrink-0 flex items-center gap-1 transition-colors ${
                    isSelected
                      ? 'bg-[#111827] text-white font-semibold'
                      : 'bg-[#f2f4f6] text-[#191c1e] hover:bg-[#e6e8ea]'
                  }`}
                >
                  {isSelected && <span className="material-symbols-outlined text-[13px]">check</span>}
                  <span>{c.name}</span>
                </button>
              );
            })}
            <button
              type="button"
              onClick={() => setShowNewClientForm(!showNewClientForm)}
              className="h-8 px-3 rounded-full bg-[#e1e0ff] text-[#07006c] text-[12px] font-semibold flex-shrink-0 flex items-center gap-1 hover:bg-[#c0c1ff] transition-colors"
            >
              <span className="material-symbols-outlined text-[14px]">add</span>
              <span>New Client</span>
            </button>
          </div>

          {/* New Client Inline Modal Form */}
          {showNewClientForm && (
            <div className="p-3 bg-[#f2f4f6] rounded-xl flex flex-col gap-2 mt-1">
              <span className="text-[12px] font-bold text-[#191c1e]">Add New Client</span>
              <input
                type="text"
                placeholder="Full Client Name *"
                value={newClientName}
                onChange={(e) => setNewClientName(e.target.value)}
                className="w-full h-9 px-3 rounded-lg bg-white text-[13px] outline-none border border-[#e6e8ea]"
              />
              <div className="grid grid-cols-2 gap-2">
                <input
                  type="email"
                  placeholder="Billing Email"
                  value={newClientEmail}
                  onChange={(e) => setNewClientEmail(e.target.value)}
                  className="w-full h-9 px-3 rounded-lg bg-white text-[12px] outline-none border border-[#e6e8ea]"
                />
                <input
                  type="tel"
                  placeholder="Phone (WhatsApp)"
                  value={newClientPhone}
                  onChange={(e) => setNewClientPhone(e.target.value)}
                  className="w-full h-9 px-3 rounded-lg bg-white text-[12px] outline-none border border-[#e6e8ea]"
                />
              </div>
              <div className="flex justify-end gap-2 pt-1">
                <button
                  type="button"
                  onClick={() => setShowNewClientForm(false)}
                  className="px-3 py-1 rounded-md text-[12px] text-[#45464c]"
                >
                  Cancel
                </button>
                <button
                  type="button"
                  onClick={handleCreateNewClient}
                  className="px-3 py-1 rounded-md bg-[#4648d4] text-white text-[12px] font-semibold"
                >
                  Save Client
                </button>
              </div>
            </div>
          )}

          {/* Auto-fill Client Info Pill Card */}
          <div className="flex items-center gap-2.5 p-2.5 bg-[#f2f4f6] rounded-xl mt-0.5">
            <div className="w-8 h-8 rounded-full bg-[#6063ee] text-white flex items-center justify-center font-bold text-[12px] flex-shrink-0">
              {selectedClient.initials}
            </div>
            <div className="flex flex-col min-w-0 flex-1">
              <p className="text-[13px] font-bold text-[#191c1e] truncate">{selectedClient.name}</p>
              <p className="text-[11px] text-[#45464c] truncate">
                {selectedClient.email} • {selectedClient.phone}
              </p>
            </div>
            <span className="material-symbols-outlined text-[#009668] text-[18px] shrink-0">
              verified
            </span>
          </div>
        </section>

        {/* Due Date & Speed Presets */}
        <section className="bg-white rounded-2xl p-4 shadow-xs border border-[#eceef0] flex flex-col gap-2.5">
          <div className="flex items-center justify-between">
            <label className="text-[13px] font-bold text-[#191c1e] flex items-center gap-1.5">
              <span className="material-symbols-outlined text-[17px] text-[#4648d4]">event</span>
              Due Date
            </label>
            <span className="text-[11px] px-2 py-0.5 rounded-full bg-[#ffdad6] text-[#93000a] font-semibold">
              {dueTag}
            </span>
          </div>

          {/* Interactive Date Input */}
          <div className="flex items-center gap-2 bg-[#f2f4f6] rounded-xl px-3 h-11 border border-[#e6e8ea]">
            <span className="material-symbols-outlined text-[#76777d] text-[18px]">
              calendar_month
            </span>
            <input
              type="date"
              value={dueDate}
              onChange={(e) => {
                setDueDate(e.target.value);
                setDueTag('Custom Date');
              }}
              className="bg-transparent text-[13px] font-semibold text-[#191c1e] focus:outline-none w-full"
            />
          </div>

          {/* Quick speed preset buttons */}
          <div className="grid grid-cols-5 gap-1.5 pt-1">
            <button
              type="button"
              onClick={() => handleSetPresetDate('Today', 0)}
              className={`h-8 rounded-lg text-[12px] font-medium transition-colors ${
                dueTag === 'Today' ? 'bg-[#111827] text-white' : 'bg-[#f2f4f6] text-[#191c1e]'
              }`}
            >
              Today
            </button>
            <button
              type="button"
              onClick={() => handleSetPresetDate('Net 7', 7)}
              className={`h-8 rounded-lg text-[12px] font-medium transition-colors ${
                dueTag === 'Net 7' ? 'bg-[#111827] text-white' : 'bg-[#f2f4f6] text-[#191c1e]'
              }`}
            >
              Net 7
            </button>
            <button
              type="button"
              onClick={() => handleSetPresetDate('Net 14 Days', 14)}
              className={`h-8 rounded-lg text-[12px] font-medium transition-colors ${
                dueTag === 'Net 14 Days' ? 'bg-[#111827] text-white' : 'bg-[#f2f4f6] text-[#191c1e]'
              }`}
            >
              Net 14
            </button>
            <button
              type="button"
              onClick={() => handleSetPresetDate('Net 30', 30)}
              className={`h-8 rounded-lg text-[12px] font-medium transition-colors ${
                dueTag === 'Net 30' ? 'bg-[#111827] text-white' : 'bg-[#f2f4f6] text-[#191c1e]'
              }`}
            >
              Net 30
            </button>
            <button
              type="button"
              onClick={() => handleSetPresetDate('Custom', null)}
              className={`h-8 rounded-lg text-[12px] font-medium transition-colors ${
                dueTag === 'Custom' || dueTag === 'Custom Date'
                  ? 'bg-[#111827] text-white'
                  : 'bg-[#f2f4f6] text-[#191c1e]'
              }`}
            >
              Custom
            </button>
          </div>
        </section>

        {/* Optional Metadata Accordion */}
        <details className="group bg-white rounded-2xl p-4 shadow-xs border border-[#eceef0] overflow-hidden transition-all">
          <summary className="flex items-center justify-between cursor-pointer list-none select-none">
            <div className="flex items-center gap-2">
              <span className="material-symbols-outlined text-[#4648d4] text-[18px]">
                receipt_long
              </span>
              <span className="text-[13px] font-bold text-[#191c1e]">
                Invoice Details & Attachments
              </span>
              <span className="text-[10px] text-[#45464c] bg-[#f2f4f6] px-2 py-0.5 rounded-full font-medium">
                Optional
              </span>
            </div>
            <span className="material-symbols-outlined text-[#76777d] group-open:rotate-180 transition-transform text-[18px]">
              expand_more
            </span>
          </summary>

          <div className="flex flex-col gap-3 pt-3">
            {/* Reference Number */}
            <div>
              <label className="text-[11px] font-medium text-[#45464c] block mb-1">
                Invoice Reference #
              </label>
              <div className="flex items-center bg-[#f2f4f6] rounded-xl px-3 h-10 border border-[#e6e8ea]">
                <span className="material-symbols-outlined text-[#76777d] text-[16px] mr-2">tag</span>
                <input
                  type="text"
                  value={invoiceNumber}
                  onChange={(e) => setInvoiceNumber(e.target.value)}
                  className="bg-transparent text-[13px] text-[#191c1e] focus:outline-none w-full font-mono"
                />
                <button
                  type="button"
                  onClick={() =>
                    setInvoiceNumber(
                      `INV-${new Date().getFullYear()}-${Math.floor(100 + Math.random() * 900)}`
                    )
                  }
                  className="text-[#4648d4] text-[11px] font-bold hover:underline"
                >
                  Auto
                </button>
              </div>
            </div>

            {/* Scope / Description */}
            <div>
              <label className="text-[11px] font-medium text-[#45464c] block mb-1">
                Scope / Project Description
              </label>
              <div className="bg-[#f2f4f6] rounded-xl p-2.5 border border-[#e6e8ea]">
                <textarea
                  rows={2}
                  value={scopeDescription}
                  onChange={(e) => setScopeDescription(e.target.value)}
                  placeholder="e.g., Q4 Design System Sprint & Asset Handoff"
                  className="w-full bg-transparent text-[13px] text-[#191c1e] focus:outline-none resize-none"
                />
              </div>
            </div>

            {/* Quick Document Attach */}
            <div>
              <label className="w-full h-11 rounded-xl bg-[#eceef0] flex items-center justify-center gap-2 text-[#191c1e] text-[12px] font-semibold hover:bg-[#e0e3e5] cursor-pointer transition-colors">
                <span className="material-symbols-outlined text-[#4648d4] text-[18px]">
                  attachment
                </span>
                <span>{attachmentFileName || 'Attach PDF or Snap Photo'}</span>
                <span className="material-symbols-outlined text-[#76777d] text-[16px]">
                  photo_camera
                </span>
                <input
                  type="file"
                  accept="application/pdf,image/*"
                  onChange={handleFileUpload}
                  className="hidden"
                />
              </label>
            </div>
          </div>
        </details>

        {/* Smart Reminder Automation Switch (Delight Feature from Stitch) */}
        <section className="bg-gradient-to-br from-white to-[#f2f4f6] rounded-2xl p-4 shadow-xs border border-[#eceef0] flex flex-col gap-2.5">
          <div className="flex items-start justify-between gap-2">
            <div className="flex items-center gap-2">
              <div className="w-8 h-8 rounded-full bg-[#6063ee] text-white flex items-center justify-center flex-shrink-0 shadow-xs">
                <span className="material-symbols-outlined text-[18px]">notifications_active</span>
              </div>
              <div>
                <h3 className="text-[13px] font-bold text-[#191c1e]">Activate Smart Sequence</h3>
                <span className="text-[11px] text-[#009668] font-bold">94% on-time recovery rate</span>
              </div>
            </div>

            {/* Toggle switch */}
            <button
              type="button"
              onClick={() => setSmartSequence(!smartSequence)}
              className={`w-11 h-6 rounded-full transition-colors relative flex items-center px-0.5 shrink-0 ${
                smartSequence ? 'bg-[#6063ee]' : 'bg-[#d8dadc]'
              }`}
            >
              <div
                className={`w-5 h-5 rounded-full bg-white shadow-xs transform transition-transform ${
                  smartSequence ? 'translate-x-5' : 'translate-x-0'
                }`}
              />
            </button>
          </div>

          <p className="text-[12px] text-[#45464c] leading-relaxed">
            Polite multi-stage nudges: 3 days before, on the due date, and gentle escalations if
            delayed. Zero awkward conversations.
          </p>

          {/* Channel Selector Chips */}
          <div className="flex items-center gap-2 pt-1">
            <button
              type="button"
              onClick={() => setEmailChannel(!emailChannel)}
              className={`h-7 px-3 rounded-full text-[11px] font-semibold flex items-center gap-1 transition-colors ${
                emailChannel ? 'bg-[#111827] text-white' : 'bg-[#eceef0] text-[#45464c]'
              }`}
            >
              <span className="material-symbols-outlined text-[13px]">
                {emailChannel ? 'check' : 'add'}
              </span>
              <span>Email Nudge</span>
            </button>

            <button
              type="button"
              onClick={() => setWhatsappChannel(!whatsappChannel)}
              className={`h-7 px-3 rounded-full text-[11px] font-semibold flex items-center gap-1 transition-colors ${
                whatsappChannel ? 'bg-[#111827] text-white' : 'bg-[#eceef0] text-[#45464c]'
              }`}
            >
              <span className="material-symbols-outlined text-[13px]">
                {whatsappChannel ? 'check' : 'add'}
              </span>
              <span>WhatsApp Link</span>
            </button>

            <button
              type="button"
              onClick={() => setSmsChannel(!smsChannel)}
              className={`h-7 px-3 rounded-full text-[11px] font-semibold flex items-center gap-1 transition-colors ${
                smsChannel ? 'bg-[#111827] text-white' : 'bg-[#eceef0] text-[#45464c]'
              }`}
            >
              <span className="material-symbols-outlined text-[13px]">
                {smsChannel ? 'check' : 'add'}
              </span>
              <span>SMS Fallback</span>
            </button>
          </div>
        </section>
      </div>

      {/* Sticky Bottom M3 Action Tray */}
      <div className="fixed bottom-0 left-0 right-0 bg-white/95 backdrop-blur-md px-4 py-3 pb-8 flex flex-col gap-2 shadow-[0_-4px_16px_rgba(0,0,0,0.06)] z-30 max-w-md mx-auto">
        <button
          type="button"
          disabled={isSubmitting}
          onClick={() => handleSubmit(false)}
          className="w-full h-13 rounded-full bg-[#4648d4] hover:bg-[#3b3dbb] text-white text-[14px] font-bold flex items-center justify-center gap-2 shadow-lg active:scale-[0.99] transition-all disabled:opacity-75"
        >
          {isSubmitting ? (
            <>
              <span className="material-symbols-outlined animate-spin text-[18px]">
                progress_activity
              </span>
              <span>Tracking Engaged...</span>
            </>
          ) : (
            <>
              <span className="material-symbols-outlined text-[20px]">bolt</span>
              <span>Save &amp; Start Tracking</span>
            </>
          )}
        </button>

        <button
          type="button"
          onClick={() => handleSubmit(true)}
          className="w-full h-9 rounded-full bg-transparent text-[#45464c] hover:bg-[#eceef0] text-[12px] font-medium transition-colors"
        >
          Save as Unsent Draft
        </button>
      </div>
    </div>
  );
};
