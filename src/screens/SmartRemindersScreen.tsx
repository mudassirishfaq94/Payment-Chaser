import React, { useState } from 'react';
import { usePaymentChaser } from '../context/PaymentChaserContext';
import { SmartSequenceStep } from '../types';

export const SmartRemindersScreen: React.FC = () => {
  const { steps, updateSequenceStep, prefs, updatePrefs, showToast } = usePaymentChaser();

  const [activeTone, setActiveTone] = useState<'friendly' | 'casual' | 'strict'>(prefs.activeTone);
  const [editingStep, setEditingStep] = useState<SmartSequenceStep | null>(null);
  const [editCopy, setEditCopy] = useState<string>('');

  const handleToggleMaster = () => {
    const nextState = !prefs.autopilotActive;
    updatePrefs({ autopilotActive: nextState });
    showToast(nextState ? 'Master Autopilot: Activated' : 'Master Autopilot: Paused');
  };

  const handleSelectTone = (tone: 'friendly' | 'casual' | 'strict') => {
    setActiveTone(tone);
    updatePrefs({ activeTone: tone });
    showToast(`Tone updated to: ${tone === 'friendly' ? 'Friendly & Professional' : tone === 'casual' ? 'Casual Freelancer' : 'Strict Corporate'}`);
  };

  const handleToggleStep = (step: SmartSequenceStep) => {
    updateSequenceStep({
      ...step,
      isActive: !step.isActive,
    });
  };

  const handleOpenEditCopy = (step: SmartSequenceStep) => {
    setEditingStep(step);
    setEditCopy(step.templateCopy);
  };

  const handleSaveStepCopy = () => {
    if (editingStep) {
      updateSequenceStep({
        ...editingStep,
        templateCopy: editCopy,
      });
      setEditingStep(null);
      showToast('Template copy updated.');
    }
  };

  const handleTestOnMyself = () => {
    showToast(`Test sequence sent to ${prefs.ownerEmail}!`);
  };

  return (
    <div className="flex flex-col w-full pb-32 select-none">
      {/* Master Automation Bar */}
      <div className="px-4 pt-3 pb-2">
        <div className="bg-white rounded-2xl p-4 shadow-xs border border-[#eceef0] flex items-center justify-between">
          <div className="flex items-center gap-3 min-w-0">
            <div className="w-10 h-10 rounded-full bg-[#e1e0ff] text-[#07006c] flex items-center justify-center shrink-0">
              <span className="material-symbols-outlined text-[20px] [font-variation-settings:'FILL'_1]">
                bolt
              </span>
            </div>
            <div className="flex flex-col min-w-0">
              <span className="text-[14px] font-bold text-[#191c1e] truncate">Master Autopilot</span>
              <span className="text-[11px] text-[#45464c] flex items-center gap-1">
                <span className="w-2 h-2 rounded-full bg-[#4648d4] inline-block animate-pulse"></span>
                Active for 14 clients
              </span>
            </div>
          </div>

          {/* Master Switch */}
          <button
            type="button"
            onClick={handleToggleMaster}
            aria-pressed={prefs.autopilotActive}
            className={`w-12 h-7 rounded-full p-1 transition-colors duration-200 relative flex items-center ${
              prefs.autopilotActive ? 'bg-[#4648d4] justify-end' : 'bg-[#d8dadc] justify-start'
            }`}
          >
            <span className="w-5 h-5 bg-white rounded-full shadow-xs flex items-center justify-center">
              {prefs.autopilotActive && (
                <span className="material-symbols-outlined text-[13px] text-[#4648d4]">check</span>
              )}
            </span>
          </button>
        </div>
      </div>

      {/* Warm Philosophy Banner */}
      <div className="px-4 mb-4">
        <div className="relative overflow-hidden rounded-3xl bg-[#141b2b] p-5 text-white shadow-md">
          <div className="absolute -right-6 -bottom-6 w-32 h-32 rounded-full bg-[#4648d4]/20 blur-xl pointer-events-none"></div>
          <div className="relative z-10 flex flex-col gap-1.5">
            <div className="inline-flex items-center gap-1.5 self-start px-2.5 py-1 rounded-full bg-white/10 text-[#e1e0ff] text-[11px] font-medium backdrop-blur-md">
              <span className="material-symbols-outlined text-[13px]">auto_awesome</span>
              Zero Friction Cadence
            </div>
            <h2 className="text-[20px] font-bold text-white mt-1 tracking-tight">
              Effortless, Polite &amp; Effective
            </h2>
            <p className="text-[12px] text-[#c0c6db] leading-relaxed">
              Payment Chaser automatically delivers calculated follow-ups so you never have to
              endure the awkwardness of manual chasing.
            </p>

            {/* Metric pill */}
            <div className="mt-2 pt-2 bg-white/5 rounded-xl px-3 py-2 flex items-center justify-between">
              <div className="flex items-center gap-1.5">
                <span className="material-symbols-outlined text-[#6ffbbe] text-[16px]">verified</span>
                <span className="text-[11px] text-white">Avg. resolution time</span>
              </div>
              <span className="text-[13px] font-bold text-[#6ffbbe]">2.4 Days Early</span>
            </div>
          </div>
        </div>
      </div>

      {/* Tone of Voice Section */}
      <div className="px-4 mb-4">
        <div className="flex items-center justify-between mb-2">
          <div className="flex items-center gap-1.5">
            <span className="material-symbols-outlined text-[#4648d4] text-[18px]">psychology</span>
            <h3 className="text-[14px] font-bold text-[#191c1e]">Communication Tone</h3>
          </div>
          <span className="text-[11px] text-[#4648d4] font-semibold">Adaptive AI</span>
        </div>

        {/* Tone Cards */}
        <div className="flex flex-col gap-2">
          {/* Option 1: Friendly & Professional */}
          <div
            onClick={() => handleSelectTone('friendly')}
            className={`cursor-pointer rounded-2xl p-3.5 shadow-xs transition-all border ${
              activeTone === 'friendly'
                ? 'bg-white border-[#4648d4] ring-1 ring-[#4648d4]'
                : 'bg-[#f2f4f6] border-transparent hover:bg-white'
            }`}
          >
            <div className="flex items-start justify-between gap-2.5">
              <div className="w-9 h-9 rounded-xl bg-[#e1e0ff] text-[#07006c] flex items-center justify-center shrink-0">
                <span className="material-symbols-outlined text-[19px]">sentiment_satisfied</span>
              </div>
              <div className="flex-1 min-w-0">
                <div className="flex items-center gap-2">
                  <span className="text-[13px] font-bold text-[#191c1e]">
                    Friendly &amp; Professional
                  </span>
                  <span className="px-2 py-0.5 rounded-full bg-[#e1e0ff] text-[#07006c] text-[10px] font-semibold">
                    Recommended
                  </span>
                </div>
                <p className="text-[11px] text-[#45464c] mt-0.5 leading-normal">
                  Firm yet polite. Keeps client relationships warm while driving prompt settlements.
                </p>
              </div>
              <div
                className={`w-5 h-5 rounded-full flex items-center justify-center shrink-0 ${
                  activeTone === 'friendly'
                    ? 'bg-[#4648d4] text-white'
                    : 'bg-[#eceef0] text-transparent'
                }`}
              >
                <span className="material-symbols-outlined text-[13px]">check</span>
              </div>
            </div>
          </div>

          {/* Option 2: Casual Freelancer */}
          <div
            onClick={() => handleSelectTone('casual')}
            className={`cursor-pointer rounded-2xl p-3.5 shadow-xs transition-all border ${
              activeTone === 'casual'
                ? 'bg-white border-[#4648d4] ring-1 ring-[#4648d4]'
                : 'bg-[#f2f4f6] border-transparent hover:bg-white'
            }`}
          >
            <div className="flex items-start justify-between gap-2.5">
              <div className="w-9 h-9 rounded-xl bg-[#e6e8ea] text-[#45464c] flex items-center justify-center shrink-0">
                <span className="material-symbols-outlined text-[19px]">coffee</span>
              </div>
              <div className="flex-1 min-w-0">
                <span className="text-[13px] font-bold text-[#191c1e]">Casual Freelancer</span>
                <p className="text-[11px] text-[#45464c] mt-0.5 leading-normal">
                  Warm, peer-to-peer conversational phrasing tailored for solo creators and studios.
                </p>
              </div>
              <div
                className={`w-5 h-5 rounded-full flex items-center justify-center shrink-0 ${
                  activeTone === 'casual'
                    ? 'bg-[#4648d4] text-white'
                    : 'bg-[#eceef0] text-transparent'
                }`}
              >
                <span className="material-symbols-outlined text-[13px]">check</span>
              </div>
            </div>
          </div>

          {/* Option 3: Strict Corporate */}
          <div
            onClick={() => handleSelectTone('strict')}
            className={`cursor-pointer rounded-2xl p-3.5 shadow-xs transition-all border ${
              activeTone === 'strict'
                ? 'bg-white border-[#4648d4] ring-1 ring-[#4648d4]'
                : 'bg-[#f2f4f6] border-transparent hover:bg-white'
            }`}
          >
            <div className="flex items-start justify-between gap-2.5">
              <div className="w-9 h-9 rounded-xl bg-[#e6e8ea] text-[#45464c] flex items-center justify-center shrink-0">
                <span className="material-symbols-outlined text-[19px]">gavel</span>
              </div>
              <div className="flex-1 min-w-0">
                <span className="text-[13px] font-bold text-[#191c1e]">Strict Corporate</span>
                <p className="text-[11px] text-[#45464c] mt-0.5 leading-normal">
                  Direct, formal, and structured. Emphasizes contractual milestones and late fee
                  terms.
                </p>
              </div>
              <div
                className={`w-5 h-5 rounded-full flex items-center justify-center shrink-0 ${
                  activeTone === 'strict'
                    ? 'bg-[#4648d4] text-white'
                    : 'bg-[#eceef0] text-transparent'
                }`}
              >
                <span className="material-symbols-outlined text-[13px]">check</span>
              </div>
            </div>
          </div>
        </div>
      </div>

      {/* Automation Sequence Stepper */}
      <div className="px-4 mb-4">
        <div className="flex items-center justify-between mb-2">
          <div className="flex items-center gap-1.5">
            <span className="material-symbols-outlined text-[#4648d4] text-[18px]">schedule</span>
            <h3 className="text-[14px] font-bold text-[#191c1e]">Sequence Timeline</h3>
          </div>
          <span className="text-[11px] text-[#45464c] bg-[#eceef0] px-2 py-0.5 rounded-full font-semibold">
            {steps.filter((s) => s.isActive).length}-Step Active
          </span>
        </div>

        {/* Stepper with connecting line */}
        <div className="relative flex flex-col gap-3">
          <div className="absolute left-4 top-5 bottom-5 w-0.5 bg-[#eceef0] pointer-events-none"></div>

          {steps.map((step) => (
            <div key={step.id} className="relative flex items-start gap-2.5">
              {/* Step Marker Dot */}
              <div
                className={`w-8 h-8 rounded-full flex items-center justify-center text-[11px] font-bold shrink-0 z-10 shadow-xs mt-1.5 ${
                  step.isActive ? 'bg-[#4648d4] text-white' : 'bg-[#e0e3e5] text-[#45464c]'
                }`}
              >
                {step.stepNumber}
              </div>

              {/* Step Card */}
              <div className="flex-1 min-w-0 bg-white rounded-2xl p-3.5 shadow-xs border border-[#eceef0]">
                <div className="flex items-center justify-between gap-1 mb-1">
                  <span className="text-[13px] font-bold text-[#191c1e]">{step.title}</span>
                  <span className="px-2 py-0.5 rounded-full bg-[#f2f4f6] text-[#45464c] text-[10px] font-semibold shrink-0">
                    {step.tag}
                  </span>
                </div>

                {/* Channels */}
                <div className="flex items-center gap-1.5 mb-2">
                  {step.channels.map((chan) => (
                    <span
                      key={chan}
                      className="inline-flex items-center gap-1 text-[10px] font-semibold text-[#4648d4] bg-[#e1e0ff]/60 px-2 py-0.5 rounded-md"
                    >
                      <span className="material-symbols-outlined text-[12px]">
                        {chan === 'Email' ? 'mail' : chan === 'WhatsApp' ? 'chat' : 'sms'}
                      </span>
                      <span>{chan}</span>
                    </span>
                  ))}
                </div>

                {/* Snippet preview */}
                <div className="bg-[#f2f4f6] rounded-xl p-2.5 text-[#45464c] text-[11px] italic mb-2 border border-[#e6e8ea]">
                  "{step.templateCopy}"
                </div>

                {/* Controls */}
                <div className="flex items-center justify-between pt-0.5">
                  <button
                    onClick={() => handleOpenEditCopy(step)}
                    className="text-[11px] text-[#4648d4] font-semibold flex items-center gap-1 hover:underline"
                  >
                    <span className="material-symbols-outlined text-[13px]">edit</span>
                    <span>Customize Copy</span>
                  </button>

                  <div className="flex items-center gap-1.5">
                    <span className="text-[11px] text-[#76777d]">
                      {step.isActive ? 'Active' : 'Off'}
                    </span>
                    <button
                      onClick={() => handleToggleStep(step)}
                      aria-pressed={step.isActive}
                      className={`w-9 h-5 rounded-full transition-colors relative flex items-center px-0.5 ${
                        step.isActive ? 'bg-[#4648d4]' : 'bg-[#d8dadc]'
                      }`}
                    >
                      <div
                        className={`w-4 h-4 rounded-full bg-white shadow-xs transform transition-transform ${
                          step.isActive ? 'translate-x-4' : 'translate-x-0'
                        }`}
                      />
                    </button>
                  </div>
                </div>
              </div>
            </div>
          ))}
        </div>
      </div>

      {/* Delivery Channels Hub */}
      <div className="px-4 mb-4">
        <div className="flex items-center justify-between mb-2">
          <div className="flex items-center gap-1.5">
            <span className="material-symbols-outlined text-[#4648d4] text-[18px]">hub</span>
            <h3 className="text-[14px] font-bold text-[#191c1e]">Delivery Channels</h3>
          </div>
          <span className="text-[11px] text-[#009668] font-semibold">Ready to Fire</span>
        </div>

        <div className="bg-white rounded-2xl p-4 shadow-xs border border-[#eceef0] flex flex-col gap-3">
          <div className="flex items-center justify-between">
            <div className="flex items-center gap-2.5 min-w-0">
              <div className="w-9 h-9 rounded-xl bg-[#e1e0ff] text-[#4648d4] flex items-center justify-center shrink-0">
                <span className="material-symbols-outlined text-[18px]">forward_to_inbox</span>
              </div>
              <div className="flex flex-col min-w-0">
                <span className="text-[13px] font-bold text-[#191c1e] truncate">
                  Automated Email Engine
                </span>
                <span className="text-[11px] text-[#45464c]">
                  via Payment Chaser Cloud (High Deliverability)
                </span>
              </div>
            </div>
            <span className="px-2.5 py-0.5 rounded-full bg-[#e1e0ff] text-[#07006c] text-[11px] font-semibold shrink-0">
              Active
            </span>
          </div>

          <div className="h-px bg-[#eceef0] w-full" />

          <div className="flex items-center justify-between">
            <div className="flex items-center gap-2.5 min-w-0">
              <div className="w-9 h-9 rounded-xl bg-[#f2f4f6] text-[#191c1e] flex items-center justify-center shrink-0">
                <span className="material-symbols-outlined text-[18px]">chat_bubble</span>
              </div>
              <div className="flex flex-col min-w-0">
                <span className="text-[13px] font-bold text-[#191c1e] truncate">
                  WhatsApp 1-Tap Share
                </span>
                <span className="text-[11px] text-[#45464c]">
                  Opens pre-formatted chat with link
                </span>
              </div>
            </div>
            <span className="px-2.5 py-0.5 rounded-full bg-[#f2f4f6] text-[#191c1e] text-[11px] font-semibold shrink-0">
              Ready
            </span>
          </div>

          {/* Pro Add-on Note */}
          <div className="bg-[#f2f4f6] rounded-xl p-3 flex items-start gap-2 border border-[#e6e8ea]">
            <span className="material-symbols-outlined text-[#4648d4] text-[16px] shrink-0 mt-0.5">
              lock_open
            </span>
            <div className="flex-1 min-w-0 text-[11px]">
              <p className="text-[#45464c] leading-relaxed">
                <strong className="text-[#191c1e]">Pro Feature:</strong> Fully autonomous
                background WhatsApp dispatching via official Meta Cloud API integration.
              </p>
            </div>
          </div>
        </div>
      </div>

      {/* Stop Rule Guarantee */}
      <div className="px-4 mb-4">
        <div className="bg-[#f2f4f6] rounded-2xl p-3 flex items-center gap-2.5 border border-[#e6e8ea]">
          <div className="w-8 h-8 rounded-full bg-[#eceef0] flex items-center justify-center shrink-0 text-[#191c1e]">
            <span className="material-symbols-outlined text-[17px]">security</span>
          </div>
          <p className="text-[11px] text-[#45464c] flex-1">
            <strong>Stop Rule Active:</strong> Follow-ups pause automatically the exact second an
            invoice is marked settled.
          </p>
        </div>
      </div>

      {/* Sticky Bottom Actions */}
      <div className="fixed bottom-0 left-0 right-0 p-4 bg-white/95 backdrop-blur-md z-30 flex items-center gap-2 shadow-[0_-4px_16px_rgba(0,0,0,0.06)] max-w-md mx-auto">
        <button
          onClick={handleTestOnMyself}
          className="flex-1 h-12 rounded-full bg-[#eceef0] text-[#191c1e] text-[13px] font-semibold flex items-center justify-center gap-1.5 active:scale-95 transition-transform"
        >
          <span className="material-symbols-outlined text-[17px]">send</span>
          <span>Test on Myself</span>
        </button>

        <button
          onClick={() => showToast('Smart sequence cadence saved!')}
          className="flex-1 h-12 rounded-full bg-[#4648d4] text-white text-[13px] font-semibold flex items-center justify-center gap-1.5 shadow-md active:scale-95 transition-transform"
        >
          <span className="material-symbols-outlined text-[17px]">done_all</span>
          <span>Save Sequence</span>
        </button>
      </div>

      {/* Customize Copy Modal */}
      {editingStep && (
        <div className="fixed inset-0 z-50 flex items-center justify-center bg-black/40 backdrop-blur-xs p-4">
          <div className="w-full max-w-sm bg-white rounded-3xl p-5 shadow-2xl flex flex-col gap-3">
            <h3 className="text-[15px] font-bold text-[#191c1e]">
              Customize: {editingStep.title}
            </h3>
            <p className="text-[11px] text-[#76777d]">
              Available placeholders: <code>[Client]</code>, <code>[Invoice]</code>,{' '}
              <code>[Amount]</code>, <code>[DueDate]</code>.
            </p>
            <textarea
              rows={4}
              value={editCopy}
              onChange={(e) => setEditCopy(e.target.value)}
              className="w-full p-3 rounded-xl bg-[#f2f4f6] text-[13px] text-[#191c1e] border border-[#e6e8ea] outline-none resize-none"
            />
            <div className="flex gap-2 pt-1">
              <button
                type="button"
                onClick={() => setEditingStep(null)}
                className="flex-1 h-10 rounded-full bg-[#eceef0] text-[#191c1e] text-[12px] font-semibold"
              >
                Cancel
              </button>
              <button
                type="button"
                onClick={handleSaveStepCopy}
                className="flex-1 h-10 rounded-full bg-[#4648d4] text-white text-[12px] font-semibold shadow-sm"
              >
                Save Copy
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};
