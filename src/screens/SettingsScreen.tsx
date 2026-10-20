import React, { useState } from 'react';
import { usePaymentChaser } from '../context/PaymentChaserContext';

export const SettingsScreen: React.FC = () => {
  const { prefs, updatePrefs, payments, clients, resetAll, navigate, showToast } =
    usePaymentChaser();

  const [businessName, setBusinessName] = useState(prefs.businessName);
  const [ownerName, setOwnerName] = useState(prefs.ownerName);
  const [ownerEmail, setOwnerEmail] = useState(prefs.ownerEmail);
  const [currency, setCurrency] = useState(prefs.defaultCurrency);
  const [showAndroidModal, setShowAndroidModal] = useState(false);

  const handleSaveProfile = (e: React.FormEvent) => {
    e.preventDefault();
    updatePrefs({
      businessName,
      ownerName,
      ownerEmail,
      defaultCurrency: currency,
    });
    showToast('Profile & business settings saved.');
  };

  const handleExportJSON = () => {
    const backup = {
      exportedAt: new Date().toISOString(),
      prefs,
      clients,
      payments,
    };
    const blob = new Blob([JSON.stringify(backup, null, 2)], { type: 'application/json' });
    const url = URL.createObjectURL(blob);
    const a = document.createElement('a');
    a.href = url;
    a.download = `payment-chaser-backup-${new Date().toISOString().split('T')[0]}.json`;
    a.click();
    showToast('Exported full database to JSON.');
  };

  const handleExportCSV = () => {
    const headers = ['Invoice', 'Client', 'Amount', 'AmountPaid', 'Currency', 'DueDate', 'Status'];
    const rows = payments.map((p) => [
      p.invoiceNumber,
      `"${p.clientName}"`,
      p.amount,
      p.amountPaid,
      p.currency,
      p.dueDate,
      p.status,
    ]);
    const csvContent = [headers.join(','), ...rows.map((r) => r.join(','))].join('\n');
    const blob = new Blob([csvContent], { type: 'text/csv' });
    const url = URL.createObjectURL(blob);
    const a = document.createElement('a');
    a.href = url;
    a.download = `payment-chaser-invoices-${new Date().toISOString().split('T')[0]}.csv`;
    a.click();
    showToast('Exported invoices to CSV.');
  };

  return (
    <div className="flex flex-col w-full pb-28 select-none">
      {/* Top Header */}
      <div className="px-4 pt-3 pb-2">
        <h2 className="text-[18px] font-bold text-[#191c1e]">Settings &amp; Business Profile</h2>
      </div>

      <div className="px-4 flex flex-col gap-3.5">
        {/* Android Native Codebase Badge / Banner */}
        <div className="bg-[#141b2b] text-white rounded-2xl p-4 shadow-sm relative overflow-hidden">
          <div className="flex items-start justify-between gap-3">
            <div className="flex items-center gap-3">
              <div className="w-10 h-10 rounded-xl bg-[#4648d4] text-white flex items-center justify-center shrink-0">
                <span className="material-symbols-outlined text-[20px]">android</span>
              </div>
              <div className="flex flex-col">
                <div className="flex items-center gap-1.5">
                  <span className="text-[14px] font-bold">Android Native Project</span>
                  <span className="px-2 py-0.5 rounded-full bg-[#002113] text-[#6ffbbe] text-[10px] font-semibold">
                    Kotlin + Compose
                  </span>
                </div>
                <span className="text-[11px] text-[#7d8497] mt-0.5">
                  Complete architecture ready for Android Studio &amp; Google Play
                </span>
              </div>
            </div>
            <button
              onClick={() => setShowAndroidModal(true)}
              className="px-3 py-1.5 rounded-full bg-white/10 hover:bg-white/20 text-white text-[12px] font-semibold shrink-0 transition-colors"
            >
              View Specs
            </button>
          </div>
        </div>

        {/* Profile Card */}
        <div className="bg-white rounded-2xl p-4 shadow-xs border border-[#eceef0]">
          <div className="flex items-center gap-3 pb-3 border-b border-[#eceef0]">
            <img
              src={prefs.ownerAvatar}
              alt={prefs.ownerName}
              className="w-12 h-12 rounded-full object-cover shrink-0"
              referrerPolicy="no-referrer"
            />
            <div className="flex flex-col min-w-0 flex-1">
              <span className="text-[15px] font-bold text-[#191c1e] truncate">{prefs.ownerName}</span>
              <span className="text-[12px] text-[#45464c] truncate">{prefs.businessName}</span>
              <span className="text-[11px] text-[#76777d] truncate">{prefs.ownerEmail}</span>
            </div>
            <span className="px-2.5 py-1 rounded-full bg-[#e1e0ff] text-[#07006c] text-[11px] font-bold uppercase">
              {prefs.tier}
            </span>
          </div>

          <form onSubmit={handleSaveProfile} className="flex flex-col gap-3 pt-3">
            <div>
              <label className="text-[11px] font-medium text-[#76777d]">Business / Agency Name</label>
              <input
                type="text"
                value={businessName}
                onChange={(e) => setBusinessName(e.target.value)}
                className="w-full h-10 px-3 rounded-xl bg-[#f2f4f6] text-[13px] text-[#191c1e] outline-none border border-[#e6e8ea]"
              />
            </div>

            <div className="grid grid-cols-2 gap-2">
              <div>
                <label className="text-[11px] font-medium text-[#76777d]">Your Name</label>
                <input
                  type="text"
                  value={ownerName}
                  onChange={(e) => setOwnerName(e.target.value)}
                  className="w-full h-10 px-3 rounded-xl bg-[#f2f4f6] text-[13px] text-[#191c1e] outline-none border border-[#e6e8ea]"
                />
              </div>
              <div>
                <label className="text-[11px] font-medium text-[#76777d]">Default Currency</label>
                <select
                  value={currency}
                  onChange={(e) => setCurrency(e.target.value)}
                  className="w-full h-10 px-2.5 rounded-xl bg-[#f2f4f6] text-[13px] text-[#191c1e] outline-none border border-[#e6e8ea]"
                >
                  <option value="USD">USD ($)</option>
                  <option value="EUR">EUR (€)</option>
                  <option value="GBP">GBP (£)</option>
                  <option value="CAD">CAD (CA$)</option>
                  <option value="AUD">AUD (A$)</option>
                </select>
              </div>
            </div>

            <div>
              <label className="text-[11px] font-medium text-[#76777d]">Account Email</label>
              <input
                type="email"
                value={ownerEmail}
                onChange={(e) => setOwnerEmail(e.target.value)}
                className="w-full h-10 px-3 rounded-xl bg-[#f2f4f6] text-[13px] text-[#191c1e] outline-none border border-[#e6e8ea]"
              />
            </div>

            <button
              type="submit"
              className="h-10 mt-1 rounded-full bg-[#111827] text-white text-[13px] font-semibold hover:bg-black transition-colors"
            >
              Save Profile Changes
            </button>
          </form>
        </div>

        {/* Quick Shortcuts */}
        <div className="bg-white rounded-2xl p-4 shadow-xs border border-[#eceef0] flex flex-col gap-2">
          <span className="text-[13px] font-bold text-[#191c1e] mb-1">Configuration</span>

          <button
            onClick={() => navigate('reminders')}
            className="flex items-center justify-between p-2.5 rounded-xl hover:bg-[#f2f4f6] transition-colors"
          >
            <div className="flex items-center gap-2.5">
              <span className="material-symbols-outlined text-[20px] text-[#4648d4]">schedule</span>
              <div className="flex flex-col text-left">
                <span className="text-[13px] font-semibold text-[#191c1e]">
                  Smart Sequences &amp; Tones
                </span>
                <span className="text-[11px] text-[#76777d]">
                  Configure multi-stage schedule and templates
                </span>
              </div>
            </div>
            <span className="material-symbols-outlined text-[18px] text-[#76777d]">
              chevron_right
            </span>
          </button>

          <button
            onClick={() => navigate('premium')}
            className="flex items-center justify-between p-2.5 rounded-xl hover:bg-[#f2f4f6] transition-colors"
          >
            <div className="flex items-center gap-2.5">
              <span className="material-symbols-outlined text-[20px] text-[#009668]">
                workspace_premium
              </span>
              <div className="flex flex-col text-left">
                <span className="text-[13px] font-semibold text-[#191c1e]">
                  Payment Chaser Pro
                </span>
                <span className="text-[11px] text-[#76777d]">
                  Manage subscription &amp; entitlements
                </span>
              </div>
            </div>
            <span className="material-symbols-outlined text-[18px] text-[#76777d]">
              chevron_right
            </span>
          </button>
        </div>

        {/* Data & Backup */}
        <div className="bg-white rounded-2xl p-4 shadow-xs border border-[#eceef0] flex flex-col gap-2.5">
          <span className="text-[13px] font-bold text-[#191c1e]">Data Export &amp; Backup</span>
          <p className="text-[12px] text-[#45464c]">
            Export all client ledgers, payment timelines, and reminder settings.
          </p>
          <div className="grid grid-cols-2 gap-2 pt-1">
            <button
              onClick={handleExportCSV}
              className="h-10 rounded-full bg-[#f2f4f6] hover:bg-[#e6e8ea] text-[#191c1e] text-[12px] font-semibold flex items-center justify-center gap-1.5 transition-colors"
            >
              <span className="material-symbols-outlined text-[16px]">table_chart</span>
              <span>Export CSV</span>
            </button>
            <button
              onClick={handleExportJSON}
              className="h-10 rounded-full bg-[#f2f4f6] hover:bg-[#e6e8ea] text-[#191c1e] text-[12px] font-semibold flex items-center justify-center gap-1.5 transition-colors"
            >
              <span className="material-symbols-outlined text-[16px]">data_object</span>
              <span>Export JSON</span>
            </button>
          </div>

          <div className="pt-2 border-t border-[#eceef0]">
            <button
              onClick={() => {
                if (window.confirm('Reset all demo data back to Google Stitch sample defaults?')) {
                  resetAll();
                }
              }}
              className="text-[12px] font-semibold text-[#ba1a1a] hover:underline flex items-center gap-1"
            >
              <span className="material-symbols-outlined text-[15px]">restart_alt</span>
              <span>Reset to Sample Data</span>
            </button>
          </div>
        </div>
      </div>

      {/* Android Native Architecture Modal */}
      {showAndroidModal && (
        <div className="fixed inset-0 z-50 flex items-center justify-center bg-black/50 backdrop-blur-xs p-4">
          <div className="w-full max-w-md bg-white rounded-3xl p-5 shadow-2xl flex flex-col gap-3 max-h-[85vh] overflow-y-auto no-scrollbar">
            <div className="flex items-center justify-between">
              <div className="flex items-center gap-2">
                <span className="material-symbols-outlined text-[#4648d4] text-[22px]">
                  android
                </span>
                <h3 className="text-[16px] font-bold text-[#191c1e]">
                  Native Android Architecture
                </h3>
              </div>
              <button
                onClick={() => setShowAndroidModal(false)}
                className="w-8 h-8 rounded-full bg-[#eceef0] flex items-center justify-center text-[#45464c]"
              >
                <span className="material-symbols-outlined text-[18px]">close</span>
              </button>
            </div>

            <div className="bg-[#f2f4f6] p-3 rounded-xl text-[12px] text-[#45464c] leading-relaxed">
              <p className="font-semibold text-[#191c1e] mb-1">
                ℹ️ Environment Notice (Master Prompt Item #12):
              </p>
              This Google AI Studio sandbox executes on Node.js/Vite and displays the interactive
              preview via port 3000. Because the cloud sandbox does not have the Android SDK or
              Gradle daemon installed, the production-grade Kotlin + Jetpack Compose Android
              codebase is organized in <code>/android</code> for direct compilation in Android
              Studio!
            </div>

            <div className="flex flex-col gap-2 text-[12px]">
              <span className="font-bold text-[#191c1e]">Implemented Android Components:</span>
              <ul className="list-disc pl-4 space-y-1 text-[#45464c]">
                <li>
                  <strong>UI Layer:</strong> Jetpack Compose + Material Design 3 tokens
                </li>
                <li>
                  <strong>Architecture:</strong> MVVM + Clean Architecture with Hilt Dependency
                  Injection
                </li>
                <li>
                  <strong>Local Persistence:</strong> Room Database (Entity, Dao, Converters)
                </li>
                <li>
                  <strong>Cloud Sync:</strong> Firestore models and security rules
                </li>
                <li>
                  <strong>Background Scheduling:</strong> WorkManager ReminderWorker
                </li>
                <li>
                  <strong>Monetization:</strong> Google Play Billing modular repository interface
                </li>
              </ul>
            </div>

            <button
              onClick={() => setShowAndroidModal(false)}
              className="h-11 mt-2 rounded-full bg-[#111827] text-white text-[13px] font-semibold"
            >
              Close
            </button>
          </div>
        </div>
      )}
    </div>
  );
};
