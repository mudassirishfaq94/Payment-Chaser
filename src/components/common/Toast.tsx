import React from 'react';
import { usePaymentChaser } from '../../context/PaymentChaserContext';

export const Toast: React.FC = () => {
  const { toast } = usePaymentChaser();

  if (!toast) return null;

  return (
    <div className="fixed bottom-24 left-1/2 transform -translate-x-1/2 z-50 bg-[#111827] text-white px-4 py-2.5 rounded-full text-[12px] font-semibold shadow-2xl flex items-center gap-2 animate-in fade-in slide-in-from-bottom-2 duration-200 border border-white/10 max-w-xs text-center pointer-events-none">
      <span className="material-symbols-outlined text-[17px] text-[#6ffbbe]">check_circle</span>
      <span>{toast}</span>
    </div>
  );
};
