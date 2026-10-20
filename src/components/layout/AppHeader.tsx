import React from 'react';
import { usePaymentChaser, ScreenTab } from '../../context/PaymentChaserContext';

interface AppHeaderProps {
  title?: string;
  showBack?: boolean;
}

export const AppHeader: React.FC<AppHeaderProps> = ({ title, showBack }) => {
  const { currentScreen, previousScreen, navigate, prefs, deviceViewMode, setDeviceViewMode } = usePaymentChaser();

  const getScreenTitle = (): string => {
    if (title) return title;
    switch (currentScreen) {
      case 'home':
        return 'Home Dashboard';
      case 'payments':
        return 'Payments';
      case 'add':
        return 'Add Payment';
      case 'clients':
        return 'Clients';
      case 'reminders':
        return 'Smart Reminders';
      case 'details':
        return 'Payment Details';
      case 'premium':
        return 'Premium';
      case 'settings':
        return 'Settings';
      default:
        return 'Payment Chaser';
    }
  };

  const handleBack = () => {
    if (previousScreen && previousScreen !== currentScreen) {
      navigate(previousScreen);
    } else {
      navigate('home');
    }
  };

  const shouldShowBack = showBack !== undefined ? showBack : currentScreen !== 'home';

  return (
    <header className="sticky top-0 w-full z-40 bg-[#f7f9fb]/90 backdrop-blur-xl border-b border-[#eceef0]/60 shadow-[0_1px_8px_rgba(0,0,0,0.02)]">
      {/* Android System Status Bar (Material 3 standard) */}
      <div className="h-6 px-4 flex items-center justify-between text-[#191c1e] text-[11px] font-semibold tracking-tight select-none pt-0.5">
        <span>9:41</span>
        <div className="flex items-center gap-1.5 opacity-80">
          <span className="material-symbols-outlined text-[14px]">signal_cellular_alt</span>
          <span className="material-symbols-outlined text-[14px]">wifi</span>
          <span className="material-symbols-outlined text-[16px]">battery_full</span>
        </div>
      </div>

      {/* Primary Top Bar */}
      <div className="h-14 px-3 flex items-center justify-between">
        <div className="flex items-center gap-1 min-w-0">
          {shouldShowBack ? (
            <button
              onClick={handleBack}
              aria-label="Go Back"
              className="w-10 h-10 flex items-center justify-center rounded-full text-[#191c1e] hover:bg-[#eceef0] active:scale-95 transition-all"
            >
              <span className="material-symbols-outlined text-[22px]">arrow_back</span>
            </button>
          ) : (
            <div className="w-9 h-9 rounded-xl bg-[#4648d4] text-white flex items-center justify-center font-bold text-sm shadow-sm mr-1.5 shrink-0">
              <span className="material-symbols-outlined text-[20px]">bolt</span>
            </div>
          )}

          <div className="flex flex-col min-w-0">
            {currentScreen === 'home' && (
              <span className="text-[10px] font-semibold uppercase tracking-wider text-[#76777d] leading-none mb-0.5">
                Payment Chaser
              </span>
            )}
            <h1 className="text-[17px] font-bold text-[#191c1e] leading-tight truncate">
              {getScreenTitle()}
            </h1>
          </div>
        </div>

        {/* Right side controls */}
        <div className="flex items-center gap-1">
          {/* Responsive / Mobile Viewport Preview Toggle */}
          <button
            onClick={() => setDeviceViewMode(deviceViewMode === 'mobile' ? 'responsive' : 'mobile')}
            title={`Switch to ${deviceViewMode === 'mobile' ? 'Responsive View' : 'Android Phone Frame'}`}
            className="hidden md:flex items-center gap-1 px-2.5 py-1 rounded-full text-[11px] font-medium bg-[#eceef0] text-[#45464c] hover:bg-[#e0e3e5] transition-colors"
          >
            <span className="material-symbols-outlined text-[14px]">
              {deviceViewMode === 'mobile' ? 'open_in_full' : 'phone_android'}
            </span>
            <span>{deviceViewMode === 'mobile' ? 'Expand' : 'Phone'}</span>
          </button>

          {/* Quick Reminders Hub Shortcut */}
          <button
            onClick={() => navigate('reminders')}
            aria-label="Smart Reminders"
            className="w-9 h-9 flex items-center justify-center rounded-full text-[#45464c] hover:text-[#191c1e] hover:bg-[#eceef0] transition-colors active:scale-95 relative"
          >
            <span className="material-symbols-outlined text-[20px]">notifications</span>
            <span className="absolute top-2 right-2 w-2 h-2 rounded-full bg-[#4648d4]"></span>
          </button>

          {/* User Profile Avatar */}
          <button
            onClick={() => navigate('settings')}
            aria-label="Settings & Profile"
            className="w-9 h-9 rounded-full ring-2 ring-transparent hover:ring-[#4648d4]/30 transition-all overflow-hidden ml-1 active:scale-95 shrink-0"
          >
            <img
              src={prefs.ownerAvatar}
              alt={prefs.ownerName}
              className="w-full h-full object-cover"
              referrerPolicy="no-referrer"
            />
          </button>
        </div>
      </div>
    </header>
  );
};
