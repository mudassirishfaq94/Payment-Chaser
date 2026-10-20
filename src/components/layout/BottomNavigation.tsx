import React from 'react';
import { usePaymentChaser, ScreenTab } from '../../context/PaymentChaserContext';

export const BottomNavigation: React.FC = () => {
  const { currentScreen, navigate } = usePaymentChaser();

  // Hide bottom navigation on modal-like full screens like 'add', 'premium', or 'details' if desired,
  // or keep it consistent with the Stitch mobile tab shell:
  const isTabBarVisible = !['premium'].includes(currentScreen);

  if (!isTabBarVisible) return null;

  const tabs: { key: ScreenTab; label: string; icon: string }[] = [
    { key: 'home', label: 'Home', icon: 'home' },
    { key: 'payments', label: 'Payments', icon: 'receipt_long' },
    { key: 'clients', label: 'Clients', icon: 'group' },
    { key: 'settings', label: 'Settings', icon: 'tune' },
  ];

  return (
    <nav className="fixed bottom-0 left-0 right-0 z-40 bg-[#f7f9fb]/95 backdrop-blur-xl border-t border-[#e6e8ea]/80 shadow-[0_-4px_16px_rgba(15,23,42,0.04)] select-none">
      <div className="max-w-md mx-auto h-16 px-3 flex items-center justify-around relative">
        {/* Tab 1: Home */}
        <button
          onClick={() => navigate('home')}
          className={`flex flex-col items-center justify-center min-w-[56px] h-12 transition-colors ${
            currentScreen === 'home' ? 'text-[#4648d4]' : 'text-[#76777d] hover:text-[#191c1e]'
          }`}
        >
          <span
            className={`material-symbols-outlined text-[23px] ${
              currentScreen === 'home' ? "font-bold [font-variation-settings:'FILL'_1]" : ''
            }`}
          >
            home
          </span>
          <span className="text-[11px] font-medium mt-0.5">Home</span>
        </button>

        {/* Tab 2: Payments */}
        <button
          onClick={() => navigate('payments')}
          className={`flex flex-col items-center justify-center min-w-[56px] h-12 transition-colors ${
            currentScreen === 'payments' ? 'text-[#4648d4]' : 'text-[#76777d] hover:text-[#191c1e]'
          }`}
        >
          <span
            className={`material-symbols-outlined text-[23px] ${
              currentScreen === 'payments' ? "font-bold [font-variation-settings:'FILL'_1]" : ''
            }`}
          >
            receipt_long
          </span>
          <span className="text-[11px] font-medium mt-0.5">Payments</span>
        </button>

        {/* Center Floating Action Button (+ Add Payment) */}
        <div className="relative flex flex-col items-center justify-center min-w-[56px]">
          <button
            onClick={() => navigate('add')}
            aria-label="Add Payment"
            className="absolute -top-6 w-13 h-13 rounded-full bg-[#4648d4] text-white shadow-[0_8px_20px_rgba(70,72,212,0.38)] hover:bg-[#3b3dbb] active:scale-95 transition-all flex items-center justify-center focus:outline-none focus:ring-4 focus:ring-[#4648d4]/30"
          >
            <span className="material-symbols-outlined text-[28px]">add</span>
          </button>
          <span className="text-[11px] font-medium mt-7 text-[#76777d]">Add</span>
        </div>

        {/* Tab 4: Clients */}
        <button
          onClick={() => navigate('clients')}
          className={`flex flex-col items-center justify-center min-w-[56px] h-12 transition-colors ${
            currentScreen === 'clients' ? 'text-[#4648d4]' : 'text-[#76777d] hover:text-[#191c1e]'
          }`}
        >
          <span
            className={`material-symbols-outlined text-[23px] ${
              currentScreen === 'clients' ? "font-bold [font-variation-settings:'FILL'_1]" : ''
            }`}
          >
            group
          </span>
          <span className="text-[11px] font-medium mt-0.5">Clients</span>
        </button>

        {/* Tab 5: Settings */}
        <button
          onClick={() => navigate('settings')}
          className={`flex flex-col items-center justify-center min-w-[56px] h-12 transition-colors ${
            currentScreen === 'settings' ? 'text-[#4648d4]' : 'text-[#76777d] hover:text-[#191c1e]'
          }`}
        >
          <span
            className={`material-symbols-outlined text-[23px] ${
              currentScreen === 'settings' ? "font-bold [font-variation-settings:'FILL'_1]" : ''
            }`}
          >
            tune
          </span>
          <span className="text-[11px] font-medium mt-0.5">Settings</span>
        </button>
      </div>

      {/* Android Gesture Bar Inset */}
      <div className="h-3 flex items-center justify-center pb-safe">
        <div className="w-24 h-1 rounded-full bg-[#191c1e]/20"></div>
      </div>
    </nav>
  );
};
