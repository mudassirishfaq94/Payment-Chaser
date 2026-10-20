import React, { useState } from 'react';
import { usePaymentChaser } from '../context/PaymentChaserContext';

export const PremiumScreen: React.FC = () => {
  const { navigate, updatePrefs, showToast } = usePaymentChaser();
  const [billingPlan, setBillingPlan] = useState<'annual' | 'monthly'>('annual');

  const handleStartTrial = () => {
    updatePrefs({ tier: 'pro' });
    showToast('Payment Chaser Pro trial activated!');
    setTimeout(() => {
      navigate('home');
    }, 800);
  };

  return (
    <div className="flex flex-col w-full pb-36 select-none bg-white min-h-screen">
      {/* Top Dismiss Bar */}
      <div className="flex items-center justify-between px-4 py-3">
        <button
          onClick={() => navigate('home')}
          aria-label="Dismiss upgrade screen"
          className="w-10 h-10 flex items-center justify-center rounded-full bg-[#f2f4f6] hover:bg-[#eceef0] transition-colors text-[#191c1e]"
        >
          <span className="material-symbols-outlined text-[20px]">close</span>
        </button>
        <div className="flex items-center gap-1 px-3 py-1 rounded-full bg-[#e1e0ff] text-[#07006c]">
          <span className="material-symbols-outlined text-[15px] [font-variation-settings:'FILL'_1]">
            verified
          </span>
          <span className="text-[11px] font-bold uppercase tracking-wider">Risk-Free Trial</span>
        </div>
      </div>

      {/* Hero Banner */}
      <div className="px-4 pt-1 pb-3 flex flex-col items-center text-center">
        {/* Glowing Badge */}
        <div className="inline-flex items-center gap-1.5 px-3 py-1 rounded-full bg-[#141b2b] text-[#6ffbbe] shadow-sm mb-2">
          <span className="material-symbols-outlined text-[16px] [font-variation-settings:'FILL'_1]">
            bolt
          </span>
          <span className="text-[11px] uppercase tracking-widest font-bold">Payment Chaser Pro</span>
        </div>

        {/* Main Headline */}
        <h1 className="text-[24px] font-bold text-[#191c1e] mb-1.5 tracking-tight leading-tight">
          Get Paid 3x Faster with Zero Awkward Chasing
        </h1>

        {/* Subhead */}
        <p className="text-[13px] text-[#45464c] max-w-xs leading-relaxed">
          Unlock unlimited automated reminders, multi-channel messaging, and advanced cash flow
          forecasts.
        </p>
      </div>

      {/* Plan Switcher */}
      <div className="px-4 mb-4">
        <div className="bg-[#e6e8ea] p-1 rounded-full flex relative items-center">
          <button
            onClick={() => setBillingPlan('monthly')}
            className={`flex-1 py-2 rounded-full text-center text-[13px] transition-all duration-200 ${
              billingPlan === 'monthly'
                ? 'bg-[#111827] text-white font-bold shadow-xs'
                : 'text-[#45464c] font-medium'
            }`}
          >
            Monthly ($12/mo)
          </button>
          <button
            onClick={() => setBillingPlan('annual')}
            className={`flex-1 py-2 rounded-full text-center text-[13px] transition-all duration-200 flex items-center justify-center gap-1.5 ${
              billingPlan === 'annual'
                ? 'bg-[#111827] text-white font-bold shadow-xs'
                : 'text-[#45464c] font-medium'
            }`}
          >
            <span>Annual ($99/yr)</span>
            <span className="bg-[#4648d4] text-white text-[9px] uppercase font-bold px-1.5 py-0.5 rounded-full">
              Save 31%
            </span>
          </button>
        </div>
      </div>

      {/* Tier Comparison */}
      <div className="px-4 mb-4 flex flex-col gap-2">
        <div className="flex items-center justify-between px-1 pb-1">
          <span className="text-[13px] font-bold text-[#191c1e]">Tier Comparison</span>
          <div className="flex items-center gap-4 text-right">
            <span className="text-[11px] text-[#76777d] uppercase font-semibold w-12 text-center">
              Free
            </span>
            <span className="text-[11px] text-[#4648d4] uppercase font-bold w-14 text-center">
              Pro
            </span>
          </div>
        </div>

        {/* Row 1: Payments */}
        <div className="bg-[#f7f9fb] p-3 rounded-2xl border border-[#eceef0] flex flex-col gap-1">
          <div className="flex items-center justify-between">
            <div className="flex items-center gap-2">
              <div className="w-7 h-7 rounded-full bg-[#e1e0ff] flex items-center justify-center text-[#07006c]">
                <span className="material-symbols-outlined text-[16px]">receipt_long</span>
              </div>
              <span className="text-[13px] font-bold text-[#191c1e]">Active Payments</span>
            </div>
            <div className="flex items-center gap-4">
              <span className="text-[12px] text-[#76777d] w-12 text-center">Max 10</span>
              <span className="text-[12px] text-[#4648d4] font-bold w-14 text-center flex items-center justify-center gap-0.5">
                <span className="material-symbols-outlined text-[13px]">all_inclusive</span> Unltd
              </span>
            </div>
          </div>
          <p className="text-[#45464c] text-[11px] pl-9">
            Pro delivers zero caps on simultaneous client debts &amp; automated ledger syncing.
          </p>
        </div>

        {/* Row 2: Smart Sequences */}
        <div className="bg-[#f7f9fb] p-3 rounded-2xl border border-[#eceef0] flex flex-col gap-1">
          <div className="flex items-center justify-between">
            <div className="flex items-center gap-2">
              <div className="w-7 h-7 rounded-full bg-[#e1e0ff] flex items-center justify-center text-[#07006c]">
                <span className="material-symbols-outlined text-[16px]">schedule_send</span>
              </div>
              <span className="text-[13px] font-bold text-[#191c1e]">Smart Sequences</span>
            </div>
            <div className="flex items-center gap-4">
              <span className="text-[12px] text-[#76777d] w-12 text-center">Manual</span>
              <span className="text-[12px] text-[#4648d4] font-bold w-14 text-center flex items-center justify-center gap-0.5">
                <span className="material-symbols-outlined text-[13px]">bolt</span> Auto
              </span>
            </div>
          </div>
          <p className="text-[#45464c] text-[11px] pl-9">
            Multi-step cadence across Email &amp; SMS that pauses the instant debtor pays.
          </p>
        </div>

        {/* Row 3: WhatsApp Chaser */}
        <div className="bg-[#f7f9fb] p-3 rounded-2xl border border-[#eceef0] flex flex-col gap-1">
          <div className="flex items-center justify-between">
            <div className="flex items-center gap-2">
              <div className="w-7 h-7 rounded-full bg-[#e1e0ff] flex items-center justify-center text-[#07006c]">
                <span className="material-symbols-outlined text-[16px]">chat</span>
              </div>
              <span className="text-[13px] font-bold text-[#191c1e]">WhatsApp Chaser</span>
            </div>
            <div className="flex items-center gap-4">
              <span className="text-[12px] text-[#76777d] w-12 text-center">Link only</span>
              <span className="text-[12px] text-[#4648d4] font-bold w-14 text-center flex items-center justify-center gap-0.5">
                <span className="material-symbols-outlined text-[13px]">check_circle</span> 1-Tap
              </span>
            </div>
          </div>
          <p className="text-[#45464c] text-[11px] pl-9">
            Pre-formatted cordial-to-urgent chat templates linked straight to Apple Pay / Cards.
          </p>
        </div>

        {/* Row 4: Tone Customization */}
        <div className="bg-[#f7f9fb] p-3 rounded-2xl border border-[#eceef0] flex flex-col gap-1">
          <div className="flex items-center justify-between">
            <div className="flex items-center gap-2">
              <div className="w-7 h-7 rounded-full bg-[#e1e0ff] flex items-center justify-center text-[#07006c]">
                <span className="material-symbols-outlined text-[16px]">tune</span>
              </div>
              <span className="text-[13px] font-bold text-[#191c1e]">Tone &amp; Escalation</span>
            </div>
            <div className="flex items-center gap-4">
              <span className="text-[12px] text-[#76777d] w-12 text-center">Standard</span>
              <span className="text-[12px] text-[#4648d4] font-bold w-14 text-center flex items-center justify-center gap-0.5">
                <span className="material-symbols-outlined text-[13px]">military_tech</span> Custom
              </span>
            </div>
          </div>
          <p className="text-[#45464c] text-[11px] pl-9">
            From "Gentle Friendly Nudge" to "Formal Legal Hold" with automated tier shifts.
          </p>
        </div>
      </div>

      {/* Social Proof & Testimonial Card */}
      <div className="px-4 mb-4">
        <div className="bg-[#f2f4f6] p-4 rounded-2xl flex flex-col gap-2 border border-[#e6e8ea]">
          <div className="flex items-center justify-between">
            <div className="flex items-center gap-1 text-[#191c1e]">
              <div className="flex text-[#4648d4]">
                <span className="material-symbols-outlined text-[16px] [font-variation-settings:'FILL'_1]">
                  star
                </span>
                <span className="material-symbols-outlined text-[16px] [font-variation-settings:'FILL'_1]">
                  star
                </span>
                <span className="material-symbols-outlined text-[16px] [font-variation-settings:'FILL'_1]">
                  star
                </span>
                <span className="material-symbols-outlined text-[16px] [font-variation-settings:'FILL'_1]">
                  star
                </span>
                <span className="material-symbols-outlined text-[16px] [font-variation-settings:'FILL'_1]">
                  star
                </span>
              </div>
              <span className="text-[13px] font-bold ml-1">4.9/5</span>
            </div>
            <span className="text-[11px] text-[#45464c]">12,000+ happy pros</span>
          </div>

          <blockquote className="text-[13px] text-[#191c1e] italic leading-relaxed">
            “Payment Chaser collected $8,400 in overdue invoices in my first week. It completely
            removed the anxiety from asking for what I earned.”
          </blockquote>

          <div className="flex items-center gap-2 pt-1">
            <img
              className="w-8 h-8 rounded-full object-cover"
              src="https://lh3.googleusercontent.com/aida-public/AB6AXuA7Pj3Q3izUZFQU-dj3twsDXGhdmXl1OT1737b4tpKJvAa-2maiy00X4RMwKTiBPHPutS78RpsJks7blXz7glRcZf4aRvEu1xi9AoNieJo5y-YXwePkg0dFl6w3JmKnhnDIxh37dQOW6kfECPv1e0cYGKoj2Bgo9WO5a8kEqsgxNPyB90QPQU_iJoizNDbFrFIe7n4nOcS7HnDdh6DB8mmW1yY2OzLcbrvEEVDheuCmo9RJPpGKX__9"
              alt="Elena Rostova"
              referrerPolicy="no-referrer"
            />
            <div className="flex flex-col">
              <span className="text-[12px] font-bold text-[#191c1e]">Elena Rostova</span>
              <span className="text-[10px] text-[#76777d]">
                Independent Product Designer, Berlin
              </span>
            </div>
          </div>
        </div>
      </div>

      {/* Trust Badges */}
      <div className="px-4 mb-4 flex items-center justify-around text-center">
        <div className="flex flex-col items-center gap-0.5">
          <span className="material-symbols-outlined text-[18px] text-[#4648d4]">
            event_available
          </span>
          <span className="text-[11px] text-[#45464c] font-medium">14-Day Free</span>
        </div>
        <div className="w-px h-5 bg-[#e0e3e5]"></div>
        <div className="flex flex-col items-center gap-0.5">
          <span className="material-symbols-outlined text-[18px] text-[#4648d4]">
            credit_card_off
          </span>
          <span className="text-[11px] text-[#45464c] font-medium">Cancel Anytime</span>
        </div>
        <div className="w-px h-5 bg-[#e0e3e5]"></div>
        <div className="flex flex-col items-center gap-0.5">
          <span className="material-symbols-outlined text-[18px] text-[#4648d4]">security</span>
          <span className="text-[11px] text-[#45464c] font-medium">Google Protected</span>
        </div>
      </div>

      {/* Sticky Conversion Bottom Dock */}
      <div className="fixed bottom-0 left-0 right-0 bg-white/95 backdrop-blur-md px-4 pt-3 pb-8 shadow-[0_-4px_24px_rgba(0,0,0,0.06)] flex flex-col items-center max-w-md mx-auto z-40 border-t border-[#eceef0]">
        <button
          onClick={handleStartTrial}
          className="w-full h-13 rounded-full bg-[#111827] text-white text-[15px] font-bold flex items-center justify-center gap-2 active:scale-[0.98] transition-transform shadow-md"
        >
          <span className="material-symbols-outlined text-[20px]">rocket_launch</span>
          <span>Start 14-Day Free Trial</span>
        </button>

        <p className="text-[11px] text-[#76777d] text-center mt-2">
          {billingPlan === 'annual'
            ? 'Then $99/year ($8.25/mo). Cancel easily anytime in Google Play.'
            : 'Then $12/month. Billed monthly, cancel easily anytime in Google Play.'}
        </p>

        <div className="flex items-center justify-center gap-3 mt-1.5 text-[#76777d] text-[11px]">
          <button
            onClick={() => showToast('Entitlements restored from Google Play.')}
            className="hover:underline"
          >
            Restore Purchase
          </button>
          <span>•</span>
          <button onClick={() => showToast('Terms of Service: Standard Google Play EULA')} className="hover:underline">
            Terms of Service
          </button>
          <span>•</span>
          <button onClick={() => showToast('Privacy Policy: User data stored securely on client & private Firestore.')} className="hover:underline">
            Privacy Policy
          </button>
        </div>
      </div>
    </div>
  );
};
