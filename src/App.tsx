import React from 'react';
import { PaymentChaserProvider, usePaymentChaser } from './context/PaymentChaserContext';
import { AppHeader } from './components/layout/AppHeader';
import { BottomNavigation } from './components/layout/BottomNavigation';
import { QuickChaseModal } from './components/chase/QuickChaseModal';
import { Toast } from './components/common/Toast';
import { HomeScreen } from './screens/HomeScreen';
import { PaymentsScreen } from './screens/PaymentsScreen';
import { AddPaymentScreen } from './screens/AddPaymentScreen';
import { PaymentDetailsScreen } from './screens/PaymentDetailsScreen';
import { SmartRemindersScreen } from './screens/SmartRemindersScreen';
import { ClientsScreen } from './screens/ClientsScreen';
import { PremiumScreen } from './screens/PremiumScreen';
import { SettingsScreen } from './screens/SettingsScreen';

const MainContent: React.FC = () => {
  const { currentScreen, deviceViewMode } = usePaymentChaser();

  const renderScreen = () => {
    switch (currentScreen) {
      case 'home':
        return <HomeScreen />;
      case 'payments':
        return <PaymentsScreen />;
      case 'add':
        return <AddPaymentScreen />;
      case 'details':
        return <PaymentDetailsScreen />;
      case 'reminders':
        return <SmartRemindersScreen />;
      case 'clients':
        return <ClientsScreen />;
      case 'premium':
        return <PremiumScreen />;
      case 'settings':
        return <SettingsScreen />;
      default:
        return <HomeScreen />;
    }
  };

  return (
    <div
      className={`min-h-screen bg-[#f7f9fb] text-[#191c1e] flex flex-col justify-start items-center ${
        deviceViewMode === 'mobile' ? 'py-4 md:py-8 bg-[#e6e8ea]' : ''
      }`}
    >
      <div
        className={`w-full bg-[#f7f9fb] flex flex-col relative transition-all duration-300 ${
          deviceViewMode === 'mobile'
            ? 'max-w-[420px] min-h-[880px] rounded-[44px] shadow-2xl border-[8px] border-[#141b2b] overflow-hidden my-auto'
            : 'max-w-md min-h-screen shadow-md'
        }`}
      >
        {/* Android Punch Hole Camera when in Phone Frame */}
        {deviceViewMode === 'mobile' && (
          <div className="absolute top-2 left-1/2 -translate-x-1/2 w-4 h-4 rounded-full bg-black z-50 pointer-events-none"></div>
        )}

        <AppHeader />

        <main className="flex-1 w-full flex flex-col relative">{renderScreen()}</main>

        <BottomNavigation />
        <QuickChaseModal />
        <Toast />
      </div>
    </div>
  );
};

export default function App() {
  return (
    <PaymentChaserProvider>
      <MainContent />
    </PaymentChaserProvider>
  );
}
