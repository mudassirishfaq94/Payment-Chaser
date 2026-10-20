import React, { useState } from 'react';
import { usePaymentChaser } from '../context/PaymentChaserContext';
import { Client } from '../types';
import { formatCurrency } from '../utils/formatters';

export const ClientsScreen: React.FC = () => {
  const { clients, payments, addClient, updateClient, archiveClient, navigate, showToast } =
    usePaymentChaser();

  const [search, setSearch] = useState('');
  const [showAddModal, setShowAddModal] = useState(false);
  const [editingClient, setEditingClient] = useState<Client | null>(null);

  // Form states
  const [name, setName] = useState('');
  const [company, setCompany] = useState('');
  const [email, setEmail] = useState('');
  const [phone, setPhone] = useState('');
  const [notes, setNotes] = useState('');

  const activeClients = clients.filter((c) => !c.isArchived);
  const filteredClients = activeClients.filter((c) => {
    if (!search.trim()) return true;
    const q = search.toLowerCase();
    return (
      c.name.toLowerCase().includes(q) ||
      (c.company && c.company.toLowerCase().includes(q)) ||
      c.email.toLowerCase().includes(q)
    );
  });

  const handleOpenAdd = () => {
    setName('');
    setCompany('');
    setEmail('');
    setPhone('');
    setNotes('');
    setEditingClient(null);
    setShowAddModal(true);
  };

  const handleOpenEdit = (c: Client) => {
    setName(c.name);
    setCompany(c.company || '');
    setEmail(c.email);
    setPhone(c.phone);
    setNotes(c.notes || '');
    setEditingClient(c);
    setShowAddModal(true);
  };

  const handleSaveClient = (e: React.FormEvent) => {
    e.preventDefault();
    if (!name.trim()) return;

    if (editingClient) {
      updateClient({
        ...editingClient,
        name: name.trim(),
        company: company.trim() || undefined,
        email: email.trim(),
        phone: phone.trim(),
        notes: notes.trim() || undefined,
      });
    } else {
      addClient({
        name: name.trim(),
        company: company.trim() || undefined,
        email: email.trim(),
        phone: phone.trim(),
        initials: name.slice(0, 2).toUpperCase(),
        notes: notes.trim() || undefined,
      });
    }
    setShowAddModal(false);
  };

  const handleArchive = (client: Client) => {
    const res = archiveClient(client.id);
    if (!res.success && res.reason) {
      alert(res.reason);
    }
  };

  return (
    <div className="flex flex-col w-full pb-28 select-none">
      {/* Top action and search */}
      <div className="px-4 pt-3 pb-2 flex flex-col gap-2.5">
        <div className="flex items-center justify-between">
          <h2 className="text-[18px] font-bold text-[#191c1e]">Clients Counterparties</h2>
          <button
            onClick={handleOpenAdd}
            className="h-9 px-3.5 rounded-full bg-[#4648d4] text-white text-[12px] font-semibold flex items-center gap-1 shadow-xs active:scale-95 transition-transform"
          >
            <span className="material-symbols-outlined text-[16px]">add</span>
            <span>Add Client</span>
          </button>
        </div>

        {/* Search */}
        <div className="relative w-full flex items-center bg-white rounded-full px-3.5 h-11 shadow-xs border border-[#e6e8ea]">
          <span className="material-symbols-outlined text-[#76777d] text-[20px]">search</span>
          <input
            type="text"
            value={search}
            onChange={(e) => setSearch(e.target.value)}
            placeholder="Search by client name, email, or company..."
            className="w-full bg-transparent border-0 outline-none px-2 text-[13px] text-[#191c1e] placeholder:text-[#76777d]"
          />
          {search && (
            <button
              onClick={() => setSearch('')}
              className="text-[#76777d] hover:text-[#191c1e] material-symbols-outlined text-[16px]"
            >
              cancel
            </button>
          )}
        </div>
      </div>

      {/* Clients Cards List */}
      <div className="px-4 flex flex-col gap-3 pt-1">
        {filteredClients.map((client) => {
          // Calculate client debts and records dynamically from payments
          const clientPayments = payments.filter((p) => p.clientId === client.id);
          const activeInvoices = clientPayments.filter((p) => p.status !== 'paid');
          const totalOutstanding = activeInvoices.reduce(
            (sum, p) => sum + (p.amount - p.amountPaid),
            0
          );
          const hasOverdue = activeInvoices.some((p) => p.status === 'overdue');

          return (
            <div
              key={client.id}
              className="bg-white rounded-2xl p-4 shadow-xs border border-[#eceef0] flex flex-col gap-3"
            >
              <div className="flex items-start justify-between gap-3">
                <div className="flex items-center gap-3 min-w-0">
                  <div
                    className="w-11 h-11 rounded-xl text-white flex items-center justify-center font-bold text-[14px] shrink-0 shadow-xs"
                    style={{ backgroundColor: client.avatarColor || '#4648d4' }}
                  >
                    {client.initials}
                  </div>
                  <div className="flex flex-col min-w-0">
                    <span className="text-[15px] font-bold text-[#191c1e] truncate">
                      {client.name}
                    </span>
                    <span className="text-[12px] text-[#45464c] truncate">
                      {client.company || 'Direct Client'} • {client.phone}
                    </span>
                  </div>
                </div>

                <div className="text-right shrink-0">
                  <span className="text-[15px] font-bold text-[#191c1e] tabular-nums">
                    {formatCurrency(totalOutstanding)}
                  </span>
                  <span
                    className={`block text-[10px] font-semibold mt-0.5 ${
                      hasOverdue ? 'text-[#ba1a1a]' : 'text-[#76777d]'
                    }`}
                  >
                    {activeInvoices.length} active invoice(s)
                  </span>
                </div>
              </div>

              {/* Sub contact & quick action bar */}
              <div className="flex items-center justify-between pt-1 border-t border-[#f2f4f6] text-[12px]">
                <div className="flex items-center gap-2">
                  <a
                    href={`https://wa.me/${client.phone.replace(/[^0-9]/g, '')}`}
                    target="_blank"
                    rel="noreferrer"
                    className="flex items-center gap-1 text-[#009668] hover:underline font-semibold"
                  >
                    <span className="material-symbols-outlined text-[15px]">chat</span>
                    <span>WhatsApp</span>
                  </a>
                  <span className="text-[#c6c6cd]">•</span>
                  <a
                    href={`mailto:${client.email}`}
                    className="flex items-center gap-1 text-[#4648d4] hover:underline font-semibold"
                  >
                    <span className="material-symbols-outlined text-[15px]">mail</span>
                    <span>Email</span>
                  </a>
                </div>

                <div className="flex items-center gap-1">
                  <button
                    onClick={() => handleOpenEdit(client)}
                    className="w-8 h-8 rounded-full bg-[#f2f4f6] hover:bg-[#e6e8ea] flex items-center justify-center text-[#45464c]"
                    title="Edit Client"
                  >
                    <span className="material-symbols-outlined text-[16px]">edit</span>
                  </button>
                  <button
                    onClick={() => handleArchive(client)}
                    className="w-8 h-8 rounded-full bg-[#f2f4f6] hover:bg-[#ffdad6] hover:text-[#ba1a1a] flex items-center justify-center text-[#45464c]"
                    title="Archive Client"
                  >
                    <span className="material-symbols-outlined text-[16px]">archive</span>
                  </button>
                </div>
              </div>
            </div>
          );
        })}
      </div>

      {/* Add / Edit Client Modal */}
      {showAddModal && (
        <div className="fixed inset-0 z-50 flex items-center justify-center bg-black/40 backdrop-blur-xs p-4">
          <div className="w-full max-w-sm bg-white rounded-3xl p-5 shadow-2xl flex flex-col gap-3 max-h-[90vh] overflow-y-auto no-scrollbar">
            <div className="flex items-center justify-between">
              <h3 className="text-[16px] font-bold text-[#191c1e]">
                {editingClient ? 'Edit Client Counterparty' : 'Add New Client'}
              </h3>
              <button
                onClick={() => setShowAddModal(false)}
                className="w-8 h-8 rounded-full bg-[#eceef0] flex items-center justify-center text-[#45464c]"
              >
                <span className="material-symbols-outlined text-[18px]">close</span>
              </button>
            </div>

            <form onSubmit={handleSaveClient} className="flex flex-col gap-2.5">
              <div>
                <label className="text-[11px] font-medium text-[#76777d]">Client Name *</label>
                <input
                  type="text"
                  required
                  value={name}
                  onChange={(e) => setName(e.target.value)}
                  placeholder="e.g. Acme Studio Inc."
                  className="w-full h-10 px-3 rounded-xl bg-[#f2f4f6] text-[13px] outline-none border border-[#e6e8ea]"
                />
              </div>

              <div>
                <label className="text-[11px] font-medium text-[#76777d]">Company / Brand</label>
                <input
                  type="text"
                  value={company}
                  onChange={(e) => setCompany(e.target.value)}
                  placeholder="e.g. Acme Global"
                  className="w-full h-10 px-3 rounded-xl bg-[#f2f4f6] text-[13px] outline-none border border-[#e6e8ea]"
                />
              </div>

              <div>
                <label className="text-[11px] font-medium text-[#76777d]">Billing Email *</label>
                <input
                  type="email"
                  required
                  value={email}
                  onChange={(e) => setEmail(e.target.value)}
                  placeholder="contact@acmestudio.com"
                  className="w-full h-10 px-3 rounded-xl bg-[#f2f4f6] text-[13px] outline-none border border-[#e6e8ea]"
                />
              </div>

              <div>
                <label className="text-[11px] font-medium text-[#76777d]">Phone (for WhatsApp) *</label>
                <input
                  type="tel"
                  required
                  value={phone}
                  onChange={(e) => setPhone(e.target.value)}
                  placeholder="+1 (555) 019-2834"
                  className="w-full h-10 px-3 rounded-xl bg-[#f2f4f6] text-[13px] outline-none border border-[#e6e8ea]"
                />
              </div>

              <div>
                <label className="text-[11px] font-medium text-[#76777d]">Internal Notes</label>
                <textarea
                  rows={2}
                  value={notes}
                  onChange={(e) => setNotes(e.target.value)}
                  placeholder="Payment preferences, billing contact..."
                  className="w-full p-2 rounded-xl bg-[#f2f4f6] text-[13px] outline-none border border-[#e6e8ea] resize-none"
                />
              </div>

              <div className="flex gap-2 pt-2">
                <button
                  type="button"
                  onClick={() => setShowAddModal(false)}
                  className="flex-1 h-11 rounded-full bg-[#eceef0] text-[#191c1e] text-[13px] font-semibold"
                >
                  Cancel
                </button>
                <button
                  type="submit"
                  className="flex-1 h-11 rounded-full bg-[#4648d4] text-white text-[13px] font-semibold shadow-sm"
                >
                  Save Client
                </button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  );
};
